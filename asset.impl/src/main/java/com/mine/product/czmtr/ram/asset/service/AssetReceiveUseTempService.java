package com.mine.product.czmtr.ram.asset.service;

import com.mine.base.dict.dto.DictDto;
import com.mine.base.dict.service.IDictService;
import com.mine.base.user.dto.DeptInfoDto;
import com.mine.base.user.dto.UserInfoDto;
import com.mine.base.user.service.IUserService;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dao.AssetAssetDao;
import com.mine.product.czmtr.ram.asset.dao.AssetReceiveRevertDao;
import com.mine.product.czmtr.ram.asset.dao.AssetReceiveUseDao;
import com.mine.product.czmtr.ram.asset.dao.AssetReceiveUseTempDao;
import com.mine.product.czmtr.ram.asset.dto.*;
import com.mine.product.czmtr.ram.asset.model.AssetAssetModel;
import com.mine.product.czmtr.ram.asset.model.AssetReceiveRevertModel;
import com.mine.product.czmtr.ram.asset.model.AssetReceiveUseModel;
import com.mine.product.czmtr.ram.asset.model.AssetReceiveUseTempModel;
import com.mine.product.czmtr.ram.asset.service.IAssetService.ASSET_STATUS;
import com.mine.product.czmtr.ram.asset.service.IAssetService.CHANGE_TYPE;
import com.mine.product.czmtr.ram.asset.service.IAssetService.HISTORY_TYPE;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.VGUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AssetReceiveUseTempService implements IAssetReceiveUseTempService {

    @Autowired
    private AssetReceiveUseDao assetReceiveUseDao;
    @Autowired
    private AssetReceiveUseTempDao assetReceiveUseTempDao;
    @Autowired
    private AssetReceiveRevertDao assetReceiveRevertDao;
    @Autowired
    private AssetAssetDao assetDao; // 资产
    @Autowired
    private IAssetService assetService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IDictService dictService;
    @Autowired
    private IBaseService baseService;
    @Autowired
    private IAssetReceiveUseService assetReceiveUseService;
    @Autowired
    private IAssetReceiveRevertService assetReceiveRevertService;

    @Override
    public List<String> getAssetIdList(String id) {
        List<AssetReceiveUseTempDto> dtolist = this.getAssetReceiveUseTempDtoList(id);
        if (dtolist.size() <= 0) {
            throw new RuntimeException("资产列表不能为空！");
        }
        return dtolist.stream().map(e -> e.getAssetId()).collect(Collectors.toList());
    }

    @Override
    public Map<String, String> getAssetMap(String id) {
        List<AssetReceiveUseTempDto> dtolist = this.getAssetReceiveUseTempDtoList(id);
        if (dtolist.size() <= 0) {
            throw new RuntimeException("资产列表不能为空！");
        }
        Map<String, String> map = new HashMap<>();
        for (AssetReceiveUseTempDto dto : dtolist) {
            map.put(dto.getAssetId(), dto.getSavePlaceId());
        }
        return map;
    }

    @Override
    public List<String> getAssetIdListByReceiveRevertId(String id) {
        List<AssetReceiveUseTempDto> dtolist = this.getAssetReceiveUseTempDtoListByRevertId(id);
        if (dtolist.size() <= 0) {
            throw new RuntimeException("资产列表不能为空！");
        }
        return dtolist.stream().map(e -> e.getAssetId()).collect(Collectors.toList());
    }



    // 通用检查
    public void commonCheckAssetReceiveUseTempDto(AssetReceiveUseTempDto dto) {
        AssetAssetModel assetModel = assetDao.findById(dto.getAssetId()).get();
        if (VGUtility.isEmpty(assetModel))
            throw new RuntimeException(dto.getAssetChsName() + "数据已被删除！");

        if (VGUtility.isEmpty(dto.getAssetReceiveUseID()) && VGUtility.isEmpty(dto.getAssetReceiveRevertID()))
            throw new RuntimeException("领用单Id或归还单Id，不能为空！");
        if (VGUtility.isEmpty(dto.getSavePlaceId()))
            throw new RuntimeException("使用位置ID，不能为空！");
        if (VGUtility.isEmpty(dto.getOldUserID()))
            throw new RuntimeException("原使用人ID，不能为空！");
        if (VGUtility.isEmpty(dto.getNewUserID()))
            throw new RuntimeException("现使用人ID，不能为空！");
    }

    /***
     * 创建资产领用单子表
     */
    @Override
    public void createAssetReceiveUseTempForList(List<AssetReceiveUseTempDto> dtolist, String assetReceiveUseModelId) {
        AssetReceiveUseModel model = assetReceiveUseDao.findById(assetReceiveUseModelId).get();
        if (VGUtility.isEmpty(model))
            throw new RuntimeException("领用信息不能为空，不能为空！");
//        DeptInfoDto receiveUseDepartment = new DeptInfoDto();
//        UserInfoDto receiveUseUser = new UserInfoDto();
//        if (!VGUtility.isEmpty(model.getAssetReceiveUseDepartmentID())) {
//            receiveUseDepartment = (DeptInfoDto) baseService.getDeptInfo(model.getAssetReceiveUseDepartmentID());
//        }
//        if (!VGUtility.isEmpty(model.getAssetReceiveUseUserID())) {
//            receiveUseUser = userService.getUserInfo(model.getAssetReceiveUseUserID());
//        }
        for (AssetReceiveUseTempDto temp : dtolist) {
            AssetReceiveUseTempModel tempModel = new AssetReceiveUseTempModel();
            tempModel.setAssetReceiveUseModel(model);
            tempModel.setSavePlaceId(temp.getSavePlaceId());
            tempModel.setAssetId(temp.getAssetId());
            tempModel.setOldUserID(model.getCreateUserID());
            tempModel.setNewUserID(model.getAssetReceiveUseUserID());
//            AssetAssetDto asset = assetService.getAssetByAssetId(temp.getAssetId());
//            Boolean flag = assetService.getAssetStatus(ASSET_STATUS.values()[VGUtility.toInteger(asset.getAssetStatusStr())], ASSET_STATUS.使用);
//            if (!flag)
//                throw new RuntimeException(asset.getCombinationAssetName() + "当前资产不可以领用 ");

//			assetService.updateAssetBeforeAssetStatus(asset);

//            AssetHistoryDto historyDto = new AssetHistoryDto();
//            historyDto.setHistoryType(HISTORY_TYPE.变更记录);
//            historyDto.setChangeType(String.valueOf(CHANGE_TYPE.资产领用.ordinal()));
//            historyDto.setAssetModelId(tempModel.getAssetId());
//            historyDto.setCreateUserId(model.getCreateUserID());
//            historyDto.setChangeContent("操作[创建资产领用单]"
//                    + "[" + receiveUseDepartment.getDeptName() + "]"
//                    + receiveUseUser.getChsName() + "领用");
//            assetService.createHistory(historyDto);

            assetReceiveUseTempDao.save(tempModel);
        }


    }

    // 根据资产ID,插入资产使用子表
    @Override
    public void createAssetReceiveUseTempByassetIdListStr(String assetIdListStr, AssetReceiveUseDto dto) {
        List<String> assetIdList = new ArrayList<String>();
        if (!VGUtility.isEmpty(assetIdListStr))
            Arrays.stream(assetIdListStr.split(",")).forEach(arr -> assetIdList.add(arr));

        AssetReceiveUseModel assetReceiveUseModel = assetReceiveUseDao.findById(dto.getId()).get();
        if (VGUtility.isEmpty(assetReceiveUseModel))
            throw new RuntimeException("该领用单数据已被删除！");

//        DeptInfoDto receiveUseDepartment = new DeptInfoDto();
//        UserInfoDto receiveUseUser = new UserInfoDto();
//        if (!VGUtility.isEmpty(assetReceiveUseModel.getAssetReceiveUseDepartmentID())) {
//            receiveUseDepartment = (DeptInfoDto) baseService.getDeptInfo(assetReceiveUseModel.getAssetReceiveUseDepartmentID());
//        }
//        if (!VGUtility.isEmpty(assetReceiveUseModel.getAssetReceiveUseUserID())) {
//            receiveUseUser = userService.getUserInfo(assetReceiveUseModel.getAssetReceiveUseUserID());
//        }

        for (String assetId : assetIdList) {
            AssetAssetDto asset = assetService.getAssetByAssetId(assetId);
            if (VGUtility.isEmpty(asset))
                throw new RuntimeException("该资产数据已被删除！");

//            Boolean flag = assetService.getAssetStatus(ASSET_STATUS.values()[VGUtility.toInteger(asset.getAssetStatusStr())], ASSET_STATUS.使用);
//            if (!flag)
//                throw new RuntimeException(asset.getCombinationAssetName() + "当前资产不可以借出 ");
//
//            asset.setBeforeChangeAssetStatusStr(String.valueOf(ASSET_STATUS.闲置.ordinal()));
//            assetService.updateAssetBeforeAssetStatus(asset);

            AssetReceiveUseTempModel tempModel = new AssetReceiveUseTempModel();
            tempModel.setAssetReceiveUseModel(assetReceiveUseModel);
            tempModel.setSavePlaceId(asset.getSavePlaceId());
            tempModel.setOldUserID(asset.getManagerId());
            tempModel.setNewUserID(assetReceiveUseModel.getAssetReceiveUseUserID());
            tempModel.setAssetId(asset.getId());

//            AssetHistoryDto historyDto = new AssetHistoryDto();
//            historyDto.setHistoryType(HISTORY_TYPE.变更记录);
//            historyDto.setChangeType(String.valueOf(CHANGE_TYPE.资产领用.ordinal()));
//            historyDto.setAssetModelId(tempModel.getAssetId());
//            historyDto.setCreateUserId(assetReceiveUseModel.getCreateUserID());
//            historyDto.setChangeContent("操作[创建资产领用单]"
//                    + "[" + receiveUseDepartment.getDeptName() + "]"
//                    + receiveUseUser.getChsName() + "领用");
//            assetService.createHistory(historyDto);
            assetReceiveUseTempDao.save(tempModel);
        }
    }

    // 根据资产ID,插入资产归还子表
    @Override
    public void createAssetReceiveUseTempByAssetIdListStrForRevert(String AssetReceiveUseTempIdListStr, AssetReceiveRevertDto dto) {
        List<String> AssetReceiveUseTempIdList = new ArrayList<String>();
        if (!VGUtility.isEmpty(AssetReceiveUseTempIdListStr))
            Arrays.stream(AssetReceiveUseTempIdListStr.split(",")).forEach(arr -> AssetReceiveUseTempIdList.add(arr));

        AssetReceiveRevertModel assetReceiveRevertModel = assetReceiveRevertDao.findById(dto.getId()).get();
        if (VGUtility.isEmpty(assetReceiveRevertModel))
            throw new RuntimeException("该归还单数据已被删除！");
//
//        DeptInfoDto receiveRevertDepartment = new DeptInfoDto();
//        UserInfoDto receiveRevertUser = new UserInfoDto();
//        if (!VGUtility.isEmpty(assetReceiveRevertModel.getAssetReceiveRevertDepartmentID())) {
//            receiveRevertDepartment = (DeptInfoDto) baseService.getDeptInfo(assetReceiveRevertModel.getAssetReceiveRevertDepartmentID());
//        }
//        if (!VGUtility.isEmpty(assetReceiveRevertModel.getAssetReceiveRevertUserID())) {
//            receiveRevertUser = userService.getUserInfo(assetReceiveRevertModel.getAssetReceiveRevertUserID());
//        }
        for (String AssetReceiveUseTempId : AssetReceiveUseTempIdList) {
            AssetReceiveUseTempModel tempModel = assetReceiveUseTempDao.findById(AssetReceiveUseTempId).get();
            AssetAssetDto asset = assetService.getAssetByAssetId(tempModel.getAssetId());
            if (VGUtility.isEmpty(asset))
                throw new RuntimeException("该资产数据已被删除！");
//
//            Boolean flag = assetService.getAssetStatus(ASSET_STATUS.values()[VGUtility.toInteger(asset.getAssetStatusStr())], ASSET_STATUS.闲置);
//            if (!flag)
//                throw new RuntimeException(asset.getCombinationAssetName() + "当前资产不可以归还 ");
//
//            asset.setBeforeChangeAssetStatusStr(String.valueOf(ASSET_STATUS.使用.ordinal()));
//            assetService.updateAssetBeforeAssetStatus(asset);

            tempModel.setAssetReceiveRevertModel(assetReceiveRevertModel);
            tempModel.setSavePlaceId(asset.getSavePlaceId());
            tempModel.setOldUserID(assetReceiveRevertModel.getAssetReceiveRevertUserID());
            tempModel.setNewUserID(assetReceiveRevertModel.getCreateUserID());
            tempModel.setAssetId(asset.getId());

//            AssetHistoryDto historyDto = new AssetHistoryDto();
//            historyDto.setHistoryType(HISTORY_TYPE.变更记录);
//            historyDto.setChangeType(String.valueOf(CHANGE_TYPE.资产领用归还.ordinal()));
//            historyDto.setAssetModelId(tempModel.getAssetId());
//            historyDto.setCreateUserId(assetReceiveRevertModel.getCreateUserID());
//            historyDto.setChangeContent("操作[创建资产领用归还单]"
//                    + "[" + receiveRevertDepartment.getDeptName() + "]"
//                    + receiveRevertUser.getChsName() + "归还");
//            assetService.createHistory(historyDto);

            assetReceiveUseTempDao.save(tempModel);
        }
    }

    ;

    /*
     * 创建资产归还子表
     */
    @Override
    public void createAssetReceiveRevertTempForList(List<AssetReceiveUseTempDto> dtolist, String assetReceiveRevertModelId) {
        AssetReceiveRevertModel model = assetReceiveRevertDao.getOne(assetReceiveRevertModelId);
        if (VGUtility.isEmpty(model))
            throw new RuntimeException("归还信息不能为空，不能为空！");

//        DeptInfoDto receiveRevertDepartment = new DeptInfoDto();
//        UserInfoDto receiveRevertUser = new UserInfoDto();
//        if (!VGUtility.isEmpty(model.getAssetReceiveRevertDepartmentID())) {
//            receiveRevertDepartment = (DeptInfoDto) baseService.getDeptInfo(model.getAssetReceiveRevertDepartmentID());
//        }
//        if (!VGUtility.isEmpty(model.getAssetReceiveRevertUserID())) {
//            receiveRevertUser = userService.getUserInfo(model.getAssetReceiveRevertUserID());
//        }
        for (AssetReceiveUseTempDto temp : dtolist) {
            AssetReceiveUseTempModel tempModel = assetReceiveUseTempDao.getOne(temp.getId());
//            AssetAssetDto asset = assetService.getAssetByAssetId(temp.getAssetId());
//            if (VGUtility.isEmpty(asset))
//                throw new RuntimeException("该资产数据已被删除！");
//
//            Boolean flag = assetService.getAssetStatus(ASSET_STATUS.values()[VGUtility.toInteger(asset.getAssetStatusStr())], ASSET_STATUS.闲置);
//            if (!flag)
//                throw new RuntimeException(asset.getCombinationAssetName() + "当前资产不可以归还 ");

//            asset.setBeforeChangeAssetStatusStr(String.valueOf(ASSET_STATUS.使用.ordinal()));
//            assetService.updateAssetBeforeAssetStatus(asset);

            tempModel.setId(temp.getId());
            tempModel.setAssetReceiveRevertModel(model);
            tempModel.setRevertSavePlaceId(temp.getRevertSavePlaceId());
            tempModel.setAssetId(temp.getAssetId());
            tempModel.setOldUserID(model.getCreateUserID());
            tempModel.setNewUserID(model.getAssetReceiveRevertUserID());
            if (!VGUtility.isEmpty(temp.getAssetReceiveUseID()))
                tempModel.setAssetReceiveUseModel(assetReceiveUseDao.findById(temp.getAssetReceiveUseID()).get());

//            AssetHistoryDto historyDto = new AssetHistoryDto();
//            historyDto.setHistoryType(HISTORY_TYPE.变更记录);
//            historyDto.setChangeType(String.valueOf(CHANGE_TYPE.资产领用归还.ordinal()));
//            historyDto.setAssetModelId(tempModel.getAssetId());
//            historyDto.setCreateUserId(model.getCreateUserID());
//            historyDto.setChangeContent("操作[创建资产领用归还单]"
//                    + "[" + receiveRevertDepartment.getDeptName() + "]"
//                    + receiveRevertUser.getChsName() + "归还");
//            assetService.createHistory(historyDto);
            assetReceiveUseTempDao.save(tempModel);
        }
    }

    /**
     * 更新
     */
    @Override
    public void updateAssetReceiveUseTempByDeleteAssetReceiveRevertModel(AssetReceiveUseTempDto dto) {
        commonCheckAssetReceiveUseTempDto(dto);
        if (VGUtility.isEmpty(dto.getAssetId()))
            throw new RuntimeException("资产ID，不能为空！");

        AssetReceiveUseTempModel tempModel = assetReceiveUseTempDao.findById(dto.getId()).get();
        if (!VGUtility.isEmpty(dto.getAssetReceiveUseID())) {
            AssetReceiveUseModel assetReceiveUseModel = assetReceiveUseDao.findById(dto.getAssetReceiveUseID()).get();
            if (!VGUtility.isEmpty(assetReceiveUseModel))
                tempModel.setAssetReceiveUseModel(assetReceiveUseModel);
        }
        tempModel.setAssetReceiveRevertModel(null);
        if (VGUtility.isEmpty(dto.getNewUserID()))
            tempModel.setNewUserID(dto.getNewUserID());
        if (VGUtility.isEmpty(dto.getOldUserID()))
            tempModel.setOldUserID(dto.getOldUserID());
        if (VGUtility.isEmpty(dto.getAssetId()))
            tempModel.setAssetId(dto.getAssetId());
        if (!VGUtility.isEmpty(dto.getSavePlaceId()))
            tempModel.setSavePlaceId(dto.getSavePlaceId());
        assetReceiveUseTempDao.save(tempModel);
    }

    ;

    @Override
    public void updateAssetReceiveUseTemp(AssetReceiveUseTempDto dto) {
        commonCheckAssetReceiveUseTempDto(dto);
        if (VGUtility.isEmpty(dto.getAssetId()))
            throw new RuntimeException("资产ID，不能为空！");

        AssetReceiveUseTempModel tempModel = assetReceiveUseTempDao.findById(dto.getId()).get();
        if (!VGUtility.isEmpty(dto.getAssetReceiveUseID())) {
            AssetReceiveUseModel assetReceiveUseModel = assetReceiveUseDao.findById(dto.getAssetReceiveUseID()).get();
            if (!VGUtility.isEmpty(assetReceiveUseModel))
                tempModel.setAssetReceiveUseModel(assetReceiveUseModel);
        }
        if (!VGUtility.isEmpty(dto.getAssetReceiveRevertID())) {
            AssetReceiveRevertModel assetReceiveRevertModel = assetReceiveRevertDao.findById(dto.getAssetReceiveRevertID()).get();
            if (!VGUtility.isEmpty(assetReceiveRevertModel))
                tempModel.setAssetReceiveRevertModel(assetReceiveRevertModel);
        }
        if (!VGUtility.isEmpty(dto.getNewUserID()))
            tempModel.setNewUserID(dto.getNewUserID());
        if (!VGUtility.isEmpty(dto.getOldUserID()))
            tempModel.setOldUserID(dto.getOldUserID());
        if (!VGUtility.isEmpty(dto.getAssetId()))
            tempModel.setAssetId(dto.getAssetId());
        if (!VGUtility.isEmpty(dto.getSavePlaceId()))
            tempModel.setSavePlaceId(dto.getSavePlaceId());
        if (!VGUtility.isEmpty(dto.getRevertSavePlaceId()))
            tempModel.setRevertSavePlaceId(dto.getRevertSavePlaceId());
        assetReceiveUseTempDao.save(tempModel);
    }

    ;

    /***
     * 删除资产归还的子表信息(且判断不能全部删除)
     */
    @Override
    public void deleteAssetReceiveUseTempByAssetReceiveRevertID(List<String> tempID, String AssetReceiveRevertID) {
        for (String temp : tempID) {
            AssetReceiveUseTempModel model = assetReceiveUseTempDao.findById(temp).get();
            AssetAssetDto asset = assetService.getAssetByAssetId(model.getAssetId());
            if (VGUtility.isEmpty(asset))
                throw new RuntimeException("该资产数据已被删除！");

//			asset.setAssetStatusStr(String.valueOf(ASSET_STATUS.使用.ordinal()));
//			asset.setBeforeChangeAssetStatusStr(null);
//			assetService.updateAssetBeforeAssetStatus(asset);

            updateAssetReceiveUseTempByDeleteAssetReceiveRevertModel(convertModelToDto(model));
        }
    }

    /***
     * 删除资产领用的子表信息(且判断不能全部删除)
     */
    @Override
    public void deleteAssetReceiveUseTempByAssetReceiveUseID(List<String> tempID, String AssetReceiveUseID) {
        for (String temp : tempID) {
            AssetReceiveUseTempModel model = assetReceiveUseTempDao.findById(temp).get();
            AssetAssetDto asset = assetService.getAssetByAssetId(model.getAssetId());
            if (VGUtility.isEmpty(asset))
                throw new RuntimeException("该资产数据已被删除！");

//			asset.setAssetStatusStr(String.valueOf(ASSET_STATUS.闲置.ordinal()));
//			asset.setBeforeChangeAssetStatusStr(null);
//			assetService.updateAssetBeforeAssetStatus(asset);

            assetReceiveUseTempDao.deleteById(temp);
        }
    }

    /**
     * 查询
     * 根据子表查询领用或归还资产的信息
     */
    @Override
    public AssetReceiveUseTempDto getAssetReceiveUseTempDto(String id) {
        AssetReceiveUseTempModel model = assetReceiveUseTempDao.getOne(id);
        AssetReceiveUseTempDto tempDto = convertAssetDtoToDto(model);
        return tempDto;
    }

    /*******资产领用子表查询 start*****/
    @Override
    public List<AssetReceiveUseTempDto> getAssetReceiveUseTempDtoList(String assetReceiveUseId) {
        List<AssetReceiveUseTempModel> modelList = assetReceiveUseTempDao.findByAssetReceiveUseModel_Id(assetReceiveUseId);
        List<AssetReceiveUseTempDto> dtoList = new ArrayList<AssetReceiveUseTempDto>();
        for (AssetReceiveUseTempModel model : modelList) {
            dtoList.add(getByAssetReceiveUseTempModel(model));
        }
        return dtoList;
    }
    /*******资产领用子表查询 end*****/
    /*******资产归还子表查询 start*****/
    @Override
    public List<AssetReceiveUseTempDto> getAssetReceiveUseTempDtoListByRevertId(String assetReceiveRevertId) {
        List<AssetReceiveUseTempModel> modelList = assetReceiveUseTempDao.findByAssetReceiveRevertModel_Id(assetReceiveRevertId);
        List<AssetReceiveUseTempDto> dtoList = new ArrayList<AssetReceiveUseTempDto>();
        for (AssetReceiveUseTempModel model : modelList) {
            dtoList.add(getByAssetReceiveUseTempModel(model));
        }
        return dtoList;
    }

    /*******资产归还子表查询 end*****/
    public AssetReceiveUseTempDto getByAssetReceiveUseTempModel(AssetReceiveUseTempModel model) {
        AssetReceiveUseTempDto assetDto = convertAssetDtoToDto(model);
        AssetReceiveRevertModel revert = new AssetReceiveRevertModel();
        if (!VGUtility.isEmpty(model.getAssetReceiveRevertModel()))
            revert = model.getAssetReceiveRevertModel();
        if (revert.getReceiptStatus() == FlowableInfo.WORKSTATUS .已审批)
            assetDto.setRevertStatus(true);
        if (!VGUtility.isEmpty(model.getAssetReceiveUseModel()))
            assetDto.setRevertTimeStr(VGUtility.toDateStr(model.getAssetReceiveUseModel().getReceiveTime(), "yyyy-MM-dd"));
        return assetDto;
    }

    //查询所有未归还的资产(根据当前登陆人查询所有已审批领用单,再查询所有没有归还单ID的子表)
    @Override
    public List<AssetReceiveUseTempDto> getListByUserId(String userId) {
        List<AssetReceiveUseTempDto> dto = new ArrayList<AssetReceiveUseTempDto>();
        List<AssetReceiveUseDto> assetReceiveUseDto = (List<AssetReceiveUseDto>) assetReceiveUseService.getAssetReceiveUseDtoByQuerysForDataGrid(new ISearchExpression() {
            @Override
            public Object change(Object... arg0) {
                CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                Root root = (Root) arg0[0];
                List<Predicate> andList = new ArrayList<>();
                Predicate finalPred = null;
                if (!VGUtility.isEmpty(userId))
                    andList.add(builder.equal(root.get("createUserID"), userId));
                andList.add(builder.equal(root.get("receiptStatus"), FlowableInfo.WORKSTATUS.已审批));
                if (andList.size() > 0)
                    finalPred = builder.and(andList.toArray(new Predicate[andList.size()]));
                return finalPred;
            }
        }, null).get("rows");
        for (AssetReceiveUseDto usedto : assetReceiveUseDto) {
            List<AssetReceiveUseTempDto> dtoList = getAssetReceiveUseTempDtoList(usedto.getId());
            dto.addAll(dtoList);
        }
        return dto;
    }

    // 查询未归还的
    @Override
    public List<AssetReceiveUseTempDto> getNRAsset(UserInfoDto userInfoDto, AssetAssetDto asset) {
        List<AssetReceiveUseTempDto> dtoList = new ArrayList<AssetReceiveUseTempDto>();

        List<AssetReceiveUseTempModel> modelList = assetReceiveUseTempDao.findAll(new Sort(Sort.Direction.DESC, "createTimestamp"));

        for (AssetReceiveUseTempModel model : modelList) {
            if ((VGUtility.isEmpty(model.getAssetReceiveRevertModel()) || (!VGUtility.isEmpty(model.getAssetReceiveRevertModel()) && model.getAssetReceiveRevertModel().getReceiptStatus()==(FlowableInfo.WORKSTATUS.拟稿)))
                    && model.getAssetReceiveUseModel().getCreateUserID().equals(userInfoDto.getId())
                    && model.getAssetReceiveUseModel().getReceiptStatus()==(FlowableInfo.WORKSTATUS.已审批)) {
                dtoList.add(convertAssetDtoToDto(model));
            }
        }
        if (!VGUtility.isEmpty(asset.getAssetCode()))
            dtoList = dtoList.stream().filter(a -> Objects.nonNull(a.getAssetCode()))
                    .filter(a -> a.getAssetCode().contains(asset.getAssetCode()))
                    .collect(Collectors.toList());
        if (!VGUtility.isEmpty(asset.getCombinationAssetName()))
            dtoList = dtoList.stream().filter(a -> Objects.nonNull(a.getCombinationAssetName()))
                    .filter(a -> a.getCombinationAssetName().contains(asset.getCombinationAssetName()))
                    .collect(Collectors.toList());
        if (!VGUtility.isEmpty(asset.getUseDeptId()))
            dtoList = dtoList.stream().filter(a -> Objects.nonNull(a.getUseDeptId()))
                    .filter(a -> a.getUseDeptId().contains(asset.getUseDeptId()))
                    .collect(Collectors.toList());
        if (!VGUtility.isEmpty(asset.getUserId()))
            dtoList = dtoList.stream().filter(a -> Objects.nonNull(a.getUserId()))
                    .filter(a -> a.getUserId().contains(asset.getUserId()))
                    .collect(Collectors.toList());
        //使用人=归还人
        if (!VGUtility.isEmpty(asset.getRevertUserId()))
            dtoList = dtoList.stream().filter(a -> Objects.nonNull(a.getNewUserID()))
                    .filter(a -> a.getNewUserID().contains(asset.getRevertUserId()))
                    .collect(Collectors.toList());

        //使用人=归还人
        if (!VGUtility.isEmpty(asset.getProduceType()))
            dtoList = dtoList.stream().filter(a -> Objects.nonNull(a.getProduceType()))
                    .filter(a -> a.getProduceType().equals(asset.getProduceType()))
                    .collect(Collectors.toList());
        return dtoList;
    }

    // ********************Convert Start********************

    /**
     * 数据转换 convert model to dto convert dto to model
     */
    private AssetReceiveUseTempDto convertModelToDto(AssetReceiveUseTempModel model) {
        AssetReceiveUseTempDto dto = new AssetReceiveUseTempDto();
        dto.setId(model.getId());
        if (!VGUtility.isEmpty(model.getAssetReceiveUseModel()))
            dto.setAssetReceiveUseID(model.getAssetReceiveUseModel().getId());
        if (!VGUtility.isEmpty(model.getAssetReceiveRevertModel()))
            dto.setAssetReceiveRevertID(model.getAssetReceiveRevertModel().getId());
        dto.setSavePlaceId(model.getSavePlaceId());
        dto.setNewUserID(model.getNewUserID());
        dto.setOldUserID(model.getOldUserID());
        dto.setAssetId(model.getAssetId());
        dto.setCreateTimestamp(model.getCreateTimestamp());
        return dto;
    }

    private AssetReceiveUseTempDto convertAssetDtoToDto(AssetReceiveUseTempModel model) {
        AssetReceiveUseTempDto temp = new AssetReceiveUseTempDto();
        AssetAssetDto dto = assetService.getAssetByAssetId(model.getAssetId());
        if (VGUtility.isEmpty(dto))
            throw new RuntimeException("资产已被删除");
        temp.setId(model.getId());
        temp.setAssetStatusStr(dto.getAssetStatusStr());
        temp.setProduceType(dto.getProduceType());

        // 根据assetId查到assetDto,根据dto中的placeId查到dictionaryDto，再查到code和chsName
        if (!VGUtility.isEmpty(model.getSavePlaceId())) {
            temp.setSavePlaceId(model.getSavePlaceId());
            DictDto savePlaceDto = dictService.getCommonCode(model.getSavePlaceId());
            if (!VGUtility.isEmpty(savePlaceDto)) {
                temp.setSavePlaceStr(savePlaceDto.getCode() + " " + savePlaceDto.getChsName());
            }
        }

        // 根据assetId查到assetDto,根据dto中的placeId查到dictionaryDto，再查到code和chsName
        if (!VGUtility.isEmpty(model.getRevertSavePlaceId())) {
            temp.setRevertSavePlaceId(model.getRevertSavePlaceId());
            DictDto savePlaceDto = dictService.getCommonCode(model.getRevertSavePlaceId());
            if (!VGUtility.isEmpty(savePlaceDto)) {
                temp.setRevertSavePlaceStr(savePlaceDto.getCode() + " " + savePlaceDto.getChsName());
            }
        }

        temp.setNewUserID(model.getNewUserID());
        if (!VGUtility.isEmpty(model.getNewUserID())) {
            UserInfoDto userDto = userService.getUserInfo(model.getNewUserID());
            if (!VGUtility.isEmpty(userDto)){
                temp.setUseStr(userDto.getChsName());
            }
        }
        temp.setOldUserID(model.getOldUserID());
        temp.setAssetId(model.getAssetId());
        temp.setCreateTimestamp(model.getCreateTimestamp());
        if (!VGUtility.isEmpty(model.getAssetReceiveUseModel()))
            temp.setAssetReceiveUseID(model.getAssetReceiveUseModel().getId());
        if (!VGUtility.isEmpty(model.getAssetReceiveRevertModel()))
            temp.setAssetReceiveRevertID(model.getAssetReceiveRevertModel().getId());
        temp.setAssetCode(dto.getAssetCode());
        temp.setAssetTypeStr(dto.getAssetTypeStr());
        temp.setSpecAndModels(dto.getSpecAndModels());
        temp.setSeriesNum(dto.getSeriesNum());
        temp.setUnitOfMeasStr(dto.getUnitOfMeasStr());
        if (!VGUtility.isEmpty(dto.getUnitOfMeasId())) {
            DictDto dictCommonDto = dictService.getCommonCode(dto.getUnitOfMeasId());
            if (!VGUtility.isEmpty(dictCommonDto))
                temp.setUnitOfMeasStr(dictCommonDto.getChsName());
        }
        temp.setAssetBrand(dto.getAssetBrand());
        double purcPrice = Double.parseDouble(dto.getPurcPrice());
        String purcPriceStr = new String();
        if (purcPrice > 0)
            purcPriceStr = VGUtility.toDoubleStr(purcPrice, "0.##");
        else
            purcPriceStr = "";
        temp.setPurcPrice(purcPriceStr);

        double equiOrigValue = Double.parseDouble(dto.getEquiOrigValue());
        String equiOrigValueStr = new String();
        if (equiOrigValue > 0)
            equiOrigValueStr = VGUtility.toDoubleStr(equiOrigValue, "0.##");
        else
            equiOrigValueStr = "";
        temp.setEquiOrigValue(equiOrigValueStr);

        // 延伸信息
        temp.setCompanyId(dto.getCompanyId());
        if (!VGUtility.isEmpty(dto.getCompanyId())) {
            DeptInfoDto deptDto = (DeptInfoDto) baseService.getDeptInfo(dto.getCompanyId());
            if (!VGUtility.isEmpty(deptDto))
                temp.setCompanyStr(deptDto.getDeptName());
        }
        temp.setBelongLine(dto.getBelongLine());
        if (!VGUtility.isEmpty(dto.getBelongLine())) {
            DictDto dictCommonDto = dictService.getCommonCode(dto.getBelongLine());
            if (!VGUtility.isEmpty(dictCommonDto))
                temp.setBelongLineStr(dictCommonDto.getChsName());
        }
        // 根据物资编码查询并将查询结果组合为资产类别和资产名称，然后set到dto中
        String assetTypeName = baseService.getAssetTypeByMaterialCode(dto.getMaterialCode());
        if (!VGUtility.isEmpty(assetTypeName))
            temp.setCombinationAssetType(assetTypeName);

        String assetName = baseService.getAssetNameByMaterialCode(dto.getMaterialCode());
        if (!VGUtility.isEmpty(assetName))
            temp.setCombinationAssetName(assetName);

        temp.setTechPara(dto.getTechPara());
        temp.setRemark(dto.getRemark());
        temp.setBuyDate(dto.getBuyDate());
        if (!VGUtility.isEmpty(dto.getManageDeptId())) {
            DeptInfoDto deptDto = (DeptInfoDto) baseService.getDeptInfo(dto.getManageDeptId());
            if (!VGUtility.isEmpty(deptDto))
                temp.setManageDeptStr(deptDto.getDeptName());
        }
        if (!VGUtility.isEmpty(dto.getManagerId())) {
            UserInfoDto userDto = userService.getUserInfo(dto.getManagerId());
            if (!VGUtility.isEmpty(userDto))
                temp.setManagerStr(userDto.getChsName());
        }
        temp.setAssetSource(dto.getAssetSource());
        temp.setContractNum(dto.getContractNum());
        temp.setTendersNum(dto.getTendersNum());
        temp.setMainPeriod(dto.getMainPeriod());
        temp.setSourceUser(dto.getSourceUser());
        temp.setSourceContactInfo(dto.getSourceContactInfo());
        temp.setProdTime(dto.getProdTime());
        temp.setAssetId(dto.getId());
        temp.setAssetStatus(ASSET_STATUS.values()[VGUtility.toInteger(dto.getAssetStatusStr())].toString());
        AssetReceiveUseDto use = null;
        if (!VGUtility.isEmpty(temp.getAssetReceiveUseID()))
            use = assetReceiveUseService.getAssetReceiveUseDto(temp.getAssetReceiveUseID());
        if (!VGUtility.isEmpty(temp.getAssetReceiveUseCode()))
            temp.setAssetReceiveUseCode(temp.getAssetReceiveUseCode());
        else if (!VGUtility.isEmpty(use))
            temp.setAssetReceiveUseCode(use.getAssetReceiveUseCode());
        AssetReceiveRevertDto revert = null;
        if (!VGUtility.isEmpty(temp.getAssetReceiveRevertID()))
            revert = assetReceiveRevertService.getAssetReceiveRevertDto(temp.getAssetReceiveRevertID());
        if (!VGUtility.isEmpty(temp.getAssetReceiveRevertCode()))
            temp.setAssetReceiveRevertCode(temp.getAssetReceiveRevertCode());
        else if (!VGUtility.isEmpty(revert))
            temp.setAssetReceiveRevertCode(revert.getAssetReceiveRevertCode());

        if (!VGUtility.isEmpty(temp.getCreateTimestamp()))
            temp.setCreateTimestamp(temp.getCreateTimestamp());
        if (!VGUtility.isEmpty(temp.getRevertTime())) {
            temp.setRevertTime(temp.getRevertTime());
            temp.setRevertTimeStr(VGUtility.toDateStr(VGUtility.toDateObj(temp.getRevertTime(), "yyyy/M/d"), "yyyy-MM-dd"));
        }
        return temp;
    }

}

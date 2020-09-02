package com.mine.product.czmtr.ram.asset.service;

import com.mine.base.dict.dto.DictDto;
import com.mine.base.dict.service.IDictService;
import com.mine.base.user.dto.DeptInfoDto;
import com.mine.base.user.dto.UserInfoDto;
import com.mine.base.user.service.IUserService;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dao.AssetAssetDao;
import com.mine.product.czmtr.ram.asset.dao.AssetBorrowDao;
import com.mine.product.czmtr.ram.asset.dao.AssetBorrowTempDao;
import com.mine.product.czmtr.ram.asset.dao.AssetRevertDao;
import com.mine.product.czmtr.ram.asset.dto.*;
import com.mine.product.czmtr.ram.asset.model.AssetAssetModel;
import com.mine.product.czmtr.ram.asset.model.AssetBorrowModel;
import com.mine.product.czmtr.ram.asset.model.AssetBorrowTempModel;
import com.mine.product.czmtr.ram.asset.model.AssetRevertModel;
import com.mine.product.czmtr.ram.asset.service.IAssetService.ASSET_STATUS;
import com.mine.product.czmtr.ram.asset.service.IAssetService.CHANGE_TYPE;
import com.mine.product.czmtr.ram.asset.service.IAssetService.HISTORY_TYPE;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.VGUtility;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 资产借用详情管理
 *
 * @author rockh
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AssetBorrowTempService implements IAssetBorrowTempService {

    @Autowired
    private AssetBorrowTempDao assetBorrowTempDao;
    @Autowired
    private AssetBorrowDao assetBorrowDao;
    @Autowired
    private AssetRevertDao assetRevertDao;
    @Autowired
    private AssetAssetDao assetDao; // 资产
    @Autowired
    private IUserService userService;
    @Autowired
    private IDictService dictService;
    @Autowired
    private IBaseService baseService;
    @Autowired
    private IAssetService assetService;
    @Autowired
    private IAssetBorrowService assetBorrowService;
    @Autowired
    private IAssetRevertService assetRevertService;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Map<String, String> getAssetMap(String id) {
        List<AssetBorrowTempDto> dtolist = this.getAssetBorrowTempDtoList(id);
        if (dtolist.size() <= 0) {
            throw new RuntimeException("资产列表不能为空！");
        }
        Map<String, String> map = new HashMap<>();
        for (AssetBorrowTempDto dto : dtolist) {
            map.put(dto.getAssetId(), dto.getSavePlaceId());
        }
        return map;
    }

    @Override
    public List<String> getAssetIdList(String id) {
        List<AssetBorrowTempDto> dtolist = this.getAssetBorrowTempDtoList(id);
        if (dtolist.size() <= 0) {
            throw new RuntimeException("资产列表不能为空！");
        }
        return dtolist.stream().map(e -> e.getAssetId()).collect(Collectors.toList());
    }

    @Override
    public List<String> getAssetIdListByAssetReverId(String id) {
        List<AssetBorrowTempModel> dtolist = assetBorrowTempDao.findByAssetRevertId(id);
        if (dtolist.size() <= 0) {
            throw new RuntimeException("资产列表不能为空！");
        }
        return dtolist.stream().map(e -> e.getAssetId()).collect(Collectors.toList());


    }

    // 通用检查
    public void commonCheckAssetBorrowListDto(AssetBorrowTempDto dto) {
        AssetAssetModel assetModel = assetDao.findById(dto.getAssetId()).get();
        if (VGUtility.isEmpty(assetModel))
            throw new RuntimeException(dto.getAssetChsName() + "数据已被删除！");

        if (VGUtility.isEmpty(dto.getAssetBorrowID()) && VGUtility.isEmpty(dto.getAssetRevertID()))
            throw new RuntimeException("借用单Id或归还单Id，不能为空！");
        if (VGUtility.isEmpty(dto.getSavePlaceId()))
            throw new RuntimeException("使用位置ID，不能为空！");
        if (VGUtility.isEmpty(dto.getOldUserID()))
            throw new RuntimeException("原使用人ID，不能为空！");
        if (VGUtility.isEmpty(dto.getNewUserID()))
            throw new RuntimeException("现使用人ID，不能为空！");
    }

    /**
     * 添加
     */
    @Override
    public AssetBorrowTempDto createAssetBorrowTemp(AssetBorrowTempDto dto) {
        commonCheckAssetBorrowListDto(dto);

        AssetBorrowTempModel tempModel = new AssetBorrowTempModel();
        AssetBorrowModel assetBorrowModel = assetBorrowDao.findById(dto.getAssetBorrowID()).get();
        tempModel.setAssetBorrowModel(assetBorrowModel);
        tempModel.setSavePlaceId(dto.getSavePlaceId());
        tempModel.setAssetId(dto.getAssetId());
        tempModel.setOldUserID(dto.getOldUserID());
        tempModel.setNewUserID(dto.getNewUserID());
        return convertModelToDto(assetBorrowTempDao.save(tempModel));
    }

    ;

    /*
     * 创建资产借用子表
     */
    @Override
    public void createAssetBorrowTempForList(List<AssetBorrowTempDto> dtolist, String assetBorrowModelId) {
        AssetBorrowModel model = assetBorrowDao.findById(assetBorrowModelId).get();
//        if (VGUtility.isEmpty(model))
//            throw new RuntimeException("借用信息不能为空，不能为空！");
//        DeptInfoDto borrowDepartment = new DeptInfoDto();
//        UserInfoDto borrowUser = new UserInfoDto();
//        if (!VGUtility.isEmpty(model.getAssetborrowDepartmentID())) {
//            borrowDepartment = (DeptInfoDto) baseService.getDeptInfo(model.getAssetborrowDepartmentID());
//        }
//        if (!VGUtility.isEmpty(model.getAssetborrowUserID())) {
//            borrowUser = userService.getUserInfo(model.getAssetborrowUserID());
//        }

        for (AssetBorrowTempDto temp : dtolist) {
            AssetBorrowTempModel tempModel = new AssetBorrowTempModel();
            tempModel.setAssetBorrowModel(model);
            tempModel.setSavePlaceId(temp.getSavePlaceId());
            tempModel.setAssetId(temp.getAssetId());
            tempModel.setOldUserID(model.getCreateUserID());
            tempModel.setNewUserID(model.getAssetborrowUserID());
//            AssetAssetDto asset = assetService.getAssetByAssetId(temp.getAssetId());
//            Boolean flag = assetService.getAssetStatus(ASSET_STATUS.values()[VGUtility.toInteger(asset.getAssetStatusStr())], ASSET_STATUS.借出);
//            if (!flag)
//                throw new RuntimeException(asset.getCombinationAssetName() + "当前资产不可以借出 ");
//
//            asset.setBeforeChangeAssetStatusStr(String.valueOf(ASSET_STATUS.闲置.ordinal()));
//            assetService.updateAssetBeforeAssetStatus(asset);
//
//            AssetHistoryDto historyDto = new AssetHistoryDto();
//            historyDto.setHistoryType(HISTORY_TYPE.变更记录);
//            historyDto.setChangeType(String.valueOf(CHANGE_TYPE.资产借用.ordinal()));
//            historyDto.setAssetModelId(tempModel.getAssetId());
//            historyDto.setCreateUserId(model.getCreateUserID());
//            historyDto.setChangeContent("操作[创建资产借用单]"
//                    + "借给[" + borrowDepartment.getDeptName() + "]"
//                    + borrowUser.getChsName()
//                    + ",借期至" + VGUtility.toDateStr(model.getRevertTime(), "yyyy-MM-dd"));
//            assetService.createHistory(historyDto);

            assetBorrowTempDao.save(tempModel);
        }
    }

    /*
     * 创建资产归还子表
     */
    @Override
    public void createAssetRevertTempForList(List<AssetBorrowTempDto> dtolist, String assetRevertModelId) {
        AssetRevertModel model = assetRevertDao.findById(assetRevertModelId).get();
        if (VGUtility.isEmpty(model))
            throw new RuntimeException("借用信息不能为空，不能为空！");
//        DeptInfoDto revertDepartment = new DeptInfoDto();
//        UserInfoDto revertUser = new UserInfoDto();
//        if (!VGUtility.isEmpty(model.getAssetrevertDepartmentID())) {
//            revertDepartment = (DeptInfoDto) baseService.getDeptInfo(model.getAssetrevertDepartmentID());
//        }
//        if (!VGUtility.isEmpty(model.getAssetrevertUserID())) {
//            revertUser = userService.getUserInfo(model.getAssetrevertUserID());
//        }

        for (AssetBorrowTempDto temp : dtolist) {
            AssetBorrowTempModel tempModel = new AssetBorrowTempModel();
            if (!VGUtility.isEmpty(temp.getId()))
                tempModel = assetBorrowTempDao.getOne(temp.getId());
            tempModel.setId(temp.getId());
            tempModel.setAssetRevertModel(model);
            tempModel.setRevertSavePlaceId(temp.getSavePlaceId());
            tempModel.setAssetId(temp.getAssetId());
            tempModel.setOldUserID(model.getCreateUserID());
            tempModel.setNewUserID(model.getAssetrevertUserID());
//			tempModel.setCreateTimestamp(temp.getCreateTimestamp());
//            AssetAssetDto asset = assetService.getAssetByAssetId(temp.getAssetId());
//            Boolean flag = assetService.getAssetStatus(ASSET_STATUS.values()[VGUtility.toInteger(asset.getAssetStatusStr())], ASSET_STATUS.闲置);
//            if (!flag)
//                throw new RuntimeException(asset.getCombinationAssetName() + "当前资产不可以归还 ");
//
//            asset.setBeforeChangeAssetStatusStr(String.valueOf(ASSET_STATUS.借出.ordinal()));
//            assetService.updateAssetBeforeAssetStatus(asset);

//            AssetHistoryDto historyDto = new AssetHistoryDto();
//            historyDto.setHistoryType(HISTORY_TYPE.变更记录);
//            historyDto.setChangeType(String.valueOf(CHANGE_TYPE.资产借用归还.ordinal()));
//            historyDto.setAssetModelId(tempModel.getAssetId());
//            historyDto.setCreateUserId(model.getCreateUserID());
//            historyDto.setChangeContent("操作[创建资产借用归还单]"
//                    + "[" + revertDepartment.getDeptName() + "]"
//                    + revertUser.getChsName() + "归还");
//            assetService.createHistory(historyDto);

            if (!VGUtility.isEmpty(temp.getAssetBorrowID()))
                tempModel.setAssetBorrowModel(assetBorrowDao.findById(temp.getAssetBorrowID()).get());
            assetBorrowTempDao.save(tempModel);
        }
    }

    //根据资产ID,插入资产借用子表
    @Override
    public void createAssetBorrowTempByassetIdListStr(String assetIdListStr, AssetBorrowDto dto) {
        List<String> assetIdList = new ArrayList<String>();
        if (!VGUtility.isEmpty(assetIdListStr))
            Arrays.stream(assetIdListStr.split(",")).forEach(arr -> assetIdList.add(arr));

        AssetBorrowModel assetBorrowModel = assetBorrowDao.findById(dto.getId()).get();
        if (VGUtility.isEmpty(assetBorrowModel))
            throw new RuntimeException("该借用单数据已被删除！");

//        DeptInfoDto borrowDepartment = new DeptInfoDto();
//        UserInfoDto borrowUser = new UserInfoDto();
//        if (!VGUtility.isEmpty(assetBorrowModel.getAssetborrowDepartmentID())) {
//            borrowDepartment = (DeptInfoDto) baseService.getDeptInfo(assetBorrowModel.getAssetborrowDepartmentID());
//        }
//        if (!VGUtility.isEmpty(assetBorrowModel.getAssetborrowUserID())) {
//            borrowUser = userService.getUserInfo(assetBorrowModel.getAssetborrowUserID());
//        }

        for (String assetId : assetIdList) {
            AssetAssetDto asset = assetService.getAssetByAssetId(assetId);
//            if (VGUtility.isEmpty(asset))
//                throw new RuntimeException("该资产数据已被删除！");
//
//            Boolean flag = assetService.getAssetStatus(ASSET_STATUS.values()[VGUtility.toInteger(asset.getAssetStatusStr())], ASSET_STATUS.借出);
//            if (flag == false)
//                throw new RuntimeException(asset.getCombinationAssetName() + "当前资产不可以借出 ");
//
//            asset.setBeforeChangeAssetStatusStr(String.valueOf(ASSET_STATUS.闲置.ordinal()));
//            assetService.updateAssetBeforeAssetStatus(asset);

            AssetBorrowTempModel tempModel = new AssetBorrowTempModel();
            tempModel.setAssetBorrowModel(assetBorrowModel);
            tempModel.setSavePlaceId(asset.getSavePlaceId());
            tempModel.setOldUserID(asset.getManagerId());
            tempModel.setNewUserID(assetBorrowModel.getAssetborrowUserID());
            tempModel.setAssetId(asset.getId());

//            AssetHistoryDto historyDto = new AssetHistoryDto();
//            historyDto.setHistoryType(HISTORY_TYPE.变更记录);
//            historyDto.setChangeType(String.valueOf(CHANGE_TYPE.资产借用.ordinal()));
//            historyDto.setAssetModelId(tempModel.getAssetId());
//            historyDto.setCreateUserId(assetBorrowModel.getCreateUserID());
//            historyDto.setChangeContent("操作[创建资产借用单]"
//                    + "借给[" + borrowDepartment.getDeptName() + "]"
//                    + borrowUser.getChsName()
//                    + ",借期至" + VGUtility.toDateStr(assetBorrowModel.getRevertTime(), "yyyy-MM-dd"));
//            assetService.createHistory(historyDto);
            assetBorrowTempDao.save(tempModel);
        }
    }

    ;

    //更新表    根据资产ID,插入资产归还子表
    @Override
    public void createAssetBorrowTempByAssetIdListStrForRevert(String assetBorrowTempIdListStr, AssetRevertDto dto) {
        List<String> assetBorrowTempIdList = new ArrayList<String>();
        if (!VGUtility.isEmpty(assetBorrowTempIdListStr))
            Arrays.stream(assetBorrowTempIdListStr.split(",")).forEach(arr -> assetBorrowTempIdList.add(arr));

        AssetRevertModel assetRevertModel = assetRevertDao.findById(dto.getId()).get();
        if (VGUtility.isEmpty(assetRevertModel))
            throw new RuntimeException("该归还单数据已被删除！");

//        DeptInfoDto revertDepartment = new DeptInfoDto();
//        UserInfoDto revertUser = new UserInfoDto();
//        if (!VGUtility.isEmpty(assetRevertModel.getAssetrevertDepartmentID())) {
//            revertDepartment = (DeptInfoDto) baseService.getDeptInfo(assetRevertModel.getAssetrevertDepartmentID());
//        }
//        if (!VGUtility.isEmpty(assetRevertModel.getAssetrevertUserID())) {
//            revertUser = userService.getUserInfo(assetRevertModel.getAssetrevertUserID());
//        }
        for (String assetBorrowTempId : assetBorrowTempIdList) {
            AssetBorrowTempModel tempModel = assetBorrowTempDao.findById(assetBorrowTempId).get();
            AssetAssetDto asset = assetService.getAssetByAssetId(tempModel.getAssetId());
//            if (VGUtility.isEmpty(asset))
//                throw new RuntimeException("该资产数据已被删除！");
//
//            Boolean flag = assetService.getAssetStatus(ASSET_STATUS.values()[VGUtility.toInteger(asset.getAssetStatusStr())], ASSET_STATUS.闲置);
//            if (!flag)
//                throw new RuntimeException(asset.getCombinationAssetName() + "当前资产不可以归还 ");
//
//            asset.setBeforeChangeAssetStatusStr(String.valueOf(ASSET_STATUS.借出.ordinal()));
//            assetService.updateAssetBeforeAssetStatus(asset);

            tempModel.setAssetRevertModel(assetRevertModel);
            tempModel.setRevertSavePlaceId(asset.getSavePlaceId());
            tempModel.setOldUserID(assetRevertModel.getCreateUserID());
            tempModel.setNewUserID(assetRevertModel.getAssetrevertUserID());
            tempModel.setAssetId(asset.getId());

//            AssetHistoryDto historyDto = new AssetHistoryDto();
//            historyDto.setHistoryType(HISTORY_TYPE.变更记录);
//            historyDto.setChangeType(String.valueOf(CHANGE_TYPE.资产借用归还.ordinal()));
//            historyDto.setAssetModelId(tempModel.getAssetId());
//            historyDto.setCreateUserId(assetRevertModel.getCreateUserID());
//            historyDto.setChangeContent("操作[创建资产借用归还单]"
//                    + "[" + revertDepartment.getDeptName() + "]"
//                    + revertUser.getChsName() + "归还");
//            assetService.createHistory(historyDto);
            assetBorrowTempDao.save(tempModel);
        }
    }

    ;

    /**
     * 更新
     */
    @Override
    public void updateAssetBorrowTemp(AssetBorrowTempDto dto) {
        commonCheckAssetBorrowListDto(dto);
        if (VGUtility.isEmpty(dto.getAssetId()))
            throw new RuntimeException("资产ID，不能为空！");

        AssetBorrowTempModel tempModel = assetBorrowTempDao.findById(dto.getId()).get();
        if (!VGUtility.isEmpty(dto.getAssetBorrowID())) {
            AssetBorrowModel assetBorrowModel = assetBorrowDao.findById(dto.getAssetBorrowID()).get();
            if (!VGUtility.isEmpty(assetBorrowModel))
                tempModel.setAssetBorrowModel(assetBorrowModel);
        }
        if (!VGUtility.isEmpty(dto.getAssetRevertID())) {
            AssetRevertModel assetRevertModel = assetRevertDao.findById(dto.getAssetRevertID()).get();
            if (!VGUtility.isEmpty(assetRevertModel))
                tempModel.setAssetRevertModel(assetRevertModel);
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
        assetBorrowTempDao.save(tempModel);
    }

    ;

    /***
     * 删除资产归还子表数据
     * @param dto
     */
    @Override
    public void updateAssetBorrowTempBydDeleteAssetRevertModel(AssetBorrowTempDto dto) {
        commonCheckAssetBorrowListDto(dto);
        if (VGUtility.isEmpty(dto.getAssetId()))
            throw new RuntimeException("资产ID，不能为空！");

        AssetBorrowTempModel tempModel = assetBorrowTempDao.findById(dto.getId()).get();
        if (!VGUtility.isEmpty(dto.getAssetBorrowID())) {
            AssetBorrowModel assetBorrowModel = assetBorrowDao.findById(dto.getAssetBorrowID()).get();
            if (!VGUtility.isEmpty(assetBorrowModel))
                tempModel.setAssetBorrowModel(assetBorrowModel);
        }
        tempModel.setAssetRevertModel(null);
        if (VGUtility.isEmpty(dto.getAssetId()))
            tempModel.setAssetId(dto.getAssetId());
        if (!VGUtility.isEmpty(dto.getSavePlaceId()))
            tempModel.setSavePlaceId(dto.getSavePlaceId());
        assetBorrowTempDao.save(tempModel);
    }

    /**
     * 删除 借用单子表
     */
    @Override
    public void deleteAssetBorrowTempByAssetBorrowID(List<String> assetIdListStr, String AssetBorrowID) {
        for (String assetBorrowTempId : assetIdListStr) {
            AssetBorrowTempModel model = assetBorrowTempDao.findById(assetBorrowTempId).get();
            AssetAssetDto asset = assetService.getAssetByAssetId(model.getAssetId());
            if (VGUtility.isEmpty(asset))
                throw new RuntimeException("该资产数据已被删除！");

            assetBorrowTempDao.deleteById(assetBorrowTempId);
        }
    }

    ;

    /**
     * 删除 归还单子表
     */
    @Override
    public void deleteAssetBorrowTempByAssetRevertID(List<String> assetIdListStr, String AssetRevertID) {
        for (String assetBorrowTempId : assetIdListStr) {
            AssetBorrowTempModel model = assetBorrowTempDao.findById(assetBorrowTempId).get();
            AssetAssetDto asset = assetService.getAssetByAssetId(model.getAssetId());
            if (VGUtility.isEmpty(asset))
                throw new RuntimeException("该资产数据已被删除！");

            updateAssetBorrowTempBydDeleteAssetRevertModel(convertModelToDto(model));
        }
    }

    ;

    /**
     * 查询
     * 根据子表查询借用或归还资产的信息
     */
    @Override
    public AssetBorrowTempDto getAssetBorrowTempDto(String id) {
        AssetBorrowTempModel model = assetBorrowTempDao.findById(id).get();
        AssetBorrowTempDto tempDto = convertModelToDto(model);
        return tempDto;
    }

    /*******资产借用子表查询 start*****/
    @Override
    public List<AssetBorrowTempDto> getAssetBorrowTempDtoList(String assetBorrowId) {
        List<AssetBorrowTempModel> modelList = assetBorrowTempDao.findByAssetBorrowID(assetBorrowId);
        List<AssetBorrowTempDto> dto = new ArrayList<AssetBorrowTempDto>();
        for (AssetBorrowTempModel model : modelList) {
            dto.add(getByAssetBorrowTempModel(model));
        }
        return dto;
    }

    /***
     * 资产借用未归还的
     * @param assetBorrowId
     * @return
     */
    public List<AssetBorrowTempDto> getAssetBorrowTempList(String assetBorrowId) {
        List<AssetBorrowTempModel> modelList = assetBorrowTempDao.findByAssetBorrowIDAndAssetRevertIDISNULL(assetBorrowId);
        List<AssetBorrowTempDto> dtoList = new ArrayList<AssetBorrowTempDto>();
        for (AssetBorrowTempModel model : modelList) {
            dtoList.add(getByAssetBorrowTempModel(model));
        }
        return dtoList;
    }

    /*******资产借用子表查询 end*****/
    /*******资产归还子表查询 start*****/
    @Override
    public List<AssetBorrowTempDto> getAssetBorrowTempDtoListByRevertId(String assetRevertId) {
        List<AssetBorrowTempModel> modelList = assetBorrowTempDao.findByAssetRevertId(assetRevertId);
        List<AssetBorrowTempDto> dtoList = new ArrayList<AssetBorrowTempDto>();
        for (AssetBorrowTempModel model : modelList) {
            dtoList.add(getByAssetBorrowTempModel(model));
        }
        return dtoList;
    }

    /***
     * 查询
     */
    @Override
    public Map<String, Object> getAssetBorrowTempDtoForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto) {
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<AssetBorrowTempModel> assetBorrowCriteria = builder.createQuery(AssetBorrowTempModel.class);
        Root<AssetBorrowTempModel> root = assetBorrowCriteria.from(AssetBorrowTempModel.class);
        Predicate predicate = (Predicate) searchExpression.change(root, builder);
        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            assetBorrowCriteria.where((Predicate) predicate);
        assetBorrowCriteria.orderBy(builder.asc(root.get("createTimestamp")));
        Query<AssetBorrowTempModel> query = session.createQuery(assetBorrowCriteria);
        //分页
        if (!VGUtility.isEmpty(pageableDto))
            query.setFirstResult((pageableDto.getPage() - 1) * pageableDto.getSize()).setMaxResults(pageableDto.getSize());
        List<AssetBorrowTempModel> modelList = query.getResultList();
        //total
        CriteriaQuery<Long> longCriteria = builder.createQuery(Long.class);
        root = longCriteria.from(AssetBorrowTempModel.class);
        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            longCriteria.where((Predicate) predicate);
        longCriteria.select(builder.count(root));
        long total = session.createQuery(longCriteria).uniqueResult();
        List<AssetBorrowTempDto> dtoList = new ArrayList<AssetBorrowTempDto>();
        for (AssetBorrowTempModel model : modelList) {
            dtoList.add(getByAssetBorrowTempModel(model));
        }
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtoList);
        modelMap.addAttribute("total", total);
        return modelMap;
    }

    /***
     * 根据当前登陆人,查询当前登陆人未归还的资产
     */
    @Override
    public List<AssetBorrowTempDto> getNRAsset(UserInfoDto userInfoDto, AssetAssetDto asset) {
        List<AssetBorrowTempDto> dtoList = new ArrayList<AssetBorrowTempDto>();
        List<AssetBorrowTempModel> modelList = assetBorrowTempDao.findAll(new Sort(Sort.Direction.DESC, "createTimestamp"));
        for (AssetBorrowTempModel model : modelList) {
            if ((VGUtility.isEmpty(model.getAssetRevertModel()) || (!VGUtility.isEmpty(model.getAssetRevertModel()) && model.getAssetRevertModel().getReceiptStatus()==FlowableInfo.WORKSTATUS.拟稿))
                    //资产借出时由资产管理员创建的单子   故此时登录人=资产的管理员；
                    && model.getAssetBorrowModel().getCreateUserID().equals(userInfoDto.getId())
                    && model.getAssetBorrowModel().getReceiptStatus()==FlowableInfo.WORKSTATUS .已审批
            ) {
                dtoList.add(convertModelToAssetDto(model));
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
                    .filter(a -> a.getNewUserID().equals(asset.getRevertUserId()))
                    .collect(Collectors.toList());
        //生产型和非生产型
        if (!VGUtility.isEmpty(asset.getProduceType()))
            dtoList = dtoList.stream().filter(a -> Objects.nonNull(a.getProduceType()))
                    .filter(a -> a.getProduceType().equals(asset.getProduceType()))
                    .collect(Collectors.toList());
        return dtoList;
    }

    //查询所有未归还的资产(根据当前登陆人查询所有已审批借用单,再查询所有没有归还单ID的子表)
    @Override
    public List<AssetBorrowTempDto> getListByUserId(String userId) {
        List<AssetBorrowTempDto> dto = new ArrayList<AssetBorrowTempDto>();
        List<AssetBorrowDto> assetBorrowdto = (List<AssetBorrowDto>) assetBorrowService.getAssetBorrowDtoByQuerysForDataGrid(new ISearchExpression() {
            @Override
            public Object change(Object... arg0) {
                CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                Root root = (Root) arg0[0];
                List<Predicate> andList = new ArrayList<>();
                Predicate finalPred = null;
                if (!VGUtility.isEmpty(userId))
                    andList.add(builder.equal(root.get("createUserID"), userId));
                andList.add(builder.equal(root.get("receiptStatus"), FlowableInfo.WORKSTATUS .已审批));
                if (andList.size() > 0)
                    finalPred = builder.and(andList.toArray(new Predicate[andList.size()]));
                return finalPred;
            }
        }, null).get("rows");
        for (AssetBorrowDto borrowdto : assetBorrowdto) {
            List<AssetBorrowTempDto> dtoList = getAssetBorrowTempList(borrowdto.getId());
            dto.addAll(dtoList);
        }
        return dto;
    }

    // ********************Convert Start********************

    /**
     * 数据转换 convert model to dto convert dto to model
     */
    public AssetBorrowTempDto getByAssetBorrowTempModel(AssetBorrowTempModel model) {
        AssetBorrowTempDto assetDto = convertModelToAssetDto(model);
        AssetBorrowModel borrow = model.getAssetBorrowModel();
        AssetRevertModel revert = new AssetRevertModel();
        if (!VGUtility.isEmpty(model.getAssetRevertModel()))
            revert = model.getAssetRevertModel();
        if (revert.getReceiptStatus() == FlowableInfo.WORKSTATUS .已审批)
            assetDto.setRevertStatus(true);
        if (!VGUtility.isEmpty(borrow))
            assetDto.setRevertTimeStr(VGUtility.toDateStr(borrow.getRevertTime(), "yyyy-MM-dd"));

        return assetDto;
    }

    private AssetBorrowTempDto convertModelToDto(AssetBorrowTempModel model) {
        AssetBorrowTempDto dto = new AssetBorrowTempDto();
        dto.setId(model.getId());
        if (!VGUtility.isEmpty(model.getAssetBorrowModel()))
            dto.setAssetBorrowID(model.getAssetBorrowModel().getId());
        if (!VGUtility.isEmpty(model.getAssetRevertModel()))
            dto.setAssetRevertID(model.getAssetRevertModel().getId());
        dto.setSavePlaceId(model.getSavePlaceId());
        dto.setRevertSavePlaceId(model.getRevertSavePlaceId());
        dto.setNewUserID(model.getNewUserID());
        dto.setOldUserID(model.getOldUserID());
        dto.setAssetId(model.getAssetId());
        dto.setCreateTimestamp(model.getCreateTimestamp());
        return dto;
    }


    private AssetBorrowTempDto convertModelToAssetDto(AssetBorrowTempModel model) {
        AssetBorrowTempDto temp = new AssetBorrowTempDto();
        AssetAssetDto dto = assetService.getAssetByAssetId(model.getAssetId());
        temp.setId(model.getId());
        //根据assetId查到assetDto,根据dto中的placeId查到dictionaryDto，再查到code和chsName
        if (!VGUtility.isEmpty(model.getSavePlaceId())) {
            temp.setSavePlaceId(model.getSavePlaceId());
            DictDto savePlaceDto = dictService.getCommonCode(model.getSavePlaceId());
            if (!VGUtility.isEmpty(savePlaceDto)) {
                temp.setSavePlaceStr(savePlaceDto.getCode() + " " + savePlaceDto.getChsName());
            }
        }
        //根据assetId查到assetDto,根据dto中的placeId查到dictionaryDto，再查到code和chsName
        if (!VGUtility.isEmpty(model.getRevertSavePlaceId())) {
            temp.setRevertSavePlaceId(model.getRevertSavePlaceId());
            DictDto savePlaceDto = dictService.getCommonCode(model.getRevertSavePlaceId());
            if (!VGUtility.isEmpty(savePlaceDto)) {
                temp.setRevertSavePlaceStr(savePlaceDto.getCode() + " " + savePlaceDto.getChsName());
            }
        }

        temp.setAssetStatusStr(dto.getAssetStatusStr());
        temp.setNewUserID(model.getNewUserID());
        if (!VGUtility.isEmpty(model.getNewUserID())) {
            UserInfoDto userDto = userService.getUserInfo(model.getNewUserID());
            if (!VGUtility.isEmpty(userDto)){
                temp.setUseStr(userDto.getChsName());
            }
        }

        temp.setOldUserID(model.getOldUserID());
        if (!VGUtility.isEmpty(model.getAssetBorrowModel()))
            temp.setAssetBorrowID(model.getAssetBorrowModel().getId());
        if (!VGUtility.isEmpty(model.getAssetRevertModel()))
            temp.setAssetRevertID(model.getAssetRevertModel().getId());
        temp.setProduceType(dto.getProduceType());
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
        AssetBorrowModel borrow = model.getAssetBorrowModel();
        if (!VGUtility.isEmpty(temp.getAssetborrowCode())) {
            temp.setAssetborrowCode(temp.getAssetborrowCode());
        } else if (!VGUtility.isEmpty(borrow)) {
            temp.setAssetborrowCode(borrow.getAssetborrowCode());
        }

        AssetRevertModel revert = model.getAssetRevertModel();
        if (!VGUtility.isEmpty(temp.getAssetrevertCode())) {
            temp.setAssetrevertCode(temp.getAssetrevertCode());
        } else if (!VGUtility.isEmpty(revert)) {
            temp.setAssetrevertCode(revert.getAssetrevertCode());
        }

        if (!VGUtility.isEmpty(model.getCreateTimestamp()))
            temp.setCreateTimestamp(model.getCreateTimestamp());
        if (!VGUtility.isEmpty(model.getAssetRevertModel())) {
            temp.setRevertTime(VGUtility.toDateStr(model.getAssetRevertModel().getRealrevertTime(), "yyyy-MM-dd"));
            temp.setRevertTimeStr(VGUtility.toDateStr(model.getAssetRevertModel().getRealrevertTime(), "yyyy-MM-dd"));
        }
        return temp;
    }
}

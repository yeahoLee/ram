package com.mine.product.czmtr.ram.asset.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mine.base.user.dto.DeptInfoDto;
import com.mine.base.user.dto.UserInfoDto;
import com.mine.base.user.service.IUserService;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dao.AssetAssetDao;
import com.mine.product.czmtr.ram.asset.dao.AssetBorrowDao;
import com.mine.product.czmtr.ram.asset.dao.AssetBorrowTempDao;
import com.mine.product.czmtr.ram.asset.dto.*;
import com.mine.product.czmtr.ram.asset.model.AssetAssetModel;
import com.mine.product.czmtr.ram.asset.model.AssetBorrowModel;
import com.mine.product.czmtr.ram.asset.model.AssetBorrowTempModel;
import com.mine.product.czmtr.ram.asset.service.IAssetService.ASSET_STATUS;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.VGUtility;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 资产借用管理
 *
 * @author rockh
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AssetBorrowService implements IAssetBorrowService {

    @Autowired
    private AssetBorrowDao assetBorrowDao;
    @Autowired
    private AssetBorrowTempDao assetBorrowTempDao;
    @Autowired
    private AssetAssetDao assetDao;
    @Autowired
    private IUserService userService;
    @Autowired
    private IAssetBorrowTempService assetBorrowTempService;
    @Autowired
    private IAssetService assetService;
    @Autowired
    private IBaseService baseService;
    @PersistenceContext
    private EntityManager entityManager;

    // 通用检查
    @Override
    public void commonCheckAssetBorrowDto(AssetBorrowDto dto) {
        if (VGUtility.isEmpty(dto.getAssetborrowDepartmentID()))
            throw new RuntimeException("借用部门为必填项，不能为空！");
        else {
            try {
                baseService.getDeptInfo(dto.getAssetborrowDepartmentID());
            } catch (Exception ex) {
                throw new RuntimeException("借用部门格式不正确！");
            }
        }

        if (VGUtility.isEmpty(dto.getAssetborrowUserID()))
            throw new RuntimeException("借用人为必填项，不能为空！");
        else {
            try {
                userService.getUserInfo(dto.getAssetborrowUserID());
            } catch (Exception ex) {
                throw new RuntimeException("借用人格式不正确！");
            }
        }
        if (VGUtility.isEmpty(dto.getReason()))
            throw new RuntimeException("借用事由为必填项，不能为空！");
        if (VGUtility.isEmpty(dto.getRevertTime()))
            throw new RuntimeException("拟归还日期为必填项,不能为空！");
        else {
            try {
                VGUtility.toDateObj(dto.getRevertTime(), "yyyy/M/d");
            } catch (Exception ex) {
                throw new RuntimeException("拟归还日期格式不正确！");
            }
        }
    }

    /**
     * 添加
     */
    @Override
    public AssetBorrowDto createAssetBorrow(String assetBorrowTempDtoList, AssetBorrowDto dto, UserInfoDto userInfoDto) {
        commonCheckAssetBorrowDto(dto);

        ArrayList<AssetBorrowTempDto> AssetBorrowTempDtoList = new ArrayList<AssetBorrowTempDto>();
        if (!VGUtility.isEmpty(assetBorrowTempDtoList))
            AssetBorrowTempDtoList = JSON.parseObject(assetBorrowTempDtoList, new TypeReference<ArrayList<AssetBorrowTempDto>>() {
            });
        else
            throw new RuntimeException("资产列表不能为空！");

        AssetBorrowModel model = (AssetBorrowModel) convertAssetBorrowModel(dto);
        model.setCreateUserID(userInfoDto.getId());
        model.setAssetborrowCode(getAssetborrowCode());
        model.setReceiptStatus(FlowableInfo.WORKSTATUS.拟稿);
        model.setRevertStatus(ASSETBORROW_REVERTSTATUS.未归还);
        model = assetBorrowDao.save(model);
        assetBorrowTempService.createAssetBorrowTempForList(AssetBorrowTempDtoList, model.getId());

        return convertModelToDto(model);
    }

    ;

    /**
     * 修改
     */
    @Override
    public AssetBorrowDto updateAssetBorrow(AssetBorrowDto dto) {
        commonCheckAssetBorrowDto(dto);

        if (VGUtility.isEmpty(dto.getAssetborrowCode()))
            throw new RuntimeException("借用编号不能为空！");
        AssetBorrowModel model = assetBorrowDao.findById(dto.getId()).get();
        List<AssetBorrowTempDto> templist = assetBorrowTempService.getAssetBorrowTempDtoList(model.getId());
        if (templist.size() <= 0)
            throw new RuntimeException("资产列表不能为空！");

        if (!VGUtility.isEmpty(dto.getAssetborrowDepartmentID()))
            model.setAssetborrowDepartmentID(dto.getAssetborrowDepartmentID());
        if (!VGUtility.isEmpty(dto.getAssetborrowUserID()))
            model.setAssetborrowUserID(dto.getAssetborrowUserID());
        if (!VGUtility.isEmpty(dto.getRevertTime()))
            model.setRevertTime(VGUtility.toDateObj(dto.getRevertTime(), "yyyy/M/d"));
        if (!VGUtility.isEmpty(dto.getReason()))
            model.setReason(dto.getReason());
        if (!VGUtility.isEmpty(dto.getRevertStatus()))
            model.setRevertStatus(dto.getRevertStatus());
        if (!VGUtility.isEmpty(dto.getReceiptStatus()))
            model.setReceiptStatus(dto.getReceiptStatus());
        if (!VGUtility.isEmpty(dto.getProduceType()))
            model.setProduceType(dto.getProduceType());
        return convertModelToDto(assetBorrowDao.save(model));
    }

    ;

    //模拟发送审批
    @Override
    public void updateAssetBorrowStatus(AssetBorrowDto dto) {
        commonCheckAssetBorrowDto(dto);

        if (VGUtility.isEmpty(dto.getAssetborrowCode()))
            throw new RuntimeException("借用编号不能为空！");

        AssetBorrowModel model = assetBorrowDao.findById(dto.getId()).get();
        if (!VGUtility.isEmpty(dto.getReceiptStatus()))
            model.setReceiptStatus(dto.getReceiptStatus());
        assetBorrowDao.save(model);

        List<AssetBorrowTempDto> list = assetBorrowTempService.getAssetBorrowTempDtoList(model.getId());
        if (list.size() <= 0)
            throw new RuntimeException("资产列表不能为空！");
        for (AssetBorrowTempDto temp : list) {
            AssetAssetDto asset = assetService.getAssetByAssetId(temp.getAssetId());
            Boolean flag = assetService.getAssetStatus(ASSET_STATUS.values()[VGUtility.toInteger(asset.getAssetStatusStr())], ASSET_STATUS.借出);
            if (!flag)
                throw new RuntimeException(asset.getCombinationAssetName() + "当前资产不可以借出 ");
            if (dto.getReceiptStatus() == FlowableInfo.WORKSTATUS.已审批) {
                asset.setAssetStatusStr(String.valueOf(ASSET_STATUS.借出.ordinal()));
                asset.setBeforeChangeAssetStatusStr(null);
            }
            assetService.updateAssetBeforeAssetStatus(asset);
        }

    }

    ;

    /**
     * 删除
     */
    @Override
    public void deleteAssetBorrow(String assetBorrowID) {
        AssetBorrowModel model = assetBorrowDao.findById(assetBorrowID).get();
        List<AssetBorrowTempDto> dtolist = assetBorrowTempService.getAssetBorrowTempDtoList(model.getId());
//		for (AssetBorrowTempDto temp : dtolist) {
//			AssetAssetDto asset = assetService.getAssetByAssetId(temp.getAssetId());
//			asset.setAssetStatusStr(String.valueOf(ASSET_STATUS.闲置.ordinal()));
//			asset.setBeforeChangeAssetStatusStr(null);
//			assetService.updateAssetBeforeAssetStatus(asset);
//		}
        assetBorrowTempDao.deleteAssetBorrowTempModelByAssetBorrowModel(model);
        assetBorrowDao.deleteById(assetBorrowID);
    }

    ;

    /**
     * 查询
     */
    @Override
    public ModelMap getAssetBorrowDtoByQuerysForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto) {
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<AssetBorrowModel> assetBorrowCriteria = builder.createQuery(AssetBorrowModel.class);
        Root<AssetBorrowModel> root = assetBorrowCriteria.from(AssetBorrowModel.class);
        Predicate predicate = (Predicate) searchExpression.change(root, builder);

        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            assetBorrowCriteria.where((Predicate) predicate);

        assetBorrowCriteria.orderBy(builder.desc(root.get("assetborrowCode")));
        Query<AssetBorrowModel> query = session.createQuery(assetBorrowCriteria);
        //分页
        if (!VGUtility.isEmpty(pageableDto))
            query.setFirstResult((pageableDto.getPage() - 1) * pageableDto.getSize()).setMaxResults(pageableDto.getSize());
        List<AssetBorrowModel> modelList = query.getResultList();
        //total
        CriteriaQuery<Long> longCriteria = builder.createQuery(Long.class);
        root = longCriteria.from(AssetBorrowModel.class);
        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            longCriteria.where((Predicate) predicate);
        longCriteria.select(builder.count(root));
        long total = session.createQuery(longCriteria).uniqueResult();
        //convert
        List<AssetBorrowDto> dtoList = new ArrayList<AssetBorrowDto>();
        for (AssetBorrowModel model : modelList) {
            dtoList.add(convertAssetBorrowTempStr(model));
        }
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtoList);
        modelMap.addAttribute("total", total);
        return modelMap;
    }

    /***
     * 资产借用列表查询
     */
    @Override
    public Map<String, Object> getAssetBorrowByForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto, AssetBorrowDto assetBorrowDto) {
        int pagesize = pageableDto.getSize();
        int page = pageableDto.getPage();
        List<AssetBorrowDto> dtoList = (List<AssetBorrowDto>) getAssetBorrowDtoByQuerysForDataGrid(searchExpression, null).get("rows");
        if (!VGUtility.isEmpty(assetBorrowDto.getAssetCode()))
            dtoList = dtoList.stream().filter(a -> Objects.nonNull(a.getAssetCode()))
                    .filter(a -> a.getAssetCode().contains(assetBorrowDto.getAssetCode()))
                    .collect(Collectors.toList());
        if (!VGUtility.isEmpty(assetBorrowDto.getAssetBorrowTempStr()))
            dtoList = dtoList.stream().filter(a -> Objects.nonNull(a.getAssetBorrowTempStr()))
                    .filter(a -> a.getAssetBorrowTempStr().contains(assetBorrowDto.getAssetBorrowTempStr()))
                    .collect(Collectors.toList());
        ModelMap modelMap = new ModelMap();
        if (dtoList.size() >= pagesize)
            modelMap.addAttribute("rows", dtoList.subList((page - 1) * pagesize, page * pagesize <= dtoList.size() ? page * pagesize : dtoList.size()));
        else
            modelMap.addAttribute("rows", dtoList);
        modelMap.addAttribute("total", dtoList.size());
        return modelMap;
    }

    /***
     * 将借用单Model转换成dto,并获取借用资产名称(多个资产名称用,隔开),以及资产编码(多个编码名称用,隔开)用作查询条件
     * @param model
     * @return
     */
    public AssetBorrowDto convertAssetBorrowTempStr(AssetBorrowModel model) {
        AssetBorrowDto assetDto = convertModelToDto(model);
        List<AssetBorrowTempModel> resultList = assetBorrowTempDao.findByAssetBorrowID(model.getId());
        String str = "";
        String assetCodestr = "";
        for (AssetBorrowTempModel temp : resultList) {
            AssetAssetModel assetModel = assetDao.findAssetAssetModelById(temp.getAssetId());
            if (!VGUtility.isEmpty(assetModel)) {
                assetCodestr += "," + assetModel.getAssetCode();
                str += "," + baseService.getAssetNameByMaterialCode(assetModel.getMaterialCode());
            }
        }
        if (!VGUtility.isEmpty(assetCodestr))
            assetDto.setAssetCode(assetCodestr.substring(1, assetCodestr.length()));
        if (!VGUtility.isEmpty(str))
            assetDto.setAssetBorrowTempStr(str.substring(1, str.length()));
        return assetDto;
    }

    /***
     * 根据借用单ID获取借用单信息
     */
    @Override
    public AssetBorrowDto getAssetBorrowDto(String id) {
        AssetBorrowModel model = assetBorrowDao.findById(id).get();
        return convertModelToDto(model);
    }

    // ********************Convert Start********************

    /**
     * 数据转换 convert model to dto convert dto to model
     */
    @Override
    public Object convertAssetBorrowModel(AssetBorrowDto dto) {
        AssetBorrowModel model = new AssetBorrowModel();
        // 基本信息
        model.setId(dto.getId());
        model.setAssetborrowCode(dto.getAssetborrowCode());
        model.setAssetborrowDepartmentID(dto.getAssetborrowDepartmentID());
        model.setAssetborrowUserID(dto.getAssetborrowUserID());
        model.setCreateUserID(dto.getCreateUserID());
        model.setReason(dto.getReason());
        model.setRevertTime(VGUtility.toDateObj(dto.getRevertTime(), "yyyy/M/d"));
        model.setProduceType(dto.getProduceType());
        return model;
    }

    private AssetBorrowDto convertModelToDto(AssetBorrowModel model) {
        AssetBorrowDto dto = new AssetBorrowDto();
        dto.setId(model.getId());
        dto.setAssetborrowCode(model.getAssetborrowCode());
        dto.setAssetborrowDepartmentID(model.getAssetborrowDepartmentID());
        dto.setAssetborrowUserID(model.getAssetborrowUserID());
        dto.setCreateUserID(model.getCreateUserID());
        dto.setReason(model.getReason());
        dto.setRevertTime(VGUtility.toDateStr(model.getRevertTime(), "yyyy/M/d"));
        dto.setRevertTimeStr(VGUtility.toDateStr(model.getRevertTime(), "yyyy-MM-dd"));
        dto.setReceiptStatus(model.getReceiptStatus());
        dto.setReceiptIndex(model.getReceiptStatus().ordinal());
        dto.setRevertStatus(model.getRevertStatus());
        dto.setProduceType(model.getProduceType());
        if (!VGUtility.isEmpty(model.getRevertStatus()))
            dto.setRevertStatusStr(model.getRevertStatus().ordinal());
        dto.setCreateTimestamp(VGUtility.toDateStr(model.getCreateTimestamp(), "yyyy-MM-dd"));
        if (!VGUtility.isEmpty(userService.getUserInfo(model.getCreateUserID())))
            dto.setCreateUserName(userService.getUserInfo(model.getCreateUserID()).getChsName());
        if (!VGUtility.isEmpty(userService.getUserInfo(model.getAssetborrowUserID())))
            dto.setAssetborrowUserName(userService.getUserInfo(model.getAssetborrowUserID()).getChsName());
        if (!VGUtility.isEmpty((DeptInfoDto) baseService.getDeptInfo(model.getAssetborrowDepartmentID())))
            dto.setAssetborrowDepartmentName(((DeptInfoDto) baseService.getDeptInfo(model.getAssetborrowDepartmentID())).getDeptName());
        return dto;
    }

    // 生成借用编号
    @Override
    public String getAssetborrowCode() {
        Date time = Calendar.getInstance().getTime();
        String date = VGUtility.toDateStr(time, "yyyy-MM-dd");
        String hql = "from AssetBorrowModel where to_char(createtimestamp,'yyyy-MM-dd')='" + date
                + "' order by assetborrowCode desc";
        TypedQuery<AssetBorrowModel> query = entityManager.createQuery(hql, AssetBorrowModel.class);

        List<AssetBorrowModel> modelList = query.getResultList();
        String code = "";
        if (modelList.size() > 0) {
            String borrowCode = modelList.get(0).getAssetborrowCode();
            if (!VGUtility.isEmpty(borrowCode)) {
                code = borrowCode;
                Long num = Long.parseLong(code.substring(2)) + 1;
                code = code.substring(0, 2) + num;
            } else {
                code = "JY" + date.replace("-", "") + "001";
            }
        } else {
            code = "JY" + date.replace("-", "") + "001";
        }

        return code;
    }

    @Override
    public AssetApprove doApproveNotify(String id) {
        List<String> assetIdList = assetBorrowTempService.getAssetIdList(id);
        //校验资产状态，是否可以审批；
        return assetService.doAssetApproveCheck(assetIdList, ASSET_STATUS.借出);
    }

    @Override
    public void approveUpdateNotify(FlowableInfo.WORKSTATUS workStatus, String id) {
        List<String> assetIdList;
        switch (workStatus) {
            case 审批中:
                //资产冻结
                assetIdList = assetBorrowTempService.getAssetIdList(id);
                assetService.LockAssetState(assetIdList);
                break;
            case 驳回:
                //资产状态回滚
                assetIdList = assetBorrowTempService.getAssetIdList(id);
                assetService.approveFailureAssetStateRollback(assetIdList);
                break;
            case 已审批:
                //将资产状态落地
                AssetBorrowModel model = assetBorrowDao.findById(id).get();
                assetIdList = assetBorrowTempService.getAssetIdList(id);
                Map<String, String> assetMap = assetBorrowTempService.getAssetMap(id);
                assetService.approveSuccessUpdateAssetState(assetIdList, ASSET_STATUS.借出);
                assetService.doAssetUserUpdate(assetMap,model.getAssetborrowUserID(),model.getAssetborrowDepartmentID());
                this.createHistory(model, assetIdList);
                break;
            default:
                break;

        }
        //更新主表状态
        AssetBorrowModel model = assetBorrowDao.findById(id).get();
        model.setReceiptStatus(workStatus);

    }


    private void createHistory(AssetBorrowModel model, List<String> assetIdList) {
        DeptInfoDto borrowDepartment = new DeptInfoDto();
        UserInfoDto borrowUser = new UserInfoDto();
        if (!VGUtility.isEmpty(model.getAssetborrowDepartmentID())) {
            borrowDepartment = (DeptInfoDto) baseService.getDeptInfo(model.getAssetborrowDepartmentID());
        }
        if (!VGUtility.isEmpty(model.getAssetborrowUserID())) {
            borrowUser = userService.getUserInfo(model.getAssetborrowUserID());
        }
        for (String assetId : assetIdList) {
            AssetHistoryDto historyDto = new AssetHistoryDto();
            historyDto.setHistoryType(IAssetService.HISTORY_TYPE.变更记录);
            historyDto.setChangeType(String.valueOf(IAssetService.CHANGE_TYPE.资产借用.ordinal()));
            historyDto.setAssetModelId(assetId);
            historyDto.setCreateUserId(model.getCreateUserID());
            historyDto.setChangeContent("操作[资产借用]"
                    + "借给[" + borrowDepartment.getDeptName() + "]"
                    + borrowUser.getChsName()
                    + ",借期至" + VGUtility.toDateStr(model.getRevertTime(), "yyyy-MM-dd"));
            assetService.createHistory(historyDto);
        }

    }
}

package com.mine.product.czmtr.ram.asset.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mine.base.dict.dto.DictDto;
import com.mine.base.dict.service.IDictService;
import com.mine.base.user.dto.DeptInfoDto;
import com.mine.base.user.dto.UserInfoDto;
import com.mine.base.user.service.IUserService;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dao.AssetAllocationDao;
import com.mine.product.czmtr.ram.asset.dao.AssetAllocationTempDao;
import com.mine.product.czmtr.ram.asset.dao.AssetAssetDao;
import com.mine.product.czmtr.ram.asset.dto.*;
import com.mine.product.czmtr.ram.asset.model.AssetAllocationModel;
import com.mine.product.czmtr.ram.asset.model.AssetAllocationTempModel;
import com.mine.product.czmtr.ram.asset.model.AssetAssetModel;
import com.mine.product.czmtr.ram.asset.service.IAssetService.ASSET_STATUS;
import com.mine.product.czmtr.ram.asset.service.IAssetService.HISTORY_TYPE;
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

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AssetAllocationService implements IAssetAllocationService {

    @Autowired
    private AssetAllocationDao assetAllocationDao;
    @Autowired
    private AssetAllocationTempDao assetAllocationTempDao;
    @Autowired
    private AssetAssetDao assetDao;
    @Autowired
    private IUserService userService;
    @Autowired
    private IDictService dictService;
    @Autowired
    private IAssetAllocationTempService assetAllocationTempService;
    @Autowired
    private IBaseService baseService;
    @Autowired
    private IAssetService assetService;
    @PersistenceContext
    private EntityManager entityManager;

    // 通用检查
    @Override
    public void commonCheckAssetAllocationDto(AssetAllocationDto dto) {
        if (VGUtility.isEmpty(dto.getCallOutDepartmentId()))
            throw new RuntimeException("调出部门为必填项，不能为空！");
        else {
            try {
                baseService.getDeptInfo(dto.getCallOutDepartmentId());
            } catch (Exception ex) {
                throw new RuntimeException("调出部门格式不正确！");
            }
        }
        if (VGUtility.isEmpty(dto.getCallInDepartmentId()))
            throw new RuntimeException("调入部门为必填项，不能为空！");
        else {
            try {
                baseService.getDeptInfo(dto.getCallInDepartmentId());
            } catch (Exception ex) {
                throw new RuntimeException("调入部门为必填项，不能为空！");
            }
        }
        if (VGUtility.isEmpty(dto.getReason()))
            throw new RuntimeException("调拨原因为必填项，不能为空！");
    }

    /**
     * 添加
     */
    @Override
    public AssetAllocationDto createAssetAllocation(String assetAllocationTempDtoList, AssetAllocationDto dto, UserInfoDto userInfoDto) {
        commonCheckAssetAllocationDto(dto);
        List<AssetAllocationTempDto> AssetAllocationTempDtoList = new ArrayList<AssetAllocationTempDto>();
        if (!VGUtility.isEmpty(assetAllocationTempDtoList)) {
            AssetAllocationTempDtoList = JSON.parseObject(assetAllocationTempDtoList, new TypeReference<ArrayList<AssetAllocationTempDto>>() {
            });
        } else {
            throw new RuntimeException("资产列表不能为空！");
        }

        AssetAllocationModel model = convertAssetAllocationModel(dto);
        model.setCreateUserID(userInfoDto.getId());
        model.setAssetAllocationCode(getAssetAllocationCode());
        model.setReceiptStatus(FlowableInfo.WORKSTATUS.拟稿);
        model = assetAllocationDao.save(model);
        assetAllocationTempService.createAssetAllocationTempForList(AssetAllocationTempDtoList, model.getId());

        return convertModelToDto(model);
    }

    ;

    /**
     * 修改
     */
    @Override
    public AssetAllocationDto updateAssetAllocatiom(AssetAllocationDto dto) {
        commonCheckAssetAllocationDto(dto);
        if (VGUtility.isEmpty(dto.getAssetAllocationCode()))
            throw new RuntimeException("调拨编号不能为空！");

        AssetAllocationModel model = assetAllocationDao.findById(dto.getId()).get();
        List<AssetAllocationTempDto> templist = assetAllocationTempService.getAssetAllocationTempDtoList(model.getId());
        if (templist.size() <= 0)
            throw new RuntimeException("资产列表不能为空！");

        if (!VGUtility.isEmpty(dto.getCallInDepartmentId()))
            model.setCallInDepartmentId(dto.getCallInDepartmentId());
        if (!VGUtility.isEmpty(dto.getCallInAssetManagerId()))
            model.setCallInAssetManagerId(dto.getCallInAssetManagerId());
        if (!VGUtility.isEmpty(dto.getCallInSavePlaceId()))
            model.setCallInSavePlaceId(dto.getCallInSavePlaceId());
        if (!VGUtility.isEmpty(dto.getReason()))
            model.setReason(dto.getReason());
        if (!VGUtility.isEmpty(dto.getCallOutDepartmentId()))
            model.setCallOutDepartmentId(dto.getCallOutDepartmentId());

        return convertModelToDto(assetAllocationDao.save(model));
    }

    ;

    /***
     * 模拟发送审批
     */
    @Override
    public AssetAllocationDto updateAssetAllocationStatus(AssetAllocationDto dto) {
        commonCheckAssetAllocationDto(dto);

        if (VGUtility.isEmpty(dto.getAssetAllocationCode()))
            throw new RuntimeException("调拨编号不能为空！");
        AssetAllocationModel model = assetAllocationDao.findById(dto.getId()).get();
        DeptInfoDto checkIn = new DeptInfoDto();
        DeptInfoDto checkOut = new DeptInfoDto();
        DeptInfoDto checkInCom = new DeptInfoDto();
        DeptInfoDto checkOutCom = new DeptInfoDto();
        if (!VGUtility.isEmpty(model.getCallInDepartmentId())) {
            checkIn = (DeptInfoDto) baseService.getDeptInfo(model.getCallInDepartmentId());
            if (!VGUtility.isEmpty(checkIn.getPdId()))
                checkInCom = baseService.getDeptByParentId(checkIn.getPdId());
            else
                checkInCom = checkIn;
        }
        if (!VGUtility.isEmpty(model.getCallOutDepartmentId())) {
            checkOut = (DeptInfoDto) baseService.getDeptInfo(model.getCallOutDepartmentId());
            if (!VGUtility.isEmpty(checkOut.getPdId()))
                checkOutCom = baseService.getDeptByParentId(checkOut.getPdId());
            else
                checkOutCom = checkOut;
        }
        String CallInSavePlaceStr = "";
        // 根据assetId查到assetDto,根据dto中的placeId查到dictionaryDto，再查到code和chsName
        if (!VGUtility.isEmpty(model.getCallInSavePlaceId())) {
            DictDto savePlaceDto = dictService.getCommonCode(model.getCallInSavePlaceId());
            if (!VGUtility.isEmpty(savePlaceDto)) {
                CallInSavePlaceStr = savePlaceDto.getCode() + " " + savePlaceDto.getChsName();
            }
        }
        List<AssetAllocationTempDto> dtolist = assetAllocationTempService.getAssetAllocationTempDtoList(dto.getId());
        if (dtolist.size() <= 0)
            throw new RuntimeException("资产列表不能为空！");
        for (AssetAllocationTempDto temp : dtolist) {
            AssetAssetModel assetModel = assetDao.findById(temp.getAssetId()).get();
            Boolean flag = assetService.getAssetStatus(assetModel.getAssetStatus(), ASSET_STATUS.调拨);
            if (!flag) {
                throw new RuntimeException("当前资产不可以调拨 ");
            } else if (!assetModel.getManagerId().equals(dto.getCreateUserID()))
                throw new RuntimeException("当前资产不可以调拨 ");

            if (VGUtility.isEmpty(dto.getCallInAssetManagerId()))
                assetModel.setManagerId(null);
            else
                assetModel.setManagerId(dto.getCallInAssetManagerId());
            if (!VGUtility.isEmpty(dto.getCallInSavePlaceId()))
                assetModel.setSavePlaceId(dto.getCallInSavePlaceId());

            if (dto.getReceiptStatus() == FlowableInfo.WORKSTATUS.已审批) {
                assetModel.setAssetStatus(ASSET_STATUS.闲置);
                assetModel.setBeforeChangeAssetStatus(null);
            }
            assetModel.setManageDeptId(dto.getCallInDepartmentId());
            assetDao.save(assetModel);

            AssetHistoryDto historyDto = new AssetHistoryDto();
            historyDto.setHistoryType(HISTORY_TYPE.调拨记录);
            historyDto.setAssetModelId(temp.getAssetId());
            historyDto.setCreateUserId(model.getCreateUserID());
            historyDto.setCheckInCom(checkInCom.getDeptName());//调入公司
            historyDto.setCheckInDept(checkIn.getDeptName());//调入部门
            historyDto.setCheckOutCom(checkOutCom.getDeptName());//调出公司
            historyDto.setCheckOutDept(checkOut.getDeptName());//调出部门
            historyDto.setModifyContent((dto.getReceiptStatus() == FlowableInfo.WORKSTATUS.审批中 ? "操作[发起审批]" : "操作[审批完成]")
                    + "调入公司[" + checkInCom.getDeptName() + "],"
                    + "调入部门[" + checkIn.getDeptName() + "],"
                    + "调出公司[" + checkOutCom.getDeptName() + "],"
                    + "调出部门[" + checkOut.getDeptName() + "]");
            if (!VGUtility.isEmpty(model.getCallInSavePlaceId()))
                historyDto.setChangeContent("移动至" + CallInSavePlaceStr);
            assetService.createHistory(historyDto);

        }
        model.setReceiptStatus(dto.getReceiptStatus());
        return convertModelToDto(assetAllocationDao.save(model));
    }

    ;

    /**
     * 删除
     */
    @Override
    public void deleteAssetAllocation(String assetAllocationID) {
        AssetAllocationModel model = assetAllocationDao.findById(assetAllocationID).get();
        assetAllocationTempDao.deleteAssetAllocationTempModelByAssetAllocationModel(model);
        assetAllocationDao.deleteById(assetAllocationID);
    }


    /***
     * 根据调拨单ID获取调拨单信息
     */
    @Override
    public AssetAllocationDto getAssetAllocationDtoById(String id) {
        AssetAllocationModel model = assetAllocationDao.findById(id).get();
        return convertModelToDto(model);
    }

    /***
     * 根据调拨单code获取调拨单信息
     */
    @Override
    public AssetAllocationDto getAssetAllocationDtoByCode(String assetAllocationCode) {
        AssetAllocationModel model = assetAllocationDao.getAssetAllocationModelByAssetAllocationCode(assetAllocationCode);
        return convertModelToDto(model);
    }

    /**
     * 查询
     */
    @Override
    public ModelMap getAssetAllocationDtoByQuerysForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto) {
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<AssetAllocationModel> Criteria = builder.createQuery(AssetAllocationModel.class);
        Root<AssetAllocationModel> root = Criteria.from(AssetAllocationModel.class);
        Predicate predicate = (Predicate) searchExpression.change(root, builder);

        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            Criteria.where((Predicate) predicate);

        Criteria.orderBy(builder.desc(root.get("assetAllocationCode")));
        Query<AssetAllocationModel> query = session.createQuery(Criteria);
        //分页
        if (!VGUtility.isEmpty(pageableDto))
            query.setFirstResult((pageableDto.getPage() - 1) * pageableDto.getSize()).setMaxResults(pageableDto.getSize());
        List<AssetAllocationModel> modelList = query.getResultList();
        //total
        CriteriaQuery<Long> longCriteria = builder.createQuery(Long.class);
        root = longCriteria.from(AssetAllocationModel.class);
        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            longCriteria.where((Predicate) predicate);
        longCriteria.select(builder.count(root));
        long total = session.createQuery(longCriteria).uniqueResult();
        //convert
        List<AssetAllocationDto> dtoList = new ArrayList<AssetAllocationDto>();
        for (AssetAllocationModel model : modelList) {
            dtoList.add(convertAssetAllocationTempStr(model));
        }
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtoList);
        modelMap.addAttribute("total", total);
        return modelMap;
    }

    /***
     * 将借用单Model转换成dto,并获取借用资产名称(多个资产名称用,隔开),以及资产编码(多个编码名称用,隔开)用作查询条件
     * @param model
     * @return
     */
    public AssetAllocationDto convertAssetAllocationTempStr(AssetAllocationModel model) {
        AssetAllocationDto assetDto = convertModelToDto(model);
        List<AssetAllocationTempModel> resultList = assetAllocationTempDao.findAllByAssetAllocationModel_Id(model.getId());
        String str = "";
        for (AssetAllocationTempModel temp : resultList) {
            AssetAssetModel assetModel = assetDao.findAssetAssetModelById(temp.getAssetId());
            if (!VGUtility.isEmpty(assetModel))
                str += "," + baseService.getAssetNameByMaterialCode(assetModel.getMaterialCode());
        }
        if (!VGUtility.isEmpty(str))
            assetDto.setAssetAllocationTempStr(str.substring(1, str.length()));
        return assetDto;
    }

    // ********************Convert Start********************

    /**
     * 数据转换 convert model to dto convert dto to model
     */
    @Override
    public AssetAllocationModel convertAssetAllocationModel(AssetAllocationDto dto) {
        AssetAllocationModel model = new AssetAllocationModel();
        // 基本信息
        model.setId(dto.getId());
        model.setAssetAllocationCode(dto.getAssetAllocationCode());
        if (!VGUtility.isEmpty(dto.getCallInAssetManagerId()))
            model.setCallInAssetManagerId(dto.getCallInAssetManagerId());
        model.setCallInDepartmentId(dto.getCallInDepartmentId());
        model.setCreateUserID(dto.getCreateUserID());
        model.setReason(dto.getReason());
        if (!VGUtility.isEmpty(dto.getCallInSavePlaceId()))
            model.setCallInSavePlaceId(dto.getCallInSavePlaceId());
        model.setCallOutDepartmentId(dto.getCallOutDepartmentId());
        model.setProduceType(dto.getProduceType());
        return model;
    }

    private AssetAllocationDto convertModelToDto(AssetAllocationModel model) {
        AssetAllocationDto dto = new AssetAllocationDto();
        dto.setId(model.getId());
        dto.setAssetAllocationCode(model.getAssetAllocationCode());
        if (!VGUtility.isEmpty(model.getCallInAssetManagerId()))
            dto.setCallInAssetManagerId(model.getCallInAssetManagerId());
        dto.setCallInDepartmentId(model.getCallInDepartmentId());
        dto.setCreateUserID(model.getCreateUserID());
        dto.setReason(model.getReason());
        dto.setCallInSavePlaceId(model.getCallInSavePlaceId());
        // 根据assetId查到assetDto,根据dto中的placeId查到dictionaryDto，再查到code和chsName
        if (!VGUtility.isEmpty(model.getCallInSavePlaceId())) {
            DictDto savePlaceDto = dictService.getCommonCode(model.getCallInSavePlaceId());
            if (!VGUtility.isEmpty(savePlaceDto)) {
                dto.setCallInSavePlaceStr(savePlaceDto.getCode() + " " + savePlaceDto.getChsName());
            }
        }

        dto.setCallOutDepartmentId(model.getCallOutDepartmentId());
        dto.setReceiptStatus(model.getReceiptStatus());
        if (!VGUtility.isEmpty(model.getReceiptStatus()))
            dto.setReceiptStatusStr(model.getReceiptStatus().ordinal());
        dto.setCreateTimestamp(VGUtility.toDateStr(model.getCreateTimestamp(), "yyyy-MM-dd"));
        dto.setCreateUserName(userService.getUserInfo(model.getCreateUserID()).getChsName());
        if (!VGUtility.isEmpty(model.getCallInAssetManagerId()))
            dto.setCallInAssetManagerName(userService.getUserInfo(model.getCallInAssetManagerId()).getChsName());
        if (!VGUtility.isEmpty(model.getCallInDepartmentId()))
            dto.setCallInDepartmentName(((DeptInfoDto) baseService.getDeptInfo(model.getCallInDepartmentId())).getDeptName());
        if (!VGUtility.isEmpty(model.getCallOutDepartmentId()))
            dto.setCallOutDepartmentName(((DeptInfoDto) baseService.getDeptInfo(model.getCallOutDepartmentId())).getDeptName());
        dto.setProduceType(model.getProduceType());
        return dto;
    }

    // 生成借用编号
    @Override
    public String getAssetAllocationCode() {
        Date time = Calendar.getInstance().getTime();
        String date = VGUtility.toDateStr(time, "yyyy-MM-dd");
        String hql = "from AssetAllocationModel where to_char(createtimestamp,'yyyy-MM-dd')='" + date
                + "' order by assetAllocationCode desc";
        TypedQuery<AssetAllocationModel> query = entityManager.createQuery(hql, AssetAllocationModel.class);

        List<AssetAllocationModel> modelList = query.getResultList();
        String code = "";
        if (modelList.size() > 0) {
            String allocationCode = modelList.get(0).getAssetAllocationCode();
            if (!VGUtility.isEmpty(allocationCode)) {
                code = allocationCode;
                Long num = Long.parseLong(code.substring(2)) + 1;
                code = code.substring(0, 2) + num;
            } else {
                code = "DB" + date.replace("-", "") + "001";
            }
        } else {
            code = "DB" + date.replace("-", "") + "001";
        }

        return code;
    }

    @Override
    public AssetApprove doApproveNotify(String id) {
        List<String> assetIdList = assetAllocationTempService.getAssetIdList(id);
        //校验资产状态，是否可以审批；
        return assetService.doAssetApproveCheck(assetIdList, ASSET_STATUS.调拨);
    }


    @Override
    public void approveUpdateNotify(FlowableInfo.WORKSTATUS workStatus, String id) {
        List<String> assetIdList;
        switch (workStatus) {
            case 审批中:
                //资产冻结
                assetIdList = assetAllocationTempService.getAssetIdList(id);
                assetService.LockAssetState(assetIdList);
                break;
            case 驳回:
                //资产状态回滚
                assetIdList = assetAllocationTempService.getAssetIdList(id);
                assetService.approveFailureAssetStateRollback(assetIdList);
                break;
            case 已审批:
                //将资产状态落地
                AssetAllocationModel model = assetAllocationDao.findById(id).get();
                assetIdList = assetAllocationTempService.getAssetIdList(id);
                assetService.approveSuccessUpdateAssetState(assetIdList, null);
                assetService.doAssetAllocation(assetIdList, model.getCallInDepartmentId(), model.getCallInAssetManagerId(), model.getCallInSavePlaceId());
                //创建历史记录；
                createHistory(model, assetIdList.toArray(new String[assetIdList.size()]));
                break;
            default:
                break;

        }
        //更新主表状态
        AssetAllocationModel model = assetAllocationDao.findById(id).get();
        model.setReceiptStatus(workStatus);

    }

    private void createHistory(AssetAllocationModel model, String[] assetIdList) {

        DeptInfoDto checkIn = new DeptInfoDto();
        DeptInfoDto checkOut = new DeptInfoDto();
        DeptInfoDto checkInCom = new DeptInfoDto();
        DeptInfoDto checkOutCom = new DeptInfoDto();
        if (!VGUtility.isEmpty(model.getCallInDepartmentId())) {
            checkIn = (DeptInfoDto) baseService.getDeptInfo(model.getCallInDepartmentId());
            if (!VGUtility.isEmpty(checkIn.getPdId())) {
                checkInCom = baseService.getDeptByParentId(checkIn.getPdId());
            } else {
                checkInCom = checkIn;
            }
        }
        if (!VGUtility.isEmpty(model.getCallOutDepartmentId())) {
            checkOut = (DeptInfoDto) baseService.getDeptInfo(model.getCallOutDepartmentId());
            if (!VGUtility.isEmpty(checkOut.getPdId()))
                checkOutCom = baseService.getDeptByParentId(checkOut.getPdId());
            else
                checkOutCom = checkOut;
        }
        String CallInSavePlaceStr = "";
        // 根据assetId查到assetDto,根据dto中的placeId查到dictionaryDto，再查到code和chsName
        if (!VGUtility.isEmpty(model.getCallInSavePlaceId())) {
            DictDto savePlaceDto = dictService.getCommonCode(model.getCallInSavePlaceId());
            if (!VGUtility.isEmpty(savePlaceDto)) {
                CallInSavePlaceStr = savePlaceDto.getCode() + " " + savePlaceDto.getChsName();
            }
        }

        //创建历史记录
        for (String assetId : assetIdList) {
            AssetHistoryDto historyDto = new AssetHistoryDto();
            historyDto.setHistoryType(HISTORY_TYPE.调拨记录);
            historyDto.setAssetModelId(assetId);
            historyDto.setCreateUserId(model.getCreateUserID());
            historyDto.setCheckInCom(checkInCom.getDeptName());//调入公司
            historyDto.setCheckInDept(checkIn.getDeptName());//调入部门
            historyDto.setCheckOutCom(checkOutCom.getDeptName());//调出公司
            historyDto.setCheckOutDept(checkOut.getDeptName());//调出部门
            FlowableInfo.WORKSTATUS status = model.getReceiptStatus();
            String contentStr = "";
            switch (status) {
                case 拟稿:
                    break;
                case 审批中:
                    contentStr = "操作[审批中]";
                    break;
                case 已审批:
                    contentStr = "操作[已审批]";
                    break;
                case 驳回:
                    contentStr = "操作[驳回]";
                    break;
                case 作废:
                    contentStr = "操作[作废]";
                    break;
                case 撤回:
                    contentStr = "操作[驳回]";
                    break;
                default:
                    break;
            }
            contentStr = "操作[调拨成功]";
            historyDto.setModifyContent(contentStr
                    + "调入公司[" + checkInCom.getDeptName() + "],"
                    + "调入部门[" + checkIn.getDeptName() + "],"
                    + "调出公司[" + checkOutCom.getDeptName() + "],"
                    + "调出部门[" + checkOut.getDeptName() + "]");
            if (!VGUtility.isEmpty(model.getCallInSavePlaceId()))
                historyDto.setChangeContent("移动至" + CallInSavePlaceStr);
            assetService.createHistory(historyDto);
        }


    }
}

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
import com.mine.product.czmtr.ram.asset.dao.AssetRevertDao;
import com.mine.product.czmtr.ram.asset.dto.*;
import com.mine.product.czmtr.ram.asset.model.AssetAssetModel;
import com.mine.product.czmtr.ram.asset.model.AssetBorrowModel;
import com.mine.product.czmtr.ram.asset.model.AssetBorrowTempModel;
import com.mine.product.czmtr.ram.asset.model.AssetRevertModel;
import com.mine.product.czmtr.ram.asset.service.IAssetBorrowService.ASSETBORROW_REVERTSTATUS;
import com.mine.product.czmtr.ram.asset.service.IAssetService.ASSET_STATUS;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.VGUtility;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.BeanUtils;
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
 * 资产归还管理
 *
 * @author rockh
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AssetRevertService implements IAssetRevertService {
    @Autowired
    private AssetRevertDao assetRevertDao;//资产归还
    @Autowired
    private AssetBorrowDao assetBorrowDao;//资产借用
    @Autowired
    private AssetBorrowTempDao assetBorrowTempDao;//资产借用子表dao
    @Autowired
    private AssetAssetDao assetDao; //资产
    @Autowired
    private IUserService userService;
    @Autowired
    private IAssetBorrowTempService assetBorrowTempService;
    @Autowired
    private IBaseService baseService;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private IAssetService assetService;

    // 通用检查
    @Override
    public void commonCheckAssetRevertDto(AssetRevertDto dto) {
        if (VGUtility.isEmpty(dto.getAssetrevertDepartmentID()))
            throw new RuntimeException("归还部门为必填项，不能为空！");
        else {
            try {
                baseService.getDeptInfo(dto.getAssetrevertDepartmentID());
            } catch (Exception ex) {
                throw new RuntimeException("归还部门格式不正确!");
            }
        }
        if (VGUtility.isEmpty(dto.getAssetrevertUserID()))
            throw new RuntimeException("归还人为必填项，不能为空！");
        else {
            try {
                userService.getUserInfo(dto.getAssetrevertUserID());
            } catch (Exception ex) {
                throw new RuntimeException("归还人格式不正确!");
            }
        }
        if (VGUtility.isEmpty(dto.getRealrevertTime()))
            throw new RuntimeException("归还日期为必填项,不能为空！");
        else {
            try {
                VGUtility.toDateObj(dto.getRealrevertTime(), "yyyy/M/d");
            } catch (Exception ex) {
                throw new RuntimeException("归还日期格式不正确！");
            }
        }
    }

    /**
     * 添加
     */
    @Override
    public AssetRevertDto createAssetRevert(String assetBorrowTempDtoList, AssetRevertDto dto, UserInfoDto userInfoDto) {
        commonCheckAssetRevertDto(dto);
        ArrayList<AssetBorrowTempDto> AssetBorrowTempDtoList = new ArrayList<AssetBorrowTempDto>();

        if (!VGUtility.isEmpty(assetBorrowTempDtoList))
            AssetBorrowTempDtoList = JSON.parseObject(assetBorrowTempDtoList, new TypeReference<ArrayList<AssetBorrowTempDto>>() {
            });
        else
            throw new RuntimeException("资产列表不能为空！");

        AssetRevertModel model = convertAssetRevertModel(dto);
        model.setCreateUserID(userInfoDto.getId());
        model.setAssetrevertCode(getAssetRevertCode());
        model.setReceiptStatus(FlowableInfo.WORKSTATUS .拟稿);
        AssetRevertModel assetRevertModel = assetRevertDao.save(model);
        assetBorrowTempService.createAssetRevertTempForList(AssetBorrowTempDtoList, assetRevertModel.getId());
        return convertModelToDto(assetRevertModel);
    }

    ;

    /**
     * 修改
     */
    @Override
    public AssetRevertDto updateAssetRevert(AssetRevertDto dto) {
        commonCheckAssetRevertDto(dto);

        if (VGUtility.isEmpty(dto.getAssetrevertCode()))
            throw new RuntimeException("归还编号不能为空！");
        AssetRevertModel model = assetRevertDao.findById(dto.getId()).get();
        if (!VGUtility.isEmpty(dto.getAssetrevertDepartmentID()))
            model.setAssetrevertDepartmentID(dto.getAssetrevertDepartmentID());
        if (!VGUtility.isEmpty(dto.getAssetrevertUserID()))
            model.setAssetrevertUserID(dto.getAssetrevertUserID());
        if (!VGUtility.isEmpty(dto.getRealrevertTime()))
            model.setRealrevertTime(VGUtility.toDateObj(dto.getRealrevertTime(), "yyyy/M/d"));
        if (!VGUtility.isEmpty(dto.getRemarks()))
            model.setRemarks(dto.getRemarks());

        model.setId(dto.getId());
        model.setAssetrevertCode(dto.getAssetrevertCode());
        model.setReceiptStatus(FlowableInfo.WORKSTATUS .拟稿);
        model.setCreateUserID(dto.getCreateUserID());
        model.setProduceType(dto.getProduceType());
        model = assetRevertDao.save(model);
        List<AssetBorrowTempDto> dtolist = assetBorrowTempService.getAssetBorrowTempDtoListByRevertId(model.getId());
        if (dtolist.size() <= 0)
            throw new RuntimeException("资产列表不能为空！");
        for (AssetBorrowTempDto temp : dtolist) {
            temp.setAssetRevertID(model.getId());
            temp.setNewUserID(model.getCreateUserID());
            assetBorrowTempService.updateAssetBorrowTemp(temp);
        }
        return convertModelToDto(model);
    }

    ;

    // 模拟发送审批
    @Override
    public void updateAssetRevertStatus(AssetRevertDto dto) {
        AssetRevertModel model = assetRevertDao.findById(dto.getId()).get();
        if (!VGUtility.isEmpty(dto.getReceiptStatus()))
            model.setReceiptStatus(dto.getReceiptStatus());
        assetRevertDao.save(model);

        List<AssetBorrowTempModel> tempModel = assetBorrowTempDao.findAssetBorrowTempModelsByAssetRevertModel(model);
        if (tempModel.size() <= 0)
            throw new RuntimeException("资产列表不能为空！");
        for (AssetBorrowTempModel temp : tempModel) {
            int num = 0;
            AssetAssetModel assetModel = assetDao.getOne(temp.getAssetId());
            assetModel.setAssetStatus(ASSET_STATUS.闲置);
            assetModel.setBeforeChangeAssetStatus(null);
            assetDao.save(assetModel);

            AssetBorrowModel borrowModel = assetBorrowDao.getOne(temp.getAssetBorrowModel().getId());
            List<AssetBorrowTempModel> list = assetBorrowTempDao.findAssetBorrowTempModelsByAssetBorrowModel(borrowModel);
            for (AssetBorrowTempModel borrowtemp : list) {
                if (!VGUtility.isEmpty(borrowtemp.getAssetRevertModel())) {
                    AssetRevertModel revert = assetRevertDao.getOne(borrowtemp.getAssetRevertModel().getId());
                    if (revert.getReceiptStatus() == FlowableInfo.WORKSTATUS.已审批)
                        num++;
                }
                if (num != list.size())
                    borrowModel.setRevertStatus(ASSETBORROW_REVERTSTATUS.部分归还);
                else
                    borrowModel.setRevertStatus(ASSETBORROW_REVERTSTATUS.已归还);

                assetBorrowDao.save(borrowModel);
            }
        }
    }

    ;

    /**
     * 删除
     */
    @Override
    public void deleteAssetRevert(String AssetRevertId) {
        List<AssetBorrowTempDto> dtolist = assetBorrowTempService.getAssetBorrowTempDtoListByRevertId(AssetRevertId);
        for (AssetBorrowTempDto temp : dtolist) {
//			AssetAssetDto asset = assetService.getAssetByAssetId(temp.getAssetId());
//			asset.setAssetStatusStr(String.valueOf(ASSET_STATUS.借出.ordinal()));
//			asset.setBeforeChangeAssetStatusStr(null);
//			assetService.updateAssetBeforeAssetStatus(asset);

            assetBorrowTempService.updateAssetBorrowTempBydDeleteAssetRevertModel(temp);
        }
        assetRevertDao.deleteById(AssetRevertId);
    }

    ;

    /**
     * 查询
     */
    @Override
    public AssetRevertDto getAssetRevertDto(String id) {
        AssetRevertModel model = assetRevertDao.findById(id).get();
        return convertModelToDto(model);
    }

    @Override
    public Map<String, Object> getAssetRevertDtoByQuerysForDataGrid(ISearchExpression searchExpression,
                                                                    PageableDto pageableDto) {
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<AssetRevertModel> assetRevertCriteria = builder.createQuery(AssetRevertModel.class);
        Root<AssetRevertModel> root = assetRevertCriteria.from(AssetRevertModel.class);
        Predicate predicate = (Predicate) searchExpression.change(root, builder);

        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            assetRevertCriteria.where((Predicate) predicate);

        assetRevertCriteria.orderBy(builder.desc(root.get("assetrevertCode")));
        Query<AssetRevertModel> query = session.createQuery(assetRevertCriteria);
        // 分页
        if (!VGUtility.isEmpty(pageableDto))
            query.setFirstResult((pageableDto.getPage() - 1) * pageableDto.getSize())
                    .setMaxResults(pageableDto.getSize());
        List<AssetRevertModel> modelList = query.getResultList();
        // total
        CriteriaQuery<Long> longCriteria = builder.createQuery(Long.class);
        root = longCriteria.from(AssetRevertModel.class);
        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            longCriteria.where((Predicate) predicate);
        longCriteria.select(builder.count(root));
        long total = session.createQuery(longCriteria).uniqueResult();
        // convert
        List<AssetRevertDto> dtoList = new ArrayList<AssetRevertDto>();
        for (AssetRevertModel model : modelList) {
            dtoList.add(convertAssetRevertTempStr(model));
        }
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtoList);
        modelMap.addAttribute("total", total);
        return modelMap;
    }

    /***
     * 资产归还列表查询
     */
    @Override
    public Map<String, Object> getAssetRevertByForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto, AssetRevertDto assetRevertDto) {
        int pagesize = pageableDto.getSize();
        int page = pageableDto.getPage();
        List<AssetRevertDto> dtoList = (List<AssetRevertDto>) getAssetRevertDtoByQuerysForDataGrid(searchExpression, null).get("rows");
        if (!VGUtility.isEmpty(assetRevertDto.getAssetCode()))
            dtoList = dtoList.stream().filter(a -> Objects.nonNull(a.getAssetCode()))
                    .filter(a -> a.getAssetCode().contains(assetRevertDto.getAssetCode()))
                    .collect(Collectors.toList());
        if (!VGUtility.isEmpty(assetRevertDto.getAssetRevertTempStr()))
            dtoList = dtoList.stream().filter(a -> Objects.nonNull(a.getAssetRevertTempStr()))
                    .filter(a -> a.getAssetRevertTempStr().contains(assetRevertDto.getAssetRevertTempStr()))
                    .collect(Collectors.toList());
        ModelMap modelMap = new ModelMap();
        if (dtoList.size() >= pagesize)
            modelMap.addAttribute("rows", dtoList.subList((page - 1) * pagesize, page * pagesize <= dtoList.size() ? page * pagesize : dtoList.size()));
        else
            modelMap.addAttribute("rows", dtoList);
        modelMap.addAttribute("total", dtoList.size());
        return modelMap;
    }

    public AssetRevertDto convertAssetRevertTempStr(AssetRevertModel model) {
        AssetRevertDto assetDto = convertModelToDto(model);
        List<AssetBorrowTempModel> resultList = assetBorrowTempDao.findByAssetRevertId(model.getId());
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
            assetDto.setAssetRevertTempStr(str.substring(1, str.length()));

        return assetDto;
    }

    // ********************Convert Start********************

    /**
     * 数据转换 convert model to dto convert dto to model
     */
    public AssetRevertModel convertAssetRevertModel(AssetRevertDto dto) {
        AssetRevertModel model = new AssetRevertModel();
        BeanUtils.copyProperties(dto, model);
        model.setRealrevertTime(VGUtility.toDateObj(dto.getRealrevertTime(), "yyyy/M/d"));
        return model;
    }

    private AssetRevertDto convertModelToDto(AssetRevertModel model) {
        AssetRevertDto dto = new AssetRevertDto();
        BeanUtils.copyProperties(model, dto);
        dto.setAssetrevertUserID2(model.getAssetrevertUserID());
        dto.setCreateTimestamp(VGUtility.toDateStr(model.getCreateTimestamp(), "yyyy-MM-dd"));
        dto.setRealrevertTime(VGUtility.toDateStr(model.getRealrevertTime(), "yyyy/M/d"));
        dto.setRealrevertTimeStr(VGUtility.toDateStr(model.getRealrevertTime(), "yyyy-MM-dd"));

        if (!VGUtility.isEmpty(model.getReceiptStatus()))
            dto.setReceiptIndex(model.getReceiptStatus().ordinal());
        if (!VGUtility.isEmpty(userService.getUserInfo(model.getCreateUserID())))
            dto.setCreateUserName(userService.getUserInfo(model.getCreateUserID()).getChsName());
        if (!VGUtility.isEmpty(userService.getUserInfo(model.getAssetrevertUserID())))
            dto.setAssetrevertUserName(userService.getUserInfo(model.getAssetrevertUserID()).getChsName());
        if (!VGUtility.isEmpty((DeptInfoDto) baseService.getDeptInfo(model.getAssetrevertDepartmentID())))
            dto.setAssetrevertDepartmentName(((DeptInfoDto) baseService.getDeptInfo(model.getAssetrevertDepartmentID())).getDeptName());
        return dto;
    }

    // 生成借用编号
    @Override
    public String getAssetRevertCode() {
        Date time = Calendar.getInstance().getTime();
        String date = VGUtility.toDateStr(time, "yyyy-MM-dd");
        String hql = "from AssetRevertModel where to_char(createtimestamp,'yyyy-MM-dd')='" + date + "' order by assetRevertCode desc";
        TypedQuery<AssetRevertModel> query = entityManager.createQuery(hql, AssetRevertModel.class);

        List<AssetRevertModel> modelList = query.getResultList();
        String code = "";
        // SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        if (modelList.size() > 0) {
            if (!VGUtility.isEmpty(modelList.get(0).getAssetrevertCode())) {
                code = modelList.get(0).getAssetrevertCode();
                Long num = Long.parseLong(code.substring(2)) + 1;
                code = code.substring(0, 2) + num;
            } else {
                code = "GH" + date.replace("-", "") + "001";
            }
        } else {
            code = "GH" + date.replace("-", "") + "001";
        }

        return code;
    }

    @Override
    public AssetApprove doApproveNotify(String id) {
        List<String> assetIdList = assetBorrowTempService.getAssetIdListByAssetReverId(id);
        //校验资产状态，是否可以审批；
        return assetService.doAssetApproveCheck(assetIdList, ASSET_STATUS.闲置);
    }

    @Override
    public void approveUpdateNotify(FlowableInfo.WORKSTATUS workStatus, String id) {
        List<String> assetIdList;
        switch (workStatus) {
            case 审批中:
                //资产冻结
                assetIdList = assetBorrowTempService.getAssetIdListByAssetReverId(id);
                assetService.LockAssetState(assetIdList);
                break;
            case 驳回:
                //资产状态回滚
                assetIdList = assetBorrowTempService.getAssetIdListByAssetReverId(id);
                assetService.approveFailureAssetStateRollback(assetIdList);
                break;
            case 已审批:
                //将资产状态落地
                AssetRevertModel model = assetRevertDao.findById(id).get();
                assetIdList = assetBorrowTempService.getAssetIdListByAssetReverId(id);
                assetService.approveSuccessUpdateAssetState(assetIdList, ASSET_STATUS.闲置);
                this.createHistory(model, assetIdList);
                break;
            default:
                break;

        }
        //更新主表状态
        AssetRevertModel model = assetRevertDao.findById(id).get();
        model.setReceiptStatus(workStatus);

    }

    private void createHistory(AssetRevertModel model, List<String> assetIdList) {
        DeptInfoDto revertDepartment = new DeptInfoDto();
        UserInfoDto revertUser = new UserInfoDto();
        if (!VGUtility.isEmpty(model.getAssetrevertDepartmentID())) {
            revertDepartment = (DeptInfoDto) baseService.getDeptInfo(model.getAssetrevertDepartmentID());
        }
        if (!VGUtility.isEmpty(model.getAssetrevertUserID())) {
            revertUser = userService.getUserInfo(model.getAssetrevertUserID());
        }
        for (String assetId : assetIdList) {
            AssetHistoryDto historyDto = new AssetHistoryDto();
            historyDto.setHistoryType(IAssetService.HISTORY_TYPE.变更记录);
            historyDto.setChangeType(String.valueOf(IAssetService.CHANGE_TYPE.资产借用归还.ordinal()));
            historyDto.setAssetModelId(assetId);
            historyDto.setCreateUserId(model.getCreateUserID());
            historyDto.setChangeContent("操作[创建资产借用归还单]"
                    + "[" + revertDepartment.getDeptName() + "]"
                    + revertUser.getChsName() + "归还");
            assetService.createHistory(historyDto);
        }

    }
}

package com.mine.product.czmtr.ram.asset.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mine.base.user.dto.DeptInfoDto;
import com.mine.base.user.dto.UserInfoDto;
import com.mine.base.user.service.IUserService;
import com.mine.platform.common.dto.PageableDto;
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
import com.mine.product.czmtr.ram.asset.service.IAssetReceiveUseService.ASSETRECEIVEUSE_REVERTSTATUS;
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
 * 资产归归还管理
 *
 * @author rockh
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AssetReceiveRevertService implements IAssetReceiveRevertService {
    @Autowired
    private AssetReceiveRevertDao assetReceiveRevertDao;
    @Autowired
    private AssetReceiveUseTempDao useTempDao;
    @Autowired
    private AssetReceiveUseDao userDao;
    @Autowired
    private AssetAssetDao assetDao; // 资产
    @Autowired
    private IUserService userService;
    @Autowired
    private IAssetReceiveUseTempService assetReceiveUseTempService;
    @Autowired
    private IAssetService assetService;
    @Autowired
    private IBaseService baseService;
    @PersistenceContext
    private EntityManager entityManager;

    // 通用检查
    @Override
    public void commonCheckAssetReceiveRevertDto(AssetReceiveRevertDto dto) {
        if (VGUtility.isEmpty(dto.getAssetReceiveRevertDepartmentID()))
            throw new RuntimeException("归还部门为必填项，不能为空！");
        else {
            try {
                baseService.getDeptInfo(dto.getAssetReceiveRevertDepartmentID());
            } catch (Exception ex) {
                throw new RuntimeException("归还部门格式不正确");
            }
        }
        if (VGUtility.isEmpty(dto.getAssetReceiveRevertUserID()))
            throw new RuntimeException("归还人为必填项，不能为空！");
        else {
            try {
                userService.getUserInfo(dto.getAssetReceiveRevertUserID());
            } catch (Exception ex) {
                throw new RuntimeException("归还人格式不正确！");
            }
        }
        if (VGUtility.isEmpty(dto.getRealrevertTime()))
            throw new RuntimeException("归还日期为必填项，不能为空！");
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
    public AssetReceiveRevertDto createAssetReceiveRevert(String assetReceiveUseTempDtoList, AssetReceiveRevertDto dto, UserInfoDto userInfoDto) {
        commonCheckAssetReceiveRevertDto(dto);
        ArrayList<AssetReceiveUseTempDto> AssetReceiveUseTempDtoList = new ArrayList<AssetReceiveUseTempDto>();

        if ((!VGUtility.isEmpty(assetReceiveUseTempDtoList)))
            AssetReceiveUseTempDtoList = JSON.parseObject(assetReceiveUseTempDtoList, new TypeReference<ArrayList<AssetReceiveUseTempDto>>() {
            });
        else
            throw new RuntimeException("资产列表不能为空！");

        AssetReceiveRevertModel model = convertAssetReceiveRevertModel(dto);
        model.setCreateUserID(userInfoDto.getId());
        model.setAssetReceiveRevertCode(getAssetReceiveRevertCode());
        model.setReceiptStatus(FlowableInfo.WORKSTATUS.拟稿);
        model = assetReceiveRevertDao.save(model);
        assetReceiveUseTempService.createAssetReceiveRevertTempForList(AssetReceiveUseTempDtoList, model.getId());
        return convertModelToDto(model);
    }

    ;

    /**
     * 修改
     */
    @Override
    public AssetReceiveRevertDto updateAssetReceiveRevert(AssetReceiveRevertDto dto) {
        commonCheckAssetReceiveRevertDto(dto);

        if (VGUtility.isEmpty(dto.getAssetReceiveRevertCode()))
            throw new RuntimeException("归还编号不能为空！");

        AssetReceiveRevertModel model = assetReceiveRevertDao.findById(dto.getId()).get();
        if (!VGUtility.isEmpty(dto.getAssetReceiveRevertDepartmentID()))
            model.setAssetReceiveRevertDepartmentID(dto.getAssetReceiveRevertDepartmentID());
        if (!VGUtility.isEmpty(dto.getAssetReceiveRevertUserID()))
            model.setAssetReceiveRevertUserID(dto.getAssetReceiveRevertUserID());
        if (!VGUtility.isEmpty(dto.getRealrevertTime()))
            model.setRealrevertTime(VGUtility.toDateObj(dto.getRealrevertTime(), "yyyy/M/d"));
        if (!VGUtility.isEmpty(dto.getRemarks()))
            model.setRemarks(dto.getRemarks());

        model.setProduceType(dto.getProduceType());
        model.setId(dto.getId());
        model.setAssetReceiveRevertCode(dto.getAssetReceiveRevertCode());
        model.setReceiptStatus(FlowableInfo.WORKSTATUS.拟稿);
        model.setCreateUserID(dto.getCreateUserID());
        model = assetReceiveRevertDao.save(model);
        List<AssetReceiveUseTempDto> dtolist = assetReceiveUseTempService.getAssetReceiveUseTempDtoList(model.getId());
        if (!VGUtility.isEmpty(dtolist)) {
            for (AssetReceiveUseTempDto temp : dtolist) {
                temp.setAssetReceiveRevertID(model.getId());
                temp.setNewUserID(model.getCreateUserID());
                assetReceiveUseTempService.updateAssetReceiveUseTemp(temp);
            }
        }
        return convertModelToDto(model);
    }

    ;

    // 模拟发送审批
    @Override
    public void updateAssetReceiveRevertStatus(AssetReceiveRevertDto dto) {
        AssetReceiveRevertModel model = assetReceiveRevertDao.findById(dto.getId()).get();
        if (!VGUtility.isEmpty(dto.getReceiptStatus())) {
            model.setReceiptStatus(dto.getReceiptStatus());
        }
        assetReceiveRevertDao.save(model);
        List<AssetReceiveUseTempModel> tempModel = useTempDao.findAssetReceiveUseTempModelsByAssetReceiveRevertModel(model);
        if (tempModel.size() <= 0)
            throw new RuntimeException("资产列表不能为空！");
        for (AssetReceiveUseTempModel temp : tempModel) {
            int num = 0;
            AssetAssetModel assetModel = assetDao.getOne(temp.getAssetId());
            assetModel.setAssetStatus(ASSET_STATUS.闲置);
            assetModel.setBeforeChangeAssetStatus(null);
            assetDao.save(assetModel);

            //查询资产领用单数据
            AssetReceiveUseModel assetReceiveUse = userDao.getOne(temp.getAssetReceiveUseModel().getId());
            //根据资产领用单,查询资产领用单资产列表
            List<AssetReceiveUseTempModel> list = useTempDao.findAssetReceiveUseTempModelsByAssetReceiveUseModel(assetReceiveUse);
            for (AssetReceiveUseTempModel usetempdto : list) {
                if (!VGUtility.isEmpty(usetempdto.getAssetReceiveRevertModel())) {
                    AssetReceiveRevertModel revert = assetReceiveRevertDao.getOne(usetempdto.getAssetReceiveRevertModel().getId());
                    if (revert.getReceiptStatus() == FlowableInfo.WORKSTATUS.已审批)
                        num++;
                }
                if (num != list.size())
                    assetReceiveUse.setRevertStatus(ASSETRECEIVEUSE_REVERTSTATUS.部分归还);
                else
                    assetReceiveUse.setRevertStatus(ASSETRECEIVEUSE_REVERTSTATUS.已归还);

                userDao.save(assetReceiveUse);
            }
        }

    }

    ;

    /**
     * 删除
     */
    @Override
    public void deleteAssetReceiveRevert(String AssetReceiveRevertId) {
        List<AssetReceiveUseTempDto> dtolist = assetReceiveUseTempService.getAssetReceiveUseTempDtoListByRevertId(AssetReceiveRevertId);
        for (AssetReceiveUseTempDto temp : dtolist) {
//			AssetAssetDto asset = assetService.getAssetByAssetId(temp.getAssetId());
//			asset.setAssetStatusStr(String.valueOf(ASSET_STATUS.使用.ordinal()));
//			asset.setBeforeChangeAssetStatusStr(null);
//			assetService.updateAssetBeforeAssetStatus(asset);

            assetReceiveUseTempService.updateAssetReceiveUseTempByDeleteAssetReceiveRevertModel(temp);
        }
        assetReceiveRevertDao.deleteById(AssetReceiveRevertId);
    }

    ;

    /**
     * 查询:根据资产归还单(资产领用)的ID,查询归还单信息
     */
    @Override
    public AssetReceiveRevertDto getAssetReceiveRevertDto(String id) {
        AssetReceiveRevertModel model = assetReceiveRevertDao.findById(id).get();
        return convertModelToDto(model);
    }

    @Override
    public Map<String, Object> getAssetReceiveRevertDtoByQuerysForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto) {
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<AssetReceiveRevertModel> assetReceiveRevertCriteria = builder.createQuery(AssetReceiveRevertModel.class);
        Root<AssetReceiveRevertModel> root = assetReceiveRevertCriteria.from(AssetReceiveRevertModel.class);
        Predicate predicate = (Predicate) searchExpression.change(root, builder);

        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            assetReceiveRevertCriteria.where((Predicate) predicate);

        assetReceiveRevertCriteria.orderBy(builder.desc(root.get("assetReceiveRevertCode")));
        Query<AssetReceiveRevertModel> query = session.createQuery(assetReceiveRevertCriteria);
        // 分页
        if (!VGUtility.isEmpty(pageableDto))
            query.setFirstResult((pageableDto.getPage() - 1) * pageableDto.getSize())
                    .setMaxResults(pageableDto.getSize());
        List<AssetReceiveRevertModel> modelList = query.getResultList();
        //total
        CriteriaQuery<Long> longCriteria = builder.createQuery(Long.class);
        root = longCriteria.from(AssetReceiveRevertModel.class);
        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            longCriteria.where((Predicate) predicate);
        longCriteria.select(builder.count(root));
        long total = session.createQuery(longCriteria).uniqueResult();
        //convert
        List<AssetReceiveRevertDto> dtoList = new ArrayList<AssetReceiveRevertDto>();
        for (AssetReceiveRevertModel model : modelList) {
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
    public Map<String, Object> getAssetReceiveRevertByForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto, AssetReceiveRevertDto assetReceiveRevertDto) {
        int pagesize = pageableDto.getSize();
        int page = pageableDto.getPage();
        List<AssetReceiveRevertDto> dtoList = (List<AssetReceiveRevertDto>) getAssetReceiveRevertDtoByQuerysForDataGrid(searchExpression, null).get("rows");
        if (!VGUtility.isEmpty(assetReceiveRevertDto.getAssetCode()))
            dtoList = dtoList.stream().filter(a -> Objects.nonNull(a.getAssetCode()))
                    .filter(a -> a.getAssetCode().contains(assetReceiveRevertDto.getAssetCode()))
                    .collect(Collectors.toList());
        if (!VGUtility.isEmpty(assetReceiveRevertDto.getAssetReceiveRevertTempStr()))
            dtoList = dtoList.stream().filter(a -> Objects.nonNull(a.getAssetReceiveRevertTempStr()))
                    .filter(a -> a.getAssetReceiveRevertTempStr().contains(assetReceiveRevertDto.getAssetReceiveRevertTempStr()))
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
     * 根据 资产归还的Model转换成dto,包含资产归还的资产信息,以及资产编码
     * @param model
     * @return
     */
    public AssetReceiveRevertDto convertAssetRevertTempStr(AssetReceiveRevertModel model) {
        AssetReceiveRevertDto assetDto = convertModelToDto(model);
        String str = "";
        String assetCodestr = "";
        List<AssetReceiveUseTempModel> resultList = useTempDao.findAssetReceiveUseTempModelsByAssetReceiveRevertModel(model);
        for (AssetReceiveUseTempModel temp : resultList) {
            AssetAssetModel assetModel = assetDao.findAssetAssetModelById(temp.getAssetId());
            if (!VGUtility.isEmpty(assetModel)) {
                assetCodestr += "," + assetModel.getAssetCode();
                str += "," + baseService.getAssetNameByMaterialCode(assetModel.getMaterialCode());
            }
        }
        if (!VGUtility.isEmpty(assetCodestr))
            assetDto.setAssetCode(assetCodestr.substring(1, assetCodestr.length()));
        if (!VGUtility.isEmpty(str))
            assetDto.setAssetReceiveRevertTempStr(str.substring(1, str.length()));

        return assetDto;
    }

    // ********************Convert Start********************

    /**
     * 数据转换 convert model to dto convert dto to model
     */
    public AssetReceiveRevertModel convertAssetReceiveRevertModel(AssetReceiveRevertDto dto) {
        AssetReceiveRevertModel model = new AssetReceiveRevertModel();
        // 基本信息
        BeanUtils.copyProperties(dto, model);
        model.setRealrevertTime(VGUtility.toDateObj(dto.getRealrevertTime(), "yyyy/M/d"));
        return model;
    }

    private AssetReceiveRevertDto convertModelToDto(AssetReceiveRevertModel model) {
        AssetReceiveRevertDto dto = new AssetReceiveRevertDto();
        BeanUtils.copyProperties(model, dto);
        dto.setCreateTimestamp(VGUtility.toDateStr(model.getCreateTimestamp(), "yyyy-MM-dd"));
        dto.setRealrevertTime(VGUtility.toDateStr(model.getRealrevertTime(), "yyyy/M/d"));
        dto.setRealrevertTimeStr(VGUtility.toDateStr(model.getRealrevertTime(), "yyyy-MM-dd"));
        if (!VGUtility.isEmpty(model.getReceiptStatus()))
            dto.setReceiptIndex(model.getReceiptStatus().ordinal());
        if (!VGUtility.isEmpty(userService.getUserInfo(model.getCreateUserID())))
            dto.setCreateUserName(userService.getUserInfo(model.getCreateUserID()).getChsName());
        if (!VGUtility.isEmpty(userService.getUserInfo(model.getAssetReceiveRevertUserID())))
            dto.setAssetReceiveRevertUserName(userService.getUserInfo(model.getAssetReceiveRevertUserID()).getChsName());
        if (!VGUtility.isEmpty((DeptInfoDto) baseService.getDeptInfo(model.getAssetReceiveRevertDepartmentID())))
            dto.setAssetReceiveRevertDepartmentName(((DeptInfoDto) baseService.getDeptInfo(model.getAssetReceiveRevertDepartmentID())).getDeptName());
        return dto;
    }

    // 生成领用编号
    @Override
    public String getAssetReceiveRevertCode() {
        Date time = Calendar.getInstance().getTime();
        String date = VGUtility.toDateStr(time, "yyyy-MM-dd");
        String hql = "from AssetReceiveRevertModel where to_char(createtimestamp,'yyyy-MM-dd')='" + date + "' order by assetReceiveRevertCode desc";
        TypedQuery<AssetReceiveRevertModel> query = entityManager.createQuery(hql, AssetReceiveRevertModel.class);

        List<AssetReceiveRevertModel> modelList = query.getResultList();
        String code = "";
        if (modelList.size() > 0) {
            if (!VGUtility.isEmpty(modelList.get(0).getAssetReceiveRevertCode())) {
                code = modelList.get(0).getAssetReceiveRevertCode();
                Long num = Long.parseLong(code.substring(4)) + 1;
                code = code.substring(0, 4) + num;
            } else {
                code = "LYGH" + date.replace("-", "") + "001";
            }
        } else {
            code = "LYGH" + date.replace("-", "") + "001";
        }

        return code;
    }

    @Override
    public AssetApprove doApproveNotify(String id) {
        List<String> assetIdList = assetReceiveUseTempService.getAssetIdListByReceiveRevertId(id);
        //校验资产状态，是否可以审批；
        return assetService.doAssetApproveCheck(assetIdList, ASSET_STATUS.闲置);
    }


    @Override
    public void approveUpdateNotify(FlowableInfo.WORKSTATUS workStatus, String id) {
        List<String> assetIdList;
        switch (workStatus) {
            case 审批中:
                //资产冻结
                assetIdList = assetReceiveUseTempService.getAssetIdListByReceiveRevertId(id);
                assetService.LockAssetState(assetIdList);
                break;
            case 驳回:
                //资产状态回滚
                assetIdList = assetReceiveUseTempService.getAssetIdListByReceiveRevertId(id);
                assetService.approveFailureAssetStateRollback(assetIdList);
                break;
            case 已审批:
                //将资产状态落地
                AssetReceiveRevertModel model = assetReceiveRevertDao.findById(id).get();
                assetIdList = assetReceiveUseTempService.getAssetIdListByReceiveRevertId(id);
                assetService.approveSuccessUpdateAssetState(assetIdList, ASSET_STATUS.闲置);
                this.createHistory(model,assetIdList);
                break;
            default:
                break;

        }
        //更新主表状态
        AssetReceiveRevertModel model = assetReceiveRevertDao.findById(id).get();
        model.setReceiptStatus(workStatus);

    }

    private void createHistory(AssetReceiveRevertModel model, List<String> assetIdList) {
        DeptInfoDto receiveRevertDepartment = new DeptInfoDto();
        UserInfoDto receiveRevertUser = new UserInfoDto();
        if (!VGUtility.isEmpty(model.getAssetReceiveRevertDepartmentID())) {
            receiveRevertDepartment = (DeptInfoDto) baseService.getDeptInfo(model.getAssetReceiveRevertDepartmentID());
        }
        if (!VGUtility.isEmpty(model.getAssetReceiveRevertUserID())) {
            receiveRevertUser = userService.getUserInfo(model.getAssetReceiveRevertUserID());
        }
        for (String assetId : assetIdList) {
            AssetHistoryDto historyDto = new AssetHistoryDto();
            historyDto.setHistoryType(IAssetService.HISTORY_TYPE.变更记录);
            historyDto.setChangeType(String.valueOf(IAssetService.CHANGE_TYPE.资产领用归还.ordinal()));
            historyDto.setAssetModelId(assetId);
            historyDto.setCreateUserId(model.getCreateUserID());
            historyDto.setChangeContent("操作[资产领用归还]"
                    + "[" + receiveRevertDepartment.getDeptName() + "]"
                    + receiveRevertUser.getChsName() + "归还");
            assetService.createHistory(historyDto);
        }

    }
}

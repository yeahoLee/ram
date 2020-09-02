package com.mine.product.czmtr.ram.asset.service;

import com.mine.base.user.dto.DeptInfoDto;
import com.mine.base.user.service.IUserService;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dao.AssetAssetDao;
import com.mine.product.czmtr.ram.asset.dao.AssetReduceDao;
import com.mine.product.czmtr.ram.asset.dao.AssetReduceTempDao;
import com.mine.product.czmtr.ram.asset.dto.*;
import com.mine.product.czmtr.ram.asset.model.AssetAssetModel;
import com.mine.product.czmtr.ram.asset.model.AssetReduceModel;
import com.mine.product.czmtr.ram.asset.model.AssetReduceTempModel;
import com.mine.product.czmtr.ram.asset.service.IAssetService.HISTORY_TYPE;
import com.mine.product.czmtr.ram.asset.service.IAssetService.REDUCE_TYPE;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.MineSecureUtility;
import com.vgtech.platform.common.utility.SpringSecureUserInfo;
import com.vgtech.platform.common.utility.VGUtility;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;

/***
 * 资产减损管理
 * @author guoli.sun
 *
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AssetReduceService implements IAssetReduceService {
    @Autowired
    private AssetReduceDao reduceDao; //管理员变更
    @Autowired
    private AssetReduceTempDao reduceTempDao;// 管理员变更临时
    @Autowired
    private AssetAssetDao assetAssetDao; //资产管理
    @Autowired
    private AssetAssetDao assetDao; // 资产
    @Autowired
    private IUserService userService;
    @Autowired
    private IBaseService baseService;
    @Autowired
    private IAssetService assetService;
    @PersistenceContext
    private EntityManager entityManager;

    /***
     * 保存
     */
    @Override
    public AssetReduceDto saveReduce(AssetReduceDto dto) {
        assetReduceDtoCommonCheck(dto);
        List<String> assetIdList = new ArrayList<String>();
        Arrays.stream(dto.getAssetIdListStr().split(",")).forEach(arr -> assetIdList.add(arr));
        AssetReduceModel model = new AssetReduceModel();
        model.setOrderName(dto.getOrderName());
        model.setReduceType(IAssetService.REDUCE_TYPE.values()[VGUtility.toInteger(dto.getReduceType())]);
        model.setSurplusValue(VGUtility.toDouble("100.00"));// TODO 待与财务接口对接，获取残余价值
        model.setProcessingCost(VGUtility.toDouble(dto.getProcessingCost()));
        model.setActualLoss(VGUtility.toDouble(dto.getActualLoss()));
        model.setReason(dto.getReason());
        model.setProposedDisposal(dto.getProposedDisposal());
        model.setReceiptStatus(FlowableInfo.WORKSTATUS.拟稿);
        model.setCreateUserId(((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo().getId());
        model.setProduceType(dto.getProduceType());

        String changeNum = "JS-BF-";
        //目前是写死匹配，可以改为获取汉字首字母
        if (REDUCE_TYPE.丢失 == (model.getReduceType()))
            changeNum = "JS-DS-";
        if (REDUCE_TYPE.捐出 == (model.getReduceType()))
//            changeNum = "JS-JZ-";
            changeNum = "JS-JC-";
        if (REDUCE_TYPE.盘亏 == (model.getReduceType()))
            changeNum = "JS-PK-";

        changeNum += VGUtility.toDateStr(new Date(), "yyyyMMdd");
        model.setChangeNum(getMaxChangeNum(changeNum));
        AssetReduceModel save = reduceDao.save(model);
        for (int i = 0; i < assetIdList.size(); i++) {
            AssetReduceTempModel tempModel = new AssetReduceTempModel();
            tempModel.setAssetId(assetIdList.get(i));
            tempModel.setAssetReduceModelId(model.getId());
            tempModel.setIsSysn(false);
            //发起审批后,进入审批流程中,会冻结资产
            AssetAssetDto asset = assetService.getAssetByAssetId(assetIdList.get(i));
            asset.setBeforeChangeAssetStatusStr(asset.getAssetStatusStr());
            assetService.updateAssetBeforeAssetStatus(asset);

            AssetHistoryDto historyDto = new AssetHistoryDto();
            historyDto.setHistoryType(HISTORY_TYPE.减少记录);
            historyDto.setReduceType(String.valueOf(model.getReduceType().ordinal()));
            historyDto.setAssetModelId(tempModel.getAssetId());
            historyDto.setCreateUserId(model.getCreateUserId());
            assetService.createHistory(historyDto);

            reduceTempDao.save(tempModel);
        }

        AssetReduceDto assetReduceDto = new AssetReduceDto();
        assetReduceDto.setId(save.getId());
        assetReduceDto.setChangeNum(save.getChangeNum());
        assetReduceDto.setOrderName(save.getOrderName());
        return assetReduceDto;

    }

    /***
     * 管理员变更-保存&发起审批
     */
    @Override
    public void saveAndCheckReduce(AssetReduceDto dto) {
        //字段校验
        assetReduceDtoCommonCheck(dto);
        List<String> assetIdList = new ArrayList<String>();
        Arrays.stream(dto.getAssetIdListStr().split(",")).forEach(arr -> assetIdList.add(arr));
        AssetReduceModel model = new AssetReduceModel();
        model.setOrderName(dto.getOrderName());
        model.setReduceType(IAssetService.REDUCE_TYPE.values()[VGUtility.toInteger(dto.getReduceType())]);
        model.setSurplusValue(VGUtility.toDouble("100.00"));// TODO 待与财务接口对接，获取残余价值
        model.setProcessingCost(VGUtility.toDouble(dto.getProcessingCost()));
        model.setActualLoss(VGUtility.toDouble(dto.getActualLoss()));
        model.setReason(dto.getReason());
        model.setProposedDisposal(dto.getProposedDisposal());
        model.setReceiptStatus(FlowableInfo.WORKSTATUS.已审批);
        model.setCreateUserId(((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo().getId());
        String changeNum = "JS-BF-";
        //目前是写死匹配，可以改为获取汉字首字母
        if (REDUCE_TYPE.丢失 == (model.getReduceType()))
            changeNum = "JS-DS-";
        if (REDUCE_TYPE.捐出 == (model.getReduceType()))
//            changeNum = "JS-JZ-";
            changeNum = "JS-JC-";
        if (REDUCE_TYPE.盘亏 == (model.getReduceType()))
            changeNum = "JS-PK-";
        changeNum += VGUtility.toDateStr(new Date(), "yyyyMMdd");
        model.setChangeNum(getMaxChangeNum(changeNum));
        reduceDao.save(model);
        for (int i = 0; i < assetIdList.size(); i++) {
            AssetReduceTempModel tempModel = new AssetReduceTempModel();
            tempModel.setAssetId(assetIdList.get(i));
            tempModel.setAssetReduceModelId(model.getId());

            //发起审批后,进入审批流程中,会冻结资产
            AssetAssetDto asset = assetService.getAssetByAssetId(assetIdList.get(i));
            asset.setBeforeChangeAssetStatusStr(asset.getAssetStatusStr());
            assetService.updateAssetBeforeAssetStatus(asset);

            AssetHistoryDto historyDto = new AssetHistoryDto();
            historyDto.setHistoryType(HISTORY_TYPE.减少记录);
            historyDto.setReduceType(String.valueOf(model.getReduceType().ordinal()));
            historyDto.setAssetModelId(assetIdList.get(i));
            historyDto.setCreateUserId(model.getCreateUserId());
            assetService.createHistory(historyDto);

            reduceTempDao.save(tempModel);
        }
        //发起审批
        checkReduceReceipt(model.getId());
    }

    /***
     * 相关字段校验
     * @param dto
     */
    private void assetReduceDtoCommonCheck(AssetReduceDto dto) {
        if (VGUtility.isEmpty(dto.getOrderName()))
            throw new RuntimeException("资产减损单名称不能为空！");
        if (VGUtility.isEmpty(dto.getReduceType()))
            throw new RuntimeException("资产减损类型不能为空！");
        if (VGUtility.isEmpty(dto.getProcessingCost()))
            throw new RuntimeException("处理费用不能为空！");
        if (VGUtility.isEmpty(dto.getActualLoss()))
            throw new RuntimeException("实际损失不能为空！");
        if (VGUtility.isEmpty(dto.getReason()))
            throw new RuntimeException("减损原因不能为空！");
        if (VGUtility.isEmpty(dto.getProposedDisposal()))
            throw new RuntimeException("拟处置方法不能为空！");
        if (VGUtility.isEmpty(dto.getAssetIdListStr()))
            throw new RuntimeException("资产列表不能为空！");
    }

    /***
     * 获取列表数据
     */
    @Override
    public Map<String, Object> getAssetReduceForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto) {
        //通过hibernate获得Session
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        //通过session获得标准生成器 CriteriaBuilder
        CriteriaBuilder builder = session.getCriteriaBuilder();
        //标准化对象查询
        CriteriaQuery<AssetReduceModel> assetCriteria = builder.createQuery(AssetReduceModel.class);
        //获取根
        Root<AssetReduceModel> root = assetCriteria.from(AssetReduceModel.class);
        Predicate predicate = (Predicate) searchExpression.change(root, builder);
        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            assetCriteria.where((Predicate) predicate);
        //根据查询器添加顺序
        Order order = builder.asc(root.get("createTimestamp"));
        assetCriteria.orderBy(order);
        //创建查询器
        Query<AssetReduceModel> query = session.createQuery(assetCriteria);
        //分页
        if (!VGUtility.isEmpty(pageableDto))
            query.setFirstResult((pageableDto.getPage() - 1) * pageableDto.getSize()).setMaxResults(pageableDto.getSize());
        List<AssetReduceModel> modelList = query.getResultList();
        //convert
        List<AssetReduceDto> dtoList = new ArrayList<AssetReduceDto>();
        for (AssetReduceModel model : modelList) {
            AssetReduceDto dto = convertReduceModelToDto(model);
            dtoList.add(dto);
        }
        //total
        CriteriaQuery<Long> longCriteria = builder.createQuery(Long.class);
        root = longCriteria.from(AssetReduceModel.class);
        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            longCriteria.where((Predicate) predicate);
        longCriteria.select(builder.count(root));
        long total = session.createQuery(longCriteria).uniqueResult();

        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtoList);
        modelMap.addAttribute("total", total);
        return modelMap;
    }

    private AssetReduceDto convertReduceModelToDto(AssetReduceModel model) {
        AssetReduceDto dto = new AssetReduceDto();
        dto.setProduceType(model.getProduceType());
        dto.setId(model.getId());
        dto.setOrderName(model.getOrderName());
        dto.setReduceType(model.getReduceType().ordinal() + "");
        dto.setReduceTypeStr(model.getReduceType().name());
        dto.setSurplusValue(VGUtility.toDoubleStr(model.getSurplusValue(), "0.00"));//保留两位小数
        dto.setProcessingCost(VGUtility.toDoubleStr(model.getProcessingCost(), "0.00"));//保留两位小数 "{0:F2}"
        dto.setActualLoss(VGUtility.toDoubleStr(model.getActualLoss(), "0.00"));//保留两位小数
        dto.setProposedDisposal(model.getProposedDisposal());
        dto.setCreateUserId(model.getCreateUserId());
        dto.setCreateUserIdStr(userService.getUserInfo(model.getCreateUserId()).getChsName());
        dto.setReason(model.getReason());
        dto.setChangeNum(model.getChangeNum());
        dto.setReceiptStatus(model.getReceiptStatus().ordinal() + "");
        dto.setReceiptStatusStr(model.getReceiptStatus().name());
        dto.setCreateTimestamp(VGUtility.toDateStr(model.getCreateTimestamp(), "yyyy-MM-dd"));
        String hql = "from AssetReduceTempModel where 1=1 ";
        if (!VGUtility.isEmpty(model.getId())) {
            hql += "and assetReduceModelId = " + "'" + model.getId() + "'";
        }
        TypedQuery<AssetReduceTempModel> query = entityManager.createQuery(hql, AssetReduceTempModel.class);
        List<AssetReduceTempModel> resultList = query.getResultList();
        String assetChange = "";
        List<String> assetIdList = new ArrayList<String>();
        dto.setAssetNum(VGUtility.debugToString(resultList.size()));//资产数量
        for (AssetReduceTempModel tempModel : resultList) {
            //有问题
            AssetAssetModel assetModel = assetDao.findAssetAssetModelById(tempModel.getAssetId());
            assetIdList.add(tempModel.getAssetId());
            if (!VGUtility.isEmpty(assetModel))
                assetChange += "、" + baseService.getAssetNameByMaterialCode(assetModel.getMaterialCode());
        }
        if (!VGUtility.isEmpty(assetChange))
            assetChange = assetChange.substring(1);
        dto.setAssetChange(assetChange);
        dto.setAssetIdList(assetIdList);
        String assetIdListStr = assetIdList.toString();
        assetIdListStr = assetIdListStr.substring(1, assetIdListStr.length() - 1);
        dto.setAssetIdListStr(assetIdListStr);
        return dto;
    }

    /***
     * 获取单号
     * @param changeNum
     * @return
     */
    public String getMaxChangeNum(String changeNum) {
        String maxChangeNum = reduceDao.getMaxChangeNum(changeNum + "%");
        if (VGUtility.isEmpty(maxChangeNum)) {
            return changeNum + "001";
        } else {
            String newNum = "";
            String num = maxChangeNum.substring(maxChangeNum.length() - 3, maxChangeNum.length());
            String n = String.valueOf((VGUtility.toInteger(num) + 1));
            if (n.length() == 1)
                newNum = "00" + n;
            else if (n.length() == 2)
                newNum = "0" + n;
            else
                newNum = n;
            return changeNum + newNum;
        }
    }

    /***
     * 删除
     */
    @Override
    public void deleteReduceReceipt(String id) {
        if (VGUtility.isEmpty(reduceDao.findById(id))) {
            throw new RuntimeException("该资产减损申请单不存在！");
        } else {
            AssetReduceModel ReduceModel = reduceDao.findById(id).get();
            reduceDao.deleteById(id);
            List<AssetReduceTempModel> list = reduceTempDao.findByAssetReduceModelId(id);
            if (list.size() > 0) {
                for (AssetReduceTempModel model : list) {
                    //修改资产原有状态
                    AssetAssetDto asset = assetService.getAssetByAssetId(model.getAssetId());
                    asset.setAssetStatusStr(asset.getBeforeChangeAssetStatusStr());
                    asset.setBeforeChangeAssetStatusStr(null);
                    assetService.updateAssetBeforeAssetStatus(asset);

                    AssetHistoryDto historyDto = new AssetHistoryDto();
                    historyDto.setHistoryType(HISTORY_TYPE.减少记录);
                    historyDto.setReduceType(String.valueOf(ReduceModel.getReduceType().ordinal()));
                    historyDto.setAssetModelId(model.getAssetId());
                    historyDto.setCreateUserId(((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo().getId());
                    assetService.createHistory(historyDto);

                    reduceTempDao.deleteById(model.getId());
                }
            } else {
                throw new RuntimeException("该资产减损申请单不存在！");
            }
        }
    }

    /***
     * 根据ID获取单条信息
     */
    @Override
    public AssetReduceDto getReduceReceiptById(String id) {
        if (VGUtility.isEmpty(id)) {
            return null;
        } else {
            Optional<AssetReduceModel> model = reduceDao.findById(id);
            if (!VGUtility.isEmpty(model)) {
                AssetReduceDto dto = convertReduceModelToDto(model.get());
                return dto;
            } else {
                return null;
            }
        }
    }

    /***
     * 发起审批
     */
    @Override
    public void checkReduceReceipt(String id) {
        if (VGUtility.isEmpty(reduceDao.findById(id))) {
            throw new RuntimeException("该资产减损申请单不存在！");
        } else {
            //修改主表审核状态
            AssetReduceModel model = reduceDao.findById(id).get();
            model.setReceiptStatus(FlowableInfo.WORKSTATUS.已审批);
            reduceDao.save(model);
            AssetReduceTempModel temp = new AssetReduceTempModel();
            temp.setAssetReduceModelId(model.getId());
            // 将其他类型对象转化为Example类型
            Example<AssetReduceTempModel> example = Example.of(temp);
            // jpa通过findAll方法的Example类型，可按照查询任意字段属性查询；
            List<AssetReduceTempModel> existList = reduceTempDao.findAll(example);
            //更新资产的【状态】信息
            for (int i = 0; i < existList.size(); i++) {
                AssetAssetModel a = assetAssetDao.findById(existList.get(i).getAssetId()).get();
                a.setAssetStatus(IAssetService.ASSET_STATUS.valueOf(model.getReduceType().name()));
                assetAssetDao.save(a);
            }
        }
    }

    /***
     * 编辑更新
     */
    @Override
    public void updateReduceReceipt(AssetReduceDto dto) {
        //校验字段
        assetReduceDtoCommonCheck(dto);
        List<String> assetIdList = new ArrayList<String>();
        Arrays.stream(dto.getAssetIdListStr().split(",")).forEach(arr -> assetIdList.add(arr));
        if (VGUtility.isEmpty(dto.getId())) {
            throw new RuntimeException("该资产减损申请单不存在。");
        } else {
            // 更新-台账
            AssetReduceModel model = reduceDao.findById(dto.getId()).get();
            model.setOrderName(dto.getOrderName());
            model.setReduceType(IAssetService.REDUCE_TYPE.values()[VGUtility.toInteger(dto.getReduceType())]);
            model.setSurplusValue(VGUtility.toDouble(dto.getSurplusValue()));
            model.setProcessingCost(VGUtility.toDouble(dto.getProcessingCost()));
            model.setActualLoss(VGUtility.toDouble(dto.getActualLoss()));
            model.setReason(dto.getReason());
            model.setProposedDisposal(dto.getProposedDisposal());
            model.setReceiptStatus(FlowableInfo.WORKSTATUS.拟稿);
            model.setProduceType(dto.getProduceType());
            reduceDao.save(model);
            //更新-台账明细表信息
            AssetReduceTempModel temp = new AssetReduceTempModel();
            temp.setAssetReduceModelId(model.getId());
            Example<AssetReduceTempModel> example = Example.of(temp);
            // jpa通过findAll方法的Example类型，可按照查询任意字段属性查询；
            List<AssetReduceTempModel> existList = reduceTempDao.findAll(example);
            for (int i = 0; i < existList.size(); i++) {
                // assetIdList 中去除表中已存在明细，避免重复添加；明细表去除assetIdList中不包含的明细数据，即前台已删除信息
                if (assetIdList.contains(existList.get(i).getAssetId().trim())) {
                    assetIdList.remove(existList.get(i).getAssetId().trim());
                } else {
                    AssetHistoryDto historyDto = new AssetHistoryDto();
                    historyDto.setHistoryType(HISTORY_TYPE.减少记录);
                    historyDto.setReduceType(String.valueOf(model.getReduceType().ordinal()));
                    historyDto.setAssetModelId(existList.get(i).getAssetId().trim());
                    historyDto.setCreateUserId(((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo().getId());
                    assetService.createHistory(historyDto);

                    reduceTempDao.deleteById(existList.get(i).getId().trim());
                }
            }
            //保存明细
            for (int i = 0; i < assetIdList.size(); i++) {
                AssetReduceTempModel tempModel = new AssetReduceTempModel();
                tempModel.setAssetId(assetIdList.get(i).trim());
                tempModel.setAssetReduceModelId(model.getId().trim());

                //发起审批后,进入审批流程中,会冻结资产
                AssetAssetDto asset = assetService.getAssetByAssetId(assetIdList.get(i).trim());
                asset.setBeforeChangeAssetStatusStr(asset.getAssetStatusStr());
                assetService.updateAssetBeforeAssetStatus(asset);

                AssetHistoryDto historyDto = new AssetHistoryDto();
                historyDto.setHistoryType(HISTORY_TYPE.减少记录);
                historyDto.setReduceType(String.valueOf(model.getReduceType().ordinal()));
                historyDto.setAssetModelId(assetIdList.get(i).trim());
                historyDto.setCreateUserId(model.getCreateUserId());
                assetService.createHistory(historyDto);

                reduceTempDao.save(tempModel);
            }
        }

    }

    /**
     * 获取资产减损未同步至财务系统的资产
     *
     * @return
     */
    @Override
    public List<AssetReduceTempDto> getNoSysnReduceAsset() {
        List<AssetReduceTempDto> reduceTempDtoList = new ArrayList<>();
        List<Map> reduceModelList = reduceTempDao.getNoSysnReduceAsset();
        for (Map map : reduceModelList) {
            AssetReduceTempDto dto = new AssetReduceTempDto();
            Object assetCode = map.get("ASSETCODE");
            Object companyId = map.get("COMPANYID");
            Object changeNum = map.get("CHANGENUM");
            Object reduceType = map.get("REDUCETYPE");
            Object id = map.get("ID");
            Object reason = map.get("REASON");
            if (!VGUtility.isEmpty(id)) {
                dto.setId(String.valueOf(id));
            }
            if (!VGUtility.isEmpty(assetCode)) {
                dto.setAssetCode(String.valueOf(assetCode));
            }
            if (!VGUtility.isEmpty(companyId)) {
                DeptInfoDto deptInfoDto = (DeptInfoDto) baseService.getDeptInfo(String.valueOf(companyId));
                if (!VGUtility.isEmpty(deptInfoDto))
                    dto.setCompanyCode(deptInfoDto.getDeptCode());
            }
            if (!VGUtility.isEmpty(changeNum)) {
                dto.setChangeNum(String.valueOf(changeNum));
            }
            if (!VGUtility.isEmpty(reduceType)) {
                dto.setReduceType(IAssetService.REDUCE_TYPE.values()[VGUtility.toInteger(String.valueOf(reduceType))]);
            }
            if (!VGUtility.isEmpty(reason)) {
                dto.setReason(String.valueOf(reason));
            }
            reduceTempDtoList.add(dto);
        }
        return reduceTempDtoList;
    }


    /**
     * 更新减少资产同步状态
     *
     * @param id
     * @param sync
     */
    @Override
    public void updateReduceTempSync(String id, boolean sync) {
        AssetReduceTempModel model = reduceTempDao.getOne(id);
        model.setIsSysn(sync);
    }

    private List<String> getAssetIdList(String id) {

        List<AssetReduceTempModel> dtolist = reduceTempDao.findByAssetReduceModelId(id);
        if (dtolist.size() <= 0) {
            throw new RuntimeException("资产列表不能为空！");
        }
        return dtolist.stream().map(e -> e.getAssetId()).collect(Collectors.toList());
    }


    @Override
    public AssetApprove doApproveNotify(String id) {
        List<String> assetIdList = this.getAssetIdList(id);
        //校验资产状态，是否可以审批；
        return assetService.doAssetApproveCheck(assetIdList, null);
    }


    @Override
    public void approveUpdateNotify(FlowableInfo.WORKSTATUS workStatus, String id) {
        List<String> assetIdList;
        switch (workStatus) {
            case 审批中:
                //资产冻结
                assetIdList = this.getAssetIdList(id);
                assetService.LockAssetState(assetIdList);
                break;
            case 驳回:
                //资产状态回滚
                assetIdList = this.getAssetIdList(id);
                assetService.approveFailureAssetStateRollback(assetIdList);
                break;
            case 已审批:
                //将资产状态落地
                assetIdList = this.getAssetIdList(id);
                AssetReduceModel model = reduceDao.findById(id).get();
                assetService.approveSuccessUpdateAssetState(assetIdList, IAssetService.ASSET_STATUS.valueOf(model.getReduceType().name()));
                this.createHistory(model, assetIdList);
                break;
            default:
                break;

        }
        //更新主表状态
        AssetReduceModel model = reduceDao.findById(id).get();
        model.setReceiptStatus(workStatus);

    }

    private void createHistory(AssetReduceModel model, List<String> assetIdList) {
        for (String assetId : assetIdList) {
            AssetHistoryDto historyDto = new AssetHistoryDto();
            historyDto.setHistoryType(HISTORY_TYPE.减少记录);
            historyDto.setReduceType(String.valueOf(model.getReduceType().ordinal()));
            historyDto.setAssetModelId(assetId);
            historyDto.setCreateUserId(model.getCreateUserId());
            assetService.createHistory(historyDto);
        }

    }
}

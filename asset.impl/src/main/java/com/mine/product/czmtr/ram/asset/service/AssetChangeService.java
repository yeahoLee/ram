package com.mine.product.czmtr.ram.asset.service;

import com.mine.base.dict.dto.DictDto;
import com.mine.base.dict.service.IDictService;
import com.mine.base.user.dto.DeptInfoDto;
import com.mine.base.user.dto.UserInfoDto;
import com.mine.base.user.service.IUserService;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dao.*;
import com.mine.product.czmtr.ram.asset.dto.*;
import com.mine.product.czmtr.ram.asset.model.*;
import com.mine.product.czmtr.ram.asset.service.IAssetService.ASSET_STATUS;
import com.mine.product.czmtr.ram.asset.service.IAssetService.CHANGE_TYPE;
import com.mine.product.czmtr.ram.asset.service.IAssetService.HISTORY_TYPE;
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

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AssetChangeService implements IAssetChangeService {
    @Autowired
    private AssetManagerChangeDao managerChangeDao; //管理员变更
    @Autowired
    private AssetManagerChangeTempDao managerChangeTempDao;// 管理员变更临时
    @Autowired
    private AssetUserChangeDao userChangeDao; //使用人变更
    @Autowired
    private AssetUserChangeTempDao userChangeTempDao; //使用人变更临时
    @Autowired
    private AssetSavePlaceChangeDao savePlaceChangeDao; //安装位置变更
    @Autowired
    private AssetSavePlaceChangeTempDao savePlaceChangeTempDao; // 安装位置变更临时
    @Autowired
    private AssetAssetDao assetAssetDao; //资产管理
    @Autowired
    private IUserService userService;
    @Autowired
    private IBaseService baseService;
    @Autowired
    private IDictService dictService;
    @Autowired
    private IAssetService assetService;
    @PersistenceContext
    private EntityManager entityManager;

    /******************************************************资产管理员变更 start************************************************************/

    /***
     * 管理员变更-保存
     */
    @Override
    public AssetManagerChangeDto saveChangeManager(AssetManagerChangeDto dto) {
        assetManagerChangeDtoCommonCheck(dto);
        List<String> assetIdList = new ArrayList<String>();
        Arrays.stream(dto.getAssetIdListStr().split(",")).forEach(arr -> assetIdList.add(arr));
        AssetManagerChangeModel model = new AssetManagerChangeModel();
        model.setOldAssetManagerId(dto.getOldAssetManagerId());
        model.setAssetManagerId(dto.getAssetManagerId());
        model.setReason(dto.getReason());
        model.setProduceType(dto.getProduceType());
        model.setReceiptStatus(FlowableInfo.WORKSTATUS.拟稿);
        model.setCreateUserId(((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo().getId());
        String changeNum = "MCHG";
        changeNum += VGUtility.toDateStr(new Date(), "yyyyMMdd");
        model.setChangeNum(getMaxChangeNum(changeNum));
        AssetManagerChangeModel save = managerChangeDao.save(model);
//        UserInfoDto OldAssetManager = new UserInfoDto();
//        UserInfoDto AssetManager = new UserInfoDto();
//        if (!VGUtility.isEmpty(model.getOldAssetManagerId())) {
//            OldAssetManager = userService.getUserInfo(model.getOldAssetManagerId());
//        }
//        if (!VGUtility.isEmpty(model.getAssetManagerId())) {
//            AssetManager = userService.getUserInfo(model.getAssetManagerId());
//        }

        for (int i = 0; i < assetIdList.size(); i++) {
            AssetManagerChangeTempModel tempModel = new AssetManagerChangeTempModel();
            tempModel.setAssetId(assetIdList.get(i));
            tempModel.setAssetManagerChangeModelId(model.getId());
            //发起审批后,进入审批流程中,会冻结资产
//            AssetAssetDto asset = assetService.getAssetByAssetId(assetIdList.get(i));
//            asset.setBeforeChangeAssetStatusStr(asset.getAssetStatusStr());
//            assetService.updateAssetBeforeAssetStatus(asset);

//            AssetHistoryDto historyDto = new AssetHistoryDto();
//            historyDto.setHistoryType(HISTORY_TYPE.变更记录);
//            historyDto.setChangeType(String.valueOf(CHANGE_TYPE.管理人变更.ordinal()));
//            historyDto.setAssetModelId(tempModel.getAssetId());
//            historyDto.setCreateUserId(model.getCreateUserId());
//            historyDto.setChangeContent("管理员变更:新增"
//                    + "[" + OldAssetManager.getChsName() + "]->"
//                    + "[" + AssetManager.getChsName() + "]");
//            assetService.createHistory(historyDto);

            managerChangeTempDao.save(tempModel);
        }
        AssetManagerChangeDto assetManagerChangeDto = new AssetManagerChangeDto();
        assetManagerChangeDto.setId(save.getId());
        assetManagerChangeDto.setChangeNum(save.getChangeNum());
        return assetManagerChangeDto;

    }

    /***
     * 管理员变更-保存&发起审批
     */
    @Override
    public void saveAndCheckChangeManager(AssetManagerChangeDto dto) {
        //字段校验
        assetManagerChangeDtoCommonCheck(dto);
        List<String> assetIdList = new ArrayList<String>();
        Arrays.stream(dto.getAssetIdListStr().split(",")).forEach(arr -> assetIdList.add(arr));
        AssetManagerChangeModel model = new AssetManagerChangeModel();
        model.setOldAssetManagerId(dto.getOldAssetManagerId());
        model.setAssetManagerId(dto.getAssetManagerId());
        model.setReason(dto.getReason());
        model.setReceiptStatus(FlowableInfo.WORKSTATUS.已审批);
        model.setCreateUserId(((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo().getId());
        String changeNum = "MCHG";
        changeNum += VGUtility.toDateStr(new Date(), "yyyyMMdd");
        model.setChangeNum(getMaxChangeNum(changeNum));
        managerChangeDao.save(model);

        UserInfoDto OldAssetManager = new UserInfoDto();
        UserInfoDto AssetManager = new UserInfoDto();
        if (!VGUtility.isEmpty(model.getOldAssetManagerId())) {
            OldAssetManager = userService.getUserInfo(model.getOldAssetManagerId());
        }
        if (!VGUtility.isEmpty(model.getAssetManagerId())) {
            AssetManager = userService.getUserInfo(model.getAssetManagerId());
        }
        for (int i = 0; i < assetIdList.size(); i++) {
            AssetManagerChangeTempModel tempModel = new AssetManagerChangeTempModel();
            tempModel.setAssetId(assetIdList.get(i));
            tempModel.setAssetManagerChangeModelId(model.getId());
            managerChangeTempDao.save(tempModel);
            //发起审批后,进入审批流程中,会冻结资产
            AssetAssetDto asset = assetService.getAssetByAssetId(assetIdList.get(i));
            asset.setBeforeChangeAssetStatusStr(asset.getAssetStatusStr());
            assetService.updateAssetBeforeAssetStatus(asset);

            AssetHistoryDto historyDto = new AssetHistoryDto();
            historyDto.setHistoryType(HISTORY_TYPE.变更记录);
            historyDto.setChangeType(String.valueOf(CHANGE_TYPE.管理人变更.ordinal()));
            historyDto.setAssetModelId(tempModel.getAssetId());
            historyDto.setCreateUserId(model.getCreateUserId());
            historyDto.setChangeContent("管理员变更:新增"
                    + "[" + OldAssetManager.getChsName() + "]->"
                    + "[" + AssetManager.getChsName() + "]");
            assetService.createHistory(historyDto);
        }
        //发起审批
        checkChangeManagerReceipt(model.getId());
    }

    /***
     * 管理员变更-相关字段校验
     * @param dto
     */
    private void assetManagerChangeDtoCommonCheck(AssetManagerChangeDto dto) {
        if (VGUtility.isEmpty(dto.getOldAssetManagerId()))
            throw new RuntimeException("原资产管理员不能为空！");
        if (VGUtility.isEmpty(dto.getAssetManagerId()))
            throw new RuntimeException("新资产管理员不能为空！");
        if (dto.getOldAssetManagerId().equals(dto.getAssetManagerId()))
            throw new RuntimeException("原资产管理员与新资产管理员不能为同一个人！");
        if (VGUtility.isEmpty(dto.getReason()))
            throw new RuntimeException("变更原因不能为空！");
        if (VGUtility.isEmpty(dto.getAssetIdListStr()))
            throw new RuntimeException("资产列表不能为空！");
    }

    /***
     * 管理员变更-获取列表数据
     */
    @Override
    public Map<String, Object> getAssetManagerChangeForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto) {
        //通过hibernate获得Session
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        //通过session获得标准生成器 CriteriaBuilder
        CriteriaBuilder builder = session.getCriteriaBuilder();
        //标准化对象查询
        CriteriaQuery<AssetManagerChangeModel> assetCriteria = builder.createQuery(AssetManagerChangeModel.class);
        //获取根
        Root<AssetManagerChangeModel> root = assetCriteria.from(AssetManagerChangeModel.class);
        Predicate predicate = (Predicate) searchExpression.change(root, builder);
        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            assetCriteria.where((Predicate) predicate);
        //根据查询器添加顺序
        Order order = builder.asc(root.get("createTimestamp"));
        assetCriteria.orderBy(order);
        //创建查询器
        Query<AssetManagerChangeModel> query = session.createQuery(assetCriteria);
        //分页
        if (!VGUtility.isEmpty(pageableDto))
            query.setFirstResult((pageableDto.getPage() - 1) * pageableDto.getSize()).setMaxResults(pageableDto.getSize());
        List<AssetManagerChangeModel> modelList = query.getResultList();
        //convert
        List<AssetManagerChangeDto> dtoList = new ArrayList<AssetManagerChangeDto>();
        for (AssetManagerChangeModel model : modelList) {
            AssetManagerChangeDto dto = convertManagerChangeModelToDto(model);
            dtoList.add(dto);
        }
        //total
        CriteriaQuery<Long> longCriteria = builder.createQuery(Long.class);
        root = longCriteria.from(AssetManagerChangeModel.class);
        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            longCriteria.where((Predicate) predicate);
        longCriteria.select(builder.count(root));
        long total = session.createQuery(longCriteria).uniqueResult();

        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtoList);
        modelMap.addAttribute("total", total);
        return modelMap;
    }

    private AssetManagerChangeDto convertManagerChangeModelToDto(AssetManagerChangeModel model) {
        AssetManagerChangeDto dto = new AssetManagerChangeDto();
        dto.setProduceType(model.getProduceType());
        dto.setId(model.getId());
        dto.setOldAssetManagerId(model.getOldAssetManagerId());
        dto.setOldAssetManagerStr(userService.getUserInfo(model.getOldAssetManagerId()).getChsName());
        dto.setAssetManagerId(model.getAssetManagerId());
        dto.setAssetManagerStr(userService.getUserInfo(model.getAssetManagerId()).getChsName());
        dto.setCreateUserId(model.getCreateUserId());
        dto.setCreateUserIdStr(userService.getUserInfo(model.getCreateUserId()).getChsName());
        dto.setReason(model.getReason());
        dto.setChangeNum(model.getChangeNum());
        dto.setReceiptStatus(model.getReceiptStatus().ordinal() + "");
        dto.setReceiptStatusStr(model.getReceiptStatus().name());
        dto.setCreateTimestamp(VGUtility.toDateStr(model.getCreateTimestamp(), "yyyy-MM-dd"));
        String hql = "from AssetManagerChangeTempModel where 1=1 ";
        if (!VGUtility.isEmpty(model.getId())) {
            hql += "and assetManagerChangeModelId = " + "'" + model.getId() + "'";
        }
        TypedQuery<AssetManagerChangeTempModel> query = entityManager.createQuery(hql, AssetManagerChangeTempModel.class);
        List<AssetManagerChangeTempModel> resultList = query.getResultList();
        String assetChange = "";
        List<String> assetIdList = new ArrayList<String>();
        for (AssetManagerChangeTempModel tempModel : resultList) {
            //有问题
            hql = "from AssetAssetModel where id=" + "'" + tempModel.getAssetId().trim() + "'";
            assetIdList.add(tempModel.getAssetId());
            TypedQuery<AssetAssetModel> assetQuery = entityManager.createQuery(hql, AssetAssetModel.class);
            if (!VGUtility.isEmpty(assetQuery) && assetQuery.getResultList().size() > 0)
                assetChange += "、" + baseService.getAssetNameByMaterialCode(assetQuery.getResultList().get(0).getMaterialCode());
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
     * 管理员变更-获取最大变更单号
     * @param changeNum
     * @return
     */
    public String getMaxChangeNum(String changeNum) {
        String maxChangeNum = managerChangeDao.getMaxChangeNum(changeNum + "%");
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
     * 管理员变更-删除
     */
    @Override
    public void deleteChangeManagerReceipt(String id) {
        if (VGUtility.isEmpty(managerChangeDao.findById(id))) {
            throw new RuntimeException("该管理员变更申请单不存在！");
        } else {
            AssetManagerChangeModel ManagetChangeModel = managerChangeDao.findById(id).get();
            UserInfoDto OldAssetManager = new UserInfoDto();
            UserInfoDto AssetManager = new UserInfoDto();
            if (!VGUtility.isEmpty(ManagetChangeModel.getOldAssetManagerId())) {
                OldAssetManager = userService.getUserInfo(ManagetChangeModel.getOldAssetManagerId());
            }
            if (!VGUtility.isEmpty(ManagetChangeModel.getAssetManagerId())) {
                AssetManager = userService.getUserInfo(ManagetChangeModel.getAssetManagerId());
            }
            managerChangeDao.deleteById(id);
            String hql = "from AssetManagerChangeTempModel where assetManagerChangeModelId=" + "'" + id + "'";
            TypedQuery<AssetManagerChangeTempModel> assetQuery = entityManager.createQuery(hql, AssetManagerChangeTempModel.class);
            if (assetQuery.getResultList().size() > 0) {
                for (AssetManagerChangeTempModel model : assetQuery.getResultList()) {
                    AssetAssetDto asset = assetService.getAssetByAssetId(model.getAssetId());
//					asset.setAssetStatusStr(asset.getBeforeChangeAssetStatusStr());
//					asset.setBeforeChangeAssetStatusStr(null);
//					assetService.updateAssetBeforeAssetStatus(asset);

                    AssetHistoryDto historyDto = new AssetHistoryDto();
                    historyDto.setHistoryType(HISTORY_TYPE.变更记录);
                    historyDto.setChangeType(String.valueOf(CHANGE_TYPE.管理人变更.ordinal()));
                    historyDto.setAssetModelId(asset.getId());
                    historyDto.setCreateUserId(((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo().getId());
                    historyDto.setChangeContent("管理员变更:删除"
                            + "[" + OldAssetManager.getChsName() + "]->"
                            + "[" + AssetManager.getChsName() + "]");
                    assetService.createHistory(historyDto);
                    managerChangeTempDao.deleteById(model.getId());
                }
            } else {
                throw new RuntimeException("该管理员变更申请单不存在！");
            }
        }
    }

    /***
     * 管理员变更-根据ID获取单条变更信息
     */
    @Override
    public AssetManagerChangeDto getManagerChangeReceiptById(String id) {
        if (VGUtility.isEmpty(id)) {
            return null;
        } else {
            Optional<AssetManagerChangeModel> model = managerChangeDao.findById(id);
            if (!VGUtility.isEmpty(model)) {
                AssetManagerChangeDto dto = convertManagerChangeModelToDto(model.get());
                return dto;
            } else {
                return null;
            }
        }
    }

    /***
     * 管理员变更-发起审批
     */
    @Override
    public void checkChangeManagerReceipt(String id) {
        if (VGUtility.isEmpty(managerChangeDao.findById(id))) {
            throw new RuntimeException("该管理员变更申请单不存在！");
        } else {
            //修改主表审核状态
            AssetManagerChangeModel model = managerChangeDao.findById(id).get();
            model.setReceiptStatus(FlowableInfo.WORKSTATUS.已审批);

            AssetManagerChangeTempModel temp = new AssetManagerChangeTempModel();
            temp.setAssetManagerChangeModelId(model.getId());
            // 将其他类型对象转化为Example类型
            Example<AssetManagerChangeTempModel> example = Example.of(temp);
            // jpa通过findAll方法的Example类型，可按照查询任意字段属性查询；
            List<AssetManagerChangeTempModel> existList = managerChangeTempDao.findAll(example);
            //获取管理员部门信息
//			DeptInfoDto dept = (DeptInfoDto) baseService.getDeptInfoDtoByCode(model.getAssetManagerId());
            //更新资产的【管理员】信息
            for (int i = 0; i < existList.size(); i++) {
                AssetAssetModel a = assetAssetDao.findById(existList.get(i).getAssetId()).get();
//				if (!a.getManagerId().equals(model.getOldAssetManagerId())) 
//					throw new RuntimeException("该资产已经变更过，请重新编辑申请单！");
                a.setManagerId(model.getAssetManagerId());
//				a.setManageDeptId(dept.getId());
                assetAssetDao.save(a);
            }
            managerChangeDao.save(model);
			
			/*managerChangeDao.save(model);		//之前代码
			AssetManagerChangeTempModel temp = new AssetManagerChangeTempModel();
			temp.setAssetManagerChangeModelId(model.getId());
			// 将其他类型对象转化为Example类型
			Example<AssetManagerChangeTempModel> example = Example.of(temp);
			// jpa通过findAll方法的Example类型，可按照查询任意字段属性查询；
			List<AssetManagerChangeTempModel> existList = managerChangeTempDao.findAll(example);
			//更新资产的【管理员】信息
			for(int i=0;i<existList.size();i++) {
				AssetAssetModel a = assetAssetDao.findById(existList.get(i).getAssetId()).get();
				a.setManagerId(model.getAssetManagerId());
				assetAssetDao.save(a);
			}*/
        }
    }

    /***
     * 管理员变更-编辑更新
     */
    @Override
    public void updateChangeManagerReceipt(AssetManagerChangeDto dto) {
        //校验字段
        assetManagerChangeDtoCommonCheck(dto);
        List<String> assetIdList = new ArrayList<String>();
        Arrays.stream(dto.getAssetIdListStr().split(",")).forEach(arr -> assetIdList.add(arr));
        if (VGUtility.isEmpty(dto.getId())) {
            throw new RuntimeException("该资产管理员变更申请单不存在。");
        } else {
            // 更新-管理员变更台账
            AssetManagerChangeModel model = managerChangeDao.findById(dto.getId()).get();
            model.setOldAssetManagerId(dto.getOldAssetManagerId());
            model.setAssetManagerId(dto.getAssetManagerId());
            model.setReason(dto.getReason());
            model.setReceiptStatus(FlowableInfo.WORKSTATUS.拟稿);
            model.setProduceType(dto.getProduceType());
            managerChangeDao.save(model);
//            UserInfoDto OldAssetManager = new UserInfoDto();
//            UserInfoDto AssetManager = new UserInfoDto();
//            if (!VGUtility.isEmpty(model.getOldAssetManagerId())) {
//                OldAssetManager = userService.getUserInfo(model.getOldAssetManagerId());
//            }
//            if (!VGUtility.isEmpty(model.getAssetManagerId())) {
//                AssetManager = userService.getUserInfo(model.getAssetManagerId());
//            }
            //更新-管理员变更台账明细表信息
            AssetManagerChangeTempModel temp = new AssetManagerChangeTempModel();
            temp.setAssetManagerChangeModelId(model.getId());
            Example<AssetManagerChangeTempModel> example = Example.of(temp);
            // jpa通过findAll方法的Example类型，可按照查询任意字段属性查询；
            List<AssetManagerChangeTempModel> existList = managerChangeTempDao.findAll(example);
            for (int i = 0; i < existList.size(); i++) {
                // assetIdList 中去除表中已存在明细，避免重复添加；明细表去除assetIdList中不包含的明细数据，即前台已删除信息
                if (assetIdList.contains(existList.get(i).getAssetId())) {
                    assetIdList.remove(existList.get(i).getAssetId());
                } else {
                    //发起审批后,进入审批流程中,会冻结资产
//					AssetAssetDto asset=assetService.getAssetByAssetId(assetIdList.get(i));
//					asset.setAssetStatusStr(asset.getBeforeChangeAssetStatusStr());
//					asset.setBeforeChangeAssetStatusStr(null);
//					assetService.updateAssetBeforeAssetStatus(asset);

//                    AssetHistoryDto historyDto = new AssetHistoryDto();
//                    historyDto.setHistoryType(HISTORY_TYPE.变更记录);
//                    historyDto.setChangeType(String.valueOf(CHANGE_TYPE.管理人变更.ordinal()));
//                    historyDto.setAssetModelId(existList.get(i).getAssetId());
//                    historyDto.setCreateUserId(model.getCreateUserId());
//                    historyDto.setChangeContent("管理员变更:删除变更记录"
//                            + "[" + OldAssetManager.getChsName() + "]->"
//                            + "[" + AssetManager.getChsName() + "]");
//                    assetService.createHistory(historyDto);
                    managerChangeTempDao.deleteById(existList.get(i).getId());
                }
            }
            //保存明细
            for (int i = 0; i < assetIdList.size(); i++) {
                AssetManagerChangeTempModel tempModel = new AssetManagerChangeTempModel();
                tempModel.setAssetId(assetIdList.get(i).trim());
                tempModel.setAssetManagerChangeModelId(model.getId().trim());
                managerChangeTempDao.save(tempModel);

                //发起审批后,进入审批流程中,会冻结资产
                AssetAssetDto asset = assetService.getAssetByAssetId(assetIdList.get(i).trim());
                asset.setBeforeChangeAssetStatusStr(asset.getAssetStatusStr());
                assetService.updateAssetBeforeAssetStatus(asset);

//                AssetHistoryDto historyDto = new AssetHistoryDto();
//                historyDto.setHistoryType(HISTORY_TYPE.变更记录);
//                historyDto.setChangeType(String.valueOf(CHANGE_TYPE.管理人变更.ordinal()));
//                historyDto.setAssetModelId(tempModel.getAssetId());
//                historyDto.setCreateUserId(model.getCreateUserId());
//                historyDto.setChangeContent("管理员变更:编辑"
//                        + "[" + OldAssetManager.getChsName() + "]->"
//                        + "[" + AssetManager.getChsName() + "]");
//                assetService.createHistory(historyDto);
            }
        }

    }

    /******************************************************资产管理员变更 end************************************************************/


    /******************************************************资产使用人变更 start************************************************************/
    /***
     * 使用人变更-保存
     */
    @Override
    public AssetUserChangeDto saveChangeUser(AssetUserChangeDto dto) {
        //字段校验
        assetUserChangeDtoCommonCheck(dto);
        List<String> assetIdList = new ArrayList<String>();
        Arrays.stream(dto.getAssetIdListStr().split(",")).forEach(arr -> assetIdList.add(arr));
        AssetUserChangeModel model = new AssetUserChangeModel();
        // 获取使用人信息
        String oldAssetUserId = "";
        for (int i = 0; i < assetIdList.size(); i++) {
            String temp = assetAssetDao.findById(assetIdList.get(i).trim()).get().getUserId();
            if (!VGUtility.isEmpty(temp)) {
                if (oldAssetUserId.indexOf(temp) > -1) {
                    continue;
                } else {
                    oldAssetUserId += temp + ",";
                }
            }
        }
        if (!VGUtility.isEmpty(oldAssetUserId))
            model.setOldAssetUserId(oldAssetUserId.substring(0, oldAssetUserId.length() - 1));
        model.setAssetUserId(dto.getAssetUserId());
        model.setCreateUserId(((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo().getId());
        model.setReason(dto.getReason());
        model.setReceiptStatus(FlowableInfo.WORKSTATUS.拟稿);
        String changeNum = "UC";
        changeNum += VGUtility.toDateStr(new Date(), "yyyyMMdd");
        model.setChangeNum(getMaxChangeNumUser(changeNum));
        model.setProduceType(dto.getProduceType());
        AssetUserChangeModel save = userChangeDao.save(model);
//        UserInfoDto OldAssetUser = new UserInfoDto();
//        UserInfoDto AssetUser = new UserInfoDto();
//        if (!VGUtility.isEmpty(model.getOldAssetUserId())) {
//            OldAssetUser = userService.getUserInfo(model.getOldAssetUserId());
//        }
//        if (!VGUtility.isEmpty(model.getAssetUserId())) {
//            AssetUser = userService.getUserInfo(model.getAssetUserId());
//        }
        for (int i = 0; i < assetIdList.size(); i++) {
            AssetUserChangeTempModel tempModel = new AssetUserChangeTempModel();
            tempModel.setAssetId(assetIdList.get(i));
            tempModel.setAssetUserChangeModelId(model.getId());

            //发起审批后,进入审批流程中,会冻结资产
//            AssetAssetDto asset = assetService.getAssetByAssetId(assetIdList.get(i));
//            asset.setBeforeChangeAssetStatusStr(asset.getAssetStatusStr());
//            assetService.updateAssetBeforeAssetStatus(asset);

            userChangeTempDao.save(tempModel);
//            AssetHistoryDto historyDto = new AssetHistoryDto();
//            historyDto.setHistoryType(HISTORY_TYPE.变更记录);
//            historyDto.setChangeType(String.valueOf(CHANGE_TYPE.使用人变更.ordinal()));
//            historyDto.setAssetModelId(tempModel.getAssetId());
//            historyDto.setCreateUserId(model.getCreateUserId());
//            historyDto.setChangeContent("使用人变更:新增"
//                    + "[" + OldAssetUser.getChsName() + "]->"
//                    + "[" + AssetUser.getChsName() + "]");
//            assetService.createHistory(historyDto);
        }
        AssetUserChangeDto assetUserChangeDto = new AssetUserChangeDto();
        assetUserChangeDto.setId(save.getId());
        assetUserChangeDto.setChangeNum(save.getChangeNum());
        return assetUserChangeDto;
    }

    /***
     * 使用人变更-保存并发起审批
     */
    @Override
    public void saveAndCheckChangeUser(AssetUserChangeDto dto) {
        assetUserChangeDtoCommonCheck(dto);
        List<String> assetIdList = new ArrayList<String>();
        Arrays.stream(dto.getAssetIdListStr().split(",")).forEach(arr -> assetIdList.add(arr));
        AssetUserChangeModel model = new AssetUserChangeModel();
        model.setAssetUserId(dto.getAssetUserId());
        model.setCreateUserId(((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo().getId());
        // 获取使用人信息
        String oldAssetUserId = "";
        for (int i = 0; i < assetIdList.size(); i++) {
            String temp = assetAssetDao.findById(assetIdList.get(i).trim()).get().getUserId();
            if (!VGUtility.isEmpty(temp)) {
                if (oldAssetUserId.indexOf(temp) > -1) {
                    continue;
                } else {
                    oldAssetUserId += temp + ",";
                }
            }
        }
        if (!VGUtility.isEmpty(oldAssetUserId))
            model.setOldAssetUserId(oldAssetUserId.substring(0, oldAssetUserId.length() - 1));
        model.setReason(dto.getReason());
        model.setReceiptStatus(FlowableInfo.WORKSTATUS.已审批);
        String changeNum = "UC";
        changeNum += VGUtility.toDateStr(new Date(), "yyyyMMdd");
        model.setChangeNum(getMaxChangeNumUser(changeNum));
        userChangeDao.save(model);
        UserInfoDto OldAssetUser = new UserInfoDto();
        UserInfoDto AssetUser = new UserInfoDto();
        if (!VGUtility.isEmpty(model.getOldAssetUserId())) {
            OldAssetUser = userService.getUserInfo(model.getOldAssetUserId());
        }
        if (!VGUtility.isEmpty(model.getAssetUserId())) {
            AssetUser = userService.getUserInfo(model.getAssetUserId());
        }
        for (int i = 0; i < assetIdList.size(); i++) {
            AssetUserChangeTempModel tempModel = new AssetUserChangeTempModel();
            tempModel.setAssetId(assetIdList.get(i));
            tempModel.setAssetUserChangeModelId(model.getId());
            userChangeTempDao.save(tempModel);

            AssetHistoryDto historyDto = new AssetHistoryDto();
            historyDto.setHistoryType(HISTORY_TYPE.变更记录);
            historyDto.setChangeType(String.valueOf(CHANGE_TYPE.使用人变更.ordinal()));
            historyDto.setAssetModelId(tempModel.getAssetId());
            historyDto.setCreateUserId(model.getCreateUserId());
            historyDto.setChangeContent("使用人变更:新增"
                    + "[" + OldAssetUser.getChsName() + "]->"
                    + "[" + AssetUser.getChsName() + "]");
            assetService.createHistory(historyDto);
        }
        // 发起审批
        checkChangeUserReceipt(model.getId());
    }

    /***
     * 使用人变更-字段校验
     * @param dto
     */
    private void assetUserChangeDtoCommonCheck(AssetUserChangeDto dto) {
        if (VGUtility.isEmpty(dto.getAssetUserId()))
            throw new RuntimeException("新资产使用人不能为空！");
        if (VGUtility.isEmpty(dto.getReason()))
            throw new RuntimeException("变更原因不能为空！");
        if (VGUtility.isEmpty(dto.getAssetIdListStr()))
            throw new RuntimeException("资产列表不能为空！");
    }

    /***
     * 使用人变更-获取列表信息
     */
    @Override
    public Map<String, Object> getAssetUserChangeForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto) {
        //通过hibernate获得Session
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        //通过session获得标准生成器 CriteriaBuilder
        CriteriaBuilder builder = session.getCriteriaBuilder();
        //标准化对象查询
        CriteriaQuery<AssetUserChangeModel> assetCriteria = builder.createQuery(AssetUserChangeModel.class);
        //获取根
        Root<AssetUserChangeModel> root = assetCriteria.from(AssetUserChangeModel.class);
        Predicate predicate = (Predicate) searchExpression.change(root, builder);
        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            assetCriteria.where((Predicate) predicate);
        //根据查询器添加顺序
        Order order = builder.asc(root.get("createTimestamp"));
        assetCriteria.orderBy(order);
        //创建查询器
        Query<AssetUserChangeModel> query = session.createQuery(assetCriteria);
        //分页
        if (!VGUtility.isEmpty(pageableDto))
            query.setFirstResult((pageableDto.getPage() - 1) * pageableDto.getSize()).setMaxResults(pageableDto.getSize());
        List<AssetUserChangeModel> modelList = query.getResultList();
        //convert
        List<AssetUserChangeDto> dtoList = new ArrayList<AssetUserChangeDto>();
        for (AssetUserChangeModel model : modelList) {
            AssetUserChangeDto dto = convertUserChangeModelToDto(model);
            dtoList.add(dto);
        }
        //total
        CriteriaQuery<Long> longCriteria = builder.createQuery(Long.class);
        root = longCriteria.from(AssetUserChangeModel.class);
        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            longCriteria.where((Predicate) predicate);
        longCriteria.select(builder.count(root));
        long total = session.createQuery(longCriteria).uniqueResult();

        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtoList);
        modelMap.addAttribute("total", total);
        return modelMap;
    }

    private AssetUserChangeDto convertUserChangeModelToDto(AssetUserChangeModel model) {
        AssetUserChangeDto dto = new AssetUserChangeDto();
        dto.setProduceType(model.getProduceType());
        dto.setId(model.getId());
        dto.setOldAssetUserId(model.getOldAssetUserId());
        //获取原使用人信息
        List<String> sp = new ArrayList<String>();
        if (!VGUtility.isEmpty(model.getOldAssetUserId()))
            Arrays.stream(model.getOldAssetUserId().split(",")).forEach(arr -> sp.add(arr));
        String oldAssetUserStr = "";
        for (String str : sp) {
            String temp = userService.getUserInfo(str).getChsName();
            if (oldAssetUserStr.indexOf(temp) > -1) {
                continue;
            } else {
                oldAssetUserStr += temp + ",";
            }
        }
        if (!VGUtility.isEmpty(oldAssetUserStr))
            dto.setOldAssetUserStr(oldAssetUserStr.substring(0, oldAssetUserStr.length() - 1));
        dto.setAssetUserId(model.getAssetUserId());
        dto.setAssetUserStr(userService.getUserInfo(model.getAssetUserId()).getChsName());
        dto.setReason(model.getReason());
        dto.setChangeNum(model.getChangeNum());
        dto.setReceiptStatus(model.getReceiptStatus().ordinal() + "");
        dto.setReceiptStatusStr(model.getReceiptStatus().name());
        dto.setCreateTimestamp(VGUtility.toDateStr(model.getCreateTimestamp(), "yyyy-MM-dd"));
        dto.setCreateUserId(model.getCreateUserId());
        dto.setCreateUserIdStr(userService.getUserInfo(model.getCreateUserId()).getChsName());
        String hql = "from AssetUserChangeTempModel where 1=1 ";
        if (!VGUtility.isEmpty(model.getId())) {
            hql += "and assetUserChangeModelId = " + "'" + model.getId() + "'";
        }
        TypedQuery<AssetUserChangeTempModel> query = entityManager.createQuery(hql, AssetUserChangeTempModel.class);
        List<AssetUserChangeTempModel> resultList = query.getResultList();
        String assetChange = "";
        List<String> assetIdList = new ArrayList<String>();
        for (AssetUserChangeTempModel tempModel : resultList) {
            //有问题
            hql = "from AssetAssetModel where id=" + "'" + tempModel.getAssetId().trim() + "'";
            assetIdList.add(tempModel.getAssetId());
            TypedQuery<AssetAssetModel> assetQuery = entityManager.createQuery(hql, AssetAssetModel.class);
            if (!VGUtility.isEmpty(assetQuery) && assetQuery.getResultList().size() > 0)
                assetChange += "、" + baseService.getAssetNameByMaterialCode(assetQuery.getResultList().get(0).getMaterialCode());
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
     * 使用人变更-获取变更单号
     * @param changeNum
     * @return
     */
    public String getMaxChangeNumUser(String changeNum) {
        String maxChangeNum = userChangeDao.getMaxChangeNum(changeNum + "%");
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
     * 使用人变更-删除
     */
    @Override
    public void deleteChangeUserReceipt(String id) {
        if (VGUtility.isEmpty(userChangeDao.findById(id))) {
            throw new RuntimeException("该使用人变更申请单不存在！");
        } else {
            AssetUserChangeModel UserChangeModel = userChangeDao.findById(id).get();
//            UserInfoDto OldAssetUser = new UserInfoDto();
//            UserInfoDto AssetUser = new UserInfoDto();
//            if (!VGUtility.isEmpty(UserChangeModel.getOldAssetUserId())) {
//                OldAssetUser = userService.getUserInfo(UserChangeModel.getOldAssetUserId());
//            }
//            if (!VGUtility.isEmpty(UserChangeModel.getAssetUserId())) {
//                AssetUser = userService.getUserInfo(UserChangeModel.getAssetUserId());
//            }

            userChangeDao.deleteById(id);
            String hql = "from AssetUserChangeTempModel where assetUserChangeModelId=" + "'" + id + "'";
            TypedQuery<AssetUserChangeTempModel> assetQuery = entityManager.createQuery(hql, AssetUserChangeTempModel.class);
            if (assetQuery.getResultList().size() > 0) {
                for (AssetUserChangeTempModel model : assetQuery.getResultList()) {
                    //修改资产原有状态
                    AssetAssetDto asset = assetService.getAssetByAssetId(model.getAssetId());
//					asset.setAssetStatusStr(asset.getBeforeChangeAssetStatusStr());
//					asset.setBeforeChangeAssetStatusStr(null);
//					assetService.updateAssetBeforeAssetStatus(asset);
                    userChangeTempDao.deleteById(model.getId());

//                    AssetHistoryDto historyDto = new AssetHistoryDto();
//                    historyDto.setHistoryType(HISTORY_TYPE.变更记录);
//                    historyDto.setChangeType(String.valueOf(CHANGE_TYPE.使用人变更.ordinal()));
//                    historyDto.setAssetModelId(asset.getId());
//                    historyDto.setCreateUserId(((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo().getId());
//                    historyDto.setChangeContent("使用人变更:删除"
//                            + "[" + OldAssetUser + "]->"
//                            + "[" + AssetUser + "]");
//                    assetService.createHistory(historyDto);
                }
            } else {
                throw new RuntimeException("该使用人变更申请单不存在！");
            }
        }
    }

    /***
     * 使用人变更-根据ID获取单条信息
     */
    @Override
    public AssetUserChangeDto getUserChangeReceiptById(String id) {
        if (VGUtility.isEmpty(id)) {
            return null;
        } else {
            Optional<AssetUserChangeModel> model = userChangeDao.findById(id);
            if (!VGUtility.isEmpty(model)) {
                AssetUserChangeDto dto = convertUserChangeModelToDto(model.get());
                return dto;
            } else {
                return null;
            }
        }
    }

    /***
     * 使用人变更-发起审批
     */
    @Override
    public void checkChangeUserReceipt(String id) {
        if (VGUtility.isEmpty(userChangeDao.findById(id))) {
            throw new RuntimeException("该使用人变更申请单不存在！");
        } else {
            // 变更使用人台账审核状态
            AssetUserChangeModel model = userChangeDao.findById(id).get();
            model.setReceiptStatus(FlowableInfo.WORKSTATUS.已审批);
            userChangeDao.save(model);
            // 变更相关资产使用人信息
            AssetUserChangeTempModel temp = new AssetUserChangeTempModel();
            temp.setAssetUserChangeModelId(model.getId());
            Example<AssetUserChangeTempModel> example = Example.of(temp);
            // jpa通过findAll方法的Example类型，可按照查询任意字段属性查询；
            List<AssetUserChangeTempModel> existList = userChangeTempDao.findAll(example);
            //获取使用人部门信息
            UserInfoDto AssetUser = new UserInfoDto();
            if (!VGUtility.isEmpty(model.getAssetUserId())) {
                AssetUser = userService.getUserInfo(model.getAssetUserId());
            }
            DeptInfoDto dept = new DeptInfoDto();
            if (AssetUser.getDeptList().size() > 0) {
                dept = (DeptInfoDto) baseService.getDeptInfo(AssetUser.getDeptList().get(0).get("deptId").toString());
            }
            for (int i = 0; i < existList.size(); i++) {
                AssetAssetModel a = assetAssetDao.findById(existList.get(i).getAssetId()).get();
                a.setOldUserId(a.getUserId());
                a.setUserId(model.getAssetUserId());
                a.setUseDeptId(dept.getId());
                assetAssetDao.save(a);
            }
        }
    }

    /***
     * 使用人变更-更新
     */
    @Override
    public void updateChangeUserReceipt(AssetUserChangeDto dto) {
        assetUserChangeDtoCommonCheck(dto);
        List<String> assetIdList = new ArrayList<String>();
        Arrays.stream(dto.getAssetIdListStr().split(",")).forEach(arr -> assetIdList.add(arr));
        if (VGUtility.isEmpty(dto.getId())) {
            throw new RuntimeException("该资产使用人变更申请单不存在。");
        } else {
            // 更新-使用人变更台账
            AssetUserChangeModel model = userChangeDao.findById(dto.getId()).get();
            // 获取使用人信息
            String oldAssetUserId = "";
            for (int i = 0; i < assetIdList.size(); i++) {
                String temp = assetAssetDao.findById(assetIdList.get(i).trim()).get().getUserId();
                if (!VGUtility.isEmpty(temp)) {
                    if (oldAssetUserId.indexOf(temp) > -1) {
                        continue;
                    } else {
                        oldAssetUserId += temp + ",";
                    }
                }
            }
            if (!VGUtility.isEmpty(oldAssetUserId))
                model.setOldAssetUserId(oldAssetUserId.substring(0, oldAssetUserId.length() - 1));
            model.setAssetUserId(dto.getAssetUserId());
            model.setReason(dto.getReason());
            model.setReceiptStatus(FlowableInfo.WORKSTATUS.拟稿);
            model.setProduceType(dto.getProduceType());
            userChangeDao.save(model);
//            UserInfoDto OldAssetUser = new UserInfoDto();
//            UserInfoDto AssetUser = new UserInfoDto();
//            if (!VGUtility.isEmpty(model.getOldAssetUserId())) {
//                OldAssetUser = userService.getUserInfo(model.getOldAssetUserId());
//            }
//            if (!VGUtility.isEmpty(model.getAssetUserId())) {
//                AssetUser = userService.getUserInfo(model.getAssetUserId());
//            }
            //更新-使用人变更台账明细表信息
            AssetUserChangeTempModel temp = new AssetUserChangeTempModel();
            temp.setAssetUserChangeModelId(model.getId());
            // jpa通过findAll方法的Example类型，可按照查询任意字段属性查询；
            Example<AssetUserChangeTempModel> example = Example.of(temp);
            List<AssetUserChangeTempModel> existList = userChangeTempDao.findAll(example);
            for (int i = 0; i < existList.size(); i++) {
                // assetIdList 中去除表中已存在明细，避免重复添加；明细表去除assetIdList中不包含的明细数据，即前台已删除信息
                if (assetIdList.contains(existList.get(i).getAssetId().trim())) {
                    assetIdList.remove(existList.get(i).getAssetId().trim());
                } else {
//                    AssetHistoryDto historyDto = new AssetHistoryDto();
//                    historyDto.setHistoryType(HISTORY_TYPE.变更记录);
//                    historyDto.setChangeType(String.valueOf(CHANGE_TYPE.使用人变更.ordinal()));
//                    historyDto.setAssetModelId(existList.get(i).getAssetId().trim());
//                    historyDto.setCreateUserId(model.getCreateUserId());
//                    historyDto.setChangeContent("使用人变更:删除"
//                            + "[" + OldAssetUser.getChsName() + "]->"
//                            + "[" + AssetUser.getChsName() + "]");
//                    assetService.createHistory(historyDto);
                    //修改资产原有状态
//					AssetAssetDto asset=assetService.getAssetByAssetId(existList.get(i).getAssetId());
//					asset.setAssetStatusStr(asset.getBeforeChangeAssetStatusStr());
//					asset.setBeforeChangeAssetStatusStr(null);
//					assetService.updateAssetBeforeAssetStatus(asset);

                    userChangeTempDao.deleteById(existList.get(i).getId().trim());
                }
            }
            for (int i = 0; i < assetIdList.size(); i++) {
                AssetUserChangeTempModel tempModel = new AssetUserChangeTempModel();
                tempModel.setAssetId(assetIdList.get(i).trim());
                tempModel.setAssetUserChangeModelId(model.getId().trim());
                userChangeTempDao.save(tempModel);
                //发起审批后,进入审批流程中,会冻结资产
//                AssetAssetDto asset = assetService.getAssetByAssetId(assetIdList.get(i).trim());
//                asset.setBeforeChangeAssetStatusStr(asset.getAssetStatusStr());
//                assetService.updateAssetBeforeAssetStatus(asset);
//
//                AssetHistoryDto historyDto = new AssetHistoryDto();
//                historyDto.setHistoryType(HISTORY_TYPE.变更记录);
//                historyDto.setChangeType(String.valueOf(CHANGE_TYPE.使用人变更.ordinal()));
//                historyDto.setAssetModelId(tempModel.getAssetId());
//                historyDto.setCreateUserId(model.getCreateUserId());
//                historyDto.setChangeContent("使用人变更:编辑"
//                        + "[" + OldAssetUser.getChsName() + "]->"
//                        + "[" + AssetUser.getChsName() + "]");
//                assetService.createHistory(historyDto);
            }
        }

    }

    /******************************************************资产使用人变更 end************************************************************/


    /******************************************************资产安装位置变更 start************************************************************/
    /***
     * 安装位置变更-保存
     */
    @Override
    public AssetSavePlaceChangeDto saveChangeSavePlace(AssetSavePlaceChangeDto dto) {
        assetSavePlaceChangeDtoCommonCheck(dto);
        List<String> assetIdList = new ArrayList<String>();
        Arrays.stream(dto.getAssetIdListStr().split(",")).forEach(arr -> assetIdList.add(arr));
        AssetSavePlaceChangeModel model = new AssetSavePlaceChangeModel();
        model.setCreateUserId(((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo().getId());
        model.setAssetSavePlaceId(dto.getAssetSavePlaceId());
        model.setReason(dto.getReason());
        model.setProduceType(dto.getProduceType());
        model.setReceiptStatus(FlowableInfo.WORKSTATUS.拟稿);
        String changeNum = "LC";
        changeNum += VGUtility.toDateStr(new Date(), "yyyyMMdd");
        model.setChangeNum(getMaxChangeNumSavePlace(changeNum));
        AssetSavePlaceChangeModel save = savePlaceChangeDao.save(model);
//        String NewAssetSavePlaceStr = "";
//        // 根据assetId查到assetDto,根据dto中的placeId查到dictionaryDto，再查到code和chsName
//        if (!VGUtility.isEmpty(model.getAssetSavePlaceId())) {
//            DictDto savePlaceDto = dictService.getCommonCode(model.getAssetSavePlaceId());
//            if (!VGUtility.isEmpty(savePlaceDto)) {
//                NewAssetSavePlaceStr = savePlaceDto.getCode() + " " + savePlaceDto.getChsName();
//            }
//        }

        for (int i = 0; i < assetIdList.size(); i++) {
//            AssetAssetDto asset = assetService.getAssetByAssetId(assetIdList.get(i));
//            ASSET_STATUS status = ASSET_STATUS.values()[VGUtility.toInteger(asset.getAssetStatusStr())];
//            if (status != ASSET_STATUS.使用 && status != ASSET_STATUS.借出 && status != ASSET_STATUS.闲置)
//                throw new RuntimeException(asset.getCombinationAssetName() + "当前资产不可以变更安装位置 ");
//
//            asset.setBeforeChangeAssetStatusStr(String.valueOf(status.ordinal()));
//            assetService.updateAssetBeforeAssetStatus(asset);

//            String OldAssetSavePlaceStr = "";
//            // 根据assetId查到assetDto,根据dto中的placeId查到dictionaryDto，再查到code和chsName
//            if (!VGUtility.isEmpty(asset.getSavePlaceId())) {
//                DictDto savePlaceDto = dictService.getCommonCode(asset.getSavePlaceId());
//                if (!VGUtility.isEmpty(savePlaceDto)) {
//                    OldAssetSavePlaceStr = savePlaceDto.getCode() + " " + savePlaceDto.getChsName();
//                }
//            }

            AssetSavePlaceChangeTempModel tempModel = new AssetSavePlaceChangeTempModel();
            tempModel.setAssetId(assetIdList.get(i));
            tempModel.setAssetSavePlaceChangeModelId(model.getId());

//            AssetHistoryDto historyDto = new AssetHistoryDto();
//            historyDto.setHistoryType(HISTORY_TYPE.变更记录);
//            historyDto.setChangeType(String.valueOf(CHANGE_TYPE.位置变更.ordinal()));
//            historyDto.setAssetModelId(tempModel.getAssetId());
//            historyDto.setCreateUserId(model.getCreateUserId());
//            historyDto.setChangeContent("位置变更:"
//                    + "[" + OldAssetSavePlaceStr + "]->"
//                    + "[" + NewAssetSavePlaceStr + "]");
//            assetService.createHistory(historyDto);

            savePlaceChangeTempDao.save(tempModel);
        }
        AssetSavePlaceChangeDto assetSavePlaceChangeDto = new AssetSavePlaceChangeDto();
        assetSavePlaceChangeDto.setId(save.getId());
        assetSavePlaceChangeDto.setChangeNum(save.getChangeNum());
        return assetSavePlaceChangeDto;
    }

    /***
     * 安装位置变更-保存并发起审批
     */
    @Override
    public void saveAndCheckChangeSavePlace(AssetSavePlaceChangeDto dto) {
        assetSavePlaceChangeDtoCommonCheck(dto);
        List<String> assetIdList = new ArrayList<String>();
        Arrays.stream(dto.getAssetIdListStr().split(",")).forEach(arr -> assetIdList.add(arr));
        AssetSavePlaceChangeModel model = new AssetSavePlaceChangeModel();
        model.setAssetSavePlaceId(dto.getAssetSavePlaceId());
        model.setReason(dto.getReason());
        model.setReceiptStatus(FlowableInfo.WORKSTATUS.已审批);
        model.setCreateUserId(((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo().getId());
        String changeNum = "LC";
        changeNum += VGUtility.toDateStr(new Date(), "yyyyMMdd");
        model.setChangeNum(getMaxChangeNumSavePlace(changeNum));
        savePlaceChangeDao.save(model);
//        String NewAssetSavePlaceStr = "";
//        // 根据assetId查到assetDto,根据dto中的placeId查到dictionaryDto，再查到code和chsName
//        if (!VGUtility.isEmpty(model.getAssetSavePlaceId())) {
//            DictDto savePlaceDto = dictService.getCommonCode(model.getAssetSavePlaceId());
//            if (!VGUtility.isEmpty(savePlaceDto)) {
//                NewAssetSavePlaceStr = savePlaceDto.getCode() + " " + savePlaceDto.getChsName();
//            }
//        }

        for (int i = 0; i < assetIdList.size(); i++) {
            AssetSavePlaceChangeTempModel tempModel = new AssetSavePlaceChangeTempModel();
            tempModel.setAssetId(assetIdList.get(i).trim());
            tempModel.setAssetSavePlaceChangeModelId(model.getId());
            //发起审批后,进入审批流程中,会冻结资产
//            AssetAssetDto asset = assetService.getAssetByAssetId(assetIdList.get(i).trim());
//            ASSET_STATUS status = ASSET_STATUS.values()[VGUtility.toInteger(asset.getAssetStatusStr())];
//            if (status != ASSET_STATUS.使用 && status != ASSET_STATUS.借出 && status != ASSET_STATUS.闲置)
//                throw new RuntimeException(asset.getCombinationAssetName() + "当前资产不可以变更安装位置 ");

//            String OldAssetSavePlaceStr = "";
//            // 根据assetId查到assetDto,根据dto中的placeId查到dictionaryDto，再查到code和chsName
//            if (!VGUtility.isEmpty(asset.getSavePlaceId())) {
//                DictDto savePlaceDto = dictService.getCommonCode(asset.getSavePlaceId());
//                if (!VGUtility.isEmpty(savePlaceDto)) {
//                    OldAssetSavePlaceStr = savePlaceDto.getCode() + " " + savePlaceDto.getChsName();
//                }
//            }
//            AssetHistoryDto historyDto = new AssetHistoryDto();
//            historyDto.setHistoryType(HISTORY_TYPE.变更记录);
//            historyDto.setChangeType(String.valueOf(CHANGE_TYPE.位置变更.ordinal()));
//            historyDto.setAssetModelId(tempModel.getAssetId());
//            historyDto.setCreateUserId(model.getCreateUserId());
//            historyDto.setChangeContent("位置变更:"
//                    + "[" + OldAssetSavePlaceStr + "]->"
//                    + "[" + NewAssetSavePlaceStr + "]");
//            assetService.createHistory(historyDto);
//
//            asset.setBeforeChangeAssetStatusStr(null);
//            asset.setAssetStatusStr(asset.getBeforeChangeAssetStatusStr());
//            assetService.updateAssetBeforeAssetStatus(asset);
            savePlaceChangeTempDao.save(tempModel);
        }
        //发起审批
        checkChangeSavePlaceReceipt(model.getId());
    }

    /***
     * 安装位置变更-字段校验
     * @param dto
     */
    private void assetSavePlaceChangeDtoCommonCheck(AssetSavePlaceChangeDto dto) {
        if (VGUtility.isEmpty(dto.getAssetSavePlaceId()))
            throw new RuntimeException("新资产安装位置不能为空！");
        if (VGUtility.isEmpty(dto.getReason()))
            throw new RuntimeException("变更原因不能为空！");
        if (VGUtility.isEmpty(dto.getAssetIdListStr()))
            throw new RuntimeException("资产列表不能为空！");
    }

    /***
     * 安装位置-获取列表信息
     */
    @Override
    public Map<String, Object> getAssetSavePlaceChangeForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto) {
        //通过hibernate获得Session
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        //通过session获得标准生成器 CriteriaBuilder
        CriteriaBuilder builder = session.getCriteriaBuilder();
        //标准化对象查询
        CriteriaQuery<AssetSavePlaceChangeModel> assetCriteria = builder.createQuery(AssetSavePlaceChangeModel.class);
        //获取根
        Root<AssetSavePlaceChangeModel> root = assetCriteria.from(AssetSavePlaceChangeModel.class);
        Predicate predicate = (Predicate) searchExpression.change(root, builder);
        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            assetCriteria.where((Predicate) predicate);
        //根据查询器添加顺序
        Order order = builder.asc(root.get("createTimestamp"));
        assetCriteria.orderBy(order);
        //创建查询器
        Query<AssetSavePlaceChangeModel> query = session.createQuery(assetCriteria);
        //分页
        if (!VGUtility.isEmpty(pageableDto))
            query.setFirstResult((pageableDto.getPage() - 1) * pageableDto.getSize()).setMaxResults(pageableDto.getSize());
        List<AssetSavePlaceChangeModel> modelList = query.getResultList();
        //convert
        List<AssetSavePlaceChangeDto> dtoList = new ArrayList<AssetSavePlaceChangeDto>();
        for (AssetSavePlaceChangeModel model : modelList) {
            AssetSavePlaceChangeDto dto = convertSavePlaceChangeModelToDto(model);
            dtoList.add(dto);
        }
        //total
        CriteriaQuery<Long> longCriteria = builder.createQuery(Long.class);
        root = longCriteria.from(AssetSavePlaceChangeModel.class);
        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            longCriteria.where((Predicate) predicate);
        longCriteria.select(builder.count(root));
        long total = session.createQuery(longCriteria).uniqueResult();

        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtoList);
        modelMap.addAttribute("total", total);
        return modelMap;
    }

    private AssetSavePlaceChangeDto convertSavePlaceChangeModelToDto(AssetSavePlaceChangeModel model) {
        AssetSavePlaceChangeDto dto = new AssetSavePlaceChangeDto();
        dto.setProduceType(model.getProduceType());
        dto.setId(model.getId());
        //根据assetId查到assetDto,根据dto中的placeId查到dictionaryDto，再查到code和chsName
        dto.setAssetSavePlaceId(model.getAssetSavePlaceId());
        if (!VGUtility.isEmpty(model.getAssetSavePlaceId())) {
            DictDto savePlaceDto = dictService.getCommonCode(model.getAssetSavePlaceId());
            if (!VGUtility.isEmpty(savePlaceDto)) {
                dto.setAssetSavePlaceStr(savePlaceDto.getCode() + " " + savePlaceDto.getChsName());
            }
        }
        dto.setReason(model.getReason());
        dto.setChangeNum(model.getChangeNum());
        dto.setReceiptStatus(model.getReceiptStatus().ordinal() + "");
        dto.setReceiptStatusStr(model.getReceiptStatus().name());
        dto.setCreateUserId(model.getCreateUserId());
        dto.setCreateUserIdStr(userService.getUserInfo(model.getCreateUserId()).getChsName());
        dto.setCreateTimestamp(VGUtility.toDateStr(model.getCreateTimestamp(), "yyyy-MM-dd"));
        String hql = "from AssetSavePlaceChangeTempModel where 1=1 ";
        if (!VGUtility.isEmpty(model.getId())) {
            hql += "and assetSavePlaceChangeModelId = " + "'" + model.getId() + "'";
        }
        TypedQuery<AssetSavePlaceChangeTempModel> query = entityManager.createQuery(hql, AssetSavePlaceChangeTempModel.class);
        List<AssetSavePlaceChangeTempModel> resultList = query.getResultList();
        String assetChange = "";
        List<String> assetIdList = new ArrayList<String>();
        for (AssetSavePlaceChangeTempModel tempModel : resultList) {
            //有问题
            hql = "from AssetAssetModel where id=" + "'" + tempModel.getAssetId().trim() + "'";
            assetIdList.add(tempModel.getAssetId());
            TypedQuery<AssetAssetModel> assetQuery = entityManager.createQuery(hql, AssetAssetModel.class);
            if (!VGUtility.isEmpty(assetQuery) && assetQuery.getResultList().size() > 0)
                assetChange += "、" + baseService.getAssetNameByMaterialCode(assetQuery.getResultList().get(0).getMaterialCode());
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
     * 安装位置变更-获取单号
     * @param changeNum
     * @return
     */
    public String getMaxChangeNumSavePlace(String changeNum) {
        String maxChangeNum = savePlaceChangeDao.getMaxChangeNum(changeNum + "%");
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
     * 安装位置变更-删除
     */
    @Override
    public void deleteChangeSavePlaceReceipt(String id) {
        if (VGUtility.isEmpty(savePlaceChangeDao.findById(id))) {
            throw new RuntimeException("该安装位置变更申请单不存在！");
        } else {
            savePlaceChangeDao.deleteById(id);
            String hql = "from AssetSavePlaceChangeTempModel where assetSavePlaceChangeModelId=" + "'" + id + "'";
            TypedQuery<AssetSavePlaceChangeTempModel> assetQuery = entityManager.createQuery(hql, AssetSavePlaceChangeTempModel.class);
            if (assetQuery.getResultList().size() > 0) {
                for (AssetSavePlaceChangeTempModel model : assetQuery.getResultList()) {
//					//修改资产原有状态
//					AssetAssetDto asset=assetService.getAssetByAssetId(model.getAssetId());
//					asset.setAssetStatusStr(asset.getBeforeChangeAssetStatusStr());
//					asset.setBeforeChangeAssetStatusStr(null);
//					assetService.updateAssetBeforeAssetStatus(asset);
//					
                    savePlaceChangeTempDao.deleteById(model.getId());
                }
            } else {
                throw new RuntimeException("该安装位置变更申请单不存在！");
            }
        }
    }

    /***
     * 安装位置变更-根据ID获取单条信息
     */
    @Override
    public AssetSavePlaceChangeDto getSavePlaceChangeReceiptById(String id) {
        if (VGUtility.isEmpty(id)) {
            return null;
        } else {
            Optional<AssetSavePlaceChangeModel> model = savePlaceChangeDao.findById(id);
            if (!VGUtility.isEmpty(model)) {
                AssetSavePlaceChangeDto dto = convertSavePlaceChangeModelToDto(model.get());
                return dto;
            } else {
                return null;
            }
        }
    }

    /***
     * 安装位置变更-发起审批
     */
    @Override
    public void checkChangeSavePlaceReceipt(String id) {
        if (VGUtility.isEmpty(savePlaceChangeDao.findById(id))) {
            throw new RuntimeException("该使用人变更申请单不存在！");
        } else {
            // 变更-安装位置-审核状态
            AssetSavePlaceChangeModel model = savePlaceChangeDao.findById(id).get();
            model.setReceiptStatus(FlowableInfo.WORKSTATUS.已审批);
            savePlaceChangeDao.save(model);
            // 变更相关资产安装位置信息
            AssetSavePlaceChangeTempModel temp = new AssetSavePlaceChangeTempModel();
            temp.setAssetSavePlaceChangeModelId(model.getId());
            // jpa通过findAll方法的Example类型，可按照查询任意字段属性查询；
            Example<AssetSavePlaceChangeTempModel> example = Example.of(temp);
            List<AssetSavePlaceChangeTempModel> existList = savePlaceChangeTempDao.findAll(example);
            for (int i = 0; i < existList.size(); i++) {
                AssetAssetModel a = assetAssetDao.findById(existList.get(i).getAssetId()).get();
                a.setOldSavePlaceId(a.getSavePlaceId());
                a.setSavePlaceId(model.getAssetSavePlaceId());
                assetAssetDao.save(a);
            }
        }
    }

    /***
     * 安装位置变更-编辑更新
     */
    @Override
    public void updateChangeSavePlaceReceipt(AssetSavePlaceChangeDto dto) {
        assetSavePlaceChangeDtoCommonCheck(dto);
        List<String> assetIdList = new ArrayList<String>();
        Arrays.stream(dto.getAssetIdListStr().split(",")).forEach(arr -> assetIdList.add(arr));
        if (VGUtility.isEmpty(dto.getId())) {
            throw new RuntimeException("该资产安装位置变更申请单不存在。");
        } else {
            // 更新-安装位置变更台账
            AssetSavePlaceChangeModel model = savePlaceChangeDao.findById(dto.getId()).get();
            model.setAssetSavePlaceId(dto.getAssetSavePlaceId());
            model.setReason(dto.getReason());
            model.setReceiptStatus(FlowableInfo.WORKSTATUS.拟稿);
            model.setProduceType(dto.getProduceType());
            savePlaceChangeDao.save(model);
            //更新-安装位置变更台账明细表信息
            AssetSavePlaceChangeTempModel temp = new AssetSavePlaceChangeTempModel();
            temp.setAssetSavePlaceChangeModelId(model.getId());
            // jpa通过findAll方法的Example类型，可按照查询任意字段属性查询；
            Example<AssetSavePlaceChangeTempModel> example = Example.of(temp);
            List<AssetSavePlaceChangeTempModel> existList = savePlaceChangeTempDao.findAll(example);
            String NewAssetSavePlaceStr = "";
            // 根据assetId查到assetDto,根据dto中的placeId查到dictionaryDto，再查到code和chsName
            if (!VGUtility.isEmpty(model.getAssetSavePlaceId())) {
                DictDto savePlaceDto = dictService.getCommonCode(model.getAssetSavePlaceId());
                if (!VGUtility.isEmpty(savePlaceDto)) {
                    NewAssetSavePlaceStr = savePlaceDto.getCode() + " " + savePlaceDto.getChsName();
                }
            }

            for (int i = 0; i < existList.size(); i++) {
                // assetIdList 中去除表中已存在明细，避免重复添加；明细表去除assetIdList中不包含的明细数据，即前台已删除信息
                if (assetIdList.contains(existList.get(i).getAssetId())) {
                    assetIdList.remove(existList.get(i).getAssetId());
                } else {
                    //发起审批后,进入审批流程中,会冻结资产
//                    AssetAssetDto asset = assetService.getAssetByAssetId(existList.get(i).getAssetId());
//                    String OldAssetSavePlaceStr = "";
//                    // 根据assetId查到assetDto,根据dto中的placeId查到dictionaryDto，再查到code和chsName
//                    if (!VGUtility.isEmpty(asset.getSavePlaceId())) {
//                        DictDto savePlaceDto = dictService.getCommonCode(asset.getSavePlaceId());
//                        if (!VGUtility.isEmpty(savePlaceDto)) {
//                            OldAssetSavePlaceStr = savePlaceDto.getCode() + " " + savePlaceDto.getChsName();
//                        }
//                    }
//                    AssetHistoryDto historyDto = new AssetHistoryDto();
//                    historyDto.setHistoryType(HISTORY_TYPE.变更记录);
//                    historyDto.setChangeType(String.valueOf(CHANGE_TYPE.位置变更.ordinal()));
//                    historyDto.setAssetModelId(asset.getId());
//                    historyDto.setCreateUserId(model.getCreateUserId());
//                    historyDto.setChangeContent("位置变更:"
//                            + "[" + OldAssetSavePlaceStr + "]->"
//                            + "[" + NewAssetSavePlaceStr + "]");
//                    assetService.createHistory(historyDto);

//                    asset.setBeforeChangeAssetStatusStr(null);
//                    asset.setAssetStatusStr(asset.getBeforeChangeAssetStatusStr());
//                    assetService.updateAssetBeforeAssetStatus(asset);

                    savePlaceChangeTempDao.deleteById(existList.get(i).getId());
                }
            }
            for (int i = 0; i < assetIdList.size(); i++) {
                //发起审批后,进入审批流程中,会冻结资产
                AssetAssetDto asset = assetService.getAssetByAssetId(assetIdList.get(i).trim());
                ASSET_STATUS status = ASSET_STATUS.values()[VGUtility.toInteger(asset.getAssetStatusStr())];
                if (status != ASSET_STATUS.使用 && status != ASSET_STATUS.借出 && status != ASSET_STATUS.闲置)
                    throw new RuntimeException(asset.getCombinationAssetName() + "当前资产不可以变更安装位置 ");

                asset.setBeforeChangeAssetStatusStr(asset.getAssetStatusStr());
                assetService.updateAssetBeforeAssetStatus(asset);

                AssetSavePlaceChangeTempModel tempModel = new AssetSavePlaceChangeTempModel();
                tempModel.setAssetId(assetIdList.get(i).trim());
                tempModel.setAssetSavePlaceChangeModelId(model.getId().trim());
                savePlaceChangeTempDao.save(tempModel);
            }
        }
    }

    /******************************************************资产安装位置变更 end************************************************************/

}

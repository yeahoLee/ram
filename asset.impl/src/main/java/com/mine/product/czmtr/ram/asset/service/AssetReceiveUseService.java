package com.mine.product.czmtr.ram.asset.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mine.base.user.dto.DeptInfoDto;
import com.mine.base.user.dto.UserInfoDto;
import com.mine.base.user.service.IUserService;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dao.AssetAssetDao;
import com.mine.product.czmtr.ram.asset.dao.AssetReceiveUseDao;
import com.mine.product.czmtr.ram.asset.dao.AssetReceiveUseTempDao;
import com.mine.product.czmtr.ram.asset.dto.*;
import com.mine.product.czmtr.ram.asset.model.AssetAssetModel;
import com.mine.product.czmtr.ram.asset.model.AssetReceiveUseModel;
import com.mine.product.czmtr.ram.asset.model.AssetReceiveUseTempModel;
import com.mine.product.czmtr.ram.asset.service.IAssetService.ASSET_STATUS;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.VGUtility;
import org.aspectj.lang.annotation.Aspect;
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

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AssetReceiveUseService implements IAssetReceiveUseService {

    @Autowired
    private AssetReceiveUseDao assetReceiveUseDao;
    @Autowired
    private AssetAssetDao assetDao;
    @Autowired
    private AssetReceiveUseTempDao assetReceiveUseTempDao; // 资产
    @Autowired
    private IUserService userService;
    @Autowired
    private IAssetService assetService;
    @Autowired
    private IAssetReceiveUseTempService assetReceiveUseTempService;
    @Autowired
    private IBaseService baseService;
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 通用检查
     *
     * @param dto
     */
    @Override
    public void commonCheckAssetReceiveUseDto(AssetReceiveUseDto dto) {
        if (VGUtility.isEmpty(dto.getAssetReceiveUseDepartmentID()))
            throw new RuntimeException("领用部门为必填项，不能为空！");
        else {
            try {
                baseService.getDeptInfo(dto.getAssetReceiveUseDepartmentID());
            } catch (Exception ex) {
                throw new RuntimeException("领用部门格式不正确！");
            }
        }
        if (VGUtility.isEmpty(dto.getAssetReceiveUseUserID()))
            throw new RuntimeException("领用人为必填项，不能为空！");
        else {
            try {
                userService.getUserInfo(dto.getAssetReceiveUseUserID());
            } catch (Exception ex) {
                throw new RuntimeException("领用人格式不正确！");
            }
        }
        if (VGUtility.isEmpty(dto.getReason()))
            throw new RuntimeException("领用事由为必填项，不能为空！");
//		if (VGUtility.isEmpty(dto.getReceiveTime()))
//			throw new RuntimeException("拟归还日期为必填项，不能为空！");
//		else {
//			try {VGUtility.toDateObj(dto.getReceiveTime(), "yyyy/M/d");}
//			catch(Exception ex) {throw new RuntimeException("拟归还日期格式不正确！");}
//		}
    }

    /**
     * 添加
     */
    @Override
    public AssetReceiveUseDto createAssetReceiveUse(String assetReceiveUseDtoTempDtoList, AssetReceiveUseDto dto, UserInfoDto userInfoDto) {
        commonCheckAssetReceiveUseDto(dto);

        ArrayList<AssetReceiveUseTempDto> AssetReceiveUseTempDtoList = new ArrayList<AssetReceiveUseTempDto>();
        if ((!VGUtility.isEmpty(assetReceiveUseDtoTempDtoList))) {
            AssetReceiveUseTempDtoList = JSON.parseObject(assetReceiveUseDtoTempDtoList, new TypeReference<ArrayList<AssetReceiveUseTempDto>>() {
            });
        } else {
            throw new RuntimeException("资产列表不能为空！");
        }
        AssetReceiveUseModel model = convertAssetReceiveUseModel(dto);
        model.setCreateUserID(userInfoDto.getId());
        model.setAssetReceiveUseCode(getAssetReceiveUseCode());
        model.setReceiptStatus(FlowableInfo.WORKSTATUS.拟稿);
        model.setRevertStatus(ASSETRECEIVEUSE_REVERTSTATUS.未归还);
        model = assetReceiveUseDao.save(model);
        assetReceiveUseTempService.createAssetReceiveUseTempForList(AssetReceiveUseTempDtoList, model.getId());

        return convertModelToDto(model);
    }

    /**
     * 修改
     */
    @Override
    public AssetReceiveUseDto updateAssetReceiveUse(AssetReceiveUseDto dto) {
        commonCheckAssetReceiveUseDto(dto);

        if (VGUtility.isEmpty(dto.getAssetReceiveUseCode()))
            throw new RuntimeException("领用编号不能为空！");

        AssetReceiveUseModel model = assetReceiveUseDao.findById(dto.getId()).get();
        List<AssetReceiveUseTempDto> dtolist = assetReceiveUseTempService.getAssetReceiveUseTempDtoList(model.getId());
        if (dtolist.size() <= 0)
            throw new RuntimeException("资产列表不能为空！");
        if (!VGUtility.isEmpty(dto.getAssetReceiveUseDepartmentID()))
            model.setAssetReceiveUseDepartmentID(dto.getAssetReceiveUseDepartmentID());
        if (!VGUtility.isEmpty(dto.getAssetReceiveUseUserID()))
            model.setAssetReceiveUseUserID(dto.getAssetReceiveUseUserID());
        if (!VGUtility.isEmpty(dto.getReceiveTime()))
            model.setReceiveTime(VGUtility.toDateObj(dto.getReceiveTime(), "yyyy/M/d"));
        if (!VGUtility.isEmpty(dto.getReason()))
            model.setReason(dto.getReason());
        if (!VGUtility.isEmpty(dto.getAssetReceiveUseCode()))
            model.setAssetReceiveUseCode(dto.getAssetReceiveUseCode());
            model.setAssetReceiveUseCode(dto.getAssetReceiveUseCode());
        if (!VGUtility.isEmpty(dto.getCreateUserID()))
            model.setCreateUserID(dto.getCreateUserID());
        if (!VGUtility.isEmpty(dto.getReceiptStatus()))
            model.setReceiptStatus(dto.getReceiptStatus());
        if (!VGUtility.isEmpty(dto.getRevertStatus()))
            model.setRevertStatus(dto.getRevertStatus());
        if (!VGUtility.isEmpty(dto.getProduceType()))
            model.setProduceType(dto.getProduceType());
        return convertModelToDto(assetReceiveUseDao.save(model));
    }

    ;

    // 模拟发送审批
    @Override
    public AssetReceiveUseDto updateAssetReceiveUseStatus(AssetReceiveUseDto dto) {
        commonCheckAssetReceiveUseDto(dto);

        if (VGUtility.isEmpty(dto.getAssetReceiveUseCode()))
            throw new RuntimeException("领用编号不能为空！");

        AssetReceiveUseModel model = assetReceiveUseDao.findById(dto.getId()).get();
        if (!VGUtility.isEmpty(dto.getReceiptStatus()))
            model.setReceiptStatus(dto.getReceiptStatus());
        AssetReceiveUseDto assetReceiveUseDto = convertModelToDto(assetReceiveUseDao.save(model));
        List<AssetReceiveUseTempDto> dtolist = assetReceiveUseTempService.getAssetReceiveUseTempDtoList(model.getId());
        if (dtolist.size() <= 0)
            throw new RuntimeException("资产列表不能为空！");
        for (AssetReceiveUseTempDto temp : dtolist) {
            AssetAssetDto asset = assetService.getAssetByAssetId(temp.getAssetId());
            Boolean flag = assetService.getAssetStatus(ASSET_STATUS.values()[VGUtility.toInteger(asset.getAssetStatusStr())], ASSET_STATUS.使用);
            if (!flag)
                throw new RuntimeException(asset.getCombinationAssetName() + "当前资产不可以领用 ");
            if (assetReceiveUseDto.getReceiptStatus() == FlowableInfo.WORKSTATUS.已审批) {
                asset.setAssetStatusStr(String.valueOf(ASSET_STATUS.使用.ordinal()));
                asset.setBeforeChangeAssetStatusStr(null);
            }
            assetService.updateAssetBeforeAssetStatus(asset);
        }

        return assetReceiveUseDto;
    }

    ;

    /**
     * 删除
     */
    @Override
    public void deleteAssetReceiveUse(String assetReceiveUseID) {
        AssetReceiveUseModel model = assetReceiveUseDao.findById(assetReceiveUseID).get();
        List<AssetReceiveUseTempDto> dtolist = assetReceiveUseTempService.getAssetReceiveUseTempDtoList(model.getId());
		/*if (!VGUtility.isEmpty(dtolist)) {
			for (AssetReceiveUseTempDto temp : dtolist) {
				if (!VGUtility.isEmpty(temp.getAssetId())) {
					AssetAssetDto asset = assetService.getAssetByAssetId(temp.getAssetId());
					asset.setAssetStatusStr(asset.getBeforeChangeAssetStatusStr());//将资产置为使用前的状态；
					asset.setBeforeChangeAssetStatusStr(null);
					assetService.updateAssetBeforeAssetStatus(asset);
				};
				
			}
		}*/
        assetReceiveUseTempDao.deleteAssetReceiveUseTempModelByAssetReceiveUseModel(model);
        ;
        assetReceiveUseDao.deleteById(assetReceiveUseID);
    }

    ;

    /**
     * 查询
     */
    @Override
    public ModelMap getAssetReceiveUseDtoByQuerysForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto) {
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<AssetReceiveUseModel> assetReceiveUseCriteria = builder.createQuery(AssetReceiveUseModel.class);
        Root<AssetReceiveUseModel> root = assetReceiveUseCriteria.from(AssetReceiveUseModel.class);
        Predicate predicate = (Predicate) searchExpression.change(root, builder);

        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            assetReceiveUseCriteria.where((Predicate) predicate);

        assetReceiveUseCriteria.orderBy(builder.desc(root.get("assetReceiveUseCode")));
        Query<AssetReceiveUseModel> query = session.createQuery(assetReceiveUseCriteria);
        //分页
        if (!VGUtility.isEmpty(pageableDto))
            query.setFirstResult((pageableDto.getPage() - 1) * pageableDto.getSize()).setMaxResults(pageableDto.getSize());
        List<AssetReceiveUseModel> modelList = query.getResultList();
        //total
        CriteriaQuery<Long> longCriteria = builder.createQuery(Long.class);
        root = longCriteria.from(AssetReceiveUseModel.class);
        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            longCriteria.where((Predicate) predicate);
        longCriteria.select(builder.count(root));
        long total = session.createQuery(longCriteria).uniqueResult();
        //convert
        List<AssetReceiveUseDto> dtoList = new ArrayList<AssetReceiveUseDto>();
        for (AssetReceiveUseModel model : modelList) {
            dtoList.add(convertAssetBorrowTempStr(model));
        }
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtoList);
        modelMap.addAttribute("total", total);
        return modelMap;
    }

    /***
     * 资产领用列表查询
     */
    @Override
    public Map<String, Object> getAssetReceiveUseByForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto, AssetReceiveUseDto assetReceiveUseDto) {
        int pagesize = pageableDto.getSize();
        int page = pageableDto.getPage();
        List<AssetReceiveUseDto> dtoList = (List<AssetReceiveUseDto>) getAssetReceiveUseDtoByQuerysForDataGrid(searchExpression, null).get("rows");
        if (!VGUtility.isEmpty(assetReceiveUseDto.getAssetCode()))
            dtoList = dtoList.stream().filter(a -> Objects.nonNull(a.getAssetCode()))
                    .filter(a -> a.getAssetCode().contains(assetReceiveUseDto.getAssetCode()))
                    .collect(Collectors.toList());
        if (!VGUtility.isEmpty(assetReceiveUseDto.getAssetReceiveUseTempStr()))
            dtoList = dtoList.stream().filter(a -> Objects.nonNull(a.getAssetCode()))
                    .filter(a -> a.getAssetReceiveUseTempStr().contains(assetReceiveUseDto.getAssetReceiveUseTempStr()))
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
     * 根据Model获取dto,包含资产的名称以及code
     * @param model
     * @return
     */
    public AssetReceiveUseDto convertAssetBorrowTempStr(AssetReceiveUseModel model) {
        AssetReceiveUseDto assetDto = convertModelToDto(model);
        List<AssetReceiveUseTempModel> resultList = assetReceiveUseTempDao.findAssetReceiveUseTempModelsByAssetReceiveUseModel(model);
        String str = "";
        String assetCodestr = "";
        for (AssetReceiveUseTempModel temp : resultList) {
            AssetAssetModel assetModel = assetDao.findAssetAssetModelById(temp.getAssetId().trim());
            if (!VGUtility.isEmpty(assetModel)) {
                assetCodestr += "," + assetModel.getAssetCode();
                str += "," + baseService.getAssetNameByMaterialCode(assetModel.getMaterialCode());
            }
        }
        if (!VGUtility.isEmpty(assetCodestr))
            assetDto.setAssetCode(assetCodestr.substring(1, assetCodestr.length()));
        if (!VGUtility.isEmpty(str))
            assetDto.setAssetReceiveUseTempStr(str.substring(1, str.length()));

        return assetDto;
    }

    /***
     * 获取资产领用单信息
     */
    @Override
    public AssetReceiveUseDto getAssetReceiveUseDto(String id) {
        AssetReceiveUseModel model = assetReceiveUseDao.findById(id).get();
        return convertModelToDto(model);
    }


    /**
     * 数据转换 convert model to dto convert dto to model
     */
    @Override
    public AssetReceiveUseModel convertAssetReceiveUseModel(AssetReceiveUseDto dto) {
        AssetReceiveUseModel model = new AssetReceiveUseModel();
        BeanUtils.copyProperties(dto, model);
        return model;
    }

    private AssetReceiveUseDto convertModelToDto(AssetReceiveUseModel model) {
        AssetReceiveUseDto dto = new AssetReceiveUseDto();

        BeanUtils.copyProperties(model, dto);

        dto.setReceiveTime(VGUtility.toDateStr(model.getReceiveTime(), "yyyy/M/d"));
        dto.setReceiveTimeStr(VGUtility.toDateStr(model.getReceiveTime(), "yyyy-MM-dd"));
        dto.setReceiptIndex(model.getReceiptStatus().ordinal());
        if (!VGUtility.isEmpty(model.getRevertStatus()))
            dto.setRevertStatusStr(model.getRevertStatus().ordinal());
        dto.setCreateTimestamp(VGUtility.toDateStr(model.getCreateTimestamp(), "yyyy-MM-dd"));
        if (!VGUtility.isEmpty(userService.getUserInfo(model.getCreateUserID())))
            dto.setCreateUserName(userService.getUserInfo(model.getCreateUserID()).getChsName());
        if (!VGUtility.isEmpty(userService.getUserInfo(model.getAssetReceiveUseUserID())))
            dto.setAssetReceiveUseUserName(userService.getUserInfo(model.getAssetReceiveUseUserID()).getChsName());
        if (!VGUtility.isEmpty(baseService.getDeptInfo(model.getAssetReceiveUseDepartmentID())))
            dto.setAssetReceiveUseDepartmentName(((DeptInfoDto) baseService.getDeptInfo(model.getAssetReceiveUseDepartmentID())).getDeptName());
        return dto;
    }

    // 生成借用编号
    @Override
    public String getAssetReceiveUseCode() {
        Date time = Calendar.getInstance().getTime();
        String date = VGUtility.toDateStr(time, "yyyy-MM-dd");
        String hql = "from AssetReceiveUseModel where to_char(createtimestamp,'yyyy-MM-dd')='" + date
                + "' order by assetReceiveUseCode desc";
        TypedQuery<AssetReceiveUseModel> query = entityManager.createQuery(hql, AssetReceiveUseModel.class);

        List<AssetReceiveUseModel> modelList = query.getResultList();
        String code = "";
        if (modelList.size() > 0) {
            String ReceiveUse = modelList.get(0).getAssetReceiveUseCode();
            if (!VGUtility.isEmpty(ReceiveUse)) {
                code = ReceiveUse;
                Long num = Long.parseLong(code.substring(2)) + 1;
                code = code.substring(0, 2) + num;
            } else {
                code = "LY" + date.replace("-", "") + "001";
            }
        } else {
            code = "LY" + date.replace("-", "") + "001";
        }

        return code;
    }

    @Override
    public AssetApprove doApproveNotify(String id) {
        List<String> assetIdList = assetReceiveUseTempService.getAssetIdList(id);
        //校验资产状态，是否可以审批；
        return assetService.doAssetApproveCheck(assetIdList, ASSET_STATUS.使用);
    }


    @Override
    public void approveUpdateNotify(FlowableInfo.WORKSTATUS workStatus, String id) {
        List<String> assetIdList;
        switch (workStatus) {
	        case 审批中:
		        //资产冻结
		        assetIdList = assetReceiveUseTempService.getAssetIdList(id);
		        assetService.LockAssetState(assetIdList);
		        break;
            case 驳回:
                //资产状态回滚
                assetIdList = assetReceiveUseTempService.getAssetIdList(id);
                assetService.approveFailureAssetStateRollback(assetIdList);
                break;
            case 已审批:
                //将资产状态落地
                AssetReceiveUseModel model = assetReceiveUseDao.findById(id).get();
                assetIdList = assetReceiveUseTempService.getAssetIdList(id);
                Map<String, String> map = assetReceiveUseTempService.getAssetMap(id);
                assetService.approveSuccessUpdateAssetState(assetIdList, ASSET_STATUS.使用);
                assetService.doAssetUserDepAndPlaceUpdate(map, model.getAssetReceiveUseUserID(), model.getAssetReceiveUseDepartmentID());
                this.createHistory(model, assetIdList);
                break;
            default:
                break;

        }
        //更新主表状态
        AssetReceiveUseModel model = assetReceiveUseDao.findById(id).get();
        model.setReceiptStatus(workStatus);

    }

    private void createHistory(AssetReceiveUseModel model, List<String> assetIdList) {

        DeptInfoDto receiveUseDepartment = new DeptInfoDto();
        UserInfoDto receiveUseUser = new UserInfoDto();
        if (!VGUtility.isEmpty(model.getAssetReceiveUseDepartmentID())) {
            receiveUseDepartment = (DeptInfoDto) baseService.getDeptInfo(model.getAssetReceiveUseDepartmentID());
        }
        if (!VGUtility.isEmpty(model.getAssetReceiveUseUserID())) {
            receiveUseUser = userService.getUserInfo(model.getAssetReceiveUseUserID());
        }

        //创建历史记录
        for (String assetId : assetIdList) {
            AssetHistoryDto historyDto = new AssetHistoryDto();
            historyDto.setHistoryType(IAssetService.HISTORY_TYPE.变更记录);
            historyDto.setChangeType(String.valueOf(IAssetService.CHANGE_TYPE.资产领用.ordinal()));
            historyDto.setAssetModelId(assetId);
            historyDto.setCreateUserId(model.getCreateUserID());
            historyDto.setChangeContent("操作[资产领用]"
                    + "[" + receiveUseDepartment.getDeptName() + "]"
                    + receiveUseUser.getChsName() + "领用");
            assetService.createHistory(historyDto);
        }

    }

}

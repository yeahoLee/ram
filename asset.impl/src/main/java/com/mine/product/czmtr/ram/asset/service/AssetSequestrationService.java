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
import com.mine.product.czmtr.ram.asset.dao.AssetAssetDao;
import com.mine.product.czmtr.ram.asset.dao.AssetSequestrationDao;
import com.mine.product.czmtr.ram.asset.dao.AssetSequestrationTempDao;
import com.mine.product.czmtr.ram.asset.dto.AssetAssetDto;
import com.mine.product.czmtr.ram.asset.dto.AssetSequestrationDto;
import com.mine.product.czmtr.ram.asset.dto.AssetSequestrationTempDto;
import com.mine.product.czmtr.ram.asset.dto.FlowableInfo;
import com.mine.product.czmtr.ram.asset.model.AssetAssetModel;
import com.mine.product.czmtr.ram.asset.model.AssetSequestrationModel;
import com.mine.product.czmtr.ram.asset.model.AssetSequestrationTempModel;
import com.mine.product.czmtr.ram.asset.service.IAssetService.ASSET_STATUS;
import com.mine.product.czmtr.ram.asset.service.IAssetService.SEALED_UNSEALED;
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
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AssetSequestrationService implements IAssetSequestrationService {

    @Autowired
    private AssetSequestrationDao assetSequestrationDao;

    @Autowired
    private IAssetSequestrationTempService assetSequestrationTempService;

    @Autowired
    private AssetSequestrationTempDao assetSequestrationTempDao;

    @Autowired
    private AssetAssetDao assetAssetDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private IBaseService baseService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IDictService dictService;

    // 通用检查
    @Override
    public void commonCheckAssetSequestrationDto(AssetSequestrationDto dto) {
        if (VGUtility.isEmpty(dto.getSealReason()))
            throw new RuntimeException("封存原因为必填项，不能为空！");
    }

    @Override
    public Map<String, Object> getAssetSequestrationForDataGrid(ISearchExpression searchExpression, AssetSequestrationDto assetSequestrationDto
            , PageableDto pageableDto) {
        List<AssetSequestrationDto> dtoList = new ArrayList<AssetSequestrationDto>();
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<AssetSequestrationModel> assetSequestrationCriteria = builder.createQuery(AssetSequestrationModel.class);
        Root<AssetSequestrationModel> root = assetSequestrationCriteria.from(AssetSequestrationModel.class);
        Predicate predicate = (Predicate) searchExpression.change(root, builder);

        if (!VGUtility.isEmpty(predicate) && predicate instanceof Predicate)
            assetSequestrationCriteria.where((Predicate) predicate);
        assetSequestrationCriteria.orderBy(builder.desc(root.get("createTimestamp")));
        Query<AssetSequestrationModel> query = session.createQuery(assetSequestrationCriteria);

        //分页
        if (!VGUtility.isEmpty(pageableDto))
            query.setFirstResult((pageableDto.getPage() - 1) * pageableDto.getSize()).setMaxResults(pageableDto.getSize());

        List<AssetSequestrationModel> modelList = query.getResultList();

        for (AssetSequestrationModel assetSequestrationModel : modelList) {
            dtoList.add(convertAssetSequestrationModelToDto(assetSequestrationModel));
        }

        //total
        CriteriaQuery<Long> longCriteria = builder.createQuery(Long.class);
        root = longCriteria.from(AssetSequestrationModel.class);
        longCriteria.select(builder.count(root));
        long total = session.createQuery(longCriteria).uniqueResult();

        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtoList);
        modelMap.addAttribute("total", total);
        return modelMap;
    }

    @Override
    public Map<String, Object> getAssetSealForUpdateDataGrid(AssetSequestrationDto assetSequestrationDto,
                                                             PageableDto pageableDto) {
        if (VGUtility.isEmpty(assetSequestrationDto.getId()))
            throw new RuntimeException("封存单不能为空！");

        List<AssetSequestrationTempDto> resList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        List<AssetSequestrationTempDto> list = getAssetSeal(assetSequestrationDto.getId());
        int total = 0;
        if (!list.isEmpty()) {
            for (AssetSequestrationTempDto assetSequestrationTempDto : list) {
                AssetSequestrationTempDto dto = new AssetSequestrationTempDto();
                AssetAssetModel model = assetAssetDao.findById(assetSequestrationTempDto.getAssetId()).get();
                dto = convertAssetToassetSequestrationTempDto(model, assetSequestrationTempDto);
                resList.add(dto);
            }
            total = list.size();
        }

        map.put("rows", resList);
        map.put("total", total);
        return map;
    }

    private AssetSequestrationTempDto convertAssetToassetSequestrationTempDto(AssetAssetModel model,
                                                                              AssetSequestrationTempDto dto) {
        // TODO Auto-generated method stub
        dto.setAssetCode(model.getAssetCode());
        dto.setMaterialCode(model.getMaterialCode());
        dto.setAssetType(model.getAssetType().toString());
        if (!VGUtility.isEmpty(model.getSpecAndModels()))
            dto.setSpecAndModels(model.getSpecAndModels().toString());
        dto.setSeriesNum(model.getSeriesNum());
        dto.setUnitOfMeasId(model.getUnitOfMeasId());
        if (!VGUtility.isEmpty(model.getUnitOfMeasId())) {
            DictDto dictCommonDto = dictService.getCommonCode(model.getUnitOfMeasId());
            if (!VGUtility.isEmpty(dictCommonDto))
                dto.setUnitOfMeasStr(dictCommonDto.getChsName());
        }
        dto.setAssetBrand(model.getAssetBrand());
        dto.setTechPara(model.getTechPara());
        dto.setPurcPrice(model.getPurcPrice() + "");
        dto.setEquiOrigValue(model.getEquiOrigValue() + "");
        dto.setProduceStr(model.getProduceType().name());

        //延伸信息
        dto.setCompanyId(model.getCompanyId());
        if (!VGUtility.isEmpty(model.getCompanyId())) {
            DeptInfoDto deptDto = (DeptInfoDto) baseService.getDeptInfo(model.getCompanyId());
            if (!VGUtility.isEmpty(deptDto))
                dto.setCompanyStr(deptDto.getDeptName());
        }
        dto.setBelongLine(model.getBelongLine());
        if (!VGUtility.isEmpty(model.getBelongLine())) {
            DictDto dictCommonDto = dictService.getCommonCode(model.getBelongLine());
            if (!VGUtility.isEmpty(dictCommonDto))
                dto.setBelongLineStr(dictCommonDto.getChsName());
        }
        dto.setBuyDate(VGUtility.toDateStr(model.getBuyDate(), "yyyy/M/d"));
        dto.setManageDeptId(model.getManageDeptId());
        if (!VGUtility.isEmpty(model.getManageDeptId())) {
            DeptInfoDto deptDto = (DeptInfoDto) baseService.getDeptInfo(model.getManageDeptId());
            if (!VGUtility.isEmpty(deptDto))
                dto.setManageDeptStr(deptDto.getDeptName());
        }
        dto.setManagerId(model.getManagerId());
        if (!VGUtility.isEmpty(model.getManagerId())) {
            UserInfoDto userDto = userService.getUserInfo(model.getManagerId());
            if (!VGUtility.isEmpty(userDto))
                dto.setManagerStr(userDto.getChsName());
        }
        dto.setAssetSource(model.getAssetSource());
        dto.setContractNum(model.getContractNum());
        dto.setTendersNum(model.getTendersNum());
        if (!VGUtility.isEmpty(model.getMainPeriod()))
            dto.setMainPeriod(VGUtility.toDateStr(model.getMainPeriod(), "yyyy/M/d"));
        dto.setSourceUser(model.getSourceUser());
        dto.setSourceContactInfo(model.getSourceContactInfo());
        if (!VGUtility.isEmpty(model.getProdTime()))
            dto.setProdTime(VGUtility.toDateStr(model.getProdTime(), "yyyy/M/d"));
        dto.setAssetStatus(model.getAssetStatus().name());
        dto.setAssetStatusStr(Integer.toString(model.getAssetStatus().ordinal()));
        dto.setRecId(model.getRecId());
        //根据assetId查到assetDto,根据dto中的placeId查到dictionaryDto，再查到code和chsName
        dto.setSavePlaceId(model.getSavePlaceId());
        if (!VGUtility.isEmpty(model.getSavePlaceId())) {
            DictDto savePlaceDto = dictService.getCommonCode(model.getSavePlaceId());
            if (!VGUtility.isEmpty(savePlaceDto)) {
                dto.setSavePlaceCode(savePlaceDto.getCode());
                dto.setSavePlaceName(savePlaceDto.getChsName());
                dto.setSavePlaceStr(savePlaceDto.getCode() + " " + savePlaceDto.getChsName());
            }
        }
        //根据物资编码查询并将查询结果组合为资产类别和资产名称，然后set到dto中
        String assetTypeName = baseService.getAssetTypeByMaterialCode(model.getMaterialCode());
        if (!VGUtility.isEmpty(assetTypeName))
            dto.setCombinationAssetType(assetTypeName);

        String assetName = baseService.getAssetNameByMaterialCode(model.getMaterialCode());
        if (!VGUtility.isEmpty(assetName))
            dto.setCombinationAssetName(assetName);
        return dto;
    }

    private AssetSequestrationDto convertAssetSequestrationModelToDto(AssetSequestrationModel model) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        AssetSequestrationDto dto = new AssetSequestrationDto();
        dto.setId(model.getId());
        dto.setSealStatus(model.getSealStatus());
        dto.setLaunchDate(model.getLaunchDate());
        if (!VGUtility.isEmpty(model.getLaunchDate()))
            dto.setLaunchDateStr(dateFormat.format(model.getLaunchDate()));
        dto.setSequestrateNum(model.getSequestrateNum());
        dto.setSealReason(model.getSealReason());
        dto.setSealPlaceId(model.getSealPlaceId());
        dto.setSealApproveStatus(model.getSealApproveStatus());
        dto.setSealApproveStatusStr(model.getSealApproveStatus().ordinal());
        dto.setProduceType(model.getProduceType());
        if (!VGUtility.isEmpty(model.getSealPlaceId())) {
            DictDto savePlaceDto = dictService.getCommonCode(model.getSealPlaceId());
            if (!VGUtility.isEmpty(savePlaceDto)) {
                dto.setSealPlaceStr(savePlaceDto.getCode() + " " + savePlaceDto.getChsName());
            }
        }
        dto.setCreateTimestamp(model.getCreateTimestamp());
        if (!VGUtility.isEmpty(model.getSponsor()))
            dto.setSponsorStr(userService.getUserInfo(model.getSponsor()).getChsName());
        return dto;
    }

    @Override
    public AssetSequestrationDto createAssetSeal(String assetSequestrationTempDtoList, AssetSequestrationDto assetSequestrationDto,
                                                 UserInfoDto userInfoDto) {
        commonCheckAssetSequestrationDto(assetSequestrationDto);

        ArrayList<AssetSequestrationTempDto> assetSequestrationTempDtos = new ArrayList<AssetSequestrationTempDto>();
        if ((!VGUtility.isEmpty(assetSequestrationTempDtoList)))
            assetSequestrationTempDtos = JSON.parseObject(assetSequestrationTempDtoList, new TypeReference<ArrayList<AssetSequestrationTempDto>>() {
            });
        else
            throw new RuntimeException("资产列表不能为空！");
        AssetSequestrationModel model = new AssetSequestrationModel();
        model.setSealPlaceId(assetSequestrationDto.getSealPlaceId());
        model.setSealReason(assetSequestrationDto.getSealReason());
        if (!VGUtility.isEmpty(getAssetSealNum()))
            model.setSequestrateNum(getAssetSealNum());
        model.setSealStatus(SEALED_UNSEALED.封存);
        model.setSealApproveStatus(FlowableInfo.WORKSTATUS.拟稿);
		/*if(!VGUtility.isEmpty(assetSequestrationDto.getApprove()) && "yes".equals(assetSequestrationDto.getApprove()))
			model.setSealApproveStatus(FlowableInfo.WORKSTATUS.审批中);*/
        model.setCreateTimestamp(new Date());
        model.setSponsor(userInfoDto.getId());
        model.setLaunchDate(VGUtility.toDateObj(assetSequestrationDto.getLaunchDateStr(), "yyyy-MM-dd"));
        model.setProduceType(assetSequestrationDto.getProduceType());
        model = assetSequestrationDao.save(model);
        assetSequestrationTempService.createSealAsset(assetSequestrationTempDtos, model.getId());
        return convertAssetSequestrationModelToDto(model);
    }

    @Override
    public void deleteAssetSeal(String id) {
        if ((VGUtility.isEmpty(id)))
            throw new RuntimeException("封存单不能为空！");
        List<AssetSequestrationTempDto> list = getAssetSeal(id);
        for (AssetSequestrationTempDto assetSequestrationTempDto : list)
            assetSequestrationTempDao.deleteById(assetSequestrationTempDto.getId());
        assetSequestrationDao.deleteById(id);
    }

    // 生成封存编号
    @Override
    public String getAssetSealNum() {
        Date time = Calendar.getInstance().getTime();
        String date = VGUtility.toDateStr(time, "yyyy-MM-dd");
        String hql = "from AssetSequestrationModel where to_char(createtimestamp,'yyyy-MM-dd')='" + date
                + "' order by sequestrateNum desc";
        TypedQuery<AssetSequestrationModel> query = entityManager.createQuery(hql, AssetSequestrationModel.class);

        List<AssetSequestrationModel> modelList = query.getResultList();
        String code = "";
        if (modelList.size() > 0) {
            String borrowCode = modelList.get(0).getSequestrateNum();
            if (!VGUtility.isEmpty(borrowCode)) {
                code = borrowCode;
                Long num = Long.parseLong(code.substring(2)) + 1;
                code = "FC" + num;
            } else {
                code = "FC" + date.replace("-", "") + "001";
            }
        } else {
            code = "FC" + date.replace("-", "") + "001";
        }

        return code;
    }

    private String getAssetUnSealNum() {
        Date time = Calendar.getInstance().getTime();
        String date = VGUtility.toDateStr(time, "yyyy-MM-dd");
        String hql = "from AssetSequestrationModel where to_char(createtimestamp,'yyyy-MM-dd')='" + date
                + "' order by sequestrateNum desc";
        TypedQuery<AssetSequestrationModel> query = entityManager.createQuery(hql, AssetSequestrationModel.class);

        List<AssetSequestrationModel> modelList = query.getResultList();
        String code = "";
        if (modelList.size() > 0) {
            String borrowCode = modelList.get(0).getSequestrateNum();
            if (!VGUtility.isEmpty(borrowCode)) {
                code = borrowCode;
                Long num = Long.parseLong(code.substring(2)) + 1;
                code = "QF" + num;
            } else {
                code = "QF" + date.replace("-", "") + "001";
            }
        } else {
            code = "QF" + date.replace("-", "") + "001";
        }

        return code;
    }


    /*******封存资产子表查询 start*****/
    @Override
    public List<AssetSequestrationTempDto> getAssetSeal(String id) {
        String hql = "from AssetSequestrationTempModel where ASSETSEQUESTRATIONMODEL_ID='" + id + "' order by createTimestamp desc";
        TypedQuery<AssetSequestrationTempModel> query = entityManager.createQuery(hql, AssetSequestrationTempModel.class);
        List<AssetSequestrationTempModel> modelList = query.getResultList();
        List<AssetSequestrationTempDto> list = new ArrayList<>();
        for (AssetSequestrationTempModel dto : modelList) {
            AssetSequestrationTempDto assetSequestrationTempDto = new AssetSequestrationTempDto();
            assetSequestrationTempDto.setId(dto.getId());
            assetSequestrationTempDto.setAssetId(dto.getAssetId());
            list.add(assetSequestrationTempDto);
        }
        return list;
    }

    @Override
    public void save(String id, String assetSequestrationTempDtoList) {
        AssetSequestrationModel model = assetSequestrationDao.findById(id).get();
        if (VGUtility.isEmpty(model))
            throw new RuntimeException("封存信息不能为空！");
        ArrayList<AssetSequestrationTempDto> assetSequestrationTempDtos = new ArrayList<AssetSequestrationTempDto>();
        if ((!VGUtility.isEmpty(assetSequestrationTempDtoList)))
            assetSequestrationTempDtos = JSON.parseObject(assetSequestrationTempDtoList, new TypeReference<ArrayList<AssetSequestrationTempDto>>() {
            });
        for (AssetSequestrationTempDto dto : assetSequestrationTempDtos) {
            AssetSequestrationTempModel tempModel = new AssetSequestrationTempModel();
            tempModel.setAssetSequestrationModel(model);
            tempModel.setAssetId(dto.getAssetId());
            tempModel.setCreateTimestamp(new Date());
            assetSequestrationTempDao.save(tempModel);
        }

    }

    @Override
    public void deleteById(String id, String assetSequestrationTempDtoList) {
        AssetSequestrationModel model = assetSequestrationDao.findById(id).get();
        if (VGUtility.isEmpty(model))
            throw new RuntimeException("封存信息不能为空！");
        ArrayList<AssetSequestrationTempDto> assetSequestrationTempDtos = new ArrayList<AssetSequestrationTempDto>();
        if ((!VGUtility.isEmpty(assetSequestrationTempDtoList)))
            assetSequestrationTempDtos = JSON.parseObject(assetSequestrationTempDtoList, new TypeReference<ArrayList<AssetSequestrationTempDto>>() {
            });

        for (AssetSequestrationTempDto dto : assetSequestrationTempDtos) {
            assetSequestrationTempDao.deleteById(dto.getId());
        }
    }

    //发起审批
    @Override
    public void approvalAssetSeal(String id) {
        if (VGUtility.isEmpty(id))
            throw new RuntimeException("审批单号不能为空");
        AssetSequestrationModel model = assetSequestrationDao.findById(id).get();
        if (VGUtility.isEmpty(model))
            throw new RuntimeException("审批信息不能为空！");
        model.setSealApproveStatus(FlowableInfo.WORKSTATUS.已审批);
        List<AssetSequestrationTempDto> list = getAssetSeal(id);
        if (!list.isEmpty()) {
            for (AssetSequestrationTempDto temp : list) {
                AssetAssetModel assetModel = assetAssetDao.findById(temp.getAssetId()).get();
                assetModel.setBeforeChangeAssetStatus(assetModel.getAssetStatus());
                if (ASSET_STATUS.停用.equals(assetModel.getAssetStatus()))
                    assetModel.setAssetStatus(ASSET_STATUS.封存);
                else if (ASSET_STATUS.封存.equals(assetModel.getAssetStatus()))
                    assetModel.setAssetStatus(ASSET_STATUS.闲置);
                assetAssetDao.save(assetModel);
            }
        }
        assetSequestrationDao.save(model);
    }

    @Override
    public void updateSeal(AssetSequestrationDto assetSequestrationDto) {
        commonCheckAssetSequestrationDto(assetSequestrationDto);
        AssetSequestrationModel model = assetSequestrationDao.findById(assetSequestrationDto.getId()).get();
        if (VGUtility.isEmpty(model))
            throw new RuntimeException("封存信息不能为空！");
        if (!VGUtility.isEmpty(assetSequestrationDto.getApprove()) && "yes".equals(assetSequestrationDto.getApprove()))
            model.setSealApproveStatus(FlowableInfo.WORKSTATUS.审批中);
        model.setLastUpdateTimestamp(new Date());
        model.setSealReason(assetSequestrationDto.getSealReason());
        model.setSealPlaceId(assetSequestrationDto.getSealPlaceId());
        model.setProduceType(assetSequestrationDto.getProduceType());
        assetSequestrationDao.save(model);
    }

    @Override
    public Object getAssetSealById(String id) {
        AssetSequestrationModel model = assetSequestrationDao.findById(id).get();
        if (VGUtility.isEmpty(model))
            throw new RuntimeException("封存信息不能为空！");
        AssetSequestrationDto dto = convertAssetSequestrationModelToDto(model);
        return dto;
    }

    @Override
    public AssetSequestrationDto createAssetUnseal(String assetSequestrationTempDtoList, AssetSequestrationDto assetSequestrationDto,
                                                   UserInfoDto userInfoDto) {
        ArrayList<AssetSequestrationTempDto> assetSequestrationTempDtos = new ArrayList<AssetSequestrationTempDto>();
        if ((!VGUtility.isEmpty(assetSequestrationTempDtoList)))
            assetSequestrationTempDtos = JSON.parseObject(assetSequestrationTempDtoList, new TypeReference<ArrayList<AssetSequestrationTempDto>>() {
            });
        else
            throw new RuntimeException("资产列表不能为空！");
        AssetSequestrationModel model = new AssetSequestrationModel();
        model.setSealPlaceId(assetSequestrationDto.getSealPlaceId());
        model.setSealReason(assetSequestrationDto.getSealReason());
        model.setProduceType(assetSequestrationDto.getProduceType());
        if (!VGUtility.isEmpty(getAssetSealNum()))
            model.setSequestrateNum(getAssetUnSealNum());
        model.setSealStatus(SEALED_UNSEALED.启封);
        model.setSealApproveStatus(FlowableInfo.WORKSTATUS.拟稿);
        if (!VGUtility.isEmpty(assetSequestrationDto.getApprove()) && "yes".equals(assetSequestrationDto.getApprove()))
            model.setSealApproveStatus(FlowableInfo.WORKSTATUS.已审批);
        model.setCreateTimestamp(new Date());
        model.setSponsor(userInfoDto.getId());
        model.setLaunchDate(VGUtility.toDateObj(assetSequestrationDto.getLaunchDateStr(), "yyyy-MM-dd"));
        model = assetSequestrationDao.save(model);
        assetSequestrationTempService.createAssetUnseal(assetSequestrationTempDtos, model.getId());
        AssetSequestrationDto dto = convertAssetSequestrationModelToDto(model);
        return dto;

    }

    @Override
    public Map<String, Object> getSealViewById(String id) {
        Map<String, Object> map = new HashMap<>();
        List<AssetAssetDto> assetDtos = new ArrayList<>();
        List<AssetSequestrationTempDto> list = getAssetSeal(id);
        for (AssetSequestrationTempDto dto : list) {
            AssetAssetModel model = assetAssetDao.findById(dto.getAssetId()).get();
            if (!VGUtility.isEmpty(model)) {
                AssetAssetDto assetDto = convertAssetModelToDto(model);
                assetDtos.add(assetDto);
            }
        }
        map.put("rows", assetDtos);
        return map;
    }


    private AssetAssetDto convertAssetModelToDto(AssetAssetModel model) {
        AssetAssetDto dto = new AssetAssetDto();
        //基本信息
        dto.setId(model.getId());
        dto.setAssetCode(model.getAssetCode());
        dto.setMaterialCode(model.getMaterialCode());
        dto.setAssetType(Integer.toString(model.getAssetType().ordinal()));
        dto.setAssetTypeStr(model.getAssetType().name());
        dto.setSpecAndModels(model.getSpecAndModels());
        dto.setSeriesNum(model.getSeriesNum());

        dto.setUnitOfMeasId(model.getUnitOfMeasId());
        if (!VGUtility.isEmpty(model.getUnitOfMeasId())) {
            DictDto dictCommonDto = dictService.getCommonCode(model.getUnitOfMeasId());
            if (!VGUtility.isEmpty(dictCommonDto))
                dto.setUnitOfMeasStr(dictCommonDto.getChsName());
        }
        dto.setAssetBrand(model.getAssetBrand());
        double purcPrice = model.getPurcPrice();
        String purcPriceStr = new String();
        if (purcPrice > 0)
            purcPriceStr = VGUtility.toDoubleStr(purcPrice, "0.##");
        else
            purcPriceStr = "";
        dto.setPurcPrice(purcPriceStr);
        double equiOrigValue = model.getEquiOrigValue();
        String equiOrigValueStr = new String();
        if (equiOrigValue > 0)
            equiOrigValueStr = VGUtility.toDoubleStr(equiOrigValue, "0.##");
        else
            equiOrigValueStr = "";
        dto.setEquiOrigValue(equiOrigValueStr);
        dto.setTechPara(model.getTechPara());
        dto.setRemark(model.getRemark());
        //延伸信息
        dto.setCompanyId(model.getCompanyId());
        if (!VGUtility.isEmpty(model.getCompanyId())) {
            DeptInfoDto deptDto = (DeptInfoDto) baseService.getDeptInfo(model.getCompanyId());
            if (!VGUtility.isEmpty(deptDto))
                dto.setCompanyStr(deptDto.getDeptName());
        }
        dto.setBelongLine(model.getBelongLine());
        if (!VGUtility.isEmpty(model.getBelongLine())) {
            DictDto dictCommonDto = dictService.getCommonCode(model.getBelongLine());
            if (!VGUtility.isEmpty(dictCommonDto))
                dto.setBelongLineStr(dictCommonDto.getChsName());
        }
        dto.setBuyDate(VGUtility.toDateStr(model.getBuyDate(), "yyyy/M/d"));
        dto.setManageDeptId(model.getManageDeptId());
        if (!VGUtility.isEmpty(model.getManageDeptId())) {
            DeptInfoDto deptDto = (DeptInfoDto) baseService.getDeptInfo(model.getManageDeptId());
            if (!VGUtility.isEmpty(deptDto))
                dto.setManageDeptStr(deptDto.getDeptName());
        }
        dto.setManagerId(model.getManagerId());
        if (!VGUtility.isEmpty(model.getManagerId())) {
            UserInfoDto userDto = userService.getUserInfo(model.getManagerId());
            if (!VGUtility.isEmpty(userDto))
                dto.setManagerStr(userDto.getChsName());
        }
        dto.setAssetSource(model.getAssetSource());
        dto.setContractNum(model.getContractNum());
        dto.setTendersNum(model.getTendersNum());
        if (!VGUtility.isEmpty(model.getMainPeriod()))
            dto.setMainPeriod(VGUtility.toDateStr(model.getMainPeriod(), "yyyy/M/d"));
        dto.setSourceUser(model.getSourceUser());
        dto.setSourceContactInfo(model.getSourceContactInfo());
        if (!VGUtility.isEmpty(model.getProdTime()))
            dto.setProdTime(VGUtility.toDateStr(model.getProdTime(), "yyyy/M/d"));
        dto.setAssetStatus(model.getAssetStatus().name());
        dto.setAssetStatusStr(Integer.toString(model.getAssetStatus().ordinal()));
        dto.setRecId(model.getRecId());
        //根据assetId查到assetDto,根据dto中的placeId查到dictionaryDto，再查到code和chsName
        dto.setSavePlaceId(model.getSavePlaceId());
        if (!VGUtility.isEmpty(model.getSavePlaceId())) {
            DictDto savePlaceDto = dictService.getCommonCode(model.getSavePlaceId());
            if (!VGUtility.isEmpty(savePlaceDto)) {
                dto.setSavePlaceCode(savePlaceDto.getCode());
                dto.setSavePlaceName(savePlaceDto.getChsName());
                dto.setSavePlaceStr(savePlaceDto.getCode() + " " + savePlaceDto.getChsName());
            }
        }
        //根据物资编码查询并将查询结果组合为资产类别和资产名称，然后set到dto中
        String assetTypeName = baseService.getAssetTypeByMaterialCode(model.getMaterialCode());
        if (!VGUtility.isEmpty(assetTypeName))
            dto.setCombinationAssetType(assetTypeName);

        String assetName = baseService.getAssetNameByMaterialCode(model.getMaterialCode());
        if (!VGUtility.isEmpty(assetName))
            dto.setCombinationAssetName(assetName);
		/*//根据物资编码materialCode查询的资产数量和计量单位
		PageDto<DictionaryCommonCodeDto> commonCodeForDatagrid = dictService.getCommonCodeForDatagrid("{code:'"+materialCode+"'}", null);
    	List<DictionaryCommonCodeDto> commonCodeDtoRows = commonCodeForDatagrid.getRows();
    	dto.setAssetQuantity(Integer.toString(commonCodeDtoRows.size()));
    	if(!VGUtility.isEmpty(model.getMaterialCode())) {
			String hql="from AssetAssetModel where materialCode like '"+materialCode+"%'";
			TypedQuery<AssetAssetModel> query = entityManager.createQuery(hql, AssetAssetModel.class);
			if(!VGUtility.isEmpty(query)) {
				List<AssetAssetModel> resultList = query.getResultList();
				dto.setAssetQuantity(Integer.toString(resultList.size()));
				if(resultList.size()>0) {
					dto.setUnitOfMeasStr(resultList.get(0).getUnitOfMeasId());
				}
			}
		}*/
        //TODO
        return dto;
    }

}

package com.mine.product.czmtr.ram.asset.service;

import com.mine.base.dict.dto.DictDto;
import com.mine.base.dict.service.IDictService;
import com.mine.base.user.dto.DeptInfoDto;
import com.mine.base.user.dto.UserInfoDto;
import com.mine.base.user.service.IUserService;
import com.mine.platform.common.dto.PageDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.product.czmtr.ram.asset.dao.AssetTempDao;
import com.mine.product.czmtr.ram.asset.dto.AssetAssetDto;
import com.mine.product.czmtr.ram.asset.model.AssetTempModel;
import com.mine.product.czmtr.ram.asset.service.IAssetService.ASSET_STATUS;
import com.mine.product.czmtr.ram.asset.service.IAssetService.ASSET_TYPE;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.VGUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 资产管理(临时)
 *
 * @author
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AssetTempService implements IAssetTempService {
    private static final Logger logger = LoggerFactory.getLogger(AssetTempService.class);

    @Autowired
    private AssetTempDao assetTempDao;
    @Autowired
    private IAssetService assetService;

    @Autowired
    private IBaseService baseService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IDictService dictService;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AssetAssetDto> getAssetTempByRecId(String recId) {
        List<AssetTempModel> models = assetTempDao.findAssetTempModelByRecId(recId);
        if(models.size()<=0){
            return null;
        }
        List<AssetAssetDto> resultList = new ArrayList<>();
        for (AssetTempModel model : models) {
            resultList.add(convertAssetModelToDto(model));
        }

        return resultList;
    }

    @Override
    public void createAssetTemp(AssetAssetDto assetDto) {
        assetService.commonCheck(assetDto);
        assetService.commonCheckBySearch(assetDto);
        AssetTempModel model = convertAssetDtoToModel(assetDto);
        model.setAssetStatus(ASSET_STATUS.闲置);
        assetTempDao.save(model);
    }

    @Override
    public void updateAssetTemp(AssetAssetDto assetDto) {
        assetService.commonCheck(assetDto);
        assetTempDao.save(convertAssetDtoToModel(assetDto, assetTempDao.findById(assetDto.getId()).get()));
    }

    @Override
    public PageDto<AssetAssetDto> getAssetTempByRecId(String hql, Map<String, Object> params, PageableDto pageable) {
        TypedQuery<AssetTempModel> query = entityManager.createQuery(hql, AssetTempModel.class);
        TypedQuery<Long> countQuery = entityManager.createQuery("select count(id) " + hql, Long.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }
        if (!VGUtility.isEmpty(pageable))
            query.setFirstResult((pageable.getPage() - 1) * pageable.getSize()).setMaxResults(pageable.getSize());
        List<AssetTempModel> modelList = query.getResultList();
        List<AssetAssetDto> resultList = new ArrayList<AssetAssetDto>();
        for (AssetTempModel model : modelList)
            resultList.add(convertAssetModelToDto(model));
        return new PageDto<AssetAssetDto>(countQuery.getSingleResult(), resultList);
    }

    @Override
    public int getAssetTempCountByRecId(String recId) {
        return assetTempDao.countByRecId(recId);
    }

    @Override
    public void deleteAssetTempByRecId(String recId) {
        assetTempDao.deleteAssetTempModelByRecId(recId);
    }

    //********************Convert Start********************

    /***
     *
     * @param dto
     * @return
     */
    private AssetTempModel convertAssetDtoToModel(AssetAssetDto dto) {
        AssetTempModel model = new AssetTempModel();

        //基本信息
        model.setRecId(dto.getRecId());
        model.setAssetCode(dto.getAssetCode());
        model.setMaterialCode(dto.getMaterialCode());
        model.setAssetType(ASSET_TYPE.values()[VGUtility.toInteger(dto.getAssetType())]);
        model.setSpecAndModels(dto.getSpecAndModels());
        model.setSeriesNum(dto.getSeriesNum());
        model.setUnitOfMeasId(dto.getUnitOfMeasId());
        model.setAssetBrand(dto.getAssetBrand());
        model.setPurcPrice(VGUtility.toDouble(dto.getPurcPrice()));
        model.setEquiOrigValue(VGUtility.toDouble(dto.getEquiOrigValue()));
        model.setTechPara(dto.getTechPara());
        model.setRemark(dto.getRemark());
        //延伸信息
        model.setCompanyId(dto.getCompanyId());
        model.setBelongLine(dto.getBelongLine());
        model.setAssetLineId(dto.getAssetLineId());
        model.setBuyDate(VGUtility.toDateObj(dto.getBuyDate(), "yyyy/M/d"));
        model.setManageDeptId(dto.getManageDeptId());
        model.setManagerId(dto.getManagerId());
        model.setAssetSource(dto.getAssetSource());
        model.setContractNum(dto.getContractNum());
        model.setTendersNum(dto.getTendersNum());
        model.setSavePlaceId(dto.getSavePlaceId());
        if (!VGUtility.isEmpty(dto.getMainPeriod()))
            model.setMainPeriod(VGUtility.toDateObj(dto.getMainPeriod(), "yyyy/M/d"));
        model.setSourceUser(dto.getSourceUser());
        model.setSourceContactInfo(dto.getSourceContactInfo());
        if (!VGUtility.isEmpty(dto.getProdTime()))
            model.setProdTime(VGUtility.toDateObj(dto.getProdTime(), "yyyy/M/d"));

        return model;
    }

    private AssetTempModel convertAssetDtoToModel(AssetAssetDto dto, AssetTempModel model) {
        //基本信息
        model.setRecId(dto.getRecId());
        model.setAssetCode(dto.getAssetCode());
        if (!VGUtility.isEmpty(baseService.getAssetNameByMaterialCode(dto.getMaterialCode())))
            model.setMaterialCode(dto.getMaterialCode());
        model.setAssetType(ASSET_TYPE.values()[VGUtility.toInteger(dto.getAssetType())]);
        model.setSpecAndModels(dto.getSpecAndModels());
        model.setSeriesNum(dto.getSeriesNum());
        if (!VGUtility.isEmpty(dto.getUnitOfMeasId())) {
            try {
                dictService.getCommonCode(dto.getUnitOfMeasId());
                model.setUnitOfMeasId(dto.getUnitOfMeasId());
            } catch (Exception e) {
            }
        }
        model.setAssetBrand(dto.getAssetBrand());
        model.setPurcPrice(VGUtility.toDouble(dto.getPurcPrice()));
        model.setEquiOrigValue(VGUtility.toDouble(dto.getEquiOrigValue()));
        model.setTechPara(dto.getTechPara());
        model.setRemark(dto.getRemark());
        //延伸信息
        if (!VGUtility.isEmpty(dto.getCompanyId())) {
            try {
                baseService.getDeptInfo(dto.getCompanyId());
                model.setCompanyId(dto.getCompanyId());
            } catch (Exception e) {
            }
        }
        model.setCompanyId(dto.getCompanyId());
        if (!VGUtility.isEmpty(dto.getBelongLine())) {
            try {
                dictService.getCommonCode(dto.getBelongLine());
                model.setBelongLine(dto.getBelongLine());
            } catch (Exception e) {
            }
        }
        model.setAssetLineId(dto.getAssetLineId());
        model.setBuyDate(VGUtility.toDateObj(dto.getBuyDate(), "yyyy/M/d"));
        if (!VGUtility.isEmpty(dto.getManageDeptId())) {
            try {
                baseService.getDeptInfo(dto.getManageDeptId());
                model.setManageDeptId(dto.getManageDeptId());
            } catch (Exception e) {
            }
        }
        if (!VGUtility.isEmpty(dto.getManagerId())) {
            try {
                userService.getUserInfo(dto.getManagerId());
                model.setManagerId(dto.getManagerId());
            } catch (Exception e) {
            }
        }
        model.setAssetSource(dto.getAssetSource());
        model.setContractNum(dto.getContractNum());
        model.setTendersNum(dto.getTendersNum());
        if (!VGUtility.isEmpty(dto.getSavePlaceId())) {
            try {
                dictService.getCommonCode(dto.getSavePlaceId());
                model.setSavePlaceId(dto.getSavePlaceId());
            } catch (Exception e) {
            }
        }
        if (!VGUtility.isEmpty(dto.getMainPeriod()))
            model.setMainPeriod(VGUtility.toDateObj(dto.getMainPeriod(), "yyyy/M/d"));
        model.setSourceUser(dto.getSourceUser());
        model.setSourceContactInfo(dto.getSourceContactInfo());
        if (!VGUtility.isEmpty(dto.getProdTime()))
            model.setProdTime(VGUtility.toDateObj(dto.getProdTime(), "yyyy/M/d"));

        return model;
    }

    private AssetAssetDto convertAssetModelToDto(AssetTempModel model) {
        AssetAssetDto dto = new AssetAssetDto();
        //基本信息
        dto.setId(model.getId());
        dto.setAssetCode(model.getAssetCode());
        dto.setMaterialCode(model.getMaterialCode());
        dto.setSeriesNum(model.getSeriesNum());

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
                dto.setCompanyStr(deptDto.getDeptCode() + " " + deptDto.getDeptName());
        }
        dto.setBelongLine(model.getBelongLine());
        if (!VGUtility.isEmpty(model.getBelongLine())) {
            DictDto dictCommonDto = dictService.getCommonCode(model.getBelongLine());
            if (!VGUtility.isEmpty(dictCommonDto))
                dto.setBelongLineStr(dictCommonDto.getCode() + " " + dictCommonDto.getChsName());
        }
        dto.setBuyDate(VGUtility.toDateStr(model.getBuyDate(), "yyyy/M/d"));
        dto.setManageDeptId(model.getManageDeptId());
        if (!VGUtility.isEmpty(model.getManageDeptId())) {
            DeptInfoDto deptDto = (DeptInfoDto) baseService.getDeptInfo(model.getManageDeptId());
            if (!VGUtility.isEmpty(deptDto))
                dto.setManageDeptStr(deptDto.getDeptCode() + " " + deptDto.getDeptName());
        }
        dto.setManagerId(model.getManagerId());
        if (!VGUtility.isEmpty(model.getManagerId())) {
            UserInfoDto userDto = userService.getUserInfo(model.getManagerId());
            if (!VGUtility.isEmpty(userDto))
                dto.setManagerStr(userDto.getUserName() + " " + userDto.getChsName());
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
        if (!VGUtility.isEmpty(assetTypeName)) {
            dto.setCombinationAssetType(assetTypeName);
        }

        Map<String, String> assetMap = baseService.getAssetMapByMaterialCode(model.getMaterialCode());
        if (!VGUtility.isEmpty(assetMap)) {
            dto.setCombinationAssetName(assetMap.get("assetName"));
            dto.setAssetTypeStr(assetMap.get("W_PRO_CODE"));
            dto.setSpecAndModels(assetMap.get("MARTERIALS_SPEC"));
            dto.setUnitOfMeasStr(assetMap.get("W_UNIT_CODE"));
        }
        return dto;
    }

    @Override
    public void deleteTempAssetByIdList(String[] deleteIdList, String recId) {
        for (String deleteId : deleteIdList) {
            assetTempDao.deleteById(deleteId);
        }
    }

    @Override
    public void insertAssetCode(Map<String, String> map) {
        for (String id : map.keySet()) {
            AssetTempModel model = assetTempDao.findById(id).get();
            model.setAssetCode(map.get(id));
        }
    }
}
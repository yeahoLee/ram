package com.mine.product.czmtr.ram.asset.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mine.platform.common.dto.PageDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.product.czmtr.ram.asset.dao.MaterialCodeDao;
import com.mine.product.czmtr.ram.asset.dao.MaterialCodeTempDao;
import com.mine.product.czmtr.ram.asset.dto.MaterialCodeDto;
import com.mine.product.czmtr.ram.asset.dto.MaterialCodeTempDto;
import com.mine.product.czmtr.ram.asset.model.MaterialCodeModel;
import com.mine.product.czmtr.ram.asset.model.MaterialCodeTempModel;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.VGUtility;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class MaterialCodeService implements IMaterialCodeService {

    @Autowired
    private MaterialCodeDao materialCodeDao;
    @Autowired
    private MaterialCodeTempDao materialCodeTempDao;

    @Autowired
    private IBaseService baseService;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public MaterialCodeDto createMaterialCode(MaterialCodeDto dto) {
        if (VGUtility.isEmpty(dto)) throw new RuntimeException("MaterialCodeDto is null!");
        MaterialCodeModel model = new MaterialCodeModel();
        model = convertDtoToModel(dto);
        MaterialCodeModel modelNew = materialCodeDao.save(model);
        return convertModelToDto(modelNew);
    }

    @Override
    public void deleteMaterialCode(String id) {
        if (VGUtility.isEmpty(id)) throw new RuntimeException("Id is null!");
        materialCodeDao.deleteById(id);
    }

    @Override
    public MaterialCodeDto updateMaterialCode(MaterialCodeDto dto) {
        if (VGUtility.isEmpty(dto)) throw new RuntimeException("MaterialCodeDto is null!");
        if (VGUtility.isEmpty(dto.getId())) throw new RuntimeException("Id is null!");
        Optional<MaterialCodeModel> modelOpt = materialCodeDao.findById(dto.getId());
        if (modelOpt.isPresent()) {
            MaterialCodeModel model = modelOpt.get();
            model = convertDtoToModel(dto);
            materialCodeDao.save(model);
            return convertModelToDto(model);
        } else
            throw new RuntimeException("Can not found result!");
    }

    @Override
    public MaterialCodeDto getMaterialCodeById(String id) {
        if (VGUtility.isEmpty(id)) throw new RuntimeException("Id is null!");
        Optional<MaterialCodeModel> modelOpt = materialCodeDao.findById(id);
        if (modelOpt.isPresent()) {
            MaterialCodeModel model = modelOpt.get();
            return convertModelToDto(model);
        } else
            throw new RuntimeException("Can not found result!");
    }

    @Override
    public PageDto<MaterialCodeDto> getMaterialCode(String hql, Map<String, Object> params, PageableDto pageable) {
        return getMaterialCode(hql, "select count(id) " + hql, params, pageable);
    }

    @Override
    public PageDto<MaterialCodeDto> getMaterialCode(String hql, String countHql, Map<String, Object> params,
                                                    PageableDto pageable) {
        TypedQuery<MaterialCodeModel> query = entityManager.createQuery(hql, MaterialCodeModel.class);
        TypedQuery<Long> countQuery = entityManager.createQuery("select count(id) " + hql, Long.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }
        if (!VGUtility.isEmpty(pageable))
            query.setFirstResult((pageable.getPage() - 1) * pageable.getSize()).setMaxResults(pageable.getSize());
        List<MaterialCodeModel> modelList = query.getResultList();

        List<MaterialCodeDto> resultList = new ArrayList<MaterialCodeDto>();
        for (MaterialCodeModel model : modelList) {
            resultList.add(convertModelToDto(model));
        }
        return new PageDto<MaterialCodeDto>(countQuery.getSingleResult(), resultList);
    }

    private MaterialCodeDto convertModelToDto(MaterialCodeModel model) {
        MaterialCodeDto dto = new MaterialCodeDto();

        dto.setId(model.getId());
        dto.setMaterialCode(model.getMaterialCode());
        dto.setRunningNum(model.getRunningNum());

        dto.setDescription(model.getDescription());
        if (!VGUtility.isEmpty(model.getIsFixed())) {
            dto.setIsFixed(String.valueOf(model.getIsFixed()));
        }
        dto.setMaterialCate(model.getMaterialCate());
        dto.setUnit(model.getUnit());
        if (!VGUtility.isEmpty(model.getLastUpdateDate())) {
            dto.setLastUpdateDate(VGUtility.toDateStr(model.getLastUpdateDate(), "yyyy/M/d HH:mm"));
        }
        dto.setCreateTimestamp(VGUtility.toDateStr(model.getCreateTimestamp(), "yyyy/M/d HH:mm"));
        dto.setLastUpdateTimestamp(VGUtility.toDateStr(model.getLastUpdateTimestamp(), "yyyy/M/d HH:mm"));
        return dto;
    }

    private MaterialCodeModel convertDtoToModel(MaterialCodeDto dto) {
        MaterialCodeModel model = new MaterialCodeModel();

        model.setId(dto.getId());
        model.setMaterialCode(dto.getMaterialCode());
        model.setRunningNum(dto.getRunningNum());

        model.setDescription(dto.getDescription());
        if (!VGUtility.isEmpty(dto.getIsFixed())) {
            model.setIsFixed(Integer.parseInt(dto.getIsFixed()));
        }
        model.setMaterialCate(dto.getMaterialCate());
        model.setUnit(dto.getUnit());
        if (!VGUtility.isEmpty(dto.getLastUpdateDate())) {
            model.setLastUpdateDate(VGUtility.toDateObj(dto.getLastUpdateDate(), "yyyy/M/d HH:mm"));
        }

        if (!VGUtility.isEmpty(dto.getCreateTimestamp())) {
            model.setCreateTimestamp(VGUtility.toDateObj(dto.getCreateTimestamp(), "yyyy/M/d HH:mm"));
        }
        if (!VGUtility.isEmpty(dto.getLastUpdateTimestamp())) {
            model.setLastUpdateTimestamp(VGUtility.toDateObj(dto.getLastUpdateTimestamp(), "yyyy/M/d HH:mm"));
        }
        return model;
    }

    @Override
    public String getMaterialCodeByAssetCode(String code) {
        String materialCode = null;
        String runningNum = baseService.getRunningNumByMaterialCode(code);

        if (VGUtility.isEmpty(runningNum)) {
            return "";
        } else {
            Integer rn = Integer.parseInt(runningNum);
            if (rn != 99999) {
                Integer next = rn + 1;
                String size = "";
                if (next.toString().length() < 5) {
                    for (int i = 0; i < 5 - next.toString().length(); i++) {
                        size += "0";
                    }
                }
                runningNum = size + next;
                materialCode = code + runningNum;//流水号+1
            }
        }

        baseService.UpdateRamAssetTypeByMaterialCode(code, runningNum);

        return materialCode;
    }

    @Override
    public List<MaterialCodeTempDto> getMultipleMaterialCode(String code, Integer num) {
        List<MaterialCodeTempDto> dtos = new ArrayList<MaterialCodeTempDto>();
        //MaterialCodeModel model = materialCodeDao.getMaterialCodeByMaterialCode(code);
        String assetTypeName = new String();

        if (!VGUtility.isEmpty(code))
            assetTypeName = baseService.getAssetNameByMaterialCode(code);
        if (!VGUtility.isEmpty(assetTypeName)) {
            if (!VGUtility.isEmpty(num) && num > 0) {
                for (int i = 0; i < num; i++) {
                    MaterialCodeTempDto dto = new MaterialCodeTempDto();
                    if (VGUtility.isEmpty(this.getMaterialCodeByAssetCode(code))) {
                        break;
                    } else {
                        dto.setCodeTemp(this.getMaterialCodeByAssetCode(code));
                        dto.setTempNum(UUID.randomUUID().toString());
                        dtos.add(dto);
                    }
                }
            }
        }
        if (dtos.size() == num) {
            //返回物资编码+验证码 保存到数据库
            for (MaterialCodeTempDto dto : dtos) {
                MaterialCodeTempModel tempModel = new MaterialCodeTempModel();
                tempModel.setId(dto.getId());
                tempModel.setCodeTemp(dto.getCodeTemp());
                tempModel.setTempNum(dto.getTempNum());
                materialCodeTempDao.save(tempModel);
            }
            return dtos;
        } else {
            //没有编码或流水号不够
            return new ArrayList<MaterialCodeTempDto>();
        }
    }
}

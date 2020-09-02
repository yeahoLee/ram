package com.mine.product.czmtr.ram.asset.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.mine.product.czmtr.ram.asset.dao.MaterialReceiptDao;
import com.mine.product.czmtr.ram.asset.dto.MaterialCodeDto;
import com.mine.product.czmtr.ram.asset.dto.MaterialCodeTempDto;
import com.mine.product.czmtr.ram.asset.model.AssetAssetModel;
import com.mine.product.czmtr.ram.asset.model.MaterialCodeModel;
import com.mine.product.czmtr.ram.asset.model.MaterialCodeTempModel;
import com.vgtech.platform.common.utility.VGUtility;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class MaterialCodeTempService implements IMaterialCodeTempService {

    @Autowired
    private MaterialCodeTempDao materialCodeTempDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public MaterialCodeTempDto createMaterialCodeTemp(MaterialCodeTempDto dto) {
        if (VGUtility.isEmpty(dto)) throw new RuntimeException("MaterialCodeTempDto is null!");
        MaterialCodeTempModel model = new MaterialCodeTempModel();
        model = convertDtoToModel(dto);
        MaterialCodeTempModel modelNew = materialCodeTempDao.save(model);
        return convertModelToDto(modelNew);
    }

    @Override
    public void deleteMaterialCodeTemp(String id) {
        if (VGUtility.isEmpty(id)) throw new RuntimeException("Id is null!");
        materialCodeTempDao.deleteById(id);
    }

    @Override
    public MaterialCodeTempDto updateMaterialCodeTemp(MaterialCodeTempDto dto) {
        if (VGUtility.isEmpty(dto)) throw new RuntimeException("MaterialCodeTempDto is null!");
        if (VGUtility.isEmpty(dto.getId())) throw new RuntimeException("Id is null!");
        Optional<MaterialCodeTempModel> modelOpt = materialCodeTempDao.findById(dto.getId());
        if (modelOpt.isPresent()) {
            MaterialCodeTempModel model = modelOpt.get();
            model = convertDtoToModel(dto);
            materialCodeTempDao.save(model);
            return convertModelToDto(model);
        } else
            throw new RuntimeException("Can not found result!");
    }

    @Override
    public MaterialCodeTempDto getMaterialCodeTempById(String id) {
        if (VGUtility.isEmpty(id)) throw new RuntimeException("Id is null!");
        Optional<MaterialCodeTempModel> modelOpt = materialCodeTempDao.findById(id);
        if (modelOpt.isPresent()) {
            MaterialCodeTempModel model = modelOpt.get();
            return convertModelToDto(model);
        } else
            throw new RuntimeException("Can not found result!");
    }

    @Override
    public PageDto<MaterialCodeTempDto> getMaterialCodeTemp(String hql, Map<String, Object> params, PageableDto pageable) {
        return getMaterialCodeTemp(hql, "select count(id) " + hql, params, pageable);
    }

    @Override
    public PageDto<MaterialCodeTempDto> getMaterialCodeTemp(String hql, String countHql, Map<String, Object> params,
                                                            PageableDto pageable) {
        TypedQuery<MaterialCodeTempModel> query = entityManager.createQuery(hql, MaterialCodeTempModel.class);
        TypedQuery<Long> countQuery = entityManager.createQuery("select count(id) " + hql, Long.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }
        if (!VGUtility.isEmpty(pageable))
            query.setFirstResult((pageable.getPage() - 1) * pageable.getSize()).setMaxResults(pageable.getSize());
        List<MaterialCodeTempModel> modelList = query.getResultList();

        List<MaterialCodeTempDto> resultList = new ArrayList<MaterialCodeTempDto>();
        for (MaterialCodeTempModel model : modelList) {
            resultList.add(convertModelToDto(model));
        }
        return new PageDto<MaterialCodeTempDto>(countQuery.getSingleResult(), resultList);
    }

    private MaterialCodeTempDto convertModelToDto(MaterialCodeTempModel model) {
        MaterialCodeTempDto dto = new MaterialCodeTempDto();

        dto.setId(model.getId());
        dto.setCodeTemp(model.getCodeTemp());
        dto.setTempNum(model.getTempNum());

        dto.setCreateTimestamp(VGUtility.toDateStr(model.getCreateTimestamp(), "yyyy/M/d HH:mm"));
        dto.setLastUpdateTimestamp(VGUtility.toDateStr(model.getLastUpdateTimestamp(), "yyyy/M/d HH:mm"));
        return dto;
    }

    private MaterialCodeTempModel convertDtoToModel(MaterialCodeTempDto dto) {
        MaterialCodeTempModel model = new MaterialCodeTempModel();

        model.setId(dto.getId());
        model.setCodeTemp(dto.getCodeTemp());
        model.setTempNum(dto.getTempNum());

        model.setCreateTimestamp(VGUtility.toDateObj(dto.getCreateTimestamp(), "yyyy/M/d HH:mm"));
        model.setLastUpdateTimestamp(VGUtility.toDateObj(dto.getLastUpdateTimestamp(), "yyyy/M/d HH:mm"));

        return model;
    }

    @Override
    public boolean findMaterialCodeTempModelByCodeTempAndTempNum(String codeTemp, String tempNum) {
        MaterialCodeTempModel model = materialCodeTempDao.findMaterialCodeTempModelByCodeTempAndTempNum(codeTemp, tempNum);
        if (!VGUtility.isEmpty(model)) {
            return true;
        } else {
            return false;
        }
    }

}

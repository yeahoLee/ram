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
import com.mine.product.czmtr.ram.asset.dao.HeadAssetDao;
import com.mine.product.czmtr.ram.asset.dao.MaterialReceiptDao;
import com.mine.product.czmtr.ram.asset.dto.HeadAssetDto;
import com.mine.product.czmtr.ram.asset.model.HeadAssetModel;
import com.vgtech.platform.common.utility.VGUtility;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class HeadAssetService implements IHeadAssetService {

    @Autowired
    private HeadAssetDao headAssetDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public HeadAssetDto createHeadAsset(HeadAssetDto dto) {
        if (VGUtility.isEmpty(dto)) throw new RuntimeException("HeadAssetDto is null!");
        HeadAssetModel model = new HeadAssetModel();
        model = convertDtoToModel(dto);
        HeadAssetModel modelNew = headAssetDao.save(model);
        return convertModelToDto(modelNew);
    }

    @Override
    public void deleteHeadAsset(String id) {
        if (VGUtility.isEmpty(id)) throw new RuntimeException("Id is null!");
        headAssetDao.deleteById(id);
    }

    @Override
    public HeadAssetDto updateHeadAsset(HeadAssetDto dto) {
        if (VGUtility.isEmpty(dto)) throw new RuntimeException("HeadAssetDto is null!");
        if (VGUtility.isEmpty(dto.getId())) throw new RuntimeException("Id is null!");
        Optional<HeadAssetModel> modelOpt = headAssetDao.findById(dto.getId());
        if (modelOpt.isPresent()) {
            HeadAssetModel model = modelOpt.get();
            model = convertDtoToModel(dto);
            headAssetDao.save(model);
            return convertModelToDto(model);
        } else
            throw new RuntimeException("Can not found result!");
    }

    @Override
    public HeadAssetDto getHeadAssetById(String id) {
        if (VGUtility.isEmpty(id)) throw new RuntimeException("Id is null!");
        Optional<HeadAssetModel> modelOpt = headAssetDao.findById(id);
        if (modelOpt.isPresent()) {
            HeadAssetModel model = modelOpt.get();
            return convertModelToDto(model);
        } else
            throw new RuntimeException("Can not found result!");
    }

    @Override
    public PageDto<HeadAssetDto> getHeadAsset(String hql, Map<String, Object> params, PageableDto pageable) {
        return getHeadAsset(hql, "select count(id) " + hql, params, pageable);
    }

    @Override
    public PageDto<HeadAssetDto> getHeadAsset(String hql, String countHql, Map<String, Object> params,
                                              PageableDto pageable) {
        TypedQuery<HeadAssetModel> query = entityManager.createQuery(hql, HeadAssetModel.class);
        TypedQuery<Long> countQuery = entityManager.createQuery("select count(id) " + hql, Long.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }
        if (!VGUtility.isEmpty(pageable))
            query.setFirstResult((pageable.getPage() - 1) * pageable.getSize()).setMaxResults(pageable.getSize());
        List<HeadAssetModel> modelList = query.getResultList();

        List<HeadAssetDto> resultList = new ArrayList<HeadAssetDto>();
        for (HeadAssetModel model : modelList) {
            resultList.add(convertModelToDto(model));
        }
        return new PageDto<HeadAssetDto>(countQuery.getSingleResult(), resultList);
    }

    @Override
    public List<HeadAssetDto> getHeadAssetModelByHeadId(String headId) {
        List<HeadAssetModel> models = headAssetDao.getHeadAssetModelByHeadId(headId);
        List<HeadAssetDto> dtos = new ArrayList<HeadAssetDto>();
        if (!VGUtility.isEmpty(models)) {
            for (HeadAssetModel model : models) {
                HeadAssetDto dto = convertModelToDto(model);
                dtos.add(dto);
            }
        }
        return dtos;
    }

    private HeadAssetDto convertModelToDto(HeadAssetModel model) {
        HeadAssetDto dto = new HeadAssetDto();

        dto.setId(model.getId());
        dto.setHeadId(model.getHeadId());
        dto.setAssetId(model.getAssetId());

        dto.setCreateTimestamp(VGUtility.toDateStr(model.getCreateTimestamp(), "yyyy/M/d HH:mm"));
        dto.setLastUpdateTimestamp(VGUtility.toDateStr(model.getLastUpdateTimestamp(), "yyyy/M/d HH:mm"));
        return dto;
    }

    private HeadAssetModel convertDtoToModel(HeadAssetDto dto) {
        HeadAssetModel model = new HeadAssetModel();

        model.setId(dto.getId());
        model.setHeadId(dto.getHeadId());
        model.setAssetId(dto.getAssetId());

        model.setCreateTimestamp(VGUtility.toDateObj(dto.getCreateTimestamp(), "yyyy/M/d HH:mm"));
        model.setLastUpdateTimestamp(VGUtility.toDateObj(dto.getLastUpdateTimestamp(), "yyyy/M/d HH:mm"));

        return model;
    }


}

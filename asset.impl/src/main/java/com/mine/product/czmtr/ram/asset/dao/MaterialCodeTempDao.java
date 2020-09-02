package com.mine.product.czmtr.ram.asset.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mine.product.czmtr.ram.asset.model.MaterialCodeTempModel;

@Repository
public interface MaterialCodeTempDao extends JpaRepository<MaterialCodeTempModel, String> {
    MaterialCodeTempModel findMaterialCodeTempModelByCodeTempAndTempNum(String codeTemp, String tempNum);
}

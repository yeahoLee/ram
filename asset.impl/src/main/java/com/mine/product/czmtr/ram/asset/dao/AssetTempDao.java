package com.mine.product.czmtr.ram.asset.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mine.product.czmtr.ram.asset.model.AssetTempModel;

@Repository
public interface AssetTempDao extends JpaRepository<AssetTempModel, String> {
    List<AssetTempModel> findAssetTempModelByRecId(String recId);

    void deleteAssetTempModelByRecId(String recId);

    int countByRecId(String recId);
}

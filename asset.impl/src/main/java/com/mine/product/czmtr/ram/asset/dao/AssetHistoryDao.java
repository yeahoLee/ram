package com.mine.product.czmtr.ram.asset.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mine.product.czmtr.ram.asset.model.AssetHistoryModel;

@Repository
public interface AssetHistoryDao extends JpaRepository<AssetHistoryModel, String> {
    void deleteByAssetModelId(String assetModelId);
}

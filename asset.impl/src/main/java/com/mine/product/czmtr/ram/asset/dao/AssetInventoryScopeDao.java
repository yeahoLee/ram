package com.mine.product.czmtr.ram.asset.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mine.product.czmtr.ram.asset.model.AssetInventoryScopeModel;

@Repository
public interface AssetInventoryScopeDao extends JpaRepository<AssetInventoryScopeModel, String> {

    @Query("from AssetInventoryScopeModel  where AssetInventoryModel_ID=:assetInventoryModel_ID")
    List<AssetInventoryScopeModel> findAssetByInventoryModelId(
            @Param("assetInventoryModel_ID") String assetInventoryModel_ID);

    @Modifying
    @Query("delete from AssetInventoryScopeModel  where AssetInventoryModel_ID=:assetInventoryModel_ID")
    void deleteAssetInventoryScopeByInventoryById(@Param("assetInventoryModel_ID") String assetInventoryModel_ID);

}

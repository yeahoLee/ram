package com.mine.product.czmtr.ram.asset.dao;

import com.mine.product.czmtr.ram.asset.model.AssetSequestrationTempModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetSequestrationTempDao extends JpaRepository<AssetSequestrationTempModel, String> {

    @Query(value = "select * from AssetSequestrationTempModel where ASSETID =:assetId and ASSETSEQUESTRATIONMODEL_ID =:id", nativeQuery = true)
    List<AssetSequestrationTempModel> findByAssetId(@Param("assetId") String assetId, @Param("id") String id);

    @Query("from AssetSequestrationTempModel  where AssetSequestrationModel_ID=:assetSequestrationModel_ID")
    List<AssetSequestrationTempModel> findByAssetSequestrationModelId(  @Param("assetSequestrationModel_ID") String assetSequestrationModel_ID);


}

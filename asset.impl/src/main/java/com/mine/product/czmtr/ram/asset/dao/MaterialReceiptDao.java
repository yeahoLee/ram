package com.mine.product.czmtr.ram.asset.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mine.product.czmtr.ram.asset.model.AssetAssetModel;
import com.mine.product.czmtr.ram.asset.model.MaterialReceiptModel;

@Repository
public interface MaterialReceiptDao extends JpaRepository<MaterialReceiptModel, String> {
    @Query("from AssetAssetModel a where a.id in (select h.assetId from HeadAssetModel h where h.headId=:headId)")
    List<AssetAssetModel> findAssetByHeadId(@Param("headId") String headId);

    @Query("select max(m.runningNum) from MaterialReceiptModel m where m.runningNum like :runningNum")
    String getMaxRunningNum(@Param("runningNum") String runningNum);
}

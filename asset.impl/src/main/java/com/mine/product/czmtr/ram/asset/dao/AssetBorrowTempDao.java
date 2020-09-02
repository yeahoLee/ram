package com.mine.product.czmtr.ram.asset.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mine.product.czmtr.ram.asset.model.AssetBorrowModel;
import com.mine.product.czmtr.ram.asset.model.AssetBorrowTempModel;
import com.mine.product.czmtr.ram.asset.model.AssetRevertModel;

@Repository
public interface AssetBorrowTempDao extends JpaRepository<AssetBorrowTempModel, String>, JpaSpecificationExecutor<AssetBorrowTempModel> {
    void deleteAssetBorrowTempModelByAssetBorrowModel(AssetBorrowModel model);

    void deleteAssetBorrowTempModelByAssetRevertModel(AssetRevertModel model);

    List<AssetBorrowTempModel> findAssetBorrowTempModelsByAssetRevertModel(AssetRevertModel model);

    List<AssetBorrowTempModel> findAssetBorrowTempModelsByAssetBorrowModel(AssetBorrowModel model);

    @Query("from AssetBorrowTempModel where ASSETBORROWMODEL_ID=:assetBorrowId order by createTimestamp")
    List<AssetBorrowTempModel> findByAssetBorrowID(@Param("assetBorrowId") String assetBorrwId);

    @Query("from AssetBorrowTempModel where ASSETBORROWMODEL_ID=:assetBorrowId and ASSETREVERTMODEL_ID is  null order by createTimestamp desc")
    List<AssetBorrowTempModel> findByAssetBorrowIDAndAssetRevertIDISNULL(@Param("assetBorrowId") String assetBorrwId);

    @Query("from AssetBorrowTempModel where ASSETREVERTMODEL_ID=:assetRevertId order by createTimestamp")
    List<AssetBorrowTempModel> findByAssetRevertId(@Param("assetRevertId") String assetRevertId);
}

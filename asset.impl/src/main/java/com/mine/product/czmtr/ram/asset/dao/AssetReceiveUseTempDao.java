package com.mine.product.czmtr.ram.asset.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mine.product.czmtr.ram.asset.model.AssetBorrowModel;
import com.mine.product.czmtr.ram.asset.model.AssetBorrowTempModel;
import com.mine.product.czmtr.ram.asset.model.AssetReceiveRevertModel;
import com.mine.product.czmtr.ram.asset.model.AssetReceiveUseModel;
import com.mine.product.czmtr.ram.asset.model.AssetReceiveUseTempModel;

@Repository
public interface AssetReceiveUseTempDao extends JpaRepository<AssetReceiveUseTempModel, String> {
    void deleteAssetReceiveUseTempModelByAssetReceiveUseModel(AssetReceiveUseModel model);

    void deleteAssetReceiveUseTempModelByAssetReceiveRevertModel(AssetReceiveRevertModel model);

    List<AssetReceiveUseTempModel> findAssetReceiveUseTempModelsByAssetReceiveRevertModel(AssetReceiveRevertModel model);

    List<AssetReceiveUseTempModel> findAssetReceiveUseTempModelsByAssetReceiveUseModel(AssetReceiveUseModel model);

    List<AssetReceiveUseTempModel> findByAssetReceiveRevertModel_Id(String assetReceiveRevertId);

    List<AssetReceiveUseTempModel> findByAssetReceiveUseModel_Id(String assetReceiveUserId);
}

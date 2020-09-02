package com.mine.product.czmtr.ram.asset.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.mine.product.czmtr.ram.asset.model.AssetAllocationModel;
import com.mine.product.czmtr.ram.asset.model.AssetAllocationTempModel;

import java.util.List;

@Repository
public interface AssetAllocationTempDao extends JpaRepository<AssetAllocationTempModel, String>, JpaSpecificationExecutor<AssetAllocationTempModel> {
    void deleteAssetAllocationTempModelByAssetAllocationModel(AssetAllocationModel model);

    List<AssetAllocationTempModel> findAllByAssetAllocationModel_Id(String assetAllocationId);
}

package com.mine.product.czmtr.ram.asset.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.mine.product.czmtr.ram.asset.model.AssetAllocationModel;

import java.util.List;

@Repository
public interface AssetAllocationDao extends JpaRepository<AssetAllocationModel, String>, JpaSpecificationExecutor<AssetAllocationModel> {
    AssetAllocationModel getAssetAllocationModelByAssetAllocationCode(String assetAllocationCode);
}

package com.mine.product.czmtr.ram.asset.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mine.product.czmtr.ram.asset.model.AssetInventoryModel;

public interface AssetInventoryDao extends JpaRepository<AssetInventoryModel, String> {

}

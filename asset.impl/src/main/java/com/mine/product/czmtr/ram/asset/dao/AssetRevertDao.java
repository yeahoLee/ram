package com.mine.product.czmtr.ram.asset.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mine.product.czmtr.ram.asset.model.AssetRevertModel;

@Repository
public interface AssetRevertDao extends JpaRepository<AssetRevertModel, String> {

}

package com.mine.product.czmtr.ram.asset.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mine.product.czmtr.ram.asset.model.AssetSequestrationModel;

@Repository
public interface AssetSequestrationDao extends JpaRepository<AssetSequestrationModel, String> {

}

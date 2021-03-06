package com.mine.product.czmtr.ram.asset.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mine.product.czmtr.ram.asset.model.AssetDynamicViewModel;

@Repository
public interface AssetDynamicViewDao extends JpaRepository<AssetDynamicViewModel, String> {

}

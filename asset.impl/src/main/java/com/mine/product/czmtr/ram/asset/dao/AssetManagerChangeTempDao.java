package com.mine.product.czmtr.ram.asset.dao;

import com.mine.product.czmtr.ram.asset.model.AssetManagerChangeTempModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetManagerChangeTempDao extends JpaRepository<AssetManagerChangeTempModel, String> {

	List<AssetManagerChangeTempModel> findByAssetManagerChangeModelId(String id);
}

package com.mine.product.czmtr.ram.asset.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mine.product.czmtr.ram.asset.model.AssetSavePlaceChangeTempModel;

import java.util.List;


@Repository
public interface AssetSavePlaceChangeTempDao extends JpaRepository<AssetSavePlaceChangeTempModel, String> {
	List<AssetSavePlaceChangeTempModel> findByAssetSavePlaceChangeModelId(String Id);

}

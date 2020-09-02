package com.mine.product.czmtr.ram.asset.dao;

import com.mine.product.czmtr.ram.asset.model.AssetUserChangeTempModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AssetUserChangeTempDao extends JpaRepository<AssetUserChangeTempModel, String> {
	List<AssetUserChangeTempModel> findByAssetUserChangeModelId(String id);
}

package com.mine.product.czmtr.ram.asset.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mine.product.czmtr.ram.asset.model.AssetUploadFileModel;

public interface AssetUploadFileDao extends JpaRepository<AssetUploadFileModel, String> {

}

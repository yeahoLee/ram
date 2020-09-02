package com.mine.product.czmtr.ram.asset.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mine.product.czmtr.ram.asset.model.HeadAssetModel;

@Repository
public interface HeadAssetDao extends JpaRepository<HeadAssetModel, String> {
    List<HeadAssetModel> getHeadAssetModelByHeadId(String headId);
}

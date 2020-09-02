package com.mine.product.czmtr.ram.asset.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mine.product.czmtr.ram.asset.model.AssetReceiveRevertModel;

import java.util.List;

@Repository
public interface AssetReceiveRevertDao extends JpaRepository<AssetReceiveRevertModel, String> {

}

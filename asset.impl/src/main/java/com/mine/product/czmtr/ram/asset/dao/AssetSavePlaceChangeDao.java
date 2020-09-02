package com.mine.product.czmtr.ram.asset.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mine.product.czmtr.ram.asset.model.AssetSavePlaceChangeModel;

@Repository
public interface AssetSavePlaceChangeDao extends JpaRepository<AssetSavePlaceChangeModel, String> {
    @Query("select max(a.changeNum) from AssetSavePlaceChangeModel a where a.changeNum like :changeNum")
    String getMaxChangeNum(@Param("changeNum") String changeNum);
}

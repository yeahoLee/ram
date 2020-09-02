package com.mine.product.czmtr.ram.asset.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mine.product.czmtr.ram.asset.model.AssetReduceModel;

@Repository
public interface AssetReduceDao extends JpaRepository<AssetReduceModel, String> {
    @Query("select max(a.changeNum) from AssetReduceModel a where a.changeNum like :changeNum")
    String getMaxChangeNum(@Param("changeNum") String changeNum);
}

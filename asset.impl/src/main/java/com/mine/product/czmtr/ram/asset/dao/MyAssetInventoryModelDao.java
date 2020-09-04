package com.mine.product.czmtr.ram.asset.dao;

import com.mine.product.czmtr.ram.asset.model.MyAssetInventoryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MyAssetInventoryModelDao extends JpaRepository<MyAssetInventoryModel, String> {

    @Query(value = " select * from MyAssetInventoryModel  where AssetInventoryModel_ID=:assetInventoryModel_ID", nativeQuery = true)
    Page<MyAssetInventoryModel> findByassetInventoryModel(@Param("assetInventoryModel_ID") String assetInventoryModel_ID,
                                                          Pageable pageRequest);

    Page<MyAssetInventoryModel> findByAssetInventoryModelId(String assetInventoryModel_ID,Pageable pageRequest);

    @Query(value = "select my.ID as MyAssetInventoryId,ai.ID as AssetInventoryId,my.ASSETINVENTORYCODE,ai.INVENTORYNAME,ai.INVENTORYSTATUS,TO_CHAR(ai.LAUNCHDATE, 'yyyy-MM-dd') as LAUNCHDATE,my.MANAGERDEPTID,my.QUANTITY,my.INVENTORYLOSS,my.INVENTORYSTATUS as MYINVENTORYSTATUS,my.PRODUCETYPE,my.INVENTORYPROFIT from MYASSETINVENTORYMODEL my left join ASSETINVENTORYMODEL ai on MY.ASSETINVENTORYMODEL_ID=ai.ID where my.MANAGERID=:Managerid ORDER BY MY.ASSETINVENTORYCODE desc", nativeQuery = true)
    List<Map> findByManagerid(@Param("Managerid") String Managerid);

    @Modifying
    @Query("delete from MyAssetInventoryModel  where AssetInventoryModel_ID=:assetInventoryModel_ID")
    void deleteMyAssetInventoryByInventoryById(@Param("assetInventoryModel_ID") String assetInventoryModel_ID);

    @Query(value = " select * from MyAssetInventoryModel  where AssetInventoryModel_ID=:assetInventoryModel_ID", nativeQuery = true)
    List<MyAssetInventoryModel> findByInventoryId(@Param("assetInventoryModel_ID") String assetInventoryModel_ID);
}

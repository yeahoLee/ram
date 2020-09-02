package com.mine.product.czmtr.ram.asset.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mine.product.czmtr.ram.asset.model.AssetInventoryTempModel;
import com.mine.product.czmtr.ram.asset.model.MyAssetInventoryModel;

@Repository
public interface AssetInventoryTempDao extends JpaRepository<AssetInventoryTempModel, String>, JpaSpecificationExecutor<AssetInventoryTempModel> {
    @Query("from AssetInventoryTempModel  where AssetInventoryModel_ID=:assetInventoryModel_ID")
    List<AssetInventoryTempModel> findByassetInventoryModelId(
            @Param("assetInventoryModel_ID") String assetInventoryModel_ID);

    @Query(value = "select t.* from ASSETINVENTORYTEMPMODEL  t  left join AssetAssetModel a on t.ASSETID=a.ID where a.MANAGERID =:managerId and t.ASSETINVENTORYMODEL_ID=:ASSETINVENTORYMODEL_ID", nativeQuery = true)
    List<AssetInventoryTempModel> findByManagerId(@Param("managerId") String managerId, @Param("ASSETINVENTORYMODEL_ID") String AssetInventoryId);

    @Query(value = "select a.MANAGERID as mId,a.MANAGEDEPTID as managerDeptId ,COUNT(t.ID) as num from ASSETINVENTORYTEMPMODEL  t  left join AssetAssetModel a on t.ASSETID=a.ID where t.ASSETINVENTORYMODEL_ID=:AssetInventoryId GROUP  BY a.MANAGERID,a.MANAGEDEPTID ", nativeQuery = true)
    List<Map> findManagerid(@Param("AssetInventoryId") String AssetInventoryId);

    @Modifying
    @Query("delete from AssetInventoryTempModel  where AssetInventoryModel_ID=:assetInventoryModel_ID")
    void deleteAssetInventoryTempByInventoryById(@Param("assetInventoryModel_ID") String assetInventoryModel_ID);

    // 通过IDs批量删除我的盘点单；
    @Modifying
    @Query("delete from AssetInventoryTempModel  where AssetInventoryModel_ID in (:ids)")
    void deleteAssetInventoryTempByInventoryByIds(@Param("ids") List<String> ids);

    List<AssetInventoryTempModel> findBymyAssetInventoryModel(MyAssetInventoryModel model);

}

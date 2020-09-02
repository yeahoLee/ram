package com.mine.product.czmtr.ram.asset.dao;

import com.mine.product.czmtr.ram.asset.model.AssetAssetModel;
import com.mine.product.czmtr.ram.asset.service.IAssetService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AssetAssetDao extends JpaRepository<AssetAssetModel, String> {
    @Query("from AssetAssetModel a where a.id in (select h.assetId from HeadAssetModel h where h.headId=:headId)")
    List<AssetAssetModel> findAssetByHeadId(@Param("headId") String headId);

    List<AssetAssetModel> findByIdIn(List<String> assetIdList);

    AssetAssetModel findByAssetCode(String assetCode);

    AssetAssetModel findAssetAssetModelById(String assetId);

    /**
     * 获取资产新增未同步至财务系统的资产
     *
     * @return
     */
    @Query(value = "select a.* from AssetAssetModel a left join AssetTempModel t on a.assetCode = t.assetCode left join MaterialReceiptModel m on m.id = t.recId where (m.receiptStatus = 2 or a.SOURCETYPE in (0,1)) and a.isSysn=0 ", nativeQuery = true)
    List<AssetAssetModel> getNoSysnAddAsset();

    List<AssetAssetModel> findByAssetStatus(IAssetService.ASSET_STATUS assetStatus);

    List<AssetAssetModel> findByManageDeptIdIn(Set<String> depIdList);

    List<AssetAssetModel> findByManagerIdIn(String managerId);

}

package com.mine.product.czmtr.ram.asset.dao;

import com.mine.product.czmtr.ram.asset.model.AssetAssetModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mine.product.czmtr.ram.asset.model.AssetReduceTempModel;

import java.util.List;
import java.util.Map;


@Repository
public interface AssetReduceTempDao extends JpaRepository<AssetReduceTempModel, String> {
    List<AssetReduceTempModel> findByAssetReduceModelId(String assetReduceModelId);

    /**
     * 获取资产减损未同步至财务系统的资产
     *
     * @return
     */
    @Query(value = "select a.ASSETCODE,a.COMPANYID,ar.CHANGENUM,ar.REDUCETYPE,ar.REASON,art.ID from ASSETREDUCETEMPMODEL  art left join ASSETASSETMODEL a  on art.ASSETID = a.ID " +
            " left join ASSETREDUCEMODEL ar on ar.ID = art.ASSETREDUCEMODELID where ar.RECEIPTSTATUS=2 and art.ISSYSN=0 ", nativeQuery = true)
    List<Map> getNoSysnReduceAsset();
}

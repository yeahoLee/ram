package com.mine.product.czmtr.ram.asset.service;


import java.util.Map;


import com.mine.platform.common.dto.PageDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.product.czmtr.ram.asset.dto.AssetClassPropertyDto;

public interface IAssetClassProperty {
    /**
     * 添加
     */
    public void createAssetClassProperty(AssetClassPropertyDto dto);

    /**
     * 修改
     */
    public void updateAssetClassProperty(AssetClassPropertyDto dto);

    /**
     * 删除
     */
    public void deleteAssetClassProperty(String propertyId);

    /**
     * 查询
     */
    PageDto<AssetClassPropertyDto> getAssetClassPropertyList(Map<String, Object> params, PageableDto pageable);

}

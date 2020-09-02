package com.mine.product.czmtr.ram.asset.service;

import com.mine.product.czmtr.ram.asset.dto.AssetAllocationDto;
import com.mine.product.czmtr.ram.asset.dto.AssetAllocationTempDto;

import java.util.List;

public interface IAssetAllocationTempService {

    List<String> getAssetIdList(String id);

    public void createAssetAllocationTempForList(List<AssetAllocationTempDto> AssetAllocationTempDtoList, String assetAllocationId);

    public List<AssetAllocationTempDto> getAssetAllocationTempDtoList(String assetAllocationId);

    public void createAssetAllocationTempByassetIdListStr(String assetIdListStr, AssetAllocationDto dto);

    public void deleteAssetAllocationTempByAssetAllocationID(List<String> assetIdListStr, String AssetAllocationID);
}

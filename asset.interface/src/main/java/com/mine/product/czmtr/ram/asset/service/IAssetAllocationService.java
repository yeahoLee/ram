package com.mine.product.czmtr.ram.asset.service;

import com.mine.base.user.dto.UserInfoDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.AssetAllocationDto;

import java.util.Map;

public interface IAssetAllocationService extends IApproveNotify{


    public String getAssetAllocationCode();

    public Object convertAssetAllocationModel(AssetAllocationDto dto);

    public void commonCheckAssetAllocationDto(AssetAllocationDto dto);

    public AssetAllocationDto createAssetAllocation(String assetAllocationTempDtoList, AssetAllocationDto dto, UserInfoDto userInfoDto);

    public Map<String, Object> getAssetAllocationDtoByQuerysForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto);

    public AssetAllocationDto getAssetAllocationDtoById(String id);

    public AssetAllocationDto getAssetAllocationDtoByCode(String assetAllocationCode);

    public AssetAllocationDto updateAssetAllocatiom(AssetAllocationDto dto);

    public AssetAllocationDto updateAssetAllocationStatus(AssetAllocationDto dto);

    public void deleteAssetAllocation(String assetAllocationID);
}

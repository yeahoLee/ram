package com.mine.product.czmtr.ram.asset.service;

import java.util.List;
import java.util.Map;

import com.mine.base.user.dto.UserInfoDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.AssetSequestrationDto;
import com.mine.product.czmtr.ram.asset.dto.AssetSequestrationTempDto;


public interface IAssetSequestrationService {

    Map<String, Object> getAssetSequestrationForDataGrid(ISearchExpression iSearchExpression, AssetSequestrationDto assetSequestrationDto, PageableDto pageableDto
    );

    AssetSequestrationDto createAssetSeal(String assetSequestrationTempDtoList, AssetSequestrationDto assetSequestrationDto,
                                          UserInfoDto userInfoDto);

    String getAssetSealNum();

    void deleteAssetSeal(String id);

    List<AssetSequestrationTempDto> getAssetSeal(String id);

    Map<String, Object> getAssetSealForUpdateDataGrid(
            AssetSequestrationDto assetSequestrationDto, PageableDto pageableDto);

    void save(String id, String assetSequestrationTempDtoList);

    void deleteById(String id, String assetSequestrationTempDtoList);

    void updateSeal(AssetSequestrationDto assetSequestrationDto);

    Object getAssetSealById(String id);

    AssetSequestrationDto createAssetUnseal(String assetSequestrationTempDtoList, AssetSequestrationDto assetSequestrationDto,
                                            UserInfoDto userInfoDto);

    Map<String, Object> getSealViewById(String id);

    void commonCheckAssetSequestrationDto(AssetSequestrationDto dto);

    void approvalAssetSeal(String id);
}

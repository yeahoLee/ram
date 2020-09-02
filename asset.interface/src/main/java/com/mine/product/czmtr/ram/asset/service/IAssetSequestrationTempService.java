package com.mine.product.czmtr.ram.asset.service;

import com.mine.product.czmtr.ram.asset.dto.AssetSequestrationTempDto;

import java.util.ArrayList;
import java.util.List;

public interface IAssetSequestrationTempService {

    void save(String id, String assetSequestrationTempDtoList);

    void createSealAsset(ArrayList<AssetSequestrationTempDto> assetSequestrationTempDtos, String id);

    void deleteById(String id, String assetSequestrationTempDtoList);

    void createAssetUnseal(ArrayList<AssetSequestrationTempDto> assetSequestrationTempDtos, String id);

    List<String> getSealAssetIdList(String id);
    List<String> getUnSealAssetIdList(String id);
}

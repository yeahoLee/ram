package com.mine.product.czmtr.ram.asset.service;

import java.util.List;
import java.util.Map;

import com.mine.platform.common.dto.PageDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.product.czmtr.ram.asset.dto.HeadAssetDto;

public interface IHeadAssetService {

    List<HeadAssetDto> getHeadAssetModelByHeadId(String headId);

    HeadAssetDto createHeadAsset(HeadAssetDto dto);

    void deleteHeadAsset(String id);

    HeadAssetDto updateHeadAsset(HeadAssetDto dto);

    HeadAssetDto getHeadAssetById(String id);

    PageDto<HeadAssetDto> getHeadAsset(String hql, Map<String, Object> params, PageableDto pageable);

    PageDto<HeadAssetDto> getHeadAsset(String hql, String countHql, Map<String, Object> params, PageableDto pageable);
}

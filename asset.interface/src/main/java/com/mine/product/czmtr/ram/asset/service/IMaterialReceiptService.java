package com.mine.product.czmtr.ram.asset.service;

import com.mine.platform.common.dto.PageDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.product.czmtr.ram.asset.dto.AssetAssetDto;
import com.mine.product.czmtr.ram.asset.dto.FlowableInfo;
import com.mine.product.czmtr.ram.asset.dto.MaterialReceiptDto;

import java.util.List;
import java.util.Map;

public interface IMaterialReceiptService extends IApproveNotify{
    MaterialReceiptDto createMaterialReceipt(MaterialReceiptDto dto);

    void deleteMaterialReceipt(String id);

    MaterialReceiptDto updateMaterialReceipt(MaterialReceiptDto dto);

    MaterialReceiptDto getMaterialReceiptById(String id);

    PageDto<MaterialReceiptDto> getMaterialReceipt(String hql, Map<String, Object> params, PageableDto pageable);

    PageDto<MaterialReceiptDto> getMaterialReceipt(String hql, String countHql, Map<String, Object> params, PageableDto pageable);

    String getMaxRunningNum(String runningNum);

    void changeReceiptStatus(String id, FlowableInfo.WORKSTATUS exStatus, FlowableInfo.WORKSTATUS status);

    void checkReceipt(String id);

    void createAssetListByMateReceId(String mateReceId);

    List<String> checkAssetProduceType(List<AssetAssetDto> assetDtoList, IAssetService.ASSET_PRODUCE_TYPE type);
 }

package com.mine.product.czmtr.ram.asset.service;

import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.AssetInventoryTempDto;
import com.mine.product.czmtr.ram.asset.dto.MyAssetInventoryDto;

import java.util.List;
import java.util.Map;

public interface IAssetInventoryTempService extends IApproveNotify{
    byte[] exportAssetInventoryList(List<String> assetInventoryList);

    // 更新我的盘点单
    void UpdateMyAssetInventory(MyAssetInventoryDto dto);

    // 更新我的盘点清单信息
    void UpdateAssetInventoryTempModel(AssetInventoryTempDto dto);

    // 查询我的盘点单明细
    Map<String, Object> getAssetInventoryTempForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto);

    // 查询我的盘点单明细
    Map<String, Object> getAssetInventoryTempForDataGridByDto(AssetInventoryTempDto dto, PageableDto pageableDto);

    //查询盘点任务列表
    List<String> assetInventoryList(String myAssetInventoryId);

    //更新我的盘点单状态
    void UpdateMyAssetInventoryStatus(MyAssetInventoryDto dto);

    //判断我的盘点单是否可以提交
    Boolean checkForSubmit(MyAssetInventoryDto dto);

    //撤回我的盘点单
    void RecallMyInventory(MyAssetInventoryDto dto);

	List<String> getAssetIdList(String id);
}

package com.mine.product.czmtr.ram.asset.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Pageable;

import com.mine.base.user.dto.UserInfoDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.AssetAssetDto;
import com.mine.product.czmtr.ram.asset.dto.AssetInventoryDto;
import com.mine.product.czmtr.ram.asset.dto.AssetInventoryScopeDto;
import com.mine.product.czmtr.ram.asset.dto.MyAssetInventoryDto;

public interface IAssetInventoryService extends IApproveNotify{

    // 创建盘点任务单
    AssetInventoryDto createInventory(List<AssetAssetDto> assetDtoList,
                                      ArrayList<AssetInventoryScopeDto> assetInventoryScopeDtos, UserInfoDto userInfoDto, AssetInventoryDto dto);

    // 获取datagrid
    Map<String, Object> getAssetInventoryForDataGrid(ISearchExpression iSearchExpression, AssetInventoryDto dto,
                                                     PageableDto pageableDto);

    // 获取盘点单
    Map<String, Object> getAssetInventory(AssetInventoryDto dto, PageableDto pageableDto);

    // 查询我的盘点单
    Map<String, Object> getMyAssetInventoryForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto);

    // 发布盘点
    void distributionInventory(String assetInventoryModel_ID);

    // 通过id获取盘点任务单；
    AssetInventoryDto getAssetInventoryById(String id);

    // 通过盘点任务主表id获取盘点范围；
    List<AssetInventoryScopeDto> getAssetInventoryScopeDtoList(String assetInventoryId);

    // 删除盘点单
    void deleteAssetInventory(String id);

    // 通过IDs批量删除我的盘点单；
    void deleteAssetInventoryTempByIds(String assetInventoryTempIds);

    // 通过ID删除盘点任务单；
    void deleteAssetInventoryTempById(String assetInventoryTempId);

    // 通过主表ID删除盘点范围
    void deleteAssetInventoryScopeById(String assetInventoryScopeId);

    // 获取加入盘点的资产IDs
    Set<String> getAssetIdFromInventoryTemp(String id);

    // 新增盘点范围
    void addAssetInventoryScope(AssetInventoryScopeDto assetInventoryScopeDto);

    // 新增我的盘点单
    void addAssetInventoryTemp(String assetInventoryId, String assetDtoList);

    // 更新盘点任务单；
    AssetInventoryDto updateAssetInventory(AssetInventoryDto dto);

    // 保存并发布盘点任务单
    AssetInventoryDto saveAndDistributionInventory(List<AssetAssetDto> assetDtoList,
                                                   ArrayList<AssetInventoryScopeDto> assetInventoryScopeDtos, UserInfoDto userInfoDto, AssetInventoryDto dto);

    // 查询我的盘点单
    Map<String, Object> getMyAssetInventoryForDataGrid(String assetInventoryId, Pageable pageable);

    // 获取我的盘点单
    MyAssetInventoryDto getMyAssetInventoryDtoById(MyAssetInventoryDto dto);

    // 获取盘点任务信息
    AssetInventoryDto getAssetInventoryDtoById(String id);

    // 获取我的盘点单信息
    Map<String, Object> getMyAssetInventory(MyAssetInventoryDto dto, PageableDto pageableDto);

    // 保存并发布盘点单
    AssetInventoryDto createAndDistributionInventory(List<AssetAssetDto> assetDtoList,
                                                     ArrayList<AssetInventoryScopeDto> assetInventoryScopeDtos, UserInfoDto userInfoDto, AssetInventoryDto dto);

    // 更新并发布盘点单任务
    void updateAndDistributionAssetInventory(AssetInventoryDto assetInventoryDto);

    // 更加盘点任务ID获取所有我的盘点单
    List<MyAssetInventoryDto> getMyAssetInventoryDtoList(String assetInventoryId);

    // 执行发起审批逻辑
    void doApproval(String assetInventoryId);
}

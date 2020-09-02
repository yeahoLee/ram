package com.mine.product.czmtr.ram.asset.service;

import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.AssetManagerChangeDto;
import com.mine.product.czmtr.ram.asset.dto.AssetSavePlaceChangeDto;
import com.mine.product.czmtr.ram.asset.dto.AssetUserChangeDto;

import java.util.Map;

public interface IAssetChangeService {
    public static enum RECEIPT_STATUS {
        拟稿, 审核中, 审核通过, 审核不通过
    }

    // 资产管理员变更
    AssetManagerChangeDto saveChangeManager(AssetManagerChangeDto dto);

    void saveAndCheckChangeManager(AssetManagerChangeDto dto);

    public Map<String, Object> getAssetManagerChangeForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto);

    void deleteChangeManagerReceipt(String id);

    void checkChangeManagerReceipt(String id);

    AssetManagerChangeDto getManagerChangeReceiptById(String id);

    void updateChangeManagerReceipt(AssetManagerChangeDto dto);

    // 资产使用人变更
    AssetUserChangeDto saveChangeUser(AssetUserChangeDto dto);

    void saveAndCheckChangeUser(AssetUserChangeDto dto);

    public Map<String, Object> getAssetUserChangeForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto);

    void deleteChangeUserReceipt(String id);

    void checkChangeUserReceipt(String id);

    AssetUserChangeDto getUserChangeReceiptById(String id);

    void updateChangeUserReceipt(AssetUserChangeDto dto);

    // 资产安装位置变更
    AssetSavePlaceChangeDto saveChangeSavePlace(AssetSavePlaceChangeDto dto);

    void saveAndCheckChangeSavePlace(AssetSavePlaceChangeDto dto);

    public Map<String, Object> getAssetSavePlaceChangeForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto);

    void deleteChangeSavePlaceReceipt(String id);

    void checkChangeSavePlaceReceipt(String id);

    AssetSavePlaceChangeDto getSavePlaceChangeReceiptById(String id);

    void updateChangeSavePlaceReceipt(AssetSavePlaceChangeDto dto);
}

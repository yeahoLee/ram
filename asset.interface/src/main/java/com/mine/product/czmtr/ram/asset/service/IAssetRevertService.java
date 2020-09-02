package com.mine.product.czmtr.ram.asset.service;

import com.mine.base.user.dto.UserInfoDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.AssetRevertDto;

import java.util.Map;

public interface IAssetRevertService extends  IApproveNotify {

    /**
     * 资产归还单状态
     */
    public static enum ASSETREVERT_STATUS {
        拟稿, 审批中, 已审批
    }

    /**
     * 通用检查
     *
     * @param dto
     */
    public void commonCheckAssetRevertDto(AssetRevertDto dto);

    /**
     * 添加
     */
    public AssetRevertDto createAssetRevert(String assetBorrowTempDtoList, AssetRevertDto dto, UserInfoDto userInfoDto);

    /**
     * 修改
     */
    public AssetRevertDto updateAssetRevert(AssetRevertDto dto);

    public void updateAssetRevertStatus(AssetRevertDto dto);

    /**
     * 删除
     */
    public void deleteAssetRevert(String AssetRevertId);

    /**
     * 查询
     */
    public AssetRevertDto getAssetRevertDto(String id);

    public String getAssetRevertCode();

    public Map<String, Object> getAssetRevertDtoByQuerysForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto);

    public Map<String, Object> getAssetRevertByForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto, AssetRevertDto assetRevertDto);
}

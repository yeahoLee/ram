package com.mine.product.czmtr.ram.asset.service;

import com.mine.base.user.dto.UserInfoDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.AssetReceiveRevertDto;

import java.util.Map;

public interface IAssetReceiveRevertService extends  IApproveNotify {

    /**
     * 资产归还单状态
     */
    public static enum ASSETRECEIVEREVERT_STATUS {
        拟稿, 审批中, 已审批
    }

    public void commonCheckAssetReceiveRevertDto(AssetReceiveRevertDto dto);

    public AssetReceiveRevertDto getAssetReceiveRevertDto(String id);

    public String getAssetReceiveRevertCode();

    public Map<String, Object> getAssetReceiveRevertDtoByQuerysForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto);

    public void deleteAssetReceiveRevert(String AssetReceiveRevertId);

    public AssetReceiveRevertDto createAssetReceiveRevert(String assetReceiveUseTempDtoList, AssetReceiveRevertDto dto, UserInfoDto userInfoDto);

    public AssetReceiveRevertDto updateAssetReceiveRevert(AssetReceiveRevertDto dto);

    public void updateAssetReceiveRevertStatus(AssetReceiveRevertDto dto);

    public Map<String, Object> getAssetReceiveRevertByForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto, AssetReceiveRevertDto assetReceiveRevertDto);
}

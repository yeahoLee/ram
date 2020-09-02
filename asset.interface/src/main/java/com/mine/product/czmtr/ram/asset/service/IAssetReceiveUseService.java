package com.mine.product.czmtr.ram.asset.service;

import com.mine.base.user.dto.UserInfoDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.AssetReceiveUseDto;

import java.util.Map;

public interface IAssetReceiveUseService extends  IApproveNotify{

    /**
     * 资产领用单状态
     */
    public static enum ASSETRECEIVEUSE_STATUS {
        拟稿, 审批中, 已审批
    }

    /***
     * 资产归还状态
     * @author yangjie
     *
     */
    public static enum ASSETRECEIVEUSE_REVERTSTATUS {
        未归还, 部分归还, 已归还
    }

    /**
     * 通用检查
     *
     * @param dto
     */
    public void commonCheckAssetReceiveUseDto(AssetReceiveUseDto dto);

    /**
     * 添加
     */
    public AssetReceiveUseDto createAssetReceiveUse(String assetReceiveUseDtoTempDtoList, AssetReceiveUseDto dto, UserInfoDto userInfoDto);

    /**
     * 修改
     */
    public AssetReceiveUseDto updateAssetReceiveUse(AssetReceiveUseDto dto);

    public void deleteAssetReceiveUse(String assetReceiveUseID);

    public AssetReceiveUseDto getAssetReceiveUseDto(String id);

    public Object convertAssetReceiveUseModel(AssetReceiveUseDto dto);

    public String getAssetReceiveUseCode();

    public Map<String, Object> getAssetReceiveUseDtoByQuerysForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto);

    public AssetReceiveUseDto updateAssetReceiveUseStatus(AssetReceiveUseDto dto);

    public Map<String, Object> getAssetReceiveUseByForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto, AssetReceiveUseDto assetReceiveUseDto);
}

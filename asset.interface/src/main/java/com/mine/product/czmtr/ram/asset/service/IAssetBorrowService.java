package com.mine.product.czmtr.ram.asset.service;

import com.mine.base.user.dto.UserInfoDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.AssetBorrowDto;

import java.util.Map;

public interface IAssetBorrowService extends IApproveNotify {
    /**
     * 资产借用单状态
     */
    public static enum ASSETBORROW_REVERTSTATUS {
        未归还, 部分归还, 已归还
    }

    /**
     * 通用检查
     *
     * @param dto
     */
    public void commonCheckAssetBorrowDto(AssetBorrowDto dto);

    /**
     * 添加
     */
    public AssetBorrowDto createAssetBorrow(String assetBorrowTempDtoList, AssetBorrowDto dto, UserInfoDto userInfoDto);

    /**
     * 修改
     */
    public AssetBorrowDto updateAssetBorrow(AssetBorrowDto dto);

    //模拟发送审批
    public void updateAssetBorrowStatus(AssetBorrowDto dto);

    /**
     * 删除
     */
    public void deleteAssetBorrow(String borrowID);

    /**
     * 查询
     */
    public String getAssetborrowCode();

    public Map<String, Object> getAssetBorrowDtoByQuerysForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto);

    public AssetBorrowDto getAssetBorrowDto(String id);

    public Object convertAssetBorrowModel(AssetBorrowDto dto);

    public Map<String, Object> getAssetBorrowByForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto, AssetBorrowDto assetBorrowDto);
}

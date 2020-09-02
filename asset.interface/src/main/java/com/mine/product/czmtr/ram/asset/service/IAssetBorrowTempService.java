package com.mine.product.czmtr.ram.asset.service;

import com.mine.base.user.dto.UserInfoDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.AssetAssetDto;
import com.mine.product.czmtr.ram.asset.dto.AssetBorrowDto;
import com.mine.product.czmtr.ram.asset.dto.AssetBorrowTempDto;
import com.mine.product.czmtr.ram.asset.dto.AssetRevertDto;

import java.util.List;
import java.util.Map;

public interface IAssetBorrowTempService {

    List<String> getAssetIdListByAssetReverId(String id);

    /**
     * 添加
     */
    public AssetBorrowTempDto createAssetBorrowTemp(AssetBorrowTempDto dto);

    public void createAssetBorrowTempForList(List<AssetBorrowTempDto> dtolist, String assetBorrowModelId);

    public void createAssetBorrowTempByassetIdListStr(String assetIdListStr, AssetBorrowDto dto);

    public void createAssetBorrowTempByAssetIdListStrForRevert(String assetIdListStr, AssetRevertDto dto);

    public void createAssetRevertTempForList(List<AssetBorrowTempDto> dtolist, String assetBorrowModelId);

    public List<AssetBorrowTempDto> getAssetBorrowTempDtoListByRevertId(String assetRevertId);

    /**
     * 修改
     */
    public void updateAssetBorrowTemp(AssetBorrowTempDto dto);

    public void updateAssetBorrowTempBydDeleteAssetRevertModel(AssetBorrowTempDto dto);

    /**
     * 删除
     */
    public void deleteAssetBorrowTempByAssetBorrowID(List<String> assetIdListStr, String AssetBorrowID);

    public void deleteAssetBorrowTempByAssetRevertID(List<String> assetIdListStr, String AssetRevertID);

    /**
     * 查询
     */
    public List<AssetBorrowTempDto> getAssetBorrowTempDtoList(String id);

    public AssetBorrowTempDto getAssetBorrowTempDto(String id);

    public Map<String, Object> getAssetBorrowTempDtoForDataGrid(ISearchExpression searchExpression, PageableDto pageableDto);

    public List<AssetBorrowTempDto> getListByUserId(String userId);

    public List<AssetBorrowTempDto> getNRAsset(UserInfoDto userInfoDto, AssetAssetDto asset);

	Map<String, String> getAssetMap(String id);

	List<String> getAssetIdList(String id);
}

package com.mine.product.czmtr.ram.asset.service;

import com.mine.base.user.dto.UserInfoDto;
import com.mine.product.czmtr.ram.asset.dto.AssetAssetDto;
import com.mine.product.czmtr.ram.asset.dto.AssetReceiveRevertDto;
import com.mine.product.czmtr.ram.asset.dto.AssetReceiveUseDto;
import com.mine.product.czmtr.ram.asset.dto.AssetReceiveUseTempDto;

import java.util.List;
import java.util.Map;

public interface IAssetReceiveUseTempService {

	List<String> getAssetIdListByReceiveRevertId(String id);

	public void createAssetReceiveUseTempForList(List<AssetReceiveUseTempDto> dtolist, String assetReceiveUseModelId);

    public void createAssetReceiveUseTempByassetIdListStr(String assetIdListStr, AssetReceiveUseDto dto);

    /**
     * 删除
     */
    public List<AssetReceiveUseTempDto> getAssetReceiveUseTempDtoList(String assetReceiveUseId);

    public AssetReceiveUseTempDto getAssetReceiveUseTempDto(String id);

    public void createAssetReceiveRevertTempForList(List<AssetReceiveUseTempDto> dtolist, String assetReceiveRevertModelId);

    public void updateAssetReceiveUseTemp(AssetReceiveUseTempDto dto);

    public void updateAssetReceiveUseTempByDeleteAssetReceiveRevertModel(AssetReceiveUseTempDto dto);

    public List<AssetReceiveUseTempDto> getAssetReceiveUseTempDtoListByRevertId(String assetReceiveRevertId);

    public List<AssetReceiveUseTempDto> getListByUserId(String userId);

    public List<AssetReceiveUseTempDto> getNRAsset(UserInfoDto userInfoDto, AssetAssetDto asset);

    public void createAssetReceiveUseTempByAssetIdListStrForRevert(String assetIdListStr, AssetReceiveRevertDto dto);

    public void deleteAssetReceiveUseTempByAssetReceiveRevertID(List<String> tempID, String AssetReceiveRevertID);

    public void deleteAssetReceiveUseTempByAssetReceiveUseID(List<String> tempID, String AssetReceiveUseID);

    /**
     * @Description: 根据主表id获取资产id列表
     * @Param: id 主表id
     * @return:
     * @Author: lichuan.zhang
     * @Date: 2020/7/28
     */
    List<String> getAssetIdList(String id);
    Map<String ,String> getAssetMap(String id);
}

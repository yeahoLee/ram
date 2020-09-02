package com.mine.product.czmtr.ram.asset.service;

import com.mine.platform.common.dto.PageDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.product.czmtr.ram.asset.dto.AssetAssetDto;

import java.util.List;
import java.util.Map;

public interface IAssetTempService {
    public void createAssetTemp(AssetAssetDto dto);

    public void deleteAssetTempByRecId(String recId);

    public int getAssetTempCountByRecId(String id);

    PageDto<AssetAssetDto> getAssetTempByRecId(String hql, Map<String, Object> params, PageableDto pageable);

    List<AssetAssetDto> getAssetTempByRecId(String recId);

    public void updateAssetTemp(AssetAssetDto dto);

    public void deleteTempAssetByIdList(String[] deleteIdList, String recId);

    /**
     * @Description:  插入已经审批的资产编号；
     * @Param:
     * @return:
     * @Author: lichuan.zhang
     * @Date: 2020/8/5 
     */
	void insertAssetCode(Map<String, String> map);

}

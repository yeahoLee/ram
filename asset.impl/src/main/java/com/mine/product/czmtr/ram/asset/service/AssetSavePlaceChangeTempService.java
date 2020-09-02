package com.mine.product.czmtr.ram.asset.service;

import com.mine.product.czmtr.ram.asset.dao.AssetSavePlaceChangeTempDao;
import com.mine.product.czmtr.ram.asset.model.AssetSavePlaceChangeTempModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Program: ram
 * @Description: 管理员变更子表
 * @Author: lichuan.zhang
 * @Date: 2020/7/28
 **/
@Service
public class AssetSavePlaceChangeTempService implements IAssetSavePlaceTempService {
	@Autowired
	private AssetSavePlaceChangeTempDao savePlaceChangeTempDao;

	@Override
	public List<String> getAssetIdList(String id) {
		List<AssetSavePlaceChangeTempModel> tempList = savePlaceChangeTempDao.findByAssetSavePlaceChangeModelId(id);
		if (tempList.size() <= 0) {
			throw new RuntimeException("资产列表不能为空！");
		}
		return tempList.stream().map(e -> e.getAssetId()).collect(Collectors.toList());
	}


}

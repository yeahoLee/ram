package com.mine.product.czmtr.ram.asset.service;

import com.mine.product.czmtr.ram.asset.dao.AssetManagerChangeTempDao;
import com.mine.product.czmtr.ram.asset.model.AssetManagerChangeTempModel;
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
public class AssetAdminChangeTempService implements IAssetAdminChangeTempService {
	@Autowired
	private AssetManagerChangeTempDao managerChangeTempDao;

	@Override
	public List<String> getAssetIdList(String id) {
		List<AssetManagerChangeTempModel> tempList = managerChangeTempDao.findByAssetManagerChangeModelId(id);
		if (tempList.size() <= 0) {
			throw new RuntimeException("资产列表不能为空！");
		}
		return tempList.stream().map(e -> e.getAssetId()).collect(Collectors.toList());
	}


}

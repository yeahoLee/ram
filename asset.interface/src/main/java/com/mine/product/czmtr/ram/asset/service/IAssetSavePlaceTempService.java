package com.mine.product.czmtr.ram.asset.service;

import java.util.List;

public interface IAssetSavePlaceTempService {
	/**
	 * @Description: 根据主表id获取资产id列表
	 * @Param: id 主表id
	 * @return:
	 * @Author: lichuan.zhang
	 * @Date: 2020/7/28
	 */
	List<String> getAssetIdList(String id);
}

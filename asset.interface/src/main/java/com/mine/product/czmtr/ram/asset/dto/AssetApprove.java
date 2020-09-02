package com.mine.product.czmtr.ram.asset.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Program: ram
 * @Description: 发起审批回调
 * @Author: lichuan.zhang
 * @Date: 2020/7/16
 **/
@Data
public class AssetApprove {

	//可以审批
	private boolean canApprove;

	private String message;

	//不可审批资产列表
	private List<ApprovenAssetDto> unAssetList;

	public List<ApprovenAssetDto> getUnAssetList() {
		if (unAssetList == null) {
			unAssetList = new ArrayList<>();
		}
		return unAssetList;
	}


}


package com.mine.product.czmtr.ram.asset.dto;

import com.mine.product.czmtr.ram.asset.service.IAssetService;
import lombok.Data;

/**
 * @Program: ram
 * @Description: 不可审批资产列表
 * @Author: lichuan.zhang
 * @Date: 2020/7/27
 **/
@Data
public class ApprovenAssetDto {
	//资产编码
	private String assetCode;
	//资产名称
	private String assetChsName;
	//资产状态
	private IAssetService.ASSET_STATUS assetStatus;

	public ApprovenAssetDto setAssetCode(String assetCode) {
		this.assetCode = assetCode;
		return this;
	}

	public ApprovenAssetDto setAssetChsName(String assetChsName) {
		this.assetChsName = assetChsName;
		return this;
	}

	public ApprovenAssetDto setAssetStatus(IAssetService.ASSET_STATUS assetStatus) {
		this.assetStatus = assetStatus;
		return this;
	}
}

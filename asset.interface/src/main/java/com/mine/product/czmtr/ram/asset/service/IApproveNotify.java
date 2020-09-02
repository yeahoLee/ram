package com.mine.product.czmtr.ram.asset.service;

import com.mine.product.czmtr.ram.asset.dto.AssetApprove;
import com.mine.product.czmtr.ram.asset.dto.FlowableInfo;

/**
 * @Program: ram
 * @Description: 审批回调接口
 * @Author: lichuan.zhang
 * @Date: 2020/7/16
 **/
public interface IApproveNotify {
	/**
	 * @Description: 发起审批回调
	 * @Param: businessId 业务ID
	 * @return:
	 * @Author: lichuan.zhang
	 * @Date: 2020/7/16
	 */
	AssetApprove doApproveNotify(String id);

	/**
	 * @Description: 更新审批状态时回调
	 * @Param:
	 * @return:
	 * @Author: lichuan.zhang
	 * @Date: 2020/7/28
	 */
	void approveUpdateNotify(FlowableInfo.WORKSTATUS workStatus, String id);
}

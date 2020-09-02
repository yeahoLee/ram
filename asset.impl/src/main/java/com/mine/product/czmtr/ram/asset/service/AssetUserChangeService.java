package com.mine.product.czmtr.ram.asset.service;

import com.mine.base.user.dto.UserInfoDto;
import com.mine.base.user.service.IUserService;
import com.mine.product.czmtr.ram.asset.dao.AssetUserChangeDao;
import com.mine.product.czmtr.ram.asset.dto.AssetApprove;
import com.mine.product.czmtr.ram.asset.dto.AssetHistoryDto;
import com.mine.product.czmtr.ram.asset.dto.FlowableInfo;
import com.mine.product.czmtr.ram.asset.model.AssetUserChangeModel;
import com.vgtech.platform.common.utility.VGUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AssetUserChangeService implements IAssetUserChangeService {
	@Autowired
	private AssetUserChangeDao userChangeDao;

	@Autowired
	private AssetUserChangeTempService assetUserChangeTempService;
	@Autowired
	private IAssetService assetService;

	@Autowired
	private IUserService userService;


	@Override
	public AssetApprove doApproveNotify(String id) {
		List<String> assetIdList = assetUserChangeTempService.getAssetIdList(id);
		//校验资产状态，是否可以审批；
		return assetService.doAssetApproveCheck(assetIdList, null);
	}


	@Override
	public void approveUpdateNotify(FlowableInfo.WORKSTATUS workStatus, String id) {
		List<String> assetIdList;
		switch (workStatus) {
			case 审批中:
				//资产冻结
				assetIdList = assetUserChangeTempService.getAssetIdList(id);
				assetService.LockAssetState(assetIdList);
				break;
			case 驳回:
				//资产状态回滚
				assetIdList = assetUserChangeTempService.getAssetIdList(id);
				assetService.approveFailureAssetStateRollback(assetIdList);
				break;
			case 已审批:
				//将资产状态落地
				AssetUserChangeModel model = userChangeDao.findById(id).get();
				assetIdList = assetUserChangeTempService.getAssetIdList(id);
				assetService.approveSuccessUpdateAssetState(assetIdList, null);
				assetService.changeAssetUser(assetIdList, model.getAssetUserId());
				this.createHistory(model, assetIdList);
				break;
			default:
				break;

		}
		//更新主表状态
		AssetUserChangeModel model = userChangeDao.findById(id).get();
		model.setReceiptStatus(workStatus);
	}

	private void createHistory(AssetUserChangeModel model, List<String> assetIdList) {
		UserInfoDto OldAssetUser = new UserInfoDto();
		UserInfoDto AssetUser = new UserInfoDto();
		if (!VGUtility.isEmpty(model.getOldAssetUserId())) {
			OldAssetUser = userService.getUserInfo(model.getOldAssetUserId());
		}
		if (!VGUtility.isEmpty(model.getAssetUserId())) {
			AssetUser = userService.getUserInfo(model.getAssetUserId());
		}
		for (String assetId : assetIdList) {
			AssetHistoryDto historyDto = new AssetHistoryDto();
			historyDto.setHistoryType(IAssetService.HISTORY_TYPE.变更记录);
			historyDto.setChangeType(String.valueOf(IAssetService.CHANGE_TYPE.使用人变更.ordinal()));
			historyDto.setAssetModelId(assetId);
			historyDto.setCreateUserId(model.getCreateUserId());
			historyDto.setChangeContent("使用人变更:新增"
					+ "[" + OldAssetUser.getChsName() + "]->"
					+ "[" + AssetUser.getChsName() + "]");
			assetService.createHistory(historyDto);
			assetService.createHistory(historyDto);
		}

	}

}

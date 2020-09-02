package com.mine.product.czmtr.ram.asset.service;

import com.mine.base.user.dto.UserInfoDto;
import com.mine.base.user.service.IUserService;
import com.mine.product.czmtr.ram.asset.dao.AssetManagerChangeDao;
import com.mine.product.czmtr.ram.asset.dto.AssetApprove;
import com.mine.product.czmtr.ram.asset.dto.AssetHistoryDto;
import com.mine.product.czmtr.ram.asset.dto.FlowableInfo;
import com.mine.product.czmtr.ram.asset.model.AssetManagerChangeModel;
import com.vgtech.platform.common.utility.VGUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AssetAdminChangeService implements IAssetAdminChangeService {
	@Autowired
	private AssetManagerChangeDao managerChangeDao;

	@Autowired
	private IAssetAdminChangeTempService assetAdminChangeTempService;
	@Autowired
	private IAssetService assetService;
	@Autowired
	private IUserService userService;


	@Override
	public AssetApprove doApproveNotify(String id) {
		List<String> assetIdList = assetAdminChangeTempService.getAssetIdList(id);
		//校验资产状态，是否可以审批；
		AssetApprove approve = assetService.doAssetApproveCheck(assetIdList, null);
		if (!approve.isCanApprove()) {
			return approve;
		}
		return approve;
	}


	@Override
	public void approveUpdateNotify(FlowableInfo.WORKSTATUS workStatus, String id) {
		List<String> assetIdList;
		switch (workStatus) {
			case 审批中:
				//资产冻结
				assetIdList = assetAdminChangeTempService.getAssetIdList(id);
				assetService.LockAssetState(assetIdList);
				break;
			case 驳回:
				//资产状态回滚
				assetIdList = assetAdminChangeTempService.getAssetIdList(id);
				assetService.approveFailureAssetStateRollback(assetIdList);
				break;
			case 已审批:
				//变更资产管理员
				AssetManagerChangeModel managerChangeModel = managerChangeDao.findById(id).get();
				assetIdList = assetAdminChangeTempService.getAssetIdList(id);
				assetService.approveSuccessUpdateAssetState(assetIdList, null);
				assetService.changeAssetManager(assetIdList, managerChangeModel.getAssetManagerId());
				this.createHistory(managerChangeModel,assetIdList);
				break;
			default:
				break;

		}
		//更新主表状态
		AssetManagerChangeModel model = managerChangeDao.findById(id).get();
		model.setReceiptStatus(workStatus);
	}


	private void createHistory(AssetManagerChangeModel model, List<String> assetIdList) {
		UserInfoDto OldAssetManager = new UserInfoDto();
		UserInfoDto AssetManager = new UserInfoDto();
		if (!VGUtility.isEmpty(model.getOldAssetManagerId())) {
			OldAssetManager = userService.getUserInfo(model.getOldAssetManagerId());
		}
		if (!VGUtility.isEmpty(model.getAssetManagerId())) {
			AssetManager = userService.getUserInfo(model.getAssetManagerId());
		}
		for (String assetId : assetIdList) {
			AssetHistoryDto historyDto = new AssetHistoryDto();
			historyDto.setHistoryType(IAssetService.HISTORY_TYPE.变更记录);
			historyDto.setChangeType(String.valueOf(IAssetService.CHANGE_TYPE.管理人变更.ordinal()));
			historyDto.setAssetModelId(assetId);
			historyDto.setCreateUserId(model.getCreateUserId());
			historyDto.setChangeContent("管理员变更:审批通过"
					+ "[" + OldAssetManager.getChsName() + "]->"
					+ "[" + AssetManager.getChsName() + "]");
			assetService.createHistory(historyDto);
		}

	}

}

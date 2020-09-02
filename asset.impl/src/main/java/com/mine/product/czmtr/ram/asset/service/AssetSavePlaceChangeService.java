package com.mine.product.czmtr.ram.asset.service;

import com.mine.base.dict.dto.DictDto;
import com.mine.base.dict.service.IDictService;
import com.mine.product.czmtr.ram.asset.dao.AssetSavePlaceChangeDao;
import com.mine.product.czmtr.ram.asset.dto.AssetApprove;
import com.mine.product.czmtr.ram.asset.dto.AssetAssetDto;
import com.mine.product.czmtr.ram.asset.dto.AssetHistoryDto;
import com.mine.product.czmtr.ram.asset.dto.FlowableInfo;
import com.mine.product.czmtr.ram.asset.model.AssetSavePlaceChangeModel;
import com.vgtech.platform.common.utility.VGUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AssetSavePlaceChangeService implements IAssetSavePlaceChangeService {

	@Autowired
	private AssetSavePlaceChangeDao savePlaceChangeDao;


	@Autowired
	private AssetSavePlaceChangeTempService assetSavePlaceChangeTempService;
	@Autowired
	private IAssetService assetService;

//	@Autowired
//	private IUserService userService;

	@Autowired
	private IDictService dictService;


	@Override
	public AssetApprove doApproveNotify(String id) {
		List<String> assetIdList = assetSavePlaceChangeTempService.getAssetIdList(id);
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
				assetIdList = assetSavePlaceChangeTempService.getAssetIdList(id);
				assetService.LockAssetState(assetIdList);
				break;
			case 驳回:
				//资产状态回滚
				assetIdList = assetSavePlaceChangeTempService.getAssetIdList(id);
				assetService.approveFailureAssetStateRollback(assetIdList);
				break;
			case 已审批:
				//将资产状态落地
				AssetSavePlaceChangeModel model = savePlaceChangeDao.findById(id).get();
				assetIdList = assetSavePlaceChangeTempService.getAssetIdList(id);
				assetService.approveSuccessUpdateAssetState(assetIdList, null);
				assetService.changeAssetSavePlace(assetIdList, model.getAssetSavePlaceId());
				this.createHistory(model, assetIdList);
				break;
			default:
				break;

		}
		//更新主表状态
		AssetSavePlaceChangeModel model = savePlaceChangeDao.findById(id).get();
		model.setReceiptStatus(workStatus);
	}

	private void createHistory(AssetSavePlaceChangeModel model, List<String> assetIdList) {
		String NewAssetSavePlaceStr = "";
		// 根据assetId查到assetDto,根据dto中的placeId查到dictionaryDto，再查到code和chsName
		if (!VGUtility.isEmpty(model.getAssetSavePlaceId())) {
			DictDto savePlaceDto = dictService.getCommonCode(model.getAssetSavePlaceId());
			if (!VGUtility.isEmpty(savePlaceDto)) {
				NewAssetSavePlaceStr = savePlaceDto.getCode() + " " + savePlaceDto.getChsName();
			}
		}


		for (String assetId : assetIdList) {

			AssetAssetDto asset = assetService.getAssetByAssetId(assetId);
			String OldAssetSavePlaceStr = "";
			// 根据assetId查到assetDto,根据dto中的placeId查到dictionaryDto，再查到code和chsName
			if (!VGUtility.isEmpty(asset.getSavePlaceId())) {
				DictDto savePlaceDto = dictService.getCommonCode(asset.getSavePlaceId());
				if (!VGUtility.isEmpty(savePlaceDto)) {
					OldAssetSavePlaceStr = savePlaceDto.getCode() + " " + savePlaceDto.getChsName();
				}
			}

			AssetHistoryDto historyDto = new AssetHistoryDto();
			historyDto.setHistoryType(IAssetService.HISTORY_TYPE.变更记录);
			historyDto.setChangeType(String.valueOf(IAssetService.CHANGE_TYPE.位置变更.ordinal()));
			historyDto.setAssetModelId(assetId);
			historyDto.setCreateUserId(model.getCreateUserId());
			historyDto.setChangeContent("位置变更:"
					+ "[" + OldAssetSavePlaceStr + "]->"
					+ "[" + NewAssetSavePlaceStr + "]");
			assetService.createHistory(historyDto);
		}

	}

}

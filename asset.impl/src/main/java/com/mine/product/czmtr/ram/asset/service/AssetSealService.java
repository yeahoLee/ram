package com.mine.product.czmtr.ram.asset.service;

import com.mine.product.czmtr.ram.asset.dao.AssetSequestrationDao;
import com.mine.product.czmtr.ram.asset.dto.AssetApprove;
import com.mine.product.czmtr.ram.asset.dto.AssetHistoryDto;
import com.mine.product.czmtr.ram.asset.dto.FlowableInfo;
import com.mine.product.czmtr.ram.asset.model.AssetSequestrationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Program: ram
 * @Description: 资产封存
 * @Author: lichuan.zhang
 * @Date: 2020/7/30
 **/
@Service
public class AssetSealService implements IAssetSealService {

	@Autowired
	private AssetSequestrationDao assetSequestrationDao;

	@Autowired
	private IAssetSequestrationTempService assetSequestrationTempService;
	@Autowired
	private IAssetService assetService;


	@Override
	public AssetApprove doApproveNotify(String id) {
		List<String> assetIdList =  assetSequestrationTempService.getSealAssetIdList(id);
		//校验资产状态，是否可以审批；
		return assetService.doAssetApproveCheck(assetIdList, IAssetService.ASSET_STATUS.封存);
	}


	@Override
	public void approveUpdateNotify(FlowableInfo.WORKSTATUS workStatus, String id) {
		List<String> assetIdList;
		switch (workStatus) {
			case 审批中:
				//资产冻结
				assetIdList = assetSequestrationTempService.getSealAssetIdList(id);
				assetService.LockAssetState(assetIdList);
				break;
			case 驳回:
				//资产状态回滚
				assetIdList =assetSequestrationTempService.getSealAssetIdList(id);
				assetService.approveFailureAssetStateRollback(assetIdList);
				break;
			case 已审批:
				//将资产状态落地
				AssetSequestrationModel model = assetSequestrationDao.findById(id).get();
				assetIdList = assetSequestrationTempService.getSealAssetIdList(id);
				assetService.approveSuccessUpdateAssetState(assetIdList, IAssetService.ASSET_STATUS.封存 );
				assetService.doAssetPlaceUpdate(assetIdList, model.getSealPlaceId());
				this.createHistory(model, assetIdList);
				break;
			default:
				break;

		}
		//更新主表状态
		AssetSequestrationModel model = assetSequestrationDao.findById(id).get();
		model.setSealApproveStatus(workStatus);

	}


	private void createHistory(AssetSequestrationModel model, List<String> assetIdList) {
		for (String assetId : assetIdList) {
			AssetHistoryDto historyDto = new AssetHistoryDto();
			historyDto.setHistoryType(IAssetService.HISTORY_TYPE.封存启封记录);
			historyDto.setAssetModelId(assetId);
			historyDto.setSealedUnsealed(IAssetService.SEALED_UNSEALED.封存.toString());
			historyDto.setModifyContent("封存资产");
			historyDto.setCreateUserId(model.getSponsor());
			assetService.createHistory(historyDto);
		}

	}


}

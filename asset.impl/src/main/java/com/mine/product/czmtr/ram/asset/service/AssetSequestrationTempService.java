package com.mine.product.czmtr.ram.asset.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mine.product.czmtr.ram.asset.dao.AssetAssetDao;
import com.mine.product.czmtr.ram.asset.dao.AssetHistoryDao;
import com.mine.product.czmtr.ram.asset.dao.AssetSequestrationDao;
import com.mine.product.czmtr.ram.asset.dao.AssetSequestrationTempDao;
import com.mine.product.czmtr.ram.asset.dto.AssetHistoryDto;
import com.mine.product.czmtr.ram.asset.dto.AssetSequestrationTempDto;
import com.mine.product.czmtr.ram.asset.model.AssetAssetModel;
import com.mine.product.czmtr.ram.asset.model.AssetHistoryModel;
import com.mine.product.czmtr.ram.asset.model.AssetSequestrationModel;
import com.mine.product.czmtr.ram.asset.model.AssetSequestrationTempModel;
import com.mine.product.czmtr.ram.asset.service.IAssetService.*;
import com.vgtech.platform.common.utility.VGUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AssetSequestrationTempService implements IAssetSequestrationTempService {

    @Autowired
    private AssetSequestrationDao assetSequestrationDao;

    @Autowired
    private AssetAssetDao assetDao;

    @Autowired
    private AssetSequestrationTempDao assetSequestrationTempDao;

    @Autowired
    private IAssetService assetService;

    @Autowired
    private AssetHistoryDao historyDao; //历史

    @Override
    public List<String> getSealAssetIdList(String id) {
        List<AssetSequestrationTempModel> dtolist = assetSequestrationTempDao.findByAssetSequestrationModelId(id);
        if (dtolist.size() <= 0) {
            throw new RuntimeException("资产列表不能为空！");
        }
        return dtolist.stream().map(e -> e.getAssetId()).collect(Collectors.toList());
    }

    @Override
    public List<String> getUnSealAssetIdList(String id) {
        return this.getSealAssetIdList(id);
    }

    public void createSealAsset(ArrayList<AssetSequestrationTempDto> dtolist, String id) {
        AssetSequestrationModel model = assetSequestrationDao.findById(id).get();
        if (VGUtility.isEmpty(model))
            throw new RuntimeException("封存信息不能为空！");
        for (AssetSequestrationTempDto temp : dtolist) {
            AssetSequestrationTempModel tempModel = new AssetSequestrationTempModel();
            tempModel.setAssetSequestrationModel(model);
            tempModel.setAssetId(temp.getAssetId());
            tempModel.setCreateTimestamp(new Date());
            tempModel.setLastUpdateTimestamp(new Date());
            assetSequestrationTempDao.save(tempModel);
//            if (!VGUtility.isEmpty(temp.getAssetId())) {
				/*AssetAssetModel assetModel = assetDao.findById(temp.getAssetId()).get();
//				assetModel.setAssetStatus(ASSET_STATUS.封存);
//				assetDao.save(assetModel);*/
//
//                AssetHistoryDto historyDto = new AssetHistoryDto();
//                historyDto.setHistoryType(HISTORY_TYPE.封存启封记录);
//                historyDto.setAssetModelId(temp.getAssetId());
//                historyDto.setSealedUnsealed(SEALED_UNSEALED.封存.toString());
//                historyDto.setModifyContent("封存资产");
//                historyDto.setCreateUserId(((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo().getId());
//                createHisory(historyDto);
//            }
        }
    }

    //创建历史
    public void createHisory(AssetHistoryDto historyDto) {
        assetService.commonCheckHistoryDto(historyDto);
        historyDao.save(convertHistoryDtoToModel(historyDto, null));
    }

    @Override
    public void save(String id, String assetSequestrationTempDtoList) {
        AssetSequestrationModel model = assetSequestrationDao.findById(id).get();
        if (VGUtility.isEmpty(model))
            throw new RuntimeException("封存信息不能为空！");
        ArrayList<AssetSequestrationTempDto> assetSequestrationTempDtos = new ArrayList<AssetSequestrationTempDto>();
        if ((!VGUtility.isEmpty(assetSequestrationTempDtoList)))
            assetSequestrationTempDtos = JSON.parseObject(assetSequestrationTempDtoList, new TypeReference<ArrayList<AssetSequestrationTempDto>>() {
            });
        for (AssetSequestrationTempDto dto : assetSequestrationTempDtos) {
            AssetSequestrationTempModel tempModel = new AssetSequestrationTempModel();
            tempModel.setAssetSequestrationModel(model);
            tempModel.setAssetId(dto.getAssetId());
            tempModel.setCreateTimestamp(new Date());
            //校验该资产是否存在  子表里了
            List<AssetSequestrationTempModel> check = assetSequestrationTempDao.findByAssetId(tempModel.getAssetId(), model.getId());
            if (!check.isEmpty())
                continue;
            assetSequestrationTempDao.save(tempModel);
            /*AssetAssetModel assetModel = assetDao.findById(dto.getAssetId()).get();
            if (!VGUtility.isEmpty(assetModel)) {
				*//*assetModel.setAssetStatus(ASSET_STATUS.封存);
				assetDao.save(assetModel);*//*

                //创建历史
                AssetHistoryDto historyDto = new AssetHistoryDto();
                historyDto.setHistoryType(HISTORY_TYPE.封存启封记录);
                historyDto.setAssetModelId(tempModel.getAssetId());
                historyDto.setSealedUnsealed(SEALED_UNSEALED.封存.toString());
                historyDto.setModifyContent("封存资产");
                historyDto.setCreateUserId(((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo().getId());
                createHisory(historyDto);
            }*/
        }
    }

    @Override
    public void deleteById(String id, String assetSequestrationTempDtoList) {
        AssetSequestrationModel model = assetSequestrationDao.findById(id).get();
        if (VGUtility.isEmpty(model))
            throw new RuntimeException("封存信息不能为空！");
        ArrayList<AssetSequestrationTempDto> assetSequestrationTempDtos = new ArrayList<AssetSequestrationTempDto>();
        if ((!VGUtility.isEmpty(assetSequestrationTempDtoList)))
            assetSequestrationTempDtos = JSON.parseObject(assetSequestrationTempDtoList, new TypeReference<ArrayList<AssetSequestrationTempDto>>() {
            });

        for (AssetSequestrationTempDto dto : assetSequestrationTempDtos) {
            //删除把状态改为停用
            AssetAssetModel assetModel = assetDao.findById(dto.getAssetId()).get();
            assetModel.setAssetStatus(ASSET_STATUS.停用);
            assetDao.save(assetModel);

            assetSequestrationTempDao.deleteById(dto.getId());
        }
    }

    @Override
    public void createAssetUnseal(ArrayList<AssetSequestrationTempDto> dtolist, String id) {
        AssetSequestrationModel model = assetSequestrationDao.findById(id).get();
        if (VGUtility.isEmpty(model))
            throw new RuntimeException("启封信息不能为空！");
        for (AssetSequestrationTempDto temp : dtolist) {
            AssetSequestrationTempModel tempModel = new AssetSequestrationTempModel();
            tempModel.setAssetSequestrationModel(model);
            tempModel.setAssetId(temp.getAssetId());
            tempModel.setCreateTimestamp(new Date());
            tempModel.setLastUpdateTimestamp(new Date());
            assetSequestrationTempDao.save(tempModel);
               /*AssetAssetModel assetModel = assetDao.findById(temp.getAssetId()).get();
         if (!VGUtility.isEmpty(assetModel)) {
				*//*assetModel.setAssetStatus(ASSET_STATUS.闲置);
				assetDao.save(assetModel);*//*

                //创建历史
                AssetHistoryDto historyDto = new AssetHistoryDto();
                historyDto.setHistoryType(HISTORY_TYPE.封存启封记录);
                historyDto.setAssetModelId(tempModel.getAssetId());
                historyDto.setSealedUnsealed(SEALED_UNSEALED.启封.toString());
                historyDto.setModifyContent("启封的资产");
                historyDto.setCreateUserId(((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo().getId());
                createHisory(historyDto);
            }*/
        }
    }


    //历史dto转成model
    private AssetHistoryModel convertHistoryDtoToModel(AssetHistoryDto dto, AssetHistoryModel model) {
        if (VGUtility.isEmpty(model))
            model = new AssetHistoryModel();

        model.setId(dto.getId());
        model.setCreateUserId(dto.getCreateUserId());
        model.setAssetModelId(dto.getAssetModelId());
        model.setHistoryType(dto.getHistoryType());
        model.setModifyContent(dto.getModifyContent());
        model.setChangeType(CHANGE_TYPE.values()[VGUtility.toInteger(dto.getChangeType())]);
        model.setChangeContent(dto.getChangeContent());
        model.setSealedUnsealed(SEALED_UNSEALED.valueOf(dto.getSealedUnsealed()));
        model.setCheckOutCom(dto.getCheckOutCom());
        model.setCheckInCom(dto.getCheckInCom());
        model.setCheckOutDept(dto.getCheckOutDept());
        model.setCheckInDept(dto.getCheckInDept());
        model.setTakeStockResult(dto.getTakeStockResult());
        model.setSourctType(SOURCE_TYPE.values()[VGUtility.toInteger(dto.getSourctType())]);
        model.setAssetSource(dto.getAssetSource());
        model.setReduceType(REDUCE_TYPE.values()[VGUtility.toInteger(dto.getReduceType())]);

        return model;
    }

}

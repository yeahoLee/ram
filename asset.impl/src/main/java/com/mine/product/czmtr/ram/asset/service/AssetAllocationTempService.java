package com.mine.product.czmtr.ram.asset.service;

import com.mine.base.dict.dto.DictDto;
import com.mine.base.dict.service.IDictService;
import com.mine.base.user.dto.DeptInfoDto;
import com.mine.base.user.dto.UserInfoDto;
import com.mine.base.user.service.IUserService;
import com.mine.product.czmtr.ram.asset.dao.AssetAllocationDao;
import com.mine.product.czmtr.ram.asset.dao.AssetAllocationTempDao;
import com.mine.product.czmtr.ram.asset.dto.AssetAllocationDto;
import com.mine.product.czmtr.ram.asset.dto.AssetAllocationTempDto;
import com.mine.product.czmtr.ram.asset.dto.AssetAssetDto;
import com.mine.product.czmtr.ram.asset.model.AssetAllocationModel;
import com.mine.product.czmtr.ram.asset.model.AssetAllocationTempModel;
import com.mine.product.czmtr.ram.asset.service.IAssetService.ASSET_STATUS;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.VGUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AssetAllocationTempService implements IAssetAllocationTempService {

    @Autowired
    private AssetAllocationDao assetAllocationDao;
    @Autowired
    private AssetAllocationTempDao assetAllocationTempDao;
    @Autowired
    private IAssetAllocationService assetAllocationService;
    @Autowired
    private IAssetService assetService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IDictService dictService;
    @Autowired
    private IBaseService baseService;

    @Override
    public List<String> getAssetIdList(String id) {
        List<AssetAllocationTempDto> dtolist = this.getAssetAllocationTempDtoList(id);
        if (dtolist.size() <= 0) {
            throw new RuntimeException("资产列表不能为空！");
        }
        return dtolist.stream().map(e -> e.getAssetId()).collect(Collectors.toList());
    }


    @Override
    public void createAssetAllocationTempForList(List<AssetAllocationTempDto> AssetAllocationTempDtoList,
                                                 String assetAllocationId) {
        AssetAllocationModel model = assetAllocationDao.findById(assetAllocationId).get();
        if (VGUtility.isEmpty(model))
            throw new RuntimeException("调拨信息不能为空，不能为空！");
        for (AssetAllocationTempDto temp : AssetAllocationTempDtoList) {
            AssetAllocationTempModel tempModel = new AssetAllocationTempModel();
            tempModel.setAssetAllocationModel(model);
            tempModel.setAssetId(temp.getAssetId());

            AssetAssetDto asset = assetService.getAssetByAssetId(temp.getAssetId());
            Boolean flag = assetService.getAssetStatus(
                    ASSET_STATUS.values()[VGUtility.toInteger(asset.getAssetStatusStr())], ASSET_STATUS.调拨);
            if (!flag) {
                throw new RuntimeException("当前资产不可以调拨 ");
            }
//            asset.setBeforeChangeAssetStatusStr(String.valueOf(ASSET_STATUS.闲置.ordinal()));
//            assetService.updateAssetBeforeAssetStatus(asset);

            assetAllocationTempDao.save(tempModel);
        }
    }

    /***
     * 根据资产ID,插入资产调拨子表
     */
    @Override
    public void createAssetAllocationTempByassetIdListStr(String assetIdListStr, AssetAllocationDto dto) {
        List<String> assetIdList = new ArrayList<String>();
        if (!VGUtility.isEmpty(assetIdListStr))
            Arrays.stream(assetIdListStr.split(",")).forEach(arr -> assetIdList.add(arr));

        AssetAllocationModel assetAllocationModel = assetAllocationDao.findById(dto.getId()).get();
        if (VGUtility.isEmpty(assetAllocationModel))
            throw new RuntimeException("该调拨单数据已被删除！");

        for (String assetId : assetIdList) {
            AssetAssetDto asset = assetService.getAssetByAssetId(assetId);
            if (VGUtility.isEmpty(asset))
                throw new RuntimeException("该资产数据已被删除！");

            asset.setBeforeChangeAssetStatusStr(asset.getAssetStatusStr());
            assetService.updateAssetBeforeAssetStatus(asset);

            AssetAllocationTempModel tempModel = new AssetAllocationTempModel();
            tempModel.setAssetAllocationModel(assetAllocationModel);
            tempModel.setAssetId(assetId);

            assetAllocationTempDao.save(tempModel);
        }
    }

    ;

    /******* 资产领用子表查询 start *****/
    @Override
    public List<AssetAllocationTempDto> getAssetAllocationTempDtoList(String assetAllocationId) {
        List<AssetAllocationTempModel> modelList = assetAllocationTempDao.findAllByAssetAllocationModel_Id(assetAllocationId);
        List<AssetAllocationTempDto> dto = new ArrayList<AssetAllocationTempDto>();
        for (AssetAllocationTempModel model : modelList) {
            dto.add(getByAssetAllocationTempModel(model));
        }
        return dto;
    }

    /**
     * 删除 调拨单子表
     */
    @Override
    public void deleteAssetAllocationTempByAssetAllocationID(List<String> assetAllocationTempIdListStr,
                                                             String AssetAllocationID) {
        for (String assetAllocationTempId : assetAllocationTempIdListStr) {
            AssetAllocationTempModel model = assetAllocationTempDao.findById(assetAllocationTempId).get();
            AssetAssetDto asset = assetService.getAssetByAssetId(model.getAssetId());
            if (VGUtility.isEmpty(asset)) {
                throw new RuntimeException("该资产数据已被删除！");
            }
            assetAllocationTempDao.deleteById(assetAllocationTempId);
        }
    }

    ;

    // ********************Convert Start********************

    /**
     * 数据转换 convert model to dto convert dto to model
     */
    public AssetAllocationTempDto getByAssetAllocationTempModel(AssetAllocationTempModel model) {
        AssetAllocationTempDto assetDto = convertModelToDto(model);
        AssetAssetDto asset = assetService.getAssetByAssetId(assetDto.getAssetId());
        if (VGUtility.isEmpty(asset))
            throw new RuntimeException("该资产数据已被删除！");
        else {
            assetDto = convertDtoToDto(asset, assetDto);
        }
        return assetDto;
    }

    private AssetAllocationTempDto convertModelToDto(AssetAllocationTempModel model) {
        AssetAllocationTempDto dto = new AssetAllocationTempDto();
        dto.setId(model.getId());
        if (!VGUtility.isEmpty(model.getAssetAllocationModel()))
            dto.setAssetAllocationId(model.getAssetAllocationModel().getId());
        dto.setAssetId(model.getAssetId());
        dto.setCreateTimestamp(model.getCreateTimestamp());
        return dto;
    }

    AssetAllocationTempDto convertDtoToDto(AssetAssetDto dto, AssetAllocationTempDto temp) {
        temp.setAssetCode(dto.getAssetCode());
        temp.setAssetTypeStr(dto.getAssetTypeStr());
        temp.setSpecAndModels(dto.getSpecAndModels());
        temp.setSeriesNum(dto.getSeriesNum());
        temp.setUnitOfMeasStr(dto.getUnitOfMeasStr());
        if (!VGUtility.isEmpty(dto.getUnitOfMeasId())) {
            DictDto dictCommonDto = dictService.getCommonCode(dto.getUnitOfMeasId());
            if (!VGUtility.isEmpty(dictCommonDto))
                temp.setUnitOfMeasStr(dictCommonDto.getChsName());
        }
        temp.setAssetBrand(dto.getAssetBrand());
        double purcPrice = Double.parseDouble(dto.getPurcPrice());
        String purcPriceStr = new String();
        if (purcPrice > 0)
            purcPriceStr = VGUtility.toDoubleStr(purcPrice, "0.##");
        else
            purcPriceStr = "";
        temp.setPurcPrice(purcPriceStr);

        double equiOrigValue = Double.parseDouble(dto.getEquiOrigValue());
        String equiOrigValueStr = new String();
        if (equiOrigValue > 0)
            equiOrigValueStr = VGUtility.toDoubleStr(equiOrigValue, "0.##");
        else
            equiOrigValueStr = "";
        temp.setEquiOrigValue(equiOrigValueStr);

        // 延伸信息
        dto.setCompanyId(dto.getCompanyId());
        if (!VGUtility.isEmpty(dto.getCompanyId())) {
            DeptInfoDto deptDto = (DeptInfoDto) baseService.getDeptInfo(dto.getCompanyId());
            if (!VGUtility.isEmpty(deptDto))
                dto.setCompanyStr(deptDto.getDeptName());
        }
        temp.setBelongLine(dto.getBelongLine());
        if (!VGUtility.isEmpty(dto.getBelongLine())) {
            DictDto dictCommonDto = dictService.getCommonCode(dto.getBelongLine());
            if (!VGUtility.isEmpty(dictCommonDto))
                temp.setBelongLineStr(dictCommonDto.getChsName());
        }

        temp.setCompanyId(dto.getCompanyId());
        if (!VGUtility.isEmpty(dto.getCompanyId())) {
            DeptInfoDto deptDto = (DeptInfoDto) baseService.getDeptInfo(dto.getCompanyId());
            if (!VGUtility.isEmpty(deptDto))
                temp.setCompanyStr(deptDto.getDeptName());
        }
        // 根据物资编码查询并将查询结果组合为资产类别和资产名称，然后set到dto中
        String assetTypeName = baseService.getAssetTypeByMaterialCode(dto.getMaterialCode());
        if (!VGUtility.isEmpty(assetTypeName))
            temp.setCombinationAssetType(assetTypeName);

        String assetName = baseService.getAssetNameByMaterialCode(dto.getMaterialCode());
        if (!VGUtility.isEmpty(assetName))
            temp.setCombinationAssetName(assetName);

        temp.setTechPara(dto.getTechPara());
        temp.setRemark(dto.getRemark());
        temp.setBuyDate(dto.getBuyDate());
        if (!VGUtility.isEmpty(dto.getManageDeptId())) {
            DeptInfoDto deptDto = (DeptInfoDto) baseService.getDeptInfo(dto.getManageDeptId());
            if (!VGUtility.isEmpty(deptDto))
                temp.setManageDeptStr(deptDto.getDeptName());
        }
        if (!VGUtility.isEmpty(dto.getManagerId())) {
            UserInfoDto userDto = userService.getUserInfo(dto.getManagerId());
            if (!VGUtility.isEmpty(userDto))
                temp.setManagerStr(userDto.getChsName());
        }
        temp.setAssetSource(dto.getAssetSource());
        temp.setContractNum(dto.getContractNum());
        temp.setTendersNum(dto.getTendersNum());
        temp.setMainPeriod(dto.getMainPeriod());
        temp.setSourceUser(dto.getSourceUser());
        temp.setSourceContactInfo(dto.getSourceContactInfo());
        temp.setProdTime(dto.getProdTime());
        temp.setAssetId(dto.getId());
        temp.setAssetStatus(ASSET_STATUS.values()[VGUtility.toInteger(dto.getAssetStatusStr())].toString());
        AssetAllocationDto allocation = null;
        if (!VGUtility.isEmpty(temp.getAssetAllocationId()))
            allocation = assetAllocationService.getAssetAllocationDtoById(temp.getAssetAllocationId());
        if (!VGUtility.isEmpty(temp.getAssetAllocationCode()))
            temp.setAssetAllocationCode(temp.getAssetAllocationCode());
        else if (!VGUtility.isEmpty(allocation))
            temp.setAssetAllocationCode(allocation.getAssetAllocationCode());
        temp.setProduceStr(dto.getProduceStr());
        return temp;
    }
}

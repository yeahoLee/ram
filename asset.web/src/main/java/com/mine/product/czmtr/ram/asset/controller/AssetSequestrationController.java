package com.mine.product.czmtr.ram.asset.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.mine.base.user.dto.UserInfoDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.AssetSequestrationDto;
import com.mine.product.czmtr.ram.asset.service.IAssetSequestrationService;
import com.mine.product.czmtr.ram.asset.service.IAssetSequestrationTempService;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.MineSecureUtility;
import com.vgtech.platform.common.utility.SpringSecureUserInfo;
import com.vgtech.platform.common.utility.VGUtility;

@Controller
@RequestMapping(value = "/assetSequestration/")
@SessionAttributes(value = {"LoginUserInfo"})
public class AssetSequestrationController {

    private static final Logger logger = LoggerFactory.getLogger(AssetSequestrationController.class);

    @Autowired
    private IAssetSequestrationService assetSequestrationService;

    @Autowired
    private IAssetSequestrationTempService assetSequestrationTempService;

    @Autowired
    private IBaseService baseService;

    @PostMapping(value = "assetsequestration_datagrid")
    @ResponseBody
    public Map<String, Object> getAssetSequestrationByQuerysForDataGrid(AssetSequestrationDto assetSequestrationDto,
                                                                        @RequestParam(defaultValue = "1") String page,
                                                                        @RequestParam(defaultValue = "20") String rows) {
        logger.info("Controller: Get AssetSequestration ForDataGrid");
        return assetSequestrationService.getAssetSequestrationForDataGrid(new ISearchExpression() {

            @Override
            public Object change(Object... arg0) {
                CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                Root root = (Root) arg0[0];
                Predicate predicate;

                predicate = getPredicateByAssetSequestrationDto(builder, root, assetSequestrationDto);

                return predicate;
            }
        }, assetSequestrationDto, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)));
    }

    /**
     * 资产封存申请单创建
     *
     * @param assetSequestrationDto
     * @param assetSequestrationTempDtoList
     * @return
     */
    @PostMapping(value = "assetseal_create")
    @ResponseBody
    public ModelMap createAssetSeal(AssetSequestrationDto assetSequestrationDto, String assetSequestrationTempDtoList) {
        logger.info("Controller: Create AssetSeal" + assetSequestrationDto.toString());
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        AssetSequestrationDto dto = assetSequestrationService.createAssetSeal(assetSequestrationTempDtoList, assetSequestrationDto, userInfoDto);
        ModelMap map = new ModelMap();
        map.put("dto", dto);
        map.put("success", "true");
        return map;
    }

    /**
     * 创建启封资产单
     *
     * @param assetSequestrationDto
     * @param assetSequestrationTempDtoList
     * @return
     */
    @PostMapping(value = "assetseal_unseal")
    @ResponseBody
    public ModelMap createAssetUnseal(AssetSequestrationDto assetSequestrationDto, String assetSequestrationTempDtoList) {
        logger.info("Controller: Create AssetUnseal" + assetSequestrationDto.toString());
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        AssetSequestrationDto dto = assetSequestrationService.createAssetUnseal(assetSequestrationTempDtoList, assetSequestrationDto, userInfoDto);
        ModelMap map = new ModelMap();
        map.put("dto", dto);
        map.put("success", "true");
        return map;
    }

    @PostMapping(value = "assetseal_delete")
    @ResponseBody
    public String deleteAssetSeal(String id) {
        logger.info("Controller: Delete AssetSeal" + id);
        if (!baseService.getPermCheck("zcfcd_delete"))
            throw new RuntimeException("该用户没有权限删除申请单");
        assetSequestrationService.deleteAssetSeal(id);
        return "{\"success\":true}";
    }

    /**
     * 发起审批
     *
     * @param id
     * @return
     */
    @PostMapping(value = "assetseal_approval")
    @ResponseBody
    public String approvalAssetSeal(String id) {
        logger.info("Controller: approve AssetSeal" + id);
        assetSequestrationService.approvalAssetSeal(id);
        return "{\"success\":true}";
    }

    /**
     * 更新封存单 子表资产
     *
     * @param assetSequestrationDto
     * @param assetSequestrationTempDtoList
     * @return
     */
    @PostMapping(value = "assetseal_update_asset")
    @ResponseBody
    public String updateAssetSealSonAsset(AssetSequestrationDto assetSequestrationDto, String assetSequestrationTempDtoList) {
        logger.info("Controller: update AssetSeal tempmodel zibiao" + assetSequestrationDto.getId());
        assetSequestrationTempService.save(assetSequestrationDto.getId(), assetSequestrationTempDtoList);
        return "{\"success\":true}";
    }

    /**
     * 更新封存单
     *
     * @param assetSequestrationDto
     * @param assetSequestrationTempDtoList
     * @return
     */
    @PostMapping(value = "assetseal_update")
    @ResponseBody
    public String updateAssetSealAsset(AssetSequestrationDto assetSequestrationDto, String assetSequestrationTempDtoList) {
        logger.info("Controller: update AssetSeal model" + assetSequestrationDto.getId());
        assetSequestrationService.updateSeal(assetSequestrationDto);
        return "{\"success\":true}";
    }

    /**
     * 删除封存单 资产
     *
     * @param assetSequestrationDto
     * @param assetSequestrationTempDtoList
     * @return
     */
    @PostMapping(value = "assetseal_delete_asset")
    @ResponseBody
    public String deleteAssetSealSonAsset(AssetSequestrationDto assetSequestrationDto, String assetSequestrationTempDtoList) {
        logger.info("Controller: update AssetSeal tempmodel zibiao" + assetSequestrationDto.getId());
        assetSequestrationTempService.deleteById(assetSequestrationDto.getId(), assetSequestrationTempDtoList);
        return "{\"success\":true}";
    }

    /**
     * 通过封存id获取 封存资产list
     *
     * @param id
     * @return
     */
    @PostMapping(value = "assetseal_update_datagrid")
    @ResponseBody
    public Map<String, Object> updateAssetSeal(AssetSequestrationDto assetSequestrationDto,
                                               @RequestParam(defaultValue = "1") String page,
                                               @RequestParam(defaultValue = "20") String rows) {
        logger.info("Controller: Get AssetSequestration ForUpdate DataGrid");
        return assetSequestrationService.getAssetSealForUpdateDataGrid(assetSequestrationDto, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)));
    }

    /**
     * 通过id获取封存单内容
     *
     * @param id
     * @return
     */
    @PostMapping(value = "assetseal_queryById")
    @ResponseBody
    public AssetSequestrationDto getAssetSealById(String id) {
        logger.info("Controller: Get AssetSequestration byId");
        return (AssetSequestrationDto) assetSequestrationService.getAssetSealById(id);
    }

    @PostMapping(value = "assetseal_view_seal")
    @ResponseBody
    public Map<String, Object> viewAssetSeal(String id) {
        logger.info("Controller: Get AssetSequestration byId");
        return assetSequestrationService.getSealViewById(id);
    }

    private Predicate getPredicateByAssetSequestrationDto(CriteriaBuilder builder, Root root,
                                                          AssetSequestrationDto assetSequestrationDto) {
        List<Predicate> andList = new ArrayList<>();
        Predicate finalPred = null;
        if (!VGUtility.isEmpty(assetSequestrationDto.getSequestrateNum()))
            andList.add(builder.equal(root.get("sequestrateNum"), assetSequestrationDto.getSequestrateNum()));
        if (!VGUtility.isEmpty(assetSequestrationDto.getSealApproveStatus()))
            andList.add(builder.equal(root.get("sealapprovestatus"), assetSequestrationDto.getSealApproveStatus()));
        if (!VGUtility.isEmpty(assetSequestrationDto.getSealPlaceId()))
            andList.add(builder.equal(root.get("sealplaceid"), assetSequestrationDto.getSealPlaceId()));
        if (!VGUtility.isEmpty(assetSequestrationDto.getCreateTimestampStart()))
            andList.add(builder.greaterThanOrEqualTo(root.get("createTimestamp"), VGUtility.toDateObj(assetSequestrationDto.getCreateTimestampStart(), "yyyy/M/d")));
        if (!VGUtility.isEmpty(assetSequestrationDto.getCreateTimestampEnd()))
            andList.add(builder.lessThanOrEqualTo(root.get("createTimestamp"), VGUtility.toDateObj(assetSequestrationDto.getCreateTimestampEnd(), "yyyy/M/d")));
        if (andList.size() > 0)
            finalPred = builder.and(andList.toArray(new Predicate[andList.size()]));
        return finalPred;
    }

}
package com.mine.product.czmtr.ram.asset.controller;

import com.mine.product.czmtr.ram.flowable.dto.WorksDto;
import com.mine.product.czmtr.ram.flowable.service.IFlowableService;
import com.vgtech.platform.common.utility.VGUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.mine.base.user.dto.UserInfoDto;
import com.mine.product.czmtr.ram.asset.dto.AssetSequestrationDto;
import com.mine.product.czmtr.ram.asset.service.IAssetSequestrationService;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.MineSecureUtility;
import com.vgtech.platform.common.utility.SpringSecureUserInfo;

@Controller
@SessionAttributes(value = {"LoginUserInfo"})
public class AssetSequestrationPageController {

    @Autowired
    private IAssetSequestrationService assetSequestrationService;

    @Autowired
    private IBaseService baseService;

    @Autowired
    private IFlowableService flowableService;

    private static final Logger logger = LoggerFactory.getLogger(AssetSequestrationPageController.class);

    /**
     * 进入资产封存/启封管理
     *
     * @param dto
     * @param map
     * @return
     */
    @GetMapping(value = "/assetsequestration_query")
    public String assetSequestrationPage(AssetSequestrationDto dto, ModelMap map) {
        logger.info("Enter assetSequestration query Page");
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.put("userInfoDto", userInfoDto);
        return "asset/assetSequestration";
    }

    /**
     * 进入资产封存界面
     *
     * @param dto
     * @param map
     * @return
     */
    @GetMapping(value = "/assetsequestration_seal")
    public String assetSequestrationSealPage(AssetSequestrationDto dto, ModelMap map) {
        logger.info("Enter assetSequestrationSeal  Page");
        if (!baseService.getPermCheck("zcfcd_create"))
            throw new RuntimeException("该用户没有权限进行封存单创建");
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.put("userInfoDto", userInfoDto);
        return "asset/assetSequestrationSeal";
    }

    /**
     * 进入资产封存更新界面
     *
     * @param dto
     * @param map
     * @return
     */
    @GetMapping(value = "/assetsequestration_update")
    public String assetSealUpdatePage(AssetSequestrationDto dto, ModelMap map) {
        logger.info("Enter assetSequestrationSeal update Page");
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.put("userInfoDto", userInfoDto);
        map.put("assetSeal", dto);
        return "asset/assetSealUpdate";
    }

    /**
     * 进入资产启封更新界面
     *
     * @param dto
     * @param map
     * @return
     */
    @GetMapping(value = "/assetsequestration_updateUnseal")
    public String assetUnsealUpdatePage(AssetSequestrationDto dto, ModelMap map) {
        logger.info("Enter assetSequestrationSeal update Page");
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        AssetSequestrationDto assetSequestrationDto= (AssetSequestrationDto) assetSequestrationService.getAssetSealById(dto.getId());
        map.put("userInfoDto", userInfoDto);
        map.put("assetSeal", assetSequestrationDto);
        return "asset/assetUnsealUpdate";
    }

    @GetMapping(value = "/assetsequestration_unseal")
    public String assetUnsealPage(AssetSequestrationDto dto, ModelMap map) {
        logger.info("Enter asset createUnseal Page");
        if (!baseService.getPermCheck("zcqfd_create"))
            throw new RuntimeException("该用户没有权限进行启封单创建");
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.put("userInfoDto", userInfoDto);
        map.put("assetSeal", dto);
        return "asset/assetSealUnseal";
    }

    @GetMapping(value = "/assetsequestration_view_seal")
    public String goToViewSealPage(AssetSequestrationDto dto, ModelMap map) {
        logger.info("Enter asset createUnseal Page");
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.put("userInfoDto", userInfoDto);
        dto.setSponsorStr(userInfoDto.getChsName());
        AssetSequestrationDto assetSequestrationDto = (AssetSequestrationDto) assetSequestrationService.getAssetSealById(dto.getId());
        map.put("assetSeal", assetSequestrationDto);

        WorksDto worksDto = flowableService.getWorksModelByBusinessKey(dto.getId());
        if (!VGUtility.isEmpty(worksDto)) {
            map.addAttribute("processInstanceId", worksDto.getProcessInstanceId());
        }

        return "asset/assetSealView";
    }

    @GetMapping(value = "/assetsequestration_view_unseal")
    public String goToViewUnsealPage(AssetSequestrationDto dto, ModelMap map) {
        logger.info("Enter asset createUnseal Page");
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.put("userInfoDto", userInfoDto);
        dto.setSponsorStr(userInfoDto.getChsName());
        AssetSequestrationDto assetSequestrationDto = (AssetSequestrationDto) assetSequestrationService.getAssetSealById(dto.getId());
        map.put("assetSeal", assetSequestrationDto);

        WorksDto worksDto = flowableService.getWorksModelByBusinessKey(dto.getId());
        if (!VGUtility.isEmpty(worksDto)) {
            map.addAttribute("processInstanceId", worksDto.getProcessInstanceId());
        }

        return "asset/assetUnseal";
    }
}

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.mine.base.user.dto.UserInfoDto;
import com.mine.product.czmtr.ram.asset.dto.AssetAllocationDto;
import com.mine.product.czmtr.ram.asset.service.IAssetAllocationService;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.MineSecureUtility;
import com.vgtech.platform.common.utility.SpringSecureUserInfo;

@Controller
@SessionAttributes(value = {"LoginUserInfo"})
public class AssetAllocationPageController {

    private static final Logger logger = LoggerFactory.getLogger(AssetAllocationPageController.class);
    @Autowired
    private IAssetAllocationService assetAllocationService;
    @Autowired
    private IBaseService baseService;
    @Autowired
    private IFlowableService flowableService;
    @GetMapping(value = "/assetallocation_create")
    public String assetAllocationCreatePage(AssetAllocationDto dto, ModelMap map) {
        logger.info("Enter AssetAllocation Create Page");
        if (!baseService.getPermCheck("zcdbxzd_create")) {
            throw new RuntimeException("该用户没有权限进行资产调拨单创建");
        }
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.put("userInfoDto", userInfoDto);
        return "asset/assetAllocationCreate";
    }

    @GetMapping(value = "/assetallocation_query")
    public String assetAllocationListPage(ModelMap map) {
        logger.info("Enter AssetAllocation List Page");
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.put("userInfoDto", userInfoDto);
        return "asset/assetAllocationList";
    }

    @GetMapping(value = "/assetallocation_update")
    public String assetAllocationUpdatePage(@RequestParam String id, ModelMap map) {
        logger.info("Enter AssetAllocation Update Page");
        if (!baseService.getPermCheck("zcdbxzd_update")) {
            throw new RuntimeException("该用户没有权限进行资产调拨单更新");
        }
        map.put("AssetAllocationDto", assetAllocationService.getAssetAllocationDtoById(id));
        return "asset/assetAllocationUpdate";
    }

    @GetMapping(value = "/assetallocation_view")
    public String assetAllocationViewPage(@RequestParam String code, ModelMap map) {
        logger.info("Enter AssetAllocation View Page");
        map.put("AssetAllocationDto", assetAllocationService.getAssetAllocationDtoByCode(code));

        WorksDto worksDto = flowableService.getWorksModelByApproveCode(code);
        if (!VGUtility.isEmpty(worksDto)) {
            map.addAttribute("processInstanceId", worksDto.getProcessInstanceId());
        }

        return "asset/assetAllocationView";
    }
}

package com.mine.product.czmtr.ram.asset.controller;

import com.mine.product.czmtr.ram.asset.dto.AssetAssetDto;
import com.mine.product.czmtr.ram.asset.dto.AssetStanBookSubmitDto;
import com.mine.product.czmtr.ram.asset.service.IAssetService;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes(value = {"LoginUserInfo"})
public class AssetPageController {
    private static final Logger logger = LoggerFactory.getLogger(AssetPageController.class);

    @Autowired
    private IAssetService assetService;
    @Autowired
    private AssetController assetController;

    @Autowired
    private IBaseService baseService;

    @GetMapping(value = "/asset_index")
    public String assetIndexPage() {
        logger.info("Enter Asset Index Page");
        return "asset/assetAssetIndex";
    }

    @GetMapping(value = "/asset_create")
    public String assetCreatePage() {
        logger.info("Enter Asset Create Page");
        return "asset/assetAssetCreate";
    }

    @GetMapping(value = "/asset_update")
    public String assetUpdatePage(@RequestParam String assetId, ModelMap modelMap) {
        logger.info("Enter Asset Update Page");
        if (!baseService.getPermCheck("zcfcd_update"))
            throw new RuntimeException("该用户没有权限进行封存单创建");
        modelMap.addAttribute("assetDto", assetService.getAssetByAssetId(assetId));
        return "asset/assetAssetUpdate";
    }

    @PostMapping(value = "/asset_update_batch")
    public String assetUpdateBatchPage(AssetStanBookSubmitDto assetStanBookSubmit, ModelMap map) {
        logger.info("Enter Asset Update Batch Page");
//        if (!baseService.getPermCheck("update_batch"))
//            throw new RuntimeException("该用户没有权限进行信息批量修改");
        map.put("assetStanBookSubmit", assetStanBookSubmit);
        return "asset/assetAssetUpdateBatch";
    }

    @GetMapping(value = "/asset_stan_book")
    public String assetStanBookPage(AssetStanBookSubmitDto assetStanBookSubmit, ModelMap modelMap, String dynaViewId) {
        logger.info("Enter Asset Standing Book Page By {}", dynaViewId);
        modelMap.addAttribute("assetStanBookSubmit", assetStanBookSubmit);
        modelMap.addAttribute("dynamicViewDto", assetService.getDynamicViewById(dynaViewId));
        modelMap.addAttribute("dynaViewId", dynaViewId);
        boolean zctzzcfast_do = baseService.getPermCheck("zctzzcfast_do");
        modelMap.addAttribute("zctzzcfast_do", zctzzcfast_do);
        boolean zctzxzd_create = baseService.getPermCheck("zctzxzd_create");
        modelMap.addAttribute("zctzxzd_create", zctzxzd_create);
        return "asset/assetStanBook";
    }

    @GetMapping(value = "/asset_stan_book_material")
    public String assetStanBookMaterialPage(AssetStanBookSubmitDto assetStanBookSubmit, ModelMap modelMap) {
        logger.info("Enter Asset Standing Book Material Page");
        modelMap.addAttribute("assetStanBookSubmit", assetStanBookSubmit);
        boolean zctzwzfast_do = baseService.getPermCheck("zctzwzfast_do");
        modelMap.addAttribute("zctzwzfast_do", zctzwzfast_do);
        return "asset/assetStanBookMaterial";
    }

    @GetMapping(value = "/asset_dynamic_view")
    public String assetDynamicViewPage() {
        logger.info("Enter Asset Dynamic View Page");
        return "asset/assetDynamicView";
    }

    @GetMapping(value = "/asset_view")
    public String assetViewPage(@RequestParam String assetId, ModelMap modelMap) {
        logger.info("Enter Asset View Page");
        modelMap.addAttribute("assetDto", assetService.getAssetByAssetId(assetId));
        return "asset/assetAssetView";
    }

    @PostMapping(value = "/asset_view_detail")
    public String assetViewDetailPage(@RequestParam String materialCode, String backToLastPageUrl, ModelMap modelMap) {
        logger.info("Enter Asset View Detail Page");
        modelMap.addAttribute("materialCode", materialCode);
        modelMap.put("backToLastPageUrl", backToLastPageUrl);
        return "asset/assetAssetViewDetail";
    }

    /**
     * 通过AssetAssetDto接收多条件查询的条件，并返回结果
     *
     * @param backToLastPageUrl
     * @param modelMap
     * @param assetAssetDto
     * @return
     */
    @PostMapping(value = "/asset_view_detailw")
    public String assetViewDetailPageByAssetAssetDto(
            String backToLastPageUrl, ModelMap modelMap, AssetAssetDto assetAssetDto) {
        logger.info("Enter Asset View Detail Page");
        modelMap.addAttribute("materialCode", assetAssetDto.getMaterialCode());
        modelMap.put("backToLastPageUrl", backToLastPageUrl);
        modelMap.put("assetAssetDto", assetAssetDto);
        return "asset/assetAssetViewDetail";
    }

    @GetMapping(value = "/asset_save_place")
    public String assetSavePlacePage() {
        logger.info("Enter Save Place Page");
        return "asset/assetSavePlace";
    }

    @GetMapping(value = "/asset_stan_book_search")
    public String assetStanBookSearchPage() {
        logger.info("Enter Stan Book Search Page");
        return "asset/assetStanBookSearch";
    }

    @GetMapping(value = "/asset_type_place")
    public String assetTypePage() {
        logger.info("Enter AssetType Page");
        return "asset/assetType";
    }
}
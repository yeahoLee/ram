package com.mine.product.czmtr.ram.asset.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/***
 * 资产报表管理
 * @author yangjie
 *
 */
@Controller
@SessionAttributes(value = {"LoginUserInfo"})
public class AssetReportPageController {
    private static final Logger logger = LoggerFactory.getLogger(AssetReportPageController.class);

    /********************* 资产变更历史  start*******************************/
    @GetMapping(value = "/asset_change")
    public String assetChangeHistroryPage() {
        logger.info("Enter AssetChangeHistoryList Page");
        return "asset/assetChangeHistoryList";
    }

    /********************* 资产变更历史  end*******************************/


    /********************* 资产移动历史  start*******************************/
    @GetMapping(value = "/asset_move")
    public String assetMoveHistroryPage() {
        logger.info("Enter AssetMoveHistoryList Page");
        return "asset/assetMoveHistoryList";
    }
    /********************* 资产移动历史  end*******************************/


    /********************* 资产丢失  start*******************************/
    @GetMapping(value = "/asset_lose")
    public String assetLoseHistroryPage() {
        logger.info("Enter AssetLoseHistoryList Page");
        return "asset/assetLoseHistoryList";
    }
    /********************* 资产丢失  end*******************************/

    /********************* 报废统计  start*******************************/
    @GetMapping(value = "/asset_scrap")
    public String assetScrapHistroryPage() {
        logger.info("Enter AssetScrapHistoryList Page");
        return "asset/assetScrapHistoryList";
    }
    /********************* 报废统计  end*******************************/

    /********************* 资产可用性统计  start*******************************/
    @GetMapping(value = "/asset_usability")
    public String assetUsabilityHistroryPage() {
        logger.info("Enter AssetUsabilityHistoryList Page");
        return "asset/assetUsabilityHistoryList";
    }
    /********************* 资产可用性统计  end*******************************/
}

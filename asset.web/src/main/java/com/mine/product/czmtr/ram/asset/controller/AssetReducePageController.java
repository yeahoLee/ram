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
import com.mine.product.czmtr.ram.asset.service.IAssetReduceService;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.MineSecureUtility;
import com.vgtech.platform.common.utility.SpringSecureUserInfo;

/***
 * 资产减损管理-页面控制
 * @author guoli.sun
 *
 */
@Controller
@SessionAttributes(value = {"LoginUserInfo"})
public class AssetReducePageController {
    private static final Logger logger = LoggerFactory.getLogger(AssetReducePageController.class);
    @Autowired
    private IAssetReduceService assetReduceService;
    @Autowired
    private IBaseService baseService;
    @Autowired
    private IFlowableService flowableService;

    /***
     * 新增
     * @return
     */
    @GetMapping(value = "/asset_reduce_create")
    public String assetReduceCreatePage(ModelMap map) {
        logger.info("Enter Asset Reduce Create Page");
        if (!baseService.getPermCheck("zcjsxzd_create")) {
            throw new RuntimeException("该用户没有权限进行资产减损单创建");
        }
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.put("loginDeptDto", userInfoDto.getPropertyMap());
        map.put("userInfoDto", userInfoDto);
        return "asset/assetReduceCreate";
    }

    /***
     * 列表信息页
     * @return
     */
    @GetMapping(value = "/asset_reduce_list")
    public String assetReduceListPage(ModelMap map) {
        logger.info("Enter Asset Reduce List Page");
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.put("loginDeptDto", userInfoDto.getPropertyMap());
        map.put("userInfoDto", userInfoDto);
        return "asset/assetReduceList";
    }

    /***
     * 编辑更新
     * @param id
     * @param map
     * @return
     */
    @GetMapping(value = "asset_reduce_update")
    public String updateReduceUpdatePage(String id, ModelMap map) {
        logger.info("Update Asset Reduce Update Page");
        if (!baseService.getPermCheck("zcjsxzd_update")) {
            throw new RuntimeException("该用户没有权限进行资产减损单更新");
        }
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.put("userInfoDto", userInfoDto);
        map.addAttribute("AssetReduceDto", assetReduceService.getReduceReceiptById(id));
        return "asset/assetReduceUpdate";
    }

    /***
     * 查看
     * @param id
     * @param map
     * @return
     */
    @GetMapping(value = "asset_reduce_show")
    public String assetReduceShowPage(String id, ModelMap map) {
        logger.info("Enter Asset Reduce Show Page");
        map.addAttribute("AssetReduceDto", assetReduceService.getReduceReceiptById(id));
        WorksDto worksDto = flowableService.getWorksModelByBusinessKey(id);
        if (!VGUtility.isEmpty(worksDto)) {
            map.addAttribute("processInstanceId", worksDto.getProcessInstanceId());
        }
        return "asset/assetReduceShow";
    }
}

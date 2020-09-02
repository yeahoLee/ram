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
import com.mine.product.czmtr.ram.asset.service.IAssetChangeService;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.MineSecureUtility;
import com.vgtech.platform.common.utility.SpringSecureUserInfo;

@Controller
@SessionAttributes(value = {"LoginUserInfo"})
public class AssetChangePageController {
    private static final Logger logger = LoggerFactory.getLogger(AssetChangePageController.class);
    @Autowired
    private IAssetChangeService assetChangeService;
    @Autowired
    private IBaseService baseService;
    @Autowired
    private IFlowableService flowableService;
    /******************************************************资产管理员变更 start************************************************************/
    /***
     * 管理员变更页面-新增
     * @return
     */
    @GetMapping(value = "/asset_change_maneger")
    public String assetChangeManagerPage(ModelMap map) {
        if (!baseService.getPermCheck("zcglybgxzd_create")) {
            throw new RuntimeException("该用户没有权限进行管理员变更单创建");
        }
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.addAttribute("userInfoDto", userInfoDto);
        map.put("loginDeptDto", userInfoDto.getPropertyMap());
        logger.info("Enter Change Manager Page");
        return "asset/assetManagerChange";
    }

    /***
     * 管理员变更页面-列表信息页
     * @return
     */
    @GetMapping(value = "/asset_maneger_change_receiptList")
    public String assetChangeManagerReceiptListPage(ModelMap map) {
        logger.info("Enter Change Manager Receipt List");
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.addAttribute("userInfoDto", userInfoDto);
        map.put("loginDeptDto", userInfoDto.getPropertyMap());
        return "asset/assetManagerChangeReceiptList";
    }

    /***
     * 管理员变更页面-编辑更新
     * @param id
     * @param map
     * @return
     */
    @GetMapping(value = "update_manager_change_receipt")
    public String updateChangeManagerReceipt(String id, ModelMap map) {
        logger.info("Update Change Manager Receipt");
        if (!baseService.getPermCheck("zcglybgxzd_update")) {
            throw new RuntimeException("该用户没有权限进行管理员变更单更新");
        }
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.addAttribute("AssetManagerChangeDto", assetChangeService.getManagerChangeReceiptById(id));
        map.addAttribute("userInfoDto", userInfoDto);
        map.put("loginDeptDto", userInfoDto.getPropertyMap());
        return "asset/assetUpdateManagerChangeReceipt";
    }

    /***
     * 管理员变更页面-查看
     * @param id
     * @param map
     * @return
     */
    @GetMapping(value = "asset_manager_change_show")
    public String assetManagerChangeShow(String id, ModelMap map) {
        logger.info("Enter Manager Change Show");
        map.addAttribute("AssetManagerChangeDto", assetChangeService.getManagerChangeReceiptById(id));
        WorksDto worksDto = flowableService.getWorksModelByBusinessKey(id);
        if (!VGUtility.isEmpty(worksDto)) {
            map.addAttribute("processInstanceId", worksDto.getProcessInstanceId());
        }
        return "asset/assetManagerChangeShow";
    }

    /******************************************************资产管理员变更 end************************************************************/

    /******************************************************资产使用人变更 start************************************************************/

    /***
     * 使用人变更页面-新增
     * @return
     */
    @GetMapping(value = "/asset_user_change_create")
    public String assetUserChangeCreatePage(ModelMap map) {
        logger.info("Enter Change User Create Page");
        if (!baseService.getPermCheck("zcsyrbgxzd_create")) {
            throw new RuntimeException("该用户没有权限进行使用人变更单创建");
        }
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.addAttribute("userInfoDto", userInfoDto);
        map.put("loginDeptDto", userInfoDto.getPropertyMap());
        return "asset/assetUserChangeCreate";
    }

    /***
     * 使用人变更页面-列表信息页
     * @return
     */
    @GetMapping(value = "/asset_user_change_list")
    public String assetUserChangeListPage(ModelMap map) {
        logger.info("Enter Change User List");
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.addAttribute("userInfoDto", userInfoDto);
        map.put("loginDeptDto", userInfoDto.getPropertyMap());
        return "asset/assetUserChangeList";
    }

    /***
     * 使用人变更页面-编辑更新
     * @param id
     * @param map
     * @return
     */
    @GetMapping(value = "asset_user_change_update")
    public String assetUserChangeUpdatePage(String id, ModelMap map) {
        logger.info("Update Change User Receipt");
        if (!baseService.getPermCheck("zcsyrbgxzd_update")) {
            throw new RuntimeException("该用户没有权限进行使用人变更单更新");
        }
        map.addAttribute("AssetUserChangeDto", assetChangeService.getUserChangeReceiptById(id));
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.addAttribute("userInfoDto", userInfoDto);
        map.put("loginDeptDto", userInfoDto.getPropertyMap());
        return "asset/assetUserChangeUpdate";
    }

    /***
     * 使用人变更页面-查看
     * @param id
     * @param map
     * @return
     */
    @GetMapping(value = "asset_user_change_show")
    public String assetUserChangeShowPage(String id, ModelMap map) {
        logger.info("Show Change User Receipt");
        map.addAttribute("AssetUserChangeDto", assetChangeService.getUserChangeReceiptById(id));
        WorksDto worksDto = flowableService.getWorksModelByBusinessKey(id);
        if (!VGUtility.isEmpty(worksDto)) {
            map.addAttribute("processInstanceId", worksDto.getProcessInstanceId());
        }
        return "asset/assetUserChangeShow";
    }

    /******************************************************资产使用人变更 end************************************************************/

    /******************************************************资产安装位置变更 start************************************************************/
    /***
     * 安装位置变更页面-新增
     * @return
     */
    @GetMapping(value = "/asset_savePlace_change_create")
    public String assetSavePlaceChangeCreatePage(ModelMap map) {
        logger.info("Enter Change SavePlace Create Page");
        if (!baseService.getPermCheck("zcazwzbgxzd_create")) {
            throw new RuntimeException("该用户没有权限进安装位置变更单创建");
        }
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.addAttribute("userInfoDto", userInfoDto);
        map.put("loginDeptDto", userInfoDto.getPropertyMap());
        return "asset/assetSavePlaceChangeCreate";
    }

    /***
     * 安装位置变更页面-列表信息页
     * @return
     */
    @GetMapping(value = "/asset_savePlace_change_list")
    public String assetSavePlaceChangeListPage(ModelMap map) {
        logger.info("Enter Change SavePlace List");
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.addAttribute("userInfoDto", userInfoDto);
        map.put("loginDeptDto", userInfoDto.getPropertyMap());
        return "asset/assetSavePlaceChangeList";
    }

    /***
     * 安装位置变更页面-编辑更新
     * @param id
     * @param map
     * @return
     */
    @GetMapping(value = "asset_savePlace_change_update")
    public String assetSavePlaceChangeUpdatePage(String id, ModelMap map) {
        logger.info("Update Change SavePlace Receipt");
        if (!baseService.getPermCheck("zcazwzbgxzd_update")) {
            throw new RuntimeException("该用户没有权限进安装位置变更单更新");
        }
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.addAttribute("userInfoDto", userInfoDto);
        map.put("loginDeptDto", userInfoDto.getPropertyMap());
        map.addAttribute("AssetSavePlaceChangeDto", assetChangeService.getSavePlaceChangeReceiptById(id));
        return "asset/assetSavePlaceChangeUpdate";
    }

    /***
     * 安装位置变更页面-查看
     * @param id
     * @param map
     * @return
     */
    @GetMapping(value = "asset_savePlace_change_show")
    public String assetSavePlaceChangeShowPage(String id, ModelMap map) {
        logger.info("Show Change SavePlace Receipt");
        map.addAttribute("AssetSavePlaceChangeDto", assetChangeService.getSavePlaceChangeReceiptById(id));
        WorksDto worksDto = flowableService.getWorksModelByBusinessKey(id);
        if (!VGUtility.isEmpty(worksDto)) {
            map.addAttribute("processInstanceId", worksDto.getProcessInstanceId());
        }
        return "asset/assetSavePlaceChangeShow";
    }

    /******************************************************资产安装位置变更 start************************************************************/
}

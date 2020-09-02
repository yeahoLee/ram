package com.mine.product.czmtr.ram.asset.controller;

import com.mine.base.user.dto.UserInfoDto;
import com.mine.product.czmtr.ram.asset.dto.AssetInventoryDto;
import com.mine.product.czmtr.ram.asset.dto.MyAssetInventoryDto;
import com.mine.product.czmtr.ram.asset.service.IAssetInventoryService;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.MineSecureUtility;
import com.vgtech.platform.common.utility.SpringSecureUserInfo;
import com.vgtech.platform.common.utility.VGUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes(value = {"LoginUserInfo"})
public class AssetInventoryPageController {

    private static final Logger logger = LoggerFactory.getLogger(AssetInventoryPageController.class);

    @Autowired
    private IBaseService baseService;
    @Autowired
    private IAssetInventoryService assetInventoryService;

    @GetMapping(value = "/assetInventory_check_create")
    public String assetSequestrationSealPage(AssetInventoryDto dto, ModelMap map) {
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
	    String produceTypeStr = "";
	    boolean production = baseService.getPermCheck("pdrwproduction_create");
	    boolean unproduction = baseService.getPermCheck("pdrwunproduction_create");
	    if (production) {
		    produceTypeStr = "0";
	    } else if (unproduction) {
		    produceTypeStr = "1";
	    }
	    if (!"0".equals(produceTypeStr) && !"1".equals(produceTypeStr)) {
		    throw new RuntimeException("该用户没有权限进行盘点任务单创建");
	    }
	    map.put("produceTypeStr", produceTypeStr);
        map.put("userInfoDto", userInfoDto);
        return "asset/assetInventoryCreate";
    }


    @GetMapping(value = "/assetInventory_update")
    public String assetInventoryUpdatePage(String id, ModelMap map) {
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.put("userInfoDto", userInfoDto);
        map.put("assetInventoryDto", assetInventoryService.getAssetInventoryById(id));
        return "asset/assetInventoryUpdate";
    }

    /*************************************我的盘点单 start******************************************************/
    /***
     * 创建
     * @param dto
     * @param map
     * @return
     */
    @GetMapping(value = "/myassetinventory_create")
    public String myAssetInventoryPage(AssetInventoryDto dto, ModelMap map) {
        //logger.info("Enter assetSequestrationSeal  Page");
		/*if (!baseService.getPermCheck("zcfcd_create")) 
			 throw new RuntimeException("该用户没有权限进行封存单创建");*/
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.put("userInfoDto", userInfoDto);
        return "asset/myAssetInventoryCreate";
    }

    /***
     * 查看列表
     * @param dto
     * @param map
     * @return
     */
    @GetMapping(value = "/myassetinventory_query")
    public String myAssetInventoryForQueryPage(MyAssetInventoryDto dto, ModelMap map) {
        //logger.info("Enter assetSequestrationSeal  Page");
		/*if (!baseService.getPermCheck("zcfcd_create")) 
			 throw new RuntimeException("该用户没有权限进行封存单创建");*/
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.put("loginDeptDto", userInfoDto.getPropertyMap());
        map.put("userInfoDto", userInfoDto);
        return "asset/myAssetInventoryist";
    }

    /***
     * 查看
     * @param dto
     * @param map
     * @return
     */
    @GetMapping(value = "/myassetinventory_update")
    public String updateMyAssetInventoryPage(MyAssetInventoryDto dto, ModelMap map) {
        //logger.info("Enter assetSequestrationSeal  Page");
		/*if (!baseService.getPermCheck("zcfcd_create")) 
			 throw new RuntimeException("该用户没有权限进行封存单创建");*/
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        MyAssetInventoryDto myAssetInventoryDto = assetInventoryService.getMyAssetInventoryDtoById(dto);
        AssetInventoryDto assetInventoryDto = assetInventoryService.getAssetInventoryById(myAssetInventoryDto.getAssetInventoryId());
        if (!VGUtility.isEmpty(dto.getManagerDeptId())) {
            map.put("managerDeptId", dto.getManagerDeptId());
        }
        map.put("userInfoDto", userInfoDto);
        map.put("assetInventoryDto", assetInventoryDto);
        map.put("myAssetInventoryDto", myAssetInventoryDto);
        return "asset/myAssetInventoryUpdate";
    }

    /***
     * 查看
     * @param dto
     * @param map
     * @return
     */
    @GetMapping(value = "/myassetinventory_view")
    public String ViewMyAssetInventoryPage(MyAssetInventoryDto dto, ModelMap map) {
        //logger.info("Enter assetSequestrationSeal  Page");
		/*if (!baseService.getPermCheck("zcfcd_create")) 
			 throw new RuntimeException("该用户没有权限进行封存单创建");*/
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        MyAssetInventoryDto myAssetInventoryDto = assetInventoryService.getMyAssetInventoryDtoById(dto);
        AssetInventoryDto assetInventoryDto = assetInventoryService.getAssetInventoryById(myAssetInventoryDto.getAssetInventoryId());
        if (!VGUtility.isEmpty(dto.getManagerDeptId())) {
            map.put("managerDeptId", dto.getManagerDeptId());
        }
        map.put("userInfoDto", userInfoDto);
        map.put("assetInventoryDto", assetInventoryDto);
        map.put("myAssetInventoryDto", myAssetInventoryDto);
        return "asset/myAssetInventoryView";
    }

    /*************************************我的盘点单 end*****************************************************/

    /**
     * 进入盘点任务管理
     *
     * @param dto
     * @param map
     * @return
     */
    @GetMapping(value = "/assetInventory_query")
    public String assetInventoryPage(AssetInventoryDto dto, ModelMap map) {
        logger.info("Enter assetInventory query Page");
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.put("userInfoDto", userInfoDto);
        return "asset/assetInventoryList";
    }

    /**
     * 进入盘点任务查看
     *
     * @param dto
     * @param map
     * @return
     */
    @GetMapping(value = "/assetInventory_view")
    public String assetInventoryViewPage(AssetInventoryDto dto, ModelMap map) {
        logger.info("Enter assetInventory query Page");
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        AssetInventoryDto assetInventoryDto = assetInventoryService.getAssetInventoryById(dto.getId());
        map.put("assetInventoryDto", assetInventoryDto);
        map.put("userInfoDto", userInfoDto);
        return "asset/assetInventoryView";
    }
}

package com.mine.product.czmtr.ram.base.controller;

import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.MineSecureUtility;
import com.vgtech.platform.common.utility.SpringSecureUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes(value = {"LoginUserInfo"})
public class BasePageController {
    private static final Logger logger = LoggerFactory.getLogger(BasePageController.class);

    @Autowired
    private IBaseService baseService;

    @GetMapping(value = "/base_user_index")

    public String baseUseIndexPage() {
        logger.info("Enter Base User Index Page");
        return "base/baseUserIndex";
    }

    @GetMapping(value = "/base_dept_index")
    public String baseDeptIndexPage() {
        logger.info("Enter Base Dept Index Page");
//        return "base/baseDeptIndex";
        return "base/baseDeptIndex2";
    }

    @GetMapping(value = "/base_dictionary")
    public String baseDictionaryPage() {
        logger.info("Enter Base Dictionary Page");
        return "base/baseDictionary";
    }

    @GetMapping(value = "/base_permission")
    public String basePermissionPage() {
        logger.info("Enter Base Dictionary Page");
        return "base/basePermission";
    }

    @GetMapping(value = "/base_change_pwd")
    public String baseChangePwdPage(ModelMap modelMap) {
        logger.info("Enter Base Change Pwd Page");
        modelMap.addAttribute("userInfoDto", ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo());
        return "base/baseChangePwd";
    }
}

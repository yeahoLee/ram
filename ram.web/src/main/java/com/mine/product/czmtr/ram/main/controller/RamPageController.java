package com.mine.product.czmtr.ram.main.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.mine.base.menu.dto.MenuDto;
import com.mine.base.menu.service.IMenuService;
import com.mine.base.permission.dto.PermissionValueDto;
import com.mine.base.permission.service.IPermissionService;
import com.mine.base.user.dto.DeptInfoDto;
import com.mine.base.user.dto.UserInfoDto;
import com.mine.product.czmtr.ram.asset.dto.AssetDynamicViewDto;
import com.mine.product.czmtr.ram.asset.service.IAssetService;
import com.mine.product.czmtr.ram.base.dto.BaseUserDto;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.MineSecureUtility;
import com.vgtech.platform.common.utility.SpringSecureUserInfo;
import com.vgtech.platform.common.utility.VGUtility;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class RamPageController {
    private static final Logger logger = LoggerFactory.getLogger(RamPageController.class);

    @Autowired
    private IAssetService assetService;
    @Autowired
    private IPermissionService permService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IBaseService baseService;

    @GetMapping("/")
    public String enterLoginPage(ModelMap modelMap) {
        logger.info("Enter Default Page");
        return "redirect:/index";
    }

    @GetMapping(value = "/index")
    public String enterIndexPage(ModelMap modelMap) {
        logger.info("Enter Index Page");
        Set<String> menuIdSet = new HashSet<String>();
        List<MenuDto> menuDtoList = new ArrayList<MenuDto>();
        List<MenuDto> tempMenuDtoList = new ArrayList<MenuDto>();
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        Map<String, Object> propertyMap = userInfoDto.getPropertyMap();
        DeptInfoDto deptInfoDto = (DeptInfoDto) propertyMap.get("loginDeptDto");

        //当前部门所在组菜单
        BaseUserDto baseUserInfoDto = baseService.getUserInfoByUserIdAndDeptId(userInfoDto.getId(), VGUtility.isEmpty(deptInfoDto) ? null : deptInfoDto.getId());
        List<String> permGroupIdList = baseUserInfoDto.getPermGroupIdList();
        if (!VGUtility.isEmpty(permGroupIdList)) {
            for (String permGroupId : permGroupIdList) {
                PermissionValueDto dto = permService.getPermissionValue(permGroupId);
                if (!VGUtility.isEmpty(dto))
                    menuIdSet.addAll(new HashSet<String>(dto.getMenuValueList().get(IBaseService.SYSMARK)));
            }
        }

//		//对比 添加
//		tempMenuDtoList = menuService.getMenuBySysMark(IBaseService.SYSMARK); //所有菜单 
//		for(MenuDto tempMenuDto:tempMenuDtoList) {
//			for(String menuId: menuIdSet) {
//				if(tempMenuDto.getId().equals(menuId))
//					menuDtoList.add(tempMenuDto);
//			}
//		}
//		
        // 对比 添加
        tempMenuDtoList = menuService.getMenuBySysMark(IBaseService.SYSMARK); //所有菜单
        for (MenuDto tempMenuDto : tempMenuDtoList) {
            for (String menuId : menuIdSet) {
                if (tempMenuDto.getId().equals(menuId)) {//暂时未添加权限,先显示所有已添加的菜单
                    List<MenuDto> subList = tempMenuDto.getSubItem();
                    if (!VGUtility.isEmpty(subList)) {
                        List<MenuDto> result = new ArrayList<MenuDto>();
                        for (MenuDto m : subList) {
                            for (String m2 : menuIdSet) {
                                if (m.getId().equals(m2)) {
                                    result.add(m);
                                }
                            }
                        }
                        tempMenuDto.setSubItem(result);
                    }
                    menuDtoList.add(tempMenuDto);
                }
            }
        }

        for (MenuDto menuDto : menuDtoList) {
            if (menuDto.getTitle().equals("资产台账管理")) {
                List<MenuDto> tempMenuDtolist = menuDto.getSubItem();

                List<AssetDynamicViewDto> dynaViewDtoList = (List<AssetDynamicViewDto>) assetService.getAssetDynamicForDataGrid(null, userInfoDto).get("rows");
                for (AssetDynamicViewDto dynamicViewDto : dynaViewDtoList) {
                    if (dynamicViewDto.getAssetViewState() == 1) { //启用状态的视图
                        MenuDto tempMenuDto = new MenuDto();
                        tempMenuDto.setTitle(dynamicViewDto.getAssetViewName());
                        tempMenuDto.setIcon("fa fa-codepen");
                        tempMenuDto.setAddress("asset_stan_book?dynaViewId=" + dynamicViewDto.getId());
                        tempMenuDtolist.add(tempMenuDto);
                    }
                }
                break;
            }
        }

        modelMap.addAttribute("menuDtoList", menuDtoList);
        modelMap.addAttribute("userInfoDto", userInfoDto);
        modelMap.addAttribute("loginDeptDto", deptInfoDto);
        modelMap.addAttribute("deptInfoList", propertyMap.get("deptInfoList"));
        return "main";
    }

    @GetMapping(value = "/local_login")
    public String localLogin() {
        logger.info("Local Login Page");
        //return "localLogin";//原登陆页
        return "login";//新的登陆页
    }

    @GetMapping(value = "/error_page")
    public String errorPage(String errorMessage, ModelMap modelMap) {
        logger.info("Error Page");
        modelMap.addAttribute("errorMessage", StringEscapeUtils.escapeHtml(errorMessage));
        return "error";
    }

    /**
     * 统一认证登陆入口
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping(value = "/local_oauth_login")
    public void localOAuthLogin(HttpServletRequest request, HttpServletResponse response) {
        try {
            String redirect_uri = URLEncoder.encode(this.getLocalUrl(request) + "/oauth_login", "utf-8");
            String url = IBaseService.uamsbrowserUrl + "/oauth/authorize?response_type=code&client_id=" + IBaseService.client_id + "&scope=all&redirect_uri=" + redirect_uri;
            response.sendRedirect(url);
        } catch (IOException e) {
            throw new RuntimeException("登陆失败!");
        }
    }

    /**
     * 统一认证登陆
     *
     * @param code
     * @param request
     * @param response
     * @param redirectAttributes
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/oauth_login")
    public String oAuthLogin(String code, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws IOException {
        try {
            String redirect_uri = URLEncoder.encode(this.getLocalUrl(request) + "/oauth_login", "utf-8");
            String url = IBaseService.uamsbrowserUrl + "/oauth/token?grant_type=authorization_code&code=" + code + "&redirect_uri=" + redirect_uri;
            String authorization = Base64.encodeBase64String((IBaseService.client_id + ":" + IBaseService.client_secret).getBytes());
            authorization = "Basic" + " " + authorization;
            String result = baseService.sendGet(url, authorization);
            JSONObject obj = JSONObject.parseObject(result);
            String access_token = obj.get("access_token").toString().replace("\"", "");
            redirectAttributes.addAttribute("accessToken", access_token);
        } catch (IOException e) {
            return "redirect:local_oauth_login";
        }
        return "redirect:oauth_user";
    }

    /**
     * 获取登陆用户信息
     *
     * @param accessToken
     * @param redirectAttributes
     * @return
     */
    @GetMapping(value = "/oauth_user")
    public String getOAuthUser(String accessToken, RedirectAttributes redirectAttributes) {
        String url = IBaseService.uamsAdminUrl + "/resource/user/me";
        String authorization = "bearer" + " " + accessToken;
        try {
            String result = baseService.sendGet(url, authorization);
            JSONObject obj = JSONObject.parseObject(result);
            JSONObject user = JSONObject.parseObject(obj.get("data").toString());
            redirectAttributes.addAttribute("userName", user.getString("mobile"));
        } catch (Exception e) {
            return "redirect:local_oauth_login";
        }

        return "redirect:single_login_callback";
    }

    /**
     * 单点登陆回调内部登陆
     *
     * @param userName
     * @return
     */
    @GetMapping(value = "/single_login_callback", produces = "text/html")
    @ResponseBody
    public String callbackSingleLogin(@RequestParam String userName) {
        logger.info("Single Login Callback");
        UserInfoDto userInfoDto = baseService.getUserInfoDtoByUserName(userName);
        return "<html><head></head><body onload=\"document.Form1.submit()\"><form name='Form1' method='post' action='/ram/login'><input name=\"username\" type=\"hidden\" value=\"" + userInfoDto.getUserName() + "\"><input name=\"password\" type=\"hidden\" value=\"" + userInfoDto.getPassword() + "\"></form></body></html>";
    }

    /**
     * 登出
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("logout");
        String returnUrl = this.getLocalUrl(request) + "/local_oauth_login";
        String url = IBaseService.uamsbrowserUrl + "/logout?returnUrl=" + returnUrl;
        response.sendRedirect(url);
    }

    /**
     * 获取当前url,如http://localhost:8080/ram
     *
     * @param request
     * @return
     */
    public String getLocalUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
}
package com.mine.product.czmtr.ram.asset.controller;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.mine.product.czmtr.ram.flowable.dto.WorksDto;
import com.mine.product.czmtr.ram.flowable.service.IFlowableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.mine.base.user.dto.UserInfoDto;
import com.mine.base.user.service.IUserService;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.AssetReceiveUseDto;
import com.mine.product.czmtr.ram.asset.service.IAssetReceiveUseService;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.MineSecureUtility;
import com.vgtech.platform.common.utility.SpringSecureUserInfo;
import com.vgtech.platform.common.utility.VGUtility;

@Controller
@SessionAttributes(value = {"LoginUserInfo"})
public class AssetReceiveUsePageController {

    private static final Logger logger = LoggerFactory.getLogger(AssetReceiveUsePageController.class);
    @Autowired
    private IAssetReceiveUseService assetReceiveUseService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IBaseService baseService;
    @Autowired
    private IFlowableService flowableService;

    @GetMapping(value = "/assetreceiveuse_create")
    public String assetReceiveUseCreatePage(ModelMap map) {
        logger.info("Enter AssetReceiveUse Create Page");
        if (!baseService.getPermCheck("zclyxzd_create")) {
            throw new RuntimeException("该用户没有权限进行资产领用单创建");
        }
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.put("userInfoDto", userInfoDto);

        return "asset/assetReceiveUseCreate";
    }

    @GetMapping(value = "/assetreceiveuse_query")
    public String assetReceiveUseViewPage(ModelMap map) {
        logger.info("Enter AssetReceiveUse List Page");
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.put("userInfoDto", userInfoDto);
        return "asset/assetReceiveUseList";
    }

    @GetMapping(value = "/assetreceiveuse_update")
    public String assetReceiveUseUpdatePage(AssetReceiveUseDto dto, ModelMap map) {
        logger.info("Enter AssetReceiveUse Update Page");
        if (!baseService.getPermCheck("zclyxzd_update")) {
            throw new RuntimeException("该用户没有权限进行资产领用单更新");
        }
        if (VGUtility.isEmpty(dto))
            throw new RuntimeException("资产借用单，不能为空！");
        else {
            List<AssetReceiveUseDto> assetReceiveUseDto = (List<AssetReceiveUseDto>) assetReceiveUseService.getAssetReceiveUseDtoByQuerysForDataGrid(new ISearchExpression() {
                @Override
                public Object change(Object... arg0) {
                    CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                    Root root = (Root) arg0[0];
                    Predicate finalPred = null;
                    if (!VGUtility.isEmpty(dto.getAssetReceiveUseCode()))
                        finalPred = builder.equal(root.get("assetReceiveUseCode"), dto.getAssetReceiveUseCode());
                    if (!VGUtility.isEmpty(dto.getId()))
                        finalPred = builder.equal(root.get("id"), dto.getId());

                    return finalPred;
                }
            }, null).get("rows");
            if (assetReceiveUseDto.size() > 0) {
                AssetReceiveUseDto assetReceiveUse = assetReceiveUseDto.get(0);
//				assetReceiveUse.setAssetReceiveUseUserName(userService.getUserInfo(assetReceiveUse.getAssetReceiveUseUserID()).getChsName());
//				assetReceiveUse.setAssetReceiveUseDepartmentName(userService.getDeptInfo(assetReceiveUse.getAssetReceiveUseDepartmentID()).getDeptName());
//				assetReceiveUse.setCreateUserName(userService.getUserInfo(assetReceiveUse.getCreateUserID()).getChsName());
                map.put("AssetReceiveUseDto", assetReceiveUse);
            } else
                throw new RuntimeException("资产借用单，不能为空！");
        }
        return "asset/assetReceiveUseUpdate";
    }

    @GetMapping(value = "/assetreceiveuse_view")
    public String assetReceiveUseViewPage(AssetReceiveUseDto dto, ModelMap map) {
        logger.info("Enter AssetReceiveUse View Page");
        if (VGUtility.isEmpty(dto))
            throw new RuntimeException("资产借用单，不能为空！");
        else {
            List<AssetReceiveUseDto> assetReceiveUseDto = (List<AssetReceiveUseDto>) assetReceiveUseService.getAssetReceiveUseDtoByQuerysForDataGrid(new ISearchExpression() {
                @Override
                public Object change(Object... arg0) {
                    CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                    Root root = (Root) arg0[0];
                    Predicate finalPred = null;
                    if (!VGUtility.isEmpty(dto.getAssetReceiveUseCode()))
                        finalPred = builder.equal(root.get("assetReceiveUseCode"), dto.getAssetReceiveUseCode());
                    if (!VGUtility.isEmpty(dto.getId()))
                        finalPred = builder.equal(root.get("id"), dto.getId());

                    return finalPred;
                }
            }, null).get("rows");
            if (assetReceiveUseDto.size() > 0) {
                AssetReceiveUseDto assetReceiveUse = assetReceiveUseDto.get(0);
                assetReceiveUse.setAssetReceiveUseUserName(userService.getUserInfo(assetReceiveUse.getAssetReceiveUseUserID()).getChsName());
                assetReceiveUse.setAssetReceiveUseDepartmentName(userService.getDeptInfo(assetReceiveUse.getAssetReceiveUseDepartmentID()).getDeptName());
                assetReceiveUse.setCreateUserName(userService.getUserInfo(assetReceiveUse.getCreateUserID()).getChsName());
                map.put("AssetReceiveUseDto", assetReceiveUse);
            } else
                throw new RuntimeException("资产借用单，不能为空！");
        }

        WorksDto worksDto = flowableService.getWorksModelByApproveCode(dto.getAssetReceiveUseCode());
        if (!VGUtility.isEmpty(worksDto)) {
            map.addAttribute("processInstanceId", worksDto.getProcessInstanceId());
        }
        return "asset/assetReceiveUseView";
    }
}

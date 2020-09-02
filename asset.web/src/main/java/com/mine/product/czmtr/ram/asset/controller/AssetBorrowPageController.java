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
import com.mine.product.czmtr.ram.asset.dto.AssetBorrowDto;
import com.mine.product.czmtr.ram.asset.service.IAssetBorrowService;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.MineSecureUtility;
import com.vgtech.platform.common.utility.SpringSecureUserInfo;
import com.vgtech.platform.common.utility.VGUtility;

@Controller
@SessionAttributes(value = {"LoginUserInfo"})
public class AssetBorrowPageController {

    private static final Logger logger = LoggerFactory.getLogger(AssetBorrowPageController.class);

    @Autowired
    private IAssetBorrowService assetBorrowService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IBaseService baseService;
    @Autowired
    private IFlowableService flowableService;

    @GetMapping(value = "/assetborrow_create")
    public String assetBorrowCreatePage(AssetBorrowDto dto, ModelMap map) {
        logger.info("Enter AssetBorrow Create Page");
        if (!baseService.getPermCheck("zcjyxzd_create")) {
            throw new RuntimeException("该用户没有权限进行资产借用单创建");
        }
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.put("userInfoDto", userInfoDto);
        return "asset/assetBorrowCreate";
    }

    @GetMapping(value = "/assetborrow_query")
    public String assetBorrowViewPage(ModelMap map) {
        logger.info("Enter AssetBorrow List Page");
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.put("userInfoDto", userInfoDto);
        return "asset/assetBorrowList";
    }

    @GetMapping(value = "/assetborrow_update")
    public String assetBorrowUpdatePage(AssetBorrowDto dto, ModelMap map) {
        logger.info("Enter AssetBorrow Update Page");
        if (!baseService.getPermCheck("zcjyxzd_update")) {
            throw new RuntimeException("该用户没有权限进行资产借用单更新");
        }
        if (VGUtility.isEmpty(dto))
            throw new RuntimeException("资产借用单，不能为空！");
        else {
            List<AssetBorrowDto> assetBorrowDto = (List<AssetBorrowDto>) assetBorrowService.getAssetBorrowDtoByQuerysForDataGrid(new ISearchExpression() {
                @Override
                public Object change(Object... arg0) {
                    CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                    Root root = (Root) arg0[0];
                    Predicate finalPred = null;
                    if (!VGUtility.isEmpty(dto.getAssetborrowCode()))
                        finalPred = builder.equal(root.get("assetborrowCode"), dto.getAssetborrowCode());
                    if (!VGUtility.isEmpty(dto.getId()))
                        finalPred = builder.equal(root.get("id"), dto.getId());

                    return finalPred;
                }
            }, null).get("rows");
            if (assetBorrowDto.size() > 0) {
                AssetBorrowDto assetBorrow = assetBorrowDto.get(0);
                map.put("AssetBorrowDto", assetBorrow);
            } else
                throw new RuntimeException("资产借用单，不能为空！");
        }
        return "asset/assetBorrowUpdate";
    }

    @GetMapping(value = "/assetborrow_view")
    public String assetBorrowViewPage(AssetBorrowDto dto, ModelMap map) {
        logger.info("Enter AssetBorrow View Page");
        if (VGUtility.isEmpty(dto))
            throw new RuntimeException("资产借用单，不能为空！");
        else {
            List<AssetBorrowDto> assetBorrowDto = (List<AssetBorrowDto>) assetBorrowService.getAssetBorrowDtoByQuerysForDataGrid(new ISearchExpression() {
                @Override
                public Object change(Object... arg0) {
                    CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                    Root root = (Root) arg0[0];
                    Predicate finalPred = null;
                    if (!VGUtility.isEmpty(dto.getAssetborrowCode()))
                        finalPred = builder.equal(root.get("assetborrowCode"), dto.getAssetborrowCode());
                    if (!VGUtility.isEmpty(dto.getId()))
                        finalPred = builder.equal(root.get("id"), dto.getId());

                    return finalPred;
                }
            }, null).get("rows");
            if (assetBorrowDto.size() > 0) {
                AssetBorrowDto assetBorrow = assetBorrowDto.get(0);
                assetBorrow.setAssetborrowUserName(userService.getUserInfo(assetBorrow.getAssetborrowUserID()).getChsName());
                assetBorrow.setAssetborrowDepartmentName(userService.getDeptInfo(assetBorrow.getAssetborrowDepartmentID()).getDeptName());
                assetBorrow.setCreateUserName(userService.getUserInfo(assetBorrow.getCreateUserID()).getChsName());
                map.put("AssetBorrowDto", assetBorrow);
            } else
                throw new RuntimeException("资产借用单，不能为空！");
        }

        WorksDto worksDto = flowableService.getWorksModelByApproveCode(dto.getAssetborrowCode());
        if (!VGUtility.isEmpty(worksDto)) {
            map.addAttribute("processInstanceId", worksDto.getProcessInstanceId());
        }

        return "asset/assetBorrowView";
    }
}

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
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.AssetRevertDto;
import com.mine.product.czmtr.ram.asset.service.IAssetRevertService;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.MineSecureUtility;
import com.vgtech.platform.common.utility.SpringSecureUserInfo;
import com.vgtech.platform.common.utility.VGUtility;

@Controller
@SessionAttributes(value = {"LoginUserInfo"})
public class AssetRevertPageController {
    private static final Logger logger = LoggerFactory.getLogger(AssetRevertPageController.class);
    @Autowired
    private IAssetRevertService assetRevertService;
    @Autowired
    private IBaseService baseService;
    @Autowired
    private IFlowableService flowableService;

    @GetMapping(value = "/assetrevert_create")
    public String assetRevertCreatePage(ModelMap map) {
        logger.info("Enter AssetRevert Create Page");
        if (!baseService.getPermCheck("zcjyghxzd_create")) {
            throw new RuntimeException("该用户没有权限进行资产借用归还单创建");
        }
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.put("userInfoDto", userInfoDto);

        return "asset/assetRevertCreate";
    }

    @GetMapping(value = "/assetrevert_query")
    public String assetRevertQueryPage(ModelMap map) {
        logger.info("Enter AssetRevert Query Page");
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.put("userInfoDto", userInfoDto);

        return "asset/assetRevertList";
    }

    @GetMapping(value = "/assetrevert_update")
    public String assetRevertUpdatePage(AssetRevertDto dto, ModelMap map) {
        logger.info("Enter AssetRevert Update Page");
        if (!baseService.getPermCheck("zcjyghxzd_update")) {
            throw new RuntimeException("该用户没有权限进行资产借用归还单更新");
        }
        if (VGUtility.isEmpty(dto))
            throw new RuntimeException("资产归还单，不能为空！");
        else {
            List<AssetRevertDto> assetRevertDto = (List<AssetRevertDto>) assetRevertService.getAssetRevertDtoByQuerysForDataGrid(new ISearchExpression() {
                @Override
                public Object change(Object... arg0) {
                    CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                    Root root = (Root) arg0[0];
                    Predicate finalPred = null;
                    if (!VGUtility.isEmpty(dto.getAssetrevertCode()))
                        finalPred = builder.equal(root.get("assetrevertCode"), dto.getAssetrevertCode());
                    if (!VGUtility.isEmpty(dto.getId()))
                        finalPred = builder.equal(root.get("id"), dto.getId());

                    return finalPred;
                }
            }, null).get("rows");
            if (assetRevertDto.size() > 0) {
                map.put("AssetRevertDto", assetRevertDto.get(0));
            } else {
                throw new RuntimeException("资产归还单，不能为空！");
            }
        }
        return "asset/assetRevertUpdate";
    }

    @GetMapping(value = "/assetrevert_view")
    public String assetRevertViewPage(AssetRevertDto dto, ModelMap map) {
        logger.info("Enter AssetRevert View Page");

        if (VGUtility.isEmpty(dto))
            throw new RuntimeException("资产归还单，不能为空！");
        else {
            List<AssetRevertDto> assetRevertDto = (List<AssetRevertDto>) assetRevertService.getAssetRevertDtoByQuerysForDataGrid(new ISearchExpression() {
                @Override
                public Object change(Object... arg0) {
                    CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                    Root root = (Root) arg0[0];
                    Predicate finalPred = null;
                    if (!VGUtility.isEmpty(dto.getAssetrevertCode()))
                        finalPred = builder.equal(root.get("assetrevertCode"), dto.getAssetrevertCode());
                    if (!VGUtility.isEmpty(dto.getId()))
                        finalPred = builder.equal(root.get("id"), dto.getId());

                    return finalPred;
                }
            }, null).get("rows");
            if (assetRevertDto.size() > 0) {
                map.put("AssetRevertDto", assetRevertDto.get(0));
            } else {
                throw new RuntimeException("资产归还单，不能为空！");
            }
        }

        WorksDto worksDto = flowableService.getWorksModelByApproveCode(dto.getAssetrevertCode());
        if (!VGUtility.isEmpty(worksDto)) {
            map.addAttribute("processInstanceId", worksDto.getProcessInstanceId());
        }

        return "asset/assetRevertView";
    }
}

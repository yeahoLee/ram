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
import com.mine.product.czmtr.ram.asset.dto.AssetReceiveRevertDto;
import com.mine.product.czmtr.ram.asset.service.IAssetReceiveRevertService;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.MineSecureUtility;
import com.vgtech.platform.common.utility.SpringSecureUserInfo;
import com.vgtech.platform.common.utility.VGUtility;

@Controller
@SessionAttributes(value = {"LoginUserInfo"})
public class AssetReceiveRevertPageController {

    private static final Logger logger = LoggerFactory.getLogger(AssetRevertPageController.class);
    @Autowired
    private IAssetReceiveRevertService assetReceiveRevertService;
    @Autowired
    private IBaseService baseService;
    @Autowired
    private IFlowableService flowableService;

    @GetMapping(value = "/assetreceiverevert_create")
    public String assetReceiveRevertCreatePage(ModelMap map) {
        logger.info("Enter AssetRevertRevert Create Page");
        if (!baseService.getPermCheck("zclyghxzd_create")) {
            throw new RuntimeException("该用户没有权限进行资产领用单创建");
        }
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.put("userInfoDto", userInfoDto);
        return "asset/assetReceiveRevertCreate";
    }

    @GetMapping(value = "/assetreceiverevert_query")
    public String assetReceiveRevertQueryPage(ModelMap map) {
        logger.info("Enter AssetRevertRevert Query Page");
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        map.put("userInfoDto", userInfoDto);
        return "asset/assetReceiveRevertList";
    }

    @GetMapping(value = "/assetreceiverevert_update")
    public String assetReceiveRevertUpdatePage(AssetReceiveRevertDto dto, ModelMap map) {
        logger.info("Enter AssetRevertRevert Update Page");
        if (!baseService.getPermCheck("zclyghxzd_create")) {
            throw new RuntimeException("该用户没有权限进行资产领用归还单创建");
        }
        if (VGUtility.isEmpty(dto))
            throw new RuntimeException("资产归还单，不能为空！");
        else {
            List<AssetReceiveRevertDto> assetReceiveRevertDto = (List<AssetReceiveRevertDto>) assetReceiveRevertService.getAssetReceiveRevertDtoByQuerysForDataGrid(new ISearchExpression() {
                @Override
                public Object change(Object... arg0) {
                    CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                    Root root = (Root) arg0[0];
                    Predicate finalPred = null;
                    if (!VGUtility.isEmpty(dto.getAssetReceiveRevertCode()))
                        finalPred = builder.equal(root.get("assetReceiveRevertCode"), dto.getAssetReceiveRevertCode());
                    if (!VGUtility.isEmpty(dto.getId()))
                        finalPred = builder.equal(root.get("id"), dto.getId());

                    return finalPred;
                }
            }, null).get("rows");
            if (assetReceiveRevertDto.size() > 0)
                map.put("AssetReceiveRevertDto", assetReceiveRevertDto.get(0));
            else
                throw new RuntimeException("资产归还单，不能为空！");
        }
        return "asset/assetReceiveRevertUpdate";
    }

    @GetMapping(value = "/assetreceiverevert_view")
    public String assetBorrowViewPage(AssetReceiveRevertDto dto, ModelMap map) {
        logger.info("Enter AssetRevertRevert View Page");
        if (VGUtility.isEmpty(dto))
            throw new RuntimeException("资产归还单，不能为空！");
        else {
            List<AssetReceiveRevertDto> assetReceiveRevertDto = (List<AssetReceiveRevertDto>) assetReceiveRevertService.getAssetReceiveRevertDtoByQuerysForDataGrid(new ISearchExpression() {
                @Override
                public Object change(Object... arg0) {
                    CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                    Root root = (Root) arg0[0];
                    Predicate finalPred = null;
                    if (!VGUtility.isEmpty(dto.getAssetReceiveRevertCode()))
                        finalPred = builder.equal(root.get("assetReceiveRevertCode"), dto.getAssetReceiveRevertCode());
                    if (!VGUtility.isEmpty(dto.getId()))
                        finalPred = builder.equal(root.get("id"), dto.getId());

                    return finalPred;
                }
            }, null).get("rows");
            if (assetReceiveRevertDto.size() > 0)
                map.put("AssetReceiveRevertDto", assetReceiveRevertDto.get(0));
            else
                throw new RuntimeException("资产归还单，不能为空！");
        }

        WorksDto worksDto = flowableService.getWorksModelByApproveCode(dto.getAssetReceiveRevertCode());
        if (!VGUtility.isEmpty(worksDto)) {
            map.addAttribute("processInstanceId", worksDto.getProcessInstanceId());
        }

        return "asset/assetReceiveRevertView";
    }
}

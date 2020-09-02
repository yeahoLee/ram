package com.mine.product.czmtr.ram.asset.controller;

import com.mine.base.user.dto.UserInfoDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.*;
import com.mine.product.czmtr.ram.asset.service.IAssetReceiveRevertService;
import com.mine.product.czmtr.ram.asset.service.IAssetReceiveUseTempService;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.MineSecureUtility;
import com.vgtech.platform.common.utility.SpringSecureUserInfo;
import com.vgtech.platform.common.utility.VGUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/assetreceiverevert/")
@SessionAttributes(value = {"LoginUserInfo"})
public class AssetReceiveRevertController {

    private static final Logger logger = LoggerFactory.getLogger(AssetController.class);

    @Autowired
    private IAssetReceiveUseTempService assetReceiveUseTempService;
    @Autowired
    private IAssetReceiveRevertService assetReceiveRevertService;
    @Autowired
    private IBaseService baseService;

    @PostMapping(value = "assetreceivereverttemp_datagrid")
    @ResponseBody
    public Map<String, Object> getAssetReceiveUseTempByQuerysForDataGrid(AssetReceiveUseDto assetReceiveUseDto,
                                                                         @RequestParam(defaultValue = "1") String page,
                                                                         @RequestParam(defaultValue = "20") String rows) {
        logger.info("Controller: Get Asset By Querys {} For DataGrid", assetReceiveUseDto);
        //TODO
        List<AssetReceiveUseTempDto> dtolist = assetReceiveUseTempService.getListByUserId(((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo().getId());

        ModelMap map = new ModelMap();
        map.put("rows", dtolist);
        map.put("total", dtolist.size());
        return map;
    }

    //添加归还资产
    @PostMapping(value = "assetreceivereverttemp_query_norevert")
    @ResponseBody
    public Map<String, Object> getAssetReveiveUseQuery(ModelMap map, AssetAssetDto asset) {
        logger.info("获取资产未归还列表信息");
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        List<AssetReceiveUseTempDto> dtolist = assetReceiveUseTempService.getNRAsset(userInfoDto, asset);
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtolist);
        modelMap.addAttribute("total", dtolist.size());
        return modelMap;
    }

    @PostMapping(value = "assetreceivereverttemp_query")
    @ResponseBody
    public Map<String, Object> getAssetReveiveUseTempQuery(AssetReceiveUseTempDto dto, ModelMap map) {
        logger.info("Enter AssetBorrowTemp Query Page");
        List<AssetReceiveUseTempDto> dtolist = assetReceiveUseTempService.getAssetReceiveUseTempDtoListByRevertId(dto.getAssetReceiveRevertID());
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtolist);
        modelMap.addAttribute("total", dtolist.size());
        return modelMap;

    }

    @PostMapping(value = "create_assetreceiverevert")
    @ResponseBody
    public ModelMap createAssetReceiveRevert(AssetReceiveRevertDto assetReceiveRevertDto, String assetReceiveUseTempDtoList) {
        logger.info("Controller: Create AssetReceiveRevert" + assetReceiveRevertDto.toString());
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        AssetReceiveRevertDto dto = assetReceiveRevertService.createAssetReceiveRevert(assetReceiveUseTempDtoList, assetReceiveRevertDto, userInfoDto);
        ModelMap map = new ModelMap();
        map.put("dto", dto);
        map.put("success", "true");
        return map;
    }

    @PostMapping(value = "create_assetreceivereverttemp")
    @ResponseBody
    public String createAssetReceiveUseTemp(AssetReceiveRevertDto assetReceiveRevertDto, String AssetReceiveUseTempIdListStr) {
        logger.info("Controller: Create AssetReceiveUseTemp" + assetReceiveRevertDto.toString());
        assetReceiveUseTempService.createAssetReceiveUseTempByAssetIdListStrForRevert(AssetReceiveUseTempIdListStr, assetReceiveRevertDto);
        return "{\"success\":true}";
    }

    @PostMapping(value = "update_assetreceiverevert")
    @ResponseBody
    public ModelMap updateAssetReceiveRevert(AssetReceiveRevertDto assetReceiveRevertDto) {
        logger.info("Controller: Update AssetRevertDto" + assetReceiveRevertDto.toString());
        AssetReceiveRevertDto dto = assetReceiveRevertService.updateAssetReceiveRevert(assetReceiveRevertDto);
        ModelMap map = new ModelMap();
        map.put("id", dto.getId());
        map.put("success", "true");
        return map;
    }

    @PostMapping(value = "delete_assetreceivereverttemp")
    @ResponseBody
    public String deleteAssetReceiveUseTemp(String assetIdList, String AssetReceiveRevertID) {
        logger.info("Controller: Delete AssetReceiveUseTemp" + assetIdList);
        List<String> assetIdListStr = new ArrayList<String>();
        if (!VGUtility.isEmpty(assetIdList))
            Arrays.stream(assetIdList.split(",")).forEach(arr -> assetIdListStr.add(arr));

        assetReceiveUseTempService.deleteAssetReceiveUseTempByAssetReceiveRevertID(assetIdListStr, AssetReceiveRevertID);
        return "{\"success\":true}";
    }

    @PostMapping(value = "delete_assetreceiverevert")
    @ResponseBody
    public String deleteAssetReceiveRevert(String AssetReceiveRevertId) {
        logger.info("Controller: Delete AssetReceiveRevert" + AssetReceiveRevertId);
        if (!baseService.getPermCheck("zclyghxzd_delete")) {
            throw new RuntimeException("该用户没有权限进行资产领用归还单删除");
        }
        assetReceiveRevertService.deleteAssetReceiveRevert(AssetReceiveRevertId);
        return "{\"success\":true}";
    }

    @PostMapping(value = "update_assetreceivereverttemp")
    @ResponseBody
    public String updateAssetReceiveRevertTemp(String assetIdList, String savePlaceId) {
        logger.info("Controller: Create AssetReceiveRevertTemp" + assetIdList);
        List<String> assetIdListStr = new ArrayList<String>();
        if (!VGUtility.isEmpty(assetIdList))
            Arrays.stream(assetIdList.split(",")).forEach(arr -> assetIdListStr.add(arr));

        for (String assetReceiveUserTempId : assetIdListStr) {
            AssetReceiveUseTempDto dto = assetReceiveUseTempService.getAssetReceiveUseTempDto(assetReceiveUserTempId);
            dto.setRevertSavePlaceId(savePlaceId);
            assetReceiveUseTempService.updateAssetReceiveUseTemp(dto);
        }
        return "{\"success\":true}";
    }

    @PostMapping(value = "assetreceiverevert_datagrid")
    @ResponseBody
    public Map<String, Object> getAssetRevertByQuerysForDataGrid(AssetReceiveRevertDto assetReceiveRevertDto,
                                                                 @RequestParam(defaultValue = "1") String page,
                                                                 @RequestParam(defaultValue = "20") String rows) {
        logger.info("Controller: Get Asset By Querys {} For DataGrid", assetReceiveRevertDto);
        //TODO

        return assetReceiveRevertService.getAssetReceiveRevertByForDataGrid(new ISearchExpression() {
            @Override
            public Object change(Object... arg0) {
                CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                Root root = (Root) arg0[0];
                Predicate predicate;
                predicate = getPredicateByAssetReceiveRevertDto(builder, root, assetReceiveRevertDto);
                return predicate;
            }
        }, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)), assetReceiveRevertDto);
    }

    public Predicate getPredicateByAssetReceiveRevertDto(CriteriaBuilder builder, Root root, AssetReceiveRevertDto assetReceiveRevertDto) {
        List<Predicate> andList = new ArrayList<>();
        Predicate finalPred = null;
        if (!VGUtility.isEmpty(assetReceiveRevertDto.getAssetReceiveRevertCode()))
            andList.add(builder.equal(root.get("assetReceiveRevertCode"), assetReceiveRevertDto.getAssetReceiveRevertCode()));
        if (!VGUtility.isEmpty(assetReceiveRevertDto.getAssetReceiveRevertDepartmentID()))
            andList.add(builder.equal(root.get("assetReceiveRevertDepartmentID"), assetReceiveRevertDto.getAssetReceiveRevertDepartmentID()));
        if (!VGUtility.isEmpty(assetReceiveRevertDto.getAssetReceiveRevertUserID()))
            andList.add(builder.equal(root.get("assetReceiveRevertUserID"), assetReceiveRevertDto.getAssetReceiveRevertUserID()));
        if (!VGUtility.isEmpty(assetReceiveRevertDto.getRealrevertTime()))
            andList.add(builder.equal(root.get("realrevertTime"), assetReceiveRevertDto.getRealrevertTime()));
        if (!VGUtility.isEmpty(assetReceiveRevertDto.getCreateUserID()))
            andList.add(builder.equal(root.get("createUserID"), assetReceiveRevertDto.getCreateUserID()));
        if (!VGUtility.isEmpty(assetReceiveRevertDto.getType()))
            andList.add(builder.equal(root.get("type"), assetReceiveRevertDto.getType()));
        if (!VGUtility.isEmpty(assetReceiveRevertDto.getReceiptStatus()))
            andList.add(builder.equal(root.get("receiptStatus"), assetReceiveRevertDto.getReceiptStatus()));
        if (!VGUtility.isEmpty(assetReceiveRevertDto.getRealrevertTimeStart()))
            andList.add(builder.greaterThanOrEqualTo(root.get("realrevertTime"), VGUtility.toDateObj(assetReceiveRevertDto.getRealrevertTimeStart(), "yyyy/M/d")));
        if (!VGUtility.isEmpty(assetReceiveRevertDto.getRealrevertTimeEnd()))
            andList.add(builder.lessThanOrEqualTo(root.get("realrevertTime"), VGUtility.toDateObj(assetReceiveRevertDto.getRealrevertTimeEnd(), "yyyy/M/d")));
        if (andList.size() > 0)
            finalPred = builder.and(andList.toArray(new Predicate[andList.size()]));

        return finalPred;
    }

    @PostMapping(value = "update_assetreceiverevertstatus")
    @ResponseBody
    public String updateAssetReceiveRervertStatus(AssetReceiveRevertDto assetReceiveRevertDto) {
        logger.info("Controller: Update AssetReceiveRevertStatus" + assetReceiveRevertDto.toString());
        //AssetReceiveRevertDto dto=assetReceiveRevertService.getAssetReceiveRevertDto(assetReceiveRevertDto.getId());
        assetReceiveRevertDto.setReceiptStatus(FlowableInfo.WORKSTATUS.已审批);
        assetReceiveRevertService.updateAssetReceiveRevertStatus(assetReceiveRevertDto);
        return "{\"success\":true}";
    }
}

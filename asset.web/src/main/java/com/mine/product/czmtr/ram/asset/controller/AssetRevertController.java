package com.mine.product.czmtr.ram.asset.controller;

import com.mine.base.user.dto.UserInfoDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.*;
import com.mine.product.czmtr.ram.asset.service.IAssetBorrowTempService;
import com.mine.product.czmtr.ram.asset.service.IAssetRevertService;
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
@RequestMapping(value = "/assetrevert/")
@SessionAttributes(value = {"LoginUserInfo"})
public class AssetRevertController {

    private static final Logger logger = LoggerFactory.getLogger(AssetController.class);

    @Autowired
    private IAssetBorrowTempService assetBorrowTempService;
    @Autowired
    private IAssetRevertService assetRevertService;
    @Autowired
    private IBaseService baseService;

    @PostMapping(value = "assetreverttemp_datagrid")
    @ResponseBody
    public Map<String, Object> getAssetBorrowByQuerysForDataGrid(AssetBorrowDto assetBorrowDto,
                                                                 @RequestParam(defaultValue = "1") String page,
                                                                 @RequestParam(defaultValue = "20") String rows) {
        logger.info("Controller: Get Asset By Querys {} For DataGrid", assetBorrowDto);
        //TODO
        List<AssetBorrowTempDto> dtolist = assetBorrowTempService.getListByUserId(((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo().getId());

        ModelMap map = new ModelMap();
        map.put("rows", dtolist);
        map.put("total", dtolist.size());
        return map;
    }

    //添加归还资产
    @PostMapping(value = "assetborrowtemp_query_norevert")
    @ResponseBody
    public Map<String, Object> assetBorrowQuery(ModelMap map, AssetAssetDto asset) {
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        List<AssetBorrowTempDto> dtolist = assetBorrowTempService.getNRAsset(userInfoDto, asset);
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtolist);
        modelMap.addAttribute("total", dtolist.size());
        return modelMap;
    }

    @PostMapping(value = "assetreverttemp_query")
    @ResponseBody
    public Map<String, Object> assetRevertQuery(AssetBorrowTempDto dto, ModelMap map) {
        logger.info("Enter AssetBorrowTemp Query Page");
        List<AssetBorrowTempDto> dtolist = assetBorrowTempService.getAssetBorrowTempDtoListByRevertId(dto.getAssetRevertID());
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtolist);
        modelMap.addAttribute("total", dtolist.size());
        return modelMap;

    }

    @PostMapping(value = "create_assetrevert")
    @ResponseBody
    public ModelMap createAssetRevert(AssetRevertDto assetRevertDto, String assetBorrowTempDtoList) {
        logger.info("Controller: Create AssetRevert" + assetRevertDto.toString());
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        AssetRevertDto dto = assetRevertService.createAssetRevert(assetBorrowTempDtoList, assetRevertDto, userInfoDto);
        ModelMap map = new ModelMap();
        map.put("dto", dto);
        map.put("success", "true");
        return map;
    }

    @PostMapping(value = "create_assetreverttemp")
    @ResponseBody
    public String createAssetBorrowTemp(AssetRevertDto assetRevertDto, String assetBorrowTempIdListStr) {
        logger.info("Controller: Create AssetBorrowTemp" + assetRevertDto.toString());
        assetBorrowTempService.createAssetBorrowTempByAssetIdListStrForRevert(assetBorrowTempIdListStr, assetRevertDto);
        return "{\"success\":true}";
    }

    @PostMapping(value = "update_assetrevert")
    @ResponseBody
    public ModelMap updateAssetRevert(AssetRevertDto assetRevertDto) {
        logger.info("Controller: Update AssetRevertDto" + assetRevertDto.toString());
        AssetRevertDto dto = assetRevertService.updateAssetRevert(assetRevertDto);
        ModelMap map = new ModelMap();
        map.put("id", dto.getId());
        map.put("success", "true");
        return map;
    }

    @PostMapping(value = "delete_assetreverttemp")
    @ResponseBody
    public String deleteAssetBorrowTemp(String assetIdList, String AssetRevertID) {
        logger.info("Controller: Delete AssetBorrowTemp" + assetIdList);
        List<String> assetIdListStr = new ArrayList<String>();
        if (!VGUtility.isEmpty(assetIdList))
            Arrays.stream(assetIdList.split(",")).forEach(arr -> assetIdListStr.add(arr));

        assetBorrowTempService.deleteAssetBorrowTempByAssetRevertID(assetIdListStr, AssetRevertID);
        return "{\"success\":true}";
    }

    @PostMapping(value = "delete_assetrevert")
    @ResponseBody
    public String deleteAssetRevert(String AssetRevertId) {
        logger.info("Controller: Delete AssetRevert" + AssetRevertId);
        if (!baseService.getPermCheck("zcjyxzd_delete")) {
            throw new RuntimeException("该用户没有权限进行资产借用单删除");
        }
        assetRevertService.deleteAssetRevert(AssetRevertId);
        return "{\"success\":true}";
    }

    @PostMapping(value = "update_assetreverttemp")
    @ResponseBody
    public String updateAssetRevertTemp(String assetIdList, String savePlaceId) {
        logger.info("Controller: Create AssetRevertTemp" + assetIdList);
        List<String> assetIdListStr = new ArrayList<String>();
        if (!VGUtility.isEmpty(assetIdList))
            Arrays.stream(assetIdList.split(",")).forEach(arr -> assetIdListStr.add(arr));

        for (String assetBorrowTempId : assetIdListStr) {
            AssetBorrowTempDto dto = assetBorrowTempService.getAssetBorrowTempDto(assetBorrowTempId);
            dto.setRevertSavePlaceId(savePlaceId);
            assetBorrowTempService.updateAssetBorrowTemp(dto);
        }
        return "{\"success\":true}";
    }

    @PostMapping(value = "assetrevert_datagrid")
    @ResponseBody
    public Map<String, Object> getAssetRevertByQuerysForDataGrid(AssetRevertDto assetRevertDto,
                                                                 @RequestParam(defaultValue = "1") String page,
                                                                 @RequestParam(defaultValue = "20") String rows) {
        logger.info("Controller: Get Asset By Querys {} For DataGrid", assetRevertDto);
        //TODO

        return assetRevertService.getAssetRevertByForDataGrid(new ISearchExpression() {
            @Override
            public Object change(Object... arg0) {
                CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                Root root = (Root) arg0[0];
                Predicate predicate;
                predicate = getPredicateByAssetRevertDto(builder, root, assetRevertDto);
                return predicate;
            }
        }, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)), assetRevertDto);
    }

    public Predicate getPredicateByAssetRevertDto(CriteriaBuilder builder, Root root, AssetRevertDto assetRevertDto) {
        List<Predicate> andList = new ArrayList<>();
        Predicate finalPred = null;
        if (!VGUtility.isEmpty(assetRevertDto.getAssetrevertCode()))
            andList.add(builder.equal(root.get("assetrevertCode"), assetRevertDto.getAssetrevertCode()));
        if (!VGUtility.isEmpty(assetRevertDto.getAssetrevertDepartmentID()))
            andList.add(builder.equal(root.get("assetrevertDepartmentID"), assetRevertDto.getAssetrevertDepartmentID()));
        if (!VGUtility.isEmpty(assetRevertDto.getAssetrevertUserID()))
            andList.add(builder.equal(root.get("assetrevertUserID"), assetRevertDto.getAssetrevertUserID()));
        if (!VGUtility.isEmpty(assetRevertDto.getRealrevertTime()))
            andList.add(builder.equal(root.get("realrevertTime"), assetRevertDto.getRealrevertTime()));
        if (!VGUtility.isEmpty(assetRevertDto.getCreateUserID()))
            andList.add(builder.equal(root.get("createUserID"), assetRevertDto.getCreateUserID()));
        if (!VGUtility.isEmpty(assetRevertDto.getType()))
            andList.add(builder.equal(root.get("type"), assetRevertDto.getType()));
        if (!VGUtility.isEmpty(assetRevertDto.getReceiptStatus()))
            andList.add(builder.equal(root.get("receiptStatus"), assetRevertDto.getReceiptStatus()));
        if (!VGUtility.isEmpty(assetRevertDto.getRealrevertTimeStart()))
            andList.add(builder.greaterThanOrEqualTo(root.get("realrevertTime"), VGUtility.toDateObj(assetRevertDto.getRealrevertTimeStart(), "yyyy/M/d")));
        if (!VGUtility.isEmpty(assetRevertDto.getRealrevertTimeEnd()))
            andList.add(builder.lessThanOrEqualTo(root.get("realrevertTime"), VGUtility.toDateObj(assetRevertDto.getRealrevertTimeEnd(), "yyyy/M/d")));
        if (andList.size() > 0)
            finalPred = builder.and(andList.toArray(new Predicate[andList.size()]));

        return finalPred;
    }

    @PostMapping(value = "update_assetrevertstatus")
    @ResponseBody
    public String updateAssetRevertStatus(AssetRevertDto assetRevertDto) {
        logger.info("Controller: Update AssetRevertStatus" + assetRevertDto.toString());
        assetRevertDto.setReceiptStatus(FlowableInfo.WORKSTATUS .values()[assetRevertDto.getReceiptIndex()]);
        assetRevertService.updateAssetRevertStatus(assetRevertDto);
        return "{\"success\":true}";
    }

}

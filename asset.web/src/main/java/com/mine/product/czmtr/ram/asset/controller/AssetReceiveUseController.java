package com.mine.product.czmtr.ram.asset.controller;

import com.mine.base.user.dto.UserInfoDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.AssetReceiveUseDto;
import com.mine.product.czmtr.ram.asset.dto.AssetReceiveUseTempDto;
import com.mine.product.czmtr.ram.asset.dto.FlowableInfo;
import com.mine.product.czmtr.ram.asset.service.IAssetReceiveUseService;
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
@RequestMapping(value = "/assetreceiveuse/")
@SessionAttributes(value = {"LoginUserInfo"})
public class AssetReceiveUseController {

    private static final Logger logger = LoggerFactory.getLogger(AssetController.class);
    @Autowired
    private IAssetReceiveUseService assetReceiveUseService;
    @Autowired
    private IAssetReceiveUseTempService assetReceiveUseTempService;
    @Autowired
    private IBaseService baseService;

    @PostMapping(value = "create_assetreceiveuse")
    @ResponseBody
    public ModelMap createAssetReceiveUse(AssetReceiveUseDto assetReceiveUseDto, String assetReceiveUseTempDtoList) {
        logger.info("Controller: Create AssetReceiveUse" + assetReceiveUseDto.toString());
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        AssetReceiveUseDto dto = assetReceiveUseService.createAssetReceiveUse(assetReceiveUseTempDtoList, assetReceiveUseDto, userInfoDto);
        ModelMap map = new ModelMap();
        map.put("dto", dto);
        map.put("success", "true");
        return map;
    }

    @PostMapping(value = "update_assetreceiveuse")
    @ResponseBody
    public ModelMap updateAssetReceiveUse(AssetReceiveUseDto assetReceiveUseDto) {
        logger.info("Controller: Update assetReceiveUse" + assetReceiveUseDto.toString());
        AssetReceiveUseDto dto = assetReceiveUseService.updateAssetReceiveUse(assetReceiveUseDto);
        ModelMap map = new ModelMap();
        map.put("id", dto.getId());
        map.put("success", "true");
        return map;
    }

    @PostMapping(value = "create_assetreceiveusetemp")
    @ResponseBody
    public String createAssetReceiveUseTemp(AssetReceiveUseDto assetReceiveUseDto, String assetIdList) {
        logger.info("Controller: Create assetReceiveUseDto" + assetReceiveUseDto.toString());
        assetReceiveUseTempService.createAssetReceiveUseTempByassetIdListStr(assetIdList, assetReceiveUseDto);
        return "{\"success\":true}";
    }

    @PostMapping(value = "delete_assetreceiveusetemp")
    @ResponseBody
    public String deleteAssetReceiveUseTemp(String assetReceiveUseTempIdList, String AssetReceiveUseID) {
        logger.info("Controller: Delete AssetReceiveUseTemp" + assetReceiveUseTempIdList);
        List<String> assetReceiveUseTempIdListStr = new ArrayList<String>();
        if (!VGUtility.isEmpty(assetReceiveUseTempIdList))
            Arrays.stream(assetReceiveUseTempIdList.split(",")).forEach(arr -> assetReceiveUseTempIdListStr.add(arr));

        assetReceiveUseTempService.deleteAssetReceiveUseTempByAssetReceiveUseID(assetReceiveUseTempIdListStr, AssetReceiveUseID);
        return "{\"success\":true}";
    }

    @PostMapping(value = "update_assetreceiveusetemp")
    @ResponseBody
    public String updateAssetReceiveUseTemp(String assetReceiveUseTempIdList, String savePlaceId) {
        logger.info("Controller: Create AssetReceiveUseTemp" + assetReceiveUseTempIdList);
        List<String> assetReceiveUseTempIdListStr = new ArrayList<String>();
        if (!VGUtility.isEmpty(assetReceiveUseTempIdList))
            Arrays.stream(assetReceiveUseTempIdList.split(",")).forEach(arr -> assetReceiveUseTempIdListStr.add(arr));

        for (String assetReceiveUseTempId : assetReceiveUseTempIdListStr) {
            AssetReceiveUseTempDto dto = assetReceiveUseTempService.getAssetReceiveUseTempDto(assetReceiveUseTempId);
            dto.setSavePlaceId(savePlaceId);
            assetReceiveUseTempService.updateAssetReceiveUseTemp(dto);
        }
        return "{\"success\":true}";
    }

    @PostMapping(value = "delete_assetreceiveuse")
    @ResponseBody
    public String deleteAssetReceiveUse(String assetReceiveUseID) {
        logger.info("Controller: Delete AssetReceiveUse" + assetReceiveUseID);
        if (!baseService.getPermCheck("zclyxzd_create")) {
            throw new RuntimeException("该用户没有权限进行资产领用单创建");
        }
        assetReceiveUseService.deleteAssetReceiveUse(assetReceiveUseID);
        return "{\"success\":true}";
    }

    @PostMapping(value = "update_assetreceiveusestatus")
    @ResponseBody
    public String updateAssetReceiveUseStatus(AssetReceiveUseDto assetReceiveUseDto) {
        logger.info("Controller: Update assetReceiveUseStatus" + assetReceiveUseDto.toString());
        AssetReceiveUseDto dto = assetReceiveUseService.getAssetReceiveUseDto(assetReceiveUseDto.getId());
        dto.setReceiptStatus(FlowableInfo.WORKSTATUS.values()[assetReceiveUseDto.getReceiptIndex()]);
        assetReceiveUseService.updateAssetReceiveUseStatus(dto);
        return "{\"success\":true}";
    }

    @PostMapping(value = "assetreceiveusetemp_query")
    @ResponseBody
    public Map<String, Object> assetReceiveUseTempQuery(AssetReceiveUseTempDto dto, ModelMap map) {
        logger.info("Enter AssetReceiveUseTemp Query Page");
        List<AssetReceiveUseTempDto> dtolist = assetReceiveUseTempService.getAssetReceiveUseTempDtoList(dto.getAssetReceiveUseID());
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtolist);
        modelMap.addAttribute("total", dtolist.size());
        return modelMap;

    }

    @PostMapping(value = "assetreceiveuse_datagrid")
    @ResponseBody
    public Map<String, Object> getAssetReceiveUseByQuerysForDataGrid(AssetReceiveUseDto assetReceiveUseDto,
                                                                     @RequestParam(defaultValue = "1") String page,
                                                                     @RequestParam(defaultValue = "20") String rows) {
        logger.info("Controller: Get Asset By Querys {} For DataGrid", assetReceiveUseDto);
        //TODO

        return assetReceiveUseService.getAssetReceiveUseByForDataGrid(new ISearchExpression() {
            @Override
            public Object change(Object... arg0) {
                CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                Root root = (Root) arg0[0];
                Predicate predicate;

                predicate = getPredicateByAssetReceiveUseDto(builder, root, assetReceiveUseDto);

                return predicate;
            }
        }, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)), assetReceiveUseDto);
    }

    public Predicate getPredicateByAssetReceiveUseDto(CriteriaBuilder builder, Root root, AssetReceiveUseDto assetReceiveUseDto) {
        List<Predicate> andList = new ArrayList<>();
        Predicate finalPred = null;
        if (!VGUtility.isEmpty(assetReceiveUseDto.getAssetReceiveUseCode()))
            andList.add(builder.equal(root.get("assetReceiveUseCode"), assetReceiveUseDto.getAssetReceiveUseCode()));
        if (!VGUtility.isEmpty(assetReceiveUseDto.getAssetReceiveUseDepartmentID()))
            andList.add(builder.equal(root.get("assetReceiveUseDepartmentID"), assetReceiveUseDto.getAssetReceiveUseDepartmentID()));
        if (!VGUtility.isEmpty(assetReceiveUseDto.getAssetReceiveUseUserID()))
            andList.add(builder.equal(root.get("assetReceiveUseUserID"), assetReceiveUseDto.getAssetReceiveUseUserID()));
        if (!VGUtility.isEmpty(assetReceiveUseDto.getReason()))
            andList.add(builder.equal(root.get("assetID"), assetReceiveUseDto.getReason()));
        if (!VGUtility.isEmpty(assetReceiveUseDto.getReceiveTime()))
            andList.add(builder.equal(root.get("receiveTime"), assetReceiveUseDto.getReceiveTime()));
        if (!VGUtility.isEmpty(assetReceiveUseDto.getCreateUserID()))
            andList.add(builder.equal(root.get("createUserID"), assetReceiveUseDto.getCreateUserID()));
        if (!VGUtility.isEmpty(assetReceiveUseDto.getType()))
            andList.add(builder.equal(root.get("type"), assetReceiveUseDto.getType()));
        if (!VGUtility.isEmpty(assetReceiveUseDto.getReceiptStatus()))
            andList.add(builder.equal(root.get("receiptStatus"), assetReceiveUseDto.getReceiptStatus()));
        if (!VGUtility.isEmpty(assetReceiveUseDto.getReceiveTimeStart()))
            andList.add(builder.greaterThanOrEqualTo(root.get("receiveTime"), VGUtility.toDateObj(assetReceiveUseDto.getReceiveTimeStart(), "yyyy/M/d")));
        if (!VGUtility.isEmpty(assetReceiveUseDto.getReceiveTimeEnd()))
            andList.add(builder.lessThanOrEqualTo(root.get("receiveTime"), VGUtility.toDateObj(assetReceiveUseDto.getReceiveTimeEnd(), "yyyy/M/d")));
        if (andList.size() > 0)
            finalPred = builder.and(andList.toArray(new Predicate[andList.size()]));

        return finalPred;
    }
}

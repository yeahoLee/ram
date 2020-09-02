package com.mine.product.czmtr.ram.asset.controller;

import com.mine.base.user.dto.UserInfoDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.AssetAllocationDto;
import com.mine.product.czmtr.ram.asset.dto.AssetAllocationTempDto;
import com.mine.product.czmtr.ram.asset.dto.FlowableInfo;
import com.mine.product.czmtr.ram.asset.service.IAssetAllocationService;
import com.mine.product.czmtr.ram.asset.service.IAssetAllocationTempService;
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
@RequestMapping(value = "/assetallocation/")
@SessionAttributes(value = {"LoginUserInfo"})
public class AssetAllocationController {

    private static final Logger logger = LoggerFactory.getLogger(AssetController.class);

    @Autowired
    private IAssetAllocationService assetAllocationService;
    @Autowired
    private IAssetAllocationTempService assetAllocationTempService;
    @Autowired
    private IBaseService baseService;


    @PostMapping(value = "create_assetalloction")
    @ResponseBody
    public ModelMap createAssetAllocation(AssetAllocationDto assetAllocationDto, String assetAllocationTempDtoList) {
        logger.info("Controller: Create AssetAllocation");
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        AssetAllocationDto dto = assetAllocationService.createAssetAllocation(assetAllocationTempDtoList, assetAllocationDto, userInfoDto);
        ModelMap map = new ModelMap();
        map.put("success", true);
        map.put("dto", dto);
        return map;
    }

    @PostMapping(value = "update_assetalloction")
    @ResponseBody
    public ModelMap updateAssetAllocation(AssetAllocationDto assetAllocationDto) {
        logger.info("Controller: Update AssetAllocation");
        AssetAllocationDto dto = assetAllocationService.updateAssetAllocatiom(assetAllocationDto);
        ModelMap map = new ModelMap();
        map.put("success", true);
        map.put("id", dto.getId());
        return map;
    }

    @PostMapping(value = "delete_assetalloction")
    @ResponseBody
    public String deleteAssetAllocation(String AssetAllocationId) {
        logger.info("Controller: Delete AssetAllocation");
        if (!baseService.getPermCheck("zcdbxzd_delete")) {
            throw new RuntimeException("该用户没有权限进行资产调拨单删除");
        }
        assetAllocationService.deleteAssetAllocation(AssetAllocationId);
        return "{\"success\":true}";
    }

    @PostMapping(value = "assetallocation_datagrid")
    @ResponseBody
    public Map<String, Object> getAssetBorrowByQuerysForDataGrid(AssetAllocationDto assetAllocationDto,
                                                                 @RequestParam(defaultValue = "1") String page,
                                                                 @RequestParam(defaultValue = "20") String rows) {
        logger.info("Controller: Get Asset By Querys {} For DataGrid", assetAllocationDto);
        //TODO

        return assetAllocationService.getAssetAllocationDtoByQuerysForDataGrid(new ISearchExpression() {
            @Override
            public Object change(Object... arg0) {
                CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                Root root = (Root) arg0[0];
                Predicate predicate;

                predicate = getPredicateByAssetAllocationDto(builder, root, assetAllocationDto);

                return predicate;
            }
        }, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)));
    }

    public Predicate getPredicateByAssetAllocationDto(CriteriaBuilder builder, Root root, AssetAllocationDto assetAllocationDto) {
        List<Predicate> andList = new ArrayList<>();
        Predicate finalPred = null;
        if (!VGUtility.isEmpty(assetAllocationDto.getAssetAllocationCode()))
            andList.add(builder.equal(root.get("assetAllocationCode"), assetAllocationDto.getAssetAllocationCode()));
        if (!VGUtility.isEmpty(assetAllocationDto.getCallInAssetManagerId()))
            andList.add(builder.equal(root.get("callInAssetManagerId"), assetAllocationDto.getCallInAssetManagerId()));
        if (!VGUtility.isEmpty(assetAllocationDto.getCallInDepartmentId()))
            andList.add(builder.equal(root.get("callInDepartmentId"), assetAllocationDto.getCallInDepartmentId()));
        if (!VGUtility.isEmpty(assetAllocationDto.getCallOutDepartmentId()))
            andList.add(builder.equal(root.get("callOutDepartmentId"), assetAllocationDto.getCallOutDepartmentId()));
        if (!VGUtility.isEmpty(assetAllocationDto.getReason()))
            andList.add(builder.equal(root.get("reason"), assetAllocationDto.getReason()));
        if (!VGUtility.isEmpty(assetAllocationDto.getCreateUserID()))
            andList.add(builder.equal(root.get("createUserID"), assetAllocationDto.getCreateUserID()));
        if (!VGUtility.isEmpty(assetAllocationDto.getReceiptStatus()))
            andList.add(builder.equal(root.get("receiptStatus"), assetAllocationDto.getReceiptStatus()));
        if (!VGUtility.isEmpty(assetAllocationDto.getCreateTimestampStart()))
            andList.add(builder.greaterThanOrEqualTo(root.get("createTimestamp"), VGUtility.toDateObj(assetAllocationDto.getCreateTimestampStart(), "yyyy/M/d")));
        if (!VGUtility.isEmpty(assetAllocationDto.getCreateTimestampEnd()))
            andList.add(builder.lessThanOrEqualTo(root.get("createTimestamp"), VGUtility.toDateObj(assetAllocationDto.getCreateTimestampEnd(), "yyyy/M/d")));
        if (andList.size() > 0)
            finalPred = builder.and(andList.toArray(new Predicate[andList.size()]));

        return finalPred;
    }

    @PostMapping(value = "assetallocationtemp_query")
    @ResponseBody
    public Map<String, Object> assetAllocationQuery(AssetAllocationTempDto dto, ModelMap map) {
        logger.info("Enter AssetAllocationTemp Query Page");

        List<AssetAllocationTempDto> dtolist = assetAllocationTempService.getAssetAllocationTempDtoList(dto.getAssetAllocationId());

        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtolist);
        modelMap.addAttribute("total", dtolist.size());
        return modelMap;
    }

    @PostMapping(value = "create_assetallocationtemp")
    @ResponseBody
    public String createAssetAllocationTemp(AssetAllocationDto assetAllocationDto, String assetIdList) {
        logger.info("Controller: Create AssetAllocationTempDto");
        assetAllocationTempService.createAssetAllocationTempByassetIdListStr(assetIdList, assetAllocationDto);
        return "{\"success\":true}";
    }

    @PostMapping(value = "delete_assetallocationtemp")
    @ResponseBody
    public String deleteAssetAllocationTemp(String assetIdList, String AssetAllocationID) {
        logger.info("Controller: Delete AssetAllocationTemp");
        List<String> assetIdListStr = new ArrayList<String>();
        if (!VGUtility.isEmpty(assetIdList))
            Arrays.stream(assetIdList.split(",")).forEach(arr -> assetIdListStr.add(arr));

        assetAllocationTempService.deleteAssetAllocationTempByAssetAllocationID(assetIdListStr, AssetAllocationID);
        return "{\"success\":true}";
    }

    @PostMapping(value = "update_assetallocatiomstatus")
    @ResponseBody
    public String updateAssetAllocationStatus(AssetAllocationDto assetAllocationDto) {
        logger.info("Controller: Update AssetAllocationStatus");
        AssetAllocationDto dto = assetAllocationService.getAssetAllocationDtoById(assetAllocationDto.getId());
        dto.setReceiptStatus(FlowableInfo.WORKSTATUS.values()[assetAllocationDto.getReceiptStatusStr()]);
        assetAllocationService.updateAssetAllocationStatus(dto);
        return "{\"success\":true}";
    }
}

package com.mine.product.czmtr.ram.asset.controller;

import com.mine.base.user.dto.UserInfoDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.AssetBorrowDto;
import com.mine.product.czmtr.ram.asset.dto.AssetBorrowTempDto;
import com.mine.product.czmtr.ram.asset.dto.FlowableInfo;
import com.mine.product.czmtr.ram.asset.service.IAssetBorrowService;
import com.mine.product.czmtr.ram.asset.service.IAssetBorrowTempService;
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
@RequestMapping(value = "/assetborrow/")
@SessionAttributes(value = {"LoginUserInfo"})
public class AssetBorrowController {

    private static final Logger logger = LoggerFactory.getLogger(AssetController.class);

    @Autowired
    private IAssetBorrowService assetBorrowService;
    @Autowired
    private IAssetBorrowTempService assetBorrowTempService;
    @Autowired
    private IBaseService baseService;


    @PostMapping(value = "create_assetborrow")
    @ResponseBody
    public ModelMap createAssetBorrow(AssetBorrowDto assetBorrowDto, String assetBorrowTempDtoList) {
        logger.info("Controller: Create AssetBorrow" + assetBorrowDto.toString());
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        AssetBorrowDto dto = assetBorrowService.createAssetBorrow(assetBorrowTempDtoList, assetBorrowDto, userInfoDto);
        ModelMap map = new ModelMap();
        map.put("dto", dto);
        map.put("success", "true");
        return map;
    }

    @PostMapping(value = "update_assetborrow")
    @ResponseBody
    public ModelMap updateAssetBorrow(AssetBorrowDto assetBorrowDto) {
        logger.info("Controller: Update AssetBorrow" + assetBorrowDto.toString());
        AssetBorrowDto dto = assetBorrowService.updateAssetBorrow(assetBorrowDto);
        ModelMap map = new ModelMap();
        map.put("id", dto.getId());
        map.put("success", "true");
        return map;
    }

    @PostMapping(value = "query_assetborrow")
    @ResponseBody
    public Map<String, Object> query_assetBorrow(String createUserID) {
        logger.info("Controller: Query AssetBorrow" + createUserID);
        return assetBorrowService.getAssetBorrowDtoByQuerysForDataGrid(new ISearchExpression() {
            @Override
            public Object change(Object... arg0) {
                CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                Root root = (Root) arg0[0];
                Predicate finalPred = builder.equal(root.get("createUserID"), createUserID);
                return finalPred;
            }
        }, null);
    }

    @PostMapping(value = "assetborrow_datagrid")
    @ResponseBody
    public Map<String, Object> getAssetBorrowByQuerysForDataGrid(AssetBorrowDto assetBorrowDto,
                                                                 @RequestParam(defaultValue = "1") String page,
                                                                 @RequestParam(defaultValue = "20") String rows) {
        logger.info("Controller: Get Asset By Querys {} For DataGrid", assetBorrowDto);
        //TODO

        return assetBorrowService.getAssetBorrowByForDataGrid(new ISearchExpression() {
            @Override
            public Object change(Object... arg0) {
                CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                Root root = (Root) arg0[0];
                Predicate predicate;

                predicate = getPredicateByAssetBorrowDto(builder, root, assetBorrowDto);

                return predicate;
            }
        }, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)), assetBorrowDto);
    }

    public Predicate getPredicateByAssetBorrowDto(CriteriaBuilder builder, Root root, AssetBorrowDto assetBorrowDto) {
        List<Predicate> andList = new ArrayList<>();
        Predicate finalPred = null;
        if (!VGUtility.isEmpty(assetBorrowDto.getAssetborrowCode()))
            andList.add(builder.equal(root.get("assetborrowCode"), assetBorrowDto.getAssetborrowCode()));
        if (!VGUtility.isEmpty(assetBorrowDto.getAssetborrowDepartmentID()))
            andList.add(builder.equal(root.get("assetborrowDepartmentID"), assetBorrowDto.getAssetborrowDepartmentID()));
        if (!VGUtility.isEmpty(assetBorrowDto.getAssetborrowUserID()))
            andList.add(builder.equal(root.get("assetborrowUserID"), assetBorrowDto.getAssetborrowUserID()));
        if (!VGUtility.isEmpty(assetBorrowDto.getReason()))
            andList.add(builder.equal(root.get("reason"), assetBorrowDto.getReason()));
        if (!VGUtility.isEmpty(assetBorrowDto.getRevertTime()))
            andList.add(builder.equal(root.get("revertTime"), VGUtility.toDateObj(assetBorrowDto.getRevertTime(), "yyyy/M/d")));
        if (!VGUtility.isEmpty(assetBorrowDto.getCreateUserID()))
            andList.add(builder.equal(root.get("createUserID"), assetBorrowDto.getCreateUserID()));
        if (!VGUtility.isEmpty(assetBorrowDto.getType()))
            andList.add(builder.equal(root.get("type"), assetBorrowDto.getType()));
        if (!VGUtility.isEmpty(assetBorrowDto.getReceiptStatus()))
            andList.add(builder.equal(root.get("receiptStatus"), assetBorrowDto.getReceiptStatus()));
        if (!VGUtility.isEmpty(assetBorrowDto.getCreateTimestampStart()))
            andList.add(builder.greaterThanOrEqualTo(root.get("createTimestamp"), VGUtility.toDateObj(assetBorrowDto.getCreateTimestampStart(), "yyyy/M/d")));
        if (!VGUtility.isEmpty(assetBorrowDto.getCreateTimestampEnd()))
            andList.add(builder.lessThanOrEqualTo(root.get("createTimestamp"), VGUtility.toDateObj(assetBorrowDto.getCreateTimestampEnd(), "yyyy/M/d")));
        if (andList.size() > 0)
            finalPred = builder.and(andList.toArray(new Predicate[andList.size()]));

        return finalPred;
    }

    @PostMapping(value = "assetborrowtemp_query")
    @ResponseBody
    public Map<String, Object> assetBorrowQuery(AssetBorrowTempDto dto, ModelMap map) {
        logger.info("Enter AssetBorrowTemp Query Page");

        List<AssetBorrowTempDto> dtolist = assetBorrowTempService.getAssetBorrowTempDtoList(dto.getAssetBorrowID());

        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("rows", dtolist);
        modelMap.addAttribute("total", dtolist.size());
        return modelMap;
    }

    @PostMapping(value = "create_assetborrowtemp")
    @ResponseBody
    public String createAssetBorrowTemp(AssetBorrowDto assetBorrowDto, String assetIdList) {
        logger.info("Controller: Create AssetBorrowTemp" + assetBorrowDto.toString());
        assetBorrowTempService.createAssetBorrowTempByassetIdListStr(assetIdList, assetBorrowDto);
        return "{\"success\":true}";
    }

    @PostMapping(value = "delete_assetborrowtemp")
    @ResponseBody
    public String deleteAssetBorrowTemp(String assetIdList, String AssetBorrowID) {
        logger.info("Controller: Delete AssetBorrowTemp" + assetIdList);
        List<String> assetIdListStr = new ArrayList<String>();
        if (!VGUtility.isEmpty(assetIdList))
            Arrays.stream(assetIdList.split(",")).forEach(arr -> assetIdListStr.add(arr));

        assetBorrowTempService.deleteAssetBorrowTempByAssetBorrowID(assetIdListStr, AssetBorrowID);
        return "{\"success\":true}";
    }

    @PostMapping(value = "update_assetborrowtemp")
    @ResponseBody
    public String updateAssetBorrowTemp(String assetIdList, String savePlaceId) {
        logger.info("Controller: Create AssetBorrowTemp" + assetIdList);
        List<String> assetIdListStr = new ArrayList<String>();
        if (!VGUtility.isEmpty(assetIdList))
            Arrays.stream(assetIdList.split(",")).forEach(arr -> assetIdListStr.add(arr));

        for (String assetBorrowTempId : assetIdListStr) {
            AssetBorrowTempDto dto = assetBorrowTempService.getAssetBorrowTempDto(assetBorrowTempId);
            dto.setSavePlaceId(savePlaceId);
            assetBorrowTempService.updateAssetBorrowTemp(dto);
        }
        return "{\"success\":true}";
    }

    @PostMapping(value = "delete_assetborrow")
    @ResponseBody
    public String deleteAssetBorrow(String AssetBorrowId) {
        logger.info("Controller: Delete AssetBorrow" + AssetBorrowId);
        if (!baseService.getPermCheck("zcjyxzd_delete")) {
            throw new RuntimeException("该用户没有权限进行资产借用单删除");
        }
        assetBorrowService.deleteAssetBorrow(AssetBorrowId);
        return "{\"success\":true}";
    }

    @PostMapping(value = "update_assetborrowstatus")
    @ResponseBody
    public String updateAssetBorrowStatus(AssetBorrowDto assetBorrowDto) {
        logger.info("Controller: Update AssetBorrowStatus" + assetBorrowDto.toString());
        AssetBorrowDto dto = assetBorrowService.getAssetBorrowDto(assetBorrowDto.getId());
        dto.setReceiptStatus(FlowableInfo.WORKSTATUS.values()[assetBorrowDto.getReceiptIndex()]);
        assetBorrowService.updateAssetBorrowStatus(dto);
        return "{\"success\":true}";
    }
}

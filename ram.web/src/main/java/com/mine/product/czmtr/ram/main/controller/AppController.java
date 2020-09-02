package com.mine.product.czmtr.ram.main.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mine.base.user.dto.DeptInfoDto;
import com.mine.base.user.dto.UserInfoDto;
import com.mine.base.user.service.IUserService;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.AssetAssetDto;
import com.mine.product.czmtr.ram.asset.dto.AssetInventoryTempDto;
import com.mine.product.czmtr.ram.asset.dto.MyAssetInventoryDto;
import com.mine.product.czmtr.ram.asset.service.IAssetInventoryService;
import com.mine.product.czmtr.ram.asset.service.IAssetInventoryTempService;
import com.mine.product.czmtr.ram.asset.service.IAssetService;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.VGUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 杨杰
 * @date 2020/1/15 10:00
 */
@Controller
@RequestMapping(value = "/app/")
public class AppController {
    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    @Autowired
    private IUserService userService;
    @Autowired
    private IAssetService assetService;
    @Autowired
    private IBaseService baseService;
    @Autowired
    private IAssetInventoryService inventoryService;
    @Autowired
    private IAssetInventoryTempService inventoryTempService;

    /**
     * 根据用户名获取用户信息
     *
     * @param userName
     * @return
     */
    @GetMapping(value = "get_userinfo_username", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getUserInfo(@RequestParam String userName) {
        logger.info("AppController: Get UserInfo by {}", "[" + userName + "]");
        if (!VGUtility.isEmpty(userName)) {
            List<String> deptIdList = new ArrayList<String>();
            List<DeptInfoDto> resultDtoList = new ArrayList<DeptInfoDto>();

            UserInfoDto userInfo = userService.getUserInfoByUserName("RAM_WEB", userName);
            if (VGUtility.isEmpty(userInfo)) {
                return JSON.toJSONString("");
            }

            List<Map<String, Object>> deptMapList = userInfo.getDeptList();
            for (Map<String, Object> deptMap : deptMapList) {
                String deptId = (String) deptMap.get("deptId");
                if (VGUtility.isEmpty(deptId))
                    continue;
                deptIdList.add(deptId);
            }

            List<DeptInfoDto> deptInfoDtoList = userService.getDeptInfo("{}", null, null).getRowData();
            for (DeptInfoDto deptInfoDto : deptInfoDtoList) {
                if (deptIdList.contains(deptInfoDto.getId())) {
                    resultDtoList.add(deptInfoDto);
                }
            }

            Map<String, Object> deptInfoMap = new HashMap<String, Object>();
            if (resultDtoList.size() > 0)
                deptInfoMap.put("loginDeptDto", resultDtoList.get(0));
            deptInfoMap.put("deptInfoList", resultDtoList);
            userInfo.setPropertyMap(deptInfoMap);

            return JSON.toJSONString(userInfo);
        }
        return JSON.toJSONString("");
    }

    /**
     * 查询资产
     *
     * @param assetDto
     * @param userName
     * @param showType 用于判断用于什么查询,默认为0:表示会根据当前登陆人角色加上查询条件查询,
     *                 其他值表示只根据查询条件(一般用于资产盘点)
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "asset_datagrid")
    @ResponseBody
    public Map<String, Object> getAssetByQuerysForDataGrid(AssetAssetDto assetDto,
                                                           String userName, @RequestParam(required = false, defaultValue = "0") String showType,
                                                           @RequestParam(defaultValue = "1") String page,
                                                           @RequestParam(defaultValue = "20") String rows) {
        logger.info("AppController: Get Asset By Querys {} For DataGrid", "[" + assetDto + "]");

        Map<String, Object> resultMap = assetService.getAssetByQuerysForDataGrid("0", new ISearchExpression() {
            @Override
            public Object change(Object... arg0) {
                CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                Root root = (Root) arg0[0];
                Predicate predicate = getPredicateByAssetDto(builder, root, assetDto, showType, userName);

                return predicate;
            }
        }, null);
        boolean flag = false;
        List<AssetAssetDto> assetDtoList = (List<AssetAssetDto>) resultMap.get("rows");
        if (assetDtoList.size() > 0) {
            if (!VGUtility.isEmpty(assetDto.getAssetChsName()))
                assetDtoList = assetDtoList.stream().filter(a -> Objects.nonNull(a.getCombinationAssetName()))
                        .filter(a -> a.getCombinationAssetName().contains(assetDto.getAssetChsName()))
                        .collect(Collectors.toList());

            if (!VGUtility.isEmpty(assetDto.getManageDeptId()))
                assetDtoList = assetDtoList.stream().filter(a -> Objects.nonNull(a.getManageDeptId()))
                        .filter(a -> a.getManageDeptId().contains(assetDto.getManageDeptId()))
                        .collect(Collectors.toList());

            if (!VGUtility.isEmpty(assetDto.getManagerId()))
                assetDtoList = assetDtoList.stream().filter(a -> Objects.nonNull(a.getManagerId()))
                        .filter(a -> a.getManagerId().contains(assetDto.getManagerId()))
                        .collect(Collectors.toList());

            if (assetDtoList.size() > 0) {
                flag = true;
            }
        }
        if (assetDtoList.size() >= Integer.parseInt(rows))
            resultMap.put("rows", assetDtoList.subList((Integer.parseInt(page) - 1) * Integer.parseInt(rows), Integer.parseInt(page) * Integer.parseInt(rows) <= assetDtoList.size() ? Integer.parseInt(page) * Integer.parseInt(rows) : assetDtoList.size()));
        else
            resultMap.put("rows", assetDtoList);

        resultMap.put("total", assetDtoList.size());
        resultMap.put("flag", flag);

        return resultMap;
    }

    private Predicate getPredicateByAssetDto(CriteriaBuilder builder, Root root, AssetAssetDto assetDto, String showType, String userName) {
        Predicate finalPred = null;
        List<Predicate> andList = new ArrayList<>();

        if (!VGUtility.isEmpty(assetDto.getAssetCode()))
            andList.add(builder.like(root.get("assetCode"), "%" + assetDto.getAssetCode() + "%"));
        if (!VGUtility.isEmpty(assetDto.getAssetStatus())) {
            if (assetDto.getAssetStatus().contains(",")) {
                Path<Object> path = root.get("assetStatus");
                CriteriaBuilder.In<Object> in = builder.in(path);
                for (String str : assetDto.getAssetStatus().split(",")) {
                    in.value(IAssetService.ASSET_STATUS.values()[VGUtility.toInteger(str)]);
                }
                andList.add(in);
            } else {
                andList.add(builder.equal(root.get("assetStatus"), IAssetService.ASSET_STATUS.values()[VGUtility.toInteger(assetDto.getAssetStatus())]));
            }
        }
        //现资产类别是根据资产编码分类的,故前台传递资产类别时,是根据资产编码进行查询的
        if (!VGUtility.isEmpty(assetDto.getAssetType()))
            andList.add(builder.equal(root.get("materialCode"), assetDto.getAssetType()));

        UserInfoDto userInfoDto = baseService.getUserInfoDtoByUserName(userName);
        if (!VGUtility.isEmpty(userInfoDto)) {
            boolean flag = baseService.getUserIDByRoleCode("admin", userInfoDto.getId());
            if (showType.equals("0")) {
                //如果当前登陆人有系统管理员的权限,则查询所有资产
                if (flag == false) {
                    //需判断当前登陆人是否是部门领导,如果是部门领导,则可以查询出当前部门所有资产管理员的资产
                    List<UserInfoDto> userlist = baseService.getUserInfoDtoByDeptSetId(userInfoDto.getId());
                    if (userlist.size() > 0) {
                        Path<Object> path = root.get("managerId");
                        CriteriaBuilder.In<Object> in = builder.in(path);
                        for (UserInfoDto dto : userlist) {
                            in.value(dto.getId());
                        }
                        andList.add(in);
                    } else {
                        andList.add(builder.equal(root.get("managerId"), userInfoDto.getId()));
                    }
                }
            } else {
                if (!VGUtility.isEmpty(assetDto.getManageDeptId()))
                    andList.add(builder.equal(root.get("manageDeptId"), assetDto.getManageDeptId()));
                if (!VGUtility.isEmpty(assetDto.getManagerId()))
                    andList.add(builder.equal(root.get("managerId"), assetDto.getManagerId()));
            }
        } else {
            if (!VGUtility.isEmpty(assetDto.getManageDeptId()))
                andList.add(builder.equal(root.get("manageDeptId"), assetDto.getManageDeptId()));
            if (!VGUtility.isEmpty(assetDto.getManagerId()))
                andList.add(builder.equal(root.get("managerId"), assetDto.getManagerId()));
        }

        if (!VGUtility.isEmpty(assetDto.getUseDeptId()))
            andList.add(builder.equal(root.get("useDeptId"), assetDto.getUseDeptId()));
        if (!VGUtility.isEmpty(assetDto.getUserId()))
            andList.add(builder.equal(root.get("userId"), assetDto.getUserId()));
        if (!VGUtility.isEmpty(assetDto.getSavePlaceId()))
            andList.add(builder.equal(root.get("savePlaceId"), assetDto.getSavePlaceId()));
        if (!VGUtility.isEmpty(assetDto.getMaterialCode()))
            andList.add(builder.equal(root.get("materialCode"), assetDto.getMaterialCode()));
        if (!VGUtility.isEmpty(assetDto.getDeActiveTimeStr())) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.YEAR, -1);
            Date y = c.getTime();
            andList.add(builder.lessThanOrEqualTo(root.get("deActiveTime"), y));
        }

        if (andList.size() > 0)
            finalPred = builder.and(andList.toArray(new Predicate[andList.size()]));

        return finalPred;
    }

    /**
     * 查询我的盘点单 MyAssetinventory datagrid
     *
     * @param dto
     * @param page
     * @param rows
     * @return
     */
    @PostMapping(value = "myinventory_datagrid")
    @ResponseBody
    public Map<String, Object> getAssetInventoryByQuerysForDataGrid(MyAssetInventoryDto dto,
                                                                    @RequestParam(defaultValue = "1") String page, @RequestParam(defaultValue = "20") String rows) {
        logger.info("AppController: Get AssetSequestration ForDataGrid" + dto.toString());
        return inventoryService.getMyAssetInventory(dto,
                new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)));
    }

    /**
     * 查询我的盘点清单单datagrid
     *
     * @param dto
     * @param page
     * @param rows
     * @return
     */
    @PostMapping(value = "inventorytemp_datagrid")
    @ResponseBody
    public Map<String, Object> getAssetInventoryTempByQuerysForDataGrid(AssetInventoryTempDto dto,
                                                                        @RequestParam(defaultValue = "1") String page, @RequestParam(defaultValue = "20") String rows) {
        logger.info("AppController: Get AssetSequestration ForDataGrid" + dto.toString());
        return inventoryTempService.getAssetInventoryTempForDataGridByDto(dto, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)));
    }

    /**
     * 更新我的盘点清单
     *
     * @param templist
     * @return
     */
    @PostMapping(value = "update_inventorytemplist")
    @ResponseBody
    public Map<String, Object> UpdateAssetInventoryTempList(String templist) {
        logger.info("AppController: Update AssetInventoryTempList");
        if (VGUtility.isEmpty(templist))
            throw new RuntimeException("盘点清单不能为空");
        List<AssetInventoryTempDto> dtolist = JSON.parseObject(templist, new TypeReference<ArrayList<AssetInventoryTempDto>>() {
        });
        for (AssetInventoryTempDto dto : dtolist) {
            inventoryTempService.UpdateAssetInventoryTempModel(dto);
        }
        ModelMap map = new ModelMap();
        map.put("success", "true");
        return map;
    }

    /**
     * 查看资产变更历史记录
     *
     * @param historyType
     * @param assetId
     * @param page
     * @param rows
     * @return
     */
    @PostMapping(value = "history_datagrid")
    @ResponseBody
    public Map<String, Object> getHistoryByQuerysForDataGrid(@RequestParam String historyType,
                                                             @RequestParam String assetId,
                                                             @RequestParam(defaultValue = "1") String page,
                                                             @RequestParam(defaultValue = "20") String rows) {
        logger.info("AppController: Get HistoryDtos For DataGrid By HistoryType {}", IAssetService.HISTORY_TYPE.values()[VGUtility.toInteger(historyType)]);
        return assetService.getHistoryByQuerysForDataGrid(historyType, assetId, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)));
    }

    /**
     * 查看资产变更历史记录
     *
     * @param assetId
     * @param page
     * @param rows
     * @return
     */
    @PostMapping(value = "history_datagrid_interface")
    @ResponseBody
    public Map<String, Object> getHistoryByQuerysForInterface(
            @RequestParam String assetId,
            @RequestParam(defaultValue = "1") String page,
            @RequestParam(defaultValue = "20") String rows) {
        logger.info("AppController: Get HistoryDtos For interface By assetId");
        return assetService.getHistoryByQuerysForInterface(assetId);
    }
}

package com.mine.product.czmtr.ram.asset.controller;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.AssetManagerChangeDto;
import com.mine.product.czmtr.ram.asset.dto.AssetSavePlaceChangeDto;
import com.mine.product.czmtr.ram.asset.dto.AssetUserChangeDto;
import com.mine.product.czmtr.ram.asset.service.IAssetChangeService;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.VGUtility;

@Controller
@RequestMapping(value = "/assetChange/")
@SessionAttributes(value = {"LoginUserInfo"})
public class AssetChangeController {
    private static final Logger logger = LoggerFactory.getLogger(AssetChangeController.class);

    @Autowired
    private IAssetChangeService assetChangeService;
    @Autowired
    private IBaseService baseService;

    /******************************************************资产管理员变更 start************************************************************/
    /***
     * 管理员变更-保存
     * @param dto
     * @return
     */
    @PostMapping(value = "save_change_manager")
    @ResponseBody
    public AssetManagerChangeDto saveChangeManager(AssetManagerChangeDto dto) {
        logger.info("Save Change Manager");
        return assetChangeService.saveChangeManager(dto);
    }

    /***
     * 管理员变更-保存&发起审批
     * @param dto
     * @return
     */
    @PostMapping(value = "save_and_check_change_manager")
    @ResponseBody
    public String saveAndCheckChangeManager(AssetManagerChangeDto dto) {
        logger.info("Save and Check Change Manager");
        assetChangeService.saveAndCheckChangeManager(dto);
        return "{\"success\":true}";
    }

    /***
     * 管理员变更-获取列表信息
     * @param page
     * @param rows
     * @return
     */
    @SuppressWarnings("serial")
    @PostMapping(value = "/manager_change_receipt_datagrid")
    @ResponseBody
    public Map<String, Object> managerChangeReceiptDatagrid(AssetManagerChangeDto assetManagerChangeDto,
                                                            String dynaViewId,
                                                            @RequestParam(defaultValue = "1") String page,
                                                            @RequestParam(defaultValue = "20") String rows) {
        logger.info("Get Receipt Model By Query For DataGrid");
        return assetChangeService.getAssetManagerChangeForDataGrid(new ISearchExpression() {
            @Override
            public Object change(Object... arg0) {
                CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                @SuppressWarnings("rawtypes")
                Root root = (Root) arg0[0];
                Predicate predicate = getPredicateByAssetManagerChangeDto(builder, root, assetManagerChangeDto);
                return predicate;
            }
        }, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)));
    }


    /***
     * 管理员变更-查询列表信息-查詢SQL拼接
     * @param builder
     * @param root
     * @param assetManagerChangeDto
     * @return
     */
    @SuppressWarnings("unchecked")
    private Predicate getPredicateByAssetManagerChangeDto(CriteriaBuilder builder, @SuppressWarnings("rawtypes") Root root, AssetManagerChangeDto assetManagerChangeDto) {
        Predicate finalPred = null;
        List<Predicate> andList = new ArrayList<>();

        if (!VGUtility.isEmpty(assetManagerChangeDto.getChangeNum()))//变更单号
            andList.add(builder.like(root.get("changeNum").as(String.class), "%" + assetManagerChangeDto.getChangeNum() + "%"));
        if (!VGUtility.isEmpty(assetManagerChangeDto.getAssetManagerId()))//新资产管理员
            andList.add(builder.equal(root.get("assetManagerId"), assetManagerChangeDto.getAssetManagerId()));
        if (!VGUtility.isEmpty(assetManagerChangeDto.getCreateUserId()))//创建人
            andList.add(builder.equal(root.get("createUserId"), assetManagerChangeDto.getCreateUserId()));
        if (!VGUtility.isEmpty(assetManagerChangeDto.getCreateTimestampStart())) {
            try {
                andList.add(builder.greaterThanOrEqualTo(root.get("createTimestamp").as(Date.class), (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(assetManagerChangeDto.getCreateTimestampStart() + " 00:00:01")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (!VGUtility.isEmpty(assetManagerChangeDto.getCreateTimestampEnd())) {
            try {
                andList.add(builder.lessThanOrEqualTo(root.get("createTimestamp").as(Date.class), (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(assetManagerChangeDto.getCreateTimestampEnd() + " 23:59:59")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (andList.size() > 0)
            finalPred = builder.and(andList.toArray(new Predicate[andList.size()]));
        return finalPred;
    }

    /***
     * 管理员变更-根据ID删除单条信息
     * @param id
     * @return
     */
    @PostMapping(value = "delete_change_manager_receipt")
    @ResponseBody
    public String deleteChangeManager(String id) {
        logger.info("Save Change Manager");
        if (!baseService.getPermCheck("zcglybgxzd_delete")) {
            throw new RuntimeException("该用户没有权限进行管理员变更单删除");
        }
        assetChangeService.deleteChangeManagerReceipt(id);
        return "{\"success\":true}";
    }

    /***
     * 管理员变更-保存&发起审批
     * @param id
     * @return
     */
    @PostMapping(value = "check_change_manager_receipt")
    @ResponseBody
    public String checkChangeManager(String id) {
        logger.info("Check Change Manager");
        assetChangeService.checkChangeManagerReceipt(id);
        return "{\"success\":true}";
    }

    /***
     *管理员变更-更新
     * @param dto
     * @return
     */
    @PostMapping(value = "update_change_manager")
    @ResponseBody
    public String updateChangeManager(AssetManagerChangeDto dto) {
        logger.info("Update Change Manager");
        assetChangeService.updateChangeManagerReceipt(dto);
        return "{\"success\":true}";
    }

    /******************************************************资产管理员变更 end************************************************************/

    /******************************************************资产使用人变更 start************************************************************/
    /***
     * 使用人变更-保存
     * @param dto
     * @return
     */
    @PostMapping(value = "save_change_user")
    @ResponseBody
    public AssetUserChangeDto saveChangeUser(AssetUserChangeDto dto) {
        logger.info("Save Change User");
        return assetChangeService.saveChangeUser(dto);
    }

    /***
     * 使用人变更-保存&发起审批
     * @param dto
     * @return
     */
    @PostMapping(value = "save_and_check_change_user")
    @ResponseBody
    public String saveAndCheckChangeUser(AssetUserChangeDto dto) {
        logger.info("Save and Check Change User");
        assetChangeService.saveAndCheckChangeUser(dto);
        return "{\"success\":true}";
    }

    /***
     * 使用人变更-获取列表信息
     * @param page
     * @param rows
     * @return
     */
    @SuppressWarnings("serial")
    @PostMapping(value = "/user_change_receipt_datagrid")
    @ResponseBody
    public Map<String, Object> userChangeReceiptDatagrid(AssetUserChangeDto assetUserChangeDto,
                                                         @RequestParam(defaultValue = "1") String page,
                                                         @RequestParam(defaultValue = "20") String rows) {
        logger.info("Get Receipt Model By Query For DataGrid");
        return assetChangeService.getAssetUserChangeForDataGrid(new ISearchExpression() {
            @Override
            public Object change(Object... arg0) {
                CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                @SuppressWarnings("rawtypes")
                Root root = (Root) arg0[0];
                Predicate predicate = getPredicateByAssetUserChangeDto(builder, root, assetUserChangeDto);
                return predicate;
            }
        }, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)));
    }


    /***
     * 使用人变更-查询列表信息-查詢SQL拼接
     * @param builder
     * @param root
     * @param assetUserChangeDto
     * @return
     */
    @SuppressWarnings("unchecked")
    private Predicate getPredicateByAssetUserChangeDto(CriteriaBuilder builder, @SuppressWarnings("rawtypes") Root root, AssetUserChangeDto assetUserChangeDto) {
        Predicate finalPred = null;
        List<Predicate> andList = new ArrayList<>();

        if (!VGUtility.isEmpty(assetUserChangeDto.getChangeNum()))//变更单号
            andList.add(builder.like(root.get("changeNum").as(String.class), "%" + assetUserChangeDto.getChangeNum() + "%"));
        if (!VGUtility.isEmpty(assetUserChangeDto.getOldAssetUserId()))//原使用人
            andList.add(builder.like(root.get("oldAssetUserId").as(String.class), "%" + assetUserChangeDto.getOldAssetUserId() + "%"));
        if (!VGUtility.isEmpty(assetUserChangeDto.getAssetUserId()))//新使用人
            andList.add(builder.equal(root.get("assetUserId"), assetUserChangeDto.getAssetUserId()));
        if (!VGUtility.isEmpty(assetUserChangeDto.getCreateUserId()))//新使用人
            andList.add(builder.equal(root.get("createUserId"), assetUserChangeDto.getCreateUserId()));
        if (!VGUtility.isEmpty(assetUserChangeDto.getCreateTimestampStart())) {
            try {
                andList.add(builder.greaterThanOrEqualTo(root.get("createTimestamp").as(Date.class), (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(assetUserChangeDto.getCreateTimestampStart() + " 00:00:01")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (!VGUtility.isEmpty(assetUserChangeDto.getCreateTimestampEnd())) {
            try {
                andList.add(builder.lessThanOrEqualTo(root.get("createTimestamp").as(Date.class), (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(assetUserChangeDto.getCreateTimestampEnd() + " 23:59:59")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (andList.size() > 0)
            finalPred = builder.and(andList.toArray(new Predicate[andList.size()]));
        return finalPred;
    }

    /***
     * 使用人变更-根据ID删除信息
     * @param id
     * @return
     */
    @PostMapping(value = "delete_change_user_receipt")
    @ResponseBody
    public String deleteChangeUser(String id) {
        logger.info("Save Change User");
        if (!baseService.getPermCheck("zcsyrbgxzd_delete")) {
            throw new RuntimeException("该用户没有权限进行使用人变更单删除");
        }
        assetChangeService.deleteChangeUserReceipt(id);
        return "{\"success\":true}";
    }

    /***
     * 使用人变更-发起审批
     * @param id
     * @return
     */
    @PostMapping(value = "check_change_user_receipt")
    @ResponseBody
    public String checkChangeUser(String id) {
        logger.info("Check Change User");
        assetChangeService.checkChangeUserReceipt(id);
        return "{\"success\":true}";
    }

    /***
     * 使用人变更-更新
     * @param dto
     * @return
     */
    @PostMapping(value = "update_change_user")
    @ResponseBody
    public String updateChangeUser(AssetUserChangeDto dto) {
        logger.info("Update Change User");
        assetChangeService.updateChangeUserReceipt(dto);
        return "{\"success\":true}";
    }
    /******************************************************资产使用人变更 end************************************************************/


    /******************************************************资产【安装位置】变更 start************************************************************/
    /***
     * 安装位置变更-保存
     * @param dto
     * @return
     */
    @PostMapping(value = "save_change_savePlace")
    @ResponseBody
    public AssetSavePlaceChangeDto saveChangeSavePlace(AssetSavePlaceChangeDto dto) {
        logger.info("Save Change SavePlace");
        return assetChangeService.saveChangeSavePlace(dto);
    }

    /***
     * 安装位置变更-保存&发起审批
     * @param dto
     * @return
     */
    @PostMapping(value = "save_and_check_change_savePlace")
    @ResponseBody
    public String saveAndCheckChangeSavePlace(AssetSavePlaceChangeDto dto) {
        logger.info("Save and Check Change SavePlace");
        assetChangeService.saveAndCheckChangeSavePlace(dto);
        return "{\"success\":true}";
    }

    /***
     * 安装位置变更-获取列表信息
     * @param page
     * @param rows
     * @return
     */
    @PostMapping(value = "/savePlace_change_receipt_datagrid")
    @SuppressWarnings("serial")
    @ResponseBody
    public Map<String, Object> savePlaceChangeReceiptDatagrid(AssetSavePlaceChangeDto assetSavePlaceChangeDto,
                                                              @RequestParam(defaultValue = "1") String page,
                                                              @RequestParam(defaultValue = "20") String rows) {
        logger.info("Get Receipt Model By Query For DataGrid");
        return assetChangeService.getAssetSavePlaceChangeForDataGrid(new ISearchExpression() {
            @Override
            public Object change(Object... arg0) {
                CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                @SuppressWarnings("rawtypes")
                Root root = (Root) arg0[0];
                Predicate predicate = getPredicateByAssetSavePlaceChangeDto(builder, root, assetSavePlaceChangeDto);
                return predicate;
            }
        }, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)));
    }


    /***
     * 安装位置变更-查询列表信息-查詢SQL拼接
     * @param builder
     * @param root
     * @param assetSavePlaceChangeDto
     * @return
     */
    @SuppressWarnings("unchecked")
    private Predicate getPredicateByAssetSavePlaceChangeDto(CriteriaBuilder builder, @SuppressWarnings("rawtypes") Root root, AssetSavePlaceChangeDto assetSavePlaceChangeDto) {
        Predicate finalPred = null;
        List<Predicate> andList = new ArrayList<>();

        if (!VGUtility.isEmpty(assetSavePlaceChangeDto.getChangeNum()))//变更单号
            andList.add(builder.like(root.get("changeNum").as(String.class), "%" + assetSavePlaceChangeDto.getChangeNum() + "%"));
        if (!VGUtility.isEmpty(assetSavePlaceChangeDto.getAssetSavePlaceId()))//新使用人
            andList.add(builder.equal(root.get("assetSavePlaceId"), assetSavePlaceChangeDto.getAssetSavePlaceId()));
        if (!VGUtility.isEmpty(assetSavePlaceChangeDto.getCreateUserId()))//新使用人
            andList.add(builder.equal(root.get("createUserId"), assetSavePlaceChangeDto.getCreateUserId()));
        if (!VGUtility.isEmpty(assetSavePlaceChangeDto.getCreateTimestampStart())) {
            try {
                andList.add(builder.greaterThanOrEqualTo(root.get("createTimestamp").as(Date.class), (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(assetSavePlaceChangeDto.getCreateTimestampStart() + " 00:00:01")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (!VGUtility.isEmpty(assetSavePlaceChangeDto.getCreateTimestampEnd())) {
            try {
                andList.add(builder.lessThanOrEqualTo(root.get("createTimestamp").as(Date.class), (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(assetSavePlaceChangeDto.getCreateTimestampEnd() + " 23:59:59")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (andList.size() > 0)
            finalPred = builder.and(andList.toArray(new Predicate[andList.size()]));
        return finalPred;
    }

    /***
     * 安装位置变更-根据ID删除信息
     * @param id
     * @return
     */
    @PostMapping(value = "delete_change_savePlace_receipt")
    @ResponseBody
    public String deleteChangeSavePlace(String id) {
        logger.info("Save Change SavePlace");
        if (!baseService.getPermCheck("zcazwzbgxzd_delete")) {
            throw new RuntimeException("该用户没有权限进行安装位置变更单删除");
        }
        assetChangeService.deleteChangeSavePlaceReceipt(id);
        return "{\"success\":true}";
    }

    /***
     * 安装位置变更-发起审批
     * @param id
     * @return
     */
    @PostMapping(value = "check_change_savePlace_receipt")
    @ResponseBody
    public String checkChangeSavePlace(String id) {
        logger.info("Check Change SavePlace");
        assetChangeService.checkChangeSavePlaceReceipt(id);
        return "{\"success\":true}";
    }

    /***
     * 安装位置变更-更新
     * @param dto
     * @return
     */
    @PostMapping(value = "update_change_savePlace")
    @ResponseBody
    public String updateChangeSavePlace(AssetSavePlaceChangeDto dto) {
        logger.info("Update Change SavePlace");
        assetChangeService.updateChangeSavePlaceReceipt(dto);
        return "{\"success\":true}";
    }
    /******************************************************资产【安装位置】变更 end************************************************************/
}

package com.mine.product.czmtr.ram.asset.controller;

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
import com.mine.product.czmtr.ram.asset.dto.AssetReduceDto;
import com.mine.product.czmtr.ram.asset.service.IAssetReduceService;
import com.mine.product.czmtr.ram.asset.service.IAssetService;
import com.mine.product.czmtr.ram.asset.service.IAssetService.REDUCE_TYPE;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.VGUtility;

/***
 * 资产减损管理
 * @author guoli.sun
 *
 */
@Controller
@RequestMapping(value = "/assetReduce/")
@SessionAttributes(value = {"LoginUserInfo"})
public class AssetReduceController {
    private static final Logger logger = LoggerFactory.getLogger(AssetReduceController.class);

    @Autowired
    private IAssetReduceService assetReduceService;
    @Autowired
    private IBaseService baseService;

    /***
     * 保存
     * @param dto
     * @return
     */
    @PostMapping(value = "save_reduce")
    @ResponseBody
    public AssetReduceDto saveReduce(AssetReduceDto dto) {
        logger.info("Save Reduce");
        return assetReduceService.saveReduce(dto);
    }

    /***
     * 保存&发起审批
     * @param dto
     * @return
     */
    @PostMapping(value = "save_and_check_reduce")
    @ResponseBody
    public String saveAndCheckReduce(AssetReduceDto dto) {
        logger.info("Save and Check Reduce");
        assetReduceService.saveAndCheckReduce(dto);
        return "{\"success\":true}";
    }

    /***
     * 获取列表信息
     * @param page
     * @param rows
     * @return
     */
    @SuppressWarnings("serial")
    @PostMapping(value = "/reduce_receipt_datagrid")
    @ResponseBody
    public Map<String, Object> managerReduceDatagrid(AssetReduceDto assetReduceDto,
                                                     String dynaViewId,
                                                     @RequestParam(defaultValue = "1") String page,
                                                     @RequestParam(defaultValue = "20") String rows) {
        logger.info("Get Receipt Model By Query For DataGrid");
        return assetReduceService.getAssetReduceForDataGrid(new ISearchExpression() {
            @Override
            public Object change(Object... arg0) {
                CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                @SuppressWarnings("rawtypes")
                Root root = (Root) arg0[0];
                Predicate predicate = getPredicateByAssetReduceDto(builder, root, assetReduceDto);
                return predicate;
            }
        }, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)));
    }


    /***
     * 查询列表信息-查詢SQL拼接
     * @param builder
     * @param root
     * @param assetReduceDto
     * @return
     */
    @SuppressWarnings("unchecked")
    private Predicate getPredicateByAssetReduceDto(CriteriaBuilder builder, @SuppressWarnings("rawtypes") Root root, AssetReduceDto assetReduceDto) {
        Predicate finalPred = null;
        List<Predicate> andList = new ArrayList<>();

        if (!VGUtility.isEmpty(assetReduceDto.getChangeNum()))//单号
            andList.add(builder.like(root.get("changeNum").as(String.class), "%" + assetReduceDto.getChangeNum() + "%"));
        if (!VGUtility.isEmpty(assetReduceDto.getOrderName()))//单号
            andList.add(builder.like(root.get("changeNum").as(String.class), "%" + assetReduceDto.getChangeNum() + "%"));
        if (!VGUtility.isEmpty(assetReduceDto.getCreateUserId()))//订单名称
            andList.add(builder.equal(root.get("createUserId"), assetReduceDto.getCreateUserId()));
        if (!VGUtility.isEmpty(assetReduceDto.getReduceType()))//减损方式
            andList.add(builder.equal(root.<REDUCE_TYPE>get("reduceType"),
                    IAssetService.REDUCE_TYPE.values()[VGUtility.toInteger(assetReduceDto.getReduceType())]));
        if (andList.size() > 0)
            finalPred = builder.and(andList.toArray(new Predicate[andList.size()]));
        return finalPred;
    }

    /***
     * 根据ID删除单条信息
     * @param id
     * @return
     */
    @PostMapping(value = "delete_reduce_receipt")
    @ResponseBody
    public String deleteReduce(String id) {
        logger.info("Delete Reduce");
        if (!baseService.getPermCheck("zcjsxzd_create")) {
            throw new RuntimeException("该用户没有权限进行资产减损单删除");
        }
        assetReduceService.deleteReduceReceipt(id);
        return "{\"success\":true}";
    }

    /***
     * 保存&发起审批
     * @param id
     * @return
     */
    @PostMapping(value = "check_reduce_receipt")
    @ResponseBody
    public String checkReduce(String id) {
        logger.info("Check Reduce");
        assetReduceService.checkReduceReceipt(id);
        return "{\"success\":true}";
    }

    /***
     * 更新
     * @param dto
     * @return
     */
    @PostMapping(value = "update_reduce")
    @ResponseBody
    public String updateReduce(AssetReduceDto dto) {
        logger.info("Update Reduce");
        assetReduceService.updateReduceReceipt(dto);
        return "{\"success\":true}";
    }
}

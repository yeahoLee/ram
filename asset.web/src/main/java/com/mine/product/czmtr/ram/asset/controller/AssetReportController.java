package com.mine.product.czmtr.ram.asset.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

import com.mine.base.dict.dto.DictDto;
import com.mine.base.dict.dto.DictTypeDto;
import com.mine.base.dict.service.IDictService;
import com.mine.platform.common.dto.CommonComboDto;
import com.mine.platform.common.dto.PageDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.AssetReportHistoryDto;
import com.mine.product.czmtr.ram.asset.dto.AssetUsabilityHistoryDto;
import com.mine.product.czmtr.ram.asset.service.IAssetReportService;
import com.mine.product.czmtr.ram.asset.service.IAssetService.CHANGE_TYPE;
import com.mine.product.czmtr.ram.asset.service.IAssetService.HISTORY_TYPE;
import com.mine.product.czmtr.ram.asset.service.IAssetService.REDUCE_TYPE;
import com.vgtech.platform.common.utility.VGUtility;

/***
 * 资产报表管理
 *
 * @author yangjie
 *
 */
@Controller
@RequestMapping(value = "/assetreport/")
@SessionAttributes(value = {"LoginUserInfo"})
public class AssetReportController {
    private static final Logger logger = LoggerFactory.getLogger(AssetReportController.class);

    @Autowired
    private IAssetReportService assetReportService;
    @Autowired
    private IDictService dictService;

    /********************* 资产变更历史 start *******************************/
    @PostMapping(value = "assetchangereport_datagrid")
    @ResponseBody
    public Map<String, Object> getAssetChangeReportForDataGrid(AssetReportHistoryDto assetReportHistoryDto,
                                                               @RequestParam(defaultValue = "1") String page,
                                                               @RequestParam(defaultValue = "20") String rows) {
        logger.info("Controller: Get Asset By Querys {} For DataGrid", assetReportHistoryDto);
        return assetReportService.GetAssetChangeHistory(assetReportHistoryDto, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)));
    }
    /********************* 资产变更历史 end *******************************/

    /********************* 资产移动历史 start *******************************/
    @PostMapping(value = "assetmovereport_datagrid")
    @ResponseBody
    public Map<String, Object> getAssetMoveReportForDataGrid(AssetReportHistoryDto assetReportHistoryDto,
                                                             @RequestParam(defaultValue = "1") String page,
                                                             @RequestParam(defaultValue = "20") String rows) {
        logger.info("Controller: Get Asset By Querys {} For DataGrid", assetReportHistoryDto);

        return assetReportService.QueryAssetMoveHistory(assetReportHistoryDto, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)));
    }
    /********************* 资产移动历史 end *******************************/

    /********************* 资产丢失,报废 start *******************************/
    @PostMapping(value = "assetlosereport_datagrid")
    @ResponseBody
    public Map<String, Object> getAssetLoseReportForDataGrid(AssetReportHistoryDto assetReportHistoryDto,
                                                             @RequestParam(defaultValue = "1") String page,
                                                             @RequestParam(defaultValue = "20") String rows) {
        logger.info("Controller: Get Asset By Querys {} For DataGrid", assetReportHistoryDto);

        Map<String, Object> resultMap = assetReportService.QueryAssetLoseHistory(new ISearchExpression() {
            @Override
            public Object change(Object... arg0) {
                CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                Root root = (Root) arg0[0];
                return getPredicateByAssetReduceDto(builder, root, assetReportHistoryDto);
            }
        }, null);//new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)));
        List<AssetReportHistoryDto> assetReportHistoryDtoList = (List<AssetReportHistoryDto>) resultMap.get("rows");
        if (assetReportHistoryDtoList.size() > 0) {
            if (!VGUtility.isEmpty(assetReportHistoryDto.getManageDeptId()))
                assetReportHistoryDtoList = assetReportHistoryDtoList.stream().filter(a -> Objects.nonNull(a.getManageDeptId()))
                        .filter(a -> a.getManageDeptId().contains(assetReportHistoryDto.getManageDeptId()))
                        .collect(Collectors.toList());
            if (!VGUtility.isEmpty(assetReportHistoryDto.getMaterialCode()))
                assetReportHistoryDtoList = assetReportHistoryDtoList.stream().filter(a -> Objects.nonNull(a.getMaterialCode()))
                        .filter(a -> a.getMaterialCode().contains(assetReportHistoryDto.getMaterialCode()))
                        .collect(Collectors.toList());

            if (assetReportHistoryDtoList.size() >= Integer.parseInt(rows))
                resultMap.put("rows", assetReportHistoryDtoList.subList((Integer.parseInt(page) - 1) * Integer.parseInt(rows), Integer.parseInt(page) * Integer.parseInt(rows) <= assetReportHistoryDtoList.size() ? Integer.parseInt(page) * Integer.parseInt(rows) : assetReportHistoryDtoList.size()));
            else
                resultMap.put("rows", assetReportHistoryDtoList);

            resultMap.put("total", assetReportHistoryDtoList.size());

        }
        return resultMap;
    }

    @PostMapping(value = "assetscrapreport_datagrid")
    @ResponseBody
    public Map<String, Object> getAssetScarpReportForDataGrid(AssetReportHistoryDto assetReportHistoryDto,
                                                              @RequestParam(defaultValue = "1") String page,
                                                              @RequestParam(defaultValue = "20") String rows) {
        logger.info("Controller: Get Asset By Querys {} For DataGrid", assetReportHistoryDto);

        Map<String, Object> resultMap = assetReportService.QueryAssetLoseHistory(new ISearchExpression() {
            @Override
            public Object change(Object... arg0) {
                CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                Root root = (Root) arg0[0];
                return getPredicateByAssetReduceDto(builder, root, assetReportHistoryDto);
            }
        }, null);//new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)));
        List<AssetReportHistoryDto> assetReportHistoryDtoList = (List<AssetReportHistoryDto>) resultMap.get("rows");
        if (assetReportHistoryDtoList.size() > 0) {
            if (!VGUtility.isEmpty(assetReportHistoryDto.getManageDeptId()))
                assetReportHistoryDtoList = assetReportHistoryDtoList.stream().filter(a -> Objects.nonNull(a.getManageDeptId()))
                        .filter(a -> a.getManageDeptId().contains(assetReportHistoryDto.getManageDeptId()))
                        .collect(Collectors.toList());
            if (!VGUtility.isEmpty(assetReportHistoryDto.getMaterialCode()))
                assetReportHistoryDtoList = assetReportHistoryDtoList.stream().filter(a -> Objects.nonNull(a.getMaterialCode()))
                        .filter(a -> a.getMaterialCode().contains(assetReportHistoryDto.getMaterialCode()))
                        .collect(Collectors.toList());

            if (assetReportHistoryDtoList.size() >= Integer.parseInt(rows))
                resultMap.put("rows", assetReportHistoryDtoList.subList((Integer.parseInt(page) - 1) * Integer.parseInt(rows), Integer.parseInt(page) * Integer.parseInt(rows) <= assetReportHistoryDtoList.size() ? Integer.parseInt(page) * Integer.parseInt(rows) : assetReportHistoryDtoList.size()));
            else
                resultMap.put("rows", assetReportHistoryDtoList);

            resultMap.put("total", assetReportHistoryDtoList.size());

        }
        return resultMap;

    }

    private Predicate getPredicateByAssetReduceDto(CriteriaBuilder builder, Root root, AssetReportHistoryDto assetReportHistoryDto) {
        Predicate finalPred = null;
        List<Predicate> andList = new ArrayList<>();
        if (!VGUtility.isEmpty(assetReportHistoryDto.getReduceTypeStr()))
            andList.add(builder.equal(root.get("reduceType"), REDUCE_TYPE.values()[VGUtility.toInteger(assetReportHistoryDto.getReduceTypeStr())]));
        if (!VGUtility.isEmpty(assetReportHistoryDto.getCreateTimestampStart()))
            andList.add(builder.greaterThanOrEqualTo(root.get("createTimestamp"), VGUtility.toDateObj(assetReportHistoryDto.getCreateTimestampStart(), "yyyy/M/d")));
        if (!VGUtility.isEmpty(assetReportHistoryDto.getCreateTimestampEnd()))
            andList.add(builder.lessThanOrEqualTo(root.get("createTimestamp"), VGUtility.toDateObj(assetReportHistoryDto.getCreateTimestampEnd(), "yyyy/M/d")));
        if (andList.size() > 0)
            finalPred = builder.and(andList.toArray(new Predicate[andList.size()]));

        return finalPred;
    }

    /********************* 资产丢失,报废 end *******************************/

    /********************* 资产可用性统计 start *******************************/
    @PostMapping(value = "assetusabilityreport_datagrid")
    @ResponseBody
    public Map<String, Object> getAssetUsablitityReportForDataGrid(AssetUsabilityHistoryDto assetUsabilityHistoryDto) {
        logger.info("Controller: Get Asset By Querys {} For DataGrid", assetUsabilityHistoryDto);

        return assetReportService.QueryAssetUsablitityHistory(assetUsabilityHistoryDto);

    }

    /********************* 资产可用性统计 end *******************************/

    @PostMapping(value = "common_datagrid")
    @ResponseBody
    public List<CommonComboDto> getCommonCodeForDataGrid(@RequestParam String commonCodeType) {
        logger.info("Get Common Code For Combobox By {}", commonCodeType);

        List<CommonComboDto> resultList = new ArrayList<CommonComboDto>();

        if (VGUtility.isEmpty(commonCodeType))
            return null;

        List<DictTypeDto> commonCodeTypeDtoList = dictService.getCommonCodeType("{key: '" + commonCodeType + "'}", null, null).getRowData();
        if (commonCodeTypeDtoList.size() == 0)
            return null;

        PageDto<DictDto> commonCodePageDto = dictService.getCommonCode("{typeId: '" + commonCodeTypeDtoList.get(0).getId() + "'}", null, null);
        List<DictDto> commonCodeDtoList = commonCodePageDto.getRowData();
        for (DictDto dictionaryCommonCodeDto : commonCodeDtoList) {
            CommonComboDto comboDto = new CommonComboDto();
            comboDto.setValue(dictionaryCommonCodeDto.getCode());
            comboDto.setText(dictionaryCommonCodeDto.getChsName());
            resultList.add(comboDto);
        }

        return resultList;
    }

}

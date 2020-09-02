package com.mine.product.czmtr.ram.asset.controller;

import com.itextpdf.testutils.ITextTest;
import com.mine.base.dict.dto.DictDto;
import com.mine.base.user.dto.DeptInfoDto;
import com.mine.base.user.dto.UserInfoDto;
import com.mine.platform.common.dto.PageDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.AssetAssetDto;
import com.mine.product.czmtr.ram.asset.dto.AssetDownLoadFileDto;
import com.mine.product.czmtr.ram.asset.dto.AssetDynamicViewDto;
import com.mine.product.czmtr.ram.asset.dto.AssetUploadFileDto;
import com.mine.product.czmtr.ram.asset.service.IAssetService;
import com.mine.product.czmtr.ram.asset.service.IAssetService.ASSET_STATUS;
import com.mine.product.czmtr.ram.asset.service.IAssetService.HISTORY_TYPE;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.vgtech.platform.common.utility.MineSecureUtility;
import com.vgtech.platform.common.utility.SpringSecureUserInfo;
import com.vgtech.platform.common.utility.VGUtility;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/asset/")
@SessionAttributes(value = {"LoginUserInfo"})
public class AssetController {
    private static final Logger logger = LoggerFactory.getLogger(AssetController.class);

    @Autowired
    private IAssetService assetService;
    @Autowired
    private IBaseService baseService;

    /**
     * 查询资产
     *
     * @param assetDto
     * @param dynaViewId
     * @param showType   用于判断用于什么查询,默认为0:表示会根据当前登陆人角色加上查询条件查询,
     *                   其他值表示只根据查询条件(一般用于资产盘点)
     * @param page
     * @param rows
     * @return
     */
    @PostMapping(value = "asset_datagrid")
    @ResponseBody
    public Map<String, Object> getAssetByQuerysForDataGrid(AssetAssetDto assetDto,
                                                           String dynaViewId, @RequestParam(required = false, defaultValue = "0") String showType, @RequestParam(required = false, defaultValue = "0") String show,
                                                           @RequestParam(defaultValue = "1") String page,
                                                           @RequestParam(defaultValue = "20") String rows) {
        logger.info("Controller: Get Asset By Querys {} For DataGrid", "[" + assetDto + "][" + dynaViewId + "]");

        if (!VGUtility.isEmpty(dynaViewId)) {
            AssetDynamicViewDto dynaViewDto = assetService.getDynamicViewById(dynaViewId);
            if (VGUtility.isEmpty(dynaViewDto))
                throw new RuntimeException("视图已被删除！");
            if (!VGUtility.isEmpty(dynaViewDto.getSavePlaceId())) {
                assetDto.setSavePlaceId(dynaViewDto.getSavePlaceId());
            }
            if (!VGUtility.isEmpty(dynaViewDto.getAssetStatusNum())) {
                assetDto.setAssetStatus(dynaViewDto.getAssetStatusNum());
            }
            if (!VGUtility.isEmpty(dynaViewDto.getAssetTypeNum())) {
                assetDto.setAssetType(dynaViewDto.getAssetTypeNum());
            }
            if (!VGUtility.isEmpty(dynaViewDto.getManageDeptId())) {
                assetDto.setManageDeptId(dynaViewDto.getManageDeptId());
            }
            if (!VGUtility.isEmpty(dynaViewDto.getManagerId())) {
                assetDto.setManagerId(dynaViewDto.getManagerId());
            }
            if (!VGUtility.isEmpty(dynaViewDto.getUseDeptId())) {
                assetDto.setUseDeptId(dynaViewDto.getUseDeptId());
            }
            if (!VGUtility.isEmpty(dynaViewDto.getUserId())) {
                assetDto.setUserId(dynaViewDto.getUserId());
            }
        }

        Map<String, Object> resultMap = assetService.getAssetByQuerys(assetDto, showType);

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

            //移除冻结资产中先前状态非待选状态的资产；
            if (!VGUtility.isEmpty(assetDto.getAssetStatus()) && assetDto.getAssetStatus().contains(",") && show.equals("0")) {

                String dongjie = Integer.toString(ASSET_STATUS.冻结.ordinal());
                String[] status = assetDto.getAssetStatus().split(",");
                List<String> statuList = Arrays.asList(status);

                Iterator<AssetAssetDto> iterator = assetDtoList.iterator();
                while (iterator.hasNext()) {
                    AssetAssetDto dto = iterator.next();
                    String nowStatus = dto.getAssetStatusStr();
                    String beforeStatus = dto.getBeforeChangeAssetStatusStr();
                    if (!VGUtility.isEmpty(nowStatus) && nowStatus.equals(dongjie)) {
                        int i = statuList.indexOf(beforeStatus);
                        if (i <= 0 || dongjie.equals(beforeStatus)) {
                            iterator.remove();
                        }
                    }

                }
            }

            if (assetDtoList.size() >= Integer.parseInt(rows))
                resultMap.put("rows", assetDtoList.subList((Integer.parseInt(page) - 1) * Integer.parseInt(rows), Integer.parseInt(page) * Integer.parseInt(rows) <= assetDtoList.size() ? Integer.parseInt(page) * Integer.parseInt(rows) : assetDtoList.size()));
            else
                resultMap.put("rows", assetDtoList);

            resultMap.put("total", assetDtoList.size());

        }
        return resultMap;
    }

    public List<AssetAssetDto> convertForHtml(List<AssetAssetDto> assetDtoList) {
        DictDto dictDto = new DictDto();
        DeptInfoDto deptInfoDto = new DeptInfoDto();
        UserInfoDto userInfoDto = new UserInfoDto();
        Set<String> dictIdSet = new HashSet<String>();
        Set<String> deptIdSet = new HashSet<String>();
        Set<String> userIdSet = new HashSet<String>();
        Set<String> materialCodeSet = new HashSet<String>();
        Set<String> codeLv1Set = new HashSet<String>();
        Set<String> codeLv2Set = new HashSet<String>();
        Set<String> codeLv3Set = new HashSet<String>();
        Set<String> codeLv4Set = new HashSet<String>();
        Map<String, DictDto> dictMap = new HashMap<String, DictDto>();
        Map<String, DictDto> dictCodeMap = new HashMap<String, DictDto>();
        Map<String, DeptInfoDto> deptMap = new HashMap<String, DeptInfoDto>();
        Map<String, UserInfoDto> userMap = new HashMap<String, UserInfoDto>();
        Map<String, DictDto> codeLv1Map = new HashMap<String, DictDto>();
        Map<String, DictDto> codeLv2Map = new HashMap<String, DictDto>();
        Map<String, DictDto> codeLv3Map = new HashMap<String, DictDto>();
        Map<String, DictDto> codeLv4Map = new HashMap<String, DictDto>();


        //获取所有需要查询数据库的id集合
        assetDtoList.stream().forEach(o -> {
            dictIdSet.add(o.getUnitOfMeasId());
            deptIdSet.add(o.getCompanyId());
            dictIdSet.add(o.getBelongLine());
            deptIdSet.add(o.getManageDeptId());
            userIdSet.add(o.getManagerId());
            dictIdSet.add(o.getSavePlaceId());
            materialCodeSet.add(o.getMaterialCode());
            if (!VGUtility.isEmpty(o.getMaterialCode())) {
                codeLv1Set.add(o.getMaterialCode().substring(0, 2));
                codeLv2Set.add(o.getMaterialCode().substring(2, 4));
                codeLv3Set.add(o.getMaterialCode().substring(4, 6));
                codeLv4Set.add(o.getMaterialCode().substring(6, 8));
            }
        });

        dictMap = baseService.getDictDtoByIdSet(dictIdSet);
        deptMap = baseService.getDeptDtoByIdSet(deptIdSet);
        userMap = baseService.getUserDtoByIdSet(userIdSet);
        dictCodeMap = baseService.getDictByMaterialCodeSet(materialCodeSet);
        codeLv1Map = baseService.getDictDtoByCodeLvSet(IBaseService.DEVICE_CODE_LV1, codeLv1Set);
        codeLv2Map = baseService.getDictDtoByCodeLvSet(IBaseService.DEVICE_CODE_LV2, codeLv2Set);
        codeLv3Map = baseService.getDictDtoByCodeLvSet(IBaseService.DEVICE_CODE_LV3, codeLv3Set);
        codeLv4Map = baseService.getDictDtoByCodeLvSet(IBaseService.DEVICE_CODE_LV4, codeLv4Set);

        for (AssetAssetDto assetDto : assetDtoList) {
            dictDto = dictMap.get(assetDto.getUnitOfMeasId());
            if (!VGUtility.isEmpty(dictDto))
                assetDto.setUnitOfMeasStr(dictDto.getChsName());

            deptInfoDto = deptMap.get(assetDto.getCompanyId());
            if (!VGUtility.isEmpty(deptInfoDto))
                assetDto.setCompanyStr(deptInfoDto.getDeptName());

            dictDto = dictMap.get(assetDto.getBelongLine());
            if (!VGUtility.isEmpty(dictDto))
                assetDto.setBelongLineStr(dictDto.getChsName());

            deptInfoDto = deptMap.get(assetDto.getManageDeptId());
            if (!VGUtility.isEmpty(deptInfoDto))
                assetDto.setManageDeptStr(deptInfoDto.getDeptName());

            userInfoDto = userMap.get(assetDto.getManagerId());
            if (!VGUtility.isEmpty(userInfoDto))
                assetDto.setManagerStr(userInfoDto.getChsName());

            dictDto = dictMap.get(assetDto.getSavePlaceId());
            if (!VGUtility.isEmpty(dictDto)) {
                assetDto.setSavePlaceCode(dictDto.getCode());
                assetDto.setSavePlaceName(dictDto.getChsName());
                assetDto.setSavePlaceStr(dictDto.getCode() + " " + dictDto.getChsName());
            }

            dictDto = dictCodeMap.get(assetDto.getMaterialCode());
            if (!VGUtility.isEmpty(dictDto))
                assetDto.setCombinationAssetName(dictDto.getChsName());

            if (!VGUtility.isEmpty(assetDto.getMaterialCode())) {
                String tempStr = "";
                dictDto = codeLv1Map.get(assetDto.getMaterialCode().substring(0, 2));
                if (!VGUtility.isEmpty(dictDto))
                    tempStr += dictDto.getChsName() + ",";
                dictDto = codeLv2Map.get(assetDto.getMaterialCode().substring(2, 4));
                if (!VGUtility.isEmpty(dictDto))
                    tempStr += dictDto.getChsName() + ",";
                dictDto = codeLv3Map.get(assetDto.getMaterialCode().substring(4, 6));
                if (!VGUtility.isEmpty(dictDto))
                    tempStr += dictDto.getChsName() + ",";
                dictDto = codeLv4Map.get(assetDto.getMaterialCode().substring(6, 8));
                if (!VGUtility.isEmpty(dictDto))
                    tempStr += dictDto.getChsName();
                assetDto.setCombinationAssetType(tempStr);
            }
        }

        return assetDtoList;
    }

    //*************动态视图StartNew**************
    @PostMapping(value = "create_dynamic_view")
    @ResponseBody
    public String createDynamicView(AssetDynamicViewDto dynamicViewDto) {
        logger.info("Controller: Create Dynamic View");
        assetService.createDynamicView(dynamicViewDto);
        return "{\"success\":true}";
    }

    /***
     * 修改动态资产视图的分页bug
     * @param page
     * @param rows
     * @return
     */
    @PostMapping(value = "asset_dynamic_datagrid")
    @ResponseBody
    public Map<String, Object> getAssetDynamicForDataGrid(@RequestParam(defaultValue = "1") String page,
                                                          @RequestParam(defaultValue = "20") String rows) {
        logger.info("Controller: Get AssetDynamic ForDataGrid");
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        return assetService.getAssetDynamicForDataGrid(new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)), userInfoDto);
    }

    @PostMapping(value = "delete_dynamic_view")
    @ResponseBody
    public void deleteDynamicView(String dynamicViewId) {
        logger.info("Controller: Delete Dynamic View");
        assetService.deleteDynamicViewById(dynamicViewId);
    }

    @PostMapping(value = "get_dynamic_view")
    @ResponseBody
    public AssetDynamicViewDto getDynamicViewForDialog(String dynamicViewId) {
        logger.info("Controller: Get Dynamic View For Dialog");
        return assetService.getDynamicViewById(dynamicViewId);
    }

    @PostMapping(value = "update_asset_dynamic")
    @ResponseBody
    public void updateAssetDynamic(AssetDynamicViewDto dto) {
        logger.info("Controller: Update Asset Dynamic");
        assetService.updateAssetDynamicById(dto);
    }

    @PostMapping(value = "change_dynamic_view_state")
    @ResponseBody
    public void changeAssetDynamicViewState(String id, String assetViewState) {
        logger.info("Controller: Change Asset Dynamic View State");
        assetService.changeDynamicViewState(id, assetViewState);
    }

    //*************动态视图EndNew****************

    @PostMapping(value = "asset_detail_datagrid")
    @ResponseBody
    public List<AssetAssetDto> assetDetailForDataGrid(String id) {
        String replace = id.replace("[", "").replace("]", "");
        String[] split = replace.split(",");
        List<String> idList = new ArrayList<>();
        for (String s : split) {
            String trim = s.trim();
            idList.add(trim);
        }
//        List<AssetAssetDto> dtoList = assetService.assetDetailForDataGrid(StringEscapeUtils.escapeSql(materialCode));
        return assetService.getListByIdList(idList);
    }

    @PostMapping(value = "used_dynamic_view_condition_datagrid")
    @ResponseBody
    public List<AssetAssetDto> usedDynamicViewConditionForDataGrid(String id) {
        List<AssetAssetDto> dtoList = assetService.excuteAssetViewConditionForDataGrid(id);
        return dtoList;
    }


    @PostMapping(value = "asset_batch_update_datagrid")
    @ResponseBody
    public Map<String, Object> getAssetByQuerysForDataGrid(@RequestParam(defaultValue = "") String assetIdListStr, @RequestParam(required = false, defaultValue = "0") String isFilt) {
        logger.info("Controller: Get AssetDtos By AssetIdList {} and RemoveIdList {} For DataGrid", assetIdListStr);
        //convert
        List<String> assetIdList = new ArrayList<String>();
        if (!VGUtility.isEmpty(assetIdListStr))
            Arrays.stream(assetIdListStr.split(",")).forEach(arr -> assetIdList.add(arr.trim()));

        //去重
        if (assetIdList.size() > 0) {
            HashSet<String> set = new HashSet<String>(assetIdList);
            assetIdList.clear();
            assetIdList.addAll(set);
        }

        return assetService.getAssetByQuerysForDataGrid(isFilt,new ISearchExpression() {
            @Override
            public Object change(Object... arg0) {
                CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
                Root root = (Root) arg0[0];
                Predicate finalPred = builder.equal(root.get("id"), "");

                List<Predicate> orList = new ArrayList<>();
                if (assetIdList.size() > 0) {
                    for (String tempStr : assetIdList)
                        orList.add(builder.equal(root.get("id"), tempStr));
                    finalPred = builder.or(orList.toArray(new Predicate[orList.size()]));
                }

                return finalPred;
            }
        }, null);
    }

    @PostMapping(value = "check_asset_dto")
    @ResponseBody
    public String checkAssetDto(AssetAssetDto assetDto) {
        logger.info("Controller: Check Asset Dto");
        assetService.commonCheck(assetDto);
        assetService.commonCheckBySearch(assetDto);
        return "{\"success\":true}";
    }

    @PostMapping(value = "create_asset")
    @ResponseBody
    public String createAsset(AssetAssetDto assetDto) {
        logger.info("Controller: Create Asset");
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        assetService.createAsset(assetDto, userInfoDto);
        return "{\"success\":true}";
    }

    @PostMapping(value = "update_asset")
    @ResponseBody
    public String updateAsset(AssetAssetDto assetDto) {
        logger.info("Controller: Update Asset");
        assetService.updateAsset(assetDto);
        return "{\"success\":true}";
    }

    @PostMapping(value = "batch_update_asset")
    @ResponseBody
    public String batchUpdateAsset(@RequestParam String assetIdListStr, AssetAssetDto assetDto) {
        logger.info("Controller: Batch Update Asset");
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        assetService.batchUpdateAsset(assetIdListStr, assetDto, userInfoDto);
        return "{\"success\":true}";
    }

    @PostMapping(value = "import_asset_xls")
    @ResponseBody
    public Map<String, Object> importAssetFile(AssetUploadFileDto uploadFileDto) {
        logger.info("Import Material Info File");
        int i = 1;
        String comCode = new String();
        String regex = "[+-]?[1-9]+[0-9]*(\\.[0-9]+)?";
        DictDto dictDto = new DictDto();
        DeptInfoDto comInfoDto = new DeptInfoDto();
        DeptInfoDto deptInfoDto = new DeptInfoDto();
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        UserInfoDto managerInfoDto = new UserInfoDto();
        List<AssetAssetDto> assetDtoList = new ArrayList<AssetAssetDto>();
        List<DeptInfoDto> deptInfoDtoList = new ArrayList<DeptInfoDto>();
        ModelMap modelMap = new ModelMap();

        try {
            HSSFWorkbook wb = new HSSFWorkbook(uploadFileDto.getUploadFileData().getInputStream());
            HSSFSheet sheet = wb.getSheetAt(0);
            if (VGUtility.isEmpty(sheet)) {
                throw new RuntimeException("表单内容为空！");
            }

            for (; i < sheet.getPhysicalNumberOfRows(); i++) {
                int colNum = 0;
                AssetAssetDto assetDto = new AssetAssetDto();

                HSSFRow row = sheet.getRow(i);
                if (VGUtility.isEmpty(row.getCell(0))) {
                    continue;
                }

                //物资编码
                String materialCode = baseService.getCellFormatValue(row.getCell(0));
                if (null == materialCode || "".equals(materialCode) || materialCode.length() != 12) {
                    throw new RuntimeException("物资编码格式不正确！");
                } else {
                    Map<String, String> assetMap = baseService.getAssetMapByMaterialCode(materialCode);
                    if (assetMap.isEmpty()) {
                        throw new RuntimeException("物资编码不存在！");
                    } else {
                        assetDto.setMaterialCode(materialCode);
                        assetDto.setAssetChsName(assetMap.get("assetName"));
                        assetDto.setAssetTypeName(baseService.getAssetTypeByMaterialCode(materialCode));
                        assetDto.setAssetTypeStr(assetMap.get("W_PRO_CODE"));
                        assetDto.setUnitOfMeasStr(assetMap.get("W_UNIT_CODE"));
                        assetDto.setSpecAndModels(assetMap.get("MARTERIALS_SPEC"));
                    }
                }

                //所属公司编码
                boolean isBelong = false;
                comCode = baseService.getCellFormatValue(row.getCell(7));
                deptInfoDtoList = baseService.getDeptDtoListByUserId(userInfoDto.getId());
                for (DeptInfoDto tempComInfoDto : deptInfoDtoList) {
                    if (tempComInfoDto.getDeptCode().equals(comCode)) {
                        isBelong = true;
                        comInfoDto = tempComInfoDto;
                        break;
                    }
                }

                if (isBelong) {
                    assetDto.setCompanyId(comInfoDto.getId());
                    assetDto.setCompanyStr(comInfoDto.getDeptCode() + " " + comInfoDto.getDeptName());
                } else {
                    throw new RuntimeException("所属公司编码不正确！");
                }

                //主管部门编码
                deptInfoDto = (DeptInfoDto) baseService.getDeptInfoDtoByCode(baseService.getCellFormatValue(row.getCell(10)));
                if (!VGUtility.isEmpty(deptInfoDto.getPropertyMap()) && comCode.equals((String) deptInfoDto.getPropertyMap().get("COMP_NO"))) {
                    assetDto.setManageDeptId(deptInfoDto.getId());
                    assetDto.setManageDeptStr(deptInfoDto.getDeptCode() + " " + deptInfoDto.getDeptName());
                } else {
                    throw new RuntimeException("主管部门不属于所属公司！");
                }

                //资产管理员编码
                managerInfoDto = (UserInfoDto) baseService.getUserInfoByUserName(baseService.getCellFormatValue(row.getCell(11)));
                if (!VGUtility.isEmpty(managerInfoDto.getPropertyMap()) && comCode.equals((String) managerInfoDto.getPropertyMap().get("COMP_NO"))) {
                    assetDto.setManagerId(managerInfoDto.getId());
                    assetDto.setManagerStr(managerInfoDto.getUserName() + " " + managerInfoDto.getChsName());
                } else {
                    throw new RuntimeException("资产管理员不属于所属公司");
                }

                //所属建筑线路编码
                dictDto = (DictDto) baseService.getDictDtoByTypeAndCode(IBaseService.DICT_BELONG_LINE, baseService.getCellFormatValue(row.getCell(8)));
                if (VGUtility.isEmpty(dictDto.getId())) {
                    throw new RuntimeException("所属建筑线路编码不正确！");
                } else {
                    assetDto.setBelongLine(dictDto.getId());
                    assetDto.setBelongLineStr(dictDto.getCode() + " " + dictDto.getChsName());
                }

                //安装位置编码
                dictDto = (DictDto) baseService.getDictDtoByTypeAndCode(IBaseService.SAVE_PLACE, baseService.getCellFormatValue(row.getCell(14)));
                if (!VGUtility.isEmpty(dictDto.getId())) {
                    assetDto.setSavePlaceId(dictDto.getId());
                    assetDto.setSavePlaceName(dictDto.getCode() + " " + dictDto.getChsName());
                    assetDto.setSavePlaceStr(dictDto.getChsName());
                }

                //日期格式检查
                String tempDateStr = baseService.getCellFormatValue(row.getCell(9));
                Date tempDate = VGUtility.toDateObj(tempDateStr, "yyyy/MM/dd");
                if (VGUtility.isEmpty(tempDate)) {
                    throw new RuntimeException("购置日期格式不正确！");
                } else {
                    assetDto.setBuyDate(tempDateStr);
                }

                tempDateStr = baseService.getCellFormatValue(row.getCell(16));
                tempDate = VGUtility.toDateObj(tempDateStr, "yyyy/MM/dd");
                if (VGUtility.isEmpty(tempDate)) {
                    throw new RuntimeException("维保期格式不正确！");
                } else {
                    assetDto.setMainPeriod(baseService.getCellFormatValue(row.getCell(16)));
                }

                tempDateStr = baseService.getCellFormatValue(row.getCell(19));
                tempDate = VGUtility.toDateObj(baseService.getCellFormatValue(row.getCell(19)), "yyyy/MM/dd");
                if (VGUtility.isEmpty(tempDate)) {
                    throw new RuntimeException("出厂日期格式不正确！");
                } else {
                    assetDto.setProdTime(tempDateStr);
                }

                //检查金额格式
                String tempNumStr = baseService.getCellFormatValue(row.getCell(3));
                if (tempNumStr.matches(regex)) {
                    assetDto.setPurcPrice(tempNumStr);
                } else {
                    throw new RuntimeException("采购金额必须为数字！");
                }

                tempNumStr = baseService.getCellFormatValue(row.getCell(4));
                if (tempNumStr.matches(regex)) {
                    assetDto.setEquiOrigValue(tempNumStr);
                } else {
                    throw new RuntimeException("资产原值必须为数字！");
                }

                assetDto.setSeriesNum(baseService.getCellFormatValue(row.getCell(1)));
                assetDto.setAssetBrand(baseService.getCellFormatValue(row.getCell(2)));
                assetDto.setTechPara(baseService.getCellFormatValue(row.getCell(5)));
                assetDto.setRemark(baseService.getCellFormatValue(row.getCell(6)));
                assetDto.setContractNum(baseService.getCellFormatValue(row.getCell(12)));
                assetDto.setAssetSource(baseService.getCellFormatValue(row.getCell(13)));
                assetDto.setTendersNum(baseService.getCellFormatValue(row.getCell(15)));
                assetDto.setSourceUser(baseService.getCellFormatValue(row.getCell(17)));
                assetDto.setSourceContactInfo(baseService.getCellFormatValue(row.getCell(18)));

                assetDtoList.add(assetDto);
            }
        } catch (Exception e) {
            logger.error("Exception when read Excel File", e);
            modelMap.addAttribute("errorMessage", "第" + i + "行的" + e.getMessage());
        }
        modelMap.addAttribute("assetDtoList", assetDtoList);
        return modelMap;
    }


    @PostMapping(value = "delete_asset")
    @ResponseBody
    public String deleteAsset(@RequestParam String assetId) {
        logger.info("Controller: Delete Asset");
        assetService.deleteAsset(assetId);
        return "{\"success\":true}";
    }


    @PostMapping(value = "history_datagrid")
    @ResponseBody
    public Map<String, Object> getHistoryByQuerysForDataGrid(@RequestParam String historyType,
                                                             @RequestParam String assetId,
                                                             @RequestParam(defaultValue = "1") String page,
                                                             @RequestParam(defaultValue = "20") String rows) {
        logger.info("Controller: Get HistoryDtos For DataGrid By HistoryType {}", HISTORY_TYPE.values()[VGUtility.toInteger(historyType)]);
        return assetService.getHistoryByQuerysForDataGrid(historyType, assetId, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)));
    }

    @PostMapping(value = "history_datagrid_interface")
    @ResponseBody
    public Map<String, Object> getHistoryByQuerysForInterface(
            @RequestParam String assetId,
            @RequestParam(defaultValue = "1") String page,
            @RequestParam(defaultValue = "20") String rows) {
        logger.info("Controller: Get HistoryDtos For interface By assetId");
        return assetService.getHistoryByQuerysForInterface(assetId);
    }

    @PostMapping(value = "upload_file")
    public String uploadFile(AssetUploadFileDto uploadFileDto) throws IOException {
        logger.info("Controller: Upload File");
        assetService.uploadFile(uploadFileDto.getAssetModelId(),
                uploadFileDto.getUploadFileData().getOriginalFilename(),
                uploadFileDto.getUploadFileData().getBytes(), uploadFileDto.getFileSpec());
        return "redirect:/asset_update?assetId=" + uploadFileDto.getAssetModelId();
    }

    @PostMapping(value = "upload_file_datagrid")
    @ResponseBody
    public Map<String, Object> getUploadFileByQuerysForDataGrid(@RequestParam String assetModelId,
                                                                @RequestParam(defaultValue = "1") String page,
                                                                @RequestParam(defaultValue = "20") String rows) {
        logger.info("Controller: Get UploadFile Dtos By Asset Model Id {} For DataGrid", assetModelId);
        return assetService.getUploadFileByQuerysForDataGrid(assetModelId, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)));
    }

    @GetMapping(value = "download_upload_file")
    @ResponseBody
    public void downloadFile(@RequestParam String uploadId, HttpServletRequest request, HttpServletResponse response) {
        logger.info("Controller: Download File By UploadId:" + uploadId);
        AssetDownLoadFileDto dto = assetService.getDownloadFileById(uploadId);

        byte[] bytes = dto.getFileByteArray();

        String prefix = "";
        String fileName = dto.getFileName();
        if (fileName != null && fileName.length() > 0)
            prefix = fileName.substring(fileName.lastIndexOf(".") + 1);

        if ("pdf".equals(prefix.toLowerCase())) {
            response.setContentType("application/pdf");
            response.setHeader("Content-Length", Integer.toString(bytes.length));
        } else {
            //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType("multipart/form-data");
            //2.设置文件头
            try {
                if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0)
                    //IE的话，通过URLEncoder对filename进行UTF8编码
                    fileName = URLEncoder.encode(fileName, "UTF-8");
                else
                    //而其他的浏览器（firefox、chrome、safari、opera），则要通过字节转换成ISO8859-1了
                    fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            } catch (Exception e) {
            }
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        }

        try {
            response.getOutputStream().write(bytes);
        } catch (Exception e) {
            logger.error("Exception when write result", e);
            throw new RuntimeException("下载文件出错！");
        }
    }

    @PostMapping(value = "delete_upload_file")
    @ResponseBody
    public String deleteUploadFile(String uploadId) {
        logger.info("Delete Upload File By UploadId:" + uploadId);
        assetService.deleteUploadFile(uploadId);
        return "{\"success\":true}";
    }

    //导出商品
    @GetMapping(value = "export_asset_stan_book")
    @ResponseBody
    public void exportAssetStanBook(AssetAssetDto assetDto, HttpServletRequest request, HttpServletResponse response) {
        logger.info("Export Asset Stan Book By AssetDto {}", assetDto);
        //HttpHeaders headers = new HttpHeaders();
        //String fileName = "asset_stan_book "+VGUtility.toDateStr(new Date(), "yyyy/M/d")+".xls";
        String fileName = "assetstanbook" + ".xls";
        //headers.setCacheControl("no-cache,no-store,must-revalidate");
        /*headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        
        try {
            headers.setContentDispositionFormData("attachment", new String(fileName.getBytes("gb2312"), "iso-8859-1"));
            //headers.setPragma("public");
        } catch (UnsupportedEncodingException e) {
            logger.error("exception when return controller",e);
            throw new RuntimeException("相应出错");
        }*/
        Map<String, Object> assetQueryMap = assetService.getAssetByQuerys(assetDto, "0");
        byte[] bytes = assetService.exportAssetStanBookByQuerys(assetQueryMap.get("rows"));
        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        //2.设置文件头
        try {
            if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0)
                fileName = URLEncoder.encode(fileName, "UTF-8"); // IE的话，通过URLEncoder对filename进行UTF8编码
            else
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1"); // 而其他的浏览器（firefox、chrome、safari、opera），则要通过字节转换成ISO8859-1了
        } catch (Exception e) {
        }
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);

        try {
            response.getOutputStream().write(bytes);
        } catch (Exception e) {
            logger.error("Exception when write result", e);
            throw new RuntimeException("导出出错！");
        }
        //return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "print_asset_card_by_asset_id_list")
    @ResponseBody
    public void printAssetCardByAssetIdList(@RequestParam(required = false) String assetIdList, HttpServletResponse response) {
        logger.info("Print Asset Card By Asset Id List [{}]", assetIdList);
        byte[] bytes = assetService.printAssetCardByAssetIdList(assetIdList);
        response.setContentType("application/pdf");
        response.setHeader("Content-Length", Integer.toString(bytes.length));
        try {
            response.getOutputStream().write(bytes);
        } catch (Exception e) {
            logger.error("Exception when write result", e);
            throw new RuntimeException("打印出错！");
        }
    }

    @RequestMapping(value = "print_asset_card_by_query")
    @ResponseBody
    public void printAssetCardByQuery(AssetAssetDto assetDto, HttpServletResponse response) {
        Map<String, Object> resultMap = assetService.getAssetByQuerys(assetDto, "0");

        List<AssetAssetDto> assetDtoList = (List<AssetAssetDto>) resultMap.get("rows");
        if (CollectionUtils.isEmpty(assetDtoList)) {
            return;
        }
        String assetIdList = assetDtoList.stream().map(e ->
                e.getId()
        ).collect(Collectors.joining(","));
        byte[] bytes = assetService.printAssetCardByAssetIdList(assetIdList);
        response.setContentType("application/pdf");
        response.setHeader("Content-Length", Integer.toString(bytes.length));
        try {
            response.getOutputStream().write(bytes);
        } catch (Exception e) {
            logger.error("Exception when write result", e);
            throw new RuntimeException("打印出错！");
        }
    }

    @RequestMapping(value = "material_mode_datagrid")
    @ResponseBody
    public Map<String, Object> getMaterialModelForDatagridByQuerys(AssetAssetDto assetAssetDto,
                                                                   @RequestParam(defaultValue = "1") String page,
                                                                   @RequestParam(defaultValue = "20") String rows) {
        Map<String, Object> result = new HashMap<String, Object>();
        //根据当前登录人的角色获取所管理的资产
        Map<String, Object> resultMap = assetService.getAssetByQuerys(assetAssetDto, "0");
        List<AssetAssetDto> assetDtoList = (List<AssetAssetDto>) resultMap.get("rows");


        List<DictDto> list = new ArrayList<>();
        if (assetDtoList.size() > 0) {
            Map<String, Set<String>> materialCodeMap = new HashMap<>();
            for (AssetAssetDto dto : assetDtoList) {
                Set<String> set = materialCodeMap.get(dto.getMaterialCode());
                if (null == set) {
                    set = new HashSet<>();
                    materialCodeMap.put(dto.getMaterialCode(), set);
                }
                set.add(dto.getId());
            }

            //查询全部
            Set<String> typeIdSet = assetService.getAssetTypeIdSetByUserId(""/*userInfoDto.getId()*/);
            //通过当前登录人查询
//        typeIdSet = assetService.getAssetTypeIdSetByUserRole();

            String queryStr = new String();
            for (String typeId : typeIdSet) {
                queryStr += "'" + typeId + "',";
            }

            if (queryStr.length() > 0) {
                queryStr = queryStr.substring(0, queryStr.length() - 1);
            }

            //获取物资数据
            PageDto<DictDto> dictPageDto = baseService.getCommonCodeForDatagridByQuerys(queryStr, IBaseService.DICT_ASSET_TYPE, null);
            List<DictDto> dictDtoList = dictPageDto.getRowData();

            for (DictDto dictDto : dictDtoList) {
                Set<String> assetIdList = materialCodeMap.get(dictDto.getCode());
                //添加有资产的物资
                if (null != assetIdList && assetIdList.size() > 0) {
                    dictDto.setParentDictEngName(assetIdList.toString());
                    dictDto.getPropertyMap().put("assetQuantity", assetIdList.size());
                    //物资类型和是否进设备台账进行映射；
                    if (!VGUtility.isEmpty(dictDto.getPropertyMap()) && !VGUtility.isEmpty(dictDto.getPropertyMap().get("W_IS_PRO"))) {
                        String w_is_pro = dictDto.getPropertyMap().get("W_IS_PRO").toString();
                        String produce = w_is_pro.equals("是") ? "生产性物资" : "非生产性物资";
                        dictDto.getPropertyMap().put("W_IS_PRO", produce);
                    }
                    list.add(dictDto);
                }

            }

            if (!VGUtility.isEmpty(assetAssetDto.getAssetChsName())) {
                list = list.stream().filter(a -> StringUtils.isNotEmpty(a.getChsName()))
                        .filter(a -> a.getChsName().contains(assetAssetDto.getAssetChsName()))
                        .collect(Collectors.toList());
            }

        }
        
        if (list.size() >= Integer.parseInt(rows)) {
            result.put("rows", list.subList((Integer.parseInt(page) - 1) * Integer.parseInt(rows), Integer.parseInt(page) * Integer.parseInt(rows) <= list.size() ? Integer.parseInt(page) * Integer.parseInt(rows) : list.size()));
        } else {
            result.put("rows", list);
        }
        result.put("total", list.size());
        return result;
    }

}

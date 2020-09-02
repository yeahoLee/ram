package com.mine.product.czmtr.ram.base.controller;

import com.alibaba.fastjson.JSON;
import com.mine.base.common.exception.DuplicateException;
import com.mine.base.dict.dto.DictDto;
import com.mine.base.dict.dto.DictTypeDto;
import com.mine.base.dict.service.IDictService;
import com.mine.base.menu.dto.MenuDto;
import com.mine.base.menu.service.IMenuService;
import com.mine.base.permission.dto.PermissionGroupDto;
import com.mine.base.permission.dto.PermissionItemDto;
import com.mine.base.permission.dto.PermissionValueDto;
import com.mine.base.permission.service.IPermissionService;
import com.mine.base.permission.service.IPermissionService.PermissionType;
import com.mine.base.user.dto.DeptInfoDto;
import com.mine.base.user.dto.UserInfoDto;
import com.mine.base.user.service.IUserService;
import com.mine.platform.common.dto.CommonComboDto;
import com.mine.platform.common.dto.CommonTreeDto;
import com.mine.platform.common.dto.PageDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.product.czmtr.ram.asset.dto.MaterialCodeSyncBean;
import com.mine.product.czmtr.ram.asset.service.IAssetService;
import com.mine.product.czmtr.ram.base.dto.*;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.mine.product.czmtr.ram.base.service.ICommonUtils;
import com.vgtech.platform.common.utility.MineSecureUtility;
import com.vgtech.platform.common.utility.SpringSecureUserInfo;
import com.vgtech.platform.common.utility.VGUtility;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/base/")
@SessionAttributes(value = {"LoginUserInfo"})
public class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private IUserService userService;
    @Autowired
    private IDictService dictService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IPermissionService permService;
    @Autowired
    private IBaseService baseService;
    @Autowired
    private ICommonUtils commonUtils;

    @PostMapping(value = "dept_datagrid")
    @ResponseBody
    public Map<String, Object> getDeptByQuerysForDataGrid(@RequestParam(required = false) String qStr,
                                                          @RequestParam(defaultValue = "1") String page,
                                                          @RequestParam(defaultValue = "20") String rows) {
        logger.info("Controller: Get dept By Querys {} For DataGrid", qStr);
        PageDto<DeptInfoDto> deptDtoList = userService.getDeptInfo("{sysMark:'" + IBaseService.SYSMARK + "'}", null, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)));

        Map<String, Object> result = new HashMap<String, Object>();
        List<BaseDeptDto> baseDeptDtoList = new ArrayList<BaseDeptDto>();
        deptDtoList.getRowData().stream().forEach(o -> {
            baseDeptDtoList.add(convertDeptInfoDtoToBaseDeptDto(o));
        });
        result.put("rows", baseDeptDtoList);
        result.put("total", deptDtoList.getTotalCount());
        return result;
    }

    /**
     * @Description: 部门管理 树形结构展示
     * @Param: [qStr, page, rows]
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     * @Author: lichuan.zhang
     * @Date: 2020/7/8
     */
    @RequestMapping(value = "dept_tree")
    @ResponseBody
    public List<DeptTreeDto> getDeptTreeForDataGrid() {
        List<DeptTreeDto> list = baseService.getDeptTreeForDataGrid();
        return list;
    }

    private BaseDeptDto convertDeptInfoDtoToBaseDeptDto(DeptInfoDto deptInfoDto) {
        BaseDeptDto baseDeptDto = new BaseDeptDto();
        baseDeptDto.setId(deptInfoDto.getId());
        baseDeptDto.setDeptCode(deptInfoDto.getDeptCode());
        baseDeptDto.setDeptName(deptInfoDto.getDeptName());
        baseDeptDto.setPdId(deptInfoDto.getPdId());
        if (!VGUtility.isEmpty(deptInfoDto.getPdId())) {
            try {
                baseDeptDto.setPdName(userService.getDeptInfo(deptInfoDto.getPdId()).getDeptName());
            } catch (Exception e) {
            }
        }
        return baseDeptDto;
    }

    /**
     * 子部门combobox
     *
     * @param parentDeptId
     * @return
     */
    @PostMapping(value = "dept_combo_by_pdid")
    @ResponseBody
    public List<CommonComboDto> getDeptForCombobox(@RequestParam(required = false) String parentDeptId, @RequestParam(required = false) String q) {
        logger.info("Get Dept For Combobox");
        List<CommonComboDto> resultList = new ArrayList<CommonComboDto>();
        if (VGUtility.isEmpty(parentDeptId))
            return resultList;
        List<CommonComboDto> dtoList = baseService.getDeptForComboboxByParentDeptId(parentDeptId);
        dtoList.remove(0);
        if (!VGUtility.isEmpty(q)) {
            dtoList = dtoList.stream().filter(dto -> dto.getText().contains(q)).collect(Collectors.toList());
        }
        return dtoList;
    }

    /**
     * 部门combobox
     *
     * @return
     */
    @PostMapping(value = "dept_combo_by_querys")
    @ResponseBody
    public List<CommonComboDto> getComForCombobox(int showType, @RequestParam(required = false) String q) {
        logger.info("Get Com For Combobox");
        List<CommonComboDto> resultList = new ArrayList<CommonComboDto>();
        String queryStr = "";

        switch (showType) {
            case 0: //获取所有公司
                queryStr = "{sysMark:'" + IBaseService.SYSMARK + "','propertyMap.sysTypeMark':1}";
                break;
            case 1: //获取所有父公司,即获取所属公司
                queryStr = "{sysMark:'" + IBaseService.SYSMARK + "', 'propertyMap.sysTypeMark':0}";
                break;
            default:
                throw new RuntimeException("");
        }

        List<DeptInfoDto> deptDtoList = userService.getDeptInfo(queryStr, null, null).getRowData();
        for (DeptInfoDto deptDto : deptDtoList) {
            CommonComboDto comboDto = new CommonComboDto();
            comboDto.setValue(deptDto.getId());
            comboDto.setText(deptDto.getDeptCode() + " " + deptDto.getDeptName());
            resultList.add(comboDto);
        }

        if (!VGUtility.isEmpty(q)) {
            resultList = resultList.stream().filter(result -> result.getText().contains(q)).collect(Collectors.toList());
        }

        return resultList;
    }

    @PostMapping(value = "dept_combo_by_userId")
    @ResponseBody
    public List<CommonComboDto> getComForComboboxByUserId() {
        logger.info("Get Com For Combobox By UserId");
        List<CommonComboDto> resultList = new ArrayList<CommonComboDto>();
        List<DeptInfoDto> deptDtoList = baseService.getDeptDtoListByUserId(((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo().getId());

        for (DeptInfoDto deptDto : deptDtoList) {
            CommonComboDto comboDto = new CommonComboDto();
            comboDto.setValue(deptDto.getId());
            comboDto.setText(deptDto.getDeptCode() + " " + deptDto.getDeptName());
            resultList.add(comboDto);
        }

        return resultList;
    }

    //通过物资编码查询资产类别
    @PostMapping(value = "find_asset_type_by_code")
    @ResponseBody
    public Map<String, String> findAssetTypeByMaterialCode(String materialCode) {
        logger.info("Controller: Find Asset Type By Material Code");
        String assetTypeName = new String();

        if (!VGUtility.isEmpty(materialCode))
            assetTypeName = baseService.getAssetTypeByMaterialCode(materialCode);

        Map<String, String> map = new HashMap<String, String>();
        map.put("assetTypeName", assetTypeName);
        return map;
    }

    //通过物资编码查询资产名称
    @PostMapping(value = "find_asset_name_by_code")
    @ResponseBody
    public Map<String, String> findAssetNameByMaterialCode(String materialCode) {
        logger.info("Controller: Find Asset Name By Material Code");
        Map<String, String> assetMap = new HashMap<String, String>();

        if (!VGUtility.isEmpty(materialCode))
            assetMap = baseService.getAssetMapByMaterialCode(materialCode);

        return assetMap;
    }

    @PostMapping(value = "find_person_perm_itemlist_by_userid")
    @ResponseBody
    public List<String> findPersonitemListByUserid(String userId, String deptId) {
        List<String> itemList = new ArrayList<String>();
        try {
            List<PermissionItemDto> permissionItemDtoList = permService.getPermissionListFilterByUser(IBaseService.SYSMARK, userId);
            permissionItemDtoList.stream().forEach(o -> {
                itemList.add(o.getId());
            });
        } catch (Exception e) {
            return itemList;
        }
        return itemList;
    }

    @PostMapping(value = "find_menu_perm_list_by_id")
    @ResponseBody
    public List<String> findMenuPermListById(String id) {
        List<String> result = new ArrayList<String>();
        PermissionValueDto dto = permService.getPermissionValue(id);
        if (!VGUtility.isEmpty(dto))
            result = dto.getMenuValueList().get(IBaseService.SYSMARK);
        return result;
    }

    @PostMapping(value = "find_menu_perm_item_list_by_id")
    @ResponseBody
    public List<String> findMenuPermItemListById(String id) {
        PermissionValueDto dto = permService.getPermissionValue(id);
        if (VGUtility.isEmpty(dto))
            return null;
        return dto.getItemValueList().get(IBaseService.SYSMARK);
    }

    /**
     * 用户combobox
     *
     * @param q
     * @return
     */
    @PostMapping(value = "user_combo")
    @ResponseBody
    public List<CommonComboDto> getUserForCombobox(String q) {
        logger.info("Get User For Combobox");
        List<CommonComboDto> resultList = new ArrayList<CommonComboDto>();
        String queryStr = "{'sysMark':'" + IBaseService.SYSMARK + "'}";

        if (!VGUtility.isEmpty(q))
            queryStr = "{$and:[{$or:[{userName:{$regex:'" + q + "'}},{chsName:{$regex:'" + q + "'}}]},{'sysMark':'" + IBaseService.SYSMARK + "'}]}";

        List<UserInfoDto> userDtoList = userService.getUserInfo(queryStr, null, new PageableDto(1, 20)).getRowData();
        for (UserInfoDto userDto : userDtoList) {
            CommonComboDto comboDto = new CommonComboDto();
            comboDto.setValue(userDto.getId());
            comboDto.setText(userDto.getUserName() + " " + userDto.getChsName());
            comboDto.setValue1(userDto.getUserName());
            comboDto.setValue2(userDto.getChsName());
            resultList.add(comboDto);
        }

        return resultList;
    }


    /***
     *  获取所有所属公司
     * @return
     */
    @RequestMapping(value = "dept_combo")
    @ResponseBody
    public List<CommonComboDto> getComDtoList(String q) {
        List<CommonComboDto> resultList = new ArrayList<>();
        String queryStr = "{'sysMark':'" + IBaseService.SYSMARK + "'}";

        if (!VGUtility.isEmpty(q))
            queryStr = "{$and:[{$or:[{deptCode:{$regex:'" + q + "'}},{deptName:{$regex:'" + q + "'}}]},{'sysMark':'" + IBaseService.SYSMARK + "'}]}";

        List<DeptInfoDto> deptInfoDtos = userService.getDeptInfo(queryStr, null, null).getRowData();
        for (DeptInfoDto dept : deptInfoDtos) {
            CommonComboDto comboDto = new CommonComboDto();
            comboDto.setValue(dept.getId());
            comboDto.setText(dept.getDeptCode() + " " + dept.getDeptName());
            resultList.add(comboDto);
        }
        return resultList;
    }

    /**
     * 根据部门ID,获取用户combobox
     *
     * @param DeptId
     * @return
     */
    @PostMapping(value = "user_comboByDeptId")
    @ResponseBody
    public List<CommonComboDto> getUserForComboboxByDeptId(String DeptId) {
        logger.info("Get User For Combobox");
        List<CommonComboDto> resultList = new ArrayList<CommonComboDto>();

        List<UserInfoDto> userDtoList = userService.getUserInfoByDeptId(DeptId, null, new PageableDto(1, 20)).getRowData();
        for (UserInfoDto userDto : userDtoList) {
            CommonComboDto comboDto = new CommonComboDto();
            comboDto.setValue(userDto.getId());
            comboDto.setText(userDto.getUserName() + " " + userDto.getChsName());
            comboDto.setValue1(userDto.getUserName());
            comboDto.setValue2(userDto.getChsName());
            resultList.add(comboDto);
        }

        return resultList;
    }

    /**
     * 根据公司编码,获取用户combobox
     *
     * @param ComCode
     * @return
     */
    @PostMapping(value = "user_comboByComCode")
    @ResponseBody
    public List<CommonComboDto> getUserForComboboxByComCode(String id, @RequestParam(required = false) String q) {
        logger.info("Get User For Combobox by ComCode");
        List<CommonComboDto> resultList = new ArrayList<CommonComboDto>();
        Object obj = baseService.getDeptInfo(id);
        DeptInfoDto deptInfo = new DeptInfoDto();
        if (!VGUtility.isEmpty(obj)) {
            deptInfo = (DeptInfoDto) baseService.getDeptInfo(id);
            List<UserInfoDto> userDtoList = baseService.getUserInfoByComCode(deptInfo.getDeptCode());
            for (UserInfoDto userDto : userDtoList) {
                CommonComboDto comboDto = new CommonComboDto();
                comboDto.setValue(userDto.getId());
                comboDto.setText(userDto.getUserName() + " " + userDto.getChsName());
                comboDto.setValue1(userDto.getUserName());
                comboDto.setValue2(userDto.getChsName());
                resultList.add(comboDto);
            }
        }

        if (!VGUtility.isEmpty(q)) {
            resultList = resultList.stream().filter(result -> result.getText().contains(q)).collect(Collectors.toList());
        }

        return resultList;
    }

    @PostMapping(value = "create_common_code")
    @ResponseBody
    public void createCommonCode(DictDto dictDto, String converCode) {
        logger.info("Create Common Code");

        List<DictTypeDto> dictTypeDtoList = dictService.getCommonCodeType("{$and:[{key: '" + dictDto.getTypeKey() + "'},{sysMark:'" + IBaseService.SYSMARK + "'}]}", null, null).getRowData();

        if (dictTypeDtoList.size() == 0)
            throw new RuntimeException("数据类型不正确！");
        else
            dictDto.setTypeId(dictTypeDtoList.get(0).getId());

        //特殊处理 需要填写掩码
        Map<String, Object> map = new HashMap<String, Object>();
        if (!VGUtility.isEmpty(converCode))
            map.put("converCode", converCode);
//    	
        dictDto.setPropertyMap(map);
        try {
            dictService.addCommonCode(dictDto);
        } catch (DuplicateException e) {
            throw new RuntimeException("编码已存在！");
        }
    }

    @PostMapping(value = "create_materials_code")
    @ResponseBody
    public void createMaterialsCode(@RequestParam String typeKey, MaterialCodeSyncBean syncBean) {
        logger.info("Create Materials Code");
        DictDto dictDto = new DictDto();
        List<DictTypeDto> dictTypeDtoList = dictService.getCommonCodeType("{$and:[{key: '" + typeKey + "'},{sysMark:'" + IBaseService.SYSMARK + "'}]}", null, null).getRowData();

        if (dictTypeDtoList.size() == 0)
            throw new RuntimeException("数据类型不正确！");
        else
            dictDto.setTypeId(dictTypeDtoList.get(0).getId());

        dictDto.setCode(syncBean.getCode());
        dictDto.setChsName(syncBean.getName());

        Map<String, Object> map = new HashMap<String, Object>();
        if (!VGUtility.isEmpty(syncBean.getW_PRO_CODE()))
            map.put("W_PRO_CODE", syncBean.getW_PRO_CODE());
        if (!VGUtility.isEmpty(syncBean.getW_TYPE_CODE()))
            map.put("W_TYPE_CODE", syncBean.getW_TYPE_CODE());
        if (!VGUtility.isEmpty(syncBean.getPRICE()))
            map.put("PRICE", syncBean.getPRICE());
        if (!VGUtility.isEmpty(syncBean.getMARTERIALS_SPEC()))
            map.put("MARTERIALS_SPEC", syncBean.getMARTERIALS_SPEC());
        if (!VGUtility.isEmpty(syncBean.getW_UNIT_CODE()))
            map.put("W_UNIT_CODE", syncBean.getW_UNIT_CODE());
        if (!VGUtility.isEmpty(syncBean.getW_IS_PRO()))
            map.put("W_IS_PRO", syncBean.getW_IS_PRO());
        if (!VGUtility.isEmpty(syncBean.getIS_DAN()))
            map.put("IS_DAN", syncBean.getIS_DAN());
        if (!VGUtility.isEmpty(syncBean.getIS_DIRECT()))
            map.put("IS_DIRECT", syncBean.getIS_DIRECT());
        if (!VGUtility.isEmpty(syncBean.getMARTERIALS_STATE()))
            map.put("MARTERIALS_STATE", syncBean.getMARTERIALS_STATE());
        if (!VGUtility.isEmpty(syncBean.getBRAND_NAME()))
            map.put("BRAND_NAME", syncBean.getBRAND_NAME());
        if (!VGUtility.isEmpty(syncBean.getEXPIRATION_DATE()))
            map.put("EXPIRATION_DATE", syncBean.getEXPIRATION_DATE());
        if (!VGUtility.isEmpty(syncBean.getIS_DEL()))
            map.put("IS_DEL", syncBean.getIS_DEL());
        dictDto.setPropertyMap(map);

        try {
            dictService.addCommonCode(dictDto);
        } catch (DuplicateException e) {
            throw new RuntimeException("编码已存在！");
        }
    }

    @PostMapping(value = "update_common_code")
    @ResponseBody
    public void updateCommonCode(DictDto dictCommonCodeDto, String converCode) {
        DictDto commonCodeDto = dictService.getCommonCode(dictCommonCodeDto.getId());
        if (VGUtility.isEmpty(dictCommonCodeDto.getParentDictId()))
            commonCodeDto.setParentDictId("");
        else {
            DictDto commonCode = dictService.getCommonCode(dictCommonCodeDto.getParentDictId());
            if (VGUtility.isEmpty(commonCode))
                throw new RuntimeException("请选择正确的父类！");
            commonCodeDto.setParentDictId(dictCommonCodeDto.getParentDictId());
        }
        commonCodeDto.setCode(dictCommonCodeDto.getCode());
        commonCodeDto.setChsName(dictCommonCodeDto.getChsName());
        commonCodeDto.setEngName(dictCommonCodeDto.getEngName());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("converCode", converCode);
        commonCodeDto.setPropertyMap(map);

        dictService.updateCommonCode(commonCodeDto);
    }

    /**
     * 获取数据字典 combobox
     *
     * @param commonCodeType 数据字典类型
     * @return
     */
    @PostMapping(value = "common_combo")
    @ResponseBody
    public List<CommonComboDto> getCommonCodeForCombobox(@RequestParam String commonCodeType, boolean showCode) {
        logger.info("Get Common Code For Combobox {}", commonCodeType);
        List<DictDto> dictDtoList = dictService.getCommonCodeByType(IBaseService.SYSMARK, commonCodeType);
        Collections.sort(dictDtoList, new Comparator<DictDto>() {
            @Override
            public int compare(DictDto o1, DictDto o2) {
                //这里俩个是对属性判null处理，为null的都放到列表最下面
                if (null == o1.getCode()) {
                    return 1;
                }
                if (null == o2.getCode()) {
                    return -1;
                }
                return Collator.getInstance(Locale.ENGLISH).compare(o1.getCode(), o2.getCode());
            }
        });
        return convertDictDtoToCommonDto(dictDtoList, showCode);
    }

    @PostMapping(value = "create_dict_type")
    @ResponseBody
    public void createDictType(DictTypeDto dictTypeDto) {
        logger.info("Create Dict Type");
        dictTypeDto.setSysMark(IBaseService.SYSMARK);
        dictService.addCommonCodeType(dictTypeDto);
    }

    @PostMapping(value = "dict_type_combo")
    @ResponseBody
    public List<CommonComboDto> getDictTypeForCombobox() {
        logger.info("Get Dict Type For Combobox");

        List<CommonComboDto> resultDtoList = new ArrayList<CommonComboDto>();

        List<DictTypeDto> dictTypeDtoList = dictService.getCommonCodeType("{sysMark:'" + IBaseService.SYSMARK + "'}", null, null).getRowData();
        dictTypeDtoList.stream().forEach(o -> {
            resultDtoList.add(convertDictTypeDtoToCommonComboDto(o));
        });
        return resultDtoList;
    }

    private CommonComboDto convertDictTypeDtoToCommonComboDto(DictTypeDto dictTypeDto) {
        CommonComboDto comboDto = new CommonComboDto();

        comboDto.setId(dictTypeDto.getId());
        comboDto.setText(dictTypeDto.getName());
        comboDto.setValue(dictTypeDto.getKey());

        return comboDto;
    }

    /**
     * 获取数据字典 combobox By parentId
     *
     * @param parentId
     * @param showCode
     * @return
     */
    @RequestMapping(value = "common_combo_by_parentid")
    @ResponseBody
    public List<CommonComboDto> getCommonCodeByParentIdForCombobox(@RequestParam String parentId, boolean showCode) {
        logger.info("Get Common Code For Combobox By Parentid");
        List<DictDto> dictDtoList = dictService.getCommonCode("{parentId: '" + parentId + "'}", null, null).getRowData();
        Collections.sort(dictDtoList, new Comparator<DictDto>() {
            @Override
            public int compare(DictDto o1, DictDto o2) {
                //这里俩个是对属性判null处理，为null的都放到列表最下面
                if (null == o1.getCode()) {
                    return 1;
                }
                if (null == o2.getCode()) {
                    return -1;
                }
                return Collator.getInstance(Locale.ENGLISH).compare(o1.getCode(), o2.getCode());
            }
        });
        return convertDictDtoToCommonDto(dictDtoList, showCode);
    }

    /**
     * 数据转换
     *
     * @param dictDtoList
     * @param showCode
     * @return
     */
    private List<CommonComboDto> convertDictDtoToCommonDto(List<DictDto> dictDtoList, boolean showCode) {
        List<CommonComboDto> resultList = new ArrayList<CommonComboDto>();

        for (DictDto dictDto : dictDtoList) {
            CommonComboDto comboDto = new CommonComboDto();
            comboDto.setValue(dictDto.getId());
            comboDto.setValue1(dictDto.getCode());//
            if (showCode) {
                if (!VGUtility.isEmpty(dictDto.getPropertyMap()) && !VGUtility.isEmpty(dictDto.getPropertyMap().get("converCode"))) {
                    comboDto.setText(dictDto.getPropertyMap().get("converCode") + " " + dictDto.getChsName());
                    comboDto.setValue1((String) dictDto.getPropertyMap().get("converCode"));//
                } else
                    comboDto.setText(dictDto.getCode() + " " + dictDto.getChsName());
            } else
                comboDto.setText(dictDto.getChsName());

            resultList.add(comboDto);
        }
        return resultList;
    }

    @PostMapping(value = "common_datagrid")
    @ResponseBody
    public Map<String, Object> getCommonCodeForDataGrid(@RequestParam String commonCodeType,
                                                        @RequestParam(defaultValue = "1") String page,
                                                        @RequestParam(defaultValue = "20") String rows) {
        logger.info("Get Common Code For Combobox By {}", commonCodeType);
        List<BaseCommonCodeDto> resultDtoList = new ArrayList<BaseCommonCodeDto>();

        PageDto<DictDto> dictPageDto = baseService.getCommonCodeForDatagridByQuerys(commonCodeType, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)));
        List<DictDto> commonCodeDtoList = dictPageDto.getRowData();
        for (DictDto dictionaryCommonCodeDto : commonCodeDtoList) {
            resultDtoList.add(convertDictionaryDtoToBaseDto(dictionaryCommonCodeDto));
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", resultDtoList);
        result.put("total", dictPageDto.getTotalCount());
        return result;
    }

    /**
     * 资产台账物资模式的datagrid
     *
     * @return
     */
/*	@PostMapping(value="material_mode_datagrid")
    @ResponseBody
    public Map<String, Object> getAssetMaterialModeForDataGridByQuerys(AssetAssetDto assetAssetDto,
    		@RequestParam(defaultValue = "1") String page,
            @RequestParam(defaultValue = "20") String rows){
		logger.info("Get Material Mode For Datagrid By Querys {}", assetAssetDto);*/

//		baseService.getAssetMaterialModeForDataGridByQuerys(assetAssetDto, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)));
		
		/*String expressionStr = new String();
		String assetNameFromPage = assetAssetDto.getAssetChsName();
		List<BaseStanBookMaterialDto> resultList = new ArrayList<BaseStanBookMaterialDto>();

		String typeId = dictService.getCommonCodeType("{key: 'ASSET_TYPE'}", null, null).getRowData().get(0).getId();
		if(VGUtility.isEmpty(assetNameFromPage)) {
			expressionStr = "{typeId:'"+typeId+"'}";
		} else {
			expressionStr = "{$and:[{typeId:'"+typeId+"'},{chsName:/"+assetNameFromPage+"/}]}";
		}
		
		List<DictDto> dictDtoList = dictService.getCommonCode(expressionStr, null, null).getRowData();
		for(DictDto dictDto:dictDtoList) {
			String materialCode = dictDto.getCode();
			List<AssetAssetDto> assetDtoList = assetService.getAssetDtoByMaterialCode(materialCode,assetAssetDto);
			if(assetDtoList.size() > 0) {
				BaseStanBookMaterialDto materialDto = new BaseStanBookMaterialDto();
				materialDto.setId(dictDto.getId());
				materialDto.setMaterialCode(materialCode);
				materialDto.setAssetCount(Integer.toString(assetDtoList.size()));
				materialDto.setMaterialName(baseService.getAssetNameByMaterialCode(materialCode));
				materialDto.setMaterialType(baseService.getAssetTypeByMaterialCode(materialCode));
				materialDto.setUnitOfMeasStr(assetDtoList.get(0).getUnitOfMeasStr());
				//把assetDtoList中的所有id放在materialDto字段中的list集合中
				List<String> idList = new ArrayList<String>();
				for(AssetAssetDto assetDto:assetDtoList) {
					idList.add(assetDto.getId());
				}
				materialDto.setAssetIdList(idList);
				resultList.add(materialDto);
			}
		}*/

//		return resultList;
		
		/*Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", "");
		result.put("total", "");
		return result;
	}*/
    @PostMapping(value = "permission_datagrid")
    @ResponseBody
    public Map<String, Object> getPermissionForDataGrid(@RequestParam(defaultValue = "1") String page,
                                                        @RequestParam(defaultValue = "20") String rows) {
        logger.info("Get Permission For DataGrid");

        List<BasePermGroupDto> basePermGroupDtoList = new ArrayList<BasePermGroupDto>();

//		List<MenuDto> menuDtoList = menuService.getMenu("{}", null, null).getRowData();
        PageDto<PermissionGroupDto> permissionGroup = permService.getPermissionGroup("{sysMark: '" + IBaseService.SYSMARK + "'}", null, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)));
        for (PermissionGroupDto permGroupDto : permissionGroup.getRowData()) {
            basePermGroupDtoList.add(convertPermGroupDtoToBasePermGroupDto(permGroupDto));
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", basePermGroupDtoList);
        result.put("total", permissionGroup.getTotalCount());
        return result;
    }

    private BasePermGroupDto convertPermGroupDtoToBasePermGroupDto(PermissionGroupDto permGroupDto) {
//		StringBuffer menuValuelistStr = new StringBuffer();
        BasePermGroupDto basePermGroupDto = new BasePermGroupDto();

        basePermGroupDto.setId(permGroupDto.getId());
        basePermGroupDto.setSysMark(permGroupDto.getSysMark());
        basePermGroupDto.setCode(permGroupDto.getCode());
        basePermGroupDto.setChsName(permGroupDto.getChsName());
        basePermGroupDto.setEngName(permGroupDto.getEngName());
        if (!VGUtility.isEmpty(permGroupDto.getPropertyMap()))
            basePermGroupDto.setRemark((String) permGroupDto.getPropertyMap().get("remark"));

//		PermissionValueDto permValueDto = permService.getPermissionValue(permGroupDto.getId());
//		if(!VGUtility.isEmpty(permValueDto)) {
//			Map<String, List<String>> menuValueList = permValueDto.getMenuValueList();
//			List<String> menuList = menuValueList.get(IBaseService.SYSMARK);
//			if(!VGUtility.isEmpty(menuList)) {
//				menuList.stream().forEach(o->{
//					if(menuDtoList.contains(o))
//						menuValuelistStr.append("【"+menuDto.getTitle()+"】");
//				});
//			}
//			basePermGroupDto.setMenuValuelistStr(menuValuelistStr.toString());
//		}

        return basePermGroupDto;
    }

    /***
     * 添加menuId到menuList
     * @param set
     * @return
     */
    @PostMapping(value = "add_menuid_to_menulist")
    @ResponseBody
    public String addMenuidToMenuList(String menuIdArray, String permGroupId) {
        logger.info("Add Menue Id To Menu List {}", menuIdArray);
        List<String> menuIdList = JSON.parseArray(menuIdArray, String.class);
        permService.updatePermission(permGroupId, PermissionType.MENU, IBaseService.SYSMARK, menuIdList);
        return "{\"success\":true}";
    }

    @PostMapping(value = "add_perm_item_list")
    @ResponseBody
    public String addPermItemList(String permItemIdArray, String permGroupId) {
        logger.info("Add Menue Id To Menu List {}", permItemIdArray);
        List<String> permItemIdList = JSON.parseArray(permItemIdArray, String.class);
        permService.updatePermission(permGroupId, PermissionType.ITEM, IBaseService.SYSMARK, permItemIdList);
        return "{\"success\":true}";
    }

    @PostMapping(value = "add_item_to_pserson_itemlist")
    @ResponseBody
    public String addPersonPermItemList(String itemIdArray, String personId, String deptId) {
        logger.info("Add Menue Id To Person Menu List {}", itemIdArray);
        List<String> permItemIdList = JSON.parseArray(itemIdArray, String.class);
        permService.updatePermission(personId + "," + deptId, PermissionType.ITEM, IBaseService.SYSMARK, permItemIdList);
        return "{\"success\":true}";
    }

    @PostMapping(value = "perm_item_datagrid")
    @ResponseBody
    public Map<String, Object> getPermItemForDataGrid(@RequestParam(defaultValue = "1") String page,
                                                      @RequestParam(defaultValue = "20") String rows) {
        logger.info("Get Permission Item For DataGrid");
        PageDto<PermissionItemDto> permItemPageDto = permService.getPermissionItem("{sysMark: '" + IBaseService.SYSMARK + "'}", null, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)));
        List<PermissionItemDto> permItemDtoList = permItemPageDto.getRowData();

        List<BasePermItemDto> basePermItemDtoList = new ArrayList<BasePermItemDto>();
        permItemDtoList.stream().forEach(o -> {
            BasePermItemDto basePermItemDto = new BasePermItemDto();

            basePermItemDto.setId(o.getId());
            basePermItemDto.setSysMark(o.getSysMark());
            basePermItemDto.setCode(o.getCode());
            basePermItemDto.setChsName(o.getChsName());
            basePermItemDto.setEngName(o.getEngName());
            if (!VGUtility.isEmpty(o.getPropertiesMap()))
                basePermItemDto.setMenuPermName((String) o.getPropertiesMap().get("menuPermName"));

            basePermItemDtoList.add(basePermItemDto);
        });

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", basePermItemDtoList);
        result.put("total", permItemPageDto.getTotalCount());
        return result;
    }

    @PostMapping(value = "perm_user_datagrid")
    @ResponseBody
    public List<BaseUserDto> getPermUserForDataGrid(String permGroupId, @RequestParam(defaultValue = "") String q) {
        logger.info("Get Permission User For DataGrid By {} {}", permGroupId, q);
        String expression = new String();
        String tempStr = "{'deptList.permGroupIdList':'" + permGroupId + "'}";
        BaseUserDto baseUserDto = new BaseUserDto();
        List<String> permGroupIdList = new ArrayList<String>();
        List<BaseUserDto> result = new ArrayList<BaseUserDto>();

        if (VGUtility.isEmpty(q))
            expression = tempStr;
        else
            expression = "{$and:[{$or:[{chsName:{$regex:'" + q + "'}},{userName:{$regex:'" + q + "'}}]}," + tempStr + "]}";

        PageDto<UserInfoDto> userInfoPageDto = userService.getUserInfo(expression, null, null);
        List<UserInfoDto> userInfoDtoList = userInfoPageDto.getRowData();
        for (UserInfoDto userInfoDto : userInfoDtoList) {
            List<Map<String, Object>> deptMapList = userInfoDto.getDeptList();
            for (Map<String, Object> deptMap : deptMapList) {
                if (!VGUtility.isEmpty(deptMap.get("permGroupIdList"))) {
                    permGroupIdList = (List<String>) deptMap.get("permGroupIdList");
                    if (permGroupIdList.contains(permGroupId)) {
                        baseUserDto = baseService.getUserInfoByUserIdAndDeptId(userInfoDto.getId(), (String) deptMap.get("deptId"));
                        if (!VGUtility.isEmpty(baseUserDto))
                            result.add(baseUserDto);
                    }
                }
            }
        }

        return result;
    }

    @PostMapping(value = "perm_user_datagrid_code")
    @ResponseBody
    public List<CommonComboDto> getPermUserForDataGridByCode(String code, @RequestParam(defaultValue = "") String q) {
        logger.info("Get Permission User For DataGrid By {} {}", code, q);
        String expression = new String();
        PermissionGroupDto pre = baseService.getPerByCode(code);
        String tempStr = "";
        String permGroupId = "";
        if (!VGUtility.isEmpty(pre)) {
            permGroupId = pre.getId();
        }
        tempStr = "{'deptList.permGroupIdList':'" + permGroupId + "'}";
        BaseUserDto baseUserDto = new BaseUserDto();
        List<String> permGroupIdList = new ArrayList<String>();
        List<CommonComboDto> result = new ArrayList<CommonComboDto>();

        if (VGUtility.isEmpty(q))
            expression = tempStr;
        else
            expression = "{$and:[{$or:[{chsName:{$regex:'" + q + "'}},{userName:{$regex:'" + q + "'}}]}," + tempStr + "]}";

        PageDto<UserInfoDto> userInfoPageDto = userService.getUserInfo(expression, null, null);
        List<UserInfoDto> userInfoDtoList = userInfoPageDto.getRowData();
        for (UserInfoDto userInfoDto : userInfoDtoList) {
            List<Map<String, Object>> deptMapList = userInfoDto.getDeptList();
            for (Map<String, Object> deptMap : deptMapList) {
                if (!VGUtility.isEmpty(deptMap.get("permGroupIdList"))) {
                    permGroupIdList = (List<String>) deptMap.get("permGroupIdList");
                    if (permGroupIdList.contains(permGroupId)) {
                        baseUserDto = baseService.getUserInfoByUserIdAndDeptId(userInfoDto.getId(), (String) deptMap.get("deptId"));
                        if (!VGUtility.isEmpty(baseUserDto)) {
                            CommonComboDto comboDto = new CommonComboDto();
                            comboDto.setValue(userInfoDto.getId());
                            comboDto.setText(userInfoDto.getUserName() + " " + userInfoDto.getChsName());
                            result.add(comboDto);
                        }
                    }
                }
            }
        }

        return result;
    }

    /***
     *
     * @param deptId 部门Id
     * @return
     */
    @PostMapping(value = "dept_user_datagrid")
    @ResponseBody
    public List<BaseUserDto> getDeptUserForDataGrid(@RequestParam(required = false) String q, @RequestParam(required = false) String deptId) {
        logger.info("Get Users in Dept For DataGrid By Dept Id {}", deptId);
        List<UserInfoDto> userInfoDtoList = userService.getUserInfoByDeptId(IBaseService.SYSMARK, deptId, null).getRowData();
        List<BaseUserDto> baseUserDtoList = new ArrayList<BaseUserDto>();
        for (UserInfoDto userInfoDto : userInfoDtoList) {
            if (!VGUtility.isEmpty(q) && !userInfoDto.getChsName().contains(q.trim()) && !userInfoDto.getUserName().contains(q.trim()))
                continue;
            baseUserDtoList.add(convertUserToBaseUser(userInfoDto, deptId));
        }
        return baseUserDtoList;
    }

    private BaseUserDto convertUserToBaseUser(UserInfoDto userInfoDto, String deptId) {
        BaseUserDto baseUserDto = new BaseUserDto();
        baseUserDto.setId(userInfoDto.getId());
        baseUserDto.setUserName(userInfoDto.getUserName());
        baseUserDto.setChsName(userInfoDto.getChsName());
        if (!VGUtility.isEmpty(deptId)) {
            List<Map<String, Object>> deptMapList = userInfoDto.getDeptList();
            for (Map<String, Object> deptMap : deptMapList) {
                if (!VGUtility.isEmpty(deptMap.get("deptId")) && deptMap.get("deptId").equals(deptId)) {
                    if (!VGUtility.isEmpty(deptMap.get("manager")))
                        baseUserDto.setManager((boolean) deptMap.get("manager"));
                    if (!VGUtility.isEmpty(deptMap.get("leader")))
                        baseUserDto.setLeader((boolean) deptMap.get("leader"));
                }
            }
        }

        return baseUserDto;
    }

    @PostMapping(value = "dept_user_combobox")
    @ResponseBody
    public List<CommonComboDto> getDeptUserForCombobox(@RequestParam(required = false) String deptId, @RequestParam(required = false) String q,
                                                       @RequestParam(required = false, defaultValue = "0") String showChild, @RequestParam(defaultValue = "1") String page,
                                                       @RequestParam(defaultValue = "20") String rows) {
        logger.info("Get Dept User For Combobox By Dept Id {}", deptId);
        List<CommonComboDto> resultList = new ArrayList<CommonComboDto>();


        if ("0".equals(showChild)) {
            //仅获取本部门的人员数据
            getUserinfoComboByDeptId(deptId, resultList);
        } else {
            //获取所有子部门的人员数据；
            List<CommonComboDto> parentDeptIds = baseService.getDeptForComboboxByParentDeptId(deptId);
            if (null != parentDeptIds && parentDeptIds.size() > 0) {
                for (CommonComboDto parentDeptId : parentDeptIds) {
                    String dep = parentDeptId.getValue();
                    getUserinfoComboByDeptId(dep, resultList);
                }

            }
        }


        if (!VGUtility.isEmpty(q)) {
            resultList = resultList.stream().filter(result -> result.getText().contains(q)).collect(Collectors.toList());
        }
        return resultList;
    }

    private void getUserinfoComboByDeptId(String deptId, List<CommonComboDto> resultList) {
        if (!VGUtility.isEmpty(deptId)) {
            //根据部门查询部门下的人员,不进行分页
            //List<UserInfoDto> userInfoDtoList = userService.getUserInfoByDeptId(IBaseService.SYSMARK, deptId, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows))).getRowData();
            List<UserInfoDto> userInfoDtoList = userService.getUserInfoByDeptId(IBaseService.SYSMARK, deptId, null).getRowData();
            for (UserInfoDto userInfoDto : userInfoDtoList) {
                CommonComboDto comboDto = new CommonComboDto();
                comboDto.setValue(userInfoDto.getId());
                if (!VGUtility.isEmpty(userInfoDto.getPropertyMap())) {
                    comboDto.setValue1(userInfoDto.getPropertyMap().get("EMP_NO").toString());
                }
                comboDto.setText(userInfoDto.getUserName() + " " + userInfoDto.getChsName());
                resultList.add(comboDto);
            }
        }
    }

    private BaseCommonCodeDto convertDictionaryDtoToBaseDto(DictDto dictionaryDto) {
        BaseCommonCodeDto baseDto = new BaseCommonCodeDto();

        baseDto.setId(dictionaryDto.getId());
        baseDto.setCode(dictionaryDto.getCode());
        baseDto.setTypeId(dictionaryDto.getTypeId());
        baseDto.setTypeName(dictionaryDto.getTypeName());
        baseDto.setChsName(dictionaryDto.getChsName());
        baseDto.setEngName(dictionaryDto.getEngName());
        if (!VGUtility.isEmpty(dictionaryDto.getPropertyMap())) {
            baseDto.setConverCode((String) dictionaryDto.getPropertyMap().get("converCode"));
            baseDto.setRunningNum((String) dictionaryDto.getPropertyMap().get("runningNum"));
            baseDto.setW_PRO_CODE((String) dictionaryDto.getPropertyMap().get("W_PRO_CODE"));
            baseDto.setW_TYPE_CODE((String) dictionaryDto.getPropertyMap().get("W_TYPE_CODE"));
            baseDto.setPRICE(VGUtility.toDoubleStr((Double) dictionaryDto.getPropertyMap().get("PRICE"), "#.##"));
            baseDto.setMARTERIALS_SPEC((String) dictionaryDto.getPropertyMap().get("MARTERIALS_SPEC"));
            baseDto.setW_UNIT_CODE((String) dictionaryDto.getPropertyMap().get("W_UNIT_CODE"));
            baseDto.setW_IS_PRO((String) dictionaryDto.getPropertyMap().get("W_IS_PRO"));
            baseDto.setIS_DAN((String) dictionaryDto.getPropertyMap().get("IS_DAN"));
            baseDto.setIS_DIRECT((String) dictionaryDto.getPropertyMap().get("IS_DIRECT"));
            baseDto.setMARTERIALS_STATE((String) dictionaryDto.getPropertyMap().get("MARTERIALS_STATE"));
            baseDto.setBRAND_NAME((String) dictionaryDto.getPropertyMap().get("BRAND_NAME"));
            baseDto.setEXPIRATION_DATE((String) dictionaryDto.getPropertyMap().get("EXPIRATION_DATE"));
            baseDto.setIS_DEL((String) dictionaryDto.getPropertyMap().get("IS_DEL"));
        }
        baseDto.setParentCodeId(dictionaryDto.getParentDictId());
        if (!VGUtility.isEmpty(dictionaryDto.getParentDictCode()))
            baseDto.setParentCodeName(dictionaryDto.getParentDictCode() + " " + dictionaryDto.getParentDictChsName());

        return baseDto;
    }

    @PostMapping(value = "get_place_datagrid")
    @ResponseBody
    public Map<String, Object> getAssetPlaceForDatagrid(@RequestParam(required = false) String code,
                                                        @RequestParam(defaultValue = "1") String page,
                                                        @RequestParam(defaultValue = "10") String rows) {
        logger.info("Get Save Place For Datagrid");

        List<DictTypeDto> dictTypeDtoList = dictService.getCommonCodeType("{key: 'SAVE_PLACE'}", null, null).getRowData();
        String id = dictTypeDtoList.get(0).getId();

        //只有启用状态下的位置可以选择
        PageDto<DictDto> commonCodeForDatagrid = dictService.getCommonCode("{$and:[{typeId:'" + id + "'},{code:{$regex:'" + code + "'}},{'propertyMap.LOCATION_STATUS':{'$ne':'0'}}]}", null, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)));

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", commonCodeForDatagrid.getRowData());
        result.put("total", commonCodeForDatagrid.getTotalCount());
        return result;
    }

    @PostMapping(value = "user_datagrid")
    @ResponseBody
    public Map<String, Object> getUserByQuerysForDataGrid(@RequestParam(required = false) String q,
                                                          @RequestParam(defaultValue = "1") String page,
                                                          @RequestParam(defaultValue = "20") String rows) {
        logger.info("Controller: Get user By Querys {} For DataGrid", q);
        String criteriaStr = new String();
        List<BaseUserDto> baseUserDtoList = new ArrayList<BaseUserDto>();

        if (VGUtility.isEmpty(q))
            criteriaStr = "{'sysMark':'" + IBaseService.SYSMARK + "'}";
        else //{'propertyMap.userCode':{$regex:'"+q+"'}} 使用code查询
            criteriaStr = "{$and:[{$or:[{userName:{$regex:'" + q + "'}},{chsName:{$regex:'" + q + "'}}]},{'sysMark':'" + IBaseService.SYSMARK + "'}]}";

        /**
         * 获取所有部门
         */
        PageDto<DeptInfoDto> deptDtoList = userService.getDeptInfo("{sysMark:'" + IBaseService.SYSMARK + "'}", null, null);
        Map<String, DeptInfoDto> deptMap = baseService.getDeptDtoByDeptList(deptDtoList);

        PageDto<UserInfoDto> userPageDto = userService.getUserInfo(criteriaStr, null, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)));
        List<UserInfoDto> userInfoDtoList = userPageDto.getRowData();
        userInfoDtoList.stream().forEach(o -> {
            BaseUserDto baseUserDto = new BaseUserDto();
            baseUserDto.setId(o.getId());
//			baseUserDto.setUserCode((String)o.getPropertyMap().get("userCode"));
            baseUserDto.setUserName(o.getUserName());
            baseUserDto.setChsName(o.getChsName());
            String deptName = "";
            if (!VGUtility.isEmpty(o.getDeptList())) {
                if (o.getDeptList().size() > 1) {
                    for (int i = 0; i < o.getDeptList().size(); i++) {
                        if (!VGUtility.isEmpty(deptMap.get(o.getDeptList().get(i).get("deptId")))) {
                            deptName = deptName + "," + deptMap.get(o.getDeptList().get(i).get("deptId")).getDeptName();
                        }
                    }
                    deptName = deptName.substring(1);
                } else {
                    if (o.getDeptList().size() > 0 && !VGUtility.isEmpty(deptMap.get(o.getDeptList().get(0).get("deptId")))) {
                        deptName = deptMap.get(o.getDeptList().get(0).get("deptId")).getDeptName();
                    }
                }
            }
            baseUserDto.setPermDeptName(deptName);
            baseUserDtoList.add(baseUserDto);
        });

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", baseUserDtoList);
        result.put("total", userPageDto.getTotalCount());
        return result;
    }

    @PostMapping(value = "menu_perm_tree")
    @ResponseBody
    public List<CommonTreeDto> getMenuForTree() {
        List<CommonTreeDto> resultList = new ArrayList<CommonTreeDto>();

        List<MenuDto> menuBySysMark = menuService.getMenuBySysMark(IBaseService.SYSMARK);
        menuBySysMark.stream().forEach(o -> {
            CommonTreeDto commonTreeDto = menuService.convert(o, null, null);
            commonTreeDto.setValue(commonTreeDto.getId());
            resultList.add(commonTreeDto);
        });

        return resultList;
    }

    @PostMapping(value = "menu_perm_item_tree")
    @ResponseBody
    public List<CommonTreeDto> getPermItemForTree() {
        List<CommonTreeDto> resultDtoList = new ArrayList<CommonTreeDto>();
        CommonTreeDto specialTreeDto = new CommonTreeDto();
        specialTreeDto.setText("特殊权限");
        specialTreeDto.setChildren(new ArrayList<CommonTreeDto>());
        List<CommonTreeDto> specialChildrenDtoList = specialTreeDto.getChildren();

        PageDto<PermissionItemDto> permItemPageDto = permService.getPermissionItem("{'sysMark':'" + IBaseService.SYSMARK + "'}", null, null);
        List<PermissionItemDto> permItemDtoList = permItemPageDto.getRowData();
        permItemDtoList.stream().forEach(o -> {
            CommonTreeDto commonTreeDto = new CommonTreeDto();
            commonTreeDto.setId(o.getId());
            commonTreeDto.setText(o.getChsName());

            Map<String, Object> tempMap = o.getPropertiesMap();
            if (VGUtility.isEmpty(tempMap)) { //为空 设置为特殊权限
                specialChildrenDtoList.add(commonTreeDto);
            } else {
                String menuPermName = (String) tempMap.get("menuPermName");
                if (VGUtility.isEmpty(menuPermName)) { //没有菜单权限 设置为特殊权限
                    specialChildrenDtoList.add(commonTreeDto);
                } else {
                    boolean filled = false;
                    for (CommonTreeDto resultDto : resultDtoList) {
                        if (resultDto.getText().equals(menuPermName)) {
                            resultDto.getChildren().add(commonTreeDto);
                            filled = true;
                        }
                    }

                    if (!filled) {
                        CommonTreeDto menuPermDto = new CommonTreeDto();
                        menuPermDto.setText(menuPermName);
                        List<CommonTreeDto> childrenDtoList = new ArrayList<CommonTreeDto>();
                        childrenDtoList.add(commonTreeDto);
                        menuPermDto.setChildren(childrenDtoList);
                        resultDtoList.add(menuPermDto);
                    }
                }
            }
        });

        if (specialTreeDto.getChildren().size() != 0)
            resultDtoList.add(specialTreeDto);

        return resultDtoList;
    }

    @PostMapping(value = "menu_perm_combobox")
    @ResponseBody
    public List<CommonComboDto> getMenuPermForCombobox() {
        List<CommonComboDto> comboDtoList = new ArrayList<CommonComboDto>();
        PageDto<MenuDto> pageDto = menuService.getMenu("{'sysMark':'" + IBaseService.SYSMARK + "'}", null, null);
        List<MenuDto> menuDtoList = pageDto.getRowData();
        menuDtoList.stream().forEach(o -> {
            if (!VGUtility.isEmpty(o.getAddress())) {
                CommonComboDto comboDto = new CommonComboDto();
                comboDto.setId(o.getId());
                comboDto.setText(o.getTitle());
                comboDto.setValue(o.getId());
                comboDtoList.add(comboDto);
            }
        });
        return comboDtoList;
    }

    @PostMapping(value = "create_user")
    @ResponseBody
    public void createUser(BaseUserDto baseUserDto) {
        logger.info("Create User");

        if (VGUtility.isEmpty(baseUserDto.getUserName())) throw new RuntimeException("用户名不能为空！");
        if (VGUtility.isEmpty(baseUserDto.getChsName())) throw new RuntimeException("中文名不能为空！");

        UserInfoDto userDto = new UserInfoDto();
        userDto.setUserName(baseUserDto.getUserName());
        userDto.setChsName(baseUserDto.getChsName());
        userDto.setPassword("123456");//初始化密码
        userDto.setSysMark(IBaseService.SYSMARK);

        try {
            userService.saveUserInfo(userDto);
        } catch (Exception e) {
            throw new RuntimeException("该用户名已存在！");
        }
    }

    @PostMapping(value = "update_user")
    @ResponseBody
    public void updateUser(BaseUserDto baseUserDto) {
        logger.info("Update User");

        if (VGUtility.isEmpty(baseUserDto.getUserName())) throw new RuntimeException("用户名不能为空！");
        if (VGUtility.isEmpty(baseUserDto.getChsName())) throw new RuntimeException("中文名不能为空！");

        UserInfoDto userDto = userService.getUserInfo(baseUserDto.getId());
        userDto.setUserName(baseUserDto.getUserName());
        userDto.setChsName(baseUserDto.getChsName());

        try {
            userService.saveUserInfo(userDto);
        } catch (Exception e) {
            throw new RuntimeException("该用户名已存在！");
        }
    }

    @PostMapping(value = "create_perm")
    @ResponseBody
    public void createPermission(BasePermGroupDto basePermGroupDto) {
        logger.info("Create Permission");

        if (VGUtility.isEmpty(basePermGroupDto.getCode()))
            throw new RuntimeException("角色编码不能为空！");
        if (VGUtility.isEmpty(basePermGroupDto.getChsName()))
            throw new RuntimeException("角色名称不能为空！");

        PermissionGroupDto permGroupDto = new PermissionGroupDto();
        permGroupDto.setSysMark(IBaseService.SYSMARK);
        permGroupDto.setCode(basePermGroupDto.getCode());
        permGroupDto.setChsName(basePermGroupDto.getChsName());

        Map<String, Object> propertyMap = new HashMap<String, Object>();
        propertyMap.put("remark", basePermGroupDto.getRemark());
        permGroupDto.setPropertyMap(propertyMap);

        try {
            permService.addPermisssionGroup(permGroupDto);
        } catch (RuntimeException e) {
            throw new RuntimeException("角色编码已存在！");
        }
    }

    @PostMapping(value = "update_perm")
    @ResponseBody
    public void updatePermission(BasePermGroupDto basePermGroupDto) {
        logger.info("Update Permission");

        if (VGUtility.isEmpty(basePermGroupDto.getCode()))
            throw new RuntimeException("角色编码不能为空！");
        if (VGUtility.isEmpty(basePermGroupDto.getChsName()))
            throw new RuntimeException("角色名称不能为空！");

        PermissionGroupDto permGroupDto = permService.getPermissonGroup(basePermGroupDto.getId());
        String oldRoleCode = permGroupDto.getCode();
        String newRoleCode = basePermGroupDto.getCode();
        if (!newRoleCode.equals(oldRoleCode)) {
            //更新映射表
            baseService.updateEmpCodeMapRoleCodeByRoleCode(oldRoleCode, newRoleCode);
        }
        permGroupDto.setCode(basePermGroupDto.getCode());
        permGroupDto.setChsName(basePermGroupDto.getChsName());
        Map<String, Object> propertyMap = permGroupDto.getPropertyMap();
        if (VGUtility.isEmpty(propertyMap))
            propertyMap = new HashMap<String, Object>();
        propertyMap.put("remark", basePermGroupDto.getRemark());
        permGroupDto.setPropertyMap(propertyMap);

        try {
            permService.updatePermissionGroup(permGroupDto);
        } catch (RuntimeException e) {
            throw new RuntimeException("角色编码已存在！");
        }
    }

    @PostMapping(value = "delete_perm")
    @ResponseBody
    public void deletePermission(String id) {
        logger.info("Delete Permission");
        permService.deletePermissionGroupById(id);
    }

    @PostMapping(value = "delete_perm_item")
    @ResponseBody
    public void deletePermissionItem(String id) {
        logger.info("Delete Permission Item");
        permService.deletePermissionItem(id);
    }

    @PostMapping(value = "delete_perm_user")
    @ResponseBody
    public void deletePermissionUser(String permGroupId, String userId, String deptId, String code) {
        logger.info("Delete Permission User");
        List<String> permGroupIdList = new ArrayList<String>();
//        List<String> permGroupCodeList = new ArrayList<>();

        UserInfoDto userInfo = userService.getUserInfo(userId);
        List<Map<String, Object>> deptMapList = userInfo.getDeptList();
        for (Map<String, Object> deptMap : deptMapList) {
            if (!VGUtility.isEmpty(deptMap.get("deptId")) && deptMap.get("deptId").equals(deptId)) {
                permGroupIdList = (List<String>) deptMap.get("permGroupIdList");
                permGroupIdList.remove(permGroupId);
                deptMap.put("permGroupIdList", permGroupIdList);
                //删除权限编码到人员表中
//                permGroupCodeList = (List<String>) deptMap.get("permGroupCodeList");
//                permGroupCodeList.remove(code);
//                deptMap.put("permGroupCodeList", permGroupCodeList);
                userService.saveUserInfo(userInfo);
                break;
            }
        }
        //删除映射表
        if (!VGUtility.isEmpty(userInfo.getPropertyMap()) && !VGUtility.isEmpty(userInfo.getPropertyMap().get("EMP_NO"))) {
            String emp_no = userInfo.getPropertyMap().get("EMP_NO").toString();
            baseService.deleteEmpCodeMapRoleCodeByEmpCodeAndRoleCode(emp_no, code);
        }

        permService.deleteGroupUserById(permGroupId, userId);
    }

    //删除部门用户
    @PostMapping(value = "delete_dept_user")
    @ResponseBody
    public void deleteDeptUser(String deptId, String userIdListStr) {
        logger.info("Delete Deptment User");
        List<String> userIdList = new ArrayList<String>();

        if (VGUtility.isEmpty(userIdListStr))
            throw new RuntimeException("删除用户不能为空！");

        Arrays.stream(userIdListStr.split(",")).forEach(arr -> userIdList.add(arr));

        for (String userId : userIdList) {
            userService.deleteUserFromDept(userId, deptId);
        }
    }

    @PostMapping(value = "create_perm_item")
    @ResponseBody
    public void createPermItem(BasePermItemDto basePermItemDto) {
        logger.info("Create Permission Item");

        if (VGUtility.isEmpty(basePermItemDto.getCode())) throw new RuntimeException("角色权限编码不能为空！");
        if (VGUtility.isEmpty(basePermItemDto.getChsName())) throw new RuntimeException("角色权限名称不能为空！");

        PermissionItemDto permItemDto = new PermissionItemDto();
        permItemDto.setSysMark(IBaseService.SYSMARK);
        permItemDto.setCode(basePermItemDto.getCode());
        permItemDto.setChsName(basePermItemDto.getChsName());

        Map<String, Object> propertiesMap = new HashMap<String, Object>();
        propertiesMap.put("menuPermName", basePermItemDto.getMenuPermName());
        permItemDto.setPropertiesMap(propertiesMap);

        try {
            permService.createPermissionItem(permItemDto);
        } catch (RuntimeException e) {
            throw new RuntimeException("角色权限编码已存在！");
        }
    }

    @PostMapping(value = "update_perm_item")
    @ResponseBody
    public void updatePermItem(BasePermItemDto basePermItemDto) {
        logger.info("Update Permission Item");

        if (VGUtility.isEmpty(basePermItemDto.getCode())) throw new RuntimeException("权限项编码不能为空！");
        if (VGUtility.isEmpty(basePermItemDto.getChsName())) throw new RuntimeException("权限项名称不能为空！");

        PermissionItemDto permItemDto = permService.getPermissionItem(basePermItemDto.getId());
        permItemDto.setSysMark(IBaseService.SYSMARK);
        permItemDto.setCode(basePermItemDto.getCode());
        permItemDto.setChsName(basePermItemDto.getChsName());

        Map<String, Object> propertiesMap = permItemDto.getPropertiesMap();
        if (VGUtility.isEmpty(propertiesMap))
            propertiesMap = new HashMap<String, Object>();
        propertiesMap.put("menuPermName", basePermItemDto.getMenuPermName());
        permItemDto.setPropertiesMap(propertiesMap);

        try {
            permService.updatePermissionItem(permItemDto);
        } catch (RuntimeException e) {
            throw new RuntimeException("权限项编码已存在！");
        }
    }

    @PostMapping(value = "set_perm_user")
    @ResponseBody
    public void setPermUser(@RequestParam String permGroupId,
                            @RequestParam String deptId,
                            @RequestParam String userId, @RequestParam String code) {
        logger.info("Add Permission Item By {}", permGroupId);

        List<String> permGroupIdList = new ArrayList<String>();
//        List<String> permGroupCodeList = new ArrayList<>();
        BaseUserDto baseUserDto = baseService.getUserInfoByUserIdAndDeptId(userId, null);

        if (VGUtility.isEmpty(baseService.getDeptInfoByDeptId(deptId)))
            throw new RuntimeException("部门名称选择不正确！");
        if (VGUtility.isEmpty(baseUserDto))
            throw new RuntimeException("新增用户选择不正确！");

        permService.addGroupUser(permGroupId, userId);
        //权限组添加到 人员->部门列表->权限组列表中
        UserInfoDto userInfoDto = userService.getUserInfo(userId);
        List<Map<String, Object>> deptMapList = userInfoDto.getDeptList();
        for (Map<String, Object> deptMap : deptMapList) {
            if (!VGUtility.isEmpty(deptMap.get("deptId")) && deptMap.get("deptId").equals(deptId)) {
                if (!VGUtility.isEmpty(deptMap.get("permGroupIdList"))) {
                    permGroupIdList = (List<String>) deptMap.get("permGroupIdList");
                }
                permGroupIdList.add(permGroupId);
                deptMap.put("permGroupIdList", permGroupIdList);
                //添加权限编码到人员表中
//                if (!VGUtility.isEmpty(deptMap.get("permGroupCodeList"))) {
//                    permGroupCodeList = (List<String>) deptMap.get("permGroupCodeList");
//                }
//                permGroupCodeList.add(code);
//                deptMap.put("permGroupCodeList", permGroupCodeList);
                break;
            }
        }

        //创建映射表
        if (!VGUtility.isEmpty(userInfoDto.getPropertyMap()) && !VGUtility.isEmpty(userInfoDto.getPropertyMap().get("EMP_NO"))) {
            String emp_no = userInfoDto.getPropertyMap().get("EMP_NO").toString();
            EmpCodeMapRoleCodeDto dto = new EmpCodeMapRoleCodeDto();
            dto.setEmpCode(emp_no);
            dto.setRoleCode(code);
            baseService.createEmpCodeMapRoleCode(dto);
        }
        userService.saveUserInfo(userInfoDto);
    }

    /**
     * 添加人员至部门
     *
     * @param id
     * @param userId
     */
    @PostMapping(value = "set_dept_user")
    @ResponseBody
    public void setDeptUser(@RequestParam String deptId,
                            @RequestParam String userId,
                            @RequestParam boolean manager,
                            @RequestParam boolean leader) {
        logger.info("Set Dept User By {} {}", deptId, userId);

        if (VGUtility.isEmpty(userId)) {
            throw new RuntimeException("新增用户不能为空！");
        } else {
            try {
                userService.getUserInfo(userId);
            } catch (Exception e) {
                throw new RuntimeException("新增用户选择不正确！");
            }
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("manager", manager);
        map.put("leader", leader);
        userService.addUserToDept(userId, deptId, map);
    }

//	@PostMapping(value = "set_dept_user_manager")
//    @ResponseBody
//    public void setDeptUserManager(@RequestParam String deptId, @RequestParam String userId, @RequestParam boolean manager) {
//		logger.info("Add Permission Item By {} {}", deptId, userId);
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("manager", manager);
//		userService.addUserToDept(userId, deptId, map);
//	}

    @PostMapping(value = "create_dept")
    @ResponseBody
    public void createDept(BaseDeptDto baseDeptDto) {
        logger.info("Create Dept");

        DeptInfoDto deptDto = new DeptInfoDto();
        deptDto.setSysMark(IBaseService.SYSMARK);
        deptDto.setPdId(baseDeptDto.getPdId());
        deptDto.setDeptCode(baseDeptDto.getDeptCode());
        deptDto.setDeptName(baseDeptDto.getDeptName());
        userService.saveDeptInfo(deptDto);
    }

    /**
     * 导入安装位置
     *
     * @param fileData
     * @return
     */
    @PostMapping(value = "import_save_place_xls")
    @ResponseBody
    public String importSavePlaceXls(UploadFileDto fileData) {
        logger.info("Import Save Place Xls");

        List<DictTypeDto> dictTypeDtoList = dictService.getCommonCodeType("{$and:[{key: 'SAVE_PLACE'},{sysMark:'" + IBaseService.SYSMARK + "'}]}", null, null).getRowData();

        try {
            HSSFWorkbook wb = new HSSFWorkbook(fileData.getUploadFileData().getInputStream());
            HSSFSheet sheet = wb.getSheetAt(0);
            if (sheet != null) {
                for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                    HSSFRow row = sheet.getRow(i);

                    if (VGUtility.isEmpty(row.getCell(0)))
                        continue;

                    String code = row.getCell(0).getStringCellValue().trim();
                    String chsName = row.getCell(1).getStringCellValue().trim();
                    System.out.println(i + ":" + code + " " + chsName);

                    if (dictService.getCommonCode("{$and:[{typeId:'" + dictTypeDtoList.get(0).getId() + "'},{code:'" + code + "'}]}", null, null).getRowData().size() > 0)
                        continue;

                    DictDto dictDto = new DictDto();
                    dictDto.setTypeId(dictTypeDtoList.get(0).getId()); //mangodb 安装位置id
                    dictDto.setCode(code);
                    dictDto.setChsName(chsName);
                    dictService.addCommonCode(dictDto);
                }
            }
        } catch (Exception e) {
            logger.error("Exception when read Excel File", e);
            throw new RuntimeException("导入数据出错!");
        }
        return "{\"success\":true}";
    }

    /**
     * 数据字典导入
     *
     * @param fileData
     * @return
     */
    @PostMapping(value = "import_dict")
    @ResponseBody
    public String importDict(UploadFileDto fileData) {
        logger.info(fileData.getDictPdType() + " " + fileData.getDictType());

        DictDto tempDto = new DictDto();
        DictTypeDto dictTypePdDto = new DictTypeDto();
        DictTypeDto dictTypeDto = dictService.getCommonCodeType("{$and:[{key: '" + fileData.getDictType() + "'},{sysMark:'" + IBaseService.SYSMARK + "'}]}", null, null).getRowData().get(0); //数据字典类型Id
        if (!VGUtility.isEmpty(fileData.getDictPdType()))
            dictTypePdDto = dictService.getCommonCodeType("{$and:[{key: '" + fileData.getDictPdType() + "'},{sysMark:'" + IBaseService.SYSMARK + "'}]}", null, null).getRowData().get(0); //数据字典父类Id

        logger.info("Import Dict By DictTypeDto {} DictTypePdDto {}", dictTypeDto.getName(), VGUtility.isEmpty(dictTypePdDto) ? "" : dictTypePdDto.getName());

        try {
            HSSFWorkbook wb = new HSSFWorkbook(fileData.getUploadFileData().getInputStream());
            HSSFSheet sheet = wb.getSheetAt(0);
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                int colNum = 0;
                HSSFRow row = sheet.getRow(i);
                if (VGUtility.isEmpty(row.getCell(0)))
                    continue;

                String code = baseService.getCellFormatValue(row.getCell(colNum++));
                String parentCode = baseService.getCellFormatValue(row.getCell(colNum++));
                String chsName = baseService.getCellFormatValue(row.getCell(colNum++));
                String converCode = baseService.getCellFormatValue(row.getCell(colNum++));

                //查询数据字典 【不存在创建】 【存在更新】
                List<DictDto> dictDtoList = dictService.getCommonCode("{$and:[{typeId:'" + dictTypeDto.getId() + "'},{code:'" + code + "'}]}", null, null).getRowData();
                if (dictDtoList.size() > 0) {
                    tempDto = dictDtoList.get(0);
                }
                //!VGUtility.isEmpty(dictTypePdDto
                if (null != dictTypePdDto && dictTypePdDto.getId() != null) {
                    List<DictDto> dictPdDtoList = dictService.getCommonCode("{$and:[{typeId:'" + dictTypePdDto.getId() + "'},{code:'" + parentCode + "'}]}", null, null).getRowData();
                    if (dictPdDtoList.size() == 0)
                        throw new RuntimeException("父级编码不存在！" + parentCode);
                    tempDto.setParentDictId(dictPdDtoList.get(0).getId());
                }

                tempDto.setCode(code);
                tempDto.setTypeId(dictTypeDto.getId());
                tempDto.setChsName(chsName);

                Map<String, Object> propertyMap = new HashMap<String, Object>();
                propertyMap.put("converCode", converCode);
                tempDto.setPropertyMap(propertyMap);

                if (dictDtoList.size() > 0)
                    dictService.updateCommonCode(tempDto);
                else
                    dictService.addCommonCode(tempDto);
            }
        } catch (Exception e) {
            logger.error("Exception when read Excel File", e);
            throw new RuntimeException("导入数据出错!" + e.getMessage());
        }

        return "{\"success\":true}";
    }


    @PostMapping(value = "reset_pwd")
    @ResponseBody
    public String resetPwd(@RequestParam String userId) {
        UserInfoDto userInfo = userService.getUserInfo(userId);
        if (!VGUtility.isEmpty(userInfo))
            userInfo.setPassword("123456");
        userService.saveUserInfo(userInfo);
        return "{\"success\":true}";
    }

    @PostMapping(value = "import_usesr_xls")
    @ResponseBody
    public String importUserXls(UploadFileDto fileData) {
        try {
            HSSFWorkbook wb = new HSSFWorkbook(fileData.getUploadFileData().getInputStream());
            HSSFSheet sheet = wb.getSheetAt(0);
            if (sheet != null) {
                for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                    HSSFRow row = sheet.getRow(i);

                    if (VGUtility.isEmpty(row.getCell(0)))
                        continue;
                    if (VGUtility.isEmpty(row.getCell(1)))
                        continue;

                    String userName = getCellValueByCell(row.getCell(0), i);
                    String chsName = getCellValueByCell(row.getCell(1), i);
                    System.out.println(i + ":" + userName + " " + chsName);

                    if (!VGUtility.isEmpty(userService.getUserInfoByUserName(IBaseService.SYSMARK, userName)))
                        continue;

                    UserInfoDto userDto = new UserInfoDto();
                    userDto.setSysMark(IBaseService.SYSMARK);
                    userDto.setUserName(userName);
                    userDto.setChsName(chsName);
                    userDto.setPassword("123456");
                    userService.saveUserInfo(userDto);
                }
            }
        } catch (Exception e) {
            logger.error("Exception when read Excel File", e);
            throw new RuntimeException("导入数据出错!");
        }
        return "{\"success\":true}";
    }

    private String getCellValueByCell(HSSFCell cell, int row) {
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC:
                return VGUtility.toDoubleStr(cell.getNumericCellValue(), "#.##");
            case HSSFCell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case HSSFCell.CELL_TYPE_FORMULA:
            case HSSFCell.CELL_TYPE_BLANK:
            case HSSFCell.CELL_TYPE_BOOLEAN:
            case HSSFCell.CELL_TYPE_ERROR:
            default:
                throw new RuntimeException("第" + row + "内容格式不正确！");
        }
    }

    /**
     * 物资combobox
     *
     * @param q
     * @return
     */
    @RequestMapping(value = "asset_combo")
    @ResponseBody
    public List<CommonComboDto> getAssetForCombobox(String q) {
        logger.info("Get Asset For Combobox");
        List<CommonComboDto> resultList = new ArrayList<CommonComboDto>();
        String queryStr = "{}";

        List<DictTypeDto> list = dictService.getCommonCodeType("{key: 'ASSET_TYPE'}", null, null).getRowData();
        if (!list.isEmpty()) {
            if (!VGUtility.isEmpty(q))
                queryStr = "{$and:[{typeId:'" + list.get(0).getId() + "'},{code:{$regex:'" + q + "'}}]}";
            else
                queryStr = "{$and:[{typeId:'" + list.get(0).getId() + "'}]}";
            List<DictDto> commonCodeDtoList = dictService.getCommonCode(queryStr, null, null).getRowData();
            for (DictDto dto : commonCodeDtoList) {
                CommonComboDto comboDto = new CommonComboDto();
                comboDto.setValue(dto.getCode());
                comboDto.setText(dto.getCode());
                resultList.add(comboDto);
            }
        }
        return resultList;
    }

    /**
     * 物资combobox  区分生产型和非生产型
     *
     * @param q
     * @return
     */
    @RequestMapping(value = "asset_combo_produceType")
    @ResponseBody
    public Map<String, Object> getAssetForCombobox(@RequestParam(required = false) String assetCode,
                                                   @RequestParam(required = false) String assetName,
                                                   @RequestParam(required = false) String produceTypeStr,
                                                   @RequestParam(defaultValue = "1") String page,
                                                   @RequestParam(defaultValue = "10") String rows) {
        logger.info("Get Asset For Combobox");
        StringBuilder queryStr = new StringBuilder("{$and:[");

        Map<String, Object> stringObjectHashMap = new HashMap<>();
        List<DictTypeDto> list = dictService.getCommonCodeType("{key: 'ASSET_TYPE'}", null, null).getRowData();
        if (!list.isEmpty()) {
            queryStr.append("{'typeId':'" + list.get(0).getId() + "'}");
            if (!VGUtility.isEmpty(assetCode)) {
                //queryStr = "{$and:[{typeId:'" + list.get(0).getId() + "'},{code:{$regex:'" + assetCode + "'}}]}";
                queryStr.append(",{'code':{$regex:'" + assetCode + "'}}");
            }
            if (!VGUtility.isEmpty(assetName)) {
                queryStr.append(",{'chsName':{$regex:'" + assetName + "'}}");
            }
            String W_IS_PRO_STR = "是";
            if (Integer.toString(IAssetService.ASSET_PRODUCE_TYPE.非生产性物资.ordinal()).equals(produceTypeStr)) {
                W_IS_PRO_STR = "否";
            }
            queryStr.append(",{'propertyMap.W_IS_PRO':'" + W_IS_PRO_STR + "'}]}");
            PageDto<DictDto> dtoPageDto = dictService.getCommonCode(queryStr.toString(), null, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)));
            List<DictDto> rowData = dtoPageDto.getRowData();
            List<Map<String, String>> mapResultMap = new ArrayList<>();
            rowData.stream().forEach(o -> {
                HashMap<String, String> resultList = new HashMap<String, String>();
                resultList.put("id", o.getId());
                resultList.put("code", o.getCode());
                resultList.put("chsName", o.getChsName());
                Object w_pro_code = o.getPropertyMap().get("W_PRO_CODE");
                resultList.put("wProCode", VGUtility.isEmpty(w_pro_code) ? "" : w_pro_code.toString());
                Object w_type_code = o.getPropertyMap().get("W_TYPE_CODE");
                resultList.put("wTypeCode", VGUtility.isEmpty(w_type_code) ? "" : w_type_code.toString());
                Object marterials_spec = o.getPropertyMap().get("MARTERIALS_SPEC");
                resultList.put("marterialsSpec", VGUtility.isEmpty(marterials_spec) ? "" : marterials_spec.toString());
                Object w_unit_code = o.getPropertyMap().get("W_UNIT_CODE");
                resultList.put("wUnitCode", VGUtility.isEmpty(w_unit_code) ? "" : w_unit_code.toString());
                mapResultMap.add(resultList);
            });
            stringObjectHashMap.put("total", dtoPageDto.getTotalCount());
            stringObjectHashMap.put("rows", mapResultMap);

        }
        return stringObjectHashMap;
    }

    @PostMapping(value = "change_pwd")
    @ResponseBody
    public void chanePwd(String oldPwd, String newPwd, String confirmPwd) {
        logger.info("Change Password");
        UserInfoDto userInfo = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();

        if (!userInfo.getPassword().equals(oldPwd)) throw new RuntimeException("旧密码不正确!");
        else if (userInfo.getPassword().equals(newPwd)) throw new RuntimeException("新密码与旧密码一致!");
        else if (!newPwd.equals(confirmPwd)) throw new RuntimeException("新密码与确认密码不一致！");
        else if (!commonUtils.RegexPWD(newPwd)) throw new RuntimeException("新密码密码强度不够！必须是8位以上，且包含数字、大小写字母。");
        userInfo.setPassword(confirmPwd);
        userService.saveUserInfo(userInfo);
    }

    @PostMapping(value = "sync_com")
    @ResponseBody
    public void syncCom() {
        logger.info("Sync Com");
        baseService.syncCom();
        baseService.syncDept();
//		baseService.syncPost();
        baseService.SyncUser();
    }


    /***
     * 根据公司编码,获取用户角色为资产管理员的combobox
     * @param id
     * @param q
     * @return
     */
    @PostMapping(value = "user_comboByComCode_zcgly")
    @ResponseBody
    public List<CommonComboDto> getZCGLYForComboboxByComCode(String id, @RequestParam(required = false) String q) {
        logger.info("Get User For Combobox by ComCode");
        List<CommonComboDto> resultList = new ArrayList<CommonComboDto>();
        Object obj = baseService.getDeptInfo(id);
        DeptInfoDto deptInfo = new DeptInfoDto();
        if (!VGUtility.isEmpty(obj)) {
            deptInfo = (DeptInfoDto) obj;
            List<UserInfoDto> userDtoList = baseService.getUserInfoByComCodeAndRoleCode(deptInfo.getDeptCode(), "zcgly");
            for (UserInfoDto userDto : userDtoList) {
                CommonComboDto comboDto = new CommonComboDto();
                comboDto.setValue(userDto.getId());
                comboDto.setText(userDto.getUserName() + " " + userDto.getChsName());
                comboDto.setValue1(userDto.getUserName());
                comboDto.setValue2(userDto.getChsName());
                resultList.add(comboDto);
            }
        }

        if (!VGUtility.isEmpty(q)) {
            resultList = resultList.stream().filter(result -> result.getText().contains(q)).collect(Collectors.toList());
        }

        return resultList;
    }

    /***
     * 获取资产类别列表
     * @param code
     * @param page
     * @param rows
     * @return
     */
    @PostMapping(value = "get_assettype_datagrid")
    @ResponseBody
    public Map<String, Object> getAssetTypeForDatagrid(@RequestParam(required = false) String code,
                                                       @RequestParam(defaultValue = "1") String page,
                                                       @RequestParam(defaultValue = "10") String rows) {
        logger.info("Get AssetType For Datagrid");

        List<DictTypeDto> dictTypeDtoList = dictService.getCommonCodeType("{key: 'ASSET_TYPE'}", null, null).getRowData();
        String id = dictTypeDtoList.get(0).getId();
        //只有启用状态下的类别可以选择
        PageDto<DictDto> commonCodeForDatagrid = dictService.getCommonCode("{$and:[{typeId:'" + id + "'},{code:{$regex:/^" + code + "/}}]}", null, new PageableDto(VGUtility.toInteger(page), VGUtility.toInteger(rows)));
        Map<String, Object> result = new HashMap<>();
        result.put("rows", commonCodeForDatagrid.getRowData());
        result.put("total", commonCodeForDatagrid.getTotalCount());
        return result;
    }

    /**
     * 获取部门树
     *
     * @param pid
     * @return
     */
    @PostMapping(value = "get_dept_tree")
    @ResponseBody
    public List<DeptTreeDto> getDeptTreeByPid(@RequestParam(required = false) String pid) {
        logger.info("Get DeptTreeDto For Tree");
        return baseService.getDeptTreeByPid(pid);
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param userName
     * @return
     */
    @GetMapping(value = "get_userinfo_username")
    @ResponseBody
    public String getUserInfo(@RequestParam String userName) {
        if (!VGUtility.isEmpty(userName)) {
            return JSON.toJSONString(baseService.getUserInfoDtoByUserName(userName));
        }
        return null;
    }

    @PostMapping(value = "check_pwd")
    @ResponseBody
    public Map<String, Object> checkPwd() {
        logger.info("Check Password");
        UserInfoDto userInfo = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        Map<String, Object> result = new HashMap<>();
        if (!commonUtils.RegexPWD(userInfo.getPassword())) {
            result.put("flag", "您的密码强度不够，根据要求,请您尽快修改密码!");
        } else {
            result.put("flag", "");
        }
        return result;
    }
}

package com.mine.product.czmtr.ram.base.service;

import com.alibaba.fastjson.JSONObject;
import com.mine.base.dict.dto.DictDto;
import com.mine.base.dict.dto.DictTypeDto;
import com.mine.base.dict.service.IDictService;
import com.mine.base.permission.dto.PermissionGroupDto;
import com.mine.base.permission.dto.PermissionItemDto;
import com.mine.base.permission.dto.PermissionValueDto;
import com.mine.base.permission.service.IPermissionService;
import com.mine.base.user.dto.DeptInfoDto;
import com.mine.base.user.dto.UserInfoDto;
import com.mine.base.user.service.IUserService;
import com.mine.platform.common.dto.CommonComboDto;
import com.mine.platform.common.dto.PageDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.product.czmtr.ram.asset.dto.AssetAssetDto;
import com.mine.product.czmtr.ram.asset.dto.MaterialCodeSyncBean;
import com.mine.product.czmtr.ram.base.dao.EmpCodeMapRoleCodeDao;
import com.mine.product.czmtr.ram.base.dto.*;
import com.mine.product.czmtr.ram.base.feign.SyncClient;
import com.mine.product.czmtr.ram.base.model.EmpCodeMapRoleCodeModel;
import com.vgtech.platform.common.utility.MineSecureUtility;
import com.vgtech.platform.common.utility.SpringSecureUserInfo;
import com.vgtech.platform.common.utility.VGUtility;
import feign.Feign;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class BaseService implements IBaseService {
    private static final Logger logger = LoggerFactory.getLogger(BaseService.class);
    private static final String syncUrl = ResourceBundle.getBundle("config.webservice").getString("syncUrl").trim();
    /*private static final String syncUrl = //"http://10.0.51.15:7003/SBProject_rsForYW/RestReference_foryw/datasources";
    			"http://10.0.41.15:7003/SBProject/proxy/SBserviceProxy/datasources";
    //"http://192.168.20.166:7003/SBProject/proxy/SBserviceProxy/datasources";*/
    private static final String syncComServiceName = "HR_ORG_COMP";
    private static final String syncDeptServiceName = "HR_ORG_DEPT";
    private static final String syncUserServiceName = "HR_USER_EMP";
    private static final String syncCKServiceName = "HR_ORG_CK";

    @Autowired
    private IUserService userService;
    @Autowired
    private IDictService dictService;
    @Autowired
    private IPermissionService permService;

    @Autowired
    private EmpCodeMapRoleCodeDao empCodeMapRoleCodeDao;

    @Override
    public void deleteEmpCodeMapRoleCodeByRoleCode(String roleCode) {
        EmpCodeMapRoleCodeModel model = new EmpCodeMapRoleCodeModel();
        model.setRoleCode(roleCode);
        empCodeMapRoleCodeDao.delete(model);
    }

    @Override
    public void deleteEmpCodeMapRoleCodeByEmpCodeAndRoleCode(String empCode, String roleCode) {
        EmpCodeMapRoleCodeModel model = new EmpCodeMapRoleCodeModel();
        model.setRoleCode(roleCode);
        model.setEmpCode(empCode);

        Example<EmpCodeMapRoleCodeModel> example = Example.of(model);
        List<EmpCodeMapRoleCodeModel> all = empCodeMapRoleCodeDao.findAll(example);
        if (all.size() == 0) {
            return ;
        }
        for (EmpCodeMapRoleCodeModel save : all) {
            empCodeMapRoleCodeDao.delete(save);
        }
    }

    public List<EmpCodeMapRoleCodeDto> findByEmpCodeMapRoleCodeModel(EmpCodeMapRoleCodeModel temp) {
        Example<EmpCodeMapRoleCodeModel> example = Example.of(temp);
        List<EmpCodeMapRoleCodeModel> all = empCodeMapRoleCodeDao.findAll(example);
        if (all.size() == 0) {
            return null;
        }
        ArrayList<EmpCodeMapRoleCodeDto> dtos = new ArrayList<>();

        for (EmpCodeMapRoleCodeModel modle : all) {
            EmpCodeMapRoleCodeDto dto = new EmpCodeMapRoleCodeDto();
            BeanUtils.copyProperties(modle, dto);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<EmpCodeMapRoleCodeDto> findByRoleCode(String roleCode) {

        EmpCodeMapRoleCodeModel temp = new EmpCodeMapRoleCodeModel();
        temp.setRoleCode(roleCode);
        return this.findByEmpCodeMapRoleCodeModel(temp);
    }


    @Override
    public List<EmpCodeMapRoleCodeDto> findByEmpCode(String empCode) {
        EmpCodeMapRoleCodeModel temp = new EmpCodeMapRoleCodeModel();
        temp.setEmpCode(empCode);
        return this.findByEmpCodeMapRoleCodeModel(temp);
    }

    @Override
    public void createEmpCodeMapRoleCode(EmpCodeMapRoleCodeDto dto) {
        EmpCodeMapRoleCodeModel temp = new EmpCodeMapRoleCodeModel();
        BeanUtils.copyProperties(dto, temp);
        empCodeMapRoleCodeDao.save(temp);
    }

    @Override
    public void updateEmpCodeMapRoleCodeByRoleCode(String oldRoleCode, String newRoleCode) {
        EmpCodeMapRoleCodeModel model = new EmpCodeMapRoleCodeModel();
        model.setRoleCode(oldRoleCode);
        List<EmpCodeMapRoleCodeDto> dtos = this.findByEmpCodeMapRoleCodeModel(model);
        if (dtos != null && dtos.size() > 0) {
            for (EmpCodeMapRoleCodeDto dto : dtos) {
                EmpCodeMapRoleCodeModel save = new EmpCodeMapRoleCodeModel();
                BeanUtils.copyProperties(dto, save);
                save.setRoleCode(newRoleCode);
                empCodeMapRoleCodeDao.save(save);
            }
        }
    }


    @Override
    public List<CommonComboDto> getDeptForComboboxByParentDeptId(String parentDeptId) {
        List<CommonComboDto> result = new ArrayList<CommonComboDto>();

        //放入自己
        DeptInfoDto deptDto = userService.getDeptInfo(parentDeptId);
        CommonComboDto comboDto = new CommonComboDto();
        comboDto.setValue(deptDto.getId());
        comboDto.setText(deptDto.getDeptCode() + " " + deptDto.getDeptName());
        result.add(comboDto);

        //查询子
        List<DeptInfoDto> deptDtoList = userService.getDeptInfo("{sysMark:'RAM_WEB', parentId:'" + parentDeptId + "'}", null, null).getRowData();
        for (DeptInfoDto deptDtoItem : deptDtoList)
            result.addAll(getDeptForComboboxByParentDeptId(deptDtoItem.getId()));

        return result;
    }

    /***
     * 查询当前部分所在的分子公司
     * @param parentDeptId
     * @return
     */
    @Override
    public DeptInfoDto getDeptByParentId(String parentDeptId) {
        DeptInfoDto deptDto = getDeptInfo(parentDeptId);
        if (!VGUtility.isEmpty(deptDto.getPdId())) {
            deptDto = getDeptByParentId(deptDto.getPdId());
        }
        return deptDto;
    }

    @Override
    public String getAssetTypeByMaterialCode(String materialCode) {

        String typeId = new String();
        String assetTypeName = new String();
        DictDto dictDto = new DictDto();

        if (materialCode.length() != 12) {
            throw new RuntimeException("物资编码格式不正确！");
        }

        for (int i = 4; i > 0; i--) {
            if ("00".equals(materialCode.substring(i * 2 - 2, i * 2)))
                continue;
            if (VGUtility.isEmpty(assetTypeName)) {
                typeId = dictService.getCommonCodeType("{$and:[{sysMark:'" + IBaseService.SYSMARK + "'}, {key:'DEVICE_CODE_LV" + i + "'}]}", null, null).getRowData().get(0).getId();
                List<DictDto> rowData = dictService.getCommonCode("{$and:[{typeId:'" + typeId + "'}, {code:'" + materialCode.substring(0, i * 2) + "'}]}", null, null).getRowData();
                if (rowData.isEmpty()) {
                    throw new RuntimeException("未找到对应物资分类，请联系系统管理员！");
                }
                dictDto = rowData.get(0);
                assetTypeName = dictDto.getChsName();
            }

            if (!VGUtility.isEmpty(dictDto.getParentDictId())) {
                dictDto = dictService.getCommonCode(dictDto.getParentDictId());
                assetTypeName = dictDto.getChsName() + "." + assetTypeName;
            }
        }

        return assetTypeName;
    }

    @Override
    public MaterialCodeSyncBean getMaterialCodeSyncBeanByMaterialCode(String materialCode) {
        MaterialCodeSyncBean bean = new MaterialCodeSyncBean();

        PageDto<DictDto> commonCodeDto = dictService.getCommonCode("{code:'" + materialCode + "'}", null, null);
        List<DictDto> commonCodeDtoRows = commonCodeDto.getRowData();
        if (commonCodeDtoRows.size() > 0) {
            Map<String, Object> dict = commonCodeDtoRows.get(0).getPropertyMap();
            bean.setCode(materialCode);
            if (!VGUtility.isEmpty(dict.get("BRAND_NAME")))
                bean.setBRAND_NAME(dict.get("BRAND_NAME").toString());
            if (!VGUtility.isEmpty(dict.get("EXPIRATION_DATE")))
                bean.setEXPIRATION_DATE(dict.get("EXPIRATION_DATE").toString());
            if (!VGUtility.isEmpty(dict.get("name")))
                bean.setName(dict.get("name").toString());
            if (!VGUtility.isEmpty(dict.get("W_PRO_CODE")))
                bean.setW_PRO_CODE(dict.get("W_PRO_CODE").toString());
            if (!VGUtility.isEmpty(dict.get("W_TYPE_CODE")))
                bean.setW_TYPE_CODE(dict.get("W_TYPE_CODE").toString());
            if (!VGUtility.isEmpty(dict.get("PRICE")))
                bean.setPRICE(Double.parseDouble(dict.get("PRICE").toString()));
            if (!VGUtility.isEmpty(dict.get("MARTERIALS_SPEC")))
                bean.setMARTERIALS_SPEC(dict.get("MARTERIALS_SPEC").toString());
            if (!VGUtility.isEmpty(dict.get("W_UNIT_CODE")))
                bean.setW_UNIT_CODE(dict.get("W_UNIT_CODE").toString());
            if (!VGUtility.isEmpty(dict.get("W_IS_PRO")))
                bean.setW_IS_PRO(dict.get("W_IS_PRO").toString());
            if (!VGUtility.isEmpty(dict.get("IS_DAN")))
                bean.setIS_DAN(dict.get("IS_DAN").toString());
            if (!VGUtility.isEmpty(dict.get("IS_DIRECT")))
                bean.setIS_DIRECT(dict.get("IS_DIRECT").toString());
            if (!VGUtility.isEmpty(dict.get("MARTERIALS_STATE")))
                bean.setMARTERIALS_STATE(dict.get("MARTERIALS_STATE").toString());
            if (!VGUtility.isEmpty(dict.get("BRAND_NAME")))
                bean.setBRAND_NAME(dict.get("BRAND_NAME").toString());
            if (!VGUtility.isEmpty(dict.get("IS_DEL")))
                bean.setIS_DEL(dict.get("IS_DEL").toString());
        }
        return bean;
    }

    @Override
    public String getAssetNameByMaterialCode(String materialCode) {
        String assetName = new String();

        if (materialCode.length() == 12) {
            List<DictDto> commonCodeDtoRows = dictService.getCommonCode("{code:'" + materialCode + "'}", null, null).getRowData();
            if (commonCodeDtoRows.size() > 0)
                assetName = commonCodeDtoRows.get(0).getChsName();
        }

        return assetName;
    }

    @Override
    public Map<String, String> getAssetMapByMaterialCode(String materialCode) {
        String tempStr = new String();
        Map<String, String> map = new HashMap<String, String>();

        List<DictDto> commonCodeDtoRows = dictService.getCommonCode("{code:'" + materialCode + "'}", null, null).getRowData();
        if (!commonCodeDtoRows.isEmpty()) {
            map.put("assetName", commonCodeDtoRows.get(0).getChsName());

            Map<String, Object> propertyMap = commonCodeDtoRows.get(0).getPropertyMap();
            if (!VGUtility.isEmpty(propertyMap)) {
                tempStr = (String) commonCodeDtoRows.get(0).getPropertyMap().get("W_UNIT_CODE");
                map.put("W_UNIT_CODE", VGUtility.isEmpty(tempStr) ? "" : tempStr);
                tempStr = (String) commonCodeDtoRows.get(0).getPropertyMap().get("W_PRO_CODE");
                map.put("W_PRO_CODE", VGUtility.isEmpty(tempStr) ? "" : tempStr);
                tempStr = (String) commonCodeDtoRows.get(0).getPropertyMap().get("MARTERIALS_SPEC");
                map.put("MARTERIALS_SPEC", VGUtility.isEmpty(tempStr) ? "" : tempStr);
                tempStr = (String) commonCodeDtoRows.get(0).getPropertyMap().get("BRAND_NAME");
                map.put("BRAND_NAME", VGUtility.isEmpty(tempStr) ? "" : tempStr);
            }
        }

        return map;
    }

    @Override
    public String getRunningNumByMaterialCode(String materialCode) {
        String runningNum = new String();

        List<DictDto> dtoList = dictService.getCommonCode("{code:'" + materialCode + "'}", null, null).getRowData();
        if (dtoList.size() > 0) {
            runningNum = (String) dtoList.get(0).getPropertyMap().get("runningNum");
            if (VGUtility.isEmpty(runningNum))
                runningNum = "00001";
        }

        return runningNum;
    }

    @Override
    public void UpdateRamAssetTypeByMaterialCode(String materialCode, String runningNum) {
        DictDto dto = dictService.getCommonCode("{code:'" + materialCode + "'}", null, null).getRowData().get(0);
        Map<String, Object> propertyMap = dto.getPropertyMap();
        propertyMap.put("runningNum", runningNum);
        dictService.updateCommonCode(dto);
    }

    @Override
    public Object getPredicateByDictDto(Object builder, Object root, Object assetDto) {
        AssetAssetDto assetAssetDto = (AssetAssetDto) assetDto;
        Root roota = (Root) root;
        CriteriaBuilder criteriaBuilder = (CriteriaBuilder) builder;
        Predicate finalPred = null;
        List<Predicate> orList = new ArrayList<>();
        String assetNameFromPage = assetAssetDto.getAssetChsName();
        List<DictTypeDto> rowData = dictService.getCommonCodeType("{key: '" + IBaseService.DICT_ASSET_TYPE + "'}", null, null).getRowData();
        if (rowData.size() > 0) {
            String id = rowData.get(0).getId();
            List<DictDto> dictDtoList = dictService.getCommonCode("{$and:[{typeId:'" + id + "'},{chsName:/" + assetNameFromPage + "/}]}", null, null).getRowData();
            for (DictDto dto : dictDtoList) {
                orList.add(criteriaBuilder.equal(roota.get("materialCode"), dto.getCode()));
            }
        }
        if (orList.size() > 0) {
            finalPred = criteriaBuilder.or(orList.toArray(new Predicate[orList.size()]));
        }
        return finalPred;
    }

    @Override
    public DeptInfoDto getDeptInfoDtoByCode(String code) {
        DeptInfoDto deptInfoDto = new DeptInfoDto();

        List<DeptInfoDto> deptInfoDtoList = userService.getDeptInfo("{$and:[{sysMark:'" + IBaseService.SYSMARK + "'},{code: '" + code + "'}]}", null, null).getRowData();
        if (deptInfoDtoList.size() > 0)
            deptInfoDto = deptInfoDtoList.get(0);

        return deptInfoDto;
    }

    @Override
    public DeptInfoDto getDeptInfo(String id) {
        DeptInfoDto deptInfoDto = new DeptInfoDto();
        List<DeptInfoDto> deptInfoDtoList = userService.getDeptInfo("{id: '" + id + "'}", null, null).getRowData();
        if (deptInfoDtoList.size() > 0)
            deptInfoDto = deptInfoDtoList.get(0);

        return deptInfoDto;
    }

    @Override
    public DictDto getDictDtoByTypeAndCode(String type, String code) {
        DictDto dictDto = new DictDto();

        List<DictTypeDto> dictTypeDtoList = dictService.getCommonCodeType("{key: '" + type + "'}", null, null).getRowData();
        if (dictTypeDtoList.size() > 0) {
            List<DictDto> dictDtoList = dictService.getCommonCode("{$and:[{typeId:'" + dictTypeDtoList.get(0).getId() + "'},{code:'" + code + "'}]}", null, null).getRowData();
            if (dictDtoList.size() > 0)
                dictDto = dictDtoList.get(0);
        }

        return dictDto;
    }

    @Override
    public UserInfoDto getUserInfoByUserName(String userName) {
        UserInfoDto userInfoDto = new UserInfoDto();

        List<UserInfoDto> userInfoDtoList = userService.getUserInfo("{$and:[{},{userName: '" + userName + "'}]}", null, null).getRowData();
        if (userInfoDtoList.size() > 0)
            userInfoDto = userInfoDtoList.get(0);

        return userInfoDto;
    }

    /**
     * 根据公司编码获取公司下所有人员
     *
     * @param comCode
     * @return
     */
    @Override
    public List<UserInfoDto> getUserInfoByComCode(String comCode) {
        List<UserInfoDto> userInfoDtoList = userService.getUserInfo("{$and:[{'sysMark':'" + IBaseService.SYSMARK + "'},{'propertyMap.COMP_NO': '" + comCode + "'}]}", null, null).getRowData();
        return userInfoDtoList;
    }

    /***
     * 根据公司编码和角色编码,获取公司下当前角色下所有人员
     * @param comCode  公司编码
     * @param roleCode 角色编码
     * @return
     */
    @Override
    public List<UserInfoDto> getUserInfoByComCodeAndRoleCode(String comCode, String roleCode) {
        String permissionGroupId = "";
        PageDto<PermissionGroupDto> permissionGroup = permService.getPermissionGroup("{sysMark: '" + IBaseService.SYSMARK + "',code:'" + roleCode + "'}", null, null);
        if (permissionGroup.getRowData().size() > 0) {
            permissionGroupId = permissionGroup.getRowData().get(0).getId();
        }
        List<UserInfoDto> userInfoDtoList = new ArrayList<>();
        if (!VGUtility.isEmpty(permissionGroupId)) {
            userInfoDtoList = userService.getUserInfo("{$and:[{'sysMark':'" + IBaseService.SYSMARK + "'}" +
                    ",{'propertyMap.COMP_NO': '" + comCode + "'}" +
                    ",{'deptList.permGroupIdList':'" + permissionGroupId + "'}]}", null, null).getRowData();
        } else {
            userInfoDtoList = userService.getUserInfo("{$and:[{'sysMark':'" + IBaseService.SYSMARK + "'},{'propertyMap.COMP_NO': '" + comCode + "'}]}", null, null).getRowData();
        }

        return userInfoDtoList;
    }

    @Override
    public String getCellFormatValue(Object cellObject) {
        String result = "";
        Cell cell = (Cell) cellObject;

        if (VGUtility.isEmpty(cell))
            return result;

        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC: // 数字
                if (HSSFDateUtil.isCellDateFormatted(cell)) //如果为时间格式的内容
                    result = VGUtility.toDateStr(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()), "yyyy/M/d");
                else
                    result = VGUtility.toDoubleStr(cell.getNumericCellValue(), "0.##");
                break;
            case HSSFCell.CELL_TYPE_STRING: // 字符串
                result = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                result = cell.getBooleanCellValue() + "";
                break;
            case HSSFCell.CELL_TYPE_FORMULA: // 公式
                result = cell.getCellFormula() + "";
                break;
            case HSSFCell.CELL_TYPE_BLANK: // 空值
                result = "";
                break;
            case HSSFCell.CELL_TYPE_ERROR: // 故障
                result = "";
                break;
            default: //未知类型
                result = "";
                break;
        }

        return result.trim();
    }

    @Override
    public boolean getPermCheck(String code) {
        boolean result = false;
        String permissionItemId = new String();
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        DeptInfoDto deptInfoDto = (DeptInfoDto) userInfoDto.getPropertyMap().get("loginDeptDto");
        BaseUserDto baseUserDto = this.getUserInfoByUserIdAndDeptId(userInfoDto.getId(), deptInfoDto.getId());

        List<PermissionItemDto> permList = permService.getPermissionItem("{code: '" + code + "'}", null, null).getRowData();
        if (permList.size() == 0)
            return result;
        else
            permissionItemId = permList.get(0).getId();

        if (!VGUtility.isEmpty(baseUserDto)) {
            List<String> permGroupIdList = baseUserDto.getPermGroupIdList();
            if (!VGUtility.isEmpty(permGroupIdList)) {
                for (String permGroupId : permGroupIdList) {
                    PermissionValueDto dto = permService.getPermissionValue(permGroupId);
                    if (!VGUtility.isEmpty(dto) && !VGUtility.isEmpty(dto.getItemValueList())
                            && !VGUtility.isEmpty(dto.getItemValueList().get(IBaseService.SYSMARK))
                            && dto.getItemValueList().get(IBaseService.SYSMARK).contains(permissionItemId)) {
                        result = true;
                        break;
                    }
                }
            }
        }

        return result;
    }

    @Override
    public Map<String, DictDto> getDictDtoByIdSet(Set<String> dictIdSet) {
        Map<String, DictDto> result = new HashMap<String, DictDto>();

        List<DictDto> dictDtoList = dictService.getCommonCode(convertIdSetToExpressStr("id", dictIdSet), null, null).getRowData();
        dictDtoList.stream().forEach(o -> {
            result.put(o.getId(), o);
        });

        return result;
    }

    @Override
    public Map<String, DeptInfoDto> getDeptDtoByIdSet(Set<String> deptIdSet) {
        Map<String, DeptInfoDto> result = new HashMap<String, DeptInfoDto>();

        List<DeptInfoDto> dictDtoList = userService.getDeptInfo(convertIdSetToExpressStr("id", deptIdSet), null, null).getRowData();
        dictDtoList.stream().forEach(o -> {
            result.put(o.getId(), o);
        });

        return result;
    }

    /***
     * 判断当前人员是否有部门管理员这个角色,有则查询当前人员所在部门下(包含子部门)的 所有人员
     * @param userId 人员ID
     * @return
     */
    @Override
    public List<UserInfoDto> getUserInfoDtoByDeptSetId(String userId) {
        if (VGUtility.isEmpty(userId))
            throw new RuntimeException("人员的ID不能为空!");
        UserInfoDto userInfoDto = userService.getUserInfo(userId);
        Set<String> deptIdSet = new HashSet<String>();
        List<Map<String, Object>> maplist = new ArrayList<>();

        maplist.addAll(userInfoDto.getDeptList());
        maplist.stream().forEach(m -> {
            if (this.getUserIDByRoleCode("bmld", userId)) {
                deptIdSet.add(m.get("deptId").toString());
            }
        });

        List<CommonComboDto> combolist = new ArrayList<>();
        List<UserInfoDto> userDtoList = new ArrayList<>();
        if (deptIdSet.size() > 0) {
            List<DeptInfoDto> deptDtoList = userService.getDeptInfo(convertIdSetToExpressStr("id", deptIdSet), null, null).getRowData();

            deptDtoList.stream().forEach(o -> {
                combolist.addAll(this.getDeptForComboboxByParentDeptId(o.getId()));
            });

            if (combolist.size() > 0) {
                combolist.stream().forEach(c -> {
                    deptIdSet.add(c.getValue());
                });
            }

            userDtoList.addAll(userService.getUserInfo(convertIdSetToExpressStr("'deptList.deptId'", deptIdSet), null, null).getRowData());
        } else
            userDtoList.add(userInfoDto);

        return userDtoList;
    }

    /***
     * 判断当前人员是否有部门管理员这个角色,有则查询当前人员所在部门下(包含子部门)的 所有人员
     * @param
     * @return
     */
    @Override
    public List<UserInfoDto> getUserInfoDtoByDeptCode(String deptCode) {
        String trim = deptCode.trim();
        int length = trim.length();
        List<DeptInfoDto> deptInfoDtoList = userService.getDeptInfo("{sysMark:'" + IBaseService.SYSMARK + "'}", null, null).getRowData();
        List<String> deptList = deptInfoDtoList.stream().filter(e -> (e.getDeptCode().length() >= length && e.getDeptCode().substring(0, length).equals(trim))).map(e -> e.getId()).collect(Collectors.toList());

        List<UserInfoDto> userDtoList = new ArrayList<>();
        if (null != deptList && deptList.size() > 0) {
            Set<String> deptIdSet = new HashSet<>();
            deptIdSet.addAll(deptList);
            userDtoList.addAll(userService.getUserInfo(convertIdSetToExpressStr("'deptList.deptId'", deptIdSet), null, null).getRowData());
        }
        return userDtoList;
    }

    public Set<String> getDeptIdListByDeptCode(String deptCode) {
        String trim = deptCode.trim();
        int length = trim.length();
        List<DeptInfoDto> deptInfoDtoList = userService.getDeptInfo("{sysMark:'" + IBaseService.SYSMARK + "'}", null, null).getRowData();
        List<String> deptList = deptInfoDtoList.stream().filter(e -> (e.getDeptCode().length() >= length && e.getDeptCode().substring(0, length).equals(trim))).map(e -> e.getId()).collect(Collectors.toList());
        Set<String> deptIdSet = new HashSet<>();
        deptIdSet.addAll(deptList);
        return deptIdSet;
    }

    /**
     * 根据角色的code,人员ID判断当前人员是否有这个角色权限
     *
     * @param code
     * @param userId
     * @return
     */
    @Override
    public boolean getUserIDByRoleCode(String code, String userId) {
        boolean flag = false;
        PageDto<PermissionGroupDto> permissionGroup = permService.getPermissionGroup("{sysMark: '" + IBaseService.SYSMARK + "',code:'" + code + "'}", null, null);
        Set<String> userIdSet = new HashSet<String>();
        Set<String> idSet = new HashSet<String>();
        if (permissionGroup.getRowData().size() > 0) {
            for (PermissionGroupDto permGroupDto : permissionGroup.getRowData()) {
                userIdSet.addAll(permGroupDto.getUserList());
            }
            for (String str : userIdSet) {
                String[] userlist = str.split(",");
                for (String s : userlist) {
                    idSet.add(s);
                }
            }
            for (String str : idSet) {
                if (str.equals(userId)) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    /***
     * 根据角色编码,获取当前角色下所有人员
     * @param code
     * @return
     */
    @Override
    public List<UserInfoDto> getUserInfoDtoByRoleCode(String code) {
        if (VGUtility.isEmpty(code))
            throw new RuntimeException("角色编码不能为空!");
        PageDto<PermissionGroupDto> permissionGroup = permService.getPermissionGroup("{sysMark: '" + IBaseService.SYSMARK + "',code:'" + code + "'}", null, null);
        Set<String> userIdSet = new HashSet<String>();
        List<UserInfoDto> userDtoList = new ArrayList<>();
        if (permissionGroup.getRowData().size() > 0) {
            for (PermissionGroupDto permGroupDto : permissionGroup.getRowData()) {
                userIdSet.addAll(permGroupDto.getUserList());
            }
        }
        for (String str : userIdSet) {
            userDtoList.add(userService.getUserInfo(str));
        }
        return userDtoList;
    }

    /***
     * 根据权限编码获取权限信息
     * @param code
     * @return
     */
    @Override
    public PermissionGroupDto getPerByCode(String code) {
        if (VGUtility.isEmpty(code))
            throw new RuntimeException("角色编码不能为空!");
        PageDto<PermissionGroupDto> permissionGroup = permService.getPermissionGroup("{sysMark: '" + IBaseService.SYSMARK + "',code:'" + code + "'}", null, null);
        PermissionGroupDto pre = new PermissionGroupDto();
        if (permissionGroup.getRowData().size() > 0) {
            pre = permissionGroup.getRowData().get(0);
        }
        return pre;
    }

    @Override
    public Map<String, UserInfoDto> getUserDtoByIdSet(Set<String> userIdSet) {
        Map<String, UserInfoDto> result = new HashMap<String, UserInfoDto>();

        List<UserInfoDto> dictDtoList = userService.getUserInfo(convertIdSetToExpressStr("id", userIdSet), null, null).getRowData();
        dictDtoList.stream().forEach(o -> {
            result.put(o.getId(), o);
        });

        return result;
    }

    /***
     * 转换成MongoDB表达式
     * @param dbStr 数据库字段名称
     * @param dictIdSet 查询集合
     * @return
     */
    private String convertIdSetToExpressStr(String dbStr, Set<String> dictIdSet) {
        String expressStr = "";

        if (VGUtility.isEmpty(dbStr) || dictIdSet.size() == 0) {
            throw new RuntimeException("Convert IdList To ExpressStr Parameter error!");
        } else if (dictIdSet.size() == 1) {
            return "{" + dbStr + ": '" + dictIdSet.iterator().next() + "'}";
        } else {
            for (String tempStr : dictIdSet) {
                expressStr += "{" + dbStr + ": '" + tempStr + "'},";
            }
        }

        return "{$or:[" + expressStr.substring(0, expressStr.length() - 1) + "]}";
    }

    @Override
    public Map<String, DictDto> getDictByMaterialCodeSet(Set<String> materialCodeSet) {
        Map<String, DictDto> result = new HashMap<String, DictDto>();
        List<DictDto> dictDtoList = dictService.getCommonCode(convertIdSetToExpressStr("code", materialCodeSet), null, null).getRowData();
        dictDtoList.stream().forEach(o -> {
            result.put(o.getCode(), o);
        });
        return result;
    }

    @Override
    public Map<String, DictDto> getDictDtoByCodeLvSet(String deviceCodeLv, Set<String> codeLvSet) {
        Map<String, DictDto> result = new HashMap<String, DictDto>();

        String typeId = dictService.getCommonCodeType("{$and:[{sysMark:'" + IBaseService.SYSMARK + "'}, {key:'" + deviceCodeLv + "'}]}", null, null).getRowData().get(0).getId();
        String codeExpressionStr = this.convertIdSetToExpressStr("code", codeLvSet);
        List<DictDto> dictDtoList = dictService.getCommonCode("{$and:[{typeId:'" + typeId + "'}, " + codeExpressionStr + "]}", null, null).getRowData();
        dictDtoList.stream().forEach(o -> {
            result.put(o.getCode(), o);
        });

        return result;
    }

    @Override
    public Map<String, DictDto> getDictDtoByCodeSet(Set<String> codeLvSet) {
        Map<String, DictDto> result = new HashMap<String, DictDto>();

        List<DictDto> dictDtoList = dictService.getCommonCode(convertIdSetToExpressStr("code", codeLvSet), null, null).getRowData();
        dictDtoList.stream().forEach(o -> {
            result.put(o.getCode(), o);
        });

        return result;
    }

    /***
     *
     * @param userId 用户Id
     * @param deptId 部门Id
     * @return BaseUserDto
     */
    @Override
    public BaseUserDto getUserInfoByUserIdAndDeptId(String userId, String deptId) {
        BaseUserDto baseUserDto = new BaseUserDto();
        UserInfoDto userInfoDto = new UserInfoDto();

        try {
            userInfoDto = userService.getUserInfo(userId);
        } catch (Exception e) {
        }

        if (!VGUtility.isEmpty(userInfoDto)) {
            baseUserDto.setId(userInfoDto.getId());
            baseUserDto.setUserName(userInfoDto.getUserName());
            baseUserDto.setChsName(userInfoDto.getChsName());

            if (!VGUtility.isEmpty(deptId)) {
                for (Map<String, Object> deptMap : userInfoDto.getDeptList()) {
                    if (!VGUtility.isEmpty(deptMap.get("deptId")) && deptMap.get("deptId").equals(deptId)) {
                        BaseDeptDto parentDeptInfo = this.getDeptInfoByDeptId(deptId);
                        if (!VGUtility.isEmpty(parentDeptInfo)) {
                            baseUserDto.setPermDeptId(parentDeptInfo.getId());
                            baseUserDto.setPermDeptCode(parentDeptInfo.getDeptCode());
                            baseUserDto.setPermDeptName(parentDeptInfo.getDeptName());
                        }
                        if (!VGUtility.isEmpty(deptMap.get("manager")))
                            baseUserDto.setManager((boolean) deptMap.get("manager"));
                        if (!VGUtility.isEmpty(deptMap.get("leader")))
                            baseUserDto.setLeader((boolean) deptMap.get("leader"));
                        if (!VGUtility.isEmpty(deptMap.get("permGroupIdList")))
                            baseUserDto.setPermGroupIdList((List<String>) deptMap.get("permGroupIdList"));
                        break;
                    }
                }
            }
        }

        return baseUserDto;
    }

    /***
     *
     * @param deptId 部门Id
     * @return BaseDeptDto
     */
    @Override
    public BaseDeptDto getDeptInfoByDeptId(String deptId) {
        BaseDeptDto baseDeptDto = new BaseDeptDto();
        DeptInfoDto deptInfo = new DeptInfoDto();

        try {
            deptInfo = userService.getDeptInfo(deptId);
        } catch (Exception e) {
        }

        if (!VGUtility.isEmpty(deptInfo)) {
            baseDeptDto.setId(deptInfo.getId());
            baseDeptDto.setDeptCode(deptInfo.getDeptCode());
            baseDeptDto.setDeptName(deptInfo.getDeptName());
            baseDeptDto.setPdId(deptInfo.getPdId());
        }

        return baseDeptDto;
    }

    /**
     * fieldNameList模糊查询的字段列表,regexStr表达式,type字段连接方式
     */
    @Override
    public String regexExpr(List<String> fieldNameList, String regexStr, mergeType type) {
        String finalExpr = "";
        List<String> exprStrList = new ArrayList<String>();

        if (fieldNameList.size() == 0 || VGUtility.isEmpty(regexStr)) {
            return "";
        } else if (fieldNameList.size() == 1) {
            finalExpr = "{" + fieldNameList.get(0) + ":{$regex:'" + regexStr + "'}}";
        } else {
            for (int i = 0; i < fieldNameList.size(); i++) {
                exprStrList.add("{" + fieldNameList.get(i) + ":{$regex:'" + regexStr + "'}}");
            }

            for (int i = 0; i < exprStrList.size() - 1; i++) {
                if (i == 0)
                    finalExpr = exprMergeByType(exprStrList.get(0), exprStrList.get(1), type);
                else
                    finalExpr = exprMergeByType(finalExpr, exprStrList.get(i + 1), type);
            }
        }

        return finalExpr;
    }

    /***
     * mongoDB or and 表达式
     * @param str1
     * @param str2
     * @param type
     * @return
     */
    @Override
    public String exprMergeByType(String str1, String str2, mergeType type) {
        String resultStr = "";

        if (!VGUtility.isEmpty(str1) && !VGUtility.isEmpty(str2)) {
            switch (type.ordinal()) {
                case 0:
                    resultStr = "{$or:[" + str1 + "," + str2 + "]}";
                    break;
                case 1:
                    resultStr = "{$and:[" + str1 + "," + str2 + "]}";
                    break;
            }
        }

        return resultStr;
    }

    @Override
    public void syncCom() {
        long startTime = Calendar.getInstance().getTimeInMillis();
        SyncClient client = Feign.builder().target(SyncClient.class, syncUrl);
        List<SyncComDto> syncComDtoList = JSONObject.parseArray(client.getDataByServiceName(syncComServiceName), SyncComDto.class);

        for (SyncComDto syncComDto : syncComDtoList) {
            DeptInfoDto deptDto = new DeptInfoDto();
            Map<String, Object> map = new HashMap<String, Object>();
            if (syncComDto.getIS_DEL().equals("1")) continue; //越过已删除的行
            //判断日期格式
            if (!VGUtility.isEmpty(syncComDto.getCOMP_ESTDATE()) && !"null".equals(syncComDto.getCOMP_ESTDATE()))
                map.put("COMP_ESTDATE", VGUtility.toDateObj(syncComDto.getCOMP_ESTDATE(), "yyyy-MM-dd hh:mm:ss.S"));
            if (!VGUtility.isEmpty(syncComDto.getCREATE_TIME()) && !"null".equals(syncComDto.getCREATE_TIME()))
                map.put("CREATE_TIME", VGUtility.toDateObj(syncComDto.getCREATE_TIME(), "yyyy-MM-dd hh:mm:ss.S"));
            if (!VGUtility.isEmpty(syncComDto.getLASTUPDATE_TIME()) && !"null".endsWith(syncComDto.getLASTUPDATE_TIME()))
                map.put("LASTUPDATE_TIME", VGUtility.toDateObj(syncComDto.getLASTUPDATE_TIME(), "yyyy-MM-dd hh:mm:ss.S"));
            //填充propertyMap
            map.put("sysTypeMark", 0);
            map.put("COMP_SHORT", syncComDto.getCOMP_SHORT());
            map.put("COMP_PERSON", syncComDto.getCOMP_PERSON());
            map.put("COMP_ADDRESS", syncComDto.getCOMP_ADDRESS());
            map.put("MEMO", syncComDto.getMEMO());
            map.put("COMP_TYPE", syncComDto.getCOMP_TYPE());
            map.put("COMP_ID", syncComDto.getCOMP_ID());
            map.put("IS_DEL", VGUtility.toInteger(syncComDto.getIS_DEL()));
            map.put("F_ORDER", VGUtility.toInteger(syncComDto.getF_ORDER()));
            map.put("IS_UPDATE", VGUtility.toInteger(syncComDto.getIS_UPDATE()));

            List<DeptInfoDto> deptInfoDtoList = userService.getDeptInfo("{code:'" + syncComDto.getCOMP_NO() + "'}", null, null).getRowData();
            if (deptInfoDtoList.size() > 0) {
                deptDto = deptInfoDtoList.get(0);
                Map<String, Object> ckmap = deptDto.getPropertyMap();
                map.put("DEPT_LB", ckmap.get("DEPT_LB"));
                map.put("DEPT_LX", ckmap.get("DEPT_LX"));
                map.put("CK_NO", ckmap.get("CK_NO"));
                map.put("CK_NAME", ckmap.get("CK_NAME"));
                logger.info("Update Comp By [{}]", syncComDto.getCOMP_NO());
            } else {
                logger.info("Create Comp By [{}]", syncComDto.getCOMP_NO());
            }
            deptDto.setSysMark(IBaseService.SYSMARK);
            deptDto.setDeptCode(syncComDto.getCOMP_NO());
            deptDto.setDeptName(syncComDto.getCOMP_NAME());
            deptDto.setPropertyMap(map);
            try {
                userService.saveDeptInfo(deptDto);
            } catch (Exception e) {
                logger.info("error:" + deptDto.getDeptCode());
            }
        }

        logger.info("Time:" + (startTime - Calendar.getInstance().getTimeInMillis()));
    }

    @Override
    public void syncDept() {
        long startTime = Calendar.getInstance().getTimeInMillis();
        SyncClient client = Feign.builder().target(SyncClient.class, syncUrl);
        List<SyncDeptDto> syncDeptDtoList = JSONObject.parseArray(client.getDataByServiceName(syncDeptServiceName), SyncDeptDto.class);

        for (SyncDeptDto syncDeptDto : syncDeptDtoList) {
            DeptInfoDto deptDto = new DeptInfoDto();
            Map<String, Object> map = new HashMap<String, Object>();
            if (syncDeptDto.getIS_DEL().equals("1")) continue; //越过已删除的行
            //判断日期格式
            if (!VGUtility.isEmpty(syncDeptDto.getCREATE_TIME()) && !"null".equals(syncDeptDto.getCREATE_TIME()))
                map.put("CREATE_TIME", VGUtility.toDateObj(syncDeptDto.getCREATE_TIME(), "yyyy-MM-dd hh:mm:ss.S"));
            if (!VGUtility.isEmpty(syncDeptDto.getLASTUPDATE_TIME()) && !"null".equals(syncDeptDto.getLASTUPDATE_TIME()))
                map.put("LASTUPDATE_TIME", VGUtility.toDateObj(syncDeptDto.getLASTUPDATE_TIME(), "yyyy-MM-dd hh:mm:ss.S"));
            //填充propertyMap
            map.put("sysTypeMark", 1);
            map.put("DEPT_ID", syncDeptDto.getDEPT_ID());
            map.put("COMP_NO", syncDeptDto.getCOMP_NO());
            map.put("P_DEPT_ID", syncDeptDto.getP_DEPT_ID());
            map.put("ISSEALUP", syncDeptDto.getISSEALUP());
            map.put("LEVEL", syncDeptDto.getLEVEL());
            map.put("MEMO", syncDeptDto.getMEMO());
            map.put("P_DEPT_NO", syncDeptDto.getP_DEPT_NO());
            map.put("DEPT_TYPE", syncDeptDto.getDEPT_TYPE());
            map.put("IS_DEL", VGUtility.toInteger(syncDeptDto.getIS_DEL()));
            map.put("F_ORDER", VGUtility.toInteger(syncDeptDto.getF_ORDER()));
            map.put("IS_UPDATE", VGUtility.toInteger(syncDeptDto.getIS_UPDATE()));

            List<DeptInfoDto> deptInfoDtoList = userService.getDeptInfo("{code:'" + syncDeptDto.getDEPT_NO() + "'}", null, null).getRowData();
            if (deptInfoDtoList.size() > 0) {
                deptDto = deptInfoDtoList.get(0);
                Map<String, Object> ckmap = deptDto.getPropertyMap();
                map.put("DEPT_LB", ckmap.get("DEPT_LB"));
                map.put("DEPT_LX", ckmap.get("DEPT_LX"));
                map.put("CK_NO", ckmap.get("CK_NO"));
                map.put("CK_NAME", ckmap.get("CK_NAME"));
                logger.info("Update Dept By [{}]", syncDeptDto.getDEPT_NO());
            } else {
                logger.info("Create Dept By [{}]", syncDeptDto.getDEPT_NO());
            }
            deptDto.setSysMark(IBaseService.SYSMARK);
            deptDto.setDeptCode(syncDeptDto.getDEPT_NO());
            deptDto.setDeptName(syncDeptDto.getDEPT_NAME());
            deptDto.setPropertyMap(map);
            try {
                userService.saveDeptInfo(deptDto);
            } catch (Exception e) {
                logger.info("error:" + deptDto.getDeptCode());
            }
        }

        this.SyncDeptParentChild();

        logger.info("Time:" + (startTime - Calendar.getInstance().getTimeInMillis()));
    }

    /**
     * 同步HR_ORG_CK(财务档案数据)
     */
    @Override
    public void syncCK() {
        long startTime = Calendar.getInstance().getTimeInMillis();
        SyncClient client = Feign.builder().target(SyncClient.class, syncUrl);
        List<SyncCKDto> syncCKDtoList = JSONObject.parseArray(client.getDataByServiceName(syncCKServiceName), SyncCKDto.class);

        for (SyncCKDto syncCKDto : syncCKDtoList) {
            DeptInfoDto deptDto = new DeptInfoDto();
            Map<String, Object> map = new HashMap<String, Object>();

            List<DeptInfoDto> deptInfoDtoList = userService.getDeptInfo("{code:'" + syncCKDto.getDEPT_NO() + "'}", null, null).getRowData();
            if (deptInfoDtoList.size() > 0) {
                deptDto = deptInfoDtoList.get(0);
                map = deptDto.getPropertyMap();
                //填充propertyMap
                map.put("DEPT_LB", syncCKDto.getDEPT_LB());
                map.put("DEPT_LX", syncCKDto.getDEPT_LX());
                map.put("CK_NO", syncCKDto.getCK_NO());
                map.put("CK_NAME", syncCKDto.getCK_NAME());
                logger.info("Update Dept By [{}]", syncCKDto.getDEPT_NO());
            } else {
                logger.info("Create Dept By [{}]", syncCKDto.getDEPT_NO());
            }
            deptDto.setSysMark(IBaseService.SYSMARK);
            deptDto.setDeptCode(syncCKDto.getDEPT_NO());
            deptDto.setDeptName(syncCKDto.getDEPT_NAME());
            deptDto.setPropertyMap(map);
            try {
                userService.saveDeptInfo(deptDto);
            } catch (Exception e) {
                logger.info("error:" + deptDto.getDeptCode());
            }
        }

        logger.info("Time:" + (startTime - Calendar.getInstance().getTimeInMillis()));
    }


    private void SyncDeptParentChild() {
        String parentId = new String();
        DeptInfoDto comInfoDto = new DeptInfoDto();
        DeptInfoDto deptInfoDto = new DeptInfoDto();

        List<DeptInfoDto> deptInfoDtoList = userService.getDeptInfo("{'propertyMap.sysTypeMark':1}", null, null).getRowData();
        for (DeptInfoDto tempDto : deptInfoDtoList) {
            Map<String, Object> propertyMap = tempDto.getPropertyMap();
            parentId = propertyMap.get("P_DEPT_ID").toString();
            comInfoDto = this.getDeptInfoDtoByCOM_ID(parentId);
            if (!VGUtility.isEmpty(comInfoDto)) {
                tempDto.setPdId(comInfoDto.getId());
                logger.info("Match COM " + parentId);
            } else {
                deptInfoDto = this.getDeptInfoDtoByDEPT_ID(parentId);
                if (!VGUtility.isEmpty(deptInfoDto)) {
                    tempDto.setPdId(deptInfoDto.getId());
                    logger.info("Match DEPT " + parentId);
                } else {
                    logger.error("UnMatch " + parentId);
                }
            }
            userService.saveDeptInfo(tempDto);
        }
    }

    private DeptInfoDto getDeptInfoDtoByCOM_ID(String COM_ID) {
        List<DeptInfoDto> comInfoDto = userService.getDeptInfo("{'propertyMap.COMP_ID':'" + COM_ID + "'}", null, null).getRowData();
        if (comInfoDto.size() > 0) {
            return comInfoDto.get(0);
        } else {
            return null;
        }
    }

    private DeptInfoDto getDeptInfoDtoByDEPT_ID(String DPET_ID) {
        List<DeptInfoDto> comInfoDto = userService.getDeptInfo("{'propertyMap.DEPT_ID':'" + DPET_ID + "'}", null, null).getRowData();
        if (comInfoDto.size() > 0) {
            return comInfoDto.get(0);
        } else {
            return null;
        }
    }

    /**
     * 同步北银岗位信息
     */
	/*@Override
	public void syncPost() {
		long startTime = Calendar.getInstance().getTimeInMillis(); 
		SyncClient client = Feign.builder().target(SyncClient.class, syncUrl);
		List<SyncPostDto> syncDeptDtoList = JSONObject.parseArray(client.getDataByServiceName(syncPostServiceName), SyncPostDto.class);
		
		for(SyncPostDto syncPostDto: syncDeptDtoList) {
			DeptInfoDto deptDto = new DeptInfoDto();
			Map<String, Object> map = new HashMap<String, Object>();
			if(syncPostDto.getIS_DEL().equals("1")) continue; //越过已删除的行
			//判断日期格式
			if(!VGUtility.isEmpty(syncPostDto.getCREATE_TIME()) && !"null".endsWith(syncPostDto.getCREATE_TIME()))
				map.put("CREATE_TIME", VGUtility.toDateObj(syncPostDto.getCREATE_TIME(), "yyyy-MM-dd hh:mm:ss.S"));
			if(!VGUtility.isEmpty(syncPostDto.getLASTUPDATE_TIME()) && !"null".endsWith(syncPostDto.getLASTUPDATE_TIME()))
				map.put("LASTUPDATE_TIME", VGUtility.toDateObj(syncPostDto.getLASTUPDATE_TIME(), "yyyy-MM-dd hh:mm:ss.S"));
			//填充propertyMap
			map.put("sysTypeMark", postLvevl);
			map.put("POST_NO", syncPostDto.getPOST_NO());
			map.put("POST_TYPE", syncPostDto.getPOST_TYPE());
			map.put("DEPT_ID", syncPostDto.getDEPT_ID());
			map.put("DEPT_NO", syncPostDto.getDEPT_NO());
			map.put("P_POST_ID", syncPostDto.getP_POST_ID());
			map.put("P_POST_NO", syncPostDto.getP_POST_NO());
			map.put("FDELETE_STATUS", syncPostDto.getFDELETE_STATUS());
			map.put("IS_DEL", VGUtility.toInteger(syncPostDto.getIS_DEL()));
			map.put("IS_UPDATE", VGUtility.toInteger(syncPostDto.getIS_UPDATE()));

			List<DeptInfoDto> deptInfoDtoList = userService.getDeptInfo("{code:'POST_"+syncPostDto.getDEPT_ID()+"'}", null, null).getRowData();
			if(deptInfoDtoList.size() > 0) {
				deptDto = deptInfoDtoList.get(0);
				logger.info("Update Post By [{}]", syncPostDto.getDEPT_ID());
			} else {
				logger.info("Create Post By [{}]", syncPostDto.getDEPT_ID());
			}
			deptDto.setSysMark(IBaseService.SYSMARK);
			deptDto.setDeptCode("POST_"+syncPostDto.getPOST_ID());
			deptDto.setDeptName(syncPostDto.getPOST_NAME());
			deptDto.setPropertyMap(map);
			try {
				userService.saveDeptInfo(deptDto);
			} catch (Exception e) {
				logger.info("error:"+deptDto.getDeptCode());
			}
		}
		
		logger.info("Time:"+(startTime - Calendar.getInstance().getTimeInMillis()));
	}*/
    @Override
    public void SyncUser() {
        long startTime = Calendar.getInstance().getTimeInMillis();
        SyncClient client = Feign.builder().target(SyncClient.class, syncUrl);
        List<SyncUserDto> syncUserDtoList = JSONObject.parseArray(client.getDataByServiceName(syncUserServiceName), SyncUserDto.class);
        Map<String, Object> deptMap = new HashMap<String, Object>();
        List<Map<String, Object>> deptMapList = new ArrayList<Map<String, Object>>();

        for (SyncUserDto syncUserDto : syncUserDtoList) {
            UserInfoDto userDto = new UserInfoDto();
            Map<String, Object> map = new HashMap<String, Object>();

            if (syncUserDto.getIS_DEL().equals("1")) continue; //越过已删除的行

            List<UserInfoDto> userInfoDtoList = userService.getUserInfo("{userName:'" + syncUserDto.getF_CELL() + "'}", null, null).getRowData();
            if (userInfoDtoList.size() > 0) {
                userDto = userInfoDtoList.get(0);
                map = userDto.getPropertyMap();
                deptMapList = userDto.getDeptList();
                logger.info("Update User By [{}]", syncUserDto.getEMP_NO());
            } else {
                userDto.setPassword("123456");
                userDto.setSysMark(IBaseService.SYSMARK);
                logger.info("Create User By [{}]", syncUserDto.getEMP_NO());
            }

            //判断日期格式
            if (!VGUtility.isEmpty(syncUserDto.getF_BIRTHDAY()) && !"null".equals(syncUserDto.getF_BIRTHDAY()))
                map.put("F_BIRTHDAY", VGUtility.toDateObj(syncUserDto.getF_BIRTHDAY(), "yyyy-MM-dd hh:mm:ss.S"));
            if (!VGUtility.isEmpty(syncUserDto.getCREATE_TIME()) && !"null".equals(syncUserDto.getCREATE_TIME()))
                map.put("CREATE_TIME", VGUtility.toDateObj(syncUserDto.getCREATE_TIME(), "yyyy-MM-dd hh:mm:ss.S"));
            if (!VGUtility.isEmpty(syncUserDto.getLASTUPDATE_TIME()) && !"null".equals(syncUserDto.getLASTUPDATE_TIME()))
                map.put("LASTUPDATE_TIME", VGUtility.toDateObj(syncUserDto.getLASTUPDATE_TIME(), "yyyy-MM-dd hh:mm:ss.S"));
            if (!VGUtility.isEmpty(syncUserDto.getEMTRY_DATE()) && !"null".equals(syncUserDto.getEMTRY_DATE()))
                map.put("EMTRY_DATE", VGUtility.toDateObj(syncUserDto.getEMTRY_DATE(), "yyyy-MM-dd hh:mm:ss.S"));
            if (!VGUtility.isEmpty(syncUserDto.getP_LASTUPDATE_TIME()) && !"null".equals(syncUserDto.getP_LASTUPDATE_TIME()))
                map.put("P_LASTUPDATE_TIME", VGUtility.toDateObj(syncUserDto.getP_LASTUPDATE_TIME(), "yyyy-MM-dd hh:mm:ss.S"));
            if (!VGUtility.isEmpty(syncUserDto.getPP_LASTUPDATE_TIME()) && !"null".equals(syncUserDto.getPP_LASTUPDATE_TIME()))
                map.put("PP_LASTUPDATE_TIME", VGUtility.toDateObj(syncUserDto.getP_LASTUPDATE_TIME(), "yyyy-MM-dd hh:mm:ss.S"));

            //填充propertyMap
            map.put("EMP_NO", syncUserDto.getEMP_NO());
            map.put("EMP_CATEGORY", syncUserDto.getEMP_CATEGORY());
            map.put("DEPT_ID", syncUserDto.getDEPT_ID());
            map.put("COMP_NO", syncUserDto.getCOMP_NO());
            map.put("DEPT_NO", syncUserDto.getDEPT_NO());
            map.put("POST_ID", syncUserDto.getPOST_ID());
            map.put("ID_CODE", syncUserDto.getID_CODE());
            map.put("P_C_NO", syncUserDto.getP_C_NO());
            map.put("SAL_SN", syncUserDto.getSAL_SN());
            map.put("WORK_TYPE", syncUserDto.getWORK_TYPE());
            map.put("E_STEP", syncUserDto.getE_STEP());
            map.put("POST_NO", syncUserDto.getPOST_NO());
            map.put("POST_LEVEL", syncUserDto.getPOST_LEVEL());
            map.put("PS_NO", syncUserDto.getPS_NO());
            map.put("F_GENDER", syncUserDto.getF_GENDER());
            map.put("F_EMAIL", syncUserDto.getF_EMAIL());
            map.put("F_ADDRESS", syncUserDto.getF_ADDRESS());
            map.put("F_HOME_PHONE", syncUserDto.getF_HOME_PHONE());
            map.put("F_OFFICE_PHONE", syncUserDto.getF_OFFICE_PHONE());
            map.put("F_CELL", syncUserDto.getF_CELL());
            map.put("F_ACTU_SERVICE", syncUserDto.getF_ACTU_SERVICE());
            map.put("F_POLITICAL", syncUserDto.getF_POLITICAL());
            map.put("F_FOLK", syncUserDto.getF_FOLK());
            map.put("F_EMP_TYPE_ID", syncUserDto.getF_EMP_TYPE_ID());
            map.put("F_EMP_TYPE_NAME", syncUserDto.getF_EMP_TYPE_NAME());
            map.put("F_HIGH_TECH_ID", syncUserDto.getF_HIGH_TECH_ID());
            map.put("F_NAME_NUM", syncUserDto.getF_NAME_NUM());
            map.put("EMP_ID", syncUserDto.getEMP_ID());
            map.put("F_NAME", syncUserDto.getF_NAME());
            map.put("F_CRETIFCATENAME", syncUserDto.getF_CRETIFCATENAME());
            map.put("MEMO", syncUserDto.getMEMO());
            map.put("EMP_TYPE", syncUserDto.getEMP_TYPE());
            map.put("IS_DEL", VGUtility.toInteger(syncUserDto.getIS_DEL()));
            map.put("F_ORDER", VGUtility.toInteger(syncUserDto.getF_ORDER()));
            map.put("IS_UPDATE", VGUtility.toInteger(syncUserDto.getIS_UPDATE()));

            //关联部门
            if (!VGUtility.isEmpty(syncUserDto.getDEPT_NO())) {
                List<DeptInfoDto> deptInfoDtoList = userService.getDeptInfo("{$and:[{sysMark:'" + IBaseService.SYSMARK + "'},{code:'" + syncUserDto.getDEPT_NO() + "'}]}", null, null).getRowData();
                if (!deptInfoDtoList.isEmpty()) {
                    deptMap.put("leader", false);
                    deptMap.put("manager", false);
                    deptMap.put("deptId", deptInfoDtoList.get(0).getId());

                    if (!deptMapList.isEmpty()) {
                        Map<String, Object> exDeptMap = deptMapList.get(0);
                        if (!VGUtility.isEmpty(exDeptMap)) {
                            exDeptMap.put("deptId", deptInfoDtoList.get(0).getId());
                        }
                    } else {
                        deptMapList.add(deptMap);
                    }
                }
            }

            userDto.setUserName(syncUserDto.getF_CELL());
            userDto.setChsName(syncUserDto.getEMP_NAME());
            userDto.setDeptList(deptMapList);
            userDto.setPropertyMap(map);
            try {
                userService.saveUserInfo(userDto);
            } catch (Exception e) {
                logger.info("error:" + userDto.getUserName());
            }
        }

        logger.info("Time:" + (startTime - Calendar.getInstance().getTimeInMillis()));
    }

    /***
     *  获取所有所属公司
     * @return
     */
    @Override
    public List<DeptInfoDto> getComDtoList() {
        return userService.getDeptInfo("{$and:[{sysMark:'" + IBaseService.SYSMARK + "'},{'propertyMap.sysTypeMark':0}]}", null, null).getRowData();
    }

    @Override
    public List<DeptInfoDto> getDeptDtoListByUserId(String userId) {
        String deptId = new String();
        String comCode = new String();
        DeptInfoDto deptInfoDto = new DeptInfoDto();
        List<String> comCodeList = new ArrayList<String>();
        List<DeptInfoDto> deptInfoDtoList = new ArrayList<DeptInfoDto>();
        List<DeptInfoDto> comInfoDtoList = this.getComDtoList();
        Map<String, DeptInfoDto> comInfoDtoMap = new HashMap<String, DeptInfoDto>();

        //公司填充至map
        comInfoDtoList.stream().forEach(o -> {
            comInfoDtoMap.put(o.getDeptCode(), o);
        });

        //遍历部门的公司编码
        UserInfoDto userInfo = userService.getUserInfo(userId);
        if (!VGUtility.isEmpty(userInfo)) {
            List<Map<String, Object>> deptMapList = userInfo.getDeptList();
            for (Map<String, Object> deptMap : deptMapList) {
                deptId = (String) deptMap.get("deptId");
                if (!VGUtility.isEmpty(deptId)) {
                    deptInfoDto = userService.getDeptInfo(deptId);
                    if (!VGUtility.isEmpty(deptInfoDto) && !VGUtility.isEmpty(deptInfoDto.getPropertyMap())) {
                        comCode = (String) deptInfoDto.getPropertyMap().get("COMP_NO");
                        //多个部门时在同一个公司下时剔除重复
                        if (!comCodeList.contains(comCode) && !VGUtility.isEmpty(comInfoDtoMap.get(comCode))) {
                            comCodeList.add(comCode);
                            deptInfoDtoList.add(comInfoDtoMap.get(comCode));
                        }
                    }
                }
            }
        }

        //排序
        Collections.sort(deptInfoDtoList, new Comparator<DeptInfoDto>() {
            @Override
            public int compare(DeptInfoDto o1, DeptInfoDto o2) {
                return o1.getDeptCode().compareTo(o2.getDeptCode());
            }
        });

        return deptInfoDtoList;
    }

    @Override
    public Map<String, DeptInfoDto> getDeptDtoByDeptList(PageDto<DeptInfoDto> deptDtoList) {
        Map<String, DeptInfoDto> result = new HashMap<String, DeptInfoDto>();
        List<DeptInfoDto> infoList = deptDtoList.getRowData();
        infoList.stream().forEach(o -> {
            result.put(o.getId(), o);
        });
        return result;
    }

    @Override
    public PageDto<DictDto> getCommonCodeForDatagridByQuerys(String commonCodeType, PageableDto pageableDto) {
        String typeId = new String();

        if (VGUtility.isEmpty(commonCodeType)) {
            logger.error("DictTypeId Is Null!");
            return null;
        }

        try {
            typeId = dictService.getCommonCodeType("{key: '" + commonCodeType + "'}", null, null).getRowData().get(0).getId();
        } catch (Exception e) {
            logger.error("System unable to find" + e);
        }

        return dictService.getCommonCode("{typeId:'" + typeId + "'}", null, pageableDto);
    }

    @Override
    public PageDto<DictDto> getCommonCodeForDatagridByQuerys(String queryStr, String commonCodeType, PageableDto pageableDto) {
        String typeId = new String();

        if (VGUtility.isEmpty(commonCodeType)) {
            logger.error("DictTypeId Is Null!");
            return null;
        }

        try {
            typeId = dictService.getCommonCodeType("{key: '" + commonCodeType + "'}", null, null).getRowData().get(0).getId();
        } catch (Exception e) {
            logger.error("System unable to find" + e);
        }

        return dictService.getCommonCode("{$and:[{code:{$in:[" + queryStr + "]}}, {typeId:'" + typeId + "'}]}", null, pageableDto);


    }

    /**
     * 根据部门ID获取部门树
     *
     * @param pid
     * @return
     */
    @Override
    public List<DeptTreeDto> getDeptTreeByPid(String pid) {
        List<DeptTreeDto> treeDtoList = new ArrayList<>();
        List<DeptInfoDto> deptDtoList = new ArrayList<>();
        if (!VGUtility.isEmpty(pid)) {
            deptDtoList = userService.getDeptInfo("{sysMark:'RAM_WEB', parentId:'" + pid + "'}", null, null).getRowData();
        } else {
            deptDtoList = userService.getDeptInfo("{sysMark:'RAM_WEB'}", null, null).getRowData();
        }
        List<DeptTreeDto> treeBeans = new ArrayList<>();
        for (DeptInfoDto dept : deptDtoList) {
            DeptTreeDto treeBean = new DeptTreeDto(dept.getId(), dept.getDeptCode(), dept.getPdId(), dept.getDeptCode() + " " + dept.getDeptName(), new ArrayList<DeptTreeDto>());
            treeBeans.add(treeBean);
        }
        // 遍历获取到的List<DeptTreeDto>对象列表
        for (DeptTreeDto treeBean1 : treeBeans) {
            if (VGUtility.isEmpty(treeBean1.getPid())) {
                //递归获取父节点下的子节点
                treeBean1.setChildren(getChildrenNodes(treeBean1.getId(), treeBeans));
                //treeBean1.setState("closed");
                treeDtoList.add(treeBean1);
            }
        }
        return treeDtoList;
    }

    /**
     * 递归获取子节点下的子节点
     *
     * @param pId       父节点的ID
     * @param treesList 所有菜单树集合
     * @return
     */
    public List<DeptTreeDto> getChildrenNodes(String pId, List<DeptTreeDto> treesList) {
        List<DeptTreeDto> newTrees = new ArrayList<>();
        for (DeptTreeDto mt : treesList) {
            if (VGUtility.isEmpty(mt.getPid())) continue;
            if (mt.getPid().equals(pId)) {
                //递归获取子节点下的子节点，即设置树控件中的children
                mt.setChildren(getChildrenNodes(mt.getId(), treesList));
                newTrees.add(mt);
            }
        }
        return newTrees;
    }

    /**
     * 利用subList方法进行分页
     *
     * @param list        分页数据
     * @param pagesize    页面大小
     * @param currentPage 当前页面
     */
    @Override
    public <T> List<T> pageBySubList(List<T> list, int pagesize, int currentPage) {
        int totalcount = list.size();
        int pagecount = 0;
        List<T> subList = new ArrayList<>();
        int m = totalcount % pagesize;
        if (m > 0) {
            pagecount = totalcount / pagesize + 1;
        } else {
            pagecount = totalcount / pagesize;
        }
        if (m == 0) {
            subList = list.subList((currentPage - 1) * pagesize, pagesize * (currentPage));
        } else {
            if (currentPage == pagecount) {
                subList = list.subList((currentPage - 1) * pagesize, totalcount);
            } else {
                subList = list.subList((currentPage - 1) * pagesize, pagesize * (currentPage));
            }
        }
        return subList;
    }

    /**
     * 根据用户名获取用户信息,包含所在部门
     *
     * @param userName
     * @return
     */
    @Override
    public UserInfoDto getUserInfoDtoByUserName(String userName) {
        List<String> deptIdList = new ArrayList<String>();
        List<DeptInfoDto> resultDtoList = new ArrayList<DeptInfoDto>();

        UserInfoDto userInfo = userService.getUserInfoByUserName("RAM_WEB", userName);
        if (VGUtility.isEmpty(userInfo)) {
            return null;
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

        return userInfo;
    }

    /**
     * 向指定的URL发送GET方法的请求
     *
     * @param url           发送请求的URL
     * @param Authorization 请求参数
     * @return 远程资源的响应结果
     */
    public String sendGet(String url, String Authorization) {
        String result = "";
        BufferedReader bufferedReader = null;
        try {
            //1、读取初始URL
            String urlNameString = url;
            //2、将url转变为URL类对象
            URL realUrl = new URL(urlNameString);
            HttpURLConnection urlConnection = (HttpURLConnection) realUrl.openConnection(); //打开连接
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("GET");
            urlConnection.setUseCaches(false);

            //3、打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            //4、设置通用的请求属性
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "UTF-8");
            if (!VGUtility.isEmpty(Authorization)) {
                connection.addRequestProperty("Authorization", Authorization);
            }

            //5、建立实际的连接
            connection.connect();

            //6、定义BufferedReader输入流来读取URL的响应内容 ，UTF-8是后续自己加的设置编码格式，也可以去掉这个参数
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line = "";
            while (null != (line = bufferedReader.readLine())) {
                result += line;
            }
        } catch (Exception e) {
            throw new RuntimeException("发送GET请求出现异常！！！" + e);
        } finally {        //使用finally块来关闭输入流
            try {
                if (null != bufferedReader) {
                    bufferedReader.close();
                }
            } catch (Exception e2) {
                throw new RuntimeException("发送GET请求出现异常！！！" + e2);
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            throw new RuntimeException("发送POST请求出现异常！！！" + e);
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                throw new RuntimeException("发送POST请求出现异常！！！" + ex);
            }
        }
        return result;
    }

    /**
     * MD5加密
     *
     * @param input
     * @return
     */
    @Override
    public String stringMD5(String input) {
        try {
            // 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");

            // 输入的字符串转换成字节数组
            byte[] inputByteArray = input.getBytes();

            // inputByteArray是输入字符串转换得到的字节数组
            messageDigest.update(inputByteArray);

            // 转换并返回结果，也是字节数组，包含16个元素
            byte[] resultByteArray = messageDigest.digest();

            // 字符数组转换成字符串返回
            return byteArrayToHex(resultByteArray);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    //下面这个函数用于将字节数组换成成16进制的字符串
    public String byteArrayToHex(byte[] byteArray) {
        // 首先初始化一个字符数组，用来存放每个16进制字符
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        char[] resultCharArray = new char[byteArray.length * 2];

        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;

        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }

        // 字符数组组合成字符串返回
        return new String(resultCharArray);
    }


    @Override
    public List<DeptTreeDto> getDeptTreeForDataGrid() {
        List<DeptInfoDto> queryList = userService.getDeptInfo("{sysMark:'RAM_WEB'}", null, null).getRowData();
        Collections.sort(queryList, new Comparator<DeptInfoDto>() {
            @Override
            public int compare(DeptInfoDto o1, DeptInfoDto o2) {
                return o1.getDeptCode().compareTo(o2.getDeptCode());
            }
        });
        List<DeptTreeDto> resultList = new ArrayList<>();
        for (DeptInfoDto dto : queryList) {
            if (VGUtility.isEmpty(dto.getPdId())) {
                DeptTreeDto tree = new DeptTreeDto();
                tree.setId(dto.getId());
                tree.setCode(dto.getDeptCode());
                tree.setText(dto.getDeptName());
                tree.setPid(dto.getPdId());
                resultList.add(tree);
            }
        }
        recursionChildren(resultList, queryList);
        return resultList;
    }


    /**
     * @Description: 递归获取子节点数据
     * @Param: [parentList, allList]
     * @return: void
     * @Author: lichuan.zhang
     * @Date: 2020/7/9
     */
    private void recursionChildren(List<DeptTreeDto> parentList, List<DeptInfoDto> allList) {
        for (DeptTreeDto parentMap : parentList) {
            List<DeptTreeDto> childrenList = new ArrayList<>();
            for (DeptInfoDto allMap : allList) {
                if (!VGUtility.isEmpty(allMap.getPdId()) && allMap.getPdId().equals(parentMap.getId())) {
                    DeptTreeDto tree = new DeptTreeDto();
                    tree.setId(allMap.getId());
                    tree.setCode(allMap.getDeptCode());
                    tree.setText(allMap.getDeptName());

                    tree.setPid(allMap.getPdId());
                    tree.setpCode(parentMap.getCode());
                    tree.setPname(parentMap.getText());
                    childrenList.add(tree);
                }
            }
            if (childrenList.size() > 0) {
                parentMap.setChildren(childrenList);
                parentMap.setState("closed");
                recursionChildren(childrenList, allList);
            }
        }
    }
    
    /***
     * 根据角色编码,获取当前角色下所有人员
     * @param comCode  公司编码
     * @param roleCode 角色编码
     * @return
     */
    @Override
    public Map<String,UserInfoDto> getUserInfoByRoleCode(String roleCode) {
    	Map<String, UserInfoDto> userMap = new HashMap<String, UserInfoDto>();
    	Set<String> userIdSet = new HashSet<String>();
    	 
        PageDto<PermissionGroupDto> permissionGroup = permService.getPermissionGroup("{sysMark: '" + IBaseService.SYSMARK + "',code:'" + roleCode + "'}", null, null);
        List<UserInfoDto> userInfoDtoList = new ArrayList<>();
        if (permissionGroup.getRowData().size() > 0) {
        	PermissionGroupDto groupDto = permissionGroup.getRowData().get(0);
        	if(groupDto.getUserList().size()>0) {
        		for(String userID : groupDto.getUserList()) {
        			userIdSet.add(userID);
        		}
        	}
        }
        userMap = this.getUserDtoByIdSetForDeptCode(userIdSet);
        return userMap;
    }
    
    public Map<String, UserInfoDto> getUserDtoByIdSetForDeptCode(Set<String> userIdSet) {
        Map<String, UserInfoDto> result = new HashMap<String, UserInfoDto>();

        List<UserInfoDto> dictDtoList = userService.getUserInfo(convertIdSetToExpressStr("id", userIdSet), null, null).getRowData();
        dictDtoList.stream().forEach(o -> {
        	if(o.getPropertyMap().get("DEPT_NO").toString().length()>=5) {
        		result.put(o.getPropertyMap().get("DEPT_NO").toString().substring(0, 5), o);
        	}
        });

        return result;
    }
}

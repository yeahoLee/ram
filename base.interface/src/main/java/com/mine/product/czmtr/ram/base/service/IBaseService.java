package com.mine.product.czmtr.ram.base.service;

import com.mine.base.dict.dto.DictDto;
import com.mine.base.permission.dto.PermissionGroupDto;
import com.mine.base.user.dto.DeptInfoDto;
import com.mine.base.user.dto.UserInfoDto;
import com.mine.platform.common.dto.CommonComboDto;
import com.mine.platform.common.dto.PageDto;
import com.mine.platform.common.dto.PageableDto;
import com.mine.product.czmtr.ram.base.dto.BaseDeptDto;
import com.mine.product.czmtr.ram.base.dto.BaseUserDto;
import com.mine.product.czmtr.ram.base.dto.DeptTreeDto;
import com.mine.product.czmtr.ram.base.dto.EmpCodeMapRoleCodeDto;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public interface IBaseService {
    static final String SYSMARK = "RAM_WEB";
    static final String DICT_BELONG_LINE = "BELONG_LINE";
    static final String DICT_ASSET_TYPE = "ASSET_TYPE";
    //	static final String DICT_TYPE_
    static final String DEVICE_CODE_LV1 = "DEVICE_CODE_LV1";
    static final String DEVICE_CODE_LV2 = "DEVICE_CODE_LV2";
    static final String DEVICE_CODE_LV3 = "DEVICE_CODE_LV3";
    static final String DEVICE_CODE_LV4 = "DEVICE_CODE_LV4";
    static final String SAVE_PLACE = "SAVE_PLACE";

/*    //统一认证测试接口
    static final String uamsbrowserUrl = "http://10.0.41.22:8082";
    static final String uamsAdminUrl = "http://10.0.41.22:8081";
    static final String client_id = "am_cs";
    static final String client_secret = "Inibh179MTQPR7y";

    //统一认证生产接口
//	static final String uamsbrowserUrl="http://10.0.51.22:8082";
//	static final String uamsAdminUrl="http://10.0.51.22:8081";
//	static final String client_id="am";
//	static final String client_secret ="ii5xOxCmpx5sD";
  */

    static final String uamsbrowserUrl = ResourceBundle.getBundle("config.webservice").getString("uamsbrowserUrl").trim();
    static final String uamsAdminUrl = ResourceBundle.getBundle("config.webservice").getString("uamsAdminUrl").trim();
    static final String client_id = ResourceBundle.getBundle("config.webservice").getString("client_id").trim();
    static final String client_secret = ResourceBundle.getBundle("config.webservice").getString("client_secret").trim();

    //mongodb 查询的连接方式
    enum mergeType {
        OR, AND;
    }

    /**
     * @Description: 获取部门树结构
     * @Param:
     * @return:
     * @Author: lichuan.zhang
     * @Date: 2020/7/8
     */
    List<DeptTreeDto> getDeptTreeForDataGrid();

    List<CommonComboDto> getDeptForComboboxByParentDeptId(String parentDeptId);

    String getAssetTypeByMaterialCode(String materialCode);

    String getAssetNameByMaterialCode(String materialCode);

    String getRunningNumByMaterialCode(String materialCode);

    void UpdateRamAssetTypeByMaterialCode(String materialCode, String runningNum);

    Object getPredicateByDictDto(Object builder, Object root, Object assetDto);

    String getCellFormatValue(Object cell);

    Object getDeptInfoDtoByCode(String code);

    Object getDictDtoByTypeAndCode(String type, String code);

    Object getUserInfoByUserName(String managerIdCode);

    //	Object getDictDtoByCode(String unitOfMeasIdCode);
    Object getDeptInfo(String id);

    boolean getPermCheck(String code);

    Map<String, DictDto> getDictDtoByIdSet(Set<String> dictIdSet);

    Map<String, DeptInfoDto> getDeptDtoByIdSet(Set<String> deptIdSet);

    Map<String, UserInfoDto> getUserDtoByIdSet(Set<String> userIdSet);

    Map<String, DictDto> getDictByMaterialCodeSet(Set<String> materialCodeSet);

    Map<String, DictDto> getDictDtoByCodeLvSet(String deviceCodeLv, Set<String> codeLvSet);

    public DeptInfoDto getDeptByParentId(String parentDeptId);

    BaseDeptDto getDeptInfoByDeptId(String deptId);

    BaseUserDto getUserInfoByUserIdAndDeptId(String userId, String deptId);

    Object getMaterialCodeSyncBeanByMaterialCode(String materialCode);

    Map<String, DictDto> getDictDtoByCodeSet(Set<String> codeLvSet);

    List<UserInfoDto> getUserInfoDtoByDeptSetId(String userId);

    List<UserInfoDto> getUserInfoDtoByDeptCode(String deptCode);

    Set<String> getDeptIdListByDeptCode(String deptCode);

    String regexExpr(List<String> fieldNameList, String regexStr, mergeType type);

    String exprMergeByType(String str1, String str2, mergeType type);

    boolean getUserIDByRoleCode(String code, String userId);

    void syncCom();

    void syncDept();

    void SyncUser();

    Map<String, String> getAssetMapByMaterialCode(String materialCode);

    List<DeptInfoDto> getDeptDtoListByUserId(String id);

    List<DeptInfoDto> getComDtoList();

    List<UserInfoDto> getUserInfoByComCode(String comCode);

    Map<String, DeptInfoDto> getDeptDtoByDeptList(PageDto<DeptInfoDto> deptDtoList);

    PageDto<DictDto> getCommonCodeForDatagridByQuerys(String commonCodeType, PageableDto pageableDto);

    PageDto<DictDto> getCommonCodeForDatagridByQuerys(String queryStr, String commonCodeType, PageableDto pageableDto);

    List<UserInfoDto> getUserInfoDtoByRoleCode(String code);

    PermissionGroupDto getPerByCode(String code);

    List<UserInfoDto> getUserInfoByComCodeAndRoleCode(String comCode, String roleCode);

    List<DeptTreeDto> getDeptTreeByPid(String pid);

    <T> List<T> pageBySubList(List<T> list, int pagesize, int currentPage);

    UserInfoDto getUserInfoDtoByUserName(String userName);

    String sendGet(String url, String Authorization);

    String sendPost(String url, String param);

    String stringMD5(String input);

    void syncCK();

    void deleteEmpCodeMapRoleCodeByRoleCode(String roleCode);

    void deleteEmpCodeMapRoleCodeByEmpCodeAndRoleCode(String empCode,String roleCode);

    List<EmpCodeMapRoleCodeDto> findByRoleCode(String roleCode);

    List<EmpCodeMapRoleCodeDto> findByEmpCode(String empCode);

    void createEmpCodeMapRoleCode(EmpCodeMapRoleCodeDto dto);

    void  updateEmpCodeMapRoleCodeByRoleCode(String oldRoleCode, String newRoleCode);

    Map<String,UserInfoDto> getUserInfoByRoleCode(String roleCode);
}

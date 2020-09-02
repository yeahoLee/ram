package com.mine.product.czmtr.ram.main.secure;

import com.mine.base.user.dto.DeptInfoDto;
import com.mine.base.user.dto.UserInfoDto;
import com.mine.base.user.service.IUserService;
import com.mine.product.czmtr.ram.base.dto.EmpCodeMapRoleCodeDto;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.mine.product.czmtr.ram.base.service.ICommonUtils;
import com.vgtech.platform.common.utility.SpringSecureUserInfo;
import com.vgtech.platform.common.utility.VGUtility;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(value = "mineSpringSecureUserService")
public class MineSpringSecureUserService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(MineSpringSecureUserService.class);
    @Autowired
    private IUserService userService;
    @Autowired
    private ICommonUtils commonUtils;
    @Autowired
    private IBaseService baseService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        logger.info("Search User[{}]", s);
        List<String> deptIdList = new ArrayList<String>();
        List<DeptInfoDto> resultDtoList = new ArrayList<DeptInfoDto>();
//        List<String> permGroupCodeList = new ArrayList<>();
        UserInfoDto userInfo = userService.getUserInfoByUserName("RAM_WEB", StringEscapeUtils.escapeSql(s));
        if (VGUtility.isEmpty(userInfo)) {
            throw new UsernameNotFoundException("用户名不存在");
        }

        List<Map<String, Object>> deptMapList = userInfo.getDeptList();
        for (Map<String, Object> deptMap : deptMapList) {
            String deptId = (String) deptMap.get("deptId");
            if (VGUtility.isEmpty(deptId))
                continue;
            deptIdList.add(deptId);
//            if (!VGUtility.isEmpty(deptMap.get("permGroupCodeList"))) {
//                permGroupCodeList.addAll((List<String>) deptMap.get("permGroupCodeList"));
//            }
        }

        List<DeptInfoDto> deptInfoDtoList = userService.getDeptInfo("{}", null, null).getRowData();
        for (DeptInfoDto deptInfoDto : deptInfoDtoList) {
            if (deptIdList.contains(deptInfoDto.getId())) {
                resultDtoList.add(deptInfoDto);
            }
        }

        Set<String> roleCodeS = new HashSet<>();
        if (!VGUtility.isEmpty(userInfo.getPropertyMap()) && !VGUtility.isEmpty(userInfo.getPropertyMap().get("EMP_NO"))) {
            String emp_no = userInfo.getPropertyMap().get("EMP_NO").toString();
            List<EmpCodeMapRoleCodeDto> dtos = baseService.findByEmpCode(emp_no);
            if (dtos != null && dtos.size() > 0) {
                for (EmpCodeMapRoleCodeDto dto : dtos) {
                    roleCodeS.add(dto.getRoleCode());
                }

            }
        }

        int min = 50;
        boolean falg = false;
        boolean admin = false;
//        for (String code : permGroupCodeList) {
        if (roleCodeS.size() > 0) {
            for (String code : roleCodeS) {
                if (code.contains("-")) {
                    String[] split = code.split("-");
                    String level = split[0];
                    int i = Integer.parseInt(level);
                    min = i < min ? i : min;
                } else if ("zcgly".equals(code)) {
                    falg = true;
                } else if ("admin".equals(code)) {
                    admin = true;
                }

            }
        }

        String iszcgly = (min == 50 && falg) ? "1" : "0";
        String isboss = (min == 0 || admin) ? "1" : "0";
        //获取权限设置中的组织编码
        String depNoCut = null;
        if (min != 50 && !VGUtility.isEmpty(userInfo.getPropertyMap()) && !VGUtility.isEmpty(userInfo.getPropertyMap().get("DEPT_NO"))) {
            String deptNo = userInfo.getPropertyMap().get("DEPT_NO").toString();
            if (deptNo.length() >= min) {
                depNoCut = deptNo.substring(0, min);
            } else {
                depNoCut = deptNo;

            }

        }

        Map<String, Object> deptInfoMap = new HashMap<String, Object>();
        if (resultDtoList.size() > 0)
            deptInfoMap.put("loginDeptDto", resultDtoList.get(0));
        deptInfoMap.put("deptInfoList", resultDtoList);
        deptInfoMap.put("depNoCut", depNoCut);
        deptInfoMap.put("iszcgly", iszcgly);
        deptInfoMap.put("isboss", isboss);
        userInfo.setPropertyMap(deptInfoMap);

        return new SpringSecureUserInfo(userInfo, "USER", "ADMIN");
    }
}
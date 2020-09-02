package com.mine.product.czmtr.ram.outapi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mine.base.user.dto.DeptInfoDto;
import com.mine.base.user.dto.UserInfoDto;
import com.mine.base.user.service.IUserService;
import com.vgtech.platform.common.utility.SpringSecureUserInfo;
import com.vgtech.platform.common.utility.VGUtility;

@Service(value = "mineSpringSecureUserService")
public class MineSpringSecureUserService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(MineSpringSecureUserService.class);
    @Autowired
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        logger.info("Search User[{}]", s);
        List<String> deptIdList = new ArrayList<String>();
        List<DeptInfoDto> resultDtoList = new ArrayList<DeptInfoDto>();

        UserInfoDto userInfo = userService.getUserInfoByUserName("RAM_WEB", s);
        if (VGUtility.isEmpty(userInfo))
            throw new UsernameNotFoundException("用户名不存在");

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

        return new SpringSecureUserInfo(userInfo, "USER", "ADMIN");
    }
}
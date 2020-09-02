package com.mine.product.czmtr.ram.main.controller;

import com.mine.base.user.dto.UserInfoDto;
import com.mine.base.user.service.IUserService;
import com.mine.platform.common.dto.CommonComboDto;
import com.vgtech.platform.common.utility.MineSecureUtility;
import com.vgtech.platform.common.utility.SpringSecureUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/ram/")
@SessionAttributes(value = {"LoginUserInfo", "loginUserDto"})
public class RamController {
    @Autowired
    private IUserService userService;

    /**
     * 获取interface中枚举数据加载到页面
     *
     * @param moduleName 模块路径
     * @param methodName 枚举方法名
     * @return
     * @throws Exception
     */
    @GetMapping(value = "enum_combo")
    @ResponseBody
    public List<CommonComboDto> getEnumCombobox(@RequestParam String moduleName, @RequestParam String methodName) throws Exception {
        List<CommonComboDto> comboDtoList = new ArrayList<CommonComboDto>();
        String clazzName = "com.mine.product.czmtr." + moduleName + "$" + methodName;
        Class<?> clazz = this.getClass().getClassLoader().loadClass(clazzName);
        Method method = clazz.getMethod("values");
        Object[] inters = (Object[]) method.invoke(null);
        for (Object inter : inters) {
            CommonComboDto comboDto = new CommonComboDto();
            method = inter.getClass().getMethod("name");
            comboDto.setText((String) method.invoke(inter));
            method = inter.getClass().getMethod("ordinal");
            comboDto.setValue(Integer.toString((int) method.invoke(inter)));
            comboDtoList.add(comboDto);
        }
        return comboDtoList;
    }

    @GetMapping(value = "enum_combo_by_source_type")
    @ResponseBody
    public List<CommonComboDto> getEnumComboboxBySourceType() throws Exception {
        List<CommonComboDto> comboDtoList = new ArrayList<CommonComboDto>();
        String clazzName = "com.mine.product.czmtr.ram.asset.service.IAssetService$SOURCE_TYPE";
        Class<?> clazz = this.getClass().getClassLoader().loadClass(clazzName);
        Method method = clazz.getMethod("values");
        Object[] inters = (Object[]) method.invoke(null);
        for (Object inter : inters) {
            CommonComboDto comboDto = new CommonComboDto();
            method = inter.getClass().getMethod("name");
            comboDto.setText((String) method.invoke(inter));
            if (comboDto.getText().equals("投资") || comboDto.getText().equals("捐入") || comboDto.getText().equals("盘盈")) {
                method = inter.getClass().getMethod("ordinal");
                comboDto.setValue(Integer.toString((int) method.invoke(inter)));
                comboDtoList.add(comboDto);
            }
        }
        return comboDtoList;
    }

    @GetMapping(value = "enum_combo_by_asset_status")
    @ResponseBody
    public List<CommonComboDto> getEnumComboboxByASSETSTATUS() throws Exception {
        List<CommonComboDto> comboDtoList = new ArrayList<CommonComboDto>();
        String clazzName = "com.mine.product.czmtr.ram.asset.service.IAssetService$ASSET_STATUS";
        Class<?> clazz = this.getClass().getClassLoader().loadClass(clazzName);
        Method method = clazz.getMethod("values");
        Object[] inters = (Object[]) method.invoke(null);
        for (Object inter : inters) {
            CommonComboDto comboDto = new CommonComboDto();
            method = inter.getClass().getMethod("name");
            comboDto.setText((String) method.invoke(inter));
            if (comboDto.getText().equals("使用") || comboDto.getText().equals("借出") || comboDto.getText().equals("封存")
                    || comboDto.getText().equals("报废")
                    || comboDto.getText().equals("丢失")
                    || comboDto.getText().equals("捐出")
                    || comboDto.getText().equals("停用")
                    || comboDto.getText().equals("闲置")
                    || comboDto.getText().equals("冻结")) {
                method = inter.getClass().getMethod("ordinal");
                comboDto.setValue(Integer.toString((int) method.invoke(inter)));
                comboDtoList.add(comboDto);
            }
        }
        return comboDtoList;
    }

    /***
     * 手工录入时,盘点结果只可以选择盘亏和相符
     */
    @GetMapping(value = "enum_combo_by_inventory_result")
    @ResponseBody
    public List<CommonComboDto> getEnumComboboxByINVENTORYRESULT() throws Exception {
        List<CommonComboDto> comboDtoList = new ArrayList<CommonComboDto>();
        String clazzName = "com.mine.product.czmtr.ram.asset.service.IAssetService$INVENTORY_RESULT";
        Class<?> clazz = this.getClass().getClassLoader().loadClass(clazzName);
        Method method = clazz.getMethod("values");
        Object[] inters = (Object[]) method.invoke(null);
        for (Object inter : inters) {
            CommonComboDto comboDto = new CommonComboDto();
            method = inter.getClass().getMethod("name");
            comboDto.setText((String) method.invoke(inter));
            if (comboDto.getText().equals("盘亏") || comboDto.getText().equals("相符")) {
                method = inter.getClass().getMethod("ordinal");
                comboDto.setValue(Integer.toString((int) method.invoke(inter)));
                comboDtoList.add(comboDto);
            }
        }
        return comboDtoList;
    }

    @PostMapping(value = "change_login_dept")
    @ResponseBody
    public String changeLoginDept(String deptId) {
        UserInfoDto userInfoDto = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        Map<String, Object> propertyMap = userInfoDto.getPropertyMap();
        propertyMap.put("loginDeptDto", userService.getDeptInfo(deptId));
        userInfoDto.setPropertyMap(propertyMap);
        return "{\"success\":true}";
    }


}

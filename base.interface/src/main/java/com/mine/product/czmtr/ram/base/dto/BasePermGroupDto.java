package com.mine.product.czmtr.ram.base.dto;

import java.util.List;

public class BasePermGroupDto {
    private String id;
    private String sysMark;
    private String code;
    private String chsName;
    private String engName;
    private List<String> userList;
    private String userListStr;
    private List<String> menuValuelist;
    private String menuValuelistStr;
    private List<String> itemValueList;
    private String itemValueListStr;
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSysMark() {
        return sysMark;
    }

    public void setSysMark(String sysMark) {
        this.sysMark = sysMark;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getChsName() {
        return chsName;
    }

    public void setChsName(String chsName) {
        this.chsName = chsName;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }

    public List<String> getMenuValuelist() {
        return menuValuelist;
    }

    public void setMenuValuelist(List<String> menuValuelist) {
        this.menuValuelist = menuValuelist;
    }

    public List<String> getItemValueList() {
        return itemValueList;
    }

    public void setItemValueList(List<String> itemValueList) {
        this.itemValueList = itemValueList;
    }

    public String getUserListStr() {
        return userListStr;
    }

    public void setUserListStr(String userListStr) {
        this.userListStr = userListStr;
    }

    public String getMenuValuelistStr() {
        return menuValuelistStr;
    }

    public void setMenuValuelistStr(String menuValuelistStr) {
        this.menuValuelistStr = menuValuelistStr;
    }

    public String getItemValueListStr() {
        return itemValueListStr;
    }

    public void setItemValueListStr(String itemValueListStr) {
        this.itemValueListStr = itemValueListStr;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

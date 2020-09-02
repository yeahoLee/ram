package com.mine.product.czmtr.ram.base.dto;

import java.io.Serializable;
import java.util.List;

public class BaseUserDto implements Serializable {
    private String id;
    private String userCode;
    private String userName;
    private String chsName;
    //当前所属部门
    private String permDeptId;
    private String permDeptCode;
    private String permDeptName;
    private boolean manager;
    private boolean leader;
    private List<String> permGroupIdList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getChsName() {
        return chsName;
    }

    public void setChsName(String chsName) {
        this.chsName = chsName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public boolean isManager() {
        return manager;
    }

    public void setManager(boolean manager) {
        this.manager = manager;
    }

    public boolean isLeader() {
        return leader;
    }

    public void setLeader(boolean leader) {
        this.leader = leader;
    }

    public String getPermDeptId() {
        return permDeptId;
    }

    public void setPermDeptId(String permDeptId) {
        this.permDeptId = permDeptId;
    }

    public String getPermDeptName() {
        return permDeptName;
    }

    public void setPermDeptName(String permDeptName) {
        this.permDeptName = permDeptName;
    }

    public String getPermDeptCode() {
        return permDeptCode;
    }

    public void setPermDeptCode(String permDeptCode) {
        this.permDeptCode = permDeptCode;
    }

    public List<String> getPermGroupIdList() {
        return permGroupIdList;
    }

    public void setPermGroupIdList(List<String> permGroupIdList) {
        this.permGroupIdList = permGroupIdList;
    }
}

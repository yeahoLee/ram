package com.mine.product.czmtr.ram.base.dto;

import java.util.Date;

/**
 * 员工编码和角色编码映射表
 *
 * @author rockh
 */
public class EmpCodeMapRoleCodeDto {
    private String id;
    //员工编码
    private String empCode;
    //角色编码
    private String RoleCode;
    //创建时间
    private Date createTimestamp;
    private Date lastUpdateTimestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getRoleCode() {
        return RoleCode;
    }

    public void setRoleCode(String roleCode) {
        RoleCode = roleCode;
    }

    public Date getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Date createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public Date getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(Date lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }


}

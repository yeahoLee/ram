package com.mine.product.czmtr.ram.base.dto;

import java.io.Serializable;

/**
 * @author 杨杰
 * @date 2019/12/25 16:24
 */
public class SyncCKDto implements Serializable {
    private static final long serialVersionUID = 2417233080689799127L;
    private String DEPT_NO;         //人力资源部门档案编码
    private String DEPT_NAME;       //人力资源部门档案名称
    private String CK_NO;           //财务部门档案编码
    private String CK_NAME;         //财务部门档案名称
    private String DEPT_LX;         //部门归属类型编码
    private String DEPT_LB;         //部门归属类别名称

    public String getDEPT_NO() {
        return DEPT_NO;
    }

    public void setDEPT_NO(String DEPT_NO) {
        this.DEPT_NO = DEPT_NO;
    }

    public String getDEPT_NAME() {
        return DEPT_NAME;
    }

    public void setDEPT_NAME(String DEPT_NAME) {
        this.DEPT_NAME = DEPT_NAME;
    }

    public String getCK_NO() {
        return CK_NO;
    }

    public void setCK_NO(String CK_NO) {
        this.CK_NO = CK_NO;
    }

    public String getCK_NAME() {
        return CK_NAME;
    }

    public void setCK_NAME(String CK_NAME) {
        this.CK_NAME = CK_NAME;
    }

    public String getDEPT_LX() {
        return DEPT_LX;
    }

    public void setDEPT_LX(String DEPT_LX) {
        this.DEPT_LX = DEPT_LX;
    }

    public String getDEPT_LB() {
        return DEPT_LB;
    }

    public void setDEPT_LB(String DEPT_LB) {
        this.DEPT_LB = DEPT_LB;
    }
}

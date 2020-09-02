package com.mine.product.czmtr.ram.base.dto;

import java.io.Serializable;

public class SyncDeptDto implements Serializable {
    private static final long serialVersionUID = -5952658891793025356L;
    private String DEPT_ID; //部门Id
    private String COMP_NO; //公司编码
    private String DEPT_NO; //部门编码
    private String DEPT_NAME; //部门名称
    private String P_DEPT_ID; //上级部门Id
    private String ISSEALUP; //部门状态
    private String LEVEL; //组织等级
    private String CREATE_TIME; //创建时间
    private String LASTUPDATE_TIME; //最后更新时间
    private String MEMO; //备注
    private String P_DEPT_NO; //上级部门编码
    private String DEPT_TYPE; //部门类别
    private String IS_DEL; //是否删除
    private String F_ORDER; //显示排序号
    private String IS_UPDATE; //是否更新

    public String getDEPT_ID() {
        return DEPT_ID;
    }

    public void setDEPT_ID(String dEPT_ID) {
        DEPT_ID = dEPT_ID;
    }

    public String getCOMP_NO() {
        return COMP_NO;
    }

    public void setCOMP_NO(String cOMP_NO) {
        COMP_NO = cOMP_NO;
    }

    public String getDEPT_NO() {
        return DEPT_NO;
    }

    public void setDEPT_NO(String dEPT_NO) {
        DEPT_NO = dEPT_NO;
    }

    public String getDEPT_NAME() {
        return DEPT_NAME;
    }

    public void setDEPT_NAME(String dEPT_NAME) {
        DEPT_NAME = dEPT_NAME;
    }

    public String getP_DEPT_ID() {
        return P_DEPT_ID;
    }

    public void setP_DEPT_ID(String p_DEPT_ID) {
        P_DEPT_ID = p_DEPT_ID;
    }

    public String getISSEALUP() {
        return ISSEALUP;
    }

    public void setISSEALUP(String iSSEALUP) {
        ISSEALUP = iSSEALUP;
    }

    public String getLEVEL() {
        return LEVEL;
    }

    public void setLEVEL(String lEVEL) {
        LEVEL = lEVEL;
    }

    public String getCREATE_TIME() {
        return CREATE_TIME;
    }

    public void setCREATE_TIME(String cREATE_TIME) {
        CREATE_TIME = cREATE_TIME;
    }

    public String getLASTUPDATE_TIME() {
        return LASTUPDATE_TIME;
    }

    public void setLASTUPDATE_TIME(String lASTUPDATE_TIME) {
        LASTUPDATE_TIME = lASTUPDATE_TIME;
    }

    public String getMEMO() {
        return MEMO;
    }

    public void setMEMO(String mEMO) {
        MEMO = mEMO;
    }

    public String getP_DEPT_NO() {
        return P_DEPT_NO;
    }

    public void setP_DEPT_NO(String p_DEPT_NO) {
        P_DEPT_NO = p_DEPT_NO;
    }

    public String getDEPT_TYPE() {
        return DEPT_TYPE;
    }

    public void setDEPT_TYPE(String dEPT_TYPE) {
        DEPT_TYPE = dEPT_TYPE;
    }

    public String getIS_DEL() {
        return IS_DEL;
    }

    public void setIS_DEL(String iS_DEL) {
        IS_DEL = iS_DEL;
    }

    public String getF_ORDER() {
        return F_ORDER;
    }

    public void setF_ORDER(String f_ORDER) {
        F_ORDER = f_ORDER;
    }

    public String getIS_UPDATE() {
        return IS_UPDATE;
    }

    public void setIS_UPDATE(String iS_UPDATE) {
        IS_UPDATE = iS_UPDATE;
    }
}

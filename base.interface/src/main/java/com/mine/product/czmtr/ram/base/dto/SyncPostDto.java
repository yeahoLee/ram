package com.mine.product.czmtr.ram.base.dto;

import java.io.Serializable;

public class SyncPostDto implements Serializable {
    private static final long serialVersionUID = -3363937708212826216L;
    private String POST_ID; //岗位Id
    private String POST_NO; //岗位编码
    private String POST_NAME; //岗位名称
    private String POST_TYPE; //岗位类型
    private String DEPT_ID; //部门Id
    private String DEPT_NO; //部门编码
    private String P_POST_ID; //父级岗位Id
    private String P_POST_NO; //父级岗位编码
    private String CREATE_TIME; //创建时间
    private String LASTUPDATE_TIME; //最后修改时间
    private String IS_DEL; //是否删除
    private String IS_UPDATE; //是否更新
    private String FDELETE_STATUS; //删除状态

    public String getPOST_ID() {
        return POST_ID;
    }

    public void setPOST_ID(String pOST_ID) {
        POST_ID = pOST_ID;
    }

    public String getPOST_NO() {
        return POST_NO;
    }

    public void setPOST_NO(String pOST_NO) {
        POST_NO = pOST_NO;
    }

    public String getPOST_NAME() {
        return POST_NAME;
    }

    public void setPOST_NAME(String pOST_NAME) {
        POST_NAME = pOST_NAME;
    }

    public String getPOST_TYPE() {
        return POST_TYPE;
    }

    public void setPOST_TYPE(String pOST_TYPE) {
        POST_TYPE = pOST_TYPE;
    }

    public String getDEPT_ID() {
        return DEPT_ID;
    }

    public void setDEPT_ID(String dEPT_ID) {
        DEPT_ID = dEPT_ID;
    }

    public String getDEPT_NO() {
        return DEPT_NO;
    }

    public void setDEPT_NO(String dEPT_NO) {
        DEPT_NO = dEPT_NO;
    }

    public String getP_POST_ID() {
        return P_POST_ID;
    }

    public void setP_POST_ID(String p_POST_ID) {
        P_POST_ID = p_POST_ID;
    }

    public String getP_POST_NO() {
        return P_POST_NO;
    }

    public void setP_POST_NO(String p_POST_NO) {
        P_POST_NO = p_POST_NO;
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

    public String getIS_DEL() {
        return IS_DEL;
    }

    public void setIS_DEL(String iS_DEL) {
        IS_DEL = iS_DEL;
    }

    public String getIS_UPDATE() {
        return IS_UPDATE;
    }

    public void setIS_UPDATE(String iS_UPDATE) {
        IS_UPDATE = iS_UPDATE;
    }

    public String getFDELETE_STATUS() {
        return FDELETE_STATUS;
    }

    public void setFDELETE_STATUS(String fDELETE_STATUS) {
        FDELETE_STATUS = fDELETE_STATUS;
    }
}

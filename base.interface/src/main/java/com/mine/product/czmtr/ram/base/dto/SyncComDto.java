package com.mine.product.czmtr.ram.base.dto;

import java.io.Serializable;

public class SyncComDto implements Serializable {
    private static final long serialVersionUID = -270016888530433125L;
    private String COMP_NO; //公司编码
    private String COMP_NAME; //公司名称
    private String COMP_SHORT; //公司简称
    private String COMP_PERSON; //公司法人
    private String COMP_ESTDATE; //成立时间
    private String COMP_ADDRESS; //公司地址
    private String MEMO; //备注
    private String CREATE_TIME; //创建时间
    private String LASTUPDATE_TIME; //最后更改时间
    private String COMP_TYPE; //公司类别
    private String COMP_ID; //公司Id
    private String IS_DEL; //是否删除
    private String F_ORDER; //显示排序号
    private String IS_UPDATE; //是否更新

    public String getCOMP_NO() {
        return COMP_NO;
    }

    public void setCOMP_NO(String cOMP_NO) {
        COMP_NO = cOMP_NO;
    }

    public String getCOMP_SHORT() {
        return COMP_SHORT;
    }

    public void setCOMP_SHORT(String cOMP_SHORT) {
        COMP_SHORT = cOMP_SHORT;
    }

    public String getCOMP_PERSON() {
        return COMP_PERSON;
    }

    public void setCOMP_PERSON(String cOMP_PERSON) {
        COMP_PERSON = cOMP_PERSON;
    }

    public String getCOMP_ESTDATE() {
        return COMP_ESTDATE;
    }

    public void setCOMP_ESTDATE(String cOMP_ESTDATE) {
        COMP_ESTDATE = cOMP_ESTDATE;
    }

    public String getCOMP_ADDRESS() {
        return COMP_ADDRESS;
    }

    public void setCOMP_ADDRESS(String cOMP_ADDRESS) {
        COMP_ADDRESS = cOMP_ADDRESS;
    }

    public String getMEMO() {
        return MEMO;
    }

    public void setMEMO(String mEMO) {
        MEMO = mEMO;
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

    public String getCOMP_TYPE() {
        return COMP_TYPE;
    }

    public void setCOMP_TYPE(String cOMP_TYPE) {
        COMP_TYPE = cOMP_TYPE;
    }

    public String getCOMP_ID() {
        return COMP_ID;
    }

    public void setCOMP_ID(String cOMP_ID) {
        COMP_ID = cOMP_ID;
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

    public String getCOMP_NAME() {
        return COMP_NAME;
    }

    public void setCOMP_NAME(String cOMP_NAME) {
        COMP_NAME = cOMP_NAME;
    }
}

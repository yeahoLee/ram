package com.mine.product.czmtr.ram.base.dto;

import java.io.Serializable;

public class BaseCommonCodeDto implements Serializable {
    private static final long serialVersionUID = 8089700838403764017L;

    private String id;
    private String code;
    private String typeId;
    private String typeName;
    private String chsName;
    private String engName;
    private String converCode;
    private String runningNum;
    private String parentCodeId;
    private String parentCodeName;
    private String W_PRO_CODE; //物资属性
    private String W_TYPE_CODE; //物资类型
    private String PRICE; //参考单价
    private String MARTERIALS_SPEC; //规格型号
    private String W_UNIT_CODE; //计量单位
    private String W_IS_PRO; //是否进设备台账
    private String IS_DAN; //是否危化品
    private String IS_DIRECT; //是否直发
    private String MARTERIALS_STATE; //使用状态
    private String BRAND_NAME; //品牌名称
    private String EXPIRATION_DATE; //保质期
    private String IS_DEL; //删除标志

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
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

    public String getConverCode() {
        return converCode;
    }

    public void setConverCode(String converCode) {
        this.converCode = converCode;
    }

    public String getRunningNum() {
        return runningNum;
    }

    public void setRunningNum(String runningNum) {
        this.runningNum = runningNum;
    }

    public String getParentCodeId() {
        return parentCodeId;
    }

    public void setParentCodeId(String parentCodeId) {
        this.parentCodeId = parentCodeId;
    }

    public String getParentCodeName() {
        return parentCodeName;
    }

    public void setParentCodeName(String parentCodeName) {
        this.parentCodeName = parentCodeName;
    }

    public String getW_PRO_CODE() {
        return W_PRO_CODE;
    }

    public void setW_PRO_CODE(String w_PRO_CODE) {
        W_PRO_CODE = w_PRO_CODE;
    }

    public String getW_TYPE_CODE() {
        return W_TYPE_CODE;
    }

    public void setW_TYPE_CODE(String w_TYPE_CODE) {
        W_TYPE_CODE = w_TYPE_CODE;
    }

    public String getPRICE() {
        return PRICE;
    }

    public void setPRICE(String pRICE) {
        PRICE = pRICE;
    }

    public String getMARTERIALS_SPEC() {
        return MARTERIALS_SPEC;
    }

    public void setMARTERIALS_SPEC(String mARTERIALS_SPEC) {
        MARTERIALS_SPEC = mARTERIALS_SPEC;
    }

    public String getW_UNIT_CODE() {
        return W_UNIT_CODE;
    }

    public void setW_UNIT_CODE(String w_UNIT_CODE) {
        W_UNIT_CODE = w_UNIT_CODE;
    }

    public String getW_IS_PRO() {
        return W_IS_PRO;
    }

    public void setW_IS_PRO(String w_IS_PRO) {
        W_IS_PRO = w_IS_PRO;
    }

    public String getIS_DAN() {
        return IS_DAN;
    }

    public void setIS_DAN(String iS_DAN) {
        IS_DAN = iS_DAN;
    }

    public String getIS_DIRECT() {
        return IS_DIRECT;
    }

    public void setIS_DIRECT(String iS_DIRECT) {
        IS_DIRECT = iS_DIRECT;
    }

    public String getMARTERIALS_STATE() {
        return MARTERIALS_STATE;
    }

    public void setMARTERIALS_STATE(String mARTERIALS_STATE) {
        MARTERIALS_STATE = mARTERIALS_STATE;
    }

    public String getBRAND_NAME() {
        return BRAND_NAME;
    }

    public void setBRAND_NAME(String bRAND_NAME) {
        BRAND_NAME = bRAND_NAME;
    }

    public String getEXPIRATION_DATE() {
        return EXPIRATION_DATE;
    }

    public void setEXPIRATION_DATE(String eXPIRATION_DATE) {
        EXPIRATION_DATE = eXPIRATION_DATE;
    }

    public String getIS_DEL() {
        return IS_DEL;
    }

    public void setIS_DEL(String iS_DEL) {
        IS_DEL = iS_DEL;
    }
}

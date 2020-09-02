package com.mine.product.czmtr.ram.outapi.dto;

import java.io.Serializable;

public class LocationSyncBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2158866259362850569L;
    private String LOCATION_CODE; // 位置编码 LOCATION_CODE
    private String LOCATION_DESC; // 位置描述 LOCATION_DESC
    private String LOCATION_SHORTDESC;// 位置简称 LOCATION_SHORTDESC
    private String LOCATIONTYPE_CODE;// 位置分类代码 LOCATIONTYPE_CODE
    private String PARENTLOCATION_CODE;// 父位置编码 PARENTLOCATION_CODE
    private String LOCATION_LEVEL;// 级别 LOCATION_LEVEL
    private String LOCATION_STATUS;// 位置状态 LOCATION_STATUS

    public String getLOCATION_CODE() {
        return LOCATION_CODE;
    }

    public void setLOCATION_CODE(String lOCATION_CODE) {
        LOCATION_CODE = lOCATION_CODE;
    }

    public String getLOCATION_DESC() {
        return LOCATION_DESC;
    }

    public void setLOCATION_DESC(String lOCATION_DESC) {
        LOCATION_DESC = lOCATION_DESC;
    }

    public String getLOCATION_SHORTDESC() {
        return LOCATION_SHORTDESC;
    }

    public void setLOCATION_SHORTDESC(String lOCATION_SHORTDESC) {
        LOCATION_SHORTDESC = lOCATION_SHORTDESC;
    }

    public String getLOCATIONTYPE_CODE() {
        return LOCATIONTYPE_CODE;
    }

    public void setLOCATIONTYPE_CODE(String lOCATIONTYPE_CODE) {
        LOCATIONTYPE_CODE = lOCATIONTYPE_CODE;
    }

    public String getPARENTLOCATION_CODE() {
        return PARENTLOCATION_CODE;
    }

    public void setPARENTLOCATION_CODE(String pARENTLOCATION_CODE) {
        PARENTLOCATION_CODE = pARENTLOCATION_CODE;
    }

    public String getLOCATION_LEVEL() {
        return LOCATION_LEVEL;
    }

    public void setLOCATION_LEVEL(String lOCATION_LEVEL) {
        LOCATION_LEVEL = lOCATION_LEVEL;
    }

    public String getLOCATION_STATUS() {
        return LOCATION_STATUS;
    }

    public void setLOCATION_STATUS(String lOCATION_STATUS) {
        LOCATION_STATUS = lOCATION_STATUS;
    }

}

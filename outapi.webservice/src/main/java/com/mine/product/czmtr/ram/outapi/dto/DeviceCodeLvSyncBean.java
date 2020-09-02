package com.mine.product.czmtr.ram.outapi.dto;

import java.io.Serializable;

public class DeviceCodeLvSyncBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7682628938699354371L;
    private String CODE; // 资产分类代码
    private String NAME; // 资产分类名称
    private String PARENT_CODE;// 父资产代码
    private String ASSETS_LEVEL;// 资产分类级别
    private String IS_FIXEDASSETS;// 固定资产标识

    public String getCODE() {
        return CODE;
    }

    public void setCODE(String cODE) {
        CODE = cODE;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String nAME) {
        NAME = nAME;
    }

    public String getPARENT_CODE() {
        return PARENT_CODE;
    }

    public void setPARENT_CODE(String pARENT_CODE) {
        PARENT_CODE = pARENT_CODE;
    }

    public String getASSETS_LEVEL() {
        return ASSETS_LEVEL;
    }

    public void setASSETS_LEVEL(String aSSETS_LEVEL) {
        ASSETS_LEVEL = aSSETS_LEVEL;
    }

    public String getIS_FIXEDASSETS() {
        return IS_FIXEDASSETS;
    }

    public void setIS_FIXEDASSETS(String iS_FIXEDASSETS) {
        IS_FIXEDASSETS = iS_FIXEDASSETS;
    }

}

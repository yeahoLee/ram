package com.mine.product.czmtr.ram.outapi.dto;

import java.io.Serializable;

public class MaterialCodeBean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 196710654679249125L;
    private String materialCode;
    private String tempCode;

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getTempCode() {
        return tempCode;
    }

    public void setTempCode(String tempCode) {
        this.tempCode = tempCode;
    }

}

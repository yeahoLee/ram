package com.mine.product.czmtr.ram.asset.dto;

import java.io.Serializable;

public class MaterialCodeBean implements Serializable {
    private static final long serialVersionUID = -3672466440336645129L;
    private String code;
    private String equipcode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEquipcode() {
        return equipcode;
    }

    public void setEquipcode(String equipcode) {
        this.equipcode = equipcode;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}

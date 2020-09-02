package com.mine.product.czmtr.ram.outapi.dto;

import java.io.Serializable;

public class AssetCodeBean implements Serializable {

    private static final long serialVersionUID = -922297093807185509L;

    //资产编码
    private String assetCode;

    //验证码
    private String validateCode;

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }
}

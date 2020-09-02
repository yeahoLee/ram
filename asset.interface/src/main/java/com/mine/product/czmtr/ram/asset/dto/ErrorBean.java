package com.mine.product.czmtr.ram.asset.dto;

import java.io.Serializable;

public class ErrorBean implements Serializable {

    private static final long serialVersionUID = 1592123387486460944L;

    private String errorCode;

    private String errorDesc;        //错误信息描述

    private String assetCode;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }
}

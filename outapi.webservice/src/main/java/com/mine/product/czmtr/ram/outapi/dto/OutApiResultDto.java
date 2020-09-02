package com.mine.product.czmtr.ram.outapi.dto;

import java.io.Serializable;
import java.util.List;

import com.mine.product.czmtr.ram.asset.dto.AssetBean;
import com.mine.product.czmtr.ram.asset.dto.ErrorBean;

public class OutApiResultDto implements Serializable {

    private static final long serialVersionUID = -5344364747511217319L;

    private int result;

    private String errorDesc;

    private String errorCode;

    private List<AssetCodeBean> codeBeans;

    private List<ErrorBean> errorBeans;

    private List<AssetBean> assetDtoList;

    private int count;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public List<AssetCodeBean> getCodeBeans() {
        return codeBeans;
    }

    public void setCodeBeans(List<AssetCodeBean> codeBeans) {
        this.codeBeans = codeBeans;
    }

    public List<ErrorBean> getErrorBeans() {
        return errorBeans;
    }

    public void setErrorBeans(List<ErrorBean> errorBeans) {
        this.errorBeans = errorBeans;
    }

    public List<AssetBean> getAssetDtoList() {
        return assetDtoList;
    }

    public void setAssetDtoList(List<AssetBean> assetDtoList) {
        this.assetDtoList = assetDtoList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

package com.mine.product.czmtr.ram.asset.dto;

import java.io.Serializable;

public class AssetStanBookSubmitDto implements Serializable {
    private String backToLastPageUrl;//返回上个页面的url
    private String assetIdList;//资产编码List的String形式
    private String materialCodeSubmit;//物资编码
    private String assetCodeSubmit;//资产编码
    private String assetChsNameSubmit;//资产名称
    private String assetStatusSubmit;//资产状态
    private String assetTypeSubmit;//资产类型
    private String manageDeptIdSubmit;//管理部门
    private String managerIdSubmit;//管理员
    private String useDeptIdSubmit;//使用部门
    private String userIdSubmit;//使用人
    private String savePlaceIdSubmit;//安装位置
    private String codeAndNameSubmit;//位置的code和name

    private String materialSearchCodeSubmit; //多条件搜索框的物资编码

    public String getCodeAndNameSubmit() {
        return codeAndNameSubmit;
    }

    public void setCodeAndNameSubmit(String codeAndNameSubmit) {
        this.codeAndNameSubmit = codeAndNameSubmit;
    }

    public String getAssetIdList() {
        return assetIdList;
    }

    public void setAssetIdList(String assetIdList) {
        this.assetIdList = assetIdList;
    }

    public String getBackToLastPageUrl() {
        return backToLastPageUrl;
    }

    public void setBackToLastPageUrl(String backToLastPageUrl) {
        this.backToLastPageUrl = backToLastPageUrl;
    }

    public String getMaterialCodeSubmit() {
        return materialCodeSubmit;
    }

    public void setMaterialCodeSubmit(String materialCodeSubmit) {
        this.materialCodeSubmit = materialCodeSubmit;
    }

    public String getAssetCodeSubmit() {
        return assetCodeSubmit;
    }

    public void setAssetCodeSubmit(String assetCodeSubmit) {
        this.assetCodeSubmit = assetCodeSubmit;
    }

    public String getAssetChsNameSubmit() {
        return assetChsNameSubmit;
    }

    public void setAssetChsNameSubmit(String assetChsNameSubmit) {
        this.assetChsNameSubmit = assetChsNameSubmit;
    }

    public String getAssetStatusSubmit() {
        return assetStatusSubmit;
    }

    public void setAssetStatusSubmit(String assetStatusSubmit) {
        this.assetStatusSubmit = assetStatusSubmit;
    }

    public String getAssetTypeSubmit() {
        return assetTypeSubmit;
    }

    public void setAssetTypeSubmit(String assetTypeSubmit) {
        this.assetTypeSubmit = assetTypeSubmit;
    }

    public String getManageDeptIdSubmit() {
        return manageDeptIdSubmit;
    }

    public void setManageDeptIdSubmit(String manageDeptIdSubmit) {
        this.manageDeptIdSubmit = manageDeptIdSubmit;
    }

    public String getManagerIdSubmit() {
        return managerIdSubmit;
    }

    public void setManagerIdSubmit(String managerIdSubmit) {
        this.managerIdSubmit = managerIdSubmit;
    }

    public String getUseDeptIdSubmit() {
        return useDeptIdSubmit;
    }

    public void setUseDeptIdSubmit(String useDeptIdSubmit) {
        this.useDeptIdSubmit = useDeptIdSubmit;
    }

    public String getUserIdSubmit() {
        return userIdSubmit;
    }

    public void setUserIdSubmit(String userIdSubmit) {
        this.userIdSubmit = userIdSubmit;
    }

    public String getSavePlaceIdSubmit() {
        return savePlaceIdSubmit;
    }

    public void setSavePlaceIdSubmit(String savePlaceIdSubmit) {
        this.savePlaceIdSubmit = savePlaceIdSubmit;
    }

    @Override
    public String toString() {
        return "AssetStanBookSubmit [backToLastPageUrl=" + backToLastPageUrl + ", assetIdList=" + assetIdList
                + ", materialCodeSubmit=" + materialCodeSubmit + ", assetCodeSubmit=" + assetCodeSubmit
                + ", assetChsNameSubmit=" + assetChsNameSubmit + ", assetStatusSubmit=" + assetStatusSubmit
                + ", assetTypeSubmit=" + assetTypeSubmit + ", manageDeptIdSubmit=" + manageDeptIdSubmit
                + ", managerIdSubmit=" + managerIdSubmit + ", useDeptIdSubmit=" + useDeptIdSubmit + ", userIdSubmit="
                + userIdSubmit + ", savePlaceIdSubmit=" + savePlaceIdSubmit + "]";
    }

    public String getMaterialSearchCodeSubmit() {
        return materialSearchCodeSubmit;
    }

    public void setMaterialSearchCodeSubmit(String materialSearchCodeSubmit) {
        this.materialSearchCodeSubmit = materialSearchCodeSubmit;
    }
}

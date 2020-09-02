package com.mine.product.czmtr.ram.asset.dto;

public class AssetUsabilityHistoryDto {

    private String manageDeptId; // 主管部门id
    private String manageDeptStr;
    private String assetTypeStr; // 资产类型
    private String use; // 使用
    private String idle; // 闲置
    private String sealup; // 封存
    private String disuse; // 停用
    private String borrow; // 借出
    private String materialCode; //物资编码
    private String assetCode;//资产编码

    public String getManageDeptId() {
        return manageDeptId;
    }

    public void setManageDeptId(String manageDeptId) {
        this.manageDeptId = manageDeptId;
    }

    public String getManageDeptStr() {
        return manageDeptStr;
    }

    public void setManageDeptStr(String manageDeptStr) {
        this.manageDeptStr = manageDeptStr;
    }

    public String getAssetTypeStr() {
        return assetTypeStr;
    }

    public void setAssetTypeStr(String assetTypeStr) {
        this.assetTypeStr = assetTypeStr;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public String getIdle() {
        return idle;
    }

    public void setIdle(String idle) {
        this.idle = idle;
    }

    public String getSealup() {
        return sealup;
    }

    public void setSealup(String sealup) {
        this.sealup = sealup;
    }

    public String getDisuse() {
        return disuse;
    }

    public void setDisuse(String disuse) {
        this.disuse = disuse;
    }

    public String getBorrow() {
        return borrow;
    }

    public void setBorrow(String borrow) {
        this.borrow = borrow;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }


}

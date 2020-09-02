package com.mine.product.czmtr.ram.base.dto;

import java.io.Serializable;
import java.util.List;

public class BaseStanBookMaterialDto implements Serializable {
    private static final long serialVersionUID = 6446139686179897503L;
    private String id;
    private String materialCode;//物资编码
    private String materialName;//物资名称
    private String materialType;//物资类型
    private String assetCount;//资产数量
    private String unitOfMeasStr;//计量单位
    private List<String> assetIdList;//同一物资编码的资产id的集合

    public List<String> getAssetIdList() {
        return assetIdList;
    }

    public void setAssetIdList(List<String> assetIdList) {
        this.assetIdList = assetIdList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getAssetCount() {
        return assetCount;
    }

    public void setAssetCount(String assetCount) {
        this.assetCount = assetCount;
    }

    public String getUnitOfMeasStr() {
        return unitOfMeasStr;
    }

    public void setUnitOfMeasStr(String unitOfMeasStr) {
        this.unitOfMeasStr = unitOfMeasStr;
    }
}

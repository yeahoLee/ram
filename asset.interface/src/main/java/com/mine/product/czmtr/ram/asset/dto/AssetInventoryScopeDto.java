package com.mine.product.czmtr.ram.asset.dto;

import java.util.Date;

import com.mine.product.czmtr.ram.asset.service.IAssetService.ASSET_STATUS;
import com.mine.product.czmtr.ram.asset.service.IAssetService.ASSET_TYPE;

/**
 * 盘点资产范围dto
 *
 * @author 17893
 */
public class AssetInventoryScopeDto {

    private String id;

    /**
     * 盘点id 主表id
     */
    private String assetInventoryId;


    private String inventoryContent;        //盘点内容


    private double inventoryNum;            //盘点数量


    private String manageDeptId;            //管理部门id


    private String assetStatus;        //资产状态


    private String useDeptId;                //使用部门id


    private String userId;                    //使用人id


    private String managerId;                //资产管理员id


    private String savePlaceId;            //具体位置id（ex存放地点）


    private String assetType;            //资产类型

    /**
     * 创建时间
     */
    private Date createTimestamp;

    /**
     * 更新时间
     */
    private Date lastUpdateTimestamp;

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getAssetInventoryId() {
        return assetInventoryId;
    }


    public void setAssetInventoryId(String assetInventoryId) {
        this.assetInventoryId = assetInventoryId;
    }


    public String getInventoryContent() {
        return inventoryContent;
    }


    public void setInventoryContent(String inventoryContent) {
        this.inventoryContent = inventoryContent;
    }


    public double getInventoryNum() {
        return inventoryNum;
    }


    public void setInventoryNum(double inventoryNum) {
        this.inventoryNum = inventoryNum;
    }


    public String getManageDeptId() {
        return manageDeptId;
    }


    public void setManageDeptId(String manageDeptId) {
        this.manageDeptId = manageDeptId;
    }


    public String getAssetStatus() {
        return assetStatus;
    }


    public void setAssetStatus(String assetStatus) {
        this.assetStatus = assetStatus;
    }


    public String getUseDeptId() {
        return useDeptId;
    }


    public void setUseDeptId(String useDeptId) {
        this.useDeptId = useDeptId;
    }


    public String getUserId() {
        return userId;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getManagerId() {
        return managerId;
    }


    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }


    public String getSavePlaceId() {
        return savePlaceId;
    }


    public void setSavePlaceId(String savePlaceId) {
        this.savePlaceId = savePlaceId;
    }


    public String getAssetType() {
        return assetType;
    }


    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }


    public Date getCreateTimestamp() {
        return createTimestamp;
    }


    public void setCreateTimestamp(Date createTimestamp) {
        this.createTimestamp = createTimestamp;
    }


    public Date getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }


    public void setLastUpdateTimestamp(Date lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

}

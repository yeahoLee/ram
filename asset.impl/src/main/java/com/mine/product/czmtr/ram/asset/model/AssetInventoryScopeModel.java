package com.mine.product.czmtr.ram.asset.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.mine.product.czmtr.ram.asset.service.IAssetService.ASSET_STATUS;
import com.mine.product.czmtr.ram.asset.service.IAssetService.ASSET_TYPE;

/**
 * 存储盘点范围
 *
 * @author 17893
 */
@Entity
public class AssetInventoryScopeModel {

    @Id                                            //主键
    @GeneratedValue(generator = "gen")            //主键生成策略，根据此标识来声明主键生成器
    @GenericGenerator(
            name = "gen",
            strategy = "uuid",
            parameters = {
                    @Parameter(name = "separator", value = "_")
            })
    private String id;

    /**
     * 盘点id 主表id
     */
    @ManyToOne
    private AssetInventoryModel assetInventoryModel;

    @Column
    private String inventoryContent;        //盘点内容

    @Column
    private double inventoryNum;            //盘点数量

    @Column
    private String manageDeptId;            //管理部门id

    @Column
    private ASSET_STATUS assetStatus;        //资产状态

    @Column
    private String useDeptId;                //使用部门id

    @Column
    private String userId;                    //使用人id

    @Column
    private String managerId;                //资产管理员id

    @Column
    private String savePlaceId;            //具体位置id（ex存放地点）

    @Column
    private String assetType;                //资产类型(其实存储的是物资编码)

    @Column
    private Date createTimestamp;

    @Column
    private Date lastUpdateTimestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInventoryContent() {
        return inventoryContent;
    }

    public void setInventoryContent(String inventoryContent) {
        this.inventoryContent = inventoryContent;
    }


    public AssetInventoryModel getAssetInventoryModel() {
        return assetInventoryModel;
    }

    public void setAssetInventoryModel(AssetInventoryModel assetInventoryModel) {
        this.assetInventoryModel = assetInventoryModel;
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

    public ASSET_STATUS getAssetStatus() {
        return assetStatus;
    }

    public void setAssetStatus(ASSET_STATUS assetStatus) {
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

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }
}

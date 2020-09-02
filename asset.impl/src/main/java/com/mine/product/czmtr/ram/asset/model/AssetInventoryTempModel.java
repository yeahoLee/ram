package com.mine.product.czmtr.ram.asset.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.mine.product.czmtr.ram.asset.service.IAssetService.INVENTORY_OPERATION;
import com.mine.product.czmtr.ram.asset.service.IAssetService.INVENTORY_RESULT;
import com.mine.product.czmtr.ram.asset.service.IAssetService.INVENTORY_WAY;

/**
 * 盘点资产清单
 */
@Entity
public class AssetInventoryTempModel {

    @Id // 主键
    @GeneratedValue(generator = "gen") // 主键生成策略，根据此标识来声明主键生成器
    @GenericGenerator(name = "gen", strategy = "uuid", parameters = {@Parameter(name = "separator", value = "_")})
    private String id; // 系统自动生成id

    @ManyToOne
    private AssetInventoryModel assetInventoryModel;

    /**
     * 我的盘点单
     */
    @ManyToOne
    private MyAssetInventoryModel myAssetInventoryModel;

    /**
     * 资产id
     */
    @Column
    private String assetId;

    /**
     * 创建时间
     */
    @Column
    private Date createTimestamp;

    /**
     * 更新时间
     */
    @Column
    private Date lastUpdateTimestamp;

    /**
     * 盘点操作
     */
    @Column
    private INVENTORY_OPERATION operation;

    /**
     * 盘点结果
     */
    @Column
    private INVENTORY_RESULT result;

    /**
     * 盘点方式
     */
    @Column
    private INVENTORY_WAY inventoryWay;
    /**
     * 主管部门id
     */
    @Column
    private String manageDeptId; //

    /*
     * 备注
     */
    @Column
    private String remark;

    @PrePersist
    public void updateWhenCreate() {
        createTimestamp = Calendar.getInstance().getTime();
        lastUpdateTimestamp = Calendar.getInstance().getTime();
    }

    @PreUpdate
    public void updateWhenUpdate() {
        lastUpdateTimestamp = Calendar.getInstance().getTime();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AssetInventoryModel getAssetInventoryModel() {
        return assetInventoryModel;
    }

    public void setAssetInventoryModel(AssetInventoryModel assetInventoryModel) {
        this.assetInventoryModel = assetInventoryModel;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
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

    public INVENTORY_OPERATION getOperation() {
        return operation;
    }

    public void setOperation(INVENTORY_OPERATION operation) {
        this.operation = operation;
    }

    public INVENTORY_RESULT getResult() {
        return result;
    }

    public void setResult(INVENTORY_RESULT result) {
        this.result = result;
    }

    public INVENTORY_WAY getInventoryWay() {
        return inventoryWay;
    }

    public void setInventoryWay(INVENTORY_WAY inventoryWay) {
        this.inventoryWay = inventoryWay;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public MyAssetInventoryModel getMyAssetInventoryModel() {
        return myAssetInventoryModel;
    }

    public void setMyAssetInventoryModel(MyAssetInventoryModel myAssetInventoryModel) {
        this.myAssetInventoryModel = myAssetInventoryModel;
    }

    public String getManageDeptId() {
        return manageDeptId;
    }

    public void setManageDeptId(String manageDeptId) {
        this.manageDeptId = manageDeptId;
    }


}

package com.mine.product.czmtr.ram.asset.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
public class AssetBorrowTempModel {
    @Id
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name = "gen", strategy = "uuid",
            parameters = {@Parameter(name = "separator", value = "_")})
    private String id;
    @ManyToOne
    private AssetBorrowModel assetBorrowModel;//资产借用 主表ID
    @ManyToOne
    private AssetRevertModel assetRevertModel;//资产归还  主表ID
    @Column
    private String savePlaceId;//使用位置ID
    @Column
    private String revertSavePlaceId;//归还位置ID
    @Column
    private String oldUserID;//原有使用人
    @Column
    private String newUserID;//当前使用人
    @Column
    private String assetId;//资产id
    @Column
    private Date createTimestamp;
    @Column
    private Date lastUpdateTimestamp;

    @PrePersist
    public void updateWhenCreate() {
        createTimestamp = Calendar.getInstance().getTime();
        lastUpdateTimestamp = Calendar.getInstance().getTime();
    }

    @PreUpdate
    public void updateWhenUpdate() {
        lastUpdateTimestamp = Calendar.getInstance().getTime();
    }

    public String getOldUserID() {
        return oldUserID;
    }

    public void setOldUserID(String oldUserID) {
        this.oldUserID = oldUserID;
    }

    public String getNewUserID() {
        return newUserID;
    }

    public void setNewUserID(String newUserID) {
        this.newUserID = newUserID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public AssetBorrowModel getAssetBorrowModel() {
        return assetBorrowModel;
    }

    public void setAssetBorrowModel(AssetBorrowModel assetBorrowModel) {
        this.assetBorrowModel = assetBorrowModel;
    }

    public AssetRevertModel getAssetRevertModel() {
        return assetRevertModel;
    }

    public void setAssetRevertModel(AssetRevertModel assetRevertModel) {
        this.assetRevertModel = assetRevertModel;
    }

    public String getRevertSavePlaceId() {
        return revertSavePlaceId;
    }

    public void setRevertSavePlaceId(String revertSavePlaceId) {
        this.revertSavePlaceId = revertSavePlaceId;
    }

}

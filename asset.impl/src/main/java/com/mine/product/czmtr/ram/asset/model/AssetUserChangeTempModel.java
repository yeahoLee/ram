package com.mine.product.czmtr.ram.asset.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 资产使用人变更台账明细表
 *
 * @author sun.guoli
 */
@Entity
public class AssetUserChangeTempModel {
    @Id
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name = "gen", strategy = "uuid",
            parameters = {@Parameter(name = "separator", value = "_")})
    private String id;
    @Column
    private String assetUserChangeModelId;//使用人变更台账表ID
    @Column
    private String assetId;//资产ID
    @Column
    private Date createTimestamp; //创建时间
    @Column
    private Date lastUpdateTimestamp;//更新时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssetUserChangeModelId() {
        return assetUserChangeModelId;
    }

    public void setAssetUserChangeModelId(String assetUserChangeModelId) {
        this.assetUserChangeModelId = assetUserChangeModelId;
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

    @PrePersist
    public void updateWhenCreate() {
        createTimestamp = Calendar.getInstance().getTime();
        lastUpdateTimestamp = Calendar.getInstance().getTime();
    }

    @PreUpdate
    public void updateWhenUpdate() {
        lastUpdateTimestamp = Calendar.getInstance().getTime();
    }
}

package com.mine.product.czmtr.ram.asset.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
public class AssetSequestrationTempModel {

    @Id
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name = "gen", strategy = "uuid",
            parameters = {@Parameter(name = "separator", value = "_")})
    private String id;

    /**
     * 封存/启封id 主表id
     */
    @ManyToOne
    private AssetSequestrationModel assetSequestrationModel;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AssetSequestrationModel getAssetSequestrationModel() {
        return assetSequestrationModel;
    }

    public void setAssetSequestrationModel(AssetSequestrationModel assetSequestrationModel) {
        this.assetSequestrationModel = assetSequestrationModel;
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

}

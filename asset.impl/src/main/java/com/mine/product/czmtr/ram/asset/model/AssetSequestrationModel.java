package com.mine.product.czmtr.ram.asset.model;

import com.mine.product.czmtr.ram.asset.dto.FlowableInfo;
import com.mine.product.czmtr.ram.asset.service.IAssetService;
import com.mine.product.czmtr.ram.asset.service.IAssetService.SEALED_UNSEALED;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class AssetSequestrationModel {

    @Id
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name = "gen", strategy = "uuid",
            parameters = {@Parameter(name = "separator", value = "_")})
    private String id;

    /**
     * 封存/启封编号
     */
    @Column
    private String sequestrateNum;

    /**
     * 封存位置
     */
    @Column
    private String sealPlaceId;

    /**
     * 封存/启用原因
     */
    @Column
    private String sealReason;

    /**
     * 封存/启封状态
     */
    @Column
    private SEALED_UNSEALED sealStatus;

    /**
     * 审批状态
     */
    @Column
    private FlowableInfo.WORKSTATUS sealApproveStatus;

    /**
     * 发起日期
     */
    @Column
    private Date launchDate;

    /**
     * 创建者
     */
    @Column
    private String sponsor;

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
    //生产型物资 和非生产型物资；
    @Column
    private IAssetService.ASSET_PRODUCE_TYPE produceType;


    public IAssetService.ASSET_PRODUCE_TYPE getProduceType() {
        return produceType;
    }

    public void setProduceType(IAssetService.ASSET_PRODUCE_TYPE produceType) {
        this.produceType = produceType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSequestrateNum() {
        return sequestrateNum;
    }

    public void setSequestrateNum(String sequestrateNum) {
        this.sequestrateNum = sequestrateNum;
    }

    public String getSealPlaceId() {
        return sealPlaceId;
    }

    public void setSealPlaceId(String sealPlaceId) {
        this.sealPlaceId = sealPlaceId;
    }

    public String getSealReason() {
        return sealReason;
    }

    public void setSealReason(String sealReason) {
        this.sealReason = sealReason;
    }

    public SEALED_UNSEALED getSealStatus() {
        return sealStatus;
    }

    public void setSealStatus(SEALED_UNSEALED sealStatus) {
        this.sealStatus = sealStatus;
    }

    public FlowableInfo.WORKSTATUS getSealApproveStatus() {
        return sealApproveStatus;
    }

    public void setSealApproveStatus(FlowableInfo.WORKSTATUS sealApproveStatus) {
        this.sealApproveStatus = sealApproveStatus;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
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

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

}

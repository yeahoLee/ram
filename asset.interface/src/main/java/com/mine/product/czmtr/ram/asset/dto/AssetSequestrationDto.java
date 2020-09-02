package com.mine.product.czmtr.ram.asset.dto;

import com.mine.product.czmtr.ram.asset.service.IAssetService;
import com.mine.product.czmtr.ram.asset.service.IAssetService.SEALED_UNSEALED;

import java.io.Serializable;
import java.util.Date;

public class AssetSequestrationDto implements Serializable {

    private String id;

    /**
     * 封存/启封编号
     */
    private String sequestrateNum;

    /**
     * 封存位置
     */
    private String sealPlaceId;

    /**
     * 封存/启用原因
     */
    private String sealReason;

    /**
     * 封存/启封状态
     */
    private SEALED_UNSEALED sealStatus;

    /**
     * 审批状态
     */
    private FlowableInfo.WORKSTATUS sealApproveStatus;

    /**
     * 审批状态
     */
    private int sealApproveStatusStr;

    /**
     * 发起日期
     */
    private Date launchDate;

    /**
     * 发起人
     */
    private String sponsor;

    private String sponsorStr;

    private String sealPlaceStr;

    private String approve;

    private String launchDateStr;

    /**
     * 创建时间
     */
    private Date createTimestamp;

    /**
     * 更新时间
     */
    private Date lastUpdateTimestamp;

    private String createTimestampStart; //借用日期 开始(查询条件)

    private String createTimestampEnd; //借用日期 结束(查询条件)
    //生产型物资 和非生产型物资；
    private IAssetService.ASSET_PRODUCE_TYPE produceType;

    private String produceTypeStr;
    private String produceStr;

    public String getProduceStr() {
        return produceStr;
    }

    public void setProduceStr(String produceStr) {
        this.produceStr = produceStr;
    }

    public String getProduceTypeStr() {
        return produceTypeStr;
    }

    public void setProduceTypeStr(String produceTypeStr) {
        this.produceType = IAssetService.ASSET_PRODUCE_TYPE.values()[Integer.parseInt(produceTypeStr)];
        this.produceTypeStr = produceTypeStr;
    }

    public void setProduceType(IAssetService.ASSET_PRODUCE_TYPE produceType) {
        if (null != produceType) {
            this.produceTypeStr = Integer.toString(produceType.ordinal());
            this.produceStr= produceType.name();
        }
        this.produceType = produceType;
    }
    public IAssetService.ASSET_PRODUCE_TYPE getProduceType() {
        return produceType;
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

    public String getLaunchDateStr() {
        return launchDateStr;
    }

    public void setLaunchDateStr(String launchDateStr) {
        this.launchDateStr = launchDateStr;
    }

    public String getSponsorStr() {
        return sponsorStr;
    }

    public void setSponsorStr(String sponsorStr) {
        this.sponsorStr = sponsorStr;
    }

    public String getSealPlaceStr() {
        return sealPlaceStr;
    }

    public void setSealPlaceStr(String sealPlaceStr) {
        this.sealPlaceStr = sealPlaceStr;
    }

    public String getApprove() {
        return approve;
    }

    public void setApprove(String approve) {
        this.approve = approve;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getCreateTimestampStart() {
        return createTimestampStart;
    }

    public void setCreateTimestampStart(String createTimestampStart) {
        this.createTimestampStart = createTimestampStart;
    }

    public String getCreateTimestampEnd() {
        return createTimestampEnd;
    }

    public void setCreateTimestampEnd(String createTimestampEnd) {
        this.createTimestampEnd = createTimestampEnd;
    }

    public int getSealApproveStatusStr() {
        return sealApproveStatusStr;
    }

    public void setSealApproveStatusStr(int sealApproveStatusStr) {
        this.sealApproveStatusStr = sealApproveStatusStr;
    }
}

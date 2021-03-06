package com.mine.product.czmtr.ram.asset.dto;

import com.mine.product.czmtr.ram.asset.service.IAssetService;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class AssetReduceDto implements Serializable {
    private String id;
    private String orderName;//减损单名称
    private String reduceType;//减损类型
    private String reduceTypeStr;//减损类型
    private String surplusValue;//剩余价值，系统自动计算
    private String processingCost;//处理费用
    private String actualLoss;//实际损失
    private String proposedDisposal;//拟处置办法
    private String reason;//变更原因
    private String receiptStatus;//审批状态
    private String receiptStatusStr;//审批状态Str
    private String createTimestamp; //创建时间
    private String lastUpdateTimestamp;//更新时间
    private List<String> assetIdList;//资产ID list
    private String assetIdListStr;
    private String assetChange;//变更资产
    private String changeNum;//减损单号
    private String createTimestampStart;// 变更日期-开始
    private String createTimestampEnd;// 变更日期-结束
    private String createUserId;//创建人
    private String createUserIdStr;//创建人
    private String assetNum;//资产数量

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

    public String getAssetNum() {
        return assetNum;
    }

    public void setAssetNum(String assetNum) {
        this.assetNum = assetNum;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserIdStr() {
        return createUserIdStr;
    }

    public void setCreateUserIdStr(String createUserIdStr) {
        this.createUserIdStr = createUserIdStr;
    }

    public String getAssetIdListStr() {
        return assetIdListStr;
    }

    public void setAssetIdListStr(String assetIdListStr) {
        this.assetIdListStr = assetIdListStr;
    }

    public List<String> getAssetIdList() {
        return assetIdList;
    }

    public void setAssetIdList(List<String> assetIdList) {
        this.assetIdList = assetIdList;
    }

    public String getChangeNum() {
        return changeNum;
    }

    public void setChangeNum(String changeNum) {
        this.changeNum = changeNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getReduceType() {
        return reduceType;
    }

    public void setReduceType(String reduceType) {
        this.reduceType = reduceType;
    }

    public String getReduceTypeStr() {
        return reduceTypeStr;
    }

    public void setReduceTypeStr(String reduceTypeStr) {
        this.reduceTypeStr = reduceTypeStr;
    }

    public String getSurplusValue() {
        return surplusValue;
    }

    public void setSurplusValue(String surplusValue) {
        this.surplusValue = surplusValue;
    }

    public String getProcessingCost() {
        return processingCost;
    }

    public void setProcessingCost(String processingCost) {
        this.processingCost = processingCost;
    }

    public String getActualLoss() {
        return actualLoss;
    }

    public void setActualLoss(String actualLoss) {
        this.actualLoss = actualLoss;
    }

    public String getProposedDisposal() {
        return proposedDisposal;
    }

    public void setProposedDisposal(String proposedDisposal) {
        this.proposedDisposal = proposedDisposal;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(String receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    public String getReceiptStatusStr() {
        return receiptStatusStr;
    }

    public void setReceiptStatusStr(String receiptStatusStr) {
        this.receiptStatusStr = receiptStatusStr;
    }

    public String getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(String createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public String getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(String lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

    public String getAssetChange() {
        return assetChange;
    }

    public void setAssetChange(String assetChange) {
        this.assetChange = assetChange;
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
}

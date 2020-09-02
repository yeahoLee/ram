package com.mine.product.czmtr.ram.asset.dto;

import com.mine.product.czmtr.ram.asset.service.IAssetService;

import java.io.Serializable;

public class AssetAllocationDto implements Serializable {

    private String id;
    private String assetAllocationCode;//调拨编码
    private String callOutDepartmentId;//调出部门
    private String callInDepartmentId;//调入部门
    private String callInAssetManagerId;//调入部门资产管理员
    private String callOutDepartmentName;//调出部门名称
    private String callInDepartmentName;//调入部门名称
    private String callInAssetManagerName;//调入部门资产管理员名称
    private String callInSavePlaceId;//调入位置
    private String callInSavePlaceStr;//调入位置
    private String reason; //调拨事由
    private String createUserID;
    private String createUserName;
    private String assetAllocationTempStr;//变更资产详情(例:投影仪和笔记本)
    private FlowableInfo.WORKSTATUS receiptStatus;//审批状态
    private int receiptStatusStr;//审批状态
    private String createTimestamp;
    private String lastUpdateTimestamp;
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


    public IAssetService.ASSET_PRODUCE_TYPE getProduceType() {
        return produceType;
    }

      public void setProduceType(IAssetService.ASSET_PRODUCE_TYPE produceType) {
        if (null != produceType) {
            this.produceTypeStr = Integer.toString(produceType.ordinal());
            this.produceStr= produceType.name();
        }
        this.produceType = produceType;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssetAllocationCode() {
        return assetAllocationCode;
    }

    public void setAssetAllocationCode(String assetAllocationCode) {
        this.assetAllocationCode = assetAllocationCode;
    }

    public String getCallOutDepartmentId() {
        return callOutDepartmentId;
    }

    public void setCallOutDepartmentId(String callOutDepartmentId) {
        this.callOutDepartmentId = callOutDepartmentId;
    }

    public String getCallInDepartmentId() {
        return callInDepartmentId;
    }

    public void setCallInDepartmentId(String callInDepartmentId) {
        this.callInDepartmentId = callInDepartmentId;
    }

    public String getCallInAssetManagerId() {
        return callInAssetManagerId;
    }

    public void setCallInAssetManagerId(String callInAssetManagerId) {
        this.callInAssetManagerId = callInAssetManagerId;
    }

    public String getCallInSavePlaceId() {
        return callInSavePlaceId;
    }

    public void setCallInSavePlaceId(String callInSavePlaceId) {
        this.callInSavePlaceId = callInSavePlaceId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCreateUserID() {
        return createUserID;
    }

    public void setCreateUserID(String createUserID) {
        this.createUserID = createUserID;
    }

    public FlowableInfo.WORKSTATUS getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(FlowableInfo.WORKSTATUS receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    public int getReceiptStatusStr() {
        return receiptStatusStr;
    }

    public void setReceiptStatusStr(int receiptStatusStr) {
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

    public String getCallOutDepartmentName() {
        return callOutDepartmentName;
    }

    public void setCallOutDepartmentName(String callOutDepartmentName) {
        this.callOutDepartmentName = callOutDepartmentName;
    }

    public String getCallInDepartmentName() {
        return callInDepartmentName;
    }

    public void setCallInDepartmentName(String callInDepartmentName) {
        this.callInDepartmentName = callInDepartmentName;
    }

    public String getCallInAssetManagerName() {
        return callInAssetManagerName;
    }

    public void setCallInAssetManagerName(String callInAssetManagerName) {
        this.callInAssetManagerName = callInAssetManagerName;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getAssetAllocationTempStr() {
        return assetAllocationTempStr;
    }

    public void setAssetAllocationTempStr(String assetAllocationTempStr) {
        this.assetAllocationTempStr = assetAllocationTempStr;
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

    public String getCallInSavePlaceStr() {
        return callInSavePlaceStr;
    }

    public void setCallInSavePlaceStr(String callInSavePlaceStr) {
        this.callInSavePlaceStr = callInSavePlaceStr;
    }

}

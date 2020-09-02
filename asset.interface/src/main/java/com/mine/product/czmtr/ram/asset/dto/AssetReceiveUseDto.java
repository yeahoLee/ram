package com.mine.product.czmtr.ram.asset.dto;

import com.mine.product.czmtr.ram.asset.service.IAssetReceiveUseService.ASSETRECEIVEUSE_REVERTSTATUS;
import com.mine.product.czmtr.ram.asset.service.IAssetService;

import java.io.Serializable;

public class AssetReceiveUseDto implements Serializable {

    private String id;
    //基本信息Start
    private String assetReceiveUseCode; //领用编码
    private String assetReceiveUseDepartmentID; //领用部门ID
    private String assetReceiveUseDepartmentName; //领用部门名称

    private String assetReceiveUseUserID; //领用人ID
    private String assetReceiveUseUserName; //领用人名称
    private String receiveTime; //拟归还日期
    private String receiveTimeStr; //拟归还日期
    private String reason; //领用事由
    private String createTimestamp; //创建时间
    private String createUserID; //创建人
    private String createUserName;//创建人名称

    private String lastUpdateTimestamp; //最后更新时间

    private String type;//类型:0借用单;1归还单

    private FlowableInfo.WORKSTATUS receiptStatus;//审批状态
    private int receiptIndex;//审批状态
    private ASSETRECEIVEUSE_REVERTSTATUS revertStatus;//是否归还
    private int revertStatusStr;//是否归还
    private String assetReceiveUseTempStr;//领用资产详情(例:投影仪和笔记本)

    private String receiveTimeStart; //拟归还日期 开始(查询条件)

    private String receiveTimeEnd; //拟归还日期 结束(查询条件)
    private String assetCode;//资产编码(查询条件)

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

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getReceiveTimeStr() {
        return receiveTimeStr;
    }

    public void setReceiveTimeStr(String receiveTimeStr) {
        this.receiveTimeStr = receiveTimeStr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssetReceiveUseCode() {
        return assetReceiveUseCode;
    }

    public void setAssetReceiveUseCode(String assetReceiveUseCode) {
        this.assetReceiveUseCode = assetReceiveUseCode;
    }

    public String getAssetReceiveUseDepartmentID() {
        return assetReceiveUseDepartmentID;
    }

    public void setAssetReceiveUseDepartmentID(String assetReceiveUseDepartmentID) {
        this.assetReceiveUseDepartmentID = assetReceiveUseDepartmentID;
    }

    public String getAssetReceiveUseDepartmentName() {
        return assetReceiveUseDepartmentName;
    }

    public void setAssetReceiveUseDepartmentName(String assetReceiveUseDepartmentName) {
        this.assetReceiveUseDepartmentName = assetReceiveUseDepartmentName;
    }

    public String getAssetReceiveUseUserID() {
        return assetReceiveUseUserID;
    }

    public void setAssetReceiveUseUserID(String assetReceiveUseUserID) {
        this.assetReceiveUseUserID = assetReceiveUseUserID;
    }

    public String getAssetReceiveUseUserName() {
        return assetReceiveUseUserName;
    }

    public void setAssetReceiveUseUserName(String assetReceiveUseUserName) {
        this.assetReceiveUseUserName = assetReceiveUseUserName;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(String createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public String getCreateUserID() {
        return createUserID;
    }

    public void setCreateUserID(String createUserID) {
        this.createUserID = createUserID;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(String lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FlowableInfo.WORKSTATUS getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(FlowableInfo.WORKSTATUS receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    public int getReceiptIndex() {
        return receiptIndex;
    }

    public void setReceiptIndex(int receiptIndex) {
        this.receiptIndex = receiptIndex;
    }

    public String getAssetReceiveUseTempStr() {
        return assetReceiveUseTempStr;
    }

    public void setAssetReceiveUseTempStr(String assetReceiveUseTempStr) {
        this.assetReceiveUseTempStr = assetReceiveUseTempStr;
    }

    public String getReceiveTimeStart() {
        return receiveTimeStart;
    }

    public void setReceiveTimeStart(String receiveTimeStart) {
        this.receiveTimeStart = receiveTimeStart;
    }

    public String getReceiveTimeEnd() {
        return receiveTimeEnd;
    }

    public void setReceiveTimeEnd(String receiveTimeEnd) {
        this.receiveTimeEnd = receiveTimeEnd;
    }

    public ASSETRECEIVEUSE_REVERTSTATUS getRevertStatus() {
        return revertStatus;
    }

    public void setRevertStatus(ASSETRECEIVEUSE_REVERTSTATUS revertStatus) {
        this.revertStatus = revertStatus;
    }

    public int getRevertStatusStr() {
        return revertStatusStr;
    }

    public void setRevertStatusStr(int revertStatusStr) {
        this.revertStatusStr = revertStatusStr;
    }

}

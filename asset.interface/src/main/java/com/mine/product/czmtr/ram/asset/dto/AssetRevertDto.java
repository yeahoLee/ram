package com.mine.product.czmtr.ram.asset.dto;

import com.mine.product.czmtr.ram.asset.service.IAssetService;

import java.io.Serializable;

public class AssetRevertDto implements Serializable {
    //归还单内容
    private String id;
    private String assetrevertCode;//归还编码
    private String assetrevertDepartmentID; //归还部门ID
    private String assetrevertUserID; //归还人ID
    private String assetrevertUserID2; //归还人ID
    private String assetrevertDepartmentName; //归还部门名称

    public String getAssetrevertUserID2() {
        return assetrevertUserID2;
    }

    public void setAssetrevertUserID2(String assetrevertUserID2) {
        this.assetrevertUserID2 = assetrevertUserID2;
    }

    private String assetrevertUserName; //归还人名称
    private String realrevertTime; //真实归还日期
    private String revertTime; //拟归还日期
    private String realrevertTimeStr; //真实归还日期
    private String remarks;//归还单备注

    private String createTimestamp; //创建时间
    private String createUserID; //创建人
    private String createUserName;//创建人名称

    private String lastUpdateTimestamp; //最后更新时间

    private String type;//类型:0借用单;1归还单

    private FlowableInfo.WORKSTATUS receiptStatus;//审批状态
    private int receiptIndex;//审批状态

    private String assetRevertTempStr;//归还资产详情(例:投影仪和笔记本)

    private String realrevertTimeStart; //拟归还日期 开始(查询条件)

    private String realrevertTimeEnd; //拟归还日期 结束(查询条件)
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

    public String getRealrevertTimeStr() {
        return realrevertTimeStr;
    }

    public void setRealrevertTimeStr(String realrevertTimeStr) {
        this.realrevertTimeStr = realrevertTimeStr;
    }

    public String getRevertTime() {
        return revertTime;
    }

    public void setRevertTime(String revertTime) {
        this.revertTime = revertTime;
    }

    public int getReceiptIndex() {
        return receiptIndex;
    }

    public void setReceiptIndex(int receiptIndex) {
        this.receiptIndex = receiptIndex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssetrevertCode() {
        return assetrevertCode;
    }

    public void setAssetrevertCode(String assetrevertCode) {
        this.assetrevertCode = assetrevertCode;
    }

    public String getAssetrevertDepartmentID() {
        return assetrevertDepartmentID;
    }

    public void setAssetrevertDepartmentID(String assetrevertDepartmentID) {
        this.assetrevertDepartmentID = assetrevertDepartmentID;
    }

    public String getAssetrevertUserID() {
        return assetrevertUserID;
    }

    public void setAssetrevertUserID(String assetrevertUserID) {
        this.assetrevertUserID = assetrevertUserID;
    }

    public String getRealrevertTime() {
        return realrevertTime;
    }

    public void setRealrevertTime(String realrevertTime) {
        this.realrevertTime = realrevertTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public String getAssetRevertTempStr() {
        return assetRevertTempStr;
    }

    public void setAssetRevertTempStr(String assetRevertTempStr) {
        this.assetRevertTempStr = assetRevertTempStr;
    }

    public String getRealrevertTimeStart() {
        return realrevertTimeStart;
    }

    public void setRealrevertTimeStart(String realrevertTimeStart) {
        this.realrevertTimeStart = realrevertTimeStart;
    }

    public String getRealrevertTimeEnd() {
        return realrevertTimeEnd;
    }

    public void setRealrevertTimeEnd(String realrevertTimeEnd) {
        this.realrevertTimeEnd = realrevertTimeEnd;
    }

    public String getAssetrevertDepartmentName() {
        return assetrevertDepartmentName;
    }

    public void setAssetrevertDepartmentName(String assetrevertDepartmentName) {
        this.assetrevertDepartmentName = assetrevertDepartmentName;
    }

    public String getAssetrevertUserName() {
        return assetrevertUserName;
    }

    public void setAssetrevertUserName(String assetrevertUserName) {
        this.assetrevertUserName = assetrevertUserName;
    }


}

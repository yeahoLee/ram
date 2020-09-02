package com.mine.product.czmtr.ram.asset.dto;

import com.mine.product.czmtr.ram.asset.service.IAssetBorrowService.ASSETBORROW_REVERTSTATUS;
import com.mine.product.czmtr.ram.asset.service.IAssetService;

import java.io.Serializable;
import java.util.List;


public class AssetBorrowDto implements Serializable {
    private String id;
    //基本信息Start
    private String assetborrowCode; //借用编码
    private String assetborrowDepartmentID; //借用部门ID
    private String assetborrowDepartmentName; //借用部门名称

    private String assetborrowUserID; //借用人ID
    private String assetborrowUserName; //借用人名称
    private String revertTime; //拟归还日期
    private String revertTimeStr;

    private String reason; //借用事由
    private String createTimestamp; //创建时间,即借用日期
    private String createUserID; //创建人
    private String createUserName;//创建人名称

    private String lastUpdateTimestamp; //最后更新时间

    private String type;//类型:0借用单;1归还单

    private FlowableInfo.WORKSTATUS receiptStatus;//审批状态
    private int receiptIndex;//审批状态
    private ASSETBORROW_REVERTSTATUS revertStatus;//是否归还
    private int revertStatusStr;//是否归还
    private String assetBorrowTempStr;//借用资产详情(例:投影仪和笔记本)

    private String createTimestampStart; //借用日期 开始(查询条件)

    private String createTimestampEnd; //借用日期 结束(查询条件)
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

    public String getRevertTimeStr() {
        return revertTimeStr;
    }

    public void setRevertTimeStr(String revertTimeStr) {
        this.revertTimeStr = revertTimeStr;
    }

    public int getReceiptIndex() {
        return receiptIndex;
    }

    public void setReceiptIndex(int receiptIndex) {
        this.receiptIndex = receiptIndex;
    }

    public FlowableInfo.WORKSTATUS getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(FlowableInfo.WORKSTATUS receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    private List<AssetBorrowTempDto> assetBorrowTempDto;

    public List<AssetBorrowTempDto> getAssetBorrowTempDto() {
        return assetBorrowTempDto;
    }

    public void setAssetBorrowTempDto(List<AssetBorrowTempDto> assetBorrowTempDto) {
        this.assetBorrowTempDto = assetBorrowTempDto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssetborrowCode() {
        return assetborrowCode;
    }

    public void setAssetborrowCode(String assetborrowCode) {
        this.assetborrowCode = assetborrowCode;
    }

    public String getAssetborrowDepartmentID() {
        return assetborrowDepartmentID;
    }

    public void setAssetborrowDepartmentID(String assetborrowDepartmentID) {
        this.assetborrowDepartmentID = assetborrowDepartmentID;
    }

    public String getAssetborrowUserID() {
        return assetborrowUserID;
    }

    public void setAssetborrowUserID(String assetborrowUserID) {
        this.assetborrowUserID = assetborrowUserID;
    }

    public String getRevertTime() {
        return revertTime;
    }

    public void setRevertTime(String revertTime) {
        this.revertTime = revertTime;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAssetborrowDepartmentName() {
        return assetborrowDepartmentName;
    }

    public void setAssetborrowDepartmentName(String assetborrowDepartmentName) {
        this.assetborrowDepartmentName = assetborrowDepartmentName;
    }

    public String getAssetborrowUserName() {
        return assetborrowUserName;
    }

    public void setAssetborrowUserName(String assetborrowUserName) {
        this.assetborrowUserName = assetborrowUserName;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getAssetBorrowTempStr() {
        return assetBorrowTempStr;
    }

    public void setAssetBorrowTempStr(String assetBorrowTempStr) {
        this.assetBorrowTempStr = assetBorrowTempStr;
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

    public ASSETBORROW_REVERTSTATUS getRevertStatus() {
        return revertStatus;
    }

    public void setRevertStatus(ASSETBORROW_REVERTSTATUS revertStatus) {
        this.revertStatus = revertStatus;
    }

    public int getRevertStatusStr() {
        return revertStatusStr;
    }

    public void setRevertStatusStr(int revertStatusStr) {
        this.revertStatusStr = revertStatusStr;
    }

}

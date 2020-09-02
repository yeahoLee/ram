package com.mine.product.czmtr.ram.asset.dto;

import com.mine.product.czmtr.ram.asset.service.IAssetService;

import java.io.Serializable;

public class MaterialReceiptDto implements Serializable {

    private static final long serialVersionUID = 67745670659239332L;

    private String id;
    private String runningNum; //流水号码(年月日+000)
    private String receiptName; //名称
    private String sourceType; //添加方式
    private String sourceTypeStr; //添加方式
    private String reason; //原因
    private String remark; //备注
    private String personId; //创建人
    private String workAddress; //办公地址
    private String workPhone; //联系方式
    private FlowableInfo.WORKSTATUS receiptStatusEnum;
    private String receiptStatus; //状态
    private String receiptStatusStr; //状态
    private String assetCount;

    private Integer approveStatus;

    private String createTimestamp;
    private String lastUpdateTimestamp;

    private String type;        //资产的来源 建设移交或者采购

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

    public String getRunningNum() {
        return runningNum;
    }

    public void setRunningNum(String runningNum) {
        this.runningNum = runningNum;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
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

    public String getReceiptName() {
        return receiptName;
    }

    public void setReceiptName(String receiptName) {
        this.receiptName = receiptName;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(String receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    public String getSourceTypeStr() {
        return sourceTypeStr;
    }

    public void setSourceTypeStr(String sourceTypeStr) {
        this.sourceTypeStr = sourceTypeStr;
    }

    public String getReceiptStatusStr() {
        return receiptStatusStr;
    }

    public void setReceiptStatusStr(String receiptStatusStr) {
        this.receiptStatusStr = receiptStatusStr;
    }

    public String getAssetCount() {
        return assetCount;
    }

    public void setAssetCount(String assetCount) {
        this.assetCount = assetCount;
    }

    public FlowableInfo.WORKSTATUS getReceiptStatusEnum() {
        return receiptStatusEnum;
    }

    public void setReceiptStatusEnum(FlowableInfo.WORKSTATUS receiptStatusEnum) {
        this.receiptStatusEnum = receiptStatusEnum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(Integer approveStatus) {
        this.approveStatus = approveStatus;
    }
}

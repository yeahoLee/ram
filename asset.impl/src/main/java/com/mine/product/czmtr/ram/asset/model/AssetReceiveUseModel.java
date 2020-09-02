package com.mine.product.czmtr.ram.asset.model;

import com.mine.product.czmtr.ram.asset.dto.FlowableInfo;
import com.mine.product.czmtr.ram.asset.service.IAssetReceiveUseService.ASSETRECEIVEUSE_REVERTSTATUS;
import com.mine.product.czmtr.ram.asset.service.IAssetService;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
public class AssetReceiveUseModel {
    @Id
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name = "gen", strategy = "uuid",
            parameters = {@Parameter(name = "separator", value = "_")})
    private String id;
    /*资产领用单内容*/
    @Column
    private String assetReceiveUseCode; //借用编码
    @Column
    private String assetReceiveUseDepartmentID; //领用部门ID
    @Column
    private String assetReceiveUseUserID; //领用人ID
    @Column
    private String reason; //领用事由
    @Column
    private String createUserID; //创建人
    @Column
    private Date receiveTime; //领用日期
    @Column
    private FlowableInfo.WORKSTATUS receiptStatus;//审批状态
    @Column
    private ASSETRECEIVEUSE_REVERTSTATUS revertStatus;//归还状态
    @Column
    private Date createTimestamp; //资产创建时间
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

    @PrePersist
    public void updateWhenCreate() {
        createTimestamp = Calendar.getInstance().getTime();
        lastUpdateTimestamp = Calendar.getInstance().getTime();
    }

    @PreUpdate
    public void updateWhenUpdate() {
        lastUpdateTimestamp = Calendar.getInstance().getTime();
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

    public String getAssetReceiveUseUserID() {
        return assetReceiveUseUserID;
    }

    public void setAssetReceiveUseUserID(String assetReceiveUseUserID) {
        this.assetReceiveUseUserID = assetReceiveUseUserID;
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

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
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

    public FlowableInfo.WORKSTATUS getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(FlowableInfo.WORKSTATUS receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

    public ASSETRECEIVEUSE_REVERTSTATUS getRevertStatus() {
        return revertStatus;
    }

    public void setRevertStatus(ASSETRECEIVEUSE_REVERTSTATUS revertStatus) {
        this.revertStatus = revertStatus;
    }

}

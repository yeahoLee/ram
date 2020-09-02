package com.mine.product.czmtr.ram.asset.model;

import com.mine.product.czmtr.ram.asset.dto.FlowableInfo;
import com.mine.product.czmtr.ram.asset.service.IAssetService;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/***
 * 资产调拨
 * @author yangjie
 *
 */
@Entity
public class AssetAllocationModel {

    @Id
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name = "gen", strategy = "uuid",
            parameters = {@Parameter(name = "separator", value = "_")})
    private String id;
    @Column
    private String assetAllocationCode;//调拨编码
    @Column
    private String callOutDepartmentId;//调出部门
    @Column
    private String callInDepartmentId;//调入部门
    @Column
    private String callInAssetManagerId;//调入部门资产管理员
    @Column
    private String callInSavePlaceId;//调入位置
    @Column
    private String reason; //调拨事由
    @Column
    private String createUserID;
    @Column
    private FlowableInfo.WORKSTATUS receiptStatus;//审批状态
    @Column
    private Date createTimestamp;
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

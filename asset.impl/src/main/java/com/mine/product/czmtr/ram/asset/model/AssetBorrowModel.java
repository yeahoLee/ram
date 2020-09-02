package com.mine.product.czmtr.ram.asset.model;

import com.mine.product.czmtr.ram.asset.dto.FlowableInfo;
import com.mine.product.czmtr.ram.asset.service.IAssetBorrowService.ASSETBORROW_REVERTSTATUS;
import com.mine.product.czmtr.ram.asset.service.IAssetService;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;


/**
 * 资产借用表
 *
 * @author rockh
 */
@Entity
public class AssetBorrowModel {
    @Id
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name = "gen", strategy = "uuid",
            parameters = {@Parameter(name = "separator", value = "_")})
    private String id;
    //借用单内容
    @Column
    private String assetborrowCode; //借用编码
    @Column
    private String assetborrowDepartmentID; //借用部门ID
    @Column
    private String assetborrowUserID; //借用人ID
    @Column
    private Date revertTime; //拟归还日期
    @Column
    private String reason; //借用事由
    @Column
    private String createUserID; //创建人
    @Column
    private FlowableInfo.WORKSTATUS receiptStatus;//审批状态
    @Column
    private ASSETBORROW_REVERTSTATUS revertStatus;//归还状态
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

    public FlowableInfo.WORKSTATUS getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(FlowableInfo.WORKSTATUS receiptStatus) {
        this.receiptStatus = receiptStatus;
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

    public Date getRevertTime() {
        return revertTime;
    }

    public void setRevertTime(Date revertTime) {
        this.revertTime = revertTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public String getCreateUserID() {
        return createUserID;
    }

    public void setCreateUserID(String createUserID) {
        this.createUserID = createUserID;
    }

    public ASSETBORROW_REVERTSTATUS getRevertStatus() {
        return revertStatus;
    }

    public void setRevertStatus(ASSETBORROW_REVERTSTATUS revertStatus) {
        this.revertStatus = revertStatus;
    }

}

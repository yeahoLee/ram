package com.mine.product.czmtr.ram.asset.model;

import com.mine.product.czmtr.ram.asset.dto.FlowableInfo;
import com.mine.product.czmtr.ram.asset.service.IAssetService;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/**
 * 资产归还表
 *
 * @author rockh
 */
@Entity
public class AssetRevertModel {

    @Id
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name = "gen", strategy = "uuid",
            parameters = {@Parameter(name = "separator", value = "_")})
    private String id;

    //归还单内容
    @Column
    private String assetrevertCode;//归还编码
    @Column
    private String assetrevertDepartmentID; //归还部门ID
    @Column
    private String assetrevertUserID; //归还人ID
    @Column
    private Date realrevertTime; //真实归还日期
    @Column
    private String remarks;//归还单备注
    @Column
    private FlowableInfo.WORKSTATUS receiptStatus;//审批状态

    @Column
    private String createUserID; //创建人
    @Column
    private Date createTimestamp; //创建时间
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

    public String getCreateUserID() {
        return createUserID;
    }

    public void setCreateUserID(String createUserID) {
        this.createUserID = createUserID;
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

    public Date getRealrevertTime() {
        return realrevertTime;
    }

    public void setRealrevertTime(Date realrevertTime) {
        this.realrevertTime = realrevertTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public FlowableInfo.WORKSTATUS getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(FlowableInfo.WORKSTATUS receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

}

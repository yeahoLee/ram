package com.mine.product.czmtr.ram.asset.model;

import com.mine.product.czmtr.ram.asset.dto.FlowableInfo;
import com.mine.product.czmtr.ram.asset.service.IAssetService;
import com.mine.product.czmtr.ram.asset.service.IAssetService.SOURCE_TYPE;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/**
 * 物质新增单表
 *
 * @author
 */
@Entity
public class MaterialReceiptModel {
    @Id
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name = "gen", strategy = "uuid",
            parameters = {@Parameter(name = "separator", value = "_")})
    private String id;
    //基本信息Start
    @Column
    private String runningNum; //流水号码(年月日+000)
    @Column
    private SOURCE_TYPE sourceType; //来源方式
    @Column
    private String receiptName; //名称
    @Lob
    @Column
    private String reason; //原因
    @Lob
    @Column
    private String remark; //备注
    @Column
    private String personId; //创建人
    @Column
    private String workAddress; //办公地址
    @Column
    private String workPhone; //联系方式
    @Column
    private FlowableInfo.WORKSTATUS receiptStatus; //状态

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

    public String getReceiptName() {
        return receiptName;
    }

    public void setReceiptName(String receiptName) {
        this.receiptName = receiptName;
    }

    public SOURCE_TYPE getSourceType() {
        return sourceType;
    }

    public void setSourceType(SOURCE_TYPE sourceType) {
        this.sourceType = sourceType;
    }

    public FlowableInfo.WORKSTATUS getReceiptStatus() {
        return receiptStatus;
    }

    public void setReceiptStatus(FlowableInfo.WORKSTATUS receiptStatus) {
        this.receiptStatus = receiptStatus;
    }

}

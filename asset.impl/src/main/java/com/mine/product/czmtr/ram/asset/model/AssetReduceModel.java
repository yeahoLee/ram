package com.mine.product.czmtr.ram.asset.model;

import com.mine.product.czmtr.ram.asset.dto.FlowableInfo;
import com.mine.product.czmtr.ram.asset.service.IAssetService;
import com.mine.product.czmtr.ram.asset.service.IAssetService.REDUCE_TYPE;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/**
 * 资产减损台账表
 *
 * @author sun.guoli
 */
@Entity
public class AssetReduceModel {
    @Id
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name = "gen", strategy = "uuid",
            parameters = {@Parameter(name = "separator", value = "_")})
    private String id;
    @Column
    private String changeNum;//减损单号
    @Column
    private String orderName;//减损单名称
    @Column
    private REDUCE_TYPE reduceType;//减损类型
    @Column
    private double surplusValue;//剩余价值，系统自动计算
    @Column
    private double processingCost;//处理费用
    @Column
    private double actualLoss;//实际损失
    @Column
    private String reason;//减损原因
    @Column
    private String proposedDisposal;//拟处置办法
    @Column
    private FlowableInfo.WORKSTATUS receiptStatus;//审批状态
    @Column
    private Date createTimestamp; //创建时间
    @Column
    private Date lastUpdateTimestamp;//更新时间
    @Column
    private String createUserId;//创建人
    //生产型物资 和非生产型物资；
    @Column
    private IAssetService.ASSET_PRODUCE_TYPE produceType;


    public IAssetService.ASSET_PRODUCE_TYPE getProduceType() {
        return produceType;
    }

    public void setProduceType(IAssetService.ASSET_PRODUCE_TYPE produceType) {
        this.produceType = produceType;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getChangeNum() {
        return changeNum;
    }

    public void setChangeNum(String changeNum) {
        this.changeNum = changeNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    @PrePersist
    public void updateWhenCreate() {
        createTimestamp = Calendar.getInstance().getTime();
        lastUpdateTimestamp = Calendar.getInstance().getTime();
    }

    @PreUpdate
    public void updateWhenUpdate() {
        lastUpdateTimestamp = Calendar.getInstance().getTime();
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public REDUCE_TYPE getReduceType() {
        return reduceType;
    }

    public void setReduceType(REDUCE_TYPE reduceType) {
        this.reduceType = reduceType;
    }

    public double getSurplusValue() {
        return surplusValue;
    }

    public void setSurplusValue(double surplusValue) {
        this.surplusValue = surplusValue;
    }

    public double getProcessingCost() {
        return processingCost;
    }

    public void setProcessingCost(double processingCost) {
        this.processingCost = processingCost;
    }

    public double getActualLoss() {
        return actualLoss;
    }

    public void setActualLoss(double actualLoss) {
        this.actualLoss = actualLoss;
    }

    public String getProposedDisposal() {
        return proposedDisposal;
    }

    public void setProposedDisposal(String proposedDisposal) {
        this.proposedDisposal = proposedDisposal;
    }
}

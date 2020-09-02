package com.mine.product.czmtr.ram.asset.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.mine.product.czmtr.ram.asset.service.IAssetService.CHANGE_TYPE;
import com.mine.product.czmtr.ram.asset.service.IAssetService.HISTORY_TYPE;
import com.mine.product.czmtr.ram.asset.service.IAssetService.SEALED_UNSEALED;
import com.mine.product.czmtr.ram.asset.service.IAssetService.SOURCE_TYPE;
import com.mine.product.czmtr.ram.asset.service.IAssetService.REDUCE_TYPE;

/**
 * 历史记录
 *
 * @author rockh
 */
@Entity
public class AssetHistoryModel {
    @Id
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name = "gen", strategy = "uuid",
            parameters = {@Parameter(name = "separator", value = "_")})
    private String id;
    @Column
    private String createUserId; //经办人
    @Column
    private String assetModelId; //资产modelId
    @Column
    private HISTORY_TYPE historyType; //记录类型

    //基础信息变更 Start
    @Column(length = 4000)
    private String modifyContent; //修改内容
    //基础信息变更 End

    //变更记录 Start
    @Column
    private CHANGE_TYPE changeType; //变更记录
    @Column
    private String changeContent; //变更内容
    //变更记录 End

    //封存/启封记录 Start
    @Column
    private SEALED_UNSEALED sealedUnsealed; //操作方式
    //封存/启封记录 End

    //调拨记录 Start
    @Column
    private String checkOutCom; //调出公司
    @Column
    private String checkInCom; //调入公司
    @Column
    private String checkOutDept; //调出部门
    @Column
    private String checkInDept; //调入部门
    //调拨记录 End

    //盘点记录 Start
    @Column
    private String takeStockResult; //盘点记录
    //盘点记录 End

    //新增记录 Start
    @Column
    private SOURCE_TYPE sourctType; //来源方式
    @Column
    private String assetSource; //资产来源
    //新增记录 End

    //减少资产 Start
    @Column
    private REDUCE_TYPE reduceType; //减少类型
    //减少资产 End

    @Column
    private Date createTimestamp; //创建时间
    @Column
    private Date lastUpdateTimestamp;

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

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getAssetModelId() {
        return assetModelId;
    }

    public void setAssetModelId(String assetModelId) {
        this.assetModelId = assetModelId;
    }

    public HISTORY_TYPE getHistoryType() {
        return historyType;
    }

    public void setHistoryType(HISTORY_TYPE historyType) {
        this.historyType = historyType;
    }

    public String getModifyContent() {
        return modifyContent;
    }

    public void setModifyContent(String modifyContent) {
        this.modifyContent = modifyContent;
    }

    public CHANGE_TYPE getChangeType() {
        return changeType;
    }

    public void setChangeType(CHANGE_TYPE changeType) {
        this.changeType = changeType;
    }

    public String getChangeContent() {
        return changeContent;
    }

    public void setChangeContent(String changeContent) {
        this.changeContent = changeContent;
    }

    public SEALED_UNSEALED getSealedUnsealed() {
        return sealedUnsealed;
    }

    public void setSealedUnsealed(SEALED_UNSEALED sealedUnsealed) {
        this.sealedUnsealed = sealedUnsealed;
    }

    public String getCheckOutCom() {
        return checkOutCom;
    }

    public void setCheckOutCom(String checkOutCom) {
        this.checkOutCom = checkOutCom;
    }

    public String getCheckInCom() {
        return checkInCom;
    }

    public void setCheckInCom(String checkInCom) {
        this.checkInCom = checkInCom;
    }

    public String getCheckOutDept() {
        return checkOutDept;
    }

    public void setCheckOutDept(String checkOutDept) {
        this.checkOutDept = checkOutDept;
    }

    public String getCheckInDept() {
        return checkInDept;
    }

    public void setCheckInDept(String checkInDept) {
        this.checkInDept = checkInDept;
    }

    public String getTakeStockResult() {
        return takeStockResult;
    }

    public void setTakeStockResult(String takeStockResult) {
        this.takeStockResult = takeStockResult;
    }

    public SOURCE_TYPE getSourctType() {
        return sourctType;
    }

    public void setSourctType(SOURCE_TYPE sourctType) {
        this.sourctType = sourctType;
    }

    public String getAssetSource() {
        return assetSource;
    }

    public void setAssetSource(String assetSource) {
        this.assetSource = assetSource;
    }

    public REDUCE_TYPE getReduceType() {
        return reduceType;
    }

    public void setReduceType(REDUCE_TYPE reduceType) {
        this.reduceType = reduceType;
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

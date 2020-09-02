package com.mine.product.czmtr.ram.asset.dto;

import java.io.Serializable;

import com.mine.product.czmtr.ram.asset.service.IAssetService.HISTORY_TYPE;

public class AssetHistoryDto implements Serializable {
    private String id;
    private String createUserId; //经办人
    private String createUserStr;
    private String assetModelId; //资产modelId
    private HISTORY_TYPE historyType; //记录类型

    //基础信息变更 Start
    private String modifyContent; //修改内容
    //基础信息变更 End

    //变更记录 Start
    private String changeType; //变更记录
    private String changeContent; //变更内容
    //变更记录 End

    //封存/启封记录 Start
    private String sealedUnsealed; //操作方式
    //封存/启封记录 End

    //调拨记录 Start
    private String checkOutCom; //调出公司
    private String checkInCom; //调入公司
    private String checkOutDept; //调出部门
    private String checkInDept; //调入部门
    private String checkOutComStr; //调出公司
    private String checkInComStr; //调入公司
    private String checkOutDeptStr; //调出部门
    private String checkInDeptStr; //调入部门
    private String relatedAccounts;//相关台账.即调拨单号
    //调拨记录 End

    //盘点记录 Start
    private String takeStockResult; //盘点记录
    //盘点记录 End

    //新增记录 Start
    private String sourctType; //来源方式
    private String assetSource; //资产来源
    //新增记录 End

    //减少资产 Start
    private String reduceType; //减少类型
    //减少资产 End

    private String createTimestamp; //创建时间
    private String lastUpdateTimestamp;

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

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getChangeContent() {
        return changeContent;
    }

    public void setChangeContent(String changeContent) {
        this.changeContent = changeContent;
    }

    public String getSealedUnsealed() {
        return sealedUnsealed;
    }

    public void setSealedUnsealed(String sealedUnsealed) {
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

    public String getSourctType() {
        return sourctType;
    }

    public void setSourctType(String sourctType) {
        this.sourctType = sourctType;
    }

    public String getAssetSource() {
        return assetSource;
    }

    public void setAssetSource(String assetSource) {
        this.assetSource = assetSource;
    }

    public String getReduceType() {
        return reduceType;
    }

    public void setReduceType(String reduceType) {
        this.reduceType = reduceType;
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

    public String getCreateUserStr() {
        return createUserStr;
    }

    public void setCreateUserStr(String createUserStr) {
        this.createUserStr = createUserStr;
    }

    public String getCheckOutComStr() {
        return checkOutComStr;
    }

    public void setCheckOutComStr(String checkOutComStr) {
        this.checkOutComStr = checkOutComStr;
    }

    public String getCheckInComStr() {
        return checkInComStr;
    }

    public void setCheckInComStr(String checkInComStr) {
        this.checkInComStr = checkInComStr;
    }

    public String getCheckOutDeptStr() {
        return checkOutDeptStr;
    }

    public void setCheckOutDeptStr(String checkOutDeptStr) {
        this.checkOutDeptStr = checkOutDeptStr;
    }

    public String getCheckInDeptStr() {
        return checkInDeptStr;
    }

    public void setCheckInDeptStr(String checkInDeptStr) {
        this.checkInDeptStr = checkInDeptStr;
    }

    public String getRelatedAccounts() {
        return relatedAccounts;
    }

    public void setRelatedAccounts(String relatedAccounts) {
        this.relatedAccounts = relatedAccounts;
    }
}

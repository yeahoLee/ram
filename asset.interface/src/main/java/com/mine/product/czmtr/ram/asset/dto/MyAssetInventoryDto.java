package com.mine.product.czmtr.ram.asset.dto;

import com.mine.product.czmtr.ram.asset.service.IAssetService;

import java.util.Date;

public class MyAssetInventoryDto {

    private String id;                            //系统自动生成id
    private String assetInventoryId;            //盘点单id
    private String myAssetInventoryCode;                //盘点单编号

    private double quantity;                        //盘点数量

    private double inventoryLoss;                        //盘亏

    private double inventoryProfit;                        //盘盈

    private String managerId;    //资产管理员

    private String managerStr;

    private String inventoryProcess;                    //盘点进度

    private String reason;                                //盘亏,盘盈原因

    private String disposalAdvice;                   //处置意见

    private Date createTimestamp; //创建时间

    private Date lastUpdateTimestamp;

    private String myinventoryStatus;   //我的盘点状态

    private String myinventoryStatusStr;   //我的盘点状态

    private String inventoryStatus;   //盘点任务状态

    private String inventoryStatusStr;   //盘点任务状态

    private String managerDeptId;
    private String managerDeptStr;//管理部门

    private String launchDate;//发起日期

    private String inventoryName;                    //盘点任务名称

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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getInventoryLoss() {
        return inventoryLoss;
    }

    public void setInventoryLoss(double inventoryLoss) {
        this.inventoryLoss = inventoryLoss;
    }

    public double getInventoryProfit() {
        return inventoryProfit;
    }

    public void setInventoryProfit(double inventoryProfit) {
        this.inventoryProfit = inventoryProfit;
    }

    public String getInventoryProcess() {
        return inventoryProcess;
    }

    public void setInventoryProcess(String inventoryProcess) {
        this.inventoryProcess = inventoryProcess;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDisposalAdvice() {
        return disposalAdvice;
    }

    public void setDisposalAdvice(String disposalAdvice) {
        this.disposalAdvice = disposalAdvice;
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

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getManagerStr() {
        return managerStr;
    }

    public void setManagerStr(String managerStr) {
        this.managerStr = managerStr;
    }

    public String getAssetInventoryId() {
        return assetInventoryId;
    }

    public void setAssetInventoryId(String assetInventoryId) {
        this.assetInventoryId = assetInventoryId;
    }

    public String getMyAssetInventoryCode() {
        return myAssetInventoryCode;
    }

    public void setMyAssetInventoryCode(String myAssetInventoryCode) {
        this.myAssetInventoryCode = myAssetInventoryCode;
    }

    public String getMyinventoryStatus() {
        return myinventoryStatus;
    }

    public void setMyinventoryStatus(String myinventoryStatus) {
        this.myinventoryStatus = myinventoryStatus;
    }

    public String getMyinventoryStatusStr() {
        return myinventoryStatusStr;
    }

    public void setMyinventoryStatusStr(String myinventoryStatusStr) {
        this.myinventoryStatusStr = myinventoryStatusStr;
    }

    public String getManagerDeptStr() {
        return managerDeptStr;
    }

    public void setManagerDeptStr(String managerDeptStr) {
        this.managerDeptStr = managerDeptStr;
    }

    public String getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(String launchDate) {
        this.launchDate = launchDate;
    }

    public String getManagerDeptId() {
        return managerDeptId;
    }

    public void setManagerDeptId(String managerDeptId) {
        this.managerDeptId = managerDeptId;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    public String getInventoryStatus() {
        return inventoryStatus;
    }

    public void setInventoryStatus(String inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
    }

    public String getInventoryStatusStr() {
        return inventoryStatusStr;
    }

    public void setInventoryStatusStr(String inventoryStatusStr) {
        this.inventoryStatusStr = inventoryStatusStr;
    }
}

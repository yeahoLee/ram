package com.mine.product.czmtr.ram.asset.dto;

import com.mine.product.czmtr.ram.asset.service.IAssetService;

import java.io.Serializable;
import java.util.Date;

public class AssetInventoryDto implements Serializable {

    private String id;                                    //系统自动生成id

    private String inventoryRunningNum;                //盘点单编号

    private String inventoryName;                    //盘点任务名称

    private double quantity;                            //盘点数量

    private double inventoryLoss;                        //盘亏

    private double inventoryProfit;                        //盘盈

    private Date launchDate;                            //发起日期

    private String lanuchDateStr;

    private FlowableInfo.WORKSTATUS inventoryStatus;            //盘点状态

    private Integer inventoryStatusStr;            //盘点状态

    private String inventoryProcess;                    //盘点进度

    private String reason;                                //盘点任务说明

    private String createId;                          //发起人

    private String createrName;                          //发起人
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

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getInventoryRunningNum() {
        return inventoryRunningNum;
    }

    public void setInventoryRunningNum(String inventoryRunningNum) {
        this.inventoryRunningNum = inventoryRunningNum;
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

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    public String getLanuchDateStr() {
        return lanuchDateStr;
    }

    public void setLanuchDateStr(String lanuchDateStr) {
        this.lanuchDateStr = lanuchDateStr;
    }

    public  FlowableInfo.WORKSTATUS getInventoryStatus() {
        return inventoryStatus;
    }

    public void setInventoryStatus( FlowableInfo.WORKSTATUS inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
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

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }

    public Integer getInventoryStatusStr() {
        return inventoryStatusStr;
    }

    public void setInventoryStatusStr(Integer inventoryStatusStr) {
        this.inventoryStatusStr = inventoryStatusStr;
    }

}

package com.mine.product.czmtr.ram.asset.model;

import com.mine.product.czmtr.ram.asset.dto.FlowableInfo;
import com.mine.product.czmtr.ram.asset.service.IAssetService;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

/**
 * 创建盘点任务单
 *
 * @author 17893
 */
@Entity
public class AssetInventoryModel {


    @Id                                            //主键
    @GeneratedValue(generator = "gen")            //主键生成策略，根据此标识来声明主键生成器
    @GenericGenerator(
            name = "gen",
            strategy = "uuid",
            parameters = {
                    @Parameter(name = "separator", value = "_")
            })
    private String id;                                    //系统自动生成id


    @Column
    private String inventoryRunningNum;                    //盘点流水号

    @Column
    private double quantity;                            //盘点数量

    @Column
    private double inventoryLoss;                        //盘亏

    @Column
    private double inventoryProfit;                        //盘盈

    @Column
    private Date launchDate;                            //发起日期

    @Column
    private FlowableInfo.WORKSTATUS inventoryStatus;            //盘点状态

    @Column
    private String inventoryProcess;                    //盘点进度

    @Column
    private String inventoryName;                        //盘点任务名称

    @Lob
    @Column
    private String reason;                                //盘点任务说明
    @Column
    private String createId; // 创建人
    //生产型物资 和非生产型物资；
    @Column
    private IAssetService.ASSET_PRODUCE_TYPE produceType;


    public IAssetService.ASSET_PRODUCE_TYPE getProduceType() {
        return produceType;
    }

    public void setProduceType(IAssetService.ASSET_PRODUCE_TYPE produceType) {
        this.produceType = produceType;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    /**
     * 创建时间
     */
    @Column
    private Date createTimestamp;

    /**
     * 更新时间
     */
    @Column
    private Date lastUpdateTimestamp;

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

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
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

    public String getInventoryRunningNum() {
        return inventoryRunningNum;
    }

    public void setInventoryRunningNum(String inventoryRunningNum) {
        this.inventoryRunningNum = inventoryRunningNum;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
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

}

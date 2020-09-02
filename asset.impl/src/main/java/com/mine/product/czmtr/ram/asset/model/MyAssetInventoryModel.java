package com.mine.product.czmtr.ram.asset.model;

import com.mine.product.czmtr.ram.asset.dto.FlowableInfo;
import com.mine.product.czmtr.ram.asset.service.IAssetService;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/***
 * 我的盘点清单
 *
 * @author yangjie
 *
 */
@Entity
public class MyAssetInventoryModel {

    @Id // 主键
    @GeneratedValue(generator = "gen") // 主键生成策略，根据此标识来声明主键生成器
    @GenericGenerator(name = "gen", strategy = "uuid", parameters = {@Parameter(name = "separator", value = "_")})
    private String id; // 系统自动生成id

    @ManyToOne
    private AssetInventoryModel assetInventoryModel;

    @Column
    private String assetInventoryCode; // 盘点单编号

    @Column
    private double quantity; // 盘点数量

    @Column
    private double inventoryLoss; // 盘亏

    @Column
    private double inventoryProfit; // 盘盈

    @Column
    private String managerId; // 资产管理员

    @Column
    private String managerDeptId; // 资产管理部门

    @Column
    private String reason; // 盘亏,盘盈原因

    @Column
    private String disposalAdvice; // 处置意见

    @Column
    private Date createTimestamp; // 创建时间
    @Column
    private Date lastUpdateTimestamp;

    @Column
    private  FlowableInfo.WORKSTATUS inventoryStatus; // 盘点状态；

    public IAssetService.ASSET_PRODUCE_TYPE getProduceType() {
        return produceType;
    }

    public void setProduceType(IAssetService.ASSET_PRODUCE_TYPE produceType) {
        this.produceType = produceType;
    }

    //生产型物资 和非生产型物资；
    @Column
    private IAssetService.ASSET_PRODUCE_TYPE produceType;

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

    public String getAssetInventoryCode() {
        return assetInventoryCode;
    }

    public void setAssetInventoryCode(String assetInventoryCode) {
        this.assetInventoryCode = assetInventoryCode;
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

    public AssetInventoryModel getAssetInventoryModel() {
        return assetInventoryModel;
    }

    public void setAssetInventoryModel(AssetInventoryModel assetInventoryModel) {
        this.assetInventoryModel = assetInventoryModel;
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

    public  FlowableInfo.WORKSTATUS getInventoryStatus() {
        return inventoryStatus;
    }

    public void setInventoryStatus( FlowableInfo.WORKSTATUS inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
    }

    public String getManagerDeptId() {
        return managerDeptId;
    }

    public void setManagerDeptId(String managerDeptId) {
        this.managerDeptId = managerDeptId;
    }

}

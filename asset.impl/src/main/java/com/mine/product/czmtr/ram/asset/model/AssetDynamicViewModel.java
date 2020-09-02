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

import com.mine.product.czmtr.ram.asset.service.IAssetService.ASSET_STATUS;
import com.mine.product.czmtr.ram.asset.service.IAssetService.ASSET_TYPE;

/**
 * 创建资产视图的Model
 *
 * @author Administrator
 */
@Entity
public class AssetDynamicViewModel {
    @Id                                            //主键
    @GeneratedValue(generator = "gen")            //主键生成策略，根据此标识来声明主键生成器
    @GenericGenerator(
            name = "gen",
            strategy = "uuid",
            parameters = {
                    @Parameter(name = "separator", value = "_")
            })
    private String id;                            //系统自动生成id
    @Column
    private String assetViewName;                //视图名
    @Column
    private ASSET_STATUS assetStatus; //资产状态
    @Column
    private String assetType; //资产类型
    @Column
    private String savePlaceId; //具体位置（ex存放地点）
    @Column
    private String manageDeptId; //主管部门
    @Column
    private String useDeptId; //使用部门
    @Column
    private String managerId; //资产管理员
    @Column
    private String userId; //使用人

    @Column
    private String creator; //创建人
    @Column
    private int assetViewState;                //视图状态
    @Column
    private Date createTimestamp; //创建时间
    @Column
    private Date lastUpdateTimestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssetViewName() {
        return assetViewName;
    }

    public void setAssetViewName(String assetViewName) {
        this.assetViewName = assetViewName;
    }

    public ASSET_STATUS getAssetStatus() {
        return assetStatus;
    }

    public void setAssetStatus(ASSET_STATUS assetStatus) {
        this.assetStatus = assetStatus;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getSavePlaceId() {
        return savePlaceId;
    }

    public void setSavePlaceId(String savePlaceId) {
        this.savePlaceId = savePlaceId;
    }

    public String getManageDeptId() {
        return manageDeptId;
    }

    public void setManageDeptId(String manageDeptId) {
        this.manageDeptId = manageDeptId;
    }

    public String getUseDeptId() {
        return useDeptId;
    }

    public void setUseDeptId(String useDeptId) {
        this.useDeptId = useDeptId;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getAssetViewState() {
        return assetViewState;
    }

    public void setAssetViewState(int assetViewState) {
        this.assetViewState = assetViewState;
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

}

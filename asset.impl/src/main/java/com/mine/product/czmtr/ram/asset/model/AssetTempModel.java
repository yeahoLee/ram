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
import com.mine.product.czmtr.ram.asset.service.IAssetService.SOURCE_TYPE;

/**
 * 资产临时表
 *
 * @author
 */
@Entity
public class AssetTempModel {
    @Id
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name = "gen", strategy = "uuid",
            parameters = {@Parameter(name = "separator", value = "_")})
    private String id;
    //基本信息Start
    @Column
    private String assetCode; //资产编码
    @Column
    private String materialCode; //物资编码
    @Column
    private String assetLeave1; //资产类别（大类）
    @Column
    private String assetLeave2; //资产类别（小类）
    @Column
    private String assetLeave3; //资产类别（大组）
    @Column
    private String assetLeave4; //资产类别（小组）
    @Column
    private String assetChsName; //资产名称（ex中文名称）
    @Column
    private String assetEngName; //英文名称
    @Column
    private ASSET_TYPE assetType; //资产类型
    @Column(length = 1024)
    private String specAndModels; //规格型号
    @Column
    private String seriesNum; //序列号
    @Column
    private String unitOfMeasId; //计量单位id unit of measurement
    @Column
    private String assetBrand; //资产品牌
    @Column(length = 1024)
    private String techPara; //技术参数 technical parameters
    @Column
    private double quantity; //数量
    @Column
    private double purcPrice; //采购价 purchase price
    @Column
    private double equiOrigValue; //资产原值（ex设备原值 equipment original value）
    @Column
    private double pracServLifeYears; //已使用年限（年数） practical serviced life
    @Column
    private double alreadyDeprMoney; //已提折旧金额 already depreciation Money
    @Column
    private double monthDeprMoney; //月折旧金额
    @Column
    private double residualValue; //残余价值
    @Column
    private double netWorth; //账面净值
    @Column(length = 1024)
    private String remark; //备注
    //基本信息End

    //延伸信息Start
    @Column
    private Date buyDate; //购置日期
    @Column
    private String contractNum; //合同编号
    @Column
    private String tendersNum; //标段编号
    @Column
    private String companyId; //所属公司id
    @Column
    private String manageDeptId; //主管部门id
    @Column
    private String managerId; //资产管理员id
    @Column
    private String useDeptId; //使用部门id
    @Column
    private String userId; //使用人id
    @Column
    private String belongLine; //所属建筑
    //位置编码 = 资产线路 + 具体位置 + 设备房间号（建筑物号+楼层号+房间号）
    @Column
    private String assetLineId; //线路id
    @Column
    private String savePlaceId; //具体位置id（ex存放地点）
    @Column
    private String buildNumId; //建筑物号id
    @Column
    private String floorNumId; //楼层号id
    @Column
    private String roomNumId; //房间号id
    //位置编码 End
    @Column
    private String assetSource; //资产来源
    @Column
    private SOURCE_TYPE sourceType; //来源方式
    @Column
    private String sourceUser; //联系人
    @Column
    private String sourceContactInfo; //联系方式
    @Column
    private Date prodTime; //出厂时间 production Time
    @Column
    private Date mainPeriod; //维保期 maintenance period
    //保管使用信息End

    @Column
    private ASSET_STATUS assetStatus; //资产状态
    @Column
    private String superClassId; //上次资产Id（ex父资产Id）
    @Column
    private boolean portfolio; //资产组合 portfolio

    @Column
    private Date createTimestamp; //资产创建时间
    @Column
    private Date lastUpdateTimestamp;

    @Column
    private String recId; //资产新增单Id

    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
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

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getAssetLeave1() {
        return assetLeave1;
    }

    public void setAssetLeave1(String assetLeave1) {
        this.assetLeave1 = assetLeave1;
    }

    public String getAssetLeave2() {
        return assetLeave2;
    }

    public void setAssetLeave2(String assetLeave2) {
        this.assetLeave2 = assetLeave2;
    }

    public String getAssetLeave3() {
        return assetLeave3;
    }

    public void setAssetLeave3(String assetLeave3) {
        this.assetLeave3 = assetLeave3;
    }

    public String getAssetLeave4() {
        return assetLeave4;
    }

    public void setAssetLeave4(String assetLeave4) {
        this.assetLeave4 = assetLeave4;
    }

    public String getAssetChsName() {
        return assetChsName;
    }

    public void setAssetChsName(String assetChsName) {
        this.assetChsName = assetChsName;
    }

    public String getAssetEngName() {
        return assetEngName;
    }

    public void setAssetEngName(String assetEngName) {
        this.assetEngName = assetEngName;
    }

    public ASSET_TYPE getAssetType() {
        return assetType;
    }

    public void setAssetType(ASSET_TYPE assetType) {
        this.assetType = assetType;
    }

    public String getSpecAndModels() {
        return specAndModels;
    }

    public void setSpecAndModels(String specAndModels) {
        this.specAndModels = specAndModels;
    }

    public String getSeriesNum() {
        return seriesNum;
    }

    public void setSeriesNum(String seriesNum) {
        this.seriesNum = seriesNum;
    }

    public String getUnitOfMeasId() {
        return unitOfMeasId;
    }

    public void setUnitOfMeasId(String unitOfMeasId) {
        this.unitOfMeasId = unitOfMeasId;
    }

    public String getAssetBrand() {
        return assetBrand;
    }

    public void setAssetBrand(String assetBrand) {
        this.assetBrand = assetBrand;
    }

    public String getTechPara() {
        return techPara;
    }

    public void setTechPara(String techPara) {
        this.techPara = techPara;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getEquiOrigValue() {
        return equiOrigValue;
    }

    public void setEquiOrigValue(double equiOrigValue) {
        this.equiOrigValue = equiOrigValue;
    }

    public double getPracServLifeYears() {
        return pracServLifeYears;
    }

    public void setPracServLifeYears(double pracServLifeYears) {
        this.pracServLifeYears = pracServLifeYears;
    }

    public double getAlreadyDeprMoney() {
        return alreadyDeprMoney;
    }

    public void setAlreadyDeprMoney(double alreadyDeprMoney) {
        this.alreadyDeprMoney = alreadyDeprMoney;
    }

    public double getMonthDeprMoney() {
        return monthDeprMoney;
    }

    public void setMonthDeprMoney(double monthDeprMoney) {
        this.monthDeprMoney = monthDeprMoney;
    }

    public double getResidualValue() {
        return residualValue;
    }

    public void setResidualValue(double residualValue) {
        this.residualValue = residualValue;
    }

    public double getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(double netWorth) {
        this.netWorth = netWorth;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public String getTendersNum() {
        return tendersNum;
    }

    public void setTendersNum(String tendersNum) {
        this.tendersNum = tendersNum;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getManageDeptId() {
        return manageDeptId;
    }

    public void setManageDeptId(String manageDeptId) {
        this.manageDeptId = manageDeptId;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getUseDeptId() {
        return useDeptId;
    }

    public void setUseDeptId(String useDeptId) {
        this.useDeptId = useDeptId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAssetLineId() {
        return assetLineId;
    }

    public void setAssetLineId(String assetLineId) {
        this.assetLineId = assetLineId;
    }

    public String getSavePlaceId() {
        return savePlaceId;
    }

    public void setSavePlaceId(String savePlaceId) {
        this.savePlaceId = savePlaceId;
    }

    public String getBuildNumId() {
        return buildNumId;
    }

    public void setBuildNumId(String buildNumId) {
        this.buildNumId = buildNumId;
    }

    public String getFloorNumId() {
        return floorNumId;
    }

    public void setFloorNumId(String floorNumId) {
        this.floorNumId = floorNumId;
    }

    public String getRoomNumId() {
        return roomNumId;
    }

    public void setRoomNumId(String roomNumId) {
        this.roomNumId = roomNumId;
    }

    public String getAssetSource() {
        return assetSource;
    }

    public void setAssetSource(String assetSource) {
        this.assetSource = assetSource;
    }

    public SOURCE_TYPE getSourceType() {
        return sourceType;
    }

    public void setSourceType(SOURCE_TYPE sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceUser() {
        return sourceUser;
    }

    public void setSourceUser(String sourceUser) {
        this.sourceUser = sourceUser;
    }

    public String getSourceContactInfo() {
        return sourceContactInfo;
    }

    public void setSourceContactInfo(String sourceContactInfo) {
        this.sourceContactInfo = sourceContactInfo;
    }

    public Date getProductionTime() {
        return prodTime;
    }

    public void setProductionTime(Date productionTime) {
        this.prodTime = productionTime;
    }

    public Date getMainPeriod() {
        return mainPeriod;
    }

    public void setMainPeriod(Date mainPeriod) {
        this.mainPeriod = mainPeriod;
    }

    public ASSET_STATUS getAssetStatus() {
        return assetStatus;
    }

    public void setAssetStatus(ASSET_STATUS assetStatus) {
        this.assetStatus = assetStatus;
    }

    public String getSuperClassId() {
        return superClassId;
    }

    public void setSuperClassId(String superClassId) {
        this.superClassId = superClassId;
    }

    public boolean isPortfolio() {
        return portfolio;
    }

    public void setPortfolio(boolean portfolio) {
        this.portfolio = portfolio;
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

    public double getPurcPrice() {
        return purcPrice;
    }

    public void setPurcPrice(double purcPrice) {
        this.purcPrice = purcPrice;
    }

    public Date getProdTime() {
        return prodTime;
    }

    public void setProdTime(Date prodTime) {
        this.prodTime = prodTime;
    }

    public String getBelongLine() {
        return belongLine;
    }

    public void setBelongLine(String belongLine) {
        this.belongLine = belongLine;
    }
}

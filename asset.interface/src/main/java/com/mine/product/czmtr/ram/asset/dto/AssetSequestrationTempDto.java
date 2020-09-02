package com.mine.product.czmtr.ram.asset.dto;

import java.io.Serializable;
import java.util.Date;

public class AssetSequestrationTempDto implements Serializable {

    private String id;

    /**
     * 封存/启封id 主表id
     */
    private String assetSequestrationId;

    /**
     * 资产id
     */
    private String assetId;

    /**
     * 创建时间
     */
    private Date createTimestamp;

    /**
     * 更新时间
     */
    private Date lastUpdateTimestamp;

    private String assetCode; //资产编码
    private String materialCode; //物资编码
    private String assetType; //资产类型
    private String assetTypeStr; //资产类型
    private String specAndModels; //规格型号
    private String seriesNum; //序列号
    private String unitOfMeasId; //计量单位id unit of measurement
    private String unitOfMeasStr;
    private String assetBrand; //资产品牌
    private String techPara;
    //技术参数 technical parameters
    private String purcPrice; //采购价
    private String equiOrigValue; //资产原值（ex设备原值 equipment original value）

    //延伸信息Start
    private String buyDate; //购置日期
    private String contractNum; //合同编号
    private String tendersNum; //标段编号
    private String companyId; //所属公司id
    private String companyStr;
    private String manageDeptId; //主管部门id
    private String manageDeptStr;
    private String managerId; //资产管理员id
    private String managerStr;
    private String useDeptId; //使用部门id
    private String useDeptStr;
    private String userId; //使用人id
    private String useStr;
    private String belongLine; //所属建筑
    private String belongLineStr;
    //位置编码 = 资产线路 + 具体位置 + 设备房间号（建筑物号+楼层号+房间号）
    private String assetLineId; //资产线路id
    private String assetLineStr;
    private String savePlaceId; //具体位置id（ex存放地点）
    private String savePlaceCode; //code
    private String savePlaceName; //name
    private String savePlaceStr; //code+name
    private String buildNumId; //建筑物号id
    private String buildNumStr;
    private String floorNumId; //楼层号id
    private String floorNumStr;
    private String roomNumId; //房间号id
    private String roomNumStr;
    //位置编码 End
    private String assetSource; //资产来源
    private String sourceType; //来源方式
    private String sourceUser; //联系人
    private String sourceContactInfo; //联系方式
    private String prodTime; //出厂时间
    private String mainPeriod; //维保期 maintenance period
    //保管使用信息End

    private String assetStatus; //资产状态
    private String assetStatusStr;//资产状态int值
    private String beforeChangeAssetStatus; //变更前资产状态
    private String beforeChangeAssetStatusStr; //变更前资产状态
    private String superClassId; //上次资产Id（ex父资产Id）
    private boolean portfolio; //资产组合 portfolio

    private String recId;//新增单id

//		private String codeAndChsname;//位置编码 codeAndName

    private String combinationAssetType;//从物资编码（前8位）查询出的结果的组合
    private String combinationAssetName;//从物资编码（12位）查询出的结果的组合
    private String assetQuantity;//物资编码下的资产数量

    private String materialSearchCode;//接收多条件查询的 物资编码

    private Date deActiveTime; //停用时间

    private String deActiveTimeStr;
    //资产信息---end----
    private String produceStr;
    public String getProduceStr() {
        return produceStr;
    }

    public void setProduceStr(String produceStr) {
        this.produceStr = produceStr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssetSequestrationId() {
        return assetSequestrationId;
    }

    public void setAssetSequestrationId(String assetSequestrationId) {
        this.assetSequestrationId = assetSequestrationId;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
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

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getAssetTypeStr() {
        return assetTypeStr;
    }

    public void setAssetTypeStr(String assetTypeStr) {
        this.assetTypeStr = assetTypeStr;
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

    public String getUnitOfMeasStr() {
        return unitOfMeasStr;
    }

    public void setUnitOfMeasStr(String unitOfMeasStr) {
        this.unitOfMeasStr = unitOfMeasStr;
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

    public String getPurcPrice() {
        return purcPrice;
    }

    public void setPurcPrice(String purcPrice) {
        this.purcPrice = purcPrice;
    }

    public String getEquiOrigValue() {
        return equiOrigValue;
    }

    public void setEquiOrigValue(String equiOrigValue) {
        this.equiOrigValue = equiOrigValue;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
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

    public String getCompanyStr() {
        return companyStr;
    }

    public void setCompanyStr(String companyStr) {
        this.companyStr = companyStr;
    }

    public String getManageDeptId() {
        return manageDeptId;
    }

    public void setManageDeptId(String manageDeptId) {
        this.manageDeptId = manageDeptId;
    }

    public String getManageDeptStr() {
        return manageDeptStr;
    }

    public void setManageDeptStr(String manageDeptStr) {
        this.manageDeptStr = manageDeptStr;
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

    public String getUseDeptId() {
        return useDeptId;
    }

    public void setUseDeptId(String useDeptId) {
        this.useDeptId = useDeptId;
    }

    public String getUseDeptStr() {
        return useDeptStr;
    }

    public void setUseDeptStr(String useDeptStr) {
        this.useDeptStr = useDeptStr;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUseStr() {
        return useStr;
    }

    public void setUseStr(String useStr) {
        this.useStr = useStr;
    }

    public String getBelongLine() {
        return belongLine;
    }

    public void setBelongLine(String belongLine) {
        this.belongLine = belongLine;
    }

    public String getBelongLineStr() {
        return belongLineStr;
    }

    public void setBelongLineStr(String belongLineStr) {
        this.belongLineStr = belongLineStr;
    }

    public String getAssetLineId() {
        return assetLineId;
    }

    public void setAssetLineId(String assetLineId) {
        this.assetLineId = assetLineId;
    }

    public String getAssetLineStr() {
        return assetLineStr;
    }

    public void setAssetLineStr(String assetLineStr) {
        this.assetLineStr = assetLineStr;
    }

    public String getSavePlaceId() {
        return savePlaceId;
    }

    public void setSavePlaceId(String savePlaceId) {
        this.savePlaceId = savePlaceId;
    }

    public String getSavePlaceCode() {
        return savePlaceCode;
    }

    public void setSavePlaceCode(String savePlaceCode) {
        this.savePlaceCode = savePlaceCode;
    }

    public String getSavePlaceName() {
        return savePlaceName;
    }

    public void setSavePlaceName(String savePlaceName) {
        this.savePlaceName = savePlaceName;
    }

    public String getSavePlaceStr() {
        return savePlaceStr;
    }

    public void setSavePlaceStr(String savePlaceStr) {
        this.savePlaceStr = savePlaceStr;
    }

    public String getBuildNumId() {
        return buildNumId;
    }

    public void setBuildNumId(String buildNumId) {
        this.buildNumId = buildNumId;
    }

    public String getBuildNumStr() {
        return buildNumStr;
    }

    public void setBuildNumStr(String buildNumStr) {
        this.buildNumStr = buildNumStr;
    }

    public String getFloorNumId() {
        return floorNumId;
    }

    public void setFloorNumId(String floorNumId) {
        this.floorNumId = floorNumId;
    }

    public String getFloorNumStr() {
        return floorNumStr;
    }

    public void setFloorNumStr(String floorNumStr) {
        this.floorNumStr = floorNumStr;
    }

    public String getRoomNumId() {
        return roomNumId;
    }

    public void setRoomNumId(String roomNumId) {
        this.roomNumId = roomNumId;
    }

    public String getRoomNumStr() {
        return roomNumStr;
    }

    public void setRoomNumStr(String roomNumStr) {
        this.roomNumStr = roomNumStr;
    }

    public String getAssetSource() {
        return assetSource;
    }

    public void setAssetSource(String assetSource) {
        this.assetSource = assetSource;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
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

    public String getProdTime() {
        return prodTime;
    }

    public void setProdTime(String prodTime) {
        this.prodTime = prodTime;
    }

    public String getMainPeriod() {
        return mainPeriod;
    }

    public void setMainPeriod(String mainPeriod) {
        this.mainPeriod = mainPeriod;
    }

    public String getAssetStatus() {
        return assetStatus;
    }

    public void setAssetStatus(String assetStatus) {
        this.assetStatus = assetStatus;
    }

    public String getAssetStatusStr() {
        return assetStatusStr;
    }

    public void setAssetStatusStr(String assetStatusStr) {
        this.assetStatusStr = assetStatusStr;
    }

    public String getBeforeChangeAssetStatus() {
        return beforeChangeAssetStatus;
    }

    public void setBeforeChangeAssetStatus(String beforeChangeAssetStatus) {
        this.beforeChangeAssetStatus = beforeChangeAssetStatus;
    }

    public String getBeforeChangeAssetStatusStr() {
        return beforeChangeAssetStatusStr;
    }

    public void setBeforeChangeAssetStatusStr(String beforeChangeAssetStatusStr) {
        this.beforeChangeAssetStatusStr = beforeChangeAssetStatusStr;
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

    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }

    public String getCombinationAssetType() {
        return combinationAssetType;
    }

    public void setCombinationAssetType(String combinationAssetType) {
        this.combinationAssetType = combinationAssetType;
    }

    public String getCombinationAssetName() {
        return combinationAssetName;
    }

    public void setCombinationAssetName(String combinationAssetName) {
        this.combinationAssetName = combinationAssetName;
    }

    public String getAssetQuantity() {
        return assetQuantity;
    }

    public void setAssetQuantity(String assetQuantity) {
        this.assetQuantity = assetQuantity;
    }

    public String getMaterialSearchCode() {
        return materialSearchCode;
    }

    public void setMaterialSearchCode(String materialSearchCode) {
        this.materialSearchCode = materialSearchCode;
    }

    public Date getDeActiveTime() {
        return deActiveTime;
    }

    public void setDeActiveTime(Date deActiveTime) {
        this.deActiveTime = deActiveTime;
    }

    public String getDeActiveTimeStr() {
        return deActiveTimeStr;
    }

    public void setDeActiveTimeStr(String deActiveTimeStr) {
        this.deActiveTimeStr = deActiveTimeStr;
    }

}
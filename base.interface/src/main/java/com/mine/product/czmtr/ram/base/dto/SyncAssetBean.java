package com.mine.product.czmtr.ram.base.dto;

import java.io.Serializable;

/**
 * 实物资产主数据，推送给北明
 *
 * @author 杨杰
 */
public class SyncAssetBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1938146964280885636L;
    //基本信息Start
    private String assetCode; //资产编码
    private String materialCode; //物资编码
    private String assetChsName; //资产名称（ex中文名称）
    private String assetType; //资产类型
    private String specAndModels; //规格型号
    private String seriesNum; //序列号
    private String unitOfMeas;
    private String assetBrand; //资产品牌
    private String techPara; //技术参数 technical parameters
    private String purcPrice; //采购价
    private String equiOrigValue; //资产原值（ex设备原值 equipment original value）
    private String monthDeprMoney; //月折旧金额
    private String residualValue; //剩余价值
    private String scrapValue; //残余价值
    private String remark; //备注
    //基本信息End

    //延伸信息Start
    private String buyDate; //购置日期
    private String contractNum; //合同编号
    private String tendersNum; //标段编号
    private String companyCode;//所属公司编码
    private String manageDeptCode;//所属部门编码
    private String manageCode;   //资产管理员编码
    private String belongLine; //所属建筑
    private String savePlaceCode; //code
    //位置编码 End
    private String assetSource; //资产来源
    private String sourceUser; //联系人
    private String sourceContactInfo; //联系方式
    private String prodTime; //出厂时间
    private String mainPeriod; //维保期 maintenance period
    //保管使用信息End
    private String assetStatus; //资产状态
    private String createTime; //资产创建时间
    private String UpdateTime;

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

    public String getAssetChsName() {
        return assetChsName;
    }

    public void setAssetChsName(String assetChsName) {
        this.assetChsName = assetChsName;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
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

    public String getUnitOfMeas() {
        return unitOfMeas;
    }

    public void setUnitOfMeas(String unitOfMeas) {
        this.unitOfMeas = unitOfMeas;
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

    public String getMonthDeprMoney() {
        return monthDeprMoney;
    }

    public void setMonthDeprMoney(String monthDeprMoney) {
        this.monthDeprMoney = monthDeprMoney;
    }

    public String getResidualValue() {
        return residualValue;
    }

    public void setResidualValue(String residualValue) {
        this.residualValue = residualValue;
    }

    public String getScrapValue() {
        return scrapValue;
    }

    public void setScrapValue(String scrapValue) {
        this.scrapValue = scrapValue;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getManageDeptCode() {
        return manageDeptCode;
    }

    public void setManageDeptCode(String manageDeptCode) {
        this.manageDeptCode = manageDeptCode;
    }

    public String getManageCode() {
        return manageCode;
    }

    public void setManageCode(String manageCode) {
        this.manageCode = manageCode;
    }

    public String getBelongLine() {
        return belongLine;
    }

    public void setBelongLine(String belongLine) {
        this.belongLine = belongLine;
    }

    public String getSavePlaceCode() {
        return savePlaceCode;
    }

    public void setSavePlaceCode(String savePlaceCode) {
        this.savePlaceCode = savePlaceCode;
    }

    public String getAssetSource() {
        return assetSource;
    }

    public void setAssetSource(String assetSource) {
        this.assetSource = assetSource;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String UpdateTime) {
        this.UpdateTime = UpdateTime;
    }


}

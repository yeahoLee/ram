package com.mine.product.czmtr.ram.asset.dto;

import java.io.Serializable;

public class AssetBean implements Serializable {
    private static final long serialVersionUID = 6321338349149731765L;
    private String validateCode;    //资产编码验证码
    private String assetCode;        //资产编码
    private String materialCode;    //物资编码

    private String assetChsName; //资产名称（ex中文名称）
    private String assetStatus;        //资产状态
    private String assetType;            //资产类型
    private String assetTypeCode;    //资产类型
    private String seriesNum;        //序列号
    private String unitOfMeasCode;    //计量单位id unit of measurement
    private String assetBrand;        //资产品牌
    private String techPara;        //技术参数 technical parameters
    private String purcPrice;        //采购价 purchase price
    private String equiOrigValue;    //资产原值
    private String monthDeprMoney;   //月折旧价
    private String residualValue;    //残余价值
    private String remark;            //备注
    private String specAndModels;   //规格型号
    //基本信息End

    //延伸信息Start
    private String buyDate;        //购置日期
    private String contractNum;    //合同编号
    private String tendersNum;        //标段编号
    private String companyCode;    //所属公司编码
    private String manageDeptCode;    //主管部门编码
    private String managerCode;    //资产管理员编码
    private String useDeptCode;        //使用部门编码
    private String userCode;        //使用人编码
    private String belongLine;        //所属建筑
    //位置编码 = 资产线路 + 具体位置 + 设备房间号（建筑物号+楼层号+房间号）
    private String savePlaceCode;    //具体位置编码（ex存放地点）
    //位置编码 End
    private String assetSource;    //资产来源
    private String sourceType;        //来源方式
    private String sourceUser;        //联系人
    private String sourceContactInfo; //联系方式
    private String prodTime;        //出厂时间 production Time
    private String mainPeriod;        //维保期 maintenance period
    private String W_IS_PRO;        //是否进设备台账

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
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

    public String getAssetTypeCode() {
        return assetTypeCode;
    }

    public void setAssetTypeCode(String assetTypeCode) {
        this.assetTypeCode = assetTypeCode;
    }

    public String getSeriesNum() {
        return seriesNum;
    }

    public void setSeriesNum(String seriesNum) {
        this.seriesNum = seriesNum;
    }

    public String getUnitOfMeasCode() {
        return unitOfMeasCode;
    }

    public void setUnitOfMeasCode(String unitOfMeasCode) {
        this.unitOfMeasCode = unitOfMeasCode;
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

    public String getManagerCode() {
        return managerCode;
    }

    public void setManagerCode(String managerCode) {
        this.managerCode = managerCode;
    }

    public String getUseDeptCode() {
        return useDeptCode;
    }

    public void setUseDeptCode(String useDeptCode) {
        this.useDeptCode = useDeptCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
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

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getAssetChsName() {
        return assetChsName;
    }

    public void setAssetChsName(String assetChsName) {
        this.assetChsName = assetChsName;
    }

    public String getSpecAndModels() {
        return specAndModels;
    }

    public void setSpecAndModels(String specAndModels) {
        this.specAndModels = specAndModels;
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

    public String getW_IS_PRO() {
        return W_IS_PRO;
    }

    public void setW_IS_PRO(String w_IS_PRO) {
        W_IS_PRO = w_IS_PRO;
    }
}

package com.mine.product.czmtr.ram.asset.dto;

import com.mine.product.czmtr.ram.asset.service.IAssetService.HISTORY_TYPE;

/***
 * 资产变更历史
 * @author yangjie
 *
 */
public class AssetReportHistoryDto {
    private String assetCode; // 资产编码
    private String assetChsName; // 资产名称（ex中文名称）
    private String assetType; // 资产类型
    private String changeContent;//变更内容
    private String changeDate;//变更日期
    private String assetTypeStr; // 资产类型
    private String specAndModels; // 规格型号
    private String unitOfMeasId; // 计量单位id unit of measurement
    private String unitOfMeasStr;
    private String assetBrand; // 资产品牌
    private String manageDeptId; // 主管部门id
    private String manageDeptStr;
    private String managerId; // 资产管理员id
    private String managerStr;
    private String useDeptId; // 使用部门id
    private String useDeptStr;
    private String userId; // 使用人id
    private String useStr;
    private String belongLine; // 所属建筑
    private String belongLineStr;
    // 位置编码 = 资产线路 + 具体位置 + 设备房间号（建筑物号+楼层号+房间号）
    private String assetLineId; // 资产线路id
    private String assetLineStr;
    private String assetStatus; // 资产状态
    private String assetStatusStr;// 资产状态int值
    private String combinationAssetType;// 从物资编码（前8位）查询出的结果的组合
    private String combinationAssetName;// 从物资编码（12位）查询出的结果的组合
    private String materialCode; //物资编码
    private String createTimestampStart; //日期 开始(查询条件)
    private String createTimestampEnd; //日期 结束(查询条件)
    private String reduceTypeStr;//减损类型
    private HISTORY_TYPE historyType; //记录类型
    private String historyTypeStr; //记录类型
    private String changeTypeStr;//变更类型
    private String assetMoveHistory;//移动历史
    private String assetMoveDate;//移动日期

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
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

    public String getChangeContent() {
        return changeContent;
    }

    public void setChangeContent(String changeContent) {
        this.changeContent = changeContent;
    }

    public String getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(String changeDate) {
        this.changeDate = changeDate;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getCreateTimestampStart() {
        return createTimestampStart;
    }

    public void setCreateTimestampStart(String createTimestampStart) {
        this.createTimestampStart = createTimestampStart;
    }

    public String getCreateTimestampEnd() {
        return createTimestampEnd;
    }

    public void setCreateTimestampEnd(String createTimestampEnd) {
        this.createTimestampEnd = createTimestampEnd;
    }

    public String getReduceTypeStr() {
        return reduceTypeStr;
    }

    public void setReduceTypeStr(String reduceTypeStr) {
        this.reduceTypeStr = reduceTypeStr;
    }

    public HISTORY_TYPE getHistoryType() {
        return historyType;
    }

    public void setHistoryType(HISTORY_TYPE historyType) {
        this.historyType = historyType;
    }

    public String getHistoryTypeStr() {
        return historyTypeStr;
    }

    public void setHistoryTypeStr(String historyTypeStr) {
        this.historyTypeStr = historyTypeStr;
    }

    public String getChangeTypeStr() {
        return changeTypeStr;
    }

    public void setChangeTypeStr(String changeTypeStr) {
        this.changeTypeStr = changeTypeStr;
    }

    public String getAssetMoveHistory() {
        return assetMoveHistory;
    }

    public void setAssetMoveHistory(String assetMoveHistory) {
        this.assetMoveHistory = assetMoveHistory;
    }

    public String getAssetMoveDate() {
        return assetMoveDate;
    }

    public void setAssetMoveDate(String assetMoveDate) {
        this.assetMoveDate = assetMoveDate;
    }

}

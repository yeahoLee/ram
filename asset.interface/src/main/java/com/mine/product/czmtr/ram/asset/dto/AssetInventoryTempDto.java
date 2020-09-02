package com.mine.product.czmtr.ram.asset.dto;

public class AssetInventoryTempDto {

    /*******盘点清单字段 start******/
    private String id;
    private String assetId;
    private String operation;    //盘点操作
    private String operationStr;    //盘点操作
    private String result; //盘点结果
    private String resultStr; //盘点结果
    private String inventoryWay; //盘点方式
    private String inventoryWayStr; //盘点方式
    private String remark; //备注
    private String assetInventoryCode;//盘点编号
    /*******盘点清单字段 end******/
    //基本信息Start
    private String assetInventoryId;//任务清单ID
    private String myAssetInventoryId;//我的盘点清单ID
    private String assetCode; //资产编码
    private String materialCode; //物资编码
    private String assetChsName; //资产名称（ex中文名称）
    private String assetTypeStr; //资产类型
    private String specAndModels; //规格型号
    private String unitOfMeasId; //计量单位id unit of measurement
    private String unitOfMeasStr;
    private String equiOrigValue; //资产原值（ex设备原值 equipment original value）
    private String residualValue; //残余价值
    private String buyDate; //购置日期
    private String manageDeptId; //主管部门id
    private String manageDeptStr;
    private String managerId; //资产管理员id
    private String managerStr;
    private String useDeptId; //使用部门id
    private String useDeptStr;
    private String userId; //使用人id
    private String useStr;
    private String savePlaceStr; //code+name
    private String assetStatus;//资产状态

    //位置编码 End
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getInventoryWay() {
        return inventoryWay;
    }

    public void setInventoryWay(String inventoryWay) {
        this.inventoryWay = inventoryWay;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getAssetChsName() {
        return assetChsName;
    }

    public void setAssetChsName(String assetChsName) {
        this.assetChsName = assetChsName;
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

    public String getEquiOrigValue() {
        return equiOrigValue;
    }

    public void setEquiOrigValue(String equiOrigValue) {
        this.equiOrigValue = equiOrigValue;
    }

    public String getResidualValue() {
        return residualValue;
    }

    public void setResidualValue(String residualValue) {
        this.residualValue = residualValue;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(String buyDate) {
        this.buyDate = buyDate;
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

    public String getSavePlaceStr() {
        return savePlaceStr;
    }

    public void setSavePlaceStr(String savePlaceStr) {
        this.savePlaceStr = savePlaceStr;
    }

    public String getAssetStatus() {
        return assetStatus;
    }

    public void setAssetStatus(String assetStatus) {
        this.assetStatus = assetStatus;
    }

    public String getAssetInventoryId() {
        return assetInventoryId;
    }

    public void setAssetInventoryId(String assetInventoryId) {
        this.assetInventoryId = assetInventoryId;
    }

    public String getAssetInventoryCode() {
        return assetInventoryCode;
    }

    public void setAssetInventoryCode(String assetInventoryCode) {
        this.assetInventoryCode = assetInventoryCode;
    }

    public String getOperationStr() {
        return operationStr;
    }

    public void setOperationStr(String operationStr) {
        this.operationStr = operationStr;
    }

    public String getResultStr() {
        return resultStr;
    }

    public void setResultStr(String resultStr) {
        this.resultStr = resultStr;
    }

    public String getInventoryWayStr() {
        return inventoryWayStr;
    }

    public void setInventoryWayStr(String inventoryWayStr) {
        this.inventoryWayStr = inventoryWayStr;
    }

    public String getMyAssetInventoryId() {
        return myAssetInventoryId;
    }

    public void setMyAssetInventoryId(String myAssetInventoryId) {
        this.myAssetInventoryId = myAssetInventoryId;
    }

}

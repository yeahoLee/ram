package com.mine.product.czmtr.ram.asset.dto;

public class AssetDynamicViewDto {
    private String id;                            //系统自动生成id
    private String assetViewName;                //视图名
    private String assetStatus; //资产状态
    private String assetStatusNum;//原来是int
    private String assetType; //资产类型
    private String assetTypeNum;//原来是int
    private String savePlaceId; //具体位置（ex存放地点）
    private String manageDeptId; //主管部门
    private String useDeptId; //使用部门
    private String managerId; //资产管理员
    private String userId; //使用人
    private String creator;    //创建人
    private String assetViewCondition;//试图条件
    private String assetViewStateStr;//视图状态：启用，停用
    private int assetViewState;//1是启用状态，0是停用状态
    private String createTimestamp; //创建时间
    private String lastUpdateTimestamp;
    private String codeAndChsName;//位置的code和中文名

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

    public String getAssetStatusNum() {
        return assetStatusNum;
    }

    public void setAssetStatusNum(String assetStatusNum) {
        this.assetStatusNum = assetStatusNum;
    }

    public String getAssetTypeNum() {
        return assetTypeNum;
    }

    public void setAssetTypeNum(String assetTypeNum) {
        this.assetTypeNum = assetTypeNum;
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

    public String getAssetViewCondition() {
        return assetViewCondition;
    }

    public void setAssetViewCondition(String assetViewCondition) {
        this.assetViewCondition = assetViewCondition;
    }

    public String getAssetViewStateStr() {
        return assetViewStateStr;
    }

    public void setAssetViewStateStr(String assetViewStateStr) {
        this.assetViewStateStr = assetViewStateStr;
    }

    public int getAssetViewState() {
        return assetViewState;
    }

    public void setAssetViewState(int assetViewState) {
        this.assetViewState = assetViewState;
    }

    public String getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(String createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public String getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(String lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

    public String getCodeAndChsName() {
        return codeAndChsName;
    }

    public void setCodeAndChsName(String codeAndChsName) {
        this.codeAndChsName = codeAndChsName;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

}

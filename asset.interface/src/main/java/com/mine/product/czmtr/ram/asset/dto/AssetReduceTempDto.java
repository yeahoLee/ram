package com.mine.product.czmtr.ram.asset.dto;

import com.mine.product.czmtr.ram.asset.service.IAssetService;

import java.util.Date;

/**
 * @author 杨杰
 * @date 2019/11/26 16:07
 */
public class AssetReduceTempDto {
    private String id;
    private String assetReduceModelId;//使用人变更台账表ID
    private String assetId;//资产ID
    private Date createTimestamp; //创建时间
    private Date lastUpdateTimestamp;//更新时间
    private boolean isSysn;         //是否同步到NC财务系统
    private String changeNum;//减损单号
    private String reason;//减损原因
    private IAssetService.REDUCE_TYPE reduceType;//减损类型
    private String assetCode; //资产编码
    private String materialCode; //物资编码
    private String companyCode;//所属公司编码

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssetReduceModelId() {
        return assetReduceModelId;
    }

    public void setAssetReduceModelId(String assetReduceModelId) {
        this.assetReduceModelId = assetReduceModelId;
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

    public boolean isSysn() {
        return isSysn;
    }

    public void setSysn(boolean sysn) {
        isSysn = sysn;
    }

    public String getChangeNum() {
        return changeNum;
    }

    public void setChangeNum(String changeNum) {
        this.changeNum = changeNum;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public IAssetService.REDUCE_TYPE getReduceType() {
        return reduceType;
    }

    public void setReduceType(IAssetService.REDUCE_TYPE reduceType) {
        this.reduceType = reduceType;
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

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
}

package com.mine.product.czmtr.ram.base.dto;

/**
 * @author 杨杰
 * @date 2020/5/6 11:45
 */
public class FaCardViewDto {
    //资产编码
    private String assetCode;
    //资产名称
    private String assetName;
    //资产规格
    private String spec;
    //资产原值
    private String localoriginvalue;
    //剩余价值
    private String salvage;
    //残余价值
    private String netamount;
    //月折旧额
    private String depamount;
    //使用年限信息
    private String serviceyear;
    //对应归集的公司（NC的账套）
    private String pkCorp;

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getLocaloriginvalue() {
        return localoriginvalue;
    }

    public void setLocaloriginvalue(String localoriginvalue) {
        this.localoriginvalue = localoriginvalue;
    }

    public String getSalvage() {
        return salvage;
    }

    public void setSalvage(String salvage) {
        this.salvage = salvage;
    }

    public String getDepamount() {
        return depamount;
    }

    public void setDepamount(String depamount) {
        this.depamount = depamount;
    }

    public String getServiceyear() {
        return serviceyear;
    }

    public void setServiceyear(String serviceyear) {
        this.serviceyear = serviceyear;
    }

    public String getPkCorp() {
        return pkCorp;
    }

    public void setPkCorp(String pkCorp) {
        this.pkCorp = pkCorp;
    }

    public String getNetamount() {
        return netamount;
    }

    public void setNetamount(String netamount) {
        this.netamount = netamount;
    }

    @Override
    public String toString() {
        return "FaCardViewDto{" +
                "assetCode='" + assetCode + '\'' +
                ", assetName='" + assetName + '\'' +
                ", spec='" + spec + '\'' +
                ", localoriginvalue='" + localoriginvalue + '\'' +
                ", salvage='" + salvage + '\'' +
                ", netamount='" + netamount + '\'' +
                ", depamount='" + depamount + '\'' +
                ", serviceyear='" + serviceyear + '\'' +
                ", pkCorp='" + pkCorp + '\'' +
                '}';
    }
}

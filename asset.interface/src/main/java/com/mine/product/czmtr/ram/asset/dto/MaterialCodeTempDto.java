package com.mine.product.czmtr.ram.asset.dto;

import java.io.Serializable;

public class MaterialCodeTempDto implements Serializable {

    private static final long serialVersionUID = 4898333827647749688L;

    private String id;
    private String codeTemp; //物资编码 uuu001
    private String TempNum; //标识 uuid

    private String createTimestamp;
    private String lastUpdateTimestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCodeTemp() {
        return codeTemp;
    }

    public void setCodeTemp(String codeTemp) {
        this.codeTemp = codeTemp;
    }

    public String getTempNum() {
        return TempNum;
    }

    public void setTempNum(String tempNum) {
        TempNum = tempNum;
    }
}

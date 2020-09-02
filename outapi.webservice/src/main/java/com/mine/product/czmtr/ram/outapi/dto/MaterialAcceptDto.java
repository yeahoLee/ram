package com.mine.product.czmtr.ram.outapi.dto;

import java.io.Serializable;

public class MaterialAcceptDto implements Serializable {

    private static final long serialVersionUID = -460589497531426129L;

    private String receiptName;

    private String reason;

    private String remark;

    private String type;

    private String personId;

    public String getReceiptName() {
        return receiptName;
    }

    public void setReceiptName(String receiptName) {
        this.receiptName = receiptName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

}

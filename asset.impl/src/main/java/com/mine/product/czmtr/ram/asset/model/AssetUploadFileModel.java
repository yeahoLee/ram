package com.mine.product.czmtr.ram.asset.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/**
 * Attached file 附件
 */
@Entity
public class AssetUploadFileModel {
    @Id
    @GeneratedValue(generator = "gen")
    @GenericGenerator(
            name = "gen",
            strategy = "uuid",
            parameters = {
                    @Parameter(name = "separator", value = "_")
            }
    )
    private String id;
    @Column
    private String assetModelId; //资产id
    @Column
    private String fileName; //文件名称
    @Column
    private String fileSpec; //文件说明
    @Column(columnDefinition = "BLOB")
    private byte[] fileByteArray; //文件正文
    @Column
    private String createUserId; //创建人
    @Column
    private Date createTimestamp; //创建时间
    @Column
    private Date lastUpdateTimestamp;

    @PrePersist
    public void updateWhenCreate() {
        createTimestamp = Calendar.getInstance().getTime();
        lastUpdateTimestamp = Calendar.getInstance().getTime();
    }

    @PreUpdate
    public void updateWhenUpdate() {
        lastUpdateTimestamp = Calendar.getInstance().getTime();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssetModelId() {
        return assetModelId;
    }

    public void setAssetModelId(String assetModelId) {
        this.assetModelId = assetModelId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSpec() {
        return fileSpec;
    }

    public void setFileSpec(String fileSpec) {
        this.fileSpec = fileSpec;
    }

    public byte[] getFileByteArray() {
        return fileByteArray;
    }

    public void setFileByteArray(byte[] fileByteArray) {
        this.fileByteArray = fileByteArray;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
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
}

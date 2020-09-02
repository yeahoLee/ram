package com.mine.product.czmtr.ram.asset.dto;

import org.springframework.web.multipart.MultipartFile;

public class AssetUploadFileDto {
    private String assetModelId; //资产id
    private String fileSpec; //文件说明
    private MultipartFile uploadFileData;

    public String getAssetModelId() {
        return assetModelId;
    }

    public void setAssetModelId(String assetModelId) {
        this.assetModelId = assetModelId;
    }

    public String getFileSpec() {
        return fileSpec;
    }

    public void setFileSpec(String fileSpec) {
        this.fileSpec = fileSpec;
    }

    public MultipartFile getUploadFileData() {
        return uploadFileData;
    }

    public void setUploadFileData(MultipartFile uploadFileData) {
        this.uploadFileData = uploadFileData;
    }
}

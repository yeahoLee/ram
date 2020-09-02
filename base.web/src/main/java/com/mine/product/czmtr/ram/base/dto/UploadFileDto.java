package com.mine.product.czmtr.ram.base.dto;

import org.springframework.web.multipart.MultipartFile;

public class UploadFileDto {
    private String fileSpec; //文件说明
    private MultipartFile uploadFileData;
    private String dictType; //数据字典类型
    private String dictPdType; //数据字典父类

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

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getDictPdType() {
        return dictPdType;
    }

    public void setDictPdType(String dictPdType) {
        this.dictPdType = dictPdType;
    }
}

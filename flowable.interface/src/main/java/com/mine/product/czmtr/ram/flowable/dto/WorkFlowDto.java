package com.mine.product.czmtr.ram.flowable.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author : yeaho_lee
 * @Description : 流程基础信息
 * @createTime : 2020年07月08日 15:48
 */
@Data
public class WorkFlowDto {
    private String id;

    /**
     * 流程名称
     */
    private String flowKey;

    /**
     * 流程名称
     */
    private String flowName;

    /**
     * 流程描述
     */
    private String flowDescription;

    /**
     * 流程图片名称
     */
    private String imgName;

    private Date createTimestamp;

    private Date lastUpdateTimestamp;

    private String createTimestampStr;

    private String lastUpdateTimestampStr;
}

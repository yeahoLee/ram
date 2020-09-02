package com.mine.product.czmtr.ram.flowable.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author : yeaho_lee
 * @Description : TODO
 * @createTime : 2020年07月10日 14:56
 */
@Data
public class WorkFlowProcessDto {

    private String id;

    /**
     * 流程ID
     */
    private String flowId;

    /**
     * 环节key
     */
    private String processKey;

    /**
     * 用户组ID
     */
    private String groupId;

    /**
     * 用户组名称
     */
    private String groupName;

    private Date createTimestamp;
    private String createTimestampStr;

    private Date lastUpdateTimestamp;
    private String lastUpdateTimestampStr;
}

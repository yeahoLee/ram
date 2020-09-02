package com.mine.product.czmtr.ram.flowable.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author : yeaho_lee
 * @Description : 流程用户组
 * @createTime : 2020年07月08日 15:48
 */
@Data
public class WorkFlowUserGroupDto {

    private String id;

    /**
     * 用户组编码
     */
    private String groupCode;

    /**
     * 用户组名称
     */
    private String groupName;

    /**
     * 用户组描述
     */
    private String groupDescription;

    private Date createTimestamp;

    private Date lastUpdateTimestamp;

    private String createTimestampStr;

    private String lastUpdateTimestampStr;
}

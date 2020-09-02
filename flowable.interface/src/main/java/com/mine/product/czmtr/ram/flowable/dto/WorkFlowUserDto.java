package com.mine.product.czmtr.ram.flowable.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author : yeaho_lee
 * @Description : 流程用户组用户
 * @createTime : 2020年07月08日 15:48
 */
@Data
public class WorkFlowUserDto {
    private String id;

    /**
     * 用户Id
     */
    private String userCode;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户组ID
     */
    private String groupId;

    private Date createTimestamp;

    private Date lastUpdateTimestamp;

    private String createTimestampStr;

    private String lastUpdateTimestampStr;
}

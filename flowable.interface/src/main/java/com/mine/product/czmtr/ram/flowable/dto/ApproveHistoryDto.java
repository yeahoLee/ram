package com.mine.product.czmtr.ram.flowable.dto;

import lombok.Data;

/**
 * @author : yeaho_lee
 * @Description : TODO
 * @createTime : 2020年07月24日 09:41
 */
@Data
public class ApproveHistoryDto {

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 审批意见
     */
    private String message;

    /**
     * 审批时间
     */
    private String approveTime;

    /**
     * 审批类型
     * @see com.mine.product.czmtr.ram.flowable.info.CommentTypeEnum
     */
    private String type;

    /**
     * 审批类型名称
     */
    private String typeName;

    /**
     * 任务名称
     */
    private String taskName;


}

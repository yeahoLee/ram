package com.mine.product.czmtr.ram.flowable.dto;

import lombok.Data;

/**
 * @author : yeaho_lee
 * @Description : TODO
 * @createTime : 2020年07月15日 14:29
 */
@Data
public class FlowRuleDto {
    private String id;

    /**
     * 节点ID
     */
    private String nodeId;

    /**
     * 上一个环节是否有同样的审批人
     */
    private int previousUserSame;

    /**
     * 历史环节是否有相同的审批人
     */
    private int historyUserSame;

    /**
     * 操作类型
     */
    private int operationType;

    /**
     * 流程key
     */
    private String procDefKey;
}

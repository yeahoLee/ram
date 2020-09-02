package com.mine.product.czmtr.ram.flowable.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author : yeaho_lee
 * @Description : 流程配置表
 * @createTime : 2020年07月15日 13:58
 */
@Data
@Entity
public class FlowRuleModel {
    @Id
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name = "gen", strategy = "uuid",
            parameters = {@Parameter(name = "separator", value = "_")})
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
    @Column(columnDefinition="INT default 1")
    private int operationType;

    /**
     * 流程key
     */
    private String procDefKey;
}

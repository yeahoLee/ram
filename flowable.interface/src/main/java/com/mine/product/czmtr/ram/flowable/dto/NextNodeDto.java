package com.mine.product.czmtr.ram.flowable.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName : NextNodeDto
 * @Description : 下一环节及人员
 * @Author : Mr.Chai
 * @Date : 2020-06-30 18:41
 */
@Data
@ToString
public class NextNodeDto implements Serializable {
    private static final long serialVersionUID = 5105326541320426986L;
    /**
     * 节点id
     */
    private String nodeId;
    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 经过的网关：0未经过 1排他网关 2并行网关 3包容网关
     */
    private int gateway;
    /**
     * 是否处于会签中
     */
    private boolean isInMultiInstance;
    /**
     * 是否处于子流程中
     */
    private boolean isInSubProcess;
    /**
     * 下一环节用户
     */
    private List<NextNodeUserDto> users;

}

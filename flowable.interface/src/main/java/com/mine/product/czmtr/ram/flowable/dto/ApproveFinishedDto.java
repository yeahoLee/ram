package com.mine.product.czmtr.ram.flowable.dto;

import com.mine.product.flowable.api.dto.ret.ProcessInstanceVo;
import lombok.Data;

/**
 * @author : yeaho_lee
 * @Description : TODO
 * @createTime : 2020年07月15日 16:47
 */
@Data
public class ApproveFinishedDto extends ProcessInstanceVo {
    /**
     * 审批编号
     */
    private String approveNumber;

    /**
     * 流程开始时间
     */
    private String processInstanceStartTimeStr;

    /**
     * 当前环节
     */
    private String runningProcesses;

    /**
     * 当前环节处理人
     */
    private String runningProcessUsers;

}

package com.mine.product.czmtr.ram.flowable.dto;

import com.mine.product.flowable.api.dto.ret.TaskVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author : yeaho_lee
 * @Description : TODO
 * @createTime : 2020年07月14日 15:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApproveToDoDto extends TaskVo {

    /**
     * 审批编号
     */
    private String approveNumber;

    /**
     * 流程开始时间
     */
    private String processInstanceStartTimeStr;

    /**
     * 上一环节发送时间
     */
    private String startTimeStr;


}

package com.mine.product.czmtr.ram.flowable.dto;

import com.mine.product.czmtr.ram.asset.dto.FlowableInfo;
import com.mine.product.czmtr.ram.asset.service.IAssetService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author : yeaho_lee
 * @Description : TODO
 * @createTime : 2020年07月14日 14:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorksDto {
    private String id;

    /**
     *  发起人
     */
    private String starterId;

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 业务ID
     */
    private String businessKey;

    /**
     * 审批状态
     */
    private FlowableInfo.WORKSTATUS workStatus;

    /**
     * 工单类型
     */
    private String workOrderType;

    /**
     * 审批编号
     */
    private String approvalNumber;

    /**
     * 全局变量
     */
    private String globalVariables;

    /**
     * 复用流程
     * 管理员变更，使用人员变更，位置变更流程页面分别对应 "0"，"1"，"2"(ASSETS_USER_LOCATION_CHANGE)
     */
    public String publicUse;

    /**
     * 生产性非生产
     */
    public IAssetService.ASSET_PRODUCE_TYPE produceType;

    private Date createTimestamp;

    private Date lastUpdateTimestamp;

    private String createTimestampStr;

    private String lastUpdateTimestampStr;
}

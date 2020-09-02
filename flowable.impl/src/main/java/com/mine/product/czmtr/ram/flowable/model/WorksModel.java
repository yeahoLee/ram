package com.mine.product.czmtr.ram.flowable.model;

import com.mine.product.czmtr.ram.asset.dto.FlowableInfo;
import com.mine.product.czmtr.ram.asset.service.IAssetService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/**
 * @author : yeaho_lee
 * @Description : TODO
 * @createTime : 2020年07月14日 14:17
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class WorksModel {

    @Id
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name = "gen", strategy = "uuid",
            parameters = {@Parameter(name = "separator", value = "_")})
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
     * 封存，启封对应 "0"，"1"（FIXED_ASSETS_ARCHIVE_CENTER）
     */
    public String publicUse;

    /**
     * 生产性非生产
     */
    public IAssetService.ASSET_PRODUCE_TYPE produceType;

    private Date createTimestamp;

    private Date lastUpdateTimestamp;

    @PrePersist
    public void updateWhenCreate() {
        createTimestamp = Calendar.getInstance().getTime();
        lastUpdateTimestamp = Calendar.getInstance().getTime();
    }

    @PreUpdate
    public void updateWhenUpdate() {
        lastUpdateTimestamp = Calendar.getInstance().getTime();
    }
}

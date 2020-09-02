package com.mine.product.czmtr.ram.flowable.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/**
 * @author : yeaho_lee
 * @Description : TODO
 * @createTime : 2020年07月10日 13:42
 */
@Entity
@Data
public class WorkFlowProcessModel {
    @Id
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name = "gen", strategy = "uuid",
            parameters = {@Parameter(name = "separator", value = "_")})
    private String id;

    /**
     * 流程ID
     */
    private String flowId;

    /**
     * 环节key
     */
    private String processKey;

    /**
     * 用户组ID
     */
    private String groupId;

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

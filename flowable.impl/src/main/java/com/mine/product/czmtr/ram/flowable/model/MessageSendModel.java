package com.mine.product.czmtr.ram.flowable.model;

import com.mine.product.czmtr.ram.asset.dto.FlowableInfo;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author : yeaho_lee
 * @Description : 推送OU待办记录表
 * @createTime : 2020年08月11日 10:23
 */
@Entity
@Data
public class MessageSendModel {
    @Id
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name = "gen", strategy = "uuid",
            parameters = {@Parameter(name = "separator", value = "_")})
    private String id;

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 消息name
     */
    private String messageName;

    /**
     * 消息内容
     */
    private String messageContent;

    /**
     * 消息类型（0：消息，1：待办）
     */
    private String messageType;

    /**
     * 消息状态（0：未办 1：已办）
     */
    private String messageTodoState;

    /**
     * 消息来源系统编码
     */
    private String messageSrc;

    /**
     * 消息发送人
     */
    private String senderId;

    /**
     * 消息目标系统
     */
    private String messageTarget;

    /**
     * 消息接收人
     */
    private String receiverId;

    /**
     * 消息发送日期
     */
    private String postDt;

    /**
     * 回调url
     */
    private String url;

    /**
     * 备注
     */
    private String remark;

    /**
     * 发送状态（0：失败，1：成功）
     */
    private int status;








}

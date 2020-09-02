package com.mine.product.czmtr.ram.flowable.dao;

import com.mine.product.czmtr.ram.flowable.model.MessageSendModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : yeaho_lee
 * @Description : OU待办持久层
 * @createTime : 2020年08月11日 10:55
 */
public interface MessageSendDao extends JpaRepository<MessageSendModel, String> {
}

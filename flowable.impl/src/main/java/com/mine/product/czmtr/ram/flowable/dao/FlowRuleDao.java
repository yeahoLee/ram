package com.mine.product.czmtr.ram.flowable.dao;

import com.mine.product.czmtr.ram.flowable.model.FlowRuleModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : yeaho_lee
 * @Description : TODO
 * @createTime : 2020年07月15日 14:18
 */
public interface FlowRuleDao extends JpaRepository<FlowRuleModel, String> {

    FlowRuleModel findByNodeIdAndProcDefKey(String nodeId, String procDefKey);
}

package com.mine.product.czmtr.ram.flowable.dao;

import com.mine.product.czmtr.ram.flowable.model.WorkFlowProcessModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : yeaho_lee
 * @Description : TODO
 * @createTime : 2020年07月10日 16:11
 */
public interface WorkFlowProcessDao extends JpaRepository<WorkFlowProcessModel, String> {
    WorkFlowProcessModel findAllByFlowIdAndAndProcessKey(String flowId, String processKey);
    void deleteAllByFlowId(String flowId);
}

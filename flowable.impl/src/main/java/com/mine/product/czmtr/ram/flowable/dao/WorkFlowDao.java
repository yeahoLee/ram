package com.mine.product.czmtr.ram.flowable.dao;

import com.mine.product.czmtr.ram.flowable.model.WorkFlowModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author : yeaho_lee
 * @Description : 流程管理持久层
 * @createTime : 2020年07月09日 09:20
 */
@Repository
public interface WorkFlowDao extends JpaRepository<WorkFlowModel, String>, JpaSpecificationExecutor<WorkFlowModel> {
    WorkFlowModel findByFlowKey(String flowKey);
}

package com.mine.product.czmtr.ram.flowable.dao;

import com.mine.product.czmtr.ram.flowable.model.WorkFlowUserGroupModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author : yeaho_lee
 * @Description : 流程用户组持久层
 * @createTime : 2020年07月09日 14:00
 */
public interface WorkFlowUserGroupDao extends JpaRepository<WorkFlowUserGroupModel, String>, JpaSpecificationExecutor<WorkFlowUserGroupModel> {
    WorkFlowUserGroupModel findByGroupCode(String code);
}

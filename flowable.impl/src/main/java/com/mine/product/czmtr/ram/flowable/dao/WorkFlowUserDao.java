package com.mine.product.czmtr.ram.flowable.dao;

import com.mine.product.czmtr.ram.flowable.model.WorkFlowUserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author : yeaho_lee
 * @Description : 流程管理用户
 * @createTime : 2020年07月09日 16:47
 */
public interface WorkFlowUserDao extends JpaRepository<WorkFlowUserModel, String> {

    List<WorkFlowUserModel> findAllByGroupId(String groupId);

    void deleteAllByGroupId(String groupId);

    WorkFlowUserModel findByGroupIdAndUserCode(String groupId, String userCode);

}

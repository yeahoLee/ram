package com.mine.product.czmtr.ram.flowable.dao;

import com.mine.product.czmtr.ram.flowable.model.WorksModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : yeaho_lee
 * @Description : TODO
 * @createTime : 2020年07月14日 14:30
 */
public interface WorksDao extends JpaRepository<WorksModel, String> {

    WorksModel findByBusinessKey(String businessKey);

    WorksModel findByProcessInstanceId(String processInstanceId);

    WorksModel findByApprovalNumber(String approveCode);

}

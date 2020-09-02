package com.mine.product.czmtr.ram.flowable.service;

import com.alibaba.fastjson.JSON;
import com.mine.product.czmtr.ram.asset.dto.FlowableInfo;
import com.mine.product.czmtr.ram.flowable.dto.WorksDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author : yeaho_lee
 * @Description : TODO
 * @createTime : 2020年07月16日 18:04
 */
@Service(value="flowableApi")
@Slf4j
public class FlowableApi implements IFlowableApi{

    @Autowired
    private IFlowableService flowableService;
    @Override
    public void addFinishDeal(String params) {
        try {
            Map<String, String> paramsMap = (Map) JSON.parse(params);

            //主管领导审批完成后修改审批状态为已审批
            WorksDto worksDto = flowableService.getWorksModelByBusinessKey(paramsMap.get("businessKey"));
            worksDto.setWorkStatus(FlowableInfo.WORKSTATUS.已审批);
            WorksDto worksDto1 = flowableService.updateWorksModel(worksDto);

            //业务逻辑
            flowableService.updateBusinessStatus(worksDto1);

            //TODO 新增和减损推送到财务系统

        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getMessage());
        }
    }
}

package com.mine.product.czmtr.ram.flowable.service;

import com.mine.platform.common.dto.CommonComboDto;
import com.mine.product.czmtr.ram.asset.dto.AssetApprove;
import com.mine.product.czmtr.ram.asset.service.IApproveNotify;
import com.mine.product.czmtr.ram.flowable.dto.*;
import com.mine.product.flowable.api.dto.ProcessInstanceQueryVo;
import com.mine.product.flowable.api.dto.TaskQueryVo;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * @author : yeaho_lee
 * @Description : 业务接口
 * @createTime : 2020年07月09日 09:17
 */
public interface IFlowableService {

    /**
     *@description : 创建流程（流程管理）
     *@params : workFlowDto
     *@return : void
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/9
     *@updateInfo :
     */
    void createWorkFlow(WorkFlowDto workFlowDto);

    /**
     *@description : 查询流程管理列表
     *@params : [pageRequest, q]
     *@return : Map<String, Object>
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/9
     *@updateInfo :
     */
    Map<String, Object> getWorkFlowList(PageRequest pageRequest, String q);

    /**
     *@description : 删除流程
     *@params : [id]
     *@return : void
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/9
     *@updateInfo :
     */
    void deleteWorkFlow(String id);

    /**
     *@description : 新增用户组
     *@params : [workFlowUserGroupDto]
     *@return : void
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/9
     *@updateInfo :
     */
    void createUserGroup(WorkFlowUserGroupDto workFlowUserGroupDto);

    /**
     *@description : 查询用户组列表
     *@params : [pageRequest, workFlowId， q]
     *@return : Map<String, Object>
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/9
     *@updateInfo :
     */
    List<WorkFlowUserGroupDto> getUserGroupList(String workFlowId, String q);

    /**
     *@description : 删除用户组
     *@params : [id]
     *@return : void
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/9
     *@updateInfo :
     */
    void deleteUserGroup(String id);

    /**
     *@description : 获取用户组combobox
     *@params :
     *@return : List<CommonComboDto>
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/9
     *@updateInfo :
     */
    List<CommonComboDto> getGroupUserCombo();

    /**
     *@description : 新增用户
     *@params : [workFlowUserDto]
     *@return : void
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/9
     *@updateInfo :
     */
    void createUser(WorkFlowUserDto workFlowUserDto);

    /**
     *@description : 获取用户列表
     *@params : [groupId]
     *@return : List<WorkFlowUserDto>
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/9
     *@updateInfo :
     */
    List<WorkFlowUserDto> getUserList(String groupId);

    /**
     *@description : 删除用户
     *@params : [id]
     *@return : void
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/9
     *@updateInfo :
     */
    void deleteUser(String id);

    /**
     *@description : 创建流程环节和用户组关联表
     *@params : [workFlowProcessDto]
     *@return : void
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/10
     *@updateInfo :
     */
    void createProcessGroup(WorkFlowProcessDto workFlowProcessDto);

    /**
     *@description : 删除流程环节用户组
     *@params : [id]
     *@return : void
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/10
     *@updateInfo :
     */
    void deleteProcessGroup(String id);

    /**
     * 根据流程ID和环节key获取用户组
     * @param flowId
     * @param processKey
     * @return
     */
    WorkFlowProcessDto getGroupIdByProcessKey(String flowId, String processKey);

    /**
     * 获取环节人员
     * @param processDefinitionKey
     * @param processLink
     * @param worksDto
     * @param map
     * @return
     */
    List<NextNodeUserDto> getProcessUser(String processDefinitionKey, String processLink, WorksDto worksDto, Map<String, String> map);

    boolean isCenter(String userCode);

    WorkFlowUserGroupDto getOneWorkFlowGroup(String groupCode);

    /**
     *@description : 保存工单
     *@params : worksDto, processDefKey
     *@return : WorksDto
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/14
     *@updateInfo :
     */
    WorksDto createWorksModel(WorksDto worksDto);

    WorksDto updateWorksModel(WorksDto worksDto);

    /**
     *@description : 根据业务Id获取审批工单信息
     *@params : businessKey
     *@return : WorksDto
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/14
     *@updateInfo :
     */
    WorksDto getWorksModelByBusinessKey(String businessKey);

    /**
     *@description : 根据流程实例Id获取审批工单信息
     *@params : processInstanceId 
     *@return : WorksDto
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/14
     *@updateInfo :
     */
    WorksDto getWorksModelByProcessInstanceId(String processInstanceId);

    /**
     *@description : 获取流程环节的配置
     *@params :  nodeId, procDefKey
     *@return : FlowRuleDto
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/15
     *@updateInfo :
     */
    FlowRuleDto getFlowRuleByNodeIdAndProcDefKey(String nodeId, String procDefKey);


    /**
     * @Description:  审批成功或者审批退回时调用
     * @Param:  businessKey 业务Id
     * @Param:  approveNotify 业务的服务实现类
     * @return:
     * @Author: lichuan.zhang
     * @Date: 2020/7/16
     */
   void approveNotify(IApproveNotify approveNotify, String businessKey);

    AssetApprove doApprove(IApproveNotify approveNotify, String businessKey);

    /**
    *@description : 同步流程配置
    *@params :
    *@return :
    *@createBy : yeaho_lee
    *@reateTime : 2020/7/21
    *@updateInfo :
    */
    void syncFlowConfiguration();

    /**
     *@description : 获取已办
     *@params :  [params, page, rows]
     *@return : Map<String, Object>
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/29
     *@updateInfo :
     */
    Map<String, Object> finishedWork(ProcessInstanceQueryVo params, String page, String rows);

    /**
     *@description : 驳回
     *@params : [suggestContent, processDefinitionKey, procInstId, taskId, nodeId]
     *@return : void
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/29
     *@updateInfo :
     */
    void turnBack(String url, String suggestContent, String processDefinitionKey, String procInstId, String taskId, String nodeId);

    /**
     *@description : 同意
     *@params : [processInstanceId, taskId, params, suggestContent]
     *@return : void
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/29
     *@updateInfo :
     */
    void agreeWork(String url, String processInstanceId, String taskId, String params, String suggestContent);

    /**
     *@description : 获取流程图片
     *@params : [processInstanceId]
     *@return : byte[]
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/29
     *@updateInfo :
     */
    byte[] getWorkflowImage(String processInstanceId);

    /**
     *@description : 获取待办列表
     *@params : [params, page, rows]
     *@return : Map<String, Object>
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/29
     *@updateInfo :
     */
    Map<String, Object> getTodoWork(TaskQueryVo params, String page, String rows);

    /**
     *@description : 第一环节提交审批
     *@params : [map]
     *@return : void
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/29
     *@updateInfo :
     */
    void submitNext(String url, Map<String, String> map);

    /**
     *@description : 修改业务的状态
     *@params :  id 业务ID
     *@params :  processDefKey 流程key
     *@params :  publicUse 标识符
     *@return :  无返回
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/30
     *@updateInfo :
     */
    void updateBusinessStatus(WorksDto worksDto);

    /**
     *@description : 获取第一环节审批人
     *@params : map
     *@return : List<NextNodeDto>
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/29
     *@updateInfo :
     */
    List<NextNodeDto> getFirstNode(Map<String, String> map);

    /**
     *@description : 获取下个环节审批人员
     *@params : procInstId
     *@params : taskId
     *@params : processDefinitionKey
     *@return : List<NextNodeDto>
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/29
     *@updateInfo :
     */
    List<NextNodeDto> getNextNode(String procInstId, String taskId, String processDefinitionKey);

    /**
     *@description : 根据流程定义key获取所有环节
     *@params : [key]
     *@return : List<CommonComboDto>
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/29
     *@updateInfo :
     */
    List<CommonComboDto> getProcess(String key);

    /**
     *@description : 撤回流程
     *@params : [processInstanceId, suggestContent]
     *@return : void
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/29
     *@updateInfo :
     */
    void revorkFlow(String url, String processInstanceId, String suggestContent);

    /**
     *@description : 获取审批记录
     *@params : [processInstanceId]
     *@return : List<ApproveHistoryDto>
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/29
     *@updateInfo :
     */
    List<ApproveHistoryDto> getApproveHistory(String processInstanceId);

    /**
     *@description : 获取枚举定义流程信息列表
     *@params :
     *@return : List<CommonComboDto>
     *@createBy : yeaho_lee
     *@reateTime : 2020/7/29
     *@updateInfo :
     */
    List<CommonComboDto> getWorkFlowList();

    /**
     *@description : 根据工单code获取worksModel
     *@params : approveCode 工单编Code
     *@return : WorksDto
     *@createBy : yeaho_lee
     *@reateTime : 2020/8/3
     *@updateInfo :
     */
    WorksDto getWorksModelByApproveCode(String approveCode);

}

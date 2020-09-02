package com.mine.product.czmtr.ram.flowable.controller;

import com.mine.platform.common.dto.CommonComboDto;
import com.mine.product.czmtr.ram.flowable.dto.*;
import com.mine.product.czmtr.ram.flowable.service.IFlowableService;
import com.mine.product.czmtr.ram.flowable.utils.HttpUtil;
import com.mine.product.flowable.api.dto.ProcessInstanceQueryVo;
import com.mine.product.flowable.api.dto.TaskQueryVo;
import com.vgtech.platform.common.utility.VGUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * @Program: ram
 * @Description: flowable控制层
 * @Author: lichuan.zhang
 * @Date: 2020/7/2
 **/
@RestController
@RequestMapping(value = "/flowable/")
@Slf4j
public class FlowableController {

	@Autowired
	private IFlowableService flowableService;

	@PostMapping("create_work_flow")
	public ResponseEntity<String> createWorkFlow(@RequestBody WorkFlowDto workFlowDto) {
		log.info("Create WorkFlow FlowKey = [{}]", workFlowDto.getFlowKey());
		flowableService.createWorkFlow(workFlowDto);
		return new ResponseEntity<String>("{\"success\" : true}", HttpStatus.OK);
	}

	@PostMapping("work_flow_list")
	public Map<String, Object> getWorkFlowList(@RequestParam(defaultValue = "1") String page,
											   @RequestParam(defaultValue = "20") String rows,
											   @RequestParam(defaultValue = "") String q) throws UnsupportedEncodingException {
		log.info("Get WorkFlow List Query= [{}]", java.net.URLDecoder.decode(q, "UTF-8"));
		PageRequest pageRequest = PageRequest.of(VGUtility.toInteger(page) - 1, VGUtility.toInteger(rows));
		return flowableService.getWorkFlowList(pageRequest, q);
	}

	@DeleteMapping("delete_work_flow")
	public ResponseEntity<String> deleteWorkFlow(@RequestParam String id) {
		log.info("Delete Work Flow ID = [{}]", id);
		flowableService.deleteWorkFlow(id);
		return new ResponseEntity<String>("{\"success\" : true}", HttpStatus.OK);

	}
	@PostMapping("create_user_group")
	public ResponseEntity<String> createUserGroup(@RequestBody WorkFlowUserGroupDto workFlowUserGroupDto) {
		log.info("Create WorkFlow User Group Code = [{}]", workFlowUserGroupDto.getGroupCode());
		flowableService.createUserGroup(workFlowUserGroupDto);
		return new ResponseEntity<String>("{\"success\" : true}", HttpStatus.OK);
	}

	@PostMapping("user_group_list")
	public List<WorkFlowUserGroupDto> getUserGroupList(@RequestParam(required = false) String workFlowId,
													   @RequestParam(defaultValue = "") String q) throws UnsupportedEncodingException {
		log.info("Get USER List FlowId = [{}]", workFlowId);
		return flowableService.getUserGroupList(workFlowId, q);
	}

	@DeleteMapping("delete_user_group")
	public ResponseEntity<String> deleteUserGroup(@RequestParam String id) {
		log.info("Delete User Group ID = [{}]", id);
		flowableService.deleteUserGroup(id);
		return new ResponseEntity<String>("{\"success\" : true}", HttpStatus.OK);
	}

	@GetMapping("group_user_combo")
	public List<CommonComboDto> getGroupUserCombo() {
		log.info("Get Group User Combo ");
		return flowableService.getGroupUserCombo();
	}

	@PostMapping("create_user")
	public ResponseEntity<String> createUser(@RequestBody WorkFlowUserDto workFlowUserDto) {
		log.info("Create WorkFlow User GroupId = [{}] UserId = [{}]", workFlowUserDto.getGroupId(), workFlowUserDto.getUserCode());
		flowableService.createUser(workFlowUserDto);
		return new ResponseEntity<String>("{\"success\" : true}", HttpStatus.OK);
	}

	@PostMapping("user_list")
	public List<WorkFlowUserDto> getUserList(@RequestParam String groupId) {
		log.info("Get USER List GroupId = [{}]", groupId);
		return flowableService.getUserList(groupId);
	}

	@DeleteMapping("delete_user")
	public ResponseEntity<String> deleteUser(@RequestParam String id) {
		log.info("Delete User ID = [{}]", id);
		flowableService.deleteUser(id);
		return new ResponseEntity<String>("{\"success\" : true}", HttpStatus.OK);
	}

	@PostMapping("get_process")
	public List<CommonComboDto> getProcess(@RequestParam String key) {
		log.info("GET PROCESS WHERE KEY = {}", key);
		return flowableService.getProcess(key);
	}

	@PostMapping("create_process_group")
	public ResponseEntity<String> createProcessGroup(@RequestBody WorkFlowProcessDto workFlowProcessDto) {
		log.info("Create WorkFlow Process GroupId = [{}] processKey = [{}]", workFlowProcessDto.getGroupId(), workFlowProcessDto.getProcessKey());
		flowableService.createProcessGroup(workFlowProcessDto);
		return new ResponseEntity<String>("{\"success\" : true}", HttpStatus.OK);
	}

	@DeleteMapping("delete_process_group")
	public ResponseEntity<String> deleteProcessGroup(@RequestParam String id) {
		log.info("Delete Process Group Id = [{}]", id);
		flowableService.deleteProcessGroup(id);
		return new ResponseEntity<String>("{\"success\" : true}", HttpStatus.OK);
	}

	@PostMapping("get_group_by_process")
	public WorkFlowProcessDto getGroupIdByProcessKey(@RequestParam String flowId, @RequestParam String processKey) {
		log.info("get groupId by ProcessKey = [{}] and flowId = [{}]", processKey, flowId);
		return flowableService.getGroupIdByProcessKey(flowId, processKey);
	}

	@PostMapping("get_next_node")
	public List<NextNodeDto> getNextNode(@RequestParam String procInstId, @RequestParam String taskId, @RequestParam String processDefinitionKey) {
		log.info("GET NEXT NODE procInstId=[{}], taskId=[{}], processDefinitionKey=[{}]", procInstId, taskId, processDefinitionKey);
		return flowableService.getNextNode(procInstId, taskId, processDefinitionKey);
	}

	@PostMapping("get_first_node")
	public List<NextNodeDto> getFirstNode(@RequestBody Map<String, String> map) {
		log.info("获取第一环节审批人 id = [{}]", map.get("id"));
		return flowableService.getFirstNode(map);
	}

	@PostMapping("submit_next")
	public ResponseEntity<String> submitNext(HttpServletRequest request, @RequestBody Map<String, String> map) {
		log.info("第一环节提交 ===》 id = [{}]", map.get("id"));
		String localUrl = HttpUtil.getLocalUrl(request);
		flowableService.submitNext(localUrl, map);
		return new ResponseEntity<String>("{\"success\":true}", HttpStatus.OK);
	}

	@GetMapping("/todo-work")
	private Map<String, Object> todoWork(TaskQueryVo params, String page, String rows) {
		log.info("Get todo work");
		return flowableService.getTodoWork(params, page, rows);
	}

	@GetMapping("getWorkflowImage")
	public void getWorkflowImage(HttpServletResponse response, @RequestParam("processInstanceId") String processInstanceId){
		log.info("workflow image processInstanceId = " + "processInstanceId");
		byte[] workflowImage = flowableService.getWorkflowImage(processInstanceId);
		try {
			response.getOutputStream().write(workflowImage);
		} catch (IOException e) {
			log.error("获取分支流程图Image错误："+e.toString());
			throw new RuntimeException("获取分支流程图Image错误["+e.toString()+"]");
		}
	}

	@PostMapping("/agreeWork")
	public ResponseEntity<String> agreeWork(@RequestParam String processInstanceId,
											HttpServletRequest request,
											@RequestParam String taskId,
											@RequestParam String params,
											@RequestParam String suggestContent) {
		String localUrl = HttpUtil.getLocalUrl(request);
		flowableService.agreeWork(localUrl, processInstanceId, taskId, params, suggestContent);
		return new ResponseEntity<String>("{\"success\":true}", HttpStatus.OK);
	}

	@PostMapping("turn_back")
	public ResponseEntity<String> turnBack(@RequestParam String suggestContent,
										   HttpServletRequest request,
										   @RequestParam String processDefinitionKey,
										   @RequestParam String procInstId,
										   @RequestParam String taskId,
										   @RequestParam String nodeId) {
		log.info("turn back procInstId = [{}]", procInstId);
		String localUrl = HttpUtil.getLocalUrl(request);
		flowableService.turnBack(localUrl, suggestContent, processDefinitionKey, procInstId, taskId, nodeId);
		return new ResponseEntity<String>("{\"success\" : true}", HttpStatus.OK);
	}

	@PostMapping("finished-work")
	public Map<String, Object> finishedWork(@ModelAttribute ProcessInstanceQueryVo params, String page, String rows) {
		log.info("Get finish work");
		return flowableService.finishedWork(params, page, rows);
	}

	@PostMapping("flow_revork")
	public ResponseEntity<String> rovorkFlow(@RequestParam String processInstanceId,
											 HttpServletRequest request,
											 @RequestParam String suggestContent) {
		log.info("revork workFlow where processInstanceId = [{}]", processInstanceId);
		String localUrl = HttpUtil.getLocalUrl(request);
		flowableService.revorkFlow(localUrl, processInstanceId, suggestContent);
		return new ResponseEntity<String>("{\"success\" : true}", HttpStatus.OK);
	}

	@GetMapping("sync_flow_configuration")
	public ResponseEntity<String> syncFlowConfiguration() {
		log.info("sync flow configuration");
		flowableService.syncFlowConfiguration();
		return new ResponseEntity<String>("{\"success\" : true}", HttpStatus.OK);
	}

	@PostMapping("get_work_flow_list")
	public List<CommonComboDto> getWorkFlowList() {
		return flowableService.getWorkFlowList();
	}

	@PostMapping("get_approve_history")
	public List<ApproveHistoryDto> getApproveHistory(@RequestParam String processInstanceId) {
		log.info("Get Approve history");
		return flowableService.getApproveHistory(processInstanceId);
	}
}

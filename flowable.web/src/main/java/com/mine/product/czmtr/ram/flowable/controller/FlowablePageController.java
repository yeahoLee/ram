package com.mine.product.czmtr.ram.flowable.controller;

import com.alibaba.fastjson.JSONObject;
import com.mine.base.user.dto.UserInfoDto;
import com.mine.base.user.service.IUserService;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.*;
import com.mine.product.czmtr.ram.asset.service.*;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.mine.product.czmtr.ram.flowable.dto.ApproveFinishedDto;
import com.mine.product.czmtr.ram.flowable.dto.ApproveToDoDto;
import com.mine.product.czmtr.ram.flowable.dto.WorksDto;
import com.mine.product.czmtr.ram.flowable.service.IFlowableService;
import com.mine.product.czmtr.ram.flowable.utils.HttpUtil;
import com.mine.product.flowable.api.constant.ReturnCode;
import com.mine.product.flowable.api.dto.ProcessInstanceQueryVo;
import com.mine.product.flowable.api.dto.TaskQueryVo;
import com.mine.product.flowable.api.dto.common.ReturnPageListVo;
import com.mine.product.flowable.api.dto.common.ReturnVo;
import com.mine.product.flowable.api.dto.ret.ProcessInstanceVo;
import com.mine.product.flowable.api.dto.ret.TaskVo;
import com.mine.product.flowable.api.service.IApiFlowableProcessInstanceService;
import com.mine.product.flowable.api.service.IApiFlowableTaskService;
import com.vgtech.platform.common.utility.MineSecureUtility;
import com.vgtech.platform.common.utility.SpringSecureUserInfo;
import com.vgtech.platform.common.utility.VGUtility;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;

/**
 * @author : yeaho_lee
 * @Description : TODO
 * @createTime : 2020年07月01日 17:28
 */
@Controller
@Slf4j
public class FlowablePageController {

	@Autowired
	private IApiFlowableProcessInstanceService iApiFlowableProcessInstanceService;

	@Autowired
	private IFlowableService flowableService;

	@Autowired
	private IMaterialReceiptService materialReceiptService;

	@Autowired
	private IAssetAllocationService assetAllocationService;

	@Autowired
	private IAssetReceiveRevertService assetReceiveRevertService;

	@Autowired
	private IAssetReceiveUseService assetReceiveUseService;

	@Autowired
	private IAssetBorrowService assetBorrowService;

	@Autowired
	private IAssetRevertService assetRevertService;

	@Autowired
	private IAssetChangeService assetChangeService;

	@Autowired
	private IAssetSequestrationService assetSequestrationService;

	@Autowired
	private IAssetReduceService assetReduceService;

	@Autowired
	private IAssetInventoryService assetInventoryService;

	@Autowired
	private IApiFlowableTaskService apiFlowableTaskService;

	@Autowired
	private IUserService userService;

	@Autowired
	private IBaseService baseService;

	@GetMapping("todo_work")
	public String goTodoWorkPage(ModelMap modelMap) {
		log.info("Go into Todo Work Page");
		return "flowable/platform/todo-work";
	}

	@GetMapping("finished_work")
	public String goFinishedWorkPage() {
		log.info("Go into Finished Work Page");
		return "flowable/platform/finished-work";
	}

	@GetMapping("flowable_manage")
	public String goFlowableManagePage() {
		log.info("Go into Flowable Manage Page");
		return "flowable/workflow/workflow-list";
	}

	@GetMapping("user_group_manage")
	public String goUserGroupManagePage() {
		log.info("Go into User Group Manage Page");
		return "flowable/workflow/userGroupManage";
	}

	/**
	 * 流程图
	 * @param processInstanceId: 流程实例Id
	 * @param modelMap:
	 * @param response:
	 * @return java.lang.String
	 */
	@GetMapping("workflow-image")
	public String workflowImage(@RequestParam String processInstanceId, ModelMap modelMap, HttpServletResponse response) {
		log.info("Enter workflow image page");
		ReturnVo<ProcessInstanceVo> processInstanceVoReturnVo = iApiFlowableProcessInstanceService.getByProcessInstanceId(processInstanceId);
		if (ReturnCode.SUCCESS.equals(processInstanceVoReturnVo.getCode())) {
			modelMap.addAttribute("workFlowImageName", processInstanceVoReturnVo.getData().getFormName());
		}
		modelMap.addAttribute("workFlowImageUrl", "flowable/getWorkflowImage?processInstanceId="+processInstanceId);
		return "flowable/workflow/workflow-image";
	}

	/**
	 * 待办流程详情
	 * @return java.lang.String
	 */
	@GetMapping("/work-details")
	private String workDetails(@RequestParam String taskId, @RequestParam String processInstanceId, ModelMap modelMap) {
		log.info("Enter todo work details page");
		modelMap.addAttribute("processInstanceId", processInstanceId);
		modelMap.addAttribute("taskId", taskId);
		WorksDto worksModelByProcessInstanceId = flowableService.getWorksModelByProcessInstanceId(processInstanceId);
		String workOrderType = worksModelByProcessInstanceId.getWorkOrderType();
		String businessKey = worksModelByProcessInstanceId.getBusinessKey();
		String approveCode = worksModelByProcessInstanceId.getApprovalNumber();
		String processDefkey = worksModelByProcessInstanceId.getWorkOrderType();

		//获取流程基本信息
		TaskQueryVo params = new TaskQueryVo();
		UserInfoDto userInfo = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
		UserInfoDto userInfo1 = userService.getUserInfo(userInfo.getId());
		String emp_no = userInfo1.getPropertyMap().get("EMP_NO").toString();
		params.setUserCode(emp_no);
		params.setTaskId(taskId);
		ReturnVo<ReturnPageListVo<TaskVo>> todoWork = apiFlowableTaskService.getTodoWork(params, "1", "100");
		ReturnPageListVo<TaskVo> data = todoWork.getData();
		TaskVo taskVo = data.getRows().get(0);
		ApproveToDoDto approveToDoDto = new ApproveToDoDto();
		List<UserInfoDto> rowData = userService.getUserInfo("{$and:[{sysMark:'" + IBaseService.SYSMARK + "'},{'propertyMap.EMP_NO':'" + taskVo.getProcessInstanceStarterId() + "'}]}", null, null).getRowData();
		String processInstanceStarter = rowData.get(0).getChsName();
		taskVo.setProcessInstanceStarter(processInstanceStarter);
		String previousNodeSendUserId = taskVo.getPreviousNodeSendUserId();
		String[] split = previousNodeSendUserId.split(",");
		String previousNodeSendUserName = "";
		for (int i = 0; i < split.length; i++) {
			List<UserInfoDto> rowData2 = userService.getUserInfo("{$and:[{sysMark:'" + IBaseService.SYSMARK + "'},{'propertyMap.EMP_NO':'" + split[i] + "'}]}", null, null).getRowData();
			if (!VGUtility.isEmpty(rowData2) && rowData2.size() > 0) {
				previousNodeSendUserName += (rowData2.get(0).getChsName() + ",");
			}
		}
		if (!VGUtility.isEmpty(previousNodeSendUserName)) {
			taskVo.setPreviousNodeSendUserName(previousNodeSendUserName.substring(0, previousNodeSendUserName.length() - 1));
		}
		BeanUtils.copyProperties(taskVo, approveToDoDto);
		WorksDto worksModelByBusinessKey = flowableService.getWorksModelByBusinessKey(businessKey);
		approveToDoDto.setApproveNumber(worksModelByBusinessKey.getApprovalNumber());
		approveToDoDto.setStartTimeStr(VGUtility.toDateStr(taskVo.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
		approveToDoDto.setProcessInstanceStartTimeStr(VGUtility.toDateStr(taskVo.getProcessInstanceStartTime(), "yyyy-MM-dd HH:mm:ss"));
		modelMap.addAttribute("approveToDoDto", approveToDoDto);

		// 获取当前任务
		ReturnVo<TaskVo> taskById = apiFlowableTaskService.getTaskById(taskId);
		TaskVo data1 = taskById.getData();
		String nodeId = data1.getNodeId();
		if (Objects.equals(nodeId, FlowableInfo.FINANCE_DEPT_MANAGER_2)) {
			modelMap.addAttribute("read", true);
		}

		if ((Objects.equals(nodeId, FlowableInfo.PROCESS_PRODUCTIVE_ASSET_ADMIN) || Objects.equals(nodeId, FlowableInfo.PROCESS_NO_PRODUCTIVE_ASSET_ADMIN)) && (processDefkey.equals(FlowableInfo.ASSETS_USE_RETURN_CENTER) || processDefkey.equals(FlowableInfo.ASSETS_USE_RETURN_DEPT) || processDefkey.equals(FlowableInfo.ASSETS_USE) || processDefkey.equals(FlowableInfo.ASSETS_BORROW_CENTER) || processDefkey.equals(FlowableInfo.ASSETS_BORROW_DEPT))) {
			modelMap.addAttribute("read", true);
		}

		if (Objects.equals(nodeId, FlowableInfo.FLOW_SUBMITTER_ID)) {
			modelMap.addAttribute("draft", true);
		}

		//跳转到对应流程的详情页面
		switch (workOrderType) {
			case FlowableInfo.ASSETS_ADD :
				MaterialReceiptDto dto = materialReceiptService.getMaterialReceiptById(businessKey);
				modelMap.addAttribute("receiptDto", dto);
				return "flowable/platform/detail/addApproveDetail";
			case FlowableInfo.ASSETS_TRANSFER :
				AssetAllocationDto assetAllocationDtoById = assetAllocationService.getAssetAllocationDtoByCode(approveCode);
				modelMap.addAttribute("AssetAllocationDto", assetAllocationDtoById);
				return "flowable/platform/detail/allocationApproveDetail";
			case FlowableInfo.ASSETS_USE_RETURN_DEPT:
				List<AssetReceiveRevertDto> assetReceiveRevertDto = (List<AssetReceiveRevertDto>) assetReceiveRevertService.getAssetReceiveRevertDtoByQuerysForDataGrid(new ISearchExpression() {
					@Override
					public Object change(Object... arg0) {
						CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
						Root root = (Root) arg0[0];
						Predicate finalPred = null;
						finalPred = builder.equal(root.get("assetReceiveRevertCode"), approveCode);
						return finalPred;
					}
				}, null).get("rows");
				if (assetReceiveRevertDto.size() > 0) {
					modelMap.addAttribute("AssetReceiveRevertDto", assetReceiveRevertDto.get(0));
				}
				return "flowable/platform/detail/receiveRevertApproveDetail";
			case FlowableInfo.ASSETS_USE_RETURN_CENTER:
				List<AssetReceiveRevertDto> assetReceiveRevertDto1 = (List<AssetReceiveRevertDto>) assetReceiveRevertService.getAssetReceiveRevertDtoByQuerysForDataGrid(new ISearchExpression() {
					@Override
					public Object change(Object... arg0) {
						CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
						Root root = (Root) arg0[0];
						Predicate finalPred = null;
						finalPred = builder.equal(root.get("assetReceiveRevertCode"), approveCode);
						return finalPred;
					}
				}, null).get("rows");
				if (assetReceiveRevertDto1.size() > 0) {
					modelMap.addAttribute("AssetReceiveRevertDto", assetReceiveRevertDto1.get(0));
				}
				return "flowable/platform/detail/receiveRevertApproveDetail";
			case FlowableInfo.ASSETS_USE :
				List<AssetReceiveUseDto> assetReceiveUseDto = (List<AssetReceiveUseDto>) assetReceiveUseService.getAssetReceiveUseDtoByQuerysForDataGrid(new ISearchExpression() {
					@Override
					public Object change(Object... arg0) {
						CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
						Root root = (Root) arg0[0];
						Predicate finalPred = null;
						finalPred = builder.equal(root.get("assetReceiveUseCode"), approveCode);

						return finalPred;
					}
				}, null).get("rows");
				if (assetReceiveUseDto.size() > 0) {
					AssetReceiveUseDto assetReceiveUse = assetReceiveUseDto.get(0);
					assetReceiveUse.setAssetReceiveUseUserName(userService.getUserInfo(assetReceiveUse.getAssetReceiveUseUserID()).getChsName());
					assetReceiveUse.setAssetReceiveUseDepartmentName(userService.getDeptInfo(assetReceiveUse.getAssetReceiveUseDepartmentID()).getDeptName());
					assetReceiveUse.setCreateUserName(userService.getUserInfo(assetReceiveUse.getCreateUserID()).getChsName());
					modelMap.addAttribute("AssetReceiveUseDto", assetReceiveUse);
				}
				return "flowable/platform/detail/receiveUseApproveDetail";

			case FlowableInfo.ASSETS_BORROW_DEPT :
				List<AssetBorrowDto> assetBorrowDto = (List<AssetBorrowDto>) assetBorrowService.getAssetBorrowDtoByQuerysForDataGrid(new ISearchExpression() {
					@Override
					public Object change(Object... arg0) {
						CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
						Root root = (Root) arg0[0];
						Predicate finalPred = null;
						finalPred = builder.equal(root.get("assetborrowCode"), approveCode);

						return finalPred;
					}
				}, null).get("rows");
				if (assetBorrowDto.size() > 0) {
					AssetBorrowDto assetBorrow = assetBorrowDto.get(0);
					assetBorrow.setAssetborrowUserName(userService.getUserInfo(assetBorrow.getAssetborrowUserID()).getChsName());
					assetBorrow.setAssetborrowDepartmentName(userService.getDeptInfo(assetBorrow.getAssetborrowDepartmentID()).getDeptName());
					assetBorrow.setCreateUserName(userService.getUserInfo(assetBorrow.getCreateUserID()).getChsName());
					modelMap.addAttribute("AssetBorrowDto", assetBorrow);
				}
				return "flowable/platform/detail/borrowApproveDetail";

			case FlowableInfo.ASSETS_BORROW_CENTER :
				List<AssetBorrowDto> assetBorrowDto1 = (List<AssetBorrowDto>) assetBorrowService.getAssetBorrowDtoByQuerysForDataGrid(new ISearchExpression() {
					@Override
					public Object change(Object... arg0) {
						CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
						Root root = (Root) arg0[0];
						Predicate finalPred = null;
						finalPred = builder.equal(root.get("assetborrowCode"), approveCode);

						return finalPred;
					}
				}, null).get("rows");
				if (assetBorrowDto1.size() > 0) {
					AssetBorrowDto assetBorrow = assetBorrowDto1.get(0);
					assetBorrow.setAssetborrowUserName(userService.getUserInfo(assetBorrow.getAssetborrowUserID()).getChsName());
					assetBorrow.setAssetborrowDepartmentName(userService.getDeptInfo(assetBorrow.getAssetborrowDepartmentID()).getDeptName());
					assetBorrow.setCreateUserName(userService.getUserInfo(assetBorrow.getCreateUserID()).getChsName());
					modelMap.addAttribute("AssetBorrowDto", assetBorrow);
				}
				return "flowable/platform/detail/borrowApproveDetail";

			case FlowableInfo.ASSETS_BORROW_RETURN :
				List<AssetRevertDto> assetRevertDto = (List<AssetRevertDto>) assetRevertService.getAssetRevertDtoByQuerysForDataGrid(new ISearchExpression() {
					@Override
					public Object change(Object... arg0) {
						CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
						Root root = (Root) arg0[0];
						Predicate finalPred = null;
						finalPred = builder.equal(root.get("assetrevertCode"), approveCode);
						return finalPred;
					}
				}, null).get("rows");
				if (assetRevertDto.size() > 0) {
					modelMap.put("AssetRevertDto", assetRevertDto.get(0));
				}
				return "flowable/platform/detail/revertApproveDetail";

			case FlowableInfo.ASSETS_USER_LOCATION_CHANGE :
				if ("0".equals(worksModelByBusinessKey.getPublicUse())) {
					modelMap.addAttribute("AssetManagerChangeDto", assetChangeService.getManagerChangeReceiptById(businessKey));
					return "flowable/platform/detail/ManagerChangeApproveDetail";
				} else if ("1".equals(worksModelByBusinessKey.getPublicUse())) {
					modelMap.addAttribute("AssetUserChangeDto", assetChangeService.getUserChangeReceiptById(businessKey));
					return "flowable/platform/detail/userChangeApproveDetail";
				} else if ("2".equals(worksModelByBusinessKey.getPublicUse())) {
					modelMap.addAttribute("AssetSavePlaceChangeDto", assetChangeService.getSavePlaceChangeReceiptById(businessKey));
					return "flowable/platform/detail/savePlaceChangeApproveDetail";
				}
			case FlowableInfo.FIXED_ASSETS_ARCHIVE_CENTER :
				if ("0".equals(worksModelByBusinessKey.getPublicUse())) {
					AssetSequestrationDto assetSequestrationDto = (AssetSequestrationDto) assetSequestrationService.getAssetSealById(businessKey);
					modelMap.addAttribute("assetSeal", assetSequestrationDto);
					return "flowable/platform/detail/sealApproveDetail";
				} else if ("1".equals(worksModelByBusinessKey.getPublicUse())) {
					AssetSequestrationDto assetSequestrationDto = (AssetSequestrationDto) assetSequestrationService.getAssetSealById(businessKey);
					modelMap.addAttribute("assetSeal", assetSequestrationDto);
					return "flowable/platform/detail/unSealApproveDetail";
				}
			case FlowableInfo.ASSETS_IMPAIRMENT :
				modelMap.addAttribute("AssetReduceDto", assetReduceService.getReduceReceiptById(businessKey));
				return "flowable/platform/detail/reduceApproveDetail";

			case FlowableInfo.ASSETS_INVENTORY_TASK:
				AssetInventoryDto assetInventoryDto = assetInventoryService.getAssetInventoryById(businessKey);
				modelMap.put("assetInventoryDto", assetInventoryDto);
				return "flowable/platform/detail/assetInventoryApproveDetail";
			case FlowableInfo.ASSETS_INVENTORY_RESULT:
				MyAssetInventoryDto myAssetInventoryDto1 = new MyAssetInventoryDto();
				myAssetInventoryDto1.setId(businessKey);
				MyAssetInventoryDto myAssetInventoryDto = assetInventoryService.getMyAssetInventoryDtoById(myAssetInventoryDto1);
				AssetInventoryDto assetInventoryDto2 = assetInventoryService.getAssetInventoryById(myAssetInventoryDto.getAssetInventoryId());
				modelMap.put("assetInventoryDto", assetInventoryDto2);
				modelMap.put("myAssetInventoryDto", myAssetInventoryDto);
				return "flowable/platform/detail/myAssetInventoryApproveDetail";
			default:
				throw new RuntimeException("流程实例不存在！");

		}
	}

	/**
	 * 已办流程详情
	 * @return java.lang.String
	 */
	@GetMapping("/finished_detail")
	private String finidhedDetail(@RequestParam String processInstanceId, ModelMap modelMap) {
		log.info("Enter Finished Work details page");

		UserInfoDto userInfo = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
		UserInfoDto userInfo1 = userService.getUserInfo(userInfo.getId());
		String emp_no = userInfo1.getPropertyMap().get("EMP_NO").toString();

		modelMap.addAttribute("processInstanceId", processInstanceId);
		WorksDto worksModelByProcessInstanceId = flowableService.getWorksModelByProcessInstanceId(processInstanceId);
		String workOrderType = worksModelByProcessInstanceId.getWorkOrderType();
		String businessKey = worksModelByProcessInstanceId.getBusinessKey();
		String approveCode = worksModelByProcessInstanceId.getApprovalNumber();

		WorksDto worksModelByBusinessKey = flowableService.getWorksModelByBusinessKey(businessKey);

		//获取流程基本信息
		ProcessInstanceQueryVo params = new ProcessInstanceQueryVo();
		params.setProcessInstanceId(processInstanceId);
		params.setUserCode(emp_no);

		ReturnVo<ReturnPageListVo<ProcessInstanceVo>> finishedWork = iApiFlowableProcessInstanceService.getMyApprovedProcessInstances(params, "1", "10");
		ReturnPageListVo<ProcessInstanceVo> data1 = finishedWork.getData();
		List<ProcessInstanceVo> rows = data1.getRows();
		if (rows.size() > 0) {
			ProcessInstanceVo processInstanceVo = rows.get(0);
			ApproveFinishedDto approveFinishedDto = new ApproveFinishedDto();
			List<UserInfoDto> rowData = userService.getUserInfo("{$and:[{sysMark:'" + IBaseService.SYSMARK + "'},{'propertyMap.EMP_NO':'" + processInstanceVo.getStarterId() + "'}]}", null, null).getRowData();
			String processInstanceStarter = rowData.get(0).getChsName();
			processInstanceVo.setStarter(processInstanceStarter);
			BeanUtils.copyProperties(processInstanceVo, approveFinishedDto);
			approveFinishedDto.setProcessInstanceStartTimeStr(VGUtility.toDateStr(approveFinishedDto.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
			approveFinishedDto.setApproveNumber(worksModelByBusinessKey.getApprovalNumber());

			//获取当前环节，当前环节处理人
			List<TaskVo> runningTasks = processInstanceVo.getRunningTasks();
			String runningProcesses = "";
			String runningProcessUsers = "";
			if (!VGUtility.isEmpty(runningTasks)) {
				for (int i = 0; i < runningTasks.size(); i++) {
					String taskName = runningTasks.get(i).getTaskName();
					if (VGUtility.isEmpty(runningProcesses)) {
						runningProcesses = taskName;
					} else {
						runningProcesses = runningProcesses + "," + taskName;
					}

					String approverId = runningTasks.get(i).getApproverId();
					List<UserInfoDto> rowData1 = userService.getUserInfo("{$and:[{sysMark:'" + IBaseService.SYSMARK + "'},{'propertyMap.EMP_NO':'" + approverId + "'}]}", null, null).getRowData();
					String approver = rowData1.get(0).getChsName();
					if (VGUtility.isEmpty(runningProcessUsers)) {
						runningProcessUsers = approver;
					} else {
						runningProcessUsers = runningProcessUsers + "," + approver;
					}
				}
			}
			approveFinishedDto.setRunningProcesses(runningProcesses);
			approveFinishedDto.setRunningProcessUsers(runningProcessUsers);
			modelMap.addAttribute("approveFinishedDto", approveFinishedDto);


			//撤回按钮条件判定
			String starterId = approveFinishedDto.getStarterId();
			//判断状态是否为已完成
			if (Objects.equals(emp_no, starterId) && FlowableInfo.WORKSTATUS.已审批 != worksModelByBusinessKey.getWorkStatus() &&  FlowableInfo.WORKSTATUS.驳回 != worksModelByBusinessKey.getWorkStatus()) {
				modelMap.addAttribute("revoke", true);
			}
		}

		//跳转到对应流程的详情页面
		switch (workOrderType) {
			case FlowableInfo.ASSETS_ADD:
				MaterialReceiptDto dto = materialReceiptService.getMaterialReceiptById(businessKey);
				modelMap.addAttribute("receiptDto", dto);
				return "flowable/platform/detail/addApproveDetail";
			case FlowableInfo.ASSETS_TRANSFER:
				AssetAllocationDto assetAllocationDtoById = assetAllocationService.getAssetAllocationDtoByCode(approveCode);
				modelMap.addAttribute("AssetAllocationDto", assetAllocationDtoById);
				return "flowable/platform/detail/allocationApproveDetail";
			case FlowableInfo.ASSETS_USE_RETURN_DEPT:
				List<AssetReceiveRevertDto> assetReceiveRevertDto = (List<AssetReceiveRevertDto>) assetReceiveRevertService.getAssetReceiveRevertDtoByQuerysForDataGrid(new ISearchExpression() {
					@Override
					public Object change(Object... arg0) {
						CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
						Root root = (Root) arg0[0];
						Predicate finalPred = null;
						finalPred = builder.equal(root.get("assetReceiveRevertCode"), approveCode);
						return finalPred;
					}
				}, null).get("rows");
				if (assetReceiveRevertDto.size() > 0) {
					modelMap.addAttribute("AssetReceiveRevertDto", assetReceiveRevertDto.get(0));
				}
				return "flowable/platform/detail/receiveRevertApproveDetail";
			case FlowableInfo.ASSETS_USE_RETURN_CENTER:
				List<AssetReceiveRevertDto> assetReceiveRevertDto1 = (List<AssetReceiveRevertDto>) assetReceiveRevertService.getAssetReceiveRevertDtoByQuerysForDataGrid(new ISearchExpression() {
					@Override
					public Object change(Object... arg0) {
						CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
						Root root = (Root) arg0[0];
						Predicate finalPred = null;
						finalPred = builder.equal(root.get("assetReceiveRevertCode"), approveCode);
						return finalPred;
					}
				}, null).get("rows");
				if (assetReceiveRevertDto1.size() > 0) {
					modelMap.addAttribute("AssetReceiveRevertDto", assetReceiveRevertDto1.get(0));
				}
				return "flowable/platform/detail/receiveRevertApproveDetail";
			case FlowableInfo.ASSETS_USE:
				List<AssetReceiveUseDto> assetReceiveUseDto = (List<AssetReceiveUseDto>) assetReceiveUseService.getAssetReceiveUseDtoByQuerysForDataGrid(new ISearchExpression() {
					@Override
					public Object change(Object... arg0) {
						CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
						Root root = (Root) arg0[0];
						Predicate finalPred = null;
						finalPred = builder.equal(root.get("assetReceiveUseCode"), approveCode);

						return finalPred;
					}
				}, null).get("rows");
				if (assetReceiveUseDto.size() > 0) {
					AssetReceiveUseDto assetReceiveUse = assetReceiveUseDto.get(0);
					assetReceiveUse.setAssetReceiveUseUserName(userService.getUserInfo(assetReceiveUse.getAssetReceiveUseUserID()).getChsName());
					assetReceiveUse.setAssetReceiveUseDepartmentName(userService.getDeptInfo(assetReceiveUse.getAssetReceiveUseDepartmentID()).getDeptName());
					assetReceiveUse.setCreateUserName(userService.getUserInfo(assetReceiveUse.getCreateUserID()).getChsName());
					modelMap.addAttribute("AssetReceiveUseDto", assetReceiveUse);
				}
				return "flowable/platform/detail/receiveUseApproveDetail";

			case FlowableInfo.ASSETS_BORROW_DEPT:
				List<AssetBorrowDto> assetBorrowDto = (List<AssetBorrowDto>) assetBorrowService.getAssetBorrowDtoByQuerysForDataGrid(new ISearchExpression() {
					@Override
					public Object change(Object... arg0) {
						CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
						Root root = (Root) arg0[0];
						Predicate finalPred = null;
						finalPred = builder.equal(root.get("assetborrowCode"), approveCode);

						return finalPred;
					}
				}, null).get("rows");
				if (assetBorrowDto.size() > 0) {
					AssetBorrowDto assetBorrow = assetBorrowDto.get(0);
					assetBorrow.setAssetborrowUserName(userService.getUserInfo(assetBorrow.getAssetborrowUserID()).getChsName());
					assetBorrow.setAssetborrowDepartmentName(userService.getDeptInfo(assetBorrow.getAssetborrowDepartmentID()).getDeptName());
					assetBorrow.setCreateUserName(userService.getUserInfo(assetBorrow.getCreateUserID()).getChsName());
					modelMap.addAttribute("AssetBorrowDto", assetBorrow);
				}
				return "flowable/platform/detail/borrowApproveDetail";

			case FlowableInfo.ASSETS_BORROW_CENTER:
				List<AssetBorrowDto> assetBorrowDto1 = (List<AssetBorrowDto>) assetBorrowService.getAssetBorrowDtoByQuerysForDataGrid(new ISearchExpression() {
					@Override
					public Object change(Object... arg0) {
						CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
						Root root = (Root) arg0[0];
						Predicate finalPred = null;
						finalPred = builder.equal(root.get("assetborrowCode"), approveCode);

						return finalPred;
					}
				}, null).get("rows");
				if (assetBorrowDto1.size() > 0) {
					AssetBorrowDto assetBorrow = assetBorrowDto1.get(0);
					assetBorrow.setAssetborrowUserName(userService.getUserInfo(assetBorrow.getAssetborrowUserID()).getChsName());
					assetBorrow.setAssetborrowDepartmentName(userService.getDeptInfo(assetBorrow.getAssetborrowDepartmentID()).getDeptName());
					assetBorrow.setCreateUserName(userService.getUserInfo(assetBorrow.getCreateUserID()).getChsName());
					modelMap.addAttribute("AssetBorrowDto", assetBorrow);
				}
				return "flowable/platform/detail/borrowApproveDetail";

			case FlowableInfo.ASSETS_BORROW_RETURN:
				List<AssetRevertDto> assetRevertDto = (List<AssetRevertDto>) assetRevertService.getAssetRevertDtoByQuerysForDataGrid(new ISearchExpression() {
					@Override
					public Object change(Object... arg0) {
						CriteriaBuilder builder = (CriteriaBuilder) arg0[1];
						Root root = (Root) arg0[0];
						Predicate finalPred = null;
						finalPred = builder.equal(root.get("assetrevertCode"), approveCode);
						return finalPred;
					}
				}, null).get("rows");
				if (assetRevertDto.size() > 0) {
					modelMap.put("AssetRevertDto", assetRevertDto.get(0));
				}
				return "flowable/platform/detail/revertApproveDetail";

			case FlowableInfo.ASSETS_USER_LOCATION_CHANGE:
				if ("0".equals(worksModelByBusinessKey.getPublicUse())) {
					modelMap.addAttribute("AssetManagerChangeDto", assetChangeService.getManagerChangeReceiptById(businessKey));
					return "flowable/platform/detail/ManagerChangeApproveDetail";
				} else if ("1".equals(worksModelByBusinessKey.getPublicUse())) {
					modelMap.addAttribute("AssetUserChangeDto", assetChangeService.getUserChangeReceiptById(businessKey));
					return "flowable/platform/detail/userChangeApproveDetail";
				} else if ("2".equals(worksModelByBusinessKey.getPublicUse())) {
					modelMap.addAttribute("AssetSavePlaceChangeDto", assetChangeService.getSavePlaceChangeReceiptById(businessKey));
					return "flowable/platform/detail/savePlaceChangeApproveDetail";
				}
			case FlowableInfo.FIXED_ASSETS_ARCHIVE_CENTER:
				if ("0".equals(worksModelByBusinessKey.getPublicUse())) {
					AssetSequestrationDto assetSequestrationDto = (AssetSequestrationDto) assetSequestrationService.getAssetSealById(businessKey);
					modelMap.addAttribute("assetSeal", assetSequestrationDto);
					return "flowable/platform/detail/sealApproveDetail";
				} else if ("1".equals(worksModelByBusinessKey.getPublicUse())) {
					AssetSequestrationDto assetSequestrationDto = (AssetSequestrationDto) assetSequestrationService.getAssetSealById(businessKey);
					modelMap.addAttribute("assetSeal", assetSequestrationDto);
					return "flowable/platform/detail/unSealApproveDetail";
				}
			case FlowableInfo.ASSETS_IMPAIRMENT:
				modelMap.addAttribute("AssetReduceDto", assetReduceService.getReduceReceiptById(businessKey));
				return "flowable/platform/detail/reduceApproveDetail";

			case FlowableInfo.ASSETS_INVENTORY_TASK:
				AssetInventoryDto assetInventoryDto = assetInventoryService.getAssetInventoryById(businessKey);
				modelMap.put("assetInventoryDto", assetInventoryDto);
				return "flowable/platform/detail/assetInventoryApproveDetail";
			case FlowableInfo.ASSETS_INVENTORY_RESULT:
				MyAssetInventoryDto myAssetInventoryDto1 = new MyAssetInventoryDto();
				myAssetInventoryDto1.setId(businessKey);
				MyAssetInventoryDto myAssetInventoryDto = assetInventoryService.getMyAssetInventoryDtoById(myAssetInventoryDto1);
				AssetInventoryDto assetInventoryDto2 = assetInventoryService.getAssetInventoryById(myAssetInventoryDto.getAssetInventoryId());
				modelMap.put("assetInventoryDto", assetInventoryDto2);
				modelMap.put("myAssetInventoryDto", myAssetInventoryDto);
				return "flowable/platform/detail/myAssetInventoryApproveDetail";
			default:
				throw new RuntimeException("流程实例不存在！");
		}
	}

	@GetMapping("confirm_common")
	public String goConfirmCommon() {
		log.info("Go into Confirm Common Page");
		return "flowable/common/confirmCommon";
	}

	@GetMapping("view_work_flow")
	public String flowablePicPage(String imgName, ModelMap modelMap) {
		log.info("Go page where imgName = [{}]", imgName);
		modelMap.addAttribute("imgName", imgName);
		return "flowable/workflow/flowablePic";
	}


	/**
	 * 代办消息  统一认证登陆入口
	 * @param request
	 * @param response
	 */
	@GetMapping(value = "/login_out_link")
	public void infoLocalOAuthLogin(HttpServletRequest request, HttpServletResponse response)  {
		try {
			String param = "";
			String processInstanceId = request.getParameter("processInstanceId");
			String taskId = request.getParameter("taskId");
			if (!VGUtility.isEmpty(processInstanceId)) {
				param += ("processInstanceId=" + processInstanceId);
			}

			if (!VGUtility.isEmpty(taskId)) {
				param += (param + "&taskId=" + taskId);
			}

			String redirect_uri = URLEncoder.encode(this.getLocalUrl(request)+"/out_link?"+param,"utf-8");
			log.info("redirect_uri++++++++++++++++++："+redirect_uri);
			String repage = IBaseService.uamsbrowserUrl + "/oauth/authorize?response_type=code&client_id=" + IBaseService.client_id + "&scope=all&redirect_uri=" + redirect_uri;
			log.info("页面跳转："+repage);
			response.sendRedirect(repage);
		}catch (Exception e){
			throw new RuntimeException("登陆失败!");
		}
	}


	/**
	 * 代办消息统一认证登陆
	 * @param code
	 * @return
	 */
	@GetMapping(value = "/out_link")
	public String InfoOAuthLogin(String code, HttpServletRequest request, ModelMap modelMap)   {
		String mobile ="";
		try {
			//根据 code 获取token
			String param = "key="+request.getParameter("key")+"&lineId="+request.getParameter("lineId");
			String redirect_uri = URLEncoder.encode(this.getLocalUrl(request)+"/out_link?"+param,"utf-8");
			log.info("redirect_uri++++++++++++++++++："+redirect_uri);
			String url = IBaseService.uamsbrowserUrl + "/oauth/token?grant_type=authorization_code&code="+code+"&redirect_uri="+redirect_uri;
			//base64 编译
			final Base64 base64 = new Base64();
			final String text = (IBaseService.client_id + ":" + IBaseService.client_secret);
			final byte[] textByte = text.getBytes("UTF-8");
			String authorization = base64.encodeToString(textByte);

			authorization="Basic" + " " +authorization;
			//发送http请求
			log.info("token: "+url+" authorization:"+authorization);
			String result = HttpUtil.sendGet(url,authorization);
			JSONObject obj = JSONObject.parseObject(result);
			String accessToken=obj.get("access_token").toString().replace("\"","");
			//根据token获取用户信息
			String infoUrl = IBaseService.uamsAdminUrl + "/resource/user/me";
			String infoAuthorization="bearer" + " " +accessToken;
			//发送http请求
			String infoResult = HttpUtil.sendGet(infoUrl,infoAuthorization);
			JSONObject infoObj = JSONObject.parseObject(infoResult);
			JSONObject user = JSONObject.parseObject(infoObj.get("data").toString());
			String userName = user.getString("mobile");
			UserInfoDto userInfoDto = baseService.getUserInfoDtoByUserName(userName);
			String processInstanceId = request.getParameter("processInstanceId");
			String taskId = request.getParameter("taskId");
			String returnUrl = "";
			if (!VGUtility.isEmpty(taskId)) {
				returnUrl = this.getLocalUrl(request)+"/work-details?processInstanceId=" + processInstanceId +"&taskId=" + taskId;
			} else {
				returnUrl = this.getLocalUrl(request)+"/finished_detail?processInstanceId=" + processInstanceId;
			}
			return "<html><head></head><body onload=\"document.Form1.submit()\"><form name='Form1' method='post' action='/ram/login'><input name=\"username\" type=\"hidden\" value=\"" + userInfoDto.getUserName() + "\"><input name=\"password\" type=\"hidden\" value=\"" + userInfoDto.getPassword() + "\"><input name=\"workDetailUrl\" type=\"hidden\" value=\"" + returnUrl + "\"></form></body></html>";
		}catch(Exception e) {
			log.info("" + e.getMessage());
			return "redirect:/local_oauth_login";
		}
	}

	/**
	 * 获取当前url,如http://localhost:8080/ram
	 * @param request
	 * @return
	 */
	private String getLocalUrl(HttpServletRequest request){
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}

}

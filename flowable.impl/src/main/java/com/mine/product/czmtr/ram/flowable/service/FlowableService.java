package com.mine.product.czmtr.ram.flowable.service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mine.base.user.dto.DeptInfoDto;
import com.mine.base.user.dto.UserInfoDto;
import com.mine.base.user.service.IUserService;
import com.mine.platform.common.dto.CommonComboDto;
import com.mine.platform.common.dto.PageDto;
import com.mine.platform.common.util.ISearchExpression;
import com.mine.product.czmtr.ram.asset.dto.*;
import com.mine.product.czmtr.ram.asset.service.IApproveNotify;
import com.mine.product.czmtr.ram.asset.service.IAssetBorrowService;
import com.mine.product.czmtr.ram.asset.service.IAssetRevertService;
import com.mine.product.czmtr.ram.asset.service.IAssetService;
import com.mine.product.czmtr.ram.base.service.IBaseService;
import com.mine.product.czmtr.ram.base.utils.SpringContextUtil;
import com.mine.product.czmtr.ram.flowable.dao.*;
import com.mine.product.czmtr.ram.flowable.dto.*;
import com.mine.product.czmtr.ram.flowable.model.*;
import com.mine.product.czmtr.ram.flowable.utils.RabbitMqUtil;
import com.mine.product.flowable.api.constant.ReturnCode;
import com.mine.product.flowable.api.dto.*;
import com.mine.product.flowable.api.dto.common.ReturnPageListVo;
import com.mine.product.flowable.api.dto.common.ReturnVo;
import com.mine.product.flowable.api.dto.ret.*;
import com.mine.product.flowable.api.enm.CommentTypeEnum;
import com.mine.product.flowable.api.service.IApiFlowableProcessDefinitionService;
import com.mine.product.flowable.api.service.IApiFlowableProcessInstanceService;
import com.mine.product.flowable.api.service.IApiFlowableTaskService;
import com.mine.product.flowable.api.service.IApiFlowableWorkDetailService;
import com.vgtech.platform.common.utility.MineSecureUtility;
import com.vgtech.platform.common.utility.SpringSecureUserInfo;
import com.vgtech.platform.common.utility.VGUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : yeaho_lee
 * @Description : 流程管理业务层
 * @createTime : 2020年07月09日 09:19
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
@Slf4j
public class FlowableService implements IFlowableService {

    @Autowired
    private WorkFlowDao workFlowDao;

    @Autowired
    private WorkFlowUserGroupDao workFlowUserGroupDao;

    @Autowired
    private WorkFlowUserDao workFlowUserDao;

    @Autowired
    private IUserService userService;

    @Autowired
    private WorkFlowProcessDao workFlowProcessDao;

    @Autowired
    private WorksDao worksDao;

    @Autowired
    private FlowRuleDao flowRuleDao;

    @Autowired
    private IApiFlowableProcessDefinitionService iApiFlowableProcessDefinitionService;

    @Autowired
    private IApiFlowableWorkDetailService apiFlowableWorkDetailService;

    @Autowired
    private IApiFlowableProcessInstanceService iApiFlowableProcessInstanceService;

    @Autowired
    private IApiFlowableTaskService apiFlowableTaskService;

    @Autowired
    private IAssetBorrowService assetBorrowService;

    @Autowired
    private IAssetRevertService assetRevertService;

    @Autowired
    private MessageSendDao messageSendDao;

    /**
     * 常州测试消息来源系统编码
     */
    public static final String MESSAGE_SRC = "am_cs";

    /**
     * 消息目标系统
     */
    public static final String MESSAGE_TARGET = "MH";


    @Override
    public void createWorkFlow(WorkFlowDto workFlowDto) {
        WorkFlowModel byFlowKey = workFlowDao.findByFlowKey(workFlowDto.getFlowKey());
        if (!VGUtility.isEmpty(byFlowKey)) {
            throw new RuntimeException("该流程已存在！");
        }

        WorkFlowModel workFlowModel = new WorkFlowModel();
        BeanUtils.copyProperties(workFlowDto, workFlowModel);
        if (!VGUtility.isEmpty(workFlowDto.getId())) {
            Optional<WorkFlowModel> byId = workFlowDao.findById(workFlowDto.getId());
            byId.ifPresent(flowModel -> workFlowModel.setCreateTimestamp(flowModel.getCreateTimestamp()));
        }
        workFlowDao.save(workFlowModel);
    }

    @Override
    public Map<String, Object> getWorkFlowList(PageRequest pageRequest, String q) {
        Map<String, Object> resultMap = new HashMap<>();
        Specification<WorkFlowModel> specification = new Specification<WorkFlowModel>() {
            @Override
            public Predicate toPredicate(Root<WorkFlowModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicate = new ArrayList<>();
                predicate.add(criteriaBuilder.like(root.<String>get("flowName"), "%" + q + "%"));
                Predicate[] pre = new Predicate[predicate.size()];
                criteriaQuery.where(predicate.toArray(pre));
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createTimestamp")));
                return criteriaQuery.getRestriction();
            }
        };

        Page<WorkFlowModel> all = workFlowDao.findAll(specification, pageRequest);
        List<WorkFlowDto> collect = all.get().map(model -> {
            WorkFlowDto workFlowDto = new WorkFlowDto();
            BeanUtils.copyProperties(model, workFlowDto);
            workFlowDto.setCreateTimestampStr(VGUtility.toDateStr(model.getCreateTimestamp(), "yyyy-MM-dd HH:mm:ss"));
            workFlowDto.setLastUpdateTimestampStr(VGUtility.toDateStr(model.getLastUpdateTimestamp(), "yyyy-MM-dd HH:mm:ss"));
            return workFlowDto;
        }).collect(Collectors.toList());
        resultMap.put("total", all.getTotalElements());
        resultMap.put("rows", collect);
        return resultMap;
    }

    @Override
    public void deleteWorkFlow(String id) {
        workFlowDao.deleteById(id);
        //删除所有关联用户环节
        workFlowProcessDao.deleteAllByFlowId(id);
    }

    @Override
    public void createUserGroup(WorkFlowUserGroupDto workFlowUserGroupDto) {
        WorkFlowUserGroupModel byGroupCode = workFlowUserGroupDao.findByGroupCode(workFlowUserGroupDto.getGroupCode());
        WorkFlowUserGroupModel workFlowUserGroupModel = new WorkFlowUserGroupModel();
        BeanUtils.copyProperties(workFlowUserGroupDto, workFlowUserGroupModel);
        if (!VGUtility.isEmpty(workFlowUserGroupDto.getId())) {
            Optional<WorkFlowUserGroupModel> byId = workFlowUserGroupDao.findById(workFlowUserGroupDto.getId());
            byId.ifPresent(model -> {
                if (!VGUtility.isEmpty(byGroupCode) && !model.getGroupCode().equals(byGroupCode.getGroupCode())) {
                    throw new RuntimeException("编码不能重复！");
                }
                workFlowUserGroupModel.setCreateTimestamp(model.getCreateTimestamp());
            });
        } else {
            if (!VGUtility.isEmpty(byGroupCode)) {
                throw new RuntimeException("编码不能重复！");
            }
        }
        workFlowUserGroupDao.save(workFlowUserGroupModel);
    }

    @Override
    public List<WorkFlowUserGroupDto> getUserGroupList(String workFlowId, String q) {
        Specification<WorkFlowUserGroupModel> specification = new Specification<WorkFlowUserGroupModel>() {
            @Override
            public Predicate toPredicate(Root<WorkFlowUserGroupModel> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicate = new ArrayList<>();
                predicate.add(criteriaBuilder.like(root.<String>get("groupName"), "%" + q + "%"));
                if (!VGUtility.isEmpty(workFlowId)) {
                    predicate.add(criteriaBuilder.equal(root.<String>get("flowId"), workFlowId));
                }
                Predicate[] pre = new Predicate[predicate.size()];
                criteriaQuery.where(predicate.toArray(pre));
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createTimestamp")));
                return criteriaQuery.getRestriction();
            }
        };

        List<WorkFlowUserGroupModel> all = workFlowUserGroupDao.findAll(specification);
        List<WorkFlowUserGroupDto> collect = all.stream().map(model -> {
            WorkFlowUserGroupDto workFlowUserGroupDto = new WorkFlowUserGroupDto();
            BeanUtils.copyProperties(model, workFlowUserGroupDto);
            workFlowUserGroupDto.setCreateTimestampStr(VGUtility.toDateStr(model.getCreateTimestamp(), "yyyy-MM-dd HH:mm:ss"));
            return workFlowUserGroupDto;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public void deleteUserGroup(String id) {
        workFlowUserDao.deleteAllByGroupId(id);
        workFlowUserGroupDao.deleteById(id);
    }

    @Override
    public List<CommonComboDto> getGroupUserCombo() {
        List<WorkFlowUserGroupModel> allByFlowId = workFlowUserGroupDao.findAll();
        List<CommonComboDto> commonComboDtos = new ArrayList<CommonComboDto>();
        allByFlowId.stream().forEach(model -> {
            CommonComboDto commonComboDto = new CommonComboDto();
            commonComboDto.setValue(model.getId());
            commonComboDto.setText(model.getGroupCode() + " " + model.getGroupName());
            commonComboDtos.add(commonComboDto);
        });
        return commonComboDtos;
    }

    @Override
    public void createUser(WorkFlowUserDto workFlowUserDto) {
        WorkFlowUserGroupModel byGroupCode = workFlowUserGroupDao.findByGroupCode(FlowableInfo.DEPARTMENT_ADMINISTRATOR);
        WorkFlowUserGroupModel byGroupCode2 = workFlowUserGroupDao.findByGroupCode(FlowableInfo.CENTER_ADMINISTRATOR);

        if (byGroupCode.getId().equals(workFlowUserDto.getGroupId())) {
            String userCode = workFlowUserDto.getUserCode();
            WorkFlowUserModel byGroupIdAndUserId = workFlowUserDao.findByGroupIdAndUserCode(byGroupCode2.getId(), userCode);
            if (!VGUtility.isEmpty(byGroupIdAndUserId)) {
                throw new RuntimeException("部门资产管理员和中心资产管理员不能重复添加！");
            }
        }

        if (byGroupCode2.getId().equals(workFlowUserDto.getGroupId())) {
            String userCode = workFlowUserDto.getUserCode();
            WorkFlowUserModel byGroupIdAndUserId = workFlowUserDao.findByGroupIdAndUserCode(byGroupCode.getId(), userCode);
            if (!VGUtility.isEmpty(byGroupIdAndUserId)) {
                throw new RuntimeException("中心资产管理员和部门资产管理员不能重复添加！");
            }
        }

        WorkFlowUserModel workFlowUserModel = new WorkFlowUserModel();
        BeanUtils.copyProperties(workFlowUserDto, workFlowUserModel);
        workFlowUserDao.save(workFlowUserModel);
    }

    @Override
    public List<WorkFlowUserDto> getUserList(String groupId) {
        List<WorkFlowUserModel> allByGroupId = workFlowUserDao.findAllByGroupId(groupId);

        List<WorkFlowUserDto> collect = allByGroupId.stream().map(model -> {
            WorkFlowUserDto workFlowUserDto = new WorkFlowUserDto();
            BeanUtils.copyProperties(model, workFlowUserDto);
            List<UserInfoDto> rowData = userService.getUserInfo("{$and:[{sysMark:'" + IBaseService.SYSMARK + "'},{'propertyMap.EMP_NO':'" + model.getUserCode() + "'}]}", null, null).getRowData();
            workFlowUserDto.setUserName((!VGUtility.isEmpty(rowData) && rowData.size() > 0) ? rowData.get(0).getUserName() + "  " + rowData.get(0).getChsName() : "");
            return workFlowUserDto;
        }).collect(Collectors.toList());

        return collect;
    }

    @Override
    public void deleteUser(String id) {
        workFlowUserDao.deleteById(id);
    }

    @Override
    public void createProcessGroup(WorkFlowProcessDto workFlowProcessDto) {
        WorkFlowProcessModel allByFlowIdAndAndProcessKey = workFlowProcessDao.findAllByFlowIdAndAndProcessKey(workFlowProcessDto.getFlowId(), workFlowProcessDto.getProcessKey());
        if (!VGUtility.isEmpty(allByFlowIdAndAndProcessKey)) {
            allByFlowIdAndAndProcessKey.setGroupId(workFlowProcessDto.getGroupId());
            workFlowProcessDao.save(allByFlowIdAndAndProcessKey);
        } else {
            WorkFlowProcessModel workFlowProcessModel = new WorkFlowProcessModel();
            BeanUtils.copyProperties(workFlowProcessDto, workFlowProcessModel);
            workFlowProcessDao.save(workFlowProcessModel);
        }
    }

    @Override
    public void deleteProcessGroup(String id) {
        workFlowProcessDao.deleteById(id);
    }

    @Override
    public WorkFlowProcessDto getGroupIdByProcessKey(String flowId, String processKey) {
        WorkFlowProcessModel allByFlowIdAndAndProcessKey = workFlowProcessDao.findAllByFlowIdAndAndProcessKey(flowId, processKey);
        if (!VGUtility.isEmpty(allByFlowIdAndAndProcessKey)) {
            WorkFlowProcessDto workFlowProcessDto = new WorkFlowProcessDto();
            BeanUtils.copyProperties(allByFlowIdAndAndProcessKey, workFlowProcessDto);
            return workFlowProcessDto;
        }
        return null;
    }

    @Override
    public List<NextNodeUserDto> getProcessUser(String processDefinitionKey, String processLink, WorksDto worksDto) {

        UserInfoDto userInfo = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        UserInfoDto userInfo1 = userService.getUserInfo(userInfo.getId());
        String deptCode = userInfo1.getPropertyMap().get("DEPT_NO").toString();

        WorkFlowModel byFlowKey = workFlowDao.findByFlowKey(processDefinitionKey);
        if (VGUtility.isEmpty(byFlowKey)) {
            throw new RuntimeException("该流程实例不存在,请先在流程管理内维护！");
        }
        WorkFlowProcessModel allByFlowIdAndAndProcessKey = workFlowProcessDao.findAllByFlowIdAndAndProcessKey(byFlowKey.getId(), processLink);
        if (VGUtility.isEmpty(allByFlowIdAndAndProcessKey)) {
            throw new RuntimeException("请维护该环节相应的审批人员！");
        }
        List<WorkFlowUserModel> allByGroupId = workFlowUserDao.findAllByGroupId(allByFlowIdAndAndProcessKey.getGroupId());
        List<NextNodeUserDto> nextNodeUserDtos = new ArrayList<NextNodeUserDto>();
        List<NextNodeUserDto> nextNodeUserDtosResult = new ArrayList<NextNodeUserDto>();
        allByGroupId.stream().forEach(model -> {
            List<UserInfoDto> rowData = userService.getUserInfo("{$and:[{sysMark:'" + IBaseService.SYSMARK + "'},{'propertyMap.EMP_NO':'" + model.getUserCode() + "'}]}", null, null).getRowData();
            String chsName = rowData.get(0).getChsName();
            String deptCode1 = rowData.get(0).getPropertyMap().get("DEPT_NO").toString();
            PageDto<DeptInfoDto> deptInfoPage = userService.getDeptInfo("{code:'" + deptCode1 + "'}", null, null);
            DeptInfoDto deptInfo = deptInfoPage.getRowData().get(0);

            NextNodeUserDto nextNodeUserDto = new NextNodeUserDto();
            nextNodeUserDto.setUserId(model.getUserCode());
            nextNodeUserDto.setChsName(chsName);
            nextNodeUserDto.setDeptCode(deptCode1);
            nextNodeUserDto.setDeptName(deptInfo.getDeptName());
            nextNodeUserDto.setUserName(model.getUserCode());
            nextNodeUserDto.setDeptId(deptInfo.getId());
            nextNodeUserDtos.add(nextNodeUserDto);
        });

        //worksDto为null时说明处于拟稿人发起流程阶段
        if (VGUtility.isEmpty(worksDto)) {
            switch (processDefinitionKey) {
                //领用归还
                case FlowableInfo.ASSETS_USE_RETURN_DEPT:
                    nextNodeUserDtosResult = nextNodeUserDtos;
                    break;
                case FlowableInfo.ASSETS_USE_RETURN_CENTER:
                    nextNodeUserDtosResult = nextNodeUserDtos.stream().filter(result -> (result.getDeptCode().substring(0, 5).equals(deptCode.substring(0, 5)))).collect(Collectors.toList());
                    break;
                default :
                    if (FlowableInfo.AssignUserProcess.centerDirector.equals(processLink)) {
                        //TODO 判断拟稿人和审批用户是否处于一个中心
                        nextNodeUserDtosResult = nextNodeUserDtos.stream().filter(result -> (result.getDeptCode().length() >= 7 && result.getDeptCode().substring(0, 7).equals(deptCode.substring(0, 7)))).collect(Collectors.toList());
                    } else if (FlowableInfo.AssignUserProcess.deptAdmin.equals(processLink)) {
                        //TODO 判断拟稿人和审批用户是否处于一个部门
                        nextNodeUserDtosResult = nextNodeUserDtos.stream().filter(result -> (result.getDeptCode().substring(0, 5).equals(deptCode.substring(0, 5)))).collect(Collectors.toList());
                    }
                    break;
            }

        } else {
            //如果不为空worksDto不为空，说明已经发起审批
            /*String businessKey = worksDto.getBusinessKey();
            String starterId = worksDto.getStarterId();*/
            String approveCode = worksDto.getApprovalNumber();

            /**
             * @see com.mine.product.czmtr.ram.asset.dto.FlowableInfo
             */
            switch (processLink) {
                case FlowableInfo.AssignUserProcess.centerDirector:
                    //TODO 判断拟稿人和审批用户是否处于一个中心
                    nextNodeUserDtosResult = nextNodeUserDtos.stream().filter(result -> (result.getDeptCode().length() >= 7 && result.getDeptCode().substring(0, 7).equals(deptCode.substring(0, 7)))).collect(Collectors.toList());
                    break;
                case FlowableInfo.AssignUserProcess.deptAssetAdmin:
                    //TODO 判断拟稿人和审批用户是否处于一个部门
                    //result.getDeptCode().length() == 5 &&
                    nextNodeUserDtosResult = nextNodeUserDtos.stream().filter(result -> result.getDeptCode().substring(0, 5).equals(deptCode.substring(0, 5))).collect(Collectors.toList());
                    break;
                case FlowableInfo.AssignUserProcess.deptAdmin:
                    //TODO 判断拟稿人和审批用户是否处于一个部门
                    nextNodeUserDtosResult = nextNodeUserDtos.stream().filter(result -> result.getDeptCode().substring(0, 5).equals(deptCode.substring(0, 5))).collect(Collectors.toList());
                    break;
                case FlowableInfo.AssignUserProcess.borrowOutDeptAdmin:
                    String deptCode1 = this.getDepartmentId("0", approveCode);
                    nextNodeUserDtosResult = nextNodeUserDtos.stream().filter(result -> result.getDeptCode().substring(0, 5).equals(deptCode1.substring(0, 5))).collect(Collectors.toList());
                    break;
                case FlowableInfo.AssignUserProcess.borrowOutCenterAdmin:
                    String deptCode2 = this.getDepartmentId("0", approveCode);
                    nextNodeUserDtosResult = nextNodeUserDtos.stream().filter(result -> result.getDeptCode().length() >= 7 && result.getDeptCode().substring(0, 7).equals(deptCode2.substring(0, 7))).collect(Collectors.toList());

                    break;
                case FlowableInfo.AssignUserProcess.borrowOutCenterDirector:
                    String deptCode3 = this.getDepartmentId("0", approveCode);
                    nextNodeUserDtosResult = nextNodeUserDtos.stream().filter(result -> result.getDeptCode().length() >= 7 && result.getDeptCode().substring(0, 7).equals(deptCode3.substring(0, 7))).collect(Collectors.toList());
                    break;
                case FlowableInfo.AssignUserProcess.returnedDeptAdmin:
                    String deptCode4 = this.getDepartmentId("1", approveCode);
                    nextNodeUserDtosResult = nextNodeUserDtos.stream().filter(result -> result.getDeptCode().substring(0, 5).equals(deptCode4.substring(0, 5))).collect(Collectors.toList());
                    break;
                case FlowableInfo.AssignUserProcess.returnedDeptMinister:
                    String deptCode5 = this.getDepartmentId("1", approveCode);
                    nextNodeUserDtosResult = nextNodeUserDtos.stream().filter(result -> result.getDeptCode().substring(0, 5).equals(deptCode5.substring(0, 5))).collect(Collectors.toList());
                    break;
                case FlowableInfo.AssignUserProcess.returnedCenterAdmin:
                    String deptCode6 = this.getDepartmentId("1", approveCode);
                    nextNodeUserDtosResult = nextNodeUserDtos.stream().filter(result -> result.getDeptCode().length() >= 7 && result.getDeptCode().substring(0, 7).equals(deptCode6.substring(0, 7))).collect(Collectors.toList());
                    break;
                case FlowableInfo.AssignUserProcess.returnedCenterDirector:
                    String deptCode7 = this.getDepartmentId("1", approveCode);
                    nextNodeUserDtosResult = nextNodeUserDtos.stream().filter(result -> result.getDeptCode().length() >= 7 && result.getDeptCode().substring(0, 7).equals(deptCode7.substring(0, 7))).collect(Collectors.toList());
                    break;
                default:
                    nextNodeUserDtosResult = nextNodeUserDtos;
                    break;
            }
        }
        return nextNodeUserDtosResult;
    }

    private boolean isBusinessCenter(String type, String approveCode) {
        //0为借用工单查询，1为借用归还工单查询
        if ("0".equals(type)) {
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
                String assetborrowUserID = assetBorrow.getAssetborrowUserID();
                UserInfoDto userInfo = userService.getUserInfo(assetborrowUserID);
                return this.isCenter(userInfo.getPropertyMap().get("EMP_NO").toString());
            }
        } else if ("1".equals(type)) {
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
                AssetRevertDto assetRevertDto1 = assetRevertDto.get(0);
                //判断还入人是否为中心管理员
                String assetRevertUserID = assetRevertDto1.getAssetrevertUserID();
                UserInfoDto userInfo = userService.getUserInfo(assetRevertUserID);
                return this.isCenter(userInfo.getPropertyMap().get("EMP_NO").toString());
            }
        }
        return false;
    }

    private String getDepartmentId(String type, String approveCode) {
        //0为借用工单查询，1为借用归还工单查询
        if ("0".equals(type)) {
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
                //借出部门
                String assetborrowDepartmentID = assetBorrow.getAssetborrowDepartmentID();
                DeptInfoDto deptInfo = userService.getDeptInfo(assetborrowDepartmentID);
                return deptInfo.getDeptCode();
            }
        } else if ("1".equals(type)) {
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
                AssetRevertDto assetRevertDto1 = assetRevertDto.get(0);
                //还入部门
                String deptId = assetRevertDto1.getAssetrevertDepartmentID();
                DeptInfoDto deptInfo = userService.getDeptInfo(deptId);
                return deptInfo.getDeptCode();
            }
        }
        return "";
    }

    @Override
    public boolean isCenter(String userCode) {
        WorkFlowUserGroupModel byGroupCode = workFlowUserGroupDao.findByGroupCode(FlowableInfo.DEPARTMENT_ADMINISTRATOR);
        WorkFlowUserGroupModel byGroupCode1 = workFlowUserGroupDao.findByGroupCode(FlowableInfo.CENTER_ADMINISTRATOR);

        WorkFlowUserModel byGroupIdAndUserId = workFlowUserDao.findByGroupIdAndUserCode(byGroupCode.getId(), userCode);
        WorkFlowUserModel byGroupIdAndUserId1 = workFlowUserDao.findByGroupIdAndUserCode(byGroupCode1.getId(), userCode);
        if (!VGUtility.isEmpty(byGroupIdAndUserId) && !VGUtility.isEmpty(byGroupIdAndUserId1)) {
            throw new RuntimeException("中心管理员和部门管理员不能有相同的人");
        }

        if (VGUtility.isEmpty(byGroupIdAndUserId) && VGUtility.isEmpty(byGroupIdAndUserId1)) {
            throw new RuntimeException("请到用户组管理设置人员所属部门或者中心");
        }

        if (VGUtility.isEmpty(byGroupIdAndUserId) && !VGUtility.isEmpty(byGroupIdAndUserId1)) {
            return true;
        }

        if (!VGUtility.isEmpty(byGroupIdAndUserId) && VGUtility.isEmpty(byGroupIdAndUserId1)) {
            return false;
        }

        return false;
    }

    @Override
    public WorkFlowUserGroupDto getOneWorkFlowGroup(String groupCode) {
        WorkFlowUserGroupModel byGroupCode = workFlowUserGroupDao.findByGroupCode(groupCode);
        WorkFlowUserGroupDto workFlowUserGroupDto = new WorkFlowUserGroupDto();
        BeanUtils.copyProperties(byGroupCode, workFlowUserGroupDto);
        return workFlowUserGroupDto;
    }

    @Override
    public WorksDto createWorksModel(WorksDto worksDto) {
        UserInfoDto userInfo = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        UserInfoDto userInfo1 = userService.getUserInfo(userInfo.getId());
        boolean flag = this.isCenter(userInfo1.getPropertyMap().get("EMP_NO").toString());
        WorksModel worksModel = new WorksModel();
        Map<String, String> jsonMap = new HashMap<>();
        //判断物资是否为生产性
        boolean isProductive = IAssetService.ASSET_PRODUCE_TYPE.生产性物资.equals(worksDto.getProduceType());
        //TODO 根据实际情况加入符合流程情况的配置项
        switch (worksDto.getWorkOrderType()) {
            case FlowableInfo.ASSETS_ADD:
                this.gatewayUtil(jsonMap, 0, flag, isProductive, worksDto);
                break;
            case FlowableInfo.ASSETS_TRANSFER:
                this.gatewayUtil(jsonMap, 0, flag, isProductive, worksDto);
                break;
            //领用归还
            case FlowableInfo.ASSETS_USE_RETURN_DEPT:
                this.gatewayUtil(jsonMap, 3, flag, isProductive, worksDto);
                break;
            case FlowableInfo.ASSETS_USE_RETURN_CENTER:
                this.gatewayUtil(jsonMap, 3, flag, isProductive, worksDto);
                break;
            case FlowableInfo.ASSETS_USE:
                this.gatewayUtil(jsonMap, 0, flag, isProductive, worksDto);
                break;
            case FlowableInfo.ASSETS_BORROW:
                this.gatewayUtil(jsonMap, 1, flag, isProductive, worksDto);
                break;
            case FlowableInfo.ASSETS_BORROW_RETURN:
                this.gatewayUtil(jsonMap, 2, flag, isProductive, worksDto);
                break;
            case FlowableInfo.ASSETS_USER_LOCATION_CHANGE :
                this.gatewayUtil(jsonMap, 0, flag, isProductive, worksDto);
                break;
            case FlowableInfo.FIXED_ASSETS_ARCHIVE_CENTER:
                this.gatewayUtil(jsonMap, 0, flag, isProductive, worksDto);
                break;
            case FlowableInfo.ASSETS_INVENTORY_RESULT:
                this.gatewayUtil(jsonMap, 0, flag, isProductive, worksDto);
                break;
            case FlowableInfo.ASSETS_IMPAIRMENT:
                this.gatewayUtil(jsonMap, 0, flag, isProductive, worksDto);
                break;
            case FlowableInfo.ASSETS_INVENTORY_TASK:
                break;
            default:
                throw new RuntimeException("流程不存在");
        }

        String toJSONString = JSON.toJSONString(jsonMap);
        worksModel.setGlobalVariables(toJSONString);
        worksModel.setProcessInstanceId(worksDto.getProcessInstanceId());
        worksModel.setBusinessKey(worksDto.getBusinessKey());
        worksModel.setWorkStatus(worksDto.getWorkStatus());
        worksModel.setWorkOrderType(worksDto.getWorkOrderType());
        worksModel.setApprovalNumber(worksDto.getApprovalNumber());
        worksModel.setPublicUse(worksDto.getPublicUse());
        WorksModel save = worksDao.save(worksModel);
        WorksDto worksDto1 = new WorksDto();
        BeanUtils.copyProperties(save, worksDto1);
        return worksDto1;
    }

    private void gatewayUtil(Map<String, String> jsonMap, int type, boolean flag, boolean isProductive, WorksDto worksDto) {
        //0:默认流程，1：借用流程,2:借用归还流程
        if (type == 0) {
            if (flag) {
                FlowableInfo.GatewayEnum center_director_line = FlowableInfo.GatewayEnum.valueOf("CENTER_DIRECTOR_LINE");
                jsonMap.put(center_director_line.getKey(), center_director_line.getValue());
            } else {
                FlowableInfo.GatewayEnum dept_admin_line = FlowableInfo.GatewayEnum.valueOf("DEPT_ADMIN_LINE");
                jsonMap.put(dept_admin_line.getKey(), dept_admin_line.getValue());
            }
            //判断是否为生产性
            if (isProductive) {
                FlowableInfo.GatewayEnum productive_asset_admin_line = FlowableInfo.GatewayEnum.valueOf("PRODUCTIVE_ASSET_ADMIN_LINE");
                jsonMap.put(productive_asset_admin_line.getKey(), productive_asset_admin_line.getValue());
            } else {
                FlowableInfo.GatewayEnum no_productive_asset_admin_line = FlowableInfo.GatewayEnum.valueOf("NO_PRODUCTIVE_ASSET_ADMIN_LINE");
                jsonMap.put(no_productive_asset_admin_line.getKey(), no_productive_asset_admin_line.getValue());
            }
        } else if (type == 1) {
            if (flag) {
                FlowableInfo.GatewayEnum center_director_line = FlowableInfo.GatewayEnum.valueOf("CENTER_DIRECTOR_LINE");
                jsonMap.put(center_director_line.getKey(), center_director_line.getValue());
            } else {
                FlowableInfo.GatewayEnum dept_admin_line = FlowableInfo.GatewayEnum.valueOf("DEPT_ADMIN_LINE");
                jsonMap.put(dept_admin_line.getKey(), dept_admin_line.getValue());
            }

            boolean isCenter = this.isBusinessCenter("0", worksDto.getApprovalNumber());
            if (!isCenter) {
                FlowableInfo.GatewayEnum returned_dept_minister_line = FlowableInfo.GatewayEnum.valueOf("RETURNED_OUT_DEPT_MINISTER_LINE");
                jsonMap.put(returned_dept_minister_line.getKey(), returned_dept_minister_line.getValue());
            } else if (isCenter) {
                FlowableInfo.GatewayEnum returned_center_admin_line = FlowableInfo.GatewayEnum.valueOf("RETURNED_OUT_CENTER_ADMIN_LINE");
                jsonMap.put(returned_center_admin_line.getKey(), returned_center_admin_line.getValue());
            }

            //判断是否为生产性
            if (isProductive) {
                FlowableInfo.GatewayEnum productive_asset_admin_line = FlowableInfo.GatewayEnum.valueOf("PRODUCTIVE_ASSET_ADMIN_LINE2");
                jsonMap.put(productive_asset_admin_line.getKey(), productive_asset_admin_line.getValue());
            } else {
                FlowableInfo.GatewayEnum no_productive_asset_admin_line = FlowableInfo.GatewayEnum.valueOf("NO_PRODUCTIVE_ASSET_ADMIN_LINE2");
                jsonMap.put(no_productive_asset_admin_line.getKey(), no_productive_asset_admin_line.getValue());
            }
        } else if (type == 2) {
            if (flag) {
                FlowableInfo.GatewayEnum center_director_line = FlowableInfo.GatewayEnum.valueOf("CENTER_DIRECTOR_LINE");
                jsonMap.put(center_director_line.getKey(), center_director_line.getValue());
            } else {
                FlowableInfo.GatewayEnum dept_admin_line = FlowableInfo.GatewayEnum.valueOf("DEPT_ADMIN_LINE");
                jsonMap.put(dept_admin_line.getKey(), dept_admin_line.getValue());
            }

            boolean isCenter = this.isBusinessCenter("1", worksDto.getApprovalNumber());
            if (!isCenter) {
                FlowableInfo.GatewayEnum returned_dept_minister_line = FlowableInfo.GatewayEnum.valueOf("RETURNED_IN_DEPT_MINISTER_LINE");
                jsonMap.put(returned_dept_minister_line.getKey(), returned_dept_minister_line.getValue());
            } else if (isCenter) {
                FlowableInfo.GatewayEnum returned_center_admin_line = FlowableInfo.GatewayEnum.valueOf("RETURNED_IN_CENTER_ADMIN_LINE");
                jsonMap.put(returned_center_admin_line.getKey(), returned_center_admin_line.getValue());
            }

            //判断是否为生产性
            if (isProductive) {
                FlowableInfo.GatewayEnum productive_asset_admin_line = FlowableInfo.GatewayEnum.valueOf("PRODUCTIVE_ASSET_ADMIN_LINE3");
                jsonMap.put(productive_asset_admin_line.getKey(), productive_asset_admin_line.getValue());
            } else {
                FlowableInfo.GatewayEnum no_productive_asset_admin_line = FlowableInfo.GatewayEnum.valueOf("NO_PRODUCTIVE_ASSET_ADMIN_LINE3");
                jsonMap.put(no_productive_asset_admin_line.getKey(), no_productive_asset_admin_line.getValue());
            }
        } else if (type == 3) {
            if (flag) {
                if (isProductive) {
                    FlowableInfo.GatewayEnum productive_asset_admin_line = FlowableInfo.GatewayEnum.valueOf("PRODUCTIVE_ASSET_ADMIN_LINE4");
                    jsonMap.put(productive_asset_admin_line.getKey(), productive_asset_admin_line.getValue());
                } else {
                    FlowableInfo.GatewayEnum no_productive_asset_admin_line = FlowableInfo.GatewayEnum.valueOf("NO_PRODUCTIVE_ASSET_ADMIN_LINE4");
                    jsonMap.put(no_productive_asset_admin_line.getKey(), no_productive_asset_admin_line.getValue());
                }
            } else {
                if (isProductive) {
                    FlowableInfo.GatewayEnum productive_asset_admin_line = FlowableInfo.GatewayEnum.valueOf("PRODUCTIVE_DRAFT_LINE");
                    jsonMap.put(productive_asset_admin_line.getKey(), productive_asset_admin_line.getValue());
                } else {
                    FlowableInfo.GatewayEnum no_productive_asset_admin_line = FlowableInfo.GatewayEnum.valueOf("NO_PRODUCTIVE_DRAFT_LINE");
                    jsonMap.put(no_productive_asset_admin_line.getKey(), no_productive_asset_admin_line.getValue());
                }
            }
                //判断是否为生产性

        }
    }

    @Override
    public WorksDto updateWorksModel(WorksDto worksDto) {
        WorksDto worksDto1 = new WorksDto();
        if (!VGUtility.isEmpty(worksDto.getId())) {
            Optional<WorksModel> byId = worksDao.findById(worksDto.getId());
            byId.ifPresent(model -> {
                model.setProcessInstanceId(worksDto.getProcessInstanceId());
                model.setBusinessKey(worksDto.getBusinessKey());
                model.setWorkStatus(worksDto.getWorkStatus());
                model.setWorkOrderType(worksDto.getWorkOrderType());
                model.setApprovalNumber(worksDto.getApprovalNumber());
                model.setGlobalVariables(worksDto.getGlobalVariables());
                model.setPublicUse(worksDto.getPublicUse());
                WorksModel save = worksDao.save(model);
                BeanUtils.copyProperties(save, worksDto1);
            });
        }
        return worksDto1;
    }

    @Override
    public WorksDto getWorksModelByBusinessKey(String businessKey) {
        WorksModel byBusinessKey = worksDao.findByBusinessKey(businessKey);
        WorksDto worksDto = new WorksDto();
        if (!VGUtility.isEmpty(byBusinessKey)) {
            BeanUtils.copyProperties(byBusinessKey, worksDto);
            return worksDto;
        }
        return null;
    }

    @Override
    public WorksDto getWorksModelByProcessInstanceId(String processInstanceId) {
        WorksModel byProcessInstanceId = worksDao.findByProcessInstanceId(processInstanceId);
        WorksDto worksDto = new WorksDto();
        BeanUtils.copyProperties(byProcessInstanceId, worksDto);
        return worksDto;
    }

    @Override
    public FlowRuleDto getFlowRuleByNodeIdAndProcDefKey(String nodeId, String procDefKey) {
        FlowRuleModel byNodeIdAndProcDefKey = flowRuleDao.findByNodeIdAndProcDefKey(nodeId, procDefKey);
        FlowRuleDto flowRuleDto = new FlowRuleDto();
        BeanUtils.copyProperties(byNodeIdAndProcDefKey, flowRuleDto);
        return flowRuleDto;
    }

    @Override
    public void approveNotify(IApproveNotify approveNotify, String businessKey) {
        WorksModel worksModel = worksDao.findByBusinessKey(businessKey);
        approveNotify.approveUpdateNotify(worksModel.getWorkStatus(), businessKey);
    }

    @Override
    public AssetApprove doApprove(IApproveNotify approveNotify, String businessKey) {
        AssetApprove assetApprove = approveNotify.doApproveNotify(businessKey);
        return assetApprove;

    }

    @Override
    public void syncFlowConfiguration() {
        //删除数据库里现有数据
        flowRuleDao.deleteAll();
        //插进数据
        FlowableInfo.WorkFlowEnum[] values = FlowableInfo.WorkFlowEnum.values();
        for (int i = 0; i < values.length; i++) {
            String proceDefKey = values[i].getKey();
            ReturnVo<FlowNodeVo> asset_add = iApiFlowableProcessDefinitionService.getUserTaskByProcessDefinitionKey(proceDefKey);
            if (ReturnCode.SUCCESS.equals(asset_add.getCode())) {
                List<FlowNodeVo> datas = asset_add.getDatas();
                datas.stream().forEach(o -> {
                    FlowRuleModel flowRuleModel = new FlowRuleModel();
                    flowRuleModel.setHistoryUserSame(1);
                    flowRuleModel.setNodeId(o.getNodeId());
                    flowRuleModel.setOperationType(1);
                    flowRuleModel.setPreviousUserSame(0);
                    flowRuleModel.setProcDefKey(proceDefKey);
                    flowRuleDao.save(flowRuleModel);
                });
            }
        }
    }

    @Override
    public Map<String, Object> finishedWork(ProcessInstanceQueryVo params, String page, String rows) {
        UserInfoDto userInfo = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        UserInfoDto userInfo1 = userService.getUserInfo(userInfo.getId());
        String emp_no = userInfo1.getPropertyMap().get("EMP_NO").toString();

        params.setUserCode(emp_no);
        ReturnVo<ReturnPageListVo<ProcessInstanceVo>> finishedWork = iApiFlowableProcessInstanceService.getMyApprovedProcessInstances(params, page, rows);
        ReturnPageListVo<ProcessInstanceVo> data = finishedWork.getData();

        Map<String, Object> resultMap = new HashMap<>(5);
        resultMap.put("total", data.getTotal());
        List<ApproveFinishedDto> approveFinishedDtos = new ArrayList<>();
        data.getRows().forEach(o -> {
            ApproveFinishedDto approveFinishedDto = new ApproveFinishedDto();
            List<UserInfoDto> rowData = userService.getUserInfo("{$and:[{sysMark:'" + IBaseService.SYSMARK + "'},{'propertyMap.EMP_NO':'" + o.getStarterId() + "'}]}", null, null).getRowData();
            String processInstanceStarter = rowData.get(0).getChsName();
            o.setStarter(processInstanceStarter);
            BeanUtils.copyProperties(o, approveFinishedDto);
            String businessKey = o.getBusinessKey();
            WorksDto worksModelByBusinessKey = this.getWorksModelByBusinessKey(businessKey);
            approveFinishedDto.setApproveNumber(worksModelByBusinessKey.getApprovalNumber());
            approveFinishedDtos.add(approveFinishedDto);
        });

        resultMap.put("rows", approveFinishedDtos);
        return resultMap;
    }

    @Override
    public void turnBack(String url, String suggestContent, String processDefinitionKey, String procInstId, String taskId, String nodeId) {
        UserInfoDto userInfo = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        UserInfoDto userInfo1 = userService.getUserInfo(userInfo.getId());
        String emp_no = userInfo1.getPropertyMap().get("EMP_NO").toString();

        Map<String, Object> baseInfo = putBaseInfo(processDefinitionKey, emp_no);
        processDefinitionKey = baseInfo.get("processDefKey").toString();

        //获取当前task
        ReturnVo<TaskVo> taskById = apiFlowableTaskService.getTaskById(taskId);
        TaskVo data1 = taskById.getData();


        FlowRuleDto flowRuleByNodeIdAndProcDefKey = this.getFlowRuleByNodeIdAndProcDefKey(nodeId, processDefinitionKey);
        BackTaskVo backTaskVo = new BackTaskVo();

        //设置退回的节点
        switch (flowRuleByNodeIdAndProcDefKey.getOperationType()) {
            case 1 :
                backTaskVo.setDistFlowElementId(FlowableInfo.FLOW_SUBMITTER_ID);
                break;
            default: throw new RuntimeException("不存在此操作类型！");
        }
        backTaskVo.setTaskId(taskId);
        backTaskVo.setUserCode(emp_no);
        backTaskVo.setMessage(suggestContent);
        backTaskVo.setProcessInstanceId(procInstId);
        apiFlowableWorkDetailService.doBackStep(backTaskVo);

        //修改审批流状态
        ReturnVo<ProcessInstanceVo> byProcessInstanceId = iApiFlowableProcessInstanceService.getByProcessInstanceId(procInstId);
        ProcessInstanceVo data = byProcessInstanceId.getData();
        String businessKey = data.getBusinessKey();
        WorksDto worksModelByBusinessKey = this.getWorksModelByBusinessKey(businessKey);
        worksModelByBusinessKey.setWorkStatus(FlowableInfo.WORKSTATUS.驳回);
        this.updateWorksModel(worksModelByBusinessKey);

        //修改业务状态
        updateBusinessStatus(worksModelByBusinessKey);

        //发送给待办
        //sendToDaiBan(url, data.getFormName(), procInstId);
        //修改为已办
        //sendToYiBan(url, procInstId, emp_no, data1.getPreviousNodeSendUserId(), data.getFormName());

    }

    @Override
    public void agreeWork(String url, String processInstanceId, String taskId, String params, String suggestContent) {
        if(VGUtility.isEmpty(taskId) || VGUtility.isEmpty(processInstanceId)) {
            throw new RuntimeException("参数不正确！");
        }

        UserInfoDto userInfo = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        UserInfoDto userInfo1 = userService.getUserInfo(userInfo.getId());
        String emp_no = userInfo1.getPropertyMap().get("EMP_NO").toString();

        // 获取当前任务
        ReturnVo<TaskVo> taskById = apiFlowableTaskService.getTaskById(taskId);
        TaskVo data = taskById.getData();
        String nodeId = data.getNodeId();
        String previousNodeSendUserId = data.getPreviousNodeSendUserId();

        ReturnVo<ProcessInstanceVo> byProcessInstanceId = iApiFlowableProcessInstanceService.getByProcessInstanceId(processInstanceId);
        ProcessInstanceVo data2 = byProcessInstanceId.getData();
        String businessKey = data2.getBusinessKey();

        // 1.获取下一环节
        ReturnVo<FlowNodeVo> nextNodesByTaskReturn = apiFlowableWorkDetailService.getNextNodesByTaskId(taskId);
        // 2.处理前端传入参数
        Map<String, List<String>> nodeAndUserMap = new HashMap<>();
        if(VGUtility.isEmpty(params) && nextNodesByTaskReturn.getDatas().size() > 0) {
            //2.1获取下一环节有数据，但用户没有选择
            throw new RuntimeException("未选择环节或用户！");
        } else {
            if(!VGUtility.isEmpty(params)) {
                // 2.2解析前端传入参数 格式：huanjie1_user1;huanjie1_user2;huanjie3_user3
                String[] nodeAndUserArray = params.split(";");
                for (int i = 0; i < nodeAndUserArray.length; i++) {
                    String[] nodeAndUser = nodeAndUserArray[i].split("_");
                    if(null != nodeAndUserMap.get(nodeAndUser[0])) {
                        // 2.2.1缓存中存在环节和用户，添加在list中
                        nodeAndUserMap.get(nodeAndUser[0]).add(nodeAndUser[1]);
                    } else {
                        // 2.2.2缓存中不存在，创建
                        List<String> userList = new ArrayList<>();
                        userList.add(nodeAndUser[1]);
                        nodeAndUserMap.put(nodeAndUser[0], userList);
                    }
                }
            }
        }

        // 3.封装审批参数
        CompleteTaskVo complete = new CompleteTaskVo();
        complete.setUserCode(emp_no);
        complete.setMessage(suggestContent);
        complete.setTaskId(taskId);
        complete.setProcessInstanceId(processInstanceId);
        complete.setType(CommentTypeEnum.SP.toString());
        // 3.1 处于流程最后环节不需要封装参数
        if(nextNodesByTaskReturn.getDatas().size() > 0) {
            Map<String, Object> paramsMap = autoHandlerVariables(complete, nodeAndUserMap, nextNodesByTaskReturn, taskById.getData().getNodeId());
            complete.setVariables(paramsMap);
        }
        log.debug(">>>>>>>>>>>发送审批："+complete.toString());
        // 发送审批
        apiFlowableWorkDetailService.complete(complete);

        WorksDto worksModelByBusinessKey = this.getWorksModelByBusinessKey(businessKey);

        //退回后的重新发送，状态由退回改为审批中
        if (Objects.equals(FlowableInfo.FLOW_SUBMITTER_ID, nodeId)) {
            if (FlowableInfo.ASSETS_USE_RETURN_DEPT.equals(worksModelByBusinessKey.getWorkOrderType())) {
                worksModelByBusinessKey.setWorkStatus(FlowableInfo.WORKSTATUS.已审批);
            } else{
                worksModelByBusinessKey.setWorkStatus(FlowableInfo.WORKSTATUS.审批中);
            }
            this.updateWorksModel(worksModelByBusinessKey);
            //修改业务状态
            updateBusinessStatus(worksModelByBusinessKey);
        }


        //发送待办
        //sendToDaiBan(url, data2.getFormName(), processInstanceId);
        //发送已办
        //sendToYiBan(url, processInstanceId, emp_no, previousNodeSendUserId, data2.getFormName());

    }

    private void sendToYiBan(String url, String processInstanceId, String emp_no, String previousNodeSendUserId, String formName) {
        //修改已办
        MessageSendModel messageSendModel = new MessageSendModel();
        messageSendModel.setMessageId(processInstanceId);
        messageSendModel.setMessageName(formName);
        messageSendModel.setMessageContent(formName);
        messageSendModel.setMessageType("1");
        messageSendModel.setMessageTodoState("1");
        messageSendModel.setSenderId(previousNodeSendUserId);
        messageSendModel.setMessageSrc(MESSAGE_SRC);
        messageSendModel.setMessageTarget(MESSAGE_TARGET);
        messageSendModel.setReceiverId(emp_no);
        messageSendModel.setPostDt(VGUtility.toDateStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
        String systemURL = url + "/login_out_link?processInstanceId=" + processInstanceId;
        messageSendModel.setUrl(systemURL);
        messageSendModel.setRemark("修改为已办");
        try {
            boolean b = sendToOU(messageSendModel);
            if (b) {
                messageSendModel.setStatus(1);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            log.debug("Save MessageSendModel where ProcessInstanceId = [{}]", processInstanceId);
            messageSendDao.save(messageSendModel);
        }
    }

    public boolean sendToOU(MessageSendModel dto) throws JsonProcessingException {
        log.info("Send To OU Dan Ban: " +VGUtility.debugToString(dto));
        ObjectMapper mapper= new ObjectMapper();
        String jsonStr = mapper.writeValueAsString(dto);
        log.info("发送到门户系统》》》》》》》》》"
                + "MessageId["+dto.getMessageId()+"]"
                + "MessageName["+dto.getMessageName()+"]"
                + "MessageContent["+dto.getMessageContent()+"]"
                + "MessageType["+dto.getMessageType()+"]"
                + "MessageTodoState["+dto.getMessageTodoState()+"]"
                + "MessageSrc["+dto.getMessageSrc()+"]"
                + "SenderId["+dto.getSenderId()+"]"
                + "MessageTarget["+dto.getMessageTarget()+"]"
                + "ReceiverId["+dto.getReceiverId()+"]"
                + "PostDt["+dto.getPostDt()+"]"
                + "Url["+dto.getUrl()+"]");
        return RabbitMqUtil.sendMqMessage(jsonStr);
    }

    /**
     * 自动设置流程参数
     * @param completeTaskVo 传递给流程的参数
     * @param nodeAndUserMap 解析前端传递的参数
     * @param nextNodesByTaskReturn 下一环节数据
     * @return Map<String, Object>
     */
    private Map<String, Object> autoHandlerVariables(CompleteTaskVo completeTaskVo, Map<String, List<String>> nodeAndUserMap, ReturnVo<FlowNodeVo> nextNodesByTaskReturn, String currentNodeId) {
        if(ReturnCode.SUCCESS.equals(nextNodesByTaskReturn.getCode())) {
            List<FlowNodeVo> nextNodesByTaskId = nextNodesByTaskReturn.getDatas();
            // 2.根据不同节点类型设置值
            Map<String, Object> stringObjectMap = new HashMap<>();
            nextNodesByTaskId.forEach(flowNodeVo->{
                if(null != nodeAndUserMap.get(flowNodeVo.getNodeId())) {
                    // 2.1经过排他网关需要封装 “huanjie1Line:true” 经过的网关：0未经过 1排他网关 2并行网关 3包容网关
                    if(flowNodeVo.getGateway() == 1) {
                        stringObjectMap.put(currentNodeId+"NextNode", flowNodeVo.getNodeId());
                    }
                    // 2.2下一节点是否是会签
                    if(flowNodeVo.isInMultiInstance()) {
                        stringObjectMap.put(flowNodeVo.getNodeId()+"UserList", nodeAndUserMap.get(flowNodeVo.getNodeId()));
                    } else {
                        stringObjectMap.put(flowNodeVo.getNodeId()+"User", nodeAndUserMap.get(flowNodeVo.getNodeId()).get(0));
                    }
                }
            });
            return stringObjectMap;
        } else {
            throw new RuntimeException("获取数据失败，请重试！");
        }
    }

    @Override
    public byte[] getWorkflowImage(String processInstanceId) {
        ReturnVo<ProcessInstanceImageVo> processInstanceImageVoReturn = iApiFlowableProcessInstanceService.getProcessInstancePng(processInstanceId);
        if (ReturnCode.SUCCESS.equals(processInstanceImageVoReturn.getCode())) {
            ProcessInstanceImageVo data = processInstanceImageVoReturn.getData();
            return data.getImageByte();
        }
        return null;
    }

    @Override
    public Map<String, Object> getTodoWork(TaskQueryVo params, String page, String rows) {
        UserInfoDto userInfo = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        UserInfoDto userInfo1 = userService.getUserInfo(userInfo.getId());
        String emp_no = userInfo1.getPropertyMap().get("EMP_NO").toString();
        params.setUserCode(emp_no);
        ReturnVo<ReturnPageListVo<TaskVo>> todoWork = apiFlowableTaskService.getTodoWork(params, page, rows);
        ReturnPageListVo<TaskVo> data = todoWork.getData();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", data.getTotal());
        List<ApproveToDoDto> approveToDoDtos = new ArrayList<>();
        data.getRows().forEach(o -> {
            ApproveToDoDto approveToDoDto = new ApproveToDoDto();
            List<UserInfoDto> rowData = userService.getUserInfo("{$and:[{sysMark:'" + IBaseService.SYSMARK + "'},{'propertyMap.EMP_NO':'" + o.getProcessInstanceStarterId() + "'}]}", null, null).getRowData();
            String processInstanceStarter = rowData.get(0).getChsName();
            o.setProcessInstanceStarter(processInstanceStarter);
            String previousNodeSendUserId = o.getPreviousNodeSendUserId();
            String[] split = previousNodeSendUserId.split(",");
            String previousNodeSendUserName = "";
            for (int i = 0; i < split.length; i++) {
                List<UserInfoDto> rowData2 = userService.getUserInfo("{$and:[{sysMark:'" + IBaseService.SYSMARK + "'},{'propertyMap.EMP_NO':'" + split[i] + "'}]}", null, null).getRowData();
                if (!VGUtility.isEmpty(rowData2) && rowData2.size() > 0) {
                    previousNodeSendUserName += (rowData2.get(0).getChsName() + ",");
                }
            }
            if (!VGUtility.isEmpty(previousNodeSendUserName)) {
                o.setPreviousNodeSendUserName(previousNodeSendUserName.substring(0, previousNodeSendUserName.length() - 1));
            }
            BeanUtils.copyProperties(o, approveToDoDto);
            String businessKey = o.getBusinessKey();
            WorksDto worksModelByBusinessKey = this.getWorksModelByBusinessKey(businessKey);
            approveToDoDto.setApproveNumber(worksModelByBusinessKey.getApprovalNumber());
            approveToDoDtos.add(approveToDoDto);
        });

        resultMap.put("rows", approveToDoDtos);

        return resultMap;
    }

    @Override
    public void submitNext(String url, Map<String, String> map) {
        UserInfoDto userInfo = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        UserInfoDto userInfo1 = userService.getUserInfo(userInfo.getId());
        String emp_no = userInfo1.getPropertyMap().get("EMP_NO").toString();
        Map<String, Object> baseInfo = putBaseInfo(map.get("processDefKey"), emp_no);

        String id = map.get("id");
        String formName = map.get("formName");
        String processDefKey = baseInfo.get("processDefKey").toString();
        String params = map.get("params");
        String serialNumber = map.get("serialNumber");
        String publicUse = map.get("publicUse");
        String produceType = map.get("produceType");

        StartProcessInstanceVo startProcessInstanceVo = new StartProcessInstanceVo();
        startProcessInstanceVo.setBusinessKey(id);
        startProcessInstanceVo.setFormName(formName + "(" + serialNumber + ")");
        startProcessInstanceVo.setCreator(emp_no);
        startProcessInstanceVo.setCurrentUserCode(emp_no);
        startProcessInstanceVo.setSystemSn(IBaseService.SYSMARK);
        startProcessInstanceVo.setProcessDefinitionKey(processDefKey);

        ReturnVo<ProcessInstanceVo> processInstanceVoReturnVo = iApiFlowableProcessInstanceService.startProcessInstanceByKey(startProcessInstanceVo);

        if(ReturnCode.SUCCESS.equals(processInstanceVoReturnVo.getCode())) {
            ProcessInstanceVo data = processInstanceVoReturnVo.getData();
            String processInstanceId = data.getProcessInstanceId();
            TaskQueryVo taskQueryVo = new TaskQueryVo();
            taskQueryVo.setBusinessKey(id);
            taskQueryVo.setTaskDefKey(FlowableInfo.FLOW_SUBMITTER_ID);
            taskQueryVo.setProcessInstanceId(processInstanceId);
            taskQueryVo.setUserCode(emp_no);
            ReturnVo<ReturnPageListVo<TaskVo>> taskByParam = apiFlowableTaskService.getTaskByParam(taskQueryVo, "1", "20");
            TaskVo taskVo1 = taskByParam.getData().getRows().get(0);
            //当前环节taskId
            String taskId = taskVo1.getTaskId();
            // 1.获取下一环节
            ReturnVo<FlowNodeVo> nextNodesByTaskReturn = apiFlowableWorkDetailService.getNextNodesByTaskId(taskId);

            //boolean flag = this.isCenter(userInfo1.getPropertyMap().get("EMP_NO").toString());
            //if (flag) {
            //    param.put(FlowableInfo.FLOW_SUBMITTER_ID + "NextNode", FlowableInfo.PROCESS_CENTER);
            //} else {
            //    param.put(FlowableInfo.FLOW_SUBMITTER_ID + "NextNode", FlowableInfo.PROCESS_DEPT);
            //}

            Map<String, List<String>> nodeAndUserMap = new HashMap<>();

            if(!VGUtility.isEmpty(params)) {
                // 2.2解析前端传入参数 格式：huanjie1_user1;huanjie1_user2;huanjie3_user3
                String[] nodeAndUserArray = params.split(";");
                for (int i = 0; i < nodeAndUserArray.length; i++) {
                    String[] nodeAndUser = nodeAndUserArray[i].split("_");
                    if(null != nodeAndUserMap.get(nodeAndUser[0])) {
                        // 2.2.1缓存中存在环节和用户，添加在list中
                        nodeAndUserMap.get(nodeAndUser[0]).add(nodeAndUser[1]);
                    } else {
                        // 2.2.2缓存中不存在，创建
                        List<String> userList = new ArrayList<>();
                        userList.add(nodeAndUser[1]);
                        nodeAndUserMap.put(nodeAndUser[0], userList);
                    }
                }
            }

            // 3.封装审批参数
            CompleteTaskVo complete = new CompleteTaskVo();
            complete.setUserCode(emp_no);
            complete.setMessage("发起审批");
            complete.setTaskId(taskId);
            complete.setProcessInstanceId(processInstanceId);
            complete.setType(CommentTypeEnum.SP.toString());
            Map<String, Object> paramsMap = autoHandlerVariables(complete, nodeAndUserMap, nextNodesByTaskReturn, taskVo1.getNodeId());
            complete.setVariables(paramsMap);
            apiFlowableWorkDetailService.complete(complete);

            //保存流程配置
            WorksDto worksDto = new WorksDto();
            worksDto.setBusinessKey(id);
            if (!Objects.equals(processDefKey, FlowableInfo.ASSETS_INVENTORY_TASK)) {
                IAssetService.ASSET_PRODUCE_TYPE value = IAssetService.ASSET_PRODUCE_TYPE.values()[Integer.valueOf(produceType)];
                worksDto.setProduceType(value);
            }
            worksDto.setApprovalNumber(serialNumber);
            worksDto.setWorkOrderType(processDefKey);

            if (FlowableInfo.ASSETS_USE_RETURN_DEPT.equals(processDefKey)) {
                worksDto.setWorkStatus(FlowableInfo.WORKSTATUS.已审批);
            } else {
                worksDto.setWorkStatus(FlowableInfo.WORKSTATUS.审批中);
            }
            worksDto.setProcessInstanceId(processInstanceId);
            worksDto.setStarterId(userInfo1.getPropertyMap().get("EMP_NO").toString());
            if (!VGUtility.isEmpty(publicUse)) {
                worksDto.setPublicUse(publicUse);
            }
            WorksDto worksModel = this.createWorksModel(worksDto);

            //调用业务接口
            updateBusinessStatus(worksDto);

            //发送待办
            //sendToDaiBan(url, formName, processInstanceId);

            //修改已办
            //sendToYiBan(url, processInstanceId, emp_no, emp_no, formName);
        }
    }

    private void sendToDaiBan(String url, String formName, String processInstanceId) {
        TaskQueryVo taskQueryVo1 = new TaskQueryVo();
        taskQueryVo1.setProcessInstanceId(processInstanceId);

        //发送待办
        ReturnVo<ReturnPageListVo<TaskVo>> todoWork = apiFlowableTaskService.getTodoWork(taskQueryVo1, "1", "100");
        if (Objects.equals(todoWork.getCode(), ReturnCode.SUCCESS)) {
            ReturnPageListVo<TaskVo> data1 = todoWork.getData();
            List<TaskVo> rows = data1.getRows();
            rows.stream().forEach(o -> {
                String taskId1 = o.getTaskId();
                MessageSendModel messageSendModel = new MessageSendModel();
                messageSendModel.setMessageId(processInstanceId);
                messageSendModel.setMessageName(formName);
                messageSendModel.setMessageContent(formName);
                messageSendModel.setMessageType("1");
                messageSendModel.setMessageTodoState("0");
                messageSendModel.setSenderId(o.getPreviousNodeSendUserId());
                messageSendModel.setMessageSrc(MESSAGE_SRC);
                messageSendModel.setMessageTarget(MESSAGE_TARGET);
                messageSendModel.setReceiverId(o.getApproverId());
                messageSendModel.setPostDt(VGUtility.toDateStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
                String systemURL = url + "/login_out_link?processInstanceId=" + processInstanceId + "&taskId=" + taskId1;
                messageSendModel.setUrl(systemURL);
                messageSendModel.setRemark("发送待办");

                try {
                    boolean b = this.sendToOU(messageSendModel);
                    if (b) {
                        messageSendModel.setStatus(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    log.debug("Save MessageSendModel where ProcessInstanceId = [{}]", processInstanceId);
                    messageSendDao.save(messageSendModel);
                }
            });
        }
    }

    @Override
    public void updateBusinessStatus(WorksDto worksDto) {
        Object object = FlowableInfo.mapService.get(worksDto.getWorkOrderType());
        String serviceId = "";
        if (object instanceof String) {
            serviceId = (String) object;
        } else if (object instanceof Map) {
            Map<String, String> mapService = (Map<String, String>) object;
            serviceId = mapService.get(worksDto.getPublicUse());
        }
        IApproveNotify approveNotify = (IApproveNotify) SpringContextUtil.getBean(serviceId);
        this.approveNotify(approveNotify, worksDto.getBusinessKey());
    }

    @Override
    public List<NextNodeDto> getFirstNode(Map<String, String> map) {
        UserInfoDto userInfo = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        UserInfoDto userInfo1 = userService.getUserInfo(userInfo.getId());
        Map<String, Object> baseInfo = putBaseInfo(map.get("processDefKey"), userInfo1.getPropertyMap().get("EMP_NO").toString());
        String businessKey = map.get("id");
        String processDefinitionKey = baseInfo.get("processDefKey").toString();
        String publicUse = map.get("publicUse");
        String produceType = map.get("produceType");



        //判断业务资产是否可用
        Object object = FlowableInfo.mapService.get(processDefinitionKey);
        String serviceId = "";
        if (object instanceof java.lang.String) {
            serviceId = (String)object;
        } else if (object instanceof java.util.Map) {
            Map<String, String> mapService = (Map<String, String>)object;
            serviceId = mapService.get(publicUse);
        }

        IApproveNotify approveNotify = (IApproveNotify) SpringContextUtil.getBean(serviceId);
        AssetApprove assetApprove = this.doApprove(approveNotify, businessKey);
        if (!assetApprove.isCanApprove()) {
            StringBuilder resultStr = new StringBuilder("");
            List<ApprovenAssetDto> unAssetList = assetApprove.getUnAssetList();
            if (FlowableInfo.ASSETS_INVENTORY_TASK.equals(processDefinitionKey)) {
                resultStr.append("请选择要盘点的资产！");
            } else if (FlowableInfo.ASSETS_ADD.equals(processDefinitionKey)) {
                resultStr.append(assetApprove.getMessage());
            } else {
                for (ApprovenAssetDto approvenAssetDto : unAssetList) {
                    if (FlowableInfo.ASSETS_INVENTORY_RESULT.equals(processDefinitionKey)) {
                        resultStr.append(approvenAssetDto.getAssetCode() + "(" + (VGUtility.isEmpty(approvenAssetDto.getAssetChsName())?"":approvenAssetDto.getAssetChsName()) + ")" + "未盘点;");
                    } else {
                        resultStr.append(approvenAssetDto.getAssetCode() + "(" + (VGUtility.isEmpty(approvenAssetDto.getAssetChsName())?"":approvenAssetDto.getAssetChsName()) + ")" + "处于" + approvenAssetDto.getAssetStatus().name() + "状态;");
                    }
                }
            }
            resultStr.append("不可进行审批操作");
            throw new RuntimeException(resultStr.toString());
        }


        List<NextNodeDto> nextNodeDtos = new ArrayList<>();
        NextNodeDto nextNodeDto = new NextNodeDto();
        ReturnVo<FlowNodeVo> asset_add = iApiFlowableProcessDefinitionService.getUserTaskByProcessDefinitionKey(processDefinitionKey);
        List<FlowNodeVo> datas = asset_add.getDatas();

        getFirstNodeUser((Boolean)baseInfo.get("flag"), processDefinitionKey, nextNodeDto, datas, produceType, (Integer)baseInfo.get("type"));
        nextNodeDtos.add(nextNodeDto);
        return nextNodeDtos;
    }

    private Map<String, Object> putBaseInfo(String originProcDefKey, String empNo) {
        //boolean flag = this.isCenter(userInfo1.getPropertyMap().get("EMP_NO").toString());

        Map<String, Object> resultMap = new HashMap<>(5);

        boolean flag = this.isCenter(empNo);

        switch (originProcDefKey) {
            case FlowableInfo.ASSETS_USE_RETURN :
                String resultStr = "";
                if (flag) {
                    resultStr = originProcDefKey + "_CENTER";
                } else {
                    resultStr = originProcDefKey + "_DEPT";
                }
                resultMap.put("processDefKey", resultStr);
                resultMap.put("type", 1);
                break;
            default:
                resultMap.put("processDefKey", originProcDefKey);
                resultMap.put("type", 0);
                break;
        }

        resultMap.put("flag", flag);
        return resultMap;
    }

    private void getFirstNodeUser(boolean flag, String processDefinitionKey, NextNodeDto nextNodeDto, List<FlowNodeVo> datas, String produceType, int type) {

        List<NextNodeUserDto> firstUser = null;
        if (type== 0) {
            //默认
            if (flag) {
                firstUser = getFirstUserCommon(processDefinitionKey, FlowableInfo.PROCESS_CENTER, nextNodeDto, datas);
            } else {
                firstUser = getFirstUserCommon(processDefinitionKey, FlowableInfo.PROCESS_DEPT, nextNodeDto, datas);

            }
        } else if (type == 1) {
            //领用归还
            IAssetService.ASSET_PRODUCE_TYPE value = IAssetService.ASSET_PRODUCE_TYPE.values()[Integer.valueOf(produceType)];
            if (flag) {
                firstUser = getFirstUserCommon(processDefinitionKey, FlowableInfo.PROCESS_DEPT_ASSET_ADMIN, nextNodeDto, datas);
            } else {
                if (IAssetService.ASSET_PRODUCE_TYPE.生产性物资.equals(value)) {
                    firstUser = getFirstUserCommon(processDefinitionKey, FlowableInfo.PROCESS_PRODUCTIVE_ASSET_ADMIN, nextNodeDto, datas);
                } else {
                    firstUser = getFirstUserCommon(processDefinitionKey, FlowableInfo.PROCESS_NO_PRODUCTIVE_ASSET_ADMIN, nextNodeDto, datas);

                }
            }


        }
        nextNodeDto.setUsers(firstUser);
    }

    /**
     * 获取发起环节人员和环节ID通用部分
     * @param processDefinitionKey 流程ID
     * @param process 环节ID
     * @param nextNodeDto
     * @param datas
     * @return
     */
    private List<NextNodeUserDto> getFirstUserCommon(String processDefinitionKey, String process, NextNodeDto nextNodeDto, List<FlowNodeVo> datas) {
        List<NextNodeUserDto> processUser  = this.getProcessUser(processDefinitionKey, process, null);
        nextNodeDto.setNodeId(process);
        for (FlowNodeVo flowNodeVo : datas) {
            if (process.equals(flowNodeVo.getNodeId())) {
                nextNodeDto.setNodeName(flowNodeVo.getNodeName());
                break;
            }
        }
        return processUser;
    }

    @Override
    public List<NextNodeDto> getNextNode(String procInstId, String taskId, String processDefinitionKey) {
        UserInfoDto userInfo = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        UserInfoDto userInfo1 = userService.getUserInfo(userInfo.getId());

        Map<String, Object> baseInfo = putBaseInfo(processDefinitionKey, userInfo1.getPropertyMap().get("EMP_NO").toString());
        processDefinitionKey = baseInfo.get("processDefKey").toString();


        //获取流程实例信息
        ReturnVo<ProcessInstanceVo> byProcessInstanceId = iApiFlowableProcessInstanceService.getByProcessInstanceId(procInstId);
        ProcessInstanceVo data = byProcessInstanceId.getData();
        String businessKey = data.getBusinessKey();
        WorksDto worksModelByBusinessKey = this.getWorksModelByBusinessKey(businessKey);
        String globalVariables = worksModelByBusinessKey.getGlobalVariables();
        Map<String, String> parse = (Map) JSON.parse(globalVariables);

        //获取当前环节
        ReturnVo<TaskVo> taskById = apiFlowableTaskService.getTaskById(taskId);
        if (ReturnCode.SUCCESS.equals(taskById.getCode())) {
            TaskVo data1 = taskById.getData();
            // 1.获取下一环节
            ReturnVo<FlowNodeVo> nextNodesByTaskReturn = apiFlowableWorkDetailService.getNextNodesByTaskId(taskId);
            // 2.封装数据
            List<NextNodeDto> nextNodeDtos = new ArrayList<>();
            if(ReturnCode.SUCCESS.equals(nextNodesByTaskReturn.getCode())) {
                List<FlowNodeVo> datas = nextNodesByTaskReturn.getDatas();
                if (datas.size() > 1 ) {
                    for (FlowNodeVo flowNodeVo : datas) {
                        if (parse.containsKey(data1.getNodeId() + "NextNode")) {
                            String s = parse.get(data1.getNodeId() + "NextNode");
                            if (Objects.equals(s, (flowNodeVo.getNodeId()))) {
                                NextNodeDto dto = new NextNodeDto();
                                dto.setGateway(flowNodeVo.getGateway());
                                dto.setInMultiInstance(flowNodeVo.isInMultiInstance());
                                dto.setGateway(flowNodeVo.getGateway());
                                dto.setInSubProcess(flowNodeVo.isInSubProcess());
                                dto.setNodeId(flowNodeVo.getNodeId());
                                dto.setNodeName(flowNodeVo.getNodeName());
                                // 根据流程角色获取用户
                                List<NextNodeUserDto> processUser = this.getProcessUser(processDefinitionKey, flowNodeVo.getNodeId(), worksModelByBusinessKey);
                                dto.setUsers(processUser);
                                nextNodeDtos.add(dto);
                            }
                        }
                    }

                } else if (datas.size() > 0){
                    NextNodeDto dto = new NextNodeDto();
                    dto.setGateway(datas.get(0).getGateway());
                    dto.setInMultiInstance(datas.get(0).isInMultiInstance());
                    dto.setGateway(datas.get(0).getGateway());
                    dto.setInSubProcess(datas.get(0).isInSubProcess());
                    dto.setNodeId(datas.get(0).getNodeId());
                    dto.setNodeName(datas.get(0).getNodeName());
                    // 根据流程角色获取用户
                    List<NextNodeUserDto> processUser = this.getProcessUser(processDefinitionKey, datas.get(0).getNodeId(), worksModelByBusinessKey);
                    dto.setUsers(processUser);
                    nextNodeDtos.add(dto);
                }
            }
            return nextNodeDtos;

        }
        return null;
    }

    @Override
    public List<CommonComboDto> getProcess(String key) {
        List<CommonComboDto> commonComboDtos = new ArrayList<CommonComboDto>();
        ReturnVo<FlowNodeVo> asset_add = iApiFlowableProcessDefinitionService.getUserTaskByProcessDefinitionKey(key);
        if (ReturnCode.SUCCESS.equals(asset_add.getCode())) {
            List<FlowNodeVo> datas = asset_add.getDatas();
            datas.stream().forEach(o -> {
                if (!FlowableInfo.FLOW_SUBMITTER_ID.equals(o.getNodeId())) {
                    CommonComboDto commonComboDto = new CommonComboDto();
                    commonComboDto.setValue(o.getNodeId());
                    commonComboDto.setText(o.getNodeName());
                    commonComboDtos.add(commonComboDto);
                }
            });
        }
        return commonComboDtos;
    }

    @Override
    public void revorkFlow(String url, String processInstanceId, String suggestContent) {
        UserInfoDto userInfo = ((SpringSecureUserInfo) MineSecureUtility.currentUser()).getUserInfo();
        UserInfoDto userInfo1 = userService.getUserInfo(userInfo.getId());
        String emp_no = userInfo1.getPropertyMap().get("EMP_NO").toString();

        RevokeProcessVo revokeProcessVo = new RevokeProcessVo();
        revokeProcessVo.setProcessInstanceId(processInstanceId);
        revokeProcessVo.setUserCode(emp_no);
        revokeProcessVo.setMessage(suggestContent);

        apiFlowableWorkDetailService.revokeProcess(revokeProcessVo);

        //修改审批状态
        ReturnVo<ProcessInstanceVo> byProcessInstanceId = iApiFlowableProcessInstanceService.getByProcessInstanceId(processInstanceId);
        ProcessInstanceVo data = byProcessInstanceId.getData();
        String businessKey = data.getBusinessKey();
        WorksDto worksModelByBusinessKey = this.getWorksModelByBusinessKey(businessKey);
        worksModelByBusinessKey.setWorkStatus(FlowableInfo.WORKSTATUS.撤回);
        this.updateWorksModel(worksModelByBusinessKey);

        //修改业务状态
        updateBusinessStatus(worksModelByBusinessKey);

        //发送给待办
        //sendToDaiBan(url, data.getFormName(), processInstanceId);
    }

    @Override
    public List<ApproveHistoryDto> getApproveHistory(String processInstanceId) {
        List<ApproveHistoryDto> approveHistoryDtos = new ArrayList<>();
        ReturnVo<CommentVo> commentsByProcessInstanceId = apiFlowableWorkDetailService.getCommentsByProcessInstanceId(processInstanceId);
        if (ReturnCode.SUCCESS.equals(commentsByProcessInstanceId.getCode())) {
            List<CommentVo> datas = (List<CommentVo>)commentsByProcessInstanceId.getData();
            datas.stream().forEach(o -> {
                ApproveHistoryDto approveHistoryDto = new ApproveHistoryDto();
                approveHistoryDto.setTaskId(o.getTaskId());
                approveHistoryDto.setUserId(o.getUserId());
                List<UserInfoDto> rowData = userService.getUserInfo("{$and:[{sysMark:'" + IBaseService.SYSMARK + "'},{'propertyMap.EMP_NO':'" + o.getUserId() + "'}]}", null, null).getRowData();
                String userName = rowData.get(0).getChsName();
                approveHistoryDto.setUserName(userName);
                approveHistoryDto.setProcessInstanceId(o.getProcessInstanceId());
                approveHistoryDto.setMessage(o.getMessage());
                approveHistoryDto.setApproveTime(VGUtility.toDateStr(o.getTime(), "yyyy-MM-dd HH:mm:ss"));
                approveHistoryDto.setType(o.getType());
                approveHistoryDto.setTypeName(CommentTypeEnum.getEnumMsgByType(o.getType()));

                ReturnVo<TaskVo> taskById = apiFlowableTaskService.getTaskById(o.getTaskId());
                if (ReturnCode.SUCCESS.equals(taskById.getCode())) {
                    TaskVo data = taskById.getData();
                    approveHistoryDto.setTaskName(data.getTaskName());
                } else if (ReturnCode.FAIL.equals(taskById.getCode())) {
                    approveHistoryDto.setTaskName("拟稿");
                }
                approveHistoryDtos.add(approveHistoryDto);
            });
        }
        return approveHistoryDtos;
    }

    @Override
    public List<CommonComboDto> getWorkFlowList() {
        List<CommonComboDto> commonComboDtos = new ArrayList<CommonComboDto>();
        FlowableInfo.WorkFlowEnum[] values = FlowableInfo.WorkFlowEnum.values();
        for (int i = 0; i < values.length; i++) {
            String key = values[i].getKey();
            String value = values[i].getValue();
            CommonComboDto commonComboDto = new CommonComboDto();
            commonComboDto.setValue(value);
            commonComboDto.setText(key);
            commonComboDtos.add(commonComboDto);
        }
        return commonComboDtos;
    }

    @Override
    public WorksDto getWorksModelByApproveCode(String approveCode) {
        WorksModel byApprovalNumber = worksDao.findByApprovalNumber(approveCode);
        if (!VGUtility.isEmpty(byApprovalNumber)) {
            WorksDto worksDto = new WorksDto();
            BeanUtils.copyProperties(byApprovalNumber, worksDto);
            return worksDto;
        }
        return null;
    }
}

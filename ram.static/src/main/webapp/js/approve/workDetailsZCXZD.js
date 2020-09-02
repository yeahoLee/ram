var nextNodeData;
var createWorkId;
var receiveUserId;
var changeUserId;
var workId;
var processInstanceId;
var nextNodeId;
var nodekey;
var selectStateMachineLineId;
var currentNodeId;
var currentNodeState;
var majorList;							// 当前合同清单中对应的专业
var selectMajor = new Array();
var selectUserVsMajor = new Array();	// 用户和专业关系：用户:专业，没有则为空，
var arrayLog = [];
var flagLog = true;

function agree() {
    var currentNodeState = $('#currentNodeState').val();
    var suggestContent = $('#suggestContent').val();
    if (suggestContent.length == 0) {
        suggestContent = "同意";
    }
    // 流程结束
    if (currentNodeState == 2) {
        //$.messager.progress({title: '提示', msg: '正在处理。。。', text: '请稍等'});
        $.ajax({
            type: 'post',
            url: 'approve/finishing_work',
            dataType: 'json',
            data: {
                workId: $('#workId').val(),
                taskId: $('#taskId').val(),
                suggestContent: suggestContent
            },
            success: function (data) {
            },
            error: function (data) {
                //$.messager.progress('close');
                AjaxErrorHandler(data);
            }
        });
        $.messager.alert('提示', '正在处理审批数据', 'info', function (r) {
            windowsClose();
        });
    } else {
        getNextNode();
    }
}

function getNextNode() {

    // 清空数据
    receiveUserId = "";
    nextNodeId = "";
    selectStateMachineLineId = "";
    $.ajax({
        type: 'get',
        url: 'approve/get_next_node',
        dataType: 'json',
        data: {
            procInstId: $('#procInstId').val(),
            taskId: $('#taskId').val()
        },
        success: function (data) {
            $('#confirmDialog').dialog('open');
            $('#confirmType').val(0);
            $("#next").html('');
            $('#next').append('<span>&nbsp;同意:</span><br>');
            nextNodeData = data;
            majorList = data[0].majors;
            if (data.length == 1) {
                $('#next').append('<input type="radio" checked="checked" onClick="radioAllClick(this,0)" style="width:30px" owner="' + data[0].nodeValue + '" line="' + data[0].stateMachineLineId + '" name="action" value="' + data[0].nodeId + '" label="' + 0 + '" text="' + data[0].nodeName + '">' + data[0].nodeName);
                $('input[name="action"]:checked').click();
            } else {
                for (var i = 0; i < data.length; i++) {
                    $('#next').append('<input type="radio" checked="checked" onClick="radioAllClick(this,' + i + ')" style="width:30px" owner="' + data[i].nodeValue + '" line="' + data[0].stateMachineLineId + '" name="action" value="' + data[i].nodeId + '" label="' + i + '" text="' + data[i].nodeName + '">' + data[i].nodeName + '<br>');
                }
            }
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

/**
 * 是否在选中的数据中
 * @param selectArray
 * @param nodeIndex
 * @param userIndex
 * @returns {Boolean}
 */
function inSelectData(selectArray, nodeIndex, userIndex) {
    for (var i = 0; i < selectArray.length; i++) {
        if (selectArray[i].nodeIndex == nodeIndex
            && selectArray[i].userIndex == userIndex) {
            return true;
        }
    }
    return false;
}

/**
 * 显示选择的所有用户
 */
function showInfo() {
    $('#minute').html('');
    var actionName = "";
    var actionArray = $("input[name=action]");
    for (var j = 0; j < actionArray.length; j++) {
        if ($(actionArray[j]).attr('checked')) {
            actionName = $(actionArray[j]).attr('text');
            // 单一用户
            nextNodeId = $(actionArray[j]).attr('value');
            selectStateMachineLineId = $(actionArray[j]).attr('line');
        }
    }

    var usersName = "";
    receiveUserId = "";
    selectMajor = [];
    selectUserVsMajor = [];
    var userArray = $("input[name=users]");
    for (var i = 0; i < userArray.length; i++) {
        if (userArray[i].checked) {
            usersName += $(userArray[i]).attr('lable') + '&nbsp;';
            var userId = $(userArray[i]).attr('value');
            // 获取选择的用户
            if (receiveUserId.length == 0) {
                receiveUserId = userId;
            } else {
                receiveUserId = receiveUserId + "," + userId;
            }
            var major = $(userArray[i]).attr('text');
            var majorId = $(userArray[i]).attr('majorId');
            // 存储选中的专业
            if (major != null && major.length > 0) {
                if ($.inArray(major, selectMajor) == -1) {
                    selectMajor.push(major);
                    // 组装格式为 [用户:专业],[用户:专业]
                    var userVsMajor = userId + ":" + majorId;
                    selectUserVsMajor.push(userVsMajor);
                }
            }
        }
    }

    $('#minute').html('已选步骤:&nbsp;' + actionName + '<br><br>已选人员:&nbsp;' + usersName);
}

function radioAllClick(radio, index) {
    showInfo();
    var users = nextNodeData[index].users;
    $('#usersList').html('');
    // 是否是多选// 0-普通，1-多选，2-等待
    var flag = nextNodeData[index].flag;
    if (flag == 1) {
        for (var i = 0; i < users.length; i++) {
            var labelName = users[i].chsName;
            if (users[i].majorName != null && users[i].majorName.length > 0) {
                labelName += "(" + users[i].majorName + ")";
            }
            $('#usersList').append(
                '<input type="checkbox"'
                + ' onClick="showInfo()"'
                + ' style="width:30px"'
                + ' name="users"'
                + ' text="' + users[i].majorName + '"'
                + ' majorId="' + users[i].majorId + '"'
                + ' value="' + users[i].id + '"'
                + ' lable="' + users[i].chsName + '"'
                + '>' + labelName);
            $('#usersList').append('<br>');
            // 默认选择专业只有唯一一个用户的数据
            var autoCheck = true;
            for (var j = 0; j < users.length; j++) {
                if (users[j].majorName == users[i].majorName
                    && users[j].userName != users[i].userName) {
                    autoCheck = false;
                    break;
                }
            }
            if (autoCheck) {
                $('#usersList input:last').attr('checked', 'checked');
                showInfo();
            }
        }
    } else {
        for (var i = 0; i < users.length; i++) {
            var labelName = users[i].chsName;
            if (users[i].majorName != null && users[i].majorName.length > 0) {
                labelName += "(" + users[i].majorName + ")";
            }
            $('#usersList').append(
                '<input type="radio"'
                + ' onClick="showInfo()"'
                + ' style="width:30px"'
                + ' name="users"'
                + ' text="' + users[i].majorName + '"'
                + ' majorId="' + users[i].majorId + '"'
                + ' value="' + users[i].id + '"'
                + ' lable="' + users[i].chsName + '"'
                + '>' + labelName);
            $('#usersList').append('<br>');
        }
    }
}

/**
 * 点击选择用户
 * @param nodeIndex
 * @param userIndex
 */
function userListClick(nodeIndex, userIndex) {
    // 更新选中的用户数组
    addOrRemoveIndex(nodeIndex, userIndex);
    radioClick(nodeIndex, userIndex)
    showTotalInfo();
}

/**
 * 修改radio状态
 * @param nodeIndex
 * @param userIndex
 */
function radioClick(nodeIndex, userIndex) {
    if (nodeIndex == undefined || userIndex == undefined) {
        return;
    }
    // 只修改radio状态
    if ($('#usersList input[type=radio]') == null || $('#usersList input[type=radio]').length == 0) {
        return;
    }
    var existIndex = -1;
    for (var i = 0; i < selectDataIndex.length; i++) {
        if (selectDataIndex[i].nodeIndex == nodeIndex
            && selectDataIndex[i].userIndex == userIndex) {
            existIndex = i;
        }
    }
    if (existIndex != -1) {
        // radio处于被选中
        $('#usersList input[type=radio]:eq(' + userIndex + ')').attr('checked', true);
    } else {
        // radio处于未选中
        $('#usersList input[type=radio]:eq(' + userIndex + ')').attr('checked', false);
    }
}

/**
 * 如果存在就移除，不存在添加
 * @param nodeIndex
 * @param userIndex
 */
function addOrRemoveIndex(nodeIndex, userIndex) {
    if (nodeIndex == undefined || userIndex == undefined) {
        return;
    }
    var existIndex = -1;
    for (var i = 0; i < selectDataIndex.length; i++) {
        if (selectDataIndex[i].nodeIndex == nodeIndex
            && selectDataIndex[i].userIndex == userIndex) {
            existIndex = i;
        }
    }
    if (existIndex == -1) {	// 不存在添加
        var selectIndexs = {"nodeIndex": nodeIndex, "userIndex": userIndex};
        selectDataIndex.push(selectIndexs);
    } else {
        selectDataIndex.splice(existIndex, 1);
    }
    // radio 清除所有的
    if ($('#usersList input[type=radio]') != null && $('#usersList input[type=radio]').length > 0) {
        for (var i = 0; i < selectDataIndex.length; i++) {
            if (selectDataIndex[i].nodeIndex == nodeIndex && selectDataIndex[i].userIndex != userIndex) {
                selectDataIndex.splice(i, 1);
            }
        }
    }
}

/**
 * 发送确认
 */
function confirmSubmit() {
    // 专业存在，当前下一节点为专业工程师
//	if(majorList != null && majorList.length > 0) {
//		for(var i=0; i<majorList.length; i++) {
//			// 合同清单中的专业是否都被选
//			if($.inArray(majorList[i], selectMajor) == -1) {
//				$.messager.alert('提示', '请选择专业['+majorList[i]+']对应的专业工程师');
//				return;
//			}
//		}
//	}
//	var majors = "";
//	for(var i=0; i<selectUserVsMajor.length; i++) {
//		if(i == 0) {
//			majors = selectUserVsMajor[0];
//		} else {
//			majors = majors + "," + selectUserVsMajor[i]
//		}
//	}
    var suggestContent = $('#suggestContent').val();
    if (suggestContent.length == 0) {
        suggestContent = "同意";
    }
    $.ajax({
        type: 'POST',
        url: 'approve/send_work',
        dataType: 'json',
        data: {
            workId: $('#workId').val(),
            receiveUserId: receiveUserId,
//			majors: majors,
            suggestContent: suggestContent,
            taskId: $('#taskId').val(),
            nextNodeId: nextNodeId,
            procInstId: $('#procInstId').val()
        },
        success: function (data) {
            window.location.reload();
//			$.messager.show({
//				title:'提示',
//				msg:'发送成功',
//				timeout:5000,
//				showType:'slide'
//			});
//			windowsClose();
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

$(function () {
    /*$.ajax({
        type: 'get',
        url: 'work_flow/get_work_details',
        data: {
            workId: $('#workId').val(),
            taskId: $('#taskId').val()
        },
        dataType: 'json',
        success: function(data){
            $('#workTitle').html(data.workTitle);
            $('#workContent').val(data.workContent);
            $('#supplierUid').val(data.supplierUid);
            $('#contractName').val(data.contractCode);
//			$('#selectSupplier').val(data.supplierName);
//			$('#majorId').val(data.majorId);
//			$('#majorName').val(data.majorName);
//			$('#noNeedPerson').val(data.noNeedPerson);
            $('#executionId').val(data.executionId);
            currentNodeId = data.currentNodeId;
            currentNodeState = data.nodeState;
            processInstanceId = data.processInstanceId;
            var status = data.workStatus;
            loadGridsData();
            setContractAmounts();
        },
        error: function(e){
            AjaxErrorHandler(e);
        }
    });*/

    var recId = $('#receiptDtoId').val();
    $('#dataGridTable').datagrid({
        url: 'receipt/get_materials',
        fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        fit: false,
        singleSelect: true,
        queryParams: {recId: recId},
        title: '资产清单',
        loadMsg: '程序处理中，请稍等...',
        columns: [[
            {field: 'materialCode', title: '物资编码'},
            {field: 'combinationAssetName', title: '资产名称'},
            {field: 'assetTypeStr', title: '资产类型'},
            {field: 'specAndModels', title: '规格型号'},
            {field: 'seriesNum', title: '序列号'},
            {field: 'unitOfMeasStr', title: '计量单位'},
            {field: 'assetBrand', title: '品牌'},
            {field: 'purcPrice', title: '采购价'},
            {field: 'equiOrigValue', title: '资产原值'},
            {field: 'techPara', title: '技术参数'},
            {field: 'remark', title: '备注'},//备注
            {field: 'companyStr', title: '所属公司'},
            {field: 'belongLineStr', title: '所属线路'},
            {field: 'buyDate', title: '购置日期'},
            {field: 'manageDeptStr', title: '主管部门'},
            {field: 'managerStr', title: '资产管理员'},
            {field: 'savePlaceStr', title: '安装位置'},
            {field: 'assetSource', title: '资产来源'},
            {field: 'contractNum', title: '合同编号'},
            {field: 'sourceUser', title: '联系人'},
            {field: 'sourceContactInfo', title: '联系方式'},
            {field: 'tendersNum', title: '标段编号'},
            {field: 'mainPeriod', title: '维保期'},
            {field: 'prodTime', title: '出厂日期'}
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });

    $('#approveHistoryTable').datagrid({
        url: 'approve/get_history_by_id',
        fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        fit: false,
        singleSelect: true,
        queryParams: {recId: recId},
        title: '审批历史记录',
        loadMsg: '程序处理中，请稍等...',
        columns: [[
            {field: 'createTimestamp', title: '创建时间'},
            {field: 'workStatusStr', title: '审批环节'},
            {field: 'currentActName', title: '当前环节名称'},
            {field: 'currentUserName', title: '当前环节人员'},
            {field: 'nextActName', title: '下个环节名称'},
            {field: 'nextUserName', title: '下个环节人员'},
            {field: 'approvalOpinion', title: '审批意见'},
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });

    $("#confirmDialog").dialog({
        title: '确认审核',
        width: 530,
        height: 430,
        closed: true,
        modal: true,
        buttons: [{
            text: '确定',
            handler: function () {
                var confirmType = $('#confirmType').val();
                if (confirmType == 0) {
                    confirmSubmit();
                } else {
                    confirmRejectSubmit();
                }
                $("#confirmDialog").dialog('close');
            }
        }, {
            text: '取消',
            handler: function () {
                $("#confirmDialog").dialog('close');
            }
        }]
    });
})
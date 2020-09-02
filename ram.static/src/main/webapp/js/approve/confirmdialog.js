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

function windowsClose() {
    try {
        window.opener.location.href = window.opener.location.href;
    } catch (e) {
    }
    window.opener = null;
    window.open('', '_self');
    window.close();
}

function comeBackFlow() {
    $.messager.confirm('确定', '你确定要撤回?', function (r) {
        if (r) {
            $.ajax({
                type: 'post',
                url: 'work_flow/come_back_work',
                dataType: 'json',
                data: {
                    workId: $('#workId').val(),
                    taskId: $('#taskId').val(),
                    processInstanceId: $('#processInstanceId').val()
                },
                success: function (data) {
                    //console.log(data.success);
                    if (data.success == true) {
                        $.messager.alert('提示', '撤回成功!', '', function () {
                            windowsClose();
                        });
                    } else {
                        $.messager.alert('提示', '撤回失败!会签或不在下一个环节是无法撤回!', '', function () {
                            windowsClose();
                        });
                    }

                },
                error: function (data) {
                    //$.messager.progress('close');
                    AjaxErrorHandler(data);
                }
            });
        }
    });
}

function agree() {
    var suggestContent = $('#suggestContent').val();
    if (suggestContent.length == 0) {
        suggestContent = "同意";
    }
    // 流程结束
    if (currentNodeState == 2) {
        //$.messager.progress({title: '提示', msg: '正在处理。。。', text: '请稍等'});
        $.ajax({
            type: 'post',
            url: 'work_flow/finishing_work',
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
        getNextNode(false, currentNodeId, $('#workId').val());
    }
}

function contractTemplateExport() {
    var contractId = $('#contractId').val();
    if (contractId) {
        location.href = "contract/contract_export_template?contractId=" + contractId + '&supplierUid=' + $('#supplierUid').val();
    }
}

function disagree() {
    //getRejectNextNode(currentNodeId, $('#workId').val());
    confirmRejectSubmit();
}

// 发送
function send() {
    var noNeedPerson = $('#noNeedPerson').val();
    if (noNeedPerson == "true") {
        // 子流程退回到建设工程师,建设工程师重新发起
        var suggestContent = $('#suggestContent').val();
        if (suggestContent.length == 0) {
            suggestContent = "同意";
        }
        $.ajax({
            type: 'post',
            url: 'work_flow/send_work_without_person',
            dataType: 'json',
            data: {
                workId: $('#workId').val(),
                suggestContent: suggestContent,
                executionId: $('#executionId').val(),
                taskId: $('#taskId').val()
            },
            success: function (data) {
                $.messager.show({
                    title: '提示',
                    msg: '发送成功',
                    timeout: 5000,
                    showType: 'slide'
                });
                windowsClose();
            },
            error: function (data) {
                AjaxErrorHandler(data);
            }
        });
    } else {
        getNextNode(true, "", $('#workId').val());
    }
}

// 签收
function receive() {
    $.ajax({
        type: 'post',
        url: 'work_flow/receive_work',
        data: {
            workId: $('#workId').val()
        },
        dataType: 'json',
        success: function (data) {
            $('.agree').show();
            $('.disagree').show();
            $('.receive').hide();
            $('.send').hide();
        },
        error: function (e) {
            AjaxErrorHandler(e);
        }
    });
}

// 删除流程
function deleteWorkFlow() {
    $.ajax({
        type: 'post',
        url: 'work_flow/delete_work',
        data: {
            workId: $('#workId').val(),
            taskId: $('#taskId').val()
        },
        dataType: 'json',
        success: function (data) {
            windowsClose();
        },
        error: function (e) {
            AjaxErrorHandler(e);
        }
    });
}

//跳转流程
function change() {
    $.ajax({
        type: 'get',
        url: 'work_flow/get_changeList_work',
        dataType: 'json',
        data: {
            taskId: $('#taskId').val(),
            processInstanceId: $('#processInstanceId').attr('value')
        },
        success: function (data) {
            $("#changesDialog").dialog('open');
            $('#changeNext').html('');
            var i = 0;
            $.each(data, function (key, value) {
                $('#changeNext').append('<input type="radio" onClick="radioChangeAllClick(this,' + i + ')" name="changeAction" key="' + key + '"  text="' + value + '"  style="width:30px">' + value + '<br>');
                i++;
            })
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

//已阅
function read() {
    var procId = $('#procId').val();
    $.ajax({
        type: 'post',
        url: 'work_flow/delete_ou_message',
        dataType: 'json',
        data: {
            procId: procId
        },
        success: function (data) {
            windowsClose();
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

/***************************发起审批*************************/

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
            $("#confirmDialog").dialog('open');
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

/*
 * 获取拒绝下个环节
 */
function getRejectNextNode(nodeId, workId) {
    // 清空数据
    receiveUserId = "";
    nextNodeId = "";
    selectStateMachineLineId = "";
    $.ajax({
        type: 'get',
        url: 'work_flow/get_reject_next_node',
        dataType: 'json',
        data: {
            nodeId: nodeId,
            workId: workId
        },
        success: function (data) {
            $("#confirmDialog").dialog('open');
            $('#confirmType').val(1);
            $("#next").html('');
            $('#next').append('<span>&nbsp;不同意:</span><br>');
            nextNodeData = data;
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

function radioAllClick(radio, index) {
    showInfo();
    var users = nextNodeData[index].users;
    $('#usersList').html('');
    // 是否是多选// 0-普通，1-多选，2-等待
    var flag = nextNodeData[index].flag;
    if (flag == 1) {
        for (var i = 0; i < users.length; i++) {
            var labelName = users[i].userName;
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
                + ' value="' + users[i].userId + '"'
                + ' lable="' + users[i].userName + '"'
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
            var labelName = users[i].userName;
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
                + ' value="' + users[i].userId + '"'
                + ' lable="' + users[i].userName + '"'
                + '>' + labelName);
            $('#usersList').append('<br>');
        }
    }
}

function radioChangeAllClick(radio, index) {
    var key = "";
    var actionArray = $("input[name=changeAction]");
    for (var j = 0; j < actionArray.length; j++) {
        if ($(actionArray[j]).attr('checked')) {
            key = $(actionArray[j]).attr('key');
        }
    }
    $.ajax({
        type: 'get',
        url: 'work_flow/get_next_change_node',
        dataType: 'json',
        data: {
            processInstanceId: $('#processInstanceId').attr('value'),
            activityId: key
        },
        success: function (data) {
            $('#changeUsersList').html('');
            var result = data;
            var flag = result[0].flag;
            var users = result[0].users;
            if (flag == 1) {
                for (var i = 0; i < users.length; i++) {
                    var labelName = users[i].userName;
                    if (users[i].majorName != null && users[i].majorName.length > 0) {
                        labelName += "(" + users[i].majorName + ")";
                    }
                    $('#changeUsersList').append(
                        '<input type="checkbox"'
                        + ' onClick="changeShowInfo()"'
                        + ' style="width:30px"'
                        + ' name="changeUsers"'
                        + ' text="' + users[i].majorName + '"'
                        + ' majorId="' + users[i].majorId + '"'
                        + ' value="' + users[i].userId + '"'
                        + ' lable="' + users[i].userName + '"'
                        + '>' + labelName);
                    $('#changeUsersList').append('<br>');
                }
            } else {
                for (var i = 0; i < users.length; i++) {
                    var labelName = users[i].userName;
                    if (users[i].majorName != null && users[i].majorName.length > 0) {
                        labelName += "(" + users[i].majorName + ")";
                    }
                    $('#changeUsersList').append(
                        '<input type="radio"'
                        + ' onClick="changeShowInfo()"'
                        + ' style="width:30px"'
                        + ' name="changeUsers"'
                        + ' text="' + users[i].majorName + '"'
                        + ' majorId="' + users[i].majorId + '"'
                        + ' value="' + users[i].userId + '"'
                        + ' lable="' + users[i].userName + '"'
                        + '>' + labelName);
                    $('#changeUsersList').append('<br>');
                }
            }
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}


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
        if ($(userArray[i]).attr('checked')) {
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

function changeShowInfo() {
    $('#changeMinute').html('');
    var actionName = "";
    var actionArray = $("input[name=changeAction]");
    for (var j = 0; j < actionArray.length; j++) {
        if ($(actionArray[j]).attr('checked')) {
            actionName = $(actionArray[j]).attr('text');
            // 单一用户
            nodekey = $(actionArray[j]).attr('key');
            selectStateMachineLineId = $(actionArray[j]).attr('line');
        }
    }

    var usersName = "";
    changeUserId = "";
    selectMajor = [];
    selectUserVsMajor = [];
    var userArray = $("input[name=changeUsers]");
    for (var i = 0; i < userArray.length; i++) {
        if ($(userArray[i]).attr('checked')) {
            usersName += $(userArray[i]).attr('lable') + '&nbsp;';
            var userId = $(userArray[i]).attr('value');
            // 获取选择的用户
            if (changeUserId.length == 0) {
                changeUserId = userId;
            } else {
                changeUserId = changeUserId + "," + userId;
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

    $('#changeMinute').html('已选步骤:&nbsp;' + actionName + '<br><br>已选人员:&nbsp;' + usersName);
}

/**
 * 跳转确认
 */
function changeSubmit() {

    $.ajax({
        type: 'post',
        url: 'work_flow/change_work',
        dataType: 'json',
        data: {
            workId: $('#workId').val(),
            changeUserId: changeUserId,
            taskId: $('#taskId').val(),
            nodeId: nodekey
        },
        success: function (data) {
            $.messager.show({
                title: '提示',
                msg: '发送成功',
                timeout: 5000,
                showType: 'slide'
            });
            windowsClose();
        },
        error: function (data) {
            $.messager.show({
                title: '提示',
                msg: '跳转失败!主流程无法跳转到子流程!',
                timeout: 5000,
                showType: 'slide'
            });
        }
    });
}

/**
 * 发送确认
 */
function confirmSubmit() {
    // 专业存在，当前下一节点为专业工程师
    if (majorList != null && majorList.length > 0) {
        for (var i = 0; i < majorList.length; i++) {
            // 合同清单中的专业是否都被选
            if ($.inArray(majorList[i], selectMajor) == -1) {
                $.messager.alert('提示', '请选择专业[' + majorList[i] + ']对应的专业工程师');
                return;
            }
        }
    }
    var majors = "";
    for (var i = 0; i < selectUserVsMajor.length; i++) {
        if (i == 0) {
            majors = selectUserVsMajor[0];
        } else {
            majors = majors + "," + selectUserVsMajor[i]
        }
    }
    var suggestContent = $('#suggestContent').val();
    if (suggestContent.length == 0) {
        suggestContent = "同意";
    }
    $.ajax({
        type: 'post',
        url: 'work_flow/send_work',
        dataType: 'json',
        data: {
            workId: $('#workId').val(),
            receiveUserId: receiveUserId,
            majors: majors,
            suggestContent: suggestContent,
            taskId: $('#taskId').val(),
            nextNodeId: nextNodeId
        },
        success: function (data) {
            $.messager.show({
                title: '提示',
                msg: '发送成功',
                timeout: 5000,
                showType: 'slide'
            });
            windowsClose();
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

/**
 * 不同意确认
 */
function confirmRejectSubmit() {
    var suggestContent = $('#suggestContent').val();
    if (suggestContent.length == 0) {
        suggestContent = "不同意";
    }
    $.ajax({
        type: 'post',
        url: 'work_flow/reject_send_work',
        dataType: 'json',
        data: {
            workId: $('#workId').val(),
            suggestContent: suggestContent,
            taskId: $('#taskId').val()
        },
        success: function (data) {
            $.messager.show({
                title: '提示',
                msg: '发送成功',
                timeout: 5000,
                showType: 'slide'
            });
            windowsClose();
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function loadGridsData() {
    $('#contractInfoMaintainGrid').datagrid({
        fitColumns: true,
        singleSelect: true,
        pagination: true,
        pageList: [10, 15, 20, 50],
        pageSize: 15,
        toolbar: [{
            text: '导出数据',
            iconCls: 'icon-save',
            handler: function () {
                var contractId = $('#contractId').val();
                var supplierId = $('#supplierUid').val();
                var majorId = $('#majorId').val();
                var type = $('#expenditureType').combobox('getValue');
                type = type == "" ? "0" : type;
                if (contractId) {
                    var url = "contract/contract_export_template_by_expenditure_type?contractId=" + contractId + '&supplierUid=' + supplierId + '&majorId=' + majorId + '&type=' + type;
                    window.open(url, '_blank');
                }
            }
        }],
        columns: [[
            {field: 'contractCode', title: '合同编号'},
            {field: 'maximoSerialNumber', title: 'Maximo层次序列号'},
            {field: 'maximoStructure', title: 'Maximo层次结构'},
            {field: 'materialCode', title: '物资编码(EBS)'},
            {field: 'materialDescription', title: '物资描述(EBS)'},
            {field: 'cateLevel1', title: '一级'},
            {field: 'cateLevel2', title: '二级'},
            {field: 'cateLevel3', title: '三级'},
            {field: 'supplierName', title: '供应商'},
            {field: 'installationLocation', title: '安装位置编号'},
            {field: 'locationName', title: '位置名称（参考）'},
            {field: 'assetType', title: '资产类型'},
            {field: 'major', title: '专业'},
            {field: 'owningDepartment', title: '所属部门'},
//				{field:'storageDepartment',title:'保管部门' },
//				{field:'keeper',title:'保管人 ' },
            {field: 'count', title: '数量'},
            {field: 'price', title: '单价'},
            {field: 'foreignUnitPrice', title: '外币单价'},
            {
                field: 'totalPrice', title: '总价',
                formatter: function (value, row, index) {
                    return fmoney(value, 2);
                }
            },
            {
                field: 'totalForeignPrice', title: '外币总价',
                formatter: function (value, row, index) {
                    return fmoney(value, 2);
                }
            },
            {field: 'assetConsolidation', title: '资产合并'},
            {field: 'listPriceNo', title: '开项号'},
            {field: 'listQuantity', title: '合同数量'},
            {field: 'listUnitPrice', title: '本位币单价（合同）'},
            {field: 'listForeignUnitPrice', title: '外币单价(合同如为外币)'},
            {field: 'listName', title: '开项名称'},
            {field: 'listCode', title: '项目编号'},
            {field: 'listSpecification', title: '项目特征'}
        ]],
        rowStyler: function (index, row) {
            var color = 'background-color:white';
            if (row.outContract == 1) {
                color = 'background-color:#BFEFFF';
            }
            return color;
        },
        loader: function (param, success, error) {
            var contractId = $('#contractId').val();
            var supplierId = $('#supplierUid').val();
            var majorId = $('#majorId').val();
            if (contractId == null
                || contractId.length == 0
                || supplierId == null
                || supplierId.length == 0
            ) {
                success([]);
                return;
            }
            param.contractId = contractId;
            param.supplierId = supplierId;
            var type = $('#type').val();
            if (type != '5') {
                param.majorId = majorId;
            }
            if (param.type == undefined || param.type == null) {
                param.type = 0;
            }
            //param.type = 0;		// 0-设备清单,1-流动资产清单,2-待摊费用清单
            $.ajax({
                url: 'contract/get_contract_template_by_expenditure_type',
                type: 'post',
                data: param,
                success: function (data) {
                    success(data['gridData']);
                    var totalPrice = data['gridData'].totalPrice == null ? "" : data['gridData'].totalPrice;
                    var totalForeignPrice = data['gridData'].totalForeignPrice == null ? "" : data['gridData'].totalForeignPrice;
                    $('#totalMoney').val(fmoney(totalPrice, 2));
                    $('#totalForeignMoney').val(fmoney(totalForeignPrice, 2));
                },
                error: function (e) {
                    AjaxErrorHandler(e);
                }
            });
        }
    });

    $('#expenditureType').combobox({
        valueField: 'value',
        textField: 'label',
        data: [{
            label: '设备资产清单',
            value: 0
        }, {
            label: '流动资产清单',
            value: 1
        }, {
            label: '待摊费用清单',
            value: 2
        }],
        onSelect: function (rec) {
            $('#contractInfoMaintainGrid').datagrid('load', {
                type: rec.value
            });
        }
    });
}

function uploadTemplateSubmit() {
    $('#uploadtTemplateForm').form('submit', {
        url: 'work_flow/upload_attachment',
        onSubmit: function (param) {
            param.workId = $('#workId').val();
        },
        success: function (data) {
            $('#uploadtTemplateForm').form('reset');
            $('#uploadTemplateDialog').dialog('close');
            $('#attachmentGrid').datagrid('reload');
        },
        error: function (data) {
            $('#uploadTemplateDialog').dialog('close');
            AjaxErrorHandler(data);
        }
    })
}

// 删除附件
function deleteAttachment(id) {
    $.ajax({
        url: 'work_flow/delete_attachment',
        type: 'post',
        data: {id: id},
        success: function (data) {
            $('#attachmentGrid').datagrid('reload');
        },
        error: function (e) {
            AjaxErrorHandler(e);
        }
    });
}

function downloadAttachment(id) {
    window.open('work_flow/download_attachment?id=' + id, '_blank')
}

// 设置合同金额
function setContractAmounts() {
    var contractId = $('#contractId').val();
    var supplierUid = $('#supplierUid').val();
    $.ajax({
        url: 'contract/get_contract_amounts',
        type: 'post',
        data: {
            contractId: contractId,
            supplierUid: supplierUid
        },
        success: function (data) {
            if (data != null) {
                var contractTotalMoney = data.contractTotalMoney == null ? 0 : data.contractTotalMoney;
                var contractForeignTotalMoney = data.contractForeignTotalMoney == null ? 0 : data.contractForeignTotalMoney;
                var listTotalMoney = data.listTotalMoney == null ? 0 : data.listTotalMoney;
                var listForeignTotalMoney = data.listForeignTotalMoney == null ? 0 : data.listForeignTotalMoney;
                var balanceTotalMoney = data.balanceTotalMoney == null ? 0 : data.balanceTotalMoney;
                var balanceForeignTotalMoney = data.balanceForeignTotalMoney == null ? 0 : data.balanceForeignTotalMoney;
                $('#contractTotalMoney').val(contractTotalMoney == 0 ? "" : fmoney(contractTotalMoney, 2));
                $('#listTotalMoney').val(listTotalMoney == 0 ? "" : fmoney(listTotalMoney, 2));
                $('#balanceTotalMoney').val(balanceTotalMoney == 0 ? "" : fmoney(balanceTotalMoney, 2));
                $('#contractForeignTotalMoney').val(contractForeignTotalMoney == 0 ? "" : fmoney(contractForeignTotalMoney, 2));
                $('#listForeignTotalMoney').val(listForeignTotalMoney == 0 ? "" : fmoney(listForeignTotalMoney, 2));
                $('#balanceForeignTotalMoney').val(balanceForeignTotalMoney == 0 ? "" : fmoney(balanceForeignTotalMoney, 2));
                $('#contractCurrencyType').val(data.contractCurrencyType == null ? "" : data.contractCurrencyType);
                var faMoneyFirst = data.faMoneyFirst == null ? 0 : data.faMoneyFirst;
                var faForeignMoneyFirst = data.faForeignMoneyFirst == null ? 0 : data.faForeignMoneyFirst;
                var cuxAssetMoneyFirst = data.cuxAssetMoneyFirst == null ? 0 : data.cuxAssetMoneyFirst;
                var cuxAssetForeignMoneyFirst = data.cuxAssetForeignMoneyFirst == null ? 0 : data.cuxAssetForeignMoneyFirst;
                var expMoneyFrist = data.expMoneyFrist == null ? 0 : data.expMoneyFrist;
                var expForeignMoneyFrist = data.expForeignMoneyFrist == null ? 0 : data.expForeignMoneyFrist;
                var totalMoneyFirst = Number(faMoneyFirst) + Number(cuxAssetMoneyFirst) + Number(expMoneyFrist);
                var totalForeignFirst = Number(faForeignMoneyFirst) + Number(cuxAssetForeignMoneyFirst) + Number(expForeignMoneyFrist);
                $('#faMoneyFirst').val(faMoneyFirst == 0 ? "" : fmoney(faMoneyFirst, 2));
                $('#cuxAssetMoneyFirst').val(cuxAssetMoneyFirst == 0 ? "" : fmoney(cuxAssetMoneyFirst, 2));
                $('#expMoneyFrist').val(expMoneyFrist == 0 ? "" : fmoney(expMoneyFrist, 2));
                $('#totalMoneyFirst').val(totalMoneyFirst == 0 ? "" : fmoney(totalMoneyFirst, 2));
                $('#faForeignTotalMoneyFirst').val(faForeignMoneyFirst == 0 ? "" : fmoney(faForeignMoneyFirst, 2));
                $('#cuxAssetForeignMoneyFirst').val(cuxAssetForeignMoneyFirst == 0 ? "" : fmoney(cuxAssetForeignMoneyFirst, 2));
                $('#expForeignMoneyFrist').val(expForeignMoneyFrist == 0 ? "" : fmoney(expForeignMoneyFrist, 2));
                $('#totalForeignMoneyFirst').val(totalForeignFirst == 0 ? "" : fmoney(totalForeignFirst, 2));
            }
        },
        error: function (e) {
            AjaxErrorHandler(e);
        }
    });
}

$(function () {
    var type = $('#type').val();
    // 1-待办，2-已办  3-管理员跳转 4-已阅  5-查询
    if (type != "1") {
        $('#suggestPanel').hide();
    }
    // 获取work
    $.ajax({
        type: 'get',
        url: 'work_flow/get_work_details',
        data: {
            workId: $('#workId').val(),
            taskId: $('#taskId').val()
        },
        dataType: 'json',
        success: function (data) {
            $('#workTitle').html(data.workTitle);
            $('#workContent').val(data.workContent);
            $('#supplierUid').val(data.supplierUid);
            $('#contractName').val(data.contractCode);
            $('#selectSupplier').val(data.supplierName);
            $('#majorId').val(data.majorId);
            $('#majorName').val(data.majorName);
            $('#noNeedPerson').val(data.noNeedPerson);
            $('#executionId').val(data.executionId);
            currentNodeId = data.currentNodeId;
            currentNodeState = data.nodeState;
            processInstanceId = data.processInstanceId;
            var status = data.workStatus;
            loadGridsData();
            setContractAmounts();
        },
        error: function (e) {
            AjaxErrorHandler(e);
        }
    });
    /*var type = $('#type').val();
    // 1-待办，2-已办  3-管理员跳转 4-已阅  5-查询
    if(type == "1") {
        // 获取work
        $.ajax({
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
                $('#selectSupplier').val(data.supplierName);
                $('#majorId').val(data.majorId);
                $('#majorName').val(data.majorName);
                $('#noNeedPerson').val(data.noNeedPerson);
                $('#executionId').val(data.executionId);
                currentNodeId = data.currentNodeId;
                currentNodeState = data.nodeState;
                processInstanceId = data.processInstanceId;
                var status = data.workStatus;
                loadGridsData();
                setContractAmounts();
                if("待发送" == status) {
                    $('.agree').hide();
                    $('.disagree').hide();
                    $('.receive').hide();
                    $('.send').show();
                    $('.delete').show();
                } else if("审批中" == status) {
                    $('.agree').show();
                    $('.disagree').show();
                    $('.receive').hide();
                    $('.send').hide();
                    $('.delete').hide();
                } else if("已审批" == status) {
                    $('.agree').hide();
                    $('.disagree').hide();
                    $('.receive').hide();
                    $('.send').hide();
                    $('.delete').hide();
                } else if("退回" == status) {
                    $('.agree').hide();
                    $('.disagree').hide();
                    $('.receive').hide();
                    $('.send').show();
                    $('.delete').show();
                }
                $('.change').hide();
                $('.read').hide();
            },
            error: function(e){
                AjaxErrorHandler(e);
            }
        });
    } else if(type == "2") {
        $('.agree').hide();
        $('.disagree').hide();
        $('.receive').hide();
        $('.send').hide();
        $('.read').hide();
        $('.change').hide();
        $('#suggestPanel').hide();
        $('.back').show();
        
        $.ajax({
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
                $('#contractName').val(data.contractCode);
                $('#selectSupplier').val(data.supplierName);
                $('#majorId').val(data.majorId);
                $('#majorName').val(data.majorName);
                $('#noNeedPerson').val(data.noNeedPerson);
                $('#executionId').val(data.executionId);
                loadGridsData();
                setContractAmounts();
            },
            error: function(e){
                AjaxErrorHandler(e);
            }
        });
    } else if (type == "3"){
        $('.agree').hide();
        $('.disagree').hide();
        $('.receive').hide();
        $('.send').hide();
        $('.read').hide();
        $('.back').hide();
        $('.change').show();
        $('#suggestPanel').hide();
        
        $.ajax({
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
                $('#contractName').val(data.contractCode);
                $('#selectSupplier').val(data.supplierName);
                $('#majorId').val(data.majorId);
                $('#majorName').val(data.majorName);
                $('#noNeedPerson').val(data.noNeedPerson);
                $('#executionId').val(data.executionId);
                loadGridsData();
                setContractAmounts();
            },
            error: function(e){
                AjaxErrorHandler(e);
            }
        });
    } else if (type == "4"){
        $('.agree').hide();
        $('.disagree').hide();
        $('.receive').hide();
        $('.send').hide();
        $('.change').hide();
        $('.back').hide();
        $('.read').show();
        $('#suggestPanel').hide();
        
        $.ajax({
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
                $('#contractName').val(data.contractCode);
                $('#selectSupplier').val(data.supplierName);
                $('#majorId').val(data.majorId);
                $('#majorName').val(data.majorName);
                $('#noNeedPerson').val(data.noNeedPerson);
                $('#executionId').val(data.executionId);
                loadGridsData();
                setContractAmounts();
            },
            error: function(e){
                AjaxErrorHandler(e);
            }
        });
    } else if (type == "5"){
        $('.agree').hide();
        $('.disagree').hide();
        $('.receive').hide();
        $('.send').hide();
        $('.change').hide();
        $('.back').hide();
        $('.read').hide();
        $('#suggestPanel').hide();
        
        $.ajax({
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
                $('#contractName').val(data.contractCode);
                $('#selectSupplier').val(data.supplierName);
                $('#majorId').val(data.majorId);
                $('#majorName').val(data.majorName);
                $('#noNeedPerson').val(data.noNeedPerson);
                $('#executionId').val(data.executionId);
                loadGridsData();
                setContractAmounts();
            },
            error: function(e){
                AjaxErrorHandler(e);
            }
        });
    }*/

    /*$('#commonSuggest').combobox({
        valueField:'label',
        textField:'value',
        data: [{
            label: '同意',
            value: '同意'
        },{
            label: '不同意',
            value: '不同意'
        }],
        onSelect: function(data) {
            $("#suggestContent").val(data.value);
        }
    });*/

    $('#attachmentGrid').datagrid({
        fit: true,
        height: '200',
        singleSelect: true,
        fitColumns: true,
        pagination: true,
        pageList: [10, 15, 20, 50],
        pageSize: 15,
        toolbar: [{
            text: '上传附件',
            iconCls: 'icon-add',
            handler: function () {
                $("#uploadTemplateDialog").dialog('open');
            }
        }],
        columns: [[
            {
                field: 'id',
                title: '操作',
                formatter: function (value, row, index) {
                    return '<a href="javascript:void(0)" onClick="downloadAttachment(\'' + value + '\');" >下载</a>  '
                        + '<a href="javascript:void(0)" onClick="deleteAttachment(\'' + value + '\');" >删除</a>';
                }
            },
            {field: 'fileName', title: '文档名称', width: 100},
            {field: 'userName', title: '创建人', width: 100},
            {field: 'createTime', title: '创建时间', width: 100}
        ]],
        loader: function (param, success, error) {
            $.ajax({
                url: 'work_flow/get_attachment?workId=' + $('#workId').val(),
                type: 'get',
                data: param,
                success: function (data) {
                    success(data);
                },
                error: function (e) {
                    AjaxErrorHandler(e);
                }
            });
        }
    });

    $('#historyGrid').datagrid({
        fit: true,
        height: '450',
        singleSelect: true,
        fitColumns: true,
        /*pagination:true,
        pageList: [10,15,20,50],
        pageSize: 15,*/
        columns: [[
            {field: 'handleType', title: '操作类型', width: 100},
            {field: 'createTimestamp', title: '操作时间', width: 100},
            {field: 'nodeName', title: '当前环节', width: 100},
            {field: 'nextNodeName', title: '下一环节', width: 100},
            {field: 'handlerPersonName', title: '操作人', width: 100},
            {field: 'receivePersonName', title: '接收人', width: 100},
            {field: 'suggest', title: '备注', width: 200}
        ]],
        loader: function (param, success, error) {
            $.ajax({
                url: 'work_flow/get_history_work?workId=' + $('#workId').val(),
                type: 'get',
                data: param,
                success: function (data) {
                    if (data.length > 1) {
//	    				var newData = new Array();
//	    				for(var i=0;i<data.length;i++) {
//	    					if(i==0) {
//	    						newData.push(data[0]);
//	    					} else {
//	    						// 只显示当前环节最新的记录
//	    						if(data[i].nodeName == data[i-1].nodeName) {
//	    							newData.pop();
//	    						} 
//	    						newData.push(data[i]);
//	    					}
//	    				}
                        success(data);
                    } else {
                        success(data);
                    }
                },
                error: function (e) {
                    AjaxErrorHandler(e);
                }
            });
        },
        onLoadSuccess: function (mData) {
            var data = mData.rows;
            $('#suggestContents').html('');
            for (var i = 0; i < data.length; i++) {
                if (data[i].suggest != null && data[i].suggest.length > 0) {
                    var template = '<li>'
                        + '<div style="BORDER-TOP: #00686b 1px dashed; OVERFLOW: hidden; HEIGHT: 1px"></div>'
                        + '【'
                        + '<span class="name">' + data[i].handlerPersonName + '</span>'
                        + '<span class="time">' + data[i].createTimestamp + '</span>'
                        + ' 】：'
                        + '<span class="content">' + data[i].suggest + '</span>'
                        + '</li>';
                    $('#suggestContents').append(template);
                }
            }
            //过滤已读
            if (flagLog) {
                if (data.length > 0) {
                    for (var i = 0; i < data.length; i++) {
                        if (data[i].handleType != '已读') {
                            arrayLog.push(data[i]);
                        }
                    }
                    if (arrayLog.length > 0) {
                        flagLog = false;
                        $(this).datagrid('loadData', arrayLog);
                    }
                }
            }
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

    $("#changesDialog").dialog({
        title: '跳转流程',
        width: 530,
        height: 430,
        closed: true,
        modal: true,
        buttons: [{
            text: '确定',
            handler: function () {
                changeSubmit();
                $("#changesDialog").dialog('close');
            }
        }, {
            text: '取消',
            handler: function () {
                $("#changesDialog").dialog('close');
            }
        }]
    });

    getNextNode();
    $("#confirmDialog").dialog('open');
});
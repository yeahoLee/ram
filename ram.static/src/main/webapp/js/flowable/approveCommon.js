/**
 * 固定资产盘点结果流程
 */
var ASSETS_INVENTORY_RESULT = "ASSETS_INVENTORY_RESULT";

/**
 * 固定资产盘点任务流程
 */
var ASSETS_INVENTORY_TASK = "ASSETS_INVENTORY_TASK";

/**
 * 固定资产调拨流程
 */
var ASSETS_TRANSFER = "ASSETS_TRANSFER";

/**
 * 固定资产使用人变更/安装位置变更流程
 */
var ASSETS_USER_LOCATION_CHANGE = "ASSETS_USER_LOCATION_CHANGE";

/**
 * 固定资产领用流程
 */
var ASSETS_USE = "ASSETS_USE";

/**
 * 领用归还
 */
var ASSETS_USE_RETURN = "ASSETS_USE_RETURN";

/**
 * 固定资产封存启封流程
 */
var FIXED_ASSETS_ARCHIVE_CENTER = "FIXED_ASSETS_ARCHIVE_CENTER";

/**
 * 固定资产减损流程
 */
var ASSETS_IMPAIRMENT = "ASSETS_IMPAIRMENT";

/**
 * 固定资产借用归还流程
 */
var ASSETS_BORROW_RETURN = "ASSETS_BORROW_RETURN";

/**
 * 固定资产新增流程
 */
var ASSETS_ADD = "ASSETS_ADD";

/**
 * 固定资产借用流程
 */
var ASSETS_BORROW = "ASSETS_BORROW";

var nextNodeData;
var selectDataMap = {};		// 保存选择的环节和用户，格式{nodeIndex_userIndex:'userName'}，例如：{1_1:'user1',1_2:'user2',2_1:'user3'}


function getFirstNode(data) {
    $.ajax({
        url : "flowable/get_first_node",
        type : "POST",
        contentType: "application/json",
        dataType : "json",
        async : false,
        data : JSON.stringify(data),
        success : function (result) {
            if(result.length==0) {
                // 流程处于最后环节
                confirmDialogBtnsConfirm();
            } else {
                $('<div/>').dialog({
                    title: '确认审核',
                    id:'confirmDialog',
                    width: 600,
                    height: 400,
                    closed: false,
                    href: 'confirm_common',
                    modal: true,
                    onClose : function() {
                        $(this).dialog('destroy');
                    },
                    onLoad:function(){
                        $("#confirmDialog").dialog('open');
                        $("#selectNextNode").html('');
                        $("#selectNodeAndUser").html('');
                        $('#selectUser').html('');
                        nextNodeData = result;
                        if(result.length == 1) {
                            $('#selectNextNode').append(assembleNextNode(result, 0));
                            $('input[name="nextNodes"]:checked').click();
                        } else {
                            for(var i=0;i<data.length;i++) {
                                $('#selectNextNode').append(assembleNextNode(result, i));
                            }
                        }
                    },
                    buttons: [
                        {text:"保存",iconCls : 'icon-save',handler: function() {
                                submitNext(data);
                            }
                        },
                        {text:"关闭",iconCls : 'icon-cancel',handler : function(){
                                window.location.href = data.resultLocation;
                            }
                        }
                    ],
                });
            }
        },
        error : function (data) {
            AjaxErrorHandler(data);
        }
    })
}

function submitNext(resultData) {
    var params = "";
    for(var selectData in selectDataMap) {
        var split = selectData.split('_');
        if(params.length==0) {
            params = nextNodeData[split[0]].nodeId+"_"+nextNodeData[split[0]].users[split[1]].userName;
        } else {
            params = params + ';' + nextNodeData[split[0]].nodeId+"_"+nextNodeData[split[0]].users[split[1]].userName;
        }
    }
    resultData.params = params;
    $.ajax({
        url:"flowable/submit_next",
        type : "POST",
        contentType : "application/json;charset=UTF-8",
        data :JSON.stringify(resultData),
        dataType: "json",
        success : function (data) {
            window.location.href = resultData.resultLocation
        },
        error : function (data) {
            AjaxErrorHandler(data);
        }
    })
}

/**
 * 同意
 */
function agree() {
    $.ajax({
        type: 'POST',
        url: 'flowable/get_next_node',
        data: {
            procInstId: $('#processInstanceId').val(),
            taskId: $('#taskId').val(),
            processDefinitionKey : $("#processDefinitionKey").val()
        },
        success: function(data){
            if(data.length==0) {
                // 流程处于最后环节
                confirmDialogBtnsConfirm();
            } else {
                $("#confirmDialog").dialog('open');
                $("#selectNextNode").html('');
                $("#selectNodeAndUser").html('');
                $('#selectUser').html('');
                nextNodeData = data;
                if(data.length == 1) {
                    $('#selectNextNode').append(assembleNextNode(data, 0));
                    $('input[name="nextNodes"]:checked').click();
                } else {
                    for(var i=0;i<data.length;i++) {
                        $('#selectNextNode').append(assembleNextNode(data, i));
                    }
                }
            }
        },
        error: function(data){
            AjaxErrorHandler(data);
        }
    });
}

function disAgree() {
    var suggestContent = $("#suggestContent").val();
    if ($.trim(suggestContent) == "") {
        suggestContent = "不同意";
    }

    $.ajax({
        url : "flowable/turn_back",
        type : "POST",
        dataType : "json",
        data : {
            suggestContent : suggestContent + " ",
            processDefinitionKey : $("#processDefinitionKey").val(),
            procInstId: $('#processInstanceId').val(),
            taskId: $('#taskId').val(),
            nodeId: $("#nodeId").val()
        },
        success : function (data) {
            location.href = "todo_work"
        },
        error : function (data) {
            AjaxErrorHandler(data);
        }
    })
}

/**
 * 撤回
 */
function revork() {
    var suggestContent = $("#suggestContent").val();
    $.messager.confirm("确定", "确定要撤回吗？", function (r) {
        if (r) {
           $.ajax({
               url : "flowable/flow_revork",
               type : "POST",
               dataType: "json",
               data : {
                    processInstanceId: $("#processInstanceId").val(),
                    suggestContent : suggestContent + " "
               },
               success : function (data) {
                    $.messager.alert("信息", "撤回成功！", "info", function (data) {
                        location.href = "finished_work";
                    })
               },
               error :function (data) {
                   AjaxErrorHandler(data);
               }
           })
        }
    })
}

/**
 * 确认审核
 */
function confirmDialogBtnsConfirm() {
    var suggestContent = $('#suggestContent').val();
    if ($.trim(suggestContent) == "") {
        suggestContent = "同意";
    }
    //1.封装数据，格式：huanjie1_user1;huanjie1_user2;huanjie3_user3
    var params = "";
    for(var selectData in selectDataMap) {
        var split = selectData.split('_');
        if(params.length==0) {
            params = nextNodeData[split[0]].nodeId+"_"+nextNodeData[split[0]].users[split[1]].userName;
        } else {
            params = params + ';' + nextNodeData[split[0]].nodeId+"_"+nextNodeData[split[0]].users[split[1]].userName;
        }
    }
    //2.发送请求
    $.ajax({
        type: 'POST',
        url:  'flowable/agreeWork',
        data: {
            params: params,
            suggestContent: suggestContent + " ",
            taskId: $('#taskId').val(),
            processInstanceId: $('#processInstanceId').val(),
            nodeId: $("#nodeId").val(),
            processDefinitionKey : $("#processDefinitionKey").val(),
        },
        success: function(data){
            window.location.href = 'todo_work';
        },
        error: function(data){
            AjaxErrorHandler(data);
        }
    });
}

/**
 * 组装下一环节内容
 */
function assembleNextNode(data, index) {
    //1.根据获取的下一环节节点，如果是并行网关、包容网关，则支持环节多选
    var tempInputType = 'type="radio" checked="checked"';
    if(data[index].gateway == 2 || data[index].gateway == 3) {
        tempInputType = 'type="checkbox"'
    }
    return '<li class="list-group-item">' +
        '<input '+tempInputType+' onClick="nextNodeClick(this,'+index+')" style="width:30px" owner="'+data[index].nodeId+'" name="nextNodes" value="'+data[index].nodeId+'" label="'+index+'" text="'+data[index].nodeName+'">' +
        '<span>'+data[index].nodeName+'</span>' +
        '</li>';
}

/**
 * 点击选择下一环节
 * @param radio
 * @param index
 */
function nextNodeClick(radio, index) {
    //1.根据获取的下一环节节点，如果是并行网关、包容网关，则支持环节多选
    if(nextNodeData[index].gateway != 2 && nextNodeData[index].gateway != 3) {
        selectDataMap = {};
    } else {
        //1.1取消当前选择
        if(!$(radio).is(':checked')) {
            //1.1.1清除下一环节人员界面
            $('#selectUser').html('');
            //1.1.2在缓存中去掉该环节下的用户
            for(var data in selectDataMap) {
                if(data.indexOf(index+'_') != -1) {
                    delete selectDataMap[data];
                }
            }
            showNodesAndUsers();
            return;
        }
    }
    //2.清空选择下一个人员界面数据
    $('#selectUser').html('');
    //3.组装选择下一个人员界面数据
    var users = nextNodeData[index].users;
    for(var i=0; i<users.length; i++) {
        var labelName = users[i].chsName;
        //3.1根据获取的下一环节节点，如果是会签，则支持环节多选
        var tempInputType = 'radio';
        if(nextNodeData[index].inMultiInstance) {
            tempInputType = 'checkbox'
        }
        var temp = '<li class="list-group-item">' +
            '<input type="'+tempInputType+'" onClick="nextNodeUserClick('+index+','+i+')" style="width:30px" name="users" value="'+users[i].id+'" lable="'+users[i].chsName+'">'+
            '<span>'+labelName+'</span>' +
            '</li>';
        $('#selectUser').append(temp);
        $('#selectUser input[name="users"]:first').click();
    }
}

/**
 * 选择下一环节人员
 * @param nodeIndex
 * @param userIndex
 */
function nextNodeUserClick(nodeIndex, userIndex) {
    if(nodeIndex==undefined || userIndex==undefined) {
        return;
    }

    //2.根据获取的下一环节节点，如果是会签，则支持环节多选
    if(!nextNodeData[nodeIndex].inMultiInstance) {
        var newSelectdataMap = {};
        for(var data in selectDataMap) {
            if(data.indexOf(nodeIndex+'_') == -1) {
                newSelectdataMap[data] = selectDataMap[data];
            }
        }
        selectDataMap = newSelectdataMap;
    }
    //3.选择的下一环节不存在则加入,负责删除
    if(!selectDataMap[nodeIndex+'_'+userIndex]) {
        selectDataMap[nodeIndex+'_'+userIndex] = nextNodeData[nodeIndex].users[userIndex];
    } else {
        delete selectDataMap[nodeIndex+'_'+userIndex];
    }
    //4.显示已选择的步骤和处理人
    showNodesAndUsers();
}

/**
 * 显示已选择的步骤和处理人
 */
function showNodesAndUsers(){
    //1.清空已选择的步骤和处理人界面数据
    $('#selectNodeAndUser').html('');
    //2.组装已选择的步骤和处理人界面数据
    var selectList = '';
    for(var nodeAndUserIndex in selectDataMap) {
        var arrays = nodeAndUserIndex.split('_');
        if($('#selectNodeAndUser .list-group-item').hasClass(nextNodeData[arrays[0]].nodeId)) {
            // 2.1当前环节已经存在，在将用户追加在后面
            $('#selectNodeAndUser .'+nextNodeData[arrays[0]].nodeId+' .selectUser').html($('#selectNodeAndUser .'+nextNodeData[arrays[0]].nodeId+' .selectUser').html() + ',' + nextNodeData[arrays[0]].users[arrays[1]].chsName);
        } else {
            // 2.1当前环节不存在，添加环节列表
            var temp = '<li class="list-group-item '+nextNodeData[arrays[0]].nodeId+'" nodeName="'+nextNodeData[arrays[0]].nodeName+'">' +
                '<span class="selectNode">已选步骤: '+nextNodeData[arrays[0]].nodeName+'</span><br><span class="selectUser">已选人员: '+nextNodeData[arrays[0]].users[arrays[1]].chsName+'</span>'
            '</li>';
            $('#selectNodeAndUser').append(temp);
        }

    }
}

$(function () {
    /*流程审批记录*/
    $('#historyDatagrid').datagrid({
        url: 'flowable/get_approve_history',
        method: 'POST',
        striped: true,
        singleSelect: true,
        rownumbers: true,
        // pagination: true,
        // pageSize: 10,
        queryParams:{processInstanceId : $("#processInstanceId").val()},
        loadMsg: '程序处理中，请稍等...',
        columns: [[
            {field: 'taskName', title: '任务名称', align: 'center'},
            {field: 'userName', title: '审批人员', align: 'center'},
            {field: 'message', title: '审批意见', align: 'center'},
            {field: 'typeName',title: '审批类型', align: 'center'},
            {field: 'approveTime', title: '审批时间', align: 'center'},
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });
})
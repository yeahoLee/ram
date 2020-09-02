/**
 * 重置
 */
function resetQuerys() {
    $("#workFlowSearchForm").form("clear");
}

/**
 * 搜索
 */
function searchByQuerys() {
    var qTitle = $("#qTitle").val();
    $("#workflowListTable").datagrid({
        queryParams : {
            q : qTitle
        }
    });
}

/**
 * 打开新增流程dialog
 */
function openAddWorkFlowDialog() {
    $("#addWorkFlowForm").form("clear");
    $("#addWorkFlowDialog").dialog("open");
}

/**
 * 提交新增/修改流程
 */
function createWorkFlow() {
    var id = $("#id").val();
    var flowKey = $("#flowKey").val();
    var flowName = $("#flowName").combobox("getValue");
    var flowDescription = $("#flowDescription").val();

    if ($.trim(flowKey) == "" || $.trim(flowName) == "" || $.trim(flowDescription) == "") {
        $.messager.alert("错误","必填项不能为空！","error", function () {
            return;
        })
    } else {
        $.ajax({
            url : "flowable/create_work_flow",
            type : "POST",
            dataType : "json",
            contentType : "application/json",
            data : JSON.stringify({
                id : id,
                flowKey : flowKey,
                flowName : flowName,
                flowDescription : flowDescription
            }),
            success : function (data) {
                $("#workflowListTable").datagrid("reload");
                $("#addWorkFlowDialog").dialog("close");
            },
            error : function (data) {
                AjaxErrorHandler(data);
            }
        })
    }
}

/**
 * 删除流程
 */
function deleteWorkFlow(id) {
    $.messager.confirm('提示', '是否删除', function(r){
        if(r){
            $.ajax({
                type: 'POST',
                url: 'flowable/delete_work_flow',
                data: {
                    id : id,
                    _method : "DELETE"
                },
                success: function(data){
                    $("#workflowListTable").datagrid("reload");
                },
                error: function(data){
                    AjaxErrorHandler(data);
                }
            });
        }
    });
}

/**
 * 打开修改dialog
 * @param index
 */
function openUpdateDialog(index) {
    $("#addWorkFlowForm").form("clear");
    var rows = $('#workflowListTable').datagrid("getRows");
    var row = rows[index];
    $("#addWorkFlowForm").form("load", row);

    $("#addWorkFlowDialog").dialog("open");
}

function openAddUserGroup(index) {
    var rows = $('#workflowListTable').datagrid("getRows");
    var row = rows[index];
    $("#groupUserDialog input[name='flowId']").val(row.id);

    /**
     * 移除dialog阴影
     */
    $(".window-shadow").remove();

    $("#process").combobox({
        url : "flowable/get_process?key=" + row.flowKey,
        method : "POST",
        valueField : "value",
        textField : "text",
        onSelect : function (data) {
            $.ajax({
                url : "flowable/get_group_by_process",
                type : "POST",
                data : {flowId : row.id, processKey : data.value},
                success : function (result) {
                    $("#groupId").combobox("setValue", result.groupId);
                }
            })
        }
    })

    $("#groupUserDialog").dialog("open");
}

/**
 * 新增用户组
 */
function addUserGroup() {
    var flowId = $("#groupUserDialog input[name='flowId']").val();
    var processKey = $("#process").combobox("getValue");
    var groupId = $("#groupId").combobox("getValue");

    if ($.trim(flowId) == "" || $.trim(processKey) == "" || $.trim(groupId) == "") {
        $.messager.alert("错误","必填项不能为空！","error", function () {
            return;
        })
    } else {
        $.ajax({
            url : "flowable/create_process_group",
            type : "POST",
            dataType : "json",
            contentType: "application/json",
            data : JSON.stringify({
                flowId : flowId,
                processKey : processKey,
                groupId : groupId
            }),
            success : function (data) {
                $("#process").combobox("setValue", "");
                $("#groupId").combobox("setValue", "");
            },
            error : function (data) {
                AjaxErrorHandler(data);
            }
        })
    }
}

function viewWorkFlow(imgName) {
    window.open("view_work_flow?imgName=" + imgName);
}

$(function () {

    $("#flowName").combobox({
        url : 'flowable/get_work_flow_list',
        method : 'POST',
        valueField : "value",
        textField : "value",
        editable : false,
        onSelect : function (data) {
            $("#flowKey").val(data.text);
        }
    });


    $('#workflowListTable').datagrid({
        url: 'flowable/work_flow_list',
        fit: true,
        striped: true,
        method: 'POST',
        rownumbers: true,
        pagination: true,
        pageSize: 20,
        title: '流程管理',
        toolbar: '#addButton',
        loadMsg: '程序处理中，请稍等...',
        columns:[[
            {field: 'id',title: '操作', align: 'center',formatter:function (value, row, index) {
                    var addUserGroup = '<a href="javascript:void(0);" onClick="openAddUserGroup(\'' + index + '\');">添加用户组</a> ';
                    var viewFlowImg = '<a href="javascript:void(0);" onClick="viewWorkFlow(\'' + row.imgName + '\');">查看流程图</a> ';
                    var updateWorkFlow = '<a href="javascript:void(0);" onClick="openUpdateDialog(\'' + index + '\');">修改</a> ';
                    var deleteWorkFlow = '<a href="javascript:void(0);" onClick="deleteWorkFlow(\'' + value + '\');">删除</a> ';
                    return addUserGroup + viewFlowImg + updateWorkFlow + deleteWorkFlow;
                    // return addUserGroup + viewFlowImg;
                }},
            {field: 'flowKey',title: '流程key', align: 'center'},
            {field: 'flowName',title: '流程名称', align: 'center'},
            {field: 'flowDescription',title: '流程描述', align: 'center'},
            {field: 'createTimestampStr',title: '创建时间', align: 'center'},
            {field: 'imgName',title: '图片名称', hidden: true}
        ]],
        onLoadError:function(data){
            AjaxErrorHandler(data);
        }
    });
})


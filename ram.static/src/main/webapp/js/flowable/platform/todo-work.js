/**
 * 查询待办工作
 */
function searchByQuerys() {
    $('#todoListTable').datagrid('reload', {
        formName: $("#title").val()
    });
}

/**
 * 重置
 */
function resetQuerys() {
    $('#todoSearchForm').form("clear");
}

/**
 * 详情页
 */
function lookDetail(processInstanceId, taskId) {
    window.location.href = 'work-details?processInstanceId=' + processInstanceId + '&taskId='+taskId;
}

/**
 * 查看流程图
 */
function lookProcessPic(id) {
    window.open("workflow-image?processInstanceId=" + id);
}

$(function () {
    /*流程类型*/
    $('#type').combobox({
        // url: 'approve/get_todo_type',
        method: 'POST',
        valueField: 'id',
        textField: 'text',
        mode: 'remote',
        loadFilter: function (data) {
            return [{text: '全部', id: ''}].concat(data)
        }
    });

    /*待办表格*/
    $('#todoListTable').datagrid({
        url: 'flowable/todo-work',
        fit: true,
        method: 'GET',
        striped: true,
        singleSelect: true,
        rownumbers: true,
        pagination: true,
        pageSize: 20,
        title: '待办工作',
        loadMsg: '程序处理中，请稍等...',
        columns: [[
            {field: 'taskId', title: '操作', align: 'center',
                formatter: function (value, row) {
                    var str = "";
                    if (row.processInstanceId != null) {
                        str += '<a class="text-blue-d2" onClick="lookProcessPic(\'' + row.processInstanceId + '\');">查看流程图</a>';
                    }
                    if (row.processInstanceId != null) {
                        str += ' <a href="#" class="text-blue-d2"  onClick="lookDetail(\'' + row.processInstanceId + '\',\'' + row.taskId + '\');">查看详情</a>';
                    }
                    return str;
                }
            },
            {field: 'approveNumber', title: '流程单号', align: 'center'},
            {field: 'formName', title: '流程标题', align: 'center'},
            /*{field: 'typeName',title: '流程类型',width:130, align: 'center'},*/
            {field: 'processInstanceStarter', title: '流程发起人', align: 'center'},
            {field: 'processInstanceStartTime', title: '流程发起时间', align: 'center',
                formatter: function (value, row) {
                    return DateTimeFormatter(value, "yyyy-MM-dd HH:mm");
                }
            },
            {field: 'previousNodeSendUserName', title: '上一环节发送人', align: 'center'},
            {field: 'startTime', title: '上一环节发送时间', align: 'center',
                formatter: function (value, row) {
                    return DateTimeFormatter(value, "yyyy-MM-dd HH:mm");
                }},
            {field: 'taskName', title: '当前环节', align: 'center'}
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });
});


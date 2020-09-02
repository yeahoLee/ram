/*已办工作*/
function searchByQuerys() {
    var formName=$("#formName").val();
    $('#finishListTable').datagrid('reload', {
        formName: formName,
    });
}

//重置
function searchReset(){
    $("#finishedSearchForm").form("clear");
}

/**
 * 详情页
 */
function lookDetail(processInstanceId) {
    location.href = "finished_detail?processInstanceId=" + processInstanceId;
}

/**
 * 查看流程图
 */
function lookProcessPic(processInstanceId) {
    window.open("workflow-image?processInstanceId=" + processInstanceId);
}

$(function(){
    $('#finishListTable').datagrid({
        url: 'flowable/finished-work',
        fit: true,
        method: 'POST',
        striped: true,
        singleSelect: true,
        rownumbers: true,
        pagination: true,
        pageSize: 20,
        title: '已办工作',
        toolbar: '#authorityDiv',
        loadMsg: '程序处理中，请稍等...',
        columns:[[
            {field: 'processInstanceId', title: '操作', align: 'center',
                formatter: function (value, row) {
                    var str = "";
                    if (row.processInstanceId != null) {
                        str += '<a class="text-blue-d2" onClick="lookProcessPic(\'' + value + '\');">查看流程图</a>';
                    }
                    if (row.processInstanceId != null) {
                        str += ' <a href="#" class="text-blue-d2"  onClick="lookDetail(\'' + value + '\');">查看详情</a>';
                    }
                    return str;
                }
            },
            {field: 'approveNumber', title: '流程单号', align: 'center'},
            {field: 'formName',title: '流程标题',width:260,formatter:function(value,row){
                    var str = "";
                    if(value != null){
                        str = '<span style="cursor:pointer;color: #337ab7;"  onClick="lookDetail(\''+row.processInstanceId+'\');">'+value+'</span>';
                    }
                    return str;
                }
            },
            {field: 'starter',title: '流程发起人',width:76, align: 'center'},
            {field: 'startTime',title: '流程发起时间',width:125, align: 'center',formatter:function(value,row) {
                    return DateTimeFormatter(value, "yyyy-MM-dd HH:mm");
                }},
            // {field: 'previousNodeSendUserId',title: '上一环节发送人',width:100, align: 'center'},
            // {field: 'startTime',title: '上一环节发送时间',width:130, align: 'center',formatter:function(value,row) {
            //         return DateTimeFormatter(value, "yyyy-MM-dd HH:mm");
            //     }},
            // {field: 'taskName',title: '当前环节',width:130, align: 'center'},
            {field: 'approveType',title: '流程状态',width:70, align: 'center',formatter:function(value,row) {
                    return setFlowStatus(value);
                }},
            {field: 'endTime',title: '流程完成时间', width:125,align: 'center',formatter:function(value,row) {
                    return DateTimeFormatter(value, "yyyy-MM-dd HH:mm");
                }}
        ]],
        onLoadError:function(data){
            AjaxErrorHandler(data);
        }
    });
});


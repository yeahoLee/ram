/*function toUpdateReceipt(id) {
	window.location.href = 'updateReceipt?id=' + id;
}
function lookReceipt(id) {
	window.location.href ='lookReceipt?id='+id;
}

function deleteReceipt(id) {
	$.messager.confirm('确定', '确定要删除吗？', function(r){
		if(r){
			$.ajax({
				url: 'receipt/deleteReceipt',
				type: 'POST',
				data: {id: id},
				success: function(data){
					$.messager.alert('删除', '删除成功！', 'info');
					$('#receiptTable').datagrid('reload');
				},
				error: function(data){
					AjaxErrorHandler(data);
				}
			});
		}
	});
}

function checkReceipt(id){
	window.location.href = 'turn_to_approve_work_zczxd?id=' + id;

	$.ajax({
		url: 'approve/turn_to_approve_work',
		type: 'POST',
		data: {id: id},
		success: function(data){
			$.messager.alert('提示', '操作成功！', 'info',function(){
				$('#receiptTable').datagrid('reload');
			});
		},
		error: function(data){
			AjaxErrorHandler(data);
		}
	});
}

function searchByQuerys(){
	$('#receiptTable').datagrid('load',{
		runningNum: $('#runningNum').val(),
		receiptName: $('#receiptName').val(),
		sourceType: $('#sourceType').combobox("getValue"),
		receiptStatus: $('#receiptStatus').combobox("getValue")
    })
}

function searchByDefaultQuerys(){
	$('#receiptTable').datagrid('load',{
		runningNum: '',
		receiptName: '',
		sourceType: '',
		receiptStatus: ''
    })
	
	$('#runningNum').val('');
	$('#receiptName').val('');
	$('#sourceType').combobox("setValue",'');
	$('#receiptStatus').combobox("setValue",'');
}*/

function returnOperateStr(workType, workId, taskId, value) {
    var querys = "?workId=" + workId + "&taskId=" + taskId;

    switch (workType) {
        case "0": //资产新增单
            return
        case "1":
            return "";
        default:
            return "";
    }
}

$(function () {
//	 fillEnumCombo('ram.asset.dto.FlowableInfo', 'WORKSTATUS', 'receiptStatus');

    $('#dataGridTable').datagrid({
        url: 'approve/get_todo_work',
        fit: true,
        method: 'POST',
        striped: true,
        singleSelect: true,
        rownumbers: true,
        pagination: true,
        pageSize: 20,
        title: '待办列表',
        toolbar: '#receiptTableButtons',
        loadMsg: '程序处理中，请稍等...',
        columns: [[
            {
                field: 'approvalProcessNo', title: '流程编码', formatter: function (value, row) {
                    return '<a href="work_details?workId=' + row.workId + '&taskId=' + row.taskId + '">' + value + '</a>';
                }
            },
            {field: 'workTitle', title: '流程名称'},
            {field: 'workStatus', title: '状态'},
            {field: 'workProcess', title: '当前环节'},
            {field: 'sendPerson', title: '发送人'},
            {field: 'receiveTime', title: '接收时间'},
            {field: 'taskId', title: 'taskId'},
            {field: 'workId', title: 'workId'},
            {field: 'workType', title: 'workType'}
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });
});


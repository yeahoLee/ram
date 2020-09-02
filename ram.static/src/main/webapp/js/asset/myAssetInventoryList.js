function toUpdateAssetInventory(id, managerDeptId) {
    window.location.href = 'myassetinventory_update?id=' + id + "&managerDeptId=" + managerDeptId;
}

function lookAssetInventory(id, managerDeptId) {
    window.location.href = 'myassetinventory_view?id=' + id + "&managerDeptId=" + managerDeptId;
}

function recallAssetInventory(id) {
    $.messager.confirm('确定', '确定要撤回吗？', function (r) {
        if (r) {
            $.ajax({
                url: 'assetInventory/myinventorytemp_recall',
                type: 'POST',
                data: {id: id, myinventoryStatusStr: 0},
                success: function (data) {
                    $.messager.alert('撤回', '撤回成功！', 'info');
                    $('#dataGridTable').datagrid('reload');
                },
                error: function (data) {
                    AjaxErrorHandler(data);
                }
            });
        }
    });
}

function submitAssetInventory(index) {
    var rows = $("#dataGridTable").datagrid("getRows");
    var row = rows[index];

    var data = {};
    data.id=row.id;
    data.formName = row.inventoryName;
    data.serialNumber = row.myAssetInventoryCode;
    data.processDefKey = ASSETS_INVENTORY_RESULT;
    data.resultLocation = "myassetinventory_query";
    data.produceType = row.produceTypeStr;
    getFirstNode(data);
}

//function searchByQuerys(){
//	$('#dataGridTable').datagrid('load',{
//		runningNum: $('#runningNum').val(),
//		receiptName: $('#receiptName').val(),
//		sourceType: $('#sourceType').datebox("getValue"),
//		receiptStatus: $('#receiptStatus').datebox("getValue")
//    })
//}

function returnOperateStr(mystatus, status, index, value, managerDeptId) {
    var bj = '<a href="javascript:void(0);" onClick="toUpdateAssetInventory(\'' + value + '\',\'' + managerDeptId + '\');">编辑</a> ';
    var fqsp = '<a href="javascript:void(0);" onClick="submitAssetInventory(\'' + index + '\');">发起审批</a> ';

    switch (mystatus) {
        case "0":
            return bj + fqsp;
        default:
            return "";
    }
}

$(function () {
    var managerId = $('#createUserID').val();
    $('#dataGridTable').datagrid({
        url: 'assetInventory/myinventory_datagrid',
        fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        pagination: true,
        pageSize: 20,
        title: '我的盘点单列表',
        //toolbar: '#receiptTableButtons',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {
            managerId: managerId
        },
        columns: [[
            {
                field: 'myAssetInventoryCode', title: '盘点单编号', formatter: function (value, row) {
                    return '<a href="javascript:void(0);" onClick="lookAssetInventory(\'' + row.id + '\',\'' + row.managerDeptId + '\');">' + value + '</a> ';
                }
            },
            {field: 'inventoryName', title: '盘点任务名称'},
            {field: 'launchDate', title: '发起时间'},
            {field: 'managerDeptStr', title: '资产管理部门'},
            {field: 'quantity', title: '盘点资产数量'},
            {field: 'inventoryProfit', title: '盘盈'},
            {field: 'inventoryLoss', title: '盘亏'},
            {field: 'produceStr', title: '物资类型'},
            {field: 'produceTypeStr', hidden: true},
            {field: 'myinventoryStatus', title: '审批状态'},
            {field: 'myinventoryStatusStr', title: '审批状态', hidden: true},
            {field: 'managerDeptId', title: '部门id', hidden: true},
            {
                field: 'id', title: '操作', formatter: function (value, row, index) { //value, row, index
                    return returnOperateStr(row.myinventoryStatusStr, row.inventoryStatusStr + "", index, value, row.managerDeptId);
                }
            }
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });
});


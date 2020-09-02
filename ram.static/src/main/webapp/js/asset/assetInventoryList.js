function lookAssetInventory(id) {
    window.location.href = 'assetInventory_view?id=' + id;
}

//重置
function resetQuerys() {
    $("#runningNum").val('');
    $("#InventoryName").val('');
    $('#InventoryStatus').combobox('clear');
}

//发起审批
function doApprovalSubmit(index) {
    var rows = $("#inventoryDataGridTable").datagrid("getRows");
    var row = rows[index];

    var data = {};
    data.id=row.id;
    data.formName = row.inventoryName;
    data.serialNumber = row.inventoryRunningNum;
    data.processDefKey = ASSETS_INVENTORY_TASK;
    data.resultLocation = "assetInventory_query";
    getFirstNode(data);
}

// 发布盘点任务
function distributionInventorySubmit(id) {
    $.messager.confirm('确定', '确定要发布吗？', function (r) {
        if (r) {
            $.ajax({
                url: 'assetInventory/distribution_assetinventory',
                type: 'POST',
                data: {
                    assetInventoryId: id
                },
                success: function (data) {
                    $.messager.alert('发布', '发布成功！', 'info');
                    $('#inventoryDataGridTable').datagrid('reload');
                },
                error: function (data) {
                    AjaxErrorHandler(data);
                }
            });
        }
    });
}

// 创建盘点单界面
function goToAssetInventoryPage() {
    window.location.href = 'assetInventory_check_create';
}

// 编辑盘点单界面
function goToUpdateAssetUnsealPageSubmit(id) {
    window.location.href = 'assetInventory_update?id=' + id;
}

function deleteInventorySubmit(id) {
    $.messager.confirm('确定', '确定要删除吗？', function (r) {
        if (r) {
            $.ajax({
                url: 'assetInventory/assetinventory_delete',
                type: 'POST',
                data: {
                    id: id
                },
                success: function (data) {
                    $.messager.alert('删除', '删除成功！', 'info');
                    $('#inventoryDataGridTable').datagrid('reload');
                },
                error: function (data) {
                    AjaxErrorHandler(data);
                }
            });
        }
    });
}

function checkReceipt(id) {
    $.messager.confirm('确定', '确定要审核吗？', function (r) {
        if (r) {
            $.ajax({
                url: 'receipt/check_receipt',
                type: 'POST',
                data: {
                    id: id
                },
                success: function (data) {
                    $.messager.alert('提示', '操作成功！', 'info', function () {
                        $('#receiptTable').datagrid('reload');
                    });
                },
                error: function (data) {
                    AjaxErrorHandler(data);
                }
            });
        }
    });
}

function searchByQuerys() {
    $('#inventoryDataGridTable').datagrid('load', {
        inventoryRunningNum: $('#runningNum').val(),
        inventoryName: $('#InventoryName').val(),
        inventoryStatusStr: $('#InventoryStatus').combobox("getValue")
    })

}

function returnOperateStr(status, value, index) {
    var bj = '<a href="javascript:void(0);" onClick="goToUpdateAssetUnsealPageSubmit(\'' + value + '\');">编辑</a> ';
    var sc = '<a href="javascript:void(0);" onClick="deleteInventorySubmit(\'' + value + '\');">删除</a> ';
    // var fb = '<a href="javascript:void(0);" onClick="distributionInventorySubmit(\'' + value + '\');">发布</a> ';
    var fqsp = '<a href="javascript:void(0);" onClick="doApprovalSubmit(\'' + index + '\');">发起审批</a> ';

    switch (status) {
        //拟稿
        case "0":
            return bj + sc + fqsp;
        // 驳回
        case "2":
            return bj;
        //撤回
        case "5":
            return bj;
        //已审批
        // case "3":
        //     return fb;
        default:
    }
}

$(function () {
    fillEnumCombo('ram.asset.service.IAssetService', 'INVENTORY_CHECK', 'InventoryStatus');

    $('#inventoryDataGridTable')
        .datagrid(
            {
                url: 'assetInventory/inventory_datagrid',
                fit: true,
                method: 'POST',
                striped: true,
                rownumbers: true,
                pagination: true,
                pageSize: 20,
                title: '新增单列表',
                toolbar: '#asdiv',
                loadMsg: '程序处理中，请稍等...',
                columns: [[
                    {
                        field: 'inventoryRunningNum',
                        title: '盘点单编号',
                        formatter: function (value, row) {
                            return '<a href="javascript:void(0);" onClick="lookAssetInventory(\''
                                + row.id
                                + '\');">'
                                + value
                                + '</a> ';
                        }
                    },
                    {
                        field: 'inventoryName',
                        title: '资产盘点单名称'
                    },
                    {
                        field: 'quantity',
                        title: '数量'
                    },
                    {
                        field: 'launchDate',
                        title: '发起日期',
                        formatter: function (value) {
                            return DateTimeFormatter2(value);
                        }
                    },
                    {
                        field: 'inventoryProfit',
                        title: '盘盈'
                    },
                    {
                        field: 'inventoryLoss',
                        title: '盘亏'
                    },
                    {
                        field: 'inventoryProcess',
                        title: '盘点进度'
                    },
                    {
                        field: 'inventoryStatus',
                        title: '审批状态',
                        align: 'center'
                    },
                    {
                        field: 'inventoryStatusStr',
                        title: '状态',
                        hidden: true
                    },
                    {
                        field: 'id',
                        title: '操作',
                        formatter: function (value, row, index) { // value,
                            return returnOperateStr(row.inventoryStatusStr + "", value, index);
                        }
                    }]],
                onLoadError: function (data) {
                    AjaxErrorHandler(data);
                }
            });
});

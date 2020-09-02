function lookAssetInventory(id) {
    window.location.href = 'myassetinventory_view?id=' + id;
}

$(function () {
    var AssetInventoryId = $('#AssetInventoryId').val();
    $('#myAssetInventoryDataGrid').datagrid({
        url: 'assetInventory/inventorytemp_find',
        //fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        pagination: true,
        pageSize: 20,
        idField: 'id',
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {
            assetInventoryId: AssetInventoryId
        },
        columns: [[
            {
                field: 'myAssetInventoryCode', title: '盘点单编号', formatter: function (value, row) {
                    return '<a href="javascript:void(0);" onClick="lookAssetInventory(\'' + row.id + '\');">' + value + '</a> ';
                }
            },
            {field: 'managerDeptStr', title: '资产管理部门'},
            {field: 'managerStr', title: '资产管理员'},
            {field: 'quantity', title: '盘点资产数量', align: 'center'},
            {field: 'inventoryLoss', title: '盘亏'},
            {field: 'inventoryProfit', title: '盘盈'},
            {field: 'myinventoryStatus', title: '盘点单状态'}
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });

})
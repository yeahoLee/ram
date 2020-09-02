$(function () {
    var myAssetInventoryId = $('#MyAssetInventoryId').val();
    var AssetInventoryId = $('#AssetInventoryId').val();
    var DeptID = $('#DeptID').val();
    $('#assetInventoryDataGrid').datagrid({
        url: 'assetInventory/inventorytemp_datagrid',
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
            assetInventoryId: AssetInventoryId,
            myAssetInventoryId: myAssetInventoryId,
            manageDeptId: DeptID
        },
        columns: [[
            {field: 'operationStr', title: '操作'},
            {field: 'resultStr', title: '盘点结果'},
            {field: 'inventoryWayStr', title: '盘点方式'},
            {field: 'remark', title: '备注'},
            {field: 'assetCode', title: '资产编码'},
            {field: 'assetChsName', title: '资产名称'},
            {field: 'specAndModels', title: '规格型号'},
            {field: 'buyDate', title: '购置日期'},
            {field: 'unitOfMeasStr', title: '计量单位'},
            {field: 'equiOrigValue', title: '资产原值'},
            {field: 'residualValue', title: '残余价值'},
            {field: 'assetStatus', title: '资产状态'},
            {field: "savePlaceStr", title: "安装位置"},
            {field: 'useDeptStr', title: '使用部门'},
            {field: 'userStr', title: '使用人'},
            {field: 'manageDeptStr', title: '主管部门'},
            {field: 'managerStr', title: '资产管理员'},
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });

    //$('#assetInventoryDataGrid').datagrid("autoSizeColumn");
})
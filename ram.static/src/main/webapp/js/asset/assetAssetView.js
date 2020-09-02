function closeThisPage() {
    $.messager.alert('提示', '您确定关闭该页么？', 'info', function () {
        window.close();
    });
}

$(function () {
    var assetId = $('#assetId').val();

    $('#dataGridTable0').datagrid({
        url: 'asset/history_datagrid',
        fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        pagination: true,
        pageSize: 10,
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {
            historyType: '0',
            assetId: assetId
        },
        columns: [[
            {field: 'createTimestamp', title: '发生时间'},
            {field: 'createUserStr', title: '经办人'},
            {field: 'modifyContent', title: '修改内容'}
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });

    $('#dataGridTable1').datagrid({
        url: 'asset/history_datagrid',
        fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        singleSelect: true,
        pagination: true,
        pageSize: 10,
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {
            historyType: '1',
            assetId: assetId
        },
        columns: [[
            {field: 'createTimestamp', title: '发生时间'},
            {field: 'createUserStr', title: '经办人'},
            {field: 'changeType', title: '变更类型'},
            {field: 'changeContent', title: '变更内容'}
            //,{field: 'modifyContent',title: '修改内容'}
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });

    $('#dataGridTable2').datagrid({
        url: 'asset/history_datagrid',
        fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        singleSelect: true,
        pagination: true,
        pageSize: 10,
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {
            historyType: '2',
            assetId: assetId
        },
        columns: [[
            {field: 'createTimestamp', title: '发生时间'},
            {field: 'createUserStr', title: '经办人'},
            {field: 'sealedUnsealed', title: '操作方式'},
            {field: 'modifyContent', title: '修改内容'}
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });

    $('#dataGridTable3').datagrid({
        url: 'asset/history_datagrid',
        fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        singleSelect: true,
        pagination: true,
        pageSize: 10,
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {
            historyType: '3',
            assetId: assetId
        },
        columns: [[
            {field: 'createTimestamp', title: '发生时间'},
            {field: 'createUserStr', title: '经办人'},
            {field: 'checkOutComStr', title: '调出公司'},
            {field: 'checkOutDeptStr', title: '调出部门'},
            {field: 'checkInComStr', title: '调入公司'},
            {field: 'checkInDeptStr', title: '调入部门'}
            //,{field: 'modifyContent',title: '修改内容'}
            //,{field: 'modifyContent',title: '相关台账'}
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });

    $('#dataGridTable4').datagrid({
        url: 'asset/history_datagrid',
        fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        singleSelect: true,
        pagination: true,
        pageSize: 10,
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {
            historyType: '4',
            assetId: assetId
        },
        columns: [[
            {field: 'createTimestamp', title: '发生时间'},
            {field: 'createUserStr', title: '经办人'},
            {field: 'takeStockResult', title: '盘点结果'},
            {field: 'modifyContent', title: '修改内容'}
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });

    $('#dataGridTable5').datagrid({
        url: 'asset/history_datagrid',
        fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        singleSelect: true,
        pagination: true,
        pageSize: 10,
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {
            historyType: '5',
            assetId: assetId
        },
        columns: [[
            {field: 'createTimestamp', title: '发生时间'},
            {field: 'createUserStr', title: '经办人'},
            {field: 'sourctType', title: '来源方式'},
            {field: 'assetSource', title: '资产来源'},
            {field: 'modifyContent', title: '修改内容'}
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });

    $('#dataGridTable6').datagrid({
        url: 'asset/history_datagrid',
        fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        singleSelect: true,
        pagination: true,
        pageSize: 10,
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {
            historyType: '6',
            assetId: assetId
        },
        columns: [[
            {field: 'createTimestamp', title: '发生时间'},
            {field: 'createUserStr', title: '经办人'},
            {field: 'reduceType', title: '减少类型'},
            {field: 'modifyContent', title: '修改内容'}
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });
});
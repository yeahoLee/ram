function goToUpdateAssetPageSubmit(assetId) {
    window.location.href = './asset_update?assetId=' + assetId;
}

function deleteAsssetSubmit(assetId) {
    $.messager.confirm('确定', '确定要删除吗？', function (r) {
        if (r) {
            $.ajax({
                url: 'asset/delete_asset',
                type: 'POST',
                data: {assetId: assetId},
                success: function (data) {
                    $.messager.alert('删除', '删除成功！', 'info', function () {
                        $('#dataGridTable').datagrid('reload');
                    });
                },
                error: function (data) {
                    AjaxErrorHandler(data);
                }
            });
        }
    })
}

$(function () {
    $('#dataGridTable').datagrid({
        url: 'asset/asset_datagrid',
        fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        singleSelect: true,
        pagination: true,
        pageSize: 20,
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        columns: [[
            {
                field: 'id', title: '操作', formatter: function (value) { //value, row, index
                    var returnVar = '<a href="javascript:void(0);" onClick="goToUpdateAssetPageSubmit(\'' + value + '\');">修改</a> '
                        + '<a href="javascript:void(0);" onClick="deleteAsssetSubmit(\'' + value + '\');">删除</a>'
                    return returnVar;
                }
            },
            {field: 'materialCode', title: '物资编码'}
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });
});
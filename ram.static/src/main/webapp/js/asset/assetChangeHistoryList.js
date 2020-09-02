function searchByQuerys() {
    var createTimestampStart = $('#createTimestampStart').datebox("getValue");//开始日期
    var createTimestampEnd = $('#createTimestampEnd').datebox("getValue");//结束日期
    var startTime = DateTimeFormatter2(createTimestampStart);
    var endTime = DateTimeFormatter2(createTimestampEnd);
    if (startTime != "" && endTime != "" && startTime > endTime) {
        $.messager.alert('提示', '结束日期不能小于开始日期！', 'info');
        return;
    }
    $('#dataGridTable').datagrid('load', {
        manageDeptId: $('#manageDeptId').combotree("getValue"),//主管部门
        materialCode: $('#assetLeave').combobox("getValue"),//资产类别
        createTimestampStart: createTimestampStart,//开始日期
        createTimestampEnd: createTimestampEnd,//结束日期
        historyTypeStr: 1
    });
}

function resetQuerys() {
    $('#createTimestampStart').datebox("setValue", '');//开始日期
    $('#createTimestampEnd').datebox("setValue", '');//结束日期
    $('#manageDeptId').combotree("setValue", '');//主管部门
    $('#assetLeave').combobox("setValue", '');//资产类别
    $('#dataGridTable').datagrid('load', {
        manageDeptId: '',
        materialCode: '',
        createTimestampStart: '',
        createTimestampEnd: '',
        historyTypeStr: 1
    });
}

$(function () {
    $('#dataGridTable').datagrid({
        url: 'assetreport/assetchangereport_datagrid',
        fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        pagination: true,
        pageSize: 20,
        title: '资产变更历史',
        toolbar: '#btdiv',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {
            historyTypeStr: 1
        },
        columns: [[
            {field: 'assetCode', title: '资产编码'},
            {field: 'combinationAssetName', title: '资产名称'},
            {field: 'changeContent', title: '变更内容'},
            {field: 'changeDate', title: '变更日期'},
            {field: 'assetTypeStr', title: '资产类型'},
            {field: 'specAndModels', title: '规格型号'},
            {field: 'assetBrand', title: '品牌'},
            {field: 'unitOfMeasStr', title: '计量单位'},
            {field: 'useDeptStr', title: '使用部门'},
            {field: 'useStr', title: '使用人'},
            {field: 'manageDeptStr', title: '主管部门'},
            {field: 'managerStr', title: '资产管理员'},
            {field: 'belongLineStr', title: '所属线路'},
            {field: 'assetStatusStr', title: '资产状态'},

        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });
})
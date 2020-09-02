function toUpdateAssetReceiveRevert(id) {
    window.location.href = 'assetreceiverevert_update?id=' + id;
}

function lookAssetReceiveRevert(assetreceiverevertCode) {
    window.location.href = 'assetreceiverevert_view?assetReceiveRevertCode=' + assetreceiverevertCode;
}

function goToAssetReceiveRevertPage() {
    window.location.href = 'assetreceiverevert_create';
}

function goToAssetReceiveUsePage() {
    window.location.href = 'assetreceiveuse_create';
}

function deleteAssetReceiveRevert(id) {
    $.messager.confirm('确定', '确定要删除吗？', function (r) {
        if (r) {
            $.ajax({
                url: 'assetreceiverevert/delete_assetreceiverevert',
                type: 'POST',
                data: {AssetReceiveRevertId: id},
                success: function (data) {
                    $.messager.alert('删除', '删除成功！', 'info');
                    $('#dataGridTable').datagrid('reload');
                },
                error: function (data) {
                    AjaxErrorHandler(data);
                }
            });
        }
    });
}

//模拟发起审批
function UpdateAssetReceiveRevertStauts(index) {
    var rows = $("#dataGridTable").datagrid("getRows");
    var row = rows[index];

    var data = {};
    data.id=row.id;
    data.formName = "领用归还申请单";
    data.serialNumber = row.assetReceiveRevertCode;
    data.processDefKey = ASSETS_USE_RETURN;
    data.resultLocation = "assetreceiverevert_query";
    data.produceType = row.produceTypeStr;
    getFirstNode(data);
}

function searchByDefaultQuerys() {
    $('#realrevertTimeStart').datebox("setValue", '');//调出开始日期
    $('#realrevertTimeEnd').datebox("setValue", '');//调出结束日期
    $('#searchQuerys input[name="assetCode"]').val('');//资产编码
    $('#searchQuerys input[name="assetChsName"]').val('');//资产名称
    $('#assetReceiveRevertUserID').combobox("setValue", '');//归还人
    $('#dataGridTable').datagrid('load', {
        assetCode: '',
        assetReceiveRevertTempStr: '',
        realrevertTimeStart: '',//归还开始日期
        realrevertTimeEnd: '',//归还结束日期
        assetReceiveRevertUserID: '',
        createUserID: $('#createUserID').val()
    });
}

function searchByQuerys() {
    var realrevertTimeStart = $('#realrevertTimeStart').datebox("getValue");//调出开始日期
    var realrevertTimeEnd = $('#realrevertTimeEnd').datebox("getValue");//调出结束日期
    var startTime = DateTimeFormatter2(realrevertTimeStart);
    var endTime = DateTimeFormatter2(realrevertTimeEnd);
    if (startTime != "" && endTime != "" && startTime > endTime) {
        $.messager.alert('提示', '结束日期不能小于开始日期！', 'info');
        return;
    }
    $('#dataGridTable').datagrid('load', {
        assetCode: $('#searchQuerys input[name="assetCode"]').val(),//资产编码
        assetReceiveRevertTempStr: $('#searchQuerys input[name="assetChsName"]').val(),//资产名称
        realrevertTimeStart: realrevertTimeStart,//归还开始日期
        realrevertTimeEnd: realrevertTimeEnd,//归还结束日期
        assetReceiveRevertUserID: $('#assetReceiveRevertUserID').combobox("getValue"),//归还人
        createUserID: $('#createUserID').val()
    });
}

function returnOperateStr(status, value, index) {
    var bj = '<a href="javascript:void(0);" onClick="toUpdateAssetReceiveRevert(\'' + value + '\');">编辑</a> ';
    var sc = '<a href="javascript:void(0);" onClick="deleteAssetReceiveRevert(\'' + value + '\');">删除</a> ';
    var fqsp = '<a href="javascript:void(0);" onClick="UpdateAssetReceiveRevertStauts(\'' + index + '\');">发起审批</a> ';

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
        default:
    }
}

$(function () {
    $('#dataGridTable').datagrid({
        url: 'assetreceiverevert/assetreceiverevert_datagrid',
        fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        pagination: true,
        pageSize: 20,
        title: '资产归还台账',
        toolbar: '#btdiv',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {
            createUserID: $('#createUserID').val()
        },
        columns: [[
//			{field: 'id', checkbox:true},
            {
                field: 'assetReceiveRevertCode', title: '归还单号', formatter: function (value, row, index) {
                    var updateBtn = '<a href="javascript:void(0);" onClick="lookAssetReceiveRevert(\'' + value + '\');">' + value + '</a> ';
                    return updateBtn;
                }
            },
            {
                field: 'type', title: '单据类型', formatter: function (value, row, index) {
                    return "归还单";
                }
            },
            {field: 'assetReceiveRevertDepartmentName', title: '归还部门'},
            {field: 'assetReceiveRevertUserName', title: '归还人'},
            {field: 'realrevertTimeStr', title: '预计归还日期'},
            {field: 'assetReceiveRevertTempStr', title: '归还资产'},
            {field: 'produceStr', title: '物资类型'},
            {field: 'produceTypeStr', hidden: true},
            {field: 'receiptStatus', title: '审批状态'},
            {
                field: 'id', title: '操作', formatter: function (value, row, index) {
                    return returnOperateStr(row.receiptIndex + "", value, index);
                }
            }
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });
})
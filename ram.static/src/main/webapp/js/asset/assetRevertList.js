function toUpdateAssetRevert(id) {
    window.location.href = 'assetrevert_update?id=' + id;
}

function lookAssetRevert(assetrevertCode) {
    window.location.href = 'assetrevert_view?assetrevertCode=' + assetrevertCode;
}

function goToAssetRevertPage() {
    window.location.href = 'assetrevert_create';
}

function goToAssetBorrowPage() {
    window.location.href = 'assetborrow_create';
}

function deleteAssetRevert(id) {
    $.messager.confirm('确定', '确定要删除吗？', function (r) {
        if (r) {
            $.ajax({
                url: 'assetrevert/delete_assetrevert',
                type: 'POST',
                data: {AssetRevertId: id},
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
function UpdateAssetRevertStauts(index) {
    var rows = $("#dataGridTable").datagrid("getRows");
    var row = rows[index];

    var data = {};
    data.id=row.id;
    data.formName = "借用归还申请单";
    data.serialNumber = row.assetrevertCode;
    data.processDefKey = ASSETS_BORROW_RETURN;
    data.resultLocation = "assetrevert_query";
    data.produceType = row.produceTypeStr;
    data.assetrevertDepartmentId = row.assetrevertDepartmentID;
    data.assetrevertUserId = row.assetrevertUserID;
    getFirstNode(data);
}

function searchByDefaultQuerys() {
    $('#searchQuerys input[name="assetCode"]').val('');
    $('#searchQuerys input[name="assetChsName"]').val('');
    $('#assetrevertUserID').combobox("setValue", '');
    $('#realrevertTimeStart').datebox("setValue", '');//开始日期
    $('#realrevertTimeEnd').datebox("setValue", '');//结束日期
    $('#dataGridTable').datagrid('load', {
        assetCode: '',
        assetRevertTempStr: '',
        realrevertTimeStart: '',
        realrevertTimeEnd: '',
        assetrevertUserID: '',
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
        assetRevertTempStr: $('#searchQuerys input[name="assetChsName"]').val(),//资产名称
        realrevertTimeStart: realrevertTimeStart,//归还开始日期
        realrevertTimeEnd: realrevertTimeEnd,//归还结束日期
        assetrevertUserID: $('#assetrevertUserID').combobox("getValue"),//借用人
        createUserID: $('#createUserID').val()
    });
}

function returnOperateStr(status, value, index) {
    var bj = '<a href="javascript:void(0);" onClick="toUpdateAssetRevert(\'' + value + '\');">编辑</a> ';
    var sc = '<a href="javascript:void(0);" onClick="deleteAssetRevert(\'' + value + '\');">删除</a> ';
    var fqsp = '<a href="javascript:void(0);" onClick="UpdateAssetRevertStauts(\'' + index + '\');">发起审批</a> ';

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
        url: 'assetrevert/assetrevert_datagrid',
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
            {
                field: 'assetrevertCode', title: '归还单号', formatter: function (value, row, index) {
                    var updateBtn = '<a href="javascript:void(0);" onClick="lookAssetRevert(\'' + value + '\');">' + value + '</a> ';
                    return updateBtn;
                }
            },
            {
                field: 'type', title: '单据类型', formatter: function (value, row, index) {
                    return "归还单";
                }
            },
            {field: 'assetrevertDepartmentName', title: '归还部门'},
            {field: 'assetrevertUserName', title: '归还人'},
            {field: 'realrevertTimeStr', title: '归还日期'},
            {field: 'assetRevertTempStr', title: '归还资产'},
            {field: 'produceStr', title: '物资类型'},
            {field: 'produceTypeStr', hidden: true},
            {field: 'assetrevertDepartmentID', hidden: true},
            {field: 'assetrevertUserID', hidden: true},
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
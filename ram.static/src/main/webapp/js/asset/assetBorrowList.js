function toUpdateAssetBorrow(id) {
    window.location.href = 'assetborrow_update?id=' + id;
}

function lookAssetBorrow(assetborrowCode) {
    window.location.href = 'assetborrow_view?assetborrowCode=' + assetborrowCode;
}

function goToAssetBorrowPage() {
    window.location.href = 'assetborrow_create';
}

function goToAssetRevertPage() {
    window.location.href = 'assetrevert_create';
}

function deleteAssetBorrow(id) {
    $.messager.confirm('确定', '确定要删除吗？', function (r) {
        if (r) {
            $.ajax({
                url: 'assetborrow/delete_assetborrow',
                type: 'POST',
                data: {AssetBorrowId: id},
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
function UpdateAssetBorrowStauts(index) {
    var rows = $("#dataGridTable").datagrid("getRows");
    var row = rows[index];

    var data = {};
    data.id=row.id;
    data.formName = "借用申请单";
    data.serialNumber = row.assetborrowCode;
    data.processDefKey = ASSETS_BORROW;
    data.resultLocation = "assetborrow_query";
    data.produceType = row.produceTypeStr;
    getFirstNode(data);
}

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
        assetCode: $('#searchQuerys input[name="assetCode"]').val(),//资产编码
        assetBorrowTempStr: $('#searchQuerys input[name="assetChsName"]').val(),//资产名称
        createTimestampStart: createTimestampStart,//借用开始日期
        createTimestampEnd: createTimestampEnd,//借用结束日期
        assetborrowUserID: $('#assetborrowUserID').combobox("getValue"),//借用人
        createUserID: $('#createUserID').val()
    });
}

function searchByDefaultQuerys() {
    $('#searchQuerys input[name="assetCode"]').val('');
    $('#searchQuerys input[name="assetChsName"]').val('');
    $('#assetborrowUserID').combobox("setValue", '');
    $('#createTimestampStart').datebox("setValue", '');//开始日期
    $('#createTimestampEnd').datebox("setValue", '');//结束日期
    $('#dataGridTable').datagrid('load', {
        assetCode: '',
        assetBorrowTempStr: '',
        createTimestampStart: '',
        createTimestampEnd: '',
        assetborrowUserID: '',
        createUserID: $('#createUserID').val()
    });
}

function returnOperateStr(status, value, index) {
    var bj = '<a href="javascript:void(0);" onClick="toUpdateAssetBorrow(\'' + value + '\');">编辑</a> ';
    var sc = '<a href="javascript:void(0);" onClick="deleteAssetBorrow(\'' + value + '\');">删除</a> ';
    var fqsp = '<a href="javascript:void(0);" onClick="UpdateAssetBorrowStauts(\'' + index + '\');">发起审批</a> ';

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
        url: 'assetborrow/assetborrow_datagrid',
        fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        pagination: true,
        pageSize: 20,
        title: '资产借用台账',
        toolbar: '#btdiv',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {
            createUserID: $('#createUserID').val()
        },
        columns: [[
            {
                field: 'assetborrowCode', title: '借用单号', formatter: function (value, row, index) {
                    var updateBtn = '<a href="javascript:void(0);" onClick="lookAssetBorrow(\'' + value + '\');">' + value + '</a> ';
                    return updateBtn;
                }
            },
            {
                field: 'type', title: '单据类型', formatter: function (value, row, index) {
                    return "借用单";
                }
            },
            {field: 'assetborrowDepartmentName', title: '借用部门'},
            {field: 'assetborrowUserName', title: '借用人'},
            {field: 'createTimestamp', title: '借用日期'},
            {field: 'revertTimeStr', title: '预计归还日期'},
            {field: 'assetBorrowTempStr', title: '借用资产'},
            {
                field: 'revertStatusStr', title: '是否归还', formatter: function (value, row, index) {
                    var updateBtn = "";
                    switch (value) {
                        case 0:
                            updateBtn = "<span style='color:red;'>未归还<span>";
                            break;
                        case 1:
                            updateBtn = "<span style='color:red;'>部分归还<span>";
                            break;
                        case 2:
                            updateBtn = "<span>已归还<span>";
                            break;
                    }
                    return updateBtn;
                }
            },
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
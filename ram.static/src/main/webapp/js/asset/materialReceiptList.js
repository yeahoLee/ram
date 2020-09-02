function toUpdateReceipt(id) {
    window.location.href = 'updateReceipt?id=' + id;
}

function lookReceipt(id) {
    window.location.href = 'lookReceipt?id=' + id;
}

function deleteReceipt(id) {
    $.messager.confirm('确定', '确定要删除吗？', function (r) {
        if (r) {
            $.ajax({
                url: 'receipt/deleteReceipt',
                type: 'POST',
                data: {id: id},
                success: function (data) {
                    $.messager.alert('删除', '删除成功！', 'info');
                    $('#receiptTable').datagrid('reload');
                },
                error: function (data) {
                    AjaxErrorHandler(data);
                }
            });
        }
    });
}

function listSingleApprove(index) {
    var rows = $("#receiptTable").datagrid("getRows");
    var row = rows[index];

    var data = {};
    data.id=row.id;
    data.formName = row.receiptName;
    data.serialNumber = row.runningNum;
    data.processDefKey = ASSETS_ADD;
    data.resultLocation = "receiptList";
    data.produceType = row.produceTypeStr;
    getFirstNode(data);
}

function searchByQuerys() {
    $('#receiptTable').datagrid('load', {
        runningNum: $('#runningNum').val(),
        receiptName: $('#receiptName').val(),
        sourceType: $('#sourceType').combobox("getValue"),
        receiptStatus: $('#receiptStatus').combobox("getValue"),
        personId: $('#createUserID').val()
    })
}

function searchByDefaultQuerys() {
    $('#receiptTable').datagrid('load', {
        runningNum: '',
        receiptName: '',
        sourceType: '',
        receiptStatus: '',
        personId: $('#createUserID').val()
    })

    $('#runningNum').val('');
    $('#receiptName').val('');
    $('#sourceType').combobox("setValue", '');
    $('#receiptStatus').combobox("setValue", '');
}

//发送审批不走流程
function checkReceipt(id) {
    $.messager.confirm('确定', '确定要审核吗？', function (r) {
        if (r) {
            $.ajax({
                url: 'receipt/check_receipt',
                type: 'POST',
                data: {id: id},
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


function returnOperateStr(status, value, index) {
    var bj = '<a href="javascript:void(0);" onClick="toUpdateReceipt(\'' + value + '\');">编辑</a> ';
    var sc = '<a href="javascript:void(0);" onClick="deleteReceipt(\'' + value + '\');">删除</a> ';
    //走流程
    var fqsp = '<a href="javascript:void(0);" onClick="listSingleApprove(\'' + index + '\');">发起审批</a> ';
    //测试不走流程
    // var fqsp = '<a href="javascript:void(0);" onClick="checkReceipt(\'' + value + '\');">发起审批</a> ';

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
    fillEnumCombo('ram.asset.dto.FlowableInfo', 'WORKSTATUS', 'receiptStatus');

    $('#receiptTable').datagrid({
        url: 'receipt/receipt_datagrid',
        fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        pagination: true,
        pageSize: 20,
        title: '新增单列表',
        toolbar: '#receiptTableButtons',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {personId: $('#createUserID').val()},
        columns: [[
            {
                field: 'id', title: '操作', formatter: function (value, row, index) { //value, row, index
                    return returnOperateStr(row.receiptStatus, value, index);
                }
            },
            {
                field: 'runningNum', title: '资产新增单编号', formatter: function (value, row) {
                    return '<a href="javascript:void(0);" onClick="lookReceipt(\'' + row.id + '\');">' + value + '</a> ';
                }
            },
            {field: 'receiptName', title: '资产新增单名称'},
            {field: 'sourceTypeStr', title: '来源方式'},
            {field: 'assetCount', title: '资产数量'},
            {field: 'createTimestamp', title: '创建日期'},
            {field: 'produceStr', title: '物资类型'},
            {field: 'produceTypeStr', hidden: true},
            {field: 'receiptStatusStr', title: '状态'}
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });
});


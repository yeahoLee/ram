/*function toUpdateReceipt(id) {
	window.location.href = './updateReceipt?id=' + id;
}*/
function lookReceipt(id) {
    window.location.href = './asset_reduce_show?id=' + id;
}

function deleteReceipt(id) {
    $.messager.confirm('确定', '确定要删除吗？', function (r) {
        if (r) {
            $.ajax({
                url: 'assetReduce/delete_reduce_receipt',
                type: 'POST',
                data: {id: id},
                success: function (data) {
                    $.messager.alert('删除', '删除成功！', 'info', function () {
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

function checkReceipt(index) {
    var rows = $("#receiptTable").datagrid("getRows");
    var row = rows[index];

    var data = {};
    data.id=row.id;
    data.formName = row.changeNum;
    data.serialNumber = row.changeNum;
    data.processDefKey = ASSETS_IMPAIRMENT;
    data.resultLocation = "asset_reduce_list";
    data.produceType = row.produceTypeStr;
    getFirstNode(data);
}

//编辑
function toUpdateReceipt(id) {
    window.location.href = './asset_reduce_update?id=' + id;
}

function searchByQuerys() {
    $('#receiptTable').datagrid('load', {
        changeNum: $('#changeNum').val(),
        orderName: $("#orderName").val(),
        reduceType: $('#reduceType').combobox('getValue'),
        createUserId: $('#UserID').val()
    })
}

function searchByDefaultQuerys() {
    $('#changeNum').val('');
    $("#orderName").val('');
    $('#reduceType').combobox('clear');
    $('#receiptTable').datagrid('reload', {createUserId: $('#UserID').val()});
}

function returnOperateStr(status, value, index) {
    var bj = '<a href="javascript:void(0);" onClick="toUpdateReceipt(\'' + value + '\');">编辑</a> ';
    var sc = '<a href="javascript:void(0);" onClick="deleteReceipt(\'' + value + '\');">删除</a> ';
    var fqsp = '<a href="javascript:void(0);" onClick="checkReceipt(\'' + index + '\');">发起审批</a> ';

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
    //  fillEnumCombo('ram.asset.dto.FlowableInfo', 'WORKSTATUS', 'receiptStatus');
    $('#instSiteCodeDialog').dialog('close');

    $('#receiptTable').datagrid({
        url: 'assetReduce/reduce_receipt_datagrid',
        fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        pagination: true,
        pageSize: 20,
        title: '资产减损清单',
        toolbar: '#receiptTableButtons',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {createUserId: $('#UserID').val()},
        columns: [[
            {
                field: 'changeNum', title: '资产减损单号', formatter: function (value, row) {
                    return '<a href="javascript:void(0);" onClick="lookReceipt(\'' + row.id + '\');">' + value + '</a> ';
                }
            },
            {field: 'orderName', title: '资产减损单名称'},
            {field: 'reduceTypeStr', title: '减损类型'},
            {field: 'assetNum', title: '资产数量'},
            {field: 'createTimestamp', title: '创建日期'},
            {field: 'produceStr', title: '物资类型'},
            {field: 'produceTypeStr', hidden: true},
            {field: 'receiptStatusStr', title: '审批状态'},
            {field: 'receiptStatus', title: '审批状态值', hidden: 'true'},
            {field: 'id', title: '操作', formatter: function (value, row, index) { //value, row, index
                return returnOperateStr(row.receiptStatus, value, index);
            }
            },
        ]],
        onLoadError: function (data) {
            //AjaxErrorHandler(data);
        }
    });
});

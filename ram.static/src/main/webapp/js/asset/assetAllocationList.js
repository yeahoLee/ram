function toUpdateAssetAllocation(id) {
    window.location.href = 'assetallocation_update?id=' + id;
}

function lookAssetAllocation(code) {
    window.location.href = 'assetallocation_view?code=' + code;
}

function goToAssetAllocationPage() {
    window.location.href = 'assetallocation_create';
}

function deleteAssetAllocation(id) {
    $.messager.confirm('确定', '确定要删除吗？', function (r) {
        if (r) {
            $.ajax({
                url: 'assetallocation/delete_assetalloction',
                type: 'POST',
                data: {AssetAllocationId: id},
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
function UpdateAssetAllocationStauts(index) {
    var rows = $("#dataGridTable").datagrid("getRows");
    var row = rows[index];

    var data = {};
    data.id=row.id;
    data.formName = "调拨申请单";
    data.serialNumber = row.assetAllocationCode;
    data.processDefKey = ASSETS_TRANSFER;
    data.resultLocation = "assetallocation_query";
    data.produceType = row.produceTypeStr;
    getFirstNode(data);
}

function searchByDefaultQuerys() {
    $('#createTimestampStart').datebox("setValue", '');//调出开始日期
    $('#createTimestampEnd').datebox("setValue", '');//调出结束日期
    $('#searchQuerys input[name="assetAllocationCode"]').val('');//调出编码
    $('#callInDepartmentId').combotree("setValue", '');//目标部门
    $('#callOutDepartmentId').combotree("setValue", '');//原有部门
    $('#dataGridTable').datagrid('load', {
        assetAllocationCode: '',
        createTimestampStart: '',
        createTimestampEnd: '',
        callInDepartmentId: '',
        callOutDepartmentId: '',
        createUserID: $('#createUserID').val()
    });
}

function searchByQuerys() {
    var createTimestampStart = $('#createTimestampStart').datebox("getValue");//调出开始日期
    var createTimestampEnd = $('#createTimestampEnd').datebox("getValue");//调出结束日期
    var startTime = DateTimeFormatter2(createTimestampStart);
    var endTime = DateTimeFormatter2(createTimestampEnd);
    if (startTime != "" && endTime != "" && startTime > endTime) {
        $.messager.alert('提示', '结束日期不能小于开始日期！', 'info');
        return;
    }
    $('#dataGridTable').datagrid('load', {
        assetAllocationCode: $('#searchQuerys input[name="assetAllocationCode"]').val(),//调出编码
        createTimestampStart: createTimestampStart,//调出开始日期
        createTimestampEnd: createTimestampEnd,//调出结束日期
        callInDepartmentId: $('#callInDepartmentId').combotree("getValue"),//目标部门
        callOutDepartmentId: $('#callOutDepartmentId').combotree("getValue"),//原有部门
        createUserID: $('#createUserID').val()
    });
}

function listSingleApprove(index) {
    var rows = $("#dataGridTable").datagrid("getRows");
    var row = rows[index];
    $("#approveInfo input[name='id']").val(row.id);
    $("#approveInfo input[name='serialNumber']").val(row.assetAllocationCode);
    $("#approveInfo input[name='receiptName']").val(row.assetAllocationCode);
    // getFirstNode(ASSETS_ADD);
}

function returnOperateStr(status, value, index) {
    var bj = '<a href="javascript:void(0);" onClick="toUpdateAssetAllocation(\'' + value + '\');">编辑</a> ';
    var sc = '<a href="javascript:void(0);" onClick="deleteAssetAllocation(\'' + value + '\');">删除</a> ';
    var fqsp = '<a href="javascript:void(0);" onClick="UpdateAssetAllocationStauts(\'' + index + '\');">发起审批</a> ';

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
        url: 'assetallocation/assetallocation_datagrid',
        fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        pagination: true,
        pageSize: 20,
        title: '资产调拨台账',
        toolbar: '#btdiv',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {
            createUserID: $('#createUserID').val()
        },
        columns: [[
//			{field: 'id', checkbox:true},
            {
                field: 'assetAllocationCode', title: '调拨单号', formatter: function (value, row, index) {
                    var updateBtn = '<a href="javascript:void(0);" onClick="lookAssetAllocation(\'' + value + '\');">' + value + '</a> ';
                    return updateBtn;
                }
            },
            {
                field: 'type', title: '单据类型', formatter: function (value, row, index) {
                    return "调拨单";
                }
            },
            {field: 'callOutDepartmentName', title: '原有部门'},
            {field: 'callInDepartmentName', title: '目标部门'},
            {field: 'createTimestamp', title: '发起日期'},
            {field: 'assetAllocationTempStr', title: '变更资产'},
            {field: 'produceStr', title: '物资类型'},
            {field: 'produceTypeStr', hidden: true},
            {field: 'receiptStatus', title: '审批状态'},
            {field: 'id', title: '操作', formatter: function (value, row, index) { //value, row, index
                    return returnOperateStr(row.receiptStatusStr + "", value, index);
                }
            }
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });
})
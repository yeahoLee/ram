function toUpdateAssetReceiveUse(id) {
    window.location.href = 'assetreceiveuse_update?id=' + id;
}

function lookAssetReceiveUse(assetReceiveUseCode) {
    window.location.href = 'assetreceiveuse_view?assetReceiveUseCode=' + assetReceiveUseCode;
}

function goToAssetReceiveUsePage() {
    window.location.href = 'assetreceiveuse_create';
}

function goToAssetReceiveRevertPage() {
    window.location.href = 'assetreceiverevert_create';
}

function deleteAssetReceiveUse(id) {
    $.messager.confirm('确定', '确定要删除吗？', function (r) {
        if (r) {
            $.ajax({
                url: 'assetreceiveuse/delete_assetreceiveuse',
                type: 'POST',
                data: {assetReceiveUseID: id},
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
function UpdateAssetReceiveUseStauts(index) {
    var rows = $("#dataGridTable").datagrid("getRows");
    var row = rows[index];

    var data = {};
    data.id=row.id;
    data.formName = "领用申请单";
    data.serialNumber = row.assetReceiveUseCode;
    data.processDefKey = ASSETS_USE;
    data.resultLocation = "assetreceiveuse_query";
    data.produceType = row.produceTypeStr;
    getFirstNode(data);
}

function searchByDefaultQuerys() {
    $('#receiveTimeStart').datebox("setValue", '');//调出开始日期
    $('#receiveTimeEnd').datebox("setValue", '');//调出结束日期
    $('#searchQuerys input[name="assetCode"]').val('');//资产编码
    $('#searchQuerys input[name="assetChsName"]').val('');//资产名称
    $('#assetReceiveUseUserID').combobox("setValue", '');//领用人
    $('#dataGridTable').datagrid('load', {
        assetCode: '',
        assetReceiveUseTempStr: '',
        receiveTimeStart: '',
        receiveTimeEnd: '',
        assetReceiveUseUserID: '',
        createUserID: $('#createUserID').val()
    });
}

function searchByQuerys() {
    var receiveTimeStart = $('#receiveTimeStart').datebox("getValue");//调出开始日期
    var receiveTimeEnd = $('#receiveTimeEnd').datebox("getValue");//调出结束日期
    var startTime = DateTimeFormatter2(receiveTimeStart);
    var endTime = DateTimeFormatter2(receiveTimeEnd);
    if (startTime != "" && endTime != "" && startTime > endTime) {
        $.messager.alert('提示', '结束日期不能小于开始日期！', 'info');
        return;
    }

    $('#dataGridTable').datagrid('load', {
        assetCode: $('#searchQuerys input[name="assetCode"]').val(),//资产编码
        assetReceiveUseTempStr: $('#searchQuerys input[name="assetChsName"]').val(),//资产名称
        receiveTimeStart: receiveTimeStart,//领用开始日期
        receiveTimeEnd: receiveTimeEnd,//领用结束日期
        assetReceiveUseUserID: $('#assetReceiveUseUserID').combobox("getValue"),//领用人
        createUserID: $('#createUserID').val()
    });
}

function returnOperateStr(status, value, index) {
    var bj = '<a href="javascript:void(0);" onClick="toUpdateAssetReceiveUse(\'' + value + '\');">编辑</a> ';
    var sc = '<a href="javascript:void(0);" onClick="deleteAssetReceiveUse(\'' + value + '\');">删除</a> ';
    var fqsp = '<a href="javascript:void(0);" onClick="UpdateAssetReceiveUseStauts(\'' + index + '\');">发起审批</a> ';

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
        url: 'assetreceiveuse/assetreceiveuse_datagrid',
        fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        pagination: true,
        pageSize: 20,
        title: '资产领用台账',
        toolbar: '#btdiv',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {
            createUserID: $('#createUserID').val()
        },
        columns: [[
//			{field: 'id', checkbox:true},
            {
                field: 'assetReceiveUseCode', title: '领用单号', formatter: function (value, row, index) {
                    var updateBtn = '<a href="javascript:void(0);" onClick="lookAssetReceiveUse(\'' + value + '\');">' + value + '</a> ';
                    return updateBtn;
                }
            },
            {
                field: 'type', title: '单据类型', formatter: function (value, row, index) {
                    return "领用单";
                }
            },
            {field: 'assetReceiveUseDepartmentName', title: '领用部门'},
            {field: 'assetReceiveUseUserName', title: '领用人'},
            {field: 'createTimestamp', title: '创建日期'},
            // {field: 'receiveTimeStr', title: '预计归还日期'},
            {field: 'assetReceiveUseTempStr', title: '领用资产'},
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
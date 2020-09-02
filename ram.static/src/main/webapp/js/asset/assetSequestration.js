//多条件查询
function searchByQuerys() {
    var createTimestampStart = $('#createTimestampStart').datebox("getValue");
    var createTimestampEnd = $('#createTimestampEnd').datebox("getValue");
    var startTime = DateTimeFormatter2(createTimestampStart);
    var endTime = DateTimeFormatter2(createTimestampEnd);
    if (startTime != "" && endTime != "" && startTime > endTime) {
        $.messager.alert('提示', '结束日期不能小于开始日期！', 'info');
        return;
    }
    $('#dataGridTable').datagrid('load', {
        sequestrateNum: $('#searchQuerys input[name="sequestrateNum"]').val(),
        createTimestampStart: createTimestampStart,
        createTimestampEnd: createTimestampEnd,
        assetChsName: $('#searchQuerys input[name="assetChsName"]').val()
    })
}


function resetQuerys() {
    $('#searchQuerys input[name="sequestrateNum"]').val('');
    $('#createTimestampStart').datebox("setValue", '');
    $('#createTimestampEnd').datebox("setValue", '');
}

function deleteAsssetSubmit(id) {
    $.messager.confirm('确定', '确定要删除吗？', function (r) {
        if (r) {
            $.ajax({
                url: 'assetSequestration/assetseal_delete',
                type: 'POST',
                data: {id: id},
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

//发起审批
function approvalInitiation(index) {
    var rows = $("#dataGridTable").datagrid("getRows");
    var row = rows[index];

    var data = {};
    data.id=row.id;
    data.serialNumber = row.sequestrateNum;
    data.processDefKey = FIXED_ASSETS_ARCHIVE_CENTER;
    data.resultLocation = "assetsequestration_query";
    data.produceType = row.produceTypeStr;
    if (row.sealStatus == "封存") {
        data.formName = "封存申请单";
        data.publicUse = "0";
    } else {
        data.formName = "启存申请单";
        data.publicUse = "1";
    }
    getFirstNode(data);
}

//查看封存单
function goToViewAssetSealPage(id) {
    window.location.href = 'assetsequestration_view_seal?id=' + id;
}

//查看启封资产单
function goToViewAssetUnsealPage(id) {
    window.location.href = 'assetsequestration_view_unseal?id=' + id;
}

function goToUpdateAssetPageSubmit(id) {
    window.location.href = 'assetsequestration_update?id=' + id;
}

//封存视图界面
function goToSealViewPage() {
    window.location.href = 'asset_stan_book?backToLastPageUrl=' + "seal";
}

//创建资产封存单界面
function goToAssetSealPage() {
    window.location.href = 'assetsequestration_seal';
}

function goToUpdateAssetUnsealPageSubmit(id) {
    window.location.href = 'assetsequestration_updateUnseal?id=' + id;
}

//启封资产界面
function goToAssetUnsealPage() {
    window.location.href = 'assetsequestration_unseal';
}

function returnOperateStr(status, value, index) {
    var bj = '<a href="javascript:void(0);" onClick="goToUpdateAssetUnsealPageSubmit(\'' + value + '\');">编辑</a> ';
    var sc = '<a href="javascript:void(0);" onClick="deleteAsssetSubmit(\'' + value + '\');">删除</a> ';
    var fqsp = '<a href="javascript:void(0);" onClick="approvalInitiation(\'' + index + '\');">发起审批</a> ';

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
        url: 'assetSequestration/assetsequestration_datagrid',
        fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        singleSelect: true,
        pagination: true,
        pageSize: 20,
        toolbar: '#asdiv',
        loadMsg: '程序处理中，请稍等...',
        columns: [[

            {
                field: 'sequestrateNum', title: '封存编号', formatter: function (value, row, index) { //value, row, index
                    var returnVar = '';
                    if (row.sealStatus == '封存')
                        returnVar = '<a href="javascript:void(0);" onClick="goToViewAssetSealPage(\'' + row.id + '\');">' + value + '</a> '
                    else
                        returnVar = '<a href="javascript:void(0);" onClick="goToViewAssetUnsealPage(\'' + row.id + '\');">' + value + '</a> '
                    return returnVar;
                }
            },
            {field: 'sealReason', title: '封存/启用原因'},
            {field: 'sealStatus', title: '类型'},
            {field: 'sponsor', title: '发起人', hidden: true},
            {field: 'sponsorStr', title: '发起人'},
            {field: 'produceStr', title: '物资类型'},
            {field: 'produceTypeStr', hidden: true},
            {field: 'sealApproveStatus', title: '审批状态'},
            {field: 'launchDate', title: '发起日期', formatter: function (value) {
                    return DateTimeFormatter2(value);
                }
            },
            {field: 'id', title: '操作', formatter: function (value, row, index) { //value, row, index
                    return returnOperateStr(row.sealApproveStatusStr + "", value, index);
                }
            }
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });
});
/*function toUpdateReceipt(id) {
	window.location.href = './updateReceipt?id=' + id;
}*/
function lookReceipt(id) {
    window.location.href = './asset_savePlace_change_show?id=' + id;
}

function deleteSavePlaceChangeReceipt(id) {
    $.messager.confirm('确定', '确定要删除吗？', function (r) {
        if (r) {
            $.ajax({
                url: 'assetChange/delete_change_savePlace_receipt',
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

function checkSavePlaceChangeReceipt(index) {
    var rows = $("#receiptTable").datagrid("getRows");
    var row = rows[index];

    var data = {};
    data.id=row.id;
    data.formName = "位置变更申请单";
    data.serialNumber = row.changeNum;
    data.processDefKey = ASSETS_USER_LOCATION_CHANGE;
    data.resultLocation = "asset_savePlace_change_list";
    data.publicUse = "2";
    data.produceType = row.produceTypeStr;

    getFirstNode(data);
}

//编辑
function toUpdateSavePlaceChangeReceipt(id) {
    window.location.href = './asset_savePlace_change_update?id=' + id;
}

//重置搜索
function clearSearch() {
    $('#createTimestampStart').datebox("setValue", "");
    $('#createTimestampEnd').datebox("setValue", "");
    $('#assetSavePlaceId').val("");
    $('#changeNum').val("");
    $('#showCodeAndData').html("");

    $('#receiptTable').datagrid('load', {
        changeNum: "",
        assetSavePlaceId: "",
        createTimestampStart: "",
        createTimestampEnd: "",
        createUserId: $('#createUserID').val()
    })
}

function searchByQuerys() {
    var createTimestampStart = $('#createTimestampStart').datebox("getValue");
    var createTimestampEnd = $('#createTimestampEnd').datebox("getValue");
    if (createTimestampStart != "" && createTimestampEnd != "" && createTimestampStart > createTimestampEnd) {
        $.messager.alert('提示', '结束日期 不能小于开始日期！', 'info');
        return false;
    }
    $('#receiptTable').datagrid('load', {
        changeNum: $('#changeNum').val(),
        assetSavePlaceId: $('#assetSavePlaceId').val(),
        createTimestampStart: createTimestampStart,
        createTimestampEnd: createTimestampEnd,
        createUserId: $('#createUserID').val()
    })
}

function returnOperateStr(status, value, index) {
    var bj = '<a href="javascript:void(0);" onClick="toUpdateSavePlaceChangeReceipt(\'' + value + '\');">编辑</a> ';
    var sc = '<a href="javascript:void(0);" onClick="deleteSavePlaceChangeReceipt(\'' + value + '\');">删除</a> ';
    var fqsp = '<a href="javascript:void(0);" onClick="checkSavePlaceChangeReceipt(\'' + index + '\');">发起审批</a> ';

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
    $('#instSiteCodeDialog').dialog('close');

    $('#receiptTable').datagrid({
        url: 'assetChange/savePlace_change_receipt_datagrid',
        fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        pagination: true,
        pageSize: 20,
        title: '资产安装位置变更清单',
        toolbar: '#receiptTableButtons',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {createUserId: $('#createUserID').val()},
        columns: [[
            {
                field: 'changeNum', title: '变更单号', formatter: function (value, row) {
                    return '<a href="javascript:void(0);" onClick="lookReceipt(\'' + row.id + '\');">' + value + '</a> ';
                }
            },
//			{field: 'oldAssetSavePlaceStr',title: '原安装位置'},
            {field: 'assetSavePlaceStr', title: '新安装位置'},
            {field: 'createTimestamp', title: '发起日期'},
            {field: 'assetChange', title: '变更资产'},
            {field: 'produceStr', title: '物资类型'},
            {field: 'produceTypeStr', hidden: true},
            {field: 'receiptStatusStr', title: '审批状态'},
            {field: 'receiptStatus', title: '审批状态值', hidden: 'true'},
            {field: 'id', title: '操作', formatter: function (value, row, index) { //value, row, index
                    return returnOperateStr(row.receiptStatus, value, index);
                }
            }
        ]],
        onLoadError: function (data) {
            //AjaxErrorHandler(data);
        }
    });
});

function closeDialog() {
    $('#instSiteCodeDialog').dialog('close');
}

var isDialogInit = false;

function openDialig() {
    if (!isDialogInit) {
        $('#instSiteCodeDialogFrame').attr('src', 'asset_save_place');
        isDialogInit = true;
    }
    $('#instSiteCodeDialog').dialog('center').dialog('open');
}

function showCodeAndName(id, codeAndName) {
    $('#showCodeAndData').text(codeAndName);
    $('#assetSavePlaceId').val(id);
}

//待页面加载完成后加载以下方法
window.onload = function () {
    //重写 datebox 格式化方法
    $('#createTimestampStart').datebox({
        closeText: '关闭',
        formatter: function (date) {
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
            var h = date.getHours();
            var M = date.getMinutes();
            var s = date.getSeconds();

            function formatNumber(value) {
                return (value < 10 ? '0' : '') + value;
            }

//			alert(formatNumber(h));
            return y + '-' + formatNumber(m) + '-' + formatNumber(d);
        },
        parser: function (s) {
            var t = Date.parse(s);
            if (!isNaN(t)) {
                return new Date(t);
            } else {
                return new Date();
            }
        }
    });
    //重写 datebox 格式化方法
    $('#createTimestampEnd').datebox({
        closeText: '关闭',
        formatter: function (date) {
            var y = date.getFullYear();
            var m = date.getMonth() + 1;
            var d = date.getDate();
            var h = date.getHours();
            var M = date.getMinutes();
            var s = date.getSeconds();

            function formatNumber(value) {
                return (value < 10 ? '0' : '') + value;
            }

//			alert(formatNumber(h));
            return y + '-' + formatNumber(m) + '-' + formatNumber(d);
        },
        parser: function (s) {
            var t = Date.parse(s);
            if (!isNaN(t)) {
                return new Date(t);
            } else {
                return new Date();
            }
        }
    });
};

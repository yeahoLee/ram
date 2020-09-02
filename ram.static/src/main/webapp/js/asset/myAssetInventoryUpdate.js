function backToLastPage() {
    window.location.href = "myassetinventory_query";
}

function importAssetXls() {
    $('#importAssetConfirmBtn').linkbutton('disable');
    $('#importForm').form('submit', {
        url: 'assetInventory/import_assetinventory_xls',
        success: function (data) {
            var result = $.parseJSON(data);
            var requestData = [];
            if (!result.errorMessage) {
                var res = result.assetInventoryDtoList;
                var myAssetInventoryDto = result.myAssetInventoryDto;
                $('#inventoryProfit').html(myAssetInventoryDto.inventoryProfit);
                $('#inventoryLoss').html(myAssetInventoryDto.inventoryLoss);
                for (var i = 0; i < res.length; i++) {
                    requestData.push({
                        id: res[i].id,
                        assetId: res[i].assetId,
                        operation: res[i].operation,
                        resultStr: res[i].result,
                        inventoryWayStr: res[i].inventoryWay,
                        remark: res[i].remark,
                        assetCode: res[i].assetCode,
                        assetTypeStr: res[i].assetTypeStr,
                        specAndModels: res[i].specAndModels,
                        buyDate: res[i].buyDate,
                        unitOfMeasStr: res[i].unitOfMeasStr,
                        equiOrigValue: res[i].equiOrigValue,
                        unitOfMeasStr: res[i].unitOfMeasStr,
                        assetStatus: res[i].assetStatus,
                        savePlaceStr: res[i].savePlaceStr,
                        useDeptStr: res[i].useDeptStr,
                        userStr: res[i].userStr,
                        manageDeptStr: res[i].manageDeptStr,
                        managerStr: res[i].managerStr
                    });
                }
                var index = "";
                for (var i = 0; i < requestData.length; i++) {
                    index = $('#assetInventoryDataGrid').datagrid('getRowIndex', requestData[i].id);
                    if (index != -1)
                        $('#assetInventoryDataGrid').datagrid('updateRow', {index: index, row: requestData[i]}); // 将数据绑定到datagrid
                    else
                        $('#assetInventoryDataGrid').datagrid('appendRow', requestData[i]); // 将数据绑定到datagrid
                }
                $.messager.alert('提示', '上传成功!', 'info');
                $('#importAssetConfirmBtn').linkbutton('enable');
                $('#importAssetDialog').dialog('close');
            } else {
                $.messager.alert('提示', '上传失败:' + result.errorMessage, 'error');
                $('#importAssetConfirmBtn').linkbutton('enable');
            }
        },
    });
}

function exportExcel() {
    var data = $('#assetInventoryDataGrid').datagrid('getSelections');
    var myAssetInventoryId = $('#MyAssetInventoryId').val();
    if (data.length == 0) {
        $.messager.alert('提示', '未选择资产,则默认导出所有!', 'info', function () {
            window.location.href = 'assetInventory/export_assetinventoryAll_xls?myAssetInventoryId=' + myAssetInventoryId;
        });
    }
    else {
        var str = "";
        for (var i = 0; i < data.length; i++) {
            str = str + "," + data[i].id;
        }
        if (str.length > 0)
            str = str.substring(1, str.length);

        window.location.href = 'assetInventory/export_assetinventory_xls?assetInventoryDtoList=' + str;
    }

}

function save() {
    var reason = $('#reason').val();
    var disposalAdvice = $('#disposalAdvice').val();
    var myAssetInventoryId = $('#MyAssetInventoryId').val();
    var inventoryProfit = $('#inventoryProfit').html();
    var inventoryLoss = $('#inventoryLoss').html();
    if ((parseFloat(inventoryLoss) != 0 || parseFloat(inventoryProfit) != 0) && reason == "") {
        $.messager.alert('提示', '当前有资产为盘盈或盘亏,盘盈盘亏原因必填', 'error');
        return false;
    }
    if ((parseFloat(inventoryLoss) != 0 || parseFloat(inventoryProfit) != 0) && disposalAdvice == "") {
        $.messager.alert('提示', '当前有资产为盘盈或盘亏,处置意见必填', 'error');
        return false;
    }
    var dataparam = {
        reason: reason,
        disposalAdvice: disposalAdvice,
        id: myAssetInventoryId
    }

    return dataparam;
}

function saveMyAssetInventorySubmit(type) {
    $('#saveBtn').attr('disabled', "true");
    var data = save();
    if (data == false) {
        $('#saveBtn').removeAttr('disabled');
        return false;
    } else {
        $.ajax({
            url: 'assetInventory/update_myinventory',
            type: 'POST',
            data: data,
            success: function (data) {
                $('#saveBtn').removeAttr('disabled');
                if (type == 1) {
                    $.messager.alert('提示', '保存成功！', 'info', function () {
                        window.location.href = "myassetinventory_query";
                    });
                } else {
                    var data1 = {};
                    data1.id=data.id;
                    data1.formName = $("#inventoryName").text();
                    data1.serialNumber = $("#myAssetInventoryCode").text();
                    data1.processDefKey = ASSETS_INVENTORY_RESULT;
                    data1.resultLocation = "myassetinventory_query";
                    data1.produceType =$("#produceType").val();
                    getFirstNode(data1);
                }
            },
            error: function (data) {
                $('#saveBtn').removeAttr('disabled');
                AjaxErrorHandler(data);
            }
        });
    }
}

function SaveAndSendMyAssetInventorySubmit() {
    $.messager.confirm('确定', '确定要保存并发送吗？', function (r) {
        if (r) {
            $('#saveandsendBtn').attr('disabled', "true");
            var data = save();
            if (data == false) {
                $('#saveandsendBtn').removeAttr('disabled');
                return false;
            } else {
                $.ajax({
                    url: 'assetInventory/update_myinventory',
                    type: 'POST',
                    data: data,
                    success: function (data) {
                        updateMyAssetInventoryStatus(data.id);
                    },
                    error: function (data) {
                        $('#saveandsendBtn').removeAttr('disabled');
                        AjaxErrorHandler(data);
                    }
                });
            }
        }
    });
}

function updateMyAssetInventoryStatus(id) {
    $.ajax({
        url: 'assetInventory/myinventorytemp_submit',
        type: 'POST',
        data: {
            id: id,
            myinventoryStatusStr: 1
        },
        success: function (data) {
            $('#saveandsendBtn').removeAttr('disabled');
            $.messager.alert('提示', '发起审批成功！', 'info', function () {
                window.location.href = "myassetinventory_query";
            });
        },
        error: function (data) {
            $('#saveandsendBtn').removeAttr('disabled');
            AjaxErrorHandler(data);
        }
    });
}

function UpdateAssetInventoryTemp(id, assetId, resultStr) {
    $('#TempId').val(id);
    $('#assetId').val(assetId);
    $('#oldResultStr').val(resultStr);
    $('#result').combobox('setValue', '');
    $('#remark').val('');
    $('#assetInventoryTempDialog').dialog('center').dialog('open');
}

function saveAssetInventoryTemp() {
    var Id = $('#TempId').val();
    var assetId = $('#assetId').val();
    var result = $('#result').combobox("getValue");
    var resultStr = $('#result').combobox("getText");
    if (resultStr == "" || resultStr == null) {
        $.messager.alert('提示', '请填写盘点结果', 'error');
        return;
    }
    var remark = $('#remark').val();
    var inventoryProfit = $('#inventoryProfit').html();
    var inventoryLoss = $('#inventoryLoss').html();
    var oldresultStr = $('#oldResultStr').val();
    $.ajax({
        url: 'assetInventory/update_inventorytemp',
        type: 'POST',
        data: {
            id: Id,
            result: resultStr,
            remark: remark,
            operation: "手工录入",
            inventoryWay: "手工"
        },
        success: function (data) {
            $.messager.alert('提示', '更新成功！', 'info', function () {
                $('#assetInventoryTempDialog').dialog('close');
                var index = $('#assetInventoryDataGrid').datagrid('getRowIndex', Id);
                $('#assetInventoryDataGrid').datagrid('updateRow', {
                    index: index,
                    row: {resultStr: resultStr, remark: remark, operationStr: "手工录入", inventoryWayStr: "手工"}
                }); //
                $('#assetInventoryDataGrid').datagrid('autoSizeColumn');
                if (resultStr == "盘亏") {
                    $('#inventoryLoss').html(parseFloat(inventoryLoss) + 1);
                }
                if (resultStr == "盘盈") {
                    $('#inventoryProfit').html(parseFloat(inventoryProfit) + 1);
                }
                if (resultStr == "相符") {
                    if (oldresultStr == "盘亏")
                        $('#inventoryLoss').html(parseFloat(inventoryLoss) - 1);
                    if (oldresultStr == "盘盈") {
                        $('#inventoryProfit').html(parseFloat(inventoryProfit) - 1);
                    }
                }
            });
        },
        error: function (data) {
            $('#assetInventoryTempDialog').dialog('close');
            AjaxErrorHandler(data);
        }
    });
}

$(function () {
    fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_STATUS', 'assetStatus');

    var myAssetInventoryId = $('#MyAssetInventoryId').val();
    var AssetInventoryId = $('#AssetInventoryId').val();
    var DeptID = $('#DeptID').val();
    $('#assetInventoryDataGrid').datagrid({
        url: 'assetInventory/inventorytemp_datagrid',
        //fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        pagination: true,
        pageSize: 20,
        idField: 'id',
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {
            assetInventoryId: AssetInventoryId,
            myAssetInventoryId: myAssetInventoryId,
            manageDeptId: DeptID
        },
        columns: [[
            {field: 'id', checkbox: true},
            {
                field: 'operationStr', title: '操作', formatter: function (value, row, index) {
                    var updateBtn = '<a href="javascript:void(0);" onClick="UpdateAssetInventoryTemp(\'' + row.id + '\',\'' + row.assetId + '\',\'' + row.resultStr + '\');">' + value + '</a> ';
                    return updateBtn;
                }
            },
            {field: 'resultStr', title: '盘点结果'},
            {field: 'inventoryWayStr', title: '盘点方式'},
            {field: 'remark', title: '备注'},
            {field: 'assetCode', title: '资产编码'},
            {field: 'assetChsName', title: '资产名称'},
            {field: 'specAndModels', title: '规格型号'},
            {field: 'buyDate', title: '购置日期'},
            {field: 'unitOfMeasStr', title: '计量单位'},
            {field: 'equiOrigValue', title: '资产原值'},
            {field: 'residualValue', title: '残余价值'},
            {field: 'assetStatus', title: '资产状态'},
            {field: "savePlaceStr", title: "安装位置"},
            {field: 'useDeptStr', title: '使用部门'},
            {field: 'userStr', title: '使用人'},
            {field: 'manageDeptStr', title: '主管部门'},
            {field: 'managerStr', title: '资产管理员'}
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });

    //$('#assetInventoryDataGrid').datagrid("autoSizeColumn");
})

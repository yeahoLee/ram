var other='8';
var cut=','
var stateList = "6,7";
var defaulState=stateList +cut+ other;

function openAdvaSearchDialog() {
    var advaSearch = $("#advaSearch").css("display");
    if (advaSearch == 'none')
        $('#advaSearch').show();
    else
        $('#advaSearch').hide();
}

function backToLastPage() {
    window.location.href = "assetallocation_query";
}

function defaultSearch() {
    $('#searchQuerys input[name="assetCode"]').val('');
    $('#searchQuerys input[name="assetChsName"]').val('');
    $('#useDeptId').combotree("setValue", '');
    $('#userId').combobox("setValue", '');
    $('#dataGridTable').datagrid('load', {
        produceTypeStr: $('#produceType').combobox("getValue"),
        manageDeptId: $('#callOutDepartmentId').combotree("getValue"),
        assetStatus: defaulState,
        showType: "1",
        managerId: $('#UserID').val()
    })
}

function searchByQuerys() {
    $('#dataGridTable').datagrid('load', {
        produceTypeStr: $('#produceType').combobox("getValue"),
        assetCode: $('#searchQuerys input[name="assetCode"]').val(),
        assetChsName: $('#searchQuerys input[name="assetChsName"]').val(),
        assetStatus: defaulState,
        showType: "1",
        manageDeptId: $('#callOutDepartmentId').combotree("getValue"),
        managerId: $('#CreateUserID').val(),
        useDeptId: $('#useDeptId').combotree("getValue"),
        userId: $('#userId').combobox("getValue")
    })
}

function openAssetListDialog() {
    var produceType = $('#produceType').combobox("getValue");
    if (produceType == null || produceType == "") {
        $.messager.alert('错误', '请先选择物资的类型！', 'error');
        return;
    }
    var callOutDepartmentId = $('#callOutDepartmentId').combotree("getValue");
    if (callOutDepartmentId == null || callOutDepartmentId == "") {
        $.messager.alert('错误', '请先选择调出部门！', 'error');
        return;
    } else {
        defaultSearch();
        $('#assetListDialog').dialog('center').dialog('open');
    }
}

function addAssetToList() {
    var data = $('#dataGridTable').datagrid('getSelections');
    var assetIdList = "";
    var AssetAllocationID = $('#AssetAllocationID').val();
    if (data.length == 0) {
        $.messager.alert('错误', '选择一个添加项！', 'error');
        return;
    }

    var val = $('#callOutDepartmentId').combotree("getValue");
    $('#callOutDepartmentId').combotree({
        disabled: true,
        value: val
    });
    var getData = $('#assetAllocationTempDataGridTable').datagrid('getData');

    for (var index in data) {
        if (contains(getData.rows, data[index].id) == false)
            assetIdList = (assetIdList + "," + data[index].id);
    }

    if (assetIdList.substr(0, 1) == ',')
        assetIdList = assetIdList.substr(1);
    $.ajax({
        url: 'assetallocation/create_assetallocationtemp',
        type: 'POST',
        data: {
            assetIdList: assetIdList,
            id: AssetAllocationID
        },
    success: function (data) {
        $('#assetAllocationTempDataGridTable').datagrid('load', {assetAllocationId: AssetAllocationID});
        $.messager.alert('提示', '添加成功！', 'info', function () {
                $('#assetListDialog').dialog('close');
            });
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function removeAssetToList() {
    var data = $('#assetAllocationTempDataGridTable').datagrid('getSelections');
    var AssetAllocationID = $('#AssetAllocationID').val();
    var assetIdList = "";
    if (data.length == 0) {
        $.messager.alert('错误', '选择一个移除项！', 'error');
        return;
    }
    // 删除选中行
    for (var i = data.length - 1; i >= 0; i--) {
        assetIdList = (assetIdList + "," + data[i].id);
        if (assetIdList.substr(0, 1) == ',')
            assetIdList = assetIdList.substr(1);

    }
    $.ajax({
        url: 'assetallocation/delete_assetallocationtemp',
        type: 'POST',
        data: {
            assetIdList: assetIdList,
            AssetAllocationID: AssetAllocationID
        },
        success: function (data) {
            $('#assetAllocationTempDataGridTable').datagrid('load', {assetAllocationId: AssetAllocationID});
            $.messager.alert('提示', '删除成功！', 'info', function () {
                var data1 = $('#assetAllocationTempDataGridTable').datagrid('getData');
                if (data1.total == 0) {
                    $('#callOutDepartmentId').combotree({
                        disabled: false
                    });
                    $('#produceType').combobox({
                        disabled: false
                    });
                }

            });
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function openDialogForSavePlace() {
    var data = $('#assetSequestrationSealDataGrid').datagrid('getSelections');
    if (data.length == 0) {
        $.messager.alert('错误', '选择一个资产项！', 'error');
    }

    if (!isDialogInit) {
        $('#instSiteCodeDialogFrame').attr('src', 'asset_save_place');
        isDialogInit = true;
    }
    $('#instSiteCodeDialog').dialog('center').dialog('open');
}


function showCodeAndName(id, codeAndName) {
    $('#showCodeAndData').html(codeAndName);
    $('#callInSavePlaceIdHidden').val(id);
}

function closeDialog() {
    $('#instSiteCodeDialog').dialog('close');
}

function update() {
    var callOutDepartmentId = $('#callOutDepartmentId').combotree("getValue");
    var callInDepartmentId = $('#callInDepartmentId').combotree("getValue");
    var callInAssetManagerId = $('#callInAssetManagerId').combobox("getValue");
    var callInSavePlaceId = $('#callInSavePlaceIdHidden').val();
    var reason = $('#reason').val();
    var AssetAllocationID = $('#AssetAllocationID').val();
    var AssetAllocationCode = $('#AssetAllocationCode').val();
    var CreateUserID = $('#CreateUserID').val();

    var produceType = $('#produceType').combobox("getValue");
    if (produceType == null || produceType == "") {
        $.messager.alert('错误', '请先选择物资的类型！', 'error');
        return;
    }

    if (callOutDepartmentId == null || callOutDepartmentId == "") {
        $.messager.alert('错误', '请选择调出部门！', 'error');
        return false;
    }
    if (callInDepartmentId == null || callInDepartmentId == "") {
        $.messager.alert('错误', '请选择调入部门！', 'error');
        return false;
    }
    if (reason == null || reason == "") {
        $.messager.alert('错误', '请填写调拨原因！', 'error');
        return false;
    }

    var dataparam = {
        callOutDepartmentId: callOutDepartmentId,
        callInDepartmentId: callInDepartmentId,
        callInAssetManagerId: callInAssetManagerId,
        callInSavePlaceId: callInSavePlaceId,
        id: AssetAllocationID,
        reason: reason,
        createUserID: CreateUserID,
        AssetAllocationCode: AssetAllocationCode,
        produceTypeStr: produceType
    }
    return dataparam;
}

function updateAssetAllocationSubmit() {
    $('#saveBtn').attr('disabled', "true");
    var data = update();
    if (data == false) {
        $('#saveBtn').removeAttr('disabled');
        return false;
    } else {
        $.ajax({
            url: 'assetallocation/update_assetalloction',
            type: 'POST',
            data: data,
            success: function (data) {
                $('#saveBtn').removeAttr('disabled');
                $.messager.alert('提示', '更新成功！', 'info', function () {
                    window.location.reload();
                });
            },
            error: function (data) {
                $('#saveBtn').removeAttr('disabled');
                AjaxErrorHandler(data);
            }
        });
    }
}

function SaveAndSendAssetAllocationSubmit() {
    $.messager.confirm('确定', '确定要保存并发送吗？', function (r) {
        if (r) {
            $('#saveandsendBtn').attr('disabled', "true");
            var data = update();
            if (data == false) {
                $('#saveandsendBtn').removeAttr('disabled');
                return false;
            } else {
                $.ajax({
                    url: 'assetallocation/update_assetalloction',
                    type: 'POST',
                    data: data,
                    success: function (data) {
                        updateAssetAllocationStatus(data.id);
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

function updateAssetAllocationStatus(id) {
    $.ajax({
        url: 'assetallocation/update_assetallocatiomstatus',
        type: 'POST',
        data: {
            id: id,
            receiptStatusStr: 2
        },
        success: function (data) {
            $('#saveandsendBtn').removeAttr('disabled');
            $.messager.alert('提示', '发起审批成功！', 'info', function () {
                window.location.href = 'assetallocation_query';
            });
        },
        error: function (data) {
            $('#saveandsendBtn').removeAttr('disabled');
            AjaxErrorHandler(data);
        }
    });
}

$(function () {
    // fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_STATUS', 'assetStatus');

    fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_PRODUCE_TYPE', 'produceType');
    $('#produceType').combobox({
        disabled: true,
        value: $('#type').val()
    });

    $('#callOutDepartmentId').combotree({
        disabled: true
    });

    var managerId = $('#CreateUserID').val();
    var AssetAllocationID = $('#AssetAllocationID').val();

    $('#useDeptId').combotree({
        url: 'base/get_dept_tree',
        method: 'POST',
        onSelect: function (record) {
            $.ajax({
                url: 'base/dept_user_combobox',
                type: 'POST',
                data: {deptId: record.id},
                success: function (data) {
                    $('#userId').combobox('clear');
                    $('#userId').combobox('loadData', data);
                },
                error: function (data) {
                    AjaxErrorHandler(data);
                }
            });
        }
    });

    $('#assetAllocationTempDataGridTable').datagrid({
        url: 'assetallocation/assetallocationtemp_query',
        // fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        // pagination: true,
        // pageSize: 10,
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {
            assetAllocationId: AssetAllocationID,
        },
        columns: [[
            {field: 'id', hidden: true},
            {field: 'assetId', checkbox: true},
            {field: 'assetCode', title: '资产编码'},
            {field: 'produceStr', title: '物资类型'},
            {field: 'assetTypeStr', title: '资产类别'},
            {field: 'specAndModels', title: '规格型号'},
            {field: 'seriesNum', title: '序列号'},
            {field: 'unitOfMeasStr', title: '计量单位'},
            {field: 'assetBrand', title: '品牌'},
            {field: 'purcPrice', title: '采购价'},
            {field: 'equiOrigValue', title: '资产原值'},
            {field: 'techPara', title: '技术参数'},
            {field: 'remark', title: '备注'},
            // 延申信息
            {field: 'companyStr', title: '所属公司'},
            {field: 'belongLineStr', title: '所属线路/建筑'},
            {field: 'buyDate', title: '购置日期'},
            {field: 'manageDeptStr', title: '主管部门'},
            {field: 'managerStr', title: '资产管理员'},
            // 安装未写
            {field: 'assetSource', title: '资产来源'},
            {field: 'contractNum', title: '合同编号'},
            {field: 'tendersNum', title: '标段编号'},
            {field: 'mainPeriod', title: '维保期'},
            {field: 'sourceUser', title: '联系人'},
            {field: 'sourceContactInfo', title: '联系方式'},
            {field: 'prodTime', title: '出厂日期'},
            {field: "userId", title: "使用人", hidden: true},
            {field: "assetHistoryId", title: "变更记录ID", hidden: true}
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });


    $('#dataGridTable').datagrid({
        url: 'asset/asset_datagrid',
        // fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        pagination: true,
        pageSize: 10,
        toolbar: '#dataGridTableButtons',
        columns: [[
            {field: 'id', checkbox: true},
            {field: 'assetCode', title: '资产编码'},
            {field: 'combinationAssetName', title: '资产名称'},
            {field: 'produceStr', title: '物资类型'},
            {field: 'assetTypeStr', title: '资产类型'},
            {field: 'specAndModels', title: '规格型号'},
            {field: 'assetBrand', title: '品牌'},
            {field: 'unitOfMeasStr', title: '计量单位'},
            // {field: '',title: '安装位置'},
            {field: 'useDeptStr', title: '使用部门'},
            {field: 'useStr', title: '使用人'},
            {field: 'manageDeptStr', title: '主管部门'},
            {field: 'managerStr', title: '资产管理员'},
            {field: 'belongLineStr', title: '所属线路'},
            {field: 'assetStatus', title: '资产状态'}
            ,{field: 'beforeChangeAssetStatus', title: '先前状态'}
        ]],
        rowStyler: function (index, row) {
            if (row.assetStatusStr == other) {
                return 'background-color:#cccccc;';
            }
        },

        onClickRow: function (rowIndex, rowData) {
            if (rowData.assetStatusStr == other) {
                $(this).datagrid('unselectRow', rowIndex);
                $.messager.alert('提示', '该资产在其他审批流程中！', 'info');
            }
        },

        onCheck: function (rowIndex, rowData) {
            if (rowData.assetStatusStr == other) {
                $(this).datagrid('unselectRow', rowIndex);
            }
        },

        loadFilter: function (data) {
            var needList = stateList.split(",");

            //过滤数据
            var value = {
                total: data.total,
                rows: []
            };
            var x = 0;
            for (var i = 0; i < data.rows.length; i++) {
                //非冻结状态 不显示先前状态
                if (data.rows[i].assetStatusStr != other) {
                    data.rows[i].beforeChangeAssetStatus = "";
                    value.rows[x++] = data.rows[i];
                    continue;
                }
                //资产状态为冻结（8）时显示之前状态为查询状态的数据；
                if (data.rows[i].assetStatusStr == other && needList.indexOf(data.rows[i].beforeChangeAssetStatusStr) != -1) {
                    value.rows[x++] = data.rows[i];
                }
            }
            return value;
        }
    });

    $('#instSiteCodeDialog').dialog('close');
})

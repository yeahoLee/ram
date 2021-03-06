var isDialogInit = false;

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
    window.location.href = "assetreceiveuse_query";
}

function defaultSearch() {
    $('#searchQuerys input[name="assetCode"]').val('');
    $('#searchQuerys input[name="assetChsName"]').val('');
    $('#useDeptId').combotree("setValue", '');
    $('#userId').combobox("setValue", '');
    $('#dataGridTable').datagrid('load', {
        produceTypeStr: $('#produceType').combobox("getValue"),
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
        managerId: $('#UserID').val(),
        useDeptId: $('#useDeptId').combobox("getValue"),
        userId: $('#userId').combobox("getValue")
    })
}

function addAssetToList() {
    var data = $('#dataGridTable').datagrid('getSelections');
    var assetIdList = "";
    var AssetReceiveUseID = $('#AssetReceiveUseID').val();
    if (data.length == 0) {
        $.messager.alert('错误', '选择一个添加项！', 'error');
        return;
    }


    var getData = $('#assetReceiveUseTempDataGridTable').datagrid('getData');

    for (var index in data) {
        if (contains(getData.rows, data[index].id) == false)
            assetIdList = (assetIdList + "," + data[index].id);
    }

    if (assetIdList.substr(0, 1) == ',')
        assetIdList = assetIdList.substr(1);
    $.ajax({
        url: 'assetreceiveuse/create_assetreceiveusetemp',
        type: 'POST',
        data: {
            assetIdList: assetIdList,
            id: AssetReceiveUseID
        },
        success: function (data) {
            $('#assetReceiveUseTempDataGridTable').datagrid('load', {assetReceiveUseID: AssetReceiveUseID});
            $.messager.alert('提示', '添加成功！', 'info', function () {
                var v = $('#produceType').combobox("getValue");
                $('#produceType').combobox({
                    disabled: true,
                    value: v
                });
                $('#assetListDialog').dialog('close');
            });
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function removeAssetToList() {
    var data = $('#assetReceiveUseTempDataGridTable').datagrid('getSelections');
    var AssetReceiveUseID = $('#AssetReceiveUseID').val();
    var assetReceiveUseTempIdList = "";
    if (data.length == 0) {
        $.messager.alert('错误', '选择一个移除项！', 'error');
        return;
    }
    // 删除选中行
    for (var i = data.length - 1; i >= 0; i--) {
        assetReceiveUseTempIdList = (assetReceiveUseTempIdList + "," + data[i].id);
        if (assetReceiveUseTempIdList.substr(0, 1) == ',')
            assetReceiveUseTempIdList = assetReceiveUseTempIdList.substr(1);
    }
    $.ajax({
        url: 'assetreceiveuse/delete_assetreceiveusetemp',
        type: 'POST',
        data: {
            assetReceiveUseTempIdList: assetReceiveUseTempIdList,
            AssetReceiveUseID: AssetReceiveUseID
        },
        success: function (data) {
            $('#assetReceiveUseTempDataGridTable').datagrid('load', {assetReceiveUseID: AssetReceiveUseID});
            $.messager.alert('提示', '删除成功！', 'info', function () {
                var data1 = $('#assetReceiveUseTempDataGridTable').datagrid('getData');
                if (data1.rows == 0) {
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

function openAssetListDialog() {
    var assetReceiveUseUserID = $('#assetReceiveUseUserID').combobox('getValue');
    if (assetReceiveUseUserID == null || assetReceiveUseUserID == "") {
        $.messager.alert('错误', '请先选择领用人！', 'error');
        return;
    }
    var produceType = $('#produceType').combobox("getValue");
    if (produceType == null || produceType == "") {
        $.messager.alert('错误', '请先选择物资的类型！', 'error');
        return;
    }
    defaultSearch();

    $('#assetListDialog').dialog('center').dialog('open');
}

function openDialogForSavePlace() {
    var data = $('#assetReceiveUseTempDataGridTable').datagrid('getSelections');
    if (data.length == 0) {
        $.messager.alert('错误', '选择一个资产项！', 'error');
        return;
    }

    if (!isDialogInit) {
        $('#instSiteCodeDialogFrame').attr('src', 'asset_save_place');
        isDialogInit = true;
    }
    $('#instSiteCodeDialog').dialog('center').dialog('open');
}

function showCodeAndName(id, codeAndName) {
    var assetReceiveUseTempList = $('#assetReceiveUseTempDataGridTable').datagrid('getSelections');
    var AssetReceiveUseID = $('#AssetReceiveUseID').val();
    var assetReceiveUseTempIdList = "";
    for (var index in assetReceiveUseTempList) {
        assetReceiveUseTempIdList = (assetReceiveUseTempIdList + "," + assetReceiveUseTempList[index].id);
        if (assetReceiveUseTempIdList.substr(0, 1) == ',')
            assetReceiveUseTempIdList = assetReceiveUseTempIdList.substr(1);
    }
    $.ajax({
        url: 'assetreceiveuse/update_assetreceiveusetemp',
        type: 'POST',
        data: {
            assetReceiveUseTempIdList: assetReceiveUseTempIdList,
            savePlaceId: id
        },
        success: function (data) {
            $.messager.alert('提示', '更新成功！', 'info', function () {
                window.location.reload();
            });
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function closeDialog() {
    $('#instSiteCodeDialog').dialog('close');
}

function update() {
    var assetReceiveUseDepartmentID = $('#assetReceiveUseDepartmentID').combotree("getValue");
    var assetReceiveUseUserID = $('#assetReceiveUseUserID').combobox("getValue");
    // var receiveTime = $('#receiveTime').datebox("getValue");
    var reason = $('#reason').val();

    var AssetReceiveUseID = $('#AssetReceiveUseID').val();
    var AssetReceiveUseCode = $('#AssetReceiveUseCode').val();
    var CreateUserID = $('#CreateUserID').val();
    var newUserID = $('#assetReceiveUseUserID').combobox("getValue");

    var produceType = $('#produceType').combobox("getValue");
    if (produceType == null || produceType == "") {
        $.messager.alert('错误', '请先选择物资的类型！', 'error');
        return;
    }

    if (assetReceiveUseDepartmentID == null || assetReceiveUseDepartmentID == "") {
        $.messager.alert('错误', '请选择领用部门！', 'error');
        return false;
    }
    if (assetReceiveUseUserID == null || assetReceiveUseUserID == "") {
        $.messager.alert('错误', '请选择领用人！', 'error');
        return false;
    }
    // if (receiveTime == null || receiveTime == "") {
    // 	$.messager.alert('错误', '请选择归还日期！', 'error');
    // 	return false;
    // }
    if (reason == null || reason == "") {
        $.messager.alert('错误', '请填写领用事由！', 'error');
        return false;
    }
    var dataparam = {
        assetReceiveUseDepartmentID: assetReceiveUseDepartmentID,
        assetReceiveUseUserID: assetReceiveUseUserID,
        // receiveTime : receiveTime,
        reason: reason,
        id: AssetReceiveUseID,
        assetReceiveUseCode: AssetReceiveUseCode,
        createUserID: CreateUserID,
        newUserID: newUserID,
        produceTypeStr: produceType
    }

    return dataparam;
}

function updateAssetReceiveUserSubmit() {
    $('#saveBtn').attr('disabled', "true");
    var dataparam = update();
    if (dataparam == false) {
        $('#saveBtn').removeAttr('disabled');
        return false;
    } else {
        $.ajax({
            url: 'assetreceiveuse/update_assetreceiveuse',
            type: 'POST',
            data: dataparam,
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

function SaveAndSendAssetReceiveUseSubmit() {
    $.messager.confirm('确定', '确定要保存并发送吗？', function (r) {
        if (r) {
            $('#saveandsendBtn').attr('disabled', "true");
            var dataparam = update();
            if (dataparam == false) {
                $('#saveandsendBtn').removeAttr('disabled');
                return false;
            } else {
                $.ajax({
                    url: 'assetreceiveuse/update_assetreceiveuse',
                    type: 'POST',
                    data: dataparam,
                    success: function (data) {
                        updateAssetReceiveUseStatus(data.id);
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

function updateAssetReceiveUseStatus(id) {
    $.ajax({
        url: 'assetreceiveuse/update_assetreceiveusestatus',
        type: 'POST',
        data: {
            id: id,
            receiptIndex: 2
        },
        success: function (data) {
            $.messager.alert('提示', '发起审批成功！', 'info', function () {
                window.location.href = 'assetreceiveuse_query';
            });
        },
        error: function (data) {
            $('#saveandsendBtn').removeAttr('disabled');
            AjaxErrorHandler(data);
        }
    });
}

$(function () {
    fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_PRODUCE_TYPE', 'produceType');
    $('#produceType').combobox({
        disabled: true,
        value: $('#type').val()
    });

    fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_STATUS', 'assetStatus');
    var managerId = $('#CreateUserID').val();
    var AssetReceiveUseID = $('#AssetReceiveUseID').val();
    var assetReceiveUseDepartment = $('#assetReceiveUseDepartment').val();
    $('#assetReceiveUseDepartmentID').combotree({
        url: 'base/get_dept_tree',
        method: 'POST',
        value: assetReceiveUseDepartment,
        onSelect: function (record) {
            $.ajax({
                url: 'base/dept_user_combobox',
                type: 'POST',
                data: {deptId: record.id},
                success: function (data) {
                    $('#assetReceiveUseUserID').combobox('clear');
                    $('#assetReceiveUseUserID').combobox('loadData', data);
                },
                error: function (data) {
                    AjaxErrorHandler(data);
                }
            });
        }
    });

    $.ajax({
        url: 'base/dept_user_combobox',
        type: 'POST',
        data: {deptId: $('#assetReceiveUseDepartment').val()},
        success: function (data) {
            $('#assetReceiveUseUserID').combobox('loadData', data);
            $('#assetReceiveUseUserID').combobox('setValue', $('#assetReceiveUseUser').val());
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });

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

    $('#assetReceiveUseTempDataGridTable').datagrid({
        url: 'assetreceiveuse/assetreceiveusetemp_query',
        // fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        // pagination: true,
        // pageSize: 10,
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {
            assetReceiveUseID: AssetReceiveUseID
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
            {field: "savePlaceStr", title: "使用位置"},
            {field: "savePlaceId", hidden: true},
            {field: "userId", title: "使用人", hidden: true}
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
        loadMsg: '程序处理中，请稍等...',
        queryParams: {
            managerId: managerId,
            assetStatus: defaulState,
            showType: "1"
        },
        columns: [[
            {field: 'id', checkbox: true},
            {field: 'assetCode', title: '资产编码'},
            {field: 'produceStr', title: '物资类型'},
            {field: 'combinationAssetName', title: '资产名称'},
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
            {field: 'assetStatus', title: '资产状态'},
            {field: "savePlaceStr", title: "使用位置"}
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

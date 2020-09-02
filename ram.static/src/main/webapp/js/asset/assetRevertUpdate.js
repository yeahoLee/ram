var isDialogInit = false;

var other='8';
var cut=','
var stateList = "1";
var defaulState=stateList +cut+ other;

function openAdvaSearchDialog() {
    var advaSearch = $("#advaSearch").css("display");
    if (advaSearch == 'none')
        $('#advaSearch').show();
    else
        $('#advaSearch').hide();
}

function backToLastPage() {
    window.location.href = "assetrevert_query";
}

function defaultSearch() {
    $('#searchQuerys input[name="assetCode"]').val('');
    $('#searchQuerys input[name="assetChsName"]').val('');
    $('#useDeptId').combotree("setValue", '');
    $('#userId').combobox("setValue", '');
    var assetRevertUserID = $('#assetrevertUserID').combobox('getValue');
    var managerId = $('#UserID').val();
    $('#dataGridTable').datagrid('load', {
        produceTypeStr: $('#produceType').combobox("getValue"),
        managerId: managerId,
        assetStatus: defaulState,
        showType: "1",
        revertUserId: assetRevertUserID
    })
}

function searchByQuerys() {
    var assetRevertUserID = $('#assetrevertUserID').combobox('getValue');
    $('#dataGridTable').datagrid('load', {
        produceTypeStr: $('#produceType').combobox("getValue"),
        assetCode: $('#searchQuerys input[name="assetCode"]').val(),
        combinationAssetName: $('#searchQuerys input[name="assetChsName"]').val(),
        assetStatus: defaulState,
        showType: "1",
        managerId: $('#CreateUserID').val(),
        useDeptId: $('#useDeptId').combotree("getValue"),
        userId: $('#userId').combobox("getValue"),
        revertUserId: assetRevertUserID
    })
}

function addAssetToList() {
    var data = $('#dataGridTable').datagrid('getSelections');
    var assetBorrowTempIdListStr = "";
    var AssetRevertID = $('#AssetRevertID').val();
    if (data.length == 0) {
        $.messager.alert('错误', '选择一个添加项！', 'error');
        return;
    }

    $('#assetRevertUserID').combobox({
        disabled: true,
        value: $('#assetRevertUserID').combobox("getValue")
    });

    $('#produceType').combobox({
        disabled: true,
        value: $('#produceType').combobox("getValue")
    });

    $('#assetrevertDepartmentID').combotree({
        disabled: false,
        value: $('#assetrevertDepartmentID').combobox("getValue")
    });

    var getData = $('#assetRevertTempDataGridTable').datagrid('getData');

    for (var index in data) {
        if (contains(getData.rows, data[index].id) == false)
            assetBorrowTempIdListStr = (assetBorrowTempIdListStr + "," + data[index].id);
    }

    if (assetBorrowTempIdListStr.substr(0, 1) == ',')
        assetBorrowTempIdListStr = assetBorrowTempIdListStr.substr(1);
    $.ajax({
        url: 'assetrevert/create_assetreverttemp',
        type: 'POST',
        data: {
            assetBorrowTempIdListStr: assetBorrowTempIdListStr,
            id: AssetRevertID
        },
        success: function (data) {
            $.messager.alert('提示', '添加成功！', 'info', function () {
                $('#assetRevertTempDataGridTable').datagrid('load', {assetRevertID: AssetRevertID});
                $('#assetListDialog').dialog('close');
            });
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function removeAssetToList() {
    var data = $('#assetRevertTempDataGridTable').datagrid('getSelections');
    var AssetRevertID = $('#AssetRevertID').val();
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
        url: 'assetrevert/delete_assetreverttemp',
        type: 'POST',
        data: {
            assetIdList: assetIdList,
        },
        success: function (data) {
            $('#assetRevertTempDataGridTable').datagrid('load', {assetRevertID: AssetRevertID});
            $.messager.alert('提示', '删除成功！', 'info', function () {
                var data1 = $('#assetRevertTempDataGridTable').datagrid('getData');
                if (data1.total == 0) {
                    $('#assetRevertUserID').combobox({
                        disabled: false
                    });
                    $('#produceType').combobox({
                        disabled: false
                    });
                    $('#assetrevertDepartmentID').combotree({
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
    var assetRevertUserID = $('#assetrevertUserID').combobox('getValue');
    if (assetRevertUserID == null || assetRevertUserID == "") {
        $.messager.alert('错误', '请先选择归还人！', 'error');
        return;
    }

    var produceType = $('#produceType').combobox("getValue");
    if (produceType == null || produceType == "") {
        $.messager.alert('错误', '请先选择物资的类型！', 'error');
        return;
    } else {
        defaultSearch();
        $('#assetListDialog').dialog('center').dialog('open');
    }
}

function openDialogForSavePlace() {
    var data = $('#assetRevertTempDataGridTable').datagrid('getSelections');
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
    var assetList = $('#assetRevertTempDataGridTable').datagrid('getSelections');
    var AssetRevertID = $('#AssetRevertID').val();
    var assetIdList = "";
    for (var index in assetList) {
        assetIdList = (assetIdList + "," + assetList[index].id);
        if (assetIdList.substr(0, 1) == ',')
            assetIdList = assetIdList.substr(1);
    }
    $.ajax({
        url: 'assetrevert/update_assetreverttemp',
        type: 'POST',
        data: {
            assetIdList: assetIdList,
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
    var assetrevertDepartmentID = $('#assetrevertDepartmentID').combotree("getValue");
    var assetrevertUserID = $('#assetrevertUserID').combobox("getValue");
    var realrevertTime = $('#realrevertTime').datebox("getValue");
    var remarks = $('#remarks').val();

    var AssetRevertID = $('#AssetRevertID').val();
    var AssetrevertCode = $('#AssetrevertCode').val();
    var CreateUserID = $('#CreateUserID').val();
    var newUserID = $('#assetrevertUserID').combobox("getValue");

    var produceType = $('#produceType').combobox("getValue");
    if (produceType == null || produceType == "") {
        $.messager.alert('错误', '请先选择物资的类型！', 'error');
        return;
    }
    if (assetrevertDepartmentID == null || assetrevertDepartmentID == "") {
        $.messager.alert('错误', '请选择借用部门！', 'error');
        return false;
    }
    if (assetrevertUserID == null || assetrevertUserID == "") {
        $.messager.alert('错误', '请选择借用人！', 'error');
        return false;
    }
    if (realrevertTime == null || realrevertTime == "") {
        $.messager.alert('错误', '请选择归还日期！', 'error');
        return false;
    }

    var data = {
        assetrevertDepartmentID: assetrevertDepartmentID,
        assetrevertUserID: assetrevertUserID,
        realrevertTime: realrevertTime,
        remarks: remarks,
        id: AssetRevertID,
        assetrevertCode: AssetrevertCode,
        createUserID: CreateUserID,
        newUserID: newUserID,
        produceTypeStr: produceType
    }
    return data;
}

function updateAssetRevertSubmit() {
    $('#saveBtn').attr('disabled', "true");

    var data = update();
    if (data == false) {
        $('#saveBtn').removeAttr('disabled');
        return false;
    } else {
        $.ajax({
            url: 'assetrevert/update_assetrevert',
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

function SaveAndSendAssetRevertSubmit(id) {
    $.messager.confirm('确定', '确定要保存并发送吗？', function (r) {
        if (r) {
            $('#saveandsendBtn').attr('disabled', "true");
            var data = update();
            if (data == false) {
                $('#saveandsendBtn').removeAttr('disabled');
                return false;
            } else {
                $.ajax({
                    url: 'assetrevert/update_assetrevert',
                    type: 'POST',
                    data: data,
                    success: function (data) {
                        updateAssetRevertStatus(data.id);
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

function updateAssetRevertStatus(id) {
    var data = update();
    $.ajax({
        url: 'assetrevert/update_assetrevertstatus',
        type: 'POST',
        data: {
            id: id,
            receiptIndex: 2
        },
        success: function (data) {
            $('#saveandsendBtn').removeAttr('disabled');
            $.messager.alert('提示', '发送成功！', 'info', function () {
                window.location.href = 'assetrevert_query';
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
    var AssetRevertID = $('#AssetRevertID').val();
    var assetrevertDepartment = $('#assetrevertDepartment').val();

    $('#assetrevertDepartmentID').combotree({
        url: 'base/get_dept_tree',
        method: 'POST',
        value: assetrevertDepartment,
        disable: true,
        onSelect: function (record) {
            $.ajax({
                url: 'base/dept_user_combobox',
                type: 'POST',
                data: {deptId: record.id},
                success: function (data) {
                    $('#assetrevertUserID').combobox('clear');
                    $('#assetrevertUserID').combobox('loadData', data);
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
        data: {deptId: $('#assetrevertDepartment').val()},
        success: function (data) {
            $('#assetrevertUserID').combobox('loadData', data);
            $('#assetrevertUserID').combobox('setValue', $('#assetrevertUser').val());
            // $('#assetrevertUserID').combobox({
            //     disabled: true
            // })
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });

    $('#assetrevertUserID').combobox({
        onSelect: function () {
            $('#assetRevertTempDataGridTable').datagrid('selectAll');
            var data = $('#assetRevertTempDataGridTable').datagrid('getSelections');
            if (data.length > 0) {
                removeAssetToList();
            }
        }
    })

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

    $('#assetRevertTempDataGridTable').datagrid({
        url: 'assetrevert/assetreverttemp_query',
        // fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        // pagination: true,
        // pageSize: 10,
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {
            assetRevertID: AssetRevertID
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
            {field: "revertSavePlaceStr", title: "使用位置"},
            {field: "revertSavePlaceId", hidden: true},//
            {field: "userId", title: "使用人", hidden: true}
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });

    $('#dataGridTable').datagrid({
        url: 'assetrevert/assetborrowtemp_query_norevert',
        method: 'POST',
        striped: true,
        rownumbers: true,
        pagination: true,
        pageSize: 10,
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        columns: [[
            {field: 'id', checkbox: true},
            {field: 'assetCode', title: '资产编码'},
            {field: 'produceStr', title: '物资类型'},
            {field: 'assetBorrowID', hidden: true},
            {field: 'assetborrowCode', title: '借用编码'},
            {field: 'combinationAssetName', title: '资产名称'},
            {field: 'assetTypeStr', title: '资产类型'},
            {field: 'specAndModels', title: '规格型号'},
            {field: 'assetBrand', title: '品牌'},
            {field: 'unitOfMeasStr', title: '计量单位'},
            {field: 'useDeptStr', title: '使用部门'},
            {field: 'useStr', title: '使用人'},
            {field: 'manageDeptStr', title: '主管部门'},
            {field: 'managerStr', title: '资产管理员'},
            {field: 'belongLineStr', title: '所属线路'},
            {field: 'assetStatus', title: '资产状态'},
            {field: 'createTimestamp', hidden: true}
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

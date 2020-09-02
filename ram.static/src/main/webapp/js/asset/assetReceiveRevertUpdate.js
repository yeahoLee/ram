var isDialogInit = false;

var other='8';
var cut=','
var stateList = "0";
var defaulState=stateList +cut+ other;

function openAdvaSearchDialog() {
    var advaSearch = $("#advaSearch").css("display");
    if (advaSearch == 'none')
        $('#advaSearch').show();
    else
        $('#advaSearch').hide();
}

function backToLastPage() {
    window.location.href = "assetreceiverevert_query";
}

function defaultSearch() {
    $('#searchQuerys input[name="assetCode"]').val('');
    $('#searchQuerys input[name="assetChsName"]').val('');
    $('#useDeptId').combotree("setValue", '');
    $('#userId').combobox("setValue", '');
    var assetReceiveRevertUserID = $('#assetReceiveRevertUserID').combobox('getValue');
    var managerId = $('#UserID').val();
    $('#dataGridTable').datagrid('load', {
        produceTypeStr: $('#produceType').combobox("getValue"),
        managerId: managerId,
        assetStatus: defaulState,
        showType: "1",
        revertUserId: assetReceiveRevertUserID
    })
}

function searchByQuerys() {
    $('#dataGridTable').datagrid('load', {
        produceTypeStr: $('#produceType').combobox("getValue"),
        assetCode: $('#searchQuerys input[name="assetCode"]').val(),
        combinationAssetName: $('#searchQuerys input[name="assetChsName"]').val(),
        assetStatus: defaulState,
        showType: "1",
        managerId: $('#UserID').val(),
        useDeptId: $('#useDeptId').combotree("getValue"),
        userId: $('#userId').combobox("getValue"),
        revertUserId: $('#assetReceiveRevertUserID').combobox('getValue')
    })
}

function addAssetToList() {
    var data = $('#dataGridTable').datagrid('getSelections');
    var AssetReceiveUseTempIdListStr = "";
    var AssetReceiveRevertID = $('#AssetReceiveRevertID').val();
    if (data.length == 0) {
        $.messager.alert('错误', '选择一个添加项！', 'error');
        return;
    }

    var v = $('#produceType').combobox("getValue");
    $('#produceType').combobox({
        disabled: true,
        value: v
    });
    var v2 = $('#assetReceiveRevertUserID').combobox("getValue");
    $('#assetReceiveRevertUserID').combobox({
        disabled: true,
        value: v2
    });

    var getData = $('#assetReceiveRevertTempDataGridTable').datagrid('getData');

    for (var index in data) {
        if (contains(getData.rows, data[index].id) == false)
            AssetReceiveUseTempIdListStr = (AssetReceiveUseTempIdListStr + "," + data[index].id);
    }

    if (AssetReceiveUseTempIdListStr.substr(0, 1) == ',')
        AssetReceiveUseTempIdListStr = AssetReceiveUseTempIdListStr.substr(1);
    $.ajax({
        url: 'assetreceiverevert/create_assetreceivereverttemp',
        type: 'POST',
        data: {
            AssetReceiveUseTempIdListStr: AssetReceiveUseTempIdListStr,
            id: AssetReceiveRevertID
        },
        success: function (data) {
            $('#assetReceiveRevertTempDataGridTable').datagrid('load', {assetReceiveRevertID: AssetReceiveRevertID});
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
    var data = $('#assetReceiveRevertTempDataGridTable').datagrid('getSelections');
    var AssetReceiveRevertID = $('#AssetReceiveRevertID').val();
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
        url: 'assetreceiverevert/delete_assetreceivereverttemp',
        type: 'POST',
        data: {
            assetIdList: assetIdList
        },
        success: function (data) {
            $('#assetReceiveRevertTempDataGridTable').datagrid('load', {assetReceiveRevertID: AssetReceiveRevertID});
            $.messager.alert('提示', '删除成功！', 'info', function () {
                var data1 = $('#assetReceiveRevertTempDataGridTable').datagrid('getData');
                if (data1.rows == 0) {
                    $('#produceType').combobox({
                        disabled: false
                    });
                    $('#assetReceiveRevertUserID').combobox({
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
    var assetReceiveRevertUserID = $('#assetReceiveRevertUserID').combobox('getValue');
    if (assetReceiveRevertUserID == null || assetReceiveRevertUserID == "") {
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
    var data = $('#assetReceiveRevertTempDataGridTable').datagrid('getSelections');
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
    var assetList = $('#assetReceiveRevertTempDataGridTable').datagrid('getSelections');
    var AssetReceiveRevertID = $('#AssetReceiveRevertID').val();
    var assetIdList = "";
    for (var index in assetList) {
        assetIdList = (assetIdList + "," + assetList[index].id);
        if (assetIdList.substr(0, 1) == ',')
            assetIdList = assetIdList.substr(1);
    }
    $.ajax({
        url: 'assetreceiverevert/update_assetreceivereverttemp',
        type: 'POST',
        data: {
            assetIdList: assetIdList,
            savePlaceId: id
        },
        success: function (data) {
            $.messager.alert('提示', '更新成功！', 'info', function () {
                window.location.href = "assetreceiverevert_query";
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
    var assetReceiveRevertDepartmentID = $('#assetReceiveRevertDepartmentID').combotree("getValue");
    var assetReceiveRevertUserID = $('#assetReceiveRevertUserID').combobox("getValue");
    var realrevertTime = $('#realrevertTime').datebox("getValue");
    var remarks = $('#remarks').val();

    var AssetReceiveRevertID = $('#AssetReceiveRevertID').val();
    var AssetReceiveRevertCode = $('#AssetReceiveRevertCode').val();
    var CreateUserID = $('#CreateUserID').val();
    var newUserID = $('#assetReceiveRevertUserID').combobox("getValue");

    if (assetReceiveRevertDepartmentID == null || assetReceiveRevertDepartmentID == "") {
        $.messager.alert('错误', '请选择归还部门！', 'error');
        return false;
    }
    if (assetReceiveRevertUserID == null || assetReceiveRevertUserID == "") {
        $.messager.alert('错误', '请选择归还人！', 'error');
        return false;
    }
    if (realrevertTime == null || realrevertTime == "") {
        $.messager.alert('错误', '请选择归还日期！', 'error');
        return false;
    }

    var dataparam = {
        assetReceiveRevertDepartmentID: assetReceiveRevertDepartmentID,
        assetReceiveRevertUserID: assetReceiveRevertUserID,
        realrevertTime: realrevertTime,
        remarks: remarks,
        id: AssetReceiveRevertID,
        assetReceiveRevertCode: AssetReceiveRevertCode,
        createUserID: CreateUserID,
        newUserID: newUserID
    }

    return dataparam;
}

function updateAssetReceiveRevertSubmit() {
    $('#saveBtn').attr('disabled', "true");
    var AssetReceiveRevertID = $('#AssetReceiveRevertID').val();
    var dataparam = update();
    if (dataparam == false) {
        $('#saveBtn').removeAttr('disabled');
        return false;
    } else {
        $.ajax({
            url: 'assetreceiverevert/update_assetreceiverevert',
            type: 'POST',
            data: dataparam,
            success: function (data) {
                $('#saveBtn').removeAttr('disabled');
                $('#assetReceiveRevertTempDataGridTable').datagrid('load', {assetReceiveRevertID: AssetReceiveRevertID});
                $.messager.alert('提示', '更新成功！', 'info', function () {
                    window.location.href = "assetreceiverevert_query";
                });
            },
            error: function (data) {
                $('#saveBtn').removeAttr('disabled');
                AjaxErrorHandler(data);
            }
        });
    }
}

function SaveAndSendAssetReceiveRevertSubmit() {
    $.messager.confirm('确定', '确定要保存并发送吗？', function (r) {
        if (r) {
            $('#saveandsendBtn').attr('disabled', "true");
            var dataparam = update();
            if (dataparam == false) {
                $('#saveandsendBtn').removeAttr('disabled');
                return false;
            } else {
                $.ajax({
                    url: 'assetreceiverevert/update_assetreceiverevert',
                    type: 'POST',
                    data: dataparam,
                    success: function (data) {
                        updateAssetReceiveRevertStatus(data.id);
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

function updateAssetReceiveRevertStatus(id) {
    $.ajax({
        url: 'assetreceiverevert/update_assetreceiverevertstatus',
        type: 'POST',
        data: {
            id: id,
            receiptIndex: 2
        },
        success: function (data) {
            $.messager.alert('提示', '发起审批成功！', 'info', function () {
                window.location.href = 'assetreceiverevert_query';
            });
        },
        error: function (data) {
            $('#saveandsendBtn').removeAttr('disabled');
            AjaxErrorHandler(data);
        }
    });
}

$(function () {
    $('#instSiteCodeDialog').dialog('close');
    // fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_STATUS', 'assetStatus');

    fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_PRODUCE_TYPE', 'produceType');
    $('#produceType').combobox({
        disabled: true,
        value: $('#type').val()
    });

    $('#assetReceiveRevertUserID').combobox({
        disabled: true
    });

    var managerId = $('#CreateUserID').val();

    var AssetReceiveRevertID = $('#AssetReceiveRevertID').val();
    var assetReceiveRevertDepartment = $('#assetReceiveRevertDepartment').val();

    $('#assetReceiveRevertDepartmentID').combotree({
        url: 'base/get_dept_tree',
        method: 'POST',
        value: assetReceiveRevertDepartment,
        onSelect: function (record) {
            $.ajax({
                url: 'base/dept_user_combobox',
                type: 'POST',
                data: {deptId: record.value},
                success: function (data) {
                    $('#assetReceiveRevertUserID').combobox('clear');
                    $('#assetReceiveRevertUserID').combobox('loadData', data);
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
        data: {deptId: $('#assetReceiveRevertDepartment').val()},
        success: function (data) {
            $('#assetReceiveRevertUserID').combobox('loadData', data);
            $('#assetReceiveRevertUserID').combobox('setValue', $('#assetReceiveRevertUser').val());
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });

    $('#assetrevertUserID').combobox({
        onSelect: function () {
            $('#assetReceiveRevertTempDataGridTable').datagrid('selectAll');
            var data = $('#assetReceiveRevertTempDataGridTable').datagrid('getSelections');
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

    $('#assetReceiveRevertTempDataGridTable').datagrid({
        url: 'assetreceiverevert/assetreceivereverttemp_query',
        method: 'POST',
        striped: true,
        rownumbers: true,
        // pagination: true,
        // pageSize: 10,
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {
            assetReceiveRevertID: AssetReceiveRevertID,
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
            {field: "userId", title: "使用人", hidden: true}//
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });


    $('#dataGridTable').datagrid({
        url: 'assetreceiverevert/assetreceivereverttemp_query_norevert',
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
            {field: 'assetReceiveUseID', hidden: true},
            {field: 'assetReceiveUseCode', title: '领用编码'},
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
})

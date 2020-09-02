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
    window.location.href = "assetborrow_query";
}

function openSearch() {
    var produceType = $('#produceType').combobox("getValue");
    if (produceType == null || produceType == "") {
        $.messager.alert('错误', '请先选择物资的类型！', 'error');
        return;
    } else {
        defaultSearch();
        $('#assetListDialog').dialog('center').dialog('open');
    }
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
        //manageDeptId : $('#manageDeptId').datebox("getValue"),
        managerId: $('#UserID').val(),
        useDeptId: $('#useDeptId').combotree("getValue"),
        userId: $('#userId').combobox("getValue")
    })
}

function addAssetToList() {
    var data = $('#dataGridTable').datagrid('getSelections');
    var assetIdList = "";
    var AssetBorrowID = $('#AssetBorrowID').val();
    if (data.length == 0) {
        $.messager.alert('错误', '选择一个添加项！', 'error');
        return;
    }

    var v = $('#produceType').combobox("getValue");
    $('#produceType').combobox({
        disabled: true,
        value: v
    });

    var getData = $('#assetBorrowTempDataGridTable').datagrid('getData');

    for (var index in data) {
        if (contains(getData.rows, data[index].id) == false)
            assetIdList = (assetIdList + "," + data[index].id);
    }

    if (assetIdList.substr(0, 1) == ',')
        assetIdList = assetIdList.substr(1);
    $.ajax({
        url: 'assetborrow/create_assetborrowtemp',
        type: 'POST',
        data: {
            assetIdList: assetIdList,
            id: AssetBorrowID
        },
        success: function (data) {
            $.messager.alert('提示', '添加成功！', 'info', function () {
                $('#assetBorrowTempDataGridTable').datagrid('load', {assetBorrowID: AssetBorrowID});
                $('#assetListDialog').dialog('close');
            });
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function removeAssetToList() {
    var data = $('#assetBorrowTempDataGridTable').datagrid('getSelections');
    var AssetBorrowID = $('#AssetBorrowID').val();
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
        url: 'assetborrow/delete_assetborrowtemp',
        type: 'POST',
        data: {
            assetIdList: assetIdList,
            AssetBorrowID: AssetBorrowID
        },
        success: function (data) {
            $('#assetBorrowTempDataGridTable').datagrid('load', {assetBorrowID: AssetBorrowID});
            $.messager.alert('提示', '删除成功！', 'info', function () {
                var data1 = $('#assetBorrowTempDataGridTable').datagrid('getData');
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

function openDialogForSavePlace() {
    var data = $('#assetBorrowTempDataGridTable').datagrid('getSelections');
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
    var assetList = $('#assetBorrowTempDataGridTable').datagrid('getSelections');
    var AssetBorrowID = $('#AssetBorrowID').val();
    var assetIdList = "";
    for (var index in assetList) {
        assetIdList = (assetIdList + "," + assetList[index].id);
        if (assetIdList.substr(0, 1) == ',')
            assetIdList = assetIdList.substr(1);
    }
    $.ajax({
        url: 'assetborrow/update_assetborrowtemp',
        type: 'POST',
        data: {
            assetIdList: assetIdList,
            savePlaceId: id
        },
        success: function (data) {
            $.messager.alert('提示', '更新成功！', 'info', function () {
                $('#assetBorrowTempDataGridTable').datagrid('load', {assetBorrowID: AssetBorrowID});
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
    var assetborrowDepartmentID = $('#assetborrowDepartmentID').combotree("getValue");
    var assetborrowUserID = $('#assetborrowUserID').combobox("getValue");
    var revertTime = $('#revertTime').datebox("getValue");
    var reason = $('#reason').val();

    var AssetBorrowID = $('#AssetBorrowID').val();
    var AssetborrowCode = $('#AssetborrowCode').val();
    var CreateUserID = $('#CreateUserID').val();
    var AssetborrowUserID = $('#AssetborrowUserID').val();
    var newUserID = $('#assetborrowUserID').combobox("getValue");
	var produceType = $('#produceType').combobox("getValue");
	if (produceType == null || produceType == "") {
		$.messager.alert('错误', '请先选择物资的类型！', 'error');
		return;
	}

    if (assetborrowDepartmentID == null || assetborrowDepartmentID == "") {
        $.messager.alert('错误', '请选择借用部门！', 'error');
        return false;
    }
    if (assetborrowUserID == null || assetborrowUserID == "") {
        $.messager.alert('错误', '请选择借用人！', 'error');
        return false;
    }
    if (revertTime == null || revertTime == "") {
        $.messager.alert('错误', '请选择归还日期！', 'error');
        return false;
    }
    if (reason == null || reason == "") {
        $.messager.alert('错误', '请填写借用事由！', 'error');
        return false;
    }

    var data = {
        assetborrowDepartmentID: assetborrowDepartmentID,
        assetborrowUserID: assetborrowUserID,
        revertTime: revertTime,
        reason: reason,
        id: AssetBorrowID,
        AssetborrowCode: AssetborrowCode,
        createUserID: CreateUserID,
        newUserID: newUserID,
	    produceTypeStr: produceType
    }

    return data;
}

function updateAssetBorrowSubmit() {
    $('#saveBtn').attr('disabled', "true");

    var data = update();
    if (data == false) {
        $('#saveBtn').removeAttr('disabled');
        return false;
    } else {
        $.ajax({
            url: 'assetborrow/update_assetborrow',
            type: 'POST',
            data: data,
            success: function (data) {
                $('#saveBtn').removeAttr('disabled');
                $.messager.alert('提示', '更新成功！', 'info', function () {
	                window.location.href = 'assetborrow_query';
                });
            },
            error: function (data) {
                $('#saveBtn').removeAttr('disabled');
                AjaxErrorHandler(data);
            }
        });
    }
}

function SaveAndSendAssetBorrowSubmit() {
    $.messager.confirm('确定', '确定要保存并发送吗？', function (r) {
        if (r) {
            $('#saveandsendBtn').attr('disabled', "true");
            var data = update();
            if (data == false) {
                $('#saveandsendBtn').removeAttr('disabled');
                return false;
            } else {
                $.ajax({
                    url: 'assetborrow/update_assetborrow',
                    type: 'POST',
                    data: data,
                    success: function (data) {
                        updateAssetBorrowStatus(data.id);
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

function updateAssetBorrowStatus(id) {
    $.ajax({
        url: 'assetborrow/update_assetborrowstatus',
        type: 'POST',
        data: {
            id: id,
            receiptIndex: 2
        },
        success: function (data) {
            $('#saveandsendBtn').removeAttr('disabled');
            $.messager.alert('提示', '发起审批成功！', 'info', function () {
                window.location.href = 'assetborrow_query';
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
    fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_PRODUCE_TYPE', 'produceType');
    $('#produceType').combobox({
        disabled: true,
        value: $('#type').val()
    });


    var managerId = $('#CreateUserID').val();
    var AssetBorrowID = $('#AssetBorrowID').val();
    var assetborrowDepartment = $('#assetborrowDepartment').val();

    $('#assetBorrowTempDataGridTable').datagrid({
        url: 'assetborrow/assetborrowtemp_query',
        method: 'POST',
        striped: true,
        rownumbers: true,
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {
            AssetBorrowID: AssetBorrowID
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
            {field: 'beforeChangeAssetStatus', title: '先前状态'}

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

    $('#assetborrowDepartmentID').combotree({
        url: 'base/get_dept_tree',
        method: 'POST',
        value: assetborrowDepartment,
        onSelect: function (record) {
            $.ajax({
                url: 'base/dept_user_combobox',
                type: 'POST',
                data: {deptId: record.id,showChild: 1},
                success: function (data) {
                    $('#assetborrowUserID').combobox('clear');
                    $('#assetborrowUserID').combobox('loadData', data);
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
        data: {deptId: $('#assetborrowDepartment').val()},
        success: function (data) {
            $('#assetborrowUserID').combobox('loadData', data);
            $('#assetborrowUserID').combobox('setValue', $('#assetborrowUser').val());
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
})

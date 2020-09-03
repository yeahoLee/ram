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
    var assetRevertUserID = $('#assetRevertUserID').combobox('getValue');
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
    var assetRevertUserID = $('#assetRevertUserID').combobox('getValue');
    $('#dataGridTable').datagrid('load', {
        produceTypeStr: $('#produceType').combobox("getValue"),
        assetCode: $('#searchQuerys input[name="assetCode"]').val(),
        combinationAssetName: $('#searchQuerys input[name="assetChsName"]').val(),
        assetStatus: defaulState,
        managerId: $('#UserID').val(),
        useDeptId: $('#useDeptId').combotree("getValue"),
        userId: $('#userId').combobox("getValue"),
        showType: "1",
        revertUserId: assetRevertUserID
    })
}

function addAssetToList() {
    var data = $('#dataGridTable').datagrid('getSelections');
    if (data.length == 0) {
        $.messager.alert('错误', '选择一个添加项！', 'error');
        return;
    }

    for (var index in data) {
        var i = $('#assetRevertTempDataGridTable').datagrid("getRowIndex", data[index]);
        if (i == -1)
            $('#assetRevertTempDataGridTable').datagrid("appendRow", data[index]);
    }
    $('#assetRevertTempDataGridTable').datagrid("autoSizeColumn");
    $('#assetListDialog').dialog('close');

    var v = $('#produceType').combobox("getValue");
    $('#produceType').combobox({
        disabled: true,
        value: v
    });
    var v2 = $('#assetRevertUserID').combobox("getValue");
    $('#assetRevertUserID').combobox({
        disabled: true,
        value: v2
    });
}

function removeAssetToList() {
    var data = $('#assetRevertTempDataGridTable').datagrid('getSelections');
    if (data.length == 0) {
        $.messager.alert('错误', '选择一个移除项！', 'error');
        return;
    }
    // 删除选中行
    for (var i = data.length - 1; i >= 0; i--) {
        var rowIndex = $('#assetRevertTempDataGridTable').datagrid('getRowIndex', data[i]);
        $('#assetRevertTempDataGridTable').datagrid('deleteRow', rowIndex);
    }

    var data1 = $('#assetRevertTempDataGridTable').datagrid('getData');
    if (data1.rows == 0) {
        $('#produceType').combobox({
            disabled: false
        });
        $('#assetRevertUserID').combobox({
            disabled: false
        });
    }
}

function openAssetListDialog() {
    var assetRevertUserID = $('#assetRevertUserID').combobox('getValue');
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
    var index = "";
    for (var i = 0; i < assetList.length; i++) {
        assetList[i].savePlaceId = id;
        assetList[i].savePlaceStr = codeAndName;
        index = $('#assetRevertTempDataGridTable').datagrid('getRowIndex', assetList[i]);
        $('#assetRevertTempDataGridTable').datagrid('updateRow', {index: index, row: assetList[i]}); // 将数据绑定到datagrid
    }
}

function closeDialog() {
    $('#instSiteCodeDialog').dialog('close');
}

function save() {
    var data = $('#assetRevertTempDataGridTable').datagrid('getData');
    var assetList = data.rows;

    var assetRevertDepartmentID = $('#assetRevertDepartmentID').combotree("getValue");
    var assetRevertUserID = $('#assetRevertUserID').combobox("getValue");
    var remark = $('#remark').val();
    var realrevertTime = $('#realrevertTime').datebox("getValue");
    var AssetBorrowTempDtoList = [];
    if (assetList.length > 0) {
        for (var i = 0; i < assetList.length; i++) {
            var AssetBorrowTempDto = {};
            AssetBorrowTempDto.revertSavePlaceId = assetList[i].savePlaceId;
            AssetBorrowTempDto.status = 0;
            AssetBorrowTempDto.assetBorrowID = assetList[i].assetBorrowID;
            AssetBorrowTempDto.id = assetList[i].id;
            AssetBorrowTempDto.createTimestamp = assetList[i].createTimestamp;
            if (assetList[i].assetId != null)
                AssetBorrowTempDto.assetId = assetList[i].assetId;
            else
                AssetBorrowTempDto.assetId = assetList[i].id;
            AssetBorrowTempDtoList.push(AssetBorrowTempDto);
        }
    }

    var produceType = $('#produceType').combobox("getValue");
    if (produceType == null || produceType == "") {
        $.messager.alert('错误', '请先选择物资的类型！', 'error');
        return;
    }
    if (assetRevertDepartmentID == null || assetRevertDepartmentID == "") {
        $.messager.alert('错误', '请选择归还部门！', 'error');
        return false;
    }
    if (assetRevertUserID == null || assetRevertUserID == "") {
        $.messager.alert('错误', '请选择归还人！', 'error');
        return false;
    }
    if (realrevertTime == null || realrevertTime == "") {
        $.messager.alert('错误', '请填写归还日期！', 'error');
        return false;
    }
    var assetBorrowTempDtoList = "";

    if (AssetBorrowTempDtoList.length > 0)
        assetBorrowTempDtoList = JSON.stringify(AssetBorrowTempDtoList);

    var dataparam = {
        assetBorrowTempDtoList: assetBorrowTempDtoList,
        assetrevertDepartmentID: assetRevertDepartmentID,
        assetrevertUserID: assetRevertUserID,
        realrevertTime: realrevertTime,
        remarks: remark,
        produceTypeStr: produceType
    }
    return dataparam;
}

function saveAssetRevertSubmit(type) {
    $('#saveBtn').attr('disabled', "true");
    var data = save();
    if (data == false) {
        $('#saveBtn').removeAttr('disabled');
        return false
    } else {
        $.ajax({
            url: 'assetrevert/create_assetrevert',
            type: 'POST',
            data: data,
            success: function (dataResult) {
                $('#saveBtn').removeAttr('disabled');
                if (type == 1) {
                    $.messager.alert('提示', '保存成功！', 'info', function () {
                        window.location.href = 'assetrevert_query';
                    });
                } else {
                    var data1 = {};
                    data1.id=dataResult.dto.id;
                    data1.formName = "借用归还申请单";
                    data1.serialNumber = dataResult.dto.assetrevertCode;
                    data1.processDefKey = ASSETS_BORROW_RETURN;
                    data1.resultLocation = "assetrevert_query";
                    data1.produceType = data.produceTypeStr;
                    data1.assetrevertDepartmentId = data.assetrevertDepartmentID;
                    data1.assetrevertUserId = data.assetrevertUserID;
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

function SaveAndSendAssetRevertSubmit(id) {
    $.messager.confirm('确定', '确定要保存并发送吗？', function (r) {
        if (r) {
            $('#saveandsendBtn').attr('disabled', "true");
            var data = save();
            if (data == false) {
                $('#saveandsendBtn').removeAttr('disabled');
                return false
            } else {
                $.ajax({
                    url: 'assetrevert/create_assetrevert',
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
    $('#saveandsendBtn').attr('disabled', "true");
    var data = save();
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
    //归还日期初始值为当前日期
    $('#realrevertTime').datebox('setValue', new Date());

    fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_PRODUCE_TYPE', 'produceType');

    $('#assetRevertDepartmentID').combotree({
        url: 'base/get_dept_tree',
        method: 'POST',
        onSelect: function (record) {
            $.ajax({
                url: 'base/dept_user_combobox',
                type: 'POST',
                data: {deptId: record.id},
                success: function (data) {
                    $('#assetRevertUserID').combobox('clear');
                    $('#assetRevertUserID').combobox('loadData', data);
                },
                error: function (data) {
                    AjaxErrorHandler(data);
                }
            });
        }
    });

    $('#assetRevertUserID').combobox({
        onSelect: function () {
            $('#assetRevertTempDataGridTable').datagrid('selectAll');
            var data = $('#assetRevertTempDataGridTable').datagrid('getSelections');
            if (data.length > 0) {
                // 删除选中行
                for (var i = data.length - 1; i >= 0; i--) {
                    var rowIndex = $('#assetRevertTempDataGridTable').datagrid('getRowIndex', data[i]);
                    $('#assetRevertTempDataGridTable').datagrid('deleteRow', rowIndex);
                }
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
        method: 'POST',
        striped: true,
        rownumbers: true,
        pagination: true,
        pageSize: 10,
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        columns: [[
            {field: 'assetId', checkbox: true},
            {field: 'assetCode', title: '资产编码'},
            {field: 'produceStr', title: '物资类型'},
            {field: 'assetborrowCode', title: '借用编码'},
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
            {field: "savePlaceStr", title: "归还位置"},
            {field: "savePlaceId", hidden: true},
            {field: "userId", title: "使用人", hidden: true},
            {field: 'assetBorrowID', hidden: true},
            {field: 'id', hidden: true},
            {field: 'createTimestamp', hidden: true}
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });

    $('#dataGridTable').datagrid({
        url: 'assetrevert/assetborrowtemp_query_norevert',
        // fit: true,
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
            // {field: 'useDeptStr', title: '使用部门'},
            {field: 'useStr', title: '使用人'},
            {field: 'manageDeptStr', title: '主管部门'},
            {field: 'managerStr', title: '资产管理员'},
            {field: 'belongLineStr', title: '所属线路'},
            {field: 'assetStatus', title: '资产状态'},
            {field: 'createTimestamp', hidden: true}
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

    $('#assetRevertTempDataGridTable').datagrid("autoSizeColumn");
    $('#instSiteCodeDialog').dialog('close');
})

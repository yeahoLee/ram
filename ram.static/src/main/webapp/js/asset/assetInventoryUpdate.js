var isDialogInit = false;
var other='8';
var cut=','
var stateList = '0,1,2,6,7,10';
var defaulState=stateList +cut+ other;

//判断对象是否为空的方法
function objIsEmpty(obj) {
    var flag = true;
    for (var key in obj) {
        if (!isEmpty(obj[key])) {
            flag = false;
            return flag;
        }
        ;
    }
    return flag;
}

function openDialogForSavePlace() {
    $('#instSiteCodeDialogFrame').attr('src', 'asset_save_place');
    $('#instSiteCodeDialog').dialog('center').dialog('open');
}

function showCodeAndName(id, codeAndName) {
    $("#showCodeAndData").html(codeAndName);
    $('#savePlaceId').val(id);
}

function showAssetTypeCodeAndName(id, codeAndName) {
    $('#showAssetTypeCodeAndData').text(codeAndName);
    $('#assetTypeDialog input[name="rowIdHidden"]').val(id);
}

function closeAssetTypeDialog() {
    $('#assetTypeDialog').dialog('close');
}

function closeAssetListDialog() {
    $('#searchQuerys input[name="assetCode"]').val(''),
        $('#searchQuerys input[name="assetChsName"]').val(''),
        $('#useDeptId').combobox('clear'),
        $('#userId').combobox("clear"),
        $('#dataGridTable').datagrid('load', {});
    $('#assetListDialog').dialog('close');
}

function closeAssetScopeDialog() {
    $('#assetStatus').combobox('clear');
    //$('#assetType').combobox('clear');
    $('#showAssetTypeCodeAndData').text('');
    $('#assetTypeDialog input[name="rowIdHidden"]').val('');
    $('#manageDeptId').combobox('clear');
    $('#useDeptIdScope').combobox('clear');
    $('#managerId').combobox('clear');
    $('#userIdScope').combobox('clear');
    $('#savePlaceId').val('');
    $("#showCodeAndData").html('');
    $('#assetScopeDialog').dialog('close');
}

function deleteAssetInventoryScopeSubmit(assetInventoryScopeId) {
    $.messager.confirm('确定', '确定要删除吗？', function (r) {
        if (r) {
            $.ajax({
                url: 'assetInventory/delete_assetinventoryscope',
                type: 'POST',
                data: {assetInventoryScopeId: assetInventoryScopeId},
                success: function (data) {
                    $.messager.alert('删除', '删除成功！', 'info', function () {
                        $('#assetInventoryScope').datagrid('reload');
                    });
                },
                error: function (data) {
                    AjaxErrorHandler(data);
                }
            });
        }
    })
}

function openAdvaSearchDialog() {
    var advaSearch = $("#advaSearch").css("display");
    if (advaSearch == 'none')
        $('#advaSearch').show();
    else
        $('#advaSearch').hide();
}

function openSearch() {
    var produceType = $('#produceType').combobox("getValue");
    if (produceType == null || produceType == "") {
        $.messager.alert('错误', '请先选择物资的类型！', 'error');
        return;
    }
    defaultSearch();
    $('#assetListDialog').dialog('center').dialog('open');
}

function defaultSearch() {
    $('#searchQuerys input[name="assetCode"]').val('');
    $('#searchQuerys input[name="assetChsName"]').val('');
    $('#useDeptId').combotree("setValue", '');
    $('#userId').combobox("setValue", '');
    $('#assetManageDeptId').combotree("setValue", '');
    $('#assetManagerId').combobox("setValue", '');
    $('#assetAssetStatus').combobox("setValue", '');
    $('#dataGridTable').datagrid('load', {
        produceTypeStr: $('#produceType').combobox("getValue"),
        assetCode: '',
        assetChsName: '',
        useDeptId: '',
        userId: '',
        manageDeptId: '',
        managerId: '',
        showType: "1",
        assetStatus: defaulState
    })
}

function searchByQuerys() {
    $('#dataGridTable').datagrid('load', {
        produceTypeStr: $('#produceType').combobox("getValue"),
        assetCode: $('#searchQuerys input[name="assetCode"]').val(),
        assetChsName: $('#searchQuerys input[name="assetChsName"]').val(),
        useDeptId: $('#useDeptId').combobox("getValue"),
        userId: $('#userId').combobox("getValue"),
        manageDeptId: $('#assetManageDeptId').combotree("getValue"),
        managerId: $('#assetManagerId').combobox("getValue"),
        showType: "1",
        assetStatus: $('#assetAssetStatus').combobox("getValue")
    })
}

function addAssetToList() {
    var data = $('#dataGridTable').datagrid('getSelections');
    var assetIdList = "";
    var assetInventoryId = $('#assetInventoryId').val();
    if (data.length == 0) {
        $.messager.alert('错误', '选择一个添加项！', 'error');
        return;
    }

    var getData = $('#assetInventoryDataGrid').datagrid('getData');

    for (var index in data) {
        if (contains(getData.rows, data[index].id) == false)
            assetIdList = (assetIdList + "," + data[index].id);
    }

    if (assetIdList.substr(0, 1) == ',')
        assetIdList = assetIdList.substr(1);
    $.ajax({
        url: 'assetInventory/create_assetreceiveusetemp',
        type: 'POST',
        data: {
            assetIdListStr: assetIdList,
            assetInventoryId: assetInventoryId
        },
        success: function (data) {
            $.messager.alert('提示', '添加成功！', 'info', function () {
                closeAssetListDialog();
                $('#assetInventoryDataGrid').datagrid('load', {assetInventoryId: assetInventoryId});
            });
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });


}

function removeAssetToList() {
    var data = $('#assetInventoryDataGrid').datagrid('getSelections');
    var assetInventoryId = $('#assetInventoryId').val();
    var assetInventoryTempIdList = "";
    if (data.length == 0) {
        $.messager.alert('错误', '选择一个移除项！', 'error');
        return;
    }
    // 删除选中行
    for (var i = data.length - 1; i >= 0; i--) {
        assetInventoryTempIdList = (assetInventoryTempIdList + "," + data[i].id);
        if (assetInventoryTempIdList.substr(0, 1) == ',')
            assetInventoryTempIdList = assetInventoryTempIdList.substr(1);
    }
    $.ajax({
        url: 'assetInventory/delete_assetInventorytemps',
        type: 'POST',
        data: {
            assetInventoryTempIdListStr: assetInventoryTempIdList,
        },
        success: function (data) {
            $.messager.alert('提示', '删除成功！', 'info', function () {
                $('#assetInventoryDataGrid').datagrid('load', {assetInventoryId: assetInventoryId});
            });
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });


}

// 添加盘点范围
function addInventoryScope() {
    $('#assetScopeDialog').dialog('center').dialog('open');
}

function closeDialog() {
    $('#instSiteCodeDialog').dialog('close');
}

function openDialig() {
    if (!isDialogInit) {
        $('#instSiteCodeDialogFrame').attr('src', 'asset_save_place');
        isDialogInit = true;
    }
    $('#instSiteCodeDialog').dialog('center').dialog('open');
    isOld = true;
}

// 保存盘点资产单
function saveInventorySubmit() {
    $('#saveInventorySubmit').attr('disabled', "true");
    // 获取资产列表
// var data = $('#assetInventoryDataGrid').datagrid('getData');
// var assetList = JSON.stringify(data.rows);
// ;
    // 获取盘点资产范围列表
// var dataScope = $('#assetInventoryScope').datagrid('getData');
// var inventoryScopeList = JSON.stringify(dataScope.rows);
    var assetInventoryId = $('#assetInventoryId').val();
    var inventoryName = $('#inventoryName').val();
    var now = new Date();
    var launchDateStr = formatter(now, "yyyy-MM-dd");
    var reason = $('#reason').val();

    if (isEmpty(inventoryName)) {
        $('#saveInventorySubmit').removeAttr('disabled');
        $.messager.alert('错误', '请填写盘点单名称!', 'error');
        return;
    }
    if (isEmpty(reason)) {
        $('#saveInventorySubmit').removeAttr('disabled');
        $.messager.alert('错误', '请填写盘点任务说明!', 'error');
        return;
    }
    var produceType = $('#produceType').combobox("getValue");
    if (produceType == null || produceType == "") {
        $.messager.alert('错误', '请先选择物资的类型！', 'error');
        return;
    }
    $.ajax({
        url: 'assetInventory/update_assetinventory',
        type: 'POST',
        data: {
            reason: reason,
            inventoryName: inventoryName,
            // sponsor : sponsor,
            lanuchDateStr: launchDateStr,
            id: assetInventoryId,
            produceTypeStr: produceType
        },
        success: function (data) {
            $('#saveInventorySubmit').removeAttr('disabled');
            $.messager.alert('提示', '保存成功！', 'info', function () {
                window.location.href = 'assetInventory_query';
            });
        },
        error: function (data) {
            $('#saveInventorySubmit').removeAttr('disabled');
            AjaxErrorHandler(data);
        }
    });
}

// 保存并发起审批
function saveAndCheckInventory() {

    $('#saveInventorySubmit').attr('disabled', "true");
    $('#saveAndCheckInventory').attr('disabled', "true");
    // 获取资产列表
// var data = $('#assetInventoryDataGrid').datagrid('getData');
// var assetList = JSON.stringify(data.rows);
// ;
    // 获取盘点资产范围列表
// var dataScope = $('#assetInventoryScope').datagrid('getData');
// var inventoryScopeList = JSON.stringify(dataScope.rows);
    var assetInventoryId = $('#assetInventoryId').val();
    var inventoryName = $('#inventoryName').val();
    var now = new Date();
    var launchDateStr = formatter(now, "yyyy-MM-dd");
    var reason = $('#reason').val();

    if (isEmpty(inventoryName)) {
        $('#saveInventorySubmit').removeAttr('disabled');
        $('#saveAndCheckInventory').removeAttr('disabled');
        $.messager.alert('错误', '请填写盘点单名称!', 'error');
        return;
    }
    if (isEmpty(reason)) {
        $('#saveInventorySubmit').removeAttr('disabled');
        $('#saveAndCheckInventory').removeAttr('disabled');
        $.messager.alert('错误', '请填写盘点任务说明!', 'error');
        return;
    }

    $.ajax({
        url: 'assetInventory/update_and_distribution_assetinventory',
        type: 'POST',
        data: {
            reason: reason,
            inventoryName: inventoryName,
            // sponsor : sponsor,
            lanuchDateStr: launchDateStr,
            id: assetInventoryId
        },
        success: function (data) {
            $('#saveInventorySubmit').removeAttr('disabled');
            $('#saveAndCheckInventory').removeAttr('disabled');
            $.messager.alert('提示', '保存成功！', 'info', function () {
                window.location.href = 'assetInventory_query';
            });
        },
        error: function (data) {
            $('#saveInventorySubmit').removeAttr('disabled');
            $('#saveAndCheckInventory').removeAttr('disabled');
            AjaxErrorHandler(data);
        }
    });
}

// 发起审批
function approvalInitiation(id) {
    $.ajax({
        url: 'assetSequestration/assetseal_approval',
        type: 'POST',
        data: {
            id: id
        },
        success: function (data) {
            /*
             * $.messager.alert('审批', '审批成功！', 'info',function(){
             * $('#dataGridTable').datagrid('reload'); });
             */
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function addScopeToDatagrid() {
    $('#uploadSaveBtnScope').attr('disabled', "true");
    var data = {};
    data.assetStatus = $('#assetStatus').combobox('getValue');
    data.produceTypeStr = $('#produceType').combobox("getValue");
    data.assetType = $('#assetTypeDialog input[name="rowIdHidden"]').val();
    data.manageDeptId = $('#manageDeptId').combobox('getValue');
    data.useDeptId = $('#useDeptIdScope').combobox('getValue');
    data.managerId = $('#managerId').combobox('getValue');
    data.userId = $('#userIdScope').combobox('getValue');
    data.savePlaceId = $('#savePlaceId').val();
    data.showType = "1";

    if (objIsEmpty(data)) {
        $.messager.alert('错误', '选择一个添加项！', 'error');
        $('#uploadSaveBtnScope').removeAttr('disabled');
        return false;
    }
    // 根据条件差取资产数量
    $.ajax({
        url: 'asset/asset_datagrid',
        type: 'POST',
        data: data,
        success: function (res) {
            if (res) {
                if (res.total <= 0) {
                    $.messager.alert('错误', '该范围下没有可以盘点的资产！', 'error');
                    $('#uploadSaveBtnScope').removeAttr('disabled');
                    return false;
                }
                appendRowToDatagrid(res.total);
            }
        },
        error: function (data) {
            AjaxErrorHandler(data);
            $('#uploadSaveBtnScope').removeAttr('disabled');
        }
    });


}

function appendRowToDatagrid(num) {
    var assetInventoryId = $('#assetInventoryId').val();
    var data = {};
    data.assetInventoryId = assetInventoryId;
    data.inventoryContent = '';
    data.assetStatus = $('#assetStatus').combobox('getValue');
    data.assetType = $('#assetTypeDialog input[name="rowIdHidden"]').val();
    data.manageDeptId = $('#manageDeptId').combobox('getValue');
    data.useDeptId = $('#useDeptIdScope').combobox('getValue');
    data.managerId = $('#managerId').combobox('getValue');
    data.userId = $('#userIdScope').combobox('getValue');
    data.savePlaceId = $('#savePlaceId').val();
    data.inventoryNum = num;

    var assetStatus = $('#assetStatus').combobox('getText');
    var assetType = $('#showAssetTypeCodeAndData').text();
    var manageDeptId = $('#manageDeptId').combobox('getText');
    var useDeptId = $('#useDeptIdScope').combobox('getText');
    var managerId = $('#managerId').combobox('getText');
    var userId = $('#userIdScope').combobox('getText');
    var savePlaceId = $('#savePlaceId').val();
    var savePlace = $("#showCodeAndData").html();
    if (!isEmpty(manageDeptId))
        data.inventoryContent += '属于' + manageDeptId + '管理,';
    if (!isEmpty(useDeptId))
        data.inventoryContent += '并且属于' + useDeptId + '使用,';
    if (!isEmpty(assetType))
        data.inventoryContent += '并且类型属于' + assetType + ',';
    if (!isEmpty(assetStatus))
        data.inventoryContent += '并且状态是' + assetStatus + ',';
    if (!isEmpty(savePlace) && savePlace != '&nbsp;&nbsp;&nbsp;&nbsp;')
        data.inventoryContent += '并且位置是' + savePlace + ',';
    if (!isEmpty(managerId))
        data.inventoryContent += '并且管理员是' + managerId + ',';
    if (!isEmpty(userId))
        data.inventoryContent += '并且资产使用人是' + userId + ',';
    if (!isEmpty(data.inventoryContent)) {
        data.inventoryContent = data.inventoryContent.substring(0, data.inventoryContent.length - 1)
        data.inventoryContent = '所有' + data.inventoryContent + '的资产';
    }
    if (isEmpty(data.inventoryContent)) {
        $('#uploadSaveBtnScope').removeAttr('disabled');
        $.messager.alert('错误', '盘点范围不能为空!', 'error');
        return;
    }
    // 根据条件差取资产数量
    $.ajax({
        url: 'assetInventory/create_assetinventoryscope',
        type: 'POST',
        data: data,
        success: function (res) {
            $('#uploadSaveBtn').removeAttr('disabled');
            $('#assetInventoryScope').datagrid('load', {id: assetInventoryId});
            $('#assetInventoryScope').datagrid("autoSizeColumn");
            closeAssetScopeDialog();
            $('#uploadSaveBtnScope').removeAttr('disabled');
        },
        error: function (data) {
            AjaxErrorHandler(data);
            $('#uploadSaveBtnScope').removeAttr('disabled');
        }
    });
}

// 判断字符是否为空的方法
function isEmpty(obj) {
    if (typeof obj == "undefined" || obj == null || obj == "") {
        return true;
    } else {
        return false;
    }
}

$(function () {
    fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_PRODUCE_TYPE', 'produceType');
    $('#produceType').combobox({
        disabled: true,
        value: $('#type').val()
    });

    var assetInventoryId = $('#assetInventoryId').val();
    fillEnumComboFilter('ram.asset.service.IAssetService', 'ASSET_STATUS', 'assetStatus',defaulState);
    fillEnumComboFilter('ram.asset.service.IAssetService', 'ASSET_STATUS', 'assetAssetStatus',defaulState);
    var managerId = $('#UserID').val();

    $('#assetInventoryScope').datagrid({
        url: 'assetInventory/inventoryscope_datagrid',
        // fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        pagination: true,
        pageSize: 10,
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {
            id: assetInventoryId,
        },
        columns: [[{
            field: 'inventoryContent',
            width: 450,
            title: '盘点内容'
        }, {
            field: 'inventoryNum',
            title: '盘点数量'
        },
            {
                field: 'id', title: '操作', formatter: function (value) { // value, row, index
                    var returnVar = '<a href="javascript:void(0);" onClick="deleteAssetInventoryScopeSubmit(\'' + value + '\');">删除</a>'
                    return returnVar;
                }
            },

            // 延申信息
            {
                field: 'manageDeptId',
                title: '管理部门id',
                hidden: true
            }, {
                field: 'useDeptId',
                title: '使用部门id',
                hidden: true
            }, {
                field: 'assetStatus',
                title: '资产状态',
                hidden: true
            }, {
                field: 'managerId',
                title: '管理员id',
                hidden: true
            }, {
                field: 'savePlaceId',
                title: '具体位置id',
                hidden: true
            },
            // 安装未写
            {
                field: 'assetType',
                title: '资产类型',
                hidden: true
            }, {
                field: "userId",
                title: "使用人",
                hidden: true
            }]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });

    $('#assetInventoryDataGrid').datagrid({
        url: 'assetInventory/inventorytemp_datagrid',
        // fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        pagination: true,
        pageSize: 10,
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {
            assetInventoryId: assetInventoryId,
        },
        columns: [[{
            field: 'id',
            checkbox: true
        }, {
            field: 'assetCode',
            title: '资产编码'
        }, {
            field: 'assetTypeStr',
            title: '资产类别'
        }, {
            field: 'specAndModels',
            title: '规格型号'
        }, {
            field: 'seriesNum',
            title: '序列号'
        }, {
            field: 'unitOfMeasStr',
            title: '计量单位'
        }, {
            field: 'assetBrand',
            title: '品牌'
        }, {
            field: 'purcPrice',
            title: '采购价'
        }, {
            field: 'equiOrigValue',
            title: '资产原值'
        }, {
            field: 'techPara',
            title: '技术参数'
        }, {
            field: 'remark',
            title: '备注'
        },
            // 延申信息
            {
                field: 'companyStr',
                title: '所属公司'
            }, {
                field: 'belongLineStr',
                title: '所属线路/建筑'
            }, {
                field: 'buyDate',
                title: '购置日期'
            }, {
                field: 'manageDeptStr',
                title: '主管部门'
            }, {
                field: 'managerStr',
                title: '资产管理员'
            },
            // 安装未写
            {
                field: 'assetSource',
                title: '资产来源'
            }, {
                field: 'contractNum',
                title: '合同编号'
            }, {
                field: 'tendersNum',
                title: '标段编号'
            }, {
                field: 'mainPeriod',
                title: '维保期'
            }, {
                field: 'sourceUser',
                title: '联系人'
            }, {
                field: 'sourceContactInfo',
                title: '联系方式'
            }, {
                field: 'prodTime',
                title: '出厂日期'
            }, {
                field: "savePlaceStr",
                title: "使用位置"
            }, {
                field: "savePlaceId",
                hidden: true
            }, {
                field: "userId",
                title: "使用人",
                hidden: true
            }]],
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
            // managerId : managerId,
            // deActiveTimeStr:1,
            // assetStatus:6
        },
        columns: [[{
            field: 'id',
            checkbox: true
        }, {
            field: 'assetCode',
            title: '资产编码'
        }, {
            field: 'combinationAssetName',
            title: '资产名称'
        }, {
            field: 'assetTypeStr',
            title: '资产类型'
        }, {
            field: 'specAndModels',
            title: '规格型号'
        }, {
            field: 'assetBrand',
            title: '品牌'
        }, {
            field: 'unitOfMeasStr',
            title: '计量单位'
        },
            // {field: '',title: '安装位置'},
            {
                field: 'useDeptStr',
                title: '使用部门'
            }, {
                field: 'useStr',
                title: '使用人'
            }, {
                field: 'manageDeptStr',
                title: '主管部门'
            }, {
                field: 'managerStr',
                title: '资产管理员'
            }, {
                field: 'belongLineStr',
                title: '所属线路'
            },

            {
                field: 'assetStatus',
                title: '资产状态'
            }]]
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

    $('#useDeptIdScope').combotree({
        url: 'base/get_dept_tree',
        method: 'POST',
        onSelect: function (record) {
            $.ajax({
                url: 'base/dept_user_combobox',
                type: 'POST',
                data: {deptId: record.id},
                success: function (data) {
                    $('#userIdScope').combobox('clear');
                    $('#userIdScope').combobox('loadData', data);
                },
                error: function (data) {
                    AjaxErrorHandler(data);
                }
            });
        }
    });

    $('#instSiteCodeDialog').dialog('close');
})

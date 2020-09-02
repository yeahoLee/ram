//打开创建dialog之前清空dialog
function openCreateDialog() {
    //增加视图的dialog清空
    $('#createDynamicViewDialog input[name="assetViewName"]').val('');
    $('#assetStatus').combobox('setValue', '');
    $('#showAssetTypeCodeAndData').text('');
    $('#assetTypeDialog input[name="rowIdHidden"]').val('');
    $('#createDynamicViewDialog input[name="savePlace"]').val('');
    $('#manageDept').combotree('setValue', '');
    $('#useDept').combotree('setValue', '');
    $('#managerId').combobox('setValue', '');
    $('#userId').combobox('setValue', '');
    $('#showCodeAndData').text("");
    $('#instSiteCodeDialog input[name="rowIdHidden"]').val('');
    //增加视图中选择位置的清空
    $('#assetLineId').combobox('clear');
    $('#stationId').combobox('clear');
    $('#buildNumId').combobox('clear');
    $('#floorNumId').combobox('clear');
    $('#createDynamicViewDialog').dialog('center').dialog('open');
}

//创建动态视图
function createDynamicView() {
    $('#createBtn').attr('disabled', true);
    var assetViewName = $('#createDynamicViewDialog input[name="assetViewName"]').val();
    var assetStatus = $('#createDynamicViewDialog input[name="assetStatus"]').val();
    var assetType = $('#assetTypeDialog input[name="rowIdHidden"]').val();//$('#createDynamicViewDialog input[name="assetType"]').val();
    var savePlaceId = $('#instSiteCodeDialog input[name="rowIdHidden"]').val();
    var manageDept = $('#createDynamicViewDialog input[name="manageDept"]').val();
    var useDept = $('#createDynamicViewDialog input[name="useDept"]').val();
    var managerId = $('#createDynamicViewDialog input[name="managerId"]').val();
    var userId = $('#createDynamicViewDialog input[name="userId"]').val();

    $.ajax({
        url: 'asset/create_dynamic_view',
        type: 'POST',
        data: {
            assetViewName: assetViewName,
            assetStatus: assetStatus,
            assetType: assetType,
            savePlaceId: savePlaceId,
            manageDeptId: manageDept,
            useDeptId: useDept,
            managerId: managerId,
            userId: userId,
        },
        success: function (data) {
            $('#createBtn').removeAttr('disabled');
            $.messager.alert('提示', '新建成功！', 'info', function () {
                $("#createDynamicViewDialog").dialog("close");
                $('#dataGridTable').datagrid('reload');
            });
        },
        error: function (data) {
            $('#createBtn').removeAttr('disabled');
            AjaxErrorHandler(data);
        }
    });
}

function closeModifyDynamicView() {
    $("#modifyDynamicViewDialog").dialog("close");
}

//点击修改后打开修改动态视图的dialog
function openModifyDynamicViewDialog(dynamicViewId) {
    $('#modifyDynamicViewDialog input[name="modifyDnamicViewHiddenId"]').val(dynamicViewId);

    $.ajax({
        url: 'asset/get_dynamic_view',
        type: 'POST',
        data: {
            dynamicViewId: dynamicViewId,
        },
        success: function (data) {
            $('#modifyDynamicViewDialog input[name="assetViewName"]').val(data.assetViewName);
            $('#modifyDynamicViewDialog input[name="assetViewCondition"]').val(data.assetViewCondition);
            $('#modifyAssetStatus').combobox('setValue', data.assetStatusNum);
            //$('#modifyAssetType').combobox('setValue',data.assetTypeNum);
            $('#assetTypeDialog input[name="rowIdHidden"]').val(data.assetTypeNum);
            if (data.assetType != null) {
                $('#modifyShowAssetTypeCodeAndData').text(data.assetType);
            } else {
                $('#modifyShowAssetTypeCodeAndData').text("");
            }
            $('#modifyDynamicViewDialog input[name="savePlace"]').val(data.savePlaceId);
            $('#manageDeptModify').combotree('setValue', data.manageDeptId);
            $('#useDeptModify').combotree('setValue', data.useDeptId);
            $('#managerIdModify').combobox('setValue', data.managerId);
            $('#userIdModify').combobox('setValue', data.userId);
            //补充，修改设置地点id
            $('#instSiteCodeDialog input[name="rowIdHidden"]').val(data.savePlaceId);
            //$('#modifyDialogShowCodeAndData').text(data.codeAndChsName);
            if (data.codeAndChsName != null) {
                $('#modifyDialogShowCodeAndData').text(data.codeAndChsName);
            } else {
                $('#modifyDialogShowCodeAndData').text("");
            }
        },
        error: function (data) {
            $('#saveBtn').removeAttr('disabled');
            AjaxErrorHandler(data);
        }
    });
    //打开dialog
    $('#modifyDynamicViewDialog').dialog('center').dialog('open');
}

//点击修改按钮后执行修改操作
function modifyDynamicView() {
    var assetViewName = $('#modifyDynamicViewDialog input[name="assetViewName"]').val();
    var assetStatus = $('#modifyDynamicViewDialog input[name="assetStatus"]').val();
    //var assetType=$('#modifyDynamicViewDialog input[name="assetType"]').val();
    var assetType = $('#assetTypeDialog input[name="rowIdHidden"]').val();
    var savePlaceId = $('#instSiteCodeDialog input[name="rowIdHidden"]').val();
    var manageDept = $('#modifyDynamicViewDialog input[name="manageDept"]').val();
    //var useDept=$('#modifyDynamicViewDialog input[name="useDept"]').val();
    var useDept = $('#useDeptModify').combotree('getValue');
    var managerId = $('#modifyDynamicViewDialog input[name="managerId"]').val();
    var userId = $('#modifyDynamicViewDialog input[name="userId"]').val();
    var assetViewCondition = assetViewName + "," + assetStatus + "," + assetType + "," + savePlaceId + "," + manageDept + "," + useDept + "," + managerId + "," + userId;
    var dynamicViewId = $('#modifyDynamicViewDialog input[name="modifyDnamicViewHiddenId"]').val();

    $.ajax({
        url: 'asset/update_asset_dynamic',
        type: 'POST',
        data: {
            id: dynamicViewId,
            assetViewName: assetViewName,
            assetStatus: assetStatus,
            assetType: assetType,
            savePlaceId: savePlaceId,
            manageDeptId: manageDept,
            useDeptId: useDept,
            managerId: managerId,
            userId: userId
        },
        success: function (data) {
            $.messager.alert('提示', '修改成功！', 'info', function () {
                $("#modifyDynamicViewDialog").dialog("close");
                $('#dataGridTable').datagrid('reload');
            });
        },
        error: function (data) {
            $('#saveBtn').removeAttr('disabled');
            AjaxErrorHandler(data);
        }
    });
}

//通过id删除视图
function deleteDynamicViewConfirm(dynamicViewId) {
    $.messager.confirm('确定', '确定要删除吗?', function (r) {
        if (r) {
            $.ajax({
                url: 'asset/delete_dynamic_view',
                type: 'POST',
                data: {
                    dynamicViewId: dynamicViewId,
                },
                success: function (data) {
                    $.messager.alert('提示', '删除成功！', 'info', function () {
                        $('#dataGridTable').datagrid('reload');
                    });
                },
                error: function (data) {
                    AjaxErrorHandler(data);
                }
            });
        }
    });
}

//打开新的动态视图dialog
function openUserDynamicViewDialog(id, assetViewState) {
    //当点击启用或者停用视图时先拿到数据库的assetViewState,如果是启用改为停用反之亦然
    var stateStr;
    //var assetViewState;
    //var row = $('#dataGridTable').datagrid('getSelected');
    if (assetViewState == 0) {
        assetViewState = 1;
        stateStr = "视图已启用！";
    } else {
        assetViewState = 0;
        stateStr = "视图已停用！";
    }

    $.ajax({
        url: 'asset/change_dynamic_view_state',
        type: 'POST',
        data: {
            id: id,
            assetViewState: assetViewState
        },
        success: function (data) {
            //$.messager.alert('提示', row.assetViewName+stateStr, 'info', function(){
            $.messager.alert('提示', stateStr, 'info', function () {
                $('#dataGridTable').datagrid('reload');
            });
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function showCodeAndName(id, codeAndName) {
    $('#showCodeAndData').text(codeAndName);
    $('#modifyDialogShowCodeAndData').text(codeAndName);
    $('#instSiteCodeDialog input[name="rowIdHidden"]').val(id);
}

function closeDialog() {
    $('#instSiteCodeDialog').dialog('close');
}

function showAssetTypeCodeAndName(id, codeAndName) {
    $('#showAssetTypeCodeAndData').text(codeAndName);
    $('#modifyShowAssetTypeCodeAndData').text(codeAndName);
    $('#assetTypeDialog input[name="rowIdHidden"]').val(id);
}

function closeAssetTypeDialog() {
    $('#assetTypeDialog').dialog('close');
}

$(function () {
    // fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_TYPE', 'assetType');
    fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_STATUS', 'assetStatus');
    // fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_TYPE', 'modifyAssetType');
    fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_STATUS', 'modifyAssetStatus');

    //用datagrid加载动态视图表格
    $('#dataGridTable').datagrid({
        url: 'asset/asset_dynamic_datagrid',
        fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        pagination: true,
        pageSize: 20,
        singleSelect: true,
        title: '动态视图',
        //toolbar: '#dataGridTableButtons',
        toolbar: '#creatBt',
        loadMsg: '程序处理中，请稍等...',
        columns: [[
            {
                field: 'id', title: '操作', formatter: function (value, row) { //row
                    var operate = '<a onClick="openModifyDynamicViewDialog(\'' + value + '\');">修改</a> '
                        + '<a href="javascript:void(0);" onClick="deleteDynamicViewConfirm(\'' + value + '\')">删除</a> ';
                    return operate + '<a id="viewState" onClick="openUserDynamicViewDialog(\'' + value + '\',\'' + row.assetViewState + '\');">' + (row.assetViewState == 1 ? '停用视图' : '启用视图') + '</a>';
                }
            },
            {field: 'assetViewName', title: '视图名称'},
            {field: 'assetViewStateStr', title: '视图状态'},
            {field: 'assetViewCondition', title: '视图条件'}
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });

    $('#useDept').combotree({
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

    $('#useDeptModify').combotree({
        url: 'base/get_dept_tree',
        method: 'POST',
        onSelect: function (record) {
            $.ajax({
                url: 'base/dept_user_combobox',
                type: 'POST',
                data: {deptId: record.id},
                success: function (data) {
                    $('#userIdModify').combobox('clear');
                    $('#userIdModify').combobox('loadData', data);
                },
                error: function (data) {
                    AjaxErrorHandler(data);
                }
            });
        }
    });
})


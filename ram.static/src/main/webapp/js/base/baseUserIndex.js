function searchByQuerys() {
    $('#dataGridTable').datagrid('load', {
        q: $('#searchFilterText').val()
    })
}

function createUserSubmit() {
    var userName = $('#createUserDialog input[name="userName"]').val();
    var chsName = $('#createUserDialog input[name="chsName"]').val();

    $.ajax({
        url: 'base/create_user',
        type: 'POST',
        data: {
            userName: userName,
            chsName: chsName,
        },
        success: function () {
            $('#dataGridTable').datagrid('reload');
            $('#createUserDialog input[name="userName"]').val('');
            $('#createUserDialog input[name="chsName"]').val('');
            $('#createUserDialog').dialog('close');
            $.messager.alert('提示', '新建成功！', 'info');
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function openUpdateUserDialog(index) {
    closeAllDialog();
    var rowData = $('#dataGridTable').datagrid('getRows');
    $('#updateUserDialog input[name="id"]').val(rowData[index].id);
    $('#updateUserDialog input[name="chsName"]').val(rowData[index].chsName);
    $('#updateUserDialog input[name="userName"]').val(rowData[index].userName);
    $('#updateUserDialog').dialog('center').dialog('open');
}

function updateUserSubmit() {
    var id = $('#updateUserDialog input[name="id"]').val();
    var userName = $('#updateUserDialog input[name="userName"]').val();
    var chsName = $('#updateUserDialog input[name="chsName"]').val();

    $.ajax({
        url: 'base/update_user',
        type: 'POST',
        data: {
            id: id,
            userName: userName,
            chsName: chsName,
        },
        success: function () {
            $('#dataGridTable').datagrid('reload');
            $('#updateUserDialog').dialog('close');
            $.messager.alert('提示', '更新成功！', 'info');
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function importUserXls() {
    $('#importForm').form('submit', {
        url: 'base/import_usesr_xls',
        success: function (data) {
            var result = $.parseJSON(data);
            if (!result.errorMessage)
                $.messager.alert('提示', '上传成功!', 'info');
            else
                $.messager.alert('提示', '上传失败: ' + result.errorMessage, 'error');
        }
    });
}

function closeAllDialog() {
    $('#createUserDialog').dialog('close');
    $('#updateUserDialog').dialog('close');
    $('#importUserXlsDialog').dialog('close');
}

function openDialog(dialogName) {
    closeAllDialog();
    $('#' + dialogName + '').dialog('center').dialog('open');
}

function resetPwd(userId, chsName) {
    $.messager.confirm('确定', '确定要重置【' + chsName + '】的密码吗？', function (r) {
        if (r) {
            $.ajax({
                url: 'base/reset_pwd',
                type: 'POST',
                data: {userId: userId},
                success: function (data) {
                    $.messager.alert('重置', '重置密码成功！', 'info');
                },
                error: function (data) {
                    AjaxErrorHandler(data);
                }
            });
        }
    });
}

function syncCom() {
    $.ajax({
        url: 'base/sync_com',
        type: 'POST',
        success: function (data) {
            $.messager.alert('提示', '同步成功！', 'info');
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

$(function () {
    $('#dataGridTable').datagrid({
        url: 'base/user_datagrid',
        method: 'POST',
        title: '用户管理',
        toolbar: '#dataGridTableButtons',
        fit: true,
        striped: true,
        rownumbers: true,
        singleSelect: true,
        pagination: true,
        pageSize: 20,
        columns: [[
            {
                field: 'id', title: '操作', formatter: function (value, row, index) {
                    return '<a href="javascript:void(0);" onClick="openUpdateUserDialog(' + index + ');">修改</a> '
                        + '<a href="javascript:void(0);" onClick="resetPwd(\'' + value + '\',\'' + row.chsName + '\');">重置密码</a>';
                }
            },
            /*{field: 'userCode', title: '用户编码'},*/
            {field: 'userName', title: '用户名'},
            {field: 'chsName', title: '中文名'},
            {field: 'permDeptName', title: '所属部门'}
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });
});
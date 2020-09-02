//创建部门
function createDeptSubmit() {
    var pdId = $('#pdId').combobox('getValue');
    var deptCode = $('#createDeptDialog input[name="deptCode"]').val();
    var deptName = $('#createDeptDialog input[name="deptName"]').val();

    $.ajax({
        url: 'base/create_dept',
        type: 'POST',
        data: {
            pdId: pdId,
            deptCode: deptCode,
            deptName: deptName,
        },
        success: function () {
            $.messager.alert('提示', '新建成功！', 'info', function () {
                $('#dataGridTable').datagrid('reload');
            });
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function openDeptUserListDialog(deptId, deptName) {
    closeAllDialog();
    $('#deptUserListDialog').dialog({title: deptName});
    $('#deptUserListDialog input[name="deptId"]').val(deptId);
    $('#deptUserDataGridTable').datagrid('load', {deptId: deptId});
    $('#deptUserListDialog').dialog('center').dialog('open');
}

//新增部门用户
function setDeptUserSubmit() {
    var deptId = $('#deptUserListDialog input[name="deptId"]').val();
    var userId = $('#userId').combobox('getValue');
    var manager = $('#addUserToDeptDialog input[name="manager"]:checked').val();
    var leader = $('#addUserToDeptDialog input[name="leader"]:checked').val();

    $.ajax({
        url: 'base/set_dept_user',
        type: 'POST',
        data: {
            deptId: deptId,
            userId: userId,
            manager: manager,
            leader: leader
        },
        success: function () {
            $('#deptUserDataGridTable').datagrid('load', {deptId: deptId});
            $.messager.alert('提示', '新增成功！', 'info');
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function searchByQuerys() {
    $('#deptUserDataGridTable').datagrid('load', {
        deptId: $('#deptUserListDialog input[name="deptId"]').val(),
        q: $('#deptUserListDialog input[name="userId"]').val()
    });
}

function deleteDeptUserSubmit(userId) {
    var deptId = $('#deptUserListDialog input[name="deptId"]').val();

    $.ajax({
        url: 'base/delete_dept_user',
        type: 'POST',
        data: {
            deptId: deptId,
            userIdListStr: userId
        },
        success: function () {
            $.messager.alert('提示', '删除成功！', 'info');
            $('#deptUserDataGridTable').datagrid('load', {deptId: deptId});
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function openCreateDeptDialig() {
    closeAllDialog();
    $('#createDeptDialog').dialog('center').dialog('open');
}

function closeAllDialog() {
    $('#createDeptDialog').dialog('close');
    $('#deptUserListDialog').dialog('close');
}

$(function () {
    $('#dataGridTable').datagrid({
        url: 'base/dept_datagrid',
        method: 'POST',
        title: '部门管理',
        toolbar: '#dataGridTableButtons',
        fit: true,
        striped: true,
        rownumbers: true,
        singleSelect: true,
        pagination: true,
        pageSize: 20,
        columns: [[
            {
                field: 'id', title: '操作', formatter: function (value, row) {
                    return '<a href="javascript:void(0);" onClick="openDeptUserListDialog(\'' + value + '\');">人员设置</a> ';
                }
            },
            {field: 'deptName', title: '部门名称'},
            {field: 'deptCode', title: '部门编码'},
            {field: 'pdName', title: '上级部门名称'}
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });

    //部门用户
    $('#deptUserDataGridTable').datagrid({
        url: 'base/dept_user_datagrid',
        method: 'POST',
        toolbar: '#permUserDataGridTableButtons',
        fit: true,
        striped: true,
        singleSelect: true,
        rowNumbers: true,
//        pagination: true,
//        pageSize: 10,
        columns: [[
            {
                field: 'id', title: '操作', formatter: function (value, row) {
                    return '<a href="javascript:void(0);" onClick="deleteDeptUserSubmit(\'' + value + '\');">删除</a> ';
                }
            },
            {field: 'userName', title: '用户名'},
            {field: 'chsName', title: '中文名'},
            {
                field: 'manager', title: '资产管理员', formatter: function (value) {
                    if (value) return "是"; else return "否";
                }
            },
            {
                field: 'leader', title: '部门领导', formatter: function (value) {
                    if (value) return "是"; else return "否";
                }
            }
        ]]
    });

});

function createPermSubmit() {
    var code = $('#createPermDialog input[name="code"]').val();
    var chsName = $('#createPermDialog input[name="chsName"]').val();
    var remark = $('#createRemark').val();

    $.ajax({
        url: 'base/create_perm',
        type: 'POST',
        data: {
            code: code,
            chsName: chsName,
            remark: remark
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

function updatePermSubmit() {
    var id = $('#updatePermDialog input[name="id"]').val();
    var code = $('#updatePermDialog input[name="code"]').val();
    var chsName = $('#updatePermDialog input[name="chsName"]').val();
    var remark = $('#updateRemark').val();

    $.ajax({
        url: 'base/update_perm',
        type: 'POST',
        data: {
            id: id,
            code: code,
            chsName: chsName,
            remark: remark
        },
        success: function () {
            $.messager.alert('提示', '更新成功！', 'info', function () {
                $('#dataGridTable').datagrid('reload');
            });
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function deletePermSubmit(id) {
    $.messager.confirm('确定', '确定要删除吗？', function (r) {
        if (r) {
            $.ajax({
                url: 'base/delete_perm',
                type: 'POST',
                data: {
                    id: id
                },
                success: function () {
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

function createPermItemSubmit() {
    var code = $('#createPermItemDialog input[name="code"]').val();
    var chsName = $('#createPermItemDialog input[name="chsName"]').val();
    var menuPermName = $('#createMenuPermName').combobox('getText');

    $.ajax({
        url: 'base/create_perm_item',
        type: 'POST',
        data: {
            code: code,
            chsName: chsName,
            menuPermName: menuPermName
        },
        success: function () {
            $('#createPermItemDialog').dialog('close');
            $.messager.alert('提示', '新建成功！', 'info', function () {
                $('#permItemDataGridTable').datagrid('reload');
            });
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function updatePermItemSubmit() {
    var id = $('#updatePermItemDialog input[name="id"]').val();
    var code = $('#updatePermItemDialog input[name="code"]').val();
    var chsName = $('#updatePermItemDialog input[name="chsName"]').val();
    var menuPermName = $('#updateMenuPermName').combobox('getText');

    $.ajax({
        url: 'base/update_perm_item',
        type: 'POST',
        data: {
            id: id,
            code: code,
            chsName: chsName,
            menuPermName: menuPermName
        },
        success: function () {
            $('#updatePermItemDialog').dialog('close');
            $.messager.alert('提示', '更新成功！', 'info', function () {
                $('#permItemDataGridTable').datagrid('reload');
            });
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function deletePermItem(id) {
    $.messager.confirm('确定', '确定要删除吗？', function (r) {
        if (r) {
            $.ajax({
                url: 'base/delete_perm_item',
                type: 'POST',
                data: {
                    id: id
                },
                success: function () {
                    $.messager.alert('提示', '删除成功！', 'info', function () {
                        $('#permItemDataGridTable').datagrid('reload');
                    });
                },
                error: function (data) {
                    AjaxErrorHandler(data);
                }
            });
        }
    });
}

function deletePermUser(userId, deptId) {
    var permGroupId = $('#permUserListDialog input[name="permGroupId"]').val();

    $.messager.confirm('确定', '确定要删除吗？', function (r) {
        if (r) {
            $.ajax({
                url: 'base/delete_perm_user',
                type: 'POST',
                data: {
                    permGroupId: permGroupId,
                    userId: userId,
                    deptId: deptId,
                    code: $('#permCode').val()
                },
                success: function () {
                    $.messager.alert('提示', '删除成功！', 'info', function () {
                        $('#permUserDataGridTable').datagrid('reload');
                    });
                },
                error: function (data) {
                    AjaxErrorHandler(data);
                }
            });
        }
    });
}

function openUpdatePermDialog(index) {
    closeAllDialog();
    var rowData = $('#dataGridTable').datagrid('getRows');
    $('#updatePermDialog input[name="id"]').val(rowData[index].id);
    $('#updatePermDialog input[name="code"]').val(rowData[index].code);
    $('#updatePermDialog input[name="chsName"]').val(rowData[index].chsName);
    $('#updateRemark').val(rowData[index].remark);
    $('#updatePermDialog').dialog('center').dialog('open');
}

function openUpdatePermItemDialog(index) {
    var rowData = $('#permItemDataGridTable').datagrid('getRows');
    $('#updatePermItemDialog input[name="id"]').val(rowData[index].id);
    $('#updatePermItemDialog input[name="code"]').val(rowData[index].code);
    $('#updatePermItemDialog input[name="chsName"]').val(rowData[index].chsName);
    $('#updateMenuPermName').combobox('setValue', rowData[index].menuPermName);
    $('#updatePermItemDialog').dialog('center').dialog('open');
}

function openMenuPermListDialog(id, chsName) {
    $.ajax({
        url: 'base/find_menu_perm_list_by_id',
        type: 'POST',
        data: {id: id},
        success: function (data) {
            closeAllDialog();
            $('#menuPermListDialog input[name="permGroupId"]').val(id);
            for (i = 0; i < data.length; i++) {
                node = $('#menuPermTree').tree('find', data[i]);
                if (node != null && !node.children)
                    $('#menuPermTree').tree('check', node.target);
            }
            $('#menuPermListDialog').dialog({title: chsName}).dialog('center').dialog('open');
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function openPsersonMenuPermItemListDialog(id, deptId) {
    var nodes = $('#personMenuPermItemTree').tree('getChecked');
    for (var i = 0; i < nodes.length; i++) {
        $('#personMenuPermItemTree').tree('uncheck', nodes[i].target);
    }

    $.ajax({
        url: 'base/find_person_perm_itemlist_by_userid',
        type: 'POST',
        data: {
            userId: id,
            deptId: deptId
        },
        success: function (data) {
            $('#psersonMenuPermItemListDialog input[name="personId"]').val(id);
            $('#psersonMenuPermItemListDialog input[name="deptId"]').val(deptId);
            for (i = 0; i < data.length; i++) {
                node = $('#personMenuPermItemTree').tree('find', data[i]);
                if (node != null && !node.children)
                    $('#personMenuPermItemTree').tree('check', node.target);
            }
            $('#psersonMenuPermItemListDialog').dialog('center').dialog('open');
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function openSetPermissionDialog(id, chsName) {
    $.ajax({
        url: 'base/find_menu_perm_item_list_by_id',
        type: 'POST',
        data: {id: id},
        success: function (data) {
            closeAllDialog();
            $('#menuPermItemListDialog input[name="permGroupId"]').val(id);
            for (i = 0; i < data.length; i++) {
                node = $('#menuPermItemTree').tree('find', data[i]);
                if (node != null && !node.children)
                    $('#menuPermItemTree').tree('check', node.target);
            }
            $('#menuPermItemListDialog').dialog({title: chsName}).dialog('center').dialog('open');
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function openPermUserListDialog(id, code) {
    closeAllDialog();
    $('#permUserListDialog input[name="permGroupId"]').val(id);
    $('#permCode').val(code);
    $('#permUserDataGridTable').datagrid('load', {permGroupId: id});
    $('#permUserListDialog').dialog('center').dialog('open');
}

function openCreatePermDialog() {
    closeAllDialog();
    $('#createPermDialog').dialog('center').dialog('open');
}

function openCreatePermItemListDialog() {
    closeAllDialog();
    $('#permItemListDialog').dialog('center').dialog('open');
}

/***
 * 设置角色权限
 * @returns
 */
function setMenuPermItemListSubmit() {
    var set = new Set();
    var nodes = $('#menuPermItemTree').tree('getChecked');

    for (var node in nodes) {
        if (!nodes[node].children) {
            set.add(nodes[node].id);
        }
    }

    var permGroupId = $('#menuPermItemListDialog input[name="permGroupId"]').val();
    var permItemIdArray = Array.from(set);
    $.ajax({
        url: 'base/add_perm_item_list',
        type: 'POST',
        data: {
            permItemIdArray: JSON.stringify(permItemIdArray),
            permGroupId: permGroupId
        },
        success: function () {
            $.messager.alert('提示', '添加成功！', 'info', function () {
                $('#dataGridTable').datagrid('reload');
            });
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

/***
 * 设置菜单权限
 * @returns
 */
function setMenuPermListSubmit() {
    var set = new Set();
    var nodes = $('#menuPermTree').tree('getChecked');

    for (var node in nodes) {
        var parentNode = $('#menuPermTree').tree('getParent', nodes[node].target);
        if (parentNode)
            set.add(parentNode.id);
        set.add(nodes[node].id);
    }

    var permGroupId = $('#menuPermListDialog input[name="permGroupId"]').val();
    var menuIdArray = Array.from(set);
    $.ajax({
        url: 'base/add_menuid_to_menulist',
        type: 'POST',
        data: {
            menuIdArray: JSON.stringify(menuIdArray),
            permGroupId: permGroupId
        },
        success: function () {
            $.messager.alert('提示', '变更成功！', 'info', function () {
                $('#dataGridTable').datagrid('reload');
            });
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function openAddUserToPermGroupDialog() {
    $('#addUserToPermGroupDialog input[name="permGroupId"]').val($('#permUserListDialog input[name="permGroupId"]').val());
    $('#deptIdAddCombobox').combobox('clear');
    $('#userIdAddCombobox').combobox('clear');
    $('#addUserToPermGroupDialog').dialog('center').dialog('open');
}

function setPersonMenuPermItemListSubmit() {
    var permGroupId = $('#permUserListDialog input[name="permGroupId"]').val();
    var personId = $('#psersonMenuPermItemListDialog input[name="personId"]').val();
    var deptId = $('#psersonMenuPermItemListDialog input[name="deptId"]').val();

    var set = new Set();
    var nodes = $('#personMenuPermItemTree').tree('getChecked');

    for (var node in nodes) {
        if (!nodes[node].children) {
            set.add(nodes[node].id);
        }
    }
    var itemIdArray = Array.from(set);

    $.ajax({
        url: 'base/add_item_to_pserson_itemlist',
        type: 'POST',
        data: {
            itemIdArray: JSON.stringify(itemIdArray),
            personId: personId,
            deptId: deptId
        },
        success: function () {
            $.messager.alert('提示', '添加成功！', 'info', function () {
                $('#dataGridTable').datagrid('reload');
            });
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function addUserToPermGroupSubmit() {
    var permGroupId = $('#addUserToPermGroupDialog input[name="permGroupId"]').val();
    var deptId = $('#deptIdAddCombobox').combo('getValue');
    var userId = $('#userIdAddCombobox').combo('getValue');

    $.ajax({
        url: 'base/set_perm_user',
        type: 'POST',
        data: {
            permGroupId: permGroupId,
            deptId: deptId,
            userId: userId,
            code: $('#permCode').val()
        },
        success: function () {
            $.messager.alert('提示', '添加成功！', 'info');
            $('#permUserDataGridTable').datagrid('reload');
            $('#addUserToPermGroupDialog').dialog('close');
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function closeAllDialog() {
    //角色组
    $('#createPermDialog').dialog('close');
    $('#updatePermDialog').dialog('close');

    //角色权限组
    $('#permItemListDialog').dialog('close');
    $('#createPermItemDialog').dialog('close');
    $('#updatePermItemDialog').dialog('close');

    //菜单权限
    var roots = $('#menuPermTree').tree('getRoots');
    for (var i = 0; i < roots.length; i++) {
        var node = $('#menuPermTree').tree('find', roots[i].id);
        $('#menuPermTree').tree('uncheck', node.target);
    }
    $('#menuPermListDialog').dialog('close');

    //角色权限
    var nodes = $('#menuPermItemTree').tree('getChecked');
    for (var i = 0; i < nodes.length; i++) {
        $('#menuPermItemTree').tree('uncheck', nodes[i].target);
    }
    $('#menuPermItemListDialog').dialog('close');

    //角色用户
    $('#permUserListDialog').dialog('close');
}

function searchByQuerys() {
    $('#permUserDataGridTable').datagrid('load', {
        permGroupId: $('#permUserListDialog input[name="permGroupId"]').val(),
        q: $('#userId').val()
    });
}

$(function () {
    var permGroupId = $('#permUserListDialog input[name="permGroupId"]').val();

    //权限组
    $('#dataGridTable').datagrid({
        url: 'base/permission_datagrid',
        method: 'POST',
        fit: true,
        striped: true,
        rowNumbers: true,
        singleSelect: true,
        pagination: true,
        pageSize: 20,
        title: '角色权限管理',
        toolbar: '#dataGridTableButtons',
        columns: [[
            {
                field: 'id', title: '操作', formatter: function (value, row, index) {
                    return '<a href="javascript:void(0);" onClick="openUpdatePermDialog(' + index + ');" style="margin-right: 5px;">修改</a>'
                        + '<a href="javascript:void(0);" onClick="openMenuPermListDialog(\'' + value + '\',\'菜单权限 ' + row.chsName + '\');" style="margin-right: 5px;">菜单权限</a>'
                        + '<a href="javascript:void(0);" onClick="openSetPermissionDialog(\'' + value + '\',\'角色权限 ' + row.chsName + '\');" style="margin-right: 5px;">角色权限</a>'
                        + '<a href="javascript:void(0);" onClick="openPermUserListDialog(\'' + value + '\', \'' + row.code + '\');" style="margin-right: 5px;">角色用户</a> ';
                }
            },
            {field: 'code', title: '角色编码'},
            {field: 'chsName', title: '角色名称'},
            {field: 'remark', title: '备注'}
        ]]
    });

    //权限项
    $('#permItemDataGridTable').datagrid({
        url: 'base/perm_item_datagrid',
        method: 'POST',
        striped: true,
        rowNumbers: true,
        singleSelect: true,
        pagination: true,
        pageSize: 10,
        fit: true,
        toolbar: '#permItemDataGridTableButtons',
        columns: [[
            {
                field: 'id', title: '操作', formatter: function (value, row, index) {
                    return '<a href="javascript:void(0);" onClick="openUpdatePermItemDialog(' + index + ');" style="margin-right: 5px;">修改</a>'
                        + '<a href="javascript:void(0);" onClick="deletePermItem(\'' + value + '\');" style="margin-right: 5px;">删除</a>';
                }
            },
            {field: 'code', title: '角色权限编码'},
            {field: 'chsName', title: '角色权限名称'},
            {field: 'menuPermName', title: '菜单权限名称'},

        ]]
    });

    //权限组用户
    $('#permUserDataGridTable').datagrid({
        url: 'base/perm_user_datagrid',
        method: 'POST',
        fit: true,
        striped: true,
        rowNumbers: true,
        pagination: true,
        singleSelect: true,
        pageSize: 10,
        loadFilter: pagerFilter,
        toolbar: '#permUserDataGridTableButtons',
        queryParams: {permGroupId: permGroupId},
        columns: [[
            {
                field: 'id', title: '操作', formatter: function (value, row, index) {
//                return '<a href="javascript:void(0);" onClick="openPsersonMenuPermItemListDialog(\''+value+'\',\''+row.permDeptId+'\');" style="margin-right: 5px;">个人权限</a>'
                    return '<a href="javascript:void(0);" onClick="deletePermUser(\'' + value + '\',\'' + row.permDeptId + '\');" style="margin-right: 5px;">删除</a>';
                }
            },
            {field: 'permDeptName', title: '部门名称'},
            {field: 'chsName', title: '中文名'}
//            {field: 'manager', title: '资产管理员', formatter: function(value){
//            	if(value)
//            		return "是";
//            	else
//            		return "否";
//            }},
//            {field: 'leader', title: '部门领导', formatter: function(value){
//            	if(value)
//            		return "是";
//            	else
//            		return "否";
//            }},
        ]]
    });

    $('#menuPermTree').tree({
        url: 'base/menu_perm_tree',
        checkbox: true,
        onClick: function (node) {
            if (node.checked)
                $('#menuPermTree').tree('uncheck', node.target);
            else
                $('#menuPermTree').tree('check', node.target);
        }
    });

    $('#menuPermItemTree').tree({
        url: 'base/menu_perm_item_tree',
        checkbox: true,
        onClick: function (node) {
            if (node.checked)
                $('#menuPermItemTree').tree('uncheck', node.target);
            else
                $('#menuPermItemTree').tree('check', node.target);
        }
    });

    $('#personMenuPermItemTree').tree({
        url: 'base/menu_perm_item_tree',
        checkbox: true,
        onClick: function (node) {
            if (node.checked)
                $('#personMenuPermItemTree').tree('uncheck', node.target);
            else
                $('#personMenuPermItemTree').tree('check', node.target);
        }
    });

    $('#deptIdAddCombobox').combobox({
        url: 'base/dept_combo',
        prompt: '输入关键字自动检索',
        required: false,
        editable: true,
        hasDownArrow: true,
        filter: function (q, row) {
            var opts = $(this).combobox('options');
            return row[opts.textField].indexOf(q) > -1;
        },

        onSelect: function (record) {
            $.ajax({
                url: 'base/dept_user_combobox',
                type: 'POST',
                data: {deptId: record.value},
                success: function (data) {
                    $('#userIdAddCombobox').combobox('clear');
                    $('#userIdAddCombobox').combobox('loadData', data);
                },
                error: function (data) {
                    AjaxErrorHandler(data);
                }
            });
        }
    });
})
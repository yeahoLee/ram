/**
 * 重置
 */
function resetQuerys() {
    $("#userGroupSearchForm").form("clear");
}

/**
 * 搜索
 */
function searchByQuerys() {
    var qName = $("#qName").val();
    $("#userGroupTable").datagrid({
        queryParams : {
            q : qName
        }
    });
}

/**
 * 打开新增用户组dialog
 */
function openAddUserGroupDialog() {
    $("#addUserGroupForm").form("clear");
    $("#addUserGroupDialog").dialog("open");
}

/**
 * 提交新增/修改用户组
 */
function createUserGroup() {
    var id = $("#id").val();
    var groupCode = $("#groupCode").val();
    var groupName = $("#groupName").val();
    var groupDescription = $("#groupDescription").val();

    if ($.trim(groupCode) == "" || $.trim(groupName) == "" || $.trim(groupDescription) == "") {
        $.messager.alert("错误","必填项不能为空！","error", function () {
            return;
        })
    } else {
        $.ajax({
            url : "flowable/create_user_group",
            type : "POST",
            dataType : "json",
            contentType : "application/json",
            data : JSON.stringify({
                id : id,
                groupCode : groupCode,
                groupName : groupName,
                groupDescription : groupDescription
            }),
            success : function (data) {
                $("#userGroupTable").datagrid("reload");
                $("#addUserGroupDialog").dialog("close");
            },
            error : function (data) {
                AjaxErrorHandler(data);
            }
        })
    }
}

/**
 * 删除用户组
 */
function deleteUsergroup(id) {
    $.messager.confirm('提示', '是否删除', function(r){
        if(r){
            $.ajax({
                type: 'POST',
                url: 'flowable/delete_user_group',
                data: {
                    id : id,
                    _method : "DELETE"
                },
                success: function(data){
                    $("#userGroupTable").datagrid("reload");
                },
                error: function(data){
                    AjaxErrorHandler(data);
                }
            });
        }
    });
}

/**
 * 打开修改dialog
 * @param index
 */
function openUpdateDialog(index) {
    $("#addUserGroupForm").form("clear");
    var rows = $('#userGroupTable').datagrid("getRows");
    var row = rows[index];
    $("#addUserGroupForm").form("load", row);

    $("#addUserGroupDialog").dialog("open");
}

/**
 * 打开添加用户dialog
 */
function openAddUser(groupId) {
    $("#groupId").val(groupId);
    $("#userDataGridTable").datagrid("load", {groupId : groupId});
    /**
     * 移除dialog阴影
     */
    $(".window-shadow").remove();

    $("#userId").combobox("setValue", "");
    $("#deptIdAddCombobox").combotree("setValue", "");
    $("#userDialog").dialog("open");
}

function addUser() {
    var groupId = $("#groupId").val();
    var userCode = $("#userId").combobox("getValue");

    if ($.trim(groupId) == "" || $.trim(userId) == "") {
        $.messager.alert("错误","必填项不能为空！","error", function () {
            return;
        })
    } else {
        $.ajax({
            url : "flowable/create_user",
            type : "POST",
            dataType : "json",
            contentType: "application/json",
            data : JSON.stringify({
                groupId : groupId,
                userCode : userCode
            }),
            success : function (data) {
                $("#userDataGridTable").datagrid("reload");
            },
            error : function (data) {
                AjaxErrorHandler(data);
            }
        })
    }
}

/**
 * 删除用户
 * @param id
 */
function deleteUser(id) {
    $.ajax({
        type: 'POST',
        url: 'flowable/delete_user',
        data: {
            id : id,
            _method : "DELETE"
        },
        success: function(data){
            $("#userDataGridTable").datagrid("reload");
        },
        error: function(data){
            AjaxErrorHandler(data);
        }
    })
}

$(function () {
    $('#userGroupTable').datagrid({
        url: 'flowable/user_group_list',
        fit: true,
        striped: true,
        method: 'POST',
        rownumbers: true,
        // pagination: true,
        // pageSize: 20,
        title: '流程管理',
        toolbar: '#addButton',
        loadMsg: '程序处理中，请稍等...',
        columns:[[
            {field: 'id',title: '操作', align: 'center',formatter:function (value, row, index) {
                    var addUser = '<a href="javascript:void(0);" onClick="openAddUser(\'' + value + '\');">添加用户</a> ';
                    var updateUserGroup = '<a href="javascript:void(0);" onClick="openUpdateDialog(\'' + index + '\');">修改</a> ';
                    var deleteUserGroup = '<a href="javascript:void(0);" onClick="deleteUsergroup(\'' + value + '\');">删除</a> ';
                    return addUser + updateUserGroup + deleteUserGroup;
                }},
            {field: 'groupCode',title: '编码', align: 'center'},
            {field: 'groupName',title: '名称', align: 'center'},
            {field: 'groupDescription',title: '描述', align: 'center'},
            {field: 'createTimestampStr',title: '创建时间', align: 'center'}
        ]],
        onLoadError:function(data){
            AjaxErrorHandler(data);
        }
    });

    $("#userDataGridTable").datagrid({
        url: 'flowable/user_list',
        striped: true,
        method: 'POST',
        rownumbers: true,
        queryParams: {groupId :""},
        title: '用户',
        loadMsg: '程序处理中，请稍等...',
        columns:[[
            {field: 'id',title: '操作', align: 'center',formatter:function (value, row, index) {
                    var deleteUser = '<a href="javascript:void(0);" onClick="deleteUser(\'' + value + '\');">删除</a> ';
                    return deleteUser;
                }},
            {field: 'userName',title: '用户名', align: 'center'},
        ]],
        onLoadError:function(data){
            AjaxErrorHandler(data);
        }
    });

    $('#deptIdAddCombobox').combotree({
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


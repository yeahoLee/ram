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
    var managerId = $('#UserID').val();
    $('#dataGridTable').datagrid('load', {
        produceTypeStr: $('#produceType').combobox("getValue"),
        assetCode: $('#searchQuerys input[name="assetCode"]').val(),
        assetChsName: $('#searchQuerys input[name="assetChsName"]').val(),
        assetStatus: defaulState,
        managerId: managerId,
        showType: "1",
        useDeptId: $('#useDeptId').combotree("getValue"),
        userId: $('#userId').combobox("getValue")
    })
}

function addAssetToList() {
    var assetIdList = new Array();
    var data = $('#dataGridTable').datagrid('getSelections');

    if (data.length == 0)
        $.messager.alert('错误', '选择一个添加项！', 'error');

    var v = $('#produceType').combobox("getValue");
    $('#produceType').combobox({
        disabled: true,
        value: v
    });

    for (var row in data) {
        assetIdList.push(data[row].id);
    }

    //累加
    var inputVar = $('#assetList input[name="assetIdList"]').val();
    if (inputVar)
        $('#assetList input[name="assetIdList"]').val(inputVar + "," + assetIdList);
    else
        $('#assetList input[name="assetIdList"]').val(assetIdList);
    //查询
    $('#batchUpdateDataGridTable').datagrid('load', {
        assetIdListStr: $('#assetList input[name="assetIdList"]').val()
    })
    //获取去重后的idList
    data = $('#batchUpdateDataGridTable').datagrid('getData');
    for (var row in data.rows) {
        if (contains(assetIdList, data.rows[row].id) == false)
            assetIdList.push(data.rows[row].id);
//		if(assetIdList.indexOf(data.rows[row].id)==-1){//当前input去重
//			assetIdList.push(data.rows[row].id);
//		}	
    }
    $('#assetList input[name="assetIdList"]').val(assetIdList);
}

function removeAssetToList() {
    var assetIdList = new Array();
    var data = $('#batchUpdateDataGridTable').datagrid('getSelections');

    if (data.length == 0)
        $.messager.alert('错误', '选择一个移除项！', 'error');

    //删除选中行
    for (var i = data.length - 1; i >= 0; i--) {
        var rowIndex = $('#batchUpdateDataGridTable').datagrid('getRowIndex', data[i]);
        $('#batchUpdateDataGridTable').datagrid('deleteRow', rowIndex);
    }

    //获取剩余行
    data = $('#batchUpdateDataGridTable').datagrid('getData');
    for (var row in data.rows) {
        assetIdList.push(data.rows[row].id);
    }
    //list转Str查询
    $('#assetList input[name="assetIdList"]').val(assetIdList);
    $('#batchUpdateDataGridTable').datagrid('load', {
        assetIdListStr: $('#assetList input[name="assetIdList"]').val()
    })

    $.messager.alert('提示', '删除成功！', 'info', function () {
        var data1 = $('#batchUpdateDataGridTable').datagrid('getData');
        if (data1.rows == 0) {
            $('#produceType').combobox({
                disabled: false
            });
        }
    });
}

function updateChangeUser() {
    var id = $('#AssetUserChangeId').val();
    var assetUserId = $('#assetUserId').combobox('getValue');
    var reason = $('#reason').val();
    var assetIdListStr = $('#assetList input[name="assetIdList"]').val();
    var produceType = $('#produceType').combobox("getValue");
    if (produceType == null || produceType == "") {
        $.messager.alert('错误', '请先选择物资的类型！', 'error');
        return;
    }
    $.ajax({
        url: 'assetChange/update_change_user',
        type: 'POST',
        data: {
            id: id,
            assetUserId: assetUserId,
            reason: reason,
            assetIdListStr: assetIdListStr,
            produceTypeStr: produceType
        },
        success: function (data) {
            $('#saveBtn').removeAttr('disabled');
            $.messager.alert('提示', '保存成功！', 'info', function () {

            });
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function openAssetStanBookDialog() {
    var produceType = $('#produceType').combobox("getValue");
    if (produceType == null || produceType == "") {
        $.messager.alert('错误', '请先选择物资的类型！', 'error');
        return;
    }
    defaultSearch();
    $('#assetStanBookDialog').dialog('center').dialog('open');
}

$(function () {
    fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_STATUS', 'assetStatus');
    fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_PRODUCE_TYPE', 'produceType');
    $('#produceType').combobox({
        disabled: true,
        value: $('#type').val()
    });


    var managerId = $('#CreateUserID').val();
    var assetIdList = $('#assetList input[name="assetIdList"]').val();
    $('#batchUpdateDataGridTable').datagrid({
        url: 'asset/asset_batch_update_datagrid',
        method: 'POST',
        striped: true,
        rownumbers: true,
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {assetIdListStr: assetIdList},
        columns: [[
            {field: 'id', checkbox: true},
            {
                field: 'assetCode', title: '资产编码', formatter: function (value, row, index) {
                    return '<a onclick="goToViewAssetPageSubmit(\'' + row.id + '\')">' + value + '</a>';
                }
            },
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
            //延申信息
            {field: 'companyStr', title: '所属公司'},
            //{field: 'assetLineStr', title: '所属线路/建筑'},
            {field: 'belongLineStr', title: '所属线路/建筑'},
            {field: 'buyDate', title: '购置日期'},
            {field: 'manageDeptStr', title: '主管部门'},
            {field: 'managerStr', title: '资产管理员'},
            {field: 'useStr', title: '资产使用人'},
            //安装未写
            {field: 'assetSource', title: '资产来源'},
            {field: 'contractNum', title: '合同编号'},
            {field: 'tendersNum', title: '标段编号'},
            {field: 'mainPeriod', title: '维保期'},
            {field: 'sourceUser', title: '联系人'},
            {field: 'sourceContactInfo', title: '联系方式'},
            {field: 'prodTime', title: '出厂日期'}
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });

    var assetIdList = $('#assetIdList').val();
    $('#batchUpdateDataGridTable').datagrid('load', {
        assetIdListStr: assetIdList
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

    $('#dataGridTable').datagrid({
        url: 'asset/asset_datagrid',
//			fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        pagination: true,
        pageSize: 10,
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {
            managerId: managerId,
            assetStatus: 0,
            manageDeptId: $('#DeptID').val()
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
            {field: 'useDeptStr', title: '使用部门'},
            {field: 'useStr', title: '使用人'},
            {field: 'manageDeptStr', title: '主管部门'},
            {field: 'managerStr', title: '资产管理员'},
            {field: 'belongLineStr', title: '所属线路'},
            {field: 'assetStatus', title: '资产状态'}

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

function goToViewAssetPageSubmit(assetId) {
    window.open('asset_view?assetId=' + assetId);
}
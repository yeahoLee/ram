var isDialogInit = false;
var other='8';
var cut=','
var stateList = "6";
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
        assetCode: '',
        assetChsName: '',
        useDeptId: '',
        userId: '',
        assetStatus: defaulState,
        showType: "1",
        managerId :$('#UserID').val()
    })
}

function searchByQuerys() {
    $('#dataGridTable').datagrid('load', {
        produceTypeStr: $('#produceType').combobox("getValue"),
        assetCode: $('#searchQuerys input[name="assetCode"]').val(),
        assetChsName: $('#searchQuerys input[name="assetChsName"]').val(),
        useDeptId: $('#useDeptId').combotree("getValue"),
        userId: $('#userId').combobox("getValue"),
        assetStatus: defaulState,
        showType: "1",
        managerId :$('#UserID').val()
    })
}

function openAssetStanBookDialog() {
    var produceType = $('#produceType').combobox("getValue");
    if (produceType == null || produceType == "") {
        $.messager.alert('错误', '请先选择物资的类型！', 'error');
        return;
    }
    defaultSearch();
    $('#assetListDialog').dialog('center').dialog('open');
}

//添加资产
function addAssetToList() {
    var data = $('#dataGridTable').datagrid('getSelections');
    if (data.length == 0) {
        $.messager.alert('错误', '选择一个添加项！', 'error');
        return;
    }
    var assetList = data;
    var AssetSealTempDtoList = [];

    if (assetList.length > 0) {
        for (var i = 0; i < assetList.length; i++) {
            var AssetSequestrationTempDto = {};
            AssetSequestrationTempDto.assetId = assetList[i].id;
            AssetSealTempDtoList.push(AssetSequestrationTempDto);
        }
    }
    var AssetSequestrationTempDtoList = "";
    var id = $('#id').val();

    if (AssetSealTempDtoList.length > 0)
        var AssetSequestrationTempDtoList = JSON.stringify(AssetSealTempDtoList);

    $.ajax({
        url: 'assetSequestration/assetseal_update_asset',
        type: 'POST',
        data: {
            assetSequestrationTempDtoList: AssetSequestrationTempDtoList,
            id: id
        },
        success: function (data) {
            $('#saveBtn').removeAttr('disabled');
            /*$.messager.alert('提示', '添加成功！', 'info',function(){
                //window.location.href='assetsequestration_query';
            });*/
            $('#assetSequestrationSealDataGrid').datagrid('reload');
            $('#dataGridTable').datagrid('reload');
        },
        error: function (data) {
            $('#saveBtn').removeAttr('disabled');
            AjaxErrorHandler(data);
        }
    })
}

//删除资产
function removeAssetToList() {
    var data = $('#assetSequestrationSealDataGrid').datagrid('getSelections');
    if (data.length == 0) {
        $.messager.alert('错误', '选择一个移除项！', 'error');
        return false;
    }
    var assetList = data;
    var AssetSealTempDtoList = [];
    for (var i = 0; i < assetList.length; i++) {
        var AssetSequestrationTempDto = {};
        AssetSequestrationTempDto.Id = assetList[i].id;
        AssetSequestrationTempDto.assetId = assetList[i].assetId;
        AssetSealTempDtoList.push(AssetSequestrationTempDto);
    }
    var AssetSequestrationTempDtoList = "";
    var id = $('#id').val();

    if (AssetSealTempDtoList.length > 0)
        var AssetSequestrationTempDtoList = JSON.stringify(AssetSealTempDtoList);
    $.ajax({
        url: 'assetSequestration/assetseal_delete_asset',
        type: 'POST',
        data: {
            assetSequestrationTempDtoList: AssetSequestrationTempDtoList,
            id: id
        },
        success: function (data) {
            $('#saveBtn').removeAttr('disabled');
            /*$.messager.alert('提示', '删除成功！', 'info',function(){
                //window.location.href='assetsequestration_query';
            });*/
            $('#assetSequestrationSealDataGrid').datagrid('reload');
            $('#dataGridTable').datagrid('reload');
        },
        error: function (data) {
            $('#saveBtn').removeAttr('disabled');
            AjaxErrorHandler(data);
        }
    })
    /*// 删除选中行
    for (var i = data.length - 1; i >= 0; i--) {
        var rowIndex = $('#assetSequestrationSealDataGrid').datagrid('getRowIndex',data[i]);
        $('#assetSequestrationSealDataGrid').datagrid('deleteRow', rowIndex);
    }*/
}

function openDialogForSavePlace() {
    var data = $('#assetSequestrationSealDataGrid').datagrid('getSelections');
    if (data.length == 0) {
        $.messager.alert('错误', '选择一个资产项！', 'error');
    }

    if (!isDialogInit) {
        $('#instSiteCodeDialogFrame').attr('src', 'asset_save_place');
        isDialogInit = true;
    }
    $('#instSiteCodeDialog').dialog('center').dialog('open');
}

function showCodeAndName(id, codeAndName) {
    var assetList = $('#assetSequestrationSealDataGrid').datagrid('getSelections');
    var index = "";
    for (var i = 0; i < assetList.length; i++) {
        assetList[i].savePlaceId = id;
        assetList[i].savePlaceStr = codeAndName;
        index = $('#assetSequestrationSealDataGrid').datagrid('getRowIndex', assetList[i]);
        $('#assetSequestrationSealDataGrid').datagrid('updateRow', {index: index, row: assetList[i]}); // 将数据绑定到datagrid
    }
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

//保存 更新封存资产单  仅更新封存单，不更新资产
function saveAssetSealSubmit() {
    $('#saveBtn').attr('disabled', "true");
    var reason = $('#reason').val();
    var id = $('#id').val();
    var data = {};
    var sealPlaceId = $('#oldAssetSavePlaceId').val();
    if (sealPlaceId == "" || sealPlaceId == null) {
        $.messager.alert('错误', '请选择封存位置!', 'error');
        return;
    }
    if (reason == "" || reason == null) {
        $.messager.alert('错误', '请填写封存原因！', 'error');
        return;
    }
    var produceType = $('#produceType').combobox("getValue");
    if (produceType == null || produceType == "") {
        $.messager.alert('错误', '请先选择物资的类型！', 'error');
        return;
    }
    data.id = id;
    data.sealReason = reason;
    data.sealPlaceId = sealPlaceId;
    data.produceTypeStr = produceType;

    $.ajax({
        url: 'assetSequestration/assetseal_update',
        type: 'POST',
        data: data,
        success: function (data) {
            $('#saveBtn').removeAttr('disabled');
            $.messager.alert('提示', '保存成功！', 'info', function () {
                window.location.href = 'assetsequestration_query';
            });
            $('#dataGridTable').datagrid('reload');
        },
        error: function (data) {
            $('#saveBtn').removeAttr('disabled');
            AjaxErrorHandler(data);
        }
    });
}

//保存并发起审批
function saveAndCheckSeal() {
    $('#saveBtn').attr('disabled', "true");
    var reason = $('#reason').val();
    var id = $('#id').val();
    var data = {};
    var sealPlaceId = $('#oldAssetSavePlaceId').val();
    if (sealPlaceId == "" || sealPlaceId == null) {
        $.messager.alert('错误', '请选择封存位置!', 'error');
        return;
    }
    if (reason == "" || reason == null) {
        $.messager.alert('错误', '请填写封存原因！', 'error');
        return;
    }
    data.id = id;
    data.sealReason = reason;
    data.sealPlaceId = sealPlaceId;
    data.approve = 'yes';

    $.ajax({
        url: 'assetSequestration/assetseal_update',
        type: 'POST',
        data: data,
        success: function (data) {
            $('#saveBtn').removeAttr('disabled');
            $.messager.alert('提示', '保存成功！', 'info', function () {
                window.location.href = 'assetsequestration_query';
            });
        },
        error: function (data) {
            $('#saveBtn').removeAttr('disabled');
            AjaxErrorHandler(data);
        }
    });
}

function showCodeAndName(id, codeAndName) {
    $('#showCodeAndData').text(codeAndName);
    $('#oldAssetSavePlaceId').val(id);
}

$(function () {
    fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_STATUS', 'assetStatus');
    fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_PRODUCE_TYPE', 'produceType');
    var type = $('#type').val();
    $('#produceType').combobox('select', type);
    //var assetIdList = $('#assetList input[name="assetIdList"]').val();
    var managerId = $('#UserID').val();
    var data = {};
    data.id = $('#id').val();

    $.ajax({
        url: 'assetSequestration/assetseal_queryById',
        type: 'POST',
        data: data,
        success: function (data) {
            if (data) {
                $('#showCodeAndData').html(data.sealPlaceStr);
                $('#oldAssetSavePlaceId').val(data.sealPlaceId);
                $('#reason').val(data.sealReason);
            }
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

    $('#assetSequestrationSealDataGrid').datagrid({
        url: 'assetSequestration/assetseal_update_datagrid',
        // fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        // pagination: true,
        // pageSize: 10,
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        queryParams: data,
        columns: [[
            {field: 'id', checkbox: true},
            {field: 'assetCode', title: '资产编码'},
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
            //managerId : managerId,
            deActiveTimeStr: 1,
            assetStatus: 6
        },
        columns: [[
            {field: 'id', checkbox: true},
            {field: 'assetCode', title: '资产编码'},
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
            {field: "savePlaceStr", title: "使用位置"}
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
    $('#instSiteCodeDialog').dialog('close');
})

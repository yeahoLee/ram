function searchByQuerys() {
    var commonCodeType = $('#commonCodeType').combobox('getValue');

    $('#dataGridTable').datagrid('load', {
        q: $('#searchFilterText').val(),
        commonCodeType: $('#commonCodeType').combobox('getValue')
    })

    if (commonCodeType != 'ASSET_TYPE') {
        $('#dataGridTable').datagrid('showColumn', 'parentCodeName');
        $('#dataGridTable').datagrid('showColumn', 'engName');
        $('#dataGridTable').datagrid('showColumn', 'converCode');
        $('#dataGridTable').datagrid('hideColumn', 'W_PRO_CODE');
        $('#dataGridTable').datagrid('hideColumn', 'W_TYPE_CODE');
        $('#dataGridTable').datagrid('hideColumn', 'PRICE');
        $('#dataGridTable').datagrid('hideColumn', 'MARTERIALS_SPEC');
        $('#dataGridTable').datagrid('hideColumn', 'W_UNIT_CODE');
        $('#dataGridTable').datagrid('hideColumn', 'W_IS_PRO');
        $('#dataGridTable').datagrid('hideColumn', 'IS_DAN');
        $('#dataGridTable').datagrid('hideColumn', 'IS_DIRECT');
        $('#dataGridTable').datagrid('hideColumn', 'MARTERIALS_STATE');
        $('#dataGridTable').datagrid('hideColumn', 'BRAND_NAME');
        $('#dataGridTable').datagrid('hideColumn', 'EXPIRATION_DATE');
        $('#dataGridTable').datagrid('hideColumn', 'IS_DEL');
    } else {
        $('#dataGridTable').datagrid('hideColumn', 'parentCodeName');
        $('#dataGridTable').datagrid('hideColumn', 'engName');
        $('#dataGridTable').datagrid('hideColumn', 'converCode');
        $('#dataGridTable').datagrid('showColumn', 'W_PRO_CODE');
        $('#dataGridTable').datagrid('showColumn', 'W_TYPE_CODE');
        $('#dataGridTable').datagrid('showColumn', 'PRICE');
        $('#dataGridTable').datagrid('showColumn', 'MARTERIALS_SPEC');
        $('#dataGridTable').datagrid('showColumn', 'W_UNIT_CODE');
        $('#dataGridTable').datagrid('showColumn', 'W_IS_PRO');
        $('#dataGridTable').datagrid('showColumn', 'IS_DAN');
        $('#dataGridTable').datagrid('showColumn', 'IS_DIRECT');
        $('#dataGridTable').datagrid('showColumn', 'MARTERIALS_STATE');
        $('#dataGridTable').datagrid('showColumn', 'BRAND_NAME');
        $('#dataGridTable').datagrid('showColumn', 'EXPIRATION_DATE');
        $('#dataGridTable').datagrid('showColumn', 'IS_DEL');
    }
}

function openCreateCommonCodeDialog() {
    var commonCodeType = $('#commonCodeType').combobox('getValue');

    if (commonCodeType == 'PLACE_CODE_LV2')
        commonCodeType = 'PLACE_CODE_LV1';
    else if (commonCodeType == 'PLACE_CODE_LV4')
        commonCodeType = 'PLACE_CODE_LV3';
    else if (commonCodeType == 'DEVICE_CODE_LV2')
        commonCodeType = 'DEVICE_CODE_LV1';
    else if (commonCodeType == 'DEVICE_CODE_LV3')
        commonCodeType = 'DEVICE_CODE_LV2';
    else if (commonCodeType == 'DEVICE_CODE_LV4')
        commonCodeType = 'DEVICE_CODE_LV3';

    if (commonCodeType != 'ASSET_TYPE') {
        $('#parentDictCommonCodeNameInCreateDialog').combobox({
            url: 'base/common_combo?commonCodeType=' + commonCodeType,
            method: 'POST'
        });
        $('#createCommonCodeDialog').dialog('center').dialog('open');
    } else {
        $('#createMaterialsCodeDialog').dialog('center').dialog('open');
    }
}

function createCommonCodeSubmit() {
    var commonCodeType = $('#commonCodeType').combobox('getValue');
    var parentDictId = $('#parentDictCommonCodeNameInCreateDialog').combobox('getValue');
    var code = $('#createCommonCodeDialog input[name="code"]').val();
    var chsName = $('#createCommonCodeDialog input[name="chsName"]').val();
    var engName = $('#createCommonCodeDialog input[name="engName"]').val();
    var converCode = $('#createCommonCodeDialog input[name="converCode"]').val();

    $.ajax({
        url: 'base/create_common_code',
        type: 'POST',
        data: {
            typeKey: commonCodeType,
            parentDictId: parentDictId,
            code: code,
            chsName: chsName,
            engName: engName,
            converCode: converCode
        },
        success: function () {
            $('#createDataDialog').dialog('close');
            $.messager.alert('提示', '新建成功！', 'info', function () {
                $('#dataGridTable').datagrid('load', {commonCodeType: commonCodeType});
            });
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function createMaterialsCodeSubmit() {
    var commonCodeType = $('#commonCodeType').combobox('getValue');
    var code = $('#createMaterialsCodeDialog input[name="code"]').val();
    var chsName = $('#createMaterialsCodeDialog input[name="chsName"]').val();
    var W_PRO_CODE = $('#createMaterialsCodeDialog input[name="W_PRO_CODE"]').val();
    var W_TYPE_CODE = $('#createMaterialsCodeDialog input[name="W_TYPE_CODE"]').val();
    var PRICE = $('#createMaterialsCodeDialog input[name="PRICE"]').val();
    var MARTERIALS_SPEC = $('#createMaterialsCodeDialog input[name="MARTERIALS_SPEC"]').val();
    var W_UNIT_CODE = $('#createMaterialsCodeDialog input[name="W_UNIT_CODE"]').val();
    var W_IS_PRO = $('#createMaterialsCodeDialog input[name="W_IS_PRO"]:checked').val();
    var IS_DAN = $('#createMaterialsCodeDialog input[name="IS_DAN"]:checked').val();
    var IS_DIRECT = $('#createMaterialsCodeDialog input[name="IS_DIRECT"]:checked').val();
    var MARTERIALS_STATE = $('#MARTERIALS_STATE').combobox('getValue');
    var BRAND_NAME = $('#createMaterialsCodeDialog input[name="BRAND_NAME"]').val();
    var EXPIRATION_DATE = $('#createMaterialsCodeDialog input[name="EXPIRATION_DATE"]').val();
    var IS_DEL = $('#createMaterialsCodeDialog input[name="IS_DEL"]:checked').val();

    $.ajax({
        url: 'base/create_materials_code',
        type: 'POST',
        data: {
            typeKey: commonCodeType,
            code: code,
            name: chsName,
            W_PRO_CODE: W_PRO_CODE,
            W_TYPE_CODE: W_TYPE_CODE,
            PRICE: PRICE,
            MARTERIALS_SPEC: MARTERIALS_SPEC,
            W_UNIT_CODE: W_UNIT_CODE,
            W_IS_PRO: W_IS_PRO,
            IS_DAN: IS_DAN,
            IS_DIRECT: IS_DIRECT,
            MARTERIALS_STATE: MARTERIALS_STATE,
            BRAND_NAME: BRAND_NAME,
            EXPIRATION_DATE: EXPIRATION_DATE,
            IS_DEL: IS_DEL
        },
        success: function () {
            $('#createMaterialsCodeDialog').dialog('close');
            $.messager.alert('提示', '新建成功！', 'info', function () {
                $('#dataGridTable').datagrid('load', {commonCodeType: commonCodeType});
            });
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function createCommonCodeTypeSubmit() {
    var key = $('#createCommonCodeTypeDialog input[name="key"]').val();
    var name = $('#createCommonCodeTypeDialog input[name="name"]').val();

    $.ajax({
        url: 'base/create_dict_type',
        type: 'POST',
        data: {
            key: key,
            name: name
        },
        success: function () {
            $.messager.alert('提示', '新建成功！', 'info', function () {
                $('#commonCodeType').combobox('reload');
            });
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function openUpdateCommonCodeDialog(parentId, index) {
    var commonCodeType = $('#commonCodeType').combobox('getValue');

    if (commonCodeType == 'PLACE_CODE_LV2')
        commonCodeType = 'PLACE_CODE_LV1';
    else if (commonCodeType == 'PLACE_CODE_LV4')
        commonCodeType = 'PLACE_CODE_LV3';
    else if (commonCodeType == 'DEVICE_CODE_LV2')
        commonCodeType = 'DEVICE_CODE_LV1';
    else if (commonCodeType == 'DEVICE_CODE_LV3')
        commonCodeType = 'DEVICE_CODE_LV2';
    else if (commonCodeType == 'DEVICE_CODE_LV4')
        commonCodeType = 'DEVICE_CODE_LV3';

    $('#parentDictCommonCodeNameInUpdateDialog').combobox({
        url: 'base/common_combo?commonCodeType=' + commonCodeType,
        method: 'POST'
    });

    if (parentId != 'null')
        $('#parentDictCommonCodeNameInUpdateDialog').combobox('setValue', parentId);
    else
        $('#parentDictCommonCodeNameInUpdateDialog').combobox('setValue', '');

    var rowData = $('#dataGridTable').datagrid('getRows');
    $('#updateCommonCodeDialog input[name="id"]').val(rowData[index].id);
    $('#updateCommonCodeDialog input[name="code"]').val(rowData[index].code);
    $('#updateCommonCodeDialog input[name="chsName"]').val(rowData[index].chsName);
    $('#updateCommonCodeDialog input[name="engName"]').val(rowData[index].engName);
    $('#updateCommonCodeDialog input[name="converCode"]').val(rowData[index].converCode);
    $('#updateCommonCodeDialog').dialog('center').dialog('open');
}

function updateCommonCodeSubmit() {
    var id = $('#updateCommonCodeDialog input[name="id"]').val();
    var parentDictText = $('#parentDictCommonCodeNameInUpdateDialog').combobox('getText');
    var parentDictId = $('#parentDictCommonCodeNameInUpdateDialog').combobox('getValue');
    var code = $('#updateCommonCodeDialog input[name="code"]').val();
    var chsName = $('#updateCommonCodeDialog input[name="chsName"]').val();
    var engName = $('#updateCommonCodeDialog input[name="engName"]').val();
    var converCode = $('#updateCommonCodeDialog input[name="converCode"]').val();

    if (parentDictText == '')
        parentDictId = '';

    $.ajax({
        url: 'base/update_common_code',
        type: 'POST',
        data: {
            id: id,
            parentDictId: parentDictId,
            code: code,
            chsName: chsName,
            engName: engName,
            converCode: converCode
        },
        success: function () {
            $('#updateCommonCodeDialog').dialog('close');
            $.messager.alert('提示', '更新成功！', 'info', function () {
                $('#dataGridTable').datagrid('load', {commonCodeType: $('#commonCodeType').combobox('getValue')});
            });
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function importSavePlaceXls() {
    $('#importForm').form('submit', {
        url: 'base/import_save_place_xls',
        success: function (data) {
            var result = $.parseJSON(data);
            if (!result.errorMessage)
                $.messager.alert('提示', '上传成功!', 'info');
            else
                $.messager.alert('提示', '上传失败: ' + result.errorMessage, 'error');
        }
    });
}

function importFile() {
    $('#importFormm').form('submit', {
        url: 'base/import_dict',
        success: function (data) {
            var result = $.parseJSON(data);
            if (!result.errorMessage)
                $.messager.alert('提示', '上传成功!', 'info');
            else
                $.messager.alert('提示', '上传失败: ' + result.errorMessage, 'error');
        }
    });
}

$(function () {
    var commonCodeType = $('#commonCodeType').combobox('getValue');

    $('#dataGridTable').datagrid({
        url: 'base/common_datagrid',
        method: 'POST',
        fit: true,
        striped: true,
        rowNumbers: true,
        singleSelect: true,
        pagination: true,
        pageSize: 20,
        title: '数据字典',
        toolbar: '#dataGridTableButtons',
        queryParams: {
            commonCodeType: commonCodeType
        },
        columns: [[
            {
                field: 'id', title: '操作', formatter: function (value, row, index) {
                    return '<a href="javascript:void(0);" onClick="openUpdateCommonCodeDialog(\'' + row.parentCodeId + '\',' + index + ');">修改</a>';
                }
            },
            {field: 'parentCodeName', title: '父级编码'},
            {field: 'code', title: '编码'},
            {field: 'chsName', title: '中文名'},
            {field: 'engName', title: '英文名'},
            {field: 'converCode', title: '覆盖编码'},
            {field: 'w_PRO_CODE', title: '物资属性'},
            {field: 'w_TYPE_CODE', title: '物资类型'},
            {field: 'price', title: '参考单价'},
            {field: 'marterials_SPEC', title: '规格型号'},
            {field: 'w_UNIT_CODE', title: '计量单位'},
            {field: 'w_IS_PRO', title: '是否进设备台账'},
            {field: 'is_DAN', title: '是否危化品'},
            {field: 'is_DIRECT', title: '是否直发'},
            {field: 'marterials_STATE', title: '使用状态'},
            {field: 'brand_NAME', title: '品牌名称'},
            {field: 'expiration_DATE', title: '保质期'},
            {field: 'is_DEL', title: '删除标志'}
        ]]
    });

    $('#dataGridTable').datagrid('hideColumn', 'W_PRO_CODE');
    $('#dataGridTable').datagrid('hideColumn', 'W_TYPE_CODE');
    $('#dataGridTable').datagrid('hideColumn', 'PRICE');
    $('#dataGridTable').datagrid('hideColumn', 'MARTERIALS_SPEC');
    $('#dataGridTable').datagrid('hideColumn', 'W_UNIT_CODE');
    $('#dataGridTable').datagrid('hideColumn', 'W_IS_PRO');
    $('#dataGridTable').datagrid('hideColumn', 'IS_DAN');
    $('#dataGridTable').datagrid('hideColumn', 'IS_DIRECT');
    $('#dataGridTable').datagrid('hideColumn', 'MARTERIALS_STATE');
    $('#dataGridTable').datagrid('hideColumn', 'BRAND_NAME');
    $('#dataGridTable').datagrid('hideColumn', 'EXPIRATION_DATE');
    $('#dataGridTable').datagrid('hideColumn', 'IS_DEL');
})
function sendCodeAndName() {
    var row = $('#dataGridTable').datagrid('getSelected');

    if (!row) {
        $.messager.alert('错误', '请选择一行数据！');
    } else {
        $.ajax({
            url: 'base/find_asset_type_by_code',
            type: 'POST',
            dataType: 'json',
            data: {materialCode: row.code},
            success: function (data) {
                window.parent.showAssetTypeCodeAndName(row.code, data.assetTypeName);
                window.parent.closeAssetTypeDialog();
            },
            error: function (data) {
                AjaxErrorHandler(data);
            }
        });
    }
}

function getAssetTypeByCode() {
    var code = $('#deviceCodeLevel1').val()
        + $('#deviceCodeLevel2').val()
        + $('#deviceCodeLevel3').val()
        + $('#deviceCodeLevel4').val();
    $('#assetTypeCode').val(code);
    $('#dataGridTable').datagrid('reload', {code: code});
}

function initlevel1() {
    $("#deviceIdLevel1").combobox({
        url: 'base/common_combo?commonCodeType=DEVICE_CODE_LV1&showCode=true',
        method: 'POST',
        onLoadSuccess: function () { //加载完成后,设置选中第一项
            $(this).combobox('select', $(this).combobox('getData')[0]['value']);
        },
        onSelect: function (record) {
            $.ajax({
                url: 'base/common_combo_by_parentid',
                type: 'POST',
                data: {
                    parentId: record.value,
                    showCode: true
                },
                success: function (data) {
                    $('#deviceCodeLevel1').val(record.value1);
                    $('#deviceCodeLevel2').val('');
                    $('#deviceCodeLevel3').val('');
                    $('#deviceCodeLevel4').val('');
                    $('#deviceIdLevel2').combobox('clear');
                    $('#deviceIdLevel3').combobox('clear');
                    $('#deviceIdLevel4').combobox('clear');
                    $('#deviceIdLevel2').combobox('loadData', data);
                    getAssetTypeByCode();
                },
                error: function (data) {
                    AjaxErrorHandler(data);
                }
            });
        }
    });
}

$(function () {
    $('#dataGridTable').datagrid({
        url: 'base/get_assettype_datagrid',
        method: 'POST',
        fit: true,
        striped: true,
        rownumbers: true,
        singleSelect: true,
        pagination: true,
        pageSize: 10,
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        columns: [[
            {field: 'code', title: '编码'},
            {field: 'chsName', title: '中文名'}
        ]]
    });

    initlevel1();

    $("#deviceIdLevel2").combobox({
        onSelect: function (record) {
            $.ajax({
                url: 'base/common_combo_by_parentid',
                type: 'POST',
                data: {
                    parentId: record.value,
                    showCode: true
                },
                success: function (data) {
                    $('#deviceCodeLevel2').val(record.value1);
                    $('#deviceCodeLevel3').val('');
                    $('#deviceCodeLevel4').val('');
                    $('#deviceIdLevel3').combobox('clear');
                    $('#deviceIdLevel4').combobox('clear');
                    $('#deviceIdLevel3').combobox('loadData', data);
                    getAssetTypeByCode();
                },
                error: function (data) {
                    AjaxErrorHandler(data);
                }
            });
        }
    });

    $("#deviceIdLevel3").combobox({
        onSelect: function (record) {
            $.ajax({
                url: 'base/common_combo_by_parentid',
                type: 'POST',
                data: {
                    parentId: record.value,
                    showCode: true
                },
                success: function (data) {
                    $('#deviceCodeLevel3').val(record.value1);
                    $('#deviceCodeLevel4').val('');
                    $('#deviceIdLevel4').combobox('clear');
                    $('#deviceIdLevel4').combobox('loadData', data);
                    getAssetTypeByCode();
                },
                error: function (data) {
                    AjaxErrorHandler(data);
                }
            });
        }
    });

    $("#deviceIdLevel4").combobox({
        onSelect: function (record) {
            $('#deviceCodeLevel4').val(record.value1);
            getAssetTypeByCode();
        }
    });
})
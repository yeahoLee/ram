function sendCodeAndName() {
    var row = $('#dataGridTable').datagrid('getSelected');

    if (!row) {
        $.messager.alert('错误', '请选择一行数据！');
    } else {
        window.parent.showCodeAndName(row.id, row.code + " " + row.chsName);
        window.parent.closeDialog();
    }
}

function getAssetPlaceByCode() {
    var code = $('#assetLineCode').val()
        + $('#stationCode').val()
        + $('#buildNumCode').val()
        + $('#floorNumCode').val();
    $('#savePlaceCode').val(code);
    $('#dataGridTable').datagrid('reload', {code: code});
}

$(function () {
    $('#dataGridTable').datagrid({
        url: 'base/get_place_datagrid',
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

    $("#assetLineId").combobox({
        url: 'base/common_combo?commonCodeType=PLACE_CODE_LV1&showCode=true',
        method: 'POST',
        onLoadSuccess: function () { //加载完成后,设置选中第一项
            $(this).combobox('select', $(this).combobox('getData')[0]['value']);
        },
        onSelect: function (record) {

            $("#stationId").combobox({
                url: 'base/common_combo_by_parentid?showCode=true&parentId='+record.value,
                prompt:'输入关键字自动检索',
                required:false,
                editable:true,
                hasDownArrow:true,
                filter: function(q, row){
                    var opts = $(this).combobox('options');
                    return row[opts.textField].indexOf(q) > -1;
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
                            $('#stationCode').val(record.value1);
                            $('#buildNumCode').val('');
                            $('#floorNumCode').val('');
                            $('#buildNumId').combobox('clear');
                            $('#floorNumId').combobox('clear');
                            $('#buildNumId').combobox('loadData', data);
                            getAssetPlaceByCode();
                        },
                        error: function (data) {
                            AjaxErrorHandler(data);
                        }
                    });
                }
            });

        }
    });


    $("#buildNumId").combobox({
        onSelect: function (record) {
            $.ajax({
                url: 'base/common_combo_by_parentid',
                type: 'POST',
                data: {
                    parentId: record.value,
                    showCode: true
                },
                success: function (data) {
                    $('#buildNumCode').val(record.value1);
                    $('#floorNumCode').val('');
                    $('#floorNumId').combobox('clear');
                    $('#floorNumId').combobox('loadData', data);
                    getAssetPlaceByCode();
                },
                error: function (data) {
                    AjaxErrorHandler(data);
                }
            });
        }
    });

    $("#floorNumId").combobox({
        onSelect: function (record) {
            $('#floorNumCode').val(record.value1);
            getAssetPlaceByCode();
        }
    });
})
var flag = true;//是否可以保存资产信息
var Msg = "";//错误信息提示
function excelDialogOpen() {
    $('#batchUpload').form('clear');
    $('#excelDialog').dialog('center').dialog('open');
}

function importAssetXls() {
    $('#importAssetConfirmBtn').linkbutton('disable');
    $('#importForm').form('submit', {
        url: 'asset/import_asset_xls',
        success: function (data) {
            var result = $.parseJSON(data);
            if (!result.errorMessage) {
                for (var i = 0; i < (result.assetDtoList).length; i++) {
                    var requestData = {
                        //index: index,
                        materialCode: (result.assetDtoList)[i].materialCode,
                        assetName: (result.assetDtoList)[i].assetChsName,
                        assetType: (result.assetDtoList)[i].assetType,
                        assetTypeName: (result.assetDtoList)[i].assetTypeName,
                        assetTypeStr: (result.assetDtoList)[i].assetTypeStr,
                        specAndModels: (result.assetDtoList)[i].specAndModels,
                        seriesNum: (result.assetDtoList)[i].seriesNum,
                        unitOfMeasId: (result.assetDtoList)[i].unitOfMeasId,
                        unitOfMeasStr: (result.assetDtoList)[i].unitOfMeasStr,
                        assetBrand: (result.assetDtoList)[i].assetBrand,
                        purcPrice: (result.assetDtoList)[i].purcPrice,
                        equiOrigValue: (result.assetDtoList)[i].equiOrigValue,
                        techPara: (result.assetDtoList)[i].techPara,
                        remark: (result.assetDtoList)[i].remark,

                        companyId: (result.assetDtoList)[i].companyId,
                        companyStr: (result.assetDtoList)[i].companyStr,
                        belongLine: (result.assetDtoList)[i].belongLine,
                        belongLineStr: (result.assetDtoList)[i].belongLineStr,
                        buyDate: (result.assetDtoList)[i].buyDate,
                        manageDeptId: (result.assetDtoList)[i].manageDeptId,
                        manageDeptStr: (result.assetDtoList)[i].manageDeptStr,
                        managerId: (result.assetDtoList)[i].managerId,
                        managerStr: (result.assetDtoList)[i].managerStr,
                        contractNum: (result.assetDtoList)[i].contractNum,
                        assetSource: (result.assetDtoList)[i].assetSource,
                        savePlaceId: (result.assetDtoList)[i].savePlaceId,
                        savePlaceName: (result.assetDtoList)[i].savePlaceName,
                        savePlaceStr: (result.assetDtoList)[i].savePlaceName,
                        tendersNum: (result.assetDtoList)[i].tendersNum,
                        mainPeriod: (result.assetDtoList)[i].mainPeriod,
                        sourceUser: (result.assetDtoList)[i].sourceUser,
                        sourceContactInfo: (result.assetDtoList)[i].sourceContactInfo,
                        prodTime: (result.assetDtoList)[i].prodTime
                    };
                    $('#assetDataGridTable').datagrid('appendRow', requestData);
                    $('#assetDataGridTable').datagrid('autoSizeColumn');
                }
                $('#importAssetDialog').dialog("close");
                $.messager.alert('提示', '上传成功!', 'info');
                $('#importAssetConfirmBtn').linkbutton('enable');
            } else {
                $.messager.alert('提示', '上传失败:' + result.errorMessage, 'error');
                $('#importAssetConfirmBtn').linkbutton('enable');
            }
        },
    });
}

function saveReceipt(type) {
        $('#sBtn').attr('disabled', true);
        var receiptName = $('#mateReceInfo input[name="receiptName"]').val();
        var sourceType = $('#mateReceInfo input[name="sourceType"]').val();
        var reason = $('#reason').val();
        var remark = $('#remark').val();
        var data = $('#assetDataGridTable').datagrid('getData');
        var assetList = JSON.stringify(data.rows);
        var lengthStr = data.total
        var produceType = $('#produceType').combobox("getValue");
        if (produceType == null || produceType == "") {
            $.messager.alert('错误', '请先选择物资的类型！', 'error');
            return;
        }
        $.ajax({
            url: 'receipt/create_receipt',
            type: 'POST',
            async : false,
            data: {
                receiptName: receiptName,
                sourceType: sourceType,
                reason: reason,
                remark: remark,
                assetList: assetList,
                lengthStr: lengthStr,
                produceTypeStr: produceType
            },
            success: function (data) {
                $('#sBtn').removeAttr('disabled');
                if (type == 1) {
                    $.messager.alert('提示', '新建成功！', 'info', function () {
                        window.location.href = 'receiptList';
                    });
                } else {
                    var data1 = {};
                    data1.id=data.id;
                    data1.formName = data.receiptName;
                    data1.serialNumber = data.runningNum;
                    data1.processDefKey = ASSETS_ADD;
                    data1.resultLocation = "receiptList";
                    data1.produceType = produceType;
                    getFirstNode(data1);
                }
            },
            error: function (data) {
                $('#sBtn').removeAttr('disabled');
                AjaxErrorHandler(data);
            }
        });
}

function exportExcel() {
    window.open('receipt/export_excel');
}

function deleteAsset() {
    fromClean();
    $('#assetDialog').dialog('close');

    var rowIndexs = $('#assetDataGridTable').datagrid('getSelections');

    if (rowIndexs.length == 0)
        $.messager.alert('错误', '选择一个移除项！', 'error');

    //删除选中行
    for (var i = rowIndexs.length - 1; i >= 0; i--) {
        $('#assetDataGridTable').datagrid('deleteRow', $('#assetDataGridTable').datagrid('getRowIndex', rowIndexs[i]));
    }

    var data1 = $('#assetDataGridTable').datagrid('getData');
    if (data1.total == 0) {
        $('#produceType').combobox({
            disabled: false
        });

	    fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_PRODUCE_TYPE', 'produceType');
    }

}

function addAsset() {
    var produceType = $('#produceType').combobox("getValue");
    if (produceType == null || produceType == "") {
        $.messager.alert('错误', '请先选择物资的类型！', 'error');
        return;
    }

    fromClean();

    // $.getJSON("base/asset_combo_produceType",
    //     {produceTypeStr: produceType},
    //     function (json) {
    //         $("#materialCode").combobox({
    //             data: json,
    //             prompt: '输入关键字自动检索',
    //             required: false,
    //             editable: true,
    //             hasDownArrow: true,
    //             filter: function (q, row) {
    //                 var opts = $(this).combobox('options');
    //                 return row[opts.textField].indexOf(q) > -1;
    //             },
    //             onSelect: function (record) {
    //                 findTypeByMaterialCode();
    //             }
    //         });
    //     });

    $('#assetDialog').dialog('center').dialog('open');
}

function fromClean() {
    $('#asset_add').form('clear');
    cleanSpan();

}

function cleanSpan() {
    $('#assetTypeName').text('');
    $('#assetName').text('');
    $('#assetType').text('');
    $('#specAndModels').text('');
    $('#unitOfMeasId').text('');
    $('#showCodeAndData').text('');
}

function updateAsset(index) {
    fromClean();

    var dataRows = $('#assetDataGridTable').datagrid('getRows');
    var data = dataRows[index];
    $('#index').val(index);
    $('#materialCode').combobox('setValue', data.materialCode);
    $('#assetTypeName').text(data.assetTypeName);
    $('#assetName').text(data.assetName);
    $('#assetType').text(data.assetTypeStr)
    $('#specAndModels').text(data.specAndModels);
    $('#baseInfo input[name="seriesNum"]').val(data.seriesNum);
    $('#unitOfMeasId').text(data.unitOfMeasStr);
    $('#baseInfo input[name="assetBrand"]').val(data.assetBrand);
    $('#baseInfo input[name="purcPrice"]').val(data.purcPrice);
    $('#baseInfo input[name="equiOrigValue"]').val(data.equiOrigValue);
    $('#baseInfo textarea[name="techPara"]').val(data.techPara);
    $('#baseInfo textarea[name="remark"]').val(data.remark);
    $('#companyId').combobox('setValue', data.companyId);

    companyAjax(data.companyId, data.manageDeptId);
    $('#manageDeptId').combobox('setValue', data.manageDeptId);

    companyAjaxForUserCombobox(data.companyId, data.managerId);
    $('#managerId').combobox('setValue', data.managerId);

    $('#belongLine').combobox('setValue', data.belongLine);
    $('#buyDate').datebox('setValue', data.buyDate);
    $('#managerId').combobox('setValue', data.managerId);
    $('#extendInfo input[name="contractNum"]').val(data.contractNum);
    $('#extendInfo input[name="assetSource"]').val(data.assetSource);
    $('#extendInfo input[name="tendersNum"]').val(data.tendersNum);
    $('#extendInfo input[name="savePlaceIdHidden"]').val(data.savePlaceId);
    $('#showCodeAndData').text(data.savePlaceStr);
    $('#mainPeriod').datebox('setValue', data.mainPeriod);
    $('#extendInfo input[name="sourceUser"]').val(data.sourceUser);
    $('#extendInfo input[name="sourceContactInfo"]').val(data.sourceContactInfo);
    $('#prodTime').datebox('setValue', data.prodTime);

    $('#assetDialog').dialog('center').dialog('open');
}

function saveAsset() {
    //$('#saveBtn').attr('disabled',"true");
    if (flag != true) {
        $.messager.alert('错误', Msg, 'error');
        return;
    }
    //基本信息Start
    var index = $('#index').val();
    var materialCode = $('#materialCode').val();
    var assetTypeName = $('#assetTypeName').text();
    var assetName = $('#assetName').text();
    var assetTypeStr = $('#assetType').text();
    var specAndModels = $('#specAndModels').text();
    var seriesNum = $('#baseInfo input[name="seriesNum"]').val();
    var unitOfMeasStr = $('#unitOfMeasId').text();
    var assetBrand = $('#baseInfo input[name="assetBrand"]').val();
    var purcPrice = $('#baseInfo input[name="purcPrice"]').val();
    var equiOrigValue = $('#baseInfo input[name="equiOrigValue"]').val();
    var techPara = $('#baseInfo textarea[name="techPara"]').val();
    var remark = $('#baseInfo textarea[name="remark"]').val();
    //基本信息End

    //延展信息Start
    var companyId = $('#extendInfo input[name="companyId"]').val();
    var companyStr = $('#companyId').combobox('getText');
    var belongLine = $('#extendInfo input[name="belongLine"]').val();
    var belongLineStr = $('#belongLine').combobox('getText');
    var buyDate = $('#buyDate').datebox('getText');
    var manageDeptId = $('#extendInfo input[name="manageDeptId"]').val();
    var manageDeptStr = $('#manageDeptId').combobox('getText');
    var managerId = $('#extendInfo input[name="managerId"]').val();
    var managerStr = $('#managerId').combobox('getText');
    var contractNum = $('#extendInfo input[name="contractNum"]').val();
    var assetSource = $('#extendInfo input[name="assetSource"]').val();
    var tendersNum = $('#extendInfo input[name="tendersNum"]').val();
    var savePlaceId = $('#extendInfo input[name="savePlaceIdHidden"]').val();
    var savePlaceStr = $('#showCodeAndData').text();
    var savePlaceName = '';
    //安装位置名称通过空格拆分
    if (null != savePlaceStr && savePlaceStr.length != 0)
        savePlaceName = savePlaceStr.split(" ")[1];
    var mainPeriod = $('#mainPeriod').datebox('getText');
    var sourceUser = $('#extendInfo input[name="sourceUser"]').val();
    var sourceContactInfo = $('#extendInfo input[name="sourceContactInfo"]').val();
    var prodTime = $('#prodTime').datebox('getText');
    //延展信息End

    var requestData = {
        index: index,
        materialCode: materialCode,
        assetTypeName: assetTypeName,
        assetName: assetName,
        assetTypeStr: assetTypeStr,
        specAndModels: specAndModels,
        seriesNum: seriesNum,
        unitOfMeasStr: unitOfMeasStr,
        assetBrand: assetBrand,
        purcPrice: purcPrice,
        equiOrigValue: equiOrigValue,
        techPara: techPara,
        remark: remark,

        companyId: companyId,
        companyStr: companyStr,
        belongLine: belongLine,
        belongLineStr: belongLineStr,
        buyDate: buyDate,
        manageDeptId: manageDeptId,
        manageDeptStr: manageDeptStr,
        managerId: managerId,
        managerStr: managerStr,
        contractNum: contractNum,
        assetSource: assetSource,
        savePlaceId: savePlaceId,
        savePlaceStr: savePlaceStr,
        savePlaceName: savePlaceName,
        tendersNum: tendersNum,
        mainPeriod: mainPeriod,
        sourceUser: sourceUser,
        sourceContactInfo: sourceContactInfo,
        prodTime: prodTime
    };

    $.ajax({
        url: 'asset/check_asset_dto',
        type: 'POST',
        data: requestData,
        success: function (data) {
            $('#assetDialog').dialog('close');

            var v = $('#produceType').combobox("getValue");
            $('#produceType').combobox({
                disabled: true,
                value: v
            });
            if (index) {
                $.messager.alert('提示', '更新成功！', 'info', function () {
                    $('#assetDataGridTable').datagrid('updateRow', {
                        index: index,
                        row: requestData
                    });
                    $('#assetDataGridTable').datagrid('autoSizeColumn');
                });
            } else {
                $.messager.alert('提示', '添加成功！', 'info', function () {
                    $('#assetDataGridTable').datagrid('appendRow', requestData);
                    $('#assetDataGridTable').datagrid('autoSizeColumn');
                });
            }


        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

//通过物资编码查询资产类别和资产名称
function findTypeByMaterialCode() {
    cleanSpan();
    var materialCode = $('#baseInfo input[name="materialCode"]').val();
    $.ajax({
        url: 'base/find_asset_type_by_code',
        type: 'POST',
        dataType: 'json',
        data: {materialCode: materialCode},
        success: function (data) {
            $('#assetTypeName').text(data.assetTypeName);
            findNameByMaterialCode(materialCode);
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function findNameByMaterialCode(materialCode) {
    $.ajax({
        url: 'base/find_asset_name_by_code',
        type: 'POST',
        dataType: 'json',
        data: {materialCode: materialCode},
        success: function (data) {
            $('#assetName').text(data.assetName);
            $('#assetType').text(data.W_PRO_CODE);
            $('#specAndModels').text(data.MARTERIALS_SPEC);
            $('#unitOfMeasId').text(data.W_UNIT_CODE);
            $('#assetBrand').text(data.BRAND_NAME)
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

//提供给dialog调用
function showCodeAndName(id, codeAndName) {
    $('#showCodeAndData').text(codeAndName);
    $('#assetDialog input[name="savePlaceIdHidden"]').val(id);
}

//提供给dialog调用
function closeDialog() {
    $('#instSiteCodeDialog').dialog('close');
}

var isDialogInit = false;

function openDialig() {
    if (!isDialogInit) {
        $('#instSiteCodeDialogFrame').attr('src', 'asset_save_place');
        isDialogInit = true;
    }
    $('#instSiteCodeDialog').dialog('center').dialog('open');
//	$('#instSiteCodeDialog').dialog('refresh');
}

function companyAjax(parentId, id) {
    $.ajax({
        url: 'base/dept_combo_by_pdid',
        type: 'POST',
        data: {parentDeptId: parentId},
        success: function (data) {
            $('#manageDeptId').combobox('clear');
            $('#manageDeptId').combobox('loadData', data);
            $('#manageDeptId').combobox('setValue', id);
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function companyAjaxForUserCombobox(parentId, id) {
    $.ajax({
        url: 'base/user_comboByComCode',
        type: 'POST',
        data: {id: parentId},
        success: function (data) {
            $('#managerId').combobox('clear');
            $('#managerId').combobox('loadData', data);
            $('#managerId').combobox('setValue', id);
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function reloadAsset() {
    var produceType = $('#produceType').combobox("getValue");
    $('#dataGridTable').datagrid("load",{
        produceTypeStr : produceType
    });
    $("#assetListDialog").dialog("open");
}

function searchByQuerys() {
    var produceType = $('#produceType').combobox("getValue");
    var assetCode = $("#assetCodeQuery").val();
    var assetName = $("#assetNameQuery").val();

    $("#dataGridTable").datagrid("load",{
        assetCode : assetCode,
        assetName : assetName,
        produceTypeStr : produceType

    })
}

$(function () {

    fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_PRODUCE_TYPE', 'produceType');


    $("#companyId").combobox({
        onSelect: function (record) {
            var deptUrl = 'base/dept_combo_by_pdid?parentDeptId=' + record.value;
            var managerUrl = 'base/user_comboByComCode_zcgly?id=' + record.value;
            $('#managerId').combobox('reload', managerUrl);
            $('#manageDeptId').combobox('reload', deptUrl);
        }
    });

    $('#assetDataGridTable').datagrid({
        striped: true,
        rownumbers: true,
        title: '物资信息清单',
        toolbar: '#assetDataGridTableButtons',
        columns: [[
            {checkbox: true},
            {
                field: 'materialCode', title: '物资编码', formatter: function (value, row, index) {
                    return '<a onclick="updateAsset(\'' + index + '\')">' + value + '</a>';
                }
            },
            {field: 'assetName', title: '资产名称'},
            {field: 'assetTypeStr', title: '资产类型'},
            {field: 'assetTypeName', hidden: true},
            {field: 'specAndModels', title: '规格型号'},
            {field: 'seriesNum', title: '序列号'},
            {field: 'unitOfMeasStr', title: '计量单位'},
            {field: 'assetBrand', title: '品牌'},
            {field: 'purcPrice', title: '采购价'},
            {field: 'equiOrigValue', title: '资产原值'},
            {field: 'techPara', title: '技术参数'},
            {field: 'remark', title: '备注'},
            {field: 'companyStr', title: '所属公司'},
            {field: 'belongLineStr', title: '所属线路/建筑'},
            {field: 'buyDate', title: '购置日期'},
            {field: 'manageDeptStr', title: '主管部门'},
            {field: 'managerStr', title: '资产管理员'},
            {field: 'contractNum', title: '合同编号'},
            {field: 'assetSource', title: '资产来源'},
            {field: 'tendersNum', title: '标段标号'},
            {field: 'savePlaceName', title: '安装位置'},
            {field: 'mainPeriod', title: '维保期'},
            {field: 'sourceUser', title: '联系人'},
            {field: 'sourceContactInfo', title: '联系方式'},
            {field: 'prodTime', title: '出厂日期'}
        ]]
    })


    $('#dataGridTable').datagrid({
        url: 'base/asset_combo_produceType',
        // fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        pagination: true,
        pageSize: 10,
        toolbar: '#dataGridTableButtons',
        columns: [[
            {field: 'id', hidden:true},
            {field: 'code', title: '资产编码'},
            {field: 'chsName', title: '资产名称'},
            {field: 'wProCode', title: '物资属性'},
            {field: 'wTypeCode', title: '物资类型'},
            {field: 'marterialsSpec', title: '规格型号'},
            {field: 'wUnitCode', title: '计量单位'}
        ]],

        onClickRow: function (rowIndex, rowData) {
            var rows = $("#dataGridTable").datagrid("getRows");
            var row = rows[rowIndex];
            $("#materialCode").val(row.code);
            $("#assetTypeName").text(row.wTypeCode);
            $("#assetName").text(row.chsName);
            $("#assetType").text(row.wProCode);
            $("#specAndModels").text(row.marterialsSpec);
            $("#unitOfMeasId").text(row.wUnitCode);
            $("#dataGridTable").datagrid("reload");
            $("#assetListDialog").dialog("close");
        }
    });

    $('#instSiteCodeDialog').dialog('close');
});
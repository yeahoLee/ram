function goToRreceiptListPage() {
    window.location.href = './receiptList';
}

function goToViewAssetPageSubmit(assetId) {
    window.location.href = './asset_view?assetId=' + assetId;
}

function goToStanBookPage() {
    $('#content_title', window.parent.document).html('<i class="fa fa-chevron-right" style="margin-right: 5px;"></i>资产台账（资产模式）');
    var materialCodeSubmit = $('#searchQuerys input[name="materialCode"]').val();//物资编码
    var assetCodeSubmit = $('#searchQuerys input[name="assetCode"]').val();//资产编码
    var assetChsNameSubmit = $('#searchQuerys input[name="assetChsName"]').val();//资产名称
    // var assetStatusSubmit = $('#assetStatus').combobox("getValue");//资产状态
    var assetTypeSubmit = $('#assetTypeDialog input[name="rowIdHidden"]').val();//资产类型
    var manageDeptIdSubmit = $('#manageDeptId').combotree("getValue");//管理部门
    var managerIdSubmit = $('#managerId').combobox("getValue");//管理员
    var useDeptIdSubmit = $('#useDeptId').combotree("getValue");//使用部门
    var userIdSubmit = $('#userId').combobox("getValue");//使用人
    var codeAndNameSubmit = $('#showCodeAndData').text();//位置的code和name
    var savePlaceIdSubmit = $('#instSiteCodeDialog input[name="rowIdHidden"]').val();//位置id

    if (typeof(assetCodeSubmit) == "undefined") {
        assetCodeSubmit = "";
    }

    window.location.href = './asset_stan_book?materialCodeSubmit=' + materialCodeSubmit + '&assetCodeSubmit=' + assetCodeSubmit
        + '&assetChsNameSubmit=' + assetChsNameSubmit
        // + '&assetStatusSubmit=' + assetStatusSubmit
        + '&assetTypeSubmit=' + assetTypeSubmit + '&savePlaceIdSubmit=' + savePlaceIdSubmit
        + '&manageDeptIdSubmit=' + manageDeptIdSubmit + '&managerIdSubmit=' + managerIdSubmit
        + '&useDeptIdSubmit=' + useDeptIdSubmit + '&userIdSubmit=' + userIdSubmit
        + '&codeAndNameSubmit=' + codeAndNameSubmit + '&backToLastPageUrl=asset_stan_book';
}

function openAdvaSearchDialog() {
    $("#advaSearch").dialog('center').dialog('open');
}

function goToUpdateBatch() {
    var assetIdList = new Array();
    var data = $('#dataGridTable').datagrid('getSelections');
    for (var row in data) {
        var arrayStr = data[row].parentDictEngName.replace("[", "").replace("]", "");
        var array = arrayStr.split(",")
        assetIdList = assetIdList.concat(array);
    }
    $('#assetIdList').val(assetIdList);
    //搜索中提交的参数
    var materialCodeSubmit = $('#searchQuerys input[name="materialCode"]').val();//物资编码
    var assetCodeSubmit = $('#searchQuerys input[name="assetCode"]').val();//资产编码
    var assetChsNameSubmit = $('#searchQuerys input[name="assetChsName"]').val();//资产名称
    // var assetStatusSubmit = $('#assetStatus').combobox("getValue");//资产状态
    var assetTypeSubmit = $('#assetTypeDialog input[name="rowIdHidden"]').val();//资产类型
    var savePlaceIdSubmit = $('#instSiteCodeDialog input[name="rowIdHidden"]').val();//位置id
    var manageDeptIdSubmit = $('#manageDeptId').combotree("getValue");//管理部门
    var managerIdSubmit = $('#managerId').combobox("getValue");//管理员
    var useDeptIdSubmit = $('#useDeptId').combotree("getValue");//使用部门
    var userIdSubmit = $('#userId').combobox("getValue");//使用人
    var codeAndNameSubmit = $('#showCodeAndData').text();//位置的code和name
    $('#materialCodeSubmit').val(materialCodeSubmit);
    $('#assetCodeSubmit').val(assetCodeSubmit);
    $('#assetChsNameSubmit').val(assetChsNameSubmit);
    // $('#assetStatusSubmit').val(assetStatusSubmit);
    $('#assetTypeSubmit').val(assetTypeSubmit);
    $('#savePlaceIdSubmit').val(savePlaceIdSubmit);
    $('#manageDeptIdSubmit').val(manageDeptIdSubmit);
    $('#managerIdSubmit').val(managerIdSubmit);
    $('#useDeptIdSubmit').val(useDeptIdSubmit);
    $('#userIdSubmit').val(userIdSubmit);
    $('#codeAndNameSubmit').val(codeAndNameSubmit);
    $('#idListForm').submit();
    //window.open('./asset_update_batch?assetIdList='+assetIdList);
}

function showCodeAndName(id, codeAndName) {
    $('#showCodeAndData').text(codeAndName);
    $('#instSiteCodeDialog input[name="rowIdHidden"]').val(id);
}

function closeDialog() {
    $('#instSiteCodeDialog').dialog('close');
}

function showAssetTypeCodeAndName(id, codeAndName) {
    $('#showAssetTypeCodeAndData').text(codeAndName);
    $('#assetTypeDialog input[name="rowIdHidden"]').val(id);
}

function closeAssetTypeDialog() {
    $('#assetTypeDialog').dialog('close');
}

function searchByQuerys() {
    $('#dataGridTable').datagrid('load', {
        produceTypeStr: $('#produceType').combobox("getValue"),
        materialCode: $('#searchQuerys input[name="materialCode"]').val(),//物资编码
        assetCode: $('#searchQuerys input[name="assetCode"]').val(),//资产编码
        assetChsName: $('#searchQuerys input[name="assetChsName"]').val(),//资产名称
        // assetStatus: $('#assetStatus').combobox("getValue"),//资产状态
        assetType: $('#assetTypeDialog input[name="rowIdHidden"]').val(),//资产类型,相当于物资编码查询
        savePlaceId: $('#instSiteCodeDialog input[name="rowIdHidden"]').val(),//位置
        manageDeptId: $('#manageDeptId').combotree("getValue"),//管理部门
        managerId: $('#managerId').combobox("getValue"),//管理员
        useDeptId: $('#useDeptId').combotree("getValue"),//使用部门
        userId: $('#userId').combobox("getValue")//使用人
    })
}

function searchByDefaultQuerys() {
    $('#searchQuerys input[name="materialCode"]').val('');//物资编码
    $('#searchQuerys input[name="assetCode"]').val('');//资产编码
    $('#searchQuerys input[name="assetChsName"]').val('');//资产名称
    assetStatus: $('#assetStatus').combobox('clear');//资产状态
    assetType:$('#assetTypeDialog input[name="rowIdHidden"]').val();//资产类型,相当于物资编码查询
    savePlaceId: $('#instSiteCodeDialog input[name="rowIdHidden"]').val('');//位置
    manageDeptId: $('#manageDeptId').combotree('clear');//管理部门
    managerId: $('#managerId').combobox('clear');//管理员
    useDeptId: $('#useDeptId').combotree('clear');//使用部门
    userId: $('#userId').combobox('clear');//使用人
    produceType: $('#produceType').combobox("clear");
    $('#dataGridTable').datagrid('reload', {});
}

function exportAssetStanBookSubmit() {
    var assetCode = $('#searchQuerys input[name="assetCode"]').val();
    var assetStatus = $('#assetStatus').combobox("getValue");
    var manageDeptId = $('#manageDeptId').combotree("getValue");
    var managerId = $('#managerId').combobox("getValue");
    var useDeptId = $('#useDeptId').combotree("getValue");
    var userId = $('#userId').combobox("getValue");
    var produceTypeStr = $('#produceType').combobox("getValue");

    var openUrl = 'asset/export_asset_stan_book?assetCode=' + assetCode
        + '&assetStatus=' + assetStatus
        + '&manageDeptId=' + manageDeptId
        + '&managerId=' + managerId
        + '&useDeptId=' + useDeptId
        + '&produceTypeStr=' + produceTypeStr
        + '&userId=' + userId;

    window.open(openUrl);
}

function goToViewAssetDetailPage(assetIdList) {
    //window.location.href = './asset_view_detail?materialCode=' + materialCode;
    // $('#materialCode').val(materialCode);
    $('#assetIdSet').val(assetIdList);
    //搜索框条件
    var assetCode = $('#searchQuerys input[name="assetCode"]').val();
    var assetChsName = $('#searchQuerys input[name="assetChsName"]').val();
    // var assetStatusView = $('#assetStatus').combobox('getValue');
    var materialSearchCode = $('#searchQuerys input[name="materialCode"]').val();
    if (!isEmpty(assetCode)) {
        $('#assetCode').val(assetCode);
    }
    if (!isEmpty(assetChsName)) {
        $('#assetChsName').val(assetChsName);
    }
    // if (!isEmpty(assetStatusView)) {
    //     $('#assetStatusView').val(assetStatusView);
    // }
    if (!isEmpty(materialSearchCode)) {
        $('#materialSearchCode').val(materialSearchCode);
    }
    $('#goToAssetViewDetailForm').submit();
}

function isEmpty(obj) {
    if (typeof obj == "undefined" || obj == null || obj == "") {
        return true;
    } else {
        return false;
    }
}

/**
 * 初始化使用部门结构树
 */
function initCombotree() {
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
}

$(function () {
    initCombotree();
    fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_PRODUCE_TYPE', 'produceType');
    $("#matirialModelBtn").attr("disabled", true); //物资模式按钮不可点击
    //载入完成后判断所有Input框等是否有值，若有值则设置属性disabled:disabled
    if ($('#searchQuerys input[name="materialCode"]').val()) {
        $('#searchQuerys input[name="materialCode"]').attr('disabled', true);
    }
    if ($('#searchQuerys input[name="assetCode"]').val()) {
        $('#searchQuerys input[name="assetCode"]').attr('disabled', true);
    }
    if ($('#searchQuerys input[name="assetChsName"]').val()) {
        $('#searchQuerys input[name="assetChsName"]').attr('disabled', true);
    }
    if ($('#assetStatus').val()) {
        $('#assetStatus').combobox({disabled: true});
    }
    if ($('#searchText').val()) {
        $('#searchText').attr('disabled', true);
    }
    if ($('#showCodeAndData').text()) {
        $('#instSiteCodeDialog input[name="rowIdHidden"]').val();
        $('#showCodeAndDataButton').removeAttr('onclick');
    }
    if ($('#showAssetTypeCodeAndData').text()) {
        $('#assetTypeDialog input[name="rowIdHidden"]').val();
        $('#showAssetTypeCodeAndDataButton').removeAttr('onclick');
        $('#showAssetTypeCodeAndDataButton').hide();
    }
    if ($('#manageDeptId').val()) {
        $('#manageDeptId').combotree({disabled: true});
    }
    if ($('#managerId').val()) {
        $('#managerId').combobox({disabled: true});
    }
    if ($('#useDeptId').val()) {
        $('#useDeptId').combotree({disabled: true});
    }
    if ($('#userId').val()) {
        $('#userId').combobox({disabled: true});
    }

    //返回时获取值
    var materialCode = $('#materialCodeSubmit').val();
    var assetCode = $('#assetCodeSubmit').val();
    var assetChsName = $('#assetChsNameSubmit').val();
    var assetStatus = $('#assetStatusSubmit').val();
    var materialSearchCode = $('#materialSearchCodeSubmit').val();
    $('#assetStatus').combobox('setValue', assetStatus);
    $('#searchQuerys input[name="materialCode"]').val(materialSearchCode);
    $('#searchQuerys input[name="assetCode"]').val(assetCode);//资产编码
    $('#searchQuerys input[name="assetChsName"]').val(assetChsName);//资产名称

    // fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_TYPE', 'assetType');
    //fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_STATUS', 'assetStatus');
    var data;
    var flag = $('#returnFromUpdateBatchFlag').val();
    if (flag) {
        var materialCode = $('#materialCodeSubmit').val();//物资编码
        var assetCode = $('#assetCodeSubmit').val();//资产编码
        var assetChsName = $('#assetChsNameSubmit').val();//资产名称
        var assetStatus = $('#assetStatusSubmit').val();//资产状态
        var assetType = $('#assetTypeSubmit').val();//资产类型
        var savePlaceId = $('#savePlaceIdSubmit').val();
        var manageDeptId = $('#manageDeptIdSubmit').val();//管理部门
        var managerId = $('#managerIdSubmit').val();//管理员
        var useDeptId = $('#useDeptIdSubmit').val();//使用部门
        var userId = $('#userIdSubmit').val();//使用人
        var codeAndName = $('#codeAndNameSubmit').val();//位置的code和name
        $('#searchQuerys input[name="materialCode"]').val(materialCode),//物资编码
            $('#searchQuerys input[name="assetCode"]').val(assetCode),//资产编码
            $('#searchQuerys input[name="assetChsName"]').val(assetChsName),//资产名称
            $('#assetStatus').combobox('setValue', assetStatus),//资产状态
            $('#assetTypeDialog input[name="rowIdHidden"]').val(assetType),//资产类型
            $('#instSiteCodeDialog input[name="rowIdHidden"]').val(savePlaceId),
            $('#manageDeptId').combotree('setValue', manageDeptId),//管理部门
            $('#managerId').combobox('setValue', managerId),//管理员
            $('#useDeptId').combotree('setValue', useDeptId),//使用部门
            $('#userId').combobox('setValue', userId),//使用人
            $('#showCodeAndData').text(codeAndName);
    }
    data = {
        "materialCode": $('#searchQuerys input[name="materialCode"]').val(),//物资编码
        "assetCode": $('#searchQuerys input[name="assetCode"]').val(),//资产编码
        "assetChsName": $('#searchQuerys input[name="assetChsName"]').val(),//资产名称
        // "assetStatus": $('#assetStatus').combobox("getValue"),//资产状态
        "assetType": $('#assetTypeDialog input[name="rowIdHidden"]').val(),//资产类型
        "savePlaceId": $('#instSiteCodeDialog input[name="rowIdHidden"]').val(),
        "manageDeptId": $('#manageDeptId').combotree("getValue"),//管理部门
        "managerId": $('#managerId').combobox("getValue"),//管理员
        "useDeptId": $('#useDeptId').combotree("getValue"),//使用部门
        "userId": $('#userId').combobox("getValue"),//使用人	
    }

    $('#dataGridTable').datagrid({
        url: 'asset/material_mode_datagrid',
        fit: true,
        striped: true,
        rownumbers: true,
        pageSize: 20,
        pagination: true,
        queryParams: data,
        title: '资产台账',
        toolbar: '#btdiv',
        loadMsg: '程序处理中，请稍等...',
        columns: [[
            {field: 'id', checkbox: true},
            {
                field: 'code', title: '物资编码', formatter: function (value, row, index) {
                    return '<a href="javascript:void(0);" onClick="goToViewAssetDetailPage(\'' + row.parentDictEngName + '\');">' + value + '</a>';
                }
            },
            {
                field: 'W_IS_PRO', title: '物资的类型', formatter: function (value, row, index) {
                    return row.propertyMap.W_IS_PRO;
                }
            },
            {field: 'chsName', title: '物资名称'},
            {
                field: 'W_PRO_CODE', title: '物资类型', formatter: function (value, row, index) {
                    return row.propertyMap.W_PRO_CODE;
                }
            },
            {
                field: 'MARTERIALS_SPEC', title: '规格型号', formatter: function (value, row, index) {
                    return row.propertyMap.MARTERIALS_SPEC;
                }
            },
            {
                field: 'assetQuantity', title: '资产数量', formatter: function (value, row, index) {
                    return row.propertyMap.assetQuantity;
                }
            },
            {
                field: 'W_UNIT_CODE', title: '计量单位', formatter: function (value, row, index) {
                    return row.propertyMap.W_UNIT_CODE;
                }
            },
            {
                field: 'parentDictEngName', title: '资产id', hidden: true
            }
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });

    // $('#useDeptId').combobox({
    // 	url: 'base/dept_combo_by_querys?showType=0',
    // 	mode: 'remote',
    // 	method: 'POST',
    // 	onSelect: function (record) {
    //         $.ajax({
    //             url: 'base/dept_user_combobox',
    //             type: 'POST',
    //             data: {deptId: record.value },
    //             success: function(data){
    //                 $('#userId').combobox('clear');
    //                 $('#userId').combobox('loadData', data);
    //             },
    //             error: function(data){
    //                 AjaxErrorHandler(data);
    //             }
    //         });
    //     }
    // });

    /*if($('#returnFromUpdateBatchFlag').val()){
        var materialCode = $('#materialCodeSubmit').val();//物资编码
        var assetCode = $('#assetCodeSubmit').val();//资产编码
        var assetChsName = $('#assetChsNameSubmit').val();//资产名称
        var assetStatus = $('#assetStatusSubmit').val();//资产状态
        var assetType = $('#assetTypeSubmit').val();//资产类型
        var savePlaceId = $('#savePlaceIdSubmit').val();
        var manageDeptId = $('#manageDeptIdSubmit').val();//管理部门
        var managerId = $('#managerIdSubmit').val();//管理员
        var useDeptId = $('#useDeptIdSubmit').val();//使用部门
        var userId = $('#userIdSubmit').val();//使用人
        var codeAndName = $('#codeAndNameSubmit').val();//位置的code和name
        $('#searchQuerys input[name="materialCode"]').val(materialCode),//物资编码
        $('#searchQuerys input[name="assetCode"]').val(assetCode),//资产编码
        $('#searchQuerys input[name="assetChsName"]').val(assetChsName),//资产名称
        $('#assetStatus').combobox('setValue',assetStatus),//资产状态
        $('#assetType').combobox('setValue',assetType),//资产类型
        $('#instSiteCodeDialog input[name="rowIdHidden"]').val(savePlaceId),
        $('#manageDeptId').combobox('setValue',manageDeptId),//管理部门
        $('#managerId').combobox('setValue',managerId),//管理员
        $('#useDeptId').combobox('setValue',useDeptId),//使用部门
        $('#userId').combobox('setValue',userId),//使用人
        $('#showCodeAndData').text(codeAndName);
        //有问题（不能用延迟）
        setTimeout("searchByQuerys()","1000");
    }*/
})
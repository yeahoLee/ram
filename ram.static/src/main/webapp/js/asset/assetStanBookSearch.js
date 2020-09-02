function goToRreceiptListPage() {
    window.location.href = './receiptList';
}

function goToStanBookMatirialPage() {
    var materialCodeSubmit = $('#searchQuerys input[name="materialCode"]').val();//物资编码
    var assetCodeSubmit = $('#searchQuerys input[name="assetCode"]').val();//资产编码
    var assetChsNameSubmit = $('#searchQuerys input[name="assetChsName"]').val();//资产名称
    var assetStatusSubmit = $('#assetStatus').combobox("getValue");//资产状态
    var assetTypeSubmit = $('#assetTypeDialog input[name="rowIdHidden"]').val();//资产类型
    var manageDeptIdSubmit = $('#manageDeptId').combobox("getValue");//管理部门
    var managerIdSubmit = $('#managerId').combobox("getValue");//管理员
    var useDeptIdSubmit = $('#useDeptId').combobox("getValue");//使用部门
    var userIdSubmit = $('#userId').combobox("getValue");//使用人
    var codeAndNameSubmit = $('#showCodeAndData').text();//位置的code和name
    var savePlaceIdSubmit = $('#instSiteCodeDialog input[name="rowIdHidden"]').val();//位置id

    window.location.href = './asset_stan_book_material?materialCodeSubmit=' + materialCodeSubmit + '&assetCodeSubmit=' + assetCodeSubmit
        + '&assetChsNameSubmit=' + assetChsNameSubmit + '&assetStatusSubmit=' + assetStatusSubmit
        + '&assetTypeSubmit=' + assetTypeSubmit + '&savePlaceIdSubmit=' + savePlaceIdSubmit
        + '&manageDeptIdSubmit=' + manageDeptIdSubmit + '&managerIdSubmit=' + managerIdSubmit
        + '&useDeptIdSubmit=' + useDeptIdSubmit + '&userIdSubmit=' + userIdSubmit
        + '&codeAndNameSubmit=' + codeAndNameSubmit + '&backToLastPageUrl=asset_stan_book_material';
}

function goToViewAssetPageSubmit(assetId) {
    //$('#assetId').val(assetId);
    //$('#goToAssetViewPageForm').submit();
    window.open('./asset_view?assetId=' + assetId);
}

function openAdvaSearchDialog() {
    $("#advaSearch").dialog('center').dialog('open');
}

function goToUpdateBatch() {
    $('#idListForm').attr('action', 'asset_update_batch');
    var assetIdList = new Array();
    var data = $('#dataGridTable').datagrid('getSelections');
    for (var row in data) {
        assetIdList.push(data[row].id);
    }
    $('#assetIdList').val(assetIdList);
    //搜索中提交的参数
    var materialCodeSubmit = $('#searchQuerys input[name="materialCode"]').val();//物资编码
    var assetCodeSubmit = $('#searchQuerys input[name="assetCode"]').val();//资产编码
    var assetChsNameSubmit = $('#searchQuerys input[name="assetChsName"]').val();//资产名称
    var assetStatusSubmit = $('#assetStatus').combobox("getValue");//资产状态
    var assetTypeSubmit = $('#assetTypeDialog input[name="rowIdHidden"]').val();//资产类型
    var savePlaceIdSubmit = $('#instSiteCodeDialog input[name="rowIdHidden"]').val();//位置id
    var manageDeptIdSubmit = $('#manageDeptId').combobox("getValue");//管理部门
    var managerIdSubmit = $('#managerId').combobox("getValue");//管理员
    var useDeptIdSubmit = $('#useDeptId').combobox("getValue");//使用部门
    var userIdSubmit = $('#userId').combobox("getValue");//使用人
    var codeAndNameSubmit = $('#showCodeAndData').text();//位置的code和name
    $('#materialCodeSubmit').val(materialCodeSubmit);
    $('#assetCodeSubmit').val(assetCodeSubmit);
    $('#assetChsNameSubmit').val(assetChsNameSubmit);
    $('#assetStatusSubmit').val(assetStatusSubmit);
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
        materialCode: $('#searchQuerys input[name="materialCode"]').val(),//物资编码
        assetCode: $('#searchQuerys input[name="assetCode"]').val(),//资产编码
        assetChsName: $('#searchQuerys input[name="assetChsName"]').val(),//资产名称
        assetStatus: $('#assetStatus').combobox("getValue"),//资产状态
        assetType: $('#assetTypeDialog input[name="rowIdHidden"]').val(),//资产类型,相当于物资编码查询
        //位置原本就有在后台根据dynamicViewId查询placeID,位置若重新选择的直接传入id
        savePlaceId: $('#instSiteCodeDialog input[name="rowIdHidden"]').val(),
        manageDeptId: $('#manageDeptId').combobox("getValue"),//管理部门
        managerId: $('#managerId').combobox("getValue"),//管理员
        useDeptId: $('#useDeptId').combobox("getValue"),//使用部门
        userId: $('#userId').combobox("getValue"),//使用人
    });
}

function exportAssetStanBookSubmit() {
    var assetCode = $('#searchQuerys input[name="assetCode"]').val();
    var assetStatus = $('#assetStatus').combobox("getValue");
    var manageDeptId = $('#manageDeptId').combobox("getValue");
    var managerId = $('#managerId').combobox("getValue");
    var useDeptId = $('#useDeptId').combobox("getValue");
    var userId = $('#userId').combobox("getValue");

    var openUrl = 'asset/export_asset_stan_book?assetCode=' + assetCode
        + '&assetStatus=' + assetStatus
        + '&manageDeptId=' + manageDeptId
        + '&managerId=' + managerId
        + '&useDeptId=' + useDeptId
        + '&userId=' + userId;

    window.open(openUrl);
}

$(function () {
    fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_TYPE', 'assetType');
    //fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_STATUS', 'assetStatus');

    var dynaViewId = $('#dynaViewId').val();
    if (!dynaViewId)
        dynaViewId = "";
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
    if ($('#assetType').val()) {
        $('#assetType').combobox({disabled: true});
    }
    if ($('#searchText').val()) {
        $('#searchText').attr('disabled', true);
    }
    if ($('#showCodeAndData').text()) {
        $('#instSiteCodeDialog input[name="rowIdHidden"]').val();
        $('#showCodeAndDataButton').removeAttr('onclick');
        $('#showCodeAndDataButton').hide();
    }
    if ($('#showAssetTypeCodeAndData').text()) {
        $('#assetTypeDialog input[name="rowIdHidden"]').val();
        $('#showAssetTypeCodeAndDataButton').removeAttr('onclick');
        $('#showAssetTypeCodeAndDataButton').hide();
    }
    if ($('#manageDeptId').val()) {
        $('#manageDeptId').combobox({disabled: true});
    }
    if ($('#managerId').val()) {
        $('#managerId').combobox({disabled: true});
    }
    if ($('#useDeptId').val()) {
        $('#useDeptId').combobox({disabled: true});
    }
    if ($('#userId').val()) {
        $('#userId').combobox({disabled: true});
    }

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
            $('#manageDeptId').combobox('setValue', manageDeptId),//管理部门
            $('#managerId').combobox('setValue', managerId),//管理员
            $('#useDeptId').combobox('setValue', useDeptId),//使用部门
            $('#userId').combobox('setValue', userId),//使用人
            $('#showCodeAndData').text(codeAndName);

        data = {
            "materialCode": $('#searchQuerys input[name="materialCode"]').val(),//物资编码
            "assetCode": $('#searchQuerys input[name="assetCode"]').val(),//资产编码
            "assetChsName": $('#searchQuerys input[name="assetChsName"]').val(),//资产名称
            "assetStatus": $('#assetStatus').combobox("getValue"),//资产状态
            "assetType": $('#assetTypeDialog input[name="rowIdHidden"]').val(),//资产类型
            "savePlaceId": $('#instSiteCodeDialog input[name="rowIdHidden"]').val(),
            "manageDeptId": $('#manageDeptId').combobox("getValue"),//管理部门
            "managerId": $('#managerId').combobox("getValue"),//管理员
            "useDeptId": $('#useDeptId').combobox("getValue"),//使用部门
            "userId": $('#userId').combobox("getValue"),//使用人	
        }
    } else {
        data = {"dynaViewId": dynaViewId};
    }
    $('#dataGridTable').datagrid({
        url: 'asset/asset_datagrid',
        fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        pagination: true,
        pageSize: 20,
        title: '资产台账',
        toolbar: '#btdiv',
        loadMsg: '程序处理中，请稍等...',
        queryParams: data,
        //queryParams: {dynaViewId: dynaViewId},
        columns: [[
            {field: 'id', checkbox: true},
            {
                field: 'assetCode', title: '资产编码', formatter: function (value, row, index) {
                    return '<a href="javascript:void(0);" onClick="goToViewAssetPageSubmit(\'' + row.id + '\');">' + value + '</a>';
                }
            },
            {field: 'combinationAssetName', title: '资产名称'},
            {field: 'assetTypeStr', title: '资产类型'},
            {field: 'specAndModels', title: '规格型号'},
            {field: 'assetBrand', title: '品牌'},
            {field: 'unitOfMeasStr', title: '计量单位'},
            {field: 'savePlaceStr', title: '安装位置'},
            {field: 'useDeptStr', title: '使用部门'},
            {field: 'useStr', title: '使用人'},
            {field: 'manageDeptStr', title: '主管部门'},
            {field: 'managerStr', title: '资产管理员'},
            {field: 'belongLineStr', title: '所属线路'},
            {field: 'assetStatus', title: '资产状态'},
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });

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
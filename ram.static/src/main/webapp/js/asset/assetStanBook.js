function goToRreceiptListPage() {
    window.location.href = 'addReceipt';
}

function goToStanBookMatirialPage() {
    $('#content_title', window.parent.document).html('<i class="fa fa-chevron-right" style="margin-right: 5px;"></i>资产台账（物资模式）');
    // var materialCodeSubmit = $('#searchQuerys input[name="materialCode"]').val();//物资编码
    var assetCodeSubmit = $('#searchQuerys input[name="assetCode"]').val();//资产编码
    var assetChsNameSubmit = $('#searchQuerys input[name="assetChsName"]').val();//资产名称
    var assetStatusSubmit = $('#assetStatus').combobox("getValue");//资产状态
    var assetTypeSubmit = $('#assetTypeDialog input[name="rowIdHidden"]').val();//资产类型
    var manageDeptIdSubmit = $('#manageDeptId').combotree("getValue");//管理部门
    var managerIdSubmit = $('#managerId').combobox("getValue");//管理员
    var useDeptIdSubmit = $('#useDeptId').combotree("getValue");//使用部门
    var userIdSubmit = $('#userId').combobox("getValue");//使用人
    var codeAndNameSubmit = $('#showCodeAndData').text();//位置的code和name
    var savePlaceIdSubmit = $('#instSiteCodeDialog input[name="rowIdHidden"]').val();//位置id

    window.location.href = 'asset_stan_book_material?assetCodeSubmit=' + assetCodeSubmit
        + '&assetChsNameSubmit=' + assetChsNameSubmit + '&assetStatusSubmit=' + assetStatusSubmit
        + '&assetTypeSubmit=' + assetTypeSubmit + '&savePlaceIdSubmit=' + savePlaceIdSubmit
        + '&manageDeptIdSubmit=' + manageDeptIdSubmit + '&managerIdSubmit=' + managerIdSubmit
        + '&useDeptIdSubmit=' + useDeptIdSubmit + '&userIdSubmit=' + userIdSubmit
        + '&codeAndNameSubmit=' + codeAndNameSubmit + '&backToLastPageUrl=asset_stan_book_material';
}

function goToViewAssetPageSubmit(assetId) {
    window.open('asset_view?assetId=' + assetId);
}

function openAdvaSearchDialog() {
    $("#advaSearch").dialog('center').dialog('open');
}

/***
 * 批量修改页面
 * @returns
 */
function goToUpdateBatch() {
    $('#idListForm').attr('action', 'asset_update_batch');
    var assetIdList = new Array();
    var data = $('#dataGridTable').datagrid('getSelections');
    //闲置、停用、使用、借出 可以进行信息修改
    var str='0,1,6,7';
    for (var row in data) {
        if (str.indexOf(data[row].assetStatusStr)>-1) {
            assetIdList.push(data[row].id);
        }
    }
    $('#assetIdList').val(assetIdList);
    fillData();
    $('#idListForm').submit();
}

function printAssetCard() {
    var assetIdList = new Array();
    var data = $('#dataGridTable').datagrid('getSelections');
    if (data.length <= 0) {
        $.messager.alert('提示', '请先选择资产!', 'error');
        return;
    }
    for (var row in data) {
        assetIdList.push(data[row].id);
    }
    window.open('asset/print_asset_card_by_asset_id_list?assetIdList=' + assetIdList.join(","), '_blank');
}

function printAllAssetCard() {
    var assetCode = $('#searchQuerys input[name="assetCode"]').val();//资产编码
    var assetChsName = $('#searchQuerys input[name="assetChsName"]').val();//资产名称
    var assetStatus = $('#assetStatus').combobox("getValue");//资产状态
    var assetType = $('#assetTypeDialog input[name="rowIdHidden"]').val();//资产类型,相当于物资编码查询
    //位置原本就有在后台根据dynamicViewId查询placeID,位置若重新选择的直接传入id
    var savePlaceId = $('#instSiteCodeDialog input[name="rowIdHidden"]').val();
    var manageDeptId = $('#manageDeptId').combotree("getValue");//管理部门
    var managerId = $('#managerId').combobox("getValue");//管理员
    var useDeptId = $('#useDeptId').combotree("getValue");//使用部门
    var userId = $('#userId').combobox("getValue");//使用人


    var param = 'assetCode=' + assetCode
        + '&assetStatus=' + assetStatus
        + '&manageDeptId=' + manageDeptId
        + '&managerId=' + managerId
        + '&useDeptId=' + useDeptId
        + '&userId=' + userId
        + '&assetChsName=' + assetChsName
        + '&assetType=' + assetType
        + '&savePlaceId=' + savePlaceId;

    window.open('asset/print_asset_card_by_query?' + param, '_blank');
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
        //materialCode: $('#searchQuerys input[name="materialCode"]').val(),//物资编码
        produceTypeStr: $('#produceType').combobox("getValue"),
        assetCode: $('#searchQuerys input[name="assetCode"]').val(),//资产编码
        assetChsName: $('#searchQuerys input[name="assetChsName"]').val(),//资产名称
        assetStatus: $('#assetStatus').combobox("getValue"),//资产状态
        assetType: $('#assetTypeDialog input[name="rowIdHidden"]').val(),//资产类型,相当于物资编码查询
        //位置原本就有在后台根据dynamicViewId查询placeID,位置若重新选择的直接传入id
        savePlaceId: $('#instSiteCodeDialog input[name="rowIdHidden"]').val(),
        manageDeptId: $('#manageDeptId').combotree("getValue"),//管理部门
        managerId: $('#managerId').combobox("getValue"),//管理员
        useDeptId: $('#useDeptId').combotree("getValue"),//使用部门
        userId: $('#userId').combobox("getValue")//使用人
    });
}

function searchByDefaultQuerys() {
    $('#produceType').combobox("clear");
    $('#dataGridTable').datagrid('load', {
        materialCode: '',//物资编码
        assetCode: '',//资产编码
        assetChsName: '',//资产名称
        assetStatus: '',//资产状态
        assetType: '',//资产类型
        //位置原本就有在后台根据dynamicViewId查询placeID,位置若重新选择的直接传入id
        savePlaceId: '',
        manageDeptId: '',//管理部门
        managerId: '',//管理员
        useDeptId: '',//使用部门
        userId: '',//使用人
        produceType: $('#produceType').combobox("getValue")
    });

    // $('#searchQuerys input[name="materialCode"]').val('');//物资编码
    $('#searchQuerys input[name="assetCode"]').val('');//资产编码
    $('#searchQuerys input[name="assetChsName"]').val('');//资产名称
    $('#assetStatus').combobox("setValue", '');//资产状态
    $('#assetTypeDialog input[name="rowIdHidden"]').val(''),//资产类型
        $('#showAssetTypeCodeAndData').text('');
    $('#manageDeptId').combotree("setValue", '');//管理部门
    $('#managerId').combobox("setValue", '');//管理员
    $('#useDeptId').combotree("setValue", '');//使用部门
    $('#userId').combobox("setValue", '');//使用人
}

function exportAssetStanBookSubmit() {
    var assetCode = $('#searchQuerys input[name="assetCode"]').val();
    var assetStatus = $('#assetStatus').combobox("getValue");
    var manageDeptId = $('#manageDeptId').combotree("getValue");
    var managerId = $('#managerId').combobox("getValue");
    var useDeptId = $('#useDeptId').combotree("getValue");
    var userId = $('#userId').combobox("getValue");
    var produceTypeStr= $('#produceType').combobox("getValue");

    var openUrl = 'asset/export_asset_stan_book?assetCode=' + assetCode
        + '&assetStatus=' + assetStatus
        + '&manageDeptId=' + manageDeptId
        + '&managerId=' + managerId
        + '&useDeptId=' + useDeptId
        + '&produceTypeStr=' + produceTypeStr
        + '&userId=' + userId;

    window.location.href = openUrl;
}

function fillData() {
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
    $("#assetModelBtn").attr("disabled", true); //资产模式按钮不可点击
    fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_PRODUCE_TYPE', 'produceType');

    fillData();

    $.ajax({
        url: 'base/dept_user_combobox',
        type: 'POST',
        data: {deptId: $('#useDept').val()},
        success: function (data) {
            $('#userId').combobox('loadData', data);
            $('#userId').combobox('setValue', $('#user').val());
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });

    var data;
    var dynaViewId = $('#dynaViewId').val();
    var flag = $('#returnFromUpdateBatchFlag').val();

    if (!dynaViewId)
        dynaViewId = "";
    else {
        //设置页面不可编辑属性
        if ($('#searchQuerys input[name="materialCode"]').val())
            $('#searchQuerys input[name="materialCode"]').attr('disabled', true);

        if ($('#searchQuerys input[name="assetCode"]').val())
            $('#searchQuerys input[name="assetCode"]').attr('disabled', true);

        if ($('#searchQuerys input[name="assetChsName"]').val())
            $('#searchQuerys input[name="assetChsName"]').attr('disabled', true);

        if ($('#assetStatus').val())
            $('#assetStatus').combobox({disabled: true});

        if ($('#assetType').val())
            $('#assetType').combobox({disabled: true});

        if ($('#searchText').val())
            $('#searchText').attr('disabled', true);

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

        if ($('#manageDeptId').val())
            $('#manageDeptId').combobox({disabled: true});

        if ($('#managerId').val())
            $('#managerId').combobox({disabled: true});

        if ($('#useDeptId').val())
            $('#useDeptId').combobox({disabled: true});

        if ($('#userId').val())
            $('#userId').combobox({disabled: true});
    }

    if (flag) {
        data = {
            "materialCode": $('#searchQuerys input[name="materialCode"]').val(),//物资编码
            "assetCode": $('#searchQuerys input[name="assetCode"]').val(),//资产编码
            "assetChsName": $('#searchQuerys input[name="assetChsName"]').val(),//资产名称
            "assetStatus": $('#assetStatus').combobox("getValue"),//资产状态
            "assetType": $('#assetTypeDialog input[name="rowIdHidden"]').val(),
            "savePlaceId": $('#instSiteCodeDialog input[name="rowIdHidden"]').val(),
            "manageDeptId": $('#manageDeptId').combobox("getValue"),//管理部门
            "managerId": $('#managerId').combobox("getValue"),//管理员
            "useDeptId": $('#useDeptId').combobox("getValue"),//使用部门
            "userId": $('#userId').combobox("getValue"),//使用人	
        }
    } else {
        data = {"dynaViewId": dynaViewId};
    }
    if (null != flag && 'seal' == flag) {
        sealDataGrid();
        return;
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
        columns: [[
            {field: 'id', checkbox: true},
            {
                field: 'assetCode', title: '资产编码', formatter: function (value, row, index) {
                    return '<a href="javascript:void(0);" onClick="goToViewAssetPageSubmit(\'' + row.id + '\');">' + value + '</a>';
                }
            },
            {field: 'produceStr', title: '物资的类型'},
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
})


//封存视图datagrid刷新
function sealDataGrid() {
    //$("#assetStatus").combobox({disabled: true});
    $('#assetStatus').combobox("setValue", '2');
    var data = {};
    data.assetStatus = "2"; //封存状态
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
        columns: [[
            {field: 'id', checkbox: true},
            {
                field: 'assetCode', title: '资产编码', formatter: function (value, row, index) {
                    return '<a href="javascript:void(0);" onClick="goToViewAssetPageSubmit(\'' + row.id + '\');">' + value + '</a>';
                }
            },
            {field: 'produceStr', title: '物资的类型'},
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
            {field: "assetStatusStr", hidden: true}
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });
}
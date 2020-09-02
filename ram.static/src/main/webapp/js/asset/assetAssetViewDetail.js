function goToUpdateBatch() {
    var assetIdList = new Array();
    var data = $('#assetDetailTable').datagrid('getSelections');
    for (var row in data) {
        assetIdList.push(data[row].id);
    }
    $('#assetIdList').val(assetIdList);
    console.log("assetIdList=" + assetIdList);
    $('#idListForm').submit();
}

function backToPrePage() {
    var materialCode = $('#materialCode').val();
    var materialSearchCode = $('#materialSearchCode').val();
    var assetCode = $('#assetCode').val();
    var assetChsName = $('#assetChsName').val();
    var assetStatus = $('#assetStatus').val();
    javascript:window.location.href = 'asset_stan_book_material?assetCodeSubmit=' + assetCode + '&assetStatusSubmit=' + assetStatus + '&materialCodeSubmit=' + materialCode + '&assetChsNameSubmit=' + assetChsName + '&materialSearchCodeSubmit=' + materialSearchCode;
}

$(function () {
    var assetIdSet = $('#assetIdSet').val();
    var assetCode = $('#assetCode').val();
    var assetChsName = $('#assetChsName').val();
    var assetStatus = $('#assetStatus').val();
    $('#assetDetailTable').datagrid({
        url: 'asset/asset_detail_datagrid',
        method: 'POST',
        striped: true,
        rownumbers: true,
        pagination: true,
        fit: true,
        pageSize: 20,
        toolbar: '#divbt',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {id: assetIdSet},
        columns: [[
            {field: 'id', checkbox: true},
            {field: 'assetCode', title: '资产编码'},
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
            {field: 'assetStatus', title: '资产状态'}
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });
})

$(function () {
    fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_STATUS', 'assetStatus');

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
//			{field:'id', checkbox:true},
            {
                field: 'assetCode', title: '资产编码', formatter: function (value, row, index) {
                    return '<a onclick="goToViewAssetPageSubmit(\'' + row.id + '\')">' + value + '</a>';
                }
            },
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
            {field: 'savePlaceName', title: '安装位置'},
            {field: 'buyDate', title: '购置日期'},
            {field: 'manageDeptStr', title: '主管部门'},
            {field: 'managerStr', title: '资产管理员'},
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

    //审批记录 暂未完成
    $('#dataGridTable0').datagrid({
        //url: 'asset/history_datagrid',
        //fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        pagination: true,
        pageSize: 10,
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {
            //historyType: '0',
            //assetId: assetId
        },
        columns: [[
            {field: 'createTimestamp', title: '审批环节'},
            {field: 'createUserStr', title: '经办人'},
            {field: 'modifyContent', title: '审批时间'},
            {field: 'modifyContent', title: '审批意见'},
            {field: 'modifyContent', title: '审批结果'}
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });
})

function goToViewAssetPageSubmit(assetId) {
    window.open('asset_view?assetId=' + assetId);
}
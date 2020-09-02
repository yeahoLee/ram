$(function () {
    var recId = $('#receiptDtoId').val();
    $('#dataGridTable').datagrid({
        url: 'receipt/get_materials',
        fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        fit: false,
        singleSelect: true,
        queryParams: {recId: recId},
        title: '资产清单',
        loadMsg: '程序处理中，请稍等...',
        columns: [[
            {field: 'assetCode', title: '资产编码'},
            {field: 'materialCode', title: '物资编码'},
            {field: 'combinationAssetName', title: '资产名称'},
            {field: 'assetTypeStr', title: '资产类型'},
            {field: 'specAndModels', title: '规格型号'},
            {field: 'seriesNum', title: '序列号'},
            {field: 'unitOfMeasStr', title: '计量单位'},
            {field: 'assetBrand', title: '品牌'},
            {field: 'purcPrice', title: '采购价'},
            {field: 'equiOrigValue', title: '资产原值'},
            {field: 'techPara', title: '技术参数'},
            {field: 'remark', title: '备注'},//备注
            {field: 'companyStr', title: '所属公司'},
            {field: 'belongLineStr', title: '所属线路'},
            {field: 'buyDate', title: '购置日期'},
            {field: 'manageDeptStr', title: '主管部门'},
            {field: 'managerStr', title: '资产管理员'},
            {field: 'savePlaceStr', title: '安装位置'},
            {field: 'assetSource', title: '资产来源'},
            {field: 'contractNum', title: '合同编号'},
            {field: 'sourceUser', title: '联系人'},
            {field: 'sourceContactInfo', title: '联系方式'},
            {field: 'tendersNum', title: '标段编号'},
            {field: 'mainPeriod', title: '维保期'},
            {field: 'prodTime', title: '出厂日期'}
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });
})
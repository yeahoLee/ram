function openAdvaSearchDialog() {
    var advaSearch = $("#advaSearch").css("display");
    if (advaSearch == 'none')
        $('#advaSearch').show();
    else
        $('#advaSearch').hide();
}

function backToLastPage() {
    var url = $('#backToLastPageUrl').val();
    var materialCodeSubmit = $('#materialCodeSubmit').val();//物资编码
    var assetCodeSubmit = $('#assetCodeSubmit').val();//资产编码
    var assetChsNameSubmit = $('#assetChsNameSubmit').val();//资产名称
    var assetStatusSubmit = $('#assetStatusSubmit').val();//资产状态
    var assetTypeSubmit = $('#assetTypeSubmit').val();//资产类型
    var savePlaceIdSubmit = $('#savePlaceIdSubmit').val();
    var manageDeptIdSubmit = $('#manageDeptIdSubmit').val();//管理部门
    var managerIdSubmit = $('#managerIdSubmit').val();//管理员
    var useDeptIdSubmit = $('#useDeptIdSubmit').val();//使用部门
    var userIdSubmit = $('#userIdSubmit').val();//使用人
    var codeAndNameSubmit = $('#codeAndNameSubmit').val();

    //window.location.href ='./'+url;
    window.location.href = './' + url + '?materialCodeSubmit=' + materialCodeSubmit + '&assetCodeSubmit=' + assetCodeSubmit
        + '&assetChsNameSubmit=' + assetChsNameSubmit + '&assetStatusSubmit=' + assetStatusSubmit
        + '&assetTypeSubmit=' + assetTypeSubmit + '&savePlaceIdSubmit=' + savePlaceIdSubmit
        + '&manageDeptIdSubmit=' + manageDeptIdSubmit + '&managerIdSubmit=' + managerIdSubmit
        + '&useDeptIdSubmit=' + useDeptIdSubmit + '&userIdSubmit=' + userIdSubmit + '&backToLastPageUrl=' + url
        + '&codeAndNameSubmit=' + codeAndNameSubmit;
}

function searchByQuerys() {
    $('#dataGridTable').datagrid('load', {
        assetCode: $('#searchQuerys input[name="assetCode"]').val(),
        assetChsName: $('#searchQuerys input[name="assetChsName"]').val(),
        assetStatus: '0,1,7',//$('#assetStatus').combobox("getValue"),
        manageDeptId: $('#manageDeptId').combobox("getValue"),
        managerId: $('#managerId').combobox("getValue"),
        useDeptId: $('#useDeptId').combobox("getValue"),
        userId: $('#userId').combobox("getValue"),
    })
}

function addAssetToList() {
    var assetIdList = new Array();
    var data = $('#dataGridTable').datagrid('getSelections');

    if (data.length == 0)
        $.messager.alert('错误', '选择一个添加项！', 'error');

    for (var row in data) {
        assetIdList.push(data[row].id);
    }

    //累加
    var inputVar = $('#assetList input[name="assetIdList"]').val();
    if (inputVar)
        $('#assetList input[name="assetIdList"]').val(inputVar + "," + assetIdList);
    else
        $('#assetList input[name="assetIdList"]').val(assetIdList);
    //查询
    $('#batchUpdateDataGridTable').datagrid('load', {
        assetIdListStr: $('#assetList input[name="assetIdList"]').val()
    })
    //获取去重后的idList
    data = $('#batchUpdateDataGridTable').datagrid('getData');
    for (var row in data.rows) {
        assetIdList.push(data.rows[row].id);
    }
    $('#assetList input[name="assetIdList"]').val(assetIdList);
}

function removeAssetToList() {
    var assetIdList = new Array();
    var data = $('#batchUpdateDataGridTable').datagrid('getSelections');

    if (data.length == 0)
        $.messager.alert('错误', '选择一个移除项！', 'error');

    //删除选中行
    for (var i = data.length - 1; i >= 0; i--) {
        var rowIndex = $('#batchUpdateDataGridTable').datagrid('getRowIndex', data[i]);
        $('#batchUpdateDataGridTable').datagrid('deleteRow', rowIndex);
    }

    //获取剩余行
    data = $('#batchUpdateDataGridTable').datagrid('getData');
    for (var row in data.rows) {
        assetIdList.push(data.rows[row].id);
    }
    //list转Str查询
    $('#assetList input[name="assetIdList"]').val(assetIdList);
    $('#batchUpdateDataGridTable').datagrid('load', {
        assetIdListStr: $('#assetList input[name="assetIdList"]').val()
    })
}

function updateAssetBatchSubmit() {
    $('#saveBtn').attr('disabled', "true");

    var assetIdListStr = $('#assetList input[name="assetIdList"]').val();
    if (typeof assetIdListStr == "undefined" || assetIdListStr == null || assetIdListStr == "") {
        $.messager.alert('提示', '请添加资产！', 'info');
        $('#saveBtn').removeAttr("disabled");
        return;
    }
    //基本信息Start
    // var specAndModels = $('#baseInfo input[name="specAndModels"]').val();
    var seriesNum = $('#baseInfo input[name="seriesNum"]').val();
    var unitOfMeasId = $('#baseInfo input[name="unitOfMeasId"]').val();
    var assetBrand = $('#baseInfo input[name="assetBrand"]').val();
    var purcPrice = $('#baseInfo input[name="purcPrice"]').val();
    var techPara = $('#techPara').val();
    var remark = $('#remark').val();
    //基本信息End

    //延展信息Start
    var buyDate = $('#buyDate').datebox('getText');
    var contractNum = $('#extendInfo input[name="contractNum"]').val();
    var sourceUser = $('#extendInfo input[name="sourceUser"]').val();
    var sourceContactInfo = $('#extendInfo input[name="sourceContactInfo"]').val();
    var prodTime = $('#prodTime').datebox('getText');
    var mainPeriod = $('#mainPeriod').datebox('getText');
    //延展信息End

    $.ajax({
        url: 'asset/batch_update_asset',
        type: 'POST',
        data: {
            assetIdListStr: assetIdListStr,
            // specAndModels: specAndModels,
            seriesNum: seriesNum,
            unitOfMeasId: unitOfMeasId,
            assetBrand: assetBrand,
            purcPrice: purcPrice,
            techPara: techPara,
            remark: remark,

            buyDate: buyDate,
            contractNum: contractNum,
            sourceUser: sourceUser,
            sourceContactInfo: sourceContactInfo,
            prodTime: prodTime,
            mainPeriod: mainPeriod
        },
        success: function (data) {
            $('#saveBtn').removeAttr('disabled');
            $.messager.alert('提示', '更新成功！', 'info', function () {
                $('#batchUpdateDataGridTable').datagrid('reload');
            });
        },
        error: function (data) {
            $('#saveBtn').removeAttr('disabled');
            AjaxErrorHandler(data);
        }
    });
}

$(function () {
    $('#useDeptId').combobox({
        url: 'base/dept_combo_by_querys?showType=0',
        mode: 'remote',
        method: 'POST',
        onSelect: function (record) {
            $.ajax({
                url: 'base/dept_user_combobox',
                type: 'POST',
                data: {deptId: record.value},
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

    //fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_STATUS', 'assetStatus');
    var assetIdList = $('#assetList input[name="assetIdList"]').val();

    $('#batchUpdateDataGridTable').datagrid({
        url: 'asset/asset_batch_update_datagrid',
//			fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
//		pagination: true,
//        pageSize: 10,
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {assetIdListStr: assetIdList,isFilt:1},
        columns: [[
            {field: 'id', checkbox: true},
//				{field: 'materialCode', title: '物资编码', formatter:function(value, row, index){
//					return '<a href="javascript:void(0);" onClick="goToViewAssetPageSubmit(\''+row.id+'\');">'+row.id+'</a>';
//				}},
            //基础信息
//			{field: 'materialCode', title: '物资编号'},
            {field: 'assetCode', title: '资产编码'},
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
            {field: 'prodTime', title: '出厂日期'},
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });

    // var assetIdList = $('#assetIdList').val();
    // $('#batchUpdateDataGridTable').datagrid('load', {
    //     assetIdListStr: assetIdList
    // })

    $('#dataGridTable').datagrid({
        url: 'asset/asset_datagrid',
//			fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        pagination: true,
        pageSize: 10,
        queryParams: {assetStatus: '0,1,6,7'},
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        columns: [[
            {field: 'id', checkbox: true},
//				{field: 'materialCode', title: '物资编码', formatter:function(value, row, index){
//					return '<a href="javascript:void(0);" onClick="goToViewAssetPageSubmit(\''+row.id+'\');">'+row.id+'</a>';
//				}},
            {field: 'assetCode', title: '资产编码'},
            {field: 'combinationAssetName', title: '资产名称'},
            {field: 'assetTypeStr', title: '资产类型'},
            {field: 'specAndModels', title: '规格型号'},
            {field: 'assetBrand', title: '品牌'},
            {field: 'unitOfMeasStr', title: '计量单位'},
//				{field: '',title: '安装位置'},
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
})

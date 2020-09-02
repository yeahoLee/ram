function updateAsset() {
    $('#saveBtn').attr('disabled', "true");

    //基本信息Start
    var id = $('#assetId').val();
//	var assetCode = $('#baseInfo input[name="assetCode"]').val();
    var materialCode = $('#baseInfo input[name="materialCode"]').val();
//	var assetLeave1 = $('#baseInfo input[name="assetLeave1"]').val();
//	var assetLeave2 = $('#baseInfo input[name="assetLeave2"]').val();
//	var assetLeave3 = $('#baseInfo input[name="assetLeave3"]').val();
//	var assetLeave4 = $('#baseInfo input[name="assetLeave4"]').val();
//	var assetChsName = $('#baseInfo input[name="assetChsName"]').val();
//	var assetEngName = $('#baseInfo input[name="assetEngName"]').val();
    var assetType = $('#baseInfo input[name="assetType"]').val();
//	var assetType = $("#assetType").combobox('getValue');
    var specAndModels = $('#baseInfo input[name="specAndModels"]').val();
    var seriesNum = $('#baseInfo input[name="seriesNum"]').val();
    var unitOfMeasId = $('#baseInfo input[name="unitOfMeasId"]').val();
//	var quantity = $('#baseInfo input[name="quantity"]').val();
    var assetBrand = $('#baseInfo input[name="assetBrand"]').val();

    var purcPrice = $('#baseInfo input[name="purcPrice"]').val();
    var equiOrigValue = $('#baseInfo input[name="equiOrigValue"]').val();
//	var pracServLifeYears = $('#baseInfo input[name="pracServLifeYears"]').val();
//	var alreadyDeprMoney = $('#baseInfo input[name="alreadyDeprMoney"]').val();
//	var monthDeprMoney = $('#baseInfo input[name="monthDeprMoney"]').val();
//	var residualValue = $('#baseInfo input[name="residualValue"]').val();
//	var netWorth = $('#baseInfo input[name="netWorth"]').val();
    var techPara = $('#techPara').val();
    var remark = $('#remark').val();
    //基本信息End

    //延展信息Start
    var buyDate = $('#buyDate').datebox('getText');
    var contractNum = $('#extendInfo input[name="contractNum"]').val();
    var tendersNum = $('#extendInfo input[name="tendersNum"]').val();
    var companyId = $('#extendInfo input[name="companyId"]').val();
    var manageDeptId = $('#extendInfo input[name="manageDeptId"]').val();
    var managerId = $('#extendInfo input[name="managerId"]').val();
//	var useDeptId = $('#extendInfo input[name="useDeptId"]').val();
//	var userId = $('#extendInfo input[name="userId"]').val();
    var belongLine = $('#extendInfo input[name="belongLine"]').val();
    var assetLineId = $('#extendInfo input[name="assetLineId"]').val();
//	var savePlaceId = $('#extendInfo input[name="savePlaceId"]').val();
//	var buildNumId = $('#extendInfo input[name="buildNumId"]').val();
//	var floorNumId = $('#extendInfo input[name="floorNumId"]').val();
//	var roomNumId = $('#extendInfo input[name="roomNumId"]').val();
    var assetSource = $('#extendInfo input[name="assetSource"]').val();
//	var sourceType = $('#extendInfo input[name="sourceType"]').val();
    var sourceUser = $('#extendInfo input[name="sourceUser"]').val();
    var sourceContactInfo = $('#extendInfo input[name="sourceContactInfo"]').val();
    var prodTime = $('#prodTime').datebox('getText');
    var mainPeriod = $('#mainPeriod').datebox('getText');
//	var savePlaceId =$('#instSiteCodeDialog input[name="rowIdHidden"]').val();
    var savePlaceId = $('#instSiteCodeDialog input[name="savePlaceIdHidden"]').val();
    //延展信息End
    $.ajax({
        url: 'asset/update_asset',
        type: 'POST',
        data: {
            id: id,
            materialCode: materialCode,
            assetType: assetType,
            specAndModels: specAndModels,
            seriesNum: seriesNum,
            unitOfMeasId: unitOfMeasId,
            assetBrand: assetBrand,
            purcPrice: purcPrice,
            equiOrigValue: equiOrigValue,
            techPara: techPara,
            remark: remark,

            buyDate: buyDate,
            contractNum: contractNum,
            tendersNum: tendersNum,
            companyId: companyId,
            manageDeptId: manageDeptId,
            managerId: managerId,
            belongLine: belongLine,
            assetLineId: assetLineId,
            assetSource: assetSource,
            sourceUser: sourceUser,
            sourceContactInfo: sourceContactInfo,
            prodTime: prodTime,
            mainPeriod: mainPeriod,
            savePlaceId: savePlaceId
        },
        success: function (data) {
            $.messager.alert('提示', '更新成功！', 'info', function () {
                location.reload();
            });
        },
        error: function (data) {
            $('#saveBtn').removeAttr('disabled');
            AjaxErrorHandler(data);
        }
    });
}

function uploadFileSubmit() {
    $('#uploadSaveBtn').attr('disabled', "true");
    var files = $('input[name="uploadFileData"]').prop('files');
    if (files.length == 0) {
        $('#uploadSaveBtn').removeAttr("disabled");
        $.messager.alert('提示', '请选择上传文件！', 'error');
        return;
    }
    $('#uploadFileForm').submit();
}

function downloadFileSubmit(id) {
    window.location.href = 'asset/download_upload_file?uploadId=' + id;
}

function deleteFileSubmit(id) {
    $.messager.confirm('确定', '确定要删除吗?', function (r) {
        if (r) {
            $.ajax({
                url: 'asset/delete_upload_file',
                type: 'POST',
                data: {
                    uploadId: id
                },
                success: function (data) {
                    $.messager.alert('提示', '删除成功！', 'info', function () {
                        $('#uploadFileDataGridTable').datagrid('reload');
                    });
                },
                error: function (data) {
                    AjaxErrorHandler(data);
                }
            });
        }
    });
}

function showCodeAndName(id, codeAndName) {
    $('#showCodeAndData').text(codeAndName);
    $('#instSiteCodeDialog input[name="savePlaceIdHidden"]').val(id);
}

function closeDialog() {
    $('#instSiteCodeDialog').dialog('close');
}

/*function showCodeAndName(){
	var row = $('#dataGridTable').datagrid('getSelected');
	if (row){
	    $('#instSiteCodeDialog').dialog('close');
	    $('#showCodeAndData').text(row.code+" "+row.chsName);
	    //$('#instSiteCodeDialog input[name="rowIdHidden"]').val(row.id);
	    $('#extendInfo input[name="savePlaceIdHidden"]').val(row.id)
	}else{
		 $.messager.alert('错误', '请选择一行数据！');
	}
}*/

/*function getAssetPlaceByCode(){
	var assetLineIdHidden = $('#instSiteCodeDialog input[name="assetLineIdHidden"]').val();
	var stationIdHidden = $('#instSiteCodeDialog input[name="stationIdHidden"]').val();
	var buildNumIdHidden = $('#instSiteCodeDialog input[name="buildNumIdHidden"]').val();
	var floorNumIdHidden = $('#instSiteCodeDialog input[name="floorNumIdHidden"]').val();
	var combiCondition = assetLineIdHidden+stationIdHidden+buildNumIdHidden+floorNumIdHidden;
	
	$('#dataGridTable').datagrid({
		url: 'base/get_place_datagrid',
		method: 'POST',
		striped: true,
		rownumbers: true,
		pagination:true,
		singleSelect:true,
		pageSize:10,
		onClickRow:function(rowIndex, rowData){
			$('#instSiteCodeDialog input[name="rowId"]').val(rowData.id);
		},
        toolbar: '#dataGridTableButtons',
        queryParams:{
        	combiCondition: combiCondition,
        },
        loadMsg: '程序处理中，请稍等...',
		columns:[[
			//{field: 'id',title: ''},
			{field: 'code', title: '编码'},
            {field: 'chsName',title: '中文名'},
        ]],
        onLoadError:function(data){
			AjaxErrorHandler(data);
		}
    });
}*/

$(function () {
    var assetModelId = $('#assetId').val();
    fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_TYPE', 'assetType');
    $("#companyId").combobox({
        onSelect: function (record) {
            $.ajax({
                url: 'base/dept_combo',
                type: 'POST',
                data: {
                    parentDeptId: record.value
                },
                success: function (data) {
                    $('#manageDeptId').combobox('clear');
                    $('#manageDeptId').combobox('loadData', data);
                },
                error: function (data) {
                    AjaxErrorHandler(data);
                }
            });
        }
    });

    $('#uploadFileDataGridTable').datagrid({
        url: 'asset/upload_file_datagrid',
        method: 'POST',
        striped: true,
        rownumbers: true,
        singleSelect: true,
        pagination: true,
        pageSize: 10,
        queryParams: {assetModelId: assetModelId},
        loadMsg: '程序处理中，请稍等...',
        columns: [[
            {
                field: 'id', title: '操作', formatter: function (value) { //value, row, index
                    return '<a href="javascript:void(0);" onClick="deleteFileSubmit(\'' + value + '\');">删除附件</a>';
                }
            },
            {
                field: 'fileName', title: '文件名', formatter: function (value, row) {
                    return '<a href="javascript:void(0);" onClick="downloadFileSubmit(\'' + row.id + '\')">' + value + '</a>';
                }
            },
            {field: 'fileSize', title: '文件大小'},
            {field: 'fileSpec', title: '文件说明'},
            {field: 'createTimestamp', title: '上传时间'},
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });
});
function createAsset() {
    $('#saveBtn').attr('disabled', "true");

    //基本信息Start
//	var assetCode = $('#baseInfo input[name="assetCode"]').val();
    var materialCode = $('#baseInfo input[name="materialCode"]').val();
//	var assetLeave1 = $('#baseInfo input[name="assetLeave1"]').val();
//	var assetLeave2 = $('#baseInfo input[name="assetLeave2"]').val();
//	var assetLeave3 = $('#baseInfo input[name="assetLeave3"]').val();
//	var assetLeave4 = $('#baseInfo input[name="assetLeave4"]').val();
//	var assetChsName = $('#baseInfo input[name="assetChsName"]').val();
//	var assetEngName = $('#baseInfo input[name="assetEngName"]').val();
    var assetType = $('#baseInfo input[name="assetType"]').val();
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
    var savePlaceId = $('#instSiteCodeDialog input[name="rowIdHidden"]').val();
    //延展信息End

    $.ajax({
        url: 'asset/create_asset',
        type: 'POST',
        data: {
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
            $.messager.alert('提示', '新建成功！', 'info', function () {
                window.location.href = 'asset_index';
            });
        },
        error: function (data) {
            $('#saveBtn').removeAttr('disabled');
            AjaxErrorHandler(data);
        }
    });
}

//通过物资编码查询资产类别和资产名称
function findTypeByMaterialCode() {
    var materialCode = $('#baseInfo input[name="materialCode"]').val();

    $('#assetName').text("");
    $('#assetTypeName').text("");

    $.ajax({
        url: 'base/find_asset_type_by_code',
        type: 'POST',
        dataType: 'json',
        data: {
            materialCode: materialCode
        },
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
        data: {
            materialCode: materialCode
        },
        success: function (data) {
            $('#assetName').text(data.assetName);
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function showCodeAndName(id, codeAndName) {
    $('#showCodeAndData').text(codeAndName);
    $('#instSiteCodeDialog input[name="rowIdHidden"]').val(id);
}

function closeDialog() {
    $('#instSiteCodeDialog').dialog('close');
}

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
        toolbar: '#dataGridTableButtons',
        queryParams:{
        	combiCondition: combiCondition,
        },
        loadMsg: '程序处理中，请稍等...',
		columns:[[
			{field: 'code', title: '编码'},
            {field: 'chsName',title: '中文名'},
        ]],
        onLoadError:function(data){
			AjaxErrorHandler(data);
		}
    });
}*/

$(function () {
    fillEnumCombo('ram.asset.service.IAssetService', 'ASSET_TYPE', 'assetType');

    $("#companyId").combobox({
        onSelect: function (record) {
            $.ajax({
                url: 'base/dept_combo_by_pdid',
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
})

//	//资产类型
//	$.ajax({
//		url: './ram/enum_combo',
//		type: 'GET',
//		data: {
//			moduleName: 'ram.asset.service.IAssetService',
//			methodName: 'ASSET_TYPE',
//        },
//		success: function(result){
//			$('#assetType').combobox('loadData',result);
//		},
//		error: function(result) {
//			AjaxErrorHandler(result);
//		}
//	});
//	
//	//来源方式
//	$.ajax({
//		url: './ram/enum_combo',
//		type: 'GET',
//		data: {
//			moduleName: 'ram.asset.service.IAssetService',
//			methodName: 'SOURCE_TYPE',
//        },
//		success: function(result){
//			$('#sourceType').combobox('loadData',result);
//		},
//		error: function(result) {
//			AjaxErrorHandler(result);
//		}
//	});
//	
//	//来源方式2(没有建设移交和采购)
//	$.ajax({
//		url: './ram/enum_combo2',
//		type: 'GET',
//		data: {
//			moduleName: 'ram.asset.service.IAssetService',
//			methodName: 'SOURCE_TYPE',
//        },
//		success: function(result){
//			$('#sourceType2').combobox('loadData',result);
//		},
//		error: function(result) {
//			AjaxErrorHandler(result);
//		}
//	});
//	
//	//新增单状态
//	$.ajax({
//		url: './ram/enum_combo',
//		type: 'GET',
//		data: {
//			moduleName: 'ram.asset.service.IMaterialReceiptService',
//			methodName: 'RECEIPT_STATUS',
//        },
//		success: function(result){
//			$('#receiptStatus').combobox('loadData',result);
//		},
//		error: function(result) {
//			AjaxErrorHandler(result);
//		}
//	});
//	
//	//资产状态
//	$.ajax({
//		url: './ram/enum_combo',
//		type: 'GET',
//		data: {
//			moduleName: 'ram.asset.service.IAssetService',
//			methodName: 'ASSET_STATUS',
//        },
//		success: function(result){
//			$('#assetStatus').combobox('loadData',result);
//		},
//		error: function(result) {
//			AjaxErrorHandler(result);
//		}
//	});

//function fillSavePlaceCombobox(dialogName, comboboxId1, comboboxId2, comboboxId3, comboboxId4, 
//		textboxName1, textboxName2, textboxName3, textboxName4){
//	$("#assetLineId").combobox({
//        onSelect: function (record) {
//            $.ajax({
//            	url:'base/common_combo_by_parentid',
//                type: 'POST',
//                data: {
//                	parentId: record.value,
//                	showCode: true
//                },
//                success: function(data){
//                	$('#instSiteCodeDialog input[name="assetLineIdHidden"]').val(record.value1);
//                	$('#instSiteCodeDialog input[name="stationIdHidden"]').val();
//                	$('#instSiteCodeDialog input[name="buildNumIdHidden"]').val();
//                	$('#instSiteCodeDialog input[name="floorNumIdHidden"]').val();
//                    $('#stationId').combobox('clear');
//                    $('#stationId').combobox('loadData', data);
//                    getAssetPlaceByCode();
//                },
//                error: function(data){
//                    AjaxErrorHandler(data);
//                }
//            });
//        }
//    });

//	$('#'+comboboxId1).combobox({
//        onSelect: function (record) {
//            $.ajax({
//            	url:'base/common_combo_by_parentid',
//                type: 'POST',
//                data: {
//                	parentId: record.value,
//                	showCode: true
//                },
//                success: function(data){
//                	$('#'+dialogName+' input[name="'+textboxName1+'"]').val(record.value1);
//                	$('#'+dialogName+' input[name="'+textboxName2+'"]').val();
//                	$('#'+dialogName+' input[name="'+textboxName3+'"]').val();
//                	$('#'+dialogName+' input[name="'+textboxName4+'"]').val();
//                    $('#'+comboboxId2).combobox('clear');
//                    $('#'+comboboxId2).combobox('loadData', data);
//                    getAssetPlaceByCode();
//                },
//                error: function(data){
//                    AjaxErrorHandler(data);
//                }
//            });
//        }
//    });
//	
//	$('#'+comboboxId2).combobox({
//        onSelect: function (record) {
//            $.ajax({
//                url: 'base/common_combo',
//                type: 'POST',
//                data: {
//                	commonCodeType: "PLACE_CODE_LV3",
//                	showCode: true
//                },
//                success: function(data){
//                	$('#'+dialogName+' input[name="'+textboxName2+'"]').val(record.value1);
//                	$('#'+dialogName+' input[name="'+textboxName3+'"]').val();
//                	$('#'+dialogName+' input[name="'+textboxName4+'"]').val();
//                    $('#'+comboboxId3).combobox('clear');
//                    $('#'+comboboxId4).combobox('clear');
//                    $('#'+comboboxId3).combobox('loadData', data);
//                    getAssetPlaceByCode();
//                },
//                error: function(data){
//                    AjaxErrorHandler(data);
//                }
//            });
//        }
//    });
//	
//	$('#'+comboboxId3).combobox({
//        onSelect: function (record) {
//            $.ajax({
//                url: 'base/common_combo_by_parentid',
//                type: 'POST',
//                data: {
//                	parentId: record.value,
//                	showCode: true
//                },
//                success: function(data){
//                	$('#'+dialogName+' input[name="'+textboxName3+'"]').val(record.value1);
//                	$('#'+dialogName+' input[name="'+textboxName4+'"]').val();
//                	$('#'+comboboxId4).combobox('clear');
//                    $('#'+comboboxId4).combobox('loadData', data);
//                    getAssetPlaceByCode();
//                },
//                error: function(data){
//                    AjaxErrorHandler(data);
//                }
//            });
//        }
//    });
//	
//	$('#'+comboboxId4).combobox({
//        onSelect: function (record) {
//        	$('#'+dialogName+' input[name="'+textboxName4+'"]').val(record.value1);
//            getAssetPlaceByCode();
//        }
//    });
//}

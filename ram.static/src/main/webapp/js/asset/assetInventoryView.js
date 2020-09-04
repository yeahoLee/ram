function lookAssetInventory(id) {
    window.location.href = 'myassetinventory_view?id=' + id;
}

$(function () {
    var AssetInventoryId = $('#AssetInventoryId').val();
    $('#myAssetInventoryDataGrid').datagrid({
        url: 'assetInventory/inventorytemp_find',
        //fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        pagination: true,
        pageSize: 20,
        idField: 'id',
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {
            assetInventoryId: AssetInventoryId
        },
        columns: [[
            {
                field: 'myAssetInventoryCode', title: '盘点单编号', formatter: function (value, row) {
                    return '<a href="javascript:void(0);" onClick="lookAssetInventory(\'' + row.id + '\');">' + value + '</a> ';
                }
            },
            {field: 'managerDeptStr', title: '资产管理部门'},
            {field: 'managerStr', title: '资产管理员'},
            {field: 'quantity', title: '盘点资产数量', align: 'center'},
            {field: 'inventoryLoss', title: '盘亏'},
            {field: 'inventoryProfit', title: '盘盈'},
            {field: 'myinventoryStatus', title: '盘点单状态'}
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });

    $('#assetInventoryScope').datagrid({
        url: 'assetInventory/inventoryscope_datagrid',
        // fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        pagination: true,
        pageSize: 10,
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {
            id: AssetInventoryId,
        },
        columns: [[{
            field: 'inventoryContent',
            width: 450,
            title: '盘点内容'
        }, {
            field: 'inventoryNum',
            title: '盘点数量'
        },
            {
                field: 'id',    hidden: true
            },

            // 延申信息
            {
                field: 'manageDeptId',
                title: '管理部门id',
                hidden: true
            }, {
                field: 'useDeptId',
                title: '使用部门id',
                hidden: true
            }, {
                field: 'assetStatus',
                title: '资产状态',
                hidden: true
            }, {
                field: 'managerId',
                title: '管理员id',
                hidden: true
            }, {
                field: 'savePlaceId',
                title: '具体位置id',
                hidden: true
            },
            // 安装未写
            {
                field: 'assetType',
                title: '资产类型',
                hidden: true
            }, {
                field: "userId",
                title: "使用人",
                hidden: true
            }]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });

    $('#assetInventoryDataGrid').datagrid({
        url: 'assetInventory/inventorytemp_datagrid',
        // fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        pagination: true,
        pageSize: 10,
        toolbar: '#dataGridTableButtons',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {
            assetInventoryId: AssetInventoryId,
        },
        columns: [[{
            field: 'id',
            hidden: true
        }, {
            field: 'assetCode',
            title: '资产编码'
        }, {
            field: 'assetTypeStr',
            title: '资产类别'
        }, {
            field: 'specAndModels',
            title: '规格型号'
        }, {
            field: 'seriesNum',
            title: '序列号'
        }, {
            field: 'unitOfMeasStr',
            title: '计量单位'
        }, {
            field: 'assetBrand',
            title: '品牌'
        }, {
            field: 'purcPrice',
            title: '采购价'
        }, {
            field: 'equiOrigValue',
            title: '资产原值'
        }, {
            field: 'techPara',
            title: '技术参数'
        }, {
            field: 'remark',
            title: '备注'
        },
            // 延申信息
            {
                field: 'companyStr',
                title: '所属公司'
            }, {
                field: 'belongLineStr',
                title: '所属线路/建筑'
            }, {
                field: 'buyDate',
                title: '购置日期'
            }, {
                field: 'manageDeptStr',
                title: '主管部门'
            }, {
                field: 'managerStr',
                title: '资产管理员'
            },
            // 安装未写
            {
                field: 'assetSource',
                title: '资产来源'
            }, {
                field: 'contractNum',
                title: '合同编号'
            }, {
                field: 'tendersNum',
                title: '标段编号'
            }, {
                field: 'mainPeriod',
                title: '维保期'
            }, {
                field: 'sourceUser',
                title: '联系人'
            }, {
                field: 'sourceContactInfo',
                title: '联系方式'
            }, {
                field: 'prodTime',
                title: '出厂日期'
            }, {
                field: "savePlaceStr",
                title: "使用位置"
            }, {
                field: "savePlaceId",
                hidden: true
            }, {
                field: "userId",
                title: "使用人",
                hidden: true
            }]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });

})
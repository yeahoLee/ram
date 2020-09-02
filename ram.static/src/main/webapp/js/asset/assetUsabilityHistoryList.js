function searchByQuerys() {
    $('#dataGridTable').datagrid('load', {
        manageDeptId: $('#manageDeptId').combotree("getValue"),// 主管部门
        materialCode: $('#assetLeave').combobox("getValue")// 资产类别
    });
}

function resetQuerys() {
    $('#manageDeptId').combotree("setValue", '');//主管部门
    $('#assetLeave').combobox("setValue", '');//资产类别
    $('#dataGridTable').datagrid('load', {
        manageDeptId: '',
        materialCode: ''
    });
}

$(function () {
    $('#dataGridTable').datagrid({
        url: 'assetreport/assetusabilityreport_datagrid',
        fit: true,
        method: 'POST',
        striped: true,
        rownumbers: true,
        pagination: true,
        // showFooter: true,
        onLoadSuccess: compute,// 加载完毕后执行计算
        pageSize: 20,
        title: '资产可用性统计',
        toolbar: '#btdiv',
        loadMsg: '程序处理中，请稍等...',
        queryParams: {},
        columns: [[
            {field: 'manageDeptStr', title: '主管部门', align: 'center'},
            {field: 'assetTypeStr', title: '资产类别', align: 'center'},
            {field: 'use', sum: 'true', title: '使用', align: 'center'},
            {field: 'idle', sum: 'true', title: '闲置', align: 'center'},
            {field: 'sealup', sum: 'true', title: '封存', align: 'center'},
            {field: 'disuse', sum: 'true', title: '停用', align: 'center'},
            {field: 'borrow', sum: 'true', title: '借出', align: 'center'},
            {
                field: 'total', title: '小计', align: 'center', formatter: function (value, row, index) {
                    var total = 0;
                    total = parseFloat(row.use) + parseFloat(row.idle) + parseFloat(row.sealup) + parseFloat(row.disuse) + parseFloat(row.borrow);

                    return total;

                }
            }
        ]],
        onLoadError: function (data) {
            AjaxErrorHandler(data);
        }
    });

    function compute() {// 计算函数
        var rows = $('#dataGridTable').datagrid('getRows');// 获取当前的数据行
        var usetal = 0;// 计算usetal的总和
        var idletal = 0;// 统计idletal的总和
        var sealuptal = 0;// 计算sealuptal的总和
        var disusetal = 0;// 统计disusetal的总和
        var borrowtal = 0;// 统计borrowtal的总和
        if (rows.length > 0) {
            for (var i = 0; i < rows.length; i++) {
                usetal += parseFloat(rows[i]['use']);
                idletal += parseFloat(rows[i]['idle']);
                sealuptal += parseFloat(rows[i]['sealup']);
                disusetal += parseFloat(rows[i]['disuse']);
                borrowtal += parseFloat(rows[i]['borrow']);
            }
            // 新增一行显示统计信息
            $('#dataGridTable').datagrid('appendRow',
                {
                    itemid: '<b>合计：</b>',
                    assetTypeStr: '合计',
                    use: usetal,
                    idle: idletal,
                    sealup: sealuptal,
                    disuse: disusetal,
                    borrow: borrowtal
                });
        }
    }
})
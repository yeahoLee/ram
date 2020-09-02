/* 公共脚本 */
//弹出加载层
function load() {
    $("<div class=\"datagrid-mask\"></div>").css({display: "block", width: "100%", height: $(window).height() }).appendTo("body");
    $("<div class=\"datagrid-mask-msg\"></div>").html("提交中，请稍候。。。").appendTo("body").css({ display: "block", left: ($(document.body).outerWidth(true) - 190) / 2, top: ($(window).height() - 45) / 2 });
}

//取消加载层  
function disLoad() {
    $(".datagrid-mask").remove();
    $(".datagrid-mask-msg").remove();
}

/**
 * easyui convert Date to String
 */
$.fn.datebox.defaults.formatter = function(date){
    var y = date.getFullYear();
    var m = date.getMonth()+1;
    var d = date.getDate();
    return y+'/'+m+'/'+d;
}

/**
 * easyui convert String to Date
 */
$.fn.datebox.defaults.parser = function(s){
    var t = Date.parse(s);
    if (!isNaN(t)){
        return new Date(t);
    } else {
        return new Date();
    }
}

/*
用途：检查输入字符串是否为空或者全部都是空格
输入：str
返回：
如果全是空返回true,否则返回false
*/
function isNull(str){
    if ( str == "" ) return true;
    var regu = "^[ ]+$";
    var re = new RegExp(regu);
    return re.test(str);
}

/***
 * 判断数组是否包含
 * @param arr
 * @param obj
 * @returns
 */
function contains(arr, obj) {
    var i = arr.length;
    while (i--) {
        if (arr[i].assetId === obj) {
            return true;
        }
    }
    return false;
}

function DateTimeFormatter2(value) {
    if (value == null || value == '') {
        return '';
    }
    var dt;
    if (value instanceof Date) {
        dt = value;
    } else {
        dt = new Date(value);
        if (isNaN(dt)) {
            value = value.replace(/\/Date\((-?\d+)\)\//, '$1'); // 标红的这段是关键代码，将那个长字符串的日期值转换成正常的JS日期格式
            dt = new Date();
            dt.setTime(value);
        }
    }
    return dt.pattern("yyyy-MM-dd");
}

function DateTimeFormatter(value, pattern) {
    if (value == null || value == '') {
        return '';
    }
    var dt;
    if (value instanceof Date) {
        dt = value;
    } else {
        dt = new Date(value);
        if (isNaN(dt)) {
            value = value.replace(/\/Date\((-?\d+)\)\//, '$1'); // 将那个长字符串的日期值转换成正常的JS日期格式
            dt = new Date();
            dt.setTime(value);
        }
    }
    if(pattern==null || pattern==undefined || pattern.length==0) {
        return dt.pattern("yyyy-MM-dd HH:mm:ss");
    } else {
        return dt.pattern(pattern);
    }

}

function DateTimeFormatter3(value) {
    if (value == null || value == '') {
        return '';
    }
    var dt;
    if (value instanceof Date) {
        dt = value;
    } else {
        dt = new Date(value);
        if (isNaN(dt)) {
            value = value.replace(/\/Date\((-?\d+)\)\//, '$1'); // 将那个长字符串的日期值转换成正常的JS日期格式
            dt = new Date();
            dt.setTime(value);
        }
    }
    return dt.pattern("yyyy-MM-dd HH:mm");
}

function DateTimeFormatter4(value) {
    if (value == null || value == '') {
        return '';
    }
    var dt;
    if (value instanceof Date) {
        dt = value;
    } else {
        dt = new Date(value);
        if (isNaN(dt)) {
            value = value.replace(/\/Date\((-?\d+)\)\//, '$1'); // 将那个长字符串的日期值转换成正常的JS日期格式
            dt = new Date();
            dt.setTime(value);
        }
    }
    return dt.pattern("yyyy/MM/dd HH:mm:ss");
}

function AjaxErrorHandler(data, callback) {
    var result = $.parseJSON(data.responseText);
    $.messager.alert('错误', result.errorMessage, 'error', function(){
        if(callback) callback();
    });
}

function showAlert(content,callback) {
    if(content != "未知错误"){
        alert(content);
    }
    if(callback) callback();
}

/***
 * dataGrid 分页
 * @param data
 * @returns
 */
function pagerFilter(data){
    if (typeof data.length == 'number' && typeof data.splice == 'function'){
        data = {
            total: data.length,
            rows: data
        }
    }
    var dg = $(this);
    var opts = dg.datagrid('options');
    var pager = dg.datagrid('getPager');
    pager.pagination({
        onSelectPage:function(pageNum, pageSize){
            opts.pageNumber = pageNum;
            opts.pageSize = pageSize;
            pager.pagination('refresh',{
                pageNumber:pageNum,
                pageSize:pageSize
            });
            dg.datagrid('loadData',data);
        }
    });
    if (!data.originalRows){
        data.originalRows = (data.rows);
    }
    var start = (opts.pageNumber-1)*parseInt(opts.pageSize);
    var end = start + parseInt(opts.pageSize);
    data.rows = (data.originalRows.slice(start, end));
    return data;
}

function fillEnumCombo(moduleName, methodName, comboboxId){
    $.ajax({
        url: 'base/enum_combo',
        type: 'GET',
        data: {
            moduleName: moduleName,
            methodName: methodName
        },
        success: function(result){
            $('#'+comboboxId).combobox('loadData',result);
        },
        error: function(result) {
            AjaxErrorHandler(result);
        }
    });
}

function formatter(date,fmt){
    var o = {
        "M+": date.getMonth() + 1, //月份
        "d+": date.getDate(), //日
        "h+": date.getHours(), //小时
        "m+": date.getMinutes(), //分
        "s+": date.getSeconds(), //秒
        "q+": Math.floor((date.getMonth() + 3) / 3), //季度
        "S": date.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

Date.prototype.pattern = function(fmt) {
    var o = {
        "M+" : this.getMonth() + 1, // 月份
        "d+" : this.getDate(), // 日
        "h+" : this.getHours() % 12 == 0 ? 12 : this.getHours() % 12, // 小时
        "H+" : this.getHours(), // 小时
        "m+" : this.getMinutes(), // 分
        "s+" : this.getSeconds(), // 秒
        "q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
        "S" : this.getMilliseconds()
        // 毫秒
    };
    var week = {
        "0" : "/u65e5",
        "1" : "/u4e00",
        "2" : "/u4e8c",
        "3" : "/u4e09",
        "4" : "/u56db",
        "5" : "/u4e94",
        "6" : "/u516d"
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
            .substr(4 - RegExp.$1.length));
    }
    if (/(E+)/.test(fmt)) {
        fmt = fmt
            .replace(
                RegExp.$1,
                ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "/u661f/u671f"
                    : "/u5468")
                    : "")
                + week[this.getDay() + ""]);
    }
    for ( var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
                : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
};
/**
 * 获取服务器时间
 * @returns
 */
function getServerTime(){
    var xmlHttp = false;
    //获取服务器时间
    try {
        xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
    } catch (e) {
        try {
            xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
        } catch (e2) {
            xmlHttp = false;
        }
    }

    if (!xmlHttp && typeof XMLHttpRequest != 'undefined') {
        xmlHttp = new XMLHttpRequest();
    }

    xmlHttp.open("GET", window.location.href.toString(), false);
    xmlHttp.setRequestHeader("If-None-Match", "bytes=-1");
    xmlHttp.setRequestHeader("Cache-Control","no-cache");
    xmlHttp.send(null);

    return new Date(xmlHttp.getResponseHeader("Date"));
}

/**
 * 验证时间格式
 * @param str
 * @returns
 */
function isdatetime(str){
    var result=str.match(/^(\d{4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/);
    if(result==null) return false;
    var d= new Date(result[1], result[3]-1, result[4], result[5], result[6], result[7]);
    return (d.getFullYear()==result[1]&&(d.getMonth()+1)==result[3]&&d.getDate()==result[4]&&d.getHours()==result[5]&&d.getMinutes()==result[6]&&d.getSeconds()==result[7]);
}

/**销毁窗体
 * obj:dailog 对象
 * */
function destroyDailog(obj){
    var d = $(obj).closest('.window-body');
    d.dialog('destroy');
}

/**
 * 将查询条件
 * @param formId
 * @param array
 * @returns
 */
function searchForMap(formId,array){
    var result = {};
    var fArray = $('#'+formId).serializeArray();
    $.each(fArray,function(){
        if (this.value != ""){
            if (result[this.name]) {
                result[this.name] = result[this.name] +","+this.value;
            } else {
                result[this.name] = this.value;
                if (indexOf(array,this.name) != -1) {
                    result[this.name] = this.value;
                }
            }
        }
    });
    return result;
}

/**
 * 验证是否包含
 * @param array
 * @param name
 * @returns
 */
function indexOf(array,name){
    for(var i = 0;i<array.length;i++){
        if(array[i] == name){
            return i;
        }
    }
    return -1;
}

//设置唯一标识
function generateUUID() {
    var d = new Date().getTime();
    if (window.performance && typeof window.performance.now === "function") {
        d += performance.now(); //use high-precision timer if available
    }
    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = (d + Math.random() * 16) % 16 | 0;
        d = Math.floor(d / 16);
        return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
    });
    return uuid;
}

//判断字符是否为空的方法
function isEmpty(obj){
    if(typeof obj == "undefined" || obj == null || obj == ""){
        return true;
    }else{
        return false;
    }
}

function setFlowStatus(value) {
    if(value=='驳回') {
        return '<div class="label-danger" style="color: white;">驳回</div>';
    } else if(value=='审批结束') {
        return '<div class="label-success" style="color: white;">审批结束</div>';
    } else {
        return '<div class="label-primary" style="color: white;">' + value + '</div>';
    }
}
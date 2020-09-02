function openIframeUrl(url, title) {
    var naviBar = '<i class="fa fa-chevron-right"></i>';
    $('#content_frame').attr('src', url);
    $('#content_title').html(naviBar + ' ' + title);
}

function changeLoginDept(deptId) {
    $.ajax({
        url: 'ram/change_login_dept',
        type: 'POST',
        data: {deptId: deptId},
        success: function (data) {
            window.location.reload();
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

function logout() {
    location.href = 'logout';
}

function checkPwd() {
    $.ajax({
        url: 'base/check_pwd',
        type: 'POST',
        data: {},
        success: function (data) {
            if(data.flag!=""){
            	alert(data.flag);
            }
        },
        error: function (data) {
            AjaxErrorHandler(data);
        }
    });
}

$(function(){
	checkPwd();
})

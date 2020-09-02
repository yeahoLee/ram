function changePwd() {
    var password = $('#password').val();
    var oldPwd = $('#change_pwd input[name="oldPwd"]').val();
    var newPwd = $('#change_pwd input[name="newPwd"]').val();
    var confirmPwd = $('#change_pwd input[name="confirmPwd"]').val();

    $('#oldPwdLab').text('');
    $('#newPwdLab').text('');
    $('#confirmPwdLab').text('');

    var callAjax = true;

    if (!oldPwd || oldPwd != password) {
        $('#oldPwdLab').text('旧密码不正确！');
        callAjax = false;
    }
    if (!newPwd || newPwd.length < 8) {
        $('#newPwdLab').text('新密码不能小于8位！');
        callAjax = false;
    }
    if (newPwd == password) {
        $.messager.alert("提示", '新密码与旧密码一致！', 'error');
        callAjax = false;
    }
    if (confirmPwd != newPwd) {
        $('#confirmPwdLab').text('新密码与确认密码不相同！');
        callAjax = false;
    }

    if (callAjax) {
        $.ajax({
            url: 'base/change_pwd',
            type: 'POST',
            data: {
                oldPwd: oldPwd,
                newPwd: newPwd,
                confirmPwd: confirmPwd
            },
            success: function () {
                $.messager.alert('提示', '修改成功！', 'info', function () {
                    window.parent.location.href = 'index';
                });
            },
            error: function (data) {
                AjaxErrorHandler(data);
            }
        });
    }
}
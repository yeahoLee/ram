/**
 * 新增审批
 */
//保存并发送
function saveAndSend() {
    if ($.trim($("#id").val()) == "") {
        $('#saveBtn').attr('disabled', "true");
        var data = save();
        if (data == false) {
            $('#saveBtn').removeAttr('disabled');
            return false;
        } else {
            $.ajax({
                url: 'assetallocation/create_assetalloction',
                type: 'POST',
                data: data,
                success: function (data) {
                    $("#id").val(data)
                    getFirstNode();
                    $('#saveBtn').removeAttr('disabled');
                },
                error: function (data) {
                    $('#saveBtn').removeAttr('disabled');
                    AjaxErrorHandler(data);
                }
            });
        }
    } else {
        getFirstNode();
    }
}

function getFirstNode() {
    $.ajax({
        url : "flowable/get_first_node",
        type : "POST",
        dataType : "json",
        async : false,
        data :{
            // businessKey : JSON.id,
            // formName : JSON.title,
            processDefinitionKey : ASSETS_TRANSFER
        },
        success : function (result) {
            if(result.length==0) {
                // 流程处于最后环节
                confirmDialogBtnsConfirm();
            } else {
                $("#confirmDialog").dialog('open');
                $("#selectNextNode").html('');
                $("#selectNodeAndUser").html('');
                $('#selectUser').html('');
                nextNodeData = result;
                if(result.length == 1) {
                    $('#selectNextNode').append(assembleNextNode(result, 0));
                    $('input[name="nextNodes"]:checked').click();
                } else {
                    for(var i=0;i<data.length;i++) {
                        $('#selectNextNode').append(assembleNextNode(result, i));
                    }
                }
            }
        },
        error : function (data) {
            AjaxErrorHandler(data);
        }
    })
}

function submitNext() {
    var formName = $('#mateReceInfo input[name="receiptName"]').val();
    var params = "";
    for(var selectData in selectDataMap) {
        var split = selectData.split('_');
        if(params.length==0) {
            params = nextNodeData[split[0]].nodeId+"_"+nextNodeData[split[0]].users[split[1]].userName;
        } else {
            params = params + ';' + nextNodeData[split[0]].nodeId+"_"+nextNodeData[split[0]].users[split[1]].userName;
        }
    }
    $.ajax({
        url:"flowable/submit_next",
        type : "POST",
        data :{id : $("#id").val(), processDefinitionKey : ASSETS_TRANSFER, formName : formName, params : params },
        dataType: "json",
        success : function (data) {
            window.location.href = 'assetallocation_query';
        },
        error : function (data) {
            AjaxErrorHandler(data);
        }

    })
}


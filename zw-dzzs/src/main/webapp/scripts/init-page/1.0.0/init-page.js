/**
 * Created by lkh on 2016/5/14.
 */
function Utils(options) {

}

Utils.showFlashMessage = function(msg) {
    if($('#flash_message').length == 0) {
        $('body').append("<div class='flash-message' id='flash_message'></div>");
    }
    $("#flash_message").html(msg);
    $("#flash_message").show().animate({left: '350px'}, 300, function() {
        $("#flash_message").fadeOut(6000, function(){
            $("#flash_message").css("left", "10px");
        });
    });
}

Utils.showMessage = function(msg, followElementId, keepTime, cssClasses) {
    if($('#message').length == 0) {
        $('body').append("<div class='flash-message' id='message'></div>");
    } else {
        $('#message').removeClass();
        $('#message').addClass('flash-message');
    }

    if(cssClasses != null && cssClasses != undefined) {
        $('#message').addClass(cssClasses);
    }

    $("#message").html(msg);
    if(followElementId != null && followElementId != undefined) {
        var pos = $("#" + followElementId).offset();
        $("#message").css("left", pos.left + 80);
        $("#message").css("top", pos.top - 53);
    } else {
        $("#message").css("left", 350);
        $("#message").css("top", 0);
    }

    var kTime = 4000;
    if(!isNaN(keepTime)) {
        kTime = keepTime;
    }
    $("#message").show();
    setTimeout("$('#message').fadeOut(800)", kTime);
}

Utils.warnMessage = function(msg, followElementId) {
    Utils.showMessage(msg, followElementId, 10*1000, "flash-message-warn");
}
/* add a static method to jQuery for init a dialog template which used by artDialog */
$.extend({
    initDialog : function(id, width, height) {
        var idSelector = "#" + id;
        if($(idSelector).length == 0) {
            var template = "<div id='$id' style='width:$widthpx; height:$heightpx; display:none;'></div>";
            template = template.replace("$id", id);
            template = template.replace("$width", width);
            template = template.replace("$height", height);
            $('body').append(template);
        }
    },
    preventDefault : function(event) {
        var oEvent = window.event ? window.event : arguments[0];
        if(oEvent.preventDefault){
            oEvent.preventDefault();
        } else {
            oEvent.returnValue = false;
        }
    }
});

$.fn.extend({
    showLoadding : function() {
        $(this).html("<div class='loadding'></div>");
    }
});

$.ajaxSetup({
    cache : false,
    global: true,
    traditional: true,
    error: function(xhr, status, error) {
        try{
            var msg = $.parseJSON(xhr.responseText).msg;
            Utils.showMessage(msg);
        } catch(e) {}
    }
});

$(document).ajaxStart(function() {
    $("#loadding").show();
});

$(document).ajaxStop(function(){
    $("#loadding").hide();
});

$(document).ready(function() {
    if($("#loadding").length == 0) {
        $('body').append("<div id='loadding'>加载中...</div>");
    }
});

$(document).ajaxError(function(event, jqxhr, settings, exception) {
    if(jqxhr.status == 400) {
        var msg = "输入错误，请仔细检查输入数据。";
        Utils.warnMessage(msg);
    } else if(jqxhr.status == 401) {
        var msg = "未授权访问此功能，请与管理员联系。";
        Utils.warnMessage(msg);
    } else if(jqxhr.status == 403) {
        var msg = "您当前状态已失效，请刷新页面或点击此处重新<a href='javascript:top.location.reload();'>登录系统</a>。";
        Utils.warnMessage(msg);
    } else if(jqxhr.status == 500) {
        var msg = "服务端执行错误，请与管理员联系。";
        Utils.warnMessage(msg);
    }
});
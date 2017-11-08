<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/taglibs.jsp" %>
<title>东湖高新证书管理平台</title>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
<link rel="stylesheet" href="http://${cdn}/plugins/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet" href="${ctx}/scripts/ionicons/css/ionicons.css">
<link rel="stylesheet" href="${cdn}/bootstrap3/3.3.5/css/bootstrap.css"/>
<link rel="stylesheet" href="${ctx}/scripts/AdminLTE/css/AdminLTE.min.css"/>
<link rel="stylesheet" href="${ctx}/scripts/AdminLTE/css/skins/skin-green.min.css"/>
<link rel="stylesheet" href="${ctx}/scripts/datetimepicker/css/datetimepicker.css"/>
<link rel="stylesheet" href="${ctx}/scripts/art-dialog/css/ui-dialog.css"/>
<link rel="stylesheet" href="${ctx}/scripts/pnotify/pnotify.custom.min.css" media="all" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="${cdn}/jquery/1.11.3/jquery-1.11.3.js"></script>
<script type="text/javascript" src="${cdn}/bootstrap3/3.3.5/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${ctx}/scripts/common/html5shiv.min.js"></script>
<script type="text/javascript" src="${ctx}/scripts/common/respond.min.js"></script>
<script type="text/javascript" src="${ctx}/scripts/AdminLTE/js/app.min.js"></script>
<script type="text/javascript" src="${ctx}/scripts/jquery-validation/1.14.0/jquery.validate.min.js"></script>
<script type="text/javascript" src="${ctx}/scripts/jquery-validation/1.14.0/localization/messages_zh.min.js"></script>
<script type="text/javascript" src="${ctx}/scripts/datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="${ctx}/scripts/datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${ctx}/scripts/js-xss/xss.min.js"></script>
<script type="text/javascript" src="${ctx}/scripts/art-dialog/dist/dialog-plus.js"></script>
<script type="text/javascript" src="${ctx}/scripts/pnotify/pnotify.custom.min.js"></script>
<style type="text/css">
</style>
<script type="text/javascript">
    /**
     * 浮点数千分位格式化 xxx,xxx,xxx,xxx.xxx
     * @param num
     * @returns {*}
     */
    function formatThousands(num) {
        if (typeof num === 'undefined' || num == null) return '';
        return (num.toFixed(3) + '').replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
    }

    function numToPercentage(num) {
        return (num * 100).toFixed(3);
    }

    /**
     * 提示标签
     * @param title 标题
     * @param content 内容
     * @param type 类别info(兰) success(绿) error(红)
     */
    function tips(title, content, type) {
        PNotify.prototype.options.delay = 2000;
        new PNotify({
            title: title,
            text: content,
            type: type
//            stack: stack_context_modal
        });
    }

    /**
     * 所有页面ajax不使用缓存，避免IE浏览器奇怪现象
     */
    $.ajaxSetup({
        cache: false //关闭AJAX相应的缓存
    });
</script>



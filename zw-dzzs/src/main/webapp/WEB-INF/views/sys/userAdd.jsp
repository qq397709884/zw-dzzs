<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="../main/header.jsp" %>
    <%@ include file="/WEB-INF/commons/dataTables.jsp" %>
    <style>
        .ui-popup-backdrop {
            opacity: 0
        }
    </style>
    <link rel="stylesheet" href="${ctx}/scripts/ztree/css/metroStyle/metroStyle.css">
    <script src="${ctx}/scripts/ztree/jquery.ztree.all.min.js"></script>
</head>
<body class="hold-transition skin-green sidebar-mini">
<div id="rrapp">
    <div class="panel panel-default" style="">
        <div class="panel-heading">新增</div>
        <form class="form-horizontal">
            <div class="form-group">
                <div class="col-sm-2 control-label">用户名</div>
                <div class="col-sm-10"><input placeholder="登录账号" class="form-control" type="text"></div>
            </div>
            <div class="form-group">
                <div class="col-sm-2 control-label">所属部门</div>
                <div class="col-sm-10"><input readonly="readonly" placeholder="所属部门" class="form-control"
                                              style="cursor: pointer;" type="text"></div>
            </div>
            <div class="form-group">
                <div class="col-sm-2 control-label">密码</div>
                <div class="col-sm-10"><input placeholder="密码" class="form-control" type="text"></div>
            </div>
            <div class="form-group">
                <div class="col-sm-2 control-label">邮箱</div>
                <div class="col-sm-10"><input placeholder="邮箱" class="form-control" type="text"></div>
            </div>
            <div class="form-group">
                <div class="col-sm-2 control-label">手机号</div>
                <div class="col-sm-10"><input placeholder="手机号" class="form-control" type="text"></div>
            </div>
            <div class="form-group">
                <div class="col-sm-2 control-label">角色</div>
                <div class="col-sm-10"></div>
            </div>
            <div class="form-group">
                <div class="col-sm-2 control-label">状态</div>
                <label class="radio-inline"><input name="status" value="0" type="radio"> 禁用
                </label> <label class="radio-inline"><input name="status" value="1" type="radio"> 正常
            </label></div>
            <div class="form-group">
                <div class="col-sm-2 control-label"></div>
                <input value="确定" class="btn btn-primary" type="button">
                &nbsp;&nbsp;<input value="返回" class="btn btn-warning" type="button"></div>
        </form>
    </div>
</div>
</body>
</html>

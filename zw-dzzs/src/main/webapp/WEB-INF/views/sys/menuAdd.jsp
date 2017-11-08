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
    <script type="text/javascript">
        var setting = {
            data: {
                simpleData: {
                    enable: true,
                    idKey: "menuId",
                    pIdKey: "parentId",
                    rootPId: -1
                },
                key: {
                    url: "nourl"
                }
            },
            view: {
                selectedMulti: false
            }
        };

        var ztree;
        $(function () {

        });

        function getMenu() {
            var elem = document.getElementById("menuLayer");
            var ztreeDialog = dialog({
                "content": elem,
                "title": "选择菜单",
                "zIndex": 2000,
                "okValue": '保存',
                background: "0,0,0,0",
                opacity: 0,
                lock: false,
                "onshow": function () {
                    $.get($ctx + "/sys/menu/select", function (data) {
                        ztree = $.fn.zTree.init($("#menuTree"), setting, data);
                    }).then(function (data) {
                        $(".ui-popup-backdrop").css("opacity", 0);
                        $(".ui-popup-backdrop").css("background", "#ffffff");
                    });
                },
                ok: function () {
                    var checkNodes = ztree.getSelectedNodes();

                    if (checkNodes) {
                        $("#parent_id").val(checkNodes[0].menuId);
                        $("#parentName").val(checkNodes[0].name);
                    }


                },
                "cancelValue": '关闭',
                "cancel": function () {
                    top.location.reload();
                }
            });
            ztreeDialog.showModal();
        }

        function saveOrUpdate() {
            var url = $("#menuId").val() == null ? $ctx + "/sys/menu/save" : $ctx + "/sys/menu/update";
            //数据验证
            var data=$("#menuForm").serializeJSON();
            console.log($("#menuForm").serializeJSON());
            $.ajax({
                type: "POST",
                url: +url,
                contentType: "application/json",
                data: data,
                success: function (r) {
                    if (r.code === 0) {
                        alert('操作成功', function () {
                            vm.reload();
                        });
                    } else {
                        alert(r.msg);
                    }
                }
            });
        }
    </script>
</head>
<body class="hold-transition skin-green sidebar-mini">
<section class="content-header">
    <h1>
        菜单管理
    </h1>
    <ol class="breadcrumb">
    </ol>
</section>
<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="box box-warning">
                <div class="box-header">
                    <c:choose>
                        <c:when test="${sysMenu.menuId==null}">
                            <h3 class="box-title">新增菜单</h3>
                        </c:when>
                        <c:when test="${sysMenu.menuId!=null}">
                            <h3 class="box-title">编辑菜单</h3>
                        </c:when>
                        <c:otherwise>
                            <h3 class="box-title">新增菜单</h3>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="box-body">
                    <div class="input-group opbtns">
                        <%--<button id="addButton" type="button" class="btn btn-primary" onclick="returnUserList()">返回</button>--%>
                    </div>

                    <form class="form-horizontal" id="menuForm" method="post">
                        <input hidden id="menuId" name="menuId" value="${sysMenu.menuId}">
                        <input hidden id="parent_id" name="parentId" value="${sysMenu.parentId}">
                        <div class="form-group">
                            <div class="col-sm-2 control-label">类型</div>
                            <label class="radio-inline"><input name="type" value="0" type="radio"
                                                               <c:if test="${sysMenu.type==0}">checked</c:if> >
                                目录</label>
                            <label class="radio-inline"><input name="type" value="1" type="radio"
                                                               <c:if test="${sysMenu.type==1}">checked</c:if>>
                                菜单</label>
                            <label class="radio-inline"><input name="type" value="2" type="radio"
                                                               <c:if test="${sysMenu.type==2}">checked</c:if>>
                                按钮</label>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-2 control-label">菜单名称</div>
                            <div class="col-sm-10">
                                <input placeholder="菜单名称或按钮名称" class="form-control" type="text" name="name"
                                       value="${sysMenu.name}">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-2 control-label">上级菜单</div>
                            <div class="col-sm-10">
                                <input readonly="readonly" placeholder="一级菜单" class="form-control" name="parentName"
                                       value="${sysMenu.parentName}" onclick="getMenu()"
                                       style="cursor: pointer;" type="text"></div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-2 control-label">菜单URL</div>
                            <div class="col-sm-10"><input placeholder="菜单URL" class="form-control" type="text"
                                                          name="url" value="${sysMenu.url}"></div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-2 control-label">授权标识</div>
                            <div class="col-sm-10"><input placeholder="多个用逗号分隔，如：user:list,user:create"
                                                          value="${sysMenu.perms}"
                                                          class="form-control" type="text" name="perms"></div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-2 control-label">排序号</div>
                            <div class="col-sm-10"><input placeholder="排序号" class="form-control" type="number"
                                                          name="orderNum" value="${sysMenu.orderNum}"></div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-2 control-label">图标</div>
                            <div class="col-sm-10"><input placeholder="菜单图标" class="form-control" type="text"
                                                          name="icon" value="${sysMenu.icon}"> <code
                                    style="margin-top: 4px; display: block;">获取图标：http://fontawesome.io/icons/</code>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-2 control-label"></div>
                            <input value="确定" class="btn btn-primary" type="button" onclick="saveOrUpdate()">
                            &nbsp;&nbsp;<input value="返回" class="btn btn-warning" type="button"></div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- 选择菜单 -->
    <div id="menuLayer" style="display: none;padding:10px;">
        <ul id="menuTree" class="ztree"></ul>
    </div>
</section>
</body>
</html>

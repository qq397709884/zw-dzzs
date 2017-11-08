<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="../main/header.jsp" %>
    <%@ include file="/WEB-INF/commons/dataTables.jsp" %>
    <script type="text/javascript">
        var oTable = null;
        $(document).ready(function () {
            oTable = new DatatableWrapper();
            oTable.render('menuListTable', {
                "sAjaxSource": "${ctx}/sys/menu/listData",
                "aoColumns": [
                    {"mDataProp": null, "bSortable": false},
                    {"mDataProp": null, "bSortable": false},
                    {"mDataProp": "name", "bSortable": true},
                    {"mDataProp": "parentName", "bSortable": false},
                    {"mDataProp": "icon", "bSortable": false},
                    {"mDataProp": "type", "bSortable": false},
                    {"mDataProp": "orderNum", "bSortable": false},
                    {"mDataProp": "url", "bSortable": false},
                    {"mDataProp": "perms", "bSortable": false}
                ],
                "order": [6, "asc"],
                "fnServerParams": function (aoData) {

                },
                "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                    $('td:eq(1)', nRow).html("<input name='ids' type='checkbox' value='" + aData.id + "'>");
                    $('td:eq(4)', nRow).html(showIcon(aData.icon));
                    $('td:eq(5)', nRow).html(showType(aData.type));
                }
            });
        });

        function showIcon(icon) {
            return "<i class='" + icon + "'></i>";
        }

        function showType(type) {
            if (type == 0) {
                return "<span class='label label-primary'>目录</span>";
            }
            if (type == 1) {
                return "<span class='label label-success'>菜单</span>";
            }
            if (type == 2) {
                return "<span class='label label-warning'>按钮</span>";
            }
        }
        

            function goAdd() {
                window.location.href = "${ctx}/sys/menu/add";
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
                <div class="box-body">
                    <div class="input-prepend input-append">
                        <div class="input-group opbtns">
                            <button id="addButton" type="button" class="btn btn-primary btn-sm" onclick="goAdd()">
                                新增
                            </button>
                            <button id="enableButton" type="button" class="btn btn-primary btn-sm"
                                    onclick="updateSysMenu()">修改
                            </button>
                            <button id="disableButton" type="button" class="btn btn-primary btn-sm"
                                    onclick="deleteSysMenu()">删除
                            </button>

                        </div>
                    </div>
                    <table class="display table table-striped table-bordered bootstrap-datatable datatable"
                           id="menuListTable">
                        <thead>
                        <tr>
                            <th width="5%">序号</th>
                            <th width="3%"><input type="checkbox" name="selectAllIds"
                                                  onclick="oTable.selectAll(this)"></th>
                            <th width="10%">权限名称</th>
                            <th width="*%">上级菜单</th>
                            <th width="15%">图标</th>
                            <th width="15%">类型</th>
                            <th width="15%">排序号</th>
                            <th width="15%">菜单链接</th>
                            <th width="15%">授权标识</th>
                        </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</section>
</body>
</html>

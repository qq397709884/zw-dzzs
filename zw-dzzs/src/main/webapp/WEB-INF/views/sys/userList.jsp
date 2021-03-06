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
            oTable.render('userListTable', {
                "sAjaxSource": "${ctx}/sys/user/listData",
                "aoColumns": [
                    {"mDataProp": null, "bSortable": false},
                    {"mDataProp": null, "bSortable": false},
                    {"mDataProp": "username", "bSortable": true},
                    {"mDataProp": "deptName", "bSortable": false},
                    {"mDataProp": "email", "bSortable": false},
                    {"mDataProp": "mobile", "bSortable": false},
                    {"mDataProp": "status", "bSortable": false}
                ],
                "order": [2, "desc"],
                "fnServerParams": function (aoData) {
                    aoData.push({"name": "filter_LIKE_username", "value": $("#filter_LIKE_username").val()});
                },
                "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                    $('td:eq(1)', nRow).html("<input name='ids' type='checkbox' value='" + aData.id + "'>");
                    $('td:eq(2)', nRow).html("<a name='username' href='${ctx}/user/edit/" + aData.id + "'>" + aData.username + "</a>");
                    $('td:eq(6)', nRow).html(showStatus(aData.status));
                }
            });
        });

        //0  禁用 1 正常
        function showStatus(status) {
            if (status == 0) {
                return "<span class='label label-danger'>禁用</span>";
            }
            if (status == 1) {
                return "<span class='label label-success'>启用</span>";
            }
        }
    </script>
</head>
<body class="hold-transition skin-green sidebar-mini">

<section class="content-header">
    <h1>
        用户管理
    </h1>
    <ol class="breadcrumb">
    </ol>
</section>

<section class="content">
    <div class="row">
        <div class="col-xs-12">
            <div class="box box-warning">
                <div class="box-body">
                    <form id="query" class="form-inline" style="margin-bottom: 5px">
                        <div class="input-prepend input-append">
                            <div class="input-group">
                                <span class="input-group-addon">用户名</span>
                                <input class="form-control canreset" id="filter_LIKE_username" type="text">
                            </div>
                            <button type="button" class="btn" onclick="oTable.queryData();"><i class="icon-search"></i>查询
                            </button>
                            <button type="button" class="btn" onclick="oTable.reset('query');"><i
                                    class="icon-th-list"></i>全部
                            </button>
                            <div class="input-group opbtns">

                                    <button id="addButton" type="button" class="btn btn-primary btn-sm" onclick="goAdd()">
                                        新增用户
                                    </button>
                                    <button id="enableButton" type="button" class="btn btn-primary btn-sm"
                                            onclick="updateUserStatus('enable')">修改用户
                                    </button>
                                    <button id="disableButton" type="button" class="btn btn-primary btn-sm"
                                            onclick="updateUserStatus('disable')">删除用户
                                    </button>

                            </div>
                        </div>
                    </form>
                    <table class="display table table-striped table-bordered bootstrap-datatable datatable"
                           id="userListTable">
                        <thead>
                        <tr>
                            <th width="5%">序号</th>
                            <th width="3%"><input type="checkbox" name="selectAllIds"
                                                  onclick="oTable.selectAll(this)"></th>
                            <th width="10%">用户名</th>
                            <th width="*%">所属部门</th>
                            <th width="15%">邮箱</th>
                            <th width="15%">手机号</th>
                            <th width="15%">状态</th>
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

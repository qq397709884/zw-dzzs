<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/commons/taglibs.jsp" %>
<%--<link rel="stylesheet" href="${cdn}/dataTables/1.10.10/css/dataTables.jqueryui.min.css" />--%>
<%--<link rel="stylesheet" href="${cdn}/jquery-ui/1.11.4/smoothness/jquery-ui.min.css" />--%>
<%--<link rel="stylesheet" href="${cdn}/dataTables/1.10.10/css/table_jui.css" />--%>
<%--<script type="text/javascript" src="${cdn}/dataTables/1.10.10/js/jquery.dataTables.js"></script>--%>
<%--<script type="text/javascript" src="${cdn}/dataTables/1.10.10/js/dataTables.jqueryui.min.js"></script>--%>
<link rel="stylesheet" href="${ctx}/scripts/dataTables/css/dataTables.bootstrap.css">
<script type="text/javascript" src="${ctx}/scripts/dataTables/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="${ctx}/scripts/dataTables/js/dataTables.bootstrap.min.js"></script>
<style>
    table.dataTable thead > tr > th, table.dataTable thead td {
        padding: 8px;
    }

    table.dataTable thead > tr > th:first-child.sorting_asc {
        padding-right: 8px;
    }

    .dataTables_length {
        margin-top: 10px;
    }

    table td {
        word-break: keep-all;
        white-space: nowrap;
    }

    table th {
        word-break: keep-all;
        white-space: nowrap;
    }

    .dataTables_wrapper > .row {
        overflow-x: auto;
    }
</style>
<script type="text/javascript">
    function DatatableWrapper(options) {
        var iOptions = {'showLineNumber': true};
        $.extend(iOptions, options);
        DatatableWrapper.initTable(iOptions);	//初始化Datatable组件
        this.dt = null;							//保存当前Datatable对象
    }

    DatatableWrapper.showMessage = function (msg) {
        if ($('#flash_message').length == 0) {
            $('body').append("<div class='flash-message' id='flash_message'></div>");
        }
        $("#flash_message").html(msg);
        $("#flash_message").show().animate({left: '380px'}, 300, function () {
            $("#flash_message").fadeOut(3000, function () {
                $("#flash_message").css("left", "10px");
            });
        });
    };

    DatatableWrapper.initTable = function (options) {
        $.extend($.fn.dataTable.defaults, {
            "bProcessing": true,
            "bServerSide": true,
            "sServerMethod": "post",
            "sPaginationType": "full_numbers",
            "bFilter": false,
            "bDeferRender": true,
            "bJQueryUI": true,
            "fnDrawCallback": function (oSettings) {
                if (options.showLineNumber) {
                    $("input[name=selectAllIds]").attr("checked", false);
                    for (var i = 0, iLen = oSettings.aiDisplay.length; i < iLen; i++) {
                        $('td:eq(0)', oSettings.aoData[oSettings.aiDisplay[i]].nTr).html(i + 1);
                    }
                }
            },
            "aLengthMenu": [[10, 15, 20, 40, 100], [10, 15, 20, 40, 100]],
            "iDisplayLength": 15,
            "bStateSave": false,
            "bAutoWidth": false,

            "language": {
                "decimal": "",
                "emptyTable": "没有数据",
                "info": "当前数据为从第 _START_ 到第 _END_ 条数据；总共有 _TOTAL_ 条记录",
                "infoEmpty": "当前数据为从第 0 到第 0 条数据；总共有 0 条记录",
                "infoFiltered": "(从 _MAX_ 条记录过滤)",
                "infoPostFix": "",
                "thousands": ",",
                "lengthMenu": "每页显示 _MENU_ 条记录",
                "loadingRecords": "加载中...",
                "processing": "处理中...",
                "search": "搜索:",
                "zeroRecords": "没有找到匹配的记录",
                "paginate": {
                    "first": "首页",
                    "last": "尾页",
                    "previous": "上一页",
                    "next": "下一页"
                }
            }
        }, options);
    };

    DatatableWrapper.prototype.getDatatable = function () {
        return this.dt;
    };

    DatatableWrapper.prototype.render = function (dtContainerId, renderOptions) {
        this.dt = $('#' + dtContainerId).dataTable(renderOptions);
    };

    DatatableWrapper.prototype.delEntries = function (url) {
        var ids = [];
        $("input[name=ids]:checked").each(function () {
            ids.push($(this).val());
        });
        if (ids.length == 0) {
//            alert("没有选择要删除的记录!");
            tips("请至少勾选一个项目！","没有勾选要删除的记录!","error");
            return;
        }
        var pthis = this;
        if (confirm("确定要删除吗?")) {
            $.post(url, {"ids": ids}, function (data) {
//                DatatableWrapper.showMessage(data.msg);
                tips("删除成功",data.msg,"success");
                pthis.queryData();
            });
        }
    };

    DatatableWrapper.prototype.queryData = function () {
        this.dt.fnDraw();
    };

    DatatableWrapper.prototype.reset = function (formId) {
        $("#" + formId + " .canreset").each(function () {
            $(this).val("");
        });
        this.queryData();
    };

    DatatableWrapper.prototype.selectAll = function (followObj) {
        var checked = true;
        if (!(followObj != null && followObj.checked)) {
            checked = false;
        }
        var items = document.getElementsByName('ids');
        for (var i = 0; i < items.length; i++) {
            items[i].checked = checked;
        }
    };
</script>
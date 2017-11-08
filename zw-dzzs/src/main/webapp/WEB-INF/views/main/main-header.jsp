<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<style>
    .main-header .sidebar-toggle {
        font-family: "Glyphicons Halflings";
    }

    .main-header .sidebar-toggle::before {
        content: "\e236";
    }
</style>
<script type="text/javascript">
    $().ready(function () {
        var currentUserDisName = $("#displayName").html();
        if (currentUserDisName) {
            sessionStorage.setItem("curDisName", currentUserDisName);
        } else {
            var currentUserDisName = sessionStorage.getItem("curDisName");
            $("#displayName").html(currentUserDisName);
        }
    });
</script>
<header class="main-header">
    <!-- Logo -->
    <a href="#" class="logo">
        <span class="logo-mini">电子证书</span>
        <span class="logo-lg">东湖高新电子证书管理平台</span>
    </a>
    <!-- Header Navbar: style can be found in header.less -->
    <nav class="navbar navbar-static-top" role="navigation">
        <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
            <span class="sr-only">Toggle navigation</span>
        </a>

        <div class="navbar-custom-menu">
            <ul class="nav navbar-nav navbar-right" style="margin-right: 20px;">
                <%--<li><span style="padding:10px;color: #fff;font-size: 18px;line-height: 50px;" id="displayName">${curuser.displayName}</span></li>--%>
                <li><a title="主页面" href="${ctx}/" class="navbar-link"><i class="glyphicon glyphicon-home"></i></a></li>
                <li><a title="退出" href="${ctx}/logout" class="navbar-link"><i class="glyphicon glyphicon-off"></i></a>
                </li>
            </ul>
        </div>
    </nav>
</header>


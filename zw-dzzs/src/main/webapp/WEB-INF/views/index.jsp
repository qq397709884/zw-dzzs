<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
   <%@ include file="main/header.jsp"%>
</head>
<body class="hold-transition skin-green sidebar-mini">
<%@ include file="main/main-header.jsp"%>
<div class="wrapper" id="rrapp">
   <aside class="main-sidebar" >
      <section class="sidebar">
         <ul class="sidebar-menu" id="create">
            <li class="header">导航菜单</li>

            <!-- vue生成的菜单 -->
            <menu-item :item="item" v-for="item in menuList"></menu-item>
         </ul>
      </section>
   </aside>
   <div class="content-wrapper">
      <section class="content" style="background:#fff;">
         <iframe scrolling="yes" frameborder="0" style="width:100%;min-height:200px;overflow:visible;background:#fff;" :src="main"></iframe>
      </section>
   </div>
   <%@ include file="main/main-footer.jsp"%>
</div>
</body>
<script type="text/javascript" src="${ctx}/scripts/vue/vue.min.js"></script>
<script type="text/javascript" src="${ctx}/scripts/vue/router.js"></script>
<script type="text/javascript" src="${ctx}/static/js/index.js"></script>
</html>

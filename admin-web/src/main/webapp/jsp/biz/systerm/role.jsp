<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>角色列表</title>
</head>
<%@ include file="../../util.jsp" %>
<body>
<div>
<table id="data_table" lay-filter="data_table_filter"></table>
	<script type="text/html" id="editBar">
      <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
	</script>
</body>
<script type="text/javascript" src="<%=path%>/js/my/biz/systerm/role.js"></script>
</html>
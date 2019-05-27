<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>字典列表</title>
</head>
<body>
<%@ include file="../../util.jsp" %>
<div  style="margin-top: 20px">
   <form class="layui-form" lay-filter="form_filter">
     <div class="layui-form-item">
	    <label class="layui-form-label">字典名称:</label>
	      <div class="layui-input-inline">
	            <input type="text" name="search" class="layui-input" placeholder="请字典名称" />
	      </div>
	      <div>
		      <button class="layui-btn" lay-submit lay-filter="serachButton">搜索</button>
		  </div>
      </div>
  </form>
</div>
<div>
<table id="data_table" lay-filter="data_table_filter"></table>
	<script type="text/html" id="editBar">
      <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
	</script>
</body>
<script type="text/javascript" src="<%=path%>/js/my/biz/systerm/dictionary.js"></script>
</html>
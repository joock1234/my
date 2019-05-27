<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>路由管理</title>
</head>
<%@ include file="../../util.jsp" %>
<body>
   <div  style="margin-top: 20px">
		<form class="layui-form" lay-filter="route_form_filter">
		     <div class="layui-form-item">
			    <label class="layui-form-label">路由名称:</label>
			      <div class="layui-input-inline">
			            <input type="text" name="search" class="layui-input" placeholder="请路由名称" />
			      </div>
			      <div>
				      <button class="layui-btn" lay-submit lay-filter="serachButton">搜索</button>
				  </div>
		      </div>
		  </form>
	</div>
	<div>
	    <table id="route_table" lay-filter="route_table_filter"></table>
	</div>

</body>
<script type="text/html" id="tablebar">
  <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
</script>
<script type="text/javascript" src="<%=path%>/js/my/biz/product-config/route.js"></script>
</html>
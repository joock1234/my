<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>费率方案列表</title>
</head>
<%@ include file="../../util.jsp" %>
<body>

	<div  style="margin-top: 20px">
		<form class="layui-form" lay-filter="rateSchema_form_filter">
		     <div class="layui-form-item">
			    <label class="layui-form-label">费率方案名称:</label>
			      <div class="layui-input-inline">
			            <input type="text" name="search" class="layui-input" placeholder="请费率方案名称" />
			      </div>
			      <div>
				      <button class="layui-btn" lay-submit lay-filter="serachButton">搜索</button>
				  </div>
		      </div>
		  </form>
	</div>
	<div>
	    <table id="rateSchema_table" lay-filter="rateSchema_table_filter"></table>
	</div>

</body>
<script type="text/html" id="rateSchemaTablebar">
  <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
</script>
<script type="text/javascript" src="<%=path%>/js/my/biz/product-config/rateSchema.js"></script>
</html>
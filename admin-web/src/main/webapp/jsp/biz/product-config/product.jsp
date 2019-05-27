<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>产品列表</title>
</head>
<%@ include file="../../util.jsp" %>
<body>
<div  style="margin-top: 20px">
   <form class="layui-form" lay-filter="form_filter">
     <div class="layui-form-item">
	    <label class="layui-form-label">产品名称:</label>
	      <div class="layui-input-inline">
	            <input type="text" name="search" class="layui-input" placeholder="请产品名称" />
	      </div>
	      <div>
		      <button class="layui-btn" lay-submit lay-filter="serachButton">搜索</button>
		  </div>
      </div>
  </form>
</div>
<div>
<table id="data_table" lay-filter="data_table_filter"></table>
<script type="text/html" id="barDemo">
  <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
</script>
<script type="text/html" id="rateSchemaBar">
  <a class="layui-btn layui-btn-xs" lay-event="confirm">确认</a>
</script>

<script type="text/html" id="constraintBar">
  <a class="layui-btn layui-btn-xs" lay-event="confirm">确认</a>
</script>

</div>
</body>

<div id="open_div"  style="display: none;">
 <form class="layui-form" lay-filter="form_filter_open">
	     <div class="layui-form-item">
		      <label class="layui-form-label">产品码:</label>
		      <div class="layui-input-inline">
		            <input type="text" id="open_productCode" readonly="readonly" name="productCode" class="layui-input"/>
		      </div>
		</div>
		<div class="layui-form-item">
		       <label class="layui-form-label">产品名称:</label>
		      <div class="layui-input-inline">
		            <input type="text" id="open_name"  name="name" class="layui-input"/>
		      </div>
		</div>

		<div class="layui-form-item">
		       <label class="layui-form-label">授权交易类型：</label>
		      <div class="layui-input-inline">
		            <input type="text" id="open_authedTransType" readonly="readonly"  name="authedTransType" class="layui-input"/>
		      </div>
		</div>
		<div class="layui-form-item">
		       <label class="layui-form-label">费率方案：</label>
		      <div class="layui-input-inline">
		            <input type="hidden" id="open_rateSchemaId" name="rateSchemaId">
		            <input type="text" id="open_rateSchemaName" name="rateSchemaName" class="layui-input" onclick="rateSchemaClick()"/>
		      </div>
		</div>
		<div class="layui-form-item">
		       <label class="layui-form-label">渠道约束条件：</label>
		      <div class="layui-input-inline">
		            <input type="hidden" id="open_constraintId" name="constraintId">
		            <input type="text" id="open_constraintName" name="constraintName" class="layui-input" onclick="constraintClick()"/>
		      </div>
		</div>

		<div class="layui-form-item">
		       <label class="layui-form-label">是否开通：</label>
		      <div class="layui-input-inline">
		             <select id="open_enabled" name="open_enabled" lay-verify="required">
				          <option value="0">废弃或者未开通</option>
				          <option value="1">正常开通</option>
				      </select>
		      </div>
		</div>
		<div class="layui-form-item">
		       <label class="layui-form-label">修改交易类型:</label>
                <div class="layui-input-block" id="open_transTypeList">
		         </div>
		</div>
		<div class="layui-form-item">
		      <label class="layui-form-label"></label>
		       <div class="layui-input-inline">
		            <button class="layui-btn" lay-submit lay-filter="openDivButton">保存</button>
		       </div>
		</div>
  </form>
</div>

<div id="rateSchema_open" style="display: none;">
   <form class="layui-form" lay-filter="form_filter_rateSchema_open">
     <div class="layui-form-item">
	    <label class="layui-form-label">方案名称:</label>
	      <div class="layui-input-inline">
	            <input type="text" name="rateSchema_open_name" class="layui-input" placeholder="请输入方案名称" />
	      </div>
	      <label class="layui-form-label">方案类型：</label>
	       <div class="layui-input-inline">
		      <select name="rateSchema_select" id="rateSchema_select" lay-filter="rateSchema_select">
                     <option value="">所有</option>
		      </select>
	      </div>
	      <div>
		      <button class="layui-btn" lay-submit lay-filter="serachRateSchemaButton">搜索</button>
		  </div>
      </div>
  </form>
   <table id="rateSchema_data_table" lay-filter="rateSchema_data_table_filter"></table>
</div>

<div id="constraint_open" style="display: none;">
   <form class="layui-form" lay-filter="form_filter_constraint_open">
     <div class="layui-form-item">
	    <label class="layui-form-label">约束名称:</label>
	      <div class="layui-input-inline">
	            <input type="text" name="constraint_open_name" class="layui-input" placeholder="请输入约束名称" />
	      </div>
	      <div>
		      <button class="layui-btn" lay-submit lay-filter="serachConstraintButton">搜索</button>
		  </div>
      </div>
  </form>
   <table id="constraint_data_table" lay-filter="constraint_data_table_filter"></table>
</div>


<script type="text/javascript" src="<%=path%>/js/my/biz/product-config/product.js"></script>
</html>
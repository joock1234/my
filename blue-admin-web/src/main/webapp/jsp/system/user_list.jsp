<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
</head>
<body>
<div  style="margin-top: 20px">
   <form class="layui-form" lay-filter="form_filter">
     <div class="layui-form-item">
	    <label class="layui-form-label">用户名称:</label>
	      <div class="layui-input-inline">
	            <input type="text" name="search" class="layui-input" placeholder="用户名称" />
	      </div>
	      <div>
		      <button class="layui-btn" lay-submit lay-filter="serachButton">搜索</button>
		  </div>
      </div>
  </form>
</div>
<div>
<table id="data_table" lay-filter="data_table_filter"></table>
</div>
</body>
<script type="text/javascript">
layui.use(['layer','table','form'], function(){
	  var layer = layui.layer;
	  var $ = layui.$;
	  var form = layui.form;
	  var web_path=$("#web_path").val();
	  var table = layui.table;

	  table.render({
		    elem: '#data_table'
		    ,height: 'auto'
		    ,width:'auto'
		    ,loading:true
		    ,url:  web_path+'/user/list' //数据接口
		    ,page: true //开启分页
		    ,cols: [[ //表头
			      {field:'id', title:'用户ID', width:180, sort: true, fixed: 'left'}
			      ,{field: 'username', title: '用户名称', width:580}
			      ,{field: 'section', title: '所属部门', width:180}
			      /* ,{field: 'enabled', title: '是否开通', width: 100,
			    	  templet: function(d){
			    		    if(d.enabled==0){
			    		    	return "未开通";
			    		    }else{
			    		    	return "已开通";
			    		    }
			    	  }
			      } */
		    ]]
		  });

	  //form.render();


	  //监听提交
	  form.on('submit(serachButton)', function(data){
		  table.render({
			    elem: '#data_table'
			    ,height: 'auto'
			    ,width:'auto'
			    ,loading:true
			    ,url:  web_path+'/user/list' //数据接口
			    ,page: true //开启分页
			    ,where:{userName:data.field.search}
		        ,method: 'post'
			    ,cols: [[ //表头
		              {field:'id', title:'用户ID', width:180, sort: true, fixed: 'left'}
				      ,{field: 'username', title: '用户名称', width:580}
				      ,{field: 'section', title: '所属部门', width:180}
			    ]]
			  });
	    return false;
	  });

});
</script>
</html>
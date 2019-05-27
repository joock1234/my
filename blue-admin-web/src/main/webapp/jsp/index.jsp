<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
	String path = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>后台首页</title>
  <link rel="stylesheet" href="<%=path %>/js/layui/css/layui.css">
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
  <div class="layui-header">
    <div class="layui-logo">layui 后台布局</div>
    <!-- 头部区域（可配合layui已有的水平导航） -->
    <ul class="layui-nav layui-layout-left">
      <li class="layui-nav-item"><a href="">控制台</a></li>
      <li class="layui-nav-item"><a href="">商品管理</a></li>
      <li class="layui-nav-item"><a href="">用户</a></li>
      <li class="layui-nav-item">
        <a href="javascript:;">其它系统</a>
        <dl class="layui-nav-child">
          <dd><a href="">邮件管理</a></dd>
          <dd><a href="">消息管理</a></dd>
          <dd><a href="">授权管理</a></dd>
        </dl>
      </li>
    </ul>
    <ul class="layui-nav layui-layout-right">
      <li class="layui-nav-item">
        <a href="javascript:;">
          <img src="http://t.cn/RCzsdCq" class="layui-nav-img">
          贤心
        </a>
        <dl class="layui-nav-child">
          <dd><a href="">基本资料</a></dd>
          <dd><a href="">安全设置</a></dd>
        </dl>
      </li>
      <li class="layui-nav-item"><a href="">退了</a></li>
    </ul>
  </div>

  <div class="layui-side layui-bg-black">
    <div class="layui-side-scroll">
      <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
      <ul id="zuo_caidan_ul" class="layui-nav layui-nav-tree"  lay-filter="zuo_ul_body">
        <!-- <li class="layui-nav-item">
          <a id="test.jsp" href="javascript:;">所有商品</a>
          <dl class="layui-nav-child">
            <dd><a href="javascript:;">列表一</a></dd>
            <dd><a href="javascript:;">列表二</a></dd>
            <dd><a href="javascript:;">列表三</a></dd>
            <dd><a href="">超链接</a></dd>
          </dl>
        </li>    -->
      </ul>
    </div>
  </div>

  <div id="layui-body_id" class="layui-body">
    <!-- 内容主体区域 -->
    <div style="padding: 15px;">欢迎</div>
  </div>

  <div class="layui-footer">
    <!-- 底部固定区域 -->
    © layui.com - 底部固定区域
  </div>
</div>

<input id="web_path" type="hidden" value="<%=path%>">


</body>
<script type="text/javascript" src="<%=path%>/js/layui/layui.js"></script>
<script type="text/javascript">

layui.use(['layer','element'], function(){
	  var element = layui.element;
	  var layer = layui.layer;
	  var $ = layui.$;

	  var web_path=$("#web_path").val();

	  //一些事件监听
	  element.on('nav(zuo_ul_body)', function(data){
		  //layer.msg(data);
		   //debugger
		  var value=data.context.id;
		   if(null!=value&&""!=value){
			  $("#layui-body_id").load(value);
		   }

	  });
	  var url =web_path+"/index/getMenu";
	  $.post(url,null,function(res){
		  if(res.code==0){
			  //debugger
			  var menuList=res.data;
			  var html="";
			  for (var i = 0; i < menuList.length; i++) {
				   var menu=menuList[i];
				   if(menu.parentId == 0){
					   html+=' <li class="layui-nav-item"><a href="javascript:;">'+menu.name+'</a><dl class="layui-nav-child">';
					 //debugger
					   for (var j = 0; j < menuList.length; j++) {
						   var menu1=menuList[j];
						   if(menu1.parentId==menu.id){
							   //if(menu1.menuType == 1){
								   var u=web_path+"/"+menu1.url;
								   html+='<dd><a id="'+u+'" href="javascript:;"rel="'+menu1.id+'">'+menu1.name+'</a></dd>';
							   //}
						   }
					   }
					   html+='</dl></li>';
				   }
			  }
			  $("#zuo_caidan_ul").append(html);

			  element.render();

		  }else{
			  layer.msg(res.msg);
			  location.href=web_path+"/jsp/login.jsp";
		  }
	  });


	});
</script>

</html>
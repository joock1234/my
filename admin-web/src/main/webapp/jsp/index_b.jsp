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
  <div class="layui-body">
      <div class="layui-tab" lay-filter="body-tab-filter" lay-allowclose="true">
	  <ul class="layui-tab-title" id="body-tab-title">
	   <!--  <li class="layui-this" lay-id="11">网站设置</li>
	    <li lay-id="22">用户管理</li>
	    <li lay-id="33">权限分配</li>
	    <li lay-id="44">商品管理</li>
	    <li lay-id="55">订单管理</li> -->
	  </ul>
     <div class="layui-tab-content" id="body-tab-content">
	   <!--  <div class="layui-tab-item layui-show">
	               点击该Tab的任一标题，观察地址栏变化，再刷新页面。选项卡将会自动定位到上一次切换的项
	    </div>
	    <div class="layui-tab-item">内容2</div>
	    <div class="layui-tab-item">内容3</div>
	    <div class="layui-tab-item">内容4</div>
	    <div class="layui-tab-item">内容5</div> -->
     </div>
  </div>

  </div>



  <div class="layui-footer">
    <!-- 底部固定区域 -->
    © layui.com - 底部固定区域
  </div>
</div>

<input id="web_path" type="hidden" value="<%=path%>">


</body>
<script type="text/javascript" src="<%=path%>/js/layui/layui.js"></script>
<script type="text/javascript" src="<%=path%>/js/my/common.js"></script>
<script type="text/javascript">

layui.use(['layer','element'], function(){
	  var element = layui.element;
	  var layer = layui.layer;
	  var $ = layui.$;

	  var web_path=$("#web_path").val();

	  //一些事件监听
	  element.on('nav(zuo_ul_body)', function(data){
		  //layer.msg(data);
		  // debugger
		  var value=data.context.id;
		  var text= data.context.innerText;
		   if(null!=value&&""!=value){
			  //$("#layui-body_id").load(value);
			  //新增一个Tab项
			  var content='<div class="layui-tab-item layui-show">'
			     +'<iframe src="'+value+'" frameborder="0" height="800px" width="100%" ></iframe>'
			     +'</div>';
			     if ( $(".layui-tab-title li[lay-id='"+value+"']").length > 0 ) {
			    	 element.tabChange('body-tab-filter', value);
		    	 }else{
		    		 element.tabAdd('body-tab-filter', {
					        title: text
					       ,content:content
					       ,id: value
				       });
				       element.tabChange('body-tab-filter', value);
		    	 }
		   }

	  });
	  var url =web_path+"/index";
	  $.post(url,null,function(res){
		  if(res.code==0){
			  //debugger
			  var data=res.data;
			  var menuList=data.menuList;
			  var html="";
			  for (var i = 0; i < menuList.length; i++) {
				   var menu=menuList[i];
				   if(menu.parentId == 0){
					   html+=' <li class="layui-nav-item"><a href="javascript:;">'+menu.menuName+'</a><dl class="layui-nav-child">';
					 //debugger
					   for (var j = 0; j < menuList.length; j++) {
						   var menu1=menuList[j];
						   if(menu1.parentId==menu.menuId){
							   if(menu1.menuType == 1){
								   var u=web_path+"/"+menu1.newUrl;
								   html+='<dd><a id="'+u+'" href="javascript:;"rel="'+menu1.menuId+'">'+menu1.menuName+'</a></dd>';
							   }
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
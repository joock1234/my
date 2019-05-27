<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
	String path = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录</title>
  <link rel="stylesheet" href="<%=path %>/js/layui/css/layui.css">
  <link rel="stylesheet" href="<%=path %>/css/login.css">
</head>
<body>
  <div class="login-wrapper">

    <div class="login-header">
        <img src="<%=path %>/images/logo.png"> xxxxWeb
    </div>
     <div class=" login-body">
     <div class="layui-card">
            <div class="layui-card-header">
                <i class="layui-icon layui-icon-engine"></i>&nbsp;&nbsp;用户登录
            </div>
            <div class="layui-card-body layui-form layui-form-pane">
	                <form class="layui-form">
					  <div class="layui-form-item">
					    <label class="layui-form-label"><i class="layui-icon layui-icon-username"></i></label>
					    <div class="layui-input-inline">
					      <input type="text" name="loginName" required  lay-verify="required" placeholder="请输入标题" autocomplete="off" class="layui-input">
					    </div>
					  </div>
					  <div class="layui-form-item">
					    <label class="layui-form-label"><i class="layui-icon layui-icon-password"></i></label>
					    <div class="layui-input-inline">
					      <input type="password" name="password" required lay-verify="required" placeholder="请输入密码" autocomplete="off" class="layui-input">
					    </div>
					  </div>

					  <div class="layui-form-item">
	                    <label class="layui-form-label"><i class="layui-icon layui-icon-vercode"></i></label>
	                    <div class="layui-input-block">
	                        <div class="layui-row inline-block">
	                            <div class="layui-col-xs7">
	                                <input name="code" type="text" lay-verify="required" placeholder="验证码"
	                                       class="layui-input">
	                            </div>
	                            <div class="layui-col-xs5" style="padding-left: 10px;">
	                                <img class="login-captcha" src="https://www.oschina.net/action/user/captcha">
	                            </div>
	                       </div>
                     </div>

                     <div class="layui-form-item">
	                    <a href="javascript:;" class="layui-link">帐号注册</a>
	                    <a href="javascript:;" class="layui-link pull-right">忘记密码？</a>
                     </div>

					 <div class="layui-form-item">
					    <div class="layui-input-block">
					      <button class="layui-btn" lay-submit lay-filter="formDemo">立即提交</button>
					      <button type="reset" class="layui-btn layui-btn-primary">重置</button>
					    </div>
					 </div>
                </div>
			   </form>


            </div>

     </div>
</div>

</body>
<script type="text/javascript" src="<%=path %>/js/layui/layui.js"></script>

<script type="text/javascript">
layui.use(['layer','form'], function(){
	  var form = layui.form;
	  var $ = layui.$;
	  var layer = layui.layer;
	  //监听提交
	  form.on('submit(formDemo)', function(data){
		  var url ="<%=path%>/doLogin";
		  var param={"loginName":data.field.loginName,"password":data.field.password};
		  $.post(url,param,function(res){
			  if(res.code==0){
				  location.href="<%=path%>/jsp/index_b.jsp";
			  }else{
				  layer.msg(res.msg);
			  }
		  });
	    return false;
	  });
	});
</script>



</html>
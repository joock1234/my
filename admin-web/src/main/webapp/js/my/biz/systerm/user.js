//初始对象
var layer,$,form,web_path,table;

layui.use(['layer','table','form'], function(){
	  layer = layui.layer;
	  $ = layui.$;
	  form = layui.form;
	  web_path=$("#web_path").val();
	  table = layui.table;

	  renderUserTable(new Map());


});

//参数 传递map
function renderUserTable(map){
	var map2jsonObject=MapTOJsonObject(map);
	 table.render({
		    elem: '#data_table'
		    ,height: 'auto'
		    ,width:'auto'
		    ,loading:true
		    ,url:  web_path+'/user/query/list' //数据接口
		    ,page: true //开启分页
		    ,cellMinWidth:80
		    ,where:map2jsonObject
		    ,method: 'post'
		    ,cols: [[ //表头
		           {field:'userId', title:'用户ID'}
			      ,{field:'userName', title:'用户名'}
			      ,{field: 'trueName', title: '姓名'}
			      ,{field: 'accountId', title: '账号'}
			      ,{field: 'roleName', title: '角色'}
			      ,{field: 'email', title: '邮箱'}
			      ,{field: 'mobile', title: '电话'}
			      ,{field: 'createTime', title: '创建时间'}
			      ,{field: 'modifyTime', title: '修改时间'}
			      ,{field: 'modifyBy', title: '修改人'}
			      ,{fixed: 'right', title:'操作',align:'center', toolbar: '#editBar'}
		    ]]
	});

}
//初始对象
var layer,$,form,web_path,table;

layui.use(['layer','table','form'], function(){
	  layer = layui.layer;
	  $ = layui.$;
	  form = layui.form;
	  web_path=$("#web_path").val();
	  table = layui.table;

	  renderTable(new Map());


});

//参数 传递map
function renderTable(map){
	var map2jsonObject=MapTOJsonObject(map);
	 table.render({
		    elem: '#data_table'
		    ,height: 'auto'
		    ,width:'auto'
		    ,loading:true
		    ,url:  web_path+'/role/query/list' //数据接口
		    ,cellMinWidth:80
		    ,where:map2jsonObject
		    ,method: 'post'
		    ,cols: [[ //表头
		           {field:'roleId', title:'角色'}
			      ,{field:'roleName', title:'角色名称'}
			      ,{field: 'accountId', title: '账户号'}
			      ,{field: 'isPublic', title: '是否公用'}
			      ,{field: 'createTime', title: '创建时间'}
			      ,{field: 'modifyTime', title: '最后修改时间'}
			      ,{fixed: 'right', title:'操作',align:'center', toolbar: '#editBar'}
		    ]]
	});

}
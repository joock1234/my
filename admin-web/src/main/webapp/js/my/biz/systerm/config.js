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
		    ,url:  web_path+'/config/query/list' //数据接口
		    ,page: true //开启分页
		    ,cellMinWidth:180
		    ,where:map2jsonObject
		    ,method: 'post'
		    ,cols: [[ //表头
		           {field:'moduleName', title:'模块名称'}
			      ,{field:'theKey', title:'参数名称'}
			      ,{field: 'theValue', title: '参数值'}
			      ,{field: 'remark', title: '备注'}
			      ,{fixed: 'right', title:'操作',align:'center', toolbar: '#editBar'}
		    ]]
	});

}
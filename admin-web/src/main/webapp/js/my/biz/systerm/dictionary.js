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
		    ,url:  web_path+'/dictionary/query/list' //数据接口
		    ,page: true //开启分页
		    ,cellMinWidth:80
		    ,where:map2jsonObject
		    ,method: 'post'
		    ,cols: [[ //表头
		           {field:'table', title:'表名'}
			      ,{field:'column', title:'列名'}
			      ,{field: 'value', title: '列值'}
			      ,{field: 'desc', title: '列值意义'}
			      ,{field: 'orderBy', title: '排序'}
			      ,{field: 'createTime', title: '创建时间'}
			      ,{field: 'createBy', title: '创建人'}
			      ,{field: 'remark', title: '备注信息'}
			      ,{fixed: 'right', title:'操作',align:'center', toolbar: '#editBar'}
		    ]]
	});

}
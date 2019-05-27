//初始对象
var layer,$,form,web_path,table;

layui.use(['layer','table','form'], function(){
	  layer = layui.layer;
	  $ = layui.$;
	  form = layui.form;
	  web_path=$("#web_path").val();
	  table = layui.table;

	  renderConstraintTable(new Map());


});

//参数 传递map
function renderConstraintTable(map){
	var map2jsonObject=MapTOJsonObject(map);
	 table.render({
		    elem: '#data_table'
		    ,height: 'auto'
		    ,width:'auto'
		    ,loading:true
		    ,url:  web_path+'/constraint/query/list' //数据接口
		    ,page: true //开启分页
		    ,cellMinWidth:150
		    ,where:map2jsonObject
		    ,method: 'post'
		    ,cols: [[ //表头
		           {field:'constraintId', title:'约束ID'}
			      ,{field:'constraintName', title:'约束名称'}
			      ,{field: 'constraintType', title: '约束类型', width:400}
			      ,{field: 'creditEnabled', title: '是否允许信用卡交易'}
			      ,{field: 'creditFields', title: '信用卡交易条件'}
			      ,{field: 'debitEnabled', title: '是否允许借记卡交易'}
			      ,{field: 'debitFields', title: '借记卡交易条件'}
			      ,{field: 'onlyUseChannel', title: '指定渠道'}
			      ,{field: 'notUseChannel', title: '排除渠道'}
			     ,{fixed: 'right', title:'操作',align:'center', toolbar: '#editBar'}
		    ]]
	});

}
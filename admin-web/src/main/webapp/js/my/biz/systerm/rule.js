//初始对象
var layer,$,form,web_path,table;

layui.use(['layer','table','form'], function(){
	  layer = layui.layer;
	  $ = layui.$;
	  form = layui.form;
	  web_path=$("#web_path").val();
	  table = layui.table;

	  renderRuleTable(new Map());


});

//参数 传递map
function renderRuleTable(map){
	var map2jsonObject=MapTOJsonObject(map);
	 table.render({
		    elem: '#data_table'
		    ,height: 'auto'
		    ,width:'auto'
		    ,loading:true
		    ,url:  web_path+'/rule/query/list' //数据接口
		    ,page: true //开启分页
		    ,cellMinWidth:80
		    ,where:map2jsonObject
		    ,method: 'post'
		    ,cols: [[ //表头
		           {field:'ruleId', title:'规则ID'}
			      ,{field:'ruleName', title:'规则名称'}
			      ,{field: 'jsonPara', title: '规则参数'}
			      ,{field: 'createTime', title: '创建时间'}
			      ,{field: 'updateTime', title: '修改时间'}
			      ,{field: 'state', title: '状态'}
			      ,{field: 'scriptPath', title: '脚本路径'}
			      ,{field: 'remark', title: '备注'}
			      ,{fixed: 'right', title:'操作',align:'center', toolbar: '#editBar'}
		    ]]
	});

}
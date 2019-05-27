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
		    ,url:  web_path+'/cardbin/search/list' //数据接口
		    ,page: true //开启分页
		    ,cellMinWidth:120
		    ,where:map2jsonObject
		    ,method: 'post'
		    ,cols: [[ //表头
		           {field:'id', title:'卡BinID'}
			      ,{field:'bankName', title:'银行名称'}
			      ,{field: 'bankCode', title: '银行缩写'}
			      ,{field: 'cardBin', title: '卡BIN'}
			      ,{field: 'cardName', title: '卡名'}
			      ,{field: 'issBankName', title: '发卡行名称'}
			      ,{field: 'cardType', title: '卡类型'}
			      ,{fixed: 'right', title:'操作',align:'center', toolbar: '#editBar'}
		    ]]
	});

}
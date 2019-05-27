//初始对象
var layer,$,form,web_path,table;

layui.use(['layer','table','form'], function(){
	  layer = layui.layer;
	  $ = layui.$;
	  form = layui.form;
	  web_path=$("#web_path").val();
	  table = layui.table;

	  renderTerminalTable(new Map());


});

//参数 传递map
function renderTerminalTable(map){
	var map2jsonObject=MapTOJsonObject(map);
	 table.render({
		    elem: '#data_table'
		    ,height: 'auto'
		    ,width:'auto'
		    ,loading:true
		    ,url:  web_path+'/terminal/query/list' //数据接口
		    ,page: true //开启分页
		    ,cellMinWidth:150
		    ,where:map2jsonObject
		    ,method: 'post'
		    ,cols: [[ //表头
		           {field:'terminalId', title:'终端ID'}
			      ,{field:'terminalType', title:'终端类型'}
			      ,{field: 'terminalSn', title: '终端SN'}
			      ,{field: 'isMobileable', title: '是否可移动'}
			      ,{field: 'spCode', title: '商户编号'}
			      ,{field: 'spName', title: '所属商户'}
			      ,{field: 'state', title: '终端状态'}
			      ,{field: 'createTime', title: '创建时间'}
			      ,{field: 'createBy', title: '创建人'}
			      ,{field: 'bluetoothName', title: '蓝牙名称'}
			      ,{field: 'tmKey', title: 'POS机的tmKey'}
			      ,{field: 'attribute', title: '属性位'}
			      ,{field: 'remark', title: '备注信息'}
			     ,{fixed: 'right', title:'操作',align:'center', toolbar: '#editBar'}
		    ]]
	});

}
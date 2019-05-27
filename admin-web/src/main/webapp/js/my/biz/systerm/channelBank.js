//初始对象
var layer,$,form,web_path,table;

layui.use(['layer','table','form'], function(){
	  layer = layui.layer;
	  $ = layui.$;
	  form = layui.form;
	  web_path=$("#web_path").val();
	  table = layui.table;

	  renderChannelBankTable(new Map());


});

//参数 传递map
function renderChannelBankTable(map){
	var map2jsonObject=MapTOJsonObject(map);
	 table.render({
		    elem: '#data_table'
		    ,height: 'auto'
		    ,width:'auto'
		    ,loading:true
		    ,url:  web_path+'/channelBank/query/list' //数据接口
		    ,page: true //开启分页
		    ,cellMinWidth:80
		    ,where:map2jsonObject
		    ,method: 'post'
		    ,cols: [[ //表头
		           {field:'bankId', title:'合作机构编号'}
			      ,{field:'bankName', title:'合作机构名称'}
			      ,{field: 'host', title: '合作机构主机地址'}
			      ,{field: 'port', title: '合作机构主机端口'}
			      ,{field: 'isProxy', title: '是否使用代理'}
			      ,{field: 'proxyHost', title: '代理地址'}
			      ,{field: 'proxyPort', title: '代理端口号'}
			      ,{field: 'state', title: '状态'}
			      ,{field: 'createTime', title: '创建时间'}
			      ,{field: 'createBy', title: '创建人'}
			      ,{field: 'createTime', title: '创建时间'}
			      ,{field: 'createBy', title: '创建人'}
			     ,{fixed: 'right', title:'操作',align:'center', toolbar: '#editBar'}
		    ]]
	});

}
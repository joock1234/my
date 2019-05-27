//初始对象
var layer,$,form,web_path,table;

layui.use(['layer','table','form'], function(){
	  layer = layui.layer;
	  $ = layui.$;
	  form = layui.form;
	  web_path=$("#web_path").val();
	  table = layui.table;

	  renderChannelTable(new Map());


});

//参数 传递map
function renderChannelTable(map){
	var map2jsonObject=MapTOJsonObject(map);
	 table.render({
		    elem: '#data_table'
		    ,height: 'auto'
		    ,width:'auto'
		    ,loading:true
		    ,url:  web_path+'/channel/query/list' //数据接口
		    ,page: true //开启分页
		    ,cellMinWidth:80
		    ,where:map2jsonObject
		    ,method: 'post'
		    ,cols: [[ //表头
		           {field:'channelId', title:'渠道ID'}
			      ,{field:'channelName', title:'渠道名称'}
			      ,{field: 'state', title: '渠道状态'}
			      ,{field: 'bankName', title: '合作结构'}
			      ,{field: 'bankSettleType', title: '清算方式'}
			      ,{field: 'mcc', title: '渠道MCC码'}
			      ,{field: 'debitFields', title: '渠道商户号'}
			      ,{field: 'bankPosid', title: '渠道终端号'}
			      ,{field: 'provinceId', title: '省份'}
			      ,{field: 'cityId', title: '城市'}
			      ,{field: 'createTime', title: '创建时间'}
			      ,{field: 'createBy', title: '创建人'}
			      ,{field: 'rateSchemaName', title: '渠道费率方案'}
			      ,{field: 'constraintName', title: '渠道约束'}
			      ,{field: 'isPrivate', title: '是否专用'}
			      ,{field: 'minAmount', title: '最小金额'}
			      ,{field: 'maxAmount', title: '最大金额'}
			      ,{field: 'cardDayLimit', title: '单卡单日限额'}
			      ,{field: 'bankspDayTimes', title: '商户单日限次数'}
			      ,{field: 'bankspDayLimit', title: '商户单日限额'}
			     ,{fixed: 'right', title:'操作',align:'center', toolbar: '#editBar'}
		    ]]
	});

}
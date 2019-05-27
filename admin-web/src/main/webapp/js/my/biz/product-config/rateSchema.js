//初始对象
var layer,$,form,web_path,table;

layui.use(['layer','table','form'], function(){
	  layer = layui.layer;
	  $ = layui.$;
	  form = layui.form;
	  web_path=$("#web_path").val();
	  table = layui.table;

	  renderRateSchemaTable(new Map());


});

//费率方案渲染
//参数 传递map
function renderRateSchemaTable(map){
	var map2jsonObject=MapTOJsonObject(map);
	 table.render({
		    elem: '#rateSchema_table'
		    ,height: 'auto'
		    ,width:'auto'
		    ,loading:true
		    ,url:  web_path+'/rateSchema/query/list' //数据接口
		    ,page: true //开启分页
		    ,cellMinWidth:100
		    ,where:map2jsonObject
		    ,method: 'post'
		    ,cols: [[ //表头
		           {field:'rateSchemaId', title:'费率方案ID'}
			      ,{field:'name', title:'费率方案名称'}
			      ,{field: 'applyTo', title: '适用对象',
			    	  templet: function(d){
			    		 var applyTo= d.applyTo;
			    		 var msg='未知'
			    		 if(applyTo=='SP'){
			    			 msg="商户";
			    		  }else if(applyTo=='PRODUCT'){
			    			  msg="产品";
			    		  }else if(applyTo=='CHANNEL'){
			    			  msg="渠道";
			    		  }
	    		    	return msg;
		    	       }
			      }
			      ,{field: 'jsonType', title: '方案类型',
			    	  templet: function(d){
		    		    	return d.jsonType+':'+d.jsonTypeValue;
		    	      }
			      }
			      ,{field: 'jsonRates', title: '费率内容'}
			      ,{field: 'remark', title:'备注'}
			     ,{fixed: 'right', title:'操作',align:'center', toolbar: '#rateSchemaTablebar'}
		    ]]
	});

}
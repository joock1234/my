//初始对象
var layer,$,form,web_path,table;

layui.use(['layer','table','form'], function(){
	  layer = layui.layer;
	  $ = layui.$;
	  form = layui.form;
	  web_path=$("#web_path").val();
	  table = layui.table;

	  renderRouteTable(new Map());


});

//参数 传递map
function renderRouteTable(map){
	var map2jsonObject=MapTOJsonObject(map);
	 table.render({
		    elem: '#route_table'
		    ,height: 'auto'
		    ,width:'auto'
		    ,loading:true
		    ,url:  web_path+'/route/query/list' //数据接口
		    ,page: true //开启分页
		    ,cellMinWidth:150
		    ,where:map2jsonObject
		    ,method: 'post'
		    ,cols: [[ //表头
		           {field:'routeId', title:'路由ID'}
			      ,{field:'routeName', title:'路由名称'}
			      ,{field: 'jsonPara', title: '路由参数'}
			      ,{field: 'createTime', title: '路由创建时间',
			    	  templet: function(d){
		    		    	return getStringDateToMinute(d.createTime);
			    	       }

			      }
			      ,{field: 'state', title: '路由当前状态',
			    	  templet: function(d){
			    		  if(d.state=='VALID'){
		    		    	   return '有效';
			    	       }else{
			    	    	   return '无效';
			    	       }
			          }
			      }
			      ,{field: 'remark', title:'备注'}
			     ,{fixed: 'right', title:'操作',align:'center', toolbar: '#tablebar'}
		    ]]
	});

}
//初始对象
var layer,$,form,web_path,table;

layui.use(['layer','table','form'], function(){
	  layer = layui.layer;
	  $ = layui.$;
	  form = layui.form;
	  web_path=$("#web_path").val();
	  table = layui.table;

	  renderProductTable(new Map());

	  //监听提交
	  form.on('submit(serachButton)', function(data){
		  var map =new Map();
		  map.set("name",data.field.search);
		  renderProductTable(map);
	    return false;
	  });

	   //监听行工具事件
	  //注：tool 是工具条事件名，data_table_filter 是 table 原始容器的属性 lay-filter="对应的值"
	  table.on('tool(data_table_filter)', function(obj){
	     var data = obj.data, //获得当前行数据
	     layEvent = obj.event; //获得 lay-event 对应的值
	     if(layEvent === 'detail'){
	      layer.msg('查看操作');
	    } else if(layEvent === 'del'){
	      layer.confirm('真的删除行么', function(index){
	        obj.del(); //删除对应行（tr）的DOM结构
	        layer.close(index);
	        //向服务端发送删除指令
	      });
	    } else if(layEvent === 'edit'){
	    	var index= layer.open({
	    		    id:"edit_open"
	    	        ,type: 1
	    	        ,title: '修改产品属性'
	    	        ,area: ['800px', '500px']
	    	        ,maxmin: true
	    	        ,content:$("#open_div")
	    	        /*,btn: ['保存', '关闭'] //只是为了演示
	    	        ,yes: function(){

	    	        }
	    	        ,btn2: function(){
	    	          layer.closeAll();
	    	        }*/
	    	        ,success: function(layero){
	    	        	//弹层的遮罩DOM元素和弹层元素就在同一层了，就不会挡着弹层了。
	    	        	var mask = $(".layui-layer-shade");
	    	            mask.appendTo(layero.parent());

	    	            var url =web_path+"/product/to/update";
	    	            $.post(url,{"spId":data.productCode},function(res){
	    	            	 if(res.code==0){
                               var info=res.data.info;
                               var transTypeList=res.data.transTypeList;

                               $("#open_productCode").val(info.productCode);
                               $("#open_name").val(info.name);
                               $("#open_authedTransType").val(info.authedTransType);
                               $("#open_rateSchemaName").val(info.rateSchemaName);
                               $("#open_enabled").val(info.enabled);

                               var authedTransTypes= info.authedTransTypes;

                               var html ="";
                               for (var int = 0; int < transTypeList.length; int++) {
								  var t=transTypeList[int];
                                   html +='<input type="checkbox" name="authedTransTypes_checkbox" value="'+t.value+'" title="'+t.desc+'" lay-skin="primary"';
                                   for (var int2 = 0; int2 < authedTransTypes.length; int2++) {
                                	   var a=authedTransTypes[int2];
                                       if(a==t.value){
                                    	   html+='checked';
                                       }
								   }
                                   html +='>';
                              }
                              $("#open_transTypeList").append(html);

                               form.render();
	    	            	 }else{
	    	            		layer.msg('无此产品');
	    	            	 }

	    	            });

	    	        }
	    	      });
	       }

	      //保存更新产品
		  form.on('submit(openDivButton)', function(data){

			  debugger;
			  var field=data.field;
			  var name=field.name;
			  var open_enabled=field.open_enabled;
			  var constraintId=field.constraintId;
              var rateSchemaId=field.rateSchemaId;
              var authedTransTypes = [];
              $('#open_transTypeList input[type=checkbox]:checked').each(function(i){
	               authedTransTypes.push($(this).val());
              });
              var url=web_path+'/product/update';
              var param={
            	"name":name,
            	"enabled":open_enabled,
            	"rateSchemaId":rateSchemaId,
            	"constraintId":constraintId,
            	"authedTransTypes_temp":authedTransTypes
              }
              $.post(url,param,function(res){

            	  if(res.code==0){
            		  layer.close(index);
            		  table.render('data_table');
            	  }
            	  alert(res.msg);
              });


		      return false;
		  });

	  });

});

//产品表格渲染
//参数 传递map
function renderProductTable(map){
	var map2jsonObject=MapTOJsonObject(map);
	 table.render({
		    elem: '#data_table'
		    ,height: 'auto'
		    ,width:'auto'
		    ,loading:true
		    ,url:  web_path+'/product/query/list' //数据接口
		    ,page: true //开启分页
		    ,cellMinWidth:100
		    ,where:map2jsonObject
		       //,toolbar: '#barDemo'
		    ,method: 'post'
		    ,cols: [[ //表头
		           {field:'productCode', title:'产品Code'}
			      ,{field:'name', title:'产品名称'}
			      ,{field: 'authedTransType', title: '授权交易类型', width:400}
			      ,{field: 'rateSchemaName', title: '费率方案'}
			      ,{field: 'constraintName', title: '渠道约束条件'}
			      ,{field: 'enabled', title: '是否开通',
			    	  templet: function(d){
			    		    if(d.enabled==0){
			    		    	return "未开通";
			    		    }else{
			    		    	return "已开通";
			    		    }
			    	  }
			      }
			     ,{fixed: 'right', title:'操作',align:'center', toolbar: '#barDemo'}
		    ]]
	});

}

//费率模块表格渲染
//参数 传递map
function renderRateSchemaTable(map){
	var map2jsonObject=MapTOJsonObject(map);
	table.render({
	     elem: '#rateSchema_data_table'
	    ,height: 'auto'
	    ,width:'auto'
	    ,loading:true
	    ,url:  web_path+'/rateSchema/query/list/back?applyTo=PRODUCT'
	    ,page: true //开启分页
	    ,cellMinWidth:100
	    ,where:map2jsonObject
	    ,method: 'post'
	    ,cols: [[ //表头
		      {field:'name', title:'方案名称', width: 250}
		      ,{field: 'jsonType', title: '方案类型', width: 250,
		    	  templet: function(d){
                    return d.jsonType+":"+d.jsonTypeValue;
		    	  }
		      }
		     ,{field: 'remark', title: '备注', width: 150,}
		     ,{fixed: 'right', title:'操作', width: 100, align:'center', toolbar: '#rateSchemaBar'}
	    ]]
	  });

}
//费率input点击事件
function rateSchemaClick(){
		   var index = layer.open({
				    id:"edit_rateSchema"
				    ,type: 1
				    ,title: '修改产品费率模版'
				    ,area: ['800px', '500px']
				    ,maxmin: true
				    ,content:$("#rateSchema_open")
			        ,success: function(layero){
			        	//弹层的遮罩DOM元素和弹层元素就在同一层了，就不会挡着弹层了。
	    	        	var mask = $(".layui-layer-shade");
	    	            mask.appendTo(layero.parent());
                        //加载表格
	    	            renderRateSchemaTable(new Map());
	    	            //加载搜索下拉框
	    	            $.post(web_path+"/dictionary/get/item?table=t_rate_schema&column=Fjson_type",null,
	    	            		function(res){
                                     if(res.code==0){
                                          var data= res.data;
                                          var html="";
                                          for (var d in data) {
                                               html+=' <option value="'+data[d].value+'">'+data[d].desc+'</option>';
										  }
                                          $("#rateSchema_select").append(html);
                                          form.render();
                                     }
	    	            		}
	    	            );
	    	            //监听搜索
	    	      	   form.on('submit(serachRateSchemaButton)', function(data){
	    	      	         var map =new Map();
	    	      	         map.set("name",data.field.rateSchema_open_name);
	    	      	         map.set("jsonType",data.field.rateSchema_select);
	    	      	         renderRateSchemaTable(map);
	    	      	         return false;
	    	      	   });

			        }
            });

			 table.on('tool(rateSchema_data_table_filter)', function(obj){
				    var data = obj.data; //获得当前行数据
				    $("#open_rateSchemaName").val(data.name);
				    $("#open_rateSchemaId").val(data.rateSchemaId);
				    layer.close(index);
			 });
}
//约束input点击事件
function constraintClick(){
	var index = layer.open({
	    id:"edit_constraint"
	    ,type: 1
	    ,title: '修改产品约束模版'
	    ,area: ['800px', '500px']
	    ,maxmin: true
	    ,content:$("#constraint_open")
        ,success: function(layero){
        	//弹层的遮罩DOM元素和弹层元素就在同一层了，就不会挡着弹层了。
        	var mask = $(".layui-layer-shade");
            mask.appendTo(layero.parent());
            //加载表格
            renderConstraintTable(new Map());
            //监听搜索
      	   form.on('submit(serachConstraintButton)', function(data){
      	         var map =new Map();
      	         map.set("name",data.field.constraint_open_name);
      	         renderRateSchemaTable(map);
      	         return false;
      	   });

        }
	 });

	 table.on('tool(constraint_data_table_filter)', function(obj){
		    var data = obj.data; //获得当前行数据
		    $("#open_constraintName").val(data.constraintName);
		    $("#open_constraintId").val(data.constraintId);
		    layer.close(index);
	 });

}

//约束模块表格渲染
//参数 传递map
function renderConstraintTable(map){
	var map2jsonObject=MapTOJsonObject(map);
	table.render({
	     elem: '#constraint_data_table'
	    ,height: 'auto'
	    ,width:'auto'
	    ,loading:true
	    ,url:  web_path+'/constraint/query/list/back?constraintType=product'
	    ,page: true //开启分页
	    ,cellMinWidth:100
	    ,where:map2jsonObject
	    ,method: 'post'
	    ,cols: [[ //表头
		       {field:'constraintName', title:'约束名称', width: 250}
		      ,{fixed: 'right', title:'操作', width: 100, align:'center', toolbar: '#constraintBar'}
	    ]]
	  });

}

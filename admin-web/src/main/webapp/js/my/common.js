//参数 map
function MapTOJson(m) {
    var str = '{';
    var i = 1;
    m.forEach(function (item, key, mapObj) {
		if(mapObj.size == i){
    		str += '"'+ key+'":"'+ item + '"';
    	}else{
    		str += '"'+ key+'":"'+ item + '",';
    	}
    	i++;
    });
    str +='}';
    return str;
}

function MapTOJsonObject(m) {
  return JSON.parse(MapTOJson(m))
}
// yyyy-MM-dd HH:mm
//参数毫秒值
function getStringDateToMinute(times){
	var date = new Date(times);
	var year = date.getFullYear();
	var month = date.getMonth()+1;
	var day = date.getDate();
	var hour = date.getHours();
	var minute = date.getMinutes();
	//var second = date.getSeconds();
    return year+"-"+month+"-"+day+" "+hour+":"+minute;

}
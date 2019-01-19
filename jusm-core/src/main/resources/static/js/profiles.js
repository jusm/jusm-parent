var ProfilesHandler = {
		changeLoggerLevel:function(level){
			var json = {"configuredLevel":level};
			$.ajax({
				type: "POST",
				contentType:"application/json;charset=utf-8",  //发送信息至服务器时内容编码类型。             
			    url: "loggers/com.github",
			    data: JSON.stringify(json),
			    success: function(){
			    	 window.location.reload(); 
				}
			});
		},
		getLoggerLevel:function(pakagePath){
			var json = {"configuredLevel":level};
			$.ajax({
				type: "GET",
			    url: "loggers/com.github",
			    dataType: "json",
			    success: function(){
			    	 window.location.reload();
				}
			});
		}
}

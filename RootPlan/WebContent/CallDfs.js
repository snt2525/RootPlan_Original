$.ajaxSetup({
	contentType:'application/x-www-form-urlencoded;charset=UTF-8', 
	type:"post"
});
var ptFlag = 0;
var carFlag = 0;
for(var i = 0;i<10;i++){
	setTimeout(function() {
		alert(i);
		$.ajax({
			   type: "POST",
		       url:"/RootPlan/AddressDataServlet",
		       dataType: "html",
		       data:  $("#finish1").serialize(),  //0
		       success: function(data){
		    	   alert(data)
		    	   if(data == 1){
		    		  callResultPT();
		  	    	  alert("데이터 찾았다")
		  	    	  break;
		    	   }  	   
		       },error:function(data){alert(data)}
	   });	
	}, 1000);
}


for(var i = 0; i<10000000; i++){
	setTimeout(function(){
		if(ptFlag == 1 && carFlag  == 1){
			//다음페이지로 넘어간다.
			location.replace("LastPT.html");
			break;
		}
	}, 1000);	
}

function callResultPT(){	 //대중교통
	$.ajax({   //dfs, 결과 순서 다시 재 호출
		   type: "POST",
	       url:"/RootPlan/AddressDataServlet",
	       dataType: "html",
	       data:  $("#finishCallDFS1").serialize(), //0
	       success: function(){
	  	    	 ptFlag = 1;	    	      
	       }
	 });
}
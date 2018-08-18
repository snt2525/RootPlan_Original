$.ajaxSetup({
	contentType:'application/x-www-form-urlencoded;charset=UTF-8', 
	type:"post"
});
function callResultCar(){
	for(var i = 0 ;i<1000000;i++){
		setTimeout(function(){
			$.ajax({
			       url:"/RootPlan/AddressDataServlet",
			       dataType: "html",
			       data:  $("#finish2").serialize(),  //1
			       success: function(data){
			    	   alert(data);
			    	   if(data == 1){
			    		  callResultCar();
			  	    	  alert("데이터 찾았다")
			  	    	  break;
			    	   }  	   
			       },error:function(){alert(data)}
		   });
		}, 1000);	
	}
}

function callResultCar(){	 //자동차	
	$.ajax({   //dfs, 결과 순서 다시 재 호출
		   type: "POST",
	       url:"/RootPlan/AddressDataServlet",
	       dataType: "html",
	       data:  $("#finishCallDFS2").serialize(), //1
	       success: function(){
	    	   carFlag = 1;
	       }
	   });
}

function startWorker(){
	var w;
	if(window.Worker){
        w = new Worker("CallDfs.js");
        w.onmessage = function(event) {
            alert(event.data);
        };   
        callResultCar(); //자동차
	}
	else{
	    alert('Web worker를 지원하지 않는 브라우저 입니다!');
	}	
   w.terminate();
   w = undefined;	
}
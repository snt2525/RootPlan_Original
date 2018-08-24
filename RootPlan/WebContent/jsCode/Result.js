$.ajaxSetup({
	contentType:'application/x-www-form-urlencoded;charset=UTF-8', 
	type:"post"
});
var lineArray;


function showResultPT(){
	console.log("showResultPT 들어옴");
	$.ajax({   //pt list에 뿌려줌
		   type: "POST",
	       url:"/RootPlan/AddressDataServlet",
	       dataType: "xml",
	       data:  $("#showPT").serialize(),
	       success: function(data){
	    	   console.log("showResultPT 데이터 받아옴");
	    	   var htmlStr ="";
	    	   var totalDistance=0, totalTime=0, totalFare=0;
	    	   var now=1;
	    	   $(data).find("Data").each(function(){
	    		   console.log("pt Data 들어옴");
	    		   	htmlStr += "<div>";
	    		   	htmlStr += "<div>"+now+"에서 " +(now+1) +"까지 가는 정보입니다.</div>";
	    		   	if($(this).find('walk').text()=="true"){ // 도보
	    		   		htmlStr += "<div>도보 이용입니다.</div>";	
	    		   		totalTime += Number($(this).find('totalTime').text());
	    		   		totalFare += Number($(this).find('fare').text());
	    		   		htmlStr += "<div>구간 거리 : " + $(this).find('totalDistance').text() +"</div>";
	    		   		htmlStr += "<div>구간 시간 : " + $(this).find('totalTime').text() +"</div><hr>";
	    		   	}else{ // 도보 이용 아닌 경우 환승 정보 있으므로 
	    		   		htmlStr += "<div>첫번째 정류장 : "+$(this).find('firstStartStation').text() + "</div>";
	    		   		htmlStr += "<div>마지막 정류장 : " +$(this).find('lastEndStation').text() +"</div>";
	    		   		htmlStr += "<div>구간 거리 : " + $(this).find('totalDistance').text() +"</div>";
	    		   		htmlStr += "<div>구간 시간 : " + $(this).find('totalTime').text() +"</div>";
	    		   		htmlStr += "<div>구간 요금 : " + $(this).find('fare').text() + "</div><hr><hr>";
	    		   		totalDistance += Number($(this).find('totalDistance').text());
		   				totalTime += Number($(this).find('totalTime').text());
	    		   		totalFare += Number($(this).find('fare').text());
	    		   		var sectionSize = Number($(this).find('sectionSize').text());
    		   			var no = 1;
    		   			// 이게 될란지 모르겠네
    		   			$(data.find("section")).each(function(){ // 환승 정보 보여주기
    		   				console.log("pt Section 들어옴");
    		   				htmlStr += "<div>"; // 들여쓰기 나중에 css 할때 적용하기
    		   				htmlStr += "<div>"+no+"번째 교통정보입니다.</div>";
    		   				htmlStr += "<div>" + $(this).find('trafficType').text() +" : " + $(this).find('line').text() + "</div>"; // 지하철 몇호선 이용 | 버스 몇번 이용
    		   				htmlStr += "<div>시작 : " + $(this).find('startStation').text() + "</div>";
    		   				htmlStr += "<div>마지막 : " + $(this).find('endStation').text() + "</div>";
    		   				htmlStr += "<div>세부 거리 : " + $(this).find('sectionDistance').text() + "</div>";
    		   				htmlStr += "<div>세부 시간 : " + $(this).find('sectionTime').text() + "</div>";
    		   				htmlStr += "</div>";
    		   				if(no!=1) htmlStr += "<hr>"; 
    		   				no = no+1;
    		   				if(no==sectionSize) return false;
    		   			})
	    		   	}
	    		   	htmlStr += "</div>";
	    		   	now = now+1;
	    	   })
	    	   htmlStr += "<hr><div>총 거리 : " + totalDistance + ", 총 시간 : " +totalTime + ", 총 교통요금 : " + totalFare + "</div>";
	    	   console.log(htmlStr);
	    	   $("#resultPTList").html(htmlStr);
	       }, error:function(request,status,error){
	    	  console.log("대중교통 List 불러오기 실패");
	       }
	   });
}

function showResultCar(){
	console.log("showResultCar 들어옴");
	$.ajax({   //car list에 뿌려줌
		   type: "POST",
	       url:"/RootPlan/AddressDataServlet",
	       dataType: "xml",
	       data:  $("#showPT").serialize(),
	       success: function(data){
	    	   var htmlStr ="";
	    	   var totalDistance=0, totalTime=0, totalFare=0, now=1;
	    	   $(data).find("Data").each(function(){
	    		   	htmlStr += "<div>";
	    			htmlStr += "<div>"+now+"에서 " +(now+1) +"까지 가는 정보입니다.</div>";
	    		   	if($(this).find('walk').text()=="true"){
	    		   		htmlStr += "<div>도보 이용입니다.</div>";	    		   		
	    		   	}
	    		   	htmlStr += "<div>구간 거리 : " +  $(this).find('distance').text()+"</div>";
	    		   	totalDistance += Number($(this).find('distance').text());
	    		   	totalTime += Number($(this).find('time').text());
	    		   	htmlStr += "<div>구간 시간 : " + $(this).find('time').text() + "</div>";
	    		   	if($(this).find('walk').text()=="false"){
	    		   		totalFare += Number($(this).find('fare').text());
	    		   		htmlStr += "<div>택시요금 : " + $(this).find('fare').text() + "</div>";
	    		   	}
	    		   	htmlStr += "</div><hr>";
	    		   	now = now+1;
	    	   })
	    	   htmlStr += "<hr><div>총 거리 : " + totalDistance + ", 총 시간 : " +totalTime + ", 총 택시요금 : " + totalFare + "</div>";
	    	   $("#resultCarList").html(htmlStr);
	       }, error:function(request,status,error){
	    	  console.log("자동차 List 불러오기 실패");
	       }
	   });
}
function callPolyLine(){
	$.ajax({   //dfs, 결과 순서 다시 재 호출
		   type: "POST",
	       url:"/RootPlan/AddressDataServlet",
	       dataType: "html",
	       data:  $("#resultPoly").serialize(),
	       success: function(data){
	    	   lineArray = null;
	    	   lineArray = new Array();
	    	   $(data).find("Data").each(function(){
	    		   var Point = new naver.maps.Point($(this).find('lat').text(), $(this).find('lng').text());
	    		   lineArray.push(new naver.maps.LatLng(Point.y, Point.x));  //이상하면 x와 y를 바꿔보기.	    		 
	    	   })
	    	   var polyline = new naver.maps.Polyline({
				    map: map2,
				    path: lineArray,
				    strokeWeight: 3,
				    strokeColor: '#003499' //색상 바꾸기
				});
	    	   
	       }, error:function(request,status,error){
	    	   console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
	       }
	   });
}

function callResult(){	
	$.ajax({   //dfs, 결과 순서 다시 재 호출
		   type: "POST",
	       url:"/RootPlan/AddressDataServlet",
	       dataType: "html",
	       data:  $("#resultLatLng").serialize(),
	       success: function(data){
	    	   var i = 0;
				$(data).find("Data").each(function(){
					var tmp = i;
					var latlngPoint = new naver.maps.Point($(this).find('lat').text(),$(this).find('lng').text());
					var latlng = new naver.maps.LatLng(latlngPoint.y,latlngPoint.x);		
					var greenMarker = {
						    position: latlng,
						    map: map2,
						    icon: {
						    	 url:imgUrl[tmp],
					             size: new naver.maps.Size(38, 58),
					             anchor: new naver.maps.Point(19, 58),
						    }
						};
					marker[i] = new naver.maps.Marker(greenMarker);
					marker[i++].setMap(map2); // 추가	
					
				})
	    	  // callBack();	    	   
	       }, error:function(request,status,error){
	    	   console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
	       }
	   });
}

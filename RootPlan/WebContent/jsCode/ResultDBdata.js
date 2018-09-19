$.ajaxSetup({
	contentType:'application/x-www-form-urlencoded;charset=UTF-8', 
	type:"post"
});
var lineArray;

var imgIconUrl = [
	'img/way1.png',
	'img/way2.png',
	'img/way3.png',
	'img/way4.png',
	'img/way5.png',
	'img/way6.png',
	'img/bus.png', // 6 : 버스아이콘
	'img/subway.png' // 7 : 지하철 아이콘
]

function tabClick(title){
	if(title=="대중교통+도보"){ 
		document.resultPoly.how.value = "0";
		document.resultLatLng.how.value="0";
		callResult();
		callPolyLine(0);
		showResultPT();
	}else{
		document.resultPoly.how.value = "1";
		document.resultLatLng.how.value="1";
		callResult(); // 결과 부르기
		callPolyLine(1); // 그리기
		showResultCar(); // 옆에 결과 보여주기 
	}
}

function showResultPT(){
	$.ajax({   //pt list에 뿌려줌
		   type: "POST",
	       url:"/RootPlan/AddressDataServlet",
	       dataType: "xml",
	       data:  $("#showPT").serialize()+"&customerID="+customerID+"&flag=1",
	       success: function(data){
	    	   var htmlStr ="";
	    	   var totalDistance=0, totalTime=0, totalFare=0;
	    	   var now=-1;
	    	   var sectionSize =0, cnt=0, tmpId=0, cnt1=0, wayCount=0, count=0, cycle=0;
	    	   htmlStr += "<div>직선거리 800m 이하는 도보로 제공됩니다.</div>";
	    	   htmlStr += "<div>대중교통 이용시 걷는 시간 및 거리는 포함되지 않습니다.</div>";
	    	   htmlStr += "<div><img class='iconImg' src='img/bus.png'/> : 버스 | <img class='iconImg' src='img/subway.png'/> : 지하철 | <img class='iconImg' src='img/walk.png'/> : 도보</div><div><hr class='one'>";
	    	   $(data).find("Data").each(function(){
	    		   if($(this).find('check').text()=='-1'){ // 경로 총 몇개인지
	    			   wayCount =$(this).find('wayCount').text();
	    			   cycle = $(this).find('cycle').text();
	    		   }else if($(this).find('check').text()=='0'){
	    			 if(wayCount-1!=count){
	    				 htmlStr += "<img style='width:20px;margin-right:5px;' src=" + imgUrl[count] +" />" + $(this).find('title').text();
		    			 count = count+1;
	    				 htmlStr += "<img class='iconImg' src='img/arrow_right.png' />";
	    			 }
	    			 else{
	    				 if(cycle=='1')
	    					 htmlStr += "<img style='width:20px;margin-right:5px;' src=" + imgUrl[0] +" />" + $(this).find('title').text();
	    				 else 
	    					 htmlStr += "<img style='width:20px;margin-right:5px;' src=" + imgUrl[count] +" />" + $(this).find('title').text();
	    				 htmlStr += "</div>"; 
	    			 }
	    		   }else if($(this).find('check').text()=='1'){	   // 1번 지점
	    			   now = now+1;
	    			   cnt=0; 
	    			   cnt1=0;
	    			   // 엔터 하려면 나중에 보고나서 
	    			   htmlStr += "<div><hr class='one'>";
	    			   htmlStr += "<img class='iconImg' src='"+imgIconUrl[now] +"'/> 약 ";
	    			   if($(this).find('walk').text()=="true"){
	    				   htmlStr += (Number($(this).find('totalTime').text())/60).toFixed(0) +"분  |  요금 "; 
	    				   totalTime += Number($(this).find('totalTime').text())/60;
	    			   }
		   				else{
		   					htmlStr += Number($(this).find('totalTime').text()) +"분  |  요금 "; 
		   					totalTime += Number($(this).find('totalTime').text());
		   				}
	    			  
	    			   htmlStr += $(this).find('totalFare').text() + "원  | ";
	    			   htmlStr += (Number($(this).find('totalDistance').text()/1000)).toFixed(2) + "km | ";
	    			   htmlStr += $(this).find('totalStationCount').text() + "개 정류장 및 역";
	    			   sectionSize = Number($(this).find('sectionSize').text());
	    			   htmlStr += "</div><div class='iconImg'><hr class='two'>";
	    			   totalDistance += Number($(this) .find('totalDistance').text());
	    		   		totalFare += Number($(this).find('totalFare').text());
	    		   }else if($(this).find('check').text() == '2'){
	    			   cnt = cnt+1;
	    			   if($(this).find('trafficType').text()== "버스"){
	    				   if($(this).find('stationName').text()=="null"){
	    					   htmlStr += "해당 정보를 제공하지 않습니다.";
	    				   }else{
	    					   htmlStr += "<img class='iconImg' src='"+imgIconUrl[6] +"'/>"+$(this).find('bus').text();
		    				   htmlStr += "("+$(this).find('stationName').text()+")";   
	    				   }
	    			   }else{ // 지하철
	    				   htmlStr += "<img class='iconImg' src='"+imgIconUrl[7] +"'/>";
	    				   htmlStr += $(this).find('subwayLine').text()+"("+$(this).find('stationName').text()+")";
	    			   }
	    			   if(cnt!=sectionSize){ 
	    				   htmlStr += "<img class='iconImg' src='img/arrow_right.png'/>";
	    			   }
	    			   else{
	    				   tmpId = "sectionDetail"+now.toString();
	    				   htmlStr += "</div><div><a href=\"javascript:toggleLayer('"+tmpId+"');\">";
		    			   htmlStr += "<img src='img/arrow_down.png'/></a></div>"; // 아래로 내리는 화살표 
		    			   htmlStr += "<div id='"+tmpId+"' style='display:none'>";  
	    			   }
	    		   }else if($(this).find('check').text() == '3'){ // 섹션 정보 
	    			   cnt1=cnt1+1;
		    		   if($(this).find('walk').text()=="true"){ // 도보
		    			   htmlStr += "<div><img src='img/walk.png'/>도보 이용입니다.</div>";	
		    		   }else{ // 도보 이용 아닌 경우 환승 정보 있으므로 
    		   				htmlStr += "<div>";
    		   				// 몇번째인지 보여주기
    		   				htmlStr += "<div></div>"
    		   				htmlStr += "<div>" + $(this).find('trafficType').text() +" : " + $(this).find('line').text(); // 지하철 몇호선 이용 | 버스 몇번 이용
    		   				htmlStr += " | 거리 : " + (Number($(this).find('sectionDistance').text())/1000).toFixed(2) 
    		   							+ "km | 시간 : " + $(this).find('sectionTime').text() + "분</div>";
    		   				if($(this).find('startStation').text()=="null" &&  $(this).find('endStation').text()=="null" ){
    		   					htmlStr += "정보없음";
    		   				}else{
    		   					htmlStr += "<div>탑승 : " + $(this).find('startStation').text() 
    		   							+ " | 하차 : " + $(this).find('endStation').text() + "</div>";	
    		   				}
    		   				
    		   				htmlStr += "</div>";
    		   				if(cnt1==sectionSize) htmlStr +="</div>";
			    		   else htmlStr += "<img src='img/arrow_down1.png'/>";
		    		   	}
		    		   
	    		   }
	    	   })
	    	   // 여기있는 ht 두껍게 
	    	   htmlStr += "<div><hr class='lastHr'>총 거리 : " + (totalDistance/1000).toFixed(2) + "km | 총 시간 : " +(totalTime).toFixed(0) + "분 | 총 교통요금 : " + totalFare + "원</div><br><br>";
	    	   $("#resultPTList").html(htmlStr);
	       }, error:function(request,status,error){
	    	  console.log("대중교통 List 불러오기 실패");
	       }
	   });
}

function showResultCar(){
	$.ajax({   //car list에 뿌려줌
		   type: "POST",
	       url:"/RootPlan/AddressDataServlet",
	       dataType: "xml",
	       data:  $("#showCar").serialize()+"&customerID="+customerID+"&flag=1",
	       success: function(data){
	    	   var htmlStr ="";
	    	   var totalDistance=0, totalTime=0, totalFare=0, now=0, wayCount=0, count=0, cycle=0;
	    	   htmlStr += "<div>직선거리 800m 이하는 도보로 제공됩니다.</div>";
	    	   htmlStr += "<div><img class='iconImg' src='img/car.png'/> : 자동차 | <img class='iconImg' src='img/walk.png'/> : 도보</div><div><hr class='one'>";
	    	   $(data).find("Data").each(function(){
	    		   if($(this).find('check').text()=='-1'){
	    			   wayCount = $(this).find('wayCount').text();
	    			   cycle = $(this).find('cycle').text();
	    		   }else if($(this).find('check').text()=='0'){
		    			if(wayCount-1!=count){
		    				htmlStr += "<img  style='width:20px;margin-right:5px;' src=" + imgUrl[count] +" />" + $(this).find('title').text();
			    			count = count+1;
		    				htmlStr += "<img class='iconImg' src='img/arrow_right.png' />";
		    			}
		    			else{
		    				if(cycle=='1')
		    					htmlStr += "<img  style='width:20px;margin-right:5px;' src=" + imgUrl[0] +" />" + $(this).find('title').text();
		    				else
		    					htmlStr += "<img  style='width:20px;margin-right:5px;' src=" + imgUrl[count] +" />" + $(this).find('title').text();
		    				htmlStr += "</div>";
		    			}
	    		   }else{
	    			   htmlStr += "<hr class='one'><div>";
	    			   htmlStr += "<img class='iconImg' src='"+imgIconUrl[now] +"'/> 약 ";
	    			   htmlStr += (Number($(this).find('time').text())/60).toFixed(0).toString() +"분 | ";
	    			   if($(this).find('walk').text()=="false"){
	    				   totalFare += Number($(this).find('fare').text());
	    				   htmlStr += "택시요금 " + $(this).find('fare').text() + "원  | ";
	    			   }else{
	    				   htmlStr += "<img src='img/walk.png'/>도보 이용 | ";	
	    			   }
	    			   htmlStr += "거리 : " + (Number($(this).find('distance').text())/1000).toFixed(2) + "km";
	    			   htmlStr += "</div>";
	    			
		    		   	totalDistance += Number($(this).find('distance').text());
		    		   	totalTime += Number($(this).find('time').text())/60;
		    		   	now = now+1;
	    		   }
	    		   
	    	   })
	    	   htmlStr += "<hr class='lastHr'><div>총 거리 : " + (Number(totalDistance)/1000).toFixed(2) + "km | 총 시간 : " 
	    	   			+totalTime.toFixed(0) + "분 | 총 택시요금 : " + totalFare + "원</div>";
	    	   //console.log(htmlStr);
	    	   $("#resultCarList").html(htmlStr);
	       }, error:function(request,status,error){
	    	  console.log("자동차 List 불러오기 실패");
	       }
	   });
}

var polyline = new naver.maps.Polyline({
    map: map2,
    path: lineArray,
    strokeWeight: 3,
    strokeColor: '#003499' //처음 대중교통 색상 맞추기
});

function callPolyLine(title){ // 0:pt, 1:car
	$.ajax({   //dfs, 결과 순서 다시 재 호출
		   type: "POST",
	       url:"/RootPlan/AddressDataServlet",
	       dataType: "html",
	       data:  $("#resultPoly").serialize()+"&customerID="+customerID+"&flag=1",
	       success: function(data){
	    	   lineArray = null;
	    	   lineArray = new Array();
	    	   
	    	   polyline.setMap(null);
	    	   delete polyline;
	    	   
	    	   $(data).find("Data").each(function(){
	    		   var Point = new naver.maps.Point($(this).find('lat').text(), $(this).find('lng').text());
	    		   lineArray.push(new naver.maps.LatLng(Point.y, Point.x));  //이상하면 x와 y를 바꿔보기.
	    	   })
	    	   
	    	   if(title==0){ // 대중교통일때
	    	      polyline = new naver.maps.Polyline({
					    map: map2,
					    path: lineArray,
					    strokeWeight: 3,
					    strokeColor: '#003499' 
					});   
	    	   }else{ // 자동차일때
	    		   polyline = new naver.maps.Polyline({
					    map: map2,
					    path: lineArray,
					    strokeWeight: 3,
					    strokeColor: '#ff0000'
					});    
	    	   }
	       }, error:function(request,status,error){
	    	   console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
	       }
	   });
}

var line = new Array(10000);

function callResult(){	
	$.ajax({   //dfs, 결과 순서 다시 재 호출
		   type: "POST",
	       url:"/RootPlan/AddressDataServlet",
	       dataType: "html",
	       data:  $("#resultLatLng").serialize()+"&customerID="+customerID+"&flag=1",
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
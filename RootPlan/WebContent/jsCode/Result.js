$.ajaxSetup({
	contentType:'application/x-www-form-urlencoded;charset=UTF-8', 
	type:"post"
});
var lineArray;
		
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

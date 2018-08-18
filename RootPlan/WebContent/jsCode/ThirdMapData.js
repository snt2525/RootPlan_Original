$.ajaxSetup({
	contentType:'application/x-www-form-urlencoded;charset=UTF-8', 
	type:"post"
});

function isChecked_S(index){ //0:START,1:FINAL , i: 넘버
	//marker을 다시 전부 초기화 한다.
	cleanMap();
	$.ajax({
		url:"/RootPlan/AddressDataServlet",
		dataType: "xml",
		data: "menuIndex=4",
		success: function(data){
			var i = 0;
			$(data).find("LatLng").each(function(){
				var tmp = i;
				var latlngPoint = new naver.maps.Point($(this).find('lat').text(),$(this).find('lng').text());
				var latlng = new naver.maps.LatLng(latlngPoint.y,latlngPoint.x);	
				
				if(i == parseInt(index)){
					tmp = 10;		
				}
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
		}, error: function(data){
				alert("실패");
		}
	});
	$("#index").val(index);	
	saveIndex();
	//서버에 데이터를 넘겨준다.
}

function saveIndex(){  //start, last 데이터를 저장해 둔다.
	$.ajax({
		url:"/RootPlan/AddressDataServlet",
		dataType: "html",
		data: $("#setIndexData").serialize(),
		success: function(data){
		}
	});
}

function cleanMap(){ //마커를 초기화
	for(var i = 0;i< cnt;i++){
		marker[i].setMap(null); 
	}	
	marker = new Array(10);
}

function cleanSaveData(){
	$("#index").val("-1");
	saveIndex();
}

function possibleNext(){
	if(document.setIndexData.index.value == "-1"){
		$('#nextBtn').attr({'href':'#'});
		alert("시작 위치를 선정해주세요")
	}else{
		$('#nextBtn').attr({'href':'Fourth.html'});
	}
}


//모든 데이터 호출 
function getDataThird(){
	$.ajax({
		url:"/RootPlan/AddressDataServlet",
		dataType: "xml",
		data: "menuIndex=5",
		success: function(data){
			var htmlStr = "";
			$(data).find("Address").each(function(){
				htmlStr += "<div>";
				htmlStr += "<div class='left-content'>";
				htmlStr += "<input type='radio' name='startPosition' onClick='isChecked_S("
					+ $(this).find("num").text()+");' value='"
					+ $(this).find("num").text() + "'/> </div>";
				htmlStr += "<div class='middle-content'> ";
				htmlStr +="&nbsp;" + $(this).find('no').text() + ". " + $(this).find('data').text();		
				htmlStr += "</div></div>";
				//마크 표시
				var latlngPoint = new naver.maps.Point($(this).find('lat').text(),$(this).find('lng').text());
				var latlng = new naver.maps.LatLng(latlngPoint.y,latlngPoint.x);
				var greenMarker = {
				    position: latlng,
				    map: map2,
				    icon: {
				    	 url:imgUrl[cnt],
			             size: new naver.maps.Size(38, 58),
			             anchor: new naver.maps.Point(19, 58),
				    }
				};
				marker[cnt] = new naver.maps.Marker(greenMarker);
				marker[cnt++].setMap(map2); // 추가	
			})
			htmlStr += "</table>";	
			$("#list2").html(htmlStr);	
		}, error: function(data){
				alert("실패");
		}
	});
}


function startWorker(){
	var w;
	if(window.Worker){
        w = new Worker("RouteApi.js");
        w.onmessage = function(event) {
            alert(event.data);
        };   
        getDataThird(); //초기화
	}
	else{
	    alert('Web worker를 지원하지 않는 브라우저 입니다!');
	}	
   w.terminate();
   w = undefined;	
}

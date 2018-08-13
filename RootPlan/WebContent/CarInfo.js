var arr = new Array();
var idx=0;

//버튼을 누르면 getCarData() 호출
$("#addressList2").on("click", '#ByCar' , function(){
	//클릭하면 서블렛에서 linkedlist를 java로 넘기기
	
})

function getCarData(){
	$.ajax({
		   type: "POST",
	       url:"/RootPlan/AddressDataServlet",
	       dataType: "xml",
	       async:false,
	       data: $("#addressList2").serialize(),
	       success: function(data){
	          // 만든 데이터 배열로 만들어서 
	    	   $(data).find("PairData").each(function(){
	    		   var sn = $(this).find('startNo').text();
	    		   var en = $(this).find('endNo').text();
	    		   var sx = $(this).find('startLat').text(); // 출발지 위도
	    		   var sy = $(this).find('startLng').text(); // 출발지 경도
	    		   var ex = $(this).find('endLat').text();
	    		   var ey = $(this).find('endLng').text();

	    		   inputDataServer(sn, en, sx, sy, ex, ey);
	    		   idx = idx+1;
	    	   })
	    	   
	       }, error:function(request,status,error){
	    	   console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
	       }
	   });
}
function inputDataServer(sn, en, sx, sy, ex, ey){
	var headers = {}; 
	headers["appKey"]="9974a775-4c3d-48f1-8df7-650b3f2debfc";//실행을 위한 키 입니다. 발급받으신 AppKey를 입력하세요.
	headers["content-type"]="application/x-www-form-urlencoded";
	
	$.ajax({ 
		method:"POST",
		headers : headers,
		url:"https://api2.sktelecom.com/tmap/routes?version=1&format=xml",//자동차 경로안내 api 요청 url입니다.
		async:false,
		//data: json,
		data:{
			//출발지 위경도 좌표입니다.
			startX : sx,
			startY : sy,
			//목적지 위경도 좌표입니다.
			endX : ex,
			endY : ey,
			//출발지, 경유지, 목적지 좌표계 유형을 지정합니다.
			reqCoordType : "WGS84GEO",
			//resCoordType : "EPSG3857",
			resCoordType : "WGS84GEO",
			//각도입니다.
			//angle : "172",
			//경로 탐색 옵션 입니다.
			searchOption : 0
		},
		//데이터 로드가 성공적으로 완료되었을 때 발생하는 함수입니다.
		success:function(response){
			prtcl = response;
			
			// 결과 출력
			var prtclString = new XMLSerializer().serializeToString(prtcl);//xml to String	
		    xmlDoc = $.parseXML( prtclString ),
		    $xml = $( xmlDoc ),
	    	$intRate = $xml.find("Document");

		    arr[idx] = new Array();
		    arr[idx][0] = sn; // 시작 번호
		    arr[idx][1] = en; // 끝 번호
		    arr[idx][2] = ($intRate[0].getElementsByTagName("tmap:totalDistance")[0].childNodes[0].nodeValue/1000).toFixed(1); // km
		    arr[idx][3] = ($intRate[0].getElementsByTagName("tmap:totalTime")[0].childNodes[0].nodeValue/60).toFixed(0); // 분	
	    	arr[idx][4] = $intRate[0].getElementsByTagName("tmap:totalFare")[0].childNodes[0].nodeValue; // 원	
	    	arr[idx][5] = $intRate[0].getElementsByTagName("tmap:taxiFare")[0].childNodes[0].nodeValue; // 원
	    	
	    	
		},
		//요청 실패시 콘솔창에서 에러 내용을 확인할 수 있습니다.
		error:function(request,status,error){
			console.log("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
} 
	
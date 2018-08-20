var marker = new Array(10);
var cnt = 0;
var flag = 0;
var imgUrl = [
	'img/mark1.png',
	'img/mark2.png',
	'img/mark3.png',
	'img/mark4.png',
	'img/mark5.png',
	'img/mark6.png',
	'img/mark7.png',
	'img/mark8.png',
	'img/mark9.png',
	'img/mark10.png'
]

$.ajaxSetup({
	contentType:'application/x-www-form-urlencoded;charset=UTF-8', 
	type:"post"
});

$("form").on("click", '#btn' , function(){
	if(flag == 1){
		$.ajax({
			url:"/RootPlan/AddressDataServlet",
			dataType: "html",
			data: $("#saveAddress").serialize(),
			success: function(data){
				infoWindow.close();
				document.saveAddress.lat.value = "";
				document.saveAddress.lng.value = "";
				document.saveAddress.address.value = "";	
				if(data == 7){
					alert("목적지를 7개 이상 선택하실 수 없습니다.")
				}else{										
					getData(); 	
					flag = 0;
				}
			}, 		
		    error: function (data) {alert("목적지를 지정해 주세요")}				
		});
	}else
		alert("목적지를 지정해 주세요")
})


//모든 데이터 호출 
function getData(){
	clean();
	getDataSize(1);
	$.ajax({
		url:"/RootPlan/AddressDataServlet",
		dataType: "xml",
		data: "menuIndex=5",
		success: function(data){
			var htmlStr = "";
			if(data!= null){
				$("#resetBtn").attr("type","button");  //초기화 버튼 활성화
			}
			else{
				$("#resetBtn").attr("type","hidden");  // 값이 없으면 버튼 감추기
			}
			
			$(data).find("Address").each(function(){
				htmlStr += "<div>";
				htmlStr += "<div class='left-content'>" + $(this).find('no').text()+". " + "</div>";
				htmlStr +=  "<div class='middle-content'>" + $(this).find('data').text() + "</div>";
				htmlStr += "<div class='right-content'><a href='#' class='delete-factor' id='del', name='"+ $(this).find("no").text() +"'>X</a></div>";
				htmlStr += "</div>";
				
				//마크 표시
				var latlngPoint = new naver.maps.Point($(this).find('lat').text(),$(this).find('lng').text());
				
				var latlng = new naver.maps.LatLng(latlngPoint.y,latlngPoint.x);
				var greenMarker = {
				    position: latlng,
				    map: map,
				    icon: {
				    	 url: imgUrl[cnt],
			             size: new naver.maps.Size(38, 58),
			             anchor: new naver.maps.Point(19, 58),
				    }
				};
				marker[cnt] = new naver.maps.Marker(greenMarker);
				marker[cnt++].setMap(map); // 추가	
			})
			$("#list").html(htmlStr);	
		}, error: function(data){
				alert("실패");
		}
	});
}

$("#addressBasket").on("click", '#resetBtn' , function(){
	if(confirm("데이터를 초기화 하시겠습니까?") == true){
		$.ajax({
			url:"/RootPlan/AddressDataServlet",
			dataType: "html",
			data: "menuIndex=7",
			success: function(data){
				if(data>0){
					clean();
					var htmlStr = "";
					$("#resetBtn").attr("type","hidden");  //초기화 버튼 hidden
					$("#list").html(htmlStr); 
					getDataSize(1);
				}
			}
		});	
	}
})

function clean(){
	for(var i = 0;i< cnt;i++){
		marker[i].setMap(null); 
	}	
	marker = new Array(10);
	cnt = 0;	
	//api호출 허가
	$.ajax({
		url:"/RootPlan/AddressDataServlet",
		data: "menuIndex=12",
		type: "post"
	});	
}


//삭제 모듈
$("#list").on("click", '#del' , function(){
	if(confirm("삭제하시겠습니까?") == true){
		document.deleteAddress.deleteIndex.value = $(this).attr("name");
		$.ajax({
			url:"/RootPlan/AddressDataServlet",
			dataType: "text",
			data: $("#deleteAddress").serialize(),
			success:function(data){				
				if(data>0){
					//clean(); 
					getData();
				}else{
					alert("삭제를 실패하였습니다.");
				}			
			},
			error: function(data){alert(data)}			
		});
	}
})
	

function getDataSize(num){	
	$.ajax({
		url:"/RootPlan/AddressDataServlet",
		dataType: "text",
		data: "menuIndex=8",
		success:function(data){				
			if(data<3){
				if(num==0) 
					alert("목적지를 3개 이상 선택해 주세요.");
				$('#nextBtn1').attr({'href':'#'});
			}else{
				$('#nextBtn1').attr({'href':'Third.html'});
			}
		},
		error: function(data){alert(data)}			
	});	
}
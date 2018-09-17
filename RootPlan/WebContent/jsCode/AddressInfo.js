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

// 즐겨찾기 버튼 누르면 리스트 보여주는 함수로 가기
$(function(){
   $('#btnSaveList').click(function(){
	   // 즐겨찾기 버튼 감추고 새로운 버튼 보이게 하기 
	   document.getElementById("btnMainList").style.display="block";
	   document.getElementById("btnSaveList").style.display="none";
	   //document.getElementById("headlineID").style.display="block";
       showSaveList();
       alert("showSaveList");
   });
   $('#btnMainList').click(function(){
	   // 즐겨찾기 버튼 감추고 새로운 버튼 보이게 하기
	   document.getElementById("btnMainList").style.display="none";
	   document.getElementById("btnSaveList").style.display="block";
	  // document.getElementById("headlineID").style.display="none";
	   getData(); // main으로 돌아가면 모든 리스트 다시 보여주기
	   alert("main으로");
   });
 }); 

function showSaveList(){
	// addressServlet 6번으로 가기
	$.ajax({
		url:"/RootPlan/AddressDataServlet",
		dataType: "text", // 나중에 xml로 바꿔야함 
		data: "menuIndex=6&customerID="+customerID,
		success: function(data){
			// 모든 데이터 받아와서 매핑해야서 보여줘야함
			$("#resetBtn").attr("type","hidden");
			var htmlStr = "<h4>저장한 모든 데이터 보여주기</h4>";
			$("#list").html(htmlStr);	
		}, 		
	    error: function (data) {
	    	console.log("저장한 리스트 가져오기 실패");
    	}				
	});
}

//$("infoBtn").on("click", '#btn' , function(){
function clickADDBtn(){
	if(flag == 1){
		$.ajax({
			url:"/RootPlan/AddressDataServlet",
			dataType: "html",
			data: $("#saveAddress").serialize()+"&customerID="+customerID,
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
		alert("목적지를 지정해 주세요");
}


//모든 데이터 호출 
function getData(){
	clean();
	getDataSize(1);
	$.ajax({
		url:"/RootPlan/AddressDataServlet",
		dataType: "xml",
		data: $("#getAddressData").serialize()+"&customerID="+customerID,
		success: function(data){
			var htmlStr = "<h3 class='headline'>여행지 선택</h3>";
			if(data!= null){
				$("#resetBtn").attr("type","button");  //초기화 버튼 활성화
			}
			else{
				$("#resetBtn").attr("type","hidden");  // 값이 없으면 버튼 감추기
			}
			
			$(data).find("Address").each(function(){
				htmlStr += "<div>";
				htmlStr += "<div class='left-content'>" + $(this).find('no').text()+". </div>";
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
				console.log("실패");
		}
		
	});
}

$("#addressBasket").on("click", '#resetBtn' , function(){
	if(confirm("데이터를 초기화 하시겠습니까?") == true){
		$.ajax({
			url:"/RootPlan/AddressDataServlet",
			dataType: "html",
			data: $("#initData").serialize()+"&customerID="+customerID,
			success: function(data){
				if(data>0){
					clean();
					var htmlStr = "<h3 class='headline'>여행지 선택</h3>";
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
}


//삭제 모듈
$("#list").on("click", '#del' , function(){
	if(confirm("삭제하시겠습니까?") == true){
		document.deleteAddress.deleteIndex.value = $(this).attr("name");
		$.ajax({
			url:"/RootPlan/AddressDataServlet",
			dataType: "text",
			data: $("#deleteAddress").serialize()+"&customerID="+customerID,
			success:function(data){				
				if(data>0){
					//clean(); 
					getData();
				}else{
					alert("삭제를 실패하였습니다.");
				}			
			},
			error: function(data){
				console.log(data);
			}
		});
	}
})
	

function getDataSize(num){	
	$.ajax({
		url:"/RootPlan/AddressDataServlet",
		dataType: "text",
		data: $("#getSize").serialize()+"&customerID="+customerID,
		success:function(data){				
			if(data<3){
				if(num==0) 
					alert("목적지를 3개 이상 선택해 주세요.");
				$('#nextBtn1').attr({'href':'#'});
			}else{
				$('#nextBtn1').attr({'href':'Third.html'});
			}
		},
		error: function(data){
			console.log(data);
		}			
	});	
}

//여행지 데이터 배열
var title = new Array(100);
var LocationImg = new Array(100);
var address = new Array(100);
var roadaddress = new Array(100);
var tp = new Array(100);
var category = new Array(100);
var link = new Array(100);
var link2 = new Array(100);
var description = new Array(100);

$.ajaxSetup({
	contentType:'application/x-www-form-urlencoded;charset=UTF-8', 
	type:"post"
});


var isStarted = 0;
function getCrawlingData(){
   $.ajax({
       url:"/RootPlan/AddressDataServlet",
       dataType: "xml",
       data: $("#SiData").serialize(),
       success: function(data){
          var htmlStr = "";
          htmlStr += "<div id='box'>";
          $(data).find("CityData").each(function(){
             htmlStr += "<h2 class='h2-style'>"+ $(this).find('LocationCity').text()+ " 대표 여행지" +"</h2>";    
          })
          $(data).find("Data").each(function(){	  
             htmlStr += "<div class='item1'>";
             htmlStr += "<div class='wow fadeInLeft' data-wow-delay='0.2s'>";
             htmlStr += "<img class='image_container' src='"
                + $(this).find('LocationImage').text()+"'></div>";   
             htmlStr += "<div class='service-desc'>";
             htmlStr += "<h5 class='service-h5'>"+$(this).find('LocationTitle').text()+"</h5>";
             htmlStr += "<input type='button' value ='위치 보기' name='"+$(this).find('no').text()+"' id='"+$(this).find('no').text()+"' " +
                   " class='btn btn-skin' onClick ='showAddressData("
                +$(this).find('LocationMapx').text()+","+$(this).find('LocationMapy').text()+","+$(this).find('no').text()+");'>";
             htmlStr += "<input type='hidden' id='"+$(this).find('no').text()+"' name='"+$(this).find('no').text()+"' value='"+$(this).find('LocationTitle').text()+"'/>";
             htmlStr += "</div></div>";
             
             var num = parseInt($(this).find('no').text());
             LocationImg[num] =  $(this).find('LocationImage').text();
             link[num] = $(this).find('LocationLink').text();
             if( link[num] != "" )
            	 link2[num] =  $(this).find('LocationLink').text()+" </a></br>";
             else
            	 link2[num] = "";
             title[num] = $(this).find('LocationTitle').text();
             description[num] = $(this).find('LocationDescription').text();
             if( description[num] != "" )
            	 description[num] += "</br>";                       
             if($(this).find('LocationRoadaddress').text() == "")
            	 roadaddress[num] = $(this).find('LocationRoadaddress').text();
             else
            	 roadaddress[num] = $(this).find('LocationRoadaddress').text() +"</br>";
             title[num] = $(this).find('LocationTitle').text();
             category[num] = $(this).find('LocationCategory').text();
             tp[num] = $(this).find('LocationTP').text();
             if( tp[num] != "" )
            	 tp[num] += " | ";
             address[num] = $(this).find('LocationAddress').text() +"</br>";
          });
          htmlStr += "</div>";
          
          $("#crawlingData").html(htmlStr);
       }, error: function(data){
            // alert("실패");
    	   console.log("이전 데이터와 같습니다.");
    }
   });
}

// 크롤링에서 어느 한 데이터 클릭했을 때
function showAddressData(xData,yData,no){  //나중에 marker가 안나온다면 latlngTmp.x와 y를 바꿔보자.'
	flag = 1;
   var HOME_PATH = window.HOME_PATH || '.';
   var tm128 =  new naver.maps.Point(parseInt(xData),parseInt(yData));
    naver.maps.Service.reverseGeocode({
           location: tm128,
           coordType: naver.maps.Service.CoordType.TM128
       }, function(status, response) {
           if (status === naver.maps.Service.Status.ERROR) {
               return alert('Something Wrong!');
           }
           
		    infoWindow.setContent([
		             '<div style="position:relative;padding:20px;width:280px;height:70px;font-color:black">', 	
		             '<h6 style="font-weight:bold; color:black; float:left;">' + title[no] +'</h6>',
		             '<input type="button" name="btn" style="float:right;" value="담기" onClick="clickADDBtn();"/></br>',
		             '<p style="color:black;">' + address[no] + roadaddress[no],
		               tp[no] + category[no] +'</p>',				                      
		            // '<img src="'+ LocationImg[no] +'" width="250px" height="180px"/> <br />',		             			           
		              '<p>' + description[no],		   
		              '<a href="'+ link[no] +'" style="color:blue;text-decoration:none;"  target="_blank">'+ link2[no],
		             '</br> </p>',
					 '</div>'
		        ].join('\n'));
		
		    var latlngTmp = new naver.maps.TransCoord.fromTM128ToLatLng(tm128);
		    map.setCenter(latlngTmp);
		     infoWindow.open(map, latlngTmp);   
		     
		     //form에 위도 경도를 저장해둔다.
		    document.saveAddress.lat.value = latlngTmp.x;  
		    document.saveAddress.lng.value = latlngTmp.y;
		    document.saveAddress.address.value = title[no];
		    document.saveAddress.si.value =  document.SiData.Si.value;
		 });
}

function init(num){
	title2 = "";
	LocationImg2 = "";
	address2 = "";
	tp2 = "";
	category2 = "";
    link3 = "";
    link4 = "";
    description2 = "";  
    document.searchApi.num.value = num;
}

function callSearchApi(num){	
		//초기화
		init(num);   
       if(num == 0){
       	document.searchApi.findLocation.value = document.form.jibunAddr.value;
       	document.searchApi.address.value = document.form.roadAddrPart1.value;
       	roadAddress2 = document.form.roadAddrPart1.value + "</br>";	
       }
      
		if(document.searchApi.findLocation.value == ""){
		   makeInfo();
		}else{	    
			$.ajax({
				url:"/RootPlan/CallSearchLocalApi",
				type : "post",
				dataType: "xml",
				data: $("#searchApi").serialize(),
				success: function(data){					
					$(data).find("ResultData").each(function(){	
						if($(this).find('title').text() == "l.l"){
							roadAddress2 = "<h5>"+roadAddress2+"</h5>";
						}else{	
							title2 = $(this).find('title').text();		
							document.saveAddress.address.value = title2;
							address2 = $(this).find('address').text();
							tp2 = $(this).find('telephone').text();
							category2 = $(this).find('category').text();
							link3 = $(this).find('link').text();
							description2 = $(this).find('description').text();	
							//주소
							if(address2 != "")
								address2 += "</br>";
							//전화번호
							if( tp2 != "" )
				            	tp2 += " | ";		
							//링크
							if( link3 != "" )
				            	link4 =  $(this).find('link').text()+" </a></br>";
				            else
				            	link4 = "";					 
							//설명
							if(description2 != "" )
				            	description2 += "</br>"		
						}
					})								
					 makeInfo();
						
				}, error: function(data){
						alert("실패");
				}
			});
		}
	}
function makeInfo(flag){
	infoWindow.setContent([ 
	    '<div style="position:relative;padding:20px;width:300px;height:50px;font-color:black">',
	    '<h6 style="font-weight:bold; color:black; float:left;">' + title2 +'</h6>',
	    '<input type="button" name="btn" style="float:right;" value="담기" onClick="clickADDBtn();"/></br>',
	    '<p style="color:black;">' + address2 + roadAddress2,
        tp2 + category2 +'</p>',				                                 			           
       '<p>' + description2,		   
       '<a href="'+ link3 +'" style="color:blue;text-decoration:none;"  target="_blank">'+ link4,
       '</br> </p>',
		'</div>'
	].join('\n'));

	map.setCenter(latlngTmp);
	infoWindow.open(map, latlngTmp);
}

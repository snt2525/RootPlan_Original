var title = new Array(100);

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
             
             title[parseInt($(this).find('no').text())] = $(this).find('LocationTitle').text();
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
   var tm128 =  new naver.maps.Point(parseInt(xData),parseInt(yData));
    naver.maps.Service.reverseGeocode({
           location: tm128,
           coordType: naver.maps.Service.CoordType.TM128
       }, function(status, response) {
           if (status === naver.maps.Service.Status.ERROR) {
               return alert('Something Wrong!');
           }
           
		    infoWindow.setContent([
		            '<div style="padding:10px;min-width:200px;line-height:150%;">'+ title[no]
		             +'<a href="#" id="btn" name="btn">' +
					'<img src="img/add_color.png"/>'
					 + '</a></div>'
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

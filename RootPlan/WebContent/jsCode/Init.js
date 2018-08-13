//3번째 4번째 페이지 초기화 
var map2 = new naver.maps.Map("map2", {
           center: new naver.maps.LatLng(37.3595316, 127.1052133),
           zoom: 10,
           size : new  naver.maps.Size($(window).width() - $("#aside").width(), $(window).height()), 
           mapTypeControl: true,
          
       });
var marker = new Array(10);
var cnt = 0;
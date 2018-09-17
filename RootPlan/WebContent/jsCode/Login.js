// 모든 페이지에서 처음에 접근하는 페이지
$.ajaxSetup({
   contentType:'application/x-www-form-urlencoded;charset=UTF-8', 
   type:"post"
});

var customerID = sessionStorage.getItem("customerID");
console.log("customerID : " + customerID);
function sessionCheck(i){
	// 만약 로그인 안되어있으면 로그인 페이지로 무조건 가기
	if(sessionStorage.getItem('id')==null){
		alert("로그인이 필요합니다. 로그인 페이지로 이동합니다.");
		location.href="indexLogin.html";
		return 0;
	}else{
		console.log("emaipl : " + sessionStorage.getItem("email"));
		console.log("id : " + sessionStorage.getItem("id"));
		console.log("gender : " + sessionStorage.getItem("gender"));
		console.log("age : " + sessionStorage.getItem("age"));

		// index이면 server로 보내기
		if(i == 0){ 
			sendCustomerInfo();
		}
		
		// customerID 계속 들고다니기
		customerID = sessionStorage.getItem("customerID"); // 이걸 모든 폼에 추가로 전송하기
		return 1;
	}	
}

function sendCustomerInfo(){
	document.loginData.email.value = sessionStorage.getItem("email");
	document.loginData.cID.value = sessionStorage.getItem("id");
	document.loginData.gender.value = sessionStorage.getItem("gender");
	document.loginData.age.value = sessionStorage.getItem("age");
	$.ajax({
		url:"/RootPlan/LoginServlet",
		dataType: "text",
		data: $("#loginData").serialize(),
		success: function(data){
			console.log("customerID입력됨 " );
			sessionStorage.setItem("customerID", data); // customerID 입력
		},error:function(data){
			console.log("customerID 값 받아오기 실패"); 
		}
	});
}


//브라우저 창 그냥 닫아버릴때 이벤트
var flag=false;
window.onbeforeunload = function(){
	if(!flag){
		alert("bye!!브라우저 닫히고 세션 죽인다");
		killSession();
	}
}

function killSession(){ 
	// 로그아웃 시 and 브라우저 창 그냥 닫을때 발생 
	alert("killSession");
	//sessionStorage.clear();
	// ajax로 customerID 넘겨서 해당 번호 false로 처리하기
	$.ajax({
		url:"/RootPlan/LoginServlet", // 어디 서블렛으로 가야하지..?
		dataType: "text",
		data: "menuIndex=1&customerID="+customerID // 나중에 번호 바꿔야함 
	});
}

		
		

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
	document.loginData.customerID.value = sessionStorage.getItem("id");
	document.loginData.email.gender = sessionStorage.getItem("gender");
	document.loginData.email.age = sessionStorage.getItem("age");
	$.ajax({
		url:"/RootPlan/LoginServlet",
		dataType: "html",
		data: $("#loginData").serialize(),
		success: function(data){
			sessionStorage.setItem("customerID", data); // customerID 입력
		},error:function(data){
			console.log("customerID 값 받아오기 실패"); 
		}
	});
}



		
		


function sessionCheck(i){
	// 모든 페이지에서 접근해야함, 만약 로그인 안되어있으면 로그인 페이지로 무조건 가기
	if(sessionStorage.getItem('id')==null){
		alert("로그인이 필요합니다. 로그인 페이지로 이동합니다.");
		location.href="indexLogin.html";
		return 0;
	}else{
		console.log("emaipl : " + sessionStorage.getItem("email"));
		console.log("id : " + sessionStorage.getItem("id"));
		console.log("gender : " + sessionStorage.getItem("gender"));
		console.log("age : " + sessionStorage.getItem("age"));
		console.log("birthday : " + sessionStorage.getItem("birthday"));	
		
	/*	if(i == 0){
			$.ajax({
				url:"/RootPlan/LoginServlet",
				dataType: "html",
				data: $("#loginData").serialize(),
				success: function(data){
					
				}
			});
		}*/
		return 1;
	}	
}


		
		

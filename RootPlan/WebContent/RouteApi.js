function start(){
	$.ajax({
		contentType:'application/x-www-form-urlencoded;charset=UTF-8', 
		url:"/RootPlan/AddressDataServlet",
		dataType: "text",
		data: "menuIndex=8",
		success:function(data){	
			var size = data;
			if(size>4){ // 대중교통 반으로 나눠서 돌리기
				document.apiAB.a.value = "0";
				document.apiAB.b.value = "4";
				document.apiAB.carBlock.value = "0";
				$.ajax({
					url:"/RootPlan/AddressDataServlet",
					data: $("#apiAB").serialize(),
					type:"get"
				});	
				document.apiAB.a.value = "4";
				document.apiAB.b.value = String(size);
				document.apiAB.carBlock.value = "1";
				$.ajax({
					url:"/RootPlan/AddressDataServlet",
					data: $("#apiAB").serialize(),
					type:"get"
				});	
			}else{ // 자동차 
				document.apiAB.a.value = "0";
				document.apiAB.b.value = String(size);
				alert(document.apiAB.b.value);
				document.apiAB.carBlock.value = "0";
				$.ajax({
					url:"/RootPlan/AddressDataServlet",
					data: $("#apiAB").serialize(),
					type:"get"
				});
			}	
		},
		error: function(data){alert(data)}			
	});	
}


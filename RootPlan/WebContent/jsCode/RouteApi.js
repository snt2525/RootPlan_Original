$.ajax({
	url:"/RootPlan/AddressDataServlet",
	dataType: "text",
	data: "menuIndex=8",
	success:function(data){	
		creatData(data);	
	},
	error: function(data){alert(data)}			
});	

function creatData(size){
	if(size>4){
		document.apiAB.a.value = "0";
		document.apiAB.b.value = "4";
		document.apiAB.carBlock.value = "0";
		$.ajax({
			url:"/RootPlan/AddressDataServlet",
			data: $("#apiAB").serialize(),
			type:"post"
		});	
		document.apiAB.a.value = "4";
		document.apiAB.b.value = String(size);
		document.apiAB.carBlock.value = "1";
		$.ajax({
			url:"/RootPlan/AddressDataServlet",
			data: $("#apiAB").serialize(),
			type:"post"
		});	
	}else{
		document.apiAB.a.value = "0";
		document.apiAB.b.value = String(size);
		document.apiAB.carBlock.value = "0";
		$.ajax({
			url:"/RootPlan/AddressDataServlet",
			data: $("#apiAB").serialize(),
			type:"post"
		});
	}
}
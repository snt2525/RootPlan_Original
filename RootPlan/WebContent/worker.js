onmessage = function(event){	
	var result = "";

	var gugu = event.data;

	//worker.postMessage(data) 이런식으로 하나만 보내기 때문에 event.data.result 식으로 써줄필요없다.

	

	for(var a=1; a<10; a++){

		result += gugu + "*" + a + "=" + (gugu*a) + "<br/>";

	}

	postMessage(result);

};

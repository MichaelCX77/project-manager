	function tipo_conta(){
		var tipo = document.getElementById('tipo_conta').value
		var req = new XMLHttpRequest();
		req.open('GET',tipo,true)
		req.send();
	}
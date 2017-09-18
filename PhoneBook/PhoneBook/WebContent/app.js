window.onload = function(event) {
	
	document.getElementById("add_btn").onclick = function() {
		
		var phone = document.getElementById("add_phone");
		var name = document.getElementById("add_name");
		var res = document.getElementById("add_res");

		var xhr = new XMLHttpRequest();
		xhr.open("POST", "m", true);
		xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded", "charset=utf-8");
		xhr.send("action=add&phone=" + encodeURIComponent(phone.value) + "&name=" + encodeURIComponent(name.value));
		
		xhr.onreadystatechange = function() {
			if ( xhr.readyState != 4 ) {
				return
			}
			if ( xhr.status == 200 ) {
				res.innerHTML = 
					"\"" + name.value + 
					"\" Добавлен с номером: \"" + phone.value + "\"";
				name.value = "";
				phone.value = "";
			} else {
				res.innerHTML = xhr.responseText;
			}
		}
	}
	
	document.getElementById("del_btn").onclick = function() {
		
		var phone = document.getElementById("del_phone");
		var name = document.getElementById("del_name");
		var res = document.getElementById("del_res");
		
		var xhr = new XMLHttpRequest();
		xhr.open("POST", "m", true);
		xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded", "charset=utf-8");
		xhr.send("action=del&phone=" + encodeURIComponent(phone.value) + "&name=" + encodeURIComponent(name.value));
		
		xhr.onreadystatechange = function() {
			if ( xhr.readyState != 4 ) {
				return
			}
			if ( xhr.status == 200 ) {
				res.innerHTML = 
					"Удалена запись \"" + name.value + 
					"\" с номером: \"" + phone.value + "\"";
				name.value = "";
				phone.value = "";
			} else {
				res.innerHTML = xhr.responseText;
			}
		}
	}
}
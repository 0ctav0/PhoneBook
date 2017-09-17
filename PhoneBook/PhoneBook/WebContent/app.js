window.onload = function(event) {
	document.getElementById("btn_add").onclick = function() {
		var phone = document.getElementById("pb_phone");
		var name = document.getElementById("pb_name");
		var xhr = new XMLHttpRequest();
		xhr.open("POST", "m", true);
		xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded", "charset=utf-8");
		xhr.send("pb_phone=" + encodeURIComponent(phone.value) + "&pb_name=" + encodeURIComponent(name.value));
		
		xhr.onreadystatechange = function() {
			if ( xhr.readyState != 4 ) {
				return
			}
			if ( xhr.status == 200 ) {
				document.getElementById("result_add").innerHTML = 
					"\"" + name.value + 
					"\" Добавлен с номером: \"" + phone.value + "\"";
				name.value = "";
				phone.value = "";
			} else {
				document.getElementById("result_add").innerHTML = xhr.responseText;
			}
		}
	}
}
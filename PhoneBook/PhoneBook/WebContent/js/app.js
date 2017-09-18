$(document).ready(function(event) {
	
	var selected = null;
	
	var res = $("#res");
	var list = $("#list");
	var sub = $("#sub");
	var phone = $("#add_phone");
	var name = $("#add_name");
	loadListFromServer();
	
	sub.on("input", function() {
		loadListFromServer();
	});
	
	list.on("click", "li", function(e, name, value) {
		del_btn.prop("disabled", false);
		if ( selected != null ) {
			selected.attr("id", "");
		}
		selected = $(this);
		selected.attr("id", "selected");
		
	});
	
	var add_btn = $("#add_btn");
	var del_btn = $("#del_btn");
	del_btn.prop("disabled", true);
	
	add_btn.on("click", addToServer);
	del_btn.on("click", deleteFromServer);
	
	
	
	// functions

	function addToServer() {

		var xhr = new XMLHttpRequest();
		xhr.open("POST", "m", true);
		xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded", "charset=utf-8");
		xhr.send("action=add&phone=" + encodeURIComponent(phone.val()) + "&name=" + encodeURIComponent(name.val()));
		
		xhr.onreadystatechange = function() {
			if ( xhr.readyState != 4 ) {
				return
			}
			if ( xhr.status == 200 ) {
				res.text("\"" + name.val() + "\" Добавлен с номером: \"" + phone.val() + "\"");
				$("#list").append( "<li><pre class='phone'>" + phone.val() + "</pre> <pre class='name'>" + name.val() + "</pre></li>" );
				name.val("");
				phone.val("");
			} else {
				res.text(xhr.responseText);
			}
		}
	}
	
	function deleteFromServer() {
		
		//var phone = $("#selected .phone");
		var phone = selected.find(".phone");
		var name = selected.find(".name");
		//var name = $("#selected .name");
		
		var xhr = new XMLHttpRequest();
		xhr.open("POST", "m", true);
		xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded", "charset=utf-8");
		xhr.send("action=del&phone=" + encodeURIComponent(phone.text()) + "&name=" + encodeURIComponent(name.text()));
		
		xhr.onreadystatechange = function() {
			if ( xhr.readyState != 4 ) {
				return
			}
			if ( xhr.status == 200 ) {
				res.text("Удалена запись \"" + name.text() + "\" с номером: \"" + phone.text() + "\"");
				//$("#selected").remove();
				selected.remove();
				del_btn.prop("disabled", true);
			} else {
				res.text(xhr.responseText);
			}
		}
	}

	function loadListFromServer() {
		
		var xhr = new XMLHttpRequest();
		xhr.open("POST", "m", true);
		xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded", "charset=utf-8");
		xhr.send("action=list&sub=" + encodeURIComponent(sub.val()));
		
		xhr.onreadystatechange = function() {
			if ( xhr.readyState != 4 ) {
				return;
			}
			if ( xhr.status == 200 ) {
				parseXML(xhr.responseText);
				list.animate({
					scrollTop: list.get(0).scrollHeight
					}, 1);  
			}
		}
	}

	function parseXML(xml) {
		
		var xmlDoc = $.parseXML(xml);
		var $xml = $(xmlDoc);
		var $phoneName = $xml.find("pn");
		list.text("");
		$phoneName.each(function() {
			$("#list").append( "<li><pre class='phone'>" + $(this).find("phone").text() + "</pre> <pre class='name'>" + $(this).find("name").text() + "</pre></li>" );
		});
		
	}

});


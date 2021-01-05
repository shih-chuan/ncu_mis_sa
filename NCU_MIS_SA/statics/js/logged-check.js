function isLogged(){
	let member = localStorage.getItem("member_id");
	$.ajax({
		type: "GET",
		url: "api/auth.do",
		crossDomain: true,
		data: "func=check",
		cache: false,
		dataType: 'json',
		timeout: 5000,
		success: function (response) {
			if(response.status == "200"){
				if(response.message == "true"){
					console.log("hi")
					$(".hide-on-logged").hide();
					$(".show-on-logged").show();
					$(".member-name").html(localStorage.getItem("member_name"));
					return;
				}
			}
			localStorage.removeItem("member_id");
			localStorage.removeItem("member_name");
			$(".need-login").hide();
			$(".show-on-logged").hide();
			return;
		},
		error: function () {
			alert("無法連線到伺服器！");
			return;
		}
	});
	return;
}
function logout(){
	localStorage.removeItem("member_id");
	localStorage.removeItem("member_name");
	$.ajax({
		type: "GET",
		url: "api/auth.do",
		crossDomain: true,
		data: "func=logout",
		cache: false,
		dataType: 'json',
		timeout: 5000,
		success: function (response) {
			alert(response.message);
			if(response.status == "200"){
				alert(response.message);
				window.location.assign("index.html");
			}
		},
		error: function () {
			alert("無法連線到伺服器！");
		}
	});
}
$(function(){
	isLogged();
	$(".logout").click(function(){
		logout();
	})
})
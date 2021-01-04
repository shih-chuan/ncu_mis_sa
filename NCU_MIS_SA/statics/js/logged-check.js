function isLogged(){
	let member = localStorage.getItem("member_id");
	if(!member){
		return false;
	}
	return true;
}
function logout(){
	localStorage.removeItem("member_id");
	localStorage.removeItem("member_name");
	$.ajax({
		type: "GET",
		url: "api/auth.do",
		crossDomain: true,
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
	if(!isLogged()){
		$(".need-login").hide();
		$(".show-on-logged").hide();
	}else{
		 $(".hide-on-logged").hide();
		 $(".show-on-logged").show();
		 $(".member-name").html(localStorage.getItem("member_name"));
	}
	$(".logout").click(function(){
		logout();
	})
})
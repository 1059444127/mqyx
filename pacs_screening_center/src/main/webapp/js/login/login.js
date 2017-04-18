//登录
function checkPassword(){
	var userName = $("#UserName").val();
	var password = $("#Password").val();
	var authCode = $("#VerificationCode").val();
	
	if (userName.replace(/\s+/g,"") == "") {
		$("#tips").html("请输入用户名");
		return false;
	}
	if (password.replace(/\s+/g,"") == "") {
		$("#tips").html("请输入密码");
		return false;
	}
	if (authCode.replace(/\s+/g,"") == "") {
		$("#tips").html("请输入验证码");
		return false;
	}
	
	var params = [];
	params.push("userName=" + userName);
	params.push("password=" + password);
	params.push("authCode=" + authCode);
	var param = params.join('&');
	
	$.ajax({
		type : 'GET',
		url : '/login/checkPassword.htm',
		data : param,
		dataType : 'json',
		success : function(data){
			if(data.R == 1){
				window.location.href=data.data.nexturl;
			}else{
				$("#tips").html(data.I);
			}
		},error : function(){
			layer.msg("登录失败，请稍后重试！", {icon: 2, time: 1000});
		}
	})
}

function editUserPassword(userName){
	var password = $("#password").val();
	var newPassword = $("#newPassword").val();
	var newPasswordAgain = $("#newPasswordAgain").val();
	if(newPassword.replace(/\s+/g,"") == ""){
		layer.msg("请输入新的密码", {icon: 2, time: 1000});
		return false;
	}
	if(newPasswordAgain != newPassword){
		layer.msg("两次新密码不一致，请核对！", {icon: 2, time: 1000});
		return false;
	}
	var params = [];
	params.push("userName=" + userName);
	params.push("password=" + password);
	params.push("newPassword=" + newPassword);
	var param = params.join('&');
	$.ajax({
		type : 'GET',
		url : '/user/editUserPassword.htm',
		data : param,
		dataType : 'json',
		success : function(data){
			if(data.R != 1){
				layer.msg(data.I, {icon: 2, time: 1000});
				return false;
			}
			layer.msg(data.I, {icon: 1, time: 1000});
			$('.closediv').click();
		},error : function(){
			layer.msg("修改密码失败，请稍后重试！", {icon: 2, time: 1000});
		}
	})
}

/**
 * Created by c99 on 2017/3/21.
 */
$('.mq-hospital-point li').click(function(){
    if($(this).hasClass('current')){
        return;
    }
    console.log($(this).index());
    var i=$(this).index();
    var w=1200;
    $('.wrapper').animate({marginLeft: -w*i+"px"},1000);
    $(this).addClass('current').siblings('.current').removeClass('current');
})
var w=1200;

function nextFun(){
	var n=$('.wrapper>ul').length;
	var ml=parseInt($('.wrapper').css("marginLeft"));
	ml-=w;
	$('.wrapper').animate({marginLeft: ml+"px"},3000,function(){
		$('.wrapper').append($('.wrapper ul:first-child'));
		$('.wrapper').css("margin-left",0);
		var i=$('.mq-hospital-point .current').index();
		if(i==(n-1)){
			$('.mq-hospital-point li:first-child').addClass('current').siblings('.current').removeClass('current');
		}
		else{
			$('.mq-hospital-point li').eq(i+1).addClass('current').siblings('.current').removeClass('current');
		}
	});	
};
var start=window.setInterval(nextFun,4000);
$('.mq-hospital-main,.mq-hospital-point li').mouseenter(function(){
	window.clearInterval(start);
})
$('.mq-hospital-main,.mq-hospital-point li').mouseleave(function(){
	start=window.setInterval(nextFun,4000);
})
$('.mq-hospital-point li').mouseenter(function(){
    if($(this).hasClass('current')){
        return;
    }
    var i=$(this).index();
    $('.wrapper').animate({marginLeft: -w*i+"px"},3000);
    $(this).addClass('current').siblings('.current').removeClass('current');
})

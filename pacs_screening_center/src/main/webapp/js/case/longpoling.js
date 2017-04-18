/**
 * 轮询可抢列表
 */
function longpoling(){
	$.ajax({
		url:"/longpoling/getCase0List.htm",
		type:"get",
		dataType:"text",
		success:function(data){
			$("#caseStatus0").html("");
			var obj = eval( "(" + data + ")" );
			for(var i = 0; i < obj.length; i++){
				var ele = [];
				ele.push('<tr>');
				ele.push('<td>'+obj[i].patientName+'</td>');
				ele.push('<td>'+obj[i].modality+'</td>');
				ele.push('<td>'+obj[i].item+'</td>');
				var time = parseInt(obj[i].sendDate.time);
				var date = new Date(time);
				ele.push('<td>'+Format(date,"yyyy-MM-dd")+'</td>');
				ele.push('<td><span class="getit" caseid="'+obj[i].id+'" onclick="getCase(this)">抢</span></td>');
				ele.push('</tr>');
				$("#caseStatus0").append(ele.join(''));
			}
			setTimeout(longpoling(),200);
		},error:function(){
			setTimeout(longpoling(),200);
		}
	});
}
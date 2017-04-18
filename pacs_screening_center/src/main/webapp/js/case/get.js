var objtest = {};
/**
 * 切标签
 */
$('.list li').click(function(){
	$(this).addClass('current').siblings('.current').removeClass('current');
	var id = $(this).attr("id");
	switch(id){
	case "caseStatus1L":
		$("#caseStatus1D").show();
		$("#caseStatus5D").hide();
		$("#caseStatus3D").hide();
		getPersonAllCount();
		doCaseList(1,1);
		break;
	case "caseStatus5L":
		$("#caseStatus1D").hide();
		$("#caseStatus5D").show();
		$("#caseStatus3D").hide();
		getPersonAllCount();
		doCaseList(1,5);
		break;
	case "caseStatus3L":
		$("#caseStatus1D").hide();
		$("#caseStatus5D").hide();
		$("#caseStatus3D").show();
		getPersonAllCount();
		doCaseList(1,3);
		break;
	}
})
/**
 * 数据table操作
 */
function doCaseList(pageNo, status){
	var data = getCaseList(pageNo ,status);
	switch(status){
	case 1:
		$("#caseStatus1TB").html("");
		for(i in data){
			var ele = [];
			ele.push('<tr onclick="caseDetail('+data[i].id+')">');
			ele.push('<td>'+data[i].patientName+'</td>');
			if("F" == data[i].patientSex){
				ele.push('<td>女</td>');
			}else{
				ele.push('<td>男</td>');	
			}
			ele.push('<td>'+data[i].patientAge+'</td>');
			ele.push('<td>'+data[i].visitType+'</td>');
			ele.push('<td>'+data[i].modality+'</td>');
			ele.push('<td>'+data[i].item+'</td>');
			ele.push('<td id="timeout'+data[i].id+'" style="color: #9c0808;">--:--</td>');
			ele.push('</tr>');
			$("#caseStatus1TB").append(ele.join(""));
			//设置倒计时
			var outTime = data[i].missingTime;
			var nowTime = new Date().getTime();
			clearInterval(objtest[data[i].id + "timer"]);
			objtest[data[i].id + "time"] = Math.floor((outTime - nowTime)/1000);
			objtest[data[i].id + "timer"] = setInterval("goOutTime("+data[i].id+")",1000);
		}
		break;
	case 5:
		$("#caseStatus5TB").html("");
		for(i in data){
			var ele = [];
			ele.push('<tr onclick="caseDetail('+data[i].id+')">');
			ele.push('<td>'+data[i].patientName+'</td>');
			if("F" == data[i].patientSex){
				ele.push('<td>女</td>');
			}else{
				ele.push('<td>男</td>');	
			}
			ele.push('<td>'+data[i].patientAge+'</td>');
			ele.push('<td>'+data[i].visitType+'</td>');
			ele.push('<td>'+data[i].modality+'</td>');
			ele.push('<td>'+data[i].item+'</td>');
			ele.push('</tr>');
			$("#caseStatus5TB").append(ele.join(""));
		}
		break;
	case 3:
		$("#caseStatus3TB").html("");
		for(i in data){
			var ele = [];
			ele.push('<tr onclick="caseDetail('+data[i].id+')">');
			ele.push('<td>'+data[i].patientName+'</td>');
			if("F" == data[i].patientSex){
				ele.push('<td>女</td>');
			}else{
				ele.push('<td>男</td>');	
			}
			ele.push('<td>'+data[i].patientAge+'</td>');
			ele.push('<td>'+data[i].visitType+'</td>');
			ele.push('<td>'+data[i].modality+'</td>');
			ele.push('<td>'+data[i].item+'</td>');
			ele.push('</tr>');
			$("#caseStatus3TB").append(ele.join(""));
		}
		break;
	}
}
/**
 * 取列表数据
 */
function getCaseList(pageNo, status){
	var params = [];
	params.push("pageNo=" + pageNo);
	params.push("status=" + status);
	var param = params.join("&");
	var resArr;
	$.ajax({
		url : '/case/getCaseList.htm',
		type : 'GET',
		data : param,
		async : false,
		dataType : 'json',
		success : function(data){
			resArr = data.data.cliArray;
			switch(status){
			case 1:
				$("#status1PageCount").html(data.data.pageCount);
				break;
			case 5:
				$("#status5PageCount").html(data.data.pageCount);
				break;
			case 3:
				$("#status3PageCount").html(data.data.pageCount);
				break;
			}
		},error : function(){
			layer.msg("请求服务失败，请稍后重试！", {icon: 2, time: 1000});
		}
	})
	return resArr;
}

/**
 * 抢单
 * @param ele
 */
function getCase(ele){
	var caseId = $(ele).attr("caseId");
	var params = [];
	params.push("caseId=" + caseId);
	var param = params.join("&");
	$.ajax({
		url : '/case/getCase.htm',
		type : 'GET',
		data : param,
		dataType : 'json',
		success : function(data){
			layer.msg(data.I , {icon: 0, time: 1000});
			$(ele).parents('tr').remove();
			$("#caseStatus1L").click();
		},error : function(){
			layer.msg("请求服务失败，请稍后重试！", {icon: 2, time: 1000});
		}
	})
}
/**
 * 计时
 */
function goOutTime(id){
	//alert(id + ":" +objtest[id + "time"]);
	minutes = Math.floor(objtest[id + "time"]/60);   
	seconds = Math.floor(objtest[id + "time"]%60); 
	minutes = minutes>=10?minutes:'0'+minutes;
	seconds = seconds>=10?seconds:'0'+seconds;
	msg = minutes+":"+seconds;
	document.all["timeout" + id].innerHTML=msg;
	--objtest[id + "time"];
	if(minutes == 0 && seconds == 0){
		clearInterval(objtest[id + "timer"]);
		$("#caseStatus1L").click();
	}
}
/**
 * 上一页
 * @param status
 */
function statusPrev(status){
	switch(status){
	case 1:
		var nowNum = parseInt($("#status1Now").val());
		var pageCount = parseInt($("#status1PageCount").html());
		if(nowNum > pageCount){
			nowNum = pageCount + 1;
		}
		if(nowNum <= 1){
			nowNum = 1;
			$("#status1Now").val(nowNum);
			layer.msg("没有上一页了！", {icon: 2, time: 1000});
			return false;
		}
		$("#status1Now").val(nowNum - 1);
		doCaseList((nowNum - 1), status);
		break;
	case 5:
		var nowNum = parseInt($("#status5Now").val());
		var pageCount = parseInt($("#status5PageCount").html());
		if(nowNum > pageCount){
			nowNum = pageCount + 1;
		}
		if(nowNum <= 1){
			nowNum = 1;
			$("#status5Now").val(nowNum);
			layer.msg("没有上一页了！", {icon: 2, time: 1000});
			return false;
		}
		$("#status5Now").val(nowNum - 1);
		doCaseList((nowNum - 1), status);
		break;
	case 3:
		var nowNum = parseInt($("#status3Now").val());
		var pageCount = parseInt($("#status3PageCount").html());
		if(nowNum > pageCount){
			nowNum = pageCount + 1;
		}
		if(nowNum <= 1){
			nowNum = 1;
			$("#status3Now").val(nowNum);
			layer.msg("没有上一页了！", {icon: 2, time: 1000});
			return false;
		}
		$("#status3Now").val(nowNum - 1);
		doCaseList((nowNum - 1), status);
		break;
	}
}
/**
 * go
 * @param status
 */
function statusGo(status){
	switch(status){
	case 1:
		var nowNum = parseInt($("#status1Now").val());
		var pageCount = parseInt($("#status1PageCount").html());
		if(nowNum > pageCount){
			nowNum = pageCount;
		}
		if(nowNum <= 1){
			nowNum = 1;
		}
		$("#status1Now").val(nowNum);
		doCaseList((nowNum), status);
		break;
	case 5:
		var nowNum = parseInt($("#status5Now").val());
		var pageCount = parseInt($("#status5PageCount").html());
		if(nowNum > pageCount){
			nowNum = pageCount;
		}
		if(nowNum <= 1){
			nowNum = 1;
		}
		$("#status5Now").val(nowNum);
		doCaseList((nowNum), status);
		break;
	case 3:
		var nowNum = parseInt($("#status3Now").val());
		var pageCount = parseInt($("#status3PageCount").html());
		if(nowNum > pageCount){
			nowNum = pageCount;
		}
		if(nowNum <= 1){
			nowNum = 1;
		}
		$("#status3Now").val(nowNum);
		doCaseList((nowNum), status);
		break;
	}
}
/**
 * 下一页
 * @param status
 */
function statusNext(status){
	switch(status){
	case 1:
		var nowNum = parseInt($("#status1Now").val());
		var pageCount = parseInt($("#status1PageCount").html());
		if(nowNum > pageCount){
			nowNum = pageCount - 1;
		}
		if(nowNum == pageCount){
			layer.msg("没有下一页了！", {icon: 2, time: 1000});
			return false;
		}
		$("#status1Now").val(nowNum + 1);
		doCaseList((nowNum + 1), status);
		break;
	case 5:
		var nowNum = parseInt($("#status5Now").val());
		var pageCount = parseInt($("#status5PageCount").html());
		if(nowNum > pageCount){
			nowNum = pageCount - 1;
		}
		if(nowNum == pageCount){
			layer.msg("没有下一页了！", {icon: 2, time: 1000});
			return false;
		}
		$("#status5Now").val(nowNum + 1);
		doCaseList((nowNum + 1), status);
		break;
	case 3:
		var nowNum = parseInt($("#status3Now").val());
		var pageCount = parseInt($("#status3PageCount").html());
		if(nowNum > pageCount){
			nowNum = pageCount - 1;
		}
		if(nowNum == pageCount){
			layer.msg("没有下一页了！", {icon: 2, time: 1000});
			return false;
		}
		$("#status3Now").val(nowNum + 1);
		doCaseList((nowNum + 1), status);
		break;
	}
}
/**
 * 统计
 */
function getPersonAllCount(){
	$.ajax({
		url : '/case/getPersonAllCount.htm',
		type : 'GET',
		dataType : 'json',
		success : function(data){
			$("#status1CountSpan").html(data.data.temp2);
			$("#status3CountSpan").html(data.data.temp3);
			$("#status5CountSpan").html(data.data.temp4);
			$("#statusAllCountSpan").html(data.data.temp1);
		},error : function(){
			//layer.msg("请求服务失败，请稍后重试！", {icon: 2, time: 1000});
		}
	})
}

/**
 * 详情
 * @param caseId
 */
function caseDetail(caseId){
	var params = [];
	params.push("caseId=" + caseId);
	var param = params.join("&");
	$.ajax({
		url : '/case/getCaseDetail.htm',
		type : 'GET',
		data : param,
		dataType : 'json',
		success : function(data){
			if(data.R != 1){
				layer.msg(data.I, {icon: 2, time: 1000});
				$("#caseStatus1L").click();
				return false;
			}
			//处理
			digTem(data.data.cli, data.data.cp, data.data.cd);
		},error : function(){
			layer.msg("请求服务失败，请稍后重试！", {icon: 2, time: 1000});
		}
	})
}
/**
 * 详情处理
 * @param cli
 * @param cp
 */
function digTem(cli, cp, cd){
	//清
	$("#patientNoLS").html("--");
	$("#patientNameLS").html("--");
	$("#patientSexLS").html("--");
	$("#patientAgeLS").html("--");
	$("#itemLS").html("--");
	$("#patientPhoneLS").html("--");
	$("#sendDateLS").html("--");
	$("#visitTypeLS").html("--");
	$("#checkNoLS").html("--");
	$("#sendDoctorNameLS").html("--");
	$("#sendHospitalNameLS").html("--");
	$("#modalityLS").html("--");
	$("#pacsLS").attr("href","");
	$("#diag_A").val("");
	$("#diag_B").val("");
	$("#commitButton").attr("caseId","");
	//填
	$("#patientNoLS").html(cli.patientNo);
	$("#patientNameLS").html(cli.patientName);
	$("#patientSexLS").html(cli.patientSex);
	$("#patientAgeLS").html(cli.patientAge);
	$("#itemLS").html(cli.item);
	$("#patientPhoneLS").html(cli.patientPhone);
	$("#sendDateLS").html(Format(new Date(cli.sendDate.time),"yyyy-MM-dd"));
	$("#visitTypeLS").html(cli.visitType);
	$("#checkNoLS").html(cli.checkNo);
	$("#sendDoctorNameLS").html(cli.sendDoctorName);
	$("#sendHospitalNameLS").html(cli.sendHospitalName);
	$("#modalityLS").html(cli.modality);
	$("#pacsLS").attr("href",cp.readUrl);
	switch(cli.status){
		case '1':
			$("#tree1").css("pointer-events","auto");
			$("#diag_A").removeAttr("disabled");
			$("#diag_B").removeAttr("disabled");
			$("#commitButton").attr("caseId", cli.id);
			$("#digCL").show();
			break;
		case '5':
			$("#tree1").css("pointer-events","none");
			$("#diag_A").val(cd.digSee);
			$("#diag_B").val(cd.digMain);
			$("#diag_A").attr("disabled","disabled");
			$("#diag_B").attr("disabled","disabled");
			$("#digCL").hide();
			break;
		case '3':
			$("#tree1").css("pointer-events","none");
			$("#diag_A").val("诊断已逾期");
			$("#diag_B").val("诊断已逾期");
			$("#diag_A").attr("disabled","disabled");
			$("#diag_B").attr("disabled","disabled");
			$("#digCL").hide();
			break;
		}
	zTree_init_fun();
}
/**
 * 清空诊断
 */
function clearDiag(){
	$("#diag_A").val("");
	$("#diag_B").val("");
}
/**
 * 提交诊断
 * @param obj
 */
function commitDiag(obj){
	var caseId = $(obj).attr("caseId");
	var digSee = $("#diag_A").val();
	var digMain = $("#diag_B").val();
	
	if(digSee.replace(/\s+/g,"") == ""){
		layer.msg("请输入影像所见", {icon: 2, time: 1000});
		return false;
	}
	if(digMain.replace(/\s+/g,"") == ""){
		layer.msg("请输入诊断结果", {icon: 2, time: 1000});
		return false;
	}
	
	var params = [];
	params.push("caseId=" + caseId);
	params.push("digSee=" + digSee);
	params.push("digMain=" + digMain);
	var param = params.join("&");
	$.ajax({
		url : '/case/commitDiag.htm',
		type : 'POST',
		data : param,
		dataType : 'json',
		success : function(data){
			if(data.R != 1){
				layer.msg(data.I, {icon: 2, time: 1000});
				return false;
			}
			layer.msg(data.I, {icon: 1, time: 1000});
			$("#tree1").css("pointer-events","none");
			$("#diag_A").attr("disabled","disabled");
			$("#diag_B").attr("disabled","disabled");
			$("#digCL").hide();
			$("#caseStatus1L").click();
		},error : function(){
			layer.msg("请求服务失败，请稍后重试！", {icon: 2, time: 1000});
		}
	})
}
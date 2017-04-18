//var digUrl = "http://test.diagnose.zwjk.com";
var digUrl = "http://127.0.0.1:9090";
var digId = 0;
/*var openName = $("#openName").val();*/

function zTree_init_fun(){
	digId = 0;
	var setting = {
		view: {
			showIcon: false,
			dblClickExpand: false
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			onClick: zTreeOnClick,
			onDblClick: zTreeOnClickD
		},
		async: {
			enable: true,
			type : "GET",
			dataType: "text",
			url: digUrl + "/DigPacs/getDigPacs.htm",
			autoParam: ["id=pId"],
			dataFilter: filter
		}
	};
	//单击展开节点
	function zTreeOnClick(event, treeId, treeNode) {
		if(treeNode.type != "rept"){
			var zTree = $.fn.zTree.getZTreeObj("tree1");
			zTree.expandNode(treeNode);
		}
	};
	//双击获取详情
	function zTreeOnClickD(event, treeId, treeNode){
		if(treeNode.type == "rept"){
			if(digId != treeNode.id){
				$.ajax({
					type : 'GET',
					url : digUrl + "/DigPacs/getDigPacsDetails.htm",
					data : 'id=' + treeNode.id,
					dataType : 'json',
					success : function(data){
						//var obj = eval("("+ data + ")");
						$("#diag_A").val(data.A);
						$("#diag_B").val(data.B);
					},error : function(){
						layer.msg('获取报告模板失败', {icon: 2, time: 1000});
					}
				})
				digId = treeNode.id;
			}
		}
	};
		
	function filter(treeId, parentNode, childNodes) {
		if (!childNodes) return null;
		for (var i=0, l=childNodes.length; i<l; i++) {
			childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
		}
		return childNodes;
	}
	
	var zNodes = [];
	$.ajax({
		type : 'GET',
		url : digUrl + "/DigPacs/getDigPacs.htm",
		data : 'pId=0',
		dataType : 'json',
		success : function(data){
			//var obj = eval("("+ data + ")");
			zNodes = data;
			$.fn.zTree.init($("#tree1"), setting, zNodes);
			var zTree = $.fn.zTree.getZTreeObj("tree1");
			var openName = $("#modalityLS").html();
			var node = zTree.getNodeByParam("name", openName);
			zTree.expandNode(node);
		},error : function(){
			layer.msg('获取母节点失败', {icon: 2, time: 1000});
		}
	})
}

//**********************************************************************************************
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>账单审核</title>
<%@include file="/WEB-INF/views/include/head.jsp" %>
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" rel="stylesheet" />
<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js" type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
		
	});
	var count = ${cc_flag};
	var ccFlag = 0;
	function pass(){
		top.$.jBox.confirm("确定审核通过吗?", "确认", function(v, h, f){  
			if (v === 'ok') {
				var url="${ctx}/charge/bill/chargeBill/checkBill?bill_id=" + "${bill_id}&ccFlag=" + ccFlag;
	    		var data = ajaxFunction(url);
	    		if (data.result == "success") {
	    			top.$.jBox.alert(data.message, '提示',{ closed: function () {
			    		window.opener.subm();
			    		window.close();}
	    			});
	    		}else{
	    			top.$.jBox.alert("账单审核失败，错误信息" + data.message + ".");
	    		}
			} else if (v == 'cancel'){
				top.$.jBox.tip("cancel", 'info');
            }
			return true; //close
		});
	}
	function cc2manager(){ 
		count++;
		if(count % 2 == 0) {
			ccFlag = 0;
		} else {
			ccFlag = 1;
		}
	}  
	function ajaxFunction(url,data){
		var result;
		$.ajax({
	    	type: "post",
	    	url: url,
	    	data: data,
	        dataType : "json",  
	        async: false,
	    	error: function(request) {
	    		//loading('正在提交，请稍等...');
	     		alert("连接异常");
	    	},
	    	success: function(data) {
	    	    result = data;
	    	}
		});
		return result;
	}
</script>
</head>
<body>
<input type="button" value="审核通过" onClick="pass()" /> 
<input type=checkbox onclick="cc2manager()" name="cc" <c:if test="${cc_flag=='1'}">checked="checked"</c:if>>是否发送至客户经理<br>
${html}
</body>
</html>
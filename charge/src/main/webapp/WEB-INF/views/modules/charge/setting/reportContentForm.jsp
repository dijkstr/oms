<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>模板配置管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate();
			var vdefault = $('#remarks').val();  
		    $('#remarks').focus(function() {  
		    	//获得焦点时，如果值为默认值，则设置为空  
		    	if ($(this).val() == vdefault) {  
		        	$(this).val("");  
		    	}  
		    });  
		    $('#remarks').blur(function() {  
		    	//失去焦点时，如果值为空，则设置为默认值  
		    	if ($(this).val()== "") {  
		        	$(this).val(vdefault); ;  
		    	}  
		    }); 
		});
		function check(){
			var bill_id = $("#remarks").val();
			if (bill_id == "") {
				top.$.jBox.info("请输入账单编号。", "提示");
				return;
			}
			var url = "${ctx}/charge/bill/export/viewPDFDetail?bill_id=" + bill_id ;
			window.open(url);
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/charge/setting/reportConfig/">模板配置列表</a></li>
		<li class="active"><a href="${ctx}/charge/setting/reportConfig/form?id=${reportConfig.id}">模板内容<shiro:hasPermission name="charge:setting:reportConfig:edit">${not empty reportConfig.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="charge:setting:reportConfig:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	
	<form:form id="inputForm" modelAttribute="reportConfig" action="${ctx}/charge/setting/reportConfig/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label" for="name">内容:</label>
			<div class="controls">
				<form:textarea path="template_content" htmlEscape="false" rows="7" maxlength="100000" class="input-xxlarge"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="charge:setting:reportConfig:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			<shiro:hasPermission name="charge:bill:chargeBill:view">
				<form:input path="remarks" htmlEscape="false" maxlength="200" value="请输入账单编号" class="required" style="width:150px"/>
				<a href="javascript:void(0)" onclick="check();" class="table_button_alink">账单预览</a>
			</shiro:hasPermission>
		</div>
	</form:form>
</body>
</html>

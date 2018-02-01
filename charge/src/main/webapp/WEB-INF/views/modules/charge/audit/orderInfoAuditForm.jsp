<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>计费合同审核管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#contract_process_key").focus();
			$("#inputForm").validate();
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/charge/audit/orderInfoAudit/">计费合同审核列表</a></li>
		<li class="active"><a href="${ctx}/charge/audit/orderInfoAudit/form?id=${orderInfoAudit.id}">计费合同审核<shiro:hasPermission name="charge:audit:orderInfoAudit:edit">${not empty orderInfoAudit.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="charge:audit:orderInfoAudit:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	
	<form:form id="inputForm" modelAttribute="orderInfoAudit" action="${ctx}/charge/audit/orderInfoAudit/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label" for="contract_process_key">合同号:</label>
			<div class="controls">
				<form:input path="contract_process_key" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="remarks">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="charge:audit:orderInfoAudit:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>邮件配置管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate();
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/charge/setting/mailConfig/">邮件配置列表</a></li>
		<li class="active"><a href="${ctx}/charge/setting/mailConfig/form?id=${mailConfig.id}">邮件内容<shiro:hasPermission name="charge:setting:mailConfig:edit">${not empty mailConfig.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="charge:setting:mailConfig:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	
	<form:form id="inputForm" modelAttribute="mailConfig" action="${ctx}/charge/setting/mailConfig/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label" for="name">主题:</label>
			<div class="controls">
				<form:textarea path="mail_subject" htmlEscape="false" rows="1" maxlength="500" class="input-xxlarge"/>
			</div>
			<label class="control-label" for="name">内容:</label>
			<div class="controls">
				<form:textarea path="mail_content" htmlEscape="false" rows="7" maxlength="2000" class="input-xxlarge"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="charge:setting:mailConfig:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>

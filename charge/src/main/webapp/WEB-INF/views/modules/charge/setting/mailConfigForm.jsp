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
		<li class="active"><a href="${ctx}/charge/setting/mailConfig/form?id=${mailConfig.id}">邮件配置<shiro:hasPermission name="charge:setting:mailConfig:edit">${not empty mailConfig.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="charge:setting:mailConfig:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	
	<form:form id="inputForm" modelAttribute="mailConfig" action="${ctx}/charge/setting/mailConfig/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label" for="name">所属部门:</label>
			<div class="controls">
				<tags:treeselect id="office" name="office_id" value="${office.code}" labelName="office_name" labelValue="${office.name}" 
					title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" idVal="code"/>
			</div>

			<label class="control-label" for="name">smtp身份认证:</label>
			<div class="controls">
				<form:input path="smtpauth" htmlEscape="false" maxlength="200" class="required"/>
			</div>
			<label class="control-label" for="name">smtp主机:</label>
			<div class="controls">
				<form:input path="smtphost" htmlEscape="false" maxlength="200" class=""/>
			</div>
			<label class="control-label" for="name">smtp端口号:</label>
			<div class="controls">
				<form:input path="smtpport" htmlEscape="false" maxlength="200" class="required"/>
			</div>
			<label class="control-label" for="name">smtp发送者:</label>
			<div class="controls">
				<form:input path="smtpsender" htmlEscape="false" maxlength="200" class=""/>
			</div>
			<label class="control-label" for="name">密送:</label>
			<div class="controls">
				<form:input path="bcc" htmlEscape="false" maxlength="200" class=""/>
			</div>
			<label class="control-label" for="name">smtp用户名称:</label>
			<div class="controls">
				<form:input path="smtpuser" htmlEscape="false" maxlength="200" class=""/>
			</div>
			<label class="control-label" for="name">smtp密码:</label>
			<div class="controls">
				<form:input path="smtppassword" htmlEscape="false" maxlength="200" class=""/>
			</div>
			<label class="control-label" for="name">模板名称:</label>
			<div class="controls">
				<form:input path="template" htmlEscape="false" maxlength="200" class="required"/>
			</div>
			<label class="control-label" for="remarks">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge"/>
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

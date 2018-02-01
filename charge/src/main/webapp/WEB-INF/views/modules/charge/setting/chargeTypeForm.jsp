<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>字典管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#value").focus();
			$("#inputForm").validate();
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/charge/setting/chargeType">计费类型列表</a></li>
		<li class="active"><a href="${ctx}/charge/setting/chargeType/form?id=${dict.id}">计费类型<shiro:hasPermission name="charge:setting:chargeType:edit">${not empty chargeType.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="charge:setting:chargeType:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	
	<form:form id="inputForm" modelAttribute="chargeTypeForm" action="${ctx}/charge/setting/chargeType/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="type" />
		<tags:message content="${message}"/>
		<form:hidden path="updateDate"/>
		<div class="control-group">
			<label class="control-label" for="target">所属部门:</label>
			<div class="controls">
				<tags:treeselect id="office" name="office_id" value="${office.code}" labelName="office_name" labelValue="${office.name}" 
				title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" idVal="code"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="value">计费类型:</label>
			<div class="controls">
				<form:input path="value" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="label">计费类型名称:</label>
			<div class="controls">
				<form:input path="label" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="description">描述:</label>
			<div class="controls">
				<form:input path="description" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="sort">排序:</label>
			<div class="controls">
				<form:input path="sort" htmlEscape="false" maxlength="11" class="required digits"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="charge:setting:chargeType:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
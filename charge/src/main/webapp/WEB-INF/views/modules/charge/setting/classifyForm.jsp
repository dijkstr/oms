<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>计费分类管理</title>
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
		<li><a href="${ctx}/charge/setting/classify/">计费分类列表</a></li>
		<li class="active"><a href="${ctx}/charge/setting/classify/form?id=${classify.id}&classify_parent=${menu.classify_parent}">计费分类<shiro:hasPermission name="charge:setting:classify:edit">${not empty classify.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="charge:setting:classify:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="classifyForm" action="${ctx}/charge/setting/classify/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="updateDate"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">上级分类:</label>
			<div class="controls">
				 <tags:treeselect id="classify_parent" name="classify_parent" value="${classify_parent.id}" labelName="classify_parent_name" 
				 labelValue="${classify_parent.classify_name}" title="计费分类" url="/charge/setting/classify/treeData" cssClass="input-small" allowClear="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="target">所属部门:</label>
			<div class="controls">
				<tags:treeselect id="office" name="office_id" value="${office.code}" labelName="office_name" labelValue="${office.name}" 
				title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" idVal="code"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="name">分类名称:</label>
			<div class="controls">
				<form:input path="classify_name" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="sort">排序:</label>
			<div class="controls">
				<form:input path="order_in" htmlEscape="false" maxlength="10" class="required digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="remarks">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="charge:setting:classify:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>

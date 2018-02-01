<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>系统参数配置管理</title>
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
		<li><a href="${ctx}/charge/tsysconfig">系统参数配置列表</a></li>
		<li class="active"><a href="${ctx}/charge/tsysconfig/form?prop_name=${tsysconfig.prop_name}">系统参数配置<shiro:hasPermission name="charge:tsysconfig:edit">${not empty tsysconfig.prop_name?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="charge:tsysconfig:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	
	<form:form id="inputForm" modelAttribute="tsysconfig" action="${ctx}/charge/tsysconfig/save" method="post" class="form-horizontal">
		<tags:message content="${message}"/>	
		 <div class="control-group">		 
		   <label class="control-label" for="name">系统参数名称:</label>
		   <div class="controls">
		     <c:if test="${empty tsysconfig.prop_name}">
				<form:input path="prop_name" htmlEscape="false" maxlength="50" class="required" />
			 </c:if>
			 <c:if test="${not empty tsysconfig.prop_name}">
				<form:input path="prop_name" htmlEscape="false"  class="required" readonly="true"/>
			 </c:if>
			</div>
			
			<label class="control-label" for="name">系统参数值:</label>
			<div class="controls">
				<form:input path="prop_code" htmlEscape="false" maxlength="50" class="required"/>
			</div>
			
			<label class="control-label" for="remarks">系统参数说明:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge"/>
			</div>
			
		</div>
		
		<div class="form-actions">
		
			<shiro:hasPermission name="charge:tsysconfig:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		 	
		</div>
	</form:form>
</body>
</html>

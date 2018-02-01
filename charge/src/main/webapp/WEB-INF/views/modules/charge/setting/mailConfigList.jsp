<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>邮件配置管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnReset").on("click",function(){
				$("#searchForm input:not([type='submit'],[type='button'])").val("");
				$("#searchForm select option:selected").attr("selected", false);
				$("#searchForm a span").text("请选择");
			});
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/charge/setting/mailConfig/">邮件配置列表</a></li>
		<shiro:hasPermission name="charge:setting:mailConfig:edit"><li><a href="${ctx}/charge/setting/mailConfig/form">邮件配置添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="mailConfig" action="${ctx}/charge/setting/mailConfig/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label class="control-label" for="classify">所属部门:</label>
			<tags:treeselect id="office" name="office_id" value="${office.id}" labelName="department_name" labelValue="${office.name}" 
				title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true"/>
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>部门名称</th>
				<th>部门编号</th>
				<th>smtp身份认证</th>
				<th>smtp主机</th>
				<th>smtp端口号</th>
				<th>smtp发送者</th>
				<th>密送</th>
				<th>smtp用户名称</th>
				<th>smtp密码</th>
				<th>模板名称</th>
				<th>备注</th>
				<shiro:hasPermission name="charge:setting:mailConfig:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mailConfig">
			<tr>
				<td>${fns:getOfficeNameByCode(mailConfig.office_id)}</td>
				<td>${mailConfig.office_id}</td>
				<td>${mailConfig.smtpauth}</td>
				<td>${mailConfig.smtphost}</td>
				<td>${mailConfig.smtpport}</td>
				<td>${mailConfig.smtpsender}</td>
				<td>${mailConfig.bcc}</td>
				<td>${mailConfig.smtpuser}</td>
				<td>${mailConfig.smtppassword}</td>
				<td><a href="${ctx}/charge/setting/mailConfig/mailContentForm?id=${mailConfig.id}">${mailConfig.template}</a></td>
				<td>${mailConfig.remarks}</td>
				<shiro:hasPermission name="charge:setting:mailConfig:edit"><td>
    				<a href="${ctx}/charge/setting/mailConfig/form?id=${mailConfig.id}">修改</a>
					<a href="${ctx}/charge/setting/mailConfig/delete?id=${mailConfig.id}" onclick="return confirmx('确认要删除该邮件配置吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

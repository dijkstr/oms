<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>导出配置管理</title>
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
		<li class="active"><a href="${ctx}/charge/setting/reportConfig/">导出配置列表</a></li>
		<shiro:hasPermission name="charge:setting:reportConfig:edit"><li><a href="${ctx}/charge/setting/reportConfig/form">导出配置添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="reportConfig" action="${ctx}/charge/setting/reportConfig/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>导出主键 ：</label><form:input path="config_key" htmlEscape="false" maxlength="50" class="input-small"/>
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>导出主键</th>
				<th>文件类型</th>
				<th>模板名称</th>
				<th>文件名称</th>
				<th>实现类名称</th>
				<th>权限</th>
				<th>备注</th>
				<shiro:hasPermission name="charge:setting:reportConfig:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="reportConfig">
			<tr>
				<td>${reportConfig.config_key}</td>
				<td>${reportConfig.file_type}</td>
				<td><a href="${ctx}/charge/setting/reportConfig/reportContentForm?id=${reportConfig.id}">${reportConfig.template}</a></td>
				<td>${reportConfig.file_name}</td>
				<td>${reportConfig.service}</td>
				<td>${reportConfig.permission}</td>
				<td>${reportConfig.remarks}</td>
				<shiro:hasPermission name="charge:setting:reportConfig:edit"><td>
    				<a href="${ctx}/charge/setting/reportConfig/form?id=${reportConfig.id}">修改</a>
					<a href="${ctx}/charge/setting/reportConfig/delete?id=${reportConfig.id}" onclick="return confirmx('确认要删除该模板配置吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>系统参数配置管理</title>
	<meta name="decorator" content="default"/>
	
	<style type="text/css">
.form_tr_div {
	width: 33%;
	height: 34px;
	float: left;
	margin-bottom: 5px;
	padding-bottom: 4px;
}

.form_tr_div .input-small {
	width: 140px;
}

.select2-container.input-medium {
	margin-left: -4px;
}

.form_tr_div label {
	width: 28%;
}

.table_button_alink {
	margin-right: 5px;
	padding: 0 10px;
	line-height: 24px;
	height: 24px;
	border: 1px solid #dbdbdb;
	-webkit-border-radius: 3px;
	display: inline-block;
	background-color: #e0e0e0;
}

</style>
	
	<script type="text/javascript">
	$(document).ready(function() {
		$("#btnReset").on("click",function(){		
			$("#searchForm input:not([type='submit'],[type='button'])").val("");
			$("#searchForm select option:selected").attr("selected", false);
			$("#searchForm a span").text("请选择");
			$("#pageNo").val("1");
			$("#pageSize").val("10");		
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
		<li class="active"><a href="${ctx}/charge/tsysconfig">系统参数配置列表</a></li>
		<shiro:hasPermission name="charge:tsysconfig:edit"><li><a href="${ctx}/charge/tsysconfig/form">系统参数配置添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="tsysconfig" action="${ctx}/charge/tsysconfig/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		
		<label>系统参数名称：</label>
		<form:input  path="prop_name" htmlEscape="false" maxlength="50" class="input-small" />
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		<input id="btnReset" class="btn btn-primary" type="button" value="重置" />
		
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		<tr>
			<th>系统参数名称</th>
			<th>系统参数值</th>
			<th>系统参数说明</th>
		<shiro:hasPermission name="charge:tsysconfig:edit"><th>操作</th></shiro:hasPermission>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tsysconfig">
			<tr>
			    
				<td>${tsysconfig.prop_name}</td>				
				<td>${tsysconfig.prop_code}</td>
				<td>${tsysconfig.remarks}</td>				 
				<shiro:hasPermission name="charge:tsysconfig:edit"><td>
    				<a href="${ctx}/charge/tsysconfig/form?prop_name=${tsysconfig.prop_name}">修改</a>
					<a href="${ctx}/charge/tsysconfig/delete?prop_name=${tsysconfig.prop_name}" onclick="return confirmx('确认要删除该系统参数配置吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
				 
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>

</html>

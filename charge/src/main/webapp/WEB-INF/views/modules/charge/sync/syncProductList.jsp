<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>产品管理</title>
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
			$("#searchForm input:not([type='submit'],[type='button'],[type='hidden'])").val("");
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
		<li class="active"><a href="${ctx}/charge/sync/product/">协同产品列表</a></li>
		<!--<shiro:hasPermission name="charge:sync:product:edit"><li><a href="${ctx}/charge/sync/product/form">协同产品添加</a></li></shiro:hasPermission>-->
	</ul>
	<form:form id="searchForm" modelAttribute="syncProductForm" action="${ctx}/charge/sync/product/" method="post" class="breadcrumb form-search" style="height:100px;">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="control-group" >
		<div class="form_tr_div">
		<label>协同产品编号：</label>
		<form:input   path="productid" htmlEscape="false"  class="input-small"/>
		</div>
		<div class="form_tr_div">
		<label>协同产品名称：</label>
		<form:input  path="productname" htmlEscape="false"  class="input-small"/>
		</div>
		<div style="width: 100%; float: left; text-align: center;">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
		</div>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>产品编号</th><th>产品名称</th><th>销售状态</th></tr></thead>
		<!--<shiro:hasPermission name="charge:sync:product:edit"><th>操作</th></shiro:hasPermission></tr></thead>-->
		<tbody>
		<c:forEach items="${page.list}" var="syncProduct">
			<tr>
				<td>${syncProduct.productid}</td>
				<td>${syncProduct.productname}</td>
				<td>${syncProduct.salesstatus}</td>
				<!-- 
				<shiro:hasPermission name="charge:sync:product:edit"><td>
    				<a href="${ctx}/charge/sync/product/form?prdid=${syncProduct.prdid}">修改</a>
					<a href="${ctx}/charge/sync/product/delete?prdid=${syncProduct.prdid}" onclick="return confirmx('确认要删除该产品吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
				 -->
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>械同客户选择器</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnReset").on("click",function(){
				$("#searchForm input:not([type='submit'],[type='button'])").val("");
				$("#pageNo").val("1");
				$("#pageSize").val("5");
			});
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		
		function getCustomerInfo(){
			var param={};
			var checkedTd =  $("#contentTable").find("input[name=rowRadio]:checked").parent();
			param.customer_id= $(checkedTd).find("input[name='customerid']").val();
			param.chinesename= $(checkedTd).find("input[name='chinesename']").val();
			return param;
		}
	</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="syncCustomerForm" action="${ctx}/charge/common/syncCustomerChooser" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div style="margin-bottom: 8px;">
			<label>协同客户编号 ：</label><form:input path="customerid" htmlEscape="false" maxlength="50" class="input-small"/>
			<label>协同客户名称 ：</label><form:input path="chinesename" htmlEscape="false" maxlength="50" class="input-small"/>
		</div>
		<div style="text-align:center">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="width: 7%;"></th>
				<th style="width: 26%;">协同客户编号</th>
				<th style="width: 26%;">协同客户名称</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="syncCustomer">
			<tr>
				<td>
					<input type="radio" name="rowRadio" value="${syncCustomer.customerid}">
					<input type="hidden" name="customerid" value="${syncCustomer.customerid}">
					<input type="hidden" name="chinesename" value="${syncCustomer.chinesename}">
				</td>
				<td>${syncCustomer.customerid}</td>
				<td>${syncCustomer.chinesename}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

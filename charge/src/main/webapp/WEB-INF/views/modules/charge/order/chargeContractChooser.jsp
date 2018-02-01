<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>计费合同选择管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnReset").on("click",function(){
				$("#searchForm input:not([type='submit'],[type='button'])").val("");
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
		
		
		function getContractInfo(){
			var param={};
			var checkedTd =  $("#contentTable").find("input[name=rowRadio]:checked").parent();
			param.contract_id= $(checkedTd).find("input[name='contract_id']").val();
			param.order_begin_date= $(checkedTd).find("input[name='order_begin_date']").val();
			param.order_end_date= $(checkedTd).find("input[name='order_end_date']").val();
			param.remarks= $(checkedTd).find("input[name='remarks']").val();
			return param;
		}
	</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="orderInfoForm" action="${ctx}/charge/common/chargeContractChooser" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>协同合同编号 ：</label><form:input path="contract_id" htmlEscape="false" maxlength="50" class="input-small"/>
		<label>客户名称 ：</label><form:input path="hs_customername" htmlEscape="false" maxlength="50" class="input-small"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="width: 10%;"></th>
				<th style="width: 30%;">协同合同编号</th>
				<th>客户名称</th>
				<th>开始时间</th>
				<th>结束时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="chargeContract">
			<tr>
				<td>
					<input type="radio" name="rowRadio" value="${chargeContract.id}">
					<input type="hidden" name="contract_id" value="${chargeContract.contract_id}">
					<input type="hidden" name="order_begin_date" value="${fn:substring(chargeContract.order_begin_date,0,10)}">
					<input type="hidden" name="order_end_date" value="${fn:substring(chargeContract.order_end_date,0,10)}">
					<input type="hidden" name="remarks" value="${chargeContract.remarks}">
				</td>
				<td>${chargeContract.contract_id}</td>
				<td>${chargeContract.syncCustomer.chinesename}</td>
				<td>${fn:substring(chargeContract.order_begin_date,0,10)}</td>
				<td>${fn:substring(chargeContract.order_end_date,0,10)}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

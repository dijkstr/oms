<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>械同合同选择器</title>
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
			param.contractid= $(checkedTd).find("input[name='contractid']").val();
			param.customerid= $(checkedTd).find("input[name='customerid']").val();
			param.customername= $(checkedTd).find("input[name='customername']").val();
			param.realcustomername= $(checkedTd).find("input[name='realcustomername']").val();
			param.signeddate= $(checkedTd).find("input[name='signeddate']").val();
			param.customermanagername= $(checkedTd).find("input[name='customermanagername']").val();
			param.customermanager2name= $(checkedTd).find("input[name='customermanager2name']").val();
			param.companyid= $(checkedTd).find("input[name='companyid']").val();
			param.reporttype_id= $(checkedTd).find("input[name='reporttype_id']").val();			
			return param;
		}
	</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="syncContractForm" action="${ctx}/charge/common/syncContractChooser" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>协同合同编号 ：</label><form:input path="contractid" htmlEscape="false" maxlength="50" class="input-small"/>
		<label>客户名称 ：</label><form:input path="customername" htmlEscape="false" maxlength="50" class="input-small"/>
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
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="syncContract">
			<tr>
				<td>
					<input type="radio" name="rowRadio" value="${syncContract.con_id}">
					<input type="hidden" name="contractid" value="${syncContract.contractid}">
					<input type="hidden" name="customerid" value="${syncContract.customerid}">
					<input type="hidden" name="realcustomername" value="${syncContract.customername}">
					<input type="hidden" name="signeddate" value="${syncContract.signeddate}">
					<input type="hidden" name="customername" value="${syncContract.syncCustomer.chinesename}">
					<input type="hidden" name="customermanagername" value="${syncContract.syncCustomer.customermanagername}">
					<input type="hidden" name="customermanager2name" value="${syncContract.syncCustomer.customermanager2name}">
					<input type="hidden" name="companyid" value="${syncContract.companyid}">
					<input type="hidden" name="reporttype_id" value="${fns:getDictLabel(syncContract.reporttype_id,"synccontract_reporttype","")}">
				</td>
				<td>${syncContract.contractid}</td>
				<td>${syncContract.customername}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

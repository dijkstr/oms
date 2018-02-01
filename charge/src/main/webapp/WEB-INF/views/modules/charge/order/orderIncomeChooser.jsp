<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>合同收入设置选择管理</title>
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
		
		
		function getOrderIncome(){
			var param={};
			var checkedTd =  $("#contentTable").find("input[name=rowRadio]:checked").parent();
			param.office_id= $(checkedTd).find("input[name='office_id']").val();
			param.office_name= $(checkedTd).find("input[name='office_name']").val();
			param.contract_id= $(checkedTd).find("input[name='contract_id']").val();
			param.payment_type= $(checkedTd).find("input[name='payment_type']").val();
			param.payment_type_display= $(checkedTd).find("input[name='payment_type_display']").val();
			param.income_begin_date= $(checkedTd).find("input[name='income_begin_date']").val();
			param.income_end_date= $(checkedTd).find("input[name='income_end_date']").val();
			param.payment_type= $(checkedTd).find("input[name='payment_type']").val();
			param.product_id= $(checkedTd).find("input[name='product_id']").val();
			param.product_name= $(checkedTd).find("input[name='product_name']").val();		
			return param;
		}
	</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="orderInfoForm" action="${ctx}/charge/common/orderIncomeChooser" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>协同合同编号 ：</label><form:input path="contract_id" htmlEscape="false" maxlength="50" class="input-small"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="width: 10%;"></th>
				<th>所属部门</th>
				<th>协同合同编号</th>
				<th>收入开始日期</th>
				<th>收入结束日期</th>
				<th>固定费用类型</th>
				<th>协同销售产品编号</th>
				<th>产品名称</th>				
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="orderIncomeSetting">
			<tr>
				<td><input type="radio" name="rowRadio" value="${orderIncomeSetting.contract_id}">
				<input type="hidden" name="office_id" value="${orderIncomeSetting.orderInfo.office_id}">
				<input type="hidden" name="office_name" value="${fns:getOfficeNameByCode(orderIncomeSetting.orderInfo.office_id)}">
				<input type="hidden" name="contract_id" value="${orderIncomeSetting.contract_id}">
				<input type="hidden" name="income_begin_date" value="${fn:substring(orderIncomeSetting.income_begin_date,0,10)}">
				<input type="hidden" name="income_end_date" value="${fn:substring(orderIncomeSetting.income_end_date,0,10)}">
				<input type="hidden" name="payment_type" value="${orderIncomeSetting.payment_type}">
				<input type="hidden" name="payment_type_display" value="${fns:getDictLabel(orderIncomeSetting.payment_type, 'payment_type', '')}">
				<input type="hidden" name="product_id" value="${orderIncomeSetting.product_id}">
				<input type="hidden" name="product_name" value="${orderIncomeSetting.syncProduct.productname}">
				</td>
				<td>${fns:getOfficeNameByCode(orderIncomeSetting.orderInfo.office_id)}</td>
				<td>${orderIncomeSetting.contract_id}</td>
				<td>${fn:substring(orderIncomeSetting.income_begin_date,0,10)}</td>
				<td>${fn:substring(orderIncomeSetting.income_end_date,0,10)}</td>
				<td>${fns:getDictLabel(orderIncomeSetting.payment_type, 'payment_type', '')}</td>
				<td>${orderIncomeSetting.product_id}</td>
				<td>${orderIncomeSetting.syncProduct.productname}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

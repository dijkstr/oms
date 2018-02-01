<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>合同组合选择管理</title>
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
		
		
		function getOrderCombine(){
			var param={};
			var checkedTd =  $("#contentTable").find("input[name=rowRadio]:checked").parent();
			param.office_id= $(checkedTd).find("input[name='office_id']").val();
			param.office_name= $(checkedTd).find("input[name='office_name']").val();
			param.contract_id= $(checkedTd).find("input[name='contract_id']").val();
			param.combine_id= $(checkedTd).find("input[name='combine_id']").val();
			param.combine_begin_date= $(checkedTd).find("input[name='combine_begin_date']").val();
			param.combine_end_date= $(checkedTd).find("input[name='combine_end_date']").val();
			param.product_names= $(checkedTd).find("input[name='product_names']").val();		
			return param;
		}
	</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="orderInfoForm" action="${ctx}/charge/common/orderCombineChooser" method="post" class="breadcrumb form-search">
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
				<th>组合开始日期</th>
				<th>组合结束日期</th>
				<th>协同销售产品名称</th>				
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="orderCombine">
			<tr>
				<td><input type="radio" name="rowRadio" value="${orderCombine.contract_id}">
				<input type="hidden" name="office_id" value="${orderCombine.orderInfo.office_id}">
				<input type="hidden" name="office_name" value="${fns:getOfficeNameByCode(orderCombine.orderInfo.office_id)}">
				<input type="hidden" name="contract_id" value="${orderCombine.contract_id}">
				<input type="hidden" name="combine_id" value="${orderCombine.id}">
				<input type="hidden" name="combine_begin_date" value="${fn:substring(orderCombine.combine_begin_date,0,10)}">
				<input type="hidden" name="combine_end_date" value="${fn:substring(orderCombine.combine_end_date,0,10)}">
				<input type="hidden" name="product_names" value="${orderCombine.product_names}">
				</td>
				<td>${fns:getOfficeNameByCode(orderCombine.orderInfo.office_id)}</td>
				<td>${orderCombine.contract_id}</td>
				<td>${fn:substring(orderCombine.combine_begin_date,0,10)}</td>
				<td>${fn:substring(orderCombine.combine_end_date,0,10)}</td>
				<td>${orderCombine.product_names}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>协同产品选择器</title>
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
		
		
		function getProductInfo(){
			var param={};
			var checkedTd =  $("#contentTable").find("input[name=rowRadio]:checked").parent();
			param.product_id= $(checkedTd).find("input[name='product_id']").val();
			param.ex_product_id= $(checkedTd).find("input[name='ex_product_id']").val();
			param.product_name= $(checkedTd).find("input[name='product_name']").val();
			return param;
		}
	</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="syncProductForm" action="${ctx}/charge/common/syncProductChooser" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<div style="margin-bottom: 8px;">
			<label>协同合同编号 ：</label><form:input path="contractid" htmlEscape="false" maxlength="50" class="input-small"/>
			<label>协同销售产品编码 ：</label><form:input path="productid" htmlEscape="false" maxlength="50" class="input-small"/>
			<label>产品名称 ：</label><form:input path="productname" htmlEscape="false" maxlength="50" class="input-small"/>
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
				<th style="width: 26%;">协同合同编号</th>
				<th style="width: 26%;">协同销售产品编码</th>
				<th>产品名称</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="syncProduct">
			<tr>
				<td>
					<input type="radio" name="rowRadio" value="${syncProduct.productid}">
					<input type="hidden" name="product_id" value="${syncProduct.productid}">
					<input type="hidden" name="ex_product_id" value="${syncProduct.prdid}">
					<input type="hidden" name="product_name" value="${syncProduct.productname}">
				</td>
				<td>${syncProduct.contractid}</td>
				<td>${syncProduct.productid}</td>
				<td>${syncProduct.productname}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

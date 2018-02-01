<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>合同到款管理</title>
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
		//导出
		function  exportInfo(){
			var params = $("#searchForm").serialize();
	    	window.open("${ctx}/charge/account/receive/export/download?templatekey=exportreceive&" 
	    			+ (encodeURI(params)));
			
		}
		//查看协同合同详情
		function viewContract(billId){
			var url = "${ctx}/charge/sync/contract/form?contractid=" + billId;
			window.open(url);
		}
		//查看协同客户
		function viewCustomer(customerId){
			var url = "${ctx}/charge/sync/customer/list?customerid=" + customerId;
			window.open(url);
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/charge/account/receive">合同到款列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="chargeReceiptForm" action="${ctx}/charge/account/receive/" method="post" class="breadcrumb form-search" style="height:210px;">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="control-group" >
		 
		<div class="form_tr_div">
		 <label>所属部门 ：</label>
		<tags:treeselect id="office" name="department" value="${office.code}" labelName="office_name" labelValue="${office.name}" 
				title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" idVal="code"/>
		</div>
		
		<div class="form_tr_div">
		<label>到款开始日期：</label>
         <form:input id="begindate" class="Wdate" type="text" path="bankreceipt_month_begin" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" />
		</div>
		<div class="form_tr_div">
		<label>到款结束日期：</label>
		<form:input id="enddate" class="Wdate" type="text" path="bankreceipt_month_end" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})"/>
		</div>
		 
		<div class="form_tr_div">
		<label>协同合同编号：</label>
		<form:input id="contractid" path="contract_id" htmlEscape="false" class="input-small" />
		</div>
		
		<div class="form_tr_div">
		<label>协同客户号：</label>
		<form:input id="customerid" path="customerid" htmlEscape="false" class="input-small" />
		</div>
		<div class="form_tr_div">
		<label>协同客户名称：</label>
		<form:input id="customername" path="custname" htmlEscape="false" class="input-small" />
		</div>
		<div class="form_tr_div">
		<label>销售产品编号：</label>
		<form:input id="productid" path="saleprdid" htmlEscape="false" class="input-small" />
		</div>
		 
		<div class="form_tr_div">
		<label>到款金额：</label>
		<form:input id="recemny" path="bankreceipt_amount" htmlEscape="false" class="input-small" />
		</div>
		
		<div class="form_tr_div">
		<label>考核部门：</label>
		<form:input id="brabch" path="audit_branch_name" htmlEscape="false" class="input-small" />
		</div>
		<div class="form_tr_div">
		<label>考核人：</label>
		<form:input id="branchname" path="audit_employee_name" htmlEscape="false" class="input-small" />
		</div>
		
		<div style="width: 100%; float: left; text-align: center;">
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		<input id="btnReset" class="btn btn-primary" type="button" value="重置" />
		</div>
		</div>
	</form:form>
	<div style="padding-bottom: 10px;">
	<shiro:hasPermission name="charge:account:receive:edit">
		<input id="generateBill" class="btn btn-primary" type="button" value="导出" onclick="exportInfo()"/>
	</shiro:hasPermission>
	</div>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		<tr>
			<th>到款日期</th>
			<th>所属公司</th>
			<th>合同编号</th>
			<th>协同客户号</th>
			<th>客户名称</th>
			<th>销售产品编号</th>
			<th>销售产品名称</th>
			<th>到款金额</th>
			<th>考核部门</th>
			<th>考核人</th>
			<th>创建时间</th>
		</tr>
		</thead>
		<!--<shiro:hasPermission name="charge:account:receive:edit"><th>操作</th></shiro:hasPermission></tr></thead>-->
		<tbody>
		<c:forEach items="${page.list}" var="chargeReceipt">
			<tr>
			    <td>${fn:substring(chargeReceipt.bankreceipt_month,0,10)}</td>
				<td>${fns:getOfficeNameByCode(chargeReceipt.department)}</td>
				<td><a href="javascript:void(0)" onclick="viewContract(encodeURI(encodeURI('${chargeReceipt.contract_id}')))">${chargeReceipt.contract_id}</a></td>
				<td>${chargeReceipt.customerid}</td>
				<td><a href="javascript:void(0)" onclick="viewCustomer(${chargeReceipt.customerid})">${chargeReceipt.custname}</a></td>
				<td>${chargeReceipt.saleprdid}</td>
				<td>${chargeReceipt.product_name}</td>
				<td><fmt:formatNumber value="${chargeReceipt.bankreceipt_amount}" pattern="0.00"/> </td>
				<td>${chargeReceipt.audit_branch_name}</td>
				<td>${chargeReceipt.audit_employee_name}</td>
				<td>${fn:substring(chargeReceipt.bankreceipt_date,0,10)}</td>
				
				<!-- 
				<shiro:hasPermission name="charge:account:receive:edit"><td>
    				<a href="${ctx}/charge/account/receive/form?receiveid=${chargeReceipt.receiveid}">修改</a>
					<a href="${ctx}/charge/account/receive/delete?receiveid=${chargeReceipt.receiveid}" onclick="return confirmx('确认要删除该合同到款吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
				 -->
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>

</html>

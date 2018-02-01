<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${syncContractForm.contractid}</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate();
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
	<!--  
		<li>
		<a href="${ctx}/charge/sync/contract/">协同合同详情</a>
		</li>
	-->	
	    <li>
		协同合同详情
		</li>
	</ul><br/>

	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<b>合同主表</b>
		<br />
		<thead>
			<tr>
				<th>所属公司</th>
				<th>协同合同编号</th>
				<th>客户编号</th>
				<th>客户名称</th>
				<th>合同签订日期</th>
				<th>计费起点上报类型</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>${syncContractForm.companyname}</td>
				<td>${syncContractForm.contractid}</td>
				<td>${syncContractForm.customerid}</td>
				<td>${syncContractForm.customername}</td>
				<td>${syncContractForm.signeddate}</td>
				<td>${fns:getDictLabel(syncContractForm.reporttype_id,"reporttype","")}</td>
			</tr>
		</tbody>
	</table>

	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<b>产品明细</b>
		<br />
		<thead>
			<tr>
				<th>合同主表id</th>
				<th>销售产品id</th>
				<th>销售产品名称</th>
				<th>产品计费类型id</th>
				<th>产品计费类型</th>
				<th>服务起始日期</th>
				<th>服务终止日期</th>
				<th>服务总月数</th>
				<th>到款</th>
				<th>总价</th>
				<th>备注</th>
				<th>子表32位id</th>
				<th>计费填报标志</th>
			</tr>
		</thead>
		<!--<shiro:hasPermission name="charge:sync:contract:edit"><th>操作</th></shiro:hasPermission></tr></thead>-->
		<tbody>
			<c:forEach items="${syncContractForm.details}"
				var="syncContractDetailForm">
				<tr>
					<td>${syncContractDetailForm.con_id}</td>
					<td>${syncContractDetailForm.salprd_id}</td>
					<td>${syncContractDetailForm.productname}</td>
					<td>${syncContractDetailForm.chargetype}</td>
					<td>${fns:getDictLabel(syncContractDetailForm.chargetype,"chargetype","")}</td>
					<!-- <td>${syncContractDetailForm.chargetypename}</td> -->
					<td>${syncContractDetailForm.servicestartdate}</td>
					<td>${syncContractDetailForm.serviceenddate}</td>
					<td>${syncContractDetailForm.servicemonth}</td>
					<td>${syncContractDetailForm.hasreceive}</td>
					<td>${syncContractDetailForm.amount}</td>
					<td>${syncContractDetailForm.remarks}</td>
					<td>${syncContractDetailForm.detailid}</td>
					<td>${fns:getDictLabel(syncContractDetailForm.accountidentity,"accountidentity","")}</td>
					<!-- 
				<shiro:hasPermission name="charge:sync:contract:edit"><td>
    				<a href="${ctx}/charge/sync/contract/form?con_id=${syncContract.con_id}">修改</a>
					<a href="${ctx}/charge/sync/contract/delete?con_id=${syncContract.con_id}" onclick="return confirmx('确认要删除该合同吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
				 -->
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>

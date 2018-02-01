<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>计费合同审核管理</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
		.form_tr_div { width: 33%; height: 34px; float: left; margin-bottom: 5px; padding-bottom: 4px;}
		.form_tr_div .input-small { width: 140px;}
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
		//查看协同客户
		function viewCustomer(customerId){
			var url = "${ctx}/charge/sync/customer/list?customerid=" + customerId;
			window.open(url);
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/charge/audit/orderInfoAudit/">计费合同审核列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="orderAuditSearchForm" action="${ctx}/charge/audit/orderInfoAudit/" method="post" class="breadcrumb form-search"  style="height:118px;">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="control-group">
			<div class="form_tr_div">
				<label>所属部门 ：</label>
					<tags:treeselect id="office" name="office_id" value="${office.code}" labelName="office_name" labelValue="${office.name}" 
						title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" idVal="code"/>
			</div>
			<div class="form_tr_div">
				<label>协同合同编号 ：</label><form:input path="contract_id" htmlEscape="false" class="input-small"/>
			</div>
			<div class="form_tr_div">
				<label>协同客户号 ：</label><form:input path="customer_id" htmlEscape="false" class="input-small"/>
			</div>
			<div class="form_tr_div">
				<label>客户名称 ：</label><form:input path="customer_name" htmlEscape="false" class="input-small"/>
			</div>
			<div class="form_tr_div">
				<label>审核状态：</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<form:select path="process_status" placeholder="请选择" class="process_status input-medium" style="width: 140px;">
					<form:option value="" label="请选择" />
					<form:option value="29" label="合同审核不通过"/>
					<form:option value="30" label="合同未审核"/>
					<form:option value="40" label="合同已审核"/>
				</form:select>
			</div>
		</div>
		<div style="width: 100%; float: left; text-align: center;">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
		</div>
		
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>所属公司</th>
				<th>协同合同编号</th>
				<th>协同客户号 </th>
				<th>客户名称</th>
				<th>审核状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="orderInfo">
			<tr>
				<td>${fns:getOfficeNameByCode(orderInfo.office_id)}</td>
				<td><a href="${ctx}/charge/audit/orderInfoAudit/detail?contractProcessKey=${orderInfo.contract_process_key}">${orderInfo.contract_id}</a></td>
				<td>${orderInfo.customer_id}</td>
				<td><a href="javascript:void(0)" onclick="viewCustomer(${orderInfo.customer_id})">${orderInfo.chinesename}</a></td>
				<td>${fns:getDictLabel(orderInfo.process_status,"order_status","")}</td>
				<td>
					<c:if test="${orderInfo.process_status ne '40' }">
						<shiro:hasPermission name="charge:audit:orderInfoAudit:verify">
		    				<a href="${ctx}/charge/audit/orderInfoAudit/verifyView?contractProcessKey=${orderInfo.contract_process_key}">审核</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="charge:audit:orderInfoAudit:view">
		    				<a href="${ctx}/sys/workflow/processMap?processInstanceId=${orderInfo.audit_process_id}" class="fancybox"  data-fancybox-type="iframe">跟踪</a>
						</shiro:hasPermission>
					</c:if>
					<shiro:hasPermission name="charge:audit:orderInfoAudit:edit">
						<a href="${ctx}/charge/audit/orderInfoAudit/delete?id=${orderInfo.audit_id}&processInstanceId=${orderInfo.audit_process_id}" onclick="return confirmx('确认要删除该计费合同审核吗？', this.href)">删除</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

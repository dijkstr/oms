<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>调账管理管理</title>
	<style type="text/css">
		.form-horizontal .control-label { float: none; width: 120px; }
		.form_tr_div { width: 33%; float: left; margin-bottom: 5px; padding-bottom: 4px;}
		.form_tr_div2 { width: 1000px; float: left; margin-bottom: 8px; padding-bottom: 8px;}
		.select2-container .select2-choice { height: 28px;}
		.table_button_alink{
		    margin-right: 5px;
		    padding: 0 10px;
		    line-height: 24px;
		    height: 24px;
		    border: 1px solid #dbdbdb;
		    -webkit-border-radius: 3px;
		    display: inline-block;
		    
		    background-color: #e0e0e0;
		}
		.Wdate{width: 150px;}
		#orderAddDiv table{width: 100%; border: 1px #ddd solid;}
		.table_input_string{width:150px;}
		.table_input_num{width:150px;}
		.table_select{width:150px;}
		
		
		.table-condensed .table_input_string{width:100px;}
		.table-condensed .table_input_num{width:95px;}
		
		.specsHead{width:320px;height:35px;float:left}
		.specsHead span{width:85px;float:left}
		.product_td{width:150px;}
		
		.orderValidation{margin-left: 15px; margin-top: 8px; color: red; font-family: "微软雅黑"; font-size: 14px;}
		.orderValidation p{clear: both; overflow: hidden;}
		.orderValidation p span{float: left;width: 20px;}
		.orderValidation p em{float: left; width: 90%; font-style: normal;}
		.orderValidation_title{float: left; margin-right: 6px; width: 7%;}
		.orderValidation_text{float: left; width: 90%;}
		
		.form-actions {text-align: right; background: none; border-top: none; padding: 0;}
	</style>
	<meta name="decorator" content="default"/>
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
		<li class="active"><a href="${ctx}/charge/finance/adjustBill/">调账管理列表</a></li>
		<shiro:hasPermission name="charge:finance:adjustBill:edit"><li><a href="${ctx}/charge/finance/adjustBill/form">调账管理添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="adjustBillForm" action="${ctx}/charge/finance/adjustBill/" method="post" class="breadcrumb form-search" style="height: 100px;">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="control-group" style="float:left;">
			<div class="form_tr_div">
				<label>所属部门 ：</label>
					<tags:treeselect id="office" name="office_id" value="${office.code}" labelName="office_name" labelValue="${office.name}" 
						title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" idVal="code"/>
			</div>
			<div class="form_tr_div">
				<label>开始日期 ：</label><form:input class="Wdate" type="text" path="begin_date" style="width:150px" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyyMM',maxDate:'%y-%M'})"/>
			</div>
			<div class="form_tr_div">
				<label>结束日期 ：</label><form:input class="Wdate" type="text" path="end_date" style="width:150px" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyyMM',maxDate:'%y-%M'})"/>
			</div>
			<div class="form_tr_div">
				<label>合同编号 ：</label><form:input path="contract_id" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">
				<label>客户编号 ：</label><form:input path="customer_id" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">	
				<label>客户名称 ：</label><form:input path="user_name" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div style="width: 100%; float: left; text-align: center;">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
			</div>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>所属部门</th>
				<th>合同编号</th>
				<th>客户编号</th>
				<th>客户名称</th>
				<th>账单日期</th>
				<th>调整金额(元)</th>
				<th>客户经理1</th>
				<th>客户经理2</th>
				<th>调整状态</th>
				<shiro:hasPermission name="charge:finance:adjustBill:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="adjustBillForm">
			<tr>
				<td>${fns:getOfficeNameByCode(adjustBillForm.office_id)}</td>
				<td><a href="javascript:void(0)" onclick="viewContract(encodeURI(encodeURI('${adjustBillForm.contract_id}')))">${adjustBillForm.contract_id}</a></td>
				<td>${adjustBillForm.customer_id}</td>
				<td><a href="javascript:void(0)" onclick="viewCustomer(${adjustBillForm.customer_id})">${adjustBillForm.user_name}</a></td>
				<td>${adjustBillForm.adjust_date}</td>
				<td><fmt:formatNumber value="${adjustBillForm.adjust_balance}" pattern="#,###.##"/></td>
				<td>${adjustBillForm.customermanagername}</td>
				<td>${adjustBillForm.customermanager2name}</td>
				<td>${fns:getDictLabel(adjustBillForm.bill_adjust_status, 'bill_adjust_status', '')}</td>
				<shiro:hasPermission name="charge:finance:adjustBill:edit"><td>
    				<a href="${ctx}/charge/finance/adjustBill/form?id=${adjustBillForm.id}" class="table_button_alink">修改</a>
					<a href="${ctx}/charge/finance/adjustBill/delete?id=${adjustBillForm.id}" onclick="return confirmx('确认要删除该调账信息吗？', this.href)" class="table_button_alink">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

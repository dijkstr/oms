<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>第一次收入管理</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
	.form_tr_div { width: 33%; height: 34px; float: left; margin-bottom: 5px; padding-bottom: 4px;}
	.form_tr_div .input-small { width: 140px;}
	.select2-container.input-medium { margin-left: -4px;}
	.form_tr_div label{width: 28%;}
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
	/*对多选框样式进行修改 */
	.multi_select {
	 		max-height: 60px !important;
	 		overflow-y: scroll !important;
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
		// 导出第一次收入列表 
		function chargeFirstIncomeExport(){
			var params = $("#searchForm").serialize();
			window.open("${ctx}/charge/bill/export/download?templatekey=chargeFirstIncomeExport&" + params);
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/charge/report/chargeFirstIncome/">第一次收入列表</a></li>
		<shiro:hasPermission name="charge:report:chargeFirstIncome:edit"><li><a href="${ctx}/charge/report/chargeFirstIncome/form">第一次收入添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="searchForm" action="${ctx}/charge/report/chargeFirstIncome/" method="post" class="breadcrumb form-search" style="height:150px;">
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
				<label>计费开始年月 ：</label><form:input class="Wdate" type="text" path="charge_begin_month" style="width:150px" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyyMM',maxDate:'%y-%M'})"/>
			</div>
			<div class="form_tr_div">
				<label>计费结束年月 ：</label><form:input class="Wdate" type="text" path="charge_end_month" style="width:150px" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyyMM',maxDate:'%y-%M'})"/>
			</div>
			<div style="width: 100%; float: left; text-align: center; margin-top:20px;">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
			</div>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	<div style="padding-bottom: 10px;">
		<input id="export" class="btn btn-primary" type="button" value="导出第一次收入" onclick="chargeFirstIncomeExport()"/>
	</div>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>所属部门</th>
				<th>协同合同编号 </th>
				<th>收入开始日期</th>
				<th>收入结束日期</th>
				<th>本期开始日期</th>
				<th>第一期累计收入</th>
		<tbody>
		<c:forEach items="${page.list}" var="chargeFirstIncome">
			<tr>
				<td>${fns:getOfficeNameByCode(chargeFirstIncome.office_id)}</td>
				<td>${chargeFirstIncome.contract_id}</td>
				<td>${chargeFirstIncome.income_begin_date}</td>
				<td>${chargeFirstIncome.income_end_date}</td>
				<td>${chargeFirstIncome.charge_begin_date}</td>
				<td><fmt:formatNumber value="${chargeFirstIncome.change_income}" pattern="#,###.####"/></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>合同启动预警管理</title>
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
		function getThisMonth(){
		   var dt = new Date();
		   var m = dt.getMonth()+1;
		   m = m<10?"0"+m:m;
		   return dt.getFullYear() + '' + m
		}
		$(document).ready(function() {
			$("#btnReset").on("click",function(){
				$("#searchForm input:not([type='submit'],[type='button'])").val("");
				$("#searchForm select option:selected").attr("selected", false);
				$("#searchForm a span").text("请选择");
				$("#charge_begin_month").val(getThisMonth());
				$("#charge_end_month").val(getThisMonth());
				$("#pageNo").val("1");
				$("#pageSize").val("10");
			});
			$("searchForm").submit(function(e){
				alert("Submitted");
			});
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		// 导出合同启动预警列表 
		function chargeBeginAlertExport(){
			var params = $("#searchForm").serialize();
			window.open("${ctx}/charge/bill/export/download?templatekey=chargeBeginAlertExport&" + params);
		}
		function search(){
			if($("#charge_begin_month").val() == "" || $("#charge_end_month").val() == ""){
		    	top.$.jBox.info("必须输入开始与结束年月。", "提示");
				return;
			}
			$("#searchForm").submit();
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/charge/report/chargeBeginAlert/">合同启动预警列表</a></li>
		<shiro:hasPermission name="charge:report:chargeBeginAlert:edit"><li><a href="${ctx}/charge/report/chargeBeginAlert/form">合同启动预警添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="searchForm" action="${ctx}/charge/report/chargeBeginAlert/" method="post" class="breadcrumb form-search" style="height:150px;">
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
				<label>协同客户名称 ：</label><form:input path="hs_customername" htmlEscape="false" class="input-small"/>
			</div>
			<div class="form_tr_div">
				<label>开始年月 ：</label><form:input class="Wdate" type="text" path="charge_begin_month" style="width:150px" onfocus="WdatePicker({startDate:'%y-%M',skin:'whyGreen',dateFmt:'yyyyMM',maxDate:'%y-%M'})"/>
			</div>
			<div class="form_tr_div">
				<label>结束年月 ：</label><form:input class="Wdate" type="text" path="charge_end_month" style="width:150px" onfocus="WdatePicker({startDate:'%y-%M',skin:'whyGreen',dateFmt:'yyyyMM',maxDate:'%y-%M'})"/>
			</div>
			<div style="width: 100%; float: left; text-align: center; margin-top:20px;">
				<input id="btnSubmit" class="btn btn-primary" type="button" value="查询" onclick="search()"/>
				<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
			</div>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	<div style="padding-bottom: 10px;">
		<input id="export" class="btn btn-primary" type="button" value="导出合同启动预警" onclick="chargeBeginAlertExport()"/>
	</div>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>所属部门</th>
				<th>协同合同编号 </th>
				<th>协同客户名称 </th>
				<th>合同开始日期</th>
				<th>合同创建时间</th>
				<th>计费启动年月</th>
				<th>本期应付</th>
				<th>本期技术服务费</th>
				<th>本期收入</th>
				<th>累计收入</th>
		<tbody>
		<c:forEach items="${page.list}" var="chargeBeginAlert">
			<tr>
				<td>${fns:getOfficeNameByCode(chargeBeginAlert.office_id)}</td>
				<td>${chargeBeginAlert.contract_id}</td>
				<td>${chargeBeginAlert.customer_name}</td>
				<td>${chargeBeginAlert.order_begin_date}</td>
				<td>${chargeBeginAlert.create_date}</td>
				<td>${chargeBeginAlert.charge_begin_date}</td>
				<td><fmt:formatNumber value="${chargeBeginAlert.payable}" pattern="#,###.####"/></td>
				<td><fmt:formatNumber value="${chargeBeginAlert.change_service_charge}" pattern="#,###.####"/></td>
				<td><fmt:formatNumber value="${chargeBeginAlert.change_income}" pattern="#,###.####"/></td>
				<td><fmt:formatNumber value="${chargeBeginAlert.income}" pattern="#,###.####"/></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

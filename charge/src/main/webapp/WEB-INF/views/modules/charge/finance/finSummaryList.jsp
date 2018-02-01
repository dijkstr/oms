<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>财务汇总表管理</title>
	<style type="text/css">
		.form-horizontal .control-label { float: none; width: 120px; }
		.form_tr_div { width: 33%; float: left; margin-bottom: 5px; padding-bottom: 4px;}
		.form_tr_div2 { width: 1000px; float: left; margin-bottom: 8px; padding-bottom: 8px;}
		.select2-container .select2-choice { height: 28px;}
		.table_button_alink{
		    margin-right: 5px;
		    margin-bottom: 2px;
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
		/*对多选框样式进行修改 */
		.multi_select {
	 		max-height: 60px !important;
	 		overflow-y: scroll !important;
	 	}
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
				$("#income_source").val("").trigger('change');
			});
			var incomeSourceString = "${finSummaryForm.income_source}";
			if(incomeSourceString != undefined || incomeSourceString !== null || incomeSourceString !== ""){
				$("#income_source").val(incomeSourceString.split(',')).trigger('change');
			}
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		// 导出收入明细
		function exportFinanceIncomeDetail(){
			var dept = '';
			if(typeof($("#officeId").val()) != "undefined"){
				dept = $("#officeId").val();
			}
			var selectDatas = document.getElementsByName("radio");
			var contractIds = "";
			var num = 0;
			for(var i = selectDatas.length - 1; i >= 0; i-- ){
	    		if (selectDatas[i].checked){
	    			num += 1;
	    			contractIds = selectDatas[i].value;
	    		}
	    	}
	    	if (num >= 1) {
	    		window.open("${ctx}/charge/bill/export/download?templatekey=exportFinanceDetailList&contract_id="+contractIds + "&office_id=" + dept + "&income_source=" + getIncomeSource());
	    	} else {
	    		window.open("${ctx}/charge/bill/export/download?templatekey=exportFinanceDetailList&office_id=" + dept + "&income_source=" + getIncomeSource());
	    	}
		}
		// 确认收入
		function confirmFinanceIncome(){
			var selectDatas = document.getElementsByName("radio");
			var contractId = "";
			var num = 0;
			for(var i = selectDatas.length - 1; i >= 0; i-- ){
	    		if (selectDatas[i].checked){
	    			num += 1;
	    			contractId = selectDatas[i].value;
	    		}
	    	}
	    	if (num != 1) {
	    		top.$.jBox.alert("请选择一条记录。");
				return;
	    	}
	    	top.$.jBox.confirm("是否确认收入?", "确认", function(v, h, f){  
				if (v === 'ok') {
					var url="${ctx}/charge/finance/finSummary/confirmFinanceIncome?contract_id=" + contractId;
		    		var data = ajaxFunction(url);
		    		if(data.result == 'success'){
		    			top.$.jBox.alert("收入确认成功");
		    			$("#searchForm").submit();
		    		}else{
		    			top.$.jBox.alert("收入确认失败，请联系系统管理员");
		    		}
				} else if (v == 'cancel'){
					top.$.jBox.tip("cancel", 'info');
	            }
				return true; //close
			});
		}
		// 全部确认
		function allConfirmFinanceIncome(){
			var dept = '';
			if(typeof($("#officeId").val()) != "undefined"){
				dept = $("#officeId").val();
			}
			top.$.jBox.confirm("是否确认收入?", "确认", function(v, h, f){  
				if (v === 'ok') {
		    		var url="${ctx}/charge/finance/finSummary/confirmFinanceIncome?dept=" + dept;
		    		var data = ajaxFunction(url);
		    		if(data.result == 'success'){
		    			top.$.jBox.alert("收入确认成功");
		    			$("#searchForm").submit();
		    		}else{
		    			top.$.jBox.alert("收入确认失败，请联系系统管理员");
		    		}
				} else if (v == 'cancel'){
					top.$.jBox.tip("cancel", 'info');
	            }
				return true; //close
			});
		}
		// 导出最新明细
		function exportLastDetail(){
			var dept = '';
			if(typeof($("#officeId").val()) != "undefined"){
				dept = $("#officeId").val();
			}
	    	if ($("#charge_month").val() == '') {
	    		top.$.jBox.alert("请选择计费年月。");
				return;
	    	}
			var charge_month = $("#charge_month").val();
			window.open("${ctx}/charge/bill/export/download?templatekey=exportFinanceDetailList&charge_end_month="+charge_month + "&office_id=" + dept + "&income_source=" + getIncomeSource());
		}
		
		// 导出产品级明细
		function exportProductDetail(){
			var dept = '';
			if(typeof($("#officeId").val()) != "undefined"){
				dept = $("#officeId").val();
			}
	    	if ($("#charge_month").val() == '') {
	    		top.$.jBox.alert("请选择计费年月。");
				return;
	    	}
			var charge_month = $("#charge_month").val();
			window.open("${ctx}/charge/bill/export/download?templatekey=exportProductDetail&charge_end_month="+charge_month + "&office_id=" + dept + "&income_source=" + getIncomeSource());
		}
		
		// 导出合同级明细
		function exportContractDetail(){
			var dept = '';
			if(typeof($("#officeId").val()) != "undefined"){
				dept = $("#officeId").val();
			}
	    	if ($("#charge_month").val() == '') {
	    		top.$.jBox.alert("请选择计费年月。");
				return;
	    	}
			var charge_month = $("#charge_month").val();			
			window.open("${ctx}/charge/bill/export/download?templatekey=exportContractDetail&charge_end_month="+charge_month + "&office_id=" + dept + "&income_source=" + getIncomeSource());
		}

		function getIncomeSource(){
			var income_source = $("#income_source").val();
			if(income_source == null || income_source == undefined){
				income_source = "";
			}
			return income_source;
		}

		// 导出公司级明细
		function exportBUDetail(){
			var dept = '';
			if(typeof($("#officeId").val()) != "undefined"){
				dept = $("#officeId").val();
			}
	    	if ($("#charge_month").val() == '') {
	    		top.$.jBox.alert("请选择计费年月。");
				return;
	    	}
			var charge_month = $("#charge_month").val();
			window.open("${ctx}/charge/bill/export/download?templatekey=exportBUDetail&charge_end_month="+charge_month + "&office_id=" + dept);
		}
		
			function ajaxFunction(url,data){
				var result;
				$.ajax({
			    	type: "post",
			    	url: url,
			    	data: data,
			        dataType : "json",  
			        async: false,
			    	error: function(request) {
			    		//loading('正在提交，请稍等...');
			     		alert("连接异常");
			    	},
			    	success: function(data) {
			    	    result = data;
			    	}
				});
				return result;
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
		<li class="active"><a href="${ctx}/charge/finance/finSummaryForm/">财务汇总表列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="finSummaryForm" action="${ctx}/charge/finance/finSummary/" method="post" class="breadcrumb form-search" style="height: 118px;">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="control-group" style="float:left;">
			<div class="form_tr_div">
				<label>合同编号 ：</label><form:input path="contract_id" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">
				<label>所属部门 ：</label>
					<tags:treeselect id="office" name="office_id" value="${office.code}" labelName="office_name" labelValue="${office.name}" 
						title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" idVal="code"/>
			</div>
			<div class="form_tr_div">
				<label>计费年月 ：</label><form:input class="Wdate" type="text" path="charge_month" style="width:150px" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyyMM',maxDate:'%y-%M'})"/>
			</div>
			<div class="form_tr_div">
				<label>客户编号 ：</label><form:input path="customer_id" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">	
				<label>客户名称 ：</label><form:input path="user_name" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">
				<label>收入来源 ：</label>
				<form:select path="income_source" multiple="multiple" class="input-large multi_select" placeholder="请选择" >
					<form:options items="${fns:getDictList('income_source')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div style="width: 100%; float: left; text-align: center;">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
			</div>
		</div>
	</form:form>
	<div style="padding-bottom: 10px;">
		<shiro:hasPermission name="charge:finance:finSummary:confirmFinanceIncome">
			<input id="finSummaryForm" class="btn btn-primary" type="button" value="确认收入" onclick="confirmFinanceIncome()"/>
		</shiro:hasPermission>
		<shiro:hasPermission name="charge:finance:finSummary:allConfirmFinanceIncome">
			<input id="finSummaryForm" class="btn btn-primary" type="button" value="全部确认" onclick="allConfirmFinanceIncome()"/>
		</shiro:hasPermission>
		<shiro:hasPermission name="charge:finance:finSummary:exportFinanceIncomeDetail">
			<input id="finSummaryForm" class="btn btn-primary" type="button" value="导出明细" onclick="exportFinanceIncomeDetail()"/>
			<input id="finSummaryForm" class="btn btn-primary" type="button" value="导出历年最新明细" onclick="exportLastDetail()"/>
		</shiro:hasPermission>
		<shiro:hasPermission name="charge:finance:finSummary:exportProductDetail">
			<input id="finSummaryForm" class="btn btn-primary" type="button" value="导出产品级月统计表" onclick="exportProductDetail()"/>
		</shiro:hasPermission>
		<shiro:hasPermission name="charge:finance:finSummary:exportContractDetail">
			<input id="finSummaryForm" class="btn btn-primary" type="button" value="导出合同级月统计表" onclick="exportContractDetail()"/>
		</shiro:hasPermission>
		<shiro:hasPermission name="charge:finance:finSummary:exportBUDetail">
			<input id="finSummaryForm" class="btn btn-primary" type="button" value="导出公司级月统计表" onclick="exportBUDetail()"/>
		</shiro:hasPermission>
	</div>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th></th>
				<th>所属部门</th>
				<th>合同编号</th>
				<th>客户编号</th>
				<th>客户名称</th>
				<th>实际使用客户</th>
				<th>收入来源</th>
				<th>计费年月</th>
				<th>累计收入</th>
				<th>收入（增量）</th>
				<th>技术服务费<br>累计</th>
				<th>技术服务费<br>（增量）</th>
				<th>当月到款</th>
				<th>累计应付</th>
				<shiro:hasPermission name="charge:finance:finSummary:view"><th width="80">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="finSummaryForm">
			<tr>
				<td>
					<c:if test="${finSummaryForm.income_source == 'charge'}">
					<input type="radio" name="radio" value="${finSummaryForm.contract_id}">
					</c:if>
				</td>
				<td>${fns:getOfficeNameByCode(finSummaryForm.office_id)}</td>
				<td><a href="javascript:void(0)" onclick="viewContract(encodeURI(encodeURI('${finSummaryForm.contract_id}')))"><p style="word-break:break-all; width:70px;">${finSummaryForm.contract_id}</p></a></td>
				<td>${finSummaryForm.customer_id}</td>
				<td><a href="javascript:void(0)" onclick="viewCustomer(${finSummaryForm.customer_id})"><p style="white-space:pre-wrap; width:70px;">${finSummaryForm.user_name}</p></a></td>
				<td>${finSummaryForm.customername}</td>
				<td>${fns:getDictLabel(finSummaryForm.income_source,"income_source","")}</td>
				<td>${finSummaryForm.charge_month}</td>
				<td><fmt:formatNumber value="${finSummaryForm.income}" pattern="#,###.##"/></td>
				<td><fmt:formatNumber value="${finSummaryForm.change_income}" pattern="#,###.##"/></td>
				<td><fmt:formatNumber value="${finSummaryForm.service_charge}" pattern="#,###.##"/></td>
				<td><fmt:formatNumber value="${finSummaryForm.change_service_charge}" pattern="#,###.##"/></td>
				<td><fmt:formatNumber value="${finSummaryForm.receiveable}" pattern="#,###.##"/></td>
				<td><fmt:formatNumber value="${finSummaryForm.payable}" pattern="#,###.##"/></td>
				<td>
					<shiro:hasPermission name="charge:finance:finSummary:view">
						<a href="${ctx}/charge/finance/finSummary/viewIncomeDetail?contract_id=${finSummaryForm.contract_id}" class="table_button_alink">查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="charge:finance:finSummary:edit">
					<c:if test="${finSummaryForm.income_source == 'charge'}">
	    				<a href="${ctx}/charge/finance/finSummary/confirmIncomeDetail?contract_id=${finSummaryForm.contract_id}" class="table_button_alink">明细确认</a>
					</c:if>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>应收款管理管理</title>
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
		// 应收款导出
		function payableGenerate(){
		    if($("#charge_month").val() == "" ){
		    	top.$.jBox.info("请输入计费年月。", "提示");
				return;
			}
			var charge_month = $("#charge_month").val();
			var dept = "";
			if(typeof($("#officeId").val()) != "undefined"){
				dept = $("#officeId").val();
			}
			window.open("${ctx}/charge/bill/export/download?templatekey=payable&charge_month=" + charge_month + "&dept=" + dept);
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
		<li class="active"><a href="${ctx}/charge/finance/payable/">应收款管理列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="finSummaryForm" action="${ctx}/charge/finance/payable/" method="post" class="breadcrumb form-search" style="height:150px;">
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
				<label>产品编号 ：</label><form:input path="product_id" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">	
				<label>产品名称 ：</label><form:input path="product_name" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
		    <div class="form_tr_div">
				<label>考核人员 ：</label><form:input path="audit_employee_name" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">	
				<label>考核部门 ：</label><form:input path="audit_branch_name" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div style="width: 100%; float: left; text-align: center;">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
			</div>
		</div>
	</form:form>
	<div style="padding-bottom: 10px;">
		<shiro:hasPermission name="charge:finance:payable:payableGenerate">
			<input id="finSummaryForm" class="btn btn-primary" type="button" value="应收款生成" onclick="payableGenerate()"/>
		</shiro:hasPermission>
	</div>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>所属部门</th>
				<th>客户编号</th>
				<th>客户名称</th>
				<th>合同编号</th>
				<th>计费开始时间</th>
				<th>计费结束时间</th>
				<th>当前应收</th>
				<th>本季应收</th>
				<th>累计已到款</th>
				<th>累计总应收款</th>
				<th>累计已确认收入额</th>
				<th>累计已确认收入应收</th>
				<th>支付周期</th>
				<th>考核人</th>
				<th style="width: 110px;">考核部门</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="finSummaryForm">
			<tr>
				<td>${finSummaryForm.companyname}</td>
				<td>${finSummaryForm.customer_id}</td>
				<td><a href="javascript:void(0)" onclick="viewCustomer(${finSummaryForm.customer_id})">${finSummaryForm.user_name}</a></td>
				<td><a href="javascript:void(0)" onclick="viewContract(encodeURI(encodeURI('${finSummaryForm.contract_id}')))">${finSummaryForm.contract_id}</a></td>
				<td>${finSummaryForm.order_begin_date}</td>
				<td>${finSummaryForm.order_end_date}</td>
				<td><fmt:formatNumber value="${finSummaryForm.current_pay}" pattern="#,###.##"/></td>
				<td><fmt:formatNumber value="${finSummaryForm.season_pay}" pattern="#,###.##"/></td>
				<td><fmt:formatNumber value="${finSummaryForm.total_receive}" pattern="#,###.##"/></td>
				<td><fmt:formatNumber value="${finSummaryForm.total_pay}" pattern="#,###.##"/></td>
				<td><fmt:formatNumber value="${finSummaryForm.total_confirm_income}" pattern="#,###.##"/></td>
				<td><fmt:formatNumber value="${finSummaryForm.total_confirm_pay}" pattern="#,###.##"/></td>
				<td>${finSummaryForm.payment_cycle}</td>
				<td>${finSummaryForm.audit_employee_name}</td>
				<td>${finSummaryForm.audit_branch_name}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

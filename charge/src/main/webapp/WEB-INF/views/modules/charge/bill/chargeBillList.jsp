<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>账单管理</title>
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
	<link href="/charge/static/css/jquery.loadmask.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="/charge/static/js/charge/jquery.loadmask.js"></script>
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
		// pdf导出 
		function toPdfAccountDetail(){
			var selectDatas = document.getElementsByName("checkbox");
			var billIds = "";
			var num = 0;
			for(var i = selectDatas.length - 1; i >= 0; i-- ){
	    		if (selectDatas[i].checked){
	    			num += 1;
	    			billIds += selectDatas[i].value + ",";
	    		}
			}
			if (num < 1) {
	    		top.$.jBox.alert("请至少选择一条记录。");
				return;
	    	}
			window.open("${ctx}/charge/bill/export/download?bill_id="+billIds);
		}
		//按部门导出
		function departmentExport(){
			var beginDate = "";
			var endDate = "";
			var dept = "";
	        if($("#charge_begin_month").val() != ""){
				beginDate = $("#charge_begin_month").val();
			}
			if($("#charge_end_month").val() != ""){
				endDate = $("#charge_end_month").val();
			}
			if(typeof($("#officeId").val()) != "undefined"){
				dept = $("#officeId").val();
			}
			window.open("${ctx}/charge/bill/export/download?templatekey=export&flag=department&charge_begin_month="+beginDate+"&charge_end_month="+endDate+"&dept="+dept);
		}
		//按销售导出
		function salerExport(){
			var beginDate = "";
			var endDate = "";
			var dept = "";
	        if($("#charge_begin_month").val() != ""){
				beginDate = $("#charge_begin_month").val();
			}
			if($("#charge_end_month").val() != ""){
				endDate = $("#charge_end_month").val();
			}
			if(typeof($("#officeId").val()) != "undefined"){
				dept = $("#officeId").val();
			}
			window.open("${ctx}/charge/bill/export/download?templatekey=export&flag=saler&charge_begin_month="+beginDate+"&charge_end_month="+endDate+"&dept="+dept);
		}
		// 计费
		function chargeOrder(){
		    if($("#charge_begin_month").val() == "" || $("#charge_end_month").val() == ""){
		    	top.$.jBox.info("必须输入账单开始终了时间。", "提示");
				return;
			}
		    var params = $("#searchForm").serialize();
			var url="${ctx}/charge/bill/chargeBill/chargeOrder?";			
			var data = ajaxFunction(url,params);
			if(data.result=="success"){
				$("body").mask('账单生成中...');
				if(data.wait=="wait") {
					top.$.jBox.alert("计费程序正在执行,请耐心等待程序完成。");
					return;
				}
				getChargeOrderProgress();
			}else{
				$("body").unmask();
				top.$.jBox.error(data.message);
			}
		}
		
		// 获取计费进度信息
	    function getChargeOrderProgress() {
	    	var url = "${ctx}/charge/bill/chargeBill/getChargeOrderProgress"; 
	    	var data = ajaxFunction(url);
	    	if(data.status){
	    		//status:1--正在处理，9--处理终了，-1--异常
	    		//msg:"开始处理"，"处理终了"，"系统异常，请联系管理员"
	    		if(data.status == "1") {
	    			$("body").mask(data.contant + data.percent);
	    			setTimeout("getChargeOrderProgress()", 500);
	    		}else if(data.status == "9") {
	    			$("body").unmask();
	    			top.$.jBox.alert(data.msg);
					$("#searchForm").submit();
	    		}else if(data.status == "-1"){
	    			$("body").unmask();
	    			top.$.jBox.alert(data.msg);
	    			$("#searchForm").submit();
	    		}else {
	    			$("body").unmask();
	    			top.$.jBox.alert("502");
	    			$("#searchForm").submit();
	    		}
	    	}
	    }
		// 选择出账
		function selectGenerateBill(){
			var selectDatas = document.getElementsByName("checkbox");
			var billIds = "";
			var num = 0;
			for(var i = selectDatas.length - 1; i >= 0; i-- ){
	    		if (selectDatas[i].checked){
	    			num += 1;
	    			billIds += selectDatas[i].value + ",";
	    		}
			}
			if (num < 1) {
	    		top.$.jBox.alert("请至少选择一条记录。");
				return;
	    	}
			generateBill("bill_id="+billIds);

		}
		// 批量出账
		function batchGenerateBill(){
			var params = $("#searchForm").serialize();
			generateBill(params);
		}
		// 出账 
		function generateBill(params){
			var url="${ctx}/charge/bill/chargeBill/generateBill?"; 
			var data = ajaxFunction(url,params);  
			if(data.result=="success"){
				$("body").mask('出账中...');
				if(data.wait=="wait") {
					alert("提示","计费程序正在执行,请耐心等待程序完成。");
					return;
				}
				getGenerateBillProgress();
			}else{
				$("body").unmask();
				top.$.jBox.error(data.message);
			}
		}
		
	    // 获取出账进度信息
	    function getGenerateBillProgress() {
	    	var url = "${ctx}/charge/bill/chargeBill/getGenerateBillProgress"; 
	    	var data = ajaxFunction(url);
	    	if(data.status){
	    		//status:1--正在处理，9--处理终了，-1--异常
	    		//msg:"开始处理"，"处理终了"，"系统异常，请联系管理员"
	    		if(data.status == "1") {
	    			$("body").mask(data.contant + data.percent);
	    			setTimeout("getGenerateBillProgress()", 500);
	    		}else if(data.status == "9") {
	    			$("body").unmask();
	    			top.$.jBox.alert(data.msg);
	    			$("#searchForm").submit();
	    		}else if(data.status == "-1"){
	    			$("body").unmask();
	    			top.$.jBox.alert(data.msg);
	    			$("#searchForm").submit();
	    		}else {
	    			$("body").unmask();
	    			top.$.jBox.alert("502");
	    			$("#searchForm").submit();
	    		}
	    	}
	    }
	  	//批量发送账单
		function sendAccountDetail(){
			var selectDatas = document.getElementsByName("checkbox");
			var billIds = "";
			var num = 0;
			for(var i = selectDatas.length - 1; i >= 0; i-- ){
	    		if (selectDatas[i].checked){
	    			num += 1;
	    			billIds += selectDatas[i].value;
	    		}
			}
			if (num < 1) {
	    		top.$.jBox.alert("请至少选择一条记录。");
				return;
	    	}
			top.$.jBox.confirm("确定要发送吗?", "确认", function(v, h, f){  
				if (v === 'ok') {
		    		var url="${ctx}/charge/bill/chargeBill/sendBill?bill_id=" + billIds;
		    		$("body").mask('账单发送中...');
		    		var data = ajaxFunction(url);
		    		$("body").unmask();
		    		top.$.jBox.alert(data.message);
				} else if (v == 'cancel'){
					top.$.jBox.tip("cancel", 'info');
	            }
				return true; 
			});
		}
	    
		// 合同计费列表导出
		function contractChargeExport(){
			var params = $("#searchForm").serialize();
	    	window.open("${ctx}/charge/bill/export/download?templatekey=chargeContractList&" + params);
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
		//审核查看
		function check(){
			var selects = $("input[name='checkbox']:checked").length;
			if(selects != 1){
				top.$.jBox.alert("请选择一条记录。");
				return;
			}
			var id = $("input[name='checkbox']:checked").attr("value");
			var url = "${ctx}/charge/bill/chargeBill/checkView?bill_id=" + id;
			window.open(url);
		}
		//查看
		function viewBill(){
			var selects = $("input[name='checkbox']:checked").length;
			if(selects != 1){
				top.$.jBox.alert("请选择一条记录。");
				return;
			}
			var id = $("input[name='checkbox']:checked").attr("value");
			var url = "${ctx}/charge/bill/chargeBill/viewPDFDetail?bill_id=" + id;
			window.open(url);
		}
		
		//查看连接
		function viewBillByUrl(billId){
			var url = "${ctx}/charge/bill/chargeBill/viewPDFDetail?bill_id=" + billId;
			window.open(url);
		}
		
		function subm(){
			$("#searchForm").submit();
		}	
		//删除账单
	    function deleteBill(){
			top.$.jBox.open("iframe:${ctx}/charge/bill/chargeBill/deleteBillShow", "删除账单",200,$(top.document).height()-420,{
				buttons:{"关闭":true},submit:function(v, h, f){
					if (v=="ok"){					
				    	return true;
					}
				}, loaded:function(h){
					$(".jbox-content", top.document).css("overflow-y","hidden");
				}
			});
		}
	</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="chargeBillSearchForm" action="${ctx}/charge/bill/chargeBill/" method="post" class="breadcrumb form-search" style="height: 150px;">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="control-group" style="float:left;">
			<div class="form_tr_div">
				<label>所属部门 ：</label>
					<tags:treeselect id="office" name="office_id" value="${office.code}" labelName="office_name" labelValue="${office.name}" 
						title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" idVal="code"/>
			</div>
			<div class="form_tr_div">
				<label>账单开始年月 ：</label><form:input class="Wdate" type="text" path="charge_begin_month" style="width:150px" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyyMM',maxDate:'%y-%M'})"/>
			</div>
			<div class="form_tr_div">
				<label>账单结束年月 ：</label><form:input class="Wdate" type="text" path="charge_end_month" style="width:150px" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyyMM',maxDate:'%y-%M'})"/>
			</div>
			<div class="form_tr_div">	
				<label>账单编号 ：</label><form:input path="bill_id" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">
				<label>协同合同编号 ：</label><form:input path="contract_id" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">
				<label>协同客户编号 ：</label><form:input path="customer_id" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">	
				<label>客户名称 ：</label><form:input path="user_name" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">是否已经出账 ：</label>
				<form:select path="charge_off" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('charge_off')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">账单发送状态 ：</label>
				<form:select path="send_status" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('send_status')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div style="width: 100%; float: left; text-align: center;">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
			</div>
		</div>
		
	</form:form>
	<div style="padding-bottom: 10px;">
		<shiro:hasPermission name="charge:bill:chargeBill:view">
			<input id="generateBill" class="btn btn-primary" type="button" value="查看" onclick="viewBill()"/>&nbsp;
		</shiro:hasPermission>
		<shiro:hasPermission name="charge:bill:chargeBill:edit">
  			<input id="generateBill" class="btn btn-primary" type="button" value="审核" onclick="check()"/>&nbsp;
		</shiro:hasPermission>
		<shiro:hasPermission name="charge:bill:chargeBill:chargeOrder">
			<input id="generateBill" class="btn btn-primary" type="button" value="计费" onclick="chargeOrder()"/>&nbsp;
		</shiro:hasPermission>
		<shiro:hasPermission name="charge:bill:chargeBill:selectGenerateBill">
			<input id="generateBill" class="btn btn-primary" type="button" value="选择出账" onclick="selectGenerateBill()"/>&nbsp;
		</shiro:hasPermission>
		<shiro:hasPermission name="charge:bill:chargeBill:batchGenerateBill">
			<input id="generateBill" class="btn btn-primary" type="button" value="批量出账" onclick="batchGenerateBill()"/>&nbsp;
		</shiro:hasPermission>
		<shiro:hasPermission name="charge:bill:chargeBill:sendAccountDetail">
			<input id="generateBill" class="btn btn-primary" type="button" value="发送" onclick="sendAccountDetail()"/>&nbsp;
		</shiro:hasPermission>
		<shiro:hasPermission name="charge:bill:chargeBill:toPdfAccountDetail">
			<input id="generateBill" class="btn btn-primary" type="button" value="PDF账单导出" onclick="toPdfAccountDetail()"/>&nbsp;
		</shiro:hasPermission>
		<shiro:hasPermission name="charge:bill:chargeBill:departmentExport">
			<input id="generateBill" class="btn btn-primary" type="button" value="按部门导出" onclick="departmentExport()"/>&nbsp;
			<input id="generateBill" class="btn btn-primary" type="button" value="按销售导出" onclick="salerExport()"/>&nbsp;
		</shiro:hasPermission>
		<shiro:hasPermission name="charge:bill:chargeBill:contractChargeExport">
			<input id="generateBill" class="btn btn-primary" type="button" value="合同计费列表导出" onclick="contractChargeExport()"/>
		</shiro:hasPermission>
		<shiro:hasPermission name="charge:bill:chargeBill:edit">
			<input id="deleteBill" class="btn btn-primary" type="button" value="删除账单" onclick="deleteBill()"/>
		</shiro:hasPermission>
	</div>
	<tags:message content="${message}"/>
	<div style="width: 100%;overflow-x: auto;">
	<table id="contentTable" class="table table-striped table-bordered table-condensed" style="width:100%;margin: auto;">
		<thead>
			<tr>
				<th width="20"></th>
				<th width="70"><div style="width:70px;">所属部门</div></th>
				<th width="120"><div style="width:120px;">账单编号</div></th>
				<th width="120"><div style="width:120px;">合同编号</div></th>
				<th width="60"><div style="width:60px;">客户号</div></th>
				<th width="160"><div style="width:160px;">客户名称</div></th>
				<th width="70"><div style="width:70px;">开始日期</div></th>
				<th width="70"><div style="width:70px;">结束日期</div></th>
				<th width="70"><div style="width:70px;">审核状态</div></th>
				<th width="50"><div style="width:50px;">发送状态</div></th>
				<th width="90"><div style="width:90px;">计费金额</div></th>
				<th width="90"><div style="width:90px;">技术服务费</div></th>
				<th width="90"><div style="width:90px;">到款</div></th>
				<th width="90"><div style="width:90px;">调账金额</div></th>
				<th width="90"><div style="width:90px;">应付</div></th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="chargeBillForm">
			<tr>
				<td>
					<input type="checkbox" name="checkbox" value="${chargeBillForm.bill_id}">
				</td>
				<td>${fns:getOfficeNameByCode(chargeBillForm.office_id)}</td>
				<td><a href="javascript:void(0)" onclick="viewBillByUrl('${chargeBillForm.bill_id}')">${chargeBillForm.bill_id}</a></td>
				<td><a href="javascript:void(0)" onclick="viewContract(encodeURI(encodeURI('${chargeBillForm.contract_id}')))">${chargeBillForm.contract_id}</a></td>
				<td>${chargeBillForm.customer_id}</td>
				<td><a href="javascript:void(0)" onclick="viewCustomer(${chargeBillForm.customer_id})">${chargeBillForm.user_name}</a></td>
				<td>${chargeBillForm.charge_begin_date}</td>
				<td>${chargeBillForm.charge_end_date}</td>
				<td>${chargeBillForm.check_status}</td>
				<td>${chargeBillForm.send_status}</td>
				<td><fmt:formatNumber value="${chargeBillForm.charge_amt}" pattern="#,###.####"/></td>
				<td><fmt:formatNumber value="${chargeBillForm.service_charge}" pattern="#,###.####"/></td>
				<td><fmt:formatNumber value="${chargeBillForm.receivable}" pattern="#,###.##"/></td>
				<td><fmt:formatNumber value="${chargeBillForm.adjust_amt}" pattern="#,###.##"/></td>
				<td><fmt:formatNumber value="${chargeBillForm.payable}" pattern="#,###.####"/></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	</div>
	<div class="pagination">${page}</div>
</body>
</html>

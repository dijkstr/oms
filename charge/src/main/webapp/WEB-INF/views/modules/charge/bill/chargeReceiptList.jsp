<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>详单管理管理</title>
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
		// 按月级别导出账单级详单  
		function exportChargeBillInfo(){
			var params = $("#searchForm").serialize();
			window.open("${ctx}/charge/bill/export/download?templatekey=chargeReceiptBill&flag=2&" + params);
		}
		
		// 按日级别导出详单 
		function exportChargeReceiptBill(){
			var params = $("#searchForm").serialize();
			window.open("${ctx}/charge/bill/export/download?templatekey=chargeReceiptBill&flag=1&" + params);
		}
		//查看详单 
		function viewChargeReceipt(billId, batchNo, id){
			var url = "${ctx}/charge/bill/chargeReceipt/viewChargeReceipt?bill_id=" + billId + "&batch_no=" + batchNo + "&id=" + id;
			window.open(url);
		}
		function addElement(form, name, value) {
			var element = document.createElement("textarea");
			element.name = name;
			element.value = value;
			form.appendChild(element);
		}
		// 调账技术服务费选择器
		function chargeDetailPDFExportShow(type){
			top.$.jBox.open("iframe:${ctx}/charge/bill/chargeReceipt/chargeDetailPDFExport", "导出计费明细PDF",700,$(top.document).height()-420,{
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
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/charge/bill/chargeReceipt/">详单管理列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="chargeReceiptForm" action="${ctx}/charge/bill/chargeReceipt/" method="post" class="breadcrumb form-search" style="height: 170px;" >
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="control-group" style="float:left;">
			<div class="form_tr_div">
				<label>所属部门 ：</label>
					<tags:treeselect id="office" name="office_id" value="${office.code}" labelName="office_name" labelValue="${office.name}" 
						title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" idVal="code"/>
			</div>
			<div class="form_tr_div">
				<label>起始日期 ：</label><form:input class="Wdate" type="text" path="charge_begin_month" style="width:150px" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyyMM',maxDate:'%y-%M'})"/>
			</div>
			<div class="form_tr_div">
				<label>结束日期 ：</label><form:input class="Wdate" type="text" path="charge_end_month" style="width:150px" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyyMM',maxDate:'%y-%M'})"/>
			</div>
			<div class="form_tr_div">
				<label>结算类型 ：</label>
				<form:select path="fix_charge_type" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('fix_charge_type')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div class="form_tr_div">
				<label>合同编号 ：</label><form:input path="contract_id" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">
				<label>客户编号 ：</label><form:input path="customer_id" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">	
				<label>客户名称 ：&nbsp</label><form:input path="user_name" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">	
				<label>账单编号 ：</label><form:input path="bill_id" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div style="width: 100%; float: left; text-align: center;">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
			</div>
		</div>
	</form:form>
	<div style="padding-bottom: 10px;">
		<shiro:hasPermission name="charge:bill:chargeReceipt:exportChargeReceiptBill">
			<input id="exportChargeBillInfo" class="btn btn-primary" type="button" value="按月级别导出" onclick="exportChargeBillInfo()"/>
			<input id="exportChargeReceiptBill" class="btn btn-primary" type="button" value="按日级别导出" onclick="exportChargeReceiptBill()"/>
			<input id="exportChargeReceiptDetailPDF" class="btn btn-primary" type="button" value="技术服务费详细PDF导出" onclick="chargeDetailPDFExportShow()"/>
		</shiro:hasPermission>
	</div>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>所属部门</th>
				<th>批次号</th>
				<th>账单编号</th>
				<th>发生日期</th>
				<th>合同编号</th>
				<th>客户编号</th>
				<th>客户名称</th>
				<th>协同产品编号</th>
				<th>产品名称</th>
				<th>结算类型</th>
				<th>计费模式</th>
				<th>费率(‰)</th>
				<th>计费金额</th>
				<th>技术服务费</th>
				<th>业务数据</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="chargeReceipt">
			<tr>
				<td>${fns:getOfficeNameByCode(chargeReceipt.office_id)}</td>
				<td>${chargeReceipt.batch_no}</td>
				<td>${chargeReceipt.bill_id}</td>
				<td>${chargeReceipt.charge_begin_date}</td>
				<td>${chargeReceipt.contract_id}</td>
				<td>${chargeReceipt.customer_id}</td>
				<td>${chargeReceipt.user_name}</td>
				<td>${chargeReceipt.product_id}</td>
				<td>${chargeReceipt.product_name}</td>
				<td><a href="javascript:void(0)" onclick="viewChargeReceipt('${chargeReceipt.bill_id}', '${chargeReceipt.batch_no}', '${chargeReceipt.id}')">${fns:getDictLabel(chargeReceipt.fix_charge_type, "fix_charge_type", "")}</a></td>
				<td>${chargeReceipt.feemodel_name}</td>
				<td><fmt:formatNumber value="${chargeReceipt.fee_ratio}" pattern="#,###.####"/></td>
				<td><fmt:formatNumber value="${chargeReceipt.charge_amt}" pattern="#,###.####"/></td>
				<td><fmt:formatNumber value="${chargeReceipt.service_charge}" pattern="#,###.####"/></td>
				<td><fmt:formatNumber value="${chargeReceipt.org_amt}" pattern="#,###.####"/></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

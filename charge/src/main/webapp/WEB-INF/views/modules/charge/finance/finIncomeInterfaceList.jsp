<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>财务收入接口管理</title>
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
		// 导出收入接口 
		function exportFinIncomeInterface(){
			var params = $("#searchForm").serialize();
			window.open("${ctx}/charge/bill/export/download?templatekey=financeIncomeData&" + params);
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
		//导出NC收入
	    function exportNCIncomes(){
			top.$.jBox.open("iframe:${ctx}/charge/finance/finIncomeInterface/dateForNCIncomeShow", "导出条件",200,$(top.document).height()-420,{
				buttons:{"关闭":true},submit:function(v, h, f){
					if (v=="ok"){					
				    	return true;
					}
				}, loaded:function(h){
					$(".jbox-content", top.document).css("overflow-y","hidden");
				}
			});
		}
	    //删除收入
	    function deleteIncomeByContractId(){
			top.$.jBox.open("iframe:${ctx}/charge/finance/finIncomeInterface/contractIdForDeleteShow", "删除收入",200,$(top.document).height()-420,{
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
		<li class="active"><a href="${ctx}/charge/finance/finIncomeInterface/">财务收入接口列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="finSummaryForm" action="${ctx}/charge/finance/finIncomeInterface/" method="post" class="breadcrumb form-search" style="height: 150px;">
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
				<label class="control-label" for="name">推送状态 ：</label>
				<form:select path="update_flag" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('update_flag')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">费用类型 ：</label>
				<form:select path="payment_type" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('payment_type')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div style="width: 100%; float: left; text-align: center;">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
			</div>
		</div>
	</form:form>
	<div style="padding-bottom: 10px;">
		<shiro:hasPermission name="charge:finance:finIncomeInterface:exportFinIncomeInterface">
			<input id="exportFinIncomeInterface" class="btn btn-primary" type="button" value="导出明细" onclick="exportFinIncomeInterface()"/>
			<input id="exportNCIncomes" class="btn btn-primary" type="button" value="导出NC获取记录" onclick="exportNCIncomes()"/>
		</shiro:hasPermission>
		<shiro:hasPermission name="charge:finance:finIncomeInterface:delete">
			<input id="deleteIncomeByContractId" class="btn btn-primary" type="button" value="按合同号删除记录" onclick="deleteIncomeByContractId()"/>
		</shiro:hasPermission>
	</div>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th width="10%">批次号</th>
				<th width="10%">唯一索引</th>
				<th width="8%">所属部门</th>
				<th width="12%">合同编号</th>
				<th width="12%">产品编号</th>
				<th width="10%">产品名称</th>
				<th width="10%">计费开始日期</th>
				<th width="10%">计费结束日期</th>
				<th width="10%">费用类型</th>
				<th width="10%">财务累计收入</th>
				<th width="10%">收入增量</th>
				<th width="10%">推送状态</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="finSummaryForm">
		
			<tr>
				<td <c:if test="${finSummaryForm.update_flag=='1'}">style="background-color: #999; "</c:if>>${finSummaryForm.batch_no}</td>
				<td <c:if test="${finSummaryForm.update_flag=='1'}">style="background-color: #999; "</c:if> style="word-break: break-all; word-wrap:break-word;">${finSummaryForm.detailid}</td>
				<td <c:if test="${finSummaryForm.update_flag=='1'}">style="background-color: #999; "</c:if>>${fns:getOfficeNameByCode(finSummaryForm.office_id)}</td>
				<td <c:if test="${finSummaryForm.update_flag=='1'}">style="background-color: #999; "</c:if>>${finSummaryForm.contract_id}</td>
				<td <c:if test="${finSummaryForm.update_flag=='1'}">style="background-color: #999; "</c:if>>${finSummaryForm.product_id}</td>
				<td <c:if test="${finSummaryForm.update_flag=='1'}">style="background-color: #999; "</c:if>>${finSummaryForm.productname}</td>
				<td <c:if test="${finSummaryForm.update_flag=='1'}">style="background-color: #999; "</c:if>>${finSummaryForm.charge_begin_date}</td>
				<td <c:if test="${finSummaryForm.update_flag=='1'}">style="background-color: #999; "</c:if>>${finSummaryForm.charge_end_date}</td>
				<td <c:if test="${finSummaryForm.update_flag=='1'}">style="background-color: #999; "</c:if>>${fns:getDictLabel(finSummaryForm.payment_type, "payment_type", "")}</td>
				<td <c:if test="${finSummaryForm.update_flag=='1'}">style="background-color: #999; "</c:if>><fmt:formatNumber value="${finSummaryForm.total_finance_income}" pattern="#,###.##"/></td>
				<td <c:if test="${finSummaryForm.update_flag=='1'}">style="background-color: #999; "</c:if>><fmt:formatNumber value="${finSummaryForm.change_income}" pattern="#,###.##"/></td>
				<td <c:if test="${finSummaryForm.update_flag=='1'}">style="background-color: #999; "</c:if>>${fns:getDictLabel(finSummaryForm.update_flag, 'update_flag', '')}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

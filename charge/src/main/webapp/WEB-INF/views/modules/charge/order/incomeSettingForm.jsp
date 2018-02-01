<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<html>
<head>
	<meta name="decorator" content="default"/>
	<title>计费合同管理</title>
	
	<style type="text/css">
		.form-horizontal .control-label { float: none; width: 120px; }
		.form_tr_div { width: 350px; float: left; margin-bottom: 8px; padding-bottom: 8px;}
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
		.table_select{width:165px;}
		
		
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
		
		.form-actions {text-align: center; background: none; border-top: none; padding: 0;}
		.span{background-color:#fc0;display:-moz-inline-box;display:inline-block;width:150px;}
	</style>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		function getOrderIncomeList() {
			var incomeSettingArray = [];
			var trList = $("#body_incomeRatio").children("tr");
			if(trList!=null){
			for (var k = 0; k < trList.length; k++) {
				var incomeSetting = {};
				incomeSetting.id=$(trList[k]).find("input[name='id']").val();
				incomeSetting.payment_type=$(trList[k]).find("select[name='payment_type']").val();
				incomeSetting.income_begin_date=$(trList[k]).find("input[name='income_begin_date']").val();
				incomeSetting.income_end_date=$(trList[k]).find("input[name='income_end_date']").val();
				incomeSetting.split_amount=$(trList[k]).find("input[name='split_amount']").val();
				incomeSetting.split_ratio=$(trList[k]).find("input[name='split_ratio']").val();
				incomeSettingArray.push(incomeSetting);
			}
			}
			return incomeSettingArray;
		}
		function submitOrderIncome(){
			var moneyReg = /^(\d{1,2}(\.\d{1,3})?|100(\.0{1,3})?)$/;
			var splitRatioList = $("#orderDiv").find("input[name=split_ratio]");
			var msgStr1 = "";
			var incomeArray = getOrderIncomeList();
			for(var i=0;i<splitRatioList.length;i++){
				if($(splitRatioList[i]).val()==""||!moneyReg.test($(splitRatioList[i]).val())){
					msgStr1 = "拆分比例存在空值或格式不是0—100的数字,";
					break;
				}
			}
			top.$.jBox.confirm(msgStr1+"保存设置吗?","提示:",function(v, h, f){
				if (v === 'ok') {
					var param = "contract_id=" + "${incomeSettingForm.contract_id}" + "&jsonOrderIncomeSetting=" + obj2string(incomeArray);
					var url = "${ctx}/charge/order/orderInfo/submitOrderIncome?";
					var data = ajaxFunction(url,param);
					if(data==undefined){
						return;
					}
					if(data.result=="success"){
						top.$.jBox.confirm(data.message,"提示:",function(){
							<c:if test="${syncContract.reporttype_id == '10572' || syncContract.reporttype_id == '10573'}">
							window.location.href ="${ctx}/charge/income/chargeIncomePeriodInterface/form?contract_id=${incomeSettingForm.contract_id}";
							</c:if>
							<c:if test="${syncContract.reporttype_id == '10571'}">
				        	window.location.href ="${ctx}/charge/order/orderInfo/incomeSettingForm?contract_id=${incomeSettingForm.contract_id}&customer_id=${incomeSettingForm.customer_id}&hs_customername=${incomeSettingForm.hs_customername}";
				        	</c:if>
						});
					}else{
						top.$.jBox.warning(data.message);
					}
				} else if (v == 'cancel'){
					top.$.jBox.tip("cancel", 'info');
	            }
			},function(){
			});
			
		}
		//********  obj2string   **********//
		function obj2string(o){
			var r=[];
			if(typeof o=="string"){
		    	return "\""+o.replace(/([\"\\])/g,"\\$1").replace(/(\n)/g,"\\n").replace(/(\r)/g,"\\r").replace(/(\t)/g,"\\t")+"\"";
			}
			if(typeof o=="object"){
		    	if(!o.sort){
		        	for(var i in o){
		            	r.push('"'+i+'":'+obj2string(o[i]));
		        	}
		        	if(!!document.all&&!/^\n?function\s*toString\(\)\s*\{\n?\s*\[native code\]\n?\s*\}\n?\s*$/.test(o.toString)){
		            	r.push("toString:"+o.toString.toString());
		        	}
		        	r="{"+r.join()+"}";
		    	}else{
		        	for(var i=0;i<o.length;i++){
		            	r.push(obj2string(o[i]))
		        	}
		        	r="["+r.join()+"]";
		    	}
		    	return r;
			}
			return o.toString();
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
			
		function backBtn(){
			window.location.href="${ctx}/charge/order/orderInfo/";
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/charge/order/orderInfo/">计费合同列表</a></li>
		<li class="active">
		<a href="${ctx}/charge/order/orderInfo/incomeSettingForm?contract_id=${incomeSettingForm.contract_id}&customer_id=${incomeSettingForm.customer_id}&hs_customername=${incomeSettingForm.hs_customername}">合同收入设置</a>
		</li>
	</ul><br/>
	
	<form:form id="searchForm" modelAttribute="incomeSettingForm" action="${ctx}/charge/order/orderInfo/save" method="post" class="form-horizontal" enctype="application/x-www-form-urlencoded">
		
		<tags:message content="${message}"/>
		<div class="control-group">
			<div class="form_tr_div">
				<label class="control-label" for="name">协同合同编号:</label>
				<form:input path="contract_id" htmlEscape="false" maxlength="200" class="required" style="width:150px" onclick="contractChooserShow(1)"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">协同客户号:</label>
				<form:input path="customer_id" htmlEscape="false" maxlength="200" class="required" style="width:150px" readonly="true"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">客户名称:</label>
				<form:input path="hs_customername" htmlEscape="false" maxlength="500" class="required" style="width:200px" readonly="true"/>
			</div>
		</div>
		</form:form>
		<div class="form-actions">
			<shiro:hasPermission name="charge:order:orderInfo:edit">
				<input id="btnSubmit" class="btn btn-primary" type="button" value="保存设置" onclick="submitOrderIncome()"/>&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="backBtn();"/>
		</div>
		<div id="orderDiv" style="float: left; width:1160px;overflow-x: auto;overflow-y: hidden;">
			<table id="orderAddTable" class="table table-striped table-bordered table-condensed" style="margin-bottom: 0;"/>
				<thead><tr><th>收入开始日期</th><th>收入结束日期</th><th>固定费用类型</th><th>协同销售产品编号</th><th>产品名称</th><th>拆分金额</th><th>拆分比例(%)</th></thead>
				<tbody id="body_incomeRatio">
				<c:forEach items="${orderIncomeRes}" var="orderIncomeRes">
					<tr>
						<td style="display:none"><input type="text" name="id" value="${orderIncomeRes.id}"></td>
						<td><input type="text" name="income_begin_date" value="${orderIncomeRes.income_begin_date}" class="Wdate" onFocus="WdatePicker()"/></td>
						<td><input type="text" name="income_end_date" value="${orderIncomeRes.income_end_date}" class="Wdate" onFocus="WdatePicker()"/></td>
						<td><select name="payment_type" disabled="disabled"><option value="">${fns:getDictLabel(orderIncomeRes.payment_type, 'payment_type', '')}</option></select></td>
						<td><span name="product_id"/>${orderIncomeRes.product_id}</td>
						<td><span name="product_name"/>${orderIncomeRes.product_name}</td>
						<td><input type="text" name="split_amount" onMouseOver="this.title=this.value;" value="${orderIncomeRes.split_amount}" class="table_input_num"/></td>
						<td align="right"><input type="text" name="split_ratio" onMouseOver="this.title=this.value;" value="${orderIncomeRes.split_ratio}" class="table_input_num"/></td>
					</tr>
				</c:forEach>
			</tbody>
			</table>
		</div>
		
</body>
</html>


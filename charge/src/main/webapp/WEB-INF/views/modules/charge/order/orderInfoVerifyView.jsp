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
		
		
		.table-condensed .table_input_string{width:98px;}
		.table-condensed .table_input_num{width:94px;}
		
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
	</style>
	<script src="/charge/static/js/charge/chargeModelCommon.js" type="text/javascript"></script>
	<script src="/charge/static/js/charge/orderTable.js" type="text/javascript"></script>
	
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate();
			
			$("#orderAddDiv").append('${html}');
			if($("div[name=combineDiv]") != null){
				combineIndex = $("div[name=combineDiv]").length;
			}
			$("#termination_status").attr("readonly", true);
			
			$("select").attr("disabled", true);
			$("input").attr("disabled", true);
			$("textarea").attr("disabled", true);
			$("tr.detailButton").remove();
			$("th.detailButton").remove();
			$("td.detailButton").remove();
			$("input.detailButton").remove();
			$("a.table_button_alink").remove();
			
			$("#id").attr("disabled", false);
			$("#btnSubmitPass").attr("disabled", false);
			$("#btnSubmitReject").attr("disabled", false);
			$("#btnCancel").attr("disabled", false);
			
		});
		
		function submitVerifyOrderPass(){
			
			top.$.jBox.confirm("确定要提交吗?", "确认", function(v, h, f){
				if (v === 'ok') {
					$("#is_verify").val("1");
					$("#addOrderForm").submit();
				} else if (v == 'cancel'){
					top.$.jBox.tip("cancel", 'info');
	            }
	    		return true;
			});
			
		}
		
		function submitVerifyOrderReject(id){
			var html = "<div class='content'><textarea id='rejectReason' name='rejectReason' rows='5' /></textarea></div>"; 
			var submit = function (v, h, f) { 
				var rejectReason="";
				 if (f.rejectReason != '') { 
				    	//设置备注
				    	rejectReason=f.rejectReason;
				    }
				var status="1";
				top.$.jBox.confirm("确定要提交吗?", "确认", function(v, h, f){
					if (v === 'ok') {
			    		var url="${ctx}/charge/order/orderInfo/reject?id="+id+"&rejectReason="+rejectReason;
			    		
			    		var data = ajaxFunction(url);
			    		if (data.result == "success") {
			    			top.$.jBox.alert(data.message);
			    			//刷新页面
			    			window.location.href="${ctx}/charge/audit/orderInfoAudit/";
			    		}
			    		
					} else if (v == 'cancel'){
						top.$.jBox.tip("cancel", 'info');
		            }
		    		return true;
				});
			}; 
			 
			top.$.jBox(html,{ title: "审核不通过备注",width: 220,height: 200, submit: submit });
		}
		
		function backBtn(){
			window.location.href="${ctx}/charge/audit/orderInfoAudit/";
		}
		
		
		function tab(pid){
			if(pid == "orderAddDiv"){
			    document.getElementById("orderAddDiv").style.display="block";
			    document.getElementById("orderDiv").style.display="none";
			    $("#orderLi").addClass("active");
			    $("#incomeLi").removeClass("active");
			 }else if(pid == "orderDiv"){
				 document.getElementById("orderDiv").style.display="block";
				 document.getElementById("orderAddDiv").style.display="none";
				 $("#incomeLi").addClass("active");
				 $("#orderLi").removeClass("active");
			 }
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
		
	</script>
	
	
</head>
<body>
	<ul class="nav nav-tabs" id="form_ul">
		<li class="active"><a>计费合同审核详情</a></li>
	</ul><br/>
	
	<form:form id="addOrderForm" name="addOrderForm" modelAttribute="orderInfoForm" action="${ctx}/charge/order/orderInfo/verify" method="post" class="form-horizontal" enctype="application/x-www-form-urlencoded">
		<form:hidden path="id"/>
		<shiro:hasPermission name="charge:audit:orderInfoAudit:verify">
		<input type="hidden" id="is_verify" name="is_verify">
		</shiro:hasPermission>
		<form:hidden path="updateDate"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<div class="form_tr_div">
				<label class="control-label" for="classify">所属部门:</label>
				<tags:treeselect id="office" name="office_id" value="${office.code}" labelName="office_name" labelValue="${office.name}" 
					title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" idVal="code"/>
			</div>
			
			<div class="form_tr_div">
				<label class="control-label" for="name">协同合同编号:</label>
				<form:input path="contract_id" htmlEscape="false" maxlength="200" class="required" style="width:150px" onclick="contractChooserShow(1)"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">支付周期:</label>
				<form:select path="payment_cycle" class="input-medium">
					<form:options items="${fns:getDictList('payment_cycle')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">协同客户号:</label>
				<form:input path="customer_id" htmlEscape="false" maxlength="200" class="required" style="width:150px" readonly="true"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">客户名称:</label>
				<form:input path="hs_customername" htmlEscape="false" maxlength="200" class="required" style="width:150px" readonly="true"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">销售经理1:</label>
				<form:input path="customermanagername" htmlEscape="false" maxlength="200" class="required" style="width:150px" readonly="true"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">销售经理2:</label>
				<form:input path="customermanager2name" htmlEscape="false" maxlength="200" class="required" style="width:150px" readonly="true"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">填报类型:</label>
				<input id="reporttype_id" name="reporttype_id" style="width:150px" readonly="readonly" type="text" value="${fns:getDictLabel(orderInfoForm.reporttype_id,"synccontract_reporttype","")}">
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">收入来源:</label>
				<form:select path="income_source" class="input-medium">
					<form:options items="${fns:getDictList('income_source')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">缴费日期:</label>
				<form:input path="pay_deadline" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">框架合同编号:</label>
				<form:input path="frame_contract_id" htmlEscape="false" maxlength="200" class="required" style="width:150px" onclick="contractChooserShow(2)"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">是否允许发送邮件:</label>
				<form:select path="is_send" class="input-medium">
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">合同签订日期:</label>
				<form:input path="signeddate" value="${fn:substring(orderInfoForm.signeddate,0,10)}" htmlEscape="false" maxlength="200" class="required" style="width:150px" readonly="true"/>
			</div>
			<div class="form_tr_div" name="orderDate" style="height: 30px;">
				<label class="control-label" for="name">合同开始日期:</label>
				${fn:substring(orderInfo.order_begin_date,0,10)}
			</div>
			<div class="form_tr_div" name="orderDate" style="height: 30px;">
				<label class="control-label" for="name">合同结束日期:</label>
				${fn:substring(orderInfo.order_end_date,0,10)}
			</div>
			<div class="form_tr_div" style="height: 30px;">
				<label class="control-label" for="name">合同状态:</label>
				${fns:getDictLabel(orderInfo.order_status,"order_status","")}
			</div>
			<div class="form_tr_div" style="height: 30px;">
				<label class="control-label" for="name">创建人:</label>
				${orderInfo.createBy.name}
			</div>
			<div class="form_tr_div" style="height: 30px;">
				<label class="control-label" for="name">创建时间:</label>
				${orderInfo.createDate}
			</div>
			<div class="form_tr_div" style="height: 30px;">
				<label class="control-label" for="name">修改人:</label>
				${orderInfo.updateBy.name}
			</div>
			<div class="form_tr_div" style="height: 30px;">
				<label class="control-label" for="name">更新时间:</label>
				${orderInfo.updateDate}
			</div>
			<div class="form_tr_div2">
				<label class="control-label" for="remarks">备注:</label>
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge"/>
			</div>
			
			<div name="orderValidation_1" class="orderValidation">
				<div class="orderValidation_text">
					<c:forEach items="${orderInfoForm.orderValidates}" var="orderValidate" varStatus="status">
						<P><span>${status.index + 1}.</span><em>${orderValidate.reason}</em></P>
					</c:forEach>
				</div>
			</div>	
		</div>
		</form:form>
		<div class="form-actions">
			<shiro:hasPermission name="charge:audit:orderInfoAudit:verify">
				<c:if test="${orderInfoForm.id != null}">  
				<input id="btnSubmitPass" class="btn btn-primary" type="button" value="&nbsp;&nbsp;审核通过 &nbsp;&nbsp;" onclick="submitVerifyOrderPass()"/>&nbsp;&nbsp;&nbsp;&nbsp;
				<input id="btnSubmitReject" class="btn btn-primary" type="button" value="&nbsp;&nbsp;审核不通过&nbsp;&nbsp;" onclick="submitVerifyOrderReject('${orderInfo.id}')"/>&nbsp;&nbsp;&nbsp;&nbsp;
				</c:if>
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="&nbsp;&nbsp;返 &nbsp;回&nbsp;&nbsp;" onclick="backBtn();"/>
		</div>
		
		
		<div id="tabNav">
			<ul class="nav nav-tabs" id="form_ul">
				<li id="orderLi" class="active"><a onclick="tab('orderAddDiv')">合同组合信息</a></li>
			   	<li id="incomeLi" ><a onclick="tab('orderDiv')">收入设置信息</a></li>
			</ul>
		</div>
		<div id="tab">
			<div id="orderAddDiv" style="float: left; width:1160px;overflow-x: auto;overflow-y: hidden;display:block;">
				
			</div>
			<div id="orderDiv" style="float: left; width:1160px;overflow-x: auto;overflow-y: hidden; display:none;">
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
		</div>
</body>
</html>


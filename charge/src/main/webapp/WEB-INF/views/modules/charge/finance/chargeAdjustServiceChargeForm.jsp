<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>技术服务费调账管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate();
		});
		function submitChargeAdjustServiceCharge(){
			$("#inputForm").submit();
		}
		// 调账技术服务费选择器
		function combineChooserShow(type){
			top.$.jBox.open("iframe:${ctx}/charge/common/orderCombineChooser", "组合选择器",700,$(top.document).height()-240,{
				buttons:{"确定选择":"ok", "关闭":true},submit:function(v, h, f){
					if (v=="ok"){
						var orderIncome = h.find("iframe")[0].contentWindow.getOrderCombine();
						$("#inputForm").find("input[name='office_id']").val(orderIncome.office_id);
						$("#inputForm").find("input[name='office_name']").val(orderIncome.office_name);
						$("#inputForm").find("input[name='contract_id']").val(orderIncome.contract_id);
						$("#inputForm").find("input[name='combine_id']").val(orderIncome.combine_id);
						$("#inputForm").find("input[name='combine_begin_date']").val(orderIncome.combine_begin_date);
						$("#inputForm").find("input[name='combine_end_date']").val(orderIncome.combine_end_date);						
						$("#inputForm").find("input[name='product_names']").val(orderIncome.product_names);
				    	return true;
					}
				}, loaded:function(h){
					$(".jbox-content", top.document).css("overflow-y","hidden");
				}
			});
		}
	</script>
	<style type="text/css">
		.form-horizontal .control-label { float: none; width: 120px; }
		.form_tr_div { width: 350px; float: left; margin-bottom: 8px; padding-bottom: 8px;}
		.form_tr_div2 { width: 100%; float: left; margin-bottom: 8px; padding-bottom: 8px;}
		.Wdate{width: 150px;}
	</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/charge/finance/chargeAdjustServiceCharge/">技术服务费调账列表</a></li>
		<li class="active"><a href="${ctx}/charge/finance/chargeAdjustServiceCharge/form?id=${chargeAdjustServiceCharge.id}">技术服务费调账<shiro:hasPermission name="charge:finance:chargeAdjustServiceCharge:edit">${not empty chargeAdjustServiceCharge.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="charge:finance:chargeAdjustServiceCharge:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	
	<form:form id="inputForm" modelAttribute="chargeAdjustServiceChargeForm" action="${ctx}/charge/finance/chargeAdjustServiceCharge/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="combine_id"/>
		<form:hidden path="updateDate"/>
		<tags:message content="${message}"/>		
		<div class="control-group">
			<shiro:hasPermission name="charge:finance:chargeAdjustServiceCharge:edit">
			<div class="form_tr_div2">
				<input id="chooseCombine" class="btn btn-primary" type="button" value="选择组合" onclick="combineChooserShow()"/>&nbsp;<br>
			</div>
			</shiro:hasPermission>
			<div class="form_tr_div">
				<label class="control-label" for="classify">所属部门 ：</label>
				<input id="office_name" name="office_name" readonly="readonly" type="text" value="${fns:getOfficeNameByCode(chargeAdjustServiceChargeForm.office_id)}" maxlength="200">
				<form:hidden path="office_id"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="contract_id">协同合同编号 ：</label>
				<form:input path="contract_id" htmlEscape="false" maxlength="200" readonly="true"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="combine_begin_date">组合开始日期 ：</label>
				<form:input path="combine_begin_date" htmlEscape="false" maxlength="200" readonly="true"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="combine_end_date">组合结束日期 ：</label>
				<form:input path="combine_end_date" htmlEscape="false" maxlength="200" readonly="true"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="product_id">销售产品名称 ：</label>
				<form:input path="product_names" htmlEscape="false" maxlength="200" readonly="true"/>
			</div>			
		</div>
		<div class="control-group">
			<div class="form_tr_div">
				<label class="control-label" for="adjust_date">调账日期 ：</label>
				<form:input id="adjust_date" class="Wdate" type="text" path="adjust_date" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})"/>
			</div>
		</div>
		<div class="control-group">
			<div class="form_tr_div">
				<label class="control-label" for="adjust_amt">调账金额 ：</label>
				<form:input path="adjust_amt" htmlEscape="false" maxlength="200"/>
			</div>
		</div>
		<div class="control-group">	
			<div class="form_tr_div2">
				<label class="control-label" for="remarks">备注 ：</label>
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge"/>
			</div>
		</div>
	</form:form>
	<div class="form-actions">
		<shiro:hasPermission name="charge:finance:chargeAdjustServiceCharge:edit">
			<input id="btnSubmit" class="btn btn-primary" type="button" value="保 存" onclick="submitChargeAdjustServiceCharge()"/>&nbsp;
		</shiro:hasPermission>
		<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
	</div>
	
</body>
</html>

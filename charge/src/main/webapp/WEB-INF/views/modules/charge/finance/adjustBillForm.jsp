<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>调账管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#adjustBillForm").validate();
		});
		//协同合同号选择器
		function contractChooserShow(type){
			top.$.jBox.open("iframe:${ctx}/charge/common/syncContractChooser", "协同合同选择器",810,$(top.document).height()-240,{
				buttons:{"确定选择":"ok", "关闭":true},submit:function(v, h, f){
					if (v=="ok"){
						var contractInfo = h.find("iframe")[0].contentWindow.getContractInfo();
						if(type==1){
							$("#adjustBillForm").find("input[name='contract_id']").val(contractInfo.contractid);
							$("#adjustBillForm").find("input[name='customer_id']").val(contractInfo.customerid);
							$("#adjustBillForm").find("input[name='user_name']").val(contractInfo.customername);
						}else if(type==2){
							$("#adjustBillForm").find("input[name='frame_contract_id']").val(contractInfo.contractid);
						}
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
		<li><a href="${ctx}/charge/finance/adjustBill/">调账管理列表</a></li>
		<li class="active"><a href="${ctx}/charge/finance/adjustBill/form?id=${adjustBillForm.id}">调账管理<shiro:hasPermission name="charge:finance:adjustBill:edit">${not empty adjustBillForm.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="charge:finance:adjustBill:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	
	<form:form id="adjustBillForm" modelAttribute="adjustBillForm" action="${ctx}/charge/finance/adjustBill/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label" for="name">账单日期 ：</label>
			<div class="controls">
				<form:input class="required" type="text" path="adjust_date" htmlEscape="false" maxlength="200" onfocus="WdatePicker()"/>
			</div>
			<label class="control-label" for="name">合同编号 ：</label>
			<div class="controls">
				<form:input path="contract_id" htmlEscape="false" maxlength="200" class="required" onclick="contractChooserShow(1)"/>
			</div>
			<label class="control-label" for="name">客户编号 ：</label>
			<div class="controls">
				<form:input path="customer_id" htmlEscape="false" maxlength="200" class="required"/>
			</div>
			<label class="control-label" for="name">客户名称 ：</label>
			<div class="controls">
				<form:input path="user_name" htmlEscape="false" maxlength="200" class="required"/>
			</div>
			<label class="control-label" for="name">调整金额 ：</label>
			<div class="controls">
				<form:input path="adjust_balance" htmlEscape="false" maxlength="200" class="required"/>
			</div>
			<label class="control-label" for="name">调整原因 ：</label>
			<div class="controls">
				<form:textarea path="remark" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="charge:finance:adjustBill:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>

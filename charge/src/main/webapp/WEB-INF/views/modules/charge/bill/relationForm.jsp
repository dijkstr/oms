<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>账单联系人管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#relationForm").validate();
		});
		//协同合同号选择器
		function contractChooserShow(type){
			top.$.jBox.open("iframe:${ctx}/charge/common/syncContractChooser", "协同合同选择器",810,$(top.document).height()-240,{
				buttons:{"确定选择":"ok", "关闭":true},submit:function(v, h, f){
					if (v=="ok"){
						var contractInfo = h.find("iframe")[0].contentWindow.getContractInfo();
						if(type==1){
							$("#relationForm").find("input[name='contract_id']").val(contractInfo.contractid);
							$("#relationForm").find("input[name='customer_id']").val(contractInfo.customerid);
							$("#relationForm").find("input[name='user_name']").val(contractInfo.customername);
							$("#relationForm").find("input[name='office_id']").val(contractInfo.companyid);
						}else if(type==2){
							$("#relationForm").find("input[name='frame_contract_id']").val(contractInfo.contractid);
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
		<li><a href="${ctx}/charge/bill/relation/">账单联系人列表</a></li>
		<li class="active"><a href="${ctx}/charge/bill/relation/form?id=${relationForm.id}">账单联系人<shiro:hasPermission name="charge:bill:relation:edit">${not empty relationForm.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="charge:bill:relation:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	
	<form:form id="relationForm" modelAttribute="relationForm" action="${ctx}/charge/bill/relation/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label" for="contract_id">合同编号 ：</label>
			<div class="controls">
				<form:input path="contract_id" htmlEscape="false" maxlength="200" class="required" onclick="contractChooserShow(1)"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="office_id">所属部门 ：</label>
			<div class="controls">
				<form:input path="office_id" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="customer_id">客户编号 ：</label>
			<div class="controls">
				<form:input path="customer_id" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="user_name">客户名称 ：</label>
			<div class="controls">
				<form:input path="user_name" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="relation_name">联系人名称 ：</label>
			<div class="controls">
				<form:input path="relation_name" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="duties">职务 ：</label>
			<div class="controls">
				<form:input path="duties" htmlEscape="false" maxlength="200"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="mobile_tel">手机号码 ：</label>
			<div class="controls">
				<form:input path="mobile_tel" htmlEscape="false" maxlength="200"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="relation_tel">座机号码 ：</label>
			<div class="controls">
				<form:input path="relation_tel" htmlEscape="false" maxlength="200"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="email">收信人：</label>
			<div class="controls">
				<form:input path="email" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="cc">抄送：</label>
			<div class="controls">
				<form:input path="cc" htmlEscape="false" maxlength="200"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="bcc">密抄：</label>
			<div class="controls">
				<form:input path="bcc" htmlEscape="false" maxlength="200"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="charge:bill:relation:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>

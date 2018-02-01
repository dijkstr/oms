<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>合同到期提醒管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#value").focus();
			$("#inputForm").validate();
		});
		
		function backBtn(){
			window.location.href="${ctx}/charge/order/orderExpireWarning/";
		}
		
		//计费合同号选择器
		function chargeContractChooser(){
			top.$.jBox.open("iframe:${ctx}/charge/common/chargeContractChooser", "计费合同选择器",810,$(top.document).height()-240,{
				buttons:{"确定选择":"ok", "关闭":true},submit:function(v, h, f){
					if (v=="ok"){
						var contractInfo = h.find("iframe")[0].contentWindow.getContractInfo();
						$("#contract_id").val(contractInfo.contract_id);
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
		<li><a href="${ctx}/charge/order/orderExpireWarning">合同到期提醒列表</a></li>
		<li class="active"><a href="${ctx}/charge/order/orderExpireWarning/form?id=${dict.id}">合同到期提醒<shiro:hasPermission name="charge:order:orderExpireWarning:edit">${not empty orderExpireWarningForm.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="charge:order:orderExpireWarning:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	
	<form:form id="inputForm" modelAttribute="orderExpireWarningForm" action="${ctx}/charge/order/orderExpireWarning/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<form:hidden path="updateDate"/>
		<div class="control-group">
			<label class="control-label" for="name">协同合同编号:</label>
			<div class="controls">
				<form:input path="contract_id" htmlEscape="false" maxlength="200" class="required" style="width:150px" onclick="chargeContractChooser()"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="value">邮箱:</label>
			<div class="controls">
				<form:input path="email" htmlEscape="false" maxlength="50" class="required" onMouseOver="this.title=this.value;"/>
				<span style="color:red;">(多个邮箱以逗号隔开)</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="label">需提醒次数:</label>
			<div class="controls">
				<form:input path="warn_times" htmlEscape="false" maxlength="50" class="required digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="description">已提醒次数:</label>
			<div class="controls">
				<form:input path="warn_counter" htmlEscape="false" maxlength="50" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="sort">提醒时机:</label>
			<div class="controls">
				<form:select path="warn_type" class="input-medium">
					<form:options items="${fns:getDictList('warn_type')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="charge:order:orderExpireWarning:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="backBtn();"/>
		</div>
	</form:form>
</body>
</html>
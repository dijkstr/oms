<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>请假详细</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
		function auditPass(isPass) {
			top.$.jBox.confirm("确认提交数据？","系统提示",function(v,h,f){
			    if (v == 'ok') {
					$("#pass").val(isPass);
					$("#inputForm").submit();
			    }
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/charge/audit/orderInfoAudit/">计费合同审核列表</a></li>
		<shiro:hasPermission name="charge:audit:orderInfoAudit:view"><li class="active"><a href="${ctx}/charge/audit/orderInfoAudit/detail?contractProcessKey=${orderInfoAudit.contract_process_key}">计费合同审核详情</a></li></shiro:hasPermission>
	</ul>
	<form class="form-horizontal">
		<div class="control-group">
			<label class="control-label">合同流程号：</label>
			<div class="controls">
				${orderInfoAudit.contract_process_key }
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">创建时间：</label>
			<div class="controls">
				${fn:substring(orderInfoAudit.createDate,0,19)}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				${orderInfoAudit.remarks}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">当前状态：</label>
			<div class="controls">
				${fns:getDictLabel(orderInfoAudit.processStatus,"order_status","")}
			</div>
		</div>
	</form>
	
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
			<th>操作人</th>
			<th>操作时间</th>
			<th>审核人</th>
			<th>审核时间</th>
			<th>审核备注</th>
		</tr></thead>
		<tbody>
			<c:forEach items="${orderInfoAuditDetails}" var="orderInfoAuditDetail">
				<tr>
					<td>${fns:getUserById(orderInfoAuditDetail.operate_user_id).name}</td>
					<td><fmt:formatDate value="${orderInfoAuditDetail.operate_date}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td>${fns:getUserById(orderInfoAuditDetail.audit_user_id).name}</td>
					<td><fmt:formatDate value="${orderInfoAuditDetail.audit_date}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td>${orderInfoAuditDetail.comments }</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>

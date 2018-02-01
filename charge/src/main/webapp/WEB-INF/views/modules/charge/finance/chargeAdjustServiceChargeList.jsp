<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>技术服务费调账管理</title>
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
		// 导出技术服务费设置 
		function serviceChargeAdjustExport(){
			var params = $("#searchForm").serialize();
			window.open("${ctx}/charge/bill/export/download?templatekey=serviceChargeAdjustList&" + params);
		}
	</script>
	<style type="text/css">
		.form_tr_div { width: 33%; height: 34px; float: left; margin-bottom: 5px; padding-bottom: 4px;}
		.form_tr_div .input-small { width: 140px;}
		.select2-container.input-medium { margin-left: -4px;}
		.form_tr_div label{width: 28%;}
		
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
		/*对多选框样式进行修改 */
		.multi_select {
		 		max-height: 60px !important;
		 		overflow-y: scroll !important;
		 	}	
	</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/charge/finance/chargeAdjustServiceCharge/">技术服务费调账列表</a></li>
		<shiro:hasPermission name="charge:finance:chargeAdjustServiceCharge:edit"><li><a href="${ctx}/charge/finance/chargeAdjustServiceCharge/form">技术服务费调账添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="chargeAdjustServiceChargeForm" action="${ctx}/charge/finance/chargeAdjustServiceCharge/" method="post" class="breadcrumb form-search" style="height:100px;">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="control-group">
			<div class="form_tr_div">
				<label>所属部门 ：</label>
				<tags:treeselect id="office" name="office_id" value="${office.code}" labelName="office_name" labelValue="${office.name}" 
						title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" idVal="code"/>
			</div>
			<div class="form_tr_div">
				<label>协同合同编号 ：</label><form:input path="contract_id" htmlEscape="false" class="input-small"/>
			</div>
			<div class="form_tr_div">
				<label>调账编号：</label><form:input path="id" htmlEscape="false" class="input-small"/>
			</div>
			<div style="width: 100%; float: left; text-align: center; margin-top:20px;">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
			</div>
		</div>
	</form:form>
	<div style="padding-bottom: 10px;">
		<input id="chargeAdjustServiceChargeForm" class="btn btn-primary" type="button" value="导出技术服务费调账" onclick="serviceChargeAdjustExport()"/>
	</div>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th width="7%">调账编号</th>
				<th width="5%">是否有效</th>
				<th width="5%">所属部门</th>
				<th width="7%">协同合同id</th>
				<th width="7%">组合开始日期</th>
				<th width="7%">组合结束日期</th>
				<th width="10%">协同销售产品名称</th>
				<th width="7%">调账日期</th>
				<th width="10%">调账金额</th>
				<th width="10%">备注</th>
				<shiro:hasPermission name="charge:finance:chargeAdjustServiceCharge:edit"><th width="10%">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="chargeAdjustServiceCharge">
			<tr>
				<td style="word-break:break-all;word-wrap:break-word">${chargeAdjustServiceCharge.id}</td>
				<td>${chargeAdjustServiceCharge.status}</td>
				<td>${fns:getOfficeNameByCode(chargeAdjustServiceCharge.office_id)}</td>
				<td>${chargeAdjustServiceCharge.contract_id}</td>
				<td>${chargeAdjustServiceCharge.combine_begin_date}</td>
				<td>${chargeAdjustServiceCharge.combine_end_date}</td>
				<td style="word-break:break-all;word-wrap:break-word">${chargeAdjustServiceCharge.product_names}</td>
				<td>${chargeAdjustServiceCharge.adjust_date}</td>
				<td><fmt:formatNumber value="${chargeAdjustServiceCharge.adjust_amt}" pattern="#,###.####"/></td>
				<td style="word-break:break-all;word-wrap:break-word">${chargeAdjustServiceCharge.remarks}</td>
				<shiro:hasPermission name="charge:finance:chargeAdjustServiceCharge:edit">
				<td>
    				<a href="${ctx}/charge/finance/chargeAdjustServiceCharge/form?id=${chargeAdjustServiceCharge.id}" class="table_button_alink">修改</a>
					<a href="${ctx}/charge/finance/chargeAdjustServiceCharge/delete?id=${chargeAdjustServiceCharge.id}" onclick="return confirmx('确认要删除该技术服务费调账吗？', this.href)" class="table_button_alink">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

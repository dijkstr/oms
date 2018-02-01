<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>合同管理</title>
	<meta name="decorator" content="default"/>
	
	<style type="text/css">
	.form_tr_div { width: 33%; height: 34px; float: left; margin-bottom: 5px; padding-bottom: 4px;}
	.form_tr_div1 { width: 33%; height: 34px; float: left; margin-bottom: 5px; padding-bottom: 4px;}
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
	</style>
	
	
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
		//查看合同详情
		function viewContract(billId){
			var url = "${ctx}/charge/sync/contract/form?con_id=" + billId;
			window.open(url);
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/charge/sync/contract/">协同合同列表</a></li>
		<!--<shiro:hasPermission name="charge:sync:contract:edit"><li><a href="${ctx}/charge/sync/contract/form">协同合同添加</a></li></shiro:hasPermission>-->
	</ul>
	<form:form id="searchForm" modelAttribute="syncContractForm" action="${ctx}/charge/sync/contract/" method="post" class="breadcrumb form-search" style="height:120px;">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="control-group" >
		<div class="form_tr_div">
		<label>协同合同编号：</label><form:input path="contractid" htmlEscape="false"  class="input-small"/>
		</div>
		<div class="form_tr_div">
		<label>所属公司：</label>
		
		<tags:treeselect id="office" name="companyid" value="${office.code}" labelName="office_name" labelValue="${office.name}" 
					title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" idVal="code"/>
		 </div>
		<div class="form_tr_div">
		<label>客户编号：</label><form:input path="customerid" htmlEscape="false"  class="input-small"/>
		</div>
		<div class="form_tr_div">
		<label>客户名称：</label><form:input path="customername" htmlEscape="false"  class="input-small"/>
		</div>
		<div class="form_tr_div">
		<label>计费系统使用：</label>
				<form:select path="usetype" class="input-medium" style="width: 155px;">
					<form:option value="" label="请选择" />
					<form:option value="1" label="是" />
					<form:option value="2" label="否" />
				</form:select>
		</div>
			
		<div class="form_tr_div1">
				<label>计费起点上报类型：</label>
				<form:select path="reporttype_id" class="input-medium" style="width: 155px;">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('reporttype')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
		</div>	
			
			
		<div style="width: 100%; float: left; text-align: center;">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
			</div>
	</div>
	</form:form>

	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		<tr>
		<th>所属公司</th><th>合同编号</th><th>计费系统使用</th><th>客户编号</th><th>客户名称</th><th>合同签订日期</th>
		<th>计费起点上报类型</th>
		</tr>
		</thead>
		<!--<shiro:hasPermission name="charge:sync:contract:edit"><th>操作</th></shiro:hasPermission></tr></thead>-->
		<tbody>
		<c:forEach items="${page.list}" var="syncContract">
			<tr>
				<td>${fns:getOfficeNameByCode(syncContract.companyid)}</td>
				<td>
				<a href="javascript:void(0)" onclick="viewContract(${syncContract.con_id})">
				${syncContract.contractid}
				</a>
				</td>
				
				<td>
				<c:if test="${empty syncContract.orders}">否</c:if>
				<c:if test="${!empty syncContract.orders}">是</c:if>
				</td>
							
				<td>${syncContract.customerid}</td>
				<td>${syncContract.customername}</td>
				<td>${syncContract.signeddate}</td>
				<td>${fns:getDictLabel(syncContract.reporttype_id,"reporttype","")}</td>
				<!-- 
				<shiro:hasPermission name="charge:sync:contract:edit"><td>
    				<a href="${ctx}/charge/sync/contract/form?con_id=${syncContract.con_id}">修改</a>
					<a href="${ctx}/charge/sync/contract/delete?con_id=${syncContract.con_id}" onclick="return confirmx('确认要删除该合同吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
				 -->
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

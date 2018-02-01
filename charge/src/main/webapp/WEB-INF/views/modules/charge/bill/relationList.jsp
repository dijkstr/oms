<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>账单联系人管理</title>
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
			});
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		//查看协同合同详情
		function viewContract(billId){
			var url = "${ctx}/charge/sync/contract/form?contractid=" + billId;
			window.open(url);
		}
		//查看协同客户
		function viewCustomer(customerId){
			var url = "${ctx}/charge/sync/customer/list?customerid=" + customerId;
			window.open(url);
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/charge/bill/relation/">账单联系人列表</a></li>
		<shiro:hasPermission name="charge:bill:relation:edit"><li><a href="${ctx}/charge/bill/relation/form">账单联系人添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="relationForm" action="${ctx}/charge/bill/relation/" method="post" class="breadcrumb form-search" style="height: 108px;">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		
		<div class="control-group" style="float:left;">
			<div class="form_tr_div">
				<label>所属部门 ：</label>
					<tags:treeselect id="office" name="office_id" value="${office.code}" labelName="office_name" labelValue="${office.name}" 
						title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" idVal="code"/>
			</div>
			<div class="form_tr_div">
				<label>合同编号 ：</label><form:input path="contract_id" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">
				<label>联系人名称 ：</label><form:input path="relation_name" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">	
				<label>手机号码 ：</label><form:input path="mobile_tel" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">	
				<label>邮箱地址 ：</label><form:input path="email" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">	
				<label>客户名称 ：</label>&nbsp&nbsp&nbsp<form:input path="user_name" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
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
				<th width="2%">所属部门</th>
				<th width="5%">合同编号</th>
				<th width="5%">客户编号</th>
				<th width="5%">客户名称</th>
				<th width="5%">联系人名称</th>
				<th width="3%">职务</th>
				<th width="3%">手机号码</th>
				<th width="3%">座机号码</th>
				<th width="10%">邮箱</th>
				<shiro:hasPermission name="charge:bill:relation:edit"><th width="7%">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="relationForm">
			<tr>
				<td>${fns:getOfficeNameByCode(relationForm.office_id)}</td>
				<td><a href="javascript:void(0)" onclick="viewContract(encodeURI(encodeURI('${relationForm.contract_id}')))">${relationForm.contract_id}</a></td>
				<td>${relationForm.customer_id}</td>
				<td><a href="javascript:void(0)" onclick="viewCustomer(${relationForm.customer_id})">${relationForm.user_name}</a></td>
				<td>${relationForm.relation_name}</td>
				<td>${relationForm.duties}</td>
				<td>${relationForm.mobile_tel}</td>
				<td>${relationForm.relation_tel}</td>
				<td style="word-wrap:break-word; word-break:break-all;">${relationForm.email}</td>
				<shiro:hasPermission name="charge:bill:relation:edit"><td>
    				<a href="${ctx}/charge/bill/relation/form?id=${relationForm.id}" class="table_button_alink">修改</a>
					<a href="${ctx}/charge/bill/relation/delete?id=${relationForm.id}" onclick="return confirmx('确认要删除该账单联系人吗？', this.href)" class="table_button_alink">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

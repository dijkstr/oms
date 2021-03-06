<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>计费合同管理</title>
	<meta name="decorator" content="default"/>
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
		<li class="active"><a href="${ctx}/charge/order/orderInfo/">计费合同列表</a></li>
		<shiro:hasPermission name="charge:order:orderInfo:edit"><li><a href="${ctx}/charge/order/order/form">计费合同添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="orderInfoForm" action="${ctx}/charge/order/orderInfo/" method="post" class="breadcrumb form-search" style="height:160px;">
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
				<label>协同客户号 ：</label><form:input path="customer_id" htmlEscape="false" class="input-small"/>
			</div>
			<div class="form_tr_div">
				<label>客户名称 ：</label><form:input path="hs_customername" htmlEscape="false" class="input-small"/>
			</div>
			<div class="form_tr_div">
				<label>销售经理 ：</label><form:input path="customermanagername" htmlEscape="false" class="input-small"/>
			</div>
			<div class="form_tr_div">
				<label>即将到期月份：</label>
				<form:select path="expire_month" class="input-medium" style="width: 155px;">
					<form:option value="" label="请选择"/>
					<form:option value="1" label="一个月"/>
					<form:option value="2" label="两个月"/>
					<form:option value="3" label="三个月"/>
				</form:select>
			</div>
			<div class="form_tr_div">
				<label>费用类型：</label>
				<form:select path="payment_type" class="input-medium" style="width: 155px;">
					<form:option value="" label="请选择"/>
					<form:option value="12471" label="一次性费用"/>
					<form:option value="12472" label="不定期"/>
					<form:option value="12473" label="年费"/>
					<form:option value="12474" label="条件延后"/>
					<form:option value="12475" label="技术服务费"/>
				</form:select>
			</div>
			<div class="form_tr_div">
				<label>合同状态 ：</label>
				<form:select path="order_status" class="input-medium" style="width: 155px;">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('order_status')}" itemLabel="label" itemValue="value" htmlEscape="false" />
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
				<th style="width: 5%;">所属部门</th>
				<th style="width: 11%;">协同合同编号</th>
				<th style="width: 5%;">协同客户号</th>
				<th style="width: 15%;">客户名称</th>
				<th style="width: 5%;">销售经理1</th>
				<th style="width: 5%;">销售经理2</th>
				<th style="width: 8%;">开始时间</th>
				<th style="width: 8%;">结束时间</th>
				<th style="width: 5%;">合同状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="orderInfo">
			<tr>
				<td>${fns:getOfficeNameByCode(orderInfo.office_id)}</td>
				<td>
					<a href="javascript:void(0)" onclick="viewContract(encodeURI(encodeURI('${orderInfo.contract_id}')))">${orderInfo.contract_id}</a>
				</td>
				<td>${orderInfo.customer_id}</td>
				<td><a href="javascript:void(0)" onclick="viewCustomer(${orderInfo.customer_id})">${orderInfo.syncCustomer.chinesename}</a></td>
				<td>${orderInfo.syncCustomer.customermanagername}
				</td>
				<td>${orderInfo.syncCustomer.customermanager2name}
				</td>
				<td>${fn:substring(orderInfo.order_begin_date,0,10)}</td>
				<td>${fn:substring(orderInfo.order_end_date,0,10)}</td>
				<td>${fns:getDictLabel(orderInfo.order_status,"order_status","")}</td>
				<td>
					<shiro:hasPermission name="charge:order:orderInfo:edit">
	    				<a href="${ctx}/charge/order/order/form?id=${orderInfo.id}" class="table_button_alink">修改</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

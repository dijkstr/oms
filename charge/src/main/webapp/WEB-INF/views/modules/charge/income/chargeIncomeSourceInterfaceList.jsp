<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>收入来源接口管理</title>
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
		/*对多选框样式进行修改 */
		.multi_select {
	 		max-height: 60px !important;
	 		overflow-y: scroll !important;
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
				$("#income_source").val("").trigger('change');
				$("#send_flag").val("").trigger('change');
			});
			var incomeSourceString = "${chargeIncomeSourceInterfaceForm.income_source}";
			if(incomeSourceString != undefined || incomeSourceString !== null || incomeSourceString !== ""){
				$("#income_source").val(incomeSourceString.split(',')).trigger('change');
			}
			var sendFlagString = "${chargeIncomeSourceInterfaceForm.send_flag}";
			if(sendFlagString != undefined || sendFlagString !== null || sendFlagString !== ""){
				$("#send_flag").val(sendFlagString.split(',')).trigger('change');
			}
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		// 确认待发送状态
		function confirmSend(){
			var selectDatas = document.getElementsByName("checkbox");
			var ids = "";
			var num = 0;
			for(var i = selectDatas.length - 1; i >= 0; i-- ){
	    		if (selectDatas[i].checked){
	    			num += 1;
	    			ids += selectDatas[i].value + ",";
	    		}
			}
			if (num < 1) {
	    		top.$.jBox.alert("请至少选择一条记录。");
				return;
	    	}
			$("#searchForm").attr("action","${ctx}/charge/income/chargeIncomeSourceInterface/confirm");
			$("#ids").val(ids);
			$("#searchForm").submit();
		}
		// 导出收入接口 
		function exportIncomeSourceInterface(){
			var params = $("#searchForm").serialize();
			window.open("${ctx}/charge/bill/export/download?templatekey=exportIncomeSourceInterface&" + params);
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/charge/finance/finIncomeInterface/">财务收入来源接口列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="chargeIncomeSourceInterfaceForm" action="${ctx}/charge/income/chargeIncomeSourceInterface/list" method="post" class="breadcrumb form-search" style="height:150px;">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="ids" name="ids" type="hidden"/>
		<div class="control-group">
			<div class="form_tr_div">
				<label>所属部门 ：</label>
				<tags:treeselect id="office" name="office_id" value="${office.code}" labelName="office_name" labelValue="${office.name}" 
						title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" idVal="code"/>
			</div>
			<div class="form_tr_div">
				<label>协同合同编号 ：</label>
				<form:input path="contract_id" htmlEscape="false" class="input-large"/>
			</div>
			<div class="form_tr_div">
				<label>收入来源 ：</label>
				<form:select path="income_source" multiple="multiple" class="input-large multi_select" placeholder="请选择" >
					<form:options items="${fns:getDictList('income_source')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div class="form_tr_div">
				<label>同步状态 ：</label>
				<form:select path="send_flag" multiple="multiple" class="input-large multi_select" placeholder="请选择" >
					<form:options items="${fns:getDictList('send_flag')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div class="form_tr_div">
				<label>协同客户名称 ：</label>
				<form:input path="hs_customername" htmlEscape="false" class="input-large"/>
			</div>
			<div style="width: 100%; float: left; text-align: center; margin-top:20px;">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
			</div>
		</div>
	</form:form>
	<div style="padding-bottom: 10px;">
		<shiro:hasPermission name="charge:income:chargeIncomeSourceInterface:view">
			<input id="confirmSend" class="btn btn-primary" type="button" value="导出" onclick="exportIncomeSourceInterface()"/>
		</shiro:hasPermission>
		<shiro:hasPermission name="charge:income:chargeIncomeSourceInterface:edit">
			<input id="confirmSend" class="btn btn-primary" type="button" value="确认同步" onclick="confirmSend()"/>
		</shiro:hasPermission>
	</div>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th width="20"></th>
				<th>所属部门</th>
				<th>协同合同编号</th>
				<th>协同客户名称</th>
				<th>收入来源</th>
				<th>同步状态</th>
				<th>创建时间</th>
				<th>创建人</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="bean">
			<tr>
				<td <c:if test="${bean.send_flag=='sent'}">style="background-color: #999; "</c:if>>
				<c:if test="${bean.send_flag == 'create'}">
				<input type="checkbox" name="checkbox" value="${bean.id}">
				</c:if>
				</td>
				<td <c:if test="${bean.send_flag=='sent'}">style="background-color: #999; "</c:if>>${fns:getOfficeNameByCode(bean.office_id)}</td>
				<td <c:if test="${bean.send_flag=='sent'}">style="background-color: #999; "</c:if>>${bean.contract_id}</td>
				<td <c:if test="${bean.send_flag=='sent'}">style="background-color: #999; "</c:if>>${bean.syncContract.syncCustomer.chinesename}</td>
				<td <c:if test="${bean.send_flag=='sent'}">style="background-color: #999; "</c:if>>${fns:getDictLabel(bean.income_source,"income_source","")}</td>
				<td <c:if test="${bean.send_flag=='sent'}">style="background-color: #999; "</c:if>>${fns:getDictLabel(bean.send_flag,"send_flag","")}</td>
				<td <c:if test="${bean.send_flag=='sent'}">style="background-color: #999; "</c:if>>${bean.createDate}</td>
				<td <c:if test="${bean.send_flag=='sent'}">style="background-color: #999; "</c:if>>${bean.createBy.name}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

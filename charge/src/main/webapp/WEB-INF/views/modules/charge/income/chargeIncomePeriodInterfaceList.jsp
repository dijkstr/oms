<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>收入期间接口管理</title>
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
				$("#send_flag").val("").trigger('change');
			});
			var sendFlagString = "${chargeIncomePeriodInterfaceForm.send_flag}";
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
			$("#searchForm").attr("action","${ctx}/charge/income/chargeIncomePeriodInterface/confirm");
			$("#ids").val(ids);
			$("#searchForm").submit();
		}
		// 导出收入接口 
		function exportIncomePeriodInterface(){
			var params = $("#searchForm").serialize();
			window.open("${ctx}/charge/bill/export/download?templatekey=exportIncomePeriodInterface&" + params);
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/charge/finance/finIncomeInterface/">财务收入期间接口列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="chargeIncomePeriodInterfaceForm" action="${ctx}/charge/income/chargeIncomePeriodInterface/list" method="post" class="breadcrumb form-search" style="height:145px;">
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
				<label>同步状态 ：</label>
				<form:select path="send_flag" multiple="multiple" class="input-large multi_select" placeholder="请选择">
					<form:options items="${fns:getDictList('send_flag')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div class="form_tr_div">
				<label>显示问题记录 ：</label>
				<form:select path="showEmptyIncomeDate" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" />
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
		<shiro:hasPermission name="charge:income:chargeIncomePeriodInterface:view">
			<input id="confirmSend" class="btn btn-primary" type="button" value="导出" onclick="exportIncomePeriodInterface()"/>
		</shiro:hasPermission>
		<shiro:hasPermission name="charge:income:chargeIncomePeriodInterface:edit">
			<input id="confirmSend" class="btn btn-primary" type="button" value="确认同步" onclick="confirmSend()"/>
		</shiro:hasPermission>
	</div>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th width="2%"></th>
				<th width="5%">所属部门</th>
				<th width="7%">明细行号</th>
				<th width="5%">填报标识</th>
				<th width="10%">协同合同</th>
				<th width="10%">协同销售产品</th>
				<th width="7%">费用类型</th>
				<th width="5%">对比对象</th>
				<th width="5%">收入开始日期</th>
				<th width="5%">收入结束日期</th>
				<th width="5%">到款</th>
				<th width="5%">同步状态</th>
				<th width="5%">创建时间</th>
				<th width="5%">创建人</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="bean">
				<tr>
					<td rowspan="2" <c:if test="${bean.send_flag=='sent'}">style="background-color: #999;color:white;"</c:if>>
					<c:if test="${bean.send_flag == 'create' && not empty bean.income_end_date && not empty bean.income_end_date}">
					<input type="checkbox" name="checkbox" value="${bean.id}">
					</c:if>
					</td>				
					<td rowspan="2" <c:if test="${bean.send_flag=='sent'}">style="background-color: #999;color:white;"</c:if>>${fns:getOfficeNameByCode(bean.office_id)}</td>
					<td rowspan="2" style="<c:if test="${bean.send_flag=='sent'}">background-color: #999;color:white;</c:if>word-wrap:break-word; word-break:break-all;">${bean.detailid}</td>
					<td rowspan="2" <c:if test="${bean.send_flag=='sent'}">style="background-color: #999;color:white;"</c:if>>${fns:getDictLabel(bean.accountidentity,"accountidentity","")}</td>
					<td <c:if test="${bean.send_flag=='sent'}">style="background-color: #999;color:white;"</c:if>>${bean.contract_id}</td>
					<td <c:if test="${bean.send_flag=='sent'}">style="background-color: #999;color:white;"</c:if>>${bean.product_id}</td>
					<td rowspan="2" <c:if test="${bean.send_flag=='sent'}">style="background-color: #999;color:white;"</c:if>>${fns:getDictLabel(bean.payment_type,"payment_type","")}</td>
					<td <c:if test="${bean.send_flag=='sent'}">style="background-color: #999;color:white;"</c:if>>协同明细</td>
					<td <c:if test="${bean.send_flag=='sent'}">style="background-color: #999;color:white;"</c:if>>${bean.servicestartdate}</td>
					<td <c:if test="${bean.send_flag=='sent'}">style="background-color: #999;color:white;"</c:if>>${bean.serviceenddate}</td>
					<td <c:if test="${bean.send_flag=='sent'}">style="background-color: #999;color:white;"</c:if>>${bean.hasreceive}</td>
					<td <c:if test="${bean.send_flag=='sent'}">style="background-color: #999;color:white;"</c:if> rowspan="2">${fns:getDictLabel(bean.send_flag,"send_flag","")}</td>
					<td <c:if test="${bean.send_flag=='sent'}">style="background-color: #999;color:white;"</c:if> rowspan="2">${bean.createDate}</td>
					<td <c:if test="${bean.send_flag=='sent'}">style="background-color: #999;color:white;"</c:if> rowspan="2">${bean.createBy.name}</td>
				</tr>
				<tr>
					<td <c:if test="${bean.send_flag=='sent'}">style="background-color: #999;color:white;"</c:if>>${bean.syncContract.syncCustomer.chinesename}</td>
					<td <c:if test="${bean.send_flag=='sent'}">style="background-color: #999;color:white;"</c:if>>${bean.syncProduct.productname}</td>
					<td <c:if test="${empty bean.income_end_date || empty bean.income_end_date}">style="background-color:#FF8888"</c:if> <c:if test="${bean.send_flag=='sent'}">style="background-color: #999;color:white;"</c:if>>收入设置</td>
					<td <c:if test="${empty bean.income_begin_date}">style="background-color:#FF8888"</c:if><c:if test="${bean.send_flag=='sent'}">style="background-color: #999;color:white;"</c:if>>
						<c:if test="${bean.income_begin_date != bean.servicestartdate}">
						<font color="red">
						</c:if>
						${bean.income_begin_date}				
					<td <c:if test="${empty bean.income_end_date}">style="background-color:#FF8888"</c:if><c:if test="${bean.send_flag=='sent'}">style="background-color: #999;color:white;"</c:if>>
						<c:if test="${bean.income_end_date != bean.serviceenddate}">
						<font color="red">
						</c:if>
						${bean.income_end_date}
					</td>
					<td <c:if test="${bean.send_flag=='sent'}">style="background-color:#999;"</c:if>></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

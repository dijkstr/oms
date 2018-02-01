<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>合同级收入期间接口管理-</title>
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
		function backBtn(){
			window.location.href="${ctx}/charge/order/orderInfo/";
		}
		// 确认待发送状态
		function goConfirm(){
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
			$("#ids").val(ids);
			$("#searchForm").submit();
		}
		// 确认待发送状态
		function gorefresh(){
			$("#searchForm").attr("action","${ctx}/charge/order/orderInfo/refreshIncomePeriod");
			$("#searchForm").submit();
		}
	</script>
</head>
<body>	
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/charge/order/orderInfo/">计费合同列表</a></li>
		<li class="active">
		<a href="${ctx}/charge/income/chargeIncomePeriodInterface/form?contract_id=${incomeSettingForm.contract_id}">收入期间同步纪录</a>	
		</li>
	</ul><br/>
	<form:form id="searchForm" modelAttribute="incomeSettingForm" action="${ctx}/charge/income/chargeIncomePeriodInterface/confirm" method="post">
		<input id="ids" name="ids" type="hidden"/>
		<input id="backtype" name="backtype" type="hidden" value="1"/>
		<form:hidden path="contract_id" />
		<input id="confirmSend" class="btn btn-primary" type="button" value="确认同步" onclick="goConfirm()"/>&nbsp;
		<input id="refreshIncomePeriod" class="btn btn-primary" type="button" value="刷新" onclick="gorefresh()"/>&nbsp;
		<input id="btnCancel" class="btn" type="button" value="返 回" onclick="backBtn();"/>
	</form:form>
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
				<td <c:if test="${bean.send_flag=='sent'}">style="background-color: #999;color:white;"</c:if>>${bean.syncContract.customername}</td>
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
</body>
</html>

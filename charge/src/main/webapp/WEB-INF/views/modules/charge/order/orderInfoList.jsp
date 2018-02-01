<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>计费合同管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
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
				$("#payment_type").val("").trigger('change');
				$("#expire_month").val("").trigger('change');
				$("#order_status").val("").trigger('change');
				$("#income_source").val("").trigger('change');
			});
			// 多选框属性初始化
			var paymentTypeString = "${orderInfoForm.payment_type}";
			if(paymentTypeString != undefined || paymentTypeString !== null || paymentTypeString !== ""){
				$("#payment_type").val(paymentTypeString.split(',')).trigger('change');
			}
			var expireMonthString = "${orderInfoForm.expire_month}";
			if(expireMonthString != undefined || expireMonthString !== null || expireMonthString !== ""){
				$("#expire_month").val(expireMonthString.split(',')).trigger('change');
			}
			var orderStatusString = "${orderInfoForm.order_status}";
			if(orderStatusString != undefined || orderStatusString !== null || orderStatusString !== ""){
				$("#order_status").val(orderStatusString.split(',')).trigger('change');
			}
			var incomeSourceString = "${orderInfoForm.income_source}";
			if(incomeSourceString != undefined || incomeSourceString !== null || incomeSourceString !== ""){
				$("#income_source").val(incomeSourceString.split(',')).trigger('change');
			}
			
			$("#btnImport").click(function(){
				$.jBox($("#importBox").html(), {title:"导入数据", buttons:{"关闭":true}, 
					bottomText:"导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"});
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
		// 导出收入设置 
		function incomeExport(){
			var params = $("#searchForm").serialize();
			window.open("${ctx}/charge/bill/export/download?templatekey=incomeSettingList&" + params);
		}
		// 导出合同列表 
		function contractExport(){
			var params = $("#searchForm").serialize();
			window.open("${ctx}/charge/bill/export/download?templatekey=contractList&" + params);
		}
		// 打开催款邮件通知画面
		function showMail(contract_id){
			var url="${ctx}/charge/bill/relation/exist?contract_id=" + contract_id;		
			var data = ajaxFunction(url);
			if(data.result == 'success'){
				window.location.href = "${ctx}/charge/mail/contactMail/mail?contract_id=" + contract_id;
			}else{
				top.$.jBox.alert("协同合同编号"+ contract_id +"没有设置联系方式，请创建后再尝试。");
			}
		}
		function ajaxFunction(url,data){
			var result;
			$.ajax({
		    	type: "post",
		    	url: url,
		    	data: data,
		        dataType : "json",  
		        async: false,
		    	error: function(request) {
		    		//loading('正在提交，请稍等...');
		     		alert("连接异常");
		    	},
		    	success: function(data) {
		    	    result = data;
		    	}
			});
			return result;
		}
	</script>
</head>
<body>
	<div id="importBox" class="hide">
		<form id="importForm" action="${ctx}/charge/order/orderInfo/import" method="post" enctype="multipart/form-data"
			style="padding-left:20px;text-align:center;" class="form-search" onsubmit="loading('正在导入，请稍等...');"><br/>
			<input id="uploadFile" name="file" type="file" style="width:330px"/><br/><br/>　　
			<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>
			<a href="${ctx}/charge/order/orderInfo/import/template">下载模板</a>
		</form>
	</div>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/charge/order/orderInfo/">计费合同列表</a></li>
		<shiro:hasPermission name="charge:order:orderInfo:edit"><li><a href="${ctx}/charge/order/orderInfo/form">计费合同添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="orderInfoForm" action="${ctx}/charge/order/orderInfo/" method="post" class="breadcrumb form-search" style="height:190px;">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
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
				<label>协同客户号 ：</label>
				<form:input path="customer_id" htmlEscape="false" class="input-large"/>
			</div>
			<div class="form_tr_div">
				<label>客户名称 ：</label>
				<form:input path="hs_customername" htmlEscape="false" class="input-large"/>
			</div>
			<div class="form_tr_div">
				<label>销售经理 ：</label>
				<form:input path="customermanagername" htmlEscape="false" class="input-large"/>
			</div>
			<div class="form_tr_div">
				<label>收入来源 ：</label>
				<form:select path="income_source" multiple="multiple" class="input-large multi_select" placeholder="请选择" >
					<form:options items="${fns:getDictList('income_source')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div class="form_tr_div">
				<label>即将到期月份：</label>
				<form:select path="expire_month" multiple="multiple" placeholder="请选择" class="input-large multi_select" style="width: 190px;">
					<form:option value="1" label="本月内"/>
					<form:option value="2" label="两月内"/>
					<form:option value="3" label="三月内"/>
				</form:select>
			</div>
			<div class="form_tr_div">
				<label for="payment_type">费用类型：</label>
				<form:select path="payment_type" multiple="multiple" placeholder="请选择" class="input-large multi_select" style="width: 190px;">
					<form:option value="12471" label="一次性费用"/>
					<form:option value="12472" label="不定期"/>
					<form:option value="12473" label="年费"/>
					<form:option value="12474" label="条件延后"/>
					<form:option value="12475" label="技术服务费"/>
				</form:select>
			</div>
			<div class="form_tr_div">
				<label>合同状态 ：</label>
				<form:select path="order_status"  multiple="multiple" placeholder="请选择" class="order_status input-large multi_select" style="width: 190px;">
					<form:options items="${fns:getDictList('order_status')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div style="width: 100%; float: left; text-align: center; margin-top:20px;">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
			</div>
		</div>
	</form:form>
	<div style="padding-bottom: 10px;">
		<shiro:hasPermission name="charge:order:orderInfo:incomeExport">
			<input id="orderInfoForm" class="btn btn-primary" type="button" value="导出收入设置" onclick="incomeExport()"/>
		</shiro:hasPermission>
		<shiro:hasPermission name="charge:order:orderInfo:contractExport">
			<input id="orderInfoForm" class="btn btn-primary" type="button" value="导出合同列表" onclick="contractExport()"/>
		</shiro:hasPermission>
		<shiro:hasPermission name="charge:order:orderInfo:contractImport">
			<input id="btnImport" class="btn btn-primary" type="button" value="合同导入"/>
		</shiro:hasPermission>
	</div>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">

		<thead>
			<tr>
				<th width="5%">所属部门</th>
				<th width="8%">协同合同编号</th>
				<th width="5%">协同客户号</th>
				<th width="10%">客户名称</th>
				<th width="5%">销售经理</th>
				<th width="10%">实际使用客户</th>
				<th width="6%">收入来源</th>
				<th width="6%">开始时间</th>
				<th width="6%">结束时间</th>
				<th width="5%">合同状态</th>
				<th width="14%"h>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="orderInfo">
			<tr>
				<td>${fns:getOfficeNameByCode(orderInfo.office_id)}</td>
				<td><a href="javascript:void(0)" onclick="viewContract(encodeURI(encodeURI('${orderInfo.contract_id}')))">${orderInfo.contract_id}</a></td>
				<td>${orderInfo.customer_id}</td>
				<td><a href="javascript:void(0)" onclick="viewCustomer(${orderInfo.customer_id})">${orderInfo.hs_customername}</a></td>
				<td>${orderInfo.customermanagername}</td>
				<td>${orderInfo.customername}</td>
				<td>${fns:getDictLabel(orderInfo.income_source,"income_source","")}</td>
				<td>${fn:substring(orderInfo.order_begin_date,0,10)}</td>
				<td>${fn:substring(orderInfo.order_end_date,0,10)}</td>
				<td>${fns:getDictLabel(orderInfo.order_status,"order_status","")}</td>
				<td>
					<a target="_blank" href="${ctx}/charge/order/orderInfo/form?id=${orderInfo.id}&shiroPermission=view" class="table_button_alink">查看</a>
					<shiro:hasPermission name="charge:mail:contactMail:view">
						<a href="#" onclick="showMail('${orderInfo.contract_id}')" class="table_button_alink">邮件</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="charge:order:orderInfo:edit">
						<a href="${ctx}/charge/order/orderInfo/incomeSettingForm?contract_id=${orderInfo.contract_id}&customer_id=${orderInfo.customer_id}&hs_customername=${orderInfo.hs_customername}
						" class="table_button_alink">收入设置</a>
						<a href="${ctx}/charge/income/chargeIncomePeriodInterface/form?contract_id=${orderInfo.contract_id}" class="table_button_alink">收入期间</a>
	    				<a href="${ctx}/charge/order/orderInfo/form?id=${orderInfo.id}" class="table_button_alink">修改</a>
						<a href="${ctx}/charge/order/orderInfo/delete?id=${orderInfo.id}" class="table_button_alink" onclick="return confirmx('确认要删除该计费合同吗？', this.href)">删除</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>错单管理管理</title>
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
		//批量关闭错单
		function updateStatus(){
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
			top.$.jBox.confirm("确定要关闭吗?", "确认", function(v, h, f){  
				if (v === 'ok') {
					var url="${ctx}/charge/bill/wrongBill/updateStatus?id=" + ids;
		    		var data = ajaxFunction(url);
		    		if (data.result == "success") {
		    			top.$.jBox.alert(data.message);
		    		}
				} else if (v == 'cancel'){
					top.$.jBox.tip("cancel", 'info');
	            }
				return true; //close
			});
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
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/charge/bill/wrongBill/">错单管理列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="wrongBillForm" action="${ctx}/charge/bill/wrongBill/" method="post" class="breadcrumb form-search" style="height: 150px;">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="control-group" style="float:left;">
			<div class="form_tr_div">
				<label>起始日期 ：</label><form:input class="Wdate" type="text" path="begin_date" style="width:150px" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyyMM',maxDate:'%y-%M'})"/>
			</div>
			<div class="form_tr_div">
				<label>结束日期 ：</label><form:input class="Wdate" type="text" path="end_date" style="width:150px" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyyMM',maxDate:'%y-%M'})"/>
			</div>
			<div class="form_tr_div">
				<label>所属部门 ：</label>
					<tags:treeselect id="office" name="office_id" value="${office.code}" labelName="office_name" labelValue="${office.name}" 
						title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" idVal="code"/>
			</div>
			<div class="form_tr_div">
				<label>合同编号 ：</label><form:input path="contract_id" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">
				<label>客户编号 ：</label><form:input path="customer_id" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">	
				<label>产品编号 ：</label><form:input path="product_id" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">	
				<label>计费类型 ：</label><form:input path="fee_type" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">错单状态 ：</label>
				<form:select path="wrong_status" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('wrong_status')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">错单原因 ：</label>
				<form:select path="wrong_reason" class="input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('wrong_reason')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div style="width: 100%; float: left; text-align: center;">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
			</div>
		</div>
	</form:form>
	<div style="padding-bottom: 10px;">
		<shiro:hasPermission name="charge:bill:wrongBill:updateStatus">
			<input id="wrongBillForm" class="btn btn-primary" type="button" value="关闭" onclick="updateStatus()"/>
		</shiro:hasPermission>
	</div>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th></th>
				<th>批次号</th>
				<th>发生日期</th>
				<th>合同编号</th>
				<th>客户编号</th>
				<th>产品编号</th>
				<th>计费类型</th>
				<th>错单原因</th>
				<th>创建时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="wrongBillForm">
			<tr>
				<td>
					<input type="checkbox" name="checkbox" value="${wrongBillForm.id}">
				</td>
				<td>${wrongBillForm.batch_no}</td>
				<td>${wrongBillForm.oc_date}</td>
				<td>${wrongBillForm.contract_id}</td>
				<td>${wrongBillForm.customer_id}</td>
				<td>${wrongBillForm.product_id}</td>
				<td>${wrongBillForm.fee_type}</td>
				<td>${wrongBillForm.wrong_reason}</td>
				<td>${wrongBillForm.create_datetime}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

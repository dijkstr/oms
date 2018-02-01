<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<html>
<head>
	<meta name="decorator" content="default"/>
	<title>计费合同管理</title>
	
	<style type="text/css">
		.form-horizontal .control-label { float: none; width: 120px; }
		.form_tr_div { width: 350px; float: left; margin-bottom: 8px; padding-bottom: 8px;}
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
		.table_select{width:165px;}
		
		.table-condensed .table_input_string{width:98px;}
		.table-condensed .table_input_num{width:94px;}
		
		.specsHead{width:320px;height:35px;float:left}
		.specsHead span{width:85px;float:left}
		.product_td{width:150px;}
		
		.form-actions {text-align: center; background: none; border-top: none; padding: 0;}
	</style>
		<script src="/charge/static/js/charge/chargeModelCommon.js" type="text/javascript"></script>
	
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate();
			
		});
		
		function submitOrderCombine(){
			$("#combineForm").submit();
		}
		
		function backBtn(){
			window.location.href="${ctx}/charge/order/orderInfo/";
		}
		
	</script>
	
	
</head>
<body>
	<form:form id="combineForm" name="combineForm" modelAttribute="orderCombineForm" action="${ctx}/charge/order/order/combineSave" method="post" class="form-horizontal" enctype="application/x-www-form-urlencoded">
		<form:hidden path="id"/>
		<form:hidden path="contract_id"/>
		<input type="hidden" id="jsonListOrderCombie" name="jsonListOrderCombie">
		<form:hidden path="updateDate"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<div class="form_tr_div">
				<label class="control-label" for="name">计费开始时间:</label>
				<form:input path="combine_begin_date" htmlEscape="false" maxlength="200" class="Wdate" style="width:150px" readonly="true" onfocus="WdatePicker({})"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">计费结束时间:</label>
				<form:input path="combine_end_date" htmlEscape="false" maxlength="200" class="Wdate" style="width:150px" readonly="true" onfocus="WdatePicker({})"/>
			</div>
			
			
		</div>
	</form:form>
	
</body>
</html>


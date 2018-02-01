<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>详单管理管理</title>
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
</head>
<body>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>所属部门</th>
					<th>批次号</th>
					<th>账单编号</th>
					<th>发生日期</th>
					<th>合同编号</th>
					<th>客户编号</th>
					<th>客户名称</th>
					<th>协同产品编号</th>
					<th>产品名称</th>
					<th>计费模式</th>
					<th>费率(‰)</th>
					<th>计费金额</th>
					<th>技术服务费</th>
					<th>业务数据</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${detail}" var="detail">
					<tr>
						<td>${fns:getOfficeNameByCode(detail.office_id)}</td>
						<td>${detail.batch_no}</td>
						<td>${detail.bill_id}</td>
						<td>${detail.charge_begin_date}</td>
						<td>${detail.contract_id}</td>
						<td>${detail.customer_id}</td>
						<td>${detail.user_name}</td>
						<td>${detail.product_id}</td>
						<td>${detail.product_name}</td>
						<td>${detail.feemodel_name}</td>
						<td><fmt:formatNumber value="${detail.fee_ratio}" pattern="#,###.####"/></td>
						<td><fmt:formatNumber value="${detail.charge_amt}" pattern="#,###.####"/></td>
						<td><fmt:formatNumber value="${detail.service_charge}" pattern="#,###.####"/></td>
						<td><fmt:formatNumber value="${detail.org_amt}" pattern="#,###.####"/></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
</body>
</html>

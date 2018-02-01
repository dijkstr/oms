<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>收入明细确认</title>
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
		.break{
			white-space:normal;
			word-break:break-all;
			overflow: auto;
		}
	</style>
	<meta name="decorator" content="default"/>
<%@include file="/WEB-INF/views/include/head.jsp" %>
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" rel="stylesheet" />
<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js" type="text/javascript"></script>
<script type="text/javascript">
	function pass(){
		top.$.jBox.confirm("是否确认收入?", "确认", function(v, h, f){  
			if (v === 'ok') {
				var url="${ctx}/charge/finance/finSummary/confirmFinanceIncome?contract_id=" + "${contract_id}";;
	    		var data = ajaxFunction(url);
	    		if(data.result == 'success'){
	    			window.location.href='${ctx}/charge/finance/finSummary';
	    			top.$.jBox.alert("收入确认成功", '提示');
	    		}else{
	    			top.$.jBox.alert("收入确认失败，请联系系统管理员");
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
	<div style="padding-bottom: 10px;">
	<input type="button" class="btn btn-primary" value="确认收入" onClick="pass()" />
	</div>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
		
			<thead>
				<tr>
					<th width="10%">明细行号</th>
					<th width="10%">协同合同编号</th>
					<th width="10%">协同客户编号</th>
					<th width="10%">协同客户名称</th>
					<th width="10%">实际使用客户</th>
					<th width="10%">协同销售产品编号</th>
					<th width="10%">协同销售产品名称</th>
					<th width="10%">计费开始时间</th>
					<th width="10%">计费结束时间</th>
					<th width="10%">固定费用类型</th>
					<th width="10%">本期累计收入</th>
					<th width="10%">收入增量</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${detail}" var="detail">
					<tr>
						<td><div class="break">${detail.detailid}</div></td>
						<td>${detail.contract_id}</td>
						<td>${detail.customer_id}</td>
						<td>${detail.user_name}</td>
						<td>${detail.customername}</td>
						<td>${detail.product_id}</td>
						<td>${detail.product_name}</td>
						<td>${detail.charge_begin_date}</td>
						<td>${detail.charge_end_date}</td>
						<td>${detail.display_name}</td>
						<td><fmt:formatNumber value="${detail.finance_current_income}" pattern="#,###.##"/></td>
						<td align="right"><fmt:formatNumber value="${detail.change_income}" pattern="#,###.##"/></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
</body>
</html>
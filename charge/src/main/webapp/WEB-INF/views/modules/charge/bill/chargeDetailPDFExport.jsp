<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>导出计费明细PDF</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
		.search_div{
			width: 33%;
		    float: left;
		    margin-bottom: 5px;
		    padding-bottom: 4px;
		}
		.search_div label{width: 100px;}
	</style>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnReset").on("click",function(){
				$("#searchForm input:not([type='submit'],[type='button'])").val("");
				$("#searchForm select option:selected").attr("selected", false);
				$("#searchForm a span").text("请选择");
			});
		});
		function exportChargeReceiptDetailPDF() {
			if(!$("#contract_id").val()){
				top.$.jBox.alert("请输入协同合同编号。");
				return;
			}
			var params = $("#searchForm").serialize();
			var url="${ctx}/charge/bill/chargeBill/billExist?" + params;
			var data = ajaxFunction(url);
			if(data.result=="fail"){
				top.$.jBox.alert(data.message);
				return;
			}
			window.open("${ctx}/charge/bill/export/download?templatekey=exportChargeReceiptDetailPDF&" + params);
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
		//协同合同号选择器
		function contractChooserShow(type){
			top.$.jBox.open("iframe:${ctx}/charge/common/syncContractChooser", "协同合同选择器",810,$(top.document).height()-240,{
				buttons:{"确定选择":"ok", "关闭":true},submit:function(v, h, f){
					if (v=="ok"){
						var contractInfo = h.find("iframe")[0].contentWindow.getContractInfo();
						$("#searchForm").find("input[name='contract_id']").val(contractInfo.contractid);
				    	return true;
					}
				}, loaded:function(h){
					$(".jbox-content", top.document).css("overflow-y","hidden");
				}
			});
		}
	</script>
</head>
<body>
	<form id="searchForm" action="" method="post" class="breadcrumb form-search">
		<div style="margin-bottom: 8px;height: 60px;">
			<div class="search_div">
				<label>协同合同编号 ：</label>
				<input type="text" id="contract_id" name="contract_id" style="width:150px" maxlength="50" onclick="contractChooserShow()"/>
			</div>
			<div class="search_div">
				<label>起始日期 ：</label>
				<input class="Wdate" type="text" name="charge_begin_date" style="width:150px" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})"/>
			</div>
			<div class="search_div">
				<label>结束日期 ：</label>
				<input class="Wdate" type="text" name="charge_end_date" style="width:150px" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})"/>
			</div>
		</div>
		<div style="text-align:center">
			<input id="btnSubmit" class="btn btn-primary" type="button" value="导出" onclick="exportChargeReceiptDetailPDF()"/>
			<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
		</div>
	</form>
	<tags:message content="${message}"/>
</body>
</html>

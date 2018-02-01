<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>收入预测管理</title>
	<meta name="decorator" content="default"/>
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
	<link href="${ctxStatic}/css/jquery.loadmask.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="${ctxStatic}/js/charge/jquery.loadmask.js"></script>
	<script src="${ctxStatic}/echarts/echarts-all.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnReset").on("click",function(){
				$("#searchForm input:not([type='submit'],[type='button'])").val("");
				$("#searchForm select option:selected").attr("selected", false);
				$("#searchForm a span").text("请选择");
			});
			preview();
		});
		// 获取预测进度信息
	    function getForecastProgress() {			
	    	var url = "${ctx}/forecast/income/incomeForecast/getForecastProgress"; 
	    	var data = ajaxFunction(url);
	    	if(data.status){
	    		//status:1--正在处理，9--处理终了，-1--异常
	    		//msg:"开始处理"，"处理终了"，"系统异常，请联系管理员"
	    		if(data.status == "1") {
	    			$("body").mask(data.content + data.percent);
	    			setTimeout("getForecastProgress()", 200);
	    		}else if(data.status == "9") {
	    			$("body").unmask();
	    			top.$.jBox.alert(data.msg);
					preview();
	    		}else if(data.status == "-1"){
	    			$("body").unmask();
	    			top.$.jBox.alert(data.msg);
	    			preview();
	    		}else {
	    			$("body").unmask();
	    			top.$.jBox.alert("502");
	    			preview();
	    		}
	    	}
	    }
	 	// 刷新
		function refresh(){
			top.$.jBox.confirm("手动刷新全部预测结果需要较长时间，确认执行么？","系统提示",function(v,h,f){
			    if (v == 'ok') {
			    	var params = $("#searchForm").serialize();
					var url="${ctx}/forecast/income/incomeForecast/refresh?";			
					var data = ajaxFunction(url,params);
					if(data.result=="success"){
						$("body").mask('收入预测中...');
						if(data.wait=="wait") {
							top.$.jBox.alert("正在刷新全部预测结果,请耐心等待程序完成。");
							return;
						}
						getForecastProgress();
					}else{
						$("body").unmask();
						top.$.jBox.error(data.message);
					}
			    }
			});
		}
		function preview(){
			var url = "${ctx}/forecast/income/incomeForecast/graph?" + $('#searchForm').serialize();
			var param = {"t":Math.random()};
			var result = ajaxFunction(url,param);
			document.getElementById("forecast").innerHTML = result.html;
			//debugger;
			//console.log(result.html);
			eval(result.graph);        
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
		function contractChooserShow(){
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
		
		//协同产品选择器
		function prodChooserShow(obj){
			top.$.jBox.open("iframe:${ctx}/charge/common/productChooser", "协同产品选择器",810,$(top.document).height()-240,{
				buttons:{"确定选择":"ok", "关闭":true},submit:function(v, h, f){
					if (v=="ok"){
						var productInfo = h.find("iframe")[0].contentWindow.getProductInfo();
						$("#searchForm").find("input[name='product_id']").val(productInfo.product_id);
						$("#searchForm").find("input[name='product_name']").val(productInfo.product_name);
				    	return true;
					}
				}, loaded:function(h){
					$(".jbox-content", top.document).css("overflow-y","hidden");
				}
			});
		}
		//协同客户选择器
		function xtCustomerChooserShow(obj){
			top.$.jBox.open("iframe:${ctx}/charge/common/syncCustomerChooser", "协同客户选择器",810,$(top.document).height()-240,{
				buttons:{"确定选择":"ok", "关闭":true},submit:function(v, h, f){
					if (v=="ok"){
						var customerInfo = h.find("iframe")[0].contentWindow.getCustomerInfo();
						$("#searchForm").find("input[name='customer_id']").val(customerInfo.customer_id);
						$("#searchForm").find("input[name='chinesename']").val(customerInfo.chinesename);
				    	return true;
					}
				}, loaded:function(h){
					$(".jbox-content", top.document).css("overflow-y","hidden");
				}
			});
		}
		function exportIncomeForecastDetail(){
			window.open("${ctx}/charge/bill/export/download?templatekey=exportIncomeForecastDetail&" + $('#searchForm').serialize());
		}
	</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="incomeForecastForm" action="${ctx}/forecast/income/incomeForecast/" method="post" class="breadcrumb form-search" style="height:117px;">
		<input type="hidden" id='customer_id' name="customer_id"/>
		<input type="hidden" id='product_id' name="product_id"/>
		<div class="control-group" style="float:left;">
			<div class="form_tr_div">
				<label>所属部门 ：</label>
					<tags:treeselect id="office" name="office_id" value="${office.code}" labelName="office_name" labelValue="${office.name}" 
						title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" idVal="code"/>
			</div>
			<div class="form_tr_div">
				<label>协同合同 ：</label><form:input path="contract_id" htmlEscape="false" maxlength="200" style="width:150px" onclick="contractChooserShow()"/>
			</div>
			<div class="form_tr_div">
				<label>协同客户 ：</label><input type="text" id="chinesename" name="chinesename" htmlEscape="false" maxlength="200" style="width:150px" onclick="xtCustomerChooserShow()"/>
			</div>
			<div class="form_tr_div">
				<label>协同产品 ：</label><input type="text" id="product_name" name="product_name" htmlEscape="false" maxlength="200" style="width:150px" onclick="prodChooserShow()"/>
			</div>
			<div class="form_tr_div">
				<label>开始年月 ：</label><form:input class="Wdate" type="text" path="begin_month" style="width:150px" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyyMM'})"/>
			</div>
			<div class="form_tr_div">
				<label>结束年月 ：</label><form:input class="Wdate" type="text" path="end_month" style="width:150px" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyyMM'})"/>
			</div>
			<div style="width: 100%; float: left; text-align: center;">
				<input type="button" class="btn btn-primary" onclick="preview()" value="查询">
				<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
			</div>
		</div>
	</form:form>
	<div style="padding-bottom: 10px;">
		<shiro:hasPermission name="forecast:income:refresh">
			<input id="refresh" class="btn btn-primary" type="button" value="全部刷新" onclick="refresh()"/>&nbsp;
		</shiro:hasPermission>
		<shiro:hasPermission name="forecast:income:export">
			<input id="exportIncomeForecastDetail" class="btn btn-primary" type="button" value="导出全部预测" onclick="exportIncomeForecastDetail()"/>&nbsp;
		</shiro:hasPermission>
	</div>
	<tags:message content="${message}"/>
	<div class="control-group" style="border:3px dotted #DDDDDD;">
		<div class="controls">
			<div id="forecast" style="width:95%;height:100%"></div>
		</div>
	</div>
</body>
</html>

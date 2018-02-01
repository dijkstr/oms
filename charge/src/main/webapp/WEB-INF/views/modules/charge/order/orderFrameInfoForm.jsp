<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<html>
<head>
	<meta name="decorator" content="default"/>
	<title>框架合同管理</title>
	
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
		.table-condensed .table_input_num{width:74px;}
		
		.specsHead{width:320px;height:35px;float:left}
		.specsHead span{width:85px;float:left}
		.product_td{width:150px;}
		
		.form-actions {text-align: center; background: none; border-top: none; padding: 0;}
	</style>
	
	<script src="/charge/static/js/charge/charge_common.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate();
			
			$("#orderAddDiv").append('${html}');
			if($("#id").val() != ""){
				if($("div[name=childContractDiv]") != null){
					contractIndex = $("div[name=childContractDiv]").length;
				}
			}
			
			var shiroPermission = '${shiroPermission}';
			if(shiroPermission == "view"){
				$("select").attr("disabled", true);
				$("input").attr("disabled", true);
				$("textarea").attr("disabled", true);
				$("tr.detailButton").remove();
				$("th.detailButton").remove();
				$("td.detailButton").remove();
				$("input.detailButton").remove();
				$("a.table_button_alink").remove();
				$("#orderAddTable").remove();
				$("#btnSubmit").remove();
				$("#btnCancel").remove();
				$("#form_ul li").remove();
				$("#form_ul").append('<li class="active"><a>框架合同查看</a></li>');
				$("body").attr("style","padding-left: 80px;");
			}
			
			
			
		});
		
		//协同合同号选择器
		function contractChooserShow(type){
			top.$.jBox.open("iframe:${ctx}/charge/common/syncContractChooser", "协同合同选择器",810,$(top.document).height()-240,{
				buttons:{"确定选择":"ok", "关闭":true},submit:function(v, h, f){
					if (v=="ok"){
						var contractInfo = h.find("iframe")[0].contentWindow.getContractInfo();
						if(type==1){
							$("#addOrderForm").find("input[name='contract_id']").val(contractInfo.contractid);
							$("#addOrderForm").find("input[name='customer_id']").val(contractInfo.customerid);
							$("#addOrderForm").find("input[name='hs_customername']").val(contractInfo.customername);
							$("#addOrderForm").find("input[name='customermanagername']").val(contractInfo.customermanagername);
							$("#addOrderForm").find("input[name='customermanager2name']").val(contractInfo.customermanager2name);
						}else if(type==2){
							$("#addOrderForm").find("input[name='frame_contract_id']").val(contractInfo.contractid);
						}
				    	return true;
					}
				}, loaded:function(h){
					$(".jbox-content", top.document).css("overflow-y","hidden");
				}
			});
		}
		
		//计费合同号选择器
		function chargeContractChooser(obj){
			if($(obj).parent().parent().find("input[name='combine_id']").val() != ""){
				top.$.jBox.alert("合同编号不能修改，可以删除再新建！");
				return;
			}
			top.$.jBox.open("iframe:${ctx}/charge/common/chargeContractChooser", "计费合同选择器",810,$(top.document).height()-240,{
				buttons:{"确定选择":"ok", "关闭":true},submit:function(v, h, f){
					if (v=="ok"){
						var contractInfo = h.find("iframe")[0].contentWindow.getContractInfo();
						var contractTr = $(obj).parent().parent().parent();
						$(contractTr).find("input[name='contract_id']").val(contractInfo.contract_id);
						$(contractTr).find("input[name='order_begin_date']").val(contractInfo.order_begin_date);
						$(contractTr).find("input[name='order_end_date']").val(contractInfo.order_end_date);
						$(contractTr).next().find("textarea[name='remarks']").val(contractInfo.remarks);
				    	return true;
					}
				}, loaded:function(h){
					$(".jbox-content", top.document).css("overflow-y","hidden");
				}
			});
		}
		
		
		
		function backBtn(){
			window.location.href="${ctx}/charge/order/orderFrameInfo/";
		}
		
		
		
		
		
		var contractIndex = 0;
		function getContractIndex(){
			contractIndex = contractIndex + 1;
			return contractIndex;
		}
		
		function createChildContract(){
			var childContracts = document.getElementById("orderAddDiv");
			var childContract = document.createElement("div");
			var childContractId = "childContract" + getContractIndex();
			childContract.setAttribute("id", childContractId);
			childContract.setAttribute("name", "childContractDiv");
			creatCombineTable(childContract, childContractId);
			childContracts.appendChild(childContract);
		}
		
		var combineControls = [
		                       [
								[ 'contractButton', 'contract_id', '合同编号', '' ],
		                        [ 'calendar', 'order_begin_date', '开始时间', 'WdatePicker({})' ],
		                        [ 'calendar', 'order_end_date', '结束时间', 'WdatePicker({})' ]
		                       ],
		                       [
		                        [ 'area', 'remarks', '备注', '' ],
		                        [ '', '', '', '' ]
		                       ]
		                      ];
		var combineHiddens = [['hidden','combine_id','', '']];

		function creatCombineTable(combine, combineId) {						
			var combineTable = document.createElement("table");
			combineTable.setAttribute("id", combineId + "_in");
			var buttonTr = combineTable.insertRow(0);
			var buttonTd = buttonTr.insertCell();
			buttonTd.setAttribute("colspan", "3");
			buttonTd.setAttribute("align", "right");
			buttonTd.setAttribute("style", "border-style: none; background: #f5f5f5;");
			buttonTd.innerHTML = '<a href="javascript:void(0)" class="table_button_alink" onclick="deleteChildContract(' + combineId + ')">删除子合同</a>';
			for (var i = 0; i < combineControls.length; i++) {
				var newTr = combineTable.insertRow(i + 1);
				for (var j = 0; j < combineControls[i].length; j++) {
					var newTd = newTr.insertCell();
					newTd.setAttribute("style", "border-style: none; background: #f5f5f5;");
					if(combineControls[i][j][0]=="area"){
						newTd.setAttribute("colspan", "2");
					}
					newTd.innerHTML = drawControl(combineControls[i][j][0], combineControls[i][j][1], combineControls[i][j][2], combineControls[i][j][3]);
					if (i == 0 && j == 0) {
						newTd.innerHTML = newTd.innerHTML + drawHidden(combineHiddens);
					}
				}
			}
			
			var orderPriceTable = getOrderPriceTable(combineId);
			buttonTr = combineTable.insertRow(combineControls.length + 1);
			buttonTd = buttonTr.insertCell();
			buttonTd.setAttribute("colspan", "3");
			buttonTd.setAttribute("style", "border-style: none; background: #f5f5f5;");
			buttonTd.setAttribute("align", "middle");
			
			
			var div = document.createElement("div");
			div.setAttribute("style", "width: 1150px;overflow-x: auto;");
			div.appendChild(orderPriceTable[0]);
			buttonTd.appendChild(div);
			
			//buttonTd.appendChild(orderPriceTable[0]);
			
			combine.appendChild(combineTable);
			
		}
		
		function getOrderPriceTable(combineId) {
			var table = $('<table id="orderPrice'+combineId+'" style="width:100%;margin:0 auto;" class="table table-striped table-bordered table-condensed"></table>');
			var thead = $('<thead></thead>');
			table.append(thead);
			var tbody = $('<tbody id="body_orderPrice"></tbody>');
			table.append(tbody);
			var headTr1 = $('<tr class="detailButton"><th colspan="11" width="99%" valign="top" style="text-align: left;"><div>'+
					'<a href="javascript:void(0)" class="table_button_alink" onclick="addOrderFee(this)">添加结算</a>'+
					'</div></th></tr>');
			thead.append(headTr1);
			var headTr2 = $('<tr><th>计费来源</th><th>显示名称</th><th>开始时间</th><th>结束时间</th><th>阶梯区间</th><th>阶梯单位</th><th>阶梯开始</th><th>阶梯结束</th><th>结算比例(%)</th>'+
			'<th>按固定金额结算</th><th class="detailButton">操作</th></tr>');
			thead.append(headTr2);
			return table;
		}
		
		var orderPriceCount = 0;
		function addOrderFee(obj) {
			var tr = getOrderFeeTr(orderPriceCount);
			var specsTbody = $(obj).parent().parent().parent().parent().next("tbody");
			specsTbody.prepend(tr);
			orderPriceCount++;
		}
		
		function getOrderFeeTr(){
			var str = '<tr style="HEIGHT:22px" class="tr_insertDataGrid_add">' +
				'<td ><select name="charge_source" style="width:100px;"><option value="1">子合同应收</option><option value="2">A计费类型</option></select></td>' +
				'<td ><input type="text" name="option_name" onMouseOver="this.title=this.value;" value="" maxlength="50" class="table_input_string"/></td>' +
				'<td ><input type="text" name="charge_begin_date" class="Wdate" readonly="true" onFocus="WdatePicker()" style="width:100px;"/></td>' +
				'<td ><input type="text" name="charge_end_date" class="Wdate" readonly="true" onFocus="WdatePicker()" style="width:100px;"/></td>' +
				'<td ><select name="step_interval" onchange="itemSelectChange(this)" style="width:122px;"><option value="1">MIN&lt;N&lt;=MAX</option><option value="2">MIN&lt;=N&lt;MAX</option></select></td>' +
				'<td ><select name="step_unit" onchange="itemSelectChange(this)" style="width:50px;"><option value="0">无</option><option value="1">万</option><option value="2">亿</option></select></td>' +
				'<td ><input type="text" name="step_begin" onMouseOver="this.title=this.value;" value="" class="table_input_num"/></td>' +
			    '<td ><input type="text" name="step_end" onMouseOver="this.title=this.value;" value="" class="table_input_num"/></td>' +
				'<td ><input type="text" name="fee_ratio" onMouseOver="this.title=this.value;" value="" class="table_input_num"/></td>' +
				'<td ><input type="text" name="fixed_charge" onMouseOver="this.title=this.value;" value="" class="table_input_num"/></td>' +
				'<td style="display:none"><input type="text" name="id" value=""/></td>' +
				'</tr>';
			var specsTr = $(str);
			var td = $("<td class=\"detailButton\"><a onclick=\"delOrderFee(this)\" class=\"table_button_alink\" style=\"width: 27px;\">删除</a></td>");
			td.appendTo(specsTr);
			return specsTr;
		}
		
		function drawHidden(hiddens) {
			var hiddenText = "";
			for (var i = 0; i < hiddens.length; i++) {
				hiddenText = hiddenText + drawControl(hiddens[i][0], hiddens[i][1]);
			}
			return hiddenText;
		}

		function drawControl(type, name, label, options) {
			var controlText = "";
			if (type == "hidden") {
				controlText = '<input name="' + name + '" type="hidden"  value="">';
			} else if (type == "text") {
				controlText = '<div class="specsHead"><span>' + label + '</span><input class="table_input_num" name="' + name + '" type="text" value=""></div>';
			}  else if (type == "area") {
				controlText = '<div><span style="width:85px;float:left;">' + label + '</span><textarea name="' + name + '" readonly="readonly" rows="4" style="width: 540px;height: 60px;"></textarea></div>';
			} else if (type == "label") {
		   		controlText = '<div class="specsHead"><span>' + label + '</span><span style="width:220px;" name = "' + name + '"></span></div>';
		   	} else if (type == "calendar") {
		   		controlText = '<div class="specsHead"><span>'+ label +'</span><input class="Wdate" type="text" name="' + name + '" disabled="true" onFocus="' + options + '"/></div>';
		   	} else if (type == "contractButton") {
		   		controlText = '<div class="specsHead"><span>'+ label +'</span><input class="table_input_string" name="'+ name +'" type="text" value="" readonly="readonly" onclick="chargeContractChooser(this)"></div>';	
		   	}
			return controlText;
		}
		
		
		//删除结算信息
		function delOrderFee(obj){
			top.$.jBox.confirm("确认删除结算信息?","提示",function(v,h,f){
				if(v=='ok'){
					$(obj).parent("td").parent("tr").remove();
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
		}
		
		//删除子合同
		function deleteChildContract(combineId){
			top.$.jBox.confirm("确认删除子合同信息?","提示",function(v,h,f){
				if(v=='ok'){
					combineId.parentNode.removeChild(combineId);
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
		}
		
		//提交保存
		function submitChargeOrder(){
			var orderSubArray = getOrderSubListParam();
			if (!orderSubArray) {
				return;
			}
			
			$("#jsonListOrderFrameSub").val(obj2string(orderSubArray));
			$("#addOrderForm").submit();
		}
		
		function getOrderSubListParam() {
			var orderSubArray = [];
			var div = $("#orderAddDiv");
			var orderSubDiv = div.children("div");
			for (var i = 0; i < orderSubDiv.length; i++) {
				var combineTab =  $(orderSubDiv[i]).children("table")[0];
				// 组合
				var combParam = getCombParam(combineTab);
				orderSubArray.push(combParam);
			}
			return orderSubArray;
		}
		
		
		function getCombParam(trObj) {
			var param = {};
			var combId = $(trObj).attr("id").split("_")[0];
			param.id = $(trObj).find("input[name=combine_id]").val();
			param.contract_id = $(trObj).find("input[name=contract_id]").val();
			param.frame_contract_id = $("#addOrderForm").find("input[name=contract_id]").val();
			
			var orderPriceArray = [];
			var orderPriceTrList = $("#orderPrice" + combId).find("tbody").children("tr");
			for(var i = 0; i < orderPriceTrList.length; i++) {
				var order_frame_price = {};
				order_frame_price.charge_source = $(orderPriceTrList[i]).find("select[name=charge_source]").val();
				order_frame_price.option_name = $(orderPriceTrList[i]).find("input[name=option_name]").val();
				order_frame_price.charge_begin_date = $(orderPriceTrList[i]).find("input[name=charge_begin_date]").val();
				order_frame_price.charge_end_date = $(orderPriceTrList[i]).find("input[name=charge_end_date]").val();
				order_frame_price.step_interval = $(orderPriceTrList[i]).find("select[name=step_interval]").val();
				order_frame_price.step_unit = $(orderPriceTrList[i]).find("select[name=step_unit]").val();
				order_frame_price.step_begin = $(orderPriceTrList[i]).find("input[name=step_begin]").val();
				order_frame_price.step_end = $(orderPriceTrList[i]).find("input[name=step_end]").val();
				order_frame_price.fee_ratio = $(orderPriceTrList[i]).find("input[name=fee_ratio]").val();
				order_frame_price.fixed_charge = $(orderPriceTrList[i]).find("input[name=fixed_charge]").val();
				
				//order_frame_price.fee_ratio_division = "";
				//order_frame_price.price = "";
				//order_frame_price.max_consume = "";
				
				order_frame_price.id = $(orderPriceTrList[i]).find("input[name=id]").val();
				order_frame_price.contract_id = $("#addOrderForm").find("input[name=contract_id]").val();
				order_frame_price.sub_contract_id = param.contract_id;
				
				orderPriceArray.push(obj2string(order_frame_price));
			}
			
			param.jsonListOrderFramePrice = orderPriceArray.length == 0 ? "[]" : '['+ orderPriceArray.toString() + ']';
			return param;
		}
		
		
	</script>
	
	
</head>
<body>
	<ul class="nav nav-tabs" id="form_ul">
		<li><a href="${ctx}/charge/order/orderFrameInfo/">框架合同列表</a></li>
		<li class="active"><a href="${ctx}/charge/order/orderFrameInfo/form?id=${orderInfoForm.id}">框架合同<shiro:hasPermission name="charge:order:orderFrameInfo:edit">${not empty orderInfoForm.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="charge:order:orderFrameInfo:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	
	<form:form id="addOrderForm" name="addOrderForm" modelAttribute="orderFrameInfoForm" action="${ctx}/charge/order/orderFrameInfo/save" method="post" class="form-horizontal" enctype="application/x-www-form-urlencoded">
		<form:hidden path="id"/>
		<input type="hidden" id="jsonListOrderFrameSub" name="jsonListOrderFrameSub">
		
		<form:hidden path="updateDate"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<div class="form_tr_div">
				<label class="control-label" for="classify">所属部门:</label>
				<tags:treeselect id="office" name="office_id" value="${office.code}" labelName="office_name" labelValue="${office.name}" 
					title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" idVal="code"/>
			</div>
			
			<div class="form_tr_div">
				<label class="control-label" for="name">协同合同编号:</label>
				<form:input path="contract_id" htmlEscape="false" maxlength="200" class="required" style="width:150px" onclick="contractChooserShow(1)"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">支付周期:</label>
				<form:select path="payment_cycle" class="input-medium">
					<form:options items="${fns:getDictList('payment_cycle')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">协同客户号:</label>
				<form:input path="customer_id" htmlEscape="false" maxlength="200" class="required" style="width:150px" readonly="true"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">客户名称:</label>
				<form:input path="hs_customername" htmlEscape="false" maxlength="200" class="required" style="width:150px" readonly="true"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">销售经理1:</label>
				<form:input path="customermanagername" htmlEscape="false" maxlength="200" class="required" style="width:150px" readonly="true"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">销售经理2:</label>
				<form:input path="customermanager2name" htmlEscape="false" maxlength="200" class="required" style="width:150px" readonly="true"/>
			</div>
			<div class="form_tr_div2">
				<label class="control-label" for="remarks">备注:</label>
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge"/>
			</div>
		</div>
		</form:form>
		<div class="form-actions">
			<shiro:hasPermission name="charge:order:orderFrameInfo:edit">
				<input id="btnSubmit" class="btn btn-primary" type="button" value="&nbsp;&nbsp;保&nbsp;存 &nbsp;&nbsp;" onclick="submitChargeOrder()"/>&nbsp;&nbsp;&nbsp;&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="&nbsp;&nbsp;返 &nbsp;回&nbsp;&nbsp;" onclick="backBtn();"/>
		</div>
		<div id="orderAddDiv" style="float: left; width:1160px;overflow-x: auto;overflow-y: hidden;">
			<table id="orderAddTable" class="table table-striped table-bordered table-condensed" style="margin-bottom: 0;"/>
				<thead>
					<tr>
						<th colspan="10" width="99%" valign="top" style="text-align: left;">
							<div>
								<a href="javascript:void(0)" class="table_button_alink" onclick="createChildContract()">添加子合同</a>
							</div>
						</th>
					</tr>
				</thead>
				<tbody id="body_orderAdd"></tbody>
			</table>
			
		</div>
		
</body>
</html>


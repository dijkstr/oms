
var payCount = 0;

function fixChargeTypeCharge(obj) {
	var fix_charge_type = $(obj).val();
	var form = $(obj).parent().parent().parent().parent();
	if (fix_charge_type == "1") {
		$(form).find("select[name=is_multiplied_actualdays]").val("0");
		$(form).find("select[name=is_multiplied_actualdays]").attr("disabled", true);
	} else {
		$(form).find("select[name=is_multiplied_actualdays]").attr("disabled", false);
	}
}

function spanFeeFormulaName(fee_formula) {
	if (fee_formula == "1") {
		return "普通";
	} else if (fee_formula == "2") {
		return "普通阶梯";
	} else if (fee_formula == "3") {
		return "个税阶梯";
	} else if (fee_formula == "4") {
		return "固定费用阶梯";
	} else {
		return "";
	}
}


function getOrderFeeTable(combineId) {
	var table = $('<table id="orderFee'+combineId+'" style="width:99%;" class="table table-striped table-bordered table-condensed"></table>');
	var thead = $('<thead></thead>');
	table.append(thead);
	var tbody = $('<tbody id="body_orderFee"></tbody>');
	table.append(tbody);
	var headTr1 = $('<tr class="detailButton"><th colspan="10" width="99%" valign="top" style="text-align: left;"><div>'+
			'<a href="javascript:void(0)" class="table_button_alink" onclick="addOrderFee(this)">添加预付</a>'+
			'</div></th></tr>');
	thead.append(headTr1);
	var headTr2 = $('<tr><th>固定费用类型</th><th>显示名称</th><th>预付日期</th><th>展示日期</th><th>预付金额</th><th class="detailButton">操作</th></tr>');
	thead.append(headTr2);
	return table;
}

function getSpecTable(modelTrCount) {
	var table = $('<table id="specsDataGrid_'+modelTrCount+'" style="width:800px;" class="table table-striped table-bordered table-condensed"></table>');
	var thead = $('<thead></thead>');
	table.append(thead);
	var tbody = $('<tbody id="body_specsDataGrid"></tbody>');
	table.append(tbody);
	var headTr1 = $('<tr class="detailButton"><th colspan="10" width="99%" valign="top"><div>'+
					'<a href="javascript:void(0)" class="table_button_alink" onclick="addSpecItem(this)">添加阶梯</a>'+
					'</div></th></tr>');
	thead.append(headTr1);
	var headTr2 = $('<tr><th>选项名称</th><th>阶梯区间</th><th>阶梯单位</th><th>阶梯开始</th><th>阶梯结束</th><th>费率(‰)</th><th>费率拆分规则</th>'+
			'<th>封顶费用</th><th>固定费用</th><th class="detailButton">操作</th></tr>');
	thead.append(headTr2);
	return table;
}

function getOrderFeeTr(count){
	var str = '<tr id="tr_OrderFee_'+ count+' " style="HEIGHT:22px" class="tr_insertDataGrid_add">' +
		'<td ><select name="payment_type" style="width:150px;"><option value="10000">保底预付</option><option value="12471">一次性费用</option><option value="12473">年费</option><option value="12472">不定期</option><option value="12474">条件延后</option></select></td>' +
		'<td ><input type="text" name="display_name" onMouseOver="this.title=this.value;" value="" class="table_input_string"/></td>' +
		'<td ><input type="text" name="advance_date" class="Wdate" readonly="true" onFocus="WdatePicker()"/></td>' +
		'<td ><input type="text" name="display_date" class="Wdate" readonly="true" onFocus="WdatePicker()"/></td>' +
		'<td ><input type="text" name="amount" onMouseOver="this.title=this.value;" value="" class="table_input_num"/></td>' +
		'<td style="display:none"><input type="text" name="id" title="id" value=""/></td>' +
	   '</tr>';
	var specsTr = $(str);
	var td = $("<td class=\"detailButton\"><a onclick=\"delOrderFee(this)\" class=\"table_button_alink\" style=\"width: 30px;\">删除</a></td>");
	td.appendTo(specsTr);
	return specsTr;
}
//删除固定费用
function delOrderFee(obj){
	top.$.jBox.confirm("确认删除固定费用?","提示",function(v,h,f){
		if(v=='ok'){
			$(obj).parent("td").parent("tr").remove();
		}
	},{buttonsFocus:1});
	top.$('.jbox-body .jbox-icon').css('top','55px');
}

var orderFeeCount = 0;
function addOrderFee(obj) {
	var tr = getOrderFeeTr(orderFeeCount);
	var specsTbody = $(obj).parent().parent().parent().parent().next("tbody");
	specsTbody.prepend(tr);
	orderFeeCount++;
}

function getOrderCombListParam() {
	var combProdArray = [];
	var div = $("#orderAddDiv");
	var combineDiv = div.children("div");
	for (var i = 0; i < combineDiv.length; i++) {
		var combineTab =  $(combineDiv[i]).children("table")[0];
		// 结构：[[组合1[，产品1，产品2，...,产品n]],[组合2[产品1，产品2，...,产品n]]...]
		// 组合
		var combParam = getCombParam(combineTab);
		// 产品
		var prodArray = [];
		var prodTableList = $(combineTab).find("table[name=prodTable]");
		
		for(var j=0;j<prodTableList.length;j++){
			var prodParam = getProdParam($(prodTableList[j]));
			if (!prodParam) {
				return;
			}
			prodArray.push(obj2string(prodParam));
		}
		combParam.jsonListOrderProduct = prodArray.length == 0 ? "[]" : '['+ prodArray.toString() + ']';
		combProdArray.push(combParam);
	}
	return combProdArray;
}

function getCombParam(trObj) {
	var param = {};
	var orderModel = {};
	var combId = $(trObj).attr("id").split("_")[0];
	param.id = $(trObj).find("input[name=combine_id]").val();
	param.combine_begin_date = $(trObj).find("input[name=charge_begin_time]").val();
	param.combine_end_date = $(trObj).find("input[name=charge_end_time]").val();
	param.tech_charge_income_flag = $(trObj).find("select[name=tech_charge_income_flag]").val();
	orderModel.discount = $(trObj).find("input[name=discount]").val();
	orderModel.min_consume = $(trObj).find("input[name=min_consume]").val();
	orderModel.min_type = $(trObj).find("select[name=min_type]").val();
	orderModel.max_consume = $(trObj).find("input[name=max_consume]").val();
	orderModel.max_type = $(trObj).find("select[name=max_type]").val();
	orderModel.belong_type = "2";
	orderModel.id = $(trObj).find("input[name=combine_orderModel_id]").val();
	orderModel.ref_id = $(trObj).find("input[name=combine_id]").val();
	orderModelArray = [];
	orderModelArray.push(obj2string(orderModel));
	param.jsonListOrderModel = '['+ orderModelArray.toString() + ']';
	
	orderFeeArray = [];
	var orderFeeTrList = $("#orderFee" + combId).find("tbody").children("tr");
	for(var i = 0; i < orderFeeTrList.length; i++) {
		var order_Payment = {};
		order_Payment.payment_type = $(orderFeeTrList[i]).find("select[name=payment_type]").val();
		order_Payment.display_name = $(orderFeeTrList[i]).find("input[name=display_name]").val();
		order_Payment.advance_date = $(orderFeeTrList[i]).find("input[name=advance_date]").val();
		order_Payment.display_date = $(orderFeeTrList[i]).find("input[name=display_date]").val();
		order_Payment.amount = $(orderFeeTrList[i]).find("input[name=amount]").val();
		order_Payment.id = $(orderFeeTrList[i]).find("input[name=id]").val();
		
		order_Payment.contract_id = $("#addOrderForm").find("input[name=contract_id]").val();
		order_Payment.combine_id = param.id;
		
		orderFeeArray.push(obj2string(order_Payment));
	}
	
	param.jsonListPayment = orderFeeArray.length == 0 ? "[]" : '['+ orderFeeArray.toString() + ']';
	return param;
}

function getProdParam(trObj) {
	var param = {};
	param.id = $(trObj).find("input[name=prod_id]").val();
	param.product_id = $(trObj).find("input[name=product_id]").val();
	param.ex_product_id = $(trObj).find("input[name=ex_product_id]").val();
	param.product_name = $(trObj).find("span[name=product_name_span]").text();
	
	param.contract_id = $("#addOrderForm").find("input[name=contract_id]").val();
	var combineTbody = $(trObj).parent().parent().parent();
	param.combine_id = combineTbody.find("input[name=combine_id]").val();
	
	var prodModel = getProdModelParam(trObj);
	if (!prodModel) {
		return;
	}
	var orderModelArray = [];
	orderModelArray.push(obj2string(prodModel));
	param.jsonListOrderModel = '['+ orderModelArray.toString() + ']';
	
	return param;
}

function getProdModelParam(trObj) {
	var param = {};	
	var specsArray = [];
	param.id = $(trObj).find("input[name=prod_orderModel_id]").val();
	//param.feemodel_id = $(trObj).find("input[name=feemodel_id]").val();
	param.feemodel_name = $(trObj).find("input[name=feemodel_name]").val();
	param.fee_type = $(trObj).find("input[name=fee_type]").val();
	param.fee_formula = $(trObj).find("input[name=fee_formula]").val();
	param.fee_type_name = $(trObj).find("span[name=fee_type_name]").text();
	param.fee_formula_name = $(trObj).find("span[name=fee_formula_name]").text();
	param.fix_charge_type = $(trObj).find("select[name=fix_charge_type]").val();
	param.is_multiplied_actualdays = $(trObj).find("select[name=is_multiplied_actualdays]").val();
	param.belong_type = "3";
	param.ref_id = $(trObj).find("input[name=prod_id]").val();
	
	var fee_formula_val = $(trObj).find("input[name=fee_formula]").val();
	if (fee_formula_val == "1") {
		var specsParam = {};
		specsParam.price = $(trObj).find("input[name=price]").val();
		specsParam.fee_ratio = $(trObj).find("input[name=fee_ratio]").val();
		specsParam.fee_ratio_division = $(trObj).find("input[name=fee_ratio_division]").val();
		specsArray.push(obj2string(specsParam));
	} else if (fee_formula_val == "2" || fee_formula_val == "3" || fee_formula_val == "4") {
		var specsTrList = $(trObj).find("table").find("tbody").children("tr");
		for (var i = 0; i < specsTrList.length; i++) {
			var specsParam = {};
			specsParam.id = $(specsTrList[i]).find("input[name=id]").val();
			specsParam.feemodel_id = $(specsTrList[i]).find("input[name=feemodel_id]").val();
			specsParam.option_name = $(specsTrList[i]).find("input[name=option_name]").val();
			specsParam.step_interval = $(specsTrList[i]).find("select[name=step_interval]").val();
			specsParam.step_unit = $(specsTrList[i]).find("select[name=step_unit]").val();
			//specsParam.step_begin_val = $(specsTrList[i]).find("input[name=step_begin]").val();
			//specsParam.step_end_val = $(specsTrList[i]).find("input[name=step_end]").val();
			specsParam.step_begin = $(specsTrList[i]).find("input[name=step_begin]").val();
			specsParam.step_end = $(specsTrList[i]).find("input[name=step_end]").val();
			
			specsParam.fee_ratio = $(specsTrList[i]).find("input[name=fee_ratio]").val();
			specsParam.fee_ratio_division = $(specsTrList[i]).find("input[name=fee_ratio_division]").val();
			specsParam.max_consume = $(specsTrList[i]).find("input[name=max_consume]").val();
			specsParam.fixed_charge = $(specsTrList[i]).find("input[name=fixed_charge]").val();
			specsArray.push(obj2string(specsParam));
		}
	}
	param.jsonListOrderPrice = specsArray.length == 0 ? "[]" : '['+ specsArray.toString() + ']';
	return param;
}


function dateCompare(beforeDate, afterDate) {
	var beforeArray = beforeDate.split("-");
	var afterArray = afterDate.split("-");
	var beforetime = new Date(beforeArray[0], beforeArray[1] - 1,
			beforeArray[2]);
	var beforetimes = beforetime.getTime();
	var aftertime = new Date(afterArray[0], afterArray[1] - 1, afterArray[2]);
	var aftertimes = aftertime.getTime();
	if (beforetimes < aftertimes) {
		return "-1";
	} else if (beforetimes > aftertimes) {
		return "1";
	} else {
		return "0";
	}
}

//区域画图框架	
var combineIndex = 0;
var productIndex = 0;
var combineControls = [
                       [
                        [ 'calendar', 'charge_begin_time', '计费开始时间', 'WdatePicker({})' ],
                        [ 'calendar', 'charge_end_time', '计费结束时间', 'WdatePicker({})' ],
                        [ 'text', 'discount', '折扣（%）', '' ]
                       ],
                       [
                        [ 'text', 'min_consume', '保底', '' ],
                        [ 'select', 'min_type', '保底类型', '' ],
                        [ 'select', 'tech_charge_income_flag', '产生技术服务费收入', '' ]
                       ],
                       [
                        [ 'text', 'max_consume', '封顶', '' ],
                        [ 'select', 'max_type', '封顶类型', '' ],
                        [ '', '', '', '' ]
                       ]
                      ];
	                 
var combineHiddens = [['hidden','combine_id','', ''],['hidden','combine_orderModel_id','', '']];

function creatCombineTable(combine, combineId) {						
	var combineTable = document.createElement("table");
	combineTable.setAttribute("id", combineId + "_in");
	var buttonTr = combineTable.insertRow(0);
	var buttonTd = buttonTr.insertCell();
	buttonTd.setAttribute("colspan", "3");
	buttonTd.setAttribute("align", "right");
	buttonTd.setAttribute("style", "border-style: none; background: #f5f5f5;");
	buttonTd.innerHTML = '<a href="javascript:void(0)" class="table_button_alink" onclick="deleteCombine(' + combineId + ')">删除组合</a>';
	for (var i = 0; i < combineControls.length; i++) {
		var newTr = combineTable.insertRow(i + 1);
		for (var j = 0; j < combineControls[i].length; j++) {
			var newTd = newTr.insertCell();
			newTd.setAttribute("style", "border-style: none; background: #f5f5f5;");
			newTd.innerHTML = drawControl(combineControls[i][j][0], combineControls[i][j][1], combineControls[i][j][2], combineControls[i][j][3]);
			if (i == 0 && j == 0) {
				newTd.innerHTML = newTd.innerHTML + drawHidden(combineHiddens);
			}
		}
	}
	
	var orderFeeTable = getOrderFeeTable(combineId);
	buttonTr = combineTable.insertRow(combineControls.length + 1);
	buttonTd = buttonTr.insertCell();
	buttonTd.setAttribute("colspan", "3");
	buttonTd.setAttribute("style", "border-style: none; background: #f5f5f5;");
	buttonTd.setAttribute("align", "middle");
	buttonTd.appendChild(orderFeeTable[0]);
	//组合验证信息
	buttonTr = combineTable.insertRow(combineControls.length + 2);
	buttonTd = buttonTr.insertCell();
	buttonTd.setAttribute("colspan", "3");
	buttonTd.setAttribute("style", "border-style: none; background: #f5f5f5;");
	buttonTd.setAttribute("align", "left");
	buttonTd.innerHTML = '<div name="orderValidation_2" class="orderValidation"></div>';
	
	buttonTr = combineTable.insertRow(combineControls.length + 3);
	buttonTd = buttonTr.insertCell();
	buttonTd.setAttribute("colspan", "3");
	buttonTd.setAttribute("style", "border-style: none; background: #f5f5f5;padding-left: 10px;");
	buttonTd.setAttribute("align", "left");
	buttonTd.innerHTML = '<a href="javascript:void(0)" class="table_button_alink" onclick="creatProductTable(' + combineId + ')">添加产品</a>';
	combine.appendChild(combineTable);
}
var tmpCount = 1;

var productControls = [
                       [
                        [ 'proButton', 'product_id', '协同销售产品', '' ],
                        [ 'label', 'product_name_span', '产品名称', '' ],
                        [ '', '', '', '' ]
                       ],
                       [
                        [ 'modelButton', 'feemodel_name', '计费模式', '' ],
                        [ 'label', 'fee_type_name', '计费类型', '' ],
                        [ 'label', 'fee_formula_name', '计费公式', '' ]
                       ],
                       [	
                        [ 'select', 'fix_charge_type', '结算类型', '' ],
                        [ 'select', 'is_multiplied_actualdays', '是否乘月天数', '' ],
                        [ '', '', '', '' ]
                       ]
                      ];

var productHiddens = [['hidden','ex_product_id','', ''],['hidden','prod_id','', '']];						
var orderModelHiddens = [['hidden','fee_type','', ''],['hidden','fee_formula','', ''],['hidden','prod_orderModel_id','', '']];
function creatProductTable(combineId) {
	if($(combineId).children("table").length>0){
		//组合增加一行放产品
		var combineTable = $(combineId).children("table")[0];
		var rowLength = combineTable.rows.length;
		var prodTr = combineTable.insertRow(rowLength);
		var prodTd = prodTr.insertCell();
		prodTd.setAttribute("style", "border-style: none; background: #f5f5f5;");
		prodTd.setAttribute("colspan", "3");
		prodTd.setAttribute("align", "middle");
		//生成产品的table
		var productTable = document.createElement("table");
		var productId = "product" + getProductIndex();
		productTable.setAttribute("id", productId);
		productTable.setAttribute("name", "prodTable");
		productTable.setAttribute("style", "width: 99%;background: white;");

		var buttonTr = productTable.insertRow(0);
		var buttonTd = buttonTr.insertCell();
		buttonTd.setAttribute("colspan", "3");
		buttonTd.setAttribute("align", "right");
		buttonTd.setAttribute("style", "border-style: none;");
		buttonTd.innerHTML = '<a href="javascript:void(0)" class="table_button_alink" onclick="deleteProduct(this)">删除产品</a>';
		
		var productColumn= productControls;
		for (var i = 0; i < productColumn.length; i++) {
			var newTr = productTable.insertRow(i + 1);
			if(i!=0){//第0行是产品
				newTr.setAttribute("class", "order_model_tr");
			}
			for (var j = 0; j < productColumn[i].length; j++) {
				var newTd = newTr.insertCell();
				newTd.setAttribute("style", "border-style: none;");
				newTd.innerHTML = drawControl(productColumn[i][j][0], productColumn[i][j][1], productColumn[i][j][2], productColumn[i][j][3]);
				if (i == 0 && j == 0) {
					newTd.innerHTML = newTd.innerHTML + drawHidden(productHiddens);
				}
				if (i == 1 && j == 0) {
					newTd.innerHTML = newTd.innerHTML + drawHidden(orderModelHiddens);
				}
			}
		}
		
		//产品验证信息
		buttonTr = productTable.insertRow(productColumn.length + 1);
		buttonTd = buttonTr.insertCell();
		buttonTd.setAttribute("colspan", "3");
		buttonTd.setAttribute("align", "left");
		buttonTd.setAttribute("style", "border-style: none;");
		buttonTd.innerHTML = '<div name="orderValidation_3" class="orderValidation"></div>';
		
		prodTd.appendChild(productTable);
		tmpCount++;
	}
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
		controlText = '<div id = "' + options + '" class="specsHead"><span>' + label + '</span><input class="table_input_num" name="' + name + '" type="text" value=""></div>';
	} else if (type == "select") {
		if(name == "fix_charge_type"){
			controlText = '<div class="specsHead"><span>' + label + '</span><select class="table_select" name="' + name + '" onchange="fixChargeTypeCharge(this)">';
			controlText = controlText + '<option value="1">日结</option><option value="2">月结</option>';
			controlText = controlText + '</select></div>';
		} else if (name == "is_multiplied_actualdays") {
			controlText = '<div class="specsHead"><span>' + label + '</span><select class="table_select" name="' + name + '" disabled="disabled">';
			controlText = controlText + '<option value="0">否</option><option value="1">是</option>';
			controlText = controlText + '</select></div>';
		} else if (name == "min_type") {
			controlText = '<div id = "' + options + '" class="specsHead"><span>' + label + '</span><select class="table_select" name="' + name + '" >';
			controlText = controlText + '<option value="">无</option><option value="1">按年</option><option value="2">按月</option><option value="3">按日</option><option value="4">按不定期</option>';
			controlText = controlText + '</select></div>';
		}else if (name == "max_type") {
			controlText = '<div id = "' + options + '" class="specsHead"><span>' + label + '</span><select class="table_select" name="' + name + '" >';
			controlText = controlText + '<option value="">无</option><option value="1">按年</option><option value="2">按月</option><option value="3">按日</option><option value="4">按不定期</option>';
			controlText = controlText + '</select></div>';
		}else if (name == "tech_charge_income_flag") {
			controlText = '<div id = "' + options + '" class="specsHead"><span>' + label + '</span><select class="table_select" name="' + name + '" >';
			controlText = controlText + '<option value="1">是</option><option value="0">否</option>';
			controlText = controlText + '</select></div>';
		}
   	} else if (type == "herf") {
   		controlText = '<a href="javascript:void(0)" class="table_button_alink" onclick="'+ options +'">'+ label +'</a>';
   	} else if (type == "button") {
   		controlText = '<div class="specsHead"><span>'+ label +'</span><input class="table_input_string" name="'+ name +'" type="text" value="" readonly="readonly"><input class="detailButton" type="button" value="选择" onclick="'+ options +'"></div>';	
   	} else if (type == "label") {
   		controlText = '<div class="specsHead"><span>' + label + '</span><span style="width:220px;" name = "' + name + '"></span></div>';
   	} else if (type == "calendar") {
   		controlText = '<div class="specsHead"><span>'+ label +'</span><input class="Wdate" type="text" name="' + name + '" readonly="true" onFocus="' + options + '"/></div>';
   	}else if (type == "proButton") {
   		controlText = '<div class="specsHead"><span>'+ label +'</span><input class="table_input_string" name="'+ name +'" type="text" value="" readonly="readonly" onclick="xtProdChooserShow(this)"></div>';	
   	}else if (type == "modelButton") {
   		controlText = '<div class="specsHead"><span>'+ label +'</span><input class="table_input_string" name="'+ name +'" type="text" value="" readonly="readonly" onclick="modelChooserShow(this)"></div>';	
   	}
	return controlText;
}
  	  
function getCombineIndex(){
	combineIndex = combineIndex + 1;
	return combineIndex;
}
	  
function getProductIndex(){
	productIndex = productIndex + 1;
	return productIndex;
}
	  							
function createCombine(){
	var combines = document.getElementById("orderAddDiv");
	var combine = document.createElement("div");
	var combineId = "combine" + getCombineIndex();
	combine.setAttribute("id", combineId);
	combine.setAttribute("name", "combineDiv");
	creatCombineTable(combine, combineId);
	combines.appendChild(combine);
}

function deleteCombine(combineId){
	top.$.jBox.confirm("确认删除组合信息?","提示",function(v,h,f){
		if(v=='ok'){
			combineId.parentNode.removeChild(combineId);
		}
	},{buttonsFocus:1});
	top.$('.jbox-body .jbox-icon').css('top','55px');
}
	  
function deleteProduct(obj){
	top.$.jBox.confirm("确认删除产品信息?","提示",function(v,h,f){
		if(v=='ok'){
			var prodTable = obj.parentNode.parentNode.parentNode.parentNode;
			var combineTbody = prodTable.parentNode.parentNode.parentNode;
			var prodTr = prodTable.parentNode.parentNode;
			combineTbody.removeChild(prodTr);
		}
	},{buttonsFocus:1});
	top.$('.jbox-body .jbox-icon').css('top','55px');
}

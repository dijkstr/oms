
function feeFormulaCharge(){
	var fee_formula = $("#fee_formula").val();
	$("#price").val("");
	$("#fee_ratio").val("");
	$("#fee_ratio_division").val("");
	$("#body_specsDataGrid").empty();
	if(fee_formula =="1"){
		$("#formula_common").show();
		$("#specs").hide();
	}else if(fee_formula =="2"||fee_formula =="3"||fee_formula =="4"){
		$("#formula_common").hide();
		$("#specs").show();
	}else{
		$("#formula_common").hide();
		$("#specs").hide();
	}
}


function fixChargeTypeCharge(){
	var fix_charge_type = $("#fix_charge_type").val();
	if(fix_charge_type=="1"){
		$("#is_multiplied_actualdays").val("0");
		$("#is_multiplied_actualdays").parent().find("span").text("否");
		$("#is_multiplied_actualdays").attr("disabled", true);
	}else if(fix_charge_type=="2"){
		$("#is_multiplied_actualdays").attr("disabled", false);
	}
}

//新增产品保存，得到产品信息
function submitChargeModelParam(){

	var chargePriceArray=[];
	var fee_formula = $("#fee_formula").val();
	if(fee_formula == "2"||fee_formula =="3"||fee_formula =="4"){//计费公式-阶梯
		var priceTrList = $("#body_specsDataGrid").find("tr");
		for(var i=0;i<priceTrList.length;i++){
			var param={};
			param.option_name = $(priceTrList[i]).find("input[name=option_name]").val();
			param.step_interval = $(priceTrList[i]).find("select[name=step_interval]").val();
			param.step_unit = $(priceTrList[i]).find("select[name=step_unit]").val();
			param.step_begin = $(priceTrList[i]).find("input[name=step_begin]").val();
			param.step_end = $(priceTrList[i]).find("input[name=step_end]").val();
			param.fee_ratio = $(priceTrList[i]).find("input[name=fee_ratio]").val();
			param.fee_ratio_division = $(priceTrList[i]).find("input[name=fee_ratio_division]").val();
			param.max_consume = $(priceTrList[i]).find("input[name=max_consume]").val();
			param.fixed_charge = $(priceTrList[i]).find("input[name=fixed_charge]").val();
			param.id = $(priceTrList[i]).find("input[name=id]").val();
			param.feemodel_id = $(priceTrList[i]).find("input[name=feemodel_id]").val();
			chargePriceArray.push(obj2string(param));
		}
	}else if(fee_formula == "1"){//计费公式-普通
		var param ={};
		param.price= $("#formula_common").find("input[name=price]").val();
		param.fee_ratio= $("#formula_common").find("input[name=fee_ratio]").val();
		param.fee_ratio_division= $("#formula_common").find("input[name=fee_ratio_division]").val();
		param.id= $("#formula_common").find("input[name=id]").val();
		param.feemodel_id= $("#formula_common").find("input[name=feemodel_id]").val();
		chargePriceArray.push(obj2string(param));
	}
	var chargePriceJson = chargePriceArray.length==0 ?"": '['+chargePriceArray.toString()+']';
	
	return chargePriceJson;
}


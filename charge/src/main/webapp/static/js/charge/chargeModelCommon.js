var specialReg=/^[^`%&+/?]{1}[^`%&+?]{0,19}$/;

function getTrStr(index){
	var str = '<tr id="tr_specsDataGrid_add" style="HEIGHT:22px" class="tr_insertDataGrid_add" index = "'+index+'">' +
	    '<td ><input type="text" name="option_name" onMouseOver="this.title=this.value;" value="" maxlength="50" class="table_input_string"/></td>' +
		'<td ><select name="step_interval" onchange="itemSelectChange(this)" style="width:125px;"><option value="1">MIN&lt;N&lt;=MAX</option><option value="2">MIN&lt;=N&lt;MAX</option></select></td>' +
		'<td ><select name="step_unit" onchange="itemSelectChange(this)" style="width:60px;"><option value="0">无</option><option value="1">万</option><option value="2">亿</option></select></td>' +
		'<td ><input type="text" name="step_begin" onMouseOver="this.title=this.value;" value="" class="table_input_num"/></td>' +
	    '<td ><input type="text" name="step_end" onMouseOver="this.title=this.value;" value="" class="table_input_num"/></td>' +
		'<td ><input type="text" name="fee_ratio" onMouseOver="this.title=this.value;" value="" class="table_input_num"/></td>' +
		'<td ><input type="text" name="fee_ratio_division" onMouseOver="this.title=this.value;" value="" class="table_input_num"/></td>' +
		'<td ><input type="text" name="max_consume" onMouseOver="this.title=this.value;" value="" class="table_input_num"/></td>' +
		'<td ><input type="text" name="fixed_charge" onMouseOver="this.title=this.value;" value="" class="table_input_num"/></td>' +
		'<td style="display:none"><input type="text" name="id" value=""/><input type="text" name="feemodel_id" value=""/></td>' +
	   '</tr>';
	var specsTr = $(str);
	var td = $("<td class=\"detailButton\"><a onclick=\"delSpecItem(this)\" class=\"table_button_alink\" style=\"width: 55px;\">删除阶梯</a></td>");
	td.appendTo(specsTr);
	return specsTr;
}

function itemSelectChange(obj){
	var val = $(obj).val();
	var name = $(obj).attr("name");
	$(obj).parent().parent().parent().find("select[name='"+name+"']").val(val);
}


//新增规格选项
function addSpecItem(obj){
	var specsTbody = $(obj).parent().parent().parent().parent().next("tbody");
	var indexArray = specsTbody.find("tr").attr("index");
	var index_max = 0;
	if(indexArray != null){
		for(var i=0;i<indexArray.length;i++){
			if(indexArray[i]>index_max){
				index_max =parseInt(indexArray[i]) ;
			}
		}
	}
	var tr = getTrStr(index_max+1);
	specsTbody.prepend(tr);
	if(specsTbody.find("tr").length>1){
		var tr0=specsTbody.find("tr")[1];
		var step_interval_0 = $(tr0).find("select[name=step_interval]").val();
		$(tr).find("select[name='step_interval']").val(step_interval_0);
		var step_unit_0 = $(tr0).find("select[name=step_unit]").val();
		$(tr).find("select[name='step_unit']").val(step_unit_0);
	}
}

//删除规格选项
function delSpecItem(obj){
	top.$.jBox.confirm("确认删除规格选项?","提示",function(v,h,f){
		if(v=='ok'){
			$(obj).parent("td").parent("tr").remove();
		}
	},{buttonsFocus:1});
	top.$('.jbox-body .jbox-icon').css('top','55px');
}

function validateEmpty(list,str){
	for(var i=0;i<list.length;i++){
		if($(list[i]).val()==""){
			alert(str);
			return false;
		}
	}
	return true;
}

function typeConsumeValid(type,consume,str){
	if(type == "" && consume != ""){
		Horn.Msg.warning("提示","请选择"+str+"类型！");
		return false;
	}
	if(type != "" && consume == ""){
		Horn.Msg.warning("提示","请输入"+str+"金额！");
		return false;
	}
	return true;
}

var numReg = /^(-)?\d+(\.\d{0,8})?$/;
function validateOneNum(obj){
	var val = $(obj).val();
	if(val!=""){
		if(val.indexOf(".") != -1 && val.length>26){
			Horn.Msg.warning("提示",val+"金额格式不正确");
			return false;
		}
		if(val.indexOf(".") == -1 && val.length>25){
			Horn.Msg.warning("提示",val+"金额格式不正确");
			return false;
		}
		if(!numReg.test(val)){
			Horn.Msg.warning("提示",val+"金额格式不正确");
			return false;
		}
	}
	return true;
}

function changeOneNum(obj,count){
	var val = $(obj).val();
	if(val!=""){
		if(count == 5){
			$(obj).val(parseFloat(val).toFixed(5));
		}else{
			$(obj).val(parseFloat(val).toFixed(2));
		}
	}
}

function cellNumRenderer(td){
	var mount="";
	if(td.val!=""){
		mount = parseFloat(td.val).toFixed(2);
	}
	return '<span title=\"'+mount+'\">'+mount+'</span>';
}

//尖括号的转义
function signView(str){
	while(str.indexOf("<") != -1){
		str = str.replace("<","&lt;");
	}
	while(str.indexOf(">") != -1){
		str = str.replace(">","&gt;");
	}
    return str;
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

//********  obj2string   **********//
function obj2string(o){
	var r=[];
	if(typeof o=="string"){
    	return "\""+o.replace(/([\"\\])/g,"\\$1").replace(/(\n)/g,"\\n").replace(/(\r)/g,"\\r").replace(/(\t)/g,"\\t")+"\"";
	}
	if(typeof o=="object"){
    	if(!o.sort){
        	for(var i in o){
            	r.push('"'+i+'":'+obj2string(o[i]));
        	}
        	if(!!document.all&&!/^\n?function\s*toString\(\)\s*\{\n?\s*\[native code\]\n?\s*\}\n?\s*$/.test(o.toString)){
            	r.push("toString:"+o.toString.toString());
        	}
        	r="{"+r.join()+"}";
    	}else{
        	for(var i=0;i<o.length;i++){
            	r.push(obj2string(o[i]))
        	}
        	r="["+r.join()+"]";
    	}
    	return r;
	}
	return o.toString();
}
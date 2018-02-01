<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<html>
<head>
	<meta name="decorator" content="default"/>
	<title>计费模式管理</title>
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
		}
		
		.table_input_string{width:130px;}
		.table_input_num{width:85px;}
		.table_select{width:95px;}
	</style>
	<script src="/charge/static/js/charge/chargeModelCommon.js" type="text/javascript"></script>
	<script src="/charge/static/js/charge/chargeModel.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate();
			
			fixChargeTypeCharge();
			chargeTypeInit('${chargeModelForm.fee_type}', '${chargeModelForm.classify_id}');
			
			$("#addModelForm").after('${html}');
		});
		
		function getChargeType(classifyId){
			var url = "${ctx}/charge/setting/chargeType/getChargeTypeList";
			var param = {"id":classifyId};
			var result = ajaxFunction(url,param);
			if(result.result=="success"){
				$("#fee_type_div a span").text("请选择");
				$("#fee_type option:checked").attr("selected", "");
				$("#fee_type option").remove();
				var str = '<option value="" selected="selected">请选择</option>';
				if(result.list != null){
					var typeList = result.list;
					for(var i=0;i<typeList.length;i++){
						str = str + '<option value="'+typeList[i].value+'">'+typeList[i].label+'</option>';
					}
				}
				$("#fee_type").append(str); 
			}else{
				top.$.jBox.alert(data.message,"提示:");
				return;
			}
			
		}
		
		function chargeTypeInit(val, classifyId){
			var url = "${ctx}/charge/setting/chargeType/getChargeTypeList";
			var param = {"id":classifyId};
			var result = ajaxFunction(url,param);
			if(result.result=="success"){
				$("#fee_type option").remove();
				var str = '<option value="">请选择</option>';
				var type_name="请选择";
				if(result.list != null){
					var typeList = result.list;
					for(var i=0;i<typeList.length;i++){
						str = str + '<option value="'+typeList[i].value+'">'+typeList[i].label+'</option>';
						if(val == typeList[i].value){
							type_name = typeList[i].label;
						}
					}
				}
				$("#fee_type").append(str); 
				$("#fee_type").val(val);
				$("#fee_type_div a span").text(type_name);
			}else{
				top.$.jBox.alert(data.message,"提示:");
				return;
			}
			
		}
		
		function submitChargeModel(){
			$("#is_multiplied_actualdays").attr("disabled", false);
			var param = submitChargeModelParam();
			if(param==undefined || param==false){
			}else{
				$("#listJson").val(param);
			}
			$("#addModelForm").submit();
			
		}
		
		//计费分类选择器
		function chargeModelChooserShow(type){
			top.$.jBox.open("iframe:${ctx}/charge/setting/chargeModel/chargeModelClassifyChooser", "计费分类选择器",810,$(top.document).height()-240,{
				buttons:{"确定选择":"ok", "关闭":true},submit:function(v, h, f){
					if (v=="ok"){
						var chargeModelClassify = h.find("iframe")[0].contentWindow.getChargeModelClassify();
						$("#addModelForm").find("input[name='classify_id']").val(chargeModelClassify.chargeModelClassifyId);
						$("#addModelForm").find("input[name='classify_name']").val(chargeModelClassify.chargeModelClassifyName);
						getChargeType(chargeModelClassify.chargeModelClassifyId);
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
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/charge/setting/chargeModel/">计费模式列表</a></li>
		<li class="active"><a href="${ctx}/charge/setting/chargeModel/form?id=${chargeModelForm.id}">计费模式<shiro:hasPermission name="charge:setting:chargeModel:edit">${not empty chargeModelForm.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="charge:setting:chargeModel:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	
	<form:form id="addModelForm" name="addModelForm" modelAttribute="chargeModelForm" action="${ctx}/charge/setting/chargeModel/save" method="post" class="form-horizontal" enctype="application/x-www-form-urlencoded">
		<form:hidden path="id"/>
		<input type="hidden" id="listJson" name="listJson">
		<form:hidden path="updateDate"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<div class="form_tr_div">
				<label class="control-label" for="classify">模式分类:</label>
				<form:input path="classify_name" htmlEscape="false" maxlength="200" class="required" style="width:150px" 
					onclick="chargeModelChooserShow(1)" value="${classify.classify_name}"/>
					<input type="hidden" id="classify_id" name="classify_id" value="${chargeModelForm.classify_id}" >
			</div>
			<div class="form_tr_div" id="fee_type_div">
				<label class="control-label" for="name">计费类型:</label>
				<form:select path="fee_type" class="input-medium" >
					<form:option value="" label="请选择"/>
					<form:options items="${charge:getChargeTypeList('fee_type')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">模式名称:</label>
				<form:input path="model_name" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">保底类型:</label>
				<form:select path="min_type" class="input-medium">
					<form:option value="" label="无"/>
					<form:options items="${fns:getDictList('min_type')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">保底金额:</label>
				<form:input path="min_consume" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">封顶类型:</label>
				<form:select path="max_type" class="input-medium">
					<form:option value="" label="无"/>
					<form:options items="${fns:getDictList('max_type')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">封顶金额:</label>
				<form:input path="max_consume" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">折扣(%):</label>
				<form:input path="discount" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">计费公式:</label>
				<form:select path="fee_formula" class="input-medium" onchange="feeFormulaCharge()">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('fee_formula')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">结算类型:</label>
				<form:select path="fix_charge_type" class="input-medium" onchange="fixChargeTypeCharge()">
					<form:options items="${fns:getDictList('fix_charge_type')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">是否乘月天数:</label>
				<form:select path="is_multiplied_actualdays" class="input-medium">
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			
			<div class="form_tr_div2">
				<label class="control-label" for="remarks">备注:</label>
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge"/>
			</div>
		</div>
		</form:form>
		
	
	
		<div class="form-actions">
			<shiro:hasPermission name="charge:setting:chargeModel:edit">
				<input id="btnSubmit" class="btn btn-primary" type="button" value="保 存" onclick="submitChargeModel()"/>&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	
</body>
</html>

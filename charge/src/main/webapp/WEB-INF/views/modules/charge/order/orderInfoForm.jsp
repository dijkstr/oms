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
		
		.orderValidation{margin-left: 15px; margin-top: 8px; color: red; font-family: "微软雅黑"; font-size: 14px;}
		.orderValidation p{clear: both; overflow: hidden;}
		.orderValidation p span{float: left;width: 20px;}
		.orderValidation p em{float: left; width: 90%; font-style: normal;}
		.orderValidation_title{float: left; margin-right: 6px; width: 7%;}
		.orderValidation_text{float: left; width: 90%;}
		
		.form-actions {text-align: center; background: none; border-top: none; padding: 0;}
	</style>
	<script src="/charge/static/js/charge/chargeModelCommon.js" type="text/javascript"></script>
	<script src="/charge/static/js/charge/orderTable.js" type="text/javascript"></script>
	
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate();
			
			$("#orderAddDiv").append('${html}');
			if($("#id").val() != ""){
				$("#addOrderForm").find("div[name=orderDate]").show();
				if($("div[name=combineDiv]") != null){
					combineIndex = $("div[name=combineDiv]").length;
				}
			}else{
				$("#addOrderForm").find("div[name=orderDate]").hide();
			}
			$("#termination_status").attr("readonly", true);
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
				$("#form_ul").append('<li class="active"><a>计费合同查看</a></li>');
				$("body").attr("style","padding-left: 80px;");
			}
			if(shiroPermission == "orderIncomeSetting"){
				top.$.jBox.confirm("确认是否进入收入设置画面?","提示",function(v,h,f){
					if(v=='ok'){
						window.location.href="${ctx}/charge/order/orderInfo/incomeSettingForm?"
								+"contract_id=${orderInfoForm.contract_id}&customer_id=${orderInfoForm.customer_id}&hs_customername=${orderInfoForm.hs_customername}";
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			}
		});
		
		//计费模式选择器
		function modelChooserShow(obj){
			top.$.jBox.open("iframe:${ctx}/charge/setting/chargeModel/chargeModelChooser", "计费模式选择器",900,$(top.document).height()-200,{
				buttons:{"确定选择":"ok", "关闭":true},submit:function(v, h, f){
					if (v=="ok"){
						var chargeModelId = h.find("iframe")[0].contentWindow.getChargeModelId();
						var url = "${ctx}/charge/setting/chargeModel/getChargeModelById";
						var param = {"id":chargeModelId};
						var result = ajaxFunction(url,param);
						if(result.result=="success"){
							var prodTbody = $(obj).parent().parent().parent().parent();
							$(prodTbody).find("tr[class=order_model_tr]").remove();
							$($(prodTbody).find("tr")[1]).after(result.html);
						}else{
							Horn.Msg.warning("#springMessage('alert')",data.message);
							return;
						}
				    	return true;
					}
				}, loaded:function(h){
					$(".jbox-content", top.document).css("overflow-y","hidden");
				}
			});
		}
		
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
							$("#addOrderForm").find("input[name='signeddate']").val(contractInfo.signeddate);
							$("#addOrderForm").find("input[name='customermanagername']").val(contractInfo.customermanagername);
							$("#addOrderForm").find("input[name='customername']").val(contractInfo.realcustomername);
							$("#addOrderForm").find("input[name='reporttype_id']").val(contractInfo.reporttype_id);
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
		
		//协同产品选择器
		function xtProdChooserShow(obj){
			var contractid=$("#addOrderForm").find("input[name=contract_id]").val();
			top.$.jBox.open("iframe:${ctx}/charge/common/syncProductChooser?contractid="+contractid, "协同产品选择器",810,$(top.document).height()-240,{
				buttons:{"确定选择":"ok", "关闭":true},submit:function(v, h, f){
					if (v=="ok"){
						var productInfo = h.find("iframe")[0].contentWindow.getProductInfo();
						var prodTr = $(obj).parent().parent().parent();
						$(prodTr).find("input[name='product_id']").val(productInfo.product_id);
						$(prodTr).find("input[name='ex_product_id']").val(productInfo.ex_product_id);
						$(prodTr).find("span[name='product_name_span']").text(productInfo.product_name);
				    	return true;
					}
				}, loaded:function(h){
					$(".jbox-content", top.document).css("overflow-y","hidden");
				}
			});
		}
		
		function submitChargeOrder(){
			var orderCombArray = getOrderCombListParam();
			if (!orderCombArray) {
				return;
			}
			$("#jsonListOrderCombie").val(obj2string(orderCombArray));
			$("#order_begin_date").val("");
			$("#order_end_date").val("");
			$("#addOrderForm").submit();
		}
		
		function backBtn(){
			window.location.href="${ctx}/charge/order/orderInfo/";
		}
		
	</script>
	
	
</head>
<body>
	<ul class="nav nav-tabs" id="form_ul">
		<li><a href="${ctx}/charge/order/orderInfo/">计费合同列表</a></li>
		<li class="active"><a href="${ctx}/charge/order/orderInfo/form?id=${orderInfo.id}">计费合同<shiro:hasPermission name="charge:order:orderInfo:edit">${not empty orderInfo.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="charge:order:orderInfo:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	
	<form:form id="addOrderForm" name="addOrderForm" modelAttribute="orderInfoForm" action="${ctx}/charge/order/orderInfo/save" method="post" class="form-horizontal" enctype="application/x-www-form-urlencoded">
		<form:hidden path="id"/>
		<input type="hidden" id="jsonListOrderCombie" name="jsonListOrderCombie">
		<input type="hidden" id="order_begin_date" name="order_begin_date">
		<input type="hidden" id="order_end_date" name="order_end_date">
		<shiro:hasPermission name="charge:audit:orderInfoAudit:verify">
		<input type="hidden" id="is_verify" name="is_verify">
		</shiro:hasPermission>
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
				<label class="control-label" for="name">销售经理:</label>
				<form:input path="customermanagername" htmlEscape="false" maxlength="200" class="required" style="width:150px" readonly="true"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">实际使用客户:</label>
				<form:input path="customername" htmlEscape="false" maxlength="200" class="required" style="width:150px" readonly="true"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">填报类型:</label>
				<input id="reporttype_id" name="reporttype_id" style="width:150px" readonly="readonly" type="text" value="${fns:getDictLabel(orderInfoForm.reporttype_id,"synccontract_reporttype","")}">
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">收入来源:</label>
				<form:select path="income_source" class="input-medium">
					<form:options items="${fns:getDictList('income_source')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">缴费日期:</label>
				<form:input path="pay_deadline" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">框架合同编号:</label>
				<form:input path="frame_contract_id" htmlEscape="false" maxlength="200" class="required" style="width:150px" onclick="contractChooserShow(2)"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">是否允许发送邮件:</label>
				<form:select path="is_send" class="input-medium">
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">合同签订日期:</label>
				<form:input path="signeddate" value="${fn:substring(orderInfoForm.signeddate,0,10)}" htmlEscape="false" maxlength="200" class="required" style="width:150px" readonly="true"/>
			</div>
			<div class="form_tr_div" name="orderDate" style="height: 30px;">
				<label class="control-label" for="name">合同开始日期:</label>
				${fn:substring(orderInfo.order_begin_date,0,10)}
			</div>
			<div class="form_tr_div" name="orderDate" style="height: 30px;">
				<label class="control-label" for="name">合同结束日期:</label>
				${fn:substring(orderInfo.order_end_date,0,10)}
			</div>
			<div class="form_tr_div" style="height: 30px;">
				<label class="control-label" for="name">合同状态:</label>
				${fns:getDictLabel(orderInfo.order_status,"order_status","")}
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">合同到期提醒:</label>
				<form:select path="cc_flag" class="input-medium">
					<form:options items="${fns:getDictList('cc_flag')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div class="form_tr_div" style="height: 30px;">
				<label class="control-label" for="name">创建人:</label>
				${orderInfo.createBy.name}
			</div>
			<div class="form_tr_div" style="height: 30px;">
				<label class="control-label" for="name">创建时间:</label>
				${orderInfo.createDate}
			</div>
			<div class="form_tr_div" style="height: 30px;">
				<label class="control-label" for="name">修改人:</label>
				${orderInfo.updateBy.name}
			</div>
			<div class="form_tr_div" style="height: 30px;">
				<label class="control-label" for="name">更新时间:</label>
				${orderInfo.updateDate}
			</div>
			<div class="form_tr_div2">
				<label class="control-label" for="remarks">备注:</label>
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge"/>
			</div>
			
			<div name="orderValidation_1" class="orderValidation">
				<div class="orderValidation_text">
					<c:forEach items="${orderInfoForm.orderValidates}" var="orderValidate" varStatus="status">
						<P><span>${status.index + 1}.</span><em>${orderValidate.reason}</em></P>
					</c:forEach>
				</div>
			</div>	
		</div>
		</form:form>
		<div class="form-actions">
			<shiro:hasPermission name="charge:order:orderInfo:edit">
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
								<a href="javascript:void(0)" class="table_button_alink" onclick="createCombine()">添加组合</a>
							</div>
						</th>
					</tr>
				</thead>
				<tbody id="body_orderAdd"></tbody>
			</table>
			
		</div>
		
</body>
</html>


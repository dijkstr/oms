<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>合同校验信息管理</title>
<meta name="decorator" content="default" />

<style type="text/css">
.form_tr_div {
	width: 33%;
	height: 34px;
	float: left;
	margin-bottom: 5px;
	padding-bottom: 4px;
}

.form_tr_div .input-small {
	width: 140px;
}

.select2-container.input-medium {
	margin-left: -4px;
}

.form_tr_div label {
	width: 28%;
}

.table_button_alink {
	margin-right: 5px;
	padding: 0 10px;
	line-height: 24px;
	height: 24px;
	border: 1px solid #dbdbdb;
	-webkit-border-radius: 3px;
	display: inline-block;
	background-color: #e0e0e0;
}

</style>


<script type="text/javascript">
$(document).ready(function() {
	$("#btnReset").on("click",function(){
		$("#searchForm input:not([type='submit'],[type='button'])").val("");
		$("#searchForm select option:selected").attr("selected", false);
		$("#searchForm a span").text("请选择");
		$("#pageNo").val("1");
		$("#pageSize").val("10");
	});
	
});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	//显示
	function  showValidate(serial_no,remarks){
		var html = "<div class='content'><h>备注：</h><textarea id='remark' name='remark' rows='5'>"+remarks+"</textarea></div>"; 
		var submit = function (v, h, f) { 
			var beizhu="";
			 if (f.remark != '') { 
			    	//设置备注
			    	beizhu=f.remark;
			    }
			var status="0";
			top.$.jBox.confirm("确定要显示此条合同校验吗?", "确认", function(v, h, f){
				if (v === 'ok') {
		    		var url="${ctx}/charge/order/validate/form?serial_no="+serial_no+"&status="+status+"&remark="+beizhu;
		    		
		    		var data = ajaxFunction(url);
		    		
		    		if (data.result == "success") {
		    			top.$.jBox.alert(data.message);
		    			//刷新页面
						location.reload();
		    		}
		    		
		    		
				} else if (v == 'cancel'){
					top.$.jBox.tip("cancel", 'info');
	            }
				
				return true;
	    		
			});
			
		}; 
		 
		top.$.jBox(html,{ title: "信息显示",width: 350,height: 350, submit: submit });
	}
	//过滤
	function  filterValidate(serial_no,remarks){ 
		
		var html = "<div class='content'><h>备注：</h><textarea id='remark' name='remark' rows='5'>"+remarks+"</textarea></div>"; 
		var submit = function (v, h, f) { 
			var beizhu="";
			 if (f.remark != '') { 
			    	//设置备注
			    	beizhu=f.remark;
			    }
			var status="1";
			top.$.jBox.confirm("确定要过滤此条合同校验吗?", "确认", function(v, h, f){
				if (v === 'ok') {
		    		var url="${ctx}/charge/order/validate/form?serial_no="+serial_no+"&status="+status+"&remark="+beizhu;
		    		
		    		var data = ajaxFunction(url);
		    		
		    		if (data.result == "success") {
		    			top.$.jBox.alert(data.message);
		    			//刷新页面
		    			location.reload();
		    		}
		    		
		    		
				} else if (v == 'cancel'){
					top.$.jBox.tip("cancel", 'info');
	            }
				
	    		return true;
			});
			
		  
		}; 
		 
		top.$.jBox(html,{ title: "信息过滤",width: 350,height: 350, submit: submit });
		
	}
	//导出
	function  exportInfo(){
		var params = $("#searchForm").serialize();
    	window.open("${ctx}/charge/order/validate/export/download?templatekey=orderValidate&" 
    			+ (encodeURI(params)));
		
	}
	//合同校验
	function  orderValidate(){
		var url="${ctx}/charge/order/validate/ordervalidate";
		var data = ajaxFunction(url);
		if (data.result == "success") {
			top.$.jBox.alert(data.message);
			
		}
		//刷新页面
		location.reload();
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
	//查看协同合同详情
	function viewContract(billId){
		var url = "${ctx}/charge/sync/contract/form?contractid=" + billId;
		window.open(url);
	}
	//查看协同客户
	function viewCustomer(customerId){
		var url = "${ctx}/charge/sync/customer/list?customerid=" + customerId;
		window.open(url);
	}
	
</script>

</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/charge/order/validate/">合同校验信息</a></li>
		<!--<shiro:hasPermission name="charge:order:validate:edit"><li><a href="${ctx}/charge/order/validate/form">协同合同添加</a></li></shiro:hasPermission>-->
	</ul>
	<form:form id="searchForm" modelAttribute="orderValidateForm"
		action="${ctx}/charge/order/validate/" method="post"
		class="breadcrumb form-search"  style="height:120px;">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<div class="control-group" >
			<div class="form_tr_div">
				<label>所属部门 ：</label>
				<tags:treeselect id="office" name="order_source"
					value="${office.code}" labelName="office_name"
					labelValue="${office.name}" title="部门"
					url="/sys/office/treeData?type=2" cssClass="input-small"
					allowClear="true" idVal="code" />
			</div>
			<div class="form_tr_div">
				<label>校验类型：</label>
				<form:select path="hs_check_status" class="input-medium"
					style="width: 155px;">
					<form:option value="" label="请选择" />
					<form:options items="${fns:getDictList('hs_check_status')}"
						itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div class="form_tr_div">
				<label>协同合同编号：</label>
				<form:input path="hs_contract_id" htmlEscape="false"
					class="input-small" />
			</div>
			<div class="form_tr_div">
				<label>协同产品编号：</label>
				<form:input path="hs_product_id" htmlEscape="false"
					class="input-small" />
			</div>
			
			<div class="form_tr_div">
				<label>显示状态：</label>
				<form:select path="show_status" class="input-medium"
					style="width: 155px;">
					<!-- 
					<form:option value="" label="请选择" />
					 -->
					<form:options items="${fns:getDictList('show_status')}"
						itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div style="width: 100%; float: left; text-align: center;">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
			</div>
		</div>
	</form:form>

<div style="padding-bottom: 10px;">
        <!--  
		<input id="generateBill" class="btn btn-primary" type="button" value="显示" onclick="showValidate()"/>
		<input id="generateBill" class="btn btn-primary" type="button" value="过滤" onclick="filterValidate()"/>
		-->
		<shiro:hasPermission name="charge:order:validate:edit">
			<input id="generateBill" class="btn btn-primary" type="button" value="导出" onclick="exportInfo()"/>
		</shiro:hasPermission>	
		<input id="generateBill" class="btn btn-primary" type="button" value="合同校验" onclick="orderValidate()"/>
		
	</div>
	
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<!-- 
		<th>订单号</th>
		  <th></th>
		 -->
		      
				<th style="width: 6%;">所属公司</th>
				<th style="width: 8%;">校验类型</th>
				<th style="width: 11%;">协同合同编号</th>
				<th style="width: 11%;">客户名称</th>
				<th style="width: 11%;">协同产品编号</th>
				<th style="width: 11%;">产品名称</th>
				<th style="width: 4%;">校验级别</th>
				<th style="width: 20%;">校验信息</th>
				<th style="width: 4%;">显示状态</th>
				<th style="width: 8%;">备注</th>
				<th>操作</th>
				<!--  
		<th>所属类型</th>
		<th>显示状态</th><th>校验状态</th><th>校验类型</th><th>备注</th>
		-->

			</tr>
		</thead>
		<!--<shiro:hasPermission name="charge:order:validate:edit"><th>操作</th></shiro:hasPermission></tr></thead>-->
		<tbody>
			<c:forEach items="${page.list}" var="orderValidate">
				<tr>
					<!--  
				<td>${orderValidate.order_id}</td>
				<td>
					 <input type="checkbox" name="checkbox" value="${orderValidate.serial_no}">
				   </td>
				-->
				   
					<td>${fns:getOfficeNameByCode(orderValidate.order_source)}</td>
					<td >${fns:getDictLabel(orderValidate.hs_check_status,"hs_check_status","")}</td>
					<td><a href="javascript:void(0)" onclick="viewContract(encodeURI(encodeURI('${orderValidate.hs_contract_id}')))">${orderValidate.hs_contract_id}</a></td>
					<td><a href="javascript:void(0)" onclick="viewCustomer(${orderValidate.syncContract.customerid})">${orderValidate.syncContract.customername}</a></td>
					<td>${orderValidate.hs_product_id}</td>
					<td>${orderValidate.syncProduct.productname}</td>
					<td>${fns:getDictLabel(orderValidate.belong_type,"belong_type","")}</td>
					<td>${orderValidate.reason}</td>
					<td>${fns:getDictLabel(orderValidate.show_status,"show_status","")}</td>
					<td style="word-break:break-all">${orderValidate.remark}</td>
					<!-- <td>${orderValidate.check_column}</td>	 
					<td>${fns:getDictLabel(orderValidate.check_column,"chargetype","")}</td>		
					<td>${orderValidate.belong_type}</td>
					<td>${orderValidate.show_status}</td>
					<td>${orderValidate.hs_check_status}</td>
					<td>${orderValidate.check_type}</td>
					<td>${orderValidate.remark}</td>
					-->

					
				
				
				<td>
				<shiro:hasPermission name="charge:order:validate:view">
				<!-- 状态1，显示显示按钮 -->
				<c:if test="${orderValidate.show_status=='1'}">
				<input id="generateBill" class="btn btn-primary" type="button" value="显示" onclick="showValidate('${orderValidate.serial_no}','${orderValidate.remark}')"/>
				</c:if>
				<!-- 状态0，显示过滤按钮 -->
				<c:if test="${orderValidate.show_status=='0'}">
				<input id="generateBill" class="btn btn-primary" type="button" value="过滤" onclick="filterValidate('${orderValidate.serial_no}','${orderValidate.remark}')"/>
				</c:if>
				
    			</shiro:hasPermission>	
				</td>
				 
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

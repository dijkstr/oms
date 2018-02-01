<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>产品上线管理</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
	.form_tr_div { width: 33%; height: 34px; float: left; margin-bottom: 5px; padding-bottom: 4px;}
	.form_tr_div .input-small { width: 140px;}
	.select2-container.input-medium { margin-left: -4px;}
	.form_tr_div label{width: 28%;}
	
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
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		
		//显示过滤
		function  showOrfilter(id,status){
			var msg='';
			if(status=='0'){
				msg = '确定要显示吗?';
			}else{
				msg = '确定要过滤吗?';
			}
			top.$.jBox.confirm(msg, "确认", function(v, h, f){
				if (v === 'ok') {
		    		var url="${ctx}/charge/order/orderInfo/updateProductLineStatus?id="+id+"&status="+status;
		    		
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
		
		// 导出合同列表 
		function proLineExport(){
			var params = $("#searchForm").serialize();
			window.open("${ctx}/charge/bill/export/download?templatekey=productLineList&" + params);
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/charge/order/orderInfo/productLineList">产品上线列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="productLineInfoForm" action="${ctx}/charge/order/orderInfo/productLineList" method="post" class="breadcrumb form-search" style="height:120px;">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="control-group">
			
			<div class="form_tr_div">
				<label>协同合同编号 ：</label><form:input path="contract_id" htmlEscape="false" class="input-small"/>
			</div>
			<div class="form_tr_div">
				<label>协同客户号 ：</label><form:input path="customer_id" htmlEscape="false" class="input-small"/>
			</div>
			<div class="form_tr_div">
				<label>客户名称 ：</label><form:input path="hs_customername" htmlEscape="false" class="input-small"/>
			</div>
			<div class="form_tr_div">
				<label>协同产品编号 ：</label><form:input path="product_id" htmlEscape="false" class="input-small"/>
			</div>
			<div class="form_tr_div">
				<label>显示状态 ：</label>
				<form:select path="online_status" class="input-medium" style="width: 155px;">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('show_status')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div style="width: 100%; float: left; text-align: center;">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
			</div>
		</div>
	</form:form>
	<div style="padding-bottom: 10px;">
		<shiro:hasPermission name="charge:order:productLine:productLineExport">
			<input id="orderInfoForm" class="btn btn-primary" type="button" value="导出列表" onclick="proLineExport()"/>
		</shiro:hasPermission>
	</div>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">

		<thead>
			<tr>
				<th style="width: 16%;">协同合同编号</th>
				<th style="width: 12%;">协同客户号</th>
				<th style="width: 25%;">客户名称</th>
				<th style="width: 18%;">协同产品编号</th>
				<th style="width: 10%;">产品上线日期</th>
				<th style="width: 10%;">显示状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="productLineInfo">
			<tr>
				<td>${productLineInfo.contract_id}</td>
				<td>${productLineInfo.customer_id}</td>
				<td>${productLineInfo.syncCustomer.chinesename}</td>
				<td>${productLineInfo.product_id}</td>
				<td>${fn:substring(productLineInfo.product_line_date,0,10)}</td>
				<td>${fns:getDictLabel(productLineInfo.online_status,"show_status","")}</td>
				<td>
					<shiro:hasPermission name="charge:order:productLine:edit">
						<c:if test="${productLineInfo.online_status=='1'}">
							<input class="btn btn-primary" type="button" value="显示" onclick="showOrfilter('${productLineInfo.id}','0')"/>
						</c:if>
						<!-- 状态0，显示过滤按钮 -->
						<c:if test="${productLineInfo.online_status=='0'}">
							<input class="btn btn-primary" type="button" value="过滤" onclick="showOrfilter('${productLineInfo.id}','1')"/>
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

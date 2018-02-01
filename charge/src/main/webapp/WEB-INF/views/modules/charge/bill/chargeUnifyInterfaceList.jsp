<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>统一接口数据管理</title>
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
	<meta name="decorator" content="default"/>
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
		// 导出
		function exportDetail(){
			var params = $("#searchForm").serialize();
			window.open("${ctx}/charge/bill/export/download?templatekey=unifyData&" + params);
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/charge/bill/chargeUnifyInterface/">统一接口数据列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="chargeUnifyInterface" action="${ctx}/charge/bill/chargeUnifyInterface/list" method="post" class="breadcrumb form-search" style="height: 153px;">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="control-group" style="float:left;">
			<div class="form_tr_div">
				<label>起始日期 ：</label><form:input class="Wdate" type="text" path="charge_begin_month" style="width:150px" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyyMM',maxDate:'%y-%M'})"/>
			</div>
			<div class="form_tr_div">
				<label>结束日期 ：</label><form:input class="Wdate" type="text" path="charge_end_month" style="width:150px" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyyMM',maxDate:'%y-%M'})"/>
			</div>
			<div class="form_tr_div">
				<label>所属部门 ：</label>
					<tags:treeselect id="office" name="office_id" value="${office.code}" labelName="office_name" labelValue="${office.name}" 
						title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" idVal="code"/>
			</div>
			<div class="form_tr_div">
				<label>合同编号 ：</label><form:input path="contract_id" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">
				<label>客户编号 ：</label><form:input path="customer_id" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">	
				<label>产品编号 ：</label><form:input path="product_id" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">	
				<label>计费类型 ：</label><form:input path="fee_type" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">	
				<label>&nbsp;&nbsp;&nbsp;批次号 ：</label><form:input path="batch_no" htmlEscape="false" maxlength="200" class="required" style="width:150px"/>
			</div>
			<div style="width: 100%; float: left; text-align: center;">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
			</div>
		</div>
	</form:form>
	<div style="padding-bottom: 10px;">
		<shiro:hasPermission name="charge:bill:chargeUnifyInterface:exportDetail">
			<input id="chargeUnifyInterface" class="btn btn-primary" type="button" value="导出" onclick="exportDetail()"/>
		</shiro:hasPermission>
	</div>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>所属部门</th>
				<th>批次号</th>
				<th>合同编号</th>
				<th>发生日期</th>
				<th>客户编号</th>
				<th>产品编号</th>
				<th>计费类型</th>
				<th>业务数据1</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="chargeUnifyInterface">
			<tr>
				<td>${fns:getOfficeNameByCode(chargeUnifyInterface.office_id)}</td>
				<td>${chargeUnifyInterface.batch_no}</td>
				<td>${chargeUnifyInterface.contract_id}</td>
				<td>${chargeUnifyInterface.oc_date}</td>
				<td>${chargeUnifyInterface.customer_id}</td>
				<td>${chargeUnifyInterface.product_id}</td>
				<td>${chargeUnifyInterface.fee_type}</td>
				<td><fmt:formatNumber value="${chargeUnifyInterface.data1}" pattern="#,###.#"/></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>计费模式管理</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
		.search_div{
			width: 33%;
		    float: left;
		    margin-bottom: 5px;
		    padding-bottom: 4px;
		}
		.search_div label{width: 100px;}
	</style>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnReset").on("click",function(){
				$("#searchForm input:not([type='submit'],[type='button'])").val("");
				$("#searchForm select option:selected").attr("selected", false);
				$("#searchForm a span").text("请选择");
			});
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		
		function getChargeModelId(){
			var chargeModelId =  $("#contentTable").find("input[name=rowRadio]:checked").val();
			return chargeModelId;
		}
	</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="chargeModel" action="${ctx}/charge/setting/chargeModel/chargeModelChooser" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div style="margin-bottom: 8px;height: 70px;">
			<div class="search_div">
				<label>所属部门 ：</label>
				<tags:treeselect id="office" name="office_id" value="${office.code}" labelName="office_name" labelValue="${office.name}" 
					title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" idVal="code"/>
			</div>
			
			<div class="search_div">
			<label>计费模式分类 ：</label>
			<tags:treeselect id="classify_id" name="classify_id" value="${classify.id}" labelName="classify_name" labelValue="${classify.classify_name}" 
					title="计费分类" url="/charge/setting/classify/treeData" cssClass="input-small" allowClear="true"/>
			</div>
			<div class="search_div">
			<label>计费模式名称 ：</label><form:input path="model_name" htmlEscape="false" maxlength="50" class="input-small"/>
			</div>
			<div class="search_div">
			<label>计费类型：</label>
			<form:select path="fee_type" class="input-small">
				<form:option value="" label="默认"/>
				<form:options items="${charge:getChargeTypeList('fee_type')}" itemLabel="label" itemValue="value" htmlEscape="false" />
			</form:select>
			</div>
			<div class="search_div">
			<label>计费公式：</label>
			<form:select path="fee_formula" class="input-small">
				<form:option value="" label="默认"/>
				<form:options items="${fns:getDictList('fee_formula')}" itemLabel="label" itemValue="value" htmlEscape="false" />
			</form:select>
			</div>
		</div>
		<div style="text-align:center">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
			<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th></th>
				<th>计费模式名称</th>
				<th>计费类型</th>
				<th>计费公式</th>
				<th>保底</th>
				<th>封顶</th>
				<th>折扣(%)</th>
				<th>计费模式分类</th>
				<th>备注</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="chargeModel">
			<tr>
				<td><input type="radio" name="rowRadio" value="${chargeModel.id}"></td>
				<td>${chargeModel.model_name}</td>
				<td>${charge:getChargeTypeLabel(chargeModel.fee_type ,'fee_type','')}</td>
				<td>${fns:getDictLabel(chargeModel.fee_formula ,'fee_formula','')}</td>
				<td>${chargeModel.min_consume}</td>
				<td>${chargeModel.max_consume}</td>
				<td>${chargeModel.discount}</td>
				<td>${chargeModel.classify.classify_name}</td>
				<td>${chargeModel.remarks}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

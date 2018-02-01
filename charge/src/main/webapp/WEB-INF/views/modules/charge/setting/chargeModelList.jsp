<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>计费模式管理</title>
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
			});
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/charge/setting/chargeModel/">计费模式列表</a></li>
		<shiro:hasPermission name="charge:setting:chargeModel:edit"><li><a href="${ctx}/charge/setting/chargeModel/form">计费模式添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="chargeModel" action="${ctx}/charge/setting/chargeModel/" method="post" class="breadcrumb form-search" style="height:120px;">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="control-group">
			<div class="form_tr_div">
				<label>计费模式分类 ：</label>
				<tags:treeselect id="classify_id" name="classify_id" value="${classify.id}" labelName="classify_name" labelValue="${classify.classify_name}" 
						title="计费分类" url="/charge/setting/classify/treeData" cssClass="input-small" allowClear="true"/>
			</div>
			<div class="form_tr_div">
				<label>计费模式名称 ：</label><form:input path="model_name" htmlEscape="false" maxlength="50" class="input-small"/>
			</div>
			<div class="form_tr_div">
				<label>计费类型：</label>
				<form:select path="fee_type" class="input-small">
					<form:option value="" label="默认"/>
					<form:options items="${charge:getChargeTypeList('fee_type')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div class="form_tr_div">
				<label>计费公式：</label>
				<form:select path="fee_formula" class="input-small">
					<form:option value="" label="默认"/>
					<form:options items="${fns:getDictList('fee_formula')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
			<div style="width: 100%; float: left; text-align: center;">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
			</div>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>计费模式名称</th>
				<th>计费类型</th>
				<th>计费公式</th>
				<th>保底</th>
				<th>封顶</th>
				<th>折扣(%)</th>
				<th>计费模式分类</th>
				<th>备注</th>
				<shiro:hasPermission name="charge:setting:chargeModel:edit"><th style="width: 120px;">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="chargeModel">
			<tr>
				<td><a href="${ctx}/charge/setting/chargeModel/form?id=${chargeModel.id}">${chargeModel.model_name}</a></td>
				<td>${charge:getChargeTypeLabel(chargeModel.fee_type ,'fee_type','')}</td>
				<td>${fns:getDictLabel(chargeModel.fee_formula ,'fee_formula','')}</td>
				<td>${chargeModel.min_consume}</td>
				<td>${chargeModel.max_consume}</td>
				<td>${chargeModel.discount}</td>
				<td>${chargeModel.classify.classify_name}</td>
				<td>${chargeModel.remarks}</td>
				<shiro:hasPermission name="charge:setting:chargeModel:edit"><td>
    				<a href="${ctx}/charge/setting/chargeModel/form?id=${chargeModel.id}" class="table_button_alink">修改</a>
					<a href="${ctx}/charge/setting/chargeModel/delete?id=${chargeModel.id}"  class="table_button_alink" onclick="return confirmx('确认要删除该计费模式吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

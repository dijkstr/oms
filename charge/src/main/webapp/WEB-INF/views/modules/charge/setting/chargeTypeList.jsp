<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>计费类型管理</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
		.form_tr_div { width: 33%; float: left; margin-bottom: 5px; padding-bottom: 4px;}
		.form_tr_div .input-small { width: 140px;}
		
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
		<li class="active"><a href="${ctx}/charge/setting/chargeType/">计费类型列表</a></li>
		<shiro:hasPermission name="charge:setting:chargeType:edit"><li><a href="${ctx}/charge/setting/chargeType/form?sort=1&type=fee_type&description=计费类型">计费类型添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="chargeType" action="${ctx}/charge/setting/chargeType/" method="post" class="breadcrumb form-search"  style="height:70px;">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="control-group">
			<div class="form_tr_div">
				<label>所属部门 ：</label>
				<tags:treeselect id="office" name="office_id" value="${office.code}" labelName="office_name" labelValue="${office.name}" 
						title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" idVal="code"/>
			</div>
			<div class="form_tr_div">
				<label>计费类型 ：</label><form:input path="value" htmlEscape="false" maxlength="50" class="input-small"/>
			</div>
			<div class="form_tr_div">
				<label>计费类型名称 ：</label><form:input path="label" htmlEscape="false" maxlength="50" class="input-small"/>
			</div>
			<div style="width: 100%; float: left; text-align: center;">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
				<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
			</div>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>所属部门</th><th>计费类型</th><th>计费类型名称</th><th>描述</th><th>排序</th><shiro:hasPermission name="charge:setting:chargeType:edit"><th style="width:160px;">操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="dict">
			<tr>
				<td>${fns:getOfficeNameByCode(dict.office_id)}</td>
				<td>${dict.value}</td>
				<td><a href="${ctx}/charge/setting/chargeType/form?id=${dict.id}">${dict.label}</a></td>
				<td>${dict.description}</td>
				<td>${dict.sort}</td>
				<shiro:hasPermission name="charge:setting:chargeType:edit"><td>
    				<a href="${ctx}/charge/setting/chargeType/form?id=${dict.id}" class="table_button_alink" >修改</a>
					<a href="${ctx}/charge/setting/chargeType/delete?id=${dict.id}" class="table_button_alink"  onclick="return confirmx('确认要删除该字典吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

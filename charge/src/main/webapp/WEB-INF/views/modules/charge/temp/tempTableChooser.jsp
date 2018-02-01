<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>中间表选择器</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnReset").on("click",function(){
				$("#searchForm input:not([type='submit'],[type='button'])").val("");
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
		function getTempTables(){
			var param={};
			var checkedTd = $("#contentTable").find("input[name=rowRadio]:checked").parent();
			param.id= $(checkedTd).find("input[name='id']").val();
			param.name= $(checkedTd).find("input[name='name']").val();
			param.width= $(checkedTd).find("input[name='width']").val();
			param.height= $(checkedTd).find("input[name='height']").val();
			return param;
		}
	</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="tempTable" action="${ctx}/charge/temp/tempTable/chooser" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>中间表名称 ：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-small"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th></th>
				<th>中间表名称</th>
				<th>中间表 备注</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="tempTable">
			<tr>
				<td width="5%">
					<input type="radio" name="rowRadio" value="${tempTable.id}">
					<input type="hidden" name="id" value="${tempTable.id}">
					<input type="hidden" name="name" value="${tempTable.name}">
					<input type="hidden" name="width" value="${tempTable.width}">
					<input type="hidden" name="height" value="${tempTable.height}">
				</td>
				<td width="40%">${tempTable.name}</td>
				<td width="65%">${tempTable.remarks}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

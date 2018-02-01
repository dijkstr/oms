<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="default"/>
	<title>sql脚本查询</title>
	
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
			$("#sqlArea").val("");
		});
	});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
		
		// 查询结果导出
		function toExport(){
			var params = $("#sqlArea").serialize();
			
	    	window.open("${ctx}/charge/manager/sql/export/download?" + (encodeURI(params)));
		}
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/charge/manager/sql">sql脚本查询</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="chargeSqlForm" action="${ctx}/charge/manager/sql" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="执行sql"/>
		<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>	
		<input id="generateBill" class="btn btn-primary" type="button" value="EXCEL导出" onclick="toExport()"/>
		<br>
		<form:textarea id="sqlArea" path="sql" htmlEscape="false" rows="10" class="input-xxlarge" />
		

	
	</form:form>
  
	<tags:message content="${message}"/>
	<c:if test="${page.list.size()>=0}">

		<table id="contentTable" class="table table-striped table-bordered table-condensed">

			<c:forEach items="${page.list}" var="head" varStatus="varStatus">
				<c:if test="${varStatus.first}">
					<thead>
						<tr>
							<c:forEach items="${head}" var="headdata">
								<th>${headdata.key}</th>
							</c:forEach>
						</tr>
					</thead>
				</c:if>
			</c:forEach>
			<tbody>
				<c:forEach items="${page.list}" var="map">

					<tr>
						<c:forEach items="${map}" var="data">
							<td>${data.value}</td>
						</c:forEach>
					</tr>

				</c:forEach>
			</tbody>

		</table>
		<c:if test="${page.list.size() > 0}">
			<div class="pagination">${page}</div>
		</c:if>
	</c:if>
</body>
</html>

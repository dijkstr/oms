<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>计费分类管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<style type="text/css">
	.table td i{margin:0 2px;}
	
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
			$("#treeTable").treeTable({expandLevel : 3});
		});
		
	</script>
</head>

<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/charge/setting/classify/">计费分类列表</a></li>
		<shiro:hasPermission name="charge:setting:classify:edit"><li><a href="${ctx}/charge/setting/classify/form">计费分类添加</a></li></shiro:hasPermission>
	</ul>
	<tags:message content="${message}"/>
	<form id="listForm" method="post">
		<table id="treeTable" class="table table-striped table-bordered table-condensed">
			<tr><th>分类名称</th><th>排序</th><th>所属部门</th><shiro:hasPermission name="charge:setting:classify:edit"><th style="width:300px;">操作</th></shiro:hasPermission></tr>
			<c:forEach items="${list}" var="menu">
				<tr id="${menu.id}" pId="${menu.classify_parent ne '1' ? menu.classify_parent : '0'}">
					<td><a href="${ctx}/charge/setting/classify/form?id=${menu.id}">${menu.classify_name}</a></td>
					<td >${menu.order_in}</td>
					<td>${fns:getOfficeNameByCode(menu.office_id)}</td>
					<shiro:hasPermission name="charge:setting:classify:edit"><td>
						<a href="${ctx}/charge/setting/classify/form?id=${menu.id}" class="table_button_alink">修改</a>
						<a href="${ctx}/charge/setting/classify/delete?id=${menu.id}" class="table_button_alink" onclick="return confirmx('要删除该分类及所有子分类项吗？', this.href)">删除</a>
						<a href="${ctx}/charge/setting/classify/form?classify_parent=${menu.id}" class="table_button_alink">添加下级分类</a> 
					</td></shiro:hasPermission>
				</tr>
			</c:forEach>
		</table>
	 </form>
</body>
</html>

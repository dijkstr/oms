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
		
		function getChargeModelClassify(){
			var param={};
			var checkedTd =  $("#contentTable").find("input[name=rowRadio]:checked").parent();
			param.chargeModelClassifyId = $(checkedTd).find("input[name=classifyId]").val();
			param.chargeModelClassifyName = $(checkedTd).find("input[name=classifyName]").val();
			return param;
		}
		
		function getChargeModelId(){
			var chargeModelId =  $("#contentTable").find("input[name=rowRadio]:checked").val();
			return chargeModelId;
		}
	</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="classifyForm" action="${ctx}/charge/setting/chargeModel/chargeModelClassifyChooser" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div style="margin-bottom: 8px;height: 70px;">
			<div class="search_div">
				<label>所属部门 ：</label>
				<tags:treeselect id="office" name="office_id" value="${office.code}" labelName="office_name" labelValue="${office.name}" 
					title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" idVal="code"/>
			</div>
			
			<div class="search_div">
			<label>模式分类 ：</label>
			<tags:treeselect id="classify_id" name="classify_id" value="${classify.id}" labelName="classify_name" labelValue="${classify.classify_name}" 
					title="计费分类" url="/charge/setting/classify/treeData" cssClass="input-small" allowClear="true"/>
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
				<th>所属部门</th>
				<th>模式分类</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="clf">
			<tr>
				<td>
					<input type="radio" name="rowRadio" value="${clf.id}">
					<input type="hidden" name="classifyId" value="${clf.id}">
					<input type="hidden" name="classifyName" value="${clf.classify_name}">
				</td>
				<td>${fns:getOfficeNameByCode(clf.office_id)}</td>
				<td>${clf.classify_name}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>

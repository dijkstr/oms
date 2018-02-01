<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>导出接口条件 </title>
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
		function exportNCIncomes() {
			if(!$("#charge_month").val()){
				top.$.jBox.alert("请输入查询年月。");
				return;
			}
			window.open("${ctx}/charge/bill/export/download?templatekey=exportNCIncomes&charge_month=" + $("#charge_month").val());
			return;
		}
	</script>
</head>
<body>
	<form id="searchForm" action="" method="post" class="breadcrumb form-search">
		<div style="margin-bottom: 8px;height: 60px;">
			<div class="search_div">
				<label>NC查询年月 ：</label>
				<input class="Wdate" type="text" id="charge_month" name="charge_month" style="width:150px" onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyyMM',maxDate:'%y-%M'})"/>
			</div>
		</div>
		<div style="text-align:center">
			<input id="btnSubmit" class="btn btn-primary" type="button" value="导出" onclick="exportNCIncomes()"/>
			<input id="btnReset" class="btn btn-primary" type="button" value="重置"/>
		</div>
	</form>
	<tags:message content="${message}"/>
</body>
</html>

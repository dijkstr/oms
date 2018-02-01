<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>中间表管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate();
		});
		function previewChart(){
			var url = "${ctx}//charge/temp/tempTable/plain";
			var param = {"id":"${tempTable.id}", "t":Math.random(), "query_sql":document.getElementById("query_sql").value};
			var result = ajaxFunction(url,param);
			if(result.table){
				document.getElementById("chartPreview").innerHTML = "<div style='height:300px;width:800px;overflow-x:auto;overflow-y:auto'>" + result.table + "</div";	
			}else{
				alert(result.message);
				document.getElementById("chartPreview").innerHTML = "";
			}
			
		}
		// SQL导出
		function previewExport(){
			var url = "${ctx}//charge/temp/tempTable/plain";
			var param = {"id":"${tempTable.id}", "t":Math.random(), "query_sql":document.getElementById("query_sql").value};
			var result = ajaxFunction(url,param);
			if(result.table){
				window.open("${ctx}/charge/bill/export/download?templatekey=tempTableList&id=${tempTable.id}");	
			}else{
				alert(result.message);	
			}	    	
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
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/charge/temp/tempTable/">中间表列表</a></li>
		<li class="active"><a href="${ctx}/charge/temp/tempTable/form?id=${tempTable.id}">中间表<shiro:hasPermission name="charge:temp:tempTable:edit">${not empty tempTable.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="charge:temp:tempTable:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	
	<form:form id="inputForm" modelAttribute="tempTable" action="${ctx}/charge/temp/tempTable/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label" for="false">中间表名称:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="200" style="width:460px" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="query_sql">查询sql:</label>
			<div class="controls">
				<form:textarea path="query_sql" htmlEscape="false" cols="30" rows="10" maxlength="5000" class="input-xxlarge"/>
				&nbsp;&nbsp;
				<shiro:hasPermission name="charge:temp:tempTable:previewExport">
					<input type="button" class="btn btn-primary" onclick="previewChart()" value="SQL查询">
					<input type="button" class="btn btn-primary" onclick="previewExport()" value="SQL导出">
				</shiro:hasPermission>
			</div>
		</div>
		<div class="control-group">
			<div class="controls">
				<div id="chartPreview"></div>
			</div>
		</div> 
		<div class="control-group">
			<label class="control-label" for="remarks">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="charge:temp:tempTable:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>发送邮件</title>
	<meta name="decorator" content="default"/>
	<link href="${ctxStatic}/bootstrap/2.3.1/docs/assets/js/google-code-prettify/prettify.css" rel="stylesheet">	
	<link href="${ctxStatic}/bootstrap/2.3.1/css_default/bootstrap-responsive.min.css" rel="stylesheet">
    <link href="${ctxStatic}/font-awesome/3.0.2/css/font-awesome.css" rel="stylesheet">    
    <script src="${ctxStatic}/jquery/jquery.hotkeys.js"></script>
    <script src="${ctxStatic}/bootstrap/2.3.1/docs/assets/js/google-code-prettify/prettify.js"></script>
	<link href="${ctxStatic}/bootstrap/wysiwyg/css/wysiwyg.css" rel="stylesheet">
    <script src="${ctxStatic}/bootstrap/wysiwyg/js/bootstrap-wysiwyg.js"></script>
   
	<script type="text/javascript">
		$(document).ready(function() {
			getMailConfig();
			$("#btnReset").on("click",function(){
				$("#contactMailForm input:not([type='submit'],[type='button'])").val("");
				$("#contactMailForm select option:selected").attr("selected", false);
				getMailConfig();
			});
		});
		function getMailConfig(){
			$("#editor").html(HTMLDecode("${contactMailForm.mail_content}"));
        }
		function addElement(form, name, value) {
			var element = document.createElement("textarea");
			element.name = name;
			element.value = value;
			form.appendChild(element);
		}
		function showPDFPreview() {
			var temp = document.createElement("form");
			temp.action = "${ctx}/charge/mail/contactMail/download";
			temp.method = "post";
			temp.style.display = "none";
			var pdfHtml = document.createElement("textarea");
			pdfHtml.name = "html";
			var imgs = $('#editor').find('img');
			for(var i = 0; i < imgs.length; i++){
				var img = imgs[i];
				img.setAttribute("width", img.width);
				img.setAttribute("height", img.height);
			}
			pdfHtml.value = $("#editor").html();
			temp.appendChild(pdfHtml);
			addElement(temp, "subject", "${contactMailForm.mail_subject}");
			document.body.appendChild(temp);
			temp.submit();
			return false
		}
		function HTMLDecode(text) { 
		    var temp = document.createElement("div"); 
		    temp.innerHTML = text; 
		    var output = temp.innerText || temp.textContent; 
		    temp = null; 
		    return output; 
		}
	</script>
	<style type="text/css">
		#editor {
			max-height: 1000px;
			height: 400px;
			width: 65%;
			background-color: white;
			border-collapse: separate; 
			border: 1px solid rgb(204, 204, 204); 
			padding: 4px; 
			box-sizing: content-box; 
			-webkit-box-shadow: rgba(0, 0, 0, 0.0745098) 0px 1px 1px 0px inset; 
			box-shadow: rgba(0, 0, 0, 0.0745098) 0px 1px 1px 0px inset;
			border-top-right-radius: 3px; border-bottom-right-radius: 3px;
			border-bottom-left-radius: 3px; border-top-left-radius: 3px;
			overflow-x: hidden;
    		overflow-y: scroll;
			outline: none;
		}
	</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/charge/mail/contactMail/">邮件履历列表</a></li>
		<li class="active"><a href="${ctx}/charge/mail/contactMail/form?id=${contactMail.id}">邮件履历<shiro:hasPermission name="charge:mail:contactMail:edit">${not empty contactMail.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="charge:mail:contactMail:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="contactMailForm" modelAttribute="contactMailForm" action="${ctx}/charge/mail/contactMail/sendMail" method="post" class="form-horizontal" style="height:440px;" onsubmit="setContent()">
		<form:hidden path="id"/>
		<form:hidden path="mail_content"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label" for="email">收信人：</label>
			<div class="controls">
				${contactMailForm.email}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="cc">抄送：</label>
			<div class="controls">
				${contactMailForm.cc}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="bcc">密抄：</label>
			<div class="controls">
				${contactMailForm.bcc}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="mail_subject">邮件标题：</label>
			<div class="controls">
				${contactMailForm.mail_subject}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="mail_subject">邮件正文：</label>
			<div class="controls">
				<div class="btn-toolbar" data-role="editor-toolbar" data-target="#editor" style="width:650px;">
				  <div class="btn-group">
			        <a class="btn btn-primary" onclick="showPDFPreview()"><i class="icon-search" title="下载预览"></i></a>
			      </div>
			    </div>
			   <div id="editor">
			   </div>
		    </div>
	    </div>
	    <div class="form-actions">
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
	    </div>
	</form:form>
</body>
</html>

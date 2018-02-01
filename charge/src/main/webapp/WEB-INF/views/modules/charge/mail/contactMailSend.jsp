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
			$("#cc").val("");
			$("#bcc").val("");
			$("#mail_subject").val("");
			$("#editor").html("");
			var url="${ctx}/charge/mail/contactMail/content?mail_config_id=" + $("#mail_config_id").val()+"&contract_id=${orderRelation.contract_id}";			
			var data = ajaxFunction(url);
			var newcc = cc;
			if(data.cc){
				newcc = cc + ',' + data.cc;
			}
			var newbcc = bcc;
			if(data.bcc){
				newbcc = bcc + ',' + data.bcc;
			}
			$("#cc").val(newcc);
			$("#bcc").val(newbcc);
			if(data.mail_subject){
				$("#mail_subject").val(data.mail_subject);
			}
			if(data.mail_content){
				$("#editor").html(HTMLDecode(data.mail_content));
			}
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
			addElement(temp, "subject", $("#mail_subject").val());
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
	    function setContent(){
	    	$("#mail_content").val($("#editor").html());
	    	return true;
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
		<li><a href="${ctx}/charge/mail/contactMail/list">联系人列表</a></li>
		<li class="active"><a href="#">邮件管理</a></li>
	</ul><br/>
	<form:form id="contactMailForm" modelAttribute="contactMailForm" action="${ctx}/charge/mail/contactMail/sendMail" method="post" class="form-horizontal" style="height:440px;" onsubmit="setContent()">
		<form:hidden path="id"/>
		<form:hidden path="mail_content"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label" for="email">收信人：</label>
			<div class="controls">
				<form:input path="email" htmlEscape="false" style="width:65%" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="cc">抄送：</label>
			<div class="controls">
				<form:input path="cc" htmlEscape="false" style="width:65%"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="bcc">密抄：</label>
			<div class="controls">
				<form:input path="bcc" htmlEscape="false" style="width:65%"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="mail_config_id">选择模板：</label>
			<div class="controls">
				<form:select path="mail_config_id" placeholder="请选择" onchange="getMailConfig()" style="width:65%">
					<c:forEach items="${mailConfigs}" var="mailConfig">
						<form:option value="${mailConfig.id}" label="${mailConfig.template}"/>
					</c:forEach>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="mail_subject">邮件标题：</label>
			<div class="controls">
				<form:input path="mail_subject" htmlEscape="false" class="required" style="width:65%"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="name">重要度：</label>
			<div class="controls">
				<form:select path="importance" class="input-medium">
					<form:options items="${fns:getDictList('mail_importance')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="name">是否请求已读回执：</label>
			<div class="controls">
				<form:select path="is_read" class="input-medium">
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="mail_subject">邮件正文：</label>
			<div class="controls">
				<div class="btn-toolbar" data-role="editor-toolbar" data-target="#editor" style="width:650px;">
				  <div class="btn-group">
			        <a class="btn btn-primary" onclick="showPDFPreview()"><i class="icon-search" title="下载预览"></i></a>
			        <a class="btn" data-edit="insertHTML <div class='remove'><hr style='height:1px;border:none;border-top:1px solid #555555;'></hr></div>" title="插入邮件签名"><i class="icon-minus"></i></a>
					<a class="btn dropdown-toggle" data-toggle="dropdown" title="插入链接"><i class="icon-link"></i></a>
					<div class="dropdown-menu">
						<input class="span2" placeholder="URL" type="text" data-edit="createLink"/>
						<button class="btn" type="button">Add</button>
			        </div>
			      </div>
			      <div class="btn-group">
			        <a class="btn dropdown-toggle" data-toggle="dropdown" title="改变字体大小"><i class="icon-text-height"></i>&nbsp;<b class="caret"></b></a>
			        <ul class="dropdown-menu">
				        <li><a data-edit="fontSize 5"><font size="5">Huge</font></a></li>
				        <li><a data-edit="fontSize 3"><font size="3">Normal</font></a></li>
				        <li><a data-edit="fontSize 1"><font size="1">Small</font></a></li>
			        </ul>
			        <a class="btn" data-edit="bold" title="粗体字"><i class="icon-bold"></i></a>
			        <a class="btn" data-edit="italic" title="斜体字"><i class="icon-italic"></i></a>
			        <a class="btn" data-edit="strikethrough" title="删除线"><i class="icon-strikethrough"></i></a>
			        <a class="btn" data-edit="underline" title="下划线"><i class="icon-underline"></i></a>
			      </div>
			      <div class="btn-group">
			        <a class="btn" data-edit="outdent" title="去掉缩进"><i class="icon-indent-left"></i></a>
			        <a class="btn" data-edit="indent" title="添加缩进"><i class="icon-indent-right"></i></a>
			        <a class="btn" data-edit="justifyleft" title="左对齐"><i class="icon-align-left"></i></a>
			        <a class="btn" data-edit="justifycenter" title="居中"><i class="icon-align-center"></i></a>
			        <a class="btn" data-edit="justifyright" title="右对齐"><i class="icon-align-right"></i></a>
			      </div>
      
			      <div class="btn-group">
			        <a class="btn" title="插入图片" id="pictureBtn"><i class="icon-picture"></i></a>
			        <input type="file" data-role="magic-overlay" data-target="#pictureBtn" data-edit="insertImage" />
			      </div>
			      <div class="btn-group">
			        <a class="btn" data-edit="undo" title="撤销操作"><i class="icon-undo"></i></a>
			        <a class="btn" data-edit="redo" title="继续操作"><i class="icon-repeat"></i></a>
			      </div>
			    </div>
			   <div id="editor">
			   </div>
		    </div>
	    </div>
	    <div class="form-actions">
	    	<input id="btnSubmit" class="btn btn-primary" type="submit" value="发送"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
	    </div>
	</form:form>

	<tags:message content="${message}"/>
	<script>
	  $(function(){
		    function initToolbarBootstrapBindings() {
		      var fonts = ['Serif', 'Sans', 'Arial', 'Arial Black', 'Courier', 
		            'Courier New', 'Comic Sans MS', 'Helvetica', 'Impact', 'Lucida Grande', 'Lucida Sans', 'Tahoma', 'Times',
		            'Times New Roman', 'Verdana'],
		            fontTarget = $('[title=Font]').siblings('.dropdown-menu');
		      $.each(fonts, function (idx, fontName) {
		          fontTarget.append($('<li><a data-edit="fontName ' + fontName +'" style="font-family:\''+ fontName +'\'">'+fontName + '</a></li>'));
		      });
		      $('a[title]').tooltip({container:'body'});
		    	$('.dropdown-menu input').click(function() {return false;})
				    .change(function () {$(this).parent('.dropdown-menu').siblings('.dropdown-toggle').dropdown('toggle');})
		        .keydown('esc', function () {this.value='';$(this).change();});

		      $('[data-role=magic-overlay]').each(function () { 
		        var overlay = $(this), target = $(overlay.data('target')); 
		        overlay.css('opacity', 0).css('position', 'absolute').offset(target.offset()).width(target.outerWidth()).height(target.outerHeight());
		      });
		      if ("onwebkitspeechchange"  in document.createElement("input")) {
		        var editorOffset = $('#editor').offset();
		        $('#voiceBtn').css('position','absolute').offset({top: editorOffset.top, left: editorOffset.left+$('#editor').innerWidth()-35});
		      } else {
		        $('#voiceBtn').hide();
		      }
			};
			function showErrorAlert (reason, detail) {
				var msg='';
				if (reason==='unsupported-file-type') { msg = "Unsupported format " +detail; }
				else {
					console.log("error uploading file", reason, detail);
				}
				$('<div class="alert"> <button type="button" class="close" data-dismiss="alert">&times;</button>'+ 
				 '<strong>File upload error</strong> '+msg+' </div>').prependTo('#alerts');
			};
		    initToolbarBootstrapBindings();  
			$('#editor').wysiwyg({ fileUploadError: showErrorAlert} );
		    window.prettyPrint && prettyPrint();
		  });
		  // 初始化cc与bcc
		  var cc = $("#cc").val();
		  var bcc = $("#bcc").val();
		</script>
	</script>
</body>
</html>

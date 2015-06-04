<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>错误</title>
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=yes">

<link rel="stylesheet" type="text/css" href="${ctx }/css/main.css">

<script type="text/javascript" src="${ctx }/js/jquery-1.11.1.min.js"></script>
<script src="${ctx }/js/common.js" type="text/javascript"></script>


<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
<script src="js/html5shiv.min.js"></script>
<script src="js/respond.min.js"></script>
<![endif]-->
<!--[if IE 6]>
<script type="text/javascript" src="js/DD_belatedPNG.js"></script>
<script type="text/javascript">DD_belatedPNG.fix('*');</script>
<![endif]--><!--修复IE6下PNG图片背景透明-->

</head>

<body>
<div id="container" class="container">
	<div class="screen-height text-vertical-center ">
		<div class="cells" style="height:200px">
			<div class="cell-24">
				
			</div>
		</div>
		<div class="cells screen-height ">
			<div class="cell-6">&nbsp;</div>
			<div class="cell-20 bg-login-user" style="height:200px; padding:10px;">
			<div class="text-center" style="padding:10px;">登录错误</div>
	      <div class="form-list">
	      	<label class="form-label t-right">错误码：</label>
	        <div class="form-content">
	        ${respCode }
	        </div>
	      </div>
	      <div class="form-list">
	      	<label class="form-label t-right">错误信息</label>
	        <div class="form-content">
	        	${respMsg }
	        </div>
	      </div>
	      
			</div>
		</div>
	</div>
	<form action="" id="loginForm" method="post">
		<input type="hidden" id="realLoginName" name="loginName" />
		<input type="hidden" id="realLoginPwd" name="loginPwd" />
	</form>
</div>
</body>

<script type="text/javascript" >
//alert("${ctx}");
$("#userLogin").click(function(){
	$("#realLoginName").val($("#username").val());
	$("#realLoginPwd").val($("#loginpwd").val());
	$("loginForm").attr("action","${ctx}/user/userLogin.do");
	$("loginForm").submit();
});

$("#companyLogin").click(function(){
	$("#realLoginName").val($("#username-comp").val());
	$("#realLoginPwd").val($("#loginpwd-comp").val());
	$("loginForm").attr("action","${ctx}/user/companyLogin.do");
	$("loginForm").submit();
});

</script>
</html>
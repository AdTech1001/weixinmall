<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<%@ include file="/common/common.jsp"%>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>${channerName }</title>
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=yes">

<link rel="stylesheet" href="/css/base.css">
<link rel="stylesheet" href="/css/main.css">
<link rel="stylesheet" href="/css/mask.css">
<link rel="stylesheet" href="/css/pad.css" media="only screen and (max-width : 768px)">

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
<script src="/js/html5shiv.min.js"></script>
<script src="/js/respond.min.js"></script>
<![endif]-->
<!--[if IE 6]>
<script type="text/javascript" src="/js/DD_belatedPNG.js"></script>
<script type="text/javascript">DD_belatedPNG.fix('*');</script>
<![endif]-->
<!--修复IE6下PNG图片背景透明-->
</head>

<body>
	<%@ include file="/common/menu.jsp"%>
	<div class="container">
		<div class="comwidth">
			<form id="commForm" action="/commemorate/addCommemorate.do" method="post">
			创建我的纪念版:<br>
			纪念日：<br><input id="commemorateDate" name="commemorateDate" class="Wdate" onfocus="WdatePicker({dateFmt:'yyyyMMdd', minDate:'%y-{%M-0}-%d'})" maxlength="10" style="width: 100px;" readonly size="8" type="text">
			<br>
			描述：<br><textarea class="maskContent" id="comments" name="comments" placeholder="内容"></textarea>
			<br>
			配图：<br><input type="file" id="picPath" name="picPath" /><br>
			<div class="clear"></div>
			<div class="comwidth">
				<a href="javascript:void(0);" id="submitBtn" class="btn orange" onclick='formTo("commForm", "/commemorate/addCommemorate.do");'>确认创建</a>
				<div class="clear"></div>
			</div>
			</form>
		</div>
	</div>
	<%@ include file="/common/footer.jsp"%> 
</body>

<form id="channelForm" action="/channel/life.do">
</form>

<script type="text/javascript" src="/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript" src="/js/business.js"></script>

<script type="text/javascript">
	
</script>
<script type="text/javascript" src="/js/listener.js"></script>
</html>
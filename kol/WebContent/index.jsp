<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<%@ include file="/common/common.jsp"%>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>人生百科 WEB人生百科 webrsbk 人生百科登录页面</title>
<meta name="description"
	content="人生百科,WEB人生百科,webrsbk,提高你的人生高度,工作频道,生活频道,纪念版,创造人生价值,创造社会价值">
<meta name="keywords" content="人生百科">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">

<link rel="stylesheet" href="/css/simple.css">
<link rel="stylesheet" href="/css/mask.css">
<link rel="stylesheet" href="/css/pad.css" media="only screen and (max-width : 768px)">

<script type="text/javascript" src="/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="/js/jquery.SuperSlide.2.1.1.js"></script>
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
<style type="text/css">
a {text-decoration: none; }
</style>

</head>

<body>
	<div id="maindiv" class="div_main">
		<%@ include file="/common/menu-new.jsp"%>
		<div class="top_fix">&nbsp;</div>
		<div class="div_main_words">
			<table class="table_main_words" cellpadding="3" cellspacing="0">
				<tr><td colspan="2" class="td_words_title">人生百科 - 我们的网上家园</td></tr>
				<tr><td class="span_words_content_a">先吐为快</td><td >在这里没有什么不能明言</td></tr>
				<tr><td >纪念板</td><td >这里的我们,都会为你的纪念点赞</td></tr>
				<tr><td >猜猜我是谁</td><td >留下点蛛丝马迹, 让他们快乐的猜翻天</td></tr>
			</table>
		</div>
	</div>
	
	<div id="mainnextdiv" class="div_enter">
		<a href="javascript:void(0);" onclick="turnToIndex();">
			马上进入<br>enter now
		</a>
	</div>
	<div id="footer" class="div_footer">
		<div class="div_footer_words" id="">
			<a href="javascript:void(0);" onclick="">
				关于我们
			</a>
		</div>
	</div>
	<br />
</body>

<form id="channelForm" action="/channel/life.do" method="post">
</form>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript" src="/js/business.js"></script>
<script type="text/javascript" src="/js/listener.js"></script>
</html>
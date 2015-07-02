<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<%@ include file="/common/common.jsp"%>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>人生百科-评论</title>
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=yes">

<link rel="stylesheet" href="/css/base.css">
<link rel="stylesheet" href="/css/main.css">
<link rel="stylesheet" href="/css/mask.css">
<link rel="stylesheet" href="/css/pad.css" media="only screen and (min-width : 768px) and (max-width : 1200px)">

<script type="text/javascript" src="/js/common.js"></script>
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
.comwidth h1 div{
	padding:0px 20px 0px 20px;
}
.comwidth h1 {
	text-align:left; 
	font: normal 14px/ 30px solid;
	color: #333;
}
</style>
</head>

<body>
	<%@ include file="/common/menu.jsp"%>
	<div class="container">
		<div class="comwidth">
			<c:set var="tms" value="${res.tesTimonial }" scope="page" />
			<c:set var="commentsArray" value="${res.commentsArray }" scope="page" />
			<h1>
				<div>
				发言内容：<br />
				${tms.contents }
				<span style="font: 14px/24px solid; display:block; padding-top: 10px;">
					<a href="javascript:void(0);" style="font:normal 12px/24px;" onclick="openComments('${tms.id}');">
						发表评论
					</a>
				</span>
				</div>
			</h1>
			<h1>
				<div>
				所有评论：<br />
				<c:forEach var="comment" items="${commentsArray }">
					<fmt:parseDate var="dateTemp" value="${comment.createDate }" pattern="yyyyMMddHHmmss" />
					<fmt:formatDate var="createDate" value="${dateTemp }" type="both"/>
					${comment.contents } <br /> ${createDate } <br /><br />
				</c:forEach>
				</div>
			</h1>
		<div class="clear">
		</div>
		</div>
	</div>
	<%@ include file="/common/footer.jsp"%> 
</body>
<c:remove var="res" scope="session"/>

<form id="channelForm" action="/channel/life.do">
</form>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript" src="/js/business.js"></script>
</html>
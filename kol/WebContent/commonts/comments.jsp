<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/common.jsp"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>人生百科-评论</title>
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=yes">

<link href="${ctx }/css/base.css" rel="stylesheet">
<link rel="stylesheet" href="${ctx }/css/main.css">
<link rel="stylesheet" href="${ctx }/css/mask.css">
<link rel="stylesheet" href="${ctx }/css/pad.css" media="only screen and (min-width : 768px) and (max-width : 1200px)">

<script type="text/javascript" src="${ctx }/js/common.js"></script>
<script type="text/javascript" src="${ctx }/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="${ctx }/js/jquery.SuperSlide.2.1.1.js"></script>
<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
<script src="js/html5shiv.min.js"></script>
<script src="js/respond.min.js"></script>
<![endif]-->
<!--[if IE 6]>
<script type="text/javascript" src="js/DD_belatedPNG.js"></script>
<script type="text/javascript">DD_belatedPNG.fix('*');</script>
<![endif]-->
<!--修复IE6下PNG图片背景透明-->
</head>

<body>
	<header style="">
		<div id="logo" class="fl">
			<a href="javascript:void(0);">
				人生<span>BAI</span>科
			</a>
		</div>
		<%@ include file="/common/menu.jsp"%>
		<div class="clear">
		</div>
	</header>
	<div class="container">
		<div class="comwidth">
			<c:set var="tesTimonial" value="${res.tesTimonial }" scope="page" />
			<c:set var="commentsArray" value="${res.commentsArray }" scope="page" />
			<h1 style="text-align:left; padding-left:20px;">
			发言内容：${tesTimonial.contents }
			</h1>
			<h1 style="text-align:left; padding-left:20px;">所有评论：<br /><br />
				<c:forEach var="comment" items="${commentsArray }">
					${comment.contents } <br /> ${comment.createDate } <br /><br />
				</c:forEach>
			</h1>
		<div class="clear">
		</div>
		</div>
	</div>
	<footer>
		<div class="comwidth">
			<p class="fl">
				人生BAI科 © 2015 www.rsbk.com 版权所有
				<span>
					|
				</span>
				<a href="javascript:void(0);">
					联系我们
				</a>
			</p>
			<ul class="foot-nav fr">
				<li>
					<a href="javascript:void(0);">
						排行榜
					</a>
				</li>
				<li>
					<a href="javascript:void(0);">
						标签搜索
					</a>
					<span>
						|
					</span>
				</li>
				<li>
					<a href="javascript:void(0);">
						联系我们
					</a>
					<span>
						|
					</span>
				</li>
				<li>
					<a href="javascript:void(0);">
						积分获取
					</a>
					<span>
						|
					</span>
				</li>
				<li>
					<a href="javascript:void(0);">
						关于我们
					</a>
					<span>
						|
					</span>
				</li>
			</ul>
			<div class="clear">
			</div>
		</div>
	</footer>
</body>
<c:remove var="res" scope="session"/>

<form id="channelForm" action="${ctx }/channel/life.do">
</form>
<script type="text/javascript">
function formTo(formId, href){
	$("#"+formId).prop("action", href);
	$("#"+formId).submit();	
}

// 
jQuery(".slideBox").slide({
	mainCell : ".bd ul",
	autoPlay : true
});

//alert("${ctx}");
//$d("maskDiv").commentsModule();
function openComments(testimonialsId) {
	var commentsBtn = $d("maskDiv").dialogComments(testimonialsId);
	$("#"+commentsBtn).click(function() {
		//alert("commentsBtn");
		formTo("commentsForm", "${ctx }/comments/saveComments.do");
	});
}

/**
 * 查看发言的所有评论
 */
function allCommentsAbout(testimonialsId){
	var newInput = $("<input type='hidden' name='testimonialsId' value='"+testimonialsId+"'>");
	$("#channelForm").append(newInput);
	formTo("commentsForm", "${ctx }/comments/saveComments.do");
}

function openTestimonials() {
	var currentChannelId = '${currentChannelId}';
	$("#"+submitBtnId).click(function() {
		formTo("commentsForm", "${ctx }/testionials/saveContents.do");
	});
}
	
function turnToTuCaoBa(){
	formTo("channelForm", "${ctx }/channel/tucaoba.do");
}
function turnToOther(){
	formTo("channelForm", "${ctx }/channel/other.do");
}
function turnToCareer(){
	formTo("channelForm", "${ctx }/channel/career.do");
}
function turnToEmotion(){
	formTo("channelForm", "${ctx }/channel/emotion.do");
}
function turnToLife(){
	formTo("channelForm", "${ctx }/channel/life.do");
}
function turnToIndex(){
	formTo("channelForm", "${ctx }/channel/index.do");
}

var ohmg = "${ohmg}";
//alert(ohmg);
if(ohmg == "null" || !!! ohmg){
	turnToIndex();
}
</script>
</html>
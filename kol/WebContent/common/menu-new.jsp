<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
.addCommemorate {
	display: none; margin-left: -5px; margin-top: 5px; position: absolute; background: url(/images/addcommemorate.gif) no-repeat; width: 70px; height: 70px; text-align:center; padding-top:23px; cursor: pointer; font:normal 16px "Microsoft Yahei"; color:#fff;
}
</style>
<div id="nav" class="div_nav">
	<!-- 边距152px -->
	 
	<ul>
		<li ><div class="logo_small"></div></li>
		<li class="li_active"><a href="/index.jsp">首页</a></li>
		<li>
			<a href="javascript:void(0);" onmouseover="showAddCommemorate();" onclick="turnTo('/channel/commemorateBoard.do');">纪念板</a>
			<div id="addCommemorate" onclick="addCommemorate();" onmouseout="hideAddCommemorate();" class="addCommemorate">我要纪念</div>
		</li>
		<li><a href="/channel/new.jsp">先吐为快</a></li>
		<li>猜猜我是谁</li>
		<li><a href="javascript:void(0);" onclick="turnToIndex();">看看别人说</a></li>
		<li>加入我们</li>
		<li class="login_li" onclick='formTo("channelForm", "/user/toLogin.do");'>登录网页版 >></li>
	</ul>
</div>

<script type="text/javascript">
</script>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<article>
	<div class="zhuti-bar">
		<a href="javascript:void(0);" class="zhuti">
			<strong>${tms.title }:</strong><br />
			<%-- <img src="${ctx }/images/aimg1.jpg" alt="zhuti"> --%>
		</a>
		<p>${tms.contents}</p>
		<p>
			<span>
				发表时间：${tms.createDate }<br/>
			</span>
			<a href="javascript:void(0);" onclick="openComments('${tms.id}');">
				评价
			</a>
			&nbsp;
			<a href="javascript:void(0);" onclick="allCommentsAbout('${tms.id}');">
				查看所有评价
			</a>
		</p>
	</div>
</article>
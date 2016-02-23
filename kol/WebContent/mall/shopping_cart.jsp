<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<%@ include file="/common/common.jsp"%>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title></title>
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">

<script type="text/javascript" src="/js/jquery-1.11.1.min.js"></script>
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
.product_pic img{
	max-width: 80px;
}
.product_pic {
	width: 80px;
	height: 80px;
	display: block;
	overflow: hidden;
}
</style>
</head>

<body>
	<div class="container">
		${wxuser.nickname}
		<c:if test="${empty wxuser.password }">
			未设置登录密码，建议您<a href="javascript:void(0);" onclick="showSetPwd();">设置</a>
		</c:if>
		
		<c:forEach items="${productList }" var="product">
			<div>
				<div class="product_pic"><img src="${product.mainpic }" /></div>
				<div>${product.name }</div>
				<div>￥ ${product.price }</div>
				<div>月销 66  笔 包邮
					<span onclick="todo();">加入购物车</span>
					<span onclick="directorder('${product.id }');">直接购买</span>
				</div>
			</div>
			<hr />
		</c:forEach>
	</div>
</body>

<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript" src="/js/business.js"></script>
<script type="text/javascript">
function directorder(productid){
	window.location.href="/order/buy.do?productid="+productid;
}
</script>
</html>
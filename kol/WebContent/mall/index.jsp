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

<script type="text/javascript" charset="utf-8" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript">
//alert(location.href.split('#')[0]);

wx.config({
    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    appId: '${appId}', // 必填，公众号的唯一标识
    timestamp: '${timestamp }', // 必填，生成签名的时间戳
    nonceStr: '${nonceStr }', // 必填，生成签名的随机串
    signature: '${signature }',// 必填，签名，见附录1
    jsApiList: [
		'onMenuShareTimeline',
		'onMenuShareAppMessage',
		'onMenuShareQQ',
		'onMenuShareWeibo',
		'onMenuShareQZone',
		'startRecord',
		'stopRecord',
		'onVoiceRecordEnd',
		'playVoice',
		'pauseVoice',
		'stopVoice',
		'onVoicePlayEnd',
		'uploadVoice',
		'downloadVoice',
		'chooseImage',
		'previewImage',
		'uploadImage',
		'downloadImage',
		'translateVoice',
		'getNetworkType',
		'openLocation',
		'getLocation',
		'hideOptionMenu',
		'showOptionMenu',
		'hideMenuItems',
		'showMenuItems',
		'hideAllNonBaseMenuItem',
		'showAllNonBaseMenuItem',
		'closeWindow',
		'scanQRCode',
		'chooseWXPay',
		'openProductSpecificView',
		'addCard',
		'chooseCard',
		'openCard'
	] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
});


</script>
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
<div>
<%-- ${appId}<br />
${timestamp }<br />
${nonceStr }<br />
${signature } --%>
</div>
	<div class="container">
		<div>
			${wxuser.nickname}
			<c:if test="${empty wxuser.password }">
				未设置登录密码，建议您<a href="javascript:void(0);" onclick="showSetPwd();">设置</a>
			</c:if>
			<br />
			<br />
		</div>
		
		<c:forEach items="${productList }" var="product">
			<div>
				<div class="product_pic"><img src="${product.mainpic }" /></div>
				<div>${product.name }</div>
				<div>￥ ${product.price }</div>
				<div>月销 66  笔 包邮
					<span onclick="todo();">加入购物车</span>
					<span onclick="directorder('${product.id }');">直接购买</span>
					<span onclick="getLocation();">查看当前位置</span>
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
	window.location.href="/order/directBuy.do?productid="+productid;
}

function getLocation(){
	wx.getLocation({
	    type: 'gcj02', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
	    success: function (res) {
// 	        var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
// 	        var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
// 	        var speed = res.speed; // 速度，以米/每秒计
// 	        var accuracy = res.accuracy; // 位置精度
	        
	        wx.openLocation({
	            latitude: res.latitude, // 纬度，浮点数，范围为90 ~ -90
	            longitude: res.longitude, // 经度，浮点数，范围为180 ~ -180。
	            name: '', // 位置名
	            address: '', // 地址详情说明
	            scale: 25, // 地图缩放级别,整形值,范围从1~28。默认为最大
	            infoUrl: '' // 在查看位置界面底部显示的超链接,可点击跳转
	        });
	    }
	});
	
	
}
</script>
</html>
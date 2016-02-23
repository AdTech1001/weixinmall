function todo(){
	alert("todo");
}


/**
 * 查看发言的所有评论
 */
function allCommentsAbout(testimonialsId){
	var newInput = $("<input type='hidden' name='testimonialsId' value='"+testimonialsId+"'>");
	$("#channelForm").append(newInput);
	formTo("channelForm", "/comments/queryComments.do");
}

function formTo(formId, href){
	$("#"+formId).prop("action", href);
	$("#"+formId).submit();	
}

function turnToTuCaoBa(){
	formTo("channelForm", "/channel/tucaoba.do");
}
function turnToOther(){
	formTo("channelForm", "/channel/other.do");
}
function turnToCareer(){
	formTo("channelForm", "/channel/career.do");
}
function turnToEmotion(){
	formTo("channelForm", "/channel/emotion.do");
}
function turnToLife(){
	formTo("channelForm", "/channel/life.do");
}
function turnToIndex(){
	formTo("channelForm", "/channel/home.do");
}
function turnTo(href){
	formTo("channelForm", href);
}
/**
 * 顶一次
 * @param id
 */
function topOnce(id, changeAim) {
	var idtemp = id;
	//alert(id);
	$.ajax({
		url:"/commemorate/topOnce.do",
		cache:true,
		data:{
			id: idtemp
		},
		dataType:"text",
		success: function(data){
			if("success" == data){
				var pagetopTimes = $("#"+changeAim).text();
				if(!!! pagetopTimes) {
					pagetopTimes = 0;
				}
				pagetopTimes = pagetopTimes*1 + 1;
				$("#"+changeAim).text(pagetopTimes);
			}
		},
		error: function(){
			alert("顶失败了，没顶上去！重新试一下吧");
		}
		
	});
}

function openComments(testimonialsId) {
	var commentsBtn = $d("maskDiv").dialogComments(testimonialsId);
	$("#"+commentsBtn).click(function() {
		//alert("commentsBtn");
		formTo("commentsForm", "/comments/saveComments.do");
	});
}

function openTestimonials(currentChannelId) {
	var submitBtnId = $d("maskDiv").dialogTestimonials(currentChannelId);
	$("#"+submitBtnId).click(function() {
		if(!! $("#picFile").val()) {
			// 有图片上传
			formTo("commentsForm", "/testimonials/saveContents.do");
		} else {
			// 无图片上传
			$("#commentsForm").prop("enctype", "");
			formTo("commentsForm", "/testimonials/saveContentsNoPic.do");
		}
	});
}

function showAddCommemorate(){
	cancelBubble();
	//$("#addCommemorate").css("borderRight", "1px solid #ccc");
	$("#addCommemorate").slideDown();
}

function hideAddCommemorate(){
	cancelBubble();
	$("#addCommemorate").slideUp();
}

function addCommemorate(){
	window.location.href="/channel/addCommemorate.jsp";
}
$(function(){
	//alert($(".zhuti-bar").eq(0).html());
	//alert($(".zhuti-bar").eq(1).offset().top);
	$("#nextTestimonialDiv").click(function(){
		var currentIndex = $("#currentIndex").val();
		if(currentIndex >= ($(".zhuti-bar").length -1)) {
			alert("没有了");
			return false;
		}
		var nextIndex = currentIndex*1 + 1;
		// 当前置为 nextIndex
		$("#currentIndex").val(nextIndex);
		$("html,body").animate({scrollTop:$(".zhuti-bar").eq(nextIndex).offset().top},1000);//1000是ms,也可以用slow代替
	});
	
	$("#prevTestimonialDiv").click(function(){
		var currentIndex = $("#currentIndex").val();
		if(currentIndex <= 1) {
			alert("没有了");
			return false;
		}
		var nextIndex = currentIndex*1 - 1;
		// 当前置为 nextIndex
		$("#currentIndex").val(nextIndex);
		$("html,body").animate({scrollTop:$(".zhuti-bar").eq(nextIndex).offset().top},1000);//1000是ms,也可以用slow代替
	});
	
	$("#totopTestimonialDiv").click(function(){
		var totop = 0;
		if($(".zhuti-bar").length-1 > 0) {
			totop = $(".zhuti-bar").eq(1).offset().top;
		}
		var nextIndex = 1;
		// 当前置为 nextIndex
		$("#currentIndex").val(nextIndex);
		$("html,body").animate({scrollTop: totop},1000);//1000是ms,也可以用slow代替
	});
	
	$("#toendTestimonialDiv").click(function(){
		var total = $(".zhuti-bar").length;
		if(total-1 <=0) {
			alert("没有了");
			return false;
		}
		var toend = $(".zhuti-bar").eq(total-1).offset().top;
		// 当前置为 nextIndex
		$("#currentIndex").val(total);
		$("html,body").animate({scrollTop: toend},1000);//1000是ms,也可以用slow代替
	});
});

function showallwords(obj){
	var allwords = $(obj).next().val();
	//$(obj).parent().empty();
	$(obj).parent().text(allwords);
}
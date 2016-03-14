/* JS AOP 
 * 注：必需要在函数执行之前，先执行切入函数，如果函数已经执行，再调用切入函数则无效 
 * */

(function(window) {
	aop = {
		/**
		 * @param fnName 目标执行函数名称
		 * @param beforeFn 目标函数执行前切入的函数
		 * @param params 目标执行函数的参数
		 * @param windowObj 目标执行函数所属的父对象
		 */
		before : function (fnName , beforeFn, params, windowObj) {
			if(!!! windowObj){
				windowObj = window;
			}
			if(typeof(windowObj) == 'function') {
				windowObj = windowObj.prototype; 
			}
			if(typeof(windowObj[fnName]) != 'function') {
				return ; 
			}
			if(typeof(beforeFn) != 'function') {
				return ; 
			}
			this.initParam(params);
			var target = windowObj[fnName];
			target = target || {};
			//alert(windowObj[fnName]);
			windowObj[fnName] = function () { 
				//beforeFn.call(this,params);
				beforeFn(params);
				return target.apply(this, arguments); 
			};
		},
		after : function (fnName , afterFn , params, windowObj) { 
			if(!!! windowObj){
				windowObj = window;
			}
			if(typeof(windowObj) == 'function') {
				windowObj = windowObj.prototype ; 
			}
			if(typeof(windowObj[fnName]) != 'function') {
				return ; 
			}
			if(typeof(afterFn) != 'function') {
				return ; 
			}
			this.initParam(params);
			var target = windowObj[fnName] ;
			target = target || {};
			windowObj[fnName] = function () { 
				var returnValue = target.apply(this, arguments); 
				afterFn.call(this, params); 
				return returnValue; 
			};
		}, 
		initParam: function(params){
			params = params || {};
			var browserInfo = this.getBrowserInfo();
			params["browserName"] = browserInfo.browserName;
			params["browserVersion"] = browserInfo.browserVersion;
			//params["currentPage"] = document.URL;
			params["currentPage"] = "";
			//params["userAgent"] = navigator.userAgent.toLowerCase();
			params["userAgent"] = "";
		},
		getBrowserInfo: function(){
			var obj = {};
			if(!! this.browserInfo) {
				obj = this.browserInfo;
				return obj;
			}
			var ua = navigator.userAgent.toLowerCase();

			var  browserName = "";
			var  browserVersion = "";
			if (window.ActiveXObject){
	    	    var ieMatch = ua.match(/msie ([\d.]+)/);
	    	    if(ieMatch && ieMatch.length >1){
	    	    	browserName = "IE"; 
	    	    	browserVersion = ieMatch[1];
	    	    }
	        }
			
			if (document.getBoxObjectFor || true){
	    	 	var firefoxMatch = ua.match(/firefox\/([\d.]+)/);
	    	 	if(firefoxMatch  && firefoxMatch.length > 1){
	    	 		browserName = "FireFox"; 
	    	 		browserVersion = firefoxMatch[1];
	    	 	}
	    	}
			
			if (window.MessageEvent && !document.getBoxObjectFor){
	    		var chromeMatch = ua.match(/chrome\/([\d.]+)/);
	    		if(chromeMatch && chromeMatch.length > 1){
	    			var baiduMatch = ua.match(/bidubrowser\/([\d.]+)/);
	        		if(!!baiduMatch){
	        			browserName = "BaiDu"; 
	        			browserVersion = ua.match(/bidubrowser\/([\d.]+)/)[1];
	    			} else {
	    				browserName = "Chrome"; 
	    				browserVersion = chromeMatch[1];
	             	  	// 采用无刷新重加载方案
	    			}
	    		}
	    	}
			
			if (window.opera){
	    		var operaMatch = ua.match(/opera.([\d.]+)/);
	    		if(operaMatch && operaMatch.length > 1){
	    			browserName = "Opera"; 
	    			browserVersion = operaMatch[1];
	    		}
	    	}
			
			if (window.openDatabase){
	    		var safariMatch = ua.match(/version\/([\d.]+)/);
	    		if(safariMatch && safariMatch.length > 1){
	    			browserName = "Safari"; 
	    			browserVersion = safariMatch[1];
	    		} 	
	    	}
			//alert(browserName);
			//alert(browserVersion);
			obj["browserName"] = browserName;
			obj["browserVersion"] = browserVersion;
			// 缓存处理
			this.browserInfo = obj;
			return obj;
		}
	};
    window.SANDPAY = this;
})(window);

/**
* 跟踪用户操作
*/
function followRecord(params){
	//alert("followRecord");
	params = params || {};
	$.ajax({
		type : "post",
		url : "/js/jsListener.do",
		timeout : 45000,
		data : params,
		dataType : "json",
		success : function(){},
		error : function(){}
	});
};

function logObj(obj){
	var str ="";
	for (var k in obj){
		str += k +":"+obj[k] +"  ";
	}
	return str;
}

/**
* 跟踪用户操作
*/
var UserOperate = function(params){
	//alert("turnToIndex");
	params["listener"] = "UserOperate";
	followRecord(params);
};

//
SANDPAY.aop.before("allCommentsAbout",UserOperate, {
	"operate":"所有评论"
});
SANDPAY.aop.before("turnToOther",UserOperate, {
	"operate":"其他频道"
});
SANDPAY.aop.before("turnToCareer",UserOperate, {
	"operate":"工作频道"
});
SANDPAY.aop.before("turnToEmotion",UserOperate, {
	"operate":"情感频道"
});
SANDPAY.aop.before("turnToLife",UserOperate, {
	"operate":"生活频道"
});
SANDPAY.aop.before("turnToIndex",UserOperate, {
	"operate":"首页"
});
SANDPAY.aop.before("turnTo",UserOperate, {
	"operate":"统一跳转函数"
});

/*
// 目标切入函数必需在listener.js文件加载前加载, 或与listener.js一同加载
function testListener(){
	alert("testListener");
}
*/
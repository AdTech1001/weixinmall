package com.org.filter;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONObject;

import com.org.common.JspConstant;
import com.org.common.UserConstant;
import com.org.log.LogUtil;
import com.org.log.impl.LogUtilMg;
import com.org.util.CT;
import com.org.utils.PropertyUtil;

/**
 * @author zhou.m
 * 
 * User Login Check
 */
public class UserSessionFilter implements Filter {
	private Log log = LogFactory.getLog(UserSessionFilter.class);
	protected String loginPath = null;
	
	public void init(FilterConfig config) throws ServletException {
		loginPath = config.getServletContext().getContextPath()+ config.getInitParameter("loginPath");
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {
			HttpServletRequest req = (HttpServletRequest) request;
			
			String uri = req.getRequestURI();
			String key = "";
			if(uri.equals(CT.SYMBOL_XG)){
			}else{
				if(uri.endsWith(CT.SYMBOL_XG)){
					uri = uri.substring(0, uri.length()-1);
				}
				
				int begin = uri.indexOf(CT.SYMBOL_XG);
				int end   = uri.indexOf(CT.SYMBOL_WH);
			
				if(end == -1){
					end  = uri.length();
				}
				key = uri.substring(begin, end);
			}
			
			String nocheckSessionPages = PropertyUtil.getValue("no_check_session", "pageadress");
			
			if(nocheckSessionPages.indexOf(key) > -1){
				// 表示在列表中,不需要check session
				chain.doFilter(request, response);
				return;
			}
			/*JSONObject sessionUser = (JSONObject)req.getSession(true).getAttribute(UserConstant.SESSION_USER);
			
			if(sessionUser == null){
				log.info("您已超时，请重新进入");
				request.setAttribute(CT.RESP_CODE_NAME, "");
				request.setAttribute(CT.RESP_RESULT_NAME, "您已超时，请重新进入");
				String targetUrl = JspConstant.ERROR_PAGE;
				try {
					RequestDispatcher rd = request.getRequestDispatcher(targetUrl);
					rd.forward(request, response);
					return;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}*/
			chain.doFilter(request, response);
			return;
		} catch (Exception e) { 
			LogUtil.log(CT.LOG_CATEGORY_ERR, "验证过程失败：" + e.getMessage(), e, LogUtilMg.LOG_ERROR, CT.LOG_PATTERN_ERR);
		}
	}
	
	public void destroy() {
		loginPath = null;
	}
	
}

package com.org.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;

import org.springframework.stereotype.Controller;

import com.jspsmart.upload.File;
import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import com.org.common.CommonConstant;
import com.org.common.PageConstant;
import com.org.log.LogUtil;
import com.org.log.impl.LogUtilMg;
import com.org.servlet.CommonController;
import com.org.servlet.SmpHttpServlet;
import com.org.util.CT;
import com.org.utils.DateUtil;

/**
 * �û�����
 * @author Administrator
 * 
 */
@Controller
public class FileController extends SmpHttpServlet implements CommonController {
	private static final long serialVersionUID = 1L;
	//private static final String excelUploadPath = SmpPropertyUtil.getValue("filepath", "upload_file_path");
	private static final String excelUploadPath = "c:/";

	public FileController() {
		super();
	}

	public void post(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		try {} catch (Exception se) {
			se.printStackTrace();
		}
	}
	
	/**
	 * �ϴ��ļ�
	 * 
	 * @param request
	 * @param response
	 * @throws Exception 
	 * @throws IOException
	 */
	public void uploadFile(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		StringBuffer pathTemp = new StringBuffer();
		
		// ����Ŀ¼ / �̻�ID / ���� / �ļ���
		String dateStr = DateUtil.getCurrentShortDateStr();
		pathTemp.append(excelUploadPath).append("/").append(dateStr);
		java.io.File dir = new java.io.File(pathTemp.toString());
		// ��֤Ŀ¼����
		if(!dir.exists()){
			dir.mkdirs();
		}
		
		String targetUrl = PageConstant.SUCCESS;
		LogUtil.log(CT.LOG_CATEGORY_SERVLET, "�ļ��ϴ�", null, LogUtilMg.LOG_INFO, CT.LOG_PATTERN_SERVLET);
		// �½�һ��SmartUpload����
		SmartUpload su = null;
		try {
			su = initSmartUpload(request, response);
		} catch (java.lang.SecurityException e) {
			//e.printStackTrace();
			request.getSession().setAttribute(CT.RESP_CODE_NAME, "ERROR");
			request.getSession().setAttribute(CT.RESP_RESULT_NAME, "�ϴ��ļ����ʹ���");
			this.redirect(PageConstant.ERROR, response);
			return;
		}
        //���Ҫʵ���ļ��������ϴ�����ֻ����forѭ������getFile(0)�е�0��Ϊi����
		File file = su.getFiles().getFile(0);
		String fileName = new String(file.getFileName().getBytes(), "UTF-8");
		pathTemp.append("/").append(fileName);
		
		// �ȱ��浽��ʱĿ¼
		file.saveAs(pathTemp.toString());
		// ����ʱĿ¼�õ��ļ�
		java.io.File temp = new java.io.File(pathTemp.toString());
		
	}

	private SmartUpload initSmartUpload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, SmartUploadException {
		SmartUpload su = new SmartUpload();
		// �ϴ���ʼ��
		JspFactory jspFactory = null;
        jspFactory = JspFactory.getDefaultFactory();
        PageContext pc = jspFactory.getPageContext((HttpServlet)request.getSession().getAttribute(CommonConstant.SERVLET),request,response,"",true,8192,true);
		su.initialize(pc);
		LogUtil.log(CT.LOG_CATEGORY_SERVLET, "�ϴ����������ֽڳ���: " + request.getContentLength(), null, LogUtilMg.LOG_INFO, CT.LOG_PATTERN_SERVLET);
		// �趨�ϴ�����
		// 1.����ÿ���ϴ��ļ�����󳤶ȡ�
		su.setMaxFileSize(5000000);
		// 2.�������ϴ����ݵĳ��ȡ�
		su.setTotalMaxFileSize(50000000);
		// 3.�趨�����ϴ����ļ���ͨ����չ�����ƣ�,������doc,txt�ļ���
		su.setAllowedFilesList("xls,xlsx");
		// 4.�趨��ֹ�ϴ����ļ���ͨ����չ�����ƣ�,��ֹ�ϴ�����exe,bat,jsp,htm,html��չ�����ļ���û����չ�����ļ���
		su.setDeniedFilesList("exe,bat,jsp,htm,html,,");
		// �ϴ��ļ�
		su.upload();
				
		return su;
	}
	
}

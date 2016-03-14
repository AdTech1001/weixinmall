package com.org.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

import com.org.interfaces.controller.CommonController;
import com.org.servlet.SmpHttpServlet;
import com.org.utils.FileUploadUtil;

@Controller
public class GoodsController extends SmpHttpServlet implements CommonController{
	private static final long serialVersionUID = 2156792239072761671L;
	private static JSONArray arr = new JSONArray();

	static {
		JSONObject temp = new JSONObject();
		temp.put("goodsPrice", "200.00");
		temp.put("goodsName", "����");
		temp.put("goodsCounts", "31");
		temp.put("id", "1");
		temp.put("picPath", "/www/images/p1.png");
		arr.add(temp);
		
		temp = new JSONObject();
		temp.put("goodsPrice", "130.00");
		temp.put("goodsName", "����");
		temp.put("goodsCounts", "98");
		temp.put("id", "2");
		temp.put("picPath", "/www/images/p2.png");
		arr.add(temp);
		
		temp = new JSONObject();
		temp.put("goodsPrice", "20.00");
		temp.put("goodsName", "����");
		temp.put("goodsCounts", "135");
		temp.put("id", "3");
		temp.put("picPath", "/www/images/p3.png");
		arr.add(temp);
	}
	public GoodsController(){
		
	}
	
	private Log log = LogFactory.getLog(GoodsController.class);

	@Override
	public void post(HttpServletRequest request, HttpServletResponse response) {
		log.info("GoodsController.......");
		// ͬԴ���Բ������ȡXXX�ϵ�Զ����Դ
		// ����������������Դ������Զ�������������ʵġ���Ȼ���˴���*Ҳ�����滻Ϊָ�������������ڰ�ȫ���ǣ����齫*�滻��ָ��������
		response.setHeader("Access-Control-Allow-Origin", "*");
		try {
			FileUploadUtil.uploadFile(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	public void toShelf(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("toShelf.......");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		request.setAttribute("goodsArr", arr);
		this.forward("/www/html/shelf.jsp", request, response);
		return;
	}

	public void edit(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("GoodsController.......");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		String goodsId = request.getParameter("goodsId");
		log.info("goodsId......." + goodsId);
		
		JSONObject aim = new JSONObject();
		for (int i = 0; i < arr.size(); i++) {
			if(arr.getJSONObject(i).getString("id").equals(goodsId)) {
				aim = arr.getJSONObject(i);
				break;
			}
		}
		request.setAttribute("goods", aim);
		this.forward("/www/html/shelf_modify.jsp", request, response);
		return;
	}

	public void save(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.info("GoodsController save.......");
		response.setHeader("Access-Control-Allow-Origin", "*");

		String id = request.getParameter("id");
		String goodsPrice = request.getParameter("goodsPrice");
		String goodsName = request.getParameter("goodsName");
		String goodsCounts = request.getParameter("goodsCounts");
		String picPath = request.getParameter("picPath");
		
		
		JSONObject aim = null;
		for (int i = 0; i < arr.size(); i++) {
			if(arr.getJSONObject(i).getString("id").equals(id)) {
				aim = arr.getJSONObject(i);
				aim.put("goodsPrice", goodsPrice);
				aim.put("goodsName", goodsName);
				aim.put("goodsCounts", goodsCounts);
				aim.put("id", id);
				aim.put("picPath", picPath);
				break;
			}
		}
		request.setAttribute("goodsArr", arr);
		this.forward("/www/html/shelf.jsp", request, response);
		return;
	}
}

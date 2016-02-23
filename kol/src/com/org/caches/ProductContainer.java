package com.org.caches;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.org.interfaces.caches.Container;
import com.org.services.ProductService;
import com.org.util.SpringUtil;

/**
 *
 */
public class ProductContainer implements Container{
	public static final Integer DEFAULT_ROOM_ID = 0;
	private static ProductContainer temp;
	private Map<Integer, JSONObject> container;
	private JSONArray arr;
	private ProductContainer(){}

	public JSONObject getById(Integer id){
		if(container.containsKey(id)) {
			return container.get(id);
		}
		log.info("缓存无信息，执行数据库查询");
		ProductService service = (ProductService)SpringUtil.getBean("productService");
		JSONObject res = service.query(id);
		container.put(id, res);
		return res;
	}

	public JSONArray getAll(){
		if(arr == null) {
			arr = new JSONArray();
			if(container != null && container.size() > 0) {
				JSONObject temp = null;
				Integer key = null;
				for (Iterator<?> iterator = container.keySet().iterator(); iterator.hasNext();) {
					key = (Integer) iterator.next();
					temp = container.get(key);
					arr.add(temp);
				}
			}
		}
		return arr;
	}
	
	public void init(){
		container = new HashMap<Integer, JSONObject>();
		
		ProductService service = (ProductService)SpringUtil.getBean("productService");
		// 保存，并返回数据库保存的用户信息
		JSONArray crlist = service.queryAll();
		if(crlist != null && crlist.size() > 0) {
			// 如果有，则添加进来
			JSONObject temp = null;
			for (int i = 0; i < crlist.size(); i++) {
				temp = crlist.getJSONObject(i);
				container.put(temp.getInt("id"), temp);
			}
			log.info("已初始化商品信息"+ crlist.size() +"条");
		}
	}
	
	public static ProductContainer getInstance(){
		if(temp == null) {
			temp = new ProductContainer();
		}
		return temp;
	}
	
	private static Log log = LogFactory.getLog(ProductContainer.class);
}

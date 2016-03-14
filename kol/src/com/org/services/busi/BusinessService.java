package com.org.services.busi;
//package com.org.services;
//
//import java.util.Map;
//
//import org.springframework.util.StringUtils;
//
//import com.org.Connection;
//import com.org.common.CommonConstant;
//import com.org.utils.JSONUtils;
//
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//
//public class BusinessService {
//	
//	/**
//	 * 
//	 * @param queryFlag
//	 * @param objectParam 这是一个任意目标参数，例如它可以是一个sql或是集合名
//	 * @param queryParams
//	 * @param con
//	 * @return
//	 */
//	public JSONObject executeQuery(String queryFlag, String objectParam, 
//			JSONObject queryParams, Connection con){
//
//		if(!queryParams.containsKey("sortBy") || StringUtils.isEmpty(queryParams.containsKey("sortBy"))){
//			queryParams.put("sortBy", "orderTime");
//		}
//		
//		if(!queryParams.containsKey("sortSerial") || StringUtils.isEmpty(queryParams.containsKey("sortSerial"))){
//			queryParams.put("sortSerial", CommonConstant.ASC);
//		}
//		
//		if(!queryParams.containsKey("currentPage") || StringUtils.isEmpty(queryParams.containsKey("currentPage"))){
//			queryParams.put("currentPage", 0);
//		}
//		
//		if(!queryParams.containsKey("pageCounts") || StringUtils.isEmpty(queryParams.containsKey("pageCounts"))){
//			queryParams.put("pageCounts", 20);
//		}
//		
//		JSONObject result = new JSONObject();
//		Map<Object, Object> paramss = JSONUtils.parseJSON2Map(queryParams);
//		JSONObject res = null;
//		if(queryFlag.equals("query")){
//			res = con.queryList(paramss, objectParam);
//		}
//
//		JSONObject totalObj = (JSONObject)res.remove("totalObj");
//		JSONArray record = (JSONArray)res.remove("record");
//		
//		result.put("currentPage", queryParams.getString("currentPage"));
//		result.put("totalCounts", totalObj.getString("totalCounts"));
//		result.put("totalPageCounts", totalObj.getString("totalPageCounts"));
//		result.put("memo1", "");
//		result.put("memo2", "");
//		result.put(CommonConstant.RESP_CODE, "10000");
//		result.put(CommonConstant.RESP_MSG, "操作成功");
//		result.put("busiInfo", record);
//		return result;
//	}
//	
//	public static BusinessService getInstance(){
//		if(bs == null){
//			bs = new BusinessService();
//		}
//		return bs;
//	}
//	private static BusinessService bs;
//}

package com.org.caches;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.org.interfaces.caches.Container;
import com.org.model.ChatingRoom;
import com.org.services.RoomService;
import com.org.util.SpringUtil;

/**
 *
 */
public class RoomContainer implements Container{
	public static final Integer DEFAULT_ROOM_ID = 0;
	private Map<Integer, ChatingRoom> roomMap;
	private static RoomContainer temp;
	private RoomContainer(){}

	/**
	 * 获取用户信息
	 * @param openid
	 * @return
	 */
	public ChatingRoom getRoomById(Integer roomId){
		if(roomMap.containsKey(roomId)) {
			return roomMap.get(roomId);
		}
		log.info("缓存无信息，执行数据库查询");
		RoomService service = (RoomService)SpringUtil.getBean("roomService");
		// 保存，并返回数据库保存的用户信息
		ChatingRoom cr = service.query(roomId);
		return cr;
	}

	public void init(){
		roomMap = new HashMap<Integer, ChatingRoom>();
		
		RoomService service = (RoomService)SpringUtil.getBean("roomService");
		// 保存，并返回数据库保存的用户信息
		List<ChatingRoom> crlist = service.queryRoomList();
		if(crlist != null && crlist.size() > 0) {
			// 如果有，则添加进来
			ChatingRoom temp = null;
			for (int i = 0; i < crlist.size(); i++) {
				temp = crlist.get(i);
				roomMap.put(temp.getRoomid(), temp);
			}
			log.info("已初始化聊天室信息"+ crlist.size() +"条");
		} else {
			// 如果没有房间的话，则创建一个默认的房间
			roomMap.put(0, createDefaultRoom());
		}
	}
	
	private ChatingRoom createDefaultRoom(){
		Integer roomId = 0;
		// 房间名称
		String roomName = "快乐群";
		// 房间主题 
		String roomTitle = "开心啦";
		ChatingRoom cr = new ChatingRoom();
		cr.setRoomid(roomId);
		cr.setRoomname(roomName);
		cr.setRoomtitle(roomTitle);
		return cr;
	}

	public static RoomContainer getInstance(){
		if(temp == null) {
			temp = new RoomContainer();
		}
		return temp;
	}
	
	private static Log log = LogFactory.getLog(RoomContainer.class);
}

package com.org.caches;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.org.model.ChatingRoom;
import com.org.services.RoomService;
import com.org.util.SpringUtil;

/**
 *
 */
public class RoomContainer {
	public static final Integer DEFAULT_ROOM_ID = 0;
	private static Map<Integer, ChatingRoom> roomMap = new HashMap<Integer, ChatingRoom>();
	private RoomContainer(){}

	/**
	 * ��ȡ�û���Ϣ
	 * @param openid
	 * @return
	 */
	public static ChatingRoom getRoomById(Integer roomId){
		if(roomMap.containsKey(roomId)) {
			return roomMap.get(roomId);
		}
		RoomService service = (RoomService)SpringUtil.getBean("roomService");
		// ���棬���������ݿⱣ����û���Ϣ
		ChatingRoom cr = service.query(roomId);
		return cr;
	}

	public static void initRoom(){
		roomMap = new HashMap<Integer, ChatingRoom>();
		
		RoomService service = (RoomService)SpringUtil.getBean("roomService");
		// ���棬���������ݿⱣ����û���Ϣ
		List<ChatingRoom> crlist = service.queryRoomList();
		if(crlist != null && crlist.size() > 0) {
			// ����У�����ӽ���
			ChatingRoom temp = null;
			for (int i = 0; i < crlist.size(); i++) {
				temp = crlist.get(i);
				roomMap.put(temp.getRoomid(), temp);
			}
		} else {
			// ���û�з���Ļ����򴴽�һ��Ĭ�ϵķ���
			roomMap.put(0, createDefaultRoom());
		}
	}
	
	private static ChatingRoom createDefaultRoom(){
		Integer roomId = 0;
		// ��������
		String roomName = "����Ⱥ";
		// �������� 
		String roomTitle = "������";
		ChatingRoom cr = new ChatingRoom();
		cr.setRoomid(roomId);
		cr.setRoomname(roomName);
		cr.setRoomtitle(roomTitle);
		return cr;
	}
	
}

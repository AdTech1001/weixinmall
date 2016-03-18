package com.org.caches;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.org.interfaces.caches.Container;
import com.org.interfaces.room.Room;
import com.org.model.wx.ChatingRoom;
import com.org.services.RoomService;
import com.org.util.SpringUtil;

/**
 *
 */
public class RoomContainer implements Container{
	public static final Integer DEFAULT_ROOM_ID = 0;
	public static final Integer STORY_ROOM_ID = 1;
	private Map<Long, Room> roomMap;
	private static RoomContainer temp;
	private RoomContainer(){}

	/**
	 * ��ȡ�û���Ϣ
	 * @param openid
	 * @return
	 */
	public Room getRoomById(Integer roomId){
		if(roomMap.containsKey(roomId)) {
			return roomMap.get(roomId);
		}
		log.info("��������Ϣ��ִ�����ݿ��ѯ");
		RoomService service = (RoomService)SpringUtil.getBean("roomService");
		// ���棬���������ݿⱣ����û���Ϣ
		ChatingRoom cr = service.query(roomId);
		return cr;
	}

	public void init(){
		roomMap = new HashMap<Long, Room>();
		
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
			log.info("�ѳ�ʼ����������Ϣ"+ crlist.size() +"��");
		} else {
			// ���û�з���Ļ����򴴽�һ��Ĭ�ϵķ���
			roomMap.put(Long.valueOf("0"), createDefaultRoom());
		}
	}
	
	private ChatingRoom createDefaultRoom(){
		Integer roomId = 0;
		// ��������
		String roomName = "����Ⱥ";
		// �������� 
		String roomTitle = "������";
		ChatingRoom cr = new ChatingRoom();
		cr.setRoomid(Long.valueOf(roomId));
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

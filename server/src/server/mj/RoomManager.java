package server.mj;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.TimerTask;

import common.Log;

/**
 * 房间分配 回收
 * @author xue
 */
public class RoomManager implements TimerTask {

	/**
	 *  正在游戏的房间列表
	 */
	public Map<Integer,Room> gamingRooms;  
	/**
	 * 等待开始的房间列表
	 */
	public Map<Integer,Room> waitinRooms;  
	/**
	 * 空闲房间ID集合
	 */
	public int[] freeIds;
	
	public RoomManager()
	{
		gamingRooms = new LinkedHashMap<Integer, Room>();
		waitinRooms = new LinkedHashMap<Integer, Room>();
		freeIds = new int[100];
	}
	/**
	 * 根据条件匹配房间
	 */
	public synchronized boolean automath(MgsPlayer player,int playerLimit, int skillLimit,int quanLimit)
	{
		Room r = getMatchedRoom(playerLimit,skillLimit,quanLimit);
		//if(r == null)r = createNewRoom(player,playerLimit,skillLimit,quanLimit);
		if(r != null)
		{
			r.addPlayer(player);
			if(playerLimit == 2)
			{
				r.start_robotTimer();
			}
			return true;
		}
		return false;
	}
	private synchronized Room getMatchedRoom(int playerLimit, int skillLimit,int quanLimit)
	{
		Iterator<Room> it = waitinRooms.values().iterator();
		while(it.hasNext())
		{
			Room r = it.next();
			if(r == null)continue;
			if(r.players.size() >= r.getPlayerLimit())continue; //房间人数满了
			if(playerLimit != 0 &&r.getPlayerLimit() != 0&&playerLimit != r.getPlayerLimit()) continue;
			if(skillLimit  != 0 &&r.getSkillLimit()  != 0&&skillLimit  != r.getSkillLimit())  continue;
			if(quanLimit   != 0 &&r.getQuanLimit()   != 0&&quanLimit   != r.getQuanLimit())   continue;
			
			if(r.getPlayerLimit() ==0)	r.setPlayerLimit(playerLimit);
			if(r.getSkillLimit()  ==0)	r.setSkillLimit(skillLimit);
			if(r.getQuanLimit()   ==0) 	r.setQuanLimit(quanLimit);
			return r;
		}
		return null;
	}
	private synchronized Room createNewRoom(MgsPlayer player,int playerLimit, int skillLimit,int quanLimit)
	{
		Room r  = new Room();
		r.setPlayerLimit(playerLimit);
		r.setSkillLimit(skillLimit);
		r.setQuanLimit(quanLimit);
		//r.initZhuangId();
//		r.addPlayer(player);
		r.setRoomID(this.getFreeRoomId());
		waitinRooms.put(r.getRoomID(),r);
		return r;
	}
	/**
	 * 从等待队列中删除房间， 自动进入游戏队列
	 */
	public synchronized void removeRoomFromWaitList(Room r)
	{
		Log.info("removeRoomFromWaitList");
		if(!this.waitinRooms.containsKey((Integer)r.getRoomID()))
			{
				Log.error("删除等待房间错误，不存在这个房间：" + r.getRoomID());
				return;
			}
		this.waitinRooms.remove((Integer)r.getRoomID());
		this.gamingRooms.put(r.getRoomID(), r);
		
		//重置房间开始条件
//		r.resetFirtRoom();
		
	}
	/**
	 * 从游戏队列中删除房间， 自动进入等待队列  
	 */
	public synchronized void removeRoomFromGamingList(Room r)
	{
		if(!this.gamingRooms.containsKey((Integer)r.getRoomID()))
		{
			Log.error("删除游戏房间错误，不存在这个房间:" + r.getRoomID());
			return;
		}
		
		Log.info("removeRoomFromGamingList");
		this.gamingRooms.remove((Integer)r.getRoomID());
		this.waitinRooms.put(r.getRoomID(), r);
		r.room_state = MgsRoom.CREATED;
		r.fengkeArray = null;
		r.setZhuang(null);
		r.setZhuangId(-1);
		r.zhuangLoc = 0;
		r.lianzhuang = 0;
		r.firtPan = true;
		if(r.players == null)return;
		for(int i = 1;i <= 4; i++ )
		{
			MgsPlayer p = r.getPlayers().get(i);
			if(p == null)continue;
			//p.setQi(0);
			//p.setYun(0);
		}
		
		if(r.players != null && r.leavedPlayer != null)
		{
			for(int i = 0;i < r.leavedPlayer.size();i++)
			{
				MgsPlayer tmp = r.leavedPlayer.get(i);
				r.players.remove(tmp.getLocId());
			}
		}
	}
	/**
	 * 新建房间
	 */
	public synchronized void createRoom(MgsPlayer player,String rName,int playerLimit, int skillLimit,int quanLimit)
	{
		Room r = this.createNewRoom(player, playerLimit, skillLimit, quanLimit);
		if(r != null)
		{
			r.setManager(player);
			r.setRoomName(rName);
			r.addPlayer(player);
			if(playerLimit == 2)
			{
				r.start_robotTimer();
			}
		}
	}
	/**
	 * 房间销毁，归还房间
	 */
	public synchronized void destroyRoom(Room r)
	{
		if(this.waitinRooms.containsKey((Integer)r.getRoomID()))
			this.waitinRooms.remove((Integer)r.getRoomID());
		if(this.gamingRooms.containsKey((Integer)r.getRoomID()))
			this.gamingRooms.remove((Integer)r.getRoomID());
		givebackRoomId(r.getRoomID());
		//r.destoryRoom();
	}
	/**
	 * 获得空闲的房间ID
	 */
	public synchronized int getFreeRoomId()
	{
		int freeId = 0;
		int oldSize = this.freeIds.length;
		for(int i = 0;i < this.freeIds.length;i++)
		{
			freeId = this.freeIds[i];
			if(freeId == 0)
			{
				this.freeIds[i] = 1;
				return i+1;
			}
		}
		//扩容
		int newCapacity = (this.freeIds.length * 3)/2 + 1;
		int[] newArr = new int[newCapacity];
		System.arraycopy(freeIds, 0, newArr, 0, this.freeIds.length);
		this.freeIds = newArr;
		for(int i = oldSize-1;i < this.freeIds.length;i++)
		{
			freeId = this.freeIds[i];
			if(freeId == 0)
			{
				this.freeIds[i] = 1;
				return i+1;
			}
		}
		return 0;
	}
	/**
	 * 房间销毁后，归还房间ID
	 */
	public synchronized void givebackRoomId(int id)
	{
		if(id -1 >= this.freeIds.length)return;
		this.freeIds[id - 1] = 0; 
	}
	@Override
	public void run(Timeout arg0) throws Exception {
		
		//定时检查房间,清理闲置房间
	}
	
	public boolean isInGaming(String name)
	{
		Iterator<Room> it  = this.gamingRooms.values().iterator();
		
		while(it.hasNext())
		{
			Room r = it.next();
			
			Hashtable<Integer, MgsPlayer> players  = r.players;
			Iterator<MgsPlayer> it_p = players.values().iterator();
			while(it_p.hasNext())
			{
				MgsPlayer p = it_p.next();
				if(p.getBusiness().getPlayer().getName().equals(name))
					return true;
			}
		}
		return false;
	}
	
}

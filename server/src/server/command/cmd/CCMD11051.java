package server.command.cmd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;

import common.Log;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.Global;
import server.mj.MgsPlayer;
import server.mj.Room;

/**
 *  获取房间列表
 * @author xue
 */
public class CCMD11051 extends  CMD{
	
	private static final int PAGE_SIZE = 4;	
	
	private int count = 0;
	private boolean debug = false;
	private int startRec = 0;
	
	public void setPlayer(MgsPlayer player) {
		this.player = player;
	}
	public ChannelBuffer getBytes() {
		return null;
	}
	public ChannelBuffer getBytes(Object obj) {
		return null;
	}
	public void setBytes(ChannelBuffer buf) {
		super.setBytes(buf);

		int page = this.getIntVaule(0);
		Map<Integer,Room>  mapGaming = Global.rManager.gamingRooms;
		Map<Integer,Room>  waitmap = Global.rManager.waitinRooms;
		
		
		Iterator<Room> it = waitmap.values().iterator();
		startRec = (page -1) * PAGE_SIZE;
		int total = (waitmap.size() +mapGaming.size()) % PAGE_SIZE == 0 ? (waitmap.size() +mapGaming.size()) / PAGE_SIZE : (waitmap.size() +mapGaming.size()) / PAGE_SIZE + 1;
	
		if(total == 0) total = 1;
		if(page > total)
			page = total;
		MyArray arr = new MyArray();
//		//当前页
		arr.push(page);
//		//总页数
		arr.push(total);
		List<Object> list = new ArrayList<Object>();
		while(it.hasNext())
		{
			count++;
			if(count < startRec)continue;
			if(count >= startRec + PAGE_SIZE)break;
			Room r = it.next();
			//当前人数
			int nowPP = r.getPlayers().size();
			if(nowPP < 0)
			{
				nowPP = 0;
			}
			if(nowPP == 0)
			{
				Log.log("");
				Global.rManager.destroyRoom(r);
				continue;
			}
			//总的人数
			int totalPP = r.getPlayerLimit();
			List<Object> tmpList = new ArrayList<Object>();
			tmpList.add(r.getRoomID());
			tmpList.add(nowPP);
			tmpList.add(totalPP);
			tmpList.add(false);
			list.add(tmpList);
		}
		if(count <= startRec)
			getRoomInfo(list);
		arr.push(list);
		MyByteArray byteBuf = new MyByteArray();
		byteBuf.write(arr);
		this.player.send(11051, byteBuf.getBuf());
	}
	
	public void  getRoomInfo(List<Object> list)
	{
		Map<Integer,Room>  map = Global.rManager.gamingRooms;
		Iterator<Room> it = map.values().iterator();
		while(it.hasNext())
		{
			count++;
			if(count < startRec)continue;
			if(count > startRec + PAGE_SIZE)break;
			Room r = it.next();
			//当前人数
			int nowPP = r.getPlayers().size();
			if(nowPP < 0)
			{
				nowPP = 0;
			}
			if(nowPP == 0)
			{
				Log.log("");
				Global.rManager.destroyRoom(r);
				continue;
			}
			//总的人数
			int totalPP = r.getPlayerLimit();
			List<Object> tmpList = new ArrayList<Object>();
			tmpList.add(r.getRoomID());
			tmpList.add(nowPP);
			tmpList.add(totalPP);
			tmpList.add(true);
			list.add(tmpList);
		}
	}


		



}

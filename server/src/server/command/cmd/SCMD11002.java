package server.command.cmd;


import org.jboss.logging.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import business.entity.MJ_User;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.MgsPlayer;
import server.mj.Room;

import common.DecodeCMD;
import common.MyArrays;

/**
 * 玩家信息
 * @author xue
 *
 */
public class SCMD11002 extends CMD{
	private static final Logger logger = Logger.getLogger(SCMD11002.class.getName());
	
	@Override
	public ChannelBuffer getBytes() {
		return null;
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		
		MgsPlayer player = (MgsPlayer) obj;
		MJ_User user = player.getBusiness().getPlayer();
		MyArray arr = MyArray.getMyArray();
		arr.push(player.getRoomId());
		arr.push(user.getName());
		arr.push(user.getLevel());
		arr.push(user.getImage());
		arr.push(player.getRoomId());
		arr.push(player.getBusiness().getPlayer().getGold());
		arr.push(player.getEquipSkill());
		System.out.println("player.getEquipSkill():"+player.getEquipSkill().size());
		MyByteArray byteArray  = new MyByteArray();
		byteArray.write(arr);
		return byteArray.getBuf();
	}
	
	public ChannelBuffer autoGetBytes(Object obj)
	{
		ChannelBuffer buf  = getBytes(obj);
		return buf;
	}
	@Override
	public void setBytes(ChannelBuffer buf) {
		
	}
	@Override
	public void setPlayer(MgsPlayer player) {
		
	}
	
}

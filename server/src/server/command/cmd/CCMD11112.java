package server.command.cmd;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jboss.netty.buffer.ChannelBuffer;

import common.Log;

import server.command.CMD;
import server.command.GlobalData;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.MgsPlayer;
import business.Business;
import business.entity.MJ_Role;
import business.entity.MJ_User;

/**
 * 心跳
 * @author xue
 */
public class CCMD11112 extends  CMD{
	

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
//		Log.info("receive ccmd11112:" + this.player.getName());
		this.player.setBeatDown(10);
		
	}

}

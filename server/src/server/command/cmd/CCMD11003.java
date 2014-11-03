package server.command.cmd;

import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;

import common.Log;

import business.Business;
import business.entity.MJ_User;

import server.command.CMD;
import server.mj.Global;
import server.mj.MgsPlayer;

/**
 *   请求所有手牌
 * @author xue
 *
 */
public class CCMD11003 extends  CMD{
	
	private static final Logger logger = Logger.getLogger(CCMD11003.class.getName());

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
		
		if(this.player.getRoom() == null)
		{
			return;
		}
		//获得生成手牌的规则
		this.player.getRoom().startFirstTimer(this.player);
		
	}

}

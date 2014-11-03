package server.command.cmd;

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;

import server.command.CMD;
import server.command.PatternPai;
import server.mj.MgsPlayer;

/**
 *  流局
 * @author xue
 */
public class CCMD11018 extends  CMD{
	
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
	}

}

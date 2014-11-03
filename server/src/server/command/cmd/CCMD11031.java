package server.command.cmd;

import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;

import common.Log;

import server.command.CMD;
import server.command.PatternPai;
import server.mj.MgsCache;
import server.mj.MgsPlayer;
import server.mj.Room;

/**
 *  修改气
 * @author xue
 */
public class CCMD11031 extends  CMD{
	
	private static final Logger logger = Logger.getLogger(CCMD11031.class.getName());

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
		this.setBytes(buf);
	}
	/**
	 * 广播气的修改
	 */
	public void auto_deal(MgsPlayer p,int xiugailiang)
	{
		SCMD11031  scmd = new SCMD11031();
		scmd.setUid(p.getRoomId());
		scmd.setXiugailinag(xiugailiang);
		scmd.setResult(p.getQi());
		ChannelBuffer  buf =  scmd.getBytes();
		p.getRoom().SendRoomBroadcast(11031, buf);
		logger.info("ccmd11031 auto_deal mod qi:" + xiugailiang + ":" + p.getQi());
	}
	public ChannelBuffer getBuf(MgsPlayer p,int xiugailiang)
	{
		SCMD11031  scmd = new SCMD11031();
		scmd.setUid(p.getRoomId());
		scmd.setXiugailinag(xiugailiang);
		scmd.setResult(p.getQi());
		ChannelBuffer  buf =  scmd.getBytes();
		return p.coderCMD(11031, buf);
	}
}

package server.command.cmd;

import org.jboss.netty.buffer.ChannelBuffer;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.command.PatternPai;
import server.mj.MgsCache;
import server.mj.MgsPlayer;
import server.mj.Room;

/**
 *  技能生效
 * @author xue
 */
public class CCMD11034 extends  CMD{
	
	private PatternPai pattern = new PatternPai();

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

	public void auto_deal(MgsPlayer p, int skillId)
	{
		MyArray arr = new MyArray();
		arr.push(p.getRoomId());
		arr.push(skillId);
		MyByteArray byteBuf = new MyByteArray();
		byteBuf.write(arr);
		p.getRoom().SendRoomBroadcast(11034, byteBuf.getBuf());
	}
}

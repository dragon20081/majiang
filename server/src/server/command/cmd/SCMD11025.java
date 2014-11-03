package server.command.cmd;



import org.jboss.logging.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.MgsPlayer;
import server.mj.Room;

/**
 *	比赛结束
 * @author xue
 */
public class SCMD11025 extends CMD{
	private static final Logger logger = Logger.getLogger(SCMD11025.class.getName());
	
	public int score = 0;
	public int rank  = 0;
	public int money = 0;
	@Override
	public ChannelBuffer getBytes() {
		return null;
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		MgsPlayer p  = (MgsPlayer) obj;
		//积分  排名  钱
		return null;
	}
	@Override
	public void setBytes(ChannelBuffer buf) {
	}
	@Override
	public void setPlayer(MgsPlayer player) {
	}
	public void auto_deal(MgsPlayer p)
	{
		MyArray arr = new MyArray();
		arr.push(p.getRoomId());
		arr.push(score);
		arr.push(rank);
		arr.push(money);
		MyByteArray byteArr = new MyByteArray();
		byteArr.write(arr);
		p.getRoom().SendRoomBroadcast(11025,byteArr.getBuf() );
	}
}

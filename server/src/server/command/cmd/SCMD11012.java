package server.command.cmd;



import org.jboss.logging.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import common.DecodeCMD;
import common.MyArrays;

import server.command.CMD;
import server.mj.MgsPlayer;
import server.mj.Room;


/**
 *   玩家掉线后重新获取所有的牌
 * @author xue
 *
 */
public class SCMD11012 extends CMD{
	private static final Logger logger = Logger.getLogger(SCMD11012.class.getName());
	@Override
	public ChannelBuffer getBytes() {
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		return buf;
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		
		super.setCmdPatterns(MyArrays.asList(DecodeCMD.BYTE));
		MgsPlayer p  = (MgsPlayer) obj;
		Room 	r  = p.getRoom();
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		//写入关于房间的信息
		//  1 全输  把数 积分 ....
		for(int i = 0 ; i < r.players.size();i++)
		{
			MgsPlayer tmpp  =r.players.get(i);
			ChannelBuffer tmpbuf = tmpp.getShouPaiBuf();
			buf.writeInt(tmpp.getBusiness().getPlayer().getUid());
			buf.writeBytes(tmpbuf);
		}
		return buf;
	}
	@Override
	public void setBytes(ChannelBuffer buf) {
		
	}
	@Override
	public void setPlayer(MgsPlayer player) {
		
	}
}

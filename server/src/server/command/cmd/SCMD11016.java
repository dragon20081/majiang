package server.command.cmd;



import org.jboss.logging.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import server.command.CMD;
import server.mj.MgsPlayer;
import server.mj.Room;

/**
 *  准备
 * @author xue
 */
public class SCMD11016 extends CMD{
	private static final Logger logger = Logger.getLogger(SCMD11016.class.getName());
	
	public static  int   PASSABLE = 1;
	public static  int   PASS_NOT = 2;
	
	private int pass  =  0;
	private int paiId =  0;
	@Override
	public ChannelBuffer getBytes() {
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		return buf;
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		
		MgsPlayer p  = (MgsPlayer) obj;
		Room r = p.getRoom();
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		buf.writeInt(p.getBusiness().getPlayer().getUid());
		buf.writeByte(pass);
		buf.writeByte(this.paiId);
		return buf;
	}
	@Override
	public void setBytes(ChannelBuffer buf) {
		
	}
	@Override
	public void setPlayer(MgsPlayer player) {
		
	}
	public int getPass() {
		return pass;
	}
	public void setPass(int pass) {
		this.pass = pass;
	}
	public int getPaiId() {
		return paiId;
	}
	public void setPaiId(int paiId) {
		this.paiId = paiId;
	}
}

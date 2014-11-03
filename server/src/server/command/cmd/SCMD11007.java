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
 *  碰
 * @author xue
 *
 */
public class SCMD11007 extends CMD{
	private static final Logger logger = Logger.getLogger(SCMD11007.class.getName());
	
	public static  int   PASSABLE = 1;
	public static  int   PASS_NOT = 2;
	
	private int pass  =  0;
	private int paiId =  0;
	private int whoDaId   = 0;
	@Override
	public ChannelBuffer getBytes() {
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		return buf;
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		
		super.setCmdPatterns(MyArrays.asList(DecodeCMD.BYTE,DecodeCMD.BYTE));
		MgsPlayer p  = (MgsPlayer) obj;
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		this.writePattern(buf);
		buf.writeByte(p.getRoomId());
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
	public int getWhoDaId() {
		return whoDaId;
	}
	public void setWhoDaId(int whoDaId) {
		this.whoDaId = whoDaId;
	}
	
}

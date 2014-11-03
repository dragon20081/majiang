package server.command.cmd;



import org.jboss.logging.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.MgsPlayer;

/**
 *  听牌
 * @author xue
 */
public class SCMD11015 extends CMD{
	private static final Logger logger = Logger.getLogger(SCMD11015.class.getName());
	
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
		MyArray arr  = new MyArray();
		arr.push(p.getRoomId());
		MyByteArray byteArray  =  new MyByteArray();
		byteArray.write(arr);
		return byteArray.getBuf();
		
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

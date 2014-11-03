package server.command.cmd;



import org.jboss.logging.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;


import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.MgsPlayer;

/**
 *  发下一张牌
 * @author xue
 *
 */
public class SCMD11006 extends CMD{
	private static final Logger logger = Logger.getLogger(SCMD11006.class.getName());
	private int nextId;
	@Override
	public ChannelBuffer getBytes() {
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		return buf;
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		MgsPlayer p   = (MgsPlayer) obj;
		MyArray arr = new MyArray();
		arr.push(p.getRoomId());
		arr.push(nextId);
		MyByteArray byteArray = new MyByteArray();
		byteArray.write(arr);
		return byteArray.getBuf();
	}
	@Override
	public void setBytes(ChannelBuffer buf) {
		
	}
	@Override
	public void setPlayer(MgsPlayer player) {
		
	}
	public int getNextId() {
		return nextId;
	}
	public void setNextId(int nextId) {
		this.nextId = nextId;
	}
	
}

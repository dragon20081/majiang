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
 *	离开房间
 * @author xue
 */
public class SCMD11024 extends CMD{
	private static final Logger logger = Logger.getLogger(SCMD11024.class.getName());
	
	@Override
	public ChannelBuffer getBytes() {
		return null;
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		MgsPlayer p  = (MgsPlayer) obj;
		MyArray arr = new MyArray();
		arr.push(p.getRoomId());
		MyByteArray byteArr =  new MyByteArray();
		byteArr.write(arr);
		return byteArr.getBuf();
	}
	@Override
	public void setBytes(ChannelBuffer buf) {
	}
	@Override
	public void setPlayer(MgsPlayer player) {
	}
}

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
 *  取消匹配
 * @author xue
 */
public class SCMD11021 extends CMD{
	private static final Logger logger = Logger.getLogger(SCMD11021.class.getName());
	
	
	@Override
	public ChannelBuffer getBytes() {
	
		MyArray  arr  = new MyArray();
		MyByteArray byteArray = new MyByteArray();
		byteArray.write(arr);
		return byteArray.getBuf();		
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		
		MgsPlayer p  = (MgsPlayer) obj;
		MyArray arr = new MyArray();
		arr.push(p.getRoomId());
		MyByteArray bytearr = new MyByteArray();
		bytearr.write(arr);
		return bytearr.getBuf();
	}		
	@Override
	public void setBytes(ChannelBuffer buf) {
		
	}
	@Override
	public void setPlayer(MgsPlayer player) {
		
	}
}

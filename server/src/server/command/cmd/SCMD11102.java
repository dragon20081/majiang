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
 *  回复修改剧情进度
 * @author xue
 */
public class SCMD11102 extends CMD{
	private static final Logger logger = Logger.getLogger(SCMD11102.class.getName());
	public static final boolean SUCCESS = true;
	public static final boolean FAIL 	= false;
	public boolean result = false;
	public int id;
	
	@Override
	public ChannelBuffer getBytes() {
		MyArray arr = MyArray.getMyArray();
		arr.push(id);
		arr.push(result);
		MyByteArray byteArray  = new MyByteArray();
		byteArray.write(arr);
		return byteArray.getBuf();
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		
		return null;
	}		
	@Override
	public void setBytes(ChannelBuffer buf) {
		
	}
	@Override
	public void setPlayer(MgsPlayer player) {
		
	}
}

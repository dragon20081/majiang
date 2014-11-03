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
 *  房间信息
 * @author xue
 */
public class SCMD11022 extends CMD{
	private static final Logger logger = Logger.getLogger(SCMD11022.class.getName());
	
	private  int num;
	private  int skill;
	private  int chips;
	private  int quan;
	
	public   Room room;
	@Override
	public ChannelBuffer getBytes() {
	
		this.skill  =  room.getSkillLimit();
		this.quan   =  room.getQuanLimit();
		 skill  = 1;
		MyArray  arr  = new MyArray();
		arr.push(this.skill);
		arr.push(this.quan);
		MyByteArray byteArray = new MyByteArray();
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

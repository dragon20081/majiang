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
 *  4 人场   增加多少待定            1V1  每盘赢了+1;
 *  修改 role经验值
 * @author xue
 */
public class SCMD11103 extends CMD{
	private static final Logger logger = Logger.getLogger(SCMD11103.class.getName());
	
	public int roleId =  0;
	public int modExp = 0;
	
	@Override
	public ChannelBuffer getBytes() {
		return null;
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		MgsPlayer p   = (MgsPlayer) obj;
		MyArray arr = new MyArray();
		arr.push(roleId);
		arr.push(modExp);
		MyByteArray byteArr = new MyByteArray();
		byteArr.write(arr);
		return byteArr.getBuf();
	}		
	public void auto_deal(MgsPlayer p,int  roleid,int modExp)
	{
		this.roleId  =  roleid;
		this.modExp  =  modExp;
		p.send(11103, this.getBytes(p));
	}
	@Override
	public void setBytes(ChannelBuffer buf) {
		
	}
	@Override
	public void setPlayer(MgsPlayer player) {
		
	}
}

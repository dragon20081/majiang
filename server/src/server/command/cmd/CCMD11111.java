package server.command.cmd;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jboss.netty.buffer.ChannelBuffer;

import server.command.CMD;
import server.command.GlobalData;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.MgsPlayer;
import business.Business;
import business.entity.MJ_Role;
import business.entity.MJ_User;

/**
 *  通知天气消息
 * @author xue
 */
public class CCMD11111 extends  CMD{
	

	public void setPlayer(MgsPlayer player) {
		this.player = player;
	}
	public ChannelBuffer getBytes() {
		return null;
	}
	public ChannelBuffer getBytes(Object obj) {
		return null;
	}
	public void setBytes(ChannelBuffer buf) {
	}
	public void auto_deal(MgsPlayer p,String msg)
	{
		MyArray arr = MyArray.getMyArray(msg);
		MyByteArray byteArray  = new MyByteArray();
		byteArray.write(arr);
		p.send(11111, byteArray.getBuf());
	}
	public ChannelBuffer getBuf(MgsPlayer p,String msg)
	{
		MyArray arr = MyArray.getMyArray(msg);
		MyByteArray byteArray  = new MyByteArray();
		byteArray.write(arr);
		return p.coderCMD(11111, byteArray.getBuf());
	}
}

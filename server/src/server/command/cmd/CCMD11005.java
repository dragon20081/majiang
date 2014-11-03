package server.command.cmd;

import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;

import server.command.CMD;
import server.command.SCMD3;
import server.mj.MgsCache;
import server.mj.MgsPlayer;
import server.mj.Room;

/**
 *   测试
 * @author xue
 *
 */
public class CCMD11005 extends  CMD{
	
	private int touchPaiId =  0;

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
		super.setBytes(buf);
		List<Object> list =  this.getValues();
		for(int i  = 0; i < list.size();i++)
		{
			System.out.println(list.get(i));
		}
	}
	public void auto_deal(int paiId)
	{
	}


}

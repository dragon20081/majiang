package server.command.cmd;

import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.MgsPlayer;

public class CCMD11042 extends CMD{

	
	@Override
	public void setPlayer(MgsPlayer player) {
		this.player  = player;
	}
	@Override
	public ChannelBuffer getBytes() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		return null;
	}
	@Override
	public void setBytes(ChannelBuffer buf) {
		super.setBytes(buf);
		
		String nick = this.getStrValue(0);
		//查询玩家战绩
		List<Integer> list = player.getBusiness().queryZhanJi(nick);
		MyArray arr = new MyArray();
		if(list != null)
		{
			arr.push(nick);
			for(int i = 0; i < list.size(); i++)
			{
				int value = list.get(i);
				arr.push(value);
			}
			MyByteArray bytebuf = new MyByteArray();
			bytebuf.write(arr);
			this.player.send(11042, bytebuf.getBuf());
		}
	}
	

}

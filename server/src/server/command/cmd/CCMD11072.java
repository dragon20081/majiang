package server.command.cmd;

import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.MgsPlayer;

import common.Log;


/**
 * 某个任务完成通知
 * @author xue
 */
public class CCMD11072 extends CMD{

	@Override
	public void setPlayer(MgsPlayer player) {
		this.player = player;
	}
	@Override
	public ChannelBuffer getBytes() {
		return null;
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		return null;
	}
	@Override
	public void setBytes(ChannelBuffer buf) {
		super.setBytes(buf);
	}
	/**
	 * 通知前台某个任务已经完成
	 */
	public void auto_deal(MgsPlayer p,List<Object> value)
	{
		MyArray arr = new MyArray();
		arr.push(value.get(0));
		arr.push(value.get(1));
		String[] pro = ((String)value.get(2)).split(":");
		arr.push(Integer.parseInt(pro[0]));
		arr.push(Integer.parseInt(pro[1]));
		MyByteArray bytebuf = new MyByteArray();
		bytebuf.write(arr);
		p.send(11072, bytebuf.getBuf());
	}

}

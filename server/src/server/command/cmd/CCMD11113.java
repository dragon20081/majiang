package server.command.cmd;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import server.command.CMD;
import server.command.GlobalData;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.Global;
import server.mj.MgsCache;
import server.mj.MgsPlayer;
import business.Business;
import business.conut.Sts_Notice;
import business.entity.MJ_Role;
import business.entity.MJ_User;

/**
 *  公告
 * @author xue
 */
public class CCMD11113 extends  CMD{
	
	public static final int MOD_NOTICE = 111;
	public static final int GET_NITICE = 112;
	
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
		auto_deal_one(this.player);
	}
	public void auto_deal_one(MgsPlayer p)
	{
		List<Sts_Notice> notices = MgsCache.getInstance().notices;
		if(notices.size() == 0) return;
		String notice = notices.get(0).getContentStr();
		MyArray arr = new MyArray();
		arr.push(notice);
		MyByteArray buf = new MyByteArray();
		buf.write(arr);
		p.send(11113, buf.getBuf());
	}
	public void auto_deal_all(String notice)
	{
		MyArray arr = new MyArray();
		arr.push(notice);
		MyByteArray buf = new MyByteArray();
		buf.write(arr);
		ChannelBuffer msg = coderCMD(11113, buf.getBuf());
		Global.worldChannelGroup.write(msg);
	}
	public synchronized ChannelBuffer coderCMD(Integer cmd, ChannelBuffer buf){
		ChannelBuffer data = ChannelBuffers.dynamicBuffer();
		data.writeInt(buf.readableBytes()+2);
		data.writeShort(cmd);
		data.writeBytes(buf);
		return data;
	}
}

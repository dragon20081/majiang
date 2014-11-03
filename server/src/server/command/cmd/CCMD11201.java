package server.command.cmd;
/**
 * 获取资源ID
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;

import common.Log;

import antlr.ByteBuffer;
import business.entity.MJ_Resource;
import business.entity.MJ_TmpResource;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.Global;
import server.mj.MgsPlayer;

public class CCMD11201 extends CMD{

	public boolean test = false;
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
		
		if(test)
		{
			sendForceRes();
			return;
		}
		int type = this.getIntVaule(0);
		if(type == 0)
			sendForceRes();
		else
			sendTmpRes();

	}
	/**
	 * 强制更新资源
	 */
	public void sendForceRes()
	{
		MyArray arr = new MyArray();
		List<MJ_Resource> r = Global.resourceList;
		for(int i = 0 ; i < r.size(); i++)
		{
			MJ_Resource tmpr = r.get(i);
			arr.push(tmpr.getRid());
			arr.push(tmpr.getVersion());
		}
		MyByteArray bytebuf = new MyByteArray();
		bytebuf.write(arr);
		this.player.send(11201, bytebuf.getBuf());
	}
	/**
	 * 临时资源，可选
	 */
	public void sendTmpRes()
	{
		MyArray arr = new MyArray();
		List<MJ_TmpResource> r = Global.tmpresourceList;
		for(int i = 0 ; i < r.size(); i++)
		{
			MJ_TmpResource tmpr = r.get(i);
			if(!"资源包".equals(tmpr.getName()))continue;
			arr.push(tmpr.getRid());
			arr.push(tmpr.getVersion());
		}
		MyByteArray bytebuf = new MyByteArray();
		bytebuf.write(arr);
		this.player.send(11201, bytebuf.getBuf());
	}

	
}

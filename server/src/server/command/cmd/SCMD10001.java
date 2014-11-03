package server.command.cmd;


import java.util.Iterator;
import java.util.Map;

import org.jboss.logging.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import server.command.CMD;
import server.mj.MgsPlayer;

public class SCMD10001 extends CMD{
	private static final Logger logger = Logger.getLogger(SCMD10001.class.getName());
	
	
	private int seed  = 0;
	private int flag  = 0;
	
	private Map<String,String> userInfo_360;
	
	public void setPlayer(MgsPlayer player) {
		this.player  =   player;
	}
	@Override
	public ChannelBuffer getBytes() {
		return null;
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		logger.info("SCMD01001");
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		buf.writeByte(this.flag);
		buf.writeInt(this.seed);
		if(this.flag == 4)
		{
			if("IOS".equals(this.player.platform))
			{
				this.writeUTF("https://itunes.apple.com/us/app/tian-cai-ma-jiang/id849728892", buf);
			}else
			{
				this.writeUTF("http://115.29.179.110/majiang/majiang.apk", buf);
			}
		}else if(this.flag == 2)
		{
			this.writeUTF("登陆失败!", buf);
		}
		else if(this.flag == 7)
		{
			this.writeUTF("密码错误,请联系管理员!", buf);
		}
		else if(this.flag == 9)
		{
			this.writeUTF("用户名不存在!", buf);
		}
		else if(this.flag ==1 && "360".equals(this.player.platform) &&this.player.getUserinfo_360() != null )
		{
		  Map<String,String> userInfo = this.player.getUserinfo_360();
		  Iterator<String> it = userInfo.keySet().iterator();
		  while(it.hasNext())
		  {
			  String key = it.next();
			  String value = userInfo.get(key);
			  this.writeUTF(key, buf);
			  this.writeUTF(value, buf);
		  }
		}
		
		return buf;
	}
	@Override
	public void setBytes(ChannelBuffer buf) {
		
	}
	public int getSeed() {
		return seed;
	}
	public void setSeed(int seed) {
		this.seed = seed;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
}

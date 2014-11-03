package server.command.cmd;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.json.JSONObject;

import org.jboss.netty.buffer.ChannelBuffer;

import common.Log;

import business.WeiqiDao;
import business.conut.Sts_ApplePay;
import business.entity.MJ_PayInfo;
import business.entity.MJ_User;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.Global;
import server.mj.MgsPlayer;
import server.mj.ServerTimer;

/**
 * 用户操作
 * @author xue
 *
 */
public class CCMD11044 extends CMD{

	
	public static final int ENTER = 0;
	public static final int LEAVE = 1;
	
	
	
	public static final int SHANGCHENG = 10;
	
	private static final Logger log = Logger.getLogger("CCMD11044");
	
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
		
		int caozuo  = this.getIntVaule(0);
		int yemian  = this.getIntVaule(1);
		
		Log.info("CCMD11044:"+ caozuo+" " + yemian );
		if(caozuo == ENTER)
		{
			switch(yemian)
			{
				case  SHANGCHENG:   
					
					Log.info("CCMD11044:  stopHeartBeat"+ caozuo+" " + yemian );
					this.player.stopHeartBeat();
					break;
			}
			
		}else if(caozuo == LEAVE)
		{
			switch(yemian)
			{
				case  SHANGCHENG: 
					Log.info("CCMD11044:  startHeartBeat"+ caozuo+" " + yemian );
					this.player.startHeartBeat();
					break;
			}
		}
	}
}

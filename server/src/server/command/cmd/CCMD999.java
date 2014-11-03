package server.command.cmd;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;

import business.Business;
import business.entity.MJ_User;

import server.command.CMD;
import server.command.Login360;
import server.command.HTTPSCommunication;
import server.mj.MgsCache;
import server.mj.MgsPlayer;
import server.mj.MgsRoom;

/**
 * 
 *360登陆
 * @author xue
 *
 */
public class CCMD999 extends  CMD{
	
	private Business  business ;

	public void setPlayer(MgsPlayer player) {
		this.player = player;
		this.business  = this.player.getBusiness();
	}
	public ChannelBuffer getBytes() {
		return null;
	}

	public ChannelBuffer getBytes(Object obj) {
		return null;
	}
	public void setBytes(ChannelBuffer buf) {
		String code =  buf.readBytes(buf.readShort()).toString(Charset.forName("UTF-8"));
		
		
		Login360 login360 = new Login360();
		login360.login(code,this.player);
		
		
//		Map<String,String> map = new LinkedHashMap<String, String>();
//		map.put("grant_type","authorization_code");
//		map.put("code", code);
//		map.put("client_id", "9b1fa1a96ed0d726d2bceb6df8a02d8c");
//		map.put("client_secret", "1106640b6df74133173be7328bae64c2");
//		map.put("redirect_uri", "oob");
//		
//		URLCommunication comm = new URLCommunication();
//		
//		try {
//			comm.send("https://openapi.360.cn/oauth2/access_token", "GET", map, null);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
	}
}

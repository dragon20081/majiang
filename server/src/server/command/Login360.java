package server.command;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

import server.mj.MgsPlayer;

public class Login360 {

	private HTTPSCommunication comm = new HTTPSCommunication();
	private static final Logger logger = Logger.getLogger(Login360.class.getName());

	public boolean login(String authorization,MgsPlayer p)
	{
		
		try
		{
			Map<String,String> result1 = getAccessToken(authorization);
			if(result1 == null) return false;
			Map<String,String> result2 = getUserInfo(result1.get("access_token"));
			if(result2 == null) return false;
			p.setTokenMap(result1);
			p.setUserinfo_360(result2);
		}catch(IOException e)
		{
			logger.info(e.getMessage());
			return false;
		}
		return true;
	}
	public Map<String,String>  getAccessToken(String authorization_code) throws IOException
	{
		Map<String,String> map = new LinkedHashMap<String, String>();
		map.put("grant_type","authorization_code");
		map.put("code", authorization_code);
		map.put("client_id", "9b1fa1a96ed0d726d2bceb6df8a02d8c");
		map.put("client_secret", "1106640b6df74133173be7328bae64c2");
		map.put("redirect_uri", "oob");
		
		HTTPSCommunication comm = new HTTPSCommunication();
		Map<String,String> result = null;
		result = comm.send("https://openapi.360.cn/oauth2/access_token", "GET", map, null);
		return result;
	}
	public Map<String,String> getUserInfo(String token)throws IOException
	{
		
		Map<String,String> map = new LinkedHashMap<String, String>();
		map.put("access_token",token);
		
		HTTPSCommunication comm = new HTTPSCommunication();
		Map<String,String>  result = null;
		result = comm.send("https://openapi.360.cn/user/me", "GET", map, null);
		return result;
	}
}

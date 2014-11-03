package server.command;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.http.Html_360Notice;

import business.conut.Sts_360Order;
import business.junitTest.MD5Util;

public class PayVerify_360 {

	private static final Logger logger = Logger.getLogger(PayVerify_360.class.getName());	

	public boolean verity(Map<String,String> argsMap,Sts_360Order order)
	{
		HTTPCommunication comm = new HTTPCommunication();
		Map<String,String> result = null;
		try {
			
			Map<String,String> map = new LinkedHashMap<String, String>();
			map.put("app_key",argsMap.get("app_key"));
			map.put("product_id",argsMap.get("product_id"));
			map.put("amount",argsMap.get("amount"));
			map.put("app_uid",argsMap.get("app_uid"));
			map.put("order_id",argsMap.get("order_id"));
			map.put("app_order_id",argsMap.get("app_order_id"));
			map.put("sign_type",argsMap.get("sign_type"));
			map.put("sign_return",argsMap.get("sign_return"));
			//需要验证签名
			String md5Str = getSignBeforeStr(map);
			map.put("sign",md5Str);
			result = comm.send("http://msdk.mobilem.360.cn/pay/order_verify.json", "GET", map, null);
			
			String msg = result.get("ret");
			order.setVerify_360(msg);
			if(msg.equals("verified"))
			{
				return true;
			}else
			{
				logger.log(Level.WARNING,"payVerify_360:"+ msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	public String getSignBeforeStr(Map<String,String> map)
	{
		String[] arr = new String[]{"amount",  "app_key", "app_order_id",  "app_uid" , "order_id", "product_id",    "sign_return",  "sign_type"};
		String appSecert =  GlobalData.AppSecret; 
		
		String  strBuf = "";
		for(int i = 0; i < arr.length; i++)
		{
			String value = map.get(arr[i]);
			strBuf +=value;
			strBuf +="#";
		}
		strBuf +=appSecert;
		String before = strBuf;
		logger.info("before md5:"+ before);
		String after = MD5Util.string2MD5(before);
		
		
		return after;
	}
	
}

package server.http;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.json.JSONObject;
import server.command.cmd.CCMD11101;
import server.mj.Global;
import server.mj.ServerTimer;
import business.CountDao;
import business.WeiqiDao;
import business.conut.Sts_ApplePay;
import business.entity.MJ_PayInfo;
import business.entity.MJ_User;

/**
 *   验证  1. original_transaction_id 是否已经存在这条记录， 每次充值必须唯一 ，  保存所有的记录， 不管成功还是失败
 *   验证  2. product_id ,查看id 是否合法
 *   验证  3. status == 0   0 成功
 * @author xue
 *
 */
public class Html_weiqipay implements IHtml {
	private static final Logger log = Logger.getLogger("Html_weiqipay");

	private WeiqiDao payDao = new WeiqiDao();
	@Override
	public String getHtml(String content) {
		if(content == null || content.isEmpty()) return "无内容";
		String contents[] = content.split("&");
		int device = 0;
		int lesson = 1;
		String identifier = "";
		String productIdentifier = "";
		String date = "";
		String receipt = "";
		for(int i=0;i<contents.length;i++)
		{
			if(contents[i].startsWith("device="))
			{
				device = Integer.parseInt(contents[i].replaceAll("device=", ""));
			}else if(contents[i].startsWith("lesson="))
			{
				lesson = Integer.parseInt(contents[i].replaceAll("lesson=", ""));
			}else if(contents[i].startsWith("identifier="))
			{
				identifier = contents[i].replaceAll("identifier=", "");
			}else if(contents[i].startsWith("productIdentifier="))
			{
				productIdentifier = contents[i].replaceAll("productIdentifier=", "");
			}else if(contents[i].startsWith("date="))
			{
				date = contents[i].replaceAll("date=", "");
			}else if(contents[i].startsWith("receipt="))
			{
				receipt = contents[i].replaceAll("receipt=", "");
			}
		}
		log.info("device:"+device);
		log.info("lesson:"+lesson);
		log.info("identifier:"+identifier);
		log.info("productIdentifier:"+productIdentifier);
		log.info("date:"+date);
		log.info("receipt:"+receipt);
		boolean uniqueTransid = false;
		Sts_ApplePay pay = payDao.findPayByIdentifier(identifier,productIdentifier,date);
		if(pay == null)
		{
			uniqueTransid = true;
			pay = new Sts_ApplePay();
			pay.setIdentifier(identifier);
			pay.setProductIdentifier(productIdentifier);
			pay.setDate0(date);
//			pay.setFirstTime(ServerTimer.getNowString());
			payDao.saveObject(pay);
		}
		
		if(pay.getReceipt().isEmpty() && !receipt.isEmpty())
		{
			pay.setReceipt(receipt);
			pay.setResult(payDao.checkReceipt(receipt));
			JSONObject obj = JSONObject.fromObject(pay.getResult());
			JSONObject obj2 = obj.getJSONObject("receipt");
			log.info(obj2.toString());
			if(!obj2.isEmpty() && obj2.get("original_transaction_id") != null)
			{
				String oti = obj2.getString("original_transaction_id");
				pay.setOriginal(oti);
			}
		}
//		pay.setUsed(pay.getUsed() +1);
//		pay.setLastTime(ServerTimer.getNowString());
		payDao.saveObject(pay);
		
		int x = 1;
		int y = 0;
		int z = 1;
		JSONObject obj = null;
		if(!pay.getResult().isEmpty())
		{
			obj = JSONObject.fromObject(pay.getResult());
		}
		if(obj != null && obj.get("status") != null && obj.getInt("status") == 0)
		{
			JSONObject obj2 = obj.getJSONObject("receipt");
			log.info(obj2.toString());
			if(!obj2.isEmpty() && obj2.get("product_id") != null)
			{
				MJ_PayInfo payinfo = this.checkProduct_id(obj2.getString("product_id"));
				if(payinfo != null)
				{
						//
				}
			}
		}
		String html = "device="+1+"&result="+x+","+y+","+z;
		log.info("html:"+html);
		return html;
	}
	
	public MJ_PayInfo checkProduct_id(String pid)
	{
		Map<Integer,MJ_PayInfo> payItems = Global.payItems;
		Iterator<MJ_PayInfo> it = payItems.values().iterator();
		while(it.hasNext())
		{
			MJ_PayInfo payinfo = it.next();
			if(payinfo.getPayStr_apple().equals(pid))
				return payinfo;
		}
		return null;
	}

}

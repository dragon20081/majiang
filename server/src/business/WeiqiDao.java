package business;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Logger;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import common.Log;

import business.conut.Sts_ApplePay;

import server.mj.Global;
import server.mj.ServerTimer;

public class WeiqiDao {

	
//	public  String checkReceipt(String receipt)
//	{
//		JSONObject obj = new JSONObject();
//		try{
//			JSONObject receiptJSON = JSONObject.fromObject(receipt);
//			String urlStr = "https://sandbox.itunes.apple.com/verifyReceipt";
//			if(!receiptJSON.getString("environment").equals("Sandbox"))
//			{
//				urlStr = "https://buy.itunes.apple.com/verifyReceipt";
//			}
//            URL url = new URL(urlStr);  
//            URLConnection urlConnection = url.openConnection();
//            urlConnection.setDoOutput(true);  
//            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());  
//            String encodedReceipt = Global.BASE64Encod(receipt);
//            out.write("{\"receipt-data\" : \""+encodedReceipt+"\"}");  
//            out.flush();
//            out.close();
//            
//            // 从服务器读取响应  
//            InputStream inputStream = urlConnection.getInputStream();  
//            String body = IOUtils.toString(inputStream,Charset.forName("gbk"));
//            obj = JSONObject.fromObject(body);
//        }catch(Exception e){
//        	e.printStackTrace();
//        }
//		return obj.toString();
//	}

    public  String checkReceipt(String receipt)
	{
		JSONObject obj = new JSONObject();
		try{
			JSONObject receiptJSON = JSONObject.fromObject(receipt);
			String urlStr = "https://buy.itunes.apple.com/verifyReceipt";
			if(receiptJSON.get("environment") != null && receiptJSON.getString("environment").equalsIgnoreCase("Sandbox"))
			{
				urlStr = "https://sandbox.itunes.apple.com/verifyReceipt";
			}
            URL url = new URL(urlStr);  
            URLConnection urlConnection = url.openConnection();
            urlConnection.setDoOutput(true);  
            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());  
            String encodedReceipt = Global.BASE64Encod(receipt);
            out.write("{\"receipt-data\" : \""+encodedReceipt+"\"}");  
            out.flush();
            out.close();
            
            // 从服务器读取响应  
            InputStream inputStream = urlConnection.getInputStream();  
            String body = IOUtils.toString(inputStream,Charset.forName("gbk"));
            obj = JSONObject.fromObject(body);
            Log.error(obj.toString());
        }catch(Exception e){
            Log.error("Exception:"+e.getMessage());
        }
		return obj.toString();
	}
	
	
	@SuppressWarnings("unchecked")
	public  int getOriginalSize(String orig)
	{
		Session ss = HSF_Play.getSession();
		Criteria ct = ss.createCriteria(Sts_ApplePay.class).add(Restrictions.eq("original", orig));
		List<Sts_ApplePay> list = ct.list();
		ss.close();
		return list.size();
	}
	@SuppressWarnings("unchecked")
	public  Sts_ApplePay findPayByIdentifier(String identifier,String productIdentifier,String date)
	{
		Sts_ApplePay pay = null;
		Session ss = HSF_Play.getSession();
		Criteria ct = ss.createCriteria(Sts_ApplePay.class).add(Restrictions.eq("original", identifier));
		ct.add(Restrictions.eq("productIdentifier", productIdentifier));
		List<Sts_ApplePay> list = ct.list();
		ss.close();
		if(list.size() >0)
		{
			pay = list.get(0);
		}
//		else
//		{
//			pay = new Sts_ApplePay();
//			pay.setIdentifier(identifier);
//			pay.setProductIdentifier(productIdentifier);
//			pay.setDate0(date);
//			pay.setFirstTime(ServerTimer.getNowString());
//			saveObject(pay);
//		}
		return pay;
	}
	
	
	public void saveObject(Object obj)
	{
	    Session ss = HSF_Play.getSession();
		 Transaction ts=ss.beginTransaction();
	    try {
			ss.saveOrUpdate(obj);
			ts.commit();
		} catch (Exception e) {
			e.getStackTrace();
			ts.rollback();
		}
		ss.close();
	}
	
	
}

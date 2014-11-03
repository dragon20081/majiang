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
 * 苹果充值
 * @author xue
 *
 */
public class CCMD11043 extends CMD{

	private static final Logger log = Logger.getLogger("CCMD11043");
	private WeiqiDao payDao = new WeiqiDao();
	
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
		try {
			getHtml();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getHtml() {
		Log.log("CCMD11043:");
		
		if(this.getValues().size() == 0) return ;
		String identifier = "";
		String productIdentifier = "";
		String date = "";
		String receipt = "";
		
		receipt = this.getStrValue(0);
		identifier = this.getStrValue(1);
		productIdentifier = this.getStrValue(2);
		
		boolean uniqueTransid = false;
		Sts_ApplePay pay = payDao.findPayByIdentifier(identifier,productIdentifier,date);
		if(pay != null)
		{
			pay.setUsed(pay.getUsed() + 1);
			payDao.saveObject(pay);
			
			sendBuyResult(false);
			return;
		}
		
		if(pay == null)
		{
			uniqueTransid = true;
		}
		pay = new Sts_ApplePay();
		pay.setIdentifier(identifier);
		pay.setProductIdentifier(productIdentifier);
		pay.setDate0(date);
		pay.setUsed(1);
		pay.setPayTime(ServerTimer.getNowString());
		payDao.saveObject(pay);
		
		if(pay.getReceipt().isEmpty() && !receipt.isEmpty())
		{
			pay.setReceipt(receipt);
			String result = payDao.checkReceipt(receipt);
			pay.setResult(result);
			JSONObject obj = JSONObject.fromObject(pay.getResult());
			JSONObject obj2 = obj.getJSONObject("receipt");
			if(obj2!= null && !obj2.isEmpty() && obj2.get("original_transaction_id") != null)
			{
				String oti = obj2.getString("original_transaction_id");
				pay.setOriginal(oti);
			}
		}
		payDao.saveObject(pay);
		
		int x = 1;
		int y = 0;
		int z = 1;
		JSONObject obj = null;
		System.out.println(8);
		if(!pay.getResult().isEmpty())
		{
			obj = JSONObject.fromObject(pay.getResult());
		}
		if(obj != null && obj.get("status") != null && obj.getInt("status") == 0 && uniqueTransid)
		{
			JSONObject obj2 = obj.getJSONObject("receipt");
			log.info(obj2.toString());
			if(!obj2.isEmpty() && obj2.get("product_id") != null)
			{
				MJ_PayInfo payinfo = this.checkProduct_id(obj2.getString("product_id"));
				if(payinfo != null)
				{
					System.out.println(12+"充值成功");
					payRMB(payinfo);
					
				}
			}
		}else
		{
			System.out.println(14);
			sendBuyResult(false);
		}
		System.out.println(15);
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
	/**
	 * 人民币充值
	 */
	public void payRMB(MJ_PayInfo payItem)
	{
		CCMD11041 cmd041 = new CCMD11041();
		cmd041.setPlayer(player);
		cmd041.payItem = payItem;
		
		MJ_User user = this.player.getBusiness().getPlayer();
		int beforeGold = user.getGold();
		int beforeDia = user.getDianQuan();
		int modGold = 0;
		int modDia = payItem.getDia();
		
		int pay_dia_before = user.getDianQuan();
		cmd041.pay_dia_before = pay_dia_before;
		user.setDianQuan(user.getDianQuan() + payItem.getDia());
		this.player.getBusiness().saveDataObject(user);
		CCMD11101 cmd101 = new CCMD11101();
		cmd101.auto_deal(player,6);
		this.sendBuyResult(true); //购买结果
		cmd041.savePayCount();
		
		this.player.saveUserChargeRec(11141+"",beforeGold , modGold, user.getGold(), beforeDia, modDia, user.getDianQuan(), "充值钻石:"+payItem.getDia()+"");
		//充值统计
	}
	public void sendBuyResult(boolean result)
	{
		System.out.println("sendBuyResult:" + result);
		MyArray arr = new MyArray();
		arr.push(result);
		MyByteArray buf = new MyByteArray();
		buf.write(arr);
		this.player.send(11043,buf.getBuf());
	}
}

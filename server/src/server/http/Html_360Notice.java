package server.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.command.MyArray;
import server.command.MyByteArray;
import server.command.PayVerify_360;
import server.command.cmd.CCMD11041;
import server.command.cmd.CCMD11101;
import server.command.cmd.CCMD11102;
import server.command.cmd.CCMD11111;
import server.command.cmd.CCMD11301;
import server.mj.Global;
import server.mj.MgsCache;
import server.mj.MgsPlayer;

import business.Business;
import business.CountDao;
import business.UserDao;
import business.conut.Sts_360Order;
import business.entity.MJ_PayInfo;
import business.entity.MJ_User;

/**
 * 
 * 360支付成功通知接口
 * @author xue
 * 1.保存通知数据
 * 2.向服务器发送验证请求
 * 3.验证成功后修改充值金额，保存统计数据
 * 4.向前台发送通知成功消息
 * 5.发送前台验证，等待验证返回
 */
public class Html_360Notice implements IHtml{

	private static final Logger logger = Logger.getLogger(Html_360Notice.class.getName());	
	
	private boolean test = true;
	@Override
	public String getHtml(String content) {
		
		logger.info("Html_360Notice start");
		int i = 0;
		String contents[] = content.split("&");
		Map<String,String> map = new HashMap<String, String>();
		for(i = 0; i < contents.length;i++)
		{
			String[] tmp = contents[i].split("=");
			if(tmp.length < 2)continue;
			if(tmp[0].length() == 0) continue;
			map.put(tmp[0], tmp[1]);
		}
		
		//根据app_order_id查询出order
		do
		{
			try {
				CountDao cdao = new CountDao();
				Sts_360Order order = cdao.find360OrderById(map.get("app_order_id"));
				if(order == null)
				{
					logger.log(Level.WARNING,"360Notice: order == NULL, app_order_id:"+map.get("app_order_id"));
					break;
				}
				
				//已经收到过通知
				if(order.getOrder_id() == Long.parseLong(map.get("order_id")))
				{
					if("verified".equals(order.getVerify_360()))
					{
						logger.log(Level.INFO,"360 pay verified ,order_id:"+map.get("order_id")); 
						break;
					}
					//验证通知是否合法
				}
				//  NO 1
				order.setAmount(Integer.parseInt(map.get("amount")));
				order.setApp_ext1(map.get("app_ext1"));
				order.setApp_ext2(map.get("app_ext2"));
				order.setApp_key(map.get("app_key"));
				order.setApp_uid(map.get("app_key"));
				order.setGateway_flag(map.get("gateway_flag"));
				order.setOrder_id(Long.parseLong(map.get("order_id")));
				order.setProduct_id(map.get("product_id"));
				order.setSign(map.get("sign"));
				order.setSign_return(map.get("sign_return"));
				order.setSign_type(map.get("sign_type"));
				order.setUser_id(Long.parseLong(map.get("user_id")));
				cdao.saveSts_Object(order);

				if(!test)
				{
					//  NO 2
					PayVerify_360 payVerify = new PayVerify_360();
					boolean b = payVerify.verity(map, order);
					if(!b)
					{
						logger.log(Level.WARNING,"360Notice: 360 payVerify fail");
						cdao.saveSts_Object(order);
						break;
					}
				}
				//  NO 3 充值成功
				MJ_PayInfo payinfo = this.getPayInfoById(map.get("product_id"));
				if(payinfo == null) 
				{
					logger.log(Level.WARNING,"360Notice: payinfo == null, product_id:"+map.get("product_id"));
					break;
				}
				order.setRMB(payinfo.getRmb());
				order.setDiamand(payinfo.getDia());
				cdao.saveSts_Object(order);
				
				String userName = map.get("app_uid");
				boolean payB = onPayRMB(payinfo,userName);
				if(!payB)
				{
					logger.log(Level.WARNING,"360Notice: onPayRMB fail");
					break;
				}
				
				//NO 4	取消
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}while(false);
		
		return "ok";
	}
	public MJ_PayInfo getPayInfoById(String pid)
	{
		Map<Integer,MJ_PayInfo> payItems = Global.payItems;
		Iterator<MJ_PayInfo> it = payItems.values().iterator();
		while(it.hasNext())
		{
			MJ_PayInfo payinfo = it.next();
			if(payinfo.getPayStr_android().equals(pid))
				return payinfo;
		}
		return null;
	}
	
	/**
	 * 人民币充值
	 */
	public boolean onPayRMB(MJ_PayInfo payItem,String name)
	{
		
		  name = CCMD11301.PRE_360 + name;
		  logger.info("onPayRMB: name: " + name);
		MgsPlayer p = MgsCache.getInstance().getOnlnePlayer(name);
		if(p == null)
		{
			//查询出用户信息
			Business bs = new Business();
			MJ_User user = bs.findPlayerByName(name);
			if(user == null)
			{
				logger.log(Level.WARNING,"offLinePay fail: user == NULL  name:" +name);
				return false;
			}
			this.offLinePay(payItem,user);
		}else
		{
			this.onLinePayRMB(payItem,p);
		}
		return true;
	}
	/**
	 *  在线充值
	 * @param payItem
	 * @param player
	 */
	public void onLinePayRMB(MJ_PayInfo payItem,MgsPlayer player)
	{
		  logger.info("onLinePayRMB...");
		
		CCMD11041 cmd041 = new CCMD11041();
		cmd041.setPlayer(player);
		cmd041.payItem = payItem;
		
		MJ_User user = player.getBusiness().getPlayer();
		int beforeGold = user.getGold();
		int beforeDia = user.getDianQuan();
		int modGold = 0;
		int modDia = payItem.getDia();
		
		int pay_dia_before = user.getDianQuan();
		cmd041.pay_dia_before = pay_dia_before;
		
		logger.info("get diamand:" + payItem.getDia());
		CCMD11102 cmd102 = new CCMD11102();
		cmd102.setPlayer(player);
		cmd102.mod_dianquan(payItem.getDia());
		
		CCMD11101 cmd101 = new CCMD11101();
		cmd101.auto_deal(player,6);
		this.sendBuyResult(true,player); //购买结果
		cmd041.savePayCount();
		
		CountDao cdao = new CountDao();
		cdao.saveSts_Object(user);
		
		player.saveUserChargeRec(11141+"",beforeGold , modGold, user.getGold(), beforeDia, modDia, user.getDianQuan(), "充值钻石:"+payItem.getDia()+"");
		//充值统计
	}
	/**
	 * @param result
	 * @param player
	 */
	public void sendBuyResult(boolean result,MgsPlayer player)
	{
		int flag = -1;
		if(result)flag = 0;
		
		System.out.println("sendBuyResult:" + result);
		MyArray arr = new MyArray();
		arr.push(result);
		MyByteArray buf = new MyByteArray();
		buf.write(flag);
		player.send(11041,buf.getBuf());
		
	}
	/**
	 *  离线充值
	 * @param payItem
	 * @param user
	 */
	public void offLinePay(MJ_PayInfo payItem,MJ_User user)
	{
		  logger.info("offLinePay...");
		int beforeGold = user.getGold();
		int beforeDia = user.getDianQuan();
		int modGold = 0;
		int modDia = payItem.getDia();
		int pay_dia_before = user.getDianQuan();
		user.setDianQuan(user.getDianQuan() + payItem.getDia());
		CCMD11041 cmd041 = new CCMD11041();
		cmd041.payItem = payItem;
		cmd041.pay_dia_before = pay_dia_before;
		cmd041.savePayCountByMJ_User(user);
		CountDao cdao = new CountDao();
		cdao.saveSts_Object(user);
		MgsPlayer player = new MgsPlayer(1);
		player.saveUserChargeRec(user,11141+"",beforeGold , modGold, user.getGold(), beforeDia, modDia, user.getDianQuan(), "充值钻石:"+payItem.getDia()+"");
	}


}

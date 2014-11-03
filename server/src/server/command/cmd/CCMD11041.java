package server.command.cmd;

import java.math.BigDecimal;
import java.util.Calendar;

import org.jboss.netty.buffer.ChannelBuffer;

import server.command.CMD;
import server.command.GlobalData;
import server.command.MyArray;
import server.command.MyByteArray;
import server.command.PatternPai;
import server.mj.Global;
import server.mj.MgsPlayer;
import server.mj.ServerTimer;
import business.CountDao;
import business.conut.Sts_Arpu;
import business.conut.Sts_BuyProp;
import business.conut.Sts_GoldDia;
import business.conut.Sts_Recharge;
import business.entity.MJ_PayInfo;
import business.entity.MJ_User;
import business.entity.M_Shop;

/**
 *  购买商品
 * @author xue
 */
public class CCMD11041 extends  CMD{
	
	private PatternPai pattern = new PatternPai();
	private CCMD11102 cmd102 = new CCMD11102();
	private MJ_User user;
	private String errorStr = "购买失败!";
	private M_Shop shopItem = null;
	public MJ_PayInfo payItem = null;
	public int pay_dia_before = 0;
	
	public void setPlayer(MgsPlayer player) {
		this.player = player;
	}
	public ChannelBuffer getBytes() {
		return null;
	}
	public ChannelBuffer getBytes(Object obj) {
		return null;
	}
	public void setBytes(ChannelBuffer buf) {
		super.setBytes(buf);
		cmd102.setPlayer( this.player);
		user = this.player.getBusiness().getPlayer();
		int type = this.getIntVaule(0);
		int itemId  =  this.getIntVaule(1);
		
		switch(type)
		{
			case 0 :
				if(!Global.shopItem_roles.containsKey(itemId))return;
			shopItem = Global.shopItem_roles.get(itemId);
				buyRole(); break;//形象
			case 1 :
				if(!Global.shopItem_skills.containsKey(itemId))return;
					shopItem = Global.shopItem_skills.get(itemId);
				buySkill(); break;//技能
			case 2 :
				if(!Global.shopItem_props.containsKey(itemId))return;
					shopItem = Global.shopItem_props.get(itemId);
				buyProp(); break;//道具
			case 3 :
				if(!Global.shopItem_gold.containsKey(itemId))return;
					shopItem = Global.shopItem_gold.get(itemId);
				buyGold(); break;//金币
			case 4 :
				if(!Global.payItems.containsKey(itemId))return;
				payItem = Global.payItems.get(itemId);
//				payRMB();break;//点券  充值
		}
	}
	/**
	 * 人民币充值
	 */
	public void payRMB()
	{
		int beforeGold = user.getGold();
		int beforeDia = user.getDianQuan();
		int modGold = 0;
		int modDia = payItem.getDia();
		
		MJ_User user = this.player.getBusiness().getPlayer();
		pay_dia_before = user.getDianQuan();
		user.setDianQuan(user.getDianQuan() + payItem.getDia());
		this.player.getBusiness().saveDataObject(user);
		CCMD11101 cmd101 = new CCMD11101();
		cmd101.auto_deal(player,6);
		this.sendBuyResult(0); //购买结果
		savePayCount();
		
		this.player.saveUserChargeRec(11141+"",beforeGold , modGold, user.getGold(), beforeDia, modDia, user.getDianQuan(), "充值钻石:"+payItem.getDia()+"");
		//充值统计
	}
	 //成功 0 失败1
	public void buyRole()
	{
		int ret  = 0;
		int itemId  =  this.getIntVaule(1);
		if(shopItem == null)
		{
			errorStr = "购买失败,商品ID错误!";
			ret = -1;
		}
		if(this.user.getGold() < shopItem.getPrice_gold())
		{
			errorStr = "购买失败,金币余额不足!";
			ret = -1;
		}
		if(this.user.getDianQuan() < shopItem.getPrice_dianquan())
		{
			errorStr = "购买失败,点券余额不足!";
			ret = -1;
		}
		if(ret == 0)
		{
			boolean b = cmd102.unlockRole(user, itemId); // ID正确， 进步足够就解锁形象
			
			if(b)
			{
				MJ_User user = this.player.getBusiness().getPlayer();
				int beforeGold = user.getGold();
				int beforeDia = user.getDianQuan();
				int modGold = -shopItem.getPrice_gold();
				int modDia = -shopItem.getPrice_dianquan();
				
				Sts_BuyProp buy = new Sts_BuyProp();
				initBuyProp(buy);
				modPropData(buy,itemId,1);
				//扣除金币和点券
				if(b && shopItem.getPrice_gold() != 0)cmd102.mod_gold(-shopItem.getPrice_gold());
				if(b && shopItem.getPrice_dianquan() != 0)cmd102.mod_dianquan(-shopItem.getPrice_dianquan());
				buy.setAfterDia(user.getDianQuan());
				buy.setAfterG(user.getGold());
				CountDao cdao = new CountDao();
				cdao.saveSts_Object(buy);
				
				//修改金币统计
				Sts_GoldDia gd = cdao.findGD_today();
				if(gd == null)
				{
					gd = new  Sts_GoldDia();
					gd.setAbsDay(ServerTimer.distOfDay(Calendar.getInstance()));
					gd.setDay(ServerTimer.getDay());
				}
				Object[] arr = cdao.findTotalGoldDia();
				gd.setTotalGold(((BigDecimal)arr[0]).longValue());
				gd.setTotalDia(((BigDecimal)arr[1]).longValue());
				gd.setUsedGold(gd.getUsedGold() + shopItem.getPrice_gold());
				gd.setUseedDia(gd.getUseedDia() + shopItem.getPrice_dianquan());
				cdao.saveSts_Object(gd);
				
				this.player.saveUserChargeRec(11141+"",beforeGold , modGold, user.getGold(), beforeDia, modDia, user.getDianQuan(), "购买形象:"+itemId+"");
				
			}
			//修改点券
			if(!b)
			{
				errorStr = "购买失败!";
				ret = -1;
			}
		}
		if(ret == -1)sendErrorMsg(errorStr);
		this.sendBuyResult(ret); //购买结果
	}
	public void buySkill()
	{
		int ret  = 0;
		int itemId  =  this.getIntVaule(1);
		if(shopItem == null)
		{
			errorStr = "购买失败,商品ID错误!";
			ret = -1;
		}
		if(this.user.getGold() < shopItem.getPrice_gold())
		{
			errorStr = "购买失败,金币余额不足!";
			ret = -1;
		}
		if(this.user.getDianQuan() < shopItem.getPrice_dianquan())
		{
			errorStr = "购买失败,点券余额不足!";
			ret = -1;
		}
		if(ret == 0)
		{
			boolean b = cmd102.unlockSkill(user, itemId);	// ID正确， 进步足够就解锁技能
			
			if(b)
			{
				int beforeGold = user.getGold();
				int beforeDia = user.getDianQuan();
				int modGold = -shopItem.getPrice_gold();
				int modDia = -shopItem.getPrice_dianquan();
				
				Sts_BuyProp buy = new Sts_BuyProp();
				initBuyProp(buy);
				modPropData(buy,itemId,1);
				if(b && shopItem.getPrice_gold() != 0)cmd102.mod_gold(-shopItem.getPrice_gold());
				if(b && shopItem.getPrice_dianquan() != 0)cmd102.mod_dianquan(-shopItem.getPrice_dianquan());
				buy.setAfterDia(user.getDianQuan());
				buy.setAfterG(user.getGold());
				CountDao cdao = new CountDao();
				cdao.saveSts_Object(buy);
				
				
				//修改金币统计
				Sts_GoldDia gd = cdao.findGD_today();
				if(gd == null)
				{
					gd = new  Sts_GoldDia();
					gd.setAbsDay(ServerTimer.distOfDay(Calendar.getInstance()));
					gd.setDay(ServerTimer.getDay());
				}
				Object[] arr = cdao.findTotalGoldDia();
				gd.setTotalGold(((BigDecimal)arr[0]).longValue());
				gd.setTotalDia(((BigDecimal)arr[1]).longValue());
				gd.setUsedGold(gd.getUsedGold() + shopItem.getPrice_gold());
				gd.setUseedDia(gd.getUseedDia() + shopItem.getPrice_dianquan());
				cdao.saveSts_Object(gd);
				
				this.player.saveUserChargeRec(11141+"",beforeGold , modGold, user.getGold(), beforeDia, modDia, user.getDianQuan(), "解锁技能:"+itemId+"");
			}
			//修改点券
			if(!b)ret = -1;
		}
		if(ret == -1)sendErrorMsg(errorStr);
		this.sendBuyResult(ret); //购买结果
		
	}
	public void buyProp()
	{
		int ret  = 0;
		if(shopItem == null)
		{
			errorStr = "购买失败,商品ID错误!";
			ret = -1;
		}
		if(this.user.getGold() < shopItem.getPrice_gold())
		{
			errorStr = "购买失败,金币余额不足!";
			ret = -1;
		}
		if(this.user.getDianQuan() < shopItem.getPrice_dianquan())
		{
			errorStr = "购买失败,点券余额不足!";
			ret = -1;
		}
		if(ret == 0)
		{
			int itemId  =  shopItem.getProId();
			int propNum = shopItem.getPropNum();
			boolean b = cmd102.mod_prop(user,itemId, propNum);;	// ID正确，购买道具
			
			if(b)
			{
				int beforeGold = user.getGold();
				int beforeDia = user.getDianQuan();
				int modGold = -shopItem.getPrice_gold();
				int modDia = -shopItem.getPrice_dianquan();
				
				Sts_BuyProp buy = new Sts_BuyProp();
				initBuyProp(buy);
				mod_buyPropData(buy,itemId%100,propNum);
				if(b && shopItem.getPrice_gold() != 0)cmd102.mod_gold(-shopItem.getPrice_gold());
				if(b && shopItem.getPrice_dianquan() != 0)cmd102.mod_dianquan(-shopItem.getPrice_dianquan());
				buy.setAfterDia(user.getDianQuan());
				buy.setAfterG(user.getGold());
				CountDao cdao = new CountDao();
				cdao.saveSts_Object(buy);
				
				
				//修改金币统计
				Sts_GoldDia gd = cdao.findGD_today();
				if(gd == null)
				{
					gd = new  Sts_GoldDia();
					gd.setAbsDay(ServerTimer.distOfDay(Calendar.getInstance()));
					gd.setDay(ServerTimer.getDay());
				}
				Object[] arr = cdao.findTotalGoldDia();
				gd.setTotalGold(((BigDecimal)arr[0]).longValue());
				gd.setTotalDia(((BigDecimal)arr[1]).longValue());
				gd.setUsedGold(gd.getUsedGold() + shopItem.getPrice_gold());
				gd.setUseedDia(gd.getUseedDia() + shopItem.getPrice_dianquan());
				cdao.saveSts_Object(gd);
				
				this.player.saveUserChargeRec(11141+"",beforeGold , modGold, user.getGold(), beforeDia, modDia, user.getDianQuan(), "购买道具:"+itemId+"");
			}
			
			//修改点券
			if(!b)ret = -1;
		}
		if(ret == -1)sendErrorMsg(errorStr);
		this.sendBuyResult(ret); //购买结果
		
	}
	public void buyGold()
	{
		int ret = 0;
		int type = this.getIntVaule(1);
		boolean isRobort = this.getBooleanVaule(2);
		int gold = 0;
		int dianquan = 0;
		dianquan = this.shopItem.getPrice_dianquan();
		gold = this.shopItem.getPropNum();
		if(isRobort)
		{
			dianquan = 0;
		}
		if(gold == 0)
		{
			errorStr = "购买金币类型错误!";
			ret = -1;
		}
		if(this.user.getDianQuan() < dianquan)
		{
			errorStr = "购买失败,金币余额不足!";
			ret = -1;
		}
		if(ret == 0)
		{
			int beforeGold = user.getGold();
			int beforeDia = user.getDianQuan();
			int modGold = gold;
			int modDia = -shopItem.getPrice_dianquan();
			
			Sts_BuyProp buy = new Sts_BuyProp();
			initBuyProp(buy);
			modPropData(buy,type+10,1);
			cmd102.mod_gold(gold);
			cmd102.mod_dianquan(-dianquan);
			//修改点券
			buy.setAfterDia(user.getDianQuan());
			buy.setAfterG(user.getGold());
			CountDao cdao = new CountDao();
			cdao.saveSts_Object(buy);
			
			
			//修改金币统计
			Sts_GoldDia gd = cdao.findGD_today();
			if(gd == null)
			{
				gd = new  Sts_GoldDia();
				gd.setAbsDay(ServerTimer.distOfDay(Calendar.getInstance()));
				gd.setDay(ServerTimer.getDay());
			}
			Object[] arr = cdao.findTotalGoldDia();
			gd.setTotalGold(((BigDecimal)arr[0]).longValue());
			gd.setTotalDia(((BigDecimal)arr[1]).longValue());
			gd.setUseedDia(gd.getUseedDia() + dianquan);
			
			gd.setGoldFromDia(gd.getGoldFromDia() + gold);
			gd.setDiaToGold(gd.getDiaToGold() + dianquan);
			
			cdao.saveSts_Object(gd);
			
			String robotStr = "";
			if(isRobort)
				robotStr = ":机器人购买";
			this.player.saveUserChargeRec(11141+"",beforeGold , modGold, user.getGold(), beforeDia, modDia, user.getDianQuan(), "购买金币:"+gold+robotStr);
		}
		if(ret == -1)sendErrorMsg(errorStr);
		this.sendBuyResult(ret); //购买结果
	}
	public void sendBuyResult(int result)
	{
		MyArray arr = new MyArray();
		arr.push(result);
		MyByteArray buf = new MyByteArray();
		buf.write(arr);
		this.player.send(11041,buf.getBuf());
	}
	public void sendErrorMsg(String msg)
	{
		CCMD11111 ccmd = new CCMD11111();
		ccmd.auto_deal(this.player, msg);
	}
	public void auto_deal(MgsPlayer p)
	{
		SCMD11034 scmd = new SCMD11034();
	}
	
	public void modPropData(Sts_BuyProp buy,int index,int num)
	{
		switch(index)
		{
			case 2:
				buy.setProp2(num);
				break;
			case 3: 
				buy.setProp3(num);
				break;
			case 5: 
				buy.setProp4(num);
				break;
			case 100:
				buy.setProp5(num);
				break;
			case 101:
				buy.setProp6(num);
				break;
			case 102:
				buy.setProp7(num);
				break;
			case 103:
				buy.setProp8(num);
				break;
			case 104:
				buy.setProp9(num);
				break;
			case 105: 
				buy.setProp10(num);
				break;
			case 10:   //金币0
				buy.setProp11(num);
				break;
			case 11: //金币1
				buy.setProp12(num);
				break;
			case 12: //金币2
				buy.setProp13(num);
				break;
			case 13: //金币3
				buy.setProp14(num);
				break;
				

		}
	}
	public void mod_buyPropData(Sts_BuyProp buy,int index,int num)
	{
		switch(index)
		{
			case 1:  //喇叭
				buy.setProp1(num);
				break;
			case 2:// 邀请函
				buy.setProp15(num);
				break;
			case 3: //幸运药水
				buy.setProp16(num);
				break;
			case 4:// 强运药水
				buy.setProp17(num);
				break;
		
		}
	}
	public void initBuyProp(Sts_BuyProp buy)
	{
		buy.setAbsDay(ServerTimer.distOfSecond(Calendar.getInstance()));
		buy.setDay(ServerTimer.getNowString());
		buy.setTime1(ServerTimer.getDay());
		buy.setTime2(ServerTimer.getMonth());
		buy.setUid(user.getUid());
		buy.setAccount(this.user.getName());
		buy.setNick(user.getNick());
		buy.setBeforeDia(user.getDianQuan());
		buy.setBeforeG(user.getGold());
	}
	/**
	 * 充值统计
	 */
	public void savePayCount(){
		MJ_User user = this.player.getBusiness().getPlayer();
		savePayCountByMJ_User(user);
	}
	public void savePayCountByMJ_User(MJ_User user)
	{
		CountDao cdao= new CountDao();
		Sts_Recharge recharge = new Sts_Recharge();
		recharge.setAbsDay(ServerTimer.distOfSecond(Calendar.getInstance()));
		recharge.setAccount(user.getName());
		recharge.setUid(user.getUid());
		recharge.setDay(ServerTimer.getNowString());
		recharge.setTime1(ServerTimer.getNowString().substring(0,10));
		recharge.setTime2(ServerTimer.getNowString().substring(0,7));
		recharge.setNick(user.getNick());
		
		recharge.setDiaByfore(pay_dia_before);
		recharge.setDiaAfter(user.getDianQuan());
		
		Sts_Arpu arpu = cdao.findArpuToday();
		if(arpu == null)
		{
			arpu = new Sts_Arpu();
			arpu.setAbsDay(ServerTimer.distOfDay(Calendar.getInstance()));
			arpu.setDay(ServerTimer.getDay());
			arpu.setTimeMoth(ServerTimer.getMonth());
		}
		if(cdao.countPlayerCharge(user.getUid()) == 0){
				arpu.setChargeNum(arpu.getChargeNum() + 1);
				arpu.setAddCharge(arpu.getAddCharge() + 1);
			}else{//不是新增用户
				 	arpu.setChargeNum(arpu.getChargeNum() + 1);
			}
		
		switch(payItem.getItem())
		{
			case 1: 
				recharge.setDia1(recharge.getDia1() + payItem.getDia());
				break; 
			case 2:
				recharge.setDia2(recharge.getDia2() + payItem.getDia());
				break; 
			case 3:
				recharge.setDia3(recharge.getDia3() + payItem.getDia());
				break; 
			case 4:
				recharge.setDia4(recharge.getDia4() + payItem.getDia());
				break; 
			case 5: 
				recharge.setDia5(recharge.getDia5() + payItem.getDia());
				break; 
			case 6:
				recharge.setDia6(recharge.getDia6() + payItem.getDia());
				break; 
		}
		
		recharge.setMoney(payItem.getRmb());
		recharge.setPayWay(GlobalData.PINGTAI_SERVER);
		arpu.setAmount(arpu.getAmount() + payItem.getRmb());
		cdao.saveSts_Object(recharge);
		arpu.setArpuv((double)arpu.getAmount()/(double)arpu.getChargeNum());
		cdao.saveSts_Object(arpu);
	}

}

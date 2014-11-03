package server.command.cmd;


import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;

import common.Log;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.CheckTask;
import server.mj.Global;
import server.mj.MgsPlayer;
import server.mj.ServerTimer;
import business.CountDao;
import business.UnlockCup;
import business.conut.Sts_DJ_Count;
import business.conut.Sts_RewardProp;
import business.conut.Sts_UseProp;
import business.conut.Sts_UserCup;
import business.entity.MJ_Cup;
import business.entity.MJ_User;
import business.entity.M_Prop;
import business.entity.M_Shop;


/**
 * @author xue
 */
public class CCMD11082 extends CMD{

	private boolean flag = false;
	@Override
	public void setPlayer(MgsPlayer player) {
		this.player = player;
	}
	@Override
	public ChannelBuffer getBytes() {
		return null;
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		return null;
	}
	@Override
	public void setBytes(ChannelBuffer buf) {
		super.setBytes(buf);
		
		int type = this.getIntVaule(0);
		if(type == 1) //开
		{
			String reason = "";
			UnlockCup unlockCup = new UnlockCup();
			MJ_User user = this.player.getBusiness().getPlayer();
			int cupType = this.getIntVaule(1);
			boolean flag = unlockCup.checkCupUnlockCondition(user, cupType);
			Sts_UseProp use = new Sts_UseProp();
			List<Object>  props = null;
			if(flag)  //条件满足， 减少道具
			{
				List<Object>  arr = this.getArrValue(2);
				//检查道具
				props = arr;
				boolean propEnough = checkPropNum(arr,use);
				if(propEnough)
				{
						CCMD11102 cmd102 = new CCMD11102();
						cmd102.setPlayer(player);
						MJ_Cup cup = Global.cups.get(cupType);
						boolean useProp = cmd102.mod_prop(user, 302, -cup.getCost());
						if(!useProp)
							flag = false;
						else
						{
							use.mod_userProp(302, cup.getCost());
						}
				}else
				{
					flag = false;
					reason ="道具不足!";
				}
			}else
			{
				reason ="未解锁!";
			}
			if(flag)
			{
				this.player.cupOpen = true;
				this.player.cup_type = cupType;
				if(user.firstLogin_today)
					user.setOpenedCup(cupType);
				
				this.saveUseProp(user, use);
			}
			MyArray arr = new MyArray();
			arr.push(flag);
			MyByteArray bytenbuf = new MyByteArray();
			bytenbuf.write(arr);
			this.player.send(11082, bytenbuf.getBuf());
			
			
			MJ_Cup cup = Global.cups.get(cupType);
			CountDao cdao = new CountDao();
			Sts_UserCup sts_cup = new Sts_UserCup();
			sts_cup.startCup(player, cup,props);
			cdao.saveSts_Object(sts_cup);
			this.player.userCup = sts_cup;
			
			if(!flag)
			{
				CCMD11111 cmd111 = new CCMD11111();
				cmd111.auto_deal(player, "开启杯赛失败:"+reason);
			}
			
		}else if(type == 2)
		{
		
			boolean flag = true;
			if(!this.player.cupOpen || this.player.cup_type == -1)
				flag = false;

			String str = "";
			if(flag)
			{
				//奖励  难度*10的钻石，1邀请函 道具
	
				MJ_User user = this.player.getBusiness().getPlayer();
				MJ_Cup nowCup = Global.cups.get(this.player.cup_type);
				CCMD11102 cmd102 = new CCMD11102();
				cmd102.setPlayer(player);
				
//				cmd102.mod_dianquan(nowCup.getDifficulty() * 10);
//				cmd102.mod_prop(user, 302, 1);
				str = getCupReward(this.player.cup_type);
				
				cmd102.mod_Cup(this.player.cup_type, 1);
				UnlockCup unlockCup = new UnlockCup();
				boolean unlockNext = unlockCup.checkCupUnlockCondition(user, player.cup_type + 1);
				List<Integer> list = user.getCupScore();
				
				if(unlockNext && list.get(player.cup_type + 1) == -1) //解锁下一级别杯赛
				{
					cmd102.unlock_Cup(player.cup_type + 1);
					MJ_Cup nextCup = Global.cups.get(this.player.cup_type + 1);
					CCMD11111 cmd111 = new CCMD11111();
					cmd111.auto_deal(player,nextCup.getName()+ "  解锁!");
					
					CheckTask check = new CheckTask();
					check.checkTaskAtterHuPai(player, nextCup.getId() + 50);
					
				}
				//奖励  邀请函 金币 钻石
//				str = "获得奖励: \n钻石:" +(nowCup.getDifficulty() * 10) +"   邀请函   x 1" ;
				MJ_Cup cup = Global.cups.get(this.player.cup_type);
				CountDao cdao = new CountDao();
				Sts_UserCup sts_cup = this.player.userCup;
				sts_cup.endCup(player, cup);
				cdao.saveSts_Object(sts_cup);
			}
			this.player.cupOpen = false;
			this.player.cup_type = -1;
			this.player.userCup = null;
			MyArray arr = new MyArray();
			arr.push(flag);
			arr.push(str);
			MyByteArray bytenbuf = new MyByteArray();
			bytenbuf.write(arr);
			this.player.send(11082, bytenbuf.getBuf());
			
			//杯赛任务检查
			
			CheckTask checkTask = new CheckTask();
			checkTask.checkTaskAtterHuPai(this.player, CheckTask.TASK_CUP);
			
		}
		else if(type == 3)  //输 ：关闭
		{
			MJ_Cup cup = Global.cups.get(this.player.cup_type);
			CountDao cdao = new CountDao();
			Sts_UserCup sts_cup = this.player.userCup;
			sts_cup.failCup(player, cup);
			cdao.saveSts_Object(sts_cup);
			MyArray arr = new MyArray();
			arr.push(flag);
			arr.push("");
			MyByteArray bytenbuf = new MyByteArray();
			bytenbuf.write(arr);
			this.player.send(11082, bytenbuf.getBuf());
		}
	}
	public boolean checkPropNum(List<Object> obj,Sts_UseProp use)
	{
		if(obj == null || obj.size() == 0) return true;
		MJ_User user = this.player.getBusiness().getPlayer();
		M_Prop props = this.player.getBusiness().getPlayer().getProps();
		boolean flag = true;
		int i = 0;
		for(i = 0 ; i < obj.size(); i++)
		{
			int propId = (Integer) obj.get(i);
			int propNum = props.getPropNumById(propId);
			if(propNum <= 0)
			{
				flag = false;
				break;
			}
		}
		if(!flag)return false;
		
		for(i = 0; i < obj.size();i++)
		{
			int propId = (Integer) obj.get(i);
			use.mod_userProp(propId, 1);
			CCMD11102 cmd102 = new CCMD11102();
			cmd102.setPlayer(player);
			cmd102.mod_prop(user, propId, -1);
		}
		return true;
	}
	
	/**
	 * 每个杯赛有一定概率返回邀请函
	 * 根据杯赛获得 杯赛    ID*10 钻石的价值相当的奖励
	 * 
	 * 喇叭: z1  邀请函 z10                             道具3: g300                      道具4: g1000
	 */
	public String getCupReward(int cupId)
	{
		Map<Integer,M_Shop> shopItem_props = Global.shopItem_props;
		M_Shop prop4 = shopItem_props.get(504);
		M_Shop prop3 = shopItem_props.get(403);
		M_Shop prop1 = shopItem_props.get(101);
		
		MJ_User user = this.player.getBusiness().getPlayer();
		CCMD11102 cmd102 = new CCMD11102();
		cmd102.setPlayer(player);
		
		String str = "获得奖励:";
		int[] rewardArr = new int[]{0,0,0,0,0,0};// 邀请函，道具4，道具3，道具1，钻石，金币
		boolean flag = false;
		// 1 一定概率获得邀请函
		int gv_p2 = (int) (Math.random() * 100);
		if(gv_p2 < 30) //获得道具邀请函
		{
			rewardArr[0] = 1;
			str += "  邀请函x1";
			cmd102.mod_prop(user,302, 1);
			mod_StsRewardProp(302,1);
		}
		int value= cupId * 20;
		if(value > (prop4.getPrice_gold()/10) + prop4.getPrice_dianquan())
		{
			flag = getRand(30);
			if(flag)
			{
				value-=(prop4.getPrice_gold()/10 +  prop4.getPrice_dianquan());	rewardArr[1] = 1;
				str += "  幻运药水x1";
				cmd102.mod_prop(user,prop4.getProId(), 1);
				mod_StsRewardProp(prop4.getProId(),1);
			}
		}
		if(value > (prop3.getPrice_gold()/10) + prop3.getPrice_dianquan())
		{
			flag = getRand(30);
			if(flag)
			{
				value-= (prop3.getPrice_gold()/10 +prop3.getPrice_dianquan());	rewardArr[2] = 1;
				str += "  幸运药水x1";
				cmd102.mod_prop(user,prop3.getProId(), 1);
				mod_StsRewardProp(prop3.getProId(),1);
			}
		}
		int priceLaba = prop1.getPrice_dianquan() + prop1.getPrice_gold()/10;
		if(value > priceLaba) //喇叭
		{
			flag = getRand(30);
			if(flag)
			{
				int num = (int) (Math.random() * (value/priceLaba)+1);
				value-= (num *priceLaba) ;	rewardArr[3] = num;
				str += "  喇叭x" + num;
				cmd102.mod_prop(user,prop1.getProId(), num);
				
				mod_StsRewardProp(prop1.getProId(),num);
			}
		}
		if(value > 0)//钻石
		{
			flag = getRand(50);
			if(flag)
			{
				int num = (int) (Math.random() * value+1);
				value-= num;	rewardArr[4] = num;
				str += "  钻石x" + num;
				cmd102.mod_dianquan(num);
			}
		}
		if(value > 0)  //金币
		{
				rewardArr[5] = value * 10;
				str += "  金币x" + value * 10;
				cmd102.mod_gold(value * 10);
		}
		System.out.println(str);
		return str;
//		CCMD11111 cmd111 = new CCMD11111();
//		cmd111.auto_deal(getPlayer(), str);
	}
	public boolean getRand(int rate)
	{
		int gv = (int) (Math.random() * 100);
		if(rate < gv)
			return true;
		return false;
	}
	
	public void mod_StsRewardProp(int propId,int propNum)
	{
		MJ_User user = this.player.getBusiness().getPlayer();
		//查询今天记录， 修改
		CountDao cdao = new CountDao();
		Sts_RewardProp prop = cdao.findTodayRewardProp(user);
		prop.mod_prop(propId, propNum);
		cdao.saveSts_Object(prop);
	}
	
	public void saveUseProp(MJ_User user,Sts_UseProp use)
	{
		use.setAbsDay(ServerTimer.distOfSecond(Calendar.getInstance()));
		use.setDay(ServerTimer.getNowString());
		use.setTime1(ServerTimer.getDay());
		use.setTime2(ServerTimer.getMonth());
		use.setUid(user.getUid());
		use.setAccount(user.getName());
		use.setNick(user.getNick());
		CountDao cdao = new CountDao();
		cdao.saveSts_Object(use);
	}
	
}

package server.command.cmd;


import java.util.List;
import java.util.logging.Logger;

import org.jboss.logging.Logger.Level;
import org.jboss.netty.buffer.ChannelBuffer;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.Global;
import server.mj.MgsPlayer;
import business.CountDao;
import business.conut.Sts_DJ_Count;
import business.conut.Sts_UserDanji;
import business.conut.Sts_UserDanjiCount;
import business.entity.MJ_DanJil;
import business.entity.MJ_User;

import common.MyArrays;



/**
 * 单机
 * @author xue
 */
public class CCMD11081 extends CMD{

	private static final Logger logger = Logger.getLogger(CCMD11081.class.getName());
	
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
			int changci = this.getIntVaule(1);
			MJ_DanJil danji = Global.danji.get(changci);
			
			MJ_User  user = this.player.getBusiness().getPlayer();
			List<Integer> danjiChang = user.getDanJiChangs();
			boolean flag = false;
			if(danji != null)
				flag = true;
			
			
			if(flag && danjiChang.get(changci -1) > 0)
			{
				flag = true;
			}else
			{
				flag = false;
			}
			
			if(flag && user.getGold() >= danji.getCost())
			{
				flag = true;
				this.player.danjiOpen = true;
				this.player.danji_type =  changci;
				if(user.getOpenedDanji() == -1)
				{
					user.befroreOpenedDanji = -1;
					user.setOpenedDanji(changci);
				}else
				{
					user.befroreOpenedDanji = -2;
				}
				
				danjiChang.set(changci -1,  danjiChang.get(changci - 1) - 1);
				user.setDanJiChangs(danjiChang);
				
				
				
				CCMD11102 cmd102 = new CCMD11102();
				cmd102.setPlayer(this.player);
				cmd102.mod_gold(-danji.getCost());
			}else
			{
				flag = false;
			}
			MyArray arr = new MyArray();
			arr.push(flag);
			MyByteArray bytenbuf = new MyByteArray();
			bytenbuf.write(arr);
			this.player.send(11081, bytenbuf.getBuf());
			

			CCMD11101 cmd101  = new CCMD11101();
			cmd101.setPlayer(this.player);
			cmd101.sendUserInfo(MyArrays.asList(32));
			
			
			CountDao cdao = new CountDao();
			Sts_UserDanji  sts_danji= new Sts_UserDanji();
			sts_danji.startDanji(player, danji);
			cdao.saveSts_Object(sts_danji);
			this.player.userDanji = sts_danji;
			
			open_sts_DanjiCount(changci);
			
			
			mod_danJICount(user);
			
			
		}else if(type == 2 ) // 关
		{
			boolean flag =  true;
			if(	!this.player.danjiOpen)
			{
				flag = false;
			}
			if(player.danji_type == -1) 
			{
				flag = false;
			}
			int score = this.getIntVaule(1);
			if(flag)
			{
					int changci = player.danji_type;
					MJ_DanJil danji = Global.danji.get(changci);
					int maxScore = danji.getStartPoint();
					if(danji.getQuan() > 0)maxScore = maxScore * 4;
					else maxScore = maxScore * 2;
					if(score > maxScore)
					{
						flag = false;
						CCMD11111 cmd111 = new CCMD11111();
						cmd111.auto_deal(this.player, "积分错误！");
						CCMD11102 cmd102 = new CCMD11102();
						cmd102.setPlayer(this.player);
						cmd102.mod_gold(danji.getCost());
						
						logger.info("ccmd11081 endDanji ScoreError userScore:" + score +"  maxScore:"+maxScore +" danjiChang:" +changci);
					}
					if(flag)
					{
						CountDao cdao = new CountDao();
						Sts_UserDanji  sts_danji= this.player.userDanji;
						sts_danji.endDanji(player, danji);
						cdao.saveSts_Object(sts_danji);
					}
					close_sts_DanjiCount(changci);
			}
			if(flag)
			{
				MJ_User user = this.player.getBusiness().getPlayer();
				if(user.getOpenedDanji() > 0)
				user.setOpenedDanji(user.befroreOpenedDanji);
				
				CCMD11102 cmd102 = new CCMD11102();
				cmd102.setPlayer(this.player);
				cmd102.mod_gold(score);
			}
			MyArray arr = new MyArray();
			arr.push(flag);
			MyByteArray bytenbuf = new MyByteArray();
			bytenbuf.write(arr);
			this.player.send(11081, bytenbuf.getBuf());
			player.danji_type = -1;
			player.danjiOpen = false;
			player.userDanji = null;
			this.player.send(11081, bytenbuf.getBuf());
			

			
		}
	}
	
	public void open_sts_DanjiCount(int type)
	{
		//查询单日， 修改
		CountDao cdao = new CountDao();
		Sts_UserDanjiCount count = cdao.findTodyDanjiCount();
		count.openDanji(type);
		cdao.saveSts_Object(count);
	}
	public void close_sts_DanjiCount(int type)
	{
		//查询单日， 修改
		CountDao cdao = new CountDao();
		Sts_UserDanjiCount count = cdao.findTodyDanjiCount();
		count.closeDanji(type);
		cdao.saveSts_Object(count);
	}
	public void mod_danJICount(MJ_User user)
	{
		user.setDanjiCount(user.getDanjiCount() + 1);
		CountDao cdao = new CountDao();
		Sts_DJ_Count count = cdao.findDJCount();
		switch(user.getDanjiCount())
		{
			case 1:
				count.setTime_1(count.getTime_1() + 1);
				break; 
			case 2:
				if(count.getTime_1()>0)count.setTime_1(count.getTime_1() - 1);
				count.setTime_2(count.getTime_2() + 1);
				break; 
			case 3: 
				if(count.getTime_2()>0)count.setTime_2(count.getTime_2() - 1);
				count.setTime_3(count.getTime_3() + 1);
				break; 
			case 4: 
				if(count.getTime_3()>0)count.setTime_3(count.getTime_3() - 1);
				count.setTime_4(count.getTime_4() + 1);
				break; 
			case 5:
				if(count.getTime_4()>0)count.setTime_4(count.getTime_4() - 1);
				count.setTime_5(count.getTime_5() + 1);
				break; 
			case 6:
				if(count.getTime_5()>0)count.setTime_5(count.getTime_5() - 1);
				count.setTime_other(count.getTime_other() + 1);
				break; 
		}
		cdao.saveSts_Object(count);
		cdao.saveSts_Object(user);
	}

}

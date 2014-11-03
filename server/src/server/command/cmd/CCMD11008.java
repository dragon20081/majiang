package server.command.cmd;

import java.util.List;
import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;

import server.command.CMD;
import server.command.PatternPai;
import server.command.SCMD3;
import server.mj.MgsCache;
import server.mj.MgsPlayer;
import server.mj.Room;
import common.*;
/**
 *   杠
 * @author xue
 */
public class CCMD11008 extends  CMD{
	
	
	private static final Logger logger = Logger.getLogger(CCMD11008.class.getName());
	private PatternPai pattern = new PatternPai();
	private int gangPai;
	private int touchedPai = -1;
		
	private int skillId  = -1;
	public void setPlayer(MgsPlayer player) {
		this.player = player;
	}
	@Override
	public MgsPlayer getPlayer() {
		return player;
	}


	public ChannelBuffer getBytes() {
		return null;
	}
	public ChannelBuffer getBytes(Object obj) {
		return null;
	}
	
	private  ChannelBuffer deal_buf;
	private int type;
	public void setBytes(ChannelBuffer buf) {
		super.setBytes(buf);
		
		if(this.player.getTouchedPai() != -1)
		{
			touchedPai = this.player.getTouchedPai();
			this.player.getShoupai().add(touchedPai);
			this.player.setTouchedPai(-1);
		}
		
		Room r  =  player.getRoom();
		
		if(r.isInTuoguan(player))
		{
			return;
		}
		
		int pai =  this.getIntVaule(0);
		if(this.getValues().size() > 1)
		{
			skillId  = this.getIntVaule(1);
		}
		gangPai  = pai;
		if(this.touchedPai != -1)
		{
			if(r.getTimeFlag() != Room.TIME_DAPAI){
				return;
				};
			//摸牌 状态  chupai
			type   =  PatternPai.MO_PAI;
			logger.info("ccmd11008:"+ this.player.getName()+":gang  mopai: "+ touchedPai);
			
		}else
		{
			//打的牌
			if(r.getTimeFlag() != Room.TIME_DEALPAI || pai != r.getCurPaiId()){return;}//断线
			type   =  PatternPai.DA_PAI;
			logger.info("ccmd11008:"+ this.player.getName()+":gang  dapai: "+ pai);
		}
		
		int gangType  =  0;   // 1 明杠  2 小明杠  3  暗杠
		int paiId = pai;
		Log.log("杠牌:" + paiId);
		//  打牌  
		if(type   ==  PatternPai.DA_PAI)
		{
			boolean flag = checkInDealArray(this.player);
			if(!flag)return;
			boolean b = pattern.checkGang(this.player,paiId,type);//等待waitPlayer 处理
			if(b) gangType = 1;
		}else if(type == PatternPai.MO_PAI)
		{
			//自己有这张牌，(手牌或者 摸牌中有这张牌)
//			if(!this.player.getShoupai().contains(pai))
//			{
//				this.deal_eeor(paiId, r,type);return;	//掉线处理
//			}

			boolean xmg  = this.player.getPeng().contains(paiId);			  //摸牌 ，  ·小明杠          暗杠       小明杠 抢杠胡判断            暗杠，摸牌逻辑
			if(xmg)  gangType =  2;

			boolean anGang  =  pattern.checkAnGang(this.player, paiId);			//暗杠 1 杠摸牌 ，  2 杠其他牌
			if(anGang) gangType = 3;
		}
		if(gangType == 0){this.deal_eeor(paiId, r,type);return;}
		switch(gangType)
		{
				case 1:
					
							logger.info("cmd11008 mingGang:"+this.player.getName() +"  pai:"+paiId);
								if(this.skillId != -1)
								{
									if(skillId >0)this.player.getSkillutil().useSkillById(player, skillId);
								}
								SCMD11008   scmd  =  new SCMD11008();  
								scmd.setPaiId(paiId);
								scmd.setType(SCMD11008.DAMINGGANG);
								scmd.setWhoDaId(r.getCurPlayer().getRoomId());
								ChannelBuffer cachebuf  = player.coderCMD(11008, scmd.getBytes(this.player));
								deal_buf  =  cachebuf;
								r.waitDeal_back(this.player, "杠", this);
					break;  //明杠
				case 2: 
							logger.info("cmd11008 xiaoMingGang:"+this.player.getName() +"  pai:"+paiId);
							//检查是否其他玩家的叫牌是这张牌
							List<MgsPlayer> plist = r.check_jiao(this.player,paiId);
							//告诉客户端开始倒计时
							SCMD11008   scmd1 =  new SCMD11008();  
							scmd1.setPaiId(paiId);
							scmd1.setType(SCMD11008.XIAOMINGANGANG);
							   if(plist.size() >0)
							   {
								    r.setCurPaiId(paiId);
								    r.SendRoomBroadcast(11008,scmd1.getBytes(this.player));
								    r.setWaitDealPlayer(plist);
								    r.start_qiangganghuTimer();
									return;
							   }else
							   {
									if(this.skillId != -1)
									{
										System.out.println("not complete use skill ");
										Log.log("ccmd11008");
										if(skillId >0)this.player.getSkillutil().useSkillById(player, skillId);
									}
								    this.player.setGangshanghua(true);
								    this.player.setLiangang(this.player.getLiangang() +1);
								    this.changeShouPai(this.gangPai);
									SCMD3 scmd3_small = new SCMD3();
									scmd3_small.list.add(player.coderCMD(11008,scmd1.getBytes(this.player) ));
									this.player.increaseQi(5);
									scmd3_small.list.add(new CCMD11031().getBuf(player, 5)); //气
//									CCMD11006 ccmdSmall  = new CCMD11006(); //摸牌
//									scmd3_small.list.add(ccmdSmall.auto_deal(player));
									r.SendRoomBroadcast(3, scmd3_small.getBytes());
							   }
					break;  //小明杠
				case 3: 
						this.player.setGangshanghua(true);
					    this.player.setLiangang(this.player.getLiangang() +1);
						r.setTiantingFlag(false);
						this.changeShouPai(gangPai);
							SCMD11008   scmdAn  =  new SCMD11008();  
							scmdAn.setPaiId(paiId);
							scmdAn.setType(SCMD11008.ANGANG);
							SCMD3 scmd3_an = new SCMD3();
							ChannelBuffer cachebuf_an  = player.coderCMD(11008, scmdAn.getBytes(this.player));
							scmd3_an.list.add(cachebuf_an);
							this.player.increaseQi(5);
							scmd3_an.list.add(new CCMD11031().getBuf(player, 5)); //气
//							CCMD11006 ccmdAn  = new CCMD11006(); //摸牌
//							ccmdAn.setPlayer(player);
//							ChannelBuffer cachebuf2_an  = ccmdAn.auto_deal(this.player);
//							scmd3_an.list.add(cachebuf2_an);
							r.SendRoomBroadcast(3, scmd3_an.getBytes());
							
							logger.info("cmd11008 success:" + this.player.getName() +this.gangPai);
							logger.info(this.player.getAllPaiStr());
					break;  //暗杠    --》杠 -->摸牌
		}
		///////////////////////////////////////////////////////////////////////////
	}
	public void deal_eeor(int paiId,Room r,int type)
	{ 
		logger.info(this.player.getAllPaiStr());
		this.player.getBusiness().saveUserOperate("ccmd11008:error "+this.player.getName() + "pai:"+paiId + "  type:" +  type +"  shoupai "+ this.player.getShoupai()+"peng:"+ this.player.getPeng());
		
		if(type == PatternPai.DA_PAI)
		{
			//直接过
			r.deal_pass(player);
		}else  //摸牌  直接打掉手牌
		{
			CCMD11004  ccmd  = new CCMD11004();
			int pai =  this.player.getTouchedPai();
			if(pai == -1)
			{
				List<Integer> shoupai = this.player.getShoupai();
				MyArrays.sort(shoupai);
				int num = (int) (Math.random() * shoupai.size());
				pai = shoupai.get(num);
			}
			this.player.setTouchedPai(-1);
			ccmd.auto_deal(pai);
		}
		//托管的触发:  1. 断线  2前后台数据判断不一致  3.主动托管
		//让玩家断线
		CCMD11111 c111 = new CCMD11111();
		c111.auto_deal(this.player, "数据出错，本局已托管！  杠   " + paiId);
		this.player.getRoom().addTuoGuanQueue(player);
//		this.player.closing();
		//清理cache中缓存的玩家
//		MgsCache.getInstance().removeCache(this.player);
	}
	public void timeout_qiangganghu(MgsPlayer p )
	{
		   this.player.setGangshanghua(true);
		    this.player.setLiangang(this.player.getLiangang() +1);
//		CCMD11006 ccmd  = new CCMD11006(); //摸牌
//		p.getRoom().SendRoomBroadcast(11006,ccmd.auto_deal(p) );
	}
	
	public void deal_back()
	{
		if(this.skillId != -1)
		{
			System.out.println("not complete use skill ");
			Log.log("ccmd11008");
			if(skillId >0)this.player.getSkillutil().useSkillById(player, skillId);
		}
		this.player.setGangshanghua(true);
	    this.player.setLiangang(this.player.getLiangang() +1);
		this.player.getRoom().setTiantingFlag(false);
		//改变手牌
		changeShouPai(this.gangPai);
		SCMD3 scmd3 = new SCMD3();
		if(deal_buf != null)
			scmd3.list.add(deal_buf);
		
//		CCMD11006 ccmd  = new CCMD11006(); //摸牌
//		scmd3.list.add(ccmd.auto_deal(this.player));
		this.player.getRoom().SendRoomBroadcast(3, scmd3.getBytes());
		this.player.getRoom().changeCurPlayer(player);
		logger.info("cmd11008 success:" + this.player.getName() +this.gangPai);
		logger.info(this.player.getAllPaiStr());
	}
	public void changeShouPai(int pai)
	{
		int mopai  =  this.player.getTouchedPai();
		if( mopai != -1)
		{
			this.player.getShoupai().add(mopai);
			this.player.setTouchedPai(-1);
			//改变打出去的牌
		}
		if(type  ==  PatternPai.DA_PAI)
		{
//			this.player. 标记打牌的玩家这张牌被杠了
			List<int[]> p_dapai  = this.player.getRoom().getCurPlayer().getDapai();
			int[] arr = p_dapai.get(p_dapai.size()-1);
			arr[1] = 1; //标记为被要
			//删除手牌中的3张，  增加杠
			this.player.getGang().add(MyArrays.asList(pai,0));
			List<Integer> shoupai =  this.player.getShoupai();
			while(shoupai.contains(pai))
			{
				shoupai.remove((Integer)pai);
			}
			
		}else if(type  ==  PatternPai.MO_PAI)
		{
			//暗杠   删除4张牌
			//小明杠  删除碰  加入杠 删除一张牌
			if(this.player.getPeng().contains(pai))
			{
				this.player.getGang().add(MyArrays.asList(pai,0));
				this.player.getPeng().remove((Integer)pai);
				this.player.getShoupai().remove((Integer)pai);
			}else
			{
				this.player.getGang().add(MyArrays.asList(pai,1));
				while(this.player.getShoupai().contains(pai))
				{
					this.player.getShoupai().remove((Integer)pai);
				}
			}
			
		}

	}
	
	/**
	 * 确认指令合法性
	 * 检查玩家操作是否在等待处理玩家队列
	 */
	public boolean checkInDealArray(MgsPlayer p)
	{
		if(p == null || p.getRoom() == null)
			return false;
		List<MgsPlayer> list = p.getRoom().getWaitDealPlayer();
		if(list.contains(p))return true;
		return false;
		
	}

}

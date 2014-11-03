package server.command.cmd;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;

import server.command.CMD;
import server.command.PatternPai;
import server.mj.MgsCache;
import server.mj.MgsPlayer;
import server.mj.Room;

/**
 *   吃
 * @author xue
 */
public class CCMD11013 extends  CMD{
	
	
	private static final Logger logger = Logger.getLogger(CCMD11013.class.getName());
	private PatternPai pattern = new PatternPai();
	private int pai1;
	private int pai2;
	private int pai3;
	private int curPai;
	private int skillId = -1;

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
	public void setBytes(ChannelBuffer buf) {
		
		super.setBytes(buf);
		Room  r = this.player.getRoom();
		if(r.isInTuoguan(player))
		{
			return;
		}
		
		 pai1 = this.getIntVaule(0);
		 pai2 = this.getIntVaule(1);
		 pai3 = this.getIntVaule(2);
		 curPai = r.getCurPaiId();
		logger.info("ccmd11013 :"+ this.player.getName()+"   chi: arg: "+ pai1+"  "+ pai2+"  "+pai3+" curPai:"+curPai);
		 
		 if(this.getValues().size() > 3)
		 {
			 this.skillId =  this.getIntVaule(3);
			 logger.info("chi ccmd11013: skillid:" + skillId);
		 }
		boolean flag = checkInDealArray(this.player);
		if(!flag)return;
		 
		boolean b  =  pattern.checkChi(player, pai1, pai2, pai3,curPai);
		boolean shangjia  =check_shangjia();
		if(!shangjia)
			b = false;
		 if(curPai!=pai1 && curPai!=pai2 && curPai!=pai3 )
			 b = false; 
		//必须是自己的上家
		if(b)
		{
				r.waitDeal_back(this.player, "吃", this);
		}else
		{

			logger.info("cmd 11013:" + this.player.getAllPaiStr());
			this.player.getRoom().deal_pass(this.player);
			
			CCMD11111 c111 = new CCMD11111();
			c111.auto_deal(this.player, "数据出错，本局已托管！");
			logger.info("ccmd11013 error:"+ this.player.getName()+"  can't chi:"+ pai1+"  "+ pai2+"  "+pai3+"  "+curPai);
			this.player.getBusiness().saveUserOperate("ccmd11013 error:"+ this.player.getName()+"  can't chi:"+ pai1+"  "+ pai2+"  "+pai3+"  "+curPai);
			
			this.player.getRoom().addTuoGuanQueue(player);
			//让玩家断线
//			this.player.closing();
			//清理cache中缓存的玩家
//			MgsCache.getInstance().removeCache(this.player);
		}
	}
	/**
	 * 优先序列回调
	 */
	public void deal_back()
	{
		
		if(this.skillId != -1)
		{
			System.out.println("not complete use skill ");
		}
		Room r =  this.player.getRoom();
		r.setTiantingFlag(false);
		
		changeShoupai();
		changePrePlayerDapai();
		SCMD11013   scmd  =  new SCMD11013(); 
		scmd.setPai1(pai1);
		scmd.setPai2(pai2);
		scmd.setPai3(pai3);
		this.player.getRoom().SendRoomBroadcast(11013, scmd.getBytes(this.player));
		r.changeCurPlayer(this.player);
		r.start_chupaiTimer();
		logger.info("cmd 11013:" + this.player.getAllPaiStr());		
	}
	/**
	 *  检查打牌的玩家是否是自己的上家 
	 */
	public boolean check_shangjia()
	{
		if(this.player.getRoom().getPlayerLimit() == 2)return true;
		MgsPlayer curp  = this.player.getRoom().getCurPlayer(); //p 当前
		int thisRoomId  =  this.player.getRoomId();
		if(thisRoomId  -  curp.getRoomId() == 1)return true;
		if(thisRoomId == 1 &&  curp.getRoomId() == this.player.getRoom().MAX_COUNT )return true;
		return false;
	}
	/**
	 *  改变手牌
	 */
	public void changeShoupai()
	{
		// 增加吃， 删除 pai1 ,pai2
		int[] arr =  new int[]{pai1,pai2,pai3};
		Arrays.sort(arr);
		this.player.getChi().add(arr);
		
		if(player.getShoupai().contains(pai1) && pai1 != this.curPai)player.getShoupai().remove((Integer)pai1);
		if(player.getShoupai().contains(pai2) && pai2 != this.curPai)player.getShoupai().remove((Integer)pai2);
		if(player.getShoupai().contains(pai3) && pai3 != this.curPai)player.getShoupai().remove((Integer)pai3);
		
	}
	/**
	 *  改变打出这张牌的玩家的打牌，标记为被要
	 */
	public void changePrePlayerDapai()
	{
		List<int[]> list = this.player.getRoom().getCurPlayer().getDapai();
		int[] arr  = list.get(list.size() -1);
		if(arr[0] == this.curPai)arr[1]  = 1;
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

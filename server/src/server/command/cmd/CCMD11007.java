package server.command.cmd;

import java.util.List;
import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;

import server.command.CMD;
import server.command.PatternPai;
import server.mj.MgsCache;
import server.mj.MgsPlayer;
import server.mj.Room;

/**
 *   碰
 * @author xue
 *
 */
public class CCMD11007 extends  CMD{
	
	
	private static final Logger logger = Logger.getLogger(CCMD11007.class.getName());
	private PatternPai pattern = new PatternPai();
	private int pengPai;
	private int skillId   =  -1;
	
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
		//检查是否有技能ID 
		if(this.getValues() != null &&  this.getValues().size() > 0)
			skillId  = this.getIntVaule(0);
		
		// 优先级  碰  吃  胡  杠
		// 对比dealplayer，保存操作， 最后根据优先级决定谁的操作成功
		Room r = player.getRoom();
		if(r.isInTuoguan(player))
		{
			return;
		}
		boolean flag = checkInDealArray(this.player);
		if(!flag)return;
		
		boolean b  =  pattern.checkPeng(this.player,r.getCurPaiId());
		
		logger.info("ccmd11007:"+this.player.getName()+"  " + r.getCurPaiId());
		if(b){
			//调用room的方法处理
			pengPai   =  r.getCurPaiId();
			logger.info("cmd 11007:"+ this.player.getName() +"  pai:" + pengPai);
			
			r.waitDeal_back(this.player, "碰", this);
		}else
		{
			logger.info(this.player.getAllPaiStr());
			CCMD11111 c111 = new CCMD11111();
			c111.auto_deal(this.player, "数据出错，本局已托管！ 碰:  " + pengPai);
			logger.info("ccmd11007: error"+ this.player.getName() + "peng fail  getCurPaiId:"+r.getCurPaiId());
			this.player.getBusiness().saveUserOperate("ccmd11007: error"+ this.player.getName() + "peng fail:"+r.getCurPaiId());
			r.deal_pass(this.player);
			this.player.getRoom().addTuoGuanQueue(player);
				//让玩家断线
//				this.player.closing();
				//清理cache中缓存的玩家
//				MgsCache.getInstance().removeCache(this.player);
		}
	}
	public void deal_back()
	{
		changePrePlayerDapai();
		Room r =  this.player.getRoom();
		r.setTiantingFlag(false);
		
		int prePlayerId  = r.getCurPlayer().getRoomId();
		SCMD11007   scmd  =  new SCMD11007(); 
		int paiId  = this.player.getRoom().getCurPaiId();
		scmd.setPaiId(paiId);
		scmd.setWhoDaId(prePlayerId);
		
		if(prePlayerId > 2)
		{
			System.out.println("");
		}
		if(this.skillId != -1)
		{

		}
		r.SendRoomBroadcast(11007, scmd.getBytes(this.player));
		//改变手牌
		changeShouPai();
		//改变当前玩家    开始出牌计时
		r.changeCurPlayer(this.player);
		r.start_chupaiTimer();
		
		logger.info("cmd 11007  success:"+ this.player.getName() +"  pai:" + pengPai);
		logger.info(this.player.getAllPaiStr());
	}
	/**
	 *   增加碰牌 ， 删除手牌中的pengPai ,
	 */
	public void changeShouPai()
	{
		this.player.getPeng().add(this.pengPai);
		while(this.player.getShoupai().contains(pengPai))
		{
			this.player.getShoupai().remove((Integer)pengPai);
		}
	}
	/**
	 *  改变打出这张牌的玩家的打牌，标记为被要
	 */
	public void changePrePlayerDapai()
	{
		List<int[]> list = this.player.getRoom().getCurPlayer().getDapai();
		int[] arr  = list.get(list.size() -1);
		arr[1]  = 1;
	}
	@Override
	public MgsPlayer getPlayer() {
		return player;
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

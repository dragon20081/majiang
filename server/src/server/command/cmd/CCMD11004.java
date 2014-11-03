package server.command.cmd;

import java.util.List;
import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;

import common.Log;

import server.command.CMD;
import server.command.SCMD3;
import server.mj.MgsCache;
import server.mj.MgsPlayer;
import server.mj.MgsRoom;
import server.mj.Room;

/**
 *   玩家打出下一张牌
 * @author xue
 *
 */
public class CCMD11004 extends  CMD{
	
	private static final Logger logger = Logger.getLogger(CCMD11004.class.getName());

	private int touchPaiId =  0;
	private int skillId = -1;

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
		
		this.player.setLiangang(0);
		Room r = this.player.getRoom();
		if(r == null)
		{
			Log.log("11004 room == null");
			return;
		}
		if(r.isInTuoguan(player))
		{
			return;
		}
		
		if(r.room_state != MgsRoom.STARTED )
			return;
		
		if(this.player != r.getCurPlayer())
		{
			return;
		}
		if(!this.player.canDapai())
		{
			logger.info("pai: gang  "+player.getGang().size() +"   chi  "+ player.getChi().size() +"   peng " + player.getPeng().size()  +"  shoupai:" +player.getShoupai().size());
		}
		SCMD3 scmd3  = new SCMD3();
		int paiId  = (Integer) this.getValues().get(0);
		
		boolean boolTing = this.getBooleanVaule(1);
		logger.info("cmd 11004:" + this.player.getName()+ ",dapai:" + paiId+",boolTing:"+boolTing);
		
		if(boolTing)			//听牌
		{
			this.player.setTing(true);
	 		if(r.isTiantingFlag() && this.player.getDapai().size() == 0)this.player.setTianting(true);
			SCMD11015   scmd  =  new SCMD11015(); 
			scmd3.list.add(this.player.coderCMD(11015, scmd.getBytes(this.player)));
			this.player.increaseQi(5);
			CCMD11031 ccmd031 = new CCMD11031();
			ccmd031.auto_deal(this.player, 5);
		}
		if(this.getValues().size() > 2)
		{
			this.skillId = this.getIntVaule(2);
			logger.info("ccmd11004" + this.player.getName() +"skillId: " + skillId);
			if(this.skillId > 0)this.player.getSkillutil().useSkillById(player, skillId);
		}
		touchPaiId = this.player.getTouchedPai();
		
		if(this.player.getTouchedPai() != -1)
			this.player.getShoupai().add(this.player.getTouchedPai());
		this.player.setTouchedPai(-1);
		
		if(!this.player.getShoupai().contains(paiId) )
		{
			paiId  =  this.offLine(paiId);
		}
		this.player.getDapai().add(new int[]{paiId,0});
		this.player.getShoupai().remove((Integer)paiId);
		r.setCurPaiId(paiId);
		SCMD11004   scmd =  new SCMD11004();
		scmd.setPaiId(paiId);
		scmd3.list.add(this.player.coderCMD(11004, scmd.getBytes(player)));
		this.player.getRoom().SendRoomBroadcast(3, scmd3.getBytes());
		this.player.setCountDapai(this.player.getCountDapai()+1);
		r.setCountDapai(r.getCountDapai() +1);
		this.player.getRoom().cancelTimeOut();
		
		//检查是否有玩家需要 碰吃胡杠, 有 启动timer 
		List<MgsPlayer> dealPlayer =  r.check_whoNeedDeal(paiId);
		logger.info("11004: check need player: dealPlayer:" + dealPlayer.size());
		if(dealPlayer.size() > 0)
		{
			r.setWaitDealPlayer(dealPlayer);
			r.start_dealTimer();
		}else
		{
			this.player.getSkillutil().checkSkillAfterDapai(this.player,null,null);
			r.changeCurPlayer();
		}
	}
	public void auto_deal(int paiId)
	{
		this.player.setLiangang(0);
		System.out.println("11004   auto_deal:" + paiId);
		this.player.getDapaiList().add(paiId);
		Room r = player.getRoom();
		SCMD11004   scmd =  new SCMD11004();
		scmd.setPaiId(paiId);
		r.SendRoomBroadcast(11004,scmd.getBytes(player));
		this.player.setCountDapai(this.player.getCountDapai()+1);
		r.setCountDapai(r.getCountDapai() +1);
		
		if(this.player.getTouchedPai() != -1)
			this.player.getShoupai().add(this.player.getTouchedPai());
		this.player.setTouchedPai(-1);
		this.player.getShoupai().remove((Integer)paiId);
		this.player.getDapai().add(new int[]{paiId,0});
		r.setCurPaiId(paiId);
		this.player.getRoom().cancelTimeOut();
		
		//检查是否有玩家需要 碰吃胡杠, 有 启动timer
		if(this.player.offline)
		{
			Log.log("");
		}
		List<MgsPlayer> dealPlayer =  r.check_whoNeedDeal(paiId);
		Log.log("11004: 检查是否有人要牌: dealPlayer:" + dealPlayer.size());
		if(dealPlayer.size() > 0)
		{
			r.setWaitDealPlayer(dealPlayer);
			r.start_dealTimer();
			//检查机器人
		}else
		{
			r.changeCurPlayer();
		}
	}
	public int offLine(int noPaiId)
	{
		this.player.getBusiness().saveUserOperate("ccmd11004" + this.player.getName() +"offLine: " + "haveno pai: all:"+this.player.getShoupai().toString() +"  no:" + noPaiId);
		logger.info("ccmd11004" + this.player.getName() +"offLine: " + "haveno pai: all:"+this.player.getShoupai().toString() +"  no:" + noPaiId);
		CCMD11111 c111 = new CCMD11111();
		c111.auto_deal(this.player, "打牌错误  "+this.player.getShoupai().toString() +"  " + noPaiId);
		// 打出手牌  ,手牌 == -1 随机打出一张牌
		int pai =  this.touchPaiId;
		if(pai == -1)
		{
			List<Integer> shoupai  = this.player.getShoupai();
			int index =  (int) (Math.random()* shoupai.size());
			pai  =  shoupai.get(index);
		}
		//掉线指令

		
		this.player.getRoom().addTuoGuanQueue(player);
			//让玩家断线
			this.player.closing();
			//清理cache中缓存的玩家
			MgsCache.getInstance().removeCache(this.player);
			
			return pai;
	}

}

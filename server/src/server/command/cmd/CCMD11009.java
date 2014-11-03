package server.command.cmd;

import java.util.List;
import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;

import server.command.Algorithm_Hu;
import server.command.CMD;
import server.mj.CheckTask;
import server.mj.MgsCache;
import server.mj.MgsPlayer;
import server.mj.Room;

import common.Log;
import common.MyArrays;

/**
 *   胡
 * @author xue
 *
 */
public class CCMD11009 extends  CMD{
	
	
	private static final Logger logger = Logger.getLogger(CCMD11009.class.getName());
	public static  final int  ZIMO  = 1;
	public static  final int  DIANPAO  = 2; 
	
	private Algorithm_Hu hu; 
	private int hu_type  = 0; 
	private MgsPlayer dianpao = null; 
	private int touchedPai;
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
		
		if(this.getValues()!= null && this.getValues().size()> 0)
		{
			this.skillId  = this.getIntVaule(0);
			logger.info("ccmd11009  "+ this.player.getName()+"   sillId:"+skillId);			
		}
		
		Room r = this.player.getRoom();
		if(r.isInTuoguan(player))
		{
			return;
		}
		
		this.touchedPai =  this.player.getTouchedPai();
		int hupai = 0;
		boolean zimo = false;
		if(this.touchedPai ==-1)  //放炮
		{
			if(r.getTimeFlag() != Room.TIME_DEALPAI)
			{
				return;
			}
			boolean flag = checkInDealArray(this.player);
			if(!flag)return;
			this.hu_type   = CCMD11009.DIANPAO; 
			this.dianpao  = r.getCurPlayer();
			//点炮 
			//抢杠 
			hupai = r.getCurPaiId();
		}else                        //自摸
		{
			this.hu_type  = CCMD11009.ZIMO;
			zimo = true;
			hupai  =  touchedPai;
		}
		logger.info("ccmd11009   hu:"+ this.player.getName()+"   hupaiid:"+hupai);		
//		hupai = 1;
		List<Integer>  arr_sp =  player.getShoupai();  
		arr_sp = MyArrays.sort(arr_sp);
		hu = new Algorithm_Hu();
		hu.hupaiId    = hupai;
		hu.isZimo  = zimo;
		boolean b  =  hu.testHu(arr_sp);
		if(b == false)
		{
			System.out.println("");
		}
		if(hu_type  ==  0) b  =false;             //错误
		if(b)
		{
			arr_sp.add(hupai);
			 player.setShoupai(arr_sp);
			r.waitDeal_back(player, "胡", this);
		}else
		{
			logger.info("hupai fail  cmd11009:"+ this.player.getAllPaiStr());
			CCMD11111 c111 = new CCMD11111();
			c111.auto_deal(this.player, "数据出错，本局已托管！ 胡   " + hupai );
			this.player.getRoom().deal_pass(this.player);			//托管的触发:  1. 断线  2前后台数据判断不一致  3.主动托管
			this.player.getRoom().addTuoGuanQueue(player);			//让玩家断线
//			this.player.closing();									//清理cache中缓存的玩家
//			MgsCache.getInstance().removeCache(this.player);
		}
	}
	public void testJipin(MgsPlayer p)
	{
		List<Integer> list  =  MyArrays.asList(1,9,11,19,21,29,31,32,33,34,35,36,37);
		List<Integer> list2  =  MyArrays.asList(1,1,1,2,3,4,5,6,7,8,9,9,9);
		List<Integer> list3  =  MyArrays.asList(31,31,31,32,32,32,33,33,33,34,34,34,1);
//12,13,14,16,18,37
		List<Integer> list4  =  MyArrays.asList(12,12,12,12,13,1);
		p.setShoupai(MyArrays.asList(1));
	}
	public void deal_back()
	{
		
		logger.info("cmd11009:"+ this.player.getAllPaiStr());
		
		if(this.skillId != -1)
		{
		}
		Room r = this.player.getRoom();
		if(r.getCountDapai() == 0 && r.getZhuang() == this.player)
			this.player.setTianhe(true);
			if(this.player.getCountDapai() ==0 &&r.getZhuang() != this.player  && r.isTiantingFlag())
				this.player.setDihe(true);
			if(r.getTimeFlag() == Room.TIME_QIANGGANGHU)
				this.player.setQianggang(true);
			
			//保存在player中, 胡牌类型
			if(this.hu_type == CCMD11009.DIANPAO)
			{
				this.player.getHuMap().put(CCMD11009.DIANPAO, this.dianpao);
				this.player.getRoom().checkTask.checkTaskAtterHuPai(dianpao, CheckTask.TASK_DIANPAO);				
			}
			r.lastHuPai  = this.hu.hupaiId;
			r.hupai(this.player, hu);
			
			
			
	}
	public int getHu_type() {
		return hu_type;
	}
	public void setHu_type(int hu_type) {
		this.hu_type = hu_type;
	}
	public MgsPlayer getDianpao() {
		return dianpao;
	}
	public void setDianpao(MgsPlayer dianpao) {
		this.dianpao = dianpao;
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

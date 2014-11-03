package server.command.cmd;

import java.util.List;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.Global;
import server.mj.MgsPlayer;
import business.CountDao;
import business.conut.MJUserCharge;
import business.conut.Sts_DayTaskCompletion;
import business.conut.Sts_LongTaskCompletion;
import business.conut.Sts_RewardProp;
import business.entity.MJ_DayTask;
import business.entity.MJ_User;

import common.Log;
import common.MyArrays;


/**
 * 领取任务奖励
 * @author xue
 */
public class CCMD11073 extends CMD{

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
		int taskId = this.getIntVaule(0);
		
		Log.log("ccmd11073: taskId   " +taskId );
		MJ_DayTask task = Global.tasks.get(taskId);
		if(task == null)
		{
			sendFailMsg();
			return;
		}
		MJ_User user = this.player.getBusiness().getPlayer();
		Map<Integer,List<Object>> map = user.getTaskJinduMap();
		if(!map.containsKey(taskId))
		{
			sendFailMsg();
			return;
		}
		List<Object> taskList = map.get(taskId);
		int status = (Integer) taskList.get(1);
		
		if(status == MJ_DayTask.TASK_COMPLETE)
		{
			CCMD11102 cmd102 = new CCMD11102();
			cmd102.setPlayer(player);
			
			int gold = task.getGold();
			int dia = task.getDia();
			String[] props = task.getProps().split(",");
			
			int beforeGold = user.getGold();
			int beforeDia = user.getDianQuan();
			int modGold = gold;
			int modDia = dia;
			
//			user.setGold(user.getGold() + gold);
//			user.setDianQuan(user.getDianQuan()+ dia);
			
			cmd102.mod_gold(gold);
			cmd102.mod_dianquan(dia);
			
			
			this.player.saveUserChargeRec(11073+"",beforeGold , modGold, user.getGold(), beforeDia, modDia, user.getDianQuan(), "完成任务 任务ID:"+taskId+"");
			
			for(int j = 0 ; j < props.length; j++)
			{
				if(props[j].equals("") || props.equals(" "))continue;
				int propId = Integer.parseInt(props[j]);
				int proNum = Integer.parseInt(props[j + 1]);
				//玩家增加道具
				cmd102.mod_prop(user, propId, proNum);
				mod_StsRewardProp(propId,proNum);
				j++;
			}
			//通知前台金币更改
			//充值任务状态字符串
			String taskP = (String) taskList.get(2);
			String[] parr = taskP.split(":");
			
			String w2 = parr[0];
			if(Integer.parseInt(parr[0]) < 0);
			w2 = (-Integer.parseInt(parr[0]))+"";
			String completeP = parr[0] +":"+ w2;
			taskList.set(1, MJ_DayTask.TASK_YILINGQU);
			taskList.set(2, completeP);
			
//			CCMD11101 cmd101 = new CCMD11101();
//			cmd101.setPlayer(this.player);
//			cmd101.auto_deal(MyArrays.asList(1,2,13), null);
			sendSuccess();
			
			sts_task(task);
			//任务完成，发放奖励
		}else
		{
			Log.info("CCMD11073     领奖失败!");
		}
		user.setTaskJinduMap(map);
		this.player.getBusiness().saveDataObject(user);
	}
	
	
	
	public void sendFailMsg()
	{
		MyArray arr = new MyArray();
		arr.push(false);
		MyByteArray bytebuf = new MyByteArray();
		bytebuf.write(arr);
		this.player.send(11073, bytebuf.getBuf());
	}
	//SUCCESS
	public void sendSuccess()
	{
		MyArray arr = new MyArray();
		arr.push(true);
		MyByteArray bytebuf = new MyByteArray();
		bytebuf.write(arr);
		this.player.send(11073, bytebuf.getBuf());
	}
	public void sts_task(MJ_DayTask task)
	{
		if(task.getTaskType() == 1)
		{
			CountDao cdao = new CountDao();
			Sts_DayTaskCompletion daytask = cdao.findTodayDayTask();
			daytask.overTaskcoompletion(task.getTaskId());
			cdao.saveSts_Object(daytask);
		}else
		{
			CountDao cdao = new CountDao();
			Sts_LongTaskCompletion longtask = cdao.findLongTask();
			longtask.overTaskcoompletion(task.getTaskId());
			cdao.saveSts_Object(longtask);
		}
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
}

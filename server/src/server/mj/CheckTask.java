package server.mj;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import common.Log;

import server.command.cmd.CCMD11072;

import business.CountDao;
import business.conut.Sts_DayTaskCompletion;
import business.conut.Sts_LongTaskCompletion;
import business.entity.MJ_DayTask;
import business.entity.MJ_User;

public class CheckTask {
	
	public static final int TASK_DIANPAO = 1;
	public static final int TASK_WIN = 2;
	public static final int TASK_MANGUAN = 3;
	public static final int TASK_ZIMO = 4;
	public static final int TASK_PANPLAY = 5;
	
	public static final int TASK_JUQING = 6;
	
	//杯赛胜利
	public static final int TASK_CUP = 7;
	//杯赛解锁
	public static final int TASK_CUP_UNLOCK_2 = 8;
	public static final int TASK_CUP_UNLOCK_3 = 9;
	public static final int TASK_CUP_UNLOCK_4 = 10;
	
	public static final boolean BETA = false;

	/**
	 * 对战结算，检查与对战结果相关的任务 
	 */
	public void checkTaskAtterHuPai(MgsPlayer p,int type)
	{
		if(BETA)return;
		MJ_User user = p.getBusiness().getPlayer();
		Map<Integer,List<Object>> map = user.getTaskJinduMap();
		Iterator<Integer> it = map.keySet().iterator();
		while(it.hasNext())
		{
			int taskId = it.next();
			List<Object> list = map.get(taskId);
			MJ_DayTask task = Global.tasks.get(taskId);
			int status = (Integer)list.get(1);
			if(status != MJ_DayTask.TASK_NOCOMPLETE)continue;
			switch(type)
			{
				case  TASK_DIANPAO:		if(task.getDianpaoPan() == 0)	 continue;  break;
				case  TASK_WIN: 		if(task.getWinPan() == 0)		 continue;  break; 
				case  TASK_MANGUAN: 	if(task.getManGuanPan() == 0)	 continue;  break;
				case  TASK_ZIMO: 	if(task.getZimoPan() == 0)			 continue;  break;
				case  TASK_PANPLAY: if(task.getPlayPan() == 0)			 continue;  break;
				case TASK_CUP:  		if(task.getTaskParam1() != 41)   continue;  break;
				case TASK_CUP_UNLOCK_2: 
					if(task.getTaskParam1() != 51)   continue;  break;
				case TASK_CUP_UNLOCK_3: 
					if(task.getTaskParam1() != 52)   continue;  break;
				case TASK_CUP_UNLOCK_4: 
					if(task.getTaskParam1() != 53)   continue;  break;
				default: 
					return;
			}
			if(type == TASK_MANGUAN)
			{
				int fan = p.getFan().getTotalFan();
				boolean b = checkManguan(fan,task.getManGuanPan());
				if(!b)continue;
				if(taskId == 32)
				{
					Log.info("");
				}
			}
			//找到任务。 改变任务状态
			String[] progress = ((String)list.get(2)).split(":");
			int pan = Integer.parseInt(progress[1]) + 1;
			if(pan >= Integer.parseInt(progress[0]))
			{
				//任务完成
				list.set(1, MJ_DayTask.TASK_COMPLETE);
				list.set(2, progress[0] +":" + progress[0]);
				msg_Progress(p,list);
				user.setTaskJinduMap(map);
				
				mod_stsTask(task);
			}else
			{
				//修改状态
				String pstr = progress[0] +":" + pan;
				list.set(2, pstr);
				msg_Progress(p,list);
				user.setTaskJinduMap(map);
			}
		}
		user.setTaskJinduMap(map);
	}
	public  boolean checkManguan(int fan, int type)
	{
		switch(type)
		{
			case  1:
				
					if(fan >= 10 && fan <20) return true;
			case  2: 
					if(fan >= 20 && fan <30) return true;
			case  3: 
					if(fan >= 30) return true;
		}
		return false;
	}
	
	/**
	 * 任务状态改变  任务完成， 任务状态改变
	 */
	public void msg_Progress(MgsPlayer p,List<Object> value)
	{
		CCMD11072 cmd072 = new CCMD11072();
		cmd072.auto_deal(p, value);
		Log.info("CCMD11072:" +p.getName() +"  完成任务:" +value.get(0));
	}
	
	public void checkTaskJuqing(MgsPlayer p,int type,int zhangjie,int juqing)
	{
		if(type != TASK_JUQING)return;
		if(BETA)return;
		MJ_User user = p.getBusiness().getPlayer();
		Map<Integer,List<Object>> map = user.getTaskJinduMap();
		Iterator<Integer> it = map.keySet().iterator();
		while(it.hasNext())
		{
			int taskId = it.next();
			List<Object> list = map.get(taskId);
			MJ_DayTask task = Global.tasks.get(taskId);
			int status = (Integer)list.get(1);
			if(status != MJ_DayTask.TASK_NOCOMPLETE)continue;
			
			if(task.getFinishJuqing() == 0)continue;	
			
			//检查是否满足剧情完成条件
			
			int taskJuqing = task.getFinishJuqing()/100;
			int taskZhuangjie = task.getFinishJuqing()%100;
			
			if(  taskJuqing  != juqing)continue;
			if(taskZhuangjie  < zhangjie)continue;
			
			//找到任务。 改变任务状态
			String[] progress = ((String)list.get(2)).split(":");
			int pan = Integer.parseInt(progress[1]) + 1;
			if(pan >= Integer.parseInt(progress[0]))
			{
				//任务完成
				list.set(1, MJ_DayTask.TASK_COMPLETE);
				list.set(2, progress[0] +":" + progress[0]);
				msg_Progress(p,list);
				user.setTaskJinduMap(map);
				
				mod_stsTask(task);
				
				
			}else
			{
				//修改状态
				String pstr = progress[0] +":" + pan;
				list.set(2, pstr);
				msg_Progress(p,list);
				user.setTaskJinduMap(map);
			}
		}
		user.setTaskJinduMap(map);
	}
	public void mod_stsTask(MJ_DayTask task)
	{
		CountDao cdao = new CountDao();
		if(task.getTaskType() == 1)
		{
			//获得今天的任务进度，修改并保存
			Sts_DayTaskCompletion dayTask = cdao.findTodayDayTask();
			dayTask.modTaskcoompletion(task.getTaskId());
			cdao.saveSts_Object(dayTask);
			
		}else if(task.getTaskType() == 2)
		{
			Sts_LongTaskCompletion longTask = cdao.findLongTask();
			longTask.modTaskcoompletion(task.getTaskId());
			cdao.saveSts_Object(longTask);
		}
		
	}

}

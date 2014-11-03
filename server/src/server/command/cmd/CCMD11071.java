package server.command.cmd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
import business.entity.MJ_DayTask;
import business.entity.MJ_User;

public class CCMD11071 extends CMD{

	private MJ_User user =  null;
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
		
		user = this.player.getBusiness().getPlayer();
		restTaskStatusMsg();
	}
	public void auto_deal()
	{
		user = this.player.getBusiness().getPlayer();
		restTaskStatusMsg();
	}
	/**
	 * 登陆时执行
	 */
	public void restTaskStatusMsg()
	{
		if(user.getTaskJinDu().equals(""))
			initTaskProgress();
		cleanTaskList();
//		String now = ServerTimer.getDay();
//		String lastLogin = user.getLastLogin().substring(0, 10);
		if(user.firstLogin_today) 		//当天第一次登陆 	//重置每日任务
		{
			resetRiChangTask();        
			user.firstLogin_today = false;
		}
		
		getTaskStatus(); 				//发送任务状态
	}
	/**
	 * 发送当前任务状态消息
	 */
	public void getTaskStatus()
	{
		
		Map<Integer,List<Object>> map = user.getTaskJinduMap();
		Iterator<Integer> it = map.keySet().iterator();
		MyByteArray bytebuf = new MyByteArray();
		MyArray arr1 = new MyArray();
		while(it.hasNext())
		{
			int taskId = it.next();
			List<Object> tList = map.get(taskId);
			
			List<Object> list = new ArrayList<Object>();
			list.add(taskId);
			list.add(tList.get(1));
			String[] tprogress = ((String)tList.get(2)).split(":");
			list.add(Integer.parseInt(tprogress[0]));
			list.add(Integer.parseInt(tprogress[1]));
			Log.log(list.toString());
			arr1.push(list);
			
			Log.log("task:----->" + list);
		}
		bytebuf.write(arr1);
		this.player.send(11071, bytebuf.getBuf());
	}
	/**
	 * 初始化玩家任务状态
	 */
	public void initTaskProgress()
	{
		MJ_User user = this.player.getBusiness().getPlayer();
		Map<Integer,List<Object>> taskMap = new LinkedHashMap<Integer, List<Object>>();
		Iterator<MJ_DayTask> it	= Global.tasks.values().iterator();
		while(it.hasNext())
		{
			MJ_DayTask t = it.next();
			int status = MJ_DayTask.TASK_NOCOMPLETE;
			
			if(t.getFirstLogin() ==1)
				status = MJ_DayTask.TASK_COMPLETE;
			
			List<Object> tlist = new ArrayList<Object>();
			tlist.add(t.getTaskId());
			tlist.add(status);
			tlist.add(t.getInitProgress());
			taskMap.put(t.getTaskId(),tlist );
		}
		user.setTaskJinduMap(taskMap);
		this.player.getBusiness().saveDataObject(user);
	}
	/**
	 * 重置每日任务状态
	 */
	public void resetRiChangTask()
	{
		Map<Integer,List<Object>> map = user.getTaskJinduMap();
		Iterator<Integer> it = map.keySet().iterator();
		while(it.hasNext())
		{
			int taskId = it.next();
			List<Object> tmplist = map.get(taskId);
			
			MJ_DayTask task = Global.tasks.get(taskId);
			if(task.getTaskType() == MJ_DayTask.TASK_RICHANG )
			{
				//登陆奖励 显示已完成 //重置任务状态
				if(task.getFirstLogin() == 1)
				{
					tmplist.set(1, MJ_DayTask.TASK_COMPLETE);
					CheckTask check1 = new CheckTask();
					check1.mod_stsTask(task);					
				}
				else
					tmplist.set(1, MJ_DayTask.TASK_NOCOMPLETE);
				
				tmplist.set(2, task.getInitProgress());
			}
		}
		user.setOnLineTime(0);
		user.setTaskJinduMap(map);
		this.player.getBusiness().saveDataObject(user);
	}
	
	/**
	 * 清理任务列表， 不再的任务就清理掉
	 */
	public void cleanTaskList()
	{
		Map<Integer,List<Object>> map = user.getTaskJinduMap();
		Map<Integer,MJ_DayTask> tasks = Global.tasks;
		
		//1 加入新的任务
		Iterator<Integer> t1 = tasks.keySet().iterator();
		while(t1.hasNext())
		{
			int key = t1.next();
			if(!map.containsKey(key))
			{
				//新任务. 加入新的初始化任务
				MJ_DayTask t =  tasks.get(key);
				addNewTaskStatus(t,map);
			}
		}
		//删除没有的任务
		Iterator<Integer> t2 = map.keySet().iterator();
		List<Integer> removeList = new ArrayList<Integer>();
		while(t2.hasNext())
		{
			int taskId = t2.next();
			if(!tasks.containsKey(taskId))
				removeList.add(taskId);
		}
		for(int i = 0 ; i < removeList.size();i++)
		{
			int taskId = removeList.get(i);
			map.remove(taskId);
		}
		user.setTaskJinduMap(map);
	}
	
	/**
	 * 加入新的任务
	 */
	public void addNewTaskStatus(MJ_DayTask t,Map<Integer,List<Object>> map )
	{
		int status = MJ_DayTask.TASK_NOCOMPLETE;
		if(t.getFirstLogin() ==1)
			status = MJ_DayTask.TASK_COMPLETE;
		
		List<Object> tlist = new ArrayList<Object>();
		tlist.add(t.getTaskId());
		tlist.add(status);
		tlist.add(t.getInitProgress());
		map.put(t.getTaskId() , tlist);
	}

}

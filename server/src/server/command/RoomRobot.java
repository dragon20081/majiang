package server.command;

import org.jboss.netty.buffer.ChannelBuffer;

import business.Business;
import business.entity.MJ_User;
import server.mj.Global;
import server.mj.MgsPlayer;

public class RoomRobot {
	
	//生成mgsplayer
	//生成 mj_user 及其数据
    //
	// 摸牌  ，打牌, 碰，吃，杠，胡，都自动吃
	public void getRobot()
	{
		MgsPlayer robort  =  new MgsPlayer(true);
		MJ_User user = new MJ_User();
		//初始化机器人数据
		initUser(user);
		robort.getBusiness().setPlayer(user);
	}
	
	/**
	 * 初始化机器人数据
	 * @param user
	 */
	public void initUser(MJ_User user)
	{
		user.setEquipSkill("");
		
		int randGold  = (int) (Math.random() *3000 + 1423);
		user.setGold(randGold);
		user.setImage(101);
		int randLevel  = (int) (Math.random()* 10 +5);
		user.setLevel(randLevel);
		int randRate =  (int) (Math.random() *20 + 8);
		user.setTaopaoRate(randRate);
		user.setNick("");
	}
	/**
	 * 随机产生机器人名字
	 */
	public void getRobotRandNick()
	{
	}
	public void initRobot()
	{
		for(int i  = 0 ; i < 100;i++)
		{
			MgsPlayer robot  = new MgsPlayer(true);
			String rName =  "robot" +(i+1) ;
			robot.getBusiness().robotLogin(rName);
			Global.robot.add(robot);
		}
	}
}

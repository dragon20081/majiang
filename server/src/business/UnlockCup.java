package business;

import java.util.List;

import business.entity.MJ_User;

public class UnlockCup {

	
	public boolean checkCupUnlockCondition(MJ_User user,int type)
	{
		
		List<Integer> list = user.getCupScore();
		int zhanji = 0;
		switch(type)
		{
		    case  1:
		    		return true;
		    case  2:
		    		zhanji = list.get(type -1);
		    		if(zhanji >= 5) return true;  //原来  5
		    		break;
		    case  3: 
		    		if(user.getLevel() >= 3) return true;
		    		break;
		    case  4: 
		    		zhanji = list.get(type -1);
		    		if(user.getLevel() >= 5 &&zhanji >= 10)
		    			return true;
		    		break;
		    case  5: break;
		}
		return false;
	}
	/**
	 *  
	 * 
	 */
}

//名字，介绍，规则(十位表示几人，个位表示几场,十位为9代表复合型)，消耗，难度,条件,适合人群,参赛人数	
//Arr_info[1]	= ["社区温暖杯","由附近社区大妈组织的麻将交流赛，想成为中老年妇女之友吗？",
//				23,1,1,"无",[1003,1003,1003,1003],8];
//
//Arr_info[2]	= ["同好竞技杯","由本市热爱麻将的团体组织的比赛，吸引了许多业余选手。",
//				43,1,1,"【社区温暖杯】优胜5次",[100,101,102,103,104,105,1001,1002,1003,1003,1003],16];
//
//Arr_info[3]	= ["市级麻将大赛","官方组织的全市麻将比赛，奖金丰厚，参加人数众多。" +
//				"许多以全国大赛为目标的职业选手也用它来热身。",
//				43,2,2,"玩家等级3以上",[100,101,102,103,104,105,1001,1002],16];
//
//Arr_info[4]	= ["省级积分赛","全国大赛的选拔赛，只有参加积分赛并获得足够的分数" +
//				"才能参加全国大赛，除了少数爱好者和碰运气的人，大多都是职业选手参加！",
//				94,3,2,"玩家等级5以上，\n【市级麻将大赛】优胜10次\n等游戏下个版本更新……（=｀〜´=）",
//				[100,101,102,103,104,105,1001,1002],32];

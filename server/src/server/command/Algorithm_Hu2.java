package server.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Algorithm_Hu2 {

	
	public int[] deal(List<Integer> list)
	{
//		 * 初始化牌堆 // 1 -  9      条子	//11 - 19      筒子		//21 - 29      万字//31  -37     字牌	// 34种
		List<Integer> tiao  = new ArrayList<Integer>();
		List<Integer> tong  = new ArrayList<Integer>();
		List<Integer> wan   = new ArrayList<Integer>();
		List<Integer> zhi   = new ArrayList<Integer>();
		
		for(int i = 0 ; i < list.size();i++)
		{
			if(list.get(i)< 10)
			{
				tiao.add(list.get(i));
			}else if(list.get(i)< 20)
			{
				tong.add(list.get(i));
			}else if(list.get(i)< 30)
			{
				wan.add(list.get(i));
			}else 
			{
				zhi.add(list.get(i));
			}
		}
		List<List<Integer>> needDeal = new ArrayList<List<Integer>>();
		
	   if(tiao.size()%3 == 0)
		 {
		   if(!checkPattern(tiao,""))return null;
		 }
	   if(tong.size()%3 == 0)
		 {
		   if(!checkPattern(tiao,""))return null;
		 }
	   if(wan.size()%3 == 0)
		 {
		   if(!checkPattern(tiao,""))return null;
		 }
	   if(zhi.size()%3 == 0)
		 {
		   if(!checkPattern(tiao,"zhi"))return null;
		 }
	   if(tiao.size()%3 != 0)needDeal.add(tiao);
		if(tong.size() %3 != 0)needDeal.add(tong);
		if(wan.size()  %3 != 0)needDeal.add(wan);
		if(zhi.size()  %3 != 0)needDeal.add(zhi);
		
		if(needDeal.size() == 1)return getYushu_one(needDeal.get(0));
		if(needDeal.size() == 2)return getHuPaiIds(needDeal.get(0),needDeal.get(1));
		return null;
	}
	private  int[] getHuPaiIds(List<Integer> list1,List<Integer> list2 )
	{
		int yushu1  =  list1.size() %3;int yushu2  =  list2.size() %3;
		if(yushu1 == 1){ return null;}
		if(yushu2 == 1){ return null;}
		//{ 3 3 2   3 3 2} 找出完整的那个  1个完整 2就是顺子  ， 2个都完整 对子
		
		return null;
	}
	private int[] getYushu_one(List<Integer> list)
	{
		int i,j;
		//默认list是已经排好顺序的
		List<List<Integer>> groupList = new ArrayList<List<Integer>>();
		//分组
		while(list.size() > 0)
		{
			List<Integer> tmplist  = new ArrayList<Integer>();
			int id  = list.get(0);list.remove(0);tmplist.add(id);
			if(list.contains(id))list.remove((Integer)id);tmplist.add(id);
			if(list.contains(id))list.remove((Integer)id);tmplist.add(id);
			if(list.contains(id))list.remove((Integer)id);tmplist.add(id);
			groupList.add(tmplist);
		}
		//遍历groupList  4个成刻， 优先顺子 ,失败退回拆对或者刻
		boolean hu  =false;
		int arrlen  = groupList.size()%3 == 0 ?groupList.size()/3:groupList.size()/3+1;
		List<int[]> arrList  = new ArrayList<int[]>();
		int[][] arr  = new int[arrlen][3];
		for(i= 0 ; i < groupList.size();i++)
		{
			List<Integer> tmp  = groupList.get(i);
			if(tmp.size()== 4)
			{
				arrList.add(new int[]{tmp.get(0),tmp.get(0),tmp.get(0)});
				tmp.remove(0);tmp.remove(0);tmp.remove(0);
				continue;
			}
			if(tmp.size() == 1) // 找出单牌,向后遍历
			{
				int id1  = tmp.get(0);
				int[]  arr2 =  new int[3];arr2[0]  = id1;
				int id2  =   i+1 <groupList.size()? groupList.get(i+1).get(0):0;
				int id3  =   i+2 <groupList.size()? groupList.get(i+2).get(0):0;
				arr2[1]  =   id2;  arr2[2] = id3;
				if(arr2[2]-1 == arr2[1]&&arr2[1]-1 ==arr2[0])
				{
					arrList.add(arr2);continue;
				}else
				{
					//回退 ，拆对子或者刻
					if(i-1 < 0)return null;//失败 不能胡牌
					id3 = id2; id2 = id1; id1 = groupList.get(i-1).get(0);
					if(id1+1 == id2 && id2 +1 == id3)
						{
						groupList.get(i-1).remove(0);
						arrList.add(new int[]{id1,id2,id3});
						continue;}
						else 
							{return null;}
				}
			}else if(i < groupList.size() -1)
			{
				continue;
			}
		}
		
		while(groupList.size() > 0)
		{
		}
		
		return null;
	}
	
	private void exam(List<Integer> list)
	{
		//单牌数列
		List<Integer>  danpai = null;
		List<List<Integer>>  fupai  = null;
		boolean check = false;
		check  = checkDanpai(danpai,fupai);
		if(!check)
		{
			//取得前一张牌  再次检查
			int preid =  getPreDanpai(danpai.get(0), fupai);
			if(preid == -1)return;
			check  = checkDanpai(danpai,fupai);
			if(!check)return; //不能胡牌
			//检查复牌是否是 3 3 2 是否是刻 并且将
			//是就可以胡牌
			boolean c_fupai = chekfupai(fupai);
			if(!c_fupai)return;
		}
		//通过可以胡牌
	}
	private int getPreDanpai(int pai,List<List<Integer>> list)
	{
		int prepai = pai -1;
		for(int i = 0; i < list.size();i++)
		{
			if(prepai == list.get(i).get(0))
			{
				list.get(i).remove(0);
				return prepai;
			}
		}
		return -1;
	}
	private boolean chekfupai(List<List<Integer>> list)
	{
		while(list.size()>0)
		{
			if(list.get(0).size()==3 || list.get(0).size()==2)
				list.remove(0);
			else{return false;}
		}
		return true; 
	}
	private boolean checkDanpai(List<Integer> list,List<List<Integer>> fulist)
	{
		if(list.size() ==0)return true;
		List<Integer> tmplist  =new ArrayList<Integer>();
		tmplist.addAll(list);
		while(tmplist.size() > 0)
		{
			int id1 = tmplist.get(0);
			int id2 = tmplist.get(2);
			int id3 = tmplist.get(3);
			if(id1 +1 == id2 && id2 +1 == id3)
			{
				tmplist.remove(0);tmplist.remove(1);tmplist.remove(2);continue;
			}
			return false; //失败
		}
		return true;
	}
	private List<List<Integer>> createGroup(List<Integer> list)
	{
		//单牌数列  复牌数列 全数列
		Integer[] a = list.toArray(new Integer[]{});
		Arrays.sort(a);list  = Arrays.asList(a);
		List<List<Integer>> groupList = new ArrayList<List<Integer>>();
		while(list.size() > 0)
		{
			List<Integer> tmplist  = new ArrayList<Integer>();
			int id  = list.get(0);list.remove(0);tmplist.add(id);
			if(list.contains(id))list.remove((Integer)id);tmplist.add(id);
			if(list.contains(id))list.remove((Integer)id);tmplist.add(id);
			if(list.contains(id))list.remove((Integer)id);tmplist.add(id);
			groupList.add(tmplist);
		}
		return groupList;
	}
	
	private boolean checkPattern(List<Integer> list,String flag)
	{
		//33一对的  单个的肯定是顺子，  多个的肯定是刻
		while(list.size() > 0)
		{
			int paiId  =  list.get(0);list.remove(0);
			List<Integer> tmpList = new ArrayList<Integer>();
			tmpList.addAll(list);
			boolean ke =  checkKe(paiId,tmpList);
			if(ke)
			{
				list.remove((Integer)paiId);list.remove((Integer)paiId);
			}else
			{
				if(flag =="zhi")return false;
				int[] shunzi = checkShun(paiId,tmpList);
				if(shunzi == null)
				{
					return false;
				}else
				{
					list.remove((Integer)(paiId+1));list.remove((Integer)(paiId+2));
				}
			}
		}
		return true;
		
	}
	/**
	 *  判断是否可以做刻 
	 */
	private boolean checkKe(int paiid,List<Integer> list)
	{
		//需要还有两张相同的
		if(!list.contains(paiid))return false;
		list.remove((Integer)paiid);
		if(!list.contains(paiid))return false;
		return true;
	}
	/**
	 *  判断是否可以做顺子
	 */
	private int[] checkShun(int paiid,List<Integer> list)
	{
		if(!list.contains((Integer)(paiid+1)))return null;
		if(!list.contains((Integer)(paiid+2)))return null;
		return  new int[]{paiid+1,paiid+2};
	}
}

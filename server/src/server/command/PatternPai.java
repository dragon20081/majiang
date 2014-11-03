package server.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import common.MyArrays;

import server.mj.MgsPlayer;

public class PatternPai {

	public static final int DA_PAI  = 1;
	public static final int MO_PAI  = 2;
	
	public boolean checkPeng(MgsPlayer p ,int paiId)
	{
		//只需要检查手牌
		List<Integer> shoupai  =  p.getShoupai();
		int count  = 0;
		for(int i = 0 ; i < shoupai.size();i++)
		{
			if(shoupai.get(i) ==  paiId )
				count++;
		}
		if(count >=2)return true;
		return false;
	}
	/**
	 * type == 1 打牌   type == 2 摸牌
	 *   分摸牌和打牌  1.摸牌可以杠暗牌和明牌   2.打牌 只能杠 暗牌
	 * @return
	 */
	public boolean checkGang(MgsPlayer p, int paiId,int type)
	{
		boolean flag  = false;
		if(type == 1) flag  = checkGangDapai(p,paiId);
		if(type == 2) flag  = checkGangMopai(p,paiId);
		return flag;
	}
	/**
	 * 检查是否是暗杠
	 */
	public boolean  checkAnGang(MgsPlayer p, int pai)
	{
		// 1 手中 有4张pai  , 2 ,手中有3张pai , touchPai == pai
		int count  = 0;
		for(Integer tmp  : p.getShoupai())
		{
			if(pai == tmp)count++;
		}
		if(count == 3  && pai == p.getTouchedPai())return true;
		if(count == 4)return  true;
		return false;
	}
	 // 摸牌， 检查 明牌和暗牌
	private boolean checkGangMopai(MgsPlayer p, int paiId)
	{
		boolean  checkAnpai   = checkGangDapai(p,paiId);
		boolean  checkPengpai =  false; 
		//检查碰牌
		List<Integer> list =  p.getPeng();
		if(list.contains((Integer)paiId))
			checkPengpai =  true;
		if(checkAnpai || checkPengpai)return true;
		return false;
	}
	//打牌， 只检查暗牌
	private boolean checkGangDapai(MgsPlayer p,int paiId)
	{
		List<Integer> shoupai  =  p.getShoupai();
		int count  = 0;
		for(int i = 0 ; i < shoupai.size();i++)
		{
			if(shoupai.get(i) ==  paiId )
				count++;
		}
		if(count  == 3)return true;
		return false;
	}

	/**
	 *  吃牌
	 * @param p
	 * @param pai1  选中手牌1
	 * @param pai2 选中手牌2
	 * @param pai3 上家打牌
	 * @return
	 */
	public boolean checkChi(MgsPlayer p, int pai1, int pai2,int pai3,int curPai)
	{
		List<Integer> list =  p.getShoupai();
		if( pai1 != curPai&&!list.contains(pai1) )return false;
		if( pai2 != curPai&&!list.contains(pai2) )return false;
		if( pai3 != curPai&&!list.contains(pai3) )return false;
		int[] arr  = new int[]{pai1,pai2,pai3}; 
		Arrays.sort(arr);
		if(arr[0] + 1 == arr[1] && arr[1] + 1 == arr[2])return true;
		return false;
	}

	public int[] calculateHu(MgsPlayer p)
	{
		List<Integer> list  = p.getShoupai();  // 13  10  7   4
		//创建数组
		int len  =  (list.size() -1)/ 3 +1; 
		int[][] pai = new int[len][];
		return null;
	}
	
	//////////////////////////////////////////
	/**
	 *  是否可以碰
	 */
	public boolean checkTOBEpeng(int pai, List<Integer> arr)
	{
		int canPeng	= 0;
		for(int i=0; i<arr.size(); i++)
		{
			if(arr.get(i) == pai)
			{
				if(++canPeng == 2)
				{
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 所有可以吃的结果 
	 * @return
	 */
	public boolean checkTOBEshun(int pai,	List<Integer> newArr)
	{
		List<List<Integer>> canChi = new ArrayList<List<Integer>>();
		int tmp	 = pai % 10;
		if(pai > 30)	tmp	= 0;	//字牌
		if(tmp > 2)
		{
			//xxy
			if(newArr.indexOf(pai-1) >= 0)
			{
				if(newArr.indexOf(pai-2) >= 0)
				{
					canChi.add(MyArrays.asList(pai-2,pai-1,pai));
				}
			}
		}
		if(tmp > 1 && tmp < 9)
		{
			//xyx
			if(newArr.indexOf(pai-1) >= 0)
			{
				if(newArr.indexOf(pai+1) >= 0)
				{
					canChi.add(MyArrays.asList(pai-1,pai,pai+1));
				}
			}
		}
		if(tmp < 8 && tmp > 0)
		{
			//yxx
			if(newArr.indexOf(pai+1) >= 0)
			{
				if(newArr.indexOf(pai+2) >= 0)
				{
					canChi.add(MyArrays.asList(pai,pai+1,pai+2));
				}
			}
		}
		if(canChi.size() > 0)
		{
			return true;
		}
		return false;
	}
	
	
	//////////////////////////////////////////
	public boolean checkAll()
	{
		return true;
	}
}

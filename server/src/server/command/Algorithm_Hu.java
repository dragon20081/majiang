package server.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.MyArrays;

public class Algorithm_Hu {
	/**
	 *  是否可以碰
	 */
	private boolean checkTOBEpeng(int pai, List<Integer> arr)
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
	private List<List<Integer>> checkTOBEshun(int pai,	List<Integer> newArr)
	{
		List<List<Integer>> canChi = new ArrayList<List<Integer>>();
		int tmp	 = pai % 10;
		if(pai > 30)	tmp	= 0;	//字牌
//		if(tmp > 2)
//		{
//			//xxy
//			if(newArr.indexOf(pai-1) >= 0)
//			{
//				if(newArr.indexOf(pai-2) >= 0)
//				{
//					canChi.add(MyArrays.asList(pai-2,pai-1,pai));
//				}
//			}
//		}
//		if(tmp > 1 && tmp < 9)
//		{
//			//xyx
//			if(newArr.indexOf(pai-1) >= 0)
//			{
//				if(newArr.indexOf(pai+1) >= 0)
//				{
//					canChi.add(MyArrays.asList(pai-1,pai,pai+1));
//				}
//			}
//		}
		if(tmp >0 &&tmp < 8)
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
			return canChi;
		}
		return null;
	}
	
	

	public List<List<Integer>> Dic;
	private List<Integer> Arr_now;
	public  int  hupaiId  = 0;
	public  boolean  isZimo =false;
	public  Map<Integer,GroupShoupai>  shoupai;
	private int shoupaiIndex = 1;
	
	private List<List<Integer>>Arr_door = new ArrayList<List<Integer>>();	//门前的牌  明牌，吃碰 杠
	
	public void testGetHu()
	{
		Dic	= new ArrayList<List<Integer>>();
	    Arr_now = MyArrays.asList(1,1,1,2,3,4,5,6,7,8);  
		this.checkCanhu(this.Arr_now,null,null,null);
	}
	
	private int count = 0;
	public boolean isAnqidui = false;
	public  static void main(String[] args)
	{
		Algorithm_Hu hu  = new Algorithm_Hu();
		hu.Dic	= new ArrayList<List<Integer>>();
		hu.Arr_now =  MyArrays.asList(1,1,1,2,2,2,3,3,3,4,4,4,5);
		hu.checkCanhu(hu.Arr_now,null,null,null);
	}
	public boolean testHu(List<Integer> list)
	{ 
		//对list 排序 
		
		shoupai =  new HashMap<Integer, GroupShoupai>();
		Dic	= new ArrayList<List<Integer>>();
		Arr_now  =list;// MyArrays.sort(list);
		isAnqidui  =  check_anqidui(Arr_now);
		if(isAnqidui) return true;
		checkCanhu(Arr_now,null,null,null);
		if(shoupai.size() > 0)return true;
		return false;
	}
	private boolean check_13yao(List<Integer> list)
	{
		if(list.size() != 13)return false;
		List<Integer> allList = MyArrays.asList(1,9,11,19,21,29,31,32,33,34,35,36,37);
		 List<Integer>  allPai  = MyArrays.subList(0, list.size(), list);
		 while(allList.size() > 0){
			 int pai = allList.get(0);
			 allList.remove(0);
			 if(!allPai.contains(pai))
				 return false;
		 }
		 
		return false;
	}
	private boolean check_anqidui(List<Integer> list)
	{
		if(list.size() != 13)return false;
		 List<Integer>  allPai  = MyArrays.subList(0, list.size(), list);
		 allPai.add(this.hupaiId);
		 while(allPai.size() > 0)
		 {
			 int pai = allPai.get(0);
			 allPai.remove(0);
			 if(!allPai.contains(pai))return false;
			 allPai.remove((Integer)pai);
		 }
		 List<Integer> dan_out = new ArrayList<Integer>();
		 dan_out.add(hupaiId);
		 shoupai.put(shoupaiIndex++, new GroupShoupai(hupaiId,72,new ArrayList<List<Integer>>(), new ArrayList<Integer>(),dan_out,MyArrays.asList(hupaiId),isZimo));
		 return true;
	}
	/**
	 * @param _arr      手牌
	 * @param shun_ke  3张牌
	 * @param shuang   2张牌
	 * @param dan      1张牌
	 */
	private  void checkCanhu(List<Integer> _arr,List<List<Integer>>shun_ke,List<Integer>shuang, List<Integer> dan)
	{
		if(shun_ke == null)	shun_ke  = new ArrayList<List<Integer>>();
		if(shuang == null)	shuang   = new ArrayList<Integer>();
		if(dan == null)		dan  	 = new ArrayList<Integer>();
		List<List<Integer>>shun_ke_out ;
		List<Integer>shuang_out;
		List<Integer> dan_out;
		
		shun_ke_out 	= copyList2(shun_ke);
		shuang_out      = copyList1(shuang);
		dan_out         = copyList1(dan);
		
		if(_arr.size() == 0)
		{
//			System.out.println("arr 0");
			if(shuang_out.size() == 2 && dan_out.size()	== 0)
			{
//				System.out.println("arr 1");
				List<Integer>  tmp1  = MyArrays.asList(33322,shuang_out.get(0),shuang_out.get(1));
				addToDic(tmp1);
				if(this.hupaiId == shuang_out.get(0))
					shoupai.put(shoupaiIndex++, new GroupShoupai(hupaiId,33322,shun_ke_out, shuang_out, dan_out,MyArrays.asList(shuang_out.get(0),shuang_out.get(0)),isZimo));
				if(this.hupaiId == shuang_out.get(1))
					shoupai.put(shoupaiIndex++, new GroupShoupai(hupaiId,33322,shun_ke_out, shuang_out, dan_out,MyArrays.asList(shuang_out.get(1),shuang_out.get(1)),isZimo));
			}
			else if(shuang_out.size() == 0 && dan_out.size()	== 1)
			{
				List<Integer>  tmp1  = MyArrays.asList(33331,dan_out.get(0));
				addToDic(tmp1);
				if(this.hupaiId == dan_out.get(0))
					shoupai.put(shoupaiIndex++, new GroupShoupai(hupaiId,33331,shun_ke_out, shuang_out, dan_out,MyArrays.asList(dan_out.get(0)),isZimo));
			}
			else if(shuang_out.size() == 1 && dan_out.size() == 2)
			{
				int p1	= (Integer) dan_out.get(0);
				int p2	= (Integer) dan_out.get(1);
				if(p1 == p2 || p1 >30  ||p2 > 30)	return;
				if((int)(p1/10) != (int)(p2/10)) return;
				if(p1 > p2)
				{
					int tmpp	= p2;
					p2	= p1;
					p1	= tmpp;
				}
				List<Integer> arrHu  =new ArrayList<Integer>();
				if(p1 == p2-1)
				{
					if(p1 % 10 > 1)	arrHu.add(p1-1);
					if(p2 % 10 < 9) arrHu.add(p2+1);
				}
				else if(p1 == p2-2)
				{
					arrHu.clear(); arrHu.add(p1+1);
				}
				if(arrHu.size() > 0)
				{
					arrHu.add(0, 333211);
					addToDic(arrHu);
					for(int pai : arrHu)
					{
//						System.out.println(pai);
						if(this.hupaiId == pai)
							shoupai.put(shoupaiIndex++, new GroupShoupai(hupaiId,333211,shun_ke_out, shuang_out, dan_out,MyArrays.asList(p1,p2),isZimo));
					}
				}
			}
			return;
		}
		List<Integer> arr ;
		arr =  MyArrays.subList(0, _arr.size(), _arr);
		
//		while(arr.size() > 0) 
//		{
			int pai	= (Integer) arr.get(0);
			//顺子
			
			List<Integer> arr2;
			arr2  =   MyArrays.subList(1, arr.size(), arr);
			List<List<Integer>>  arrShun	= this.checkTOBEshun(pai,arr2);
			if(arrShun != null)
			{
				arr2 = MyArrays.subList(0, arr.size(), arr);
				shun_ke_out.add(arrShun.get(0));
				arr2.remove((Integer)( arrShun.get(0).get(0))); 
				arr2.remove((Integer)(arrShun.get(0).get(1)));
				arr2.remove((Integer)(arrShun.get(0).get(2)));
				
				if((arr2.size() +shun_ke_out.size() *3 + shuang_out.size() *2 +dan_out.size())  < 13 )
				{
//					System.out.println("error");
				}
				this.checkCanhu(arr2,shun_ke_out,shuang_out,dan_out);
			}
			
			//刻子
			 arr2  =  MyArrays.subList(1, arr.size(), arr);
			if(checkTOBEpeng(pai,arr2) == true)
			{
				shun_ke_out 	= copyList2(shun_ke);
				shuang_out      = copyList1(shuang);
				dan_out         = copyList1(dan);
				
				shun_ke_out.add(MyArrays.asList(pai,pai,pai));
				arr2.remove((Integer)(pai));
				arr2.remove((Integer)(pai));
				
				this.checkCanhu(arr2,shun_ke_out,shuang_out,dan_out);
			}
			//对子
			arr2  =  MyArrays.subList(1, arr.size(), arr);
			int po	= arr2.indexOf(pai);
			if(po != -1)
			{
				shun_ke_out 	= copyList2(shun_ke);
				shuang_out      = copyList1(shuang);
				dan_out         = copyList1(dan);
				arr2.remove(po);
				shuang_out.add(pai);
				if(shuang_out.size() > 2)return;
				this.checkCanhu(arr2,shun_ke_out,shuang_out,dan_out);
			}
			//单牌
			arr2  =  MyArrays.subList(1, arr.size(), arr);
			shun_ke_out 	= copyList2(shun_ke);
			shuang_out      = copyList1(shuang);
			dan_out         = copyList1(dan);
			
			dan_out.add(pai);
			if(dan_out.size() > 2)	return;
			else if(dan_out.size() == 2)
			{
				int p1	= dan_out.get(0);
				int p2	= dan_out.get(1);
				if(p1 == p2 || p1 > 30 || p2 > 30)	return;
				if(Math.abs(p1 - p2)> 2)	return;
			}
			this.checkCanhu(arr2,shun_ke_out,shuang_out,dan_out);
	}
	public List<List<Integer>> copyList2(List<List<Integer>> dicParent)
	{
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		for(int i = 0 ; i < dicParent.size();i++)
		{
			List<Integer> tmp;
			tmp =  MyArrays.subList(0, dicParent.get(i).size(), dicParent.get(i));
			result.add(tmp);
		}
		return result;
	}
	public List<Integer> copyList1(List<Integer> dicParent)
	{
		List<Integer> list ;
		list  = MyArrays.subList(0, dicParent.size(), dicParent);
		return list;
	}
	public void addToDic(List<Integer>  tmpList)
	{
		boolean added = false;;
		for(int i = 0 ; i < this.Dic.size();i++)
		{
			int type = tmpList.get(0);
			List<Integer> childDic  = this.Dic.get(i);
			if(childDic.get(0) != type) continue;
			added = true;
			for(int j = 1 ; j <tmpList.size();j++)
			{
				int pai = tmpList.get(j);
				if(!childDic.contains(pai))childDic.add(pai);
			}
		}
		if(!added)this.Dic.add(tmpList);
	}
	
	
}

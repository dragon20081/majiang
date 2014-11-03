package server.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SortMaJiang  implements Comparator<MajiangPai>{

	public synchronized static SortMaJiang getInstance()
	{
		SortMaJiang sort =  new SortMaJiang();
		return sort;
	}
	
	public List<MajiangPai> sortByNum(List<Integer> list)
	{
		List<MajiangPai> newList = new ArrayList<MajiangPai>();
		Map<Integer,MajiangPai> newMap = new HashMap<Integer, MajiangPai>();
		for(Integer tmp :  list)
		{
			MajiangPai tmpMaJiang = newMap.get(tmp);
			if(tmpMaJiang == null)
			{
				tmpMaJiang  = new MajiangPai(tmp,1);
				newMap.put(tmp, tmpMaJiang);
				newList.add(tmpMaJiang);
			}else
			{
				tmpMaJiang.setNum(tmpMaJiang.getNum()+1);
			}
		}
		Collections.sort(newList, this);
		return newList;
	}

	public int compare(MajiangPai o1, MajiangPai o2) {
		
		if(o1.getNum() < o2.getNum())
			return 1;
		return -1;
	}
	
}

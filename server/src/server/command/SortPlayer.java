package server.command;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import server.mj.MgsPlayer;

public class SortPlayer implements Comparator<MgsPlayer>{

	public synchronized static SortPlayer getInstance()
	{
		SortPlayer sort =  new SortPlayer();
		return sort;
	}
	public List<MgsPlayer> sortByYun(List<MgsPlayer> list)
	{
		Collections.sort(list, this);
		return list;
	}
	public int compare(MgsPlayer o1, MgsPlayer o2) {
		
		if(o1.getYun() < o2.getYun())
			return 1;
		return -1;
	}
}

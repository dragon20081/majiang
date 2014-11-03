package server.command;

import java.util.Comparator;

public class SortByFan implements Comparator<MaJiang_Fan>{

	public int compare(MaJiang_Fan o1, MaJiang_Fan o2) {
		
		if(o1.getTotalFan()  > o2.getTotalFan())
			return -1;
		return 1;
	}

	
}

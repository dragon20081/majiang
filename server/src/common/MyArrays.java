package common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyArrays {

	
	
	public synchronized static List<Integer> asList(int... args)
	{
		List<Integer> list  = new ArrayList<Integer>();
	    for (int s : args) {  
	    	list.add(s);
	    } 
		return list;
	}
	public synchronized static List<String> asList(String... args)
	{
		List<String> list  = new ArrayList<String>();
	    for (String s : args) {  
	    	list.add(s);
	    } 
		return list;
	}
	public synchronized static List<Integer> ArrasList(int[] args)
	{
		List<Integer> list  = new ArrayList<Integer>();
	    for (int s : args) {  
	    	list.add(s);
	    } 
		return list;
	}
	
	public synchronized static List<Integer> subList(int start,int end,List<Integer> list)
	{
		List<Integer> tmplist  = new ArrayList<Integer>();
		for(int i = 0; i < list.size();i++)
		{
			if(i >=start && i < end)
				tmplist.add(list.get(i));
		}
		return tmplist;
	}
	public synchronized static int[]  ListToArr(List<Integer> list)
	{
		 int[] tmp  = new int[list.size()];
		 for(int i  = 0 ; i < list.size();i++)
		 {
			 tmp[i] = list.get(i);
		 }
		 Arrays.sort(tmp);
		 return tmp;
	}
	public synchronized static List<Integer> sort(List<Integer> list)
	{
		int i = 0;
		int[] tmpa = new int[list.size()];
		for( i  = 0;i < tmpa.length;i++)
		{
			tmpa[i]  = list.get(i);
		}
		Arrays.sort(tmpa);
		
		List<Integer> list2 = new ArrayList<Integer>();
		for( i  = 0;i < tmpa.length;i++)
		{
			list2.add(tmpa[i]); 
		}
		return list2;
	}
}

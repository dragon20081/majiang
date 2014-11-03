package server.command;

import java.util.ArrayList;
import java.util.List;

public class MyArray {

	private List<Object> list;
	
	public synchronized static  MyArray getMyArray(Object... args)
	{
		MyArray arr = new MyArray();
		for(int i = 0 ; i< args.length;i++ )
		{
			arr.push(args[i]);
		}
		return arr;
	}
	public MyArray()
	{
		list  = new ArrayList<Object>();
	}
	public void push(Object obj)
	{
		list.add(obj);
	}
	/**
	 * 删除最后一个元素，并将其返回
	 */
	public Object pop()
	{
		Object obj  = list.get(list.size() -1);
		list.remove(list.size() -1);
		return obj;
	}
	public List<Object> splice(int start,int num)
	{
		int i  = start;
		List<Object> deleteOjbs = new ArrayList<Object>();
		for( ;i < list.size();i++)
		{
			Object o = list.remove(i);
			deleteOjbs.add(o);
			if(deleteOjbs.size() == num)
				return deleteOjbs;
		}
		return deleteOjbs;
	}
	/**
	 * 元素个数
	 */
	public int length()
	{
		return list.size();
	}
	public Object get(int index)
	{
		return list.get(index);
	}
	public List<Object> getList() {
		return list;
	}

	
}

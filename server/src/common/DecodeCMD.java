package common;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;

public class DecodeCMD {

	public static final int BYTE 		=  1; 
	public static final int SHORT 		=  2; 
	public static final int INT 		=  3; 
	public static final int UTF 		=  4; 
	public static final int DOUBLE 		=  5; 
	public static final int ARR 		=  6; 
	public static final int BOOLEAN 	=  7; 
	public static final int NULL    	=  8; 
	public static final int DICTIONARY  =  9; 
	
	public synchronized static List<Integer> getList(List list, int flag)
	{
		List<Integer> flags = new ArrayList<Integer>();
		for(int i = 0 ; i < list.size();i++)
		{
			flags.add(flag);
		}
		return flags;
	}
	
	public List<Integer> decode_tmp(ChannelBuffer buf)
	{
		List<Integer> patternList =  new ArrayList<Integer>();
		int nextInt = 1 ; 
		while(nextInt == 1)
		{
			int value  =  buf.readInt();
			for(int i = 0 ; i < 10;i++)
			{
				int flag  = getFlagByIndex(value,i);
				patternList.add(flag);
			}
			nextInt  =  value & 3;
		}
		return patternList;
	}
	
	public int getFlagByIndex(int value,int index)
	{
		// 1 计算出这个位置全部为1的值  与value 求与再右移动
//		int a  =  7;
		return 0;
	}
	
	public List<Object> decode(ChannelBuffer buf) throws Exception
	{
		List<Object> result =  new ArrayList<Object>();
		List<Integer> list  =  new ArrayList<Integer>();
		getTypes(list,buf);
		readObj(list,result,buf);
		return result;
	}
	private void readObj(List<Integer> list,List<Object> result,ChannelBuffer buf) throws Exception
	{
		while(list.size() > 0)
		{
			Object obj  =  read(buf,list.get(0),list);
//			if(obj == null)
//				throw new Exception("decode cmd error");
			result.add(obj);
		}
	}
	
	private  void getTypes(List<Integer> list,ChannelBuffer buf)
	{
		int value = buf.readInt();
		String str = value +"";
		boolean hasNext  = false;
		if(str.length() > 9)
		{
			hasNext = true;
			str = str.substring(1);
		}
		for(int i = 0 ; i< str.length();i++)
		{
			if(str.length() == 10 && i ==0)continue;
			char ch =  str.charAt(i);
			int num  =  Integer.parseInt(ch+"");
			list.add(num);
		}
		if(hasNext)
		{
			getTypes(list,buf);
		}
		
	}
	private Object read(ChannelBuffer buf,int flag,List<Integer> list)
	{
		list.remove(0);
		switch(flag)
		{
			case DecodeCMD.BYTE:  			return (int)buf.readByte(); 
			case DecodeCMD.SHORT:  		return (int)buf.readShort();
			case DecodeCMD.INT:  			return (int)buf.readInt();
			case DecodeCMD.DOUBLE:			return (Double)buf.readDouble();
			case DecodeCMD.UTF:  			return buf.readBytes(buf.readShort()).toString(Charset.forName("UTF-8"));
			case DecodeCMD.ARR: 

									int len  = (Integer) read(buf,list.get(0),list);// buf.readByte();
									List<Object> result =  new ArrayList<Object>();
									for(int i = 0; i < len;i++)
									{
										Object obj  =  read(buf,list.get(0),list);
										result.add(obj);
									}
									return result;
			case DecodeCMD.BOOLEAN:       
								    boolean b = false;
								    b = (buf.readByte() == 0 ? false:true); 
								    return b; 
			case DecodeCMD.NULL:			
									return null;
			
			case DecodeCMD.DICTIONARY:
									int len2  = (Integer) read(buf,list.get(0),list);// buf.readByte();
									len2 = len2 * 2;
									Map<Object,Object> map = new HashMap<Object,Object>();
									List<Object> tmpArr = new ArrayList<Object>();
									int i = 0;
									for(i = 0; i < len2 ;i++)
									{
										Object obj  =  read(buf,list.get(0),list);
										tmpArr.add(obj);
									}
									for(i = 0; i < tmpArr.size(); i++)
									{
										map.put(tmpArr.get(i),tmpArr.get(i + 1));
										i++;
									}
									return map;
		}
		return null;
	}
	
}

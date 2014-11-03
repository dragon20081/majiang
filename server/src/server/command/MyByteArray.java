package server.command;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import common.DecodeCMD;
import common.Log;

public class MyByteArray {

	public static final int BYTE 		=  1; 
	public static final int SHORT 		=  2; 
	public static final int INT 		=  3; 
	public static final int UTF 		=  4; 
	public static final int DOUBLE	   =   5; 
	public static final int ARR 		=  6; 
	public static final int BOOLEAN 	=  7; 
	public static final int NULL 		=  8; 
	public static final int DICTIONARY  =  9; 
	
	
	private ChannelBuffer buf ;
	private List<Integer> pattern;
	private List<Integer> cmdPatterns;
	
	private MyArray arrData;
	
	public MyByteArray()
	{
		buf  = ChannelBuffers.dynamicBuffer();
		pattern = new ArrayList<Integer>();
	}
	
	public void write(Object o)
	{
		if(arrData == null)arrData = new MyArray();
		arrData.push(o);	
	}
	
	@SuppressWarnings("unchecked")
	public  void write(MyArray arr)
	{
		for(int i  = 0 ; i < arr.length();i++)
		{
			Object obj = arr.get(i);
			writeObj(obj);
		}
		setCmdPatterns(pattern);
		Log.log("pattern :" +pattern);
		ChannelBuffer buf2  = ChannelBuffers.dynamicBuffer();
		writePattern(buf2);
		buf2.writeBytes(buf);
		buf =  buf2;
	}
	@SuppressWarnings("unchecked")
	private void writeObj(Object obj )
	{
		if(obj instanceof Integer)
		{
			int n  = ((Integer)obj).intValue();
			if(n >= 32767 || n <= -32767)
			{
				this.writeInt(n);
			}
			else if(n >= 127 || n <= -127)
			{
				this.writeShort(n);
			}
			else
			{
				this.writeByte(n);
			}
		}else if(obj instanceof Double)
		{
			this.writeDouble((Double)obj);
		}
		else if(obj instanceof Boolean)
		{
			int boolByte = (Boolean)obj == false ? 0 : 1;
			this.writeBoolean(boolByte);
		}
		else if(obj instanceof String)
		{
			this.writeUTF((String)obj);
		}else if(obj instanceof List<?>)
		{
			pattern.add(ARR);
			writeObj(((List<Object>)obj).size() );
			while(((List<Object>)obj).size() > 0)
			{
				Object listObj = ((List<Object>)obj).get(0);
				((List<Object>)obj).remove(0);
				writeObj(listObj);
			}
		}else if(obj == null)
		{
			this.writeNULL();
		}else if(obj instanceof Map)
		{
			pattern.add(DICTIONARY);
//			Log.info("dic length:" +((Map<Object,Object>)obj).size() );
			writeObj(((Map<Object,Object>)obj).size());
			Iterator<Object> it = ((Map<Object,Object>)obj).keySet().iterator();
			while(it.hasNext())
			{
				Object key = it.next();
				Object value = ((Map<Object,Object>)obj).get(key);
				
//				Log.info("dic map:" +key + " ,   " +  value);
				writeObj(key);
				writeObj(value);
				
				
			}
			
		}
	}
	
	public void setCmdPatterns(List<Integer> list) {
		
		this.cmdPatterns  = new ArrayList<Integer>();
		StringBuffer strBuf = new StringBuffer();
		for(int i = 0 ; i < list.size();i++)
		{
			strBuf.append(list.get(i));
			if(strBuf.length() == 9 &&  i < list.size() -1)
			{
				strBuf.insert(0, 1);
				int tmpPat = Integer.parseInt(strBuf.toString());
				cmdPatterns.add(tmpPat);
				strBuf = new StringBuffer();
			}
		}
		   int tmp =  Integer.parseInt(strBuf.toString());
			cmdPatterns.add(tmp);
	}
	public void writePattern(ChannelBuffer buf)
	{
		for(int i  = 0 ; i  < this.cmdPatterns.size();i++)
		{
			buf.writeInt(this.cmdPatterns.get(i));
		}
	}
	
	public  void writeInt(int num)
	{
		this.buf.writeInt(num);
		pattern.add(INT);
	}
	public  void writeShort(int num)
	{
		this.buf.writeShort(num);
		pattern.add(SHORT);
	}
	public  void writeByte(int num)
	{
		this.buf.writeByte(num);
		pattern.add(BYTE);
	}
	public void writeBoolean(int num)
	{
		this.buf.writeByte(num);
		pattern.add(BOOLEAN);
	}
	public void writeDouble(double num)
	{
		this.buf.writeDouble(num);
		pattern.add(DOUBLE);
	}
	public void writeNULL()
	{
		pattern.add(NULL);
	}
	
	public void writeUTF(String str)
	{
		ChannelBuffer buffer = ChannelBuffers.copiedBuffer(str,
				Charset.forName("UTF-8"));
		buf.writeShort(buffer.readableBytes());
		buf.writeBytes(buffer);
		pattern.add(UTF);
	}
	public void writeUTFBytes(String str)
	{
		ChannelBuffer buffer = ChannelBuffers.copiedBuffer(str,
				Charset.forName("UTF-8"));
		buf.writeBytes(buffer);
	}
	
	
	public List<Object>  read(ChannelBuffer buf)
	{
		List<Object> result  = null;
		try {
				result  =  decode(buf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public List<Object> decode(ChannelBuffer buf) throws Exception
	{
		List<Object> result =  new ArrayList<Object>();
		List<Integer> list =  new ArrayList<Integer>();
		int value = buf.readInt();
		String str = value +"";
		int i  =0;
		for(i = 0 ; i< str.length();i++)
		{
			if(str.length() == 10 && i ==0)continue;
			char ch =  str.charAt(i);
			int num  =  Integer.parseInt(ch+"");
			list.add(num);
		}
		for(i =0 ; i <list.size();i++ )
		{
			Object obj  =  read(buf,list.get(i));
			if(obj == null)
				throw new Exception("decode cmd error");
			result.add(obj);
		}
		return result;
	}
	

	private Object read(ChannelBuffer buf,int flag)
	{
		switch(flag)
		{
			case DecodeCMD.BYTE:  			return (int)buf.readByte(); 
			case DecodeCMD.SHORT:  		return (int)buf.readShort();
			case DecodeCMD.INT:  			return (int)buf.readInt();
//			case DC.FLOAT:  		return (double)buf.readFloat();
//			case DC.DOUBLE:  		return (double)buf.readDouble();
			case DecodeCMD.UTF:  			return buf.readBytes(buf.readShort()).toString(Charset.forName("UTF-8"));
//			case DC.UTFBYTES:  		return buf.readBytes(buf.readableBytes()).toString(Charset.forName("UTF-8"));
		}
		return null;
	}
	public ChannelBuffer getBuf() {
		if(this.arrData != null) this.write(arrData);
		return buf;
	}
	public void setBuf(ChannelBuffer buf) {
		this.buf = buf;
	}
}

package common;

import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;

public class CommonBuffer {

	public   void writeIntArr(ChannelBuffer buf,int[] arr)
	{
		buf.writeShort(arr.length);
		for(int i = 0;i < arr.length;i++)
		{
			buf.writeInt(arr[i]);
		}
	}
	public  void writeShortArr(ChannelBuffer buf,short[] arr)
	{
		buf.writeShort(arr.length);
		for(int i = 0;i < arr.length;i++)
		{
			buf.writeShort(arr[i]);
		}
	}
	public  void writeByteArr(ChannelBuffer buf,byte[] arr)
	{
		buf.writeShort(arr.length);
		for(int i = 0;i < arr.length;i++)
		{
			buf.writeByte(arr[i]);
		}
	}
	
	public  void writeByteList(ChannelBuffer buf,List<Integer> arr)
	{
		buf.writeShort(arr.size());
		for(int i = 0;i < arr.size();i++)
		{
			buf.writeByte(arr.get(i));
		}
	}
	/**
	 * 定长
	 * @param buf
	 * @param arr
	 */
	public  void writeByteArrList(ChannelBuffer buf,List<int[]> arr,int len)
	{
		buf.writeShort(arr.size()* len);
		for(int i = 0;i < arr.size();i++)
		{
			int[] tmp  = arr.get(i);
			for(int j = 0 ; j < len;j++)
			{
				buf.writeByte(tmp[j]);
			}
		}
	}
	public  void writeByteArrList2(ChannelBuffer buf,List<List<Integer>> arr,int len)
	{
		buf.writeShort(arr.size()* len);
		for(int i = 0;i < arr.size();i++)
		{
			List<Integer> tmp  = arr.get(i);
			for(int j = 0 ; j < len;j++)
			{
				buf.writeByte(tmp.get(j));
			}
		}
	}
}

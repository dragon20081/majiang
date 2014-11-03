package server.command;


import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import server.mj.MgsPlayer;

import common.DecodeCMD;

public abstract class CMD {
		
	//格式
	//
	protected MgsPlayer player;
	private List<Object> values;
	private List<Integer>    cmdPatterns ;
	
	public abstract  void setPlayer(MgsPlayer player);
	public abstract ChannelBuffer getBytes();
	public abstract ChannelBuffer getBytes(Object obj);
	
	
	
	public   void setBytes(ChannelBuffer buf)
	{
		if(buf.readableBytes() < 4)return;
		
		try {
				values =  new DecodeCMD().decode(buf);
				player.getBusiness().saveUserOperate("指令参数:" +values.toString() );
		} catch (Exception e) { e.printStackTrace(); }
	}
	public void deal_back(){}
	public List<Object> getValues() {
		return values;
	}
	public void setValues(List<Object> values) {
		this.values = values;
	}
	public int getIntVaule(int index)
	{
		if(values == null)return Integer.MIN_VALUE;
		Integer d  =  (Integer)(this.values.get(index));
		int num  = d.intValue();
		return num;
	}
	public List<Object> getArrValue(int index)
	{
		if(values == null)return null;
		if(this.values.size() < index + 1 )return null;
		List<Object> obj  =  (List<Object>)(this.values.get(index));
		return  obj;
	}
	public Map<Object,Object> getMapValue(int index)
	{
		if(values == null)return null;
		Map<Object,Object> obj  =  (Map<Object,Object>)(this.values.get(index));
		return  obj;
	}
	
	public boolean getBooleanVaule(int index)
	{
		if(values == null)return false;
		boolean b  =  (Boolean)(this.values.get(index));
		return b;
	}
	public String getStrValue(int index)
	{
		if(values == null)return null;
		String str = (String) this.values.get(index);
		return str;
	}
	public List<Integer> getCmdPatterns() {
		return cmdPatterns;
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
	public void writeUTF(String str,ChannelBuffer buf)
	{
		ChannelBuffer buffer = ChannelBuffers.copiedBuffer(str,
				Charset.forName("UTF-8"));
		buf.writeShort(buffer.readableBytes());
		buf.writeBytes(buffer);
	}
	public void writeUTFBytes(String str,ChannelBuffer buf)
	{
		ChannelBuffer buffer = ChannelBuffers.copiedBuffer(str,
				Charset.forName("UTF-8"));
		buf.writeBytes(buffer);
	}
	public String readUTF(ChannelBuffer buf)
	{
		String variable =  buf.readBytes(buf.readShort()).toString(Charset.forName("UTF-8"));
		return variable;
	}
	
	public void tracelist(List<Integer> list)
	{
		for(int i  = 0 ; i < list.size();i++)
		{
			System.out.print(list.get(i)+ ":");
		}
	}
	public MgsPlayer getPlayer() {
		return player;
	}
	
	
	
	
}

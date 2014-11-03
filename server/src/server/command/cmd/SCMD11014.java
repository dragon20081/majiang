package server.command.cmd;



import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import server.command.CMD;
import server.command.MaJiang_Fan;
import server.mj.MgsPlayer;

import common.MyArrays;

/**
 *  番数
 * @author xue
 */
public class SCMD11014 extends CMD{
	private static final Logger logger = Logger.getLogger(SCMD11014.class.getName());
	
	public static  int   PASSABLE = 1;
	public static  int   PASS_NOT = 2;
	
	private int pass  =  0;
	private int paiId =  0;
	@Override
	public ChannelBuffer getBytes() {
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		return buf;
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		
		MgsPlayer p  = (MgsPlayer) obj;
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		buf.writeInt(p.getBusiness().getPlayer().getUid());
		buf.writeInt(p.getTotalScore());
		buf.writeInt(p.getScore());
		MaJiang_Fan fan  =  p.getFan();
		buf.writeByte(fan.getTotalFan());
		return buf;
	}
	
	private int getFanTypeByName(String name)
	{
		List<String> fanNameList  =  MyArrays.asList("暗七对","清一色");
		int type  =  fanNameList.indexOf(name);
		return type;
	}
	
	@Override
	public void setBytes(ChannelBuffer buf) {
		
	}
	@Override
	public void setPlayer(MgsPlayer player) {
		
	}
	public int getPass() {
		return pass;
	}
	public void setPass(int pass) {
		this.pass = pass;
	}
	public int getPaiId() {
		return paiId;
	}
	public void setPaiId(int paiId) {
		this.paiId = paiId;
	}
}

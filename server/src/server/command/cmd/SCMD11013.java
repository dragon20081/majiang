package server.command.cmd;



import org.jboss.logging.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import common.DecodeCMD;
import common.MyArrays;

import server.command.CMD;
import server.mj.MgsPlayer;
import server.mj.Room;

/**
 *  吃
 * @author xue
 */
public class SCMD11013 extends CMD{
	private static final Logger logger = Logger.getLogger(SCMD11013.class.getName());
	
	public static  int   PASSABLE = 1;
	public static  int   PASS_NOT = 2;
	
	private int pai1  = 0;
	private int pai2  = 0;
	private int pai3  = 0;
	@Override
	public ChannelBuffer getBytes() {
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		return buf;
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		super.setCmdPatterns(MyArrays.asList(DecodeCMD.BYTE,DecodeCMD.BYTE,DecodeCMD.BYTE,DecodeCMD.BYTE));
		MgsPlayer p  = (MgsPlayer) obj;
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		this.writePattern(buf);
		buf.writeByte(p.getRoomId());
		buf.writeByte(pai1);
		buf.writeByte(pai2);
		buf.writeByte(pai3);
		return buf;
	}
	@Override
	public void setBytes(ChannelBuffer buf) {
		
	}
	@Override
	public void setPlayer(MgsPlayer player) {
		
	}
	public int getPai1() {
		return pai1;
	}
	public void setPai1(int pai1) {
		this.pai1 = pai1;
	}
	public int getPai2() {
		return pai2;
	}
	public void setPai2(int pai2) {
		this.pai2 = pai2;
	}
	public int getPai3() {
		return pai3;
	}
	public void setPai3(int pai3) {
		this.pai3 = pai3;
	}
	
	
}

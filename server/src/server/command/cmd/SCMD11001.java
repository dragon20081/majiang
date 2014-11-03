package server.command.cmd;


import org.jboss.logging.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.MgsPlayer;
import server.mj.Room;

import common.DecodeCMD;
import common.MyArrays;

/**
 * 开始自动匹配， 成功进入匹配队列
 * @author xue
 *
 */
public class SCMD11001 extends CMD{
	private static final Logger logger = Logger.getLogger(SCMD11001.class.getName());
	
	private int playerLimit;
	private int skillLimit ;
	private int chipsLimit ;
	
	@Override
	public ChannelBuffer getBytes() {
		return null;
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		//人数，庄家ID 自己位置
//		super.setCmdPatterns(MyArrays.asList(DC.BYTE,DC.BYTE,DC.BYTE));
//		MgsPlayer  p =  (MgsPlayer) obj;
//		Room r =  p.getRoom(); 
//		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
//		this.writePattern(buf);
//		buf.writeByte(r.getPlayerLimit());
//		buf.writeByte(r.getZhuangId());
//		buf.writeByte(p.getRoomId());
		
		MgsPlayer  p =  (MgsPlayer) obj;
		Room r =  p.getRoom(); 
		MyArray arr = MyArray.getMyArray(r.getPlayerLimit(),r.getZhuangId(),p.getRoomId());
		MyByteArray byteArray  = new MyByteArray();
		byteArray.write(arr);
		return byteArray.getBuf();
	}
	public ChannelBuffer  getFaliMsg(int plimit,int zid,int rid)
	{
		MyArray arr = MyArray.getMyArray(plimit,zid,rid);
		MyByteArray byteArray  = new MyByteArray();
		byteArray.write(arr);
		return byteArray.getBuf();
	}
	@Override
	public void setBytes(ChannelBuffer buf) {
		
	}
	@Override
	public void setPlayer(MgsPlayer player) {
		
	}
	public int getPlayerLimit() {
		return playerLimit;
	}
	public void setPlayerLimit(int playerLimit) {
		this.playerLimit = playerLimit;
	}
	public int getSkillLimit() {
		return skillLimit;
	}
	public void setSkillLimit(int skillLimit) {
		this.skillLimit = skillLimit;
	}
	public int getChipsLimit() {
		return chipsLimit;
	}
	public void setChipsLimit(int chipsLimit) {
		this.chipsLimit = chipsLimit;
	}

	
}

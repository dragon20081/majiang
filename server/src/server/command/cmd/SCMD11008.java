package server.command.cmd;



import org.jboss.logging.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import common.DecodeCMD;
import common.MyArrays;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.MgsPlayer;
import server.mj.Room;


/**
 *  杠
 * @author xue
 *
 */
public class SCMD11008 extends CMD{
	private static final Logger logger = Logger.getLogger(SCMD11008.class.getName());
	
	
	
	public static final int   PASSABLE = 1;
	public static final int   PASS_NOT = 2;
	
	public static final int   DAMINGGANG     = 1;
	public static final int   XIAOMINGANGANG = 2;
	public static final int   ANGANG         = 3;
	
	private int pass  = 0;
	private int paiId = 0;
	private int type  = 0;
	private int whoDaId = 0;
	@Override
	public ChannelBuffer getBytes() {
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		return buf;
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		
//		super.setCmdPatterns(MyArrays.asList(DC.BYTE,DC.BYTE,DC.BYTE));
		MgsPlayer p  = (MgsPlayer) obj;
		MyArray arr = MyArray.getMyArray();
		arr.push(p.getRoomId());
		arr.push(this.paiId);
		arr.push(type);
//		if(type  == DAMINGGANG )
//			arr.push(whoDaId);
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getWhoDaId() {
		return whoDaId;
	}
	public void setWhoDaId(int whoDaId) {
		this.whoDaId = whoDaId;
	}
	
}

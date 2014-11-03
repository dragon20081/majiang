package server.command.cmd;



import org.jboss.logging.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import common.MyArrays;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.MgsPlayer;
import server.mj.Room;

/**
 *	修改气返回
 * @author xue
 */
public class SCMD11031 extends CMD{
	private static final Logger logger = Logger.getLogger(SCMD11031.class.getName());
	private int uid;
	private int xiugailinag;
	private int result;
	
	@Override
	public ChannelBuffer getBytes() {
		MyArray arr = new MyArray();
		arr.push(uid);
		arr.push(xiugailinag);
		arr.push(result);
		MyByteArray bytearr = new MyByteArray();
		bytearr.write(arr);
		return bytearr.getBuf();
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		return null;
	}
	@Override
	public void setBytes(ChannelBuffer buf) {
	}
	@Override
	public void setPlayer(MgsPlayer player) {
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getXiugailinag() {
		return xiugailinag;
	}
	public void setXiugailinag(int xiugailinag) {
		this.xiugailinag = xiugailinag;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	
	
}

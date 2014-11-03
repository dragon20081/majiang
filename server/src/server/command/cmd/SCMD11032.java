package server.command.cmd;



import org.jboss.logging.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.MgsPlayer;
import server.mj.Room;

/**
 *	修改运返回
 * @author xue
 */
public class SCMD11032 extends CMD{
	private static final Logger logger = Logger.getLogger(SCMD11032.class.getName());
	
	private int uid;
	private int xiugailinag;
	private int result;
	private boolean flag;  // 0 临时  1 永久
	
	@Override
	public ChannelBuffer getBytes() {
		MyArray arr = new MyArray();
		arr.push(uid);
		arr.push(xiugailinag);
		arr.push(result);
		arr.push(flag);
		
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
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}

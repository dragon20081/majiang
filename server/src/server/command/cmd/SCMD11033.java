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
 *	使用技能
 * @author xue
 */
public class SCMD11033 extends CMD{
	private static final Logger logger = Logger.getLogger(SCMD11033.class.getName());
	
	private int uid;
	private int skillId;
	@Override
	public ChannelBuffer getBytes() {

		MyArray arr = new MyArray();
		arr.push(uid);
		arr.push(skillId);
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
	public int getSkillId() {
		return skillId;
	}
	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}
	
	
}

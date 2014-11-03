package server.command.cmd;

import org.jboss.netty.buffer.ChannelBuffer;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.MgsPlayer;
import server.mj.Room;

/**
 *  房间规则修改
 * @author xue
 */
public class CCMD11055 extends  CMD{
	
	
	public void setPlayer(MgsPlayer player) {
		this.player = player;
	}
	public ChannelBuffer getBytes() {
		return null;
	}
	public ChannelBuffer getBytes(Object obj) {
		return null;
	}
	public void setBytes(ChannelBuffer buf) {
		super.setBytes(buf);
		int pL = this.getIntVaule(0);
		int cL = this.getIntVaule(1);
		int beishu = this.getIntVaule(2);
		boolean skill = this.getBooleanVaule(3);
		int qL = this.getIntVaule(4);
		this.player.getRoom().setChipsLimit(cL);
		this.player.getRoom().setQuanLimit(qL);
		this.player.getRoom().setPlayerLimit(pL);
		this.player.getRoom().setBeishu(beishu);
		int skilllimit  = skill == true?1:2;
		this.player.getRoom().setSkillLimit(skilllimit);
		
		//广播房间信息
		MyArray arr = new MyArray();
		arr.push(pL);
		arr.push(cL);
		arr.push(beishu);
		arr.push(skill);
		arr.push(qL);
		arr.push(this.player.getRoom().getManager().getLocId());
		MyByteArray byteBuf = new MyByteArray();
		byteBuf.write(arr);
		this.player.getRoom().SendRoomBroadcast(11055, byteBuf.getBuf());
	}
	public void auto_deal(MgsPlayer p)
	{
		Room r = p.getRoom();
		int pL = r.getPlayerLimit();
		int cL = r.getChipsLimit();
		int beishu = r.getBeishu();
		int qL = r.getQuanLimit();

		boolean skill = false;
		skill  = r.getSkillLimit() == 1 ? true : false;
		//广播房间信息
		MyArray arr = new MyArray();
		arr.push(pL);
		arr.push(cL);
		arr.push(beishu);
		arr.push(skill);
		arr.push(qL);
		arr.push(r.getManager().getLocId());
		MyByteArray byteBuf = new MyByteArray();
		byteBuf.write(arr);
		p.getRoom().SendRoomBroadcast(11055, byteBuf.getBuf());
	}
		



}

package server.command.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;

import common.Log;

import server.command.CMD;
import server.command.PatternPai;
import server.mj.MgsPlayer;
import server.mj.Room;

/**
 *  叫牌
 * @author xue
 */
public class CCMD11014 extends  CMD{
	
	private PatternPai pattern = new PatternPai();
	private static final Logger logger = Logger.getLogger(CCMD11014.class.getName());
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
		
		if(this.getValues() == null)
		{
			this.setValues( new ArrayList<Object>());
		}
		
		Log.log("---->11014      "+ this.player.getName() +"    " +this.getValues().toString());
		
		Room r  =  player.getRoom();
		
		if(r.isInTuoguan(player))
		{
			return;
		}
		List<Integer> jiao  = new ArrayList<Integer>();
		for(int i = 0 ; i < this.getValues().size();i++)
		{
			jiao.add(this.getIntVaule(i));
			System.out.print(this.getIntVaule(i) +",");
		}
		this.player.setJiao(jiao);
		logger.info("ccmd11014 jiaopai: "+player.getName()  +"  "+jiao.toString() );
	}

}

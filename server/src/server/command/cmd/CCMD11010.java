package server.command.cmd;

import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;

import server.command.CMD;
import server.mj.MgsPlayer;
import server.mj.Room;

/**
 *   过 pass
 * @author xue
 *
 */
public class CCMD11010 extends  CMD{
	
	
	private static final Logger logger = Logger.getLogger(CCMD11010.class.getName());
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

		Room r  =  player.getRoom();
		
		if(r.isInTuoguan(player))
		{
			return;
		}
		
		logger.info("11010 pass:"+ this.player.getName() );
		this.player.getBusiness().saveUserOperate("过 pass");
		r.deal_pass(player);
	}
}

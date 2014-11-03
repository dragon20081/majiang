package server.command; 
import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import server.command.CMD;
import server.mj.MgsPlayer;


public class SCMD3 extends CMD {
	public List<ChannelBuffer> list = new ArrayList<ChannelBuffer>();
	private static final Logger logger = Logger.getLogger(SCMD3.class.getName());
	
	
//	@Override
	public ChannelBuffer getBytes() {
		ChannelBuffer buf = ChannelBuffers.dynamicBuffer();
		for(int i = 0; i < list.size(); i++){
			buf.writeBytes(list.get(i));
		}
		return buf;
	}
	@Override
	public void setPlayer(MgsPlayer player) {
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		return null;
	}
	@Override
	public void setBytes(ChannelBuffer buf) {
		
	}

}

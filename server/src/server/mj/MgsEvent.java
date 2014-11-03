package server.mj;

import org.jboss.netty.buffer.ChannelBuffer;

public class MgsEvent extends Object{
	
	public int cmd = 0;
	public int cmdCode  = 0;
	public ChannelBuffer message;
	
	public MgsEvent(int _cmd)
	{
		this.cmd = _cmd;
	}

}

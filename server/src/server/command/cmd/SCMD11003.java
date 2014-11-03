package server.command.cmd;


import java.util.List;

import org.jboss.logging.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import server.command.CMD;
import server.command.MyArray;
import server.command.MyByteArray;
import server.mj.MgsPlayer;
import server.mj.Room;

import common.CommonBuffer;
import common.DecodeCMD;
import common.Log;
import common.MyArrays;

/**
 * 回复所有手牌
 * @author xue
 *
 */
public class SCMD11003 extends CMD{
	private static final Logger logger = Logger.getLogger(SCMD11003.class.getName());
	CommonBuffer common  =  new CommonBuffer();
	@Override
	public ChannelBuffer getBytes() {
		return null;
	}
	@Override
	public ChannelBuffer getBytes(Object obj) {
		MgsPlayer player = (MgsPlayer) obj;
		List<Integer> shoupai  = player.getShoupai();
		int touchPai = player.getTouchedPai();
		MyArray arr = MyArray.getMyArray();
		arr.push(player.getRoomId());
		if(touchPai != -1)
			arr.push(touchPai);
		for(int i = 0 ; i < shoupai.size();i++)
		{
			arr.push(shoupai.get(i));
		}
		Log.log("手牌："+ touchPai +shoupai.toString());
		Log.log(player.getLocId() +"::"+ player.getRoomId());
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
}

package server.command.cmd;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;

import server.command.CMD;
import server.command.MyByteArray;
import server.mj.MgsPlayer;
import server.mj.ServerTimer;
import business.CountDao;
import business.conut.Sts_360Order;
import business.entity.MJ_User;

/**
 * 360支付生成唯一订单号
 */


public class CCMD11302 extends  CMD {

	
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
		
		String order = generateOrderId();
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("订单号", order);
		
		MyByteArray myBuf = new MyByteArray();
		myBuf.write(map);
		this.player.send(11302, myBuf.getBuf());
	}
	/**
	 * 生成订单号记录 根据数据库唯一ID + 时间生成唯一订单号
	 * @return
	 */
	private String generateOrderId()
	{
		CountDao cdao = new CountDao();
		MJ_User user = this.player.getBusiness().getPlayer();
		Sts_360Order order = new Sts_360Order();
		order.setUid(user.getUid());
		order.setNick(user.getNick());
		cdao.saveSts_Object(order);
		
		String orderId = (order.getId()+ 10000)+""+ServerTimer.distOfSecond(Calendar.getInstance()) ;
		order.setApp_order_id(orderId);
		cdao.saveSts_Object(order);
		
		return orderId;
	}



}

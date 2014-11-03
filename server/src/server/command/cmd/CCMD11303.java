package server.command.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;

import common.MyArrays;

import server.command.CMD;
import server.mj.MgsPlayer;
import business.CountDao;
import business.conut.Sts_360Order;
import business.entity.MJ_User;


public class CCMD11303 extends  CMD {

	private static final Logger logger = Logger.getLogger(CCMD11303.class.getName());
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
		
		getUnVerifiedOrder();
	}
    public void getUnVerifiedOrder()
    {

    	MJ_User user = this.player.getBusiness().getPlayer();
    	CountDao cdao = new CountDao();
    	List<Sts_360Order> list =  cdao.findUserUnVerifiedOrder(user.getUid());
    	
    	int totalDia = 0;
    	
    	for(int i = 0; i < list.size(); i++)
    	{
    		Sts_360Order order = list.get(i);
    		totalDia += order.getDiamand();
    		order.setVerify_client(true);
    		cdao.saveSts_Object(order);
    		logger.info("order verified id:"+ order.getOrder_id() +"  dia:" + order.getDiamand());
    		
    	}
		logger.info("total dia:"+totalDia);
    	CCMD11101 cmd101 = new CCMD11101();
    	cmd101.setPlayer(player);
    	cmd101.sendUserInfo(MyArrays.asList(2,6));
    	
    	if(totalDia > 0)
    	{
    		logger.info("send success msg");
    		CCMD11111 cmd111 = new CCMD11111();
    		cmd111.auto_deal(this.player, "充值成功，获得钻石:" + totalDia);
    	}
    	
    	this.player.send(11303, null);
    	
    	
    	
    	
    	
    }
}

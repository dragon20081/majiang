package server.mj;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;




import org.jboss.netty.bootstrap.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import business.entity.M_Shop;


public class HelloServer {

	private static final Logger logger = Logger.getLogger(HelloServer.class.getName());
	private final int port;
	private final int httpPort;
	
	//public static final byte VERSION = 1;
	public HelloServer(int port, int httpPort)
	{
		this.port = port;
		this.httpPort = httpPort;
	}
	private void start()
	{
		logger.log(Level.INFO, "Netty Server init!");
		ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory());
		bootstrap.setPipelineFactory(new MyChannelPipelineFactory());
		  
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);
  
        bootstrap.bind(new InetSocketAddress(this.port));
        logger.log(Level.INFO, "Netty Server started!");
        
        logger.log(Level.INFO, "Http Server init!");
        ServerBootstrap httpbootstrap = new ServerBootstrap(new NioServerSocketChannelFactory());
		httpbootstrap.setPipelineFactory(new HttpChannelPipelineFactory());
  
		httpbootstrap.bind(new InetSocketAddress(this.httpPort));
        logger.log(Level.INFO, "Http Server started!");
//        new Notice().start();
        ServerTimer.getInstance().start();
        Map<Integer,M_Shop> shopItem_roles = Global.shopItem_gold;
	}
	public static void main(String[] args) {
//		int port = 8001;
//		int httpPort = 8002;
		int port = 7000;
		int httpPort = 8000;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		}
		if(args.length > 1){
			httpPort = Integer.parseInt(args[1]);
		}
		new HelloServer(port, httpPort).start();
	}

}

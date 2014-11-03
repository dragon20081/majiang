package server.command;




import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;

import server.command.cmd.CCMD10001;
import server.command.cmd.CCMD10005;
import server.command.cmd.CCMD11001_old;
import server.command.cmd.CCMD11002;
import server.command.cmd.CCMD11003;
import server.command.cmd.CCMD11004;
import server.command.cmd.CCMD11006;
import server.command.cmd.CCMD11007;
import server.command.cmd.CCMD11008;
import server.command.cmd.CCMD11009;
import server.command.cmd.CCMD11010;
import server.command.cmd.CCMD11011;
import server.command.cmd.CCMD11012;
import server.command.cmd.CCMD11013;
import server.command.cmd.CCMD11014;
import server.command.cmd.CCMD11015;
import server.command.cmd.CCMD11017;
import server.command.cmd.CCMD11019;
import server.command.cmd.CCMD11021;
import server.command.cmd.CCMD11031;
import server.command.cmd.CCMD11032;
import server.command.cmd.CCMD11033;
import server.command.cmd.CCMD11034;
import server.command.cmd.CCMD11041;
import server.command.cmd.CCMD11042;
import server.command.cmd.CCMD11043;
import server.command.cmd.CCMD11044;
import server.command.cmd.CCMD11051;
import server.command.cmd.CCMD11052;
import server.command.cmd.CCMD11053;
import server.command.cmd.CCMD11054;
import server.command.cmd.CCMD11055;
import server.command.cmd.CCMD11056;
import server.command.cmd.CCMD11057;
import server.command.cmd.CCMD11058;
import server.command.cmd.CCMD11059;
import server.command.cmd.CCMD11060;
import server.command.cmd.CCMD11071;
import server.command.cmd.CCMD11073;
import server.command.cmd.CCMD11081;
import server.command.cmd.CCMD11082;
import server.command.cmd.CCMD11083;
import server.command.cmd.CCMD11101;
import server.command.cmd.CCMD11102;
import server.command.cmd.CCMD11104;
import server.command.cmd.CCMD11105;
import server.command.cmd.CCMD11112;
import server.command.cmd.CCMD11113;
import server.command.cmd.CCMD11114;
import server.command.cmd.CCMD11201;
import server.command.cmd.CCMD11202;
import server.command.cmd.CCMD11301;
import server.command.cmd.CCMD11302;
import server.command.cmd.CCMD11303;
import server.command.cmd.CCMD999;
import server.mj.MgsPlayer;

public class CommandMap {

	
	private static final Logger logger = Logger.getLogger(CommandMap.class.getName());
	
//	public static CommandMap getInstance()
//	{
//		CommandMap _instance  = new CommandMap();
//		return _instance;
//	}
	public  void doCMD(MgsPlayer player, Integer cmdID, ChannelBuffer message)
	{
		cmdID += 11000;
		CMD cmd = null;
//		logger.info("messageReceived!:" + cmdID);
		switch(cmdID)
		{
			case 11001: cmd  =  new CCMD11001_old();  	break;
			case 11002: cmd  =  new CCMD11002();  	break;
			case 11003: cmd  =  new CCMD11003(); 	break;
			case 11004: cmd  =  new CCMD11004();	break;
			case 11006: cmd  =  new CCMD11006();	break;
			case 11007: cmd  =  new CCMD11007();	break;
			case 11008: cmd  =  new CCMD11008();	break;
			case 11009: cmd  =  new CCMD11009();	break;
			case 11010: cmd  =  new CCMD11010();	break;
			case 11011: cmd  =  new CCMD11011();	break;
			case 11012: cmd  =  new CCMD11012();	break;
			case 11013: cmd  =  new CCMD11013();	break;
			case 11014: cmd  =  new CCMD11014();	break;
			case 11015: cmd  =  new CCMD11015();	break;
			case 11017: cmd  =  new CCMD11017();	break;
			
			case 11019: cmd  =  new CCMD11019();	break;
			case 11021: cmd  =  new CCMD11021();	break;
			case 11031: cmd  =  new CCMD11031();	break;
			case 11032: cmd  =  new CCMD11032();	break;
			case 11033: cmd  =  new CCMD11033();	break;
			case 11034: cmd  =  new CCMD11034();	break;
			case 11041: cmd  =  new CCMD11041();	break;
			case 11042: cmd  =  new CCMD11042();	break;
			case 11043: cmd  =  new CCMD11043();	break;
			case 11044: cmd  =  new CCMD11044();	break;
			
			case 11051: cmd  =  new CCMD11051();	break;
			case 11052: cmd  =  new CCMD11052();	break;
			case 11053: cmd  =  new CCMD11053();	break;
			case 11054: cmd  =  new CCMD11054();	break;
			case 11055: cmd  =  new CCMD11055();	break;
			case 11056: cmd  =  new CCMD11056();	break;
			case 11057: cmd  =  new CCMD11057();	break; 
			case 11058: cmd  =  new CCMD11058();	break;
			case 11059: cmd  =  new CCMD11059();	break;
			
			case 11060: cmd  =  new CCMD11060();	break;
			
			case 11071: cmd  =  new CCMD11071();	break;
			case 11073: cmd  =  new CCMD11073();	break;
			case 11081: cmd  =  new CCMD11081();	break;
			case 11082: cmd  =  new CCMD11082();	break;
			case 11083: cmd  =  new CCMD11083();	break;
			
			case 11101: cmd  =  new CCMD11101();	break;
			case 11102: cmd  =  new CCMD11102();	break;
			case 11104: cmd  =  new CCMD11104();	break;
			case 11105: cmd  =  new CCMD11105();	break;
			
			case 11113: cmd  =  new CCMD11113();	break;
			case 11114: cmd  =  new CCMD11114();	break;
			
			case 11201: cmd  =  new CCMD11201();	break;
			case 11202: cmd  =  new CCMD11202();	break;
			case 11302: cmd  =  new CCMD11302();	break;
			case 11303: cmd  =  new CCMD11303();	break;
			case 11999: cmd  =  new CCMD999();	break;
			
		}
		if(cmd != null){
			try {
				cmd.setPlayer(player);
				cmd.setBytes(message);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("cmdID"+ e.getMessage());
				player.getBusiness().saveUserOperate("cmdID"+ e.getMessage());
			}
		}
	}
	public  void doUnCheckCMD(MgsPlayer player, Integer cmdID, ChannelBuffer message)
	{
		CMD cmd = null;
		switch(cmdID)
		{
			case 10001 : cmd = new CCMD10001() ; break;
			case 10005 : cmd = new CCMD10005() ; break;
			case 11112 : cmd = new CCMD11112() ; break;
			case 301: cmd  = new CCMD11301() ; break;
		}
		if(cmd != null){
			cmd.setPlayer(player);
			cmd.setBytes(message);
		}
	}
	
	
}

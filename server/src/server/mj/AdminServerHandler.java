package server.mj;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.util.CharsetUtil;

import server.http.Html;
import server.http.Html_360Notice;
import server.http.Html_AddJinyan;
import server.http.Html_AiYouXi;
import server.http.Html_Arpu;
import server.http.Html_Chat;
import server.http.Html_Cup;
import server.http.Html_DanJI;
import server.http.Html_DayRecharge;
import server.http.Html_Device;
import server.http.Html_FindChat;
import server.http.Html_Flowquery;
import server.http.Html_GodDiaFlow;
import server.http.Html_GoldCheck;
import server.http.Html_GoldCount;
import server.http.Html_Juqing;
import server.http.Html_M_Black;
import server.http.Html_Notice;
import server.http.Html_Payback;
import server.http.Html_Prop;
import server.http.Html_Sts_Device;
import server.http.Html_Task;
import server.http.Html_Test360Login;
import server.http.Html_UidRecharge;
import server.http.Html_UserOnline;
import server.http.Html_adnotice;
import server.http.Html_manage_User;
import server.http.Html_miracle_manage;
import server.http.Html_rateofturn;
import server.http.Html_reg;
import server.http.Html_user_recharge;
import server.http.IHtml;

import common.Log;

public class AdminServerHandler extends SimpleChannelUpstreamHandler {
	private static final Logger logger = Logger.getLogger(AdminServerHandler.class.getName());
	private List userList = new ArrayList();
	private List propList = new ArrayList();
	private List rechargeList = new ArrayList();
	private final int PROP_NUM = 8;
	private final static String inputCharset = "utf8";
	private final static String securekey = "0ee4b27a38aeae735610";
	
	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		super.channelOpen(ctx, e);
		logger.info("channelOpen!");
	}
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		super.channelConnected(ctx, e);
		logger.info("channelConnected!");
	}
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e){
		HttpRequest request = (HttpRequest) e.getMessage();  
		
		String url = request.getUri();
		System.out.println(url);
		String content = "";
		String[] arr = url.split("[?]");
		if(arr.length >= 2)
		{
			url = arr[0];
			content = arr[1];
		}else
		{
			ChannelBuffer buf = request.getContent();
			content = buf.toString(Charset.forName("UTF-8"));
		}
		try {
			content = java.net.URLDecoder.decode(content, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		logger.warn("url:"+url+",content:"+content);
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);  
		ChannelBuffer buffer=ChannelBuffers.dynamicBuffer();
		
		IHtml h = new Html();
		String hl = h.getHtml(content);
		
		try {
			
				
			if(url.equalsIgnoreCase("/payback"))
			{
				h = new Html_Payback();
				hl = h.getHtml(content);
			}else if("/".equals(url))
			{
			    hl=new Html_manage_User().getHtml(content);
			}
			else if(url.equalsIgnoreCase("/rate"))
			{
				h = new Html_rateofturn();
				hl = h.getHtml(content);
			}else if(url.equals("/manage_user"))
			{
			    hl=new Html_manage_User().getHtml(content);
			}else if(url.equals("/miracle_manage")){
				hl=new Html_miracle_manage().getHtml(content);
			}else if(url.equals("/user_online"))
			{
				hl=new Html_UserOnline().getHtml(content);
			}else if(url.equals("/notice"))
			{
				hl=new Html_Notice().getHtml(content);
			}else if(url.equals("/denotice")){
				
				 String time=content.split("=")[1];
				 MgsCache.getInstance().deleteNotice(time);
				 
			}else if(url.equals("/adnotice")){
				
				hl=new Html_adnotice().getHtml(content);	
			}else if(url.equals("/user_recharge"))
			{
				hl=new Html_user_recharge().getHtml(content);	
			}else if(url.equals("/Rechargeid"))
			{
				hl=new Html_UidRecharge().getHtml(content);	
			}else if(url.equals("/Rechargeidday"))
			{
				hl=new Html_DayRecharge().getHtml(content);	
			}else if(url.equals("/arpu"))
			{
				hl=new Html_Arpu().getHtml(content);
			}else if(url.equals("/findchat"))
			{
				hl=new Html_FindChat().getHtml(content);
			}else if(url.equals("/chat"))
			{
				hl=new Html_Chat().getHtml(content);
			}else if(url.equals("/mute_black"))
			{
				hl=new Html_M_Black().getHtml(content);
			}else if(url.equals("/addjinyan"))
			{
				hl=new Html_AddJinyan().getHtml(content);
			}else if(url.equals("/prop"))
			{
				hl=new Html_Prop().getHtml(content);
			}else if(url.equals("/juqing"))
			{
				hl=new Html_Juqing().getHtml(content);
			}else if(url.equals("/gold_dia"))
			{
				hl=new Html_GoldCount().getHtml(content);
			}else if(url.equals("/check_goldDia"))
			{
				hl=new Html_GoldCheck().getHtml(content);
			}else if(url.equals("/flowcount"))
			{
				hl=new Html_GodDiaFlow().getHtml(content);
			}else if(url.equals("/flowquery"))
			{
				hl=new Html_Flowquery().getHtml(content);
			}else if(url.equals("/deviceid"))
			{
				hl=new Html_Device().getHtml(content);
			}else if(url.equals("/sts_device"))
			{
				hl=new Html_Sts_Device().getHtml(content);
			}else if(url.equals("/aiyouxi"))
			{
				hl=new Html_AiYouXi().getHtml(content);
			}else if(url.equals("/reg"))
			{
				hl=new Html_reg().getHtml(content);
			}
			else if(url.equals("/task"))
			{
				hl=new Html_Task().getHtml(content);
			}
			else if(url.equals("/danji"))
			{
				hl=new Html_DanJI().getHtml(content);
			}
			else if(url.equals("/cup"))
			{
				hl=new Html_Cup().getHtml(content);
			}else if(url.equals("/360Login"))
			{
				hl = new Html_Test360Login().getHtml(content);
			}else if("/360notice".equals(url))
			{
				
				hl = new Html_360Notice().getHtml(content);
			}
		} catch (Exception e2) {
			Log.error("http:"+url+"   "+ e2.getMessage());
			e2.printStackTrace();
		}
		logger.warn("url:"+url+",length:"+hl.length());
		buffer.writeBytes(hl.getBytes(Charset.forName("utf-8")));
		response.setContent(buffer);    
		response.setHeader("Content-Type", "text/html; charset=UTF-8");    
		response.setHeader("Content-Length", response.getContent().writerIndex()); 
		response.setProtocolVersion(HttpVersion.HTTP_1_1);
		response.setChunked(false);
		Channel ch = e.getChannel(); 
		ch.write(response);
		logger.info("write http complete");
//		ch.disconnect();    
//		ch.close(); 
	}
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		super.channelDisconnected(ctx, e);
		logger.info("channelDisconnected!");
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		super.channelClosed(ctx, e);
		logger.info("channelClosed");
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		Channel ch = e.getChannel();   
		Throwable cause = e.getCause();   
		if (cause instanceof TooLongFrameException) {    
			sendError(ctx, HttpResponseStatus.BAD_REQUEST);
			return;    
		}
	}

	private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {    
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status);    
		response.setHeader("CONTENT_TYPE", "text/plain; charset=UTF-8");    
		response.setContent(ChannelBuffers.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));    
		// Close the connection as soon as the error message is sent.    
		ctx.getChannel().write(response).addListener(ChannelFutureListener.CLOSE);    
	}
	
	
	
		
   
}

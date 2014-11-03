package server.http;

import java.util.Calendar;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import business.conut.Sts_Notice;

import server.mj.MgsCache;
import server.mj.ServerTimer;


public class Html_adnotice implements IHtml {

	@Override
	public String getHtml(String content) {
		String msg="0";
		if(content.split("=").length>1){
	        String ct=content.split("=")[1];
	        String time=ServerTimer.getNowString();
	        
	        Sts_Notice notice = new Sts_Notice();
	        notice.setDay(time);
	        notice.setAbsDay(ServerTimer.distOfSecond(Calendar.getInstance()));
	        notice.setContentStr(ct);
	        MgsCache.getInstance().saveNotice(notice);
	        
	        msg="1&"+"<tr>"+
				  	  //"<td>"+notice.getId()+"</td>" +
				      "<td ui-table-priority-1>"+notice.getContentStr()+"</td>"+
				      "<td ui-table-priority-2>"+notice.getDay()+"</td>"+
				      "<td ui-table-priority-2>" +
				      "<a href=\"#\" onclick=\"f(this)\" data-role=\"button\" data-icon=\"delete\" "+
				      "data-iconpos=\"notext\" data-theme=\"c\" data-inline=\"true\" data-corners=\"true\" "+
				      "data-shadow=\"true\" data-iconshadow=\"true\" data-wrapperels=\"span\" title=\"\" "+
				      "class=\"ui-btn ui-shadow ui-btn-corner-all ui-btn-inline ui-btn-icon-notext ui-btn-up-c\">"+
	                  "<span class=\"ui-btn-inner\"><span class=\"ui-btn-text\"></span>"+
	                  "<span class=\"ui-icon ui-icon-delete ui-icon-shadow\">&nbsp;</span></span></a>"+
				      "</td>"+
				  	  "</tr>";
		}
		return msg;
	}

}

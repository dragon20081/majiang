package server.http;

import java.util.Calendar;
import java.util.List;

import server.mj.ServerTimer;

import business.CountDao;
import business.conut.Sts_Chat;
import business.conut.Sts_MuteBlacklist;

public class Html_FindChat implements IHtml{

	@Override
	public String getHtml(String content) {
		
		CountDao dao=new CountDao();
		int now = ServerTimer.distOfSecond(Calendar.getInstance());
		//查询出禁言名单
		String html="";
		  
		  String hlct=
				  
				  "<div data-role=\"content\">"+
						  "<form action=\"/findchat\" method=\"post\" rel=\"external\"  data-ajax=\"false\">"+
                       " <table width=\"80%\">"+
						    "<tr><td width=\"10%\">uid:</td><td><input name=\"uid\" value=\"\"/></td></tr>"+
						     "<tr><td width=\"10%\">name:</td><td><input name=\"name\" value=\"\"/></td></tr>"+
						    "<tr><td width=\"10%\">nickname:</td><td><input name=\"nickname\" value=\"\"/></td></tr>"+
						    "<tr><td width=\"10%\">start:</td><td><input name=\"start\" value=\"\"/></td></tr>"+
						    "<tr><td width=\"10%\">end:</td><td><input name=\"end\" value=\"\"/></td></tr>"+
						    "<tr><td colspan=\"2\"><button type=\"submit\">查询</button></td></tr>"+
						    "</table>"+
			        "</form>"+
	    "</div>";
		  //数据解析
		  
		  String contents[] = content.split("&");
		  
		  String uid="";
		  int uid1= -1;
		  String name= null;
		  String nickn= null;
		  String start="";
		  int st=0;
		  boolean stm=true;
		  String end="";
		  int ed=0;
		  boolean endm=true;
		  
      if(contents[0].split("=").length>1){
    	  uid=contents[0].split("=")[1];
    	  uid1=Integer.parseInt(uid);
      }
      if(contents[1].split("=").length>1){
    	  name=contents[1].split("=")[1];
      }
      if(contents[2].split("=").length>1){
    	  nickn=contents[2].split("=")[1];
      }
      if(contents[3].split("=").length>1){
    	  start=contents[3].split("=")[1];
      }
      if(contents[4].split("=").length>1){
    	  end=contents[4].split("=")[1];
      }
      
      stm=Ihead.check(start);
      endm=Ihead.check(end);
		  if(stm)
		   st=ServerTimer.distOfSecond(ServerTimer.getCalendarFromString(start));
		  
		  if(endm)
		   ed=ServerTimer.distOfSecond(ServerTimer.getCalendarFromString(end));
      System.out.println("stm-->"+stm);

		  Sts_Chat chat1=null;
		  List<Sts_Chat> cks=dao.findPlayerChats(uid1, name, nickn, st, ed);
		  Sts_MuteBlacklist mb = null;
		  String ckt="";
		  String skstr="";
		  for(int i=0;i<cks.size();i++){
			  chat1=cks.get(i);
			  if(mb == null)
				  mb = dao.findMute_Black(chat1.getNick());
			  
			  int jinyanday = 0;
			  String isJinyan = "否";
			  if(mb != null)
			  {
				  jinyanday = mb.getMuteDays() - now >0? mb.getMuteDays() - now : 0;
				  isJinyan = mb.isMute() ? "是":"否";
				  if(now == 0) isJinyan = "否";
			  }
			  skstr+="<tr>" +
			  		"<td>"+chat1.getDay()+"</td>" +
					"<td>"+chat1.getUid()+"</td>"+
			        "<td>"+chat1.getAccount()+"</td>"+
			        "<td>"+chat1.getNick()+"</td>"+
			        "<td>"+chat1.getChatMsg()+"</td>"+
			        "<td>"+ getDayStr(jinyanday)+"</td>"+
			        "<td>"+isJinyan+"</td>"+
			       // "<td><input name=\"time\" value=\"1\" /></td>"+
			        //"<td><a href=\"#\"  onclick=\"f(this)\" data-role=\"button\" data-icon=\"check\"" +
			        //" data-iconpos=\"notext\" data-theme=\"c\" data-inline=\"true\"></a></td>"+
			  		"</tr>";
		      }
		      ckt=
				  "<div align=\"center\" data-role=\"collapsible\">"+
	              "<h3 align=\"center\">指定玩家聊天记录</h3>"+
	              "<table data-role=\"table\" id=\"table-column-toggle3335\" " +
	              "data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'>" +
	              "<thead><tr>"+
	              "<th data-priority=\"1\">时间</th>" +
	              "<th data-priority=\"2\">uid</th>" +
	              "<th data-priority=\"3\">名称</th>" +
	              "<th data-priority=\"4\">呢称</th>" +
	              "<th data-priority=\"5\">说话内容</th>" +
	              "<th data-priority=\"8\">禁言天数</th>" +
	            //  "<th data-priority=\"9\">禁言</th>" +
	              "<th data-priority=\"10\">是否在禁言列表中</th>" +
	              "</thead><tbody>" + skstr + 
				  "</tbody></table>" + 
	              "</div>";
		      
		  Sts_Chat chat=null;
		  List<Sts_Chat> alllist=dao.find300LeatestChat();
		  String table="";
		  String tr="";
		  for(int i=0;i<alllist.size();i++){
			  chat= alllist.get(i);
			  tr+="<tr>" +
			  		"<td>"+chat.getDay()+"</td>" +
					"<td>"+chat.getUid()+"</td>"+
			        "<td>"+chat.getAccount()+"</td>"+
			        "<td>"+chat.getNick()+"</td>"+
			        "<td>"+chat.getChatMsg()+"</td>"+
			  		"</tr>";
		  }
		  table=
				  "<div align=\"center\" data-role=\"collapsible\">"+
	              "<h3 align=\"center\">所有聊天记录</h3>"+
	              "<table data-role=\"table\" id=\"table-column-toggle35\" " +
	              "data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'>" +
	              "<thead><tr>" +
	              "<th data-priority=\"2\">时间</th>" +
	              "<th data-priority=\"2\">UID</th>" +
	              "<th data-priority=\"3\">名称</th>" +
	              "<th data-priority=\"4\">呢称</th>" +
	              "<th data-priority=\"5\">说话内容</th>" +
	              "</thead><tbody>" + tr + 
				  "</tbody></table>" + 
	              "</div>";
		  
		  html+="<html>"+   
			      "<head>"+
			      "<meta content=\"notranslate\" name=\"google\">"+
			      "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"+
			      "<meta name='viewport' content='width=device-width, initial-scale=1'>"+
			      "<title>用户管理</title>"+
			      "<link rel=\"stylesheet\" href=\"http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.css\" />"+
			      "<script src=\"http://code.jquery.com/jquery-1.9.1.min.js\"></script>"+
			      "<script src=\"http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.js\"></script>"+
			      "</head>"+
			      "<body ><center>"+
			      "<script type=\"text/javascript\">"+
                "function f(id){"+
		            "var v=$(id).parent().prev().find(\"input\");"+
		            "var d=v.val();"+
		             
		            "var t=\"\";"+
                 "var n1=$(id).parent().prev().prev().prev().prev()." +
                 "prev();"+

		             "var n=n1.html();"+
		             
		             
		             "var k=0;"+
		              
		            "$.post(\"/addJinyan\", { name:n,time:t,type:k,dy:d}," +
	    			"function (data, textStatus){alert(data);},\"text\")"+
	            "}"+
		  "</script>"+
			      
			      "<div data-role=\"controlgroup\" data-type=\"horizontal\" data-mini=\"true\">"+
					Ihead.formhead()+
				  "</div>"+
				  hlct+ckt+table+"</center></body></html>";
		return html;
	}
	
	public String getDayStr(int dayNum)
	{
		if(dayNum <= 0)return "0天0时0分0秒";
		int miao = dayNum % 60;
		int fen = dayNum/60 %60;
		int xiaoshi = dayNum/60/60%24;;
		int tian = dayNum/24/60/60;
		return  tian+"天 "+xiaoshi+"时 "+ fen+"分 " + miao+"秒";
	}

}

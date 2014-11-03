package server.http;

import java.util.List;

import business.CountDao;
import business.conut.Sts_Chat;

public class Html_Chat implements IHtml {

	@Override
	public String getHtml(String content) {
		String html="";
		  
		  String hlct=
				  "<div data-role=\"content\">"+
				  "<form action=\"/findchat\" method=\"post\" rel=\"external\"  data-ajax=\"false\">"+
               " <table width=\"80%\">"+    
				    "<tr><td width=\"10%\">uid:</td><td ><input name=\"uid\" value=\"\"/></td></tr>"+
				     "<tr><td width=\"10%\">name:</td><td><input name=\"name\" value=\"\"/></td></tr>"+
				    "<tr><td width=\"10%\">nickname:</td><td><input name=\"nickname\" value=\"\"/></td></tr>"+
				    "<tr><td width=\"10%\">start:</td><td><input name=\"start\" value=\"\"/></td></tr>"+
				    "<tr><td width=\"10%\">end:</td><td><input name=\"end\" value=\"\"/></td></tr>"+
				    "<tr><td colspan=\"2\"><button type=\"submit\">查询</button></td></tr>"+
				    "</table>"+
	        "</form>"+
	    "</div>";
				  
		  String table="";
		  
		  CountDao dao=new CountDao();
		  Sts_Chat chat=null;
		  List<Sts_Chat> alllist=dao.find300LeatestChat();
		  
		  String tr="";
		  for(int i=0;i<alllist.size();i++){
			  chat= alllist.get(i);
			  tr+="<tr>" +
					"<td>"+chat.getDay()+"</td>"+
					"<td>"+chat.getUid()+"</td>"+
			        "<td>"+chat.getAccount()+"</td>"+
			        "<td>"+chat.getNick()+"</td>"+
			        "<td>"+chat.getChatMsg()+"</td>"+
			        //"<td>"+chat.getCttime()+"</td>"+
			  		"</tr>";
		  }
		  table=
				  "<div align=\"center\" data-role=\"collapsible\">"+
	              "<h3 align=\"center\">所有聊天记录</h3>"+
	              "<table data-role=\"table\" id=\"table-column-toggle35\" " +
	              "data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'>" +
	              "<thead>" +
	              "<tr>" +
	              "<th data-priority=\"6\">时间</th>" +
	              "<th data-priority=\"2\">uid</th>" +
	              "<th data-priority=\"3\">名称</th>" +
	              "<th data-priority=\"4\">呢称</th>" +
	              "<th data-priority=\"5\">说话内容</th>" +
	              "</tr>"+
	              "</thead>" +
	              "<tbody>" + tr + 
				  "</tbody></table>" + 
	              "</div>";
		  
		  html+=Ihead.gethtmlHead()+hlct+table+"</center></body></html>";
		return html;
	}

}
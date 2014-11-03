package server.http;

public class Html_User_Recharge_Before implements IHtml{

	//1当日玩家充值记录
	//2制定日期玩家充值记录
	//3玩家总的充值记录
	//4当月充值记录
	//5所有月份充值记录
	//6根据用户昵称 用户名 或者用户ID查询玩家详细充值记录
	@Override
	public String getHtml(String content) {
		
		//四个按钮
		String head = chargeHead();
		return head;
	}
	public String chargeHead()
	{
		   String addnotice=
				   "<table width=\"100%\" align=\"center\">"+
		   "<tr><td align=\"center\">充值信息查询:</td></tr>"+
		   "<td  colspan=\"2\"><button onclick=\"nt()\" value=\"当日充值\"/></td>" +
		   "<td  colspan=\"2\"><button onclick=\"nt()\" value=\"指定日期\"/></td>" +
		   "<td  colspan=\"2\"><button onclick=\"nt()\" value=\"总充值记录\"/></td>" +
		   "<td  colspan=\"2\"><button onclick=\"nt()\" value=\"当月充值\"/></td>" +
		   "<td  colspan=\"2\"><button onclick=\"nt()\" value=\"所有月份\"/></td> </tr>" +
		   "</table>"+
		   
        "<table data-role=\"table\" id=\"tablqwe12\" " +
        "data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'>" +
        "<thead>" +
        "<tr>单人信息查询:</tr>"+
        "<tr>" +
        "<th data-priority=\"1\">UID</th>" +
        "<th data-priority=\"2\">账号</th>" +
        "<th data-priority=\"2\">昵称</th>" +
        "<th data-priority=\"2\"> </th>" +
        "</tr>"+
        "</thead>" +
        "<tbody>" + 
		   "<tr>" +
		   "<td><input id=\"nick_0\" value=\"\"/></td>" +
		   "<td><input id=\"uid_0\" value=\"\"/></td>"+
		   "<td><input id=\"account_0\" value=\"\"/></td>"+
		   "<td colspan=\"2\"><button onclick=\"nt()\" value=\"点击查询\"/></td></tr>" +
		  "</tbody></table>"; 
		   ;
		   
		   String msg=
		   "<html>"+   
	      "<head>"+
	      "<meta content=\"notranslate\" name=\"google\">"+
	      "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"+
	      "<meta name='viewport' content='width=device-width, initial-scale=1'>"+
	      "<title>公告管理</title>"+
	      "<link rel=\"stylesheet\" href=\"http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.css\" />"+
	      "<script src=\"http://code.jquery.com/jquery-1.9.1.min.js\"></script>"+
	      "<script src=\"http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.js\"></script>"+
	      "</head>"+
	      "<body ><center>"+
	      "<script type=\"text/javascript\">"+
           "function f(id){"+
	            "}"+
           "function nt(){"+
	            "}"+
		  "</script>"+
	      "<div data-role=\"controlgroup\" data-type=\"horizontal\" data-mini=\"true\">"+
		   Ihead.formhead()+
		  "</div>" +addnotice+
		  "</center></body></head></html>";
		return msg;
	}

}

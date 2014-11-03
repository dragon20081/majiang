package server.http;

import java.util.List;

import business.CountDao;
import business.conut.Sts_Chat;
 
/**
 * 聊天信息查看
 * @author xue
 *
 */
public class Html_Chat_Before implements IHtml{

	//1 查看前300条聊天记录
	//查看某人的聊天记录
	@Override
	public String getHtml(String content) {
		
		CountDao cdao = new CountDao();
		String htmlhead= Ihead.getHead3();
		List<Sts_Chat> list= cdao.find300LeatestChat();
		Sts_Chat chat = null;
		String ajax="";
		ajax="<script type=\"text/javascript\">"+
        "function f(){"+
             "var id=$(\"#search-1\").val();"+
             
            "$.post(\"/chatid\", {uid:id}," +
		     "function (data, textStatus){" +
               
		      "if(data==\"0\"){"+
		         "alert(\"没有该用户\");" +
		      "}else{"+
		         
		        "$(\"#body\").html(data);"+
		   "}"+
             
		   "},\"text\")"+
        "}"+
        "function f1(){"+
        "var id=$(\"#search-2\").val();"+
        
       "$.post(\"/Rechargeidday\", {uid:id}," +
	     "function (data, textStatus){" +
          
	      "if(data==\"0\"){"+
	         "alert(\"没有该用户\");" +
	      "}else{"+
	         
	        "$(\"#bodyday\").html(data);"+
	   "}"+
        
	   "},\"text\")"+
   "}"+ 
       "</script>";
		
		
		
		String tr = "";
		for(int i=0;i<list.size();i++){
			chat= list.get(i);
			tr+="<tr>"+
			    "<td>"+chat.getDay()+"</td>"+
			    "<td>"+chat.getUid()+"</td>"+
			    "<td>"+chat.getAccount()+"</td>"+
			    "<td>"+chat.getNick()+"</td>"+
			    "<td>"+chat.getChatMsg()+"</td>"+
			"</tr>";
		}
		 String table =
				  "<div align=\"center\" data-role=\"collapsible\">"+
			              "<h3 align=\"center\">所有聊天记录</h3>"+
						  		  
			              "<table data-role=\"table\" id=\"award-column-toggle01\" " +
			              "data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'>" +
			              "<thead>" +
			              "<tr>"+
			              "<th data-priority=\"1\">时间</th>" +
			              "<th data-priority=\"2\">UID</th>" +
			              "<th data-priority=\"3\">账号</th>" +
			              "<th data-priority=\"4\">昵称</th>" +
			              "<th data-priority=\"5\">消息</th>" +
			              "</tr>"+
			              "</thead>" +
			              "<tbody>" + tr + 
						  "</tbody></table></div>";
		 
		 tr = "";
			String all2="<div>"+
					 
				  "<div align=\"center\" data-role=\"collapsible\">"+
	              "<h3 align=\"center\">查询玩家聊天记录  Example  uid:1 name:ffx nick:ffxxx</h3>"+
                 
	              "<input onchange=\"f1()\" type=\"search\" name=\"search-1\" id=\"search-1\" value=\"\">"+
	              "<table data-role=\"table\" id=\"award-column-toggle04\" " +
	              "data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'>" +
	              "<thead>" +
	              "<tr>"+
	              "<th data-priority=\"1\">时间</th>" +
	              "<th data-priority=\"2\">UID</th>" +
	              "<th data-priority=\"3\">账号</th>" +
	              "<th data-priority=\"4\">昵称</th>" +
	              "<th data-priority=\"5\">消息</th>" +
	              "</tr>"+
	              "</thead>" +
	              "<tbody id=\"bodyday\">"+
				  "</tbody></table>"+
				  "</div></div>";
		 
	       String html_top="</body ></center></html>";
	        
			return htmlhead+table+html_top;
	}

}

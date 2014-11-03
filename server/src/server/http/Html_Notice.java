package server.http;

import java.util.List;

import business.conut.Sts_Notice;

import server.mj.MgsCache;


public class Html_Notice implements IHtml{

	@Override
	public String getHtml(String content) {

		   List<Sts_Notice> rt= MgsCache.getInstance().notices;
		   Sts_Notice notice = null;
		   String addnotice=
				   "<table width=\"100%\" align=\"center\">"+
		   "<tr><td align=\"center\">公告内容:</td></tr>"+
		   "<tr><td><input id=\"ct\" value=\"\"/></td></tr>"+
		   "<tr><td  colspan=\"2\"><button onclick=\"nt()\" value=\"增加公告\"/></td></tr>"+
		   "</table>";
		  String tr="";
		  if(rt!=null){
     for(int i=0;i<rt.size();i++){
    	  			notice=rt.get(i);
				  tr+="<tr>"+
				  	  //"<td>"+notice.getId()+"</td>" +
				      "<td>"+notice.getContentStr()+"</td>"+
				      "<td>"+notice.getDay()+"</td>"+
				      "<td><a href=\"#\" onclick=\"f(this)\"  data-role=\"button\" data-icon=\"delete\" " +
				      "data-iconpos=\"notext\" data-theme=\"c\" data-inline=\"true\"></a></td>"+
				  	  "</tr>";
		   }
		  }
		   String table=
					      "<div align=\"center\" data-role=\"collapsible\">"+
				              "<h3 align=\"center\">公告记录</h3>"+
				              "<table data-role=\"table\" id=\"table-column-notice36\" " +
				              "data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'>" +
				              "<thead><tr>" +
				              "<th data-priority=\"1\">公告内容</th>" +
				              "<th data-priority=\"2\">公告时间</th>" +
				              "<th data-priority=\"2\">去除公告</th>" +
				              "</thead><tbody id=\"td\">" + tr + 
							  "</tbody></table>" + 
				          "</div>";
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
           "var v=$(id).parent().prev();"+
				"var n=v.html();"+
            
				"$.post(\"/denotice\", {time:n}," +
					"function (data, textStatus){" +
				     "window.location.reload();"+ 
				"},\"text\")"+
	            "}"+
				 
           "function nt(){"+
               
	         	   "var ct=$(\"#ct\").val();"+
	         	   "$.post(\"/adnotice\", {content:ct}," +
	         				"function (data, textStatus){" +
	         				"if(data!=\"0\"){"+
	         				   "var arr=data.split(\"&\");"+
	    			            "$(\"#td\").html(($(\"#td\").html())+arr[1]);"+
	         				"}else{"+
	         					"alert(\"增加失败!\");"+
	         				"}"+
	         			"},\"text\")"+
	            "}"+
		  "</script>"+
	      "<div data-role=\"controlgroup\" data-type=\"horizontal\" data-mini=\"true\">"+
		   Ihead.formhead()+
		  "</div>" +
		   addnotice+table+
		  "</center></body></html>";

		return msg;
	}

}

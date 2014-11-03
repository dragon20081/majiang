package server.http;

public class Html_manage_User implements IHtml {

	public String getHtml(String content) {
		// TODO Auto-generated method stub
		 String manage=
			      //"<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 5 Transitional//EN\" \"http://www.w3.org/TR/html5/loose.dtd\">"+	  
			      "<html>"+
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
			      "<div data-role=\"controlgroup\" data-type=\"horizontal\" data-mini=\"true\">"+
					Ihead.formhead()+
				  "</div>"+
                   "<form action='/miracle_manage' method=\"post\" rel=\"external\"  data-ajax=\"false\">"+
			             "<label class=\"custom\" for=\"text-1\">用户id</label>"+
			             "<input class=\"custom\" type=\"text\" data-clear-btn=\"true\" name=\"nameid\" id=\"nameid\" value=\"\" ><br/>"+
			             
			             "<label class=\"custom\" for=\"text-3\">名字</label>"+
			             "<input class=\"custom\" type=\"text\" data-clear-btn=\"true\" name=\"name\" id=\"name\" value=\"\"  ><br/>"+
			             "<label class=\"custom\" for=\"text-3\">昵称</label>"+
			             "<input class=\"custom\" type=\"text\" data-clear-btn=\"true\" name=\"nick\" id=\"nick\" value=\"\"  ><br/>"+
			             "<button type=\"submit\">查询</button>"+
			         "</form></center>"+ 
			      "</body>"+
			      "</html>";
		return manage;
	}

}

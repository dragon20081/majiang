package server.http;

import java.util.Calendar;
import java.util.List;

import server.mj.ServerTimer;

import business.CountDao;
import business.conut.Sts_MuteBlacklist;

/**
 *  禁言 和黑名单列表
 * @author xue
 *
 */
public class Html_M_Black implements IHtml {

	@Override
	public String getHtml(String content) {
		
		 int nowDay = ServerTimer.distOfSecond(Calendar.getInstance());
		  String message="";
		  CountDao cdao=new CountDao();
		  List<Sts_MuteBlacklist> bk=cdao.findBlackLit();
		  Sts_MuteBlacklist blackList=null;
		  
		  List<Sts_MuteBlacklist> noall=cdao.findMuteLit();
		  Sts_MuteBlacklist bannedList=null;
		  
		  String bktbody="";
		  
		  if(bk!=null){
		  for(int i=0;i<bk.size();i++){
			  blackList= bk.get(i);
//			  double syts=ServerTimer.distOfDaydouble
//					  (ServerTimer.getCalendarFromString(blackList.getSt()))
//					  -(ServerTimer.distOfDaydouble(ServerTimer.getCalendarFromString(ServerTimer.getNowString())));
//			  if(syts<0) syts=0;
			  int blackDayLeft = (blackList.getBlackDays() -nowDay) > 0?blackList.getBlackDays() -nowDay: 0;
			  
			  bktbody+="<tr>" +
				  		//"<td>"+blackList.getId()+"</td>" +
				  		"<td>"+blackList.getNick()+"</td>" +
				  		"<td>"+ getDayStr(blackDayLeft)+"</td></tr>";
		  }
		  }
		  String back=
					"<div data-role=\"collapsible-set\" data-inset=\"false\">"+
							"<div align=\"center\" data-role=\"collapsible\">"+
				//  "<div align=\"center\" data-role=\"collapsible\">"+
	                "<h3 align=\"center\">黑名单</h3>"+
	                "<table data-role=\"table\" id=\"table-column-toggle33\" data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'><thead>" +
	                "<tr>" +
	                //"<th data-priority=\"1\">id</th>" +
	                "<th data-priority=\"2\">名称</th>" +
	                //"<th data-priority=\"3\">黑名单到期时间</th>" +
	                "<th data-priority=\"4\">剩余时间</th></thead><tbody id=\"back\">" + bktbody + 
					"</tbody></table>" + 
	                "</div>";
		  
		  String nosh="";
		  
		  Calendar c=Calendar.getInstance();
		  
		  if(noall!=null){
		  for(int i=0;i<noall.size();i++){
			  bannedList= noall.get(i);
			  
//			  double syts=ServerTimer.distOfDaydouble
//					  (ServerTimer.getCalendarFromString(bannedList.getSt()))
//					  -(ServerTimer.distOfDaydouble(ServerTimer.getCalendarFromString(ServerTimer.getNowString())));
//			  if(syts<0) syts=0;
			  int muteDayLeft = (bannedList.getMuteDays() -nowDay) > 0?bannedList.getMuteDays() -nowDay: 0;
			  nosh+="<tr>" +
			  		//"<td>"+bannedList.getId()+"</td>" +
			  		"<td>"+bannedList.getNick()+"</td>" +
			  		//"<td>"+bannedList.getSt()+"</td>" +
			  		"<td>"+getDayStr(muteDayLeft)+"</td></tr>";
		  }
		  }
		  String notak=
				  
					"<div data-role=\"collapsible-set\" data-inset=\"false\">"+
							"<div align=\"center\" data-role=\"collapsible\">"+
				 // "<div align=\"center\" data-role=\"collapsible\">"+
	                "<h3 align=\"center\">禁言</h3>"+
	                "<table data-role=\"table\" id=\"table-column-toggle34\" data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'><thead><tr>" +
	                //"<th data-priority=\"1\">id</th>" +
	                "<th data-priority=\"2\">" +
	                "名称</th>" +
	                //"<th data-priority=\"3\">禁言结束时间</th>" +
	                "<th data-priority=\"4\">剩余时间</th></thead><tbody id=\"jy\">" + nosh + 
					"</tbody></table>" + 
	              "</div>";
		  
		  
		  String htmlct=
				  "<div data-role=\"content\">"+
	       
	        "<div class=\"ui-grid-a\">"+
	        
				"<div class=\"ui-block-a\">禁言天数:</div>"+
				"<div class=\"ui-block-b\">"+
				"<input name=\"day\" value=\"1\"/>"+
				"</div>"+
				
	            "<div class=\"ui-block-a\">禁言名字:</div>"+
	            "<div class=\"ui-block-b\">"+
	            "<input name=\"name\"/>"+
	            "</div>"+
	            "<div class=\"ui-block-a\"></div>"+
	            "<div class=\"ui-block-b\">"+
	            " <input type=\"hidden\" name=\"time\"/>"+
	            "</div>"+
	            "<div class=\"ui-block-a\">"+
	                "<button onclick=\"f(this)\" type=\"submit\">禁言</button>"+  
	            "</div>"+
	            "<div class=\"ui-block-b\">"+
	                "<button onclick=\"f1(this)\" type=\"submit\">加黑名单</button>"+
	            "</div>"+
	        "</div>" +
	        "</div>"+back+notak;
		     
		 
	      message=
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
	    			      "<script type=\"text/javascript\">"+
		                        "function f(id){"+
		                       
					            "var v=$(id).parent().parent().prev().prev().prev().find(\"input\");"+
					            "var n=v.val();"+
					            "var v1=$(id).parent().parent().prev().find(\"input\");"+
					            "var t=v1.val();"+
					            "var k=0;"+
					            
					             "var day=$(id).parent().parent().prev().prev().prev().prev().prev().find(\"input\");"+
					             "var d=day.val();"+
					             
					             
					             
					            "$.post(\"/addjinyan\", { name:n,time:t,type:k,dy:d}," +
					            
				    			"function (data, textStatus){" +
				    			
				    			    "if(data!=0){"+
				    			       "window.location.reload();"+ 
				    			    "}else{"+
				    			    	
				    			    "}"+
				    			  
				    			"},\"text\")"+
				    	         
				            "}"+
				           "function f1(id){"+
				           
				        	 "var v=$(id).parent().parent().prev().prev().prev().prev().find(\"input\");"+
				        	 "var n=v.val();"+
				             "var v1=$(id).parent().parent().prev().prev().find(\"input\");"+
				             "var t=v1.val();"+
				             "var k=1;"+
				             
                          "var day=$(id).parent().parent().prev().prev().prev().prev().prev().prev().find(\"input\");"+
				        	 "var d=day.val();"+
				        	 
				             "$.post(\"/addjinyan\", { name:n,time:t,type:k,dy:d}," +
				    			"function (data, textStatus){" +
				                
								    "window.location.reload();"+ 
				    			"},\"text\")"+
				          "}"+
	    				  "</script>"+
	    			      "<div data-role=\"controlgroup\" data-type=\"horizontal\" data-mini=\"true\">"+
	    					Ihead.formhead()+
	    				  "</div>"+
	    		          htmlct+"</center></body></html>";
	      
		return message;
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

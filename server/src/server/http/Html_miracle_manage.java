package server.http;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import business.Business;
import business.entity.MJ_Role;
import business.entity.MJ_User;
import business.entity.M_Prop;

public class Html_miracle_manage implements IHtml {

	public String getHtml(String content) {
		// TODO Auto-generated method stub
		String checkinfo="";
		
		int uid=-1;
		String contents[] = content.split("&");
		if(contents[0].length()>7){
		   uid=Integer.parseInt(contents[0].substring(7));
		}
		String name="";
		if(contents[1].length()>5){
		        name=contents[1].substring(5);
		}
		String nick="";
		//System.out.println(contents[1].length());
		if(contents[2].length()>5){
				nick=contents[2].substring(5);}
		
		MJ_User user=null;
		Business userinfo=new Business();
		Integer cxid=uid;
		List<MJ_User> list=null;
		

		if(uid != -1)user=userinfo.findPlayerByUid(uid);
		
		if(user==null){
			if(!"".equals(name))user=userinfo.findPlayerByName(name);
		}
        if(nick.length()>0)
	    list=userinfo.findPlayerLikeNick(nick);;
        
        if(list==null&&user!=null){
        	list=new ArrayList<MJ_User>();
        	list.add(user);
        }else if(list!=null&&user!=null){
        	
        	boolean contained = false;
        	for(int i=0;i<list.size();i++){
        		MJ_User u1=(MJ_User)list.get(i);
        		 if(user.getName().equals(u1.getName())){
        			 contained  = true;
        			 break;
        		 }
        	}
        	if(!contained)list.add(user);
        }
		
		String fail="没有此用户信息";
		String success="";
		
        				
		checkinfo=
		  "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 5 Transitional//EN\" \"http://www.w3.org/TR/html5/loose.dtd\">"+
	      "<html>"+
	      "<head>"+
	      "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"+
	      "<meta name='viewport' content='width=device-width, initial-scale=1'>"+
	      "<title>查询用户信息</title>"+
	      "<link rel=\"stylesheet\" href=\"http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.css\" />"+
	      "<script src=\"http://code.jquery.com/jquery-1.9.1.min.js\"></script>"+
	      "<script src=\"http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.js\"></script>"+
          "<script type=\"text/javascript\">"+
	     "function f(id){"+
           
	        "var v=$(id).parent().parent().prev().find(\"input\");"+
	        "v.attr(\"readonly\",false);"+
	     "}"+
	    "function f1(id,userfiled,userid){"+
	        "var v=$(id).parent().parent().prev().find(\"input\");"+
	        
	        "var v1=v.val();"+
	    	"v.attr(\"readonly\",true);"+
	    	"$.post(\"/user_update\", { id:userid, name:userfiled,value:v1}," +
	    			"function (data, textStatus){alert(data);},\"text\")"+
	    	 "}"+
	    			
	     "</script>"+
	      "</head>"+
	    
	      "<body><center>"+
		"<script type=\"text/javascript\">"+
	     "function f(id){"+
          
	        "var v=$(id).parent().parent().prev().find(\"input\");"+
	        "v.attr(\"readonly\",false);"+
	     "}"+
	    "function f1(id,userfiled,userid){"+
	        "var v=$(id).parent().parent().prev().find(\"input\");"+
	        
	        "var v1=v.val();"+
	    	"v.attr(\"readonly\",true);"+
	    	"$.post(\"/user_update\", { id:userid, name:userfiled,value:v1}," +
	    			"function (data, textStatus){alert(data);},\"text\")"+
	    	 "}"+
	     "</script>"+

           
	     "<div data-role=\"controlgroup\" data-type=\"horizontal\" data-mini=\"true\">"+
//			"<a href=\"#\" data-role=\"button\" data-icon=\"plus\" data-theme=\"b\">Add</a>"+
//		    "<a href=\"#\" data-role=\"button\" data-icon=\"delete\" data-theme=\"b\">Delete</a>"+
//		    "<a href=\"#\" data-role=\"button\" data-icon=\"grid\" data-theme=\"b\">More</a>"+
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
         "</form>";
//	      "<div data-role=\"header\">"+
//	      "<a href=/manage_user data-icon=\"back\">返回</a>"+
//	      "<h1></h1>"+
//	     "</div>";
	      
		
	    
		if(list==null){
			
			checkinfo=checkinfo+
				    
				      "没有此用户信息--->"+
				      "</center></body>"+
				      "</html>";
		}else {
			
			String userNum="";
			for(int i=0;i<list.size();i++){
			//	userNum=userNum+Ihead.getMJ_UserData(list.get(i));
				userNum=userNum+getUserInfo(list.get(i));
			}
			success=
			   userNum+

			  "</center></body>"+
			  "</html>";
			   checkinfo=checkinfo+success;
		}
		return checkinfo;
	}
	public String getUserInfo(MJ_User user)
	{
		String table1 = "";
		String tr1 = "";
		
		 tr1+="<tr>" +
					"<td>"+user.getUid()+"</td>"+
			        "<td>"+user.getName()+"</td>"+
			        "<td>"+user.getNick()+"</td>"+
			        "<td>"+user.getGold()+"</td>"+
			        "<td>"+user.getDianQuan()+"</td>"+
			        "<td>"+user.getJuqing_1()+"</td>"+
			        "<td>"+user.getJuqing_2()+"</td>"+
			        "<td>"+user.getJuqing_3()+"</td>"+
			        "<td>"+user.getLevel()+"</td>"+
			        "<td>"+user.getScore()+"</td>"+
			        "<td>"+user.getImage()+"</td>"+
			        "<td>"+user.getOwnSkills()+"</td>"+
			        "<td>"+user.getEquipSkill()+"</td>"+
			        "<td>"+user.getRegTime()+"</td>"+
			        "<td>"+user.getLastLogin()+"</td>"+

			 "</tr>";
		table1= 
	              "<h3 align=\"center\">玩家信息</h3>"+
	              "<table data-role=\"table\" id=\"tablqwe12\" " +
	              "data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'>" +
	              "<thead>" +
	              "<tr>" +
	              "<th data-priority=\"1\">UID</th>" +
	              "<th data-priority=\"2\">账号</th>" +
	              "<th data-priority=\"3\">昵称</th>" +
	              "<th data-priority=\"4\">金币</th>" +
	              "<th data-priority=\"5\">钻石</th>" +
	              "<th data-priority=\"5\">剧情1</th>" +
	              "<th data-priority=\"5\">剧情2</th>" +
	              "<th data-priority=\"5\">剧情3</th>" +
	              "<th data-priority=\"5\">等级</th>" +
	              "<th data-priority=\"5\">积分</th>" +
	              "<th data-priority=\"5\">角色</th>" +	      
	              "<th data-priority=\"5\">拥有技能</th>" +
	              "<th data-priority=\"5\">装备技能</th>" +
	              "<th data-priority=\"5\">注册时间</th>" +
	              "<th data-priority=\"5\">上次登陆时间</th>" +
	              "</tr>"+
	              "</thead>" +
	              "<tbody>" + tr1 + 
				  "</tbody></table>";
		
		
		 Set<MJ_Role> roles = user.getRoles();
		
			String table2 = "";
			String tr2 = "";
			Iterator<MJ_Role> it = roles.iterator();
			while(it.hasNext())
			{
				MJ_Role role = it.next();
				tr2+="<tr>" +
						"<td>"+role.getId()+"</td>"+
						"<td>"+role.getRoleId()+"</td>"+
						"<td>"+role.getLevel()+"</td>"+
						"</tr>";
			}
			table2= 
		              "<h3 align=\"center\">玩家角色信息</h3>"+
		              "<table data-role=\"table\" id=\"tablqwe13\" " +
		              "data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'>" +
		              "<thead>" +
		              "<tr>" +
		              "<th data-priority=\"1\">数据库ID</th>" +
		              "<th data-priority=\"2\">roleId</th>" +
		              "<th data-priority=\"3\">等级</th>" +
		              "</tr>"+
		              "</thead>" +
		              "<tbody>" + tr2 + 
					  "</tbody></table>";
			String table3 = "";
			String tr3 = "";
			M_Prop prop = user.getProps();
			tr3+="<tr>" +
					"<td>"+prop.getPro1Num()+"</td>"+
					"</tr>";
			table3= 
		              "<h3 align=\"center\">道具信息</h3>"+
		              "<table data-role=\"table\" id=\"tablqwe14\" " +
		              "data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'>" +
		              "<thead>" +
		              "<tr>" +
		              "<th data-priority=\"1\">喇叭</th>" +
		              "</tr>"+
		              "</thead>" +
		              "<tbody>" + tr3 + 
					  "</tbody></table>";
			
			   String user1=
					   "<div data-role=\"collapsible\" data-theme=\"b\" data-content-theme=\"d\" data-inset=\"false\">"+
			        "<h3>"+user.getName()+"</h3>"+  
			        table1 + table2 + table3+
			       "</div>";
		return user1;
	}

}

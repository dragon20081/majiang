package server.http;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import business.entity.MJ_User;

public class Ihead {
     public static String getHtmlhead1(){
		
 		String head=  "<html>"+
			      "<head>"+
			      "<meta content=\"notranslate\" name=\"google\">"+
			      "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"+
			      "<meta name='viewport' content='width=device-width, initial-scale=1'>"+
			      "<title>User Manange</title>"+
			      "<link rel=\"stylesheet\" href=\"http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.css\" />"+
			      "<script src=\"http://code.jquery.com/jquery-1.9.1.min.js\"></script>"+
			      "<script src=\"http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.js\"></script>"+
			      "</head>"+
			      "<body ><center>";
		return head;
	 }

	public static String gethtmlHead() {
		// TODO Auto-generated method stub
		
		String htmlhead=  
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
					formhead()+
				  "</div>";
		return htmlhead;
	}
	
	 public static String formhead(){
			
			String form=getForm(1)+getForm(2)+getForm(3)+getForm(4)+getForm(5)+
				    getForm(6)+getForm(7)+getForm(8)+getForm(9)+getForm(10)+
				    getForm(11)+getForm(12)+getForm(13)+getForm(14)+getForm(15)+getForm(16)+getForm(17);
			return form;
		}
	    
	    public static String getForm(int type){
			String form = "";
			switch (type) {
			case 1:
				form ="<a href=\"/manage_user\" data-role=\"button\" data-icon=\"\" data-theme=\"b\" rel=\"external\"  data-ajax=\"false\">用户信息查询</a>";
				break;
			case 2:
				form ="<a href=\"/user_online\" data-role=\"button\" data-icon=\"\" data-theme=\"b\" rel=\"external\"  data-ajax=\"false\">在线统计</a>";
				break;
			case 3:
				form ="<a href=\"/notice\" data-role=\"button\" data-icon=\"\" data-theme=\"b\" rel=\"external\"  data-ajax=\"false\">公告管理</a>";
				break;	
			case 4:
				form ="<a href=\"/user_recharge\" data-role=\"button\" data-icon=\"\" data-theme=\"b\" rel=\"external\"  data-ajax=\"false\">充值信息</a>";
				break;
			case 5:
				form ="<a href=\"/arpu\" data-role=\"button\" data-icon=\"\" data-theme=\"b\" rel=\"external\"  data-ajax=\"false\">ARPU</a>";
				break;
			case 6:
				form ="<a href=\"/chat\" data-role=\"button\" data-icon=\"\" data-theme=\"b\" rel=\"external\"  data-ajax=\"false\">聊天记录查看</a>";
				break;
			case 7:
				form ="<a href=\"/mute_black\" data-role=\"button\" data-icon=\"\" data-theme=\"b\" rel=\"external\"  data-ajax=\"false\">黑名单聊天管理</a>";
				break;
			case 8:
				form ="<a href=\"/flowcount\" data-role=\"button\" data-icon=\"\" data-theme=\"b\" rel=\"external\"  data-ajax=\"false\">金币钻石流动统计</a>";
				break;

			case 9:
				form ="<a href=\"/prop\" data-role=\"button\" data-icon=\"\" data-theme=\"b\" rel=\"external\"  data-ajax=\"false\">道具统计</a>";
				break;
			case 10:
				form="<a href=\"/juqing\" data-role=\"button\" data-icon=\"\" data-theme=\"b\" rel=\"external\"  data-ajax=\"false\">剧情进度统计</a>";
				break;	
			case 11:
				form="<a href=\"/rate\" data-role=\"button\" data-icon=\"\" data-theme=\"b\" rel=\"external\"  data-ajax=\"false\">每日返回率</a>";
				break;
			case 12:
				form="<a href=\"/gold_dia\" data-role=\"button\" data-icon=\"\" data-theme=\"b\" rel=\"external\"  data-ajax=\"false\">服务器金币统计</a>";
				break;
			case 13:
				form="<a href=\"/check_goldDia\" data-role=\"button\" data-icon=\"\" data-theme=\"b\" rel=\"external\"  data-ajax=\"false\">金币钻石异常检测</a>";
				break;
			case 14:
				form="<a href=\"/sts_device\" data-role=\"button\" data-icon=\"\" data-theme=\"b\" rel=\"external\"  data-ajax=\"false\">登录设备统计</a>";
				break;
			case 15:
				form="<a href=\"/task\" data-role=\"button\" data-icon=\"\" data-theme=\"b\" rel=\"external\"  data-ajax=\"false\">任务统计</a>";
				break;
			case 16:
				form="<a href=\"/danji\" data-role=\"button\" data-icon=\"\" data-theme=\"b\" rel=\"external\"  data-ajax=\"false\">人机统计</a>";
				break;
			case 17:
				form="<a href=\"/cup\" data-role=\"button\" data-icon=\"\" data-theme=\"b\" rel=\"external\"  data-ajax=\"false\">杯赛统计</a>";
				break;
			}
			return form;
		}

    /**
     * 检查时间格式是否正确
     * @param timevalue
     * @return
     */
	public static boolean check(String timevalue) {
		// TODO Auto-generated method stub
		  boolean mark=true;
		  
		  Calendar c = Calendar.getInstance();
		  SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	         
		  try{
			  c.setTime(s.parse(timevalue));
		   }catch (Exception e) {
			// TODO: handle exception
			  mark=false;
		  }
		  
		  if(mark==false){
			  
			  SimpleDateFormat s1=new SimpleDateFormat("yyyy-MM-dd");
			  mark=true;
			  try{
				  c.setTime(s1.parse(timevalue));
			   }catch (Exception e) {
				// TODO: handle exception
				  mark=false;
				  
			  }
			  
		  }
		  
		  if(mark==false){
			  
              SimpleDateFormat s1=new SimpleDateFormat("yyyy:MM:dd");
			  mark=true;
			  try{
				  c.setTime(s1.parse(timevalue));
			   }catch (Exception e) {
				// TODO: handle exception
				  mark=false;
				 
			  }
		  }
		  return mark;
		
	}
    public static String getMJ_UserData(MJ_User user){
		   
		   String user1=
		   "<div data-role=\"collapsible\" data-theme=\"b\" data-content-theme=\"d\" data-inset=\"false\">"+
        "<h3>"+user.getName()+"</h3>"+  
          getProperties(user)+
       "</div>";
		   return user1;
	}
    public static String getProperties(MJ_User user){
		
		  String alltabls="";
		  Method[] ff=user.getClass().getDeclaredMethods();
		  ArrayList<Method> get=new ArrayList<Method>();
		  for(int j=0;j<ff.length;j++){
			  if(ff[j].getName().substring(0,3).equals("set")||
					  ff[j].getName().equals("getSkillStatus")||
					  ff[j].getName().equals("getShopItems")||
					  ff[j].getName().equals("getProps")||
					  ff[j].getName().equals("isRobot")||
					  ff[j].getName().equals("getRoles")){
			  }else{
				  get.add(ff[j]);
			  }
		  }
		  String filed="";
		  String value="";
		  for(int i=0;i<get.size();i++){
			  Method method=get.get(i);
			  String  name=method.getName();
			  filed=name.substring(3,name.length());
			  try {
				value=String.valueOf(method.invoke(user));
			  } catch (Exception e) {
				e.printStackTrace();
			  } 
			 alltabls=alltabls+gettable(filed, value,String.valueOf(user.getUid()));
		  } 
		 return alltabls;
	}
    
    public static String gettable(String filed,String value1,String id){
//		   String xg=filed+id;
		   String tablehtml="";
		   if(filed.equals("Uid")||filed.equals("Name")){
			   tablehtml=
					   "<table>"+
					              "<tr>"+
					                  "<td>"+filed+"</td>"+
					                  "<td><input readonly=\"readonly\" class=\"custom\" type=\"text\" data-clear-btn=\"false\" name=\"" +filed+"\" id=\""+filed+"\" value=\""+value1+"\" ></td>"+
					              "</tr>"+
					   "</table>";	 
		   }else{
		    tablehtml=
				   "<table>"+
				              "<tr>"+
				                  "<td>"+filed+"</td>"+
				                  "<td><input  class=\"custom\" type=\"text\" data-clear-btn=\"false\" name=\"" +filed+"\" id=\""+filed+"\" value=\""+value1+"\" ></td>"+
				                  "<td><p><a href=\"#\"  onclick=\"f1(this"+",'"+filed+"'"+","+"'"+id+"'"+")\" data-role=\"button\" data-icon=\"check\" data-iconpos=\"notext\" data-theme=\"c\" data-inline=\"true\"></a></p></td>"+
				              "</tr>"+
				   "</table>";	 
		   }
		   return tablehtml;			   
	}	
    
    public static String getHead3(){
    	
    	String header = "<html>" +
				 "<head>"+
			      "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"+
			      "<meta name='viewport' content='width=device-width, initial-scale=1'>"+
			      "<title>用户查看信息</title>"+
			      "<link rel=\"stylesheet\" href=\"http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.css\" />"+
			      "<script src=\"http://code.jquery.com/jquery-1.9.1.min.js\"></script>"+
			      "<script src=\"http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.js\"></script>"+
				"<style type='text/css'>" +
				"body, p, ol, li{ margin: 0;padding: 0;}ul{margin: 0;padding: 0;margin-left:400px;}" +
				"li {list-style:none;float:left;width:90px;background-color:#666;" +
				"text-align:center;color:#FFF;padding:6px 0px;}a {text-decoration: none;color: #FFF;display: block;" +
				"padding: 6px 0px;border-right:solid 1px #FFF;}a:hover {color: #06F;background-color: #999;}</style>" +
				"<body><center>"+
				"<div data-role=\"controlgroup\" data-type=\"horizontal\" data-mini=\"true\">"+
				 Ihead.formhead()+
				"</div><center>";
       return header;
    }
    
}

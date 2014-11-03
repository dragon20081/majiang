package server.http;

import java.util.List;

import business.CountDao;
import business.conut.Sts_Urr;

public class Html_rateofturn implements IHtml {

	public String getHtml(String content) {
		System.out.println("laile-------->>");
		CountDao dao  = new CountDao();
		List  list=dao.getTop300Info();
		String htmlhead=Ihead.gethtmlHead();
		String table="";
		String tr="";
		Sts_Urr userRR=null;
		for(int i=0;i<list.size();i++){
			 userRR=(Sts_Urr) list.get(i);
			 int m1=userRR.getDay2()+userRR.getDay3();
			 int m2=userRR.getDay4()+
				        +userRR.getDay5()+
				        +userRR.getDay6()+
				        +userRR.getDay7();
			 int m3=
				        userRR.getDay8()+
				        userRR.getDay9()+
				        userRR.getDay10()+
				        userRR.getDay11()+
				        userRR.getDay12()+
				        userRR.getDay13()+
				        userRR.getDay14()+
				        userRR.getDay15();
			 int m4=userRR.getDay16()+
				        userRR.getDay17()+
				        userRR.getDay18()+
				        userRR.getDay19()+
				        userRR.getDay20()+
				        userRR.getDay21()+
				        userRR.getDay22()+
				        userRR.getDay23()+
				        userRR.getDay24()+
				        userRR.getDay25()+
				        userRR.getDay26()+
				        userRR.getDay27()+
				        userRR.getDay28()+
				        userRR.getDay29()+
				        userRR.getDay30();
			
			 tr+="<tr>" +
						"<td>"+userRR.getDate()+"</td>"+
				        "<td>"+userRR.getNewReg()+"</td>"+
				        "<td>"+userRR.getNewDLogin()+"</td>"+
				        "<td>"+userRR.getdLogin()+"</td>"+
				        "<td>"+userRR.getOncep()+"</td>"+
				       
				        "<td>"+userRR.getDay1()+"</td>"+
				        "<td>"+m1+"</td>"+
				        
				        "<td>"+m2+"</td>"+
				        
				        "<td>"+m3+"</td>"+
				        "<td>"+m4+"</td>"+
				        "<td>"+userRR.getEarlier()+"</td>"+
				       // "<td>"+userchange.getNowtpp()+"</td>"+
				      //  "<td>"+userRR.getAveOnline()+"</td>"+
				 "</tr>";
		}
		table= 
				
	              "<h3 align=\"center\">返回记录</h3>"+
	              "<table data-role=\"table\" id=\"tablqwe12\" " +
	              "data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'>" +
	              "<thead>" +
	              "<tr>" +
	              "<th data-priority=\"1\">时间</th>" +
	              "<th data-priority=\"2\">新注册用户数</th>" +
	              "<th data-priority=\"3\">新增登录独立用户数</th>" +
	              "<th data-priority=\"4\">独立登录用户数</th>" +
	              "<th data-priority=\"5\">一次性用户数</th>" +
	             
	              "<th data-priority=\"6\">第1天返回人数</th>" +
	              "<th data-priority=\"7\">第2-3天返回人数</th>" +
	              
	              "<th data-priority=\"8\">第4-7天返回人数</th>" +
	              "<th data-priority=\"9\">第8-15天返回人数</th>" +
	              "<th data-priority=\"10\">第16-30天返回人数</th>" +
	              
				  "<th data-priority=\"11\">更早返回数</th>" +
				  //"<th data-priority=\"14\">各时段在线人数</th>" +
				//  "<th data-priority=\"12\">平均在线人数</th>"+
	              "</tr>"+
	              "</thead>" +
	              "<tbody>" + tr + 
				  "</tbody></table>";
		
		String html_top="</body ></center></html>";
		
		return htmlhead+table+html_top;
	}

}

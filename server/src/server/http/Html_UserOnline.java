package server.http;

import java.util.ArrayList;
import java.util.List;

import server.mj.MgsCache;
import server.mj.ServerTimer;
import business.CountDao;
import business.conut.Sts_Changci;
import business.conut.Sts_Online;

public class Html_UserOnline implements IHtml{

	//当日在线
	//平均在线
	//总场次
	private CountDao cdao; 
	@Override
	public String getHtml(String content) {
		
		cdao = new CountDao();
		ServerTimer sTimer = ServerTimer.getInstance();
		List<Integer> onlineInfo = MgsCache.getInstance().getOnlineInfo();
		String table1 = "";
		String table2 = "";
		String table3 = "";
		String tr1 = "";
		 tr1+="<tr>" +
					"<td>"+sTimer.getNowString()+"</td>"+
			        "<td>"+onlineInfo.get(0)+"</td>"+
			        "<td>"+onlineInfo.get(1)+"</td>"+
			        "<td>"+onlineInfo.get(2)+"</td>"+
			        "<td>"+onlineInfo.get(3)+"</td>"+
			 "</tr>";
		table1= 
	              "<h3 align=\"center\">在线统计</h3>"+
	              "<table data-role=\"table\" id=\"tablqwe12\" " +
	              "data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'>" +
	              "<thead>" +
	              "<tr>" +
	              "<th data-priority=\"1\">时间</th>" +
	              "<th data-priority=\"2\">当前在线</th>" +
	              "<th data-priority=\"3\">2人场</th>" +
	              "<th data-priority=\"4\">4人场</th>" +
	              "<th data-priority=\"5\">大厅在线</th>" +
	              "</tr>"+
	              "</thead>" +
	              "<tbody>" + tr1 + 
				  "</tbody></table>";
			table2 = this.getAverStr();
			table3 = this.getChangciStr();
			String html_top="</body ></center></html>";
			
			   String msg=
					   "<html>"+   
				      "<head>"+
				      "<meta content=\"notranslate\" name=\"google\">"+
				      "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"+
				      "<meta name='viewport' content='width=device-width, initial-scale=1'>"+
				      "<title>在线统计</title>"+
				      "<link rel=\"stylesheet\" href=\"http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.css\" />"+
				      "<script src=\"http://code.jquery.com/jquery-1.9.1.min.js\"></script>"+
				      "<script src=\"http://code.jquery.com/mobile/1.3.2/jquery.mobile-1.3.2.min.js\"></script>"+
				      "</head>"+
				      "<body ><center>"+
				      "<div data-role=\"controlgroup\" data-type=\"horizontal\" data-mini=\"true\">"+
					   Ihead.formhead()+
					  "</div>" +table1+table2+table3+
					  "</center></body></html>";
			return msg;
			
		//return htmlhead+table1+table2+table3+html_top;
	}
	public String getAverStr()
	{
			String nowDay = ServerTimer.getDay();
		   String tr = "";
		   List<Sts_Online> list = cdao.find100Sts_OnLine();
		   for(int i = 0; i < list.size(); i++)
		   {
			   Sts_Online online = list.get(i);
			   
			   int averOL = caculteAverOne(online.getOlplayer());
			   int averDating = caculteAverOne(online.getOlDating());
			   List<Integer> chang = caculteAverTwo(online.getOlChang());
			   if(!nowDay.equals(online.getDay()) && online.getAverOl() == -1)
			   {
				   online.setAverOl(averOL);
				   cdao.saveSts_Object(online);
			   }
			   
				  tr+="<tr>"+
					      "<td>"+online.getDay()+"</td>"+
					      "<td>"+averOL+"</td>"+
					      "<td>"+chang.get(0)+"</td>"+
					      "<td>"+chang.get(1)+"</td>"+
					      "<td>"+averDating+"</td>"+
					  	  "</tr>";
		   }
		   String table=
				      "<div align=\"center\" data-role=\"collapsible\">"+
			              "<h3 align=\"center\">平均在线</h3>"+
			              "<table data-role=\"table\" id=\"table-column-toggle35\" " +
			              "data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'>" +
			              "<thead><tr>" +
			              "<th data-priority=\"1\">时间</th>" +
			              "<th data-priority=\"2\">平均在线</th>" +
			              "<th data-priority=\"3\">2人场</th>" +
			              "<th data-priority=\"4\">4人场</th>" +
			              "<th data-priority=\"5\">大厅平均在线</th>" +
			              "</thead><tbody>" + tr + 
						  "</tbody></table>" + 
			          "</div>";
	
		return table;
	}
	public int caculteAverOne(String str)
	{
		String[] tmp1 = str.split(";");
		int num = 0;
		for(int i = 0; i < tmp1.length;i++)
		{
			String tmpstr1 = tmp1[i];
			String[] tmpStr2 = tmpstr1.split(":");
			num += Integer.parseInt(tmpStr2[1]);
		}
		int aver = num / tmp1.length;
		return aver;
	}
	public List<Integer> caculteAverTwo(String str)
	{
		List<Integer> list = new ArrayList<Integer>();
		String[] tmp1 = str.split(";");
		int num1 = 0;
		int num2 = 0;
		for(int i = 0; i < tmp1.length;i++)
		{
			String[] tmpStr = tmp1[i].split(":");
			num1 += Integer.parseInt(tmpStr[1]);
			num2 += Integer.parseInt(tmpStr[2]);
		}
		int aver1 = num1 / tmp1.length;
		int aver2 = num2 / tmp1.length;
		list.add(aver1); list.add(aver2);
		return list;
	}
	
	public String getChangciStr()
	{
		List<Sts_Changci> list = cdao.find300Sts_Changci();
		
			String tr = "";
			for(int i = 0; i < list.size();i++)
			{
				Sts_Changci changci = list.get(i);
				  tr+="<tr>"+
					  	  //"<td>"+notice.getId()+"</td>" +
					      "<td>"+changci.getDay()+"</td>"+
					      "<td>"+changci.getChang2()+"</td>"+
					      "<td>"+changci.getChang4()+"</td>"+
					  	  "</tr>";
			}
		   String table=
				      "<div align=\"center\" data-role=\"collapsible\">"+
			              "<h3 align=\"center\">场次统计</h3>"+
			              "<table data-role=\"table\" id=\"table-column-toggle36\" " +
			              "data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'>" +
			              "<thead><tr>" +
			              "<th data-priority=\"1\">时间</th>" +
			              "<th data-priority=\"2\">2人场</th>" +
			              "<th data-priority=\"3\">4人场</th>" +
			              "</thead><tbody>" + tr + 
						  "</tbody></table>" + 
			          "</div>";
		return table;
	}

}

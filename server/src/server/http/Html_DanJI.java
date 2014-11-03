package server.http;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import business.CountDao;
import business.conut.Sts_DJ_Count;
import business.conut.Sts_DayTaskCompletion;
import business.conut.Sts_JuqingDay;
import business.conut.Sts_LongTaskCompletion;
import business.conut.Sts_UserDanjiCount;

public class Html_DanJI implements IHtml{

	@Override
	public String getHtml(String content) {
		
		String header = Ihead.getHead3();
		String bottom = "</body></html>";
		CountDao cdao  = new CountDao();

	  //每日
	  //每月
	  //次数
		
		String daytalbeHead= "<th data-priority=\"1\">人机1开启</th><th data-priority=\"1\">人机1关闭</th><th data-priority=\"1\">人机2开启</th><th data-priority=\"1\">人机2关闭</th>"+
		"<th data-priority=\"1\">人机3开启</th><th data-priority=\"1\">人机3关闭</th><th data-priority=\"1\">人机4开启</th><th data-priority=\"1\">人机4关闭</th><th data-priority=\"1\">人机5开启</th><th data-priority=\"1\">人机5关闭</th><th data-priority=\"1\">人机6开启</th><th data-priority=\"1\">人机6关闭</th>";
	
		String longtalbeHead= "<th data-priority=\"1\">1次人机</th><th data-priority=\"1\">2次人机</th><th data-priority=\"1\">3次人机</th><th data-priority=\"1\">4次人机</th><th data-priority=\"1\">5次人机</th><th data-priority=\"1\">5次以上</th>"+
				"";
		
		String juqing_day = "";

		
		int i = 0;
		
		
		List<Sts_UserDanjiCount> days = cdao.findTop100DJDay();
		
		String dayStr = "";
		for(i =0 ;i < days.size(); i++)
		{
			Sts_UserDanjiCount day1 = days.get(i);
			dayStr +="<tr>";
			dayStr += "<td>"+day1.getTimeDay()+"</td>";
			dayStr += "<td>"+day1.getDan1_open()+"</td>";
			dayStr += "<td>"+day1.getDan1_close()+"</td>";
			dayStr += "<td>"+day1.getDan2_open()+"</td>";
			dayStr += "<td>"+day1.getDan2_close()+"</td>";
			dayStr += "<td>"+day1.getDan3_open()+"</td>";
			dayStr += "<td>"+day1.getDan3_close()+"</td>";
			dayStr += "<td>"+day1.getDan4_open()+"</td>";
			dayStr += "<td>"+day1.getDan4_close()+"</td>";
			dayStr += "<td>"+day1.getDan5_open()+"</td>";
			dayStr += "<td>"+day1.getDan5_close()+"</td>";
			dayStr += "<td>"+day1.getDan6_open()+"</td>";
			dayStr += "<td>"+day1.getDan6_close()+"</td>";
			
			dayStr +="</tr>";
		}
		
		List allMonth = cdao.findMothDanji();
		Iterator it1=allMonth.iterator();
		Object[] obs1=new Object[]{};
		String td_month="";
		while(it1.hasNext()){
			obs1=(Object[])it1.next();
			if(obs1!=null&&obs1[0]!=null){

				td_month +="<tr>";
				for(i = 0; i < obs1.length;i++)
				{
					td_month+="<td>"+obs1[i]+"</td>";
				}
				td_month +="</tr>";
			}
		}
		
		List<Sts_DJ_Count> countList = cdao.findTop100DJCount();
		if(countList == null) countList = new ArrayList<Sts_DJ_Count>();
		String long_total = "";
		for( i = 0; i < countList.size(); i++)
		{
			Sts_DJ_Count count = countList.get(i);
			long_total  += "<tr>";
			long_total += "<td>"+count.getTimeDay()+"</td>";
			long_total += "<td>"+count.getTime_1()+"</td>";
			long_total += "<td>"+count.getTime_2()+"</td>";
			long_total += "<td>"+count.getTime_3()+"</td>";
			long_total += "<td>"+count.getTime_4()+"</td>";
			long_total += "<td>"+count.getTime_5()+"</td>";
			long_total += "<td>"+count.getTime_other()+"</td>";
			long_total  += "</tr>";
		}
		
		String middle = 
		         
				"<div data-role=\"collapsible-set\" data-inset=\"false\">"+
				"<div align=\"center\" data-role=\"collapsible\">"+
				"<h3 align=\"center\">每日人机统计</h3>"+
				"<table data-role=\"table\" id=\"table-column-toggle13\" data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'><thead>><tr><th data-priority=\"1\">时间</th>" +
				daytalbeHead+
				"</tr></thead><tbody>"+ dayStr +
				"</table></div>" + 
				
				"<div align=\"center\" data-role=\"collapsible\">"+
				"<h3 align=\"center\">每月人机统计</h3>"+
				"<table data-role=\"table\" id=\"table-column-toggle13\" data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'><thead>><tr><th data-priority=\"1\">时间</th>" +
				daytalbeHead+
				"</tr></thead><tbody>"+ td_month +
				"</table></div>" + 
				

				"<div align=\"center\" data-role=\"collapsible\">"+
				"<h3 align=\"center\">每日人机次数</h3>"+
				"<table data-role=\"table\" id=\"table-column-toggle11\" data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'><thead><tr><th data-priority=\"1\">时间</th>" +
				longtalbeHead+
				"</tr></thead><tbody>" + long_total +
				"</tbody></table>" +
				
				"</div>"+
				
				"</div>";
		
		return header + middle + bottom;
	}

}

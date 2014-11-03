package server.http;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import business.CountDao;
import business.conut.Sts_JuqingDay;

public class Html_Juqing implements IHtml{

	@Override
	public String getHtml(String content) {
		
		String header = Ihead.getHead3();
		String bottom = "</body></html>";
		CountDao cdao  = new CountDao();
		

		
		
		String talbeHead= "<th data-priority=\"1\">第一章</th><th data-priority=\"1\">第二章</th><th data-priority=\"1\">第三章</th><th data-priority=\"1\">全部完成</th>";
		String juqing_day = "";
		String juqing_month = "";
		String juqing_total = "";
		
		List<Integer> totalJuqing = new ArrayList<Integer>();
		
		int i = 0;
		List<Sts_JuqingDay> dayList = cdao.findAllJuqing();
		for( i = 0; i < dayList.size();i++)
		{
			//服务器注册人数
			//当前获得头像的人数
			//参加过网络对战的人数
			Sts_JuqingDay dayjq = dayList.get(i);
			juqing_day += "<tr><td>"+dayjq.getTime1()+"</td><td>"+dayjq.getJuqing1()+"</td><td>"+dayjq.getJuqing2()+"</td><td>"+dayjq.getJuqing3()+"</td><td>"+dayjq.getJuqing3()+"</td><td>"+dayjq.getTotalPlayer()+"</td><td>"+dayjq.getHistoryHadImg()+"</td><td>"+dayjq.getHistoryNetBattle()+"</td></tr>";
		}
		
		List monthList = cdao.findAllJuqing_Month();
		for(i = 0; i < monthList.size(); i++)
		{
			juqing_month +="<tr>";
			Object[] arr = (Object[]) monthList.get(i);
			for(int j = 0; j < arr.length; j++)
			{
				juqing_month += "<td>"+arr[j]+"</td>";
				
				if(j == arr.length -1)
					juqing_month += "<td>"+arr[j]+"</td>";
				
				if(j != 0)
				{
					int num  = ((BigDecimal)arr[j]).intValue();
					//j  = 1  totaljuqing 位置插值
					if(totalJuqing.size() >= j)
						totalJuqing.set(j -1, totalJuqing.get(j - 1));
					else
						totalJuqing.add(j -1, num);
				}
			}
			juqing_month +="</tr>";
		}
		juqing_total  = "<tr>";
		for( i = 0; i < totalJuqing.size(); i++)
		{
			juqing_total += "<td>"+totalJuqing.get(i)+"</td>";
		}
		if(totalJuqing.size() > 0)juqing_total += "<td>"+totalJuqing.get(totalJuqing.size() -1)+"</td>";
		juqing_total  += "</tr>";
		
		String middle = 
		         
				"<div data-role=\"collapsible-set\" data-inset=\"false\">"+
				"<div align=\"center\" data-role=\"collapsible\">"+
				"<h3 align=\"center\">每日剧情进度统计</h3>"+
				"<table data-role=\"table\" id=\"table-column-toggle13\" data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'><thead>><tr><th data-priority=\"1\">时间</th>" +
				 talbeHead+
				 "<th data-priority=\"1\">总注册人数</th><th data-priority=\"1\">历史获得形象数</th><th data-priority=\"1\">历史参加加网络对战数</th>"+
				"</tr></thead><tbody>"+ juqing_day +
				"</table></div>" + 
				
                "<div align=\"center\" data-role=\"collapsible\">"+
                "<h3 align=\"center\">每月剧情进度统计</h3>"+
                "<table data-role=\"table\" id=\"table-column-toggle14\" data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'><thead>><tr><th data-priority=\"1\">时间</th>" +
                talbeHead+
                "</tr></thead><tbody>" + juqing_month +
				"</table></div>"+

				"<div align=\"center\" data-role=\"collapsible\">"+
				"<h3 align=\"center\">总剧情进度统计</h3>"+
				"<table data-role=\"table\" id=\"table-column-toggle11\" data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'><thead><tr>" +
		        talbeHead+
				"</tr></thead><tbody>" + juqing_total +
				"</tbody></table>" +
				
				"</div>"+
				
				"</div>";
		
		return header + middle + bottom;
	}

}

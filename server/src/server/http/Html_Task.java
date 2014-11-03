package server.http;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import common.MyArrays;

import business.CountDao;
import business.conut.Sts_DayTaskCompletion;
import business.conut.Sts_JuqingDay;
import business.conut.Sts_LongTaskCompletion;

public class Html_Task implements IHtml{

	@Override
	public String getHtml(String content) {
		
		String header = Ihead.getHead3();
		String bottom = "</body></html>";
		CountDao cdao  = new CountDao();
		

		
		
		String daytalbeHead= "<th data-priority=\"1\">每日登陆</th><th data-priority=\"1\">5分钟在线</th><th data-priority=\"1\">1小时在线</th><th data-priority=\"1\">2小时在线</th>"+
		"<th data-priority=\"1\">3小时在线</th><th data-priority=\"1\">每日首胜</th><th data-priority=\"1\">进行10局游戏</th><th data-priority=\"1\">满贯和牌(10~19番)</th><th data-priority=\"1\">倍满和牌(20~29番)</th><th data-priority=\"1\">役满和牌(30番以上)</th><th data-priority=\"1\">总计</th>";
	
		String longtalbeHead= "<th data-priority=\"1\">总盘数1000盘</th><th data-priority=\"1\">和牌1000盘</th><th data-priority=\"1\">满贯100盘</th><th data-priority=\"1\">倍满100盘</th><th data-priority=\"1\">役满100盘</th><th data-priority=\"1\">完成前三章剧情</th>"+
				"<th data-priority=\"1\">社区温暖杯首胜</th><th data-priority=\"1\">开启同好竞技杯</th><th data-priority=\"1\">开启市级麻将大赛</th><th data-priority=\"1\">开启省级积分赛</th>";
		
		String juqing_day = "";
		String long_total = "";
		
		List<Integer> totalJuqing = new ArrayList<Integer>();
		
		int i = 0;
		List<Sts_DayTaskCompletion> dayList = cdao.findTop100DayTask();
		for( i = 0; i < dayList.size();i++)
		{
			Sts_DayTaskCompletion dayTask = dayList.get(i);
			juqing_day += "<tr><td>"+dayTask.getTimeDay()+"</td><td>"+dayTask.getTask1()+":"+dayTask.getTask101()+"</td><td>"+dayTask.getTask2()+":"+dayTask.getTask102()+"</td><td>"+dayTask.getTask3()+":"+dayTask.getTask103()+"</td><td>"+dayTask.getTask4()+":"+dayTask.getTask104()+"</td><td>"+dayTask.getTask5()+":"+dayTask.getTask105()+"</td><td>"+dayTask.getTask6()+":"+dayTask.getTask106()+"</td><td>"+dayTask.getTask7()+":"+dayTask.getTask107()+"</td><td>"+dayTask.getTask8()+":"+dayTask.getTask108()+"</td><td>"+dayTask.getTask10()+":"+dayTask.getTask110()+"</td><td>"+dayTask.getTask11()+":"+dayTask.getTask111()+"</td><td>"+dayTask.getTotalTask()+":"+dayTask.getTotalOverTask()+"</td></tr>";
		}

		long_total  = "<tr>";
		
		Sts_LongTaskCompletion longTask = cdao.findLongTask();
		List<Integer> longIds = MyArrays.asList(30,31,32,33,34,35,41,51,52,53);
		for( i = 0; i < longIds.size(); i++)
		{
			int taskId = longIds.get(i);
			long_total += "<td>"+longTask.getCoutByTaskId(taskId)+":"+longTask.getOverCoutByTaskId(taskId)+"</td>";
		}
		long_total  += "</tr>";
		
		String middle = 
		         
				"<div data-role=\"collapsible-set\" data-inset=\"false\">"+
				"<div align=\"center\" data-role=\"collapsible\">"+
				"<h3 align=\"center\">每日任务完成情况</h3>"+
				"<table data-role=\"table\" id=\"table-column-toggle13\" data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'><thead>><tr><th data-priority=\"1\">时间</th>" +
				daytalbeHead+
				"</tr></thead><tbody>"+ juqing_day +
				"</table></div>" + 
				

				"<div align=\"center\" data-role=\"collapsible\">"+
				"<h3 align=\"center\">永久任务完成情况</h3>"+
				"<table data-role=\"table\" id=\"table-column-toggle11\" data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'><thead><tr>" +
				longtalbeHead+
				"</tr></thead><tbody>" + long_total +
				"</tbody></table>" +
				
				"</div>"+
				
				"</div>";
		
		return header + middle + bottom;
	}

}

package server.http;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import business.CountDao;
import business.conut.Sts_JuqingDay;
import business.conut.Sts_MJDevice;



public class Html_Sts_Device implements IHtml{

	@Override
	public String getHtml(String content) {
		
		String header = Ihead.getHead3();
		String bottom = "</body></html>";
		CountDao cdao  = new CountDao();
		
		String talbeHead= "<th data-priority=\"1\">时间</th><th data-priority=\"1\">新增设备数</th><th data-priority=\"1\">独立设备数</th><th data-priority=\"1\">设备打开次数</th>";
		String juqing_day = "";
		
		
		int i = 0;
		List<Sts_MJDevice> dayList = cdao.findAllSts_Device();
		for( i = 0; i < dayList.size();i++)
		{
			Sts_MJDevice daydv = dayList.get(i);
			juqing_day += "<tr><td>"+daydv.getTime1()+"</td><td>"+daydv.getNewAdd()+"</td><td>"+daydv.getDuliDevice()+"</td><td>"+daydv.getOpenDevice()+"</td></tr>";
		}
		
		String middle = 
		         
				"<div data-role=\"collapsible-set\" data-inset=\"false\">"+
				"<div align=\"center\" data-role=\"collapsible\">"+
				"<h3 align=\"center\">设备登录统计</h3>"+
				"<table data-role=\"table\" id=\"table-column-toggle13\" data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'><thead>><tr>" +
				 talbeHead+
				"</tr></thead><tbody>"+ juqing_day +
				"</table></div>" + 
				"</div>";
		
		return header + middle + bottom;
	}

}

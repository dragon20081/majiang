package server.http;

import java.util.List;

import business.CountDao;

public class Html_Cup implements IHtml{

	@Override
	public String getHtml(String content) {
		
		String header = Ihead.getHead3();
		String bottom = "</body></html>";
		CountDao cdao  = new CountDao();
		
		String daytalbeHead= "<th data-priority=\"1\">开启次数</th><th data-priority=\"1\">胜利次数</th><th data-priority=\"1\">失败次数</th><th data-priority=\"1\">消耗道具1</th>"+
		"<th data-priority=\"1\">消耗道具2</th>";
	
		List cup1 = cdao.findStsCupById(1);
		
		List cup2 = cdao.findStsCupById(2);
		
		List cup3 = cdao.findStsCupById(3);
		
		List cup4 = cdao.findStsCupById(4);
		
		
		String middle = 
		         
				"<div data-role=\"collapsible-set\" data-inset=\"false\">"+

				"<div align=\"center\" data-role=\"collapsible\">"+
				"<h3 align=\"center\">杯赛一</h3>"+
				"<table data-role=\"table\" id=\"table-column-toggle13\" data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'><thead><tr><th data-priority=\"1\">时间</th>" +
				daytalbeHead+
				"</tr></thead><tbody>" + getDataStr(cup1) +
				"</tbody></table>" +
				"</div>"+
				
				"<div align=\"center\" data-role=\"collapsible\">"+
				"<h3 align=\"center\">杯赛二</h3>"+
				"<table data-role=\"table\" id=\"table-column-toggle13\" data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'><thead><tr><th data-priority=\"1\">时间</th>" +
				daytalbeHead+
				"</tr></thead><tbody>" + getDataStr(cup2) +
				"</tbody></table>" +
				"</div>"+
				
				"<div align=\"center\" data-role=\"collapsible\">"+
				"<h3 align=\"center\">杯赛三</h3>"+
				"<table data-role=\"table\" id=\"table-column-toggle13\" data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'><thead><tr><th data-priority=\"1\">时间</th>" +
				daytalbeHead+
				"</tr></thead><tbody>" + getDataStr(cup3) +
				"</tbody></table>" +
				"</div>"+
				
				"<div align=\"center\" data-role=\"collapsible\">"+
				"<h3 align=\"center\">杯赛四</h3>"+
				"<table data-role=\"table\" id=\"table-column-toggle13\" data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'><thead><tr><th data-priority=\"1\">时间</th>" +
				daytalbeHead+
				"</tr></thead><tbody>" + getDataStr(cup4) +
				"</tbody></table>" +
				"</div>"+
				
				
				"</div>";
		
		return header + middle + bottom;
	}
	
	private String getDataStr(List list)
	{
		if(list == null) return "";
		String str = "";
		int i,j;
		for(i = 0; i < list.size();i++)
		{
			String tmp = "<tr>";
			Object[] arr = (Object[]) list.get(i);
			for(j = 0 ; j < arr.length;j++)
			{
				tmp += "<td>"+arr[j]+"</td>";
			}
			str += tmp;
			str +="</tr>";
		}
		
		return str;
	}
	

}

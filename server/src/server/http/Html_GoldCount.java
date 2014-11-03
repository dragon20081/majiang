package server.http;

import java.util.List;

import business.CountDao;
import business.conut.Sts_GoldDia;

public class Html_GoldCount implements IHtml{

	@Override
	public String getHtml(String content) {
		
		String header = Ihead.getHead3();
		String bottom = "</body></html>";
		CountDao cdao  = new CountDao();
		
		List<Sts_GoldDia> list  = cdao.findAllGoldCount();
		String goldStr = "";
		
		for(int i = 0 ; i < list.size(); i++)
		{
			Sts_GoldDia gd = list.get(i);
			goldStr +="<tr>";
			goldStr+= "<td>"+gd.getDay()+"</td>";
			goldStr+= "<td>"+gd.getTotalGold()+"</td>";
			goldStr+= "<td>"+gd.getTotalDia()+"</td>";
			goldStr+= "<td>"+gd.getUsedGold()+"</td>";
			goldStr+= "<td>"+gd.getUseedDia()+"</td>";
			goldStr+= "<td>"+gd.getDiaToGold()+"</td>";
			goldStr+= "<td>"+gd.getGoldFromDia()+"</td>";
			goldStr +="</tr>";
		}
		
		String talbeHead = 	"<th data-priority=\"1\">服务器总金币数</th>" +
							"<th data-priority=\"1\">服务器总钻石数</th>" +
							"<th data-priority=\"1\">当日消耗总金币</th>" +
							"<th data-priority=\"1\">当日消耗钻石数</th>"+
							"<th data-priority=\"1\">当日兑换金币的钻石数</th>" +
							"<th data-priority=\"1\">从钻石兑换来的金币数量</th>" ;

		
		String middle = 
				"<div data-role=\"collapsible-set\" data-inset=\"false\">"+
				"<div align=\"center\" data-role=\"collapsible\">"+
				"<h3 align=\"center\">服务器金币钻石统计</h3>"+
				"<table data-role=\"table\" id=\"table-column-toggle13\" data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'><thead><tr><th data-priority=\"1\">时间</th>" +
				 talbeHead+
				"</tr></thead><tbody>"+ goldStr +
				"</table></div>" + 
				"</div>";
		
		return header + middle + bottom;
	}

}

package server.http;

import business.CountDao;

public class Html_GoldCheck implements IHtml{

	@Override
	public String getHtml(String content) {
		
		CountDao cdao = new CountDao();
		
		String header = Ihead.getHead3();
		String bottom = "</body></html>";
		
		String talbeHead = 	"<tr><th data-priority=\"1\">服务器总人数</th>" +
				"<th data-priority=\"1\">金币大于10万人数</th>" +
				"<th data-priority=\"1\">金币大于100万人数</th>" +
				"<th data-priority=\"1\">金币大于1000万人数</th>"+
				"<th data-priority=\"1\">钻石大于10万人数</th>" +
				"<th data-priority=\"1\">钻石大于100万人数</th>" +
				"<th data-priority=\"1\">钻石大于1000万人数</th></tr>" ;
		
		int regNum = cdao.findTotalRegPlayer();
		int gold10 = cdao.countGold10();
		int gold100 = cdao.countGold100();
		int gold1000 = cdao.countGold1000();
		
		int dia10 = cdao.countDia10();
		int dia100 = cdao.countDia100();
		int dia1000 = cdao.countDia1000();
		
		String countStr = "<td>"+regNum+"</td><td>"+gold10+"</td><td>"+gold100+"</td><td>"+gold1000+"</td><td>"+dia10+"</td><td>"+dia100+"</td><td>"+dia1000+"</td>";

String middle = 
	"<div data-role=\"collapsible-set\" data-inset=\"false\">"+
	"<div align=\"center\" data-role=\"collapsible\">"+
	"<h3 align=\"center\">金币钻石异常检测</h3>"+
	"<table data-role=\"table\" id=\"table-column-toggle13\" data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'><thead>" +
	 talbeHead+
	"</tr></thead><tbody>"+ countStr +
	"</table></div>" + 
	"</div>";
		return header + middle + bottom;
	}

}

package server.http;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import business.CountDao;
import business.conut.Sts_Arpu;

public class Html_Arpu implements IHtml{

	@Override
	public String getHtml(String content) {
		
		CountDao cdao = new CountDao();
		String htmlhead=Ihead.gethtmlHead();
		String table="";
		String tr="";
		Sts_Arpu arpu=null;
		List<Sts_Arpu> list= cdao.find300Arpu();
		DecimalFormat decimal=new DecimalFormat("0.00");
		for(int i=0;i<list.size();i++){
			arpu= list.get(i);
			tr+="<tr>"+
			    "<td>"+arpu.getDay()+"</td>"+
			    "<td>"+arpu.getChargeNum()+"</td>"+
			    "<td>"+arpu.getAddCharge()+"</td>"+
			    "<td>"+arpu.getAmount()+"</td>"+
			    "<td>"+decimal.format(arpu.getArpuv())+"</td>"+
			"</tr>";
		}
		
		 table =
				  "<div align=\"center\" data-role=\"collapsible\">"+
			              "<h3 align=\"center\">日ARPU</h3>"+
						  		  
			              "<table data-role=\"table\" id=\"award-column-toggle01\" " +
			              "data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'>" +
			              "<thead>" +
			              "<tr>"+
			              "<th data-priority=\"1\">时间</th>" +
			              "<th data-priority=\"2\">充值用户数</th>" +
			              "<th data-priority=\"3\">新增充值用户数</th>" +
			              "<th data-priority=\"4\">充值金额</th>" +
			              "<th data-priority=\"5\">arpu</th>" +
			              "</tr>"+
			              "</thead>" +
			              "<tbody>" + tr + 
						  "</tbody></table></div>";

		 tr = "";
		 
			List list1=cdao.countArpuForMonth();
			Iterator it1=list1.iterator();
			Object[] obs1=new Object[]{};
			String tablebody1="";
			String td1="";
			int chargeNum = 0;
			int ChargeJine = 0; 
			while(it1.hasNext()){
				obs1=(Object[])it1.next();
				if(obs1!=null&&obs1[0]!=null){
						 td1+="<td>"+obs1[0]+"</td>";
						 td1+="<td>"+obs1[1]+"</td>";
						 td1+="<td>"+obs1[2]+"</td>";
						 td1+="<td>"+obs1[3]+"</td>";
						 chargeNum = ((BigDecimal)obs1[1]).intValue();
						 ChargeJine = ((BigDecimal)obs1[3]).intValue();
						 td1+="<td>"+(ChargeJine /chargeNum )+"</td>";
				}
				tablebody1+="<tr>"+td1+"</tr>";
				td1="";
			}
		String table2 =
				  "<div align=\"center\" data-role=\"collapsible\">"+
			              "<h3 align=\"center\">月ARPU</h3>"+
						  		  
			              "<table data-role=\"table\" id=\"award-column-toggle01\" " +
			              "data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'>" +
			              "<thead>" +
			              "<tr>"+
			              "<th data-priority=\"1\">时间</th>" +
			              "<th data-priority=\"2\">充值用户数</th>" +
			              "<th data-priority=\"3\">新增充值用户数</th>" +
			              "<th data-priority=\"4\">充值金额</th>" +
			              "<th data-priority=\"5\">arpu</th>" +
			              "</tr>"+
			              "</thead>" +
			              "<tbody>" + tablebody1 + 
						  "</tbody></table></div>";
		
       String html_top="</body ></center></html>";
        
		return htmlhead+table+table2+html_top;
	}

	
}

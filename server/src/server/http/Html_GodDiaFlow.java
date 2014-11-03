package server.http;

import java.util.Iterator;
import java.util.List;

import server.mj.ServerTimer;
import business.CountDao;
import business.conut.MJUserCharge;
import business.conut.Sts_Recharge;

public class Html_GodDiaFlow implements IHtml{

//	private String[] diaNumStr = null;
	private CountDao cdao; 
	@Override
	public String getHtml(String content) {
		cdao = new CountDao();
		
		String header = Ihead.getHead3();
//		diaNumStr=Sts_Recharge.getDiaNumStr().split(",");
		//ajax
		String ajax="";
		ajax="<script type=\"text/javascript\">"+
        "function f(){"+
             "var id=$(\"#search-1\").val();"+
             
            "$.post(\"/flowquery\", {uid:id}," +
		     "function (data, textStatus){" +
               
		      "if(data==\"0\"){"+
		         "alert(\"没有该用户\");" +
		      "}else{"+
		         
		        "$(\"#body\").html(data);"+
		   "}"+
             
		   "},\"text\")"+
        "}"+
       "</script>";
		
		//最新500条流动记录
		List<MJUserCharge> list = cdao.find500FlowRec();
		String daytable="";
		String daytablebody="";
		MJUserCharge charge=null;
        if(list!=null){
			
			for(int i=0;i<list.size();i++){
				
				charge=list.get(i);
				daytablebody+="<tr>" +
						"<td>"+charge.getCmd()+"</td>"+
						"<td>"+charge.getUid()+"</td>"+
						"<td>"+charge.getName()+"</td>"+
						"<td>"+charge.getNickName()+"</td>"+
						"<td>"+charge.getGold()+"</td>"+
						"<td>"+charge.getModGold()+"</td>"+
						"<td>"+charge.getAfterGold()+"</td>"+
						"<td>"+charge.getDia()+"</td>"+
						"<td>"+charge.getModDia()+"</td>"+
						"<td>"+charge.getAfterDia()+"</td>"+
						"<td>"+charge.getInfo()+"</td>"+
//						"<td>"+charge.getTime()+"</td>"+
						"<td>"+charge.getTimeStr()+"</td>"+
						"</tr>";
			}
		}  
		daytable= "<div align=\"center\" data-role=\"collapsible\">"+
				  "<h3 align=\"center\">最新500条流动记录</h3>"+
	              "<table data-role=\"table\" id=\"award-column-toggle01\" " +
	              "data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'>" +
	              "<thead>" +
	              "<tr>"+
	              "<th data-priority=\"1\">指令</th>" +
	              "<th data-priority=\"2\">UID</th>" +
	              "<th data-priority=\"3\">账号</th>" +
	              "<th data-priority=\"4\">昵称</th>" +
	              "<th data-priority=\"5\">金币</th>" +
	              "<th data-priority=\"6\">金币修改量</th>" +
	              "<th data-priority=\"7\">修改后金币</th>" +
	              "<th data-priority=\"8\">钻石</th>" +
	              "<th data-priority=\"9\">钻石修改量</th>" +
	              "<th data-priority=\"10\">钻石修改后</th>" +
	              "<th data-priority=\"11\">信息</th>" +
//	              "<th data-priority=\"12\">绝对时间</th>" +
	              "<th data-priority=\"13\">时间</th>" +
	              "</tr>"+
	              "</thead>" +
	              "<tbody>" + daytablebody + 
				  "</tbody></table></div>";
		
		String all1="<div>"+
		 
				  "<div align=\"center\" data-role=\"collapsible\">"+
	              "<h3 align=\"center\">查询玩家详细金币钻石流动   Example   UID查询:uid:1 账号查询: name:ffx 昵称查询 nick:ffx </h3>"+
                 
	              "<input onchange=\"f()\" type=\"search\" name=\"search-1\" id=\"search-1\" value=\"\">"+
	              "<table data-role=\"table\" id=\"award-column-toggle03\" " +
	              "data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'>" +
	              "<thead>" +
	              "<tr>"+
	              "<th data-priority=\"1\">指令</th>" +
	              "<th data-priority=\"2\">UID</th>" +
	              "<th data-priority=\"3\">账号</th>" +
	              "<th data-priority=\"4\">昵称</th>" +
	              "<th data-priority=\"5\">金币</th>" +
	              "<th data-priority=\"6\">金币修改量</th>" +
	              "<th data-priority=\"7\">修改后金币</th>" +
	              "<th data-priority=\"8\">钻石</th>" +
	              "<th data-priority=\"9\">钻石修改量</th>" +
	              "<th data-priority=\"10\">钻石修改后</th>" +
	              "<th data-priority=\"11\">信息</th>" +
//	              "<th data-priority=\"12\">绝对时间</th>" +
	              "<th data-priority=\"13\">时间</th>" +
	              "</tr>"+
	              "</thead>" +
	              "<tbody id=\"body\">"+
				  "</tbody></table>"+
				  "</div></div>";
		
		String bottom = "</center></body></html>";
		
		return header+ajax+daytable+all1+bottom;
	}


}

package server.http;

import java.util.Iterator;
import java.util.List;


import server.mj.ServerTimer;

import business.CountDao;
import business.conut.Sts_Recharge;


public class Html_user_recharge implements IHtml {

	private String[] diaNumStr = null;
	private CountDao cdao; 
	@Override
	public String getHtml(String content) {
		// TODO Auto-generated method stub
		cdao = new CountDao();
		
		String header = Ihead.getHead3();
		diaNumStr=Sts_Recharge.getDiaNumStr().split(",");
		//ajax
		String ajax="";
		ajax="<script type=\"text/javascript\">"+
        "function f(){"+
             "var id=$(\"#search-1\").val();"+
             
            "$.post(\"/Rechargeid\", {uid:id}," +
		     "function (data, textStatus){" +
               
		      "if(data==\"0\"){"+
		         "alert(\"没有该用户\");" +
		      "}else{"+
		         
		        "$(\"#body\").html(data);"+
		   "}"+
             
		   "},\"text\")"+
        "}"+
        "function f1(){"+
        "var id=$(\"#search-2\").val();"+
        
       "$.post(\"/Rechargeidday\", {uid:id}," +
	     "function (data, textStatus){" +
          
	      "if(data==\"0\"){"+
	         "alert(\"没有该用户\");" +
	      "}else{"+
	         
	        "$(\"#bodyday\").html(data);"+
	   "}"+
        
	   "},\"text\")"+
   "}"+ 
        
       "</script>";
		
		//当日玩家充值记录
		List list = cdao.findRechargeByDate(ServerTimer.getNowString().substring(0,10));
		System.out.println(list.size());
		String daytable="";
		String daytablebody="";
		Sts_Recharge recharge=null;
        if(list!=null){
			
			for(int i=0;i<list.size();i++){
				
				recharge=(Sts_Recharge) list.get(i);
				daytablebody+="<tr>" +
						"<td>"+recharge.getDay()+"</td>"+
						"<td>"+recharge.getUid()+"</td>"+
						"<td>"+recharge.getAccount()+"</td>"+
						"<td>"+recharge.getNick()+"</td>"+
						"<td>"+recharge.getDia1()+"</td>"+
						"<td>"+recharge.getDia2()+"</td>"+
						"<td>"+recharge.getDia3()+"</td>"+
						"<td>"+recharge.getDia4()+"</td>"+
						"<td>"+recharge.getDia5()+"</td>"+
						"<td>"+recharge.getDia6()+"</td>"+
						"<td>"+recharge.getMoney()+"</td>"+
						"<td>"+recharge.getPayWay()+"</td>"+
						"<td>"+recharge.getDiaByfore()+"</td>"+
						"<td>"+recharge.getDiaAfter()+"</td>"+
						"</tr>";
			   
			}
			
		}  
		daytable= "<div align=\"center\" data-role=\"collapsible\">"+
				  "<h3 align=\"center\">当日玩家充值记录</h3>"+
	              "<table data-role=\"table\" id=\"award-column-toggle01\" " +
	              "data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'>" +
	              "<thead>" +
	              "<tr>"+
	              "<th data-priority=\"1\">时间</th>" +
	              "<th data-priority=\"2\">UID</th>" +
	              "<th data-priority=\"3\">账号</th>" +
	              "<th data-priority=\"4\">昵称</th>" +
	              "<th data-priority=\"5\">钻石"+diaNumStr[0]+"</th>" +
	              "<th data-priority=\"6\">钻石"+diaNumStr[1]+"</th>" +
	              "<th data-priority=\"7\">钻石"+diaNumStr[2]+"</th>" +
	              "<th data-priority=\"8\">钻石"+diaNumStr[3]+"</th>" +
	              "<th data-priority=\"9\">钻石"+diaNumStr[4]+"</th>" +
	              "<th data-priority=\"10\">钻石"+diaNumStr[5]+"</th>" +
	              "<th data-priority=\"11\">充值金额</th>" +
	              "<th data-priority=\"12\">付款方式</th>" +
	              "<th data-priority=\"13\">充值前钻石数</th>" +
	              "<th data-priority=\"14\">充值后钻石数</th>" +
	              "</tr>"+
	              "</thead>" +
	              "<tbody>" + daytablebody + 
				  "</tbody></table></div>";
		
		//所有玩家总得充值记录
		List allcount = cdao.UserRechagecount();
		Iterator it=allcount.iterator();
		Object[] obs=new Object[]{};
		String tablebody="";
		String td="";
	    
		while(it.hasNext()){
			obs=(Object[])it.next();
			if(obs!=null&&obs[0]!=null){
				for(int i=0;i<obs.length;i++){
					//if(i!=6)
					td+="<td>"+obs[i]+"</td>";
				}
			}
			tablebody+="<tr>"+td+"</tr>";
			td="";
		}
		String all=
				  "<div align=\"center\" data-role=\"collapsible\">"+
	              "<h3 align=\"center\">玩家总得充值记录</h3>"+
				  		  
	              "<table data-role=\"table\" id=\"award-column-toggle02\" " +
	              "data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'>" +
	              "<thead>" +
	              "<tr>"+
	              "<th data-priority=\"1\">玩家UID</th>" +
	              "<th data-priority=\"2\">用户名</th>" +
	              "<th data-priority=\"3\">钻石"+diaNumStr[0]+"</th>" +
	              "<th data-priority=\"4\">钻石"+diaNumStr[1]+"</th>" +
	              "<th data-priority=\"5\">钻石"+diaNumStr[2]+"</th>" +
	              "<th data-priority=\"6\">钻石"+diaNumStr[3]+"</th>" +
	              "<th data-priority=\"7\">钻石"+diaNumStr[4]+"</th>" +
	              "<th data-priority=\"8\">钻石"+diaNumStr[5]+"</th>" +
	              "<th data-priority=\"9\">总金额</th>" +
	              "</tr>"+
	              "</thead>" +
	              "<tbody>" + tablebody + 
				  "</tbody></table></div>";
		
		String all1="<div>"+
		 
				  "<div align=\"center\" data-role=\"collapsible\">"+
	              "<h3 align=\"center\">查询玩家详细充值记录 Example   UID查询:uid:1 账号查询: name:ffx 昵称查询 nick:ffx </h3>"+
                 
	              "<input onchange=\"f()\" type=\"search\" name=\"search-1\" id=\"search-1\" value=\"\">"+
	              "<table data-role=\"table\" id=\"award-column-toggle03\" " +
	              "data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'>" +
	              "<thead>" +
	              "<tr>"+
	              "<th data-priority=\"1\">时间</th>" +
	              "<th data-priority=\"2\">UID</th>" +
	              "<th data-priority=\"3\">账号</th>" +
	              "<th data-priority=\"4\">昵称</th>" +
	              "<th data-priority=\"5\">钻石"+diaNumStr[0]+"</th>" +
	              "<th data-priority=\"6\">钻石"+diaNumStr[1]+"</th>" +
	              "<th data-priority=\"7\">钻石"+diaNumStr[2]+"</th>" +
	              "<th data-priority=\"8\">钻石"+diaNumStr[3]+"</th>" +
	              "<th data-priority=\"9\">钻石"+diaNumStr[4]+"</th>" +
	              "<th data-priority=\"10\">钻石"+diaNumStr[5]+"</th>" +
	              "<th data-priority=\"11\">充值金额</th>" +
	              "<th data-priority=\"12\">付款方式</th>" +
	              "<th data-priority=\"13\">充值前钻石数</th>" +
	              "<th data-priority=\"14\">充值后钻石数</th>" +
	              "</tr>"+
	              "</thead>" +
	              "<tbody id=\"body\">"+
				  "</tbody></table>"+
				  "</div></div>";
		
		
		String all2="<div>"+
				 
				  "<div align=\"center\" data-role=\"collapsible\">"+
	              "<h3 align=\"center\">查询某天充值记录 Example   2014-03-01</h3>"+
                 
	              "<input onchange=\"f1()\" type=\"search\" name=\"search-2\" id=\"search-2\" value=\"\">"+
	              "<table data-role=\"table\" id=\"award-column-toggle04\" " +
	              "data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'>" +
	              "<thead>" +
	              "<tr>"+
	              "<th data-priority=\"1\">时间</th>" +
	              "<th data-priority=\"2\">UID</th>" +
	              "<th data-priority=\"3\">账号</th>" +
	              "<th data-priority=\"4\">昵称</th>" +
	              "<th data-priority=\"5\">钻石"+diaNumStr[0]+"</th>" +
	              "<th data-priority=\"6\">钻石"+diaNumStr[1]+"</th>" +
	              "<th data-priority=\"7\">钻石"+diaNumStr[2]+"</th>" +
	              "<th data-priority=\"8\">钻石"+diaNumStr[3]+"</th>" +
	              "<th data-priority=\"9\">钻石"+diaNumStr[4]+"</th>" +
	              "<th data-priority=\"10\">钻石"+diaNumStr[5]+"</th>" +
	              "<th data-priority=\"11\">充值金额</th>" +
	              "<th data-priority=\"12\">付款方式</th>" +
	              "<th data-priority=\"13\">充值前钻石数</th>" +
	              "<th data-priority=\"14\">充值后钻石数</th>" +
	              "</tr>"+
	              "</thead>" +
	              "<tbody id=\"bodyday\">"+
				  "</tbody></table>"+
				  "</div></div>";
		
		tablebody = "";
		String time=ServerTimer.getNowString().substring(0,7);
		list = cdao.countRechargeByMonth(time);
		it=list.iterator();
		while(it.hasNext()){
			obs=(Object[])it.next();
			if(obs!=null&&obs[0]!=null){
				for(int i=0;i<obs.length;i++){
					  td+="<td>"+obs[i]+"</td>";
				}
			}
			tablebody+="<tr>"+td+"</tr>";
			td="";
		}
			String nowMonth =
		  "<div align=\"center\" data-role=\"collapsible\">"+
	              "<h3 align=\"center\">当月充值记录</h3>"+
				  		  
	              "<table data-role=\"table\" id=\"award-column-toggle05\" " +
	              "data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'>" +
	              "<thead>" +
	              "<tr>"+
	              "<th data-priority=\"2\">日期</th>" +
	              "<th data-priority=\"3\">钻石"+diaNumStr[0]+"</th>" +
	              "<th data-priority=\"4\">钻石"+diaNumStr[1]+"</th>" +
	              "<th data-priority=\"5\">钻石"+diaNumStr[2]+"</th>" +
	              "<th data-priority=\"6\">钻石"+diaNumStr[3]+"</th>" +
	              "<th data-priority=\"7\">钻石"+diaNumStr[4]+"</th>" +
	              "<th data-priority=\"8\">钻石"+diaNumStr[5]+"</th>" +
	              "<th data-priority=\"9\">总金额</th>" +
	              "</tr>"+
	              "</thead>" +
	              "<tbody>" + tablebody + 
				  "</tbody></table></div>";
		
		String allMonthStr = allMothData();
		String bottom = "</center></body></html>";
		
		return header+ajax+nowMonth+allMonthStr+daytable+all+all1+all2+bottom;
	}
	/**
	 * 所有月份充值数据
	 * @return
	 */
	public String allMothData()
	{
		List list1=cdao.countRechargeAllMonth();
		Iterator it1=list1.iterator();
		Object[] obs1=new Object[]{};
		String tablebody1="";
		String td1="";
		
		while(it1.hasNext()){
			obs1=(Object[])it1.next();
			if(obs1!=null&&obs1[0]!=null){
				for(int i=0;i<obs1.length;i++){
					 td1+="<td>"+obs1[i]+"</td>";
				}
			}
			tablebody1+="<tr>"+td1+"</tr>";
			td1="";
		}
		String all1=
				  "<div align=\"center\" data-role=\"collapsible\">"+
	              "<h3 align=\"center\">所有月份充值记录</h3>"+
				  		  
					//"<input onchange=\"f()\" type=\"search\" name=\"search\" id=\"search\" value=\"\">"+
//					"<table data-role=\"table\" id=\"award-column-toggle000111\" " +
//					"data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'>" +
//					"<thead>" +
//					"<tr>"+
//		              "<th data-priority=\"2\">日期</th>" +
//		              "<th data-priority=\"3\">钻石"+diaNumStr[0]+"</th>" +
//		              "<th data-priority=\"4\">钻石"+diaNumStr[1]+"</th>" +
//		              "<th data-priority=\"5\">钻石"+diaNumStr[2]+"</th>" +
//		              "<th data-priority=\"6\">钻石"+diaNumStr[3]+"</th>" +
//		              "<th data-priority=\"7\">钻石"+diaNumStr[4]+"</th>" +
//		              "<th data-priority=\"8\">钻石"+diaNumStr[5]+"</th>" +
//		              "<th data-priority=\"9\">总金额</th>" +
//					"</tr>"+
//					"</thead>" +
//					"<tbody id=\"body1\">"+
//					"</tbody></table>"+
	              
	              "<table data-role=\"table\" id=\"award-column-toggle06\" " +
	              "data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'>" +
	              "<thead>" +
	              "<tr>"+
	              "<th data-priority=\"2\">日期</th>" +
	              "<th data-priority=\"3\">钻石"+diaNumStr[0]+"</th>" +
	              "<th data-priority=\"4\">钻石"+diaNumStr[1]+"</th>" +
	              "<th data-priority=\"5\">钻石"+diaNumStr[2]+"</th>" +
	              "<th data-priority=\"6\">钻石"+diaNumStr[3]+"</th>" +
	              "<th data-priority=\"7\">钻石"+diaNumStr[4]+"</th>" +
	              "<th data-priority=\"8\">钻石"+diaNumStr[5]+"</th>" +
	              "<th data-priority=\"9\">总金额</th>" +
	              "</tr>"+
	              "</thead>" +
	              "<tbody>" + tablebody1 + 
				  "</tbody></table></div>";
		
		return all1;
	}

}

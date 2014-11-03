package server.http;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import common.MyArrays;

import server.command.MyArray;

import business.CountDao;
import business.conut.Sts_BuyProp;
import business.conut.Sts_RewardProp;


public class Html_Prop implements IHtml{

	@Override
	public String getHtml(String content) {
		
		
		CountDao cdao = new CountDao();
		
		int i = 0;
		int j = 0;
		
		
		String header = Ihead.getHead3();
		String bottom = "</body></html>";
		String middle = "";
		List list = null;
		//总的道具使用 

		List<Integer> totalPropBuy = new ArrayList<Integer>();
		List<Integer> totalPropUse = new ArrayList<Integer>();

//		//每月道具购买
		String prop_month = "";
		List Prop_Month = cdao.countPropBuy_month();

		for( i = 0; i < Prop_Month.size(); i++){
			Object[] arr = (Object[])  Prop_Month.get(i);
			prop_month += "<tr>";
			for(j = 0; j< arr.length;j++)
			{
				prop_month += "<td>"+ arr[j]+"</td>";
				if(j == 0)continue;
				int proNum = ((BigDecimal)arr[j]).intValue();
				if(totalPropBuy.size() < (j ) )
					totalPropBuy.add(j -1, proNum);
				else
					totalPropBuy.set(j -1, totalPropBuy.get(j-1) + proNum);
			}
			prop_month += "</tr>";
		}
		//每月道具使用
		String use_month = "";
		List Use_Month =  cdao.countPropUse_month();
    
		for( i =  0; i < Use_Month.size();  i++){
			
			Object[] arr = (Object[]) Use_Month.get(i);
			use_month += "<tr>";
			for(j = 0; j< arr.length;j++)
			{
				use_month += "<td>"+ arr[j]+"</td>";
				if(j == 0)continue;
				int proNum = ((BigDecimal)arr[j]).intValue();
			if(totalPropUse.size() < (j) )
				totalPropUse.add(j -1, proNum);
			else
				totalPropUse.set(j -1, totalPropUse.get(j-1) + proNum);
			}
			use_month += "</tr>";
		}
//		
		//单日道具购买统计
		String prop_day = "";
		List Prop_Day = cdao.countPropBuy_day();
		for(i = 0; i < Prop_Day.size(); i++){
			Object[] arr = (Object[]) Prop_Day.get(i);
			prop_day += "<tr>";
			for(j = 0; j< arr.length;j++)
			{
				prop_day += "<td>"+ arr[j]+"</td>";
			}
			prop_day += "</tr>";
		}
		//单日道具使用统计
		String use_day = "";
		List Use_Day = cdao.countPropUse_day();

		for( i = 0; i < Use_Day.size();i++){
			Object[] arr = (Object[]) Use_Day.get(i);
			use_day += "<tr>";
			for(j = 0; j< arr.length;j++)
			{
				use_day += "<td>"+ arr[j]+"</td>";
			}
			use_day += "</tr>";
		}
		

		
		
		String talbeHead = "";
		List<String> propNamestr = Sts_BuyProp.getpropNames();
		for(i = 0 ;i < propNamestr.size() ; i++)
		{
			talbeHead += "<th data-priority=\""+(i+2)+"\">"+propNamestr.get(i)+"</th>";
		}
		String rewardHead = "";
		List<String> rewardNamestr = Sts_RewardProp.getpropNames();
		for(i = 0 ;i < rewardNamestr.size() ; i++)
		{
			rewardHead += "<th data-priority=\""+(i+2)+"\">"+rewardNamestr.get(i)+"</th>";
		}

		String rewardStr = "";
		
		List rewadList = cdao.findDaysRewardProp();
		if(rewadList != null)
		{
			for(i =0; i < rewadList.size();i++)
			{
				Object[] tmp = (Object[]) rewadList.get(i);
				String tmpStr = "<tr>";
				for(j = 0; j < tmp.length; j++)
				{
					tmpStr += "<td>"+ tmp[j]+"</td>";
				}
				tmpStr  += "</tr>";
				rewardStr += tmpStr;
			}
			
		}
		String rewardMonthStr = "";
		List<Integer>  totalRewardList = MyArrays.asList(0,0,0,0);
		
		List rewadMonthList = cdao.findMonthRewardProp();
		if(rewadMonthList != null)
		{
			for(i =0; i < rewadMonthList.size();i++)
			{
				Object[] tmp = (Object[]) rewadMonthList.get(i);
				String tmpStr = "<tr>";
				for(j = 0; j < tmp.length; j++)
				{
					tmpStr += "<td>"+ tmp[j]+"</td>";
					if(j == 0)continue;
					totalRewardList.set(j -1, totalRewardList.get(j -1) + ((BigDecimal)tmp[j]).intValue());
				}
				tmpStr  += "</tr>";
				rewardMonthStr += tmpStr;
			}
		}
		
		String info = "";
		for(i = 0 ; i < totalPropBuy.size();i++)
		{
			if(propNamestr.get(i).equals("喇叭")||propNamestr.get(i).equals("邀请函")||propNamestr.get(i).equals("强运药水")||propNamestr.get(i).equals("幸运药水"))
			{
				int usedNum = 0;
				int rewardNum = 0;
				int buyNum = totalPropBuy.get(i);
				float useRate = 0;
				if(totalPropUse.size() >0)
				{
					usedNum = totalPropUse.remove(0); //喇叭 的propId为 1
					rewardNum = totalRewardList.remove(0);
				}
				if(buyNum != 0)
					useRate =  (usedNum*1.0f / (buyNum +rewardNum)* 100);
				info += "<tr><td>"+propNamestr.get(i)+"</td><td>"+buyNum+"</td><td>"+ rewardNum+"</td><td>"+usedNum+"</td><td>"+ getFloatStr(useRate)+"</td></tr>";
			}
		}
		
		info += "";
		
		
		middle = 
		         
				"<div data-role=\"collapsible-set\" data-inset=\"false\">"+
				"<div align=\"center\" data-role=\"collapsible\">"+
				"<h3 align=\"center\">单日单个道具的购买统计</h3>"+
				"<table data-role=\"table\" id=\"table-column-toggle13\" data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'><thead><tr><th data-priority=\"1\">时间</th>" +
				 talbeHead+
				"</thead><tbody>"+ prop_day +
				"</table></div>" + 
				
                "<div align=\"center\" data-role=\"collapsible\">"+
                "<h3 align=\"center\">单日单个道具的使用统计</h3>"+
                "<table data-role=\"table\" id=\"table-column-toggle14\" data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'><thead><tr><th data-priority=\"1\">时间</th>" +
                rewardHead+
                "</thead><tbody>" + use_day +
				"</table></div>"+

				"<div align=\"center\" data-role=\"collapsible\">"+
				"<h3 align=\"center\">当月统计道具的购买次数</h3>"+
				"<table data-role=\"table\" id=\"table-column-toggle11\" data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'><thead><tr><th data-priority=\"1\">时间</th>" +
		        talbeHead+
				"</thead><tbody>" + prop_month +
				"</tbody></table>" +
				
				"</div>"+
				 "<div align=\"center\" data-role=\"collapsible\">"+
	                "<h3 align=\"center\">当月道具的使用次数</h3>"+
					"<table data-role=\"table\" id=\"table-column-toggle12\" data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'><thead><tr><th data-priority=\"1\">时间</th>" +
					rewardHead+
					"</thead><tbody>" + use_month +
					"</table></div>" + 
					
				 "<div align=\"center\" data-role=\"collapsible\">"+
	                "<h3 align=\"center\">每日道具奖励统计</h3>"+
					"<table data-role=\"table\" id=\"table-column-toggle12\" data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'><thead><tr><th data-priority=\"1\">时间</th>" +
					rewardHead+
					"</thead><tbody>" + rewardStr +
					"</table></div>" + 	
					
				 "<div align=\"center\" data-role=\"collapsible\">"+
	                "<h3 align=\"center\">每月道具奖励统计</h3>"+
					"<table data-role=\"table\" id=\"table-column-toggle12\" data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'><thead><tr><th data-priority=\"1\">时间</th>" +
					rewardHead+
					"</thead><tbody>" + rewardMonthStr +
					"</table></div>" + 	
				
				"<div align=\"center\" data-role=\"collapsible\">"+
				"<h3 align=\"center\">总道具统计</h3>"+
				"<table data-role=\"table\" id=\"table-column-toggle10\" data-mode=\"columntoggle\" class=\"ui-responsive table-stroke\" border='1' cellpadding='7'><thead><tr><th data-priority=\"1\">道具名</th><th data-priority=\"2\">购买道具</th><th data-priority=\"2\">赠送道具</th><th data-priority=\"3\">使用道具</th><th data-priority=\"4\">道具使用率</th></thead><tbody>" + info + 
				"</tbody></table>" + 
				"</div>"+
				
				"</div>";
		return header + middle + bottom;
	}
	
	public String getFloatStr(float f1)
	{
		 float  a  =  f1;  
		 float  b  =  (float)(Math.round(a*100))/100;
		return b+"%";
	}

}

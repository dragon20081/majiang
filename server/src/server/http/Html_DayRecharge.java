package server.http;

import java.util.List;

import business.CountDao;
import business.conut.Sts_Recharge;


public class Html_DayRecharge implements IHtml {

	@Override
	public String getHtml(String content) {
		// TODO Auto-generated method stub
		CountDao cdao = new CountDao();
		String[] dayStr=content.split("=");
		String date="";
		String msg="0";
		String tr="";
		Sts_Recharge recharge=null;
		if(dayStr.length>1){
			
			date=dayStr[1];
			System.out.println(date);
			if(!"".equals(date)){
				List<Sts_Recharge> list=cdao.findRechargeByDate(date);
			   if(list!=null){
		            for(int i=0;i<list.size();i++){
						recharge = list.get(i);
						tr+="<tr>" +
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
			}
		}
		msg=tr;
		System.out.println("------------------------------------------->dayrecharge");
		return msg;
	}

}

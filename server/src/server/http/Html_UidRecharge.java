package server.http;

import java.util.List;

import business.CountDao;
import business.conut.Sts_Recharge;


public class Html_UidRecharge implements IHtml {

	@Override
	public String getHtml(String content) {
		// TODO Auto-generated method stub
		CountDao cdao = new CountDao();
		String[] uids=content.split("=");
		String uid="";
		int id=0;
		String msg="0";
		String tr="";
		Sts_Recharge recharge=null;
		if(uids.length>1){
			
			uid=uids[1];
			String[] type = uid.split(":");
			int argUid = -1;
			String argAccount = null;
			String nick = null;
			if(type.length > 1)
			{
				if("uid".equals(type[0]))
					argUid=Integer.parseInt(type[1]);
				if("name".equals(type[0]))
					argAccount = type[1];
				if("nick".equals(type[0]))
					nick = type[1];
				List<Sts_Recharge> list=cdao.findPlayerRecharge(argUid, argAccount, nick);
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
		System.out.println("------------------------------------------->uidrecharge");
		return msg;
	}

}

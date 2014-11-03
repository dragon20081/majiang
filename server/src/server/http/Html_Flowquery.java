package server.http;

import java.util.List;

import business.CountDao;
import business.conut.MJUserCharge;

public class Html_Flowquery implements IHtml{

	@Override
	public String getHtml(String content) {
		CountDao cdao = new CountDao();
		String[] uids=content.split("=");
		String uid="";
		int id=0;
		String msg="0";
		String tr="";
		MJUserCharge  charge=null;
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
				List<MJUserCharge> list=cdao.findPlayerChargeFlow(argUid, argAccount, nick);
				if(list!=null){
					for(int i=0;i<list.size();i++){
						charge=list.get(i);
						tr+="<tr>" +
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
//								"<td>"+charge.getTime()+"</td>"+
								"<td>"+charge.getTimeStr()+"</td>"+
								"</tr>";
					}
				}
			}
		}
		msg=tr;
		System.out.println("------------------------------------------->flowquery");
		return msg;
	}

}

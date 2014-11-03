package server.http;


import business.Business;

public class Html_reg implements IHtml {

	@Override
	public String getHtml(String content) { 
		
		String[] params = content.split("&");
		
		Business business = new Business();

		String email = "";
		if(params.length > 2)
			email = params[2];
		
		int flag    = business.register(params[0], params[1],email);
		String str = "";
		switch(flag)
		{
			case 1: str = "1:注册成功!";break; 
			case 3:	str = "3:注册失败,用户名已存在!";break; 
			case 4:	str = "4:注册失败,邮箱已存在!";break; 
			case 0:	str = "0:注册失败!";break; 
		
		}
		return str;
	}

}
package netty.mail;

import javax.mail.*;

public class MyAuthenticator extends  Authenticator{
	String username = null;
	String password = null;
	
	public MyAuthenticator(){}
	
	public MyAuthenticator(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	protected PasswordAuthentication getPasswordAuthentication(){
		return new PasswordAuthentication(this.username, this.password);
	}
}

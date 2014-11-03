package business;

import java.util.concurrent.TimeUnit;

import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.TimerTask;

import server.mj.Global;
import netty.mail.*;

public class SendEmailTimer  implements TimerTask{

	private Timeout  timeout;
	private String   pwd;
	private String  email;
	
	public void start()
	{
		this.timeout  = Global.timer.newTimeout(this, 1000, TimeUnit.MILLISECONDS);
	}
	/**
	 * 发送邮件
	 */
	public void run(Timeout out) throws Exception {
		
		if(!pwd.equals("")){
			//其中co[1]是获取到玩家注册时候的邮箱
			String[] result= email.split("@");
			String[] results = result[1].split("\\.");
			MailSenderInfo mailInfo = new MailSenderInfo();
			//发件人使用发邮件的电子信箱服务器
			mailInfo.setMailServerHost("smtp."+ results[0] +".com");
			mailInfo.setMailServerPort("25"); 
			mailInfo.setValidate(true); 
			
			//发件人的邮箱和密码
			String usermail = "110558055@qq.com";
			String passwordmail = "ofsdn*9@4gd-";
			mailInfo.setUsername(usermail);  
			mailInfo.setPassword(passwordmail);//您的邮箱密码  
			
			mailInfo.setFromAddress(usermail);   
			mailInfo.setToAddress(email); 
			
			mailInfo.setSubject("游戏密码"); 
			mailInfo.setContent("亲，你好！下面就是你的游戏密码！\n" + pwd); 
			
			//这个类主要来发送邮件 
			SimpleSendMail sms = new SimpleSendMail();  
		    boolean flags = sms.sendTextMail(mailInfo);//发送文体格式    
		    System.out.println("发送邮件:"+flags);
		}
	}

	public Timeout getTimeout() {
		return timeout;
	}
	public void setTimeout(Timeout timeout) {
		this.timeout = timeout;
	}
	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}

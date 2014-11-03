package netty.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SimpleSendMail {
	public boolean sendTextMail(MailSenderInfo mailInfo){
		// 判断是否需要身份认证 
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		if(mailInfo.isValidate()){
			// 如果需要身份认证，则创建一个密码验证器   
			authenticator = new MyAuthenticator(mailInfo.getUsername(), mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session   
		Session sendMailSession = Session.getDefaultInstance(pro,authenticator);  
		try {
			// 根据session创建一个邮件消息   
			Message mailMessage = new MimeMessage(sendMailSession);   
			// 创建邮件发送者地址   
			Address from = new InternetAddress(mailInfo.getFromAddress());   
			// 设置邮件消息的发送者   
			mailMessage.setFrom(from);   
			// 创建邮件的接收者地址，并设置到邮件消息中   
			Address to = new InternetAddress(mailInfo.getToAddress());   
			mailMessage.addRecipient(Message.RecipientType.TO,to); 
			// 设置邮件消息的主题   
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间   
			mailMessage.setSentDate(new Date());
			// 设置邮件消息的主要内容   
			mailMessage.setText(mailInfo.getContent());
			// 发送邮件   
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
	
	public static boolean sendHtmlMail(MailSenderInfo mailInfo){
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		if(mailInfo.isValidate()){
			authenticator = new MyAuthenticator(mailInfo.getUsername(), mailInfo.getPassword());
		}
		Session sendMailSession = Session.getDefaultInstance(pro,authenticator);  
		try {
			// 根据session创建一个邮件消息   
			Message mailMessage = new MimeMessage(sendMailSession);   
			// 创建邮件发送者地址   
			Address from = new InternetAddress(mailInfo.getFromAddress());   
			// 设置邮件消息的发送者   
			mailMessage.setFrom(from);   
			Address to = new InternetAddress(mailInfo.getToAddress());   
			mailMessage.setRecipient(Message.RecipientType.TO,to);  
			mailMessage.setSubject(mailInfo.getSubject());
			mailMessage.setSentDate(new Date());
			Multipart mainPart = new MimeMultipart();   
			BodyPart html = new MimeBodyPart();   
			html.setContent(mailInfo.getContent(), "text/html; charset = utf-8");
			mainPart.addBodyPart(html);   
			mailMessage.setContent(mainPart);   
			Transport.send(mailMessage);   
			return true;
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
		return false;
	}
}

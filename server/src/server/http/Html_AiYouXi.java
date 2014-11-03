package server.http;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import server.command.cmd.CCMD11041;
import server.command.cmd.CCMD11101;
import server.mj.Global;
import server.mj.MgsCache;
import server.mj.MgsPlayer;
import server.mj.ServerTimer;
import business.CountDao;
import business.UserDao;
import business.conut.MJUserCharge;
import business.conut.Sts_Arpu;
import business.conut.Sts_Recharge;
import business.entity.MJ_PayInfo;
import business.entity.MJ_User;

public class Html_AiYouXi implements IHtml{

	
	private static final Logger logger = Logger.getLogger(Html_Payback.class.getName());
	public Sts_Arpu arpu;
	private final static String inputCharset = "utf8";
	private final static String securekey = "0ee4b27a38aeae735610";
    public String content;
    
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String getHtml(String content) {
		
		try {
			chonghzi();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	private String hRet = "";
	
	public  void chonghzi() throws DocumentException
	{
		String userId = "";
		String consumeCode = "";
		String transIDO = "";
		
		SAXReader saxReader = new SAXReader();
	    Document document = saxReader.read(new ByteArrayInputStream(content.getBytes()));   
		Element rootElement = document.getRootElement();
		hRet = rootElement.elementTextTrim("hRet");
		if(rootElement.elementTextTrim("hRet").equals("0")){
			userId = rootElement.elementTextTrim("cpParam");
			consumeCode = rootElement.elementTextTrim("consumeCode");
			transIDO = rootElement.elementTextTrim("transIDO");
			//保存玩家数据
			this.save(Integer.parseInt(userId), consumeCode);
		}else{
			//充值失败
		}
	}
//	
//	/**
//	 * 加密算法
//	 * 
//	 * @throws IOException
//	 * @throws FileNotFoundException
//	 */
//	public static String createSekey( Map<String, String> sArray) {
//		String sekey;
//		String formatUri = createLinkString(paraFilter(sArray));
//		formatUri = formatUri + "&save=" + securekey;
//		logger.info("formatUri " +formatUri );
//		sekey = md5(formatUri);
//		logger.info("create sekey: "+ sekey);
//		
//		return sekey;
//	}
	/**
	 * 除去数组中的空值和签名参数
	 * 
	 * @param sArray
	 *            签名参数组
	 * @return 去掉空值与签名参数后的新签名参数组
	 */
	public static Map<String, String> paraFilter(Map<String, String> sArray) {

		Map<String, String> result = new HashMap<String, String>();

		if (sArray == null || sArray.size() <= 0) {
			return result;
		}
		
		for (String key : sArray.keySet()) {
			String value = sArray.get(key);
			if (value == null || key.equalsIgnoreCase("sekey")) {
				continue;
			}
			result.put(key, value);
		}
		logger.info("paraFilter");
		return result;
	}
	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, String> params) {

		ArrayList<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		String prestr = "";

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			if (i == keys.size() - 1) { // 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}
		logger.info("createLinkString:"+prestr);
		return prestr;
	}
//	/**
//	 * 对字符串进行MD5签名
//	 * 
//	 * @param text
//	 *            明文
//	 * 
//	 * @return 密文
//	 */
//	public static String md5(String text) {
//		byte[] bytes = getContentBytes(text, inputCharset);
//		logger.info(""+bytes.length);
//		String str = DigestUtils.md5Hex(bytes);
//		logger.info("md5:"+str);
//		return str;
//	}
//	/**
//	 * @param content
//	 * @param charset
//	 * @return
//	 * @throws SignatureException
//	 * @throws UnsupportedEncodingException
//	 */
//	private static byte[] getContentBytes(String content, String charset) {
//		if (charset == null || "".equals(charset)) {
//			return content.getBytes();
//		}
//		try {
//			byte[] bytes = content.getBytes(charset);
//			logger.info(""+bytes.length);
//			return bytes;
//		} catch (UnsupportedEncodingException e) {
//			throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:"
//					+ charset);
//		}
//	}
//	
	//int flag, String tradeCode, String tradeTitle, String tradeType
	public void save(int userId,String consumeCode){
		UserDao udao = new UserDao();
		MJ_User user = null; 
		user = udao.findPlayerByUid(userId);
				
		MgsPlayer player =MgsCache.getInstance().userInfos.get(user.getName());
		if(user!=null){
			MJ_PayInfo info = findPayInfoByConsume(consumeCode);
			if(player != null && !player.offline){
				
				payRMB(info,player);    //玩家在线，发送玩家充值成功指令，和钻石该变量
			}else{
//			 // 玩家不在线， 直接保存充值的结果到数据库userInfo.save(user);
				payRMB(info,user);
			}
		
		}
	}
	
	public MJ_PayInfo findPayInfoByConsume(String consumeCode)
	{
		 Map<Integer,MJ_PayInfo> payItems  = Global.payItems;
		 Iterator<MJ_PayInfo> it = payItems.values().iterator();
		 while(it.hasNext())
		 {
			 MJ_PayInfo info = it.next();
			 if(info.getPayStr_android().equals(consumeCode) && info.getPingtai().equals("AIYOUXI"))
			 {
				 return info;
			 }
		 }
		 return null;
	}
	
	/**
	 * 人民币充值  玩家在线
	 */
	public void payRMB(MJ_PayInfo payItem,MgsPlayer player)
	{
		CCMD11041 cmd041 = new CCMD11041();
		cmd041.setPlayer(player);
		cmd041.payItem = payItem;
		
		MJ_User user = player.getBusiness().getPlayer();
		int beforeGold = user.getGold();
		int beforeDia = user.getDianQuan();
		int modGold = 0;
		int modDia = payItem.getDia();
		
		int pay_dia_before = user.getDianQuan();
		cmd041.pay_dia_before = pay_dia_before;
		user.setDianQuan(user.getDianQuan() + payItem.getDia());
		player.getBusiness().saveDataObject(user);
		CCMD11101 cmd101 = new CCMD11101();
		cmd101.auto_deal(player,6);
		cmd041.savePayCount();
		
		player.saveUserChargeRec(11141+"",beforeGold , modGold, user.getGold(), beforeDia, modDia, user.getDianQuan(), "充值钻石:"+payItem.getDia()+"");
		//充值统计
	}
	/**
	 * 人民币充值  玩家离线
	 */
	public void payRMB(MJ_PayInfo payItem,MJ_User user)
	{
		UserDao udao = new UserDao();
		CCMD11041 cmd041 = new CCMD11041();
		cmd041.payItem = payItem;
		
		int beforeGold = user.getGold();
		int beforeDia = user.getDianQuan();
		int modGold = 0;
		int modDia = payItem.getDia();
		
		int pay_dia_before = user.getDianQuan();
		cmd041.pay_dia_before = pay_dia_before;
		user.setDianQuan(user.getDianQuan() + payItem.getDia());
		udao.saveObject(user);
		cmd041.savePayCountByMJ_User(user);
		saveUserChargeRec(11141+"",beforeGold , modGold, user.getGold(), beforeDia, modDia, user.getDianQuan(), "充值钻石:"+payItem.getDia()+"",user);
		//充值统计
	}
	
	/**
	 * 保存玩家金币钻石流动情况
	 */
	public void saveUserChargeRec(String cmd,int gold,int modGold,int afterGold,int dia,int modDia,int afterDia,String info,MJ_User user)
	{
		MJUserCharge charge = new MJUserCharge();
		charge.setUid(user.getUid());
		charge.setName(user.getName());
		charge.setNickName(user.getNick());
		charge.setTime(ServerTimer.distOfSecond(Calendar.getInstance()));
		charge.setTimeStr(ServerTimer.getNowString());
		
		charge.setGold(gold);
		charge.setModGold(modGold);
		charge.setAfterGold(afterGold);
		charge.setDia(dia);
		charge.setModDia(modDia);
		charge.setAfterDia(afterDia);
		charge.setCmd(cmd);
		charge.setInfo(info);
		UserDao udao = new UserDao();
		udao.saveObject(charge);
	}
	
}

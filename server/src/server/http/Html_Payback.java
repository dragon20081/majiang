package server.http;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.codec.digest.DigestUtils;

import server.command.cmd.CCMD11101;
import server.mj.Global;
import server.mj.MgsCache;
import server.mj.MgsPlayer;
import server.mj.ServerTimer;

import business.CountDao;
import business.UserDao;
import business.conut.Sts_Arpu;
import business.conut.Sts_Recharge;
import business.entity.MJ_User;
import business.entity.M_Shop;



public class Html_Payback implements IHtml {
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
		if(content == null || content.equals("")) return "false";
		
		String html = "";
		logger.warning("mopo:"+content);
		String contents[] = content.split("&");
		Map map = new HashMap();
		String sign = "";
		String []tradeCode = null;
		String tradeType = "";
		for(int i = 0; i < contents.length; i++){
			if(!contents[i].split("=")[0].equals("sign")){
				map.put(contents[i].split("=")[0], contents[i].split("=")[1]);
				if(contents[i].split("=")[0].equals("tradecode")){
					tradeCode = contents[i].split("=")[1].split("-");
				}
				if(contents[i].split("=")[0].equals("tradetype")){
					tradeType = contents[i].split("=")[1];
				}
			}else {
				sign = contents[i].split("=")[1];
				logger.info("sign:"+sign);
			}
		}
		logger.info("CreateSign");
		String md5Str = createSekey(map);
		logger.warning("md5Str:"+md5Str);
		//如果生成的签名和提供的签名一样就可以对玩家的道具进行修改
		if(md5Str.equals(sign)){
			html = "true";
			this.setContent(content);
			this.save(0, tradeCode[0], tradeCode[1], tradeType);
			
		}else {
			html = "false";
//			this.save(1, tradeCode[0], tradeCode[1], tradeType);
		}
		
		return html;
	}
	
	/**
	 * 加密算法
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static String createSekey( Map<String, String> sArray) {
		String sekey;
		String formatUri = createLinkString(paraFilter(sArray));
		formatUri = formatUri + "&save=" + securekey;
		logger.info("formatUri " +formatUri );
		sekey = md5(formatUri);
		logger.info("create sekey: "+ sekey);
		
		return sekey;
	}
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
	/**
	 * 对字符串进行MD5签名
	 * 
	 * @param text
	 *            明文
	 * 
	 * @return 密文
	 */
	public static String md5(String text) {
		byte[] bytes = getContentBytes(text, inputCharset);
		logger.info(""+bytes.length);
		String str = DigestUtils.md5Hex(bytes);
		logger.info("md5:"+str);
		return str;
	}
	/**
	 * @param content
	 * @param charset
	 * @return
	 * @throws SignatureException
	 * @throws UnsupportedEncodingException
	 */
	private static byte[] getContentBytes(String content, String charset) {
		if (charset == null || "".equals(charset)) {
			return content.getBytes();
		}
		try {
			byte[] bytes = content.getBytes(charset);
			logger.info(""+bytes.length);
			return bytes;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:"
					+ charset);
		}
	}
	public void save(int flag, String tradeCode, String tradeTitle, String tradeType){
		logger.info("flag:"+flag);
		logger.info("tradeCode:"+tradeCode);
		logger.info("tradeTitle:"+tradeTitle);
		logger.info("tradeType:"+tradeType);
		UserDao udao = new UserDao();
		CountDao cdao= new CountDao();
		MJ_User user = null; 
		int sum = 0;
		int money = 0;
		user = udao.findPlayerByUid(Integer.parseInt(tradeCode));
				
		int[] diaArr = Sts_Recharge.getRechargeNum();
		sum = diaArr[Integer.parseInt(tradeTitle)];
		logger.info("Gold1:"+user.getGold());
		logger.info("money:"+money);
		user.setDianQuan(user.getDianQuan() + sum);
		logger.info("Gold2:"+user.getGold());
		
		MgsPlayer player =MgsCache.getInstance().userInfos.get(user.getName());
		if(flag == 0&&user!=null){
			if(player != null && !player.offline){
              //玩家在线，发送玩家充值成功指令，和钻石该变量
				CCMD11101 cmd101 = new CCMD11101();
				cmd101.auto_deal(player, 6);
			}else{
//			 // 玩家不在线， 直接保存充值的结果到数据库userInfo.save(user);
				cdao.saveSts_Object(user);
			}
			//保存到玩家充值信息里面
			Sts_Recharge recharge = new Sts_Recharge();
			recharge.setAbsDay(ServerTimer.distOfSecond(Calendar.getInstance()));
			recharge.setAccount(user.getName());
			recharge.setUid(user.getUid());
			recharge.setDay(ServerTimer.getNowString());
			recharge.setTime1(ServerTimer.getNowString().substring(0,10));
			recharge.setTime2(ServerTimer.getNowString().substring(0,7));
			
			
			this.arpu = cdao.findArpuToday();
			if(arpu == null)
			{
				this.arpu = new Sts_Arpu();
				this.arpu.setAbsDay(ServerTimer.distOfDay(Calendar.getInstance()));
				this.arpu.setDay(ServerTimer.getDay());
			}
			if(cdao.countPlayerCharge(user.getUid()) == 0){
					this.arpu.setChargeNum(arpu.getChargeNum() + 1);
					this.arpu.setAddCharge(this.arpu.getAddCharge() + 1);
				}else{//不是新增用户
					  this.arpu.setChargeNum(arpu.getChargeNum() + 1);
				}
			
			switch (Integer.parseInt(tradeTitle)) {
			case 1:
				recharge.setDia1(recharge.getDia1() + 1);
				recharge.setMoney(2);
				this.arpu.setAmount(this.arpu.getAmount() + 2);
				break;
			case 2:
				recharge.setDia2(recharge.getDia2() + 1);
				recharge.setMoney(4);
				this.arpu.setAmount(this.arpu.getAmount() + 4);
				break;
			case 3:
				recharge.setDia3(recharge.getDia3() + 1);
				recharge.setMoney(6);
				this.arpu.setAmount(this.arpu.getAmount() + 6);
				break;
			case 4:
				recharge.setDia4(recharge.getDia4() + 1);
				recharge.setMoney(8);
				this.arpu.setAmount(this.arpu.getAmount() + 8);
				break;
			case 5:
				recharge.setDia5(recharge.getDia5() + 1);
				recharge.setMoney(10);
				this.arpu.setAmount(this.arpu.getAmount() + 10);
				break;
			case 6:
				recharge.setDia6(recharge.getDia6() + 1);
				recharge.setMoney(12);
				this.arpu.setAmount(this.arpu.getAmount() + 12);
				break;
			}
			cdao.saveSts_Object(recharge);
			this.arpu.setArpuv((double)this.arpu.getAmount()/(double)this.arpu.getChargeNum());
			cdao.saveSts_Object(arpu);
			
			switch (Integer.parseInt(tradeType)) {
			case 0:
//				counts.setStyle1(counts.getStyle1() + 1);
				break;
			}
		}
	}
	
}

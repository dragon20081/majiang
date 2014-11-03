package business.conut;


/**
 * 充值数据记录
 * 通知生成记录
 * @author xue
 *
 */

public class Sts_360Order {

	private int id;
	
	private String app_order_id = "";
	private int uid;
	private String nick = "";
	
	private String app_key = "";
	private String product_id ="";
	private int amount;
	private String app_uid = "";
	private String app_ext1 = "";
	private String app_ext2 = "";	
	private long user_id;
	private long order_id;
	private String gateway_flag = "";
	private String sign_type = "";

	private String sign_return = "";
	private String sign = "";
	
	private String verify_360 = "";        //订单已与360服务器确认
	private boolean verify_client = false;     //订单已于前台确认
	
	private int diamand;
	private int RMB;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getApp_order_id() {
		return app_order_id;
	}
	public void setApp_order_id(String app_order_id) {
		this.app_order_id = app_order_id;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getApp_key() {
		return app_key;
	}
	public void setApp_key(String app_key) {
		this.app_key = app_key;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getApp_uid() {
		return app_uid;
	}
	public void setApp_uid(String app_uid) {
		this.app_uid = app_uid;
	}
	public String getApp_ext1() {
		return app_ext1;
	}
	public void setApp_ext1(String app_ext1) {
		this.app_ext1 = app_ext1;
	}
	public String getApp_ext2() {
		return app_ext2;
	}
	public void setApp_ext2(String app_ext2) {
		this.app_ext2 = app_ext2;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public long getOrder_id() {
		return order_id;
	}
	public void setOrder_id(long order_id) {
		this.order_id = order_id;
	}
	public String getGateway_flag() {
		return gateway_flag;
	}
	public void setGateway_flag(String gateway_flag) {
		this.gateway_flag = gateway_flag;
	}
	public String getSign_type() {
		return sign_type;
	}
	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}
	public String getSign_return() {
		return sign_return;
	}
	public void setSign_return(String sign_return) {
		this.sign_return = sign_return;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getVerify_360() {
		return verify_360;
	}
	public void setVerify_360(String verify_360) {
		this.verify_360 = verify_360;
	}
	public boolean isVerify_client() {
		return verify_client;
	}
	public void setVerify_client(boolean verify_client) {
		this.verify_client = verify_client;
	}
	public int getDiamand() {
		return diamand;
	}
	public void setDiamand(int diamand) {
		this.diamand = diamand;
	}
	public int getRMB() {
		return RMB;
	}
	public void setRMB(int rMB) {
		RMB = rMB;
	}
	
}

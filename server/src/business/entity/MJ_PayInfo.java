package business.entity;
/**
 * 充值数据
 * @author xue
 */
public class MJ_PayInfo {

	public int id;
	public int payId;
	public String payStr_android;
	public String payStr_apple;
	public int rmb;
	public int dia;
	public int flag;
	public int item;
	public String pingtai = "";
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRmb() {
		return rmb;
	}
	public void setRmb(int rmb) {
		this.rmb = rmb;
	}
	public int getDia() {
		return dia;
	}
	public void setDia(int dia) {
		this.dia = dia;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public int getPayId() {
		return payId;
	}
	public void setPayId(int payId) {
		this.payId = payId;
	}
	public String getPayStr_android() {
		return payStr_android;
	}
	public void setPayStr_android(String payStr_android) {
		this.payStr_android = payStr_android;
	}
	public String getPayStr_apple() {
		return payStr_apple;
	}
	public void setPayStr_apple(String payStr_apple) {
		this.payStr_apple = payStr_apple;
	}
	public int getItem() {
		return item;
	}
	public void setItem(int item) {
		this.item = item;
	}
	public String getPingtai() {
		return pingtai;
	}
	public void setPingtai(String pingtai) {
		this.pingtai = pingtai;
	}
}

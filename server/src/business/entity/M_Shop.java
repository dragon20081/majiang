package business.entity;
/**
 * 商店出售商品类: 
 * @author xue
 */
public class M_Shop {
	
	private int id;
	private String type = "";
	private int proId;
	private int price_gold;
	private int price_dianquan;
	
	private int propNum; //商品数量
	private int flag;
	private int onSell;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getProId() {
		return proId;
	}
	public void setProId(int proId) {
		this.proId = proId;
	}

	public int getOnSell() {
		return onSell;
	}
	public void setOnSell(int onSell) {
		this.onSell = onSell;
	}
	public int getPrice_gold() {
		return price_gold;
	}
	public void setPrice_gold(int price_gold) {
		this.price_gold = price_gold;
	}
	public int getPrice_dianquan() {
		return price_dianquan;
	}
	public void setPrice_dianquan(int price_dianquan) {
		this.price_dianquan = price_dianquan;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getPropNum() {
		return propNum;
	}
	public void setPropNum(int propNum) {
		this.propNum = propNum;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
}

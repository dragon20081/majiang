package business.conut;

public class Sts_ApplePay {

	private int id;
	private String identifier = "";
	private String productIdentifier = "";
	private String date0 = "";
	private String receipt = "";
	private String result = "";
	private String original = "";
	private String payTime = "";
	private int used = 0;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getProductIdentifier() {
		return productIdentifier;
	}
	public void setProductIdentifier(String productIdentifier) {
		this.productIdentifier = productIdentifier;
	}
	public String getDate0() {
		return date0;
	}
	public void setDate0(String date0) {
		this.date0 = date0;
	}
	public String getReceipt() {
		return receipt;
	}
	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getOriginal() {
		return original;
	}
	public void setOriginal(String original) {
		this.original = original;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public int getUsed() {
		return used;
	}
	public void setUsed(int used) {
		this.used = used;
	}
	
}

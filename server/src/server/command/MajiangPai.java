package server.command;

public class MajiangPai {

	private int paiId;
	private int num;
	
	public MajiangPai()
	{
		
	}
	public MajiangPai(int id,int num)
	{
		this.paiId =  id;
		this.num   =  num;
	}
	public int getPaiId() {
		return paiId;
	}
	public void setPaiId(int paiId) {
		this.paiId = paiId;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	@Override
	public String toString() {
		return "MajiangPai [paiId=" + paiId + ", num=" + num + "]";
	}
	
}

package server.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.logging.Logger;

import server.command.cmd.SCMD11009;
import server.mj.MgsPlayer;

import common.MyArrays;

public class GroupShoupai {

	
	private static final Logger logger = Logger.getLogger(GroupShoupai.class.getName());
	
	private List<List<Integer>>shun_ke_out ;
	private List<Integer>shuang_out;
	private List<Integer> dan_out;
	private int type;
	private List<Integer> ting;
	public int hupaiId;
	public boolean isAnQidui = false;
	private boolean isZimo  = false;
	
	public MgsPlayer p;
	
	public GroupShoupai(int hupaiId,int type,List<List<Integer>>shun_ke_out,List<Integer>shuang_out,List<Integer> dan_out,List<Integer> ting,boolean isZimo)
	{
		this.hupaiId = hupaiId;
		this.ting = ting;
		this.type  = type;
		this.shun_ke_out =  shun_ke_out;
		this.shuang_out  =  shuang_out;
		this.dan_out     =  dan_out;
		this.isZimo = isZimo;
	}
	public List<List<Integer>> getShun_ke_out() {
		return shun_ke_out;
	}
	public void setShun_ke_out(List<List<Integer>> shun_ke_out) {
		this.shun_ke_out = shun_ke_out;
	}
	public List<Integer> getShuang_out() {
		return shuang_out;
	}
	public void setShuang_out(List<Integer> shuang_out) {
		this.shuang_out = shuang_out;
	}
	public List<Integer> getDan_out() {
		return dan_out;
	}
	public void setDan_out(List<Integer> dan_out) {
		this.dan_out = dan_out;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public List<Integer> getTing() {
		return ting;
	}
	public void setTing(List<Integer> ting) {
		this.ting = ting;
	}
	public int getHupaiId() {
		return hupaiId;
	}
	public void setHupaiId(int hupaiId) {
		this.hupaiId = hupaiId;
	}
	public List<Integer> getAnke() {
		return anke;
	}
	public void setAnke(List<Integer> anke) {
		this.anke = anke;
	}
	public List<List<Integer>> getShun() {
		return shun;
	}
	public void setShun(List<List<Integer>> shun) {
		this.shun = shun;
	}
	public List<Integer> getJiang() {
		return jiang;
	}
	public void setJiang(List<Integer> jiang) {
		this.jiang = jiang;
	}



	private List<Integer>       anke;
	private List<List<Integer>> shun;
	private List<Integer>      jiang;
	/**
	 * 格式化手牌 : 暗刻  顺子  将牌
	 */
	public void patternShoupai()
	{
		anke  = new ArrayList<Integer>();;
		shun  = new ArrayList<List<Integer>>();
		jiang  =  new ArrayList<Integer>();
		for(List<Integer> tmpList : shun_ke_out)
		{
			if(tmpList.get(0)== tmpList.get(1))anke.add(tmpList.get(0));
			if(tmpList.get(0)!= tmpList.get(1))shun.add(tmpList);
		}
		for(Integer pai : this.shuang_out)
		{
			if(this.hupaiId == pai)
			{
				if(shuang_out.size() > 1)
				{
					if(isZimo)anke.add(pai);
					else
						p.getPeng().add(pai);
				}else
				{
					jiang  = MyArrays.asList(pai,pai);
				}
			}else
			{
				jiang  = MyArrays.asList(pai,pai);
			}
		}
		if(this.dan_out.size()  == 1)
		{
			jiang  = MyArrays.asList(dan_out.get(0),dan_out.get(0));
		}else if(this.dan_out.size()  > 1)
		{
			int[] arr =  new int[]{this.hupaiId,dan_out.get(0),dan_out.get(1)};
			Arrays.sort(arr);
			shun.add(MyArrays.ArrasList(arr));
		}
		if(jiang.size() ==0)
		{
			logger.error("将牌出错:");
			logger.error(toString());
			logger.error("碰:"+p.getPeng().toString() +"杠:"+ p.getGang().toString() + "吃："+p.getChi().toString());
		}
		
	}
	
	@Override
	public String toString() {
		
		String   str = "胡牌:" +this.hupaiId + "  类型:"+type 
		+"刻顺:["  + shun_ke_out.toString() + "],双:[" + shuang_out.toString() + "],单:[" +dan_out.toString()+"],听:["+ting.toString()+"]" ;
		return str;
	}
	
}

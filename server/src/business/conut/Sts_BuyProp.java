package business.conut;

import java.util.ArrayList;
import java.util.List;

public class Sts_BuyProp {

	private int id;
	private int absDay;
	private String day;
	private String time1 = "";  //天  2014-03-15
	private String time2 = "";  //月  2014-03
	private int uid;
	private String account = "";
	private String nick = "";
	private int prop1;
	private int prop2;
	private int prop3;
	private int prop4;
	private int prop5;
	private int prop6;
	private int prop7;
	private int prop8;
	private int prop9;
	private int prop10;
	private int prop11;
	private int prop12;
	private int prop13;
	private int prop14;
	
	private int prop15;
	private int prop16;
	private int prop17;
	private int prop18;
	private int prop19;
	private int prop20;
	
	private int dia;  //价格
	private int gold;
	private String price = "";
	private int beforeDia;
	private int afterDia;
	private int beforeG;
	private int afterG;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public int getDia() {
		return dia;
	}
	public void setDia(int dia) {
		this.dia = dia;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public int getBeforeDia() {
		return beforeDia;
	}
	public void setBeforeDia(int beforeDia) {
		this.beforeDia = beforeDia;
	}
	public int getAfterDia() {
		return afterDia;
	}
	public void setAfterDia(int afterDia) {
		this.afterDia = afterDia;
	}
	public int getBeforeG() {
		return beforeG;
	}
	public void setBeforeG(int beforeG) {
		this.beforeG = beforeG;
	}
	public int getAfterG() {
		return afterG;
	}
	public void setAfterG(int afterG) {
		this.afterG = afterG;
	}
	public int getAbsDay() {
		return absDay;
	}
	public void setAbsDay(int absDay) {
		this.absDay = absDay;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getTime1() {
		return time1;
	}
	public void setTime1(String time1) {
		this.time1 = time1;
	}
	public String getTime2() {
		return time2;
	}
	public void setTime2(String time2) {
		this.time2 = time2;
	}
	public int getProp1() {
		return prop1;
	}
	public void setProp1(int prop1) {
		this.prop1 = prop1;
	}
	public int getProp2() {
		return prop2;
	}
	public void setProp2(int prop2) {
		this.prop2 = prop2;
	}
	public int getProp3() {
		return prop3;
	}
	public void setProp3(int prop3) {
		this.prop3 = prop3;
	}
	public int getProp4() {
		return prop4;
	}
	public void setProp4(int prop4) {
		this.prop4 = prop4;
	}
	public int getProp5() {
		return prop5;
	}
	public void setProp5(int prop5) {
		this.prop5 = prop5;
	}
	public int getProp6() {
		return prop6;
	}
	public void setProp6(int prop6) {
		this.prop6 = prop6;
	}
	public int getProp7() {
		return prop7;
	}
	public void setProp7(int prop7) {
		this.prop7 = prop7;
	}
	public int getProp8() {
		return prop8;
	}
	public void setProp8(int prop8) {
		this.prop8 = prop8;
	}
	public int getProp9() {
		return prop9;
	}
	public void setProp9(int prop9) {
		this.prop9 = prop9;
	}
	public int getProp10() {
		return prop10;
	}
	public void setProp10(int prop10) {
		this.prop10 = prop10;
	}
	public int getProp11() {
		return prop11;
	}
	public void setProp11(int prop11) {
		this.prop11 = prop11;
	}
	public int getProp12() {
		return prop12;
	}
	public void setProp12(int prop12) {
		this.prop12 = prop12;
	}
	public int getProp13() {
		return prop13;
	}
	public void setProp13(int prop13) {
		this.prop13 = prop13;
	}
	public int getProp14() {
		return prop14;
	}
	public void setProp14(int prop14) {
		this.prop14 = prop14;
	}
	
	public int getProp15() {
		return prop15;
	}
	public void setProp15(int prop15) {
		this.prop15 = prop15;
	}
	public int getProp16() {
		return prop16;
	}
	public void setProp16(int prop16) {
		this.prop16 = prop16;
	}
	public int getProp17() {
		return prop17;
	}
	public void setProp17(int prop17) {
		this.prop17 = prop17;
	}
	public int getProp18() {
		return prop18;
	}
	public void setProp18(int prop18) {
		this.prop18 = prop18;
	}
	public int getProp19() {
		return prop19;
	}
	public void setProp19(int prop19) {
		this.prop19 = prop19;
	}
	public int getProp20() {
		return prop20;
	}
	public void setProp20(int prop20) {
		this.prop20 = prop20;
	}
	public static List<String> getpropNames()
	{
		List<String> list= new  ArrayList<String>();
		list.add("喇叭");
		list.add("技能2");
		list.add("技能3");
		list.add("技能5");
		list.add("形象100");
		list.add("形象101");
		list.add("形象102");
		list.add("形象103");
		list.add("形象104");
		list.add("形象105");
		list.add("金币1");
		list.add("金币2");
		list.add("金币3");
		list.add("金币4");
		list.add("邀请函");
		list.add("幸运药水");
		list.add("强运药水");
		
		return list;
		
		
//	case 1:
//		
//		buy.setProp1(1);
//		break;
//	case 2:
//		buy.setProp2(1);
//		break;
//	case 3: 
//		buy.setProp3(1);
//		break;
//	case 5: 
//		buy.setProp4(1);
//		break;
//	case 100:
//		buy.setProp5(1);
//		break;
//	case 101:
//		buy.setProp6(1);
//		break;
//	case 102:
//		buy.setProp7(1);
//		break;
//	case 103:
//		buy.setProp8(1);
//		break;
//	case 104:
//		buy.setProp9(1);
//		break;
//	case 105: 
//		buy.setProp10(1);
//		break;
//	case 10:   //金币0
//		buy.setProp11(1);
//		break;
//	case 11: //金币1
//		buy.setProp12(1);
//		break;
//	case 12: //金币2
//		buy.setProp13(1);
//		break;
//	case 13: //金币3
//		buy.setProp14(1);
//		break;
		
	}
	
}

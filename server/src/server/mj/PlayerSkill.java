package server.mj;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import business.entity.MJ_Skill;

import server.command.cmd.CCMD11031;
import server.command.cmd.CCMD11032;
import server.command.cmd.CCMD11033;
import server.command.cmd.CCMD11034;
import server.command.cmd.CCMD11111;

import common.Log;

/**
 * 自摸牌 ，根据规则计算出牌， 如果成功返回牌id ,失败返回牌ID -1
 */
public class PlayerSkill {

	// 技能ID
	public static final int ID_QIANGYUN = 1;
	public static final int ID_DILI = 2;
	public static final int ID_LINGSHANGKAIHUA = 3;
	public static final int ID_FEIYANHUANCHAO = 4;
	public static final int ID_BUDONGRUSHAN = 5;
	public static final int ID_HUANGQUAN = 6;
	public static final int ID_HUANGWUSHENGYAN = 7;

	//新增技能
	public static final int ID_SIFENGSHI = 8;
	public static final int ID_SANYUANSHI = 9;
	public static final int ID_ZHISHUI = 10;
	public static final int ID_FUSHI = 11;
	public static final int ID_YUZHEN = 12;
	public static final int ID_GUIMEN = 13;
	public static final int ID_SHANGMEN = 14;
	
	/**
	 * 强运 被动 作为被神庇佑的存在，能无视“运”摸到自己想要的牌！ 气满 return 获得牌 -1为获得牌
	 */
	public int qiangyun(MgsPlayer p) {
		int pai = -1;
		// 1/2概率无视运
		int rand = (int) (Math.random() * 100);
		int rate = (int) ((p.getQi() * 0.5));
		if (p.isTing()) {
			rate = (int) (p.getQi() * 0.2);
			if (rand > rate)
				return pai;
			Log.log("-->qiangyun: 概率" + rate + ":随机值:" + rand);

			pai = this.getPai_zimo(p);
			if (pai != -1)
				useSkillMsg(p, ID_QIANGYUN);
			return pai;
		}
		if (rand > rate)
			return pai;
		if (p.getWishNextPais().size() == 0)
			return -1; // 条件 2:wish或者jiao数组不为空
			// pai = getPai_zimo(p);
		Log.log("技能 强运:  好牌 :" + p.getWishNextPais());
		pai = getNormal_wishPai(p);
		if (pai != -1) {
			// 使用技能成功
			// useSkillMsg(p,ID_QIANGYUN);
			Log.log("技能 强运:  牌 :" + pai);
			useSkillMsg(p, ID_QIANGYUN);
		}
		return pai;
	}

	public int getNormal_wishPai(MgsPlayer p) {
		int pai = -1;
		int rand = 0;
		Room r = p.getRoom();
		while (p.getWishNextPais().size() > 0) {
			rand = (int) (Math.random() * p.getWishNextPais().size());
			pai = p.getWishNextPais().get(rand);
			if (r.IDList.contains((Integer) pai))
				return pai;
			p.getWishNextPais().remove((Integer) pai);
			pai = -1;
		}
		return pai;
	}

	public void useSkillMsg(MgsPlayer p, int skillId) {
		Log.log("useSkillMsg skillId:" + skillId);
		CCMD11033 ccmd = new CCMD11033();
		ccmd.auto_deal(p, skillId);
	}

	public void useSkillMsg_single(MgsPlayer p, int skillId) {
		CCMD11033 ccmd = new CCMD11033();
		ccmd.auto_deal_single(p, skillId);
	}

	public void skillEffected(MgsPlayer p, int skillId) {
		Log.log("skillEffected skillId:" + skillId);
		CCMD11034 ccmd = new CCMD11034();
		ccmd.auto_deal(p, skillId);
	}

	/**
	 * 底力 被动 “我不会输的！”点炮后，让气恢复到满值。 运下降为负触发
	 */
	public void dili(MgsPlayer p) {
		Log.log("dili--->");
		MJ_Skill skill = Global.skills.get("底力");
		int qi = p.getQi() + skill.getAttr0();
		if (qi > p.getMaxQi())
			qi = p.getMaxQi();
		p.setQi(qi);
		useSkillMsg(p, ID_DILI);

	}

	/**
	 * 岭上开花 被动 高岭之花，于此盛开！极易杠上花自摸！ 气越高概率越高
	 */
	public int lingShangKaiHua(MgsPlayer p) {
		int pai = -1;
		if (!p.isGangshanghua())
			return pai;
		int rand =100 - p.getLiangang() * 10;// 概率 100 80 60
		int myrand = (int) (Math.random() * 100);
		if (myrand < rand) // 自摸

			pai = getpai_skill(p); // 技能好牌
		
		if(pai == -1)
		{
			Log.log("岭上开花: 无技能好牌: myrand" + myrand +"   rand: "+ rand);
		}
		
		if (pai == -1)
		{
			Log.log("岭上开花: 技能好牌:" +p.getSkillWishPais().toString());
			Log.log("岭上开花: 叫牌:" +p.getJiao().toString());
			pai = getPai_zimo(p);
		}
		if (pai != -1) {
			// p.setTmpYun(p.getTmpYun() +1); //岭上开花， 临时运+1
			useSkillMsg(p, ID_LINGSHANGKAIHUA);
		}
		if(pai == -1)
		{
			Log.log("岭上开花: 无叫牌: myrand");
		}
		
		return pai;
	}

	public void checkIslingShangKaiHua(MgsPlayer p, int pai) {
		Map<Integer, Integer> map = p.getSkillMap();
		int value = 0;
		if (map.containsKey(3))
			value = map.get(3);
		if (value == 0)
			return;
		// 检查技能是否开启
		if (!p.isGangshanghua())
			return;
		// 检查牌是否在技能还里面
		boolean contain = p.getSkillWishPais().contains(pai);
		if (!contain)
			contain = p.getJiao().contains(pai);
		if (contain) {
			//p.setTmpYun(p.getTmpYun() + 1); // 岭上开花， 临时运+1
			useSkillMsg(p, ID_LINGSHANGKAIHUA);
		}
	}

	/**
	 * 飞燕还巢 主动 打出的牌被其他人吃、碰、杠、和时，运增加1 消耗气1
	 */
	public boolean feiYanHuanChao(MgsPlayer p) {
		Log.log("feiYanHuanChao 开始");
		Map<Integer, Integer> skills = p.getSkillMap();
		if (!skills.containsKey(ID_FEIYANHUANCHAO)
				|| skills.get(ID_FEIYANHUANCHAO) <= 0) // 技能是否开启
			return false;
		Log.log("feiYanHuanChao 生效");
		skills.put(ID_FEIYANHUANCHAO, 0); // 技能关闭
		Log.log("feiYanHuanChao 关闭");
		List<int[]> dapai = p.getDapai();
		if (dapai.get(dapai.size() - 1)[1] == 0)
			return false; // 牌是否被别人要了

		MJ_Skill skill = Global.skills.get("飞燕还巢");
		p.setTmpYun(p.getTmpYun() + skill.getAttr0()); // 被人要了， 临时运+2
		this.skillEffected(p, ID_FEIYANHUANCHAO);
		brodcastModYun(p, skill.getAttr0(), true);
		return true;
	}

	/**
	 * 不动如山 主动 打出的牌没有被其他人吃、碰、杠、和时，运增加1 消耗气1
	 */
	public boolean zhuShuiPanShi(MgsPlayer p) {
		Log.log("---->不动如山  开始");
		Map<Integer, Integer> skills = p.getSkillMap();
		if (!skills.containsKey(ID_BUDONGRUSHAN)
				|| skills.get(ID_BUDONGRUSHAN) <= 0) // 技能是否开启
		{
			Log.log("---->不动如山  技能未开启");
			return false;
		}
		skills.put(ID_BUDONGRUSHAN, 0); // 技能关闭
		List<int[]> dapai = p.getDapai();
		if (dapai.get(dapai.size() - 1)[1] != 0) {
			Log.log("---->不动如山  失败状态:" + dapai.get(dapai.size() - 1)[1]);
			return false; // 牌被人要了就返回false
		}
		p.setTmpYun(p.getTmpYun() + 1);
		this.skillEffected(p, ID_BUDONGRUSHAN);
		Log.log("---->不动如山  完毕");
		return true;
	}

	/**
	 * 皇权 主动 听牌后，3圈内必定自摸！
	 */
	public int huangQuan(MgsPlayer p) {
		//System.out.println("huangquan:start");
		int pai = -1;
		if (!p.isTing())
			return pai; // 听牌
		Map<Integer, Integer> skills = p.getSkillMap();
		if (!skills.containsKey(ID_HUANGQUAN) || skills.get(ID_HUANGQUAN) <= 0) // 技能是否开启
			return pai;
		int quan = p.getQuan_hq();
		quan--;
		int rand = 50;
		if (quan <= 0)
			rand = 100;
		
		int myr = (int) (Math.random() * 100);
		System.out.println("皇权： quan :" +quan +" rand: " + myr  +" : " + rand);
		if (myr <= rand) // 触发技能
		{
			pai = this.getPai_zimo(p);
		}
		if(quan <= 0 && pai  <= 0)
		{
//			CCMD11111 cmd111 = new CCMD11111();
//			cmd111.auto_deal(p, "没有叫牌："+ p.getJiao());
		}
		//System.out.println("huangquan-------------> 叫："+ p.getJiao());
		
		if (skills.get(ID_HUANGQUAN) <= 0 || pai != -1) {
			skills.put(ID_HUANGQUAN, 0);
			p.setQuan_hq(0);
		}
		if (pai != -1) {
			skillEffected(p, ID_HUANGQUAN);
			p.setQuan_hq(0);
		} else {
			p.setQuan_hq(quan);
		}
		//System.out.println("huangquan-------------> 1 quan："+ p.getQuan_hq());
		//System.out.println("huangquan-------------> 2  skill:"+ skills.get(ID_HUANGQUAN));
		return pai;
	}

	public void checkIsHuangquan(MgsPlayer p, int pai) {
		Map<Integer, Integer> skills = p.getSkillMap();
		if (!skills.containsKey(ID_HUANGQUAN) || skills.get(ID_HUANGQUAN) <= 0) // 技能是否开启
			return;
		// 检查技能是否开启
		if (!p.isTing())
			return;
		// 检查牌是否在技能还里面
		boolean contain = p.getSkillWishPais().contains(pai);
		if (!contain)
			contain = p.getJiao().contains(pai);
		if (contain) {
//			System.out.println("checkIsHuangquan------->");
			skills.put(ID_HUANGQUAN, 0); // 关闭技能
			skillEffected(p, ID_HUANGQUAN);
		}
	}

	/**
	 * 荒芜盛宴 被动 所有对手运-1
	 */
	public boolean huangwushengyan(MgsPlayer p) {
		
		Log.info("触发荒芜盛宴:"+ p.getName());
		Room r = p.getRoom();
		for (int i = 1; i <= r.getMAX_COUNT(); i++) {
			MgsPlayer tmpp = r.getPlayers().get(i);
			if (tmpp == null || tmpp == p)
				continue;
			int mod = 0;
			if(r.getPlayerLimit() == 2)
			{
				tmpp.setYun(tmpp.getYun() - 2);
				mod = 2;
			}
			else
			{
				mod = 1;
				tmpp.setYun(tmpp.getYun() - 1);
			}
		}
		p.getSkillMap().put(ID_HUANGWUSHENGYAN, 0);
		return true;
	}

	/**
	 * 确定可以自摸，获得自摸的牌
	 */
	public int getPai_zimo(MgsPlayer p) {
		int pai = -1;
		int rand = 0;
		Room r = p.getRoom();
		List<Integer> copyList = copyList(p.getJiao());
		while (copyList.size() > 0) {
			rand = (int) (Math.random() * copyList.size());
			pai = copyList.get(rand);
			if (r.IDList.contains((Integer) pai))
				return pai;
			copyList.remove((Integer) pai);
			pai = -1;
		}
		if(pai == -1)
		{
//			CCMD11111 cmd111 = new CCMD11111();
//			cmd111.auto_deal(p, "没有叫牌:" + p.getJiao().toString());
		}
		return pai;
	}

	public List<Integer> copyList(List<Integer> param) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < param.size(); i++) {
			list.add(param.get(i));
		}
		return list;

	}

	/**
	 * 获取技能需要的好牌
	 */
	public int getpai_skill(MgsPlayer p) {
		int pai = -1;
		Room r = p.getRoom();
		int rand = 0;
		List<Integer> list = this.copyList(p.getSkillWishPais());
		while (list.size() > 0) {
			rand = (int) (Math.random() * list.size());
			pai = list.get(rand);
			if (r.IDList.contains((Integer) pai))
				return pai;
			list.remove((Integer) pai);
			pai = -1;
		}
		return pai;
	}

	/**
	 * 技能触发点: 1 3 6. 摸牌 2. 当前玩家 4 5.改变打牌的状态 7. 初始状态
	 * 
	 * 指令 玩家ID 技能ID ,发动成功与否 ,
	 */
	/**
	 * 
	 * 初始技能检查
	 */
	public void checkInitSkill(MgsPlayer p) {
		Map<Integer, Integer> map = p.getSkillMap();
		Iterator<Integer> it = map.keySet().iterator();
		while (it.hasNext()) {
			int key = it.next();
			int value = map.get(key);
			if (value == 0)
				continue;
			boolean b = false;
			switch (key) {
			case 7:
				b = this.huangwushengyan(p);
				if (b)
					useSkillMsg(p, ID_HUANGWUSHENGYAN);
				break; // 荒芜盛宴
			}
		}
	}

	/**
	 * 摸牌相关技能检查
	 */
	public int checkMopaiSkill(MgsPlayer p) {
		Map<Integer, Integer> map = p.getSkillMap();
		Iterator<Integer> it = map.keySet().iterator();
		int pai = -1;
		while (it.hasNext()) {
			int key = it.next();
			int value = map.get(key);
			//System.out.println("checkMopaiSkill------->    key:"+key +"    value:" + value);
			if (value == 0)
				continue;
			pai = usemopaiSkill(p, key);
			if (pai > 0)
				return pai;
		}
		return pai;
	}

	/**
	 * 打牌后技能检查
	 */
	public void checkSkillAfterDapai(MgsPlayer preP,MgsPlayer oprateP,String operate) {
		Room r = preP.getRoom();
		MgsPlayer tmpp = preP;
		Map<Integer, Integer> map = tmpp.getSkillMap();
		Iterator<Integer> it = map.keySet().iterator();
		while (it.hasNext()) {
			int key = it.next();
			int value = map.get(key);
			Log.log("checkSkillAfterDapai:" + key + "   " + value);
			if (value == 0)
				continue;
			boolean b = useSkllAfterDapai(tmpp,oprateP, key,operate);
			if (b && (key == ID_BUDONGRUSHAN)) // 技能生效 广播运的改变
			{
				brodcastModYun(tmpp, 1, true);
			}
		}
	}

	/**
	 * 状态改变后技能使用检查
	 */
	public void checkSkill_statusChange(MgsPlayer p) {
		Map<Integer, Integer> map = p.getSkillMap();
		Iterator<Integer> it = map.keySet().iterator();
		while (it.hasNext()) {
			int key = it.next();
			int value = map.get(key);
			if (value == 0)
				continue;
			boolean b = useSkill_statusChange(p, key);
			if (b) {
				this.brodcastUseSkill(p, key);
			}
		}
	}

	/**
	 * 胡牌后触发技能 MgsPlayer 点炮的玩家
	 */
	public void checkSkill_end(MgsPlayer p) {
		Map<Integer, Integer> map = p.getSkillMap();
		Iterator<Integer> it = map.keySet().iterator();
		while (it.hasNext()) {
			int key = it.next();
			int value = map.get(key);
			if (value == 0)
				continue;
			Log.log("checkSkill_end-->");
			switch (key) {
			case PlayerSkill.ID_DILI:
				this.dili(p);
				break;
			}

		}
	}

	public boolean useSkill_statusChange(MgsPlayer p, int skillId) {
		boolean b = false;
		switch (skillId) {
		case 1:
			break;
		case 2:

			// b = this.dili(p);
			break;
		}
		return b;
	}

	public boolean useSkllAfterDapai(MgsPlayer p, MgsPlayer operateP,int skillId,String operate) {
		boolean b = false;
		switch (skillId) {
		case ID_FEIYANHUANCHAO:
			b = this.feiYanHuanChao(p);
			break;
		case ID_BUDONGRUSHAN:
			b = this.zhuShuiPanShi(p);
			break;
		case ID_FUSHI:
			fushi(p,operateP);
			break;
		case ID_YUZHEN:
			yuzhen(p,operateP);
			break;
		case ID_SIFENGSHI: 
			if(operate!= null && !operate.equals("吃"))this.四风使(p,operateP);
			break;
		case ID_SANYUANSHI: 
			if(operate!= null && !operate.equals("吃"))this.三元使(p,operateP);
			break;
		case ID_GUIMEN: 
			if(operate!= null && !operate.equals("吃"))鬼门(p,operateP);
			break;
		case ID_SHANGMEN: 
			if(operate!= null && !operate.equals("吃"))伤门(p,operateP);
			break;
		}
		return b;
	}
	
	/**
	 * 四风使 --- >每次凑齐“东”、“南”、“西”、“北”的任意刻牌，该轮运+1
	 */
	private boolean 四风使(MgsPlayer preP,MgsPlayer operateP)
	{
		
		List<int[]> dapai = preP.getDapai();
		int paiId = dapai.get(dapai.size() - 1)[0];
		if(paiId >= 31 && paiId <= 34)
		{
			CCMD11032 cmd032 = new CCMD11032();
			operateP.setTmpYun(operateP.getTmpYun() + 1);
			cmd032.auto_deal(operateP, 1, true);
			
			this.skillEffected(operateP, ID_SIFENGSHI);
			return true;
		}
		
		return false;
	}
	
	/**
	 * 三元使 --->每次凑齐“中”、“发”、“白”的刻牌，该轮运+2
	 */
	private boolean 三元使(MgsPlayer preP,MgsPlayer operateP)
	{
		
		List<int[]> dapai = preP.getDapai();
		int paiId = dapai.get(dapai.size() - 1)[0];
		if(paiId > 34)
		{
			CCMD11032 cmd032 = new CCMD11032();
			operateP.setTmpYun(operateP.getTmpYun() + 2);
			cmd032.auto_deal(operateP, 2, true);
			
			this.skillEffected(operateP, ID_SANYUANSHI);
			return true;
		}
		
		return false;
	}
	/**
	 * 鬼门 --- >每次同时凑齐“东”和“北”的刻牌，本轮运+5，全体运-2。
	 */
	private boolean 鬼门(MgsPlayer preP,MgsPlayer operateP)
	{
		List<int[]> dapai = preP.getDapai();
		int paiId = dapai.get(dapai.size() - 1)[0];
		if(paiId !=31&& paiId != 34)return false;
		boolean have31 = false;
		boolean have34 = false;
		int i = 0;
		for(i = 0; i < operateP.getPeng().size();i++)
		{
			int id = operateP.getPeng().get(i);
			if(id == 31)have31 = true;
			else if(id == 34)have34 = true;
		}
		for(i = 0; i < operateP.getGang().size();i++)
		{
			int id = operateP.getGang().get(0).get(0);
			if(id == 31)have31 = true;
			else if(id == 34)have34 = true;
		}
		if(have31 && have34)
		{

			CCMD11032 cmd032 = new CCMD11032();

			
			Room r = operateP.getRoom();
			for(i = 1; i <= 4;i++)
			{
				MgsPlayer tmp = r.players.get(i);
				if(tmp == null)continue;
				if(tmp == operateP)
				{
					tmp.setTmpYun(5);
					cmd032.auto_deal(tmp, 5, true);
				}else
				{
					tmp.setTmpYun(tmp.getTmpYun()-2);
					cmd032.auto_deal(tmp, -2, true);
				}
			}
			this.skillEffected(operateP, ID_GUIMEN);
			return true;
		}
		
		return false;
	}
	/**
	 * 伤门--- >每次同时凑齐“南”和“西”的刻牌，本轮运+2，全体运-5。
	 */
	private boolean 伤门(MgsPlayer preP,MgsPlayer operateP)
	{
		List<int[]> dapai = preP.getDapai();
		int paiId = dapai.get(dapai.size() - 1)[0];
		if(paiId !=32&& paiId != 33)return false;
		boolean have32 = false;
		boolean have33 = false;
		int i = 0;
		for(i = 0; i < operateP.getPeng().size();i++)
		{
			int id = operateP.getPeng().get(i);
			if(id == 32)have32 = true;
			else if(id == 33)have33 = true;
		}
		for(i = 0; i < operateP.getGang().size();i++)
		{
			int id = operateP.getGang().get(0).get(0);
			if(id == 32)have32 = true;
			else if(id == 33)have33 = true;
		}
		if(have32 && have33)
		{
			CCMD11032 cmd032 = new CCMD11032();
			Room r = operateP.getRoom();
			for(i = 1; i <= 4;i++)
			{
				MgsPlayer tmp = r.players.get(i);
				if(tmp == null)continue;
				if(tmp == operateP)
				{
					tmp.setTmpYun(5);
					cmd032.auto_deal(tmp, 5, true);
				}else
				{
					tmp.setTmpYun(tmp.getTmpYun()-2);
					cmd032.auto_deal(tmp, -2, true);
				}
			}
			this.skillEffected(operateP, ID_SHANGMEN);
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * 余震 --- >每次对别人“吃”“碰”“杠”时，使其“气”减少15  被动技能，一直有效
	 */
	private boolean yuzhen(MgsPlayer preP,MgsPlayer operateP)
	{
		Log.log("---->余震 开始");
		Map<Integer, Integer> skills = operateP.getSkillMap();
		if (!skills.containsKey(ID_YUZHEN)
				|| skills.get(ID_YUZHEN) <= 0) // 技能是否开启
		{
			Log.log("---->余震 技能未开启");
			return false;
		}
		List<int[]> dapai = preP.getDapai();
		if (dapai.get(dapai.size() - 1)[1] == 0) {
			Log.log("---->余震  失败状态:" + dapai.get(dapai.size() - 1)[1]);
			return false; // 牌没有被人要，返回false
		}
		//是吃碰杠的玩家气 -15
		preP.setQi(preP.getQi() - 15);
		this.skillEffected(operateP, ID_YUZHEN);
		Log.log("---->余震  完毕");
		return true;
	}
	/**
	 * 腐蚀  ---->打出的牌被其他人吃、碰、杠时，该人的“气”减少30
	 * @return
	 */
	private boolean fushi(MgsPlayer p,MgsPlayer operateP)
	{
		
		Log.log("---->腐蚀  开始");
		Map<Integer, Integer> skills = p.getSkillMap();
		if (!skills.containsKey(ID_FUSHI)
				|| skills.get(ID_FUSHI) <= 0) // 技能是否开启
		{
			Log.log("---->腐蚀  技能未开启");
			return false;
		}
		skills.put(ID_FUSHI, 0); // 技能关闭
		List<int[]> dapai = p.getDapai();
		if (dapai.get(dapai.size() - 1)[1] == 0) {
			Log.log("---->腐蚀  失败状态:" + dapai.get(dapai.size() - 1)[1]);
			return false; // 牌没有被人要，返回false
		}
		//是吃碰杠的玩家气 -30
		operateP.setQi(operateP.getQi() - 30);
		this.skillEffected(p, ID_FUSHI);
		Log.log("---->腐蚀  完毕");
		return true;
	}
	

	/**
	 * 广播使用技能
	 */
	public void brodcastUseSkill(MgsPlayer p, int skillId) {
		CCMD11033 ccmd = new CCMD11033();
		ccmd.auto_deal(p, skillId);
	}

	/**
	 * 广播运的改变
	 */
	public void brodcastModYun(MgsPlayer p, int modYun, boolean linshi) {
		CCMD11032 ccmd = new CCMD11032();
		ccmd.auto_deal(p, modYun, linshi);
	}

	/**
	 * 杠摸牌 :只岭上开花, 普通摸牌 : 检查强运 听 都检查
	 */
	public int usemopaiSkill(MgsPlayer p, int skillId) {
		int pai = -1;
		boolean b = false;
		switch (skillId) {
		case 1:
			if (!p.isGangshanghua())
				pai = qiangyun(p);
			return pai; // 使用技能指令
		case 3:
			if (p.isGangshanghua())
				pai = this.lingShangKaiHua(p);
			return pai;
		case 6:
			//System.out.println("------->usemopaiSkill: huangquan");
			pai = this.huangQuan(p);
			return pai;
		}
		return pai;
	}

	/**
	 * 开启技能 ,技能状态
	 * 
	 * @param p
	 * @param skillId
	 */
	public boolean useSkillById(MgsPlayer p, int skillId) {
		boolean useSuc = false;
		Map<Integer, Integer> skills = p.getSkillMap();
		if (!skills.containsKey(skillId)) // 是否有这个技能
			return false;
		CCMD11031 ccmd031 = new CCMD11031();

		MJ_Skill skill = null;
		int costQi = 0;
		switch (skillId) {
		case 1:
			break;
		case 2:
			break;
		case ID_LINGSHANGKAIHUA:
			// if(p.getQi() <2)
			// {
			// useSuc = false; break;
			// }
			// p.setQi(p.getQi() -2);
			// if(p.getYun() < p.getMaxYun())p.setYun(p.getYun() +1);
			// p.getSkillMap().put(ID_LINGSHANGKAIHUA,1);
			// useSkillMsg(p, skillId);
			// useSuc = true;break;
		case ID_FEIYANHUANCHAO:
			skill = Global.skills.get("飞燕还巢");
			costQi = skill.getCost0();
			if (p.getQi() < costQi) {
				useSuc = false;
				break;
			}
			p.setQi(p.getQi() - costQi);
			ccmd031.auto_deal(p, -costQi);
			p.getSkillMap().put(ID_FEIYANHUANCHAO, 1);
			useSkillMsg(p, skillId);
			break;
		case ID_BUDONGRUSHAN:
			skill = Global.skills.get("不动如山");
			costQi = skill.getCost0();
			if (p.getQi() < costQi) {
				useSuc = false;
				break;
			}
			p.setQi(p.getQi() - costQi);

			ccmd031.auto_deal(p, -costQi);
			Log.log("---->开启不动如山!");
			p.getSkillMap().put(ID_BUDONGRUSHAN, 1);
			Log.log("--->value:" + p.getSkillMap().get(ID_BUDONGRUSHAN));
			useSkillMsg(p, skillId);
			break;
		case ID_HUANGQUAN:
			skill = Global.skills.get("皇权");
			costQi = skill.getCost0();
			if (p.getQi() < costQi) {
				useSuc = false;
				break;
			}
			p.setQi(p.getQi() - costQi);
			ccmd031.auto_deal(p, -costQi);
			p.getSkillMap().put(ID_HUANGQUAN, 1);
			useSkillMsg(p, skillId);
			useSuc = true;
			break;
		case ID_ZHISHUI:  //全体对手“气”、“运”归零！
			
			skill = Global.skills.get("止水");
			costQi = skill.getCost0();
			if (p.getQi() < costQi) {
				useSuc = false;
				break;
			}
			p.setQi(p.getQi() - costQi);
			ccmd031.auto_deal(p, -costQi);
			p.getSkillMap().put(ID_ZHISHUI, 1);
			useSkillMsg(p, skillId);
			useSuc = true;
			useSkill_止水(p);
			break;
			
		case ID_FUSHI:
			skill = Global.skills.get("腐蚀");
			costQi = skill.getCost0();
			if (p.getQi() < costQi) {
				useSuc = false;
				break;
			}
			p.setQi(p.getQi() - costQi);
			ccmd031.auto_deal(p, -costQi);
			p.getSkillMap().put(ID_FUSHI, 1);
			useSkillMsg(p, skillId);
			break;
//		case ID_YUZHEN: //被动技能
//			skill = Global.skills.get("余震");
//			costQi = skill.getCost0();
//			if (p.getQi() < costQi) {
//				useSuc = false;
//				break;
//			}
//			p.setQi(p.getQi() - costQi);
//			ccmd031.auto_deal(p, -costQi);
//			p.getSkillMap().put(ID_YUZHEN, 1);
//			useSkillMsg(p, skillId);
//			break;
		}
		return useSuc;
	}
	
	
	/**
	 * 召唤大海之主，使一切风浪蛰伏！全体对手“气”、“运”归零！,自己如果云为负，归零
	 * @param p
	 */
	public void useSkill_止水(MgsPlayer p)
	{
		
		CCMD11032 cmd032 = new CCMD11032();
		CCMD11031 cmd031 = new CCMD11031();
		Room r = p.getRoom();
		
		for(int i = 1 ;i <= 4; i++)
		{
			MgsPlayer tmp = r.players.get(i);
			if(tmp == null)continue;
			if (tmp == p )
			{
				if(tmp.getYun() < 0)
				{
					tmp.setTmpYun(0);
					cmd032.auto_deal(tmp,-tmp.getYun(),false);
				}
			}else{
				
				cmd032.auto_deal(tmp, -tmp.getYun(), false);
				if(tmp.getTmpYun() != 0)
				{
					tmp.setTmpYun(0);
					cmd032.auto_deal(tmp, -tmp.getTmpYun(), true);
				}
				if(tmp.getQi() > 0)
				{
					tmp.setQi(0);
					cmd031.auto_deal(tmp, -tmp.getQi());
				}
			}
				
		}
	}
}

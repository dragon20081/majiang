package business.junitTest;

import server.mj.MgsPlayer;

public class CCMD11011 extends CMD{


	private MgsPlayer player;
	
	public MgsPlayer getPlayer() {
		System.out.println(11011);
		return player;
	}

	public void setPlayer(MgsPlayer player) {
		this.player = player;
	}
	
}

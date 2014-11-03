package business.junitTest;

import server.mj.MgsPlayer;

public abstract class CMD {

	private MgsPlayer player = null;

	public MgsPlayer getPlayer() {
		System.out.println("cmd");
		return player;
	}

	public   void setPlayer(MgsPlayer player) 
	{
		this.player  = player;
	}
	
}
